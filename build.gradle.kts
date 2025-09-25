// ルート build.gradle.kts (通常は大きな設定は不要)
// モノレポ化やバージョンカタログ導入をする場合はここを拡張

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
