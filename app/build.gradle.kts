import java.io.FileInputStream
import java.util.Properties
import kotlin.apply
import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    id("org.jlleitschuh.gradle.ktlint").version("11.6.1")
    id("com.github.triplet.play") version "3.8.6"
}

val secretProperties = Properties().apply {
    load(FileInputStream(rootProject.file("secret.properties")))
}

val keystoreProperties = Properties().apply {
    load(FileInputStream(rootProject.file("keystore.properties")))
}

android {
    namespace = "com.bolivianusd.app"
    compileSdk = 36

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    defaultConfig {
        applicationId = "com.bolivianusd.app"
        minSdk = 26
        targetSdk = 34
        versionCode = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: 8
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled  = true

        buildConfigField("String", "DATABASE_NAME", "\"app_database\"")
        buildConfigField("Long", "REMOTE_CONFIG_FETCH_INTERVAL", "3600L")
        buildConfigField("Long", "REMOTE_CONFIG_TIME_OUT", "60L")
        buildConfigField("String", "CIPHER_KEY", "\"${secretProperties["cipherKey"]}\"")
        buildConfigField("String", "CIPHER_IV", "\"${secretProperties["cipherIv"]}\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "env"

    productFlavors {
        create("production") {
            dimension = "env"
            buildConfigField("String", "SUPABASE_URL", "\"${secretProperties["supabaseUrlProd"]}\"")
            buildConfigField("String", "SUPABASE_KEY", "\"${secretProperties["supabaseKeyProd"]}\"")
        }
        create("certification") {
            dimension = "env"
            applicationIdSuffix = ".cert"
            versionNameSuffix = "-cert"
            buildConfigField("String", "SUPABASE_URL", "${secretProperties["supabaseUrlCert"]}\"")
            buildConfigField("String", "SUPABASE_KEY", "${secretProperties["supabaseKeyCert"]}\"")
        }
        create("develop") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "SUPABASE_URL", "\"${secretProperties["supabaseUrlDev"]}\"")
            buildConfigField("String", "SUPABASE_KEY", "\"${secretProperties["supabaseKeyDev"]}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    lint {
        disable += "NullSafeMutableLiveData"
        checkReleaseBuilds = false
        abortOnError = false
        checkTestSources = false
        quiet = true
    }
}

play {
    serviceAccountCredentials.set(file("${rootDir}/play-upload.json"))
    defaultToAppBundles.set(true)
    track.set("internal")
    releaseStatus.set(ReleaseStatus.DRAFT)
}

dependencies {
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.gson)

    implementation(libs.lottie)
    implementation(libs.mpandroidchart)
    implementation(libs.rollingtext)
    implementation(libs.circleimageview)
    implementation(libs.switchbutton)
    implementation(libs.shimmer)
    implementation(libs.blurview)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.config)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest.kt)

    implementation(libs.ktor.client.android)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
