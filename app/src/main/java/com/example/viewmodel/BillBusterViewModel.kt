package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDataProvider
import com.example.model.AuditActivity
import com.example.model.AuditScanStage
import com.example.model.Receipt
import com.example.model.ReceiptCategory
import com.example.model.ReceiptFilter
import com.example.model.ReceiptItem
import com.example.model.ReceiptStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ScanUiState(
    val isScanning: Boolean = false,
    val currentStage: AuditScanStage = AuditScanStage.IMAGE_RECEIVED,
    val progress: Float = 0f,
    val isComplete: Boolean = false,
    val scannedReceipt: Receipt? = null,
    val selectedImagePreview: String? = null
)

data class EditItemDialogState(
    val isOpen: Boolean = false,
    val receiptId: String = "",
    val item: ReceiptItem? = null
)

class BillBusterViewModel : ViewModel() {

    private val _receipts = MutableStateFlow<List<Receipt>>(MockDataProvider.getInitialReceipts())
    val receipts: StateFlow<List<Receipt>> = _receipts.asStateFlow()

    private val _auditActivities = MutableStateFlow<List<AuditActivity>>(MockDataProvider.getInitialAuditActivities())
    val auditActivities: StateFlow<List<AuditActivity>> = _auditActivities.asStateFlow()

    private val _selectedReceipt = MutableStateFlow<Receipt?>(null)
    val selectedReceipt: StateFlow<Receipt?> = _selectedReceipt.asStateFlow()

    private val _activeFilter = MutableStateFlow(ReceiptFilter.ALL)
    val activeFilter: StateFlow<ReceiptFilter> = _activeFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedMonth = MutableStateFlow("September 2026")
    val selectedMonth: StateFlow<String> = _selectedMonth.asStateFlow()

    private val _currencySymbol = MutableStateFlow("$")
    val currencySymbol: StateFlow<String> = _currencySymbol.asStateFlow()

    private val _scanState = MutableStateFlow(ScanUiState())
    val scanState: StateFlow<ScanUiState> = _scanState.asStateFlow()

    private val _editDialogState = MutableStateFlow(EditItemDialogState())
    val editDialogState: StateFlow<EditItemDialogState> = _editDialogState.asStateFlow()

    private val _statusNotification = MutableStateFlow<String?>(null)
    val statusNotification: StateFlow<String?> = _statusNotification.asStateFlow()

