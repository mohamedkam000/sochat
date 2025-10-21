plugins {
    id("com.android.application") version "9.0.0-alpha10" apply false
    id("com.android.library") version "9.0.0-alpha10" apply false
    id("com.google.devtools.ksp") version "1.5.30-1.0.0" apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.serialization) apply false
}