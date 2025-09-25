## EasyApp (Android Sample Project)

学習用の最小構成 Android アプリ (Compose + 従来XML 画面) です。ハンディターミナル向け検証の初期雛形として利用できます。

### 機能概要
* メイン画面: Jetpack Compose カウンター (+1 / -1) と Legacy 画面遷移ボタン
* Legacy 画面: ViewBinding (XML) でカウンター表示
* Compose / XML 並存構成の例

### ディレクトリ構成 (抜粋)
```
settings.gradle.kts
build.gradle.kts (ルート)
gradle.properties
app/
	build.gradle.kts
	src/main/
		AndroidManifest.xml
		java/com/example/easyapp/
			MainActivity.kt (Compose)
			LegacyActivity.kt (XML + ViewBinding)
		res/layout/activity_legacy.xml
		res/values/strings.xml, themes.xml, colors.xml
```

### クローン & 実行 (Android Studio)
1. リポジトリを取得
2. Android Studio で `Open` → プロジェクトルートを選択
3. Gradle Sync 完了を待つ
4. AVD (または実機) を選択し Run ▶

### コマンドラインビルド
ローカルに **公式の完全な Gradle Wrapper** を生成していない状態なので、初回は Android Studio で `gradlew wrapper` を実行し正式スクリプトを再生成してください。(本リポジトリ同梱の `gradlew` は簡略化版)

生成後:
```
./gradlew assembleDebug
```
成果物: `app/build/outputs/apk/debug/app-debug.apk`

### 依存バージョン (抜粋)
* Android Gradle Plugin: 8.6.1
* Kotlin: 1.9.24
* Compose BOM: 2024.06.00
* minSdk: 24 / targetSdk: 34

### 実機 (ハンディターミナル) デプロイ手順メモ
1. 端末で開発者モード & USB デバッグ有効
2. `adb devices` で認識確認
3. Android Studio から Run (または `adb install -r app-debug.apk`)
4. バーコードスキャナがキーボード入力型なら、フォーカス中の TextField / EditText へ文字列 + Enter が飛ぶことが多い
5. メーカー独自サービス (例: DataWedge 等) 利用時は Broadcast / Intent / ContentProvider の仕様書を参照

### 今後の拡張アイデア
* スキャン結果受信用の共通インタフェース層追加
* オフラインキャッシュ(Room) + 同期ワーカー(WorkManager)
* 画面サイズ差異対応 (Smallest width qualifier など)
* CI: GitHub Actions で `./gradlew lint ktlintCheck test assembleDebug`

### ライセンス
学習目的のサンプル。必要に応じて追記してください。

---
質問や追加要望があれば Issue / Chat でどうぞ。
# easy_app