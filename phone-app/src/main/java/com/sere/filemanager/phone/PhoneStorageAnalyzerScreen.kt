package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.files.StorageFormatter
import com.sere.filemanager.phone.ui.PhoneScreenScaffold
import com.sere.filemanager.phone.ui.PhoneSectionCard

@Composable
fun PhoneStorageAnalyzerScreen(state: PhoneAnalyzerState, onAnalyze: () -> Unit, onBack: () -> Unit) {
    PhoneScreenScaffold(title = "Storage Analyzer", subtitle = state.message) {
        Button(onClick = onAnalyze, modifier = Modifier.fillMaxWidth()) { Text(if (state.isLoading) "Analyzing…" else "Analyze current folder") }
        val analysis = state.analysis
        if (analysis != null) {
            PhoneSectionCard(title = "Summary") {
                Text("Files: ${analysis.totalFiles}, size: ${StorageFormatter.bytes(analysis.totalBytes)}", style = MaterialTheme.typography.bodyMedium)
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                items(state.insights) { insight -> Card(modifier = Modifier.fillMaxWidth()) { androidx.compose.foundation.layout.Column(modifier = Modifier.padding(12.dp)) { Text(insight.title, style = MaterialTheme.typography.titleSmall); Text(insight.description, style = MaterialTheme.typography.bodySmall) } } }
                items(analysis.categories) { category -> Card(modifier = Modifier.fillMaxWidth()) { androidx.compose.foundation.layout.Column(modifier = Modifier.padding(12.dp)) { Text(category.type.name, style = MaterialTheme.typography.titleSmall); Text("${category.count} files · ${StorageFormatter.bytes(category.totalBytes)}", style = MaterialTheme.typography.bodySmall) } } }
                items(analysis.largestFiles) { file -> Text("${file.name} · ${StorageFormatter.bytes(file.sizeBytes)}", maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall) }
            }
        }
        Button(onClick = onBack) { Text("Home") }
    }
}
