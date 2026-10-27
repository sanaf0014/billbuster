package com.example.model

enum class ReceiptStatus(val displayName: String) {
    VERIFIED("Verified"),
    FLAGGED("Flagged")
}

enum class ReceiptCategory(val displayName: String) {
    GROCERIES("Groceries"),
    GAS("Gas"),
    DINING("Dining"),
    OFFICE("Office"),
    OTHER("Other")
}

data class ReceiptItem(
    val id: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val lineTotal: Double = quantity * unitPrice,
    val isFlagged: Boolean = false,
    val expectedPrice: Double? = null,
    val flagReason: String? = null
)

data class Receipt(
    val id: String,
    val merchant: String,
    val date: String,
    val time: String,
    val printedTotal: Double,
    val category: ReceiptCategory,
    val status: ReceiptStatus,
    val items: List<ReceiptItem>,
    val notes: String = "",
    val isDemoReceipt: Boolean = false
) {
    val calculatedTotal: Double
        get() = items.sumOf { it.quantity * it.unitPrice }

    val mismatch: Double
        get() = kotlin.math.abs(calculatedTotal - printedTotal)

    val hasMismatch: Boolean
        get() = mismatch > 0.01
}

enum class AuditScanStage(val label: String, val detail: String) {
    IMAGE_RECEIVED("Image Received", "Preprocessing, deskewing & contrast check"),
    TEXT_EXTRACTION("Text Extraction", "Pytesseract OCR reading lines & prices"),
    DATA_VALIDATION("Data Validation", "Regex entity parsing & math rule checksums"),
    AUDIT_COMPLETE("Audit Complete", "Receipt verification completed")
}

data class AuditActivity(
    val id: String,
    val timestamp: String,
    val merchant: String,
    val description: String,
    val status: ReceiptStatus
)

enum class ReceiptFilter {
    ALL,
    FLAGGED,
    VERIFIED
}
