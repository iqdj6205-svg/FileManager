import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.sere.filemanager.core.remote"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core-model"))
    implementation(project(":core-files"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}
