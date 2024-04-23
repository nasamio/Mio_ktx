plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mio.enc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mio.enc"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            //设置支持的架构
            abiFilters.add("armeabi")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("x86_64")
            abiFilters.add("x86")
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
    sourceSets["main"]
        .jniLibs
        .srcDirs("libs")

}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs\\gdal.aar"))
    // 导入libs下的gdal.aar
//    implementation(files("libs\\s57Library.jar"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(project(mapOf("path" to ":base")))
//    implementation(project(mapOf("path" to ":net.sf.capcode.library.S57Library")))


    // 权限
    implementation("com.yanzhenjie:permission:2.0.3")

    implementation(project(mapOf("path" to ":s57")))

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")


}