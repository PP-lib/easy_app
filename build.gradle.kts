// ルート build.gradle.kts (通常は大きな設定は不要)
// モノレポ化やバージョンカタログ導入をする場合はここを拡張

plugins {
    id("com.android.application") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
