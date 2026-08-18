package com.example.domain

object DefaultTemplates {
    val list = listOf(
        WorkToolTemplate(
            id = "auto_form",
            name = "Auto-Form & Survey Dispatcher",
            category = ToolTemplateCategory.WORK_AUTOMATION,
            description = "Time-saving automated form builder with auto-fill macros and instant field submission.",
            defaultPackageName = "com.devator.tools.autoform",
            featureHighlights = listOf(
                "Offline drafting & auto-sync upon connection",
                "GPS location stamping for field audits",
                "Instant auto-deploy patches on form schema changes"
            )
        ),
        WorkToolTemplate(
            id = "daily_audit",
            name = "Daily Audit & Metrics Sync",
            category = ToolTemplateCategory.DATA_COLLECTION,
            description = "Automates daily work log collection, safety compliance checks, and manager reports.",
            defaultPackageName = "com.devator.tools.dailyaudit",
            featureHighlights = listOf(
                "Automated end-of-day summary dispatch",
                "Encrypted local DataStore audit vault",
                "Automatic OTA update when compliance rules update"
            )
        ),
        WorkToolTemplate(
            id = "inventory_scanner",
            name = "Inventory & Asset QR Scanner",
            category = ToolTemplateCategory.FIELD_OPERATIONS,
            description = "Rapid barcode & QR stock auditor for warehouse and store inventory management.",
            defaultPackageName = "com.devator.tools.inventory",
            featureHighlights = listOf(
                "High-speed camera scanner stream",
                "Real-time stock discrepancy highlights",
                "Background auto-patching for catalog price updates"
            )
        ),
        WorkToolTemplate(
            id = "time_tracker",
            name = "Shift & Task Time Saver",
            category = ToolTemplateCategory.TIME_TRACKING,
            description = "One-tap clock-in/out widget with automated breaks, task tagging, and timesheet exports.",
            defaultPackageName = "com.devator.tools.timetracker",
            featureHighlights = listOf(
                "Floating status widget and quick action pill",
                "Automated break time calculation",
                "Direct timesheet CSV & PDF exporter"
            )
        ),
        WorkToolTemplate(
            id = "expense_packer",
            name = "Expense & Receipt Fast-Packer",
            category = ToolTemplateCategory.WORK_AUTOMATION,
            description = "Snap receipts, auto-extract totals, categorize expenses, and submit expense reports.",
            defaultPackageName = "com.devator.tools.expensepacker",
            featureHighlights = listOf(
                "Instant receipt total calculation",
                "Multi-currency auto conversion",
                "Silent background update for policy rules"
            )
        )
    )
}
