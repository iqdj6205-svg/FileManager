pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WearFileManager"
include(":wear-app")
include(":phone-app")
include(":core-model")
include(":core-files")
include(":core-remote")
include(":core-media")
include(":core-ui")
include(":core-wear-bridge")
