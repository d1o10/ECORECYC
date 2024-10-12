plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt") // Apply the kapt plugin for annotation processing
    id("com.google.gms.google-services") // Google Services plugin for Firebase
}

android {
    namespace = "com.capstone.ecorecyc" // Application namespace
    compileSdk = 34 // Compile SDK version

    defaultConfig {
        applicationId = "com.capstone.ecorecyc" // Unique application ID
        minSdk = 30 // Minimum SDK version
        targetSdk = 34 // Target SDK version
        versionCode = 1 // Application version code
        versionName = "1.0" // Application version name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Disable code shrinking for release build
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Default ProGuard rules
                "proguard-rules.pro" // Custom ProGuard rules
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Set source compatibility to Java 8
        targetCompatibility = JavaVersion.VERSION_1_8 // Set target compatibility to Java 8
    }

    kotlinOptions {
        jvmTarget = "1.8" // Set JVM target to Java 8
    }

    buildFeatures {
        viewBinding = true // Enable View Binding
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) // Android KTX core library
    implementation(libs.androidx.appcompat) // AppCompat library
    implementation(libs.material) // Material Components library
    implementation(libs.androidx.activity) // Activity library
    implementation(libs.androidx.constraintlayout) // ConstraintLayout library
    implementation(platform(libs.firebase.bom)) // Firebase BOM for version management
    implementation(libs.firebase.auth.ktx) // Firebase Authentication KTX
    implementation(libs.firebase.firestore.ktx) // Firestore KTX
    implementation(libs.firebase.storage.ktx) // Firebase Storage KTX
    implementation(libs.firebase.database.ktx) // Firebase Realtime Database KTX
    implementation(libs.glide) // Glide for image loading
    implementation(libs.androidx.navigation.fragment.ktx) // Navigation component for fragments
    implementation(libs.androidx.navigation.ui.ktx) // Navigation component for UI
    implementation(libs.androidx.recyclerview) // RecyclerView library
    kapt(libs.glide.compiler) // Glide annotation processor

    testImplementation(libs.junit) // JUnit for unit testing
    androidTestImplementation(libs.androidx.junit) // JUnit for Android UI testing
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing
}
