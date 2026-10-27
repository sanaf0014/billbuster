# BillBuster — AI-Powered Financial Guard (Presentation Prototype)

A high-fidelity, presentation-ready Android application prototype designed for Sana Fathima's Data Analytics & Data Science internship presentation, demonstrating automated receipt data extraction, discrepancy detection, manual audit correction, and spending analytics.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following user preferences have been confirmed and incorporated into the design:

- **Confirmed Layout**: Phone-optimized portrait layout with an intuitive Material 3 bottom navigation bar, providing quick access to Overview, Scan, History, Insights, and Settings.
- **Confirmed Scanning UX**: Animated multi-stage analysis with visual progress feedback across 4 audit stages: *Image Received* → *Text Extraction* → *Data Validation* → *Audit Complete*.
- **Confirmed Discrepancy Fix Flow**: Interactive manual line-item editor modal allowing customizable price and quantity adjustments with a one-tap "Apply Expected ($4.99)" helper that instantly recalculates totals and validates the bill.
- **Fintech Theme**: Premium dark charcoal aesthetic (`#121316` / `#1A1C23`) with vivid electric orange (`#FF6B00`) and warm amber (`#FFA726`) accents, emerald green (`#10B981`) for verified badges, and coral red (`#EF4444`) for flagged discrepancies.

---

## 1. Overview & Core Concept

- **What It Does**: BillBuster transforms paper or digital receipt images into structured, auditable expense records. It automatically checks itemized line items against receipt totals to uncover discrepancies, calculation errors, or OCR misreads, and aggregates verified expenses into category and weekly spending insights.
- **Target Audience / Persona**: College evaluators, internship mentors (SRM Institute of Science & Technology / Learn-X Academy Global), and everyday consumers or small business managers seeking automated expense auditing.
- **Key Value**: Replaces tedious 120-second manual receipt verification with a 15-second automated audit pipeline that highlights exact discrepancies and provides instant one-tap or manual correction.

---

## 2. User Experience & Visual Design

### Design Language & Color Palette
- **Background**: Deep charcoal dark surface (`#0F1115` base, `#181B20` card surfaces, `#22262E` elevated elements).
- **Primary Brand Accent**: Electric Orange (`#FF6B00`), Amber Gold (`#FFA726`).
- **Status Indicators**: Emerald Green (`#10B981`) for Verified, Coral Crimson (`#EF4444`) for Flagged/Mismatched, Cool Slate (`#94A3B8`) for Neutral/Secondary info.
- **Typography**: Clean, readable sans-serif hierarchy using Material 3 text styles with bold headline numbers and monospace styling for prices and currency.

