package com.sere.filemanager.core.model

data class UserMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val kind: UserMessageKind = UserMessageKind.Info,
)

enum class UserMessageKind { Info, Success, Warning, Error }
