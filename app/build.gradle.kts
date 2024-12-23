import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    id("com.google.dagger.hilt.android")
//    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sanapplications.cantransit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sanapplications.cantransit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties=Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        resValue ("string", "MAPS_API_KEY", ""+properties.getProperty("MAPS_API_KEY")+"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Google maps
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Retrofit and Moshi
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)

    // Coroutine support for asynchronous calls
    implementation(libs.kotlinx.coroutines.android)

    // Google Maps and Routes API (if not already added)
    implementation(libs.google.maps.services) // For Google-specific utilities

    // Google maps for compose
    implementation(libs.maps.compose.v280)

    // KTX for the Maps SDK for Android
    implementation(libs.maps.ktx)

    // KTX for the Maps SDK for Android Utility Library
    implementation(libs.maps.utils.ktx)

    // Permissions
    implementation(libs.accompanist.permissions)

    // Hilt dependencies
//    implementation (libs.hilt.android)
//    implementation (libs.androidx.lifecycle.viewmodel.compose)
//    ksp (libs.hilt.compiler)
//    implementation (libs.androidx.hilt.navigation.compose)

}