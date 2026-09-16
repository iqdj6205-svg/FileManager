package com.sere.filemanager.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sere.filemanager.core.ui.FileManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PhoneCompanionApp() }
    }
}

@Composable
fun PhoneCompanionApp() {
    FileManagerTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("FileManager Companion", style = MaterialTheme.typography.headlineSmall)
            Text("Pair with your Wear OS watch, transfer files, and manage remote access.")
            Button(onClick = { }) { Text("Pair watch") }
            Button(onClick = { }) { Text("Open remote manager") }
            Button(onClick = { }) { Text("Send files") }
        }
    }
}
