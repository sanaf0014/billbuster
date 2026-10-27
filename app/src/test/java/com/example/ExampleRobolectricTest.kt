package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MockDataProvider
import com.example.model.ReceiptStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("BillBuster", appName)
  }

  @Test
  fun `trader joes demo receipt has initial 10 dollar mismatch`() {
    val receipts = MockDataProvider.getInitialReceipts()
    val traderJoes = receipts.first { it.isDemoReceipt }

    assertEquals(23.76, traderJoes.printedTotal, 0.001)
    assertEquals(33.76, traderJoes.calculatedTotal, 0.001)
    assertEquals(10.00, traderJoes.mismatch, 0.001)
    assertEquals(ReceiptStatus.FLAGGED, traderJoes.status)
    assertTrue(traderJoes.hasMismatch)
  }

  @Test
  fun `correcting sourdough bread to 4_99 resolves discrepancy`() {
    val receipts = MockDataProvider.getInitialReceipts()
    val traderJoes = receipts.first { it.isDemoReceipt }

    val correctedItems = traderJoes.items.map { item ->
      if (item.name == "Sourdough Bread") {
        item.copy(unitPrice = 4.99, lineTotal = 4.99, isFlagged = false)
      } else item
    }

    val correctedReceipt = traderJoes.copy(
      items = correctedItems,
      status = ReceiptStatus.VERIFIED
    )

    assertEquals(23.76, correctedReceipt.calculatedTotal, 0.001)
    assertEquals(0.00, correctedReceipt.mismatch, 0.001)
    assertFalse(correctedReceipt.hasMismatch)
    assertEquals(ReceiptStatus.VERIFIED, correctedReceipt.status)
  }
}
