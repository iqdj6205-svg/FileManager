package com.sere.filemanager.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sere.filemanager.core.ui.FileManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PhoneCompanionApp() }
    }
}

@Composable
fun PhoneCompanionApp(viewModel: PhoneViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    FileManagerTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("FileManager Companion", style = MaterialTheme.typography.headlineSmall)
            Text("Pair with your Wear OS watch, transfer files, and manage remote access.")

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Status", style = MaterialTheme.typography.titleMedium)
                    Text(state.statusMessage)
                }
            }

            OutlinedTextField(
                value = state.remoteUrl,
                onValueChange = viewModel::setRemoteUrl,
                label = { Text("Watch HTTP URL") },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(onClick = viewModel::startPairing, modifier = Modifier.fillMaxWidth()) { Text("Pair watch") }
            Button(onClick = viewModel::startRemoteServer, modifier = Modifier.fillMaxWidth()) { Text("Start watch server") }
            Button(onClick = viewModel::stopRemoteServer, modifier = Modifier.fillMaxWidth()) { Text("Stop watch server") }
            Button(onClick = viewModel::openRemoteManager, modifier = Modifier.fillMaxWidth()) { Text("Open remote manager") }
            Button(onClick = viewModel::sendFiles, modifier = Modifier.fillMaxWidth()) { Text("Send files") }
        }
    }
}
