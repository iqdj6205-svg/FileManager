package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun PhoneStorageAnalyzerScreen(state: PhoneAnalyzerState, onAnalyze: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Storage Analyzer", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = onAnalyze, modifier = Modifier.fillMaxWidth()) { Text(if (state.isLoading) "Analyzing…" else "Analyze current folder") }
        state.message?.let { Text(it) }
        val analysis = state.analysis
        if (analysis != null) {
            Text("Files: ${analysis.totalFiles}, size: ${StorageFormatter.bytes(analysis.totalBytes)}")
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                items(state.insights) { insight -> Card(modifier = Modifier.fillMaxWidth()) { Column(modifier = Modifier.padding(12.dp)) { Text(insight.title); Text(insight.description) } } }
                items(analysis.categories) { category -> Card(modifier = Modifier.fillMaxWidth()) { Column(modifier = Modifier.padding(12.dp)) { Text(category.type.name); Text("${category.count} files · ${StorageFormatter.bytes(category.totalBytes)}") } } }
                items(analysis.largestFiles) { file -> Text("${file.name} · ${StorageFormatter.bytes(file.sizeBytes)}", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            }
        }
        Button(onClick = onBack) { Text("Home") }
    }
}
