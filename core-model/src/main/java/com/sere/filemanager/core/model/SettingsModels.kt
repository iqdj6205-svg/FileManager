package com.sere.filemanager.core.model

data class AppSettings(
    val darkTheme: Boolean = true,
    val haptics: Boolean = true,
    val advancedMode: Boolean = false,
    val showHiddenFiles: Boolean = false,
    val confirmDeletes: Boolean = true,
    val batteryPolicy: BatteryPolicy = BatteryPolicy(),
)
