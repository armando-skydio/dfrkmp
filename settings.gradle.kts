enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "mpp"
include(":androidApp")
include(":shared")
include(":uicomponents")
include(":strings")

include(":dfr-patrol")
project(":dfr-patrol").projectDir = file("./patrol-link/android/app")

include(":capacitor-cordova-android-plugins")
project(":capacitor-cordova-android-plugins").projectDir = file("./patrol-link/android/capacitor-cordova-android-plugins/")

include(":capacitor-android")
project(":capacitor-android").projectDir = file("./patrol-link/@capacitor/android/capacitor")

include(":capacitor-camera")
project(":capacitor-camera").projectDir = file("./patrol-link/@capacitor/camera/android")

include(":capacitor-splash-screen")
project(":capacitor-splash-screen").projectDir = file("./patrol-link/@capacitor/splash-screen/android")
