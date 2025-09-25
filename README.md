## EasyApp - Scan PoC

バーコードスキャナを「キーボード入力（文字列+Enter）」として扱う最小 PoC。約1時間で構築できるシンプル構成です。

### 現在の機能
* 文字列(バーコード想定)をキーイベントでバッファリング
* Enter 受信で確定 → 最新コードと履歴(最大50件)表示
* 一定時間(デフォルト 350ms)キー間が空くとバッファ自動リセット
* UI: Jetpack Compose のみ

### ディレクトリ構成 (抜粋)
```
settings.gradle.kts
build.gradle.kts (ルート)
gradle.properties
app/
		build.gradle.kts
		src/main/
			AndroidManifest.xml
			java/com/example/easyapp/MainActivity.kt (Scan PoC)
			res/values/*.xml
```

### クローン & 実行 (Android Studio)
1. リポジトリを取得
2. Android Studio で `Open` → プロジェクトルートを選択
3. Gradle Sync 完了を待つ
4. AVD (または実機) を選択し Run ▶

### 実行方法 (Android Studio)
1. クローン & Open
2. Gradle Sync 完了後 Run ▶
3. エミュレータ/実機でフォーカスされた状態で PC キーボードから `4901234567894` + Enter
4. 画面に `Last Code` として表示 / 下部に履歴追加

### コマンドラインビルド (必要な場合)
```
./gradlew assembleDebug
```
APK: `app/build/outputs/apk/debug/app-debug.apk`

### 依存バージョン (抜粋)
* Android Gradle Plugin: 8.6.1
* Kotlin: 1.9.24
* Compose BOM: 2024.06.00
* minSdk: 24 / targetSdk: 34

### 拡張アイデア (次のステップ)
| フェーズ | 追加内容 |
|----------|----------|
| 1 | JAN/EAN チェックサム検証 / 無効コード赤表示 |
| 2 | 重複短時間スキャン抑制 / バイブ / 効果音 |
| 3 | Room で履歴永続化 / 件数制限 / 検索 |
| 4 | 同期キュー (pending → sent) + WorkManager 再送 |
| 5 | Vendor SDK 抽象化 (Strategy) / 設定画面 |
| 6 | テスト (ScanBuffer 単体 / ViewModel 状態遷移) |

### ScanBuffer 仕組み概要
1. `dispatchKeyEvent` で ACTION_DOWN をフック
2. 一定間隔以上空いたらバッファクリア
3. 通常文字は連結 / DEL は1文字削除
4. ENTER で確定し履歴へ push

### 制限
* 物理スキャナが “キーボード入力型” であることが前提
* DataWedge など独自 Intent 経由モードは未対応（別途 Receiver 実装必要）
* パフォーマンス・エラー処理を簡略化

### ライセンス
学習目的サンプル。必要に応じて追記。

---
質問や追加要望があれば Issue / Chat でどうぞ。
# easy_app