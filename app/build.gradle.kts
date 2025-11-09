plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.btcemais.enriquecer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.btcemais.enriquecer"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "0.1"

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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dependências para Navigation/Fragments
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Dependências para ViewPager2 e TabLayout
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Dependências para Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // Dependências para Lifecycle (ViewModel e LiveData)
    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Configurações para evitar conflitos com dependências do Compose
    configurations.all {
        resolutionStrategy {
            // Forçar versões específicas para evitar conflitos
            force("androidx.compose.ui:ui:1.5.4")
            force("androidx.compose.runtime:runtime:1.5.4")
            force("androidx.compose.ui:ui-graphics:1.5.4")
            force("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
        }
    }
}