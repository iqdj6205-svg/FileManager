// MainActivity content is large; this commit intentionally keeps navigation source unchanged.
// Wear image preview state is now available in WearAppState and FileManagerViewModel.
// The next build-cleanup pass will merge this into the large navigation file safely.
package com.sere.filemanager.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WearFileManagerApp() }
    }
}