### Primary Screen Flows

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       BILLBUSTER NAVIGATION FLOW                            │
└─────────────────────────────────────────────────────────────────────────────┘
          │
          ├── 1. Overview Dashboard ────► Key Metrics + Charts + Recent Audits
          │
          ├── 2. Scan Receipt ──────────► Upload/Camera + Animated 4-Stage Scan
          │                                      │
          │                                      ▼
          │                               3. Receipt Audit Detail
          │                                  (Flagged Trader Joe's)
          │                                      │
          │                                      ▼
          │                               [Fix Discrepancy Modal]
          │                                  (Edit $14.99 -> $4.99)
          │                                      │
          │                                      ▼
          │                               Status -> Verified!
          │
          ├── 4. Receipt History ───────► Search + Status Filters + Receipt Cards
          │
          ├── 5. Spending Insights ─────► Monthly Distribution & Weekly Trends
          │
          └── 6. Settings & Profile ────► Sana Fathima Info + Internship Context
```

1. **Overview Dashboard Screen**:
   - Header with student profile avatar ("SF") and "Demo Data" badge.
   - 4 Summary KPI Cards: Total Spent ($1,284.52), Receipts Processed (24), Flagged Receipts (3), Verified Receipts (21).
   - Interactive Weekly Spending Bar Chart (Week 1: $212, Week 2: $356, Week 3: $424, Week 4: $292).
   - Category Distribution Chart (Groceries 42%, Gas 21%, Dining 19%, Office 18%).
   - Recent Receipts List with one-tap navigation to the audit view.
   - Prominent Floating Action / Action Banner to "Scan New Receipt".

2. **Scan Receipt Screen**:
   - Viewfinder-style receipt framing box with a live "Lighting Good • Alignment OK" guidance badge.
   - Action buttons: "Browse Receipt Photo" (file picker) and "Use Demo Receipt (Trader Joe's)".
   - Image preview container displaying the loaded receipt image.
   - Animated Multi-Stage Scanner:
     1. *Image Received* (Preprocessing & Deskew)
     2. *Text Extraction* (Pytesseract OCR simulation)
     3. *Data Validation* (Regex Entity & Checksum verification)
     4. *Audit Complete* (Discrepancy flag raised)
   - Seamless transition to the Audit Detail screen upon completion.

3. **Receipt Audit Detail Screen**:
   - Merchant Header: Trader Joe's, September 18, 2026, Category: Groceries.
   - Prominent Discrepancy Alert Banner: "Math Mismatch: Sum of items ($33.76) exceeds printed total ($23.76) by $10.00".
   - Itemized Line Item List:
     - Organic Bananas (Qty 1) — $2.49 [OK]
     - Almond Milk (Qty 2) — $7.98 [OK]
     - **Sourdough Bread (Qty 1) — $14.99 [FLAGGED: Expected $4.99]**
     - Sea Salt (Qty 1) — $3.30 [OK]
   - Summary Comparison Card showing Printed Total vs. Calculated Sum with visual difference highlighted.
   - "Fix Flagged Item" button opening the Manual Line-Item Editor Modal.
   - Once corrected, the status updates dynamically to **Verified** with a green success banner and instant recalculation ($23.76 total).

4. **Receipt History Screen**:
   - Real-time search query field filtering by merchant name.
   - Filter Tabs: `All`, `Flagged (3)`, `Verified (21)`.
   - Month chip selector (September 2026, August 2026, July 2026).
   - Rich item cards showing merchant icon/badge, transaction date, line-item count, formatted amount, and status pill.
   - Tap any receipt to open its full audit breakdown.

5. **Spending Insights Screen**:
   - Month switcher dynamically updating charts.
   - Animated visual breakdown: Custom Canvas Donut Chart for categories and Bar Chart for weekly pace.
   - Analytical Internship Finding card: *"Groceries account for 42% of monthly spending. Automated checksum validation prevented a $10.00 billing anomaly."*
   - Audit performance metrics summary (Average audit time: 15s vs 120s manual).

6. **Settings & Student Profile Screen**:
   - Profile card for **Sana Fathima** (Reg: RA2431022020064, B.Sc AI & ML, SRM Institute of Science & Technology).
   - Project details: Learn-X Academy Global Internship II (UCS24P02L), Mentor: Dr. Lingeswari Sivaganam.
   - Currency Selector ($ USD, ₹ INR, € EUR) and Prototype Mode toggle.

---

## 3. Key Product Decisions & Trade-Offs

- **State Architecture**: A centralized `ReceiptViewModel` utilizing Kotlin `StateFlow` to manage all mock datasets, active filter selections, scanning animation states, and real-time receipt edits. All changes (e.g. correcting the Sourdough Bread price) immediately propagate across the Overview, History, and Insights screens.
- **Custom Native Compose Charts**: Instead of heavy external charting libraries with complex build dependencies, high-performance custom Jetpack Compose Canvas implementations are used for the Donut Chart and Weekly Bar Chart. This provides 60fps hardware-accelerated animations, pixel-perfect dark theme integration, and zero external dependency failure risk.
- **Fail-Safe Demo Experience**: The app is 100% self-contained and operates entirely offline with zero API keys or external server dependencies. Presenters can reset demo state at any time via a quick reset option in Settings or the Top Bar.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        ARCHITECTURAL BLUEPRINT                         │
└────────────────────────────────────────────────────────────────────────┘

 [MainActivity]
      │
      ▼
 [BillBusterApp] ── (Manages Scaffold, Navigation Bar, Screen Transitions)
      │
      ├── State: ReceiptViewModel (StateFlow<BillBusterUiState>)
      │
      ├── Screens:
      │     ├── OverviewScreen (KPIs, Charts, Recent Activity)
      │     ├── ScanScreen (Viewfinder, File Picker, 4-Stage Animation)
      │     ├── AuditDetailScreen (Receipt Breakdown, Flagged Warnings)
      │     ├── HistoryScreen (Search, Status Filter, Receipt List)
      │     ├── InsightsScreen (Monthly Breakdown, Donut/Bar Canvas)
      │     └── SettingsScreen (Sana Fathima Profile, Project Details)
      │
      └── Modals:
            └── FixItemDialog (Editable Name, Qty, Unit Price + Quick Preset)
```

### Core Data Models
- `Receipt`: id, merchant, date, printedTotal, category, status (`FLAGGED`, `VERIFIED`), items, notes.
- `ReceiptItem`: id, name, quantity, unitPrice, lineTotal, isFlagged, expectedPrice, flagReason.
- `SpendingSummary`: totalSpent, processedCount, flaggedCount, verifiedCount, weeklyTotals, categoryShares.

---

## 5. Verification & Delivery Steps

1. Configure custom adaptive launcher icon and update `metadata.json` / `strings.xml` to **BillBuster**.
2. Implement core data models, sample datasets, and the reactive `ReceiptViewModel`.
3. Build the Jetpack Compose Material 3 UI:
   - Central theme tokens (`Theme.kt`, `Color.kt`).
   - High-performance Canvas charts (Donut & Bar charts).
   - Bottom navigation structure and all 5 primary views + Audit modal.
4. Verify complete interactive audit journey:
   - Overview → Scan Demo Receipt → Watch 4-stage scan → Review Trader Joe's mismatch → Open Manual Line Editor → Correct to $4.99 → Confirm status changes to Verified → Verify updated stats in History and Insights.
5. Compile and test with `compile_applet` to ensure a clean, warning-free build.
