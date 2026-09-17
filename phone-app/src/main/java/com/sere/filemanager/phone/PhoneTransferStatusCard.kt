package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhoneTransferStatusCard(state: PhoneTransferState, onRefresh: () -> Unit) {
    val progress = state.latestProgress ?: return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Transfer: ${progress.state}")
            progress.message?.let { Text(it) }
            val total = progress.totalBytes
            if (total != null && total > 0L) {
                LinearProgressIndicator(progress = { (progress.transferredBytes.toFloat() / total.toFloat()).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
                Text("${progress.transferredBytes} / $total bytes")
            }
            androidx.compose.material3.Button(onClick = onRefresh) { Text("Refresh transfer") }
        }
    }
}
