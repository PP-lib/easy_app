# EasyApp (Android Sample Project)

学習用の最小構成 Android アプリ (Compose + 従来XML 画面) です。ハンディターミナル向け検証の初期雛形として利用できます。

## 機能概要
* メイン画面: Jetpack Compose カウンター (+1 / -1) と Legacy 画面遷移ボタン
* Legacy 画面: ViewBinding (XML) でカウンター表示
* **バーコードスキャン機能**: ハンディターミナルでのバーコード読み取り対応
  - キーボード入力型スキャナ対応 (連続入力+Enter検出)
  - DataWedge等のBroadcast Intent対応 (Symbol, Honeywell等)
  - テスト用のシミュレーション機能
  - スキャン結果の履歴表示とクリア機能

### ディレクトリ構成 (抜粋)
```
settings.gradle.kts
build.gradle.kts (ルート)
gradle.properties
app/
	build.gradle.kts
	src/main/
		AndroidManifest.xml (バーコードIntent受信設定含む)
		java/com/example/easyapp/
			MainActivity.kt (Compose + バーコードスキャン)
			LegacyActivity.kt (XML + ViewBinding + バーコードスキャン)
			BarcodeAdapter.kt (RecyclerView用アダプター)
			scanner/
				BarcodeScanner.kt (統合バーコードスキャナクラス)
		res/layout/activity_legacy.xml (更新済み)
		res/values/strings.xml, themes.xml, colors.xml
```

## クローン & 実行 (Android Studio)
1. リポジトリを取得
2. Android Studio で `Open` → プロジェクトルートを選択
3. Gradle Sync 完了を待つ
4. AVD (または実機) を選択し Run ▶

## コマンドラインビルド
ローカルに **公式の完全な Gradle Wrapper** を生成していない状態なので、初回は Android Studio で `gradlew wrapper` を実行し正式スクリプトを再生成してください。(本リポジトリ同梱の `gradlew` は簡略化版)

生成後:
```
./gradlew assembleDebug
```
成果物: `app/build/outputs/apk/debug/app-debug.apk`

## 依存バージョン (抜粋)
* Android Gradle Plugin: 7.4.2 (CI環境対応バージョン)
* Kotlin: 1.8.20
* Compose BOM: 2023.05.01
* minSdk: 24 / targetSdk: 34

## バーコードスキャン機能詳細

### 対応スキャナタイプ
1. **キーボード入力型**: スキャン結果を文字列+Enterキーとして送信
2. **DataWedge系**: Symbol/Zebra端末のBroadcast Intent
3. **メーカー独自系**: Honeywell等のIntent配信

### 使用方法
- **Compose画面**: テストスキャンボタンでシミュレーション可能
- **Legacy画面**: RecyclerViewでスキャン履歴を表示
- **実機での動作**: バーコードをスキャンすると自動でリストに追加

### 実装クラス概要
- `BarcodeScanner`: 統合スキャナクラス (LifecycleObserver対応)
- `MainActivity`: Compose UI + バーコード履歴表示
- `LegacyActivity`: ViewBinding + RecyclerView履歴表示
- `BarcodeAdapter`: リスト表示用アダプター

## 実機 (ハンディターミナル) デプロイ手順メモ
1. 端末で開発者モード & USB デバッグ有効
2. `adb devices` で認識確認
3. Android Studio から Run (または `adb install -r app-debug.apk`)
4. バーコードスキャナがキーボード入力型なら、フォーカス中の TextField / EditText へ文字列 + Enter が飛ぶことが多い
5. メーカー独自サービス (例: DataWedge 等) 利用時は Broadcast / Intent / ContentProvider の仕様書を参照

### バーコードスキャナ設定例
- **Symbol/Zebra端末**: DataWedgeでIntent出力を有効化
- **Honeywell端末**: ScanWedgeでBroadcast送信を設定
- **一般的な設定**: キーボード模擬モードで接尾語にEnter設定

## 今後の拡張アイデア
* スキャン結果受信用の共通インタフェース層追加
* オフラインキャッシュ(Room) + 同期ワーカー(WorkManager)
* 画面サイズ差異対応 (Smallest width qualifier など)
* CI: GitHub Actions で `./gradlew lint ktlintCheck test assembleDebug`
* **在庫管理**: スキャンしたバーコードを商品情報と連携
* **データ出力**: CSV/JSON形式でのエクスポート機能

## トラブルシューティング

### ビルドエラー
- Android Gradle Plugin バージョンがCI環境で利用できない場合があります
- Android Studio で最初にプロジェクトを開いて同期することを推奨します

### バーコードスキャナが動作しない場合
1. メーカードキュメントでIntent配信設定を確認
2. logcatで `BarcodeScanner` タグのログを確認
3. テストスキャンボタンで基本動作を確認

### ライセンス
学習目的のサンプル。必要に応じて追記してください。

---
質問や追加要望があれば Issue / Chat でどうぞ。
# easy_app