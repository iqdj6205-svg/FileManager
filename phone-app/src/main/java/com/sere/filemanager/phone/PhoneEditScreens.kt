package com.sere.filemanager.phone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhoneRenameFileScreen(value: String, onValue: (String) -> Unit, onSave: () -> Unit, onCancel: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Rename")
        OutlinedTextField(value = value, onValueChange = onValue, label = { Text("New name") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) { Text("Save") }
        Button(onClick = onCancel) { Text("Cancel") }
    }
}

@Composable
fun PhoneCreateFolderScreen(value: String, onValue: (String) -> Unit, onCreate: () -> Unit, onCancel: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Create folder")
        OutlinedTextField(value = value, onValueChange = onValue, label = { Text("Folder name") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = onCreate, modifier = Modifier.fillMaxWidth()) { Text("Create") }
        Button(onClick = onCancel) { Text("Cancel") }
    }
}
