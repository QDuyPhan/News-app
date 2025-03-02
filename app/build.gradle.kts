import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
}
val localProperties = Properties()
val localPropertiesFile = rootProject.file("key.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

android {
    namespace = "com.example.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.newsapp"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            type = "String", name = "BASE_URL", value = localProperties["BASE_URL"].toString()
        )
        buildConfigField(
            type = "String",
            name = "RELEASE_STORE_FILE",
            value = localProperties["RELEASE_STORE_FILE"].toString()
        )
        buildConfigField(
            type = "String",
            name = "RELEASE_STORE_PASSWORD",
            value = localProperties["RELEASE_STORE_PASSWORD"].toString()
        )
        buildConfigField(
            type = "String",
            name = "RELEASE_KEY_ALIAS",
            value = localProperties["RELEASE_KEY_ALIAS"].toString()
        )
        buildConfigField(
            type = "String",
            name = "RELEASE_KEY_PASSWORD",
            value = localProperties["RELEASE_KEY_PASSWORD"].toString()
        )


    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\Work\\News-app\\keystore\\mykeystore.jks")
            storePassword = "password"
            keyAlias = "mydomain"
            keyPassword = "password"
        }
        create("release") {
            storeFile = file("D:\\Work\\News-app\\keystore\\mykeystore.jks")
            storePassword = "password"
            keyAlias = "mydomain"
            keyPassword = "password"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")

        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    dataBinding {
        enable = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //Gson
    implementation(libs.gson)
    // Coroutine
//    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Glide
    implementation(libs.glide)
    ksp(libs.compiler)
    //Client
    implementation(libs.okhttp)
    //OkHttp Interceptor
    implementation(libs.logging.interceptor)
    //Image loading
    implementation(libs.coil)
    // RoundedImageView
    implementation(libs.roundedimageview)
    //paging
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime.ktx)
    // Timber
    implementation(libs.timber)
    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.sdp.android)
    // Moshi
    implementation(libs.moshi.kotlin)
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.config.ktx)
    // Splash Screen
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.material)
    implementation(libs.circleimageview)
}