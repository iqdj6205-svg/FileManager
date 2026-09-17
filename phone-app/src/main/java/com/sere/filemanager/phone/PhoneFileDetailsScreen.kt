package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.ui.UiFormatters

@Composable
fun PhoneFileDetailsScreen(item: FileItem, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("File details")
        Text(item.name, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text("Type: ${item.type}")
        Text("Size: ${UiFormatters.compactBytes(item.sizeBytes)}")
        Text("Path: ${item.path}", maxLines = 4, overflow = TextOverflow.Ellipsis)
        Button(onClick = onBack) { Text("Back") }
    }
}
