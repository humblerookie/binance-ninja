apply(from = "../scripts/environment.gradle.kts")

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sentry)
}
@Suppress("UNCHECKED_CAST")
val getEnv = extra["getEnv"] as (String) -> String
val bundleId = extra["bundleId"] as String

kotlin {
    jvm(){
        compilations.all { kotlinOptions { jvmTarget = extra["java"].toString() } }
    }
    androidTarget(){
        compilations.all { kotlinOptions { jvmTarget = extra["java"].toString() } }
    }
}
sentry {
    autoInstallation {
        enabled.set(false)
    }
}
android {
    namespace = bundleId
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = bundleId
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = getEnv("VERSION_CODE").toInt()
        versionName = getEnv("VERSION")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file("$rootDir/release/release.jks")
            storePassword = getEnv("KEYSTORE_PASSWORD")
            keyAlias = "ninja"
            keyPassword = getEnv("KEYSTORE_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug { applicationIdSuffix = ".debug" }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures { buildConfig = true }
    packaging {
        resources { excludes.add("/META-INF/{AL2.0,LGPL2.1}") }

        // Deprecated ABIs. See https://developer.android.com/ndk/guides/abis
        jniLibs.excludes.add("lib/mips/libsqlite3x.so")
        jniLibs.excludes.add("lib/mips64/libsqlite3x.so")
        jniLibs.excludes.add("lib/armeabi/libsqlite3x.so")
    }
}

dependencies {
    implementation(project(":composeApp"))
    testImplementation(libs.junit)
    implementation(libs.inject.runtime)
    implementation(libs.androidx.activity.compose)
    ksp(libs.inject.processor)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.splashscreen)
    constraints{
        implementation(libs.androidx.fragment){
            because("Gms library depends on outdated androidx")
        }
    }
}
