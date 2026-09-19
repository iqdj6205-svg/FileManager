package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.files.FileOperationProgress
import com.sere.filemanager.core.ui.UiFormatters
import com.sere.filemanager.phone.ui.PhoneSectionCard

@Composable
fun PhoneOperationProgressCard(progress: FileOperationProgress, modifier: Modifier = Modifier) {
    if (progress.operationLabel == "Idle" && progress.message == null) return
    PhoneSectionCard(title = progress.operationLabel) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(progress.operationLabel, style = MaterialTheme.typography.titleMedium)
            progress.message?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
            progress.source?.let { Text("From: $it", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            progress.target?.let { Text("To: $it", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            val percent = progress.percent
            if (percent != null) {
                LinearProgressIndicator(progress = { percent / 100f }, modifier = Modifier.fillMaxWidth())
                Text("$percent% · ${UiFormatters.compactBytes(progress.bytesDone)} / ${UiFormatters.compactBytes(progress.bytesTotal ?: 0L)}")
            } else if (!progress.completed && !progress.failed) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text(UiFormatters.compactBytes(progress.bytesDone))
            }
        }
    }
}
