buildscript {
    ext {
        set("compose_version", "1.5.1")
        set("kotlin_version", "1.9.0")
        set("room_version", "2.6.1")
    }
}
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}
