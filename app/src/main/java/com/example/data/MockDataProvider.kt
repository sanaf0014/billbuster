package com.example.data

import com.example.model.AuditActivity
import com.example.model.Receipt
import com.example.model.ReceiptCategory
import com.example.model.ReceiptItem
import com.example.model.ReceiptStatus

object MockDataProvider {

    fun getInitialReceipts(): List<Receipt> {
        return listOf(
            Receipt(
                id = "REC-2026-0901",
                merchant = "Trader Joe's",
                date = "Sep 18, 2026",
                time = "4:12 PM",
                printedTotal = 23.76,
                category = ReceiptCategory.GROCERIES,
                status = ReceiptStatus.FLAGGED,
                isDemoReceipt = true,
                notes = "Sum of items ($33.76) differs from receipt total ($23.76) by $10.00.",
                items = listOf(
                    ReceiptItem(
                        id = "ITEM-TJ-1",
                        name = "Organic Bananas",
                        quantity = 1,
                        unitPrice = 2.49
                    ),
                    ReceiptItem(
                        id = "ITEM-TJ-2",
                        name = "Almond Milk",
                        quantity = 2,
                        unitPrice = 3.99
                    ),
                    ReceiptItem(
                        id = "ITEM-TJ-3",
                        name = "Sourdough Bread",
                        quantity = 1,
                        unitPrice = 14.99,
                        isFlagged = true,
                        expectedPrice = 4.99,
                        flagReason = "Price mismatch: Detected $14.99 vs Expected $4.99 ($10.00 difference)"
                    ),
                    ReceiptItem(
                        id = "ITEM-TJ-4",
                        name = "Sea Salt",
                        quantity = 1,
                        unitPrice = 3.30
                    ),
                    ReceiptItem(
                        id = "ITEM-TJ-5",
                        name = "Greek Yogurt (Tub)",
                        quantity = 1,
                        unitPrice = 5.00
                    )
                )
            ),
            Receipt(
                id = "REC-2026-0902",
                merchant = "CVS Pharmacy",
                date = "Sep 17, 2026",
                time = "2:30 PM",
                printedTotal = 18.32,
                category = ReceiptCategory.OTHER,
                status = ReceiptStatus.FLAGGED,
                notes = "Pricing discrepancy detected on Energy Drink line item.",
                items = listOf(
                    ReceiptItem(
                        id = "ITEM-CVS-1",
                        name = "Toothpaste Twin Pack",
                        quantity = 1,
                        unitPrice = 6.99
                    ),
                    ReceiptItem(
                        id = "ITEM-CVS-2",
                        name = "Pain Reliever Tablets",
                        quantity = 1,
                        unitPrice = 5.34
                    ),
                    ReceiptItem(
                        id = "ITEM-CVS-3",
                        name = "Energy Drink",
                        quantity = 1,
                        unitPrice = 6.00,
                        isFlagged = true,
                        expectedPrice = 2.99,
                        flagReason = "Unusual price anomaly: $3.01 above standard catalog price"
                    )
                )
            ),
            Receipt(
                id = "REC-2026-0903",
                merchant = "Shell Gas",
                date = "Sep 20, 2026",
                time = "8:45 AM",
                printedTotal = 41.10,
                category = ReceiptCategory.GAS,
                status = ReceiptStatus.VERIFIED,
                notes = "Calculated checksum matches receipt pump meter.",
                items = listOf(
                    ReceiptItem(
                        id = "ITEM-SH-1",
                        name = "Regular Unleaded Fuel (11.2 Gal)",
                        quantity = 1,
                        unitPrice = 41.10
                    )
                )
            ),
            Receipt(
                id = "REC-2026-0904",
                merchant = "Office Depot",
                date = "Sep 15, 2026",
                time = "11:20 AM",
                printedTotal = 67.44,
                category = ReceiptCategory.OFFICE,
                status = ReceiptStatus.VERIFIED,
                notes = "All 4 stationery items validated against printed subtotal.",
                items = listOf(
                    ReceiptItem(
                        id = "ITEM-OD-1",
                        name = "Multipurpose Copy Paper (Ream)",
                        quantity = 2,
                        unitPrice = 18.99
                    ),
                    ReceiptItem(
                        id = "ITEM-OD-2",
                        name = "Gel Ink Pens (12-Pack)",
                        quantity = 1,
                        unitPrice = 14.48
                    ),
                    ReceiptItem(
                        id = "ITEM-OD-3",
                        name = "Heavy Duty Filing Folders",
                        quantity = 1,
                        unitPrice = 14.98
                    )
                )
            ),
            Receipt(
                id = "REC-2026-0905",
                merchant = "Starbucks",
                date = "Sep 14, 2026",
                time = "9:15 AM",
                printedTotal = 6.85,
                category = ReceiptCategory.DINING,
                status = ReceiptStatus.VERIFIED,
                notes = "Beverage and modifier checksum confirmed.",
                items = listOf(
                    ReceiptItem(
                        id = "ITEM-SB-1",
                        name = "Iced Caramel Macchiato",
                        quantity = 1,
                        unitPrice = 5.95
                    ),
                    ReceiptItem(
                        id = "ITEM-SB-2",
                        name = "Extra Espresso Shot",
                        quantity = 1,
                        unitPrice = 0.90
                    )
                )
            ),
            Receipt(
                id = "REC-2026-0906",
                merchant = "Whole Foods Market",
                date = "Sep 22, 2026",
                time = "5:40 PM",
                printedTotal = 93.05,
                category = ReceiptCategory.GROCERIES,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-WF-1", "Organic Olive Oil 1L", 1, 18.50),
                    ReceiptItem("ITEM-WF-2", "Wild Alaskan Salmon Fillet", 1, 24.00),
                    ReceiptItem("ITEM-WF-3", "Organic Baby Spinach", 2, 4.50),
                    ReceiptItem("ITEM-WF-4", "Artisanal Cheese Selection", 1, 23.55),
                    ReceiptItem("ITEM-WF-5", "Sparkling Spring Water (12pk)", 1, 13.00),
                    ReceiptItem("ITEM-WF-6", "Fresh Herbs Bundle", 1, 5.00)
                )
            ),
            Receipt(
                id = "REC-2026-0907",
                merchant = "Costco Wholesale",
                date = "Sep 11, 2026",
                time = "1:15 PM",
                printedTotal = 184.20,
                category = ReceiptCategory.GROCERIES,
                status = ReceiptStatus.FLAGGED,
                notes = "Duplicate bulk item detected in barcode line scanning.",
                items = listOf(
                    ReceiptItem("ITEM-CS-1", "Paper Towels (12 Rolls)", 2, 26.50),
                    ReceiptItem("ITEM-CS-2", "Greek Yogurt Bulk Pack", 1, 14.20),
                    ReceiptItem(
                        "ITEM-CS-3",
                        "Mixed Roasted Nuts 2.5lb",
                        2,
                        28.50,
                        isFlagged = true,
                        expectedPrice = 18.50,
                        flagReason = "Discrepancy: Scanned as premium macadamia instead of mixed nuts"
                    ),
                    ReceiptItem("ITEM-CS-4", "Laundry Detergent Pods", 1, 24.50),
                    ReceiptItem("ITEM-CS-5", "Organic Eggs 24ct", 2, 8.25)
                )
            ),
            Receipt(
                id = "REC-2026-0908",
                merchant = "Chipotle Mexican Grill",
                date = "Sep 19, 2026",
                time = "12:50 PM",
                printedTotal = 24.80,
                category = ReceiptCategory.DINING,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-CH-1", "Chicken Burrito Bowl", 1, 12.40),
                    ReceiptItem("ITEM-CH-2", "Steak Tacos (3)", 1, 12.40)
                )
            ),
            Receipt(
                id = "REC-2026-0909",
                merchant = "Chevron Fuel",
                date = "Sep 08, 2026",
                time = "7:30 AM",
                printedTotal = 54.30,
                category = ReceiptCategory.GAS,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-CHV-1", "Diesel Fuel (14.1 Gal)", 1, 54.30)
                )
            ),
            Receipt(
                id = "REC-2026-0910",
                merchant = "Best Buy",
                date = "Sep 05, 2026",
                time = "3:25 PM",
                printedTotal = 89.99,
                category = ReceiptCategory.OFFICE,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-BB-1", "Wireless Ergonomic Mouse", 1, 49.99),
                    ReceiptItem("ITEM-BB-2", "USB-C Hub Multiport Adapter", 1, 40.00)
                )
            ),
            Receipt(
                id = "REC-2026-0911",
                merchant = "Safeway Supermarket",
                date = "Sep 03, 2026",
                time = "6:10 PM",
                printedTotal = 78.40,
                category = ReceiptCategory.GROCERIES,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-SF-1", "Organic Milk Gallon", 2, 4.90),
                    ReceiptItem("ITEM-SF-2", "Whole Grain Bread", 2, 3.80),
                    ReceiptItem("ITEM-SF-3", "Chicken Breast Fillet 3lb", 1, 18.00),
                    ReceiptItem("ITEM-SF-4", "Fresh Produce & Berries", 1, 43.00)
                )
            ),
            Receipt(
                id = "REC-2026-0912",
                merchant = "Panera Bread",
                date = "Sep 07, 2026",
                time = "1:05 PM",
                printedTotal = 32.60,
                category = ReceiptCategory.DINING,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-PB-1", "Fuji Apple Salad with Chicken", 1, 14.50),
                    ReceiptItem("ITEM-PB-2", "Broccoli Cheddar Soup Bowl", 1, 11.20),
                    ReceiptItem("ITEM-PB-3", "Lemon Drop Cookie", 2, 3.45)
                )
            ),
            Receipt(
                id = "REC-2026-0913",
                merchant = "Exxon Mobil",
                date = "Sep 24, 2026",
                time = "6:20 PM",
                printedTotal = 48.90,
                category = ReceiptCategory.GAS,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-EX-1", "Premium Unleaded (12.5 Gal)", 1, 48.90)
                )
            ),
            Receipt(
                id = "REC-2026-0914",
                merchant = "Amazon Fresh",
                date = "Sep 16, 2026",
                time = "10:15 AM",
                printedTotal = 62.15,
                category = ReceiptCategory.GROCERIES,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-AF-1", "Ground Turkey 93/7 2lb", 2, 8.50),
                    ReceiptItem("ITEM-AF-2", "Organic Avocados 4pk", 1, 6.95),
                    ReceiptItem("ITEM-AF-3", "Pantry Staples & Grains", 1, 38.20)
                )
            ),
            Receipt(
                id = "REC-2026-0915",
                merchant = "Staples Business",
                date = "Sep 21, 2026",
                time = "4:45 PM",
                printedTotal = 45.20,
                category = ReceiptCategory.OFFICE,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-STP-1", "Dry Erase Markers 8pk", 2, 9.60),
                    ReceiptItem("ITEM-STP-2", "Desk Organizer Wire Mesh", 1, 26.00)
                )
            ),
            Receipt(
                id = "REC-2026-0916",
                merchant = "Olive Garden",
                date = "Sep 25, 2026",
                time = "7:45 PM",
                printedTotal = 88.50,
                category = ReceiptCategory.DINING,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-OG-1", "Tour of Italy", 1, 24.50),
                    ReceiptItem("ITEM-OG-2", "Chicken Alfredo", 1, 22.00),
                    ReceiptItem("ITEM-OG-3", "Appetizer Sampler", 1, 16.00),
                    ReceiptItem("ITEM-OG-4", "Tiramisu Dessert & Drinks", 1, 26.00)
                )
            ),
            Receipt(
                id = "REC-2026-0917",
                merchant = "BP Fuel Station",
                date = "Sep 26, 2026",
                time = "8:10 AM",
                printedTotal = 52.40,
                category = ReceiptCategory.GAS,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-BP-1", "Regular Unleaded 13.8 Gal", 1, 52.40)
                )
            ),
            Receipt(
                id = "REC-2026-0918",
                merchant = "Target Groceries",
                date = "Sep 27, 2026",
                time = "3:30 PM",
                printedTotal = 64.30,
                category = ReceiptCategory.GROCERIES,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-TG-1", "Good & Gather Trail Mix", 2, 7.50),
                    ReceiptItem("ITEM-TG-2", "Cold Brew Coffee Concentrate", 2, 9.90),
                    ReceiptItem("ITEM-TG-3", "Household Paper Goods", 1, 29.50)
                )
            ),
            Receipt(
                id = "REC-2026-0919",
                merchant = "Shake Shack",
                date = "Sep 28, 2026",
                time = "6:30 PM",
                printedTotal = 31.20,
                category = ReceiptCategory.DINING,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-SS-1", "ShackBurger Double", 1, 11.20),
                    ReceiptItem("ITEM-SS-2", "SmokeShack Single", 1, 9.50),
                    ReceiptItem("ITEM-SS-3", "Cheese Fries & Shake", 1, 10.50)
                )
            ),
            Receipt(
                id = "REC-2026-0920",
                merchant = "FedEx Office Print",
                date = "Sep 12, 2026",
                time = "10:40 AM",
                printedTotal = 28.50,
                category = ReceiptCategory.OFFICE,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-FX-1", "Color Presentation Binding 50p", 1, 28.50)
                )
            ),
            Receipt(
                id = "REC-2026-0921",
                merchant = "Speedway Fuel",
                date = "Sep 02, 2026",
                time = "9:00 AM",
                printedTotal = 43.10,
                category = ReceiptCategory.GAS,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-SP-1", "Plus Gasoline 11.5 Gal", 1, 43.10)
                )
            ),
            Receipt(
                id = "REC-2026-0922",
                merchant = "Sweetgreen",
                date = "Sep 09, 2026",
                time = "12:15 PM",
                printedTotal = 36.40,
                category = ReceiptCategory.DINING,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-SW-1", "Harvest Bowl Organic", 2, 16.50),
                    ReceiptItem("ITEM-SW-2", "Hibiscus Iced Tea", 2, 1.70)
                )
            ),
            Receipt(
                id = "REC-2026-0923",
                merchant = "Kroger Market",
                date = "Sep 13, 2026",
                time = "5:10 PM",
                printedTotal = 48.70,
                category = ReceiptCategory.GROCERIES,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-KR-1", "Chicken Thighs Family Pack", 1, 14.50),
                    ReceiptItem("ITEM-KR-2", "Pasta & Marinara Sauces", 3, 4.20),
                    ReceiptItem("ITEM-KR-3", "Fresh Fruit & Vegetables", 1, 21.60)
                )
            ),
            Receipt(
                id = "REC-2026-0924",
                merchant = "Subway Fresh",
                date = "Sep 23, 2026",
                time = "1:40 PM",
                printedTotal = 20.36,
                category = ReceiptCategory.DINING,
                status = ReceiptStatus.VERIFIED,
                items = listOf(
                    ReceiptItem("ITEM-SUB-1", "Footlong Italian B.M.T.", 1, 12.36),
                    ReceiptItem("ITEM-SUB-2", "Fountain Drink & Chips Combo", 1, 8.00)
                )
            )
        )
    }

    fun getInitialAuditActivities(): List<AuditActivity> {
        return listOf(
            AuditActivity(
                id = "ACT-1",
                timestamp = "Today, 4:12 PM",
                merchant = "Trader Joe's",
                description = "Discrepancy flagged: Sourdough Bread ($14.99 vs expected $4.99)",
                status = ReceiptStatus.FLAGGED
            ),
            AuditActivity(
                id = "ACT-2",
                timestamp = "Yesterday, 2:30 PM",
                merchant = "CVS Pharmacy",
                description = "Discrepancy flagged: Price spike anomaly on Energy Drink",
                status = ReceiptStatus.FLAGGED
            ),
            AuditActivity(
                id = "ACT-3",
                timestamp = "Sep 20, 8:45 AM",
                merchant = "Shell Gas",
                description = "Checksum verified: Pump meter agrees with line items ($41.10)",
                status = ReceiptStatus.VERIFIED
            ),
            AuditActivity(
                id = "ACT-4",
                timestamp = "Sep 15, 11:20 AM",
                merchant = "Office Depot",
                description = "Checksum verified: 3 items subtotal match printed ($67.44)",
                status = ReceiptStatus.VERIFIED
            ),
            AuditActivity(
                id = "ACT-5",
                timestamp = "Sep 14, 9:15 AM",
                merchant = "Starbucks",
                description = "Checksum verified: Beverage items match register ($6.85)",
                status = ReceiptStatus.VERIFIED
            )
        )
    }
}
