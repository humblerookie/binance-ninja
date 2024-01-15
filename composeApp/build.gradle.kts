import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

apply(from = "environment.gradle.kts")

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.ksp)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.ktlint)
  alias(libs.plugins.spotless)
  alias(libs.plugins.buildKonfig)
}

@Suppress("UNCHECKED_CAST") val getEnv = extra["getEnv"] as (String) -> String
val bundleId = "dev.anvith.binanceninja"

kotlin {
  androidTarget { compilations.all { kotlinOptions { jvmTarget = "1.8" } } }
  jvm("desktop")

  listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries {
      framework {
        baseName = "ComposeApp"
        isStatic = true
      }
      findTest(NativeBuildType.DEBUG)?.linkerOpts = mutableListOf("-lsqlite3")
      findTest(NativeBuildType.RELEASE)?.linkerOpts = mutableListOf("-lsqlite3")
    }
  }

  sourceSets {
    val desktopMain by getting

    androidMain.dependencies {
      api(libs.compose.ui.tooling.core)
      api(libs.compose.ui.tooling.preview)
      implementation(libs.androidx.activity.compose)
      implementation(libs.sqldelight.android.driver)
      implementation(libs.ktor.client.okhttp)
      implementation(libs.bundles.workmanager)
      implementation(libs.androidx.splashscreen)
    }

    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.compose.ui.tooling.preview)
      implementation(libs.sqldelight.jvm.driver)
      implementation(libs.ktor.client.okhttp)
      implementation(libs.notify.desktop)
    }

    commonMain {
      dependencies {
        implementation(libs.bundles.compose)
        implementation(libs.bundles.voyager)
        implementation(libs.bundles.ktor)
        implementation(libs.inject.runtime)
        api(libs.lyricist.core)
        api(libs.kotlinx.coroutines)
        implementation(libs.sqldelight.extensions.coroutines)
        implementation(libs.kotlinx.serialization)
        api(libs.napier)
        implementation(libs.bundles.settings)
      }
      kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutines.test)
    }
    iosMain.dependencies {
      implementation(libs.sqldelight.native.driver)
      implementation(libs.ktor.client.darwin)
    }
  }

  // This is how we include ksp in multiplatform
  dependencies {
    add("kspCommonMainMetadata", libs.lyricist.processor)
    add("kspCommonMainMetadata", libs.inject.processor)
    add("kspAndroid", libs.inject.processor)
    add("kspIosArm64", libs.inject.processor)
    add("kspIosSimulatorArm64", libs.inject.processor)
    add("kspDesktop", libs.inject.processor)
  }
}

ksp { arg("lyricist.generateStringsProperty", "true") }

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
  if (name != "kspCommonMainKotlinMetadata") {
    dependsOn("kspCommonMainKotlinMetadata")
  }
}

android {
  namespace = bundleId
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].res.srcDirs("src/commonMain/resources")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  defaultConfig {
    applicationId = bundleId
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = getEnv("VERSION_CODE").toInt()
    versionName = getEnv("VERSION")
  }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
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
  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.6" }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = bundleId
      packageVersion = getEnv("DESKTOP_VERSION")
    }
  }
}

sqldelight {
  databases {
    create("NinjaDatabase") {
      packageName.set("$bundleId.data.cache")
      dialect(libs.sqldelight.sqlite.dialect)
      schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
      verifyMigrations.set(true)
    }
  }
}

allprojects {
  apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)
  configure<SpotlessExtension> {
    kotlin {
      ktfmt(libs.versions.ktfmt.get()).googleStyle()
      target("src/**/*.kt")
      targetExclude("${layout.buildDirectory}/**/*.kt")
    }
    kotlinGradle {
      ktfmt(libs.versions.ktfmt.get()).googleStyle()
      target("*.kts")
      targetExclude("${layout.buildDirectory}/**/*.kts")
      toggleOffOn()
    }
    format("xml") {
      target("src/**/*.xml")
      targetExclude("**/build/", ".idea/")
      trimTrailingWhitespace()
      endWithNewline()
    }
  }

  tasks.withType<KotlinCompile>().all {
    kotlinOptions { freeCompilerArgs += "-Xexpect-actual-classes" }
  }
}

ktlint {
  verbose.set(true)
  outputToConsole.set(true)
}

buildkonfig {
  packageName = bundleId
  defaultConfigs {
    buildConfigField(BOOLEAN, "IS_DEBUG", getEnv("IS_DEBUG"))
    buildConfigField(STRING, "VERSION", getEnv("VERSION"))
  }
}