    // Filtered receipts based on search and status filter
    val filteredReceipts: StateFlow<List<Receipt>> = combine(
        _receipts,
        _searchQuery,
        _activeFilter
    ) { allReceipts, query, filter ->
        allReceipts.filter { receipt ->
            val matchesSearch = query.isBlank() ||
                    receipt.merchant.contains(query, ignoreCase = true) ||
                    receipt.items.any { it.name.contains(query, ignoreCase = true) }

            val matchesFilter = when (filter) {
                ReceiptFilter.ALL -> true
                ReceiptFilter.FLAGGED -> receipt.status == ReceiptStatus.FLAGGED
                ReceiptFilter.VERIFIED -> receipt.status == ReceiptStatus.VERIFIED
            }

            matchesSearch && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Metrics for the Overview / Insights
    val totalSpent: StateFlow<Double> = _receipts.combine(_selectedMonth) { list, _ ->
        list.sumOf { it.printedTotal }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1284.52)

    val flaggedCount: StateFlow<Int> = _receipts.combine(_selectedMonth) { list, _ ->
        list.count { it.status == ReceiptStatus.FLAGGED }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 3)

    val verifiedCount: StateFlow<Int> = _receipts.combine(_selectedMonth) { list, _ ->
        list.count { it.status == ReceiptStatus.VERIFIED }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 21)

    init {
        // Default select Trader Joe's for immediate worked example demo capability
        _selectedReceipt.value = _receipts.value.firstOrNull { it.isDemoReceipt }
    }

    fun selectReceipt(receipt: Receipt) {
        _selectedReceipt.value = receipt
    }

    fun selectReceiptById(id: String) {
        _selectedReceipt.value = _receipts.value.firstOrNull { it.id == id }
    }

    fun clearSelectedReceipt() {
        _selectedReceipt.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: ReceiptFilter) {
        _activeFilter.value = filter
    }

    fun setSelectedMonth(month: String) {
        _selectedMonth.value = month
    }

    fun setCurrency(symbol: String) {
        _currencySymbol.value = symbol
    }

    fun dismissNotification() {
        _statusNotification.value = null
    }

    fun openEditDialog(receiptId: String, item: ReceiptItem) {
        _editDialogState.value = EditItemDialogState(
            isOpen = true,
            receiptId = receiptId,
            item = item
        )
    }

    fun dismissEditDialog() {
        _editDialogState.value = EditItemDialogState(isOpen = false)
    }

    fun saveLineItemChanges(
        receiptId: String,
        itemId: String,
        newName: String,
        newQuantity: Int,
        newUnitPrice: Double
    ) {
        val updatedList = _receipts.value.map { receipt ->
            if (receipt.id == receiptId) {
                val updatedItems = receipt.items.map { item ->
                    if (item.id == itemId) {
                        item.copy(
                            name = newName,
                            quantity = newQuantity,
                            unitPrice = newUnitPrice,
                            lineTotal = newQuantity * newUnitPrice,
                            isFlagged = false,
                            flagReason = null
                        )
                    } else item
                }

                val calculatedTotal = updatedItems.sumOf { it.quantity * it.unitPrice }
                val hasDiscrepancy = kotlin.math.abs(calculatedTotal - receipt.printedTotal) > 0.01

                receipt.copy(
                    items = updatedItems,
                    status = if (hasDiscrepancy) ReceiptStatus.FLAGGED else ReceiptStatus.VERIFIED,
                    notes = if (hasDiscrepancy)
                        "Remaining discrepancy of ${formatCurrency(kotlin.math.abs(calculatedTotal - receipt.printedTotal))}"
                    else
                        "Audit Complete: Corrected item matches printed total of ${formatCurrency(receipt.printedTotal)}."
                )
            } else receipt
        }

        _receipts.value = updatedList
        _selectedReceipt.value = updatedList.firstOrNull { it.id == receiptId }
        dismissEditDialog()

        // Add audit activity record
        val activity = AuditActivity(
            id = "ACT-${System.currentTimeMillis()}",
            timestamp = "Just now",
            merchant = _selectedReceipt.value?.merchant ?: "Receipt",
            description = "Line item corrected ($newName to ${formatCurrency(newUnitPrice)}). Receipt marked as Verified.",
            status = ReceiptStatus.VERIFIED
        )
        _auditActivities.value = listOf(activity) + _auditActivities.value
        _statusNotification.value = "Discrepancy resolved! Receipt marked as Verified."
    }

    fun applyExpectedPrice(receiptId: String, itemId: String) {
        val targetReceipt = _receipts.value.firstOrNull { it.id == receiptId } ?: return
        val targetItem = targetReceipt.items.firstOrNull { it.id == itemId } ?: return
        val expected = targetItem.expectedPrice ?: return

        saveLineItemChanges(
            receiptId = receiptId,
            itemId = itemId,
            newName = targetItem.name,
            newQuantity = targetItem.quantity,
            newUnitPrice = expected
        )
    }

    fun markAsReviewed(receiptId: String) {
        val updatedList = _receipts.value.map { receipt ->
            if (receipt.id == receiptId) {
                receipt.copy(
                    status = ReceiptStatus.VERIFIED,
                    notes = "Manually reviewed and approved by user."
                )
            } else receipt
        }
        _receipts.value = updatedList
        _selectedReceipt.value = updatedList.firstOrNull { it.id == receiptId }

        val activity = AuditActivity(
            id = "ACT-${System.currentTimeMillis()}",
            timestamp = "Just now",
            merchant = _selectedReceipt.value?.merchant ?: "Receipt",
            description = "Manually marked as reviewed and verified.",
            status = ReceiptStatus.VERIFIED
        )
        _auditActivities.value = listOf(activity) + _auditActivities.value
        _statusNotification.value = "Receipt marked as reviewed."
    }

    fun startScanningSimulation(imageName: String? = null, onFinished: (Receipt) -> Unit) {
        val demoReceipt = _receipts.value.firstOrNull { it.isDemoReceipt }
            ?: _receipts.value.first()

        viewModelScope.launch {
            _scanState.value = ScanUiState(
                isScanning = true,
                currentStage = AuditScanStage.IMAGE_RECEIVED,
                progress = 0.2f,
                selectedImagePreview = imageName ?: "Trader Joe's Receipt Sample"
            )
            delay(700)

            _scanState.value = _scanState.value.copy(
                currentStage = AuditScanStage.TEXT_EXTRACTION,
                progress = 0.5f
            )
            delay(800)

            _scanState.value = _scanState.value.copy(
                currentStage = AuditScanStage.DATA_VALIDATION,
                progress = 0.85f
            )
            delay(700)

            _scanState.value = _scanState.value.copy(
                currentStage = AuditScanStage.AUDIT_COMPLETE,
                progress = 1.0f,
                isComplete = true,
                scannedReceipt = demoReceipt
            )
            delay(400)

            _selectedReceipt.value = demoReceipt
            _scanState.value = _scanState.value.copy(isScanning = false)
            onFinished(demoReceipt)
        }
    }

    fun resetDemoData() {
        _receipts.value = MockDataProvider.getInitialReceipts()
        _auditActivities.value = MockDataProvider.getInitialAuditActivities()
        _selectedReceipt.value = _receipts.value.firstOrNull { it.isDemoReceipt }
        _searchQuery.value = ""
        _activeFilter.value = ReceiptFilter.ALL
        _statusNotification.value = "Demo datasets have been reset."
    }

    fun formatCurrency(amount: Double): String {
        return String.format("%s%.2f", _currencySymbol.value, amount)
    }
}
