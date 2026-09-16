plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.sere.filemanager.core.model"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    kotlinOptions { jvmTarget = "17" }
}
