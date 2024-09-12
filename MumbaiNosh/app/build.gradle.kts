val annotationProcessor: Unit
    get() {
        TODO()
    }

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.mumbainosh"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.mumbainosh"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/NOTICE.md")
    }
}


dependencies {
    implementation ("com.airbnb.android:lottie:5.0.3")
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation ("com.google.firebase:firebase-database:20.0.5")
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    implementation(libs.android.mail.v167)
    implementation(libs.android.activation)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}