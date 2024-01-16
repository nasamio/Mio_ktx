plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mio.filemanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mio.filemanager"
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
        kotlinCompilerExtensionVersion = "1.4.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // jsch
    implementation("com.jcraft:jsch:0.1.55")
    // sshj
//    implementation("net.schmizz:sshj:0.30.0")
//    implementation("com.hierynomus:sshj:0.38.0")


    // 导入本地base模块
    implementation(project(mapOf("path" to ":base")))

    // 网络请求
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // retrofit 依赖 Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:3.2.0")
    // retrofit 依赖 RxJava
    implementation("com.squareup.retrofit2:adapter-rxjava:2.9.0")

    // ktor
    implementation("io.ktor:ktor-client-android:1.6.4")
    // JsonFeature
    implementation("io.ktor:ktor-client-json:1.6.4")
    // ktor-client-gson
    implementation("io.ktor:ktor-client-gson:1.6.4")
    // ktor-client-serialization-jvm
    implementation("io.ktor:ktor-client-serialization-jvm:1.6.4")
}