plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.playservices)
}

android {
    namespace = "com.skydio.mpp.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.skydio.mpp.android"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
val abcLocationLib = "com.linecorp.abc:kmm-location:0.2.4"
dependencies {
    implementation(projects.shared)
    implementation(projects.uicomponents)
    implementation(libs.compose.ui)
    implementation(abcLocationLib)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.ktor.client.core)
    implementation(libs.compose.material3)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.lifecycle.viewmodel.compose)
}