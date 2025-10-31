plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    // ナビゲーションでカスタムNavTypeを指定するのに必要。
//    id("kotlin-parcelize")
}

android {
    namespace = "jp.naorin.yarnshelf"
    compileSdk = 35

    // Google Play Console「この App Bundle にはネイティブ コードが含まれ、デバッグ シンボルがアップロードされていません。」対応
//    ndkVersion = "27.0.12077973"

    defaultConfig {
        applicationId = "jp.naorin.yarnshelf"
        minSdk = 24
        targetSdk = 35
        versionCode = 9
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            proguardFiles(
                // Includes the default ProGuard rules files that are packaged with
                // the Android Gradle plugin. To learn more, go to the section about
                // R8 configuration files.
                getDefaultProguardFile("proguard-android-optimize.txt"),

                // Includes a local, custom Proguard rules file
                "proguard-rules.pro"
            )
            // Google Play Console「この App Bundle にはネイティブ コードが含まれ、デバッグ シンボルがアップロードされていません。」対応
            ndk {
                debugSymbolLevel = "FULL"
            }
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation("androidx.compose.material3:material3:1.2.1")

    // ViewModelを利用するのに必要。ユニット4「アーキテクチャコンポーネント」より。
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // NavHostを利用するのに必要。ユニット4「Jetpack Composeでのナビゲーション」より。
    implementation(libs.androidx.navigation.compose)
//    implementation(libs.androidx.navigation.runtime.ktx)

    // Roomを利用するのに必要。ユニット６「データの永続化」より。
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // APIを利用するのに必要。ユニット５「インターネットに接続する」より。
    // Retrofit
    implementation(libs.retrofit)
    // Retrofit with Scalar Converter
    // Retrofit は JSON 結果を String として返すことができます。
    // Serialization Converterを使うのでこちらはコメントアウト
//    implementation(libs.converter.scalars)
    // JSON解析をするのに必要。
    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    // Retrofit with Kotlin serialization Converter
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)

    //Coilを利用するのに必要。Coil は、基本的に次の 2 つのものを必要とします。
    //
    //読み込んで表示する画像の URL。
    //実際に画像を表示するための AsyncImage コンポーザブル。
    // Coil
    implementation(libs.coil.compose)

    //バーコードスキャンするのに必要。
    // https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner?hl=ja
    implementation(libs.play.services.code.scanner)

    //ML KitでGoogle Play開発者サービスからモジュールをダウンロードするために必要
    implementation(libs.play.services.base)
    implementation(libs.play.services.tflite.java)

    // Google Play での警告「androidx.fragment:fragment (androidx.fragment:fragment) により、バージョン 1.0.0 は古いことが報告されています。新しいバージョン（1.1.0+）にアップグレードすることをおすすめします。」対応
    constraints{
        implementation("androidx.fragment:fragment:1.6.2"){
            because("Gms library depends on outdated androidx fragment")
        }
    }
    // テスト
    testImplementation(libs.junit)

    // コルーチンのテスト
    testImplementation(libs.kotlinx.coroutines.test)

    // UIテスト
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    // Navigationテスト
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.junit)
    // Espresso
    // IntentsはNavigationテストにインポートが推奨、CoreはRoomのテストにインポートが推奨とされていたが、なくても動いた。
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.espresso.intents)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}