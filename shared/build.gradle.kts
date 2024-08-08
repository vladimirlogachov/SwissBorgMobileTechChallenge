plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.mokkery)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            api(libs.koin.android)
            api(libs.ktor.client.cio)
        }
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(libs.bignum)
            implementation(libs.koin.core)
            implementation(libs.bundles.connectivity)
            implementation(libs.bundles.ktor.data)
            implementation(libs.bundles.koin.compose)
        }
        commonTest.dependencies {
            implementation(libs.bundles.test)
        }
        iosMain.dependencies {
            api(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.swissborg.challenge"
    compileSdk = 34
    defaultConfig {
        minSdk = 30
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
