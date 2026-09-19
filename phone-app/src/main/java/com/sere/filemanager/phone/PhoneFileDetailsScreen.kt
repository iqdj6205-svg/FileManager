package com.sere.filemanager.phone

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.ui.UiFormatters
import com.sere.filemanager.phone.ui.PhoneScreenScaffold
import com.sere.filemanager.phone.ui.PhoneSectionCard

@Composable
fun PhoneFileDetailsScreen(item: FileItem, onBack: () -> Unit) {
    PhoneScreenScaffold(title = "File details", subtitle = item.name) {
        PhoneSectionCard(title = "Properties") {
            Text("Type: ${item.type}", maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Size: ${UiFormatters.compactBytes(item.sizeBytes)}")
            Text("Path: ${item.path}", maxLines = 4, overflow = TextOverflow.Ellipsis)
        }
        Button(onClick = onBack) { Text("Back") }
    }
}
