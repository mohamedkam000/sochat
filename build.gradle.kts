plugins {
    id("com.android.application") version "8.13" apply false
    id("com.android.library") version "8.13" apply false
    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.serialization) apply false
}