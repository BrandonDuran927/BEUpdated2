plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.beupdated"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.beupdated"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/DEPENDENCIES" // Keep this if you still want to exclude this file
        }
    }

}

dependencies {
    // Room implementation
    implementation(libs.androidx.room.runtime)
    implementation(libs.googleid)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.ui.test.android)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Twilio
    implementation(libs.twilio)

    // Amazon SES
    implementation(libs.aws.android.sdk.ses)
    implementation(libs.aws.android.sdk.core)

    // JavaMail
    implementation(libs.android.mail)
    implementation(libs.android.activation)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.compose)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Dagger hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.hilt.navigation.compose) // Hilt viewmodel
    kapt(libs.hilt.android.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}