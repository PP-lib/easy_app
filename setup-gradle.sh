#!/bin/bash
# setup-gradle.sh
# Android開発環境セットアップスクリプト

echo "EasyApp Android Project Setup"
echo "=============================="

# Gradle Wrapperの確認と生成
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ] || [ $(stat -c%s "gradle/wrapper/gradle-wrapper.jar") -eq 0 ]; then
    echo "Gradle Wrapper JARが見つからないか、空のファイルです。"
    echo "Android Studioで開いてGradle Syncを実行してください。"
    echo ""
    echo "または、以下のコマンドでGradle Wrapperを生成できます："
    echo "  gradle wrapper --gradle-version=8.2 --distribution-type=bin"
    echo ""
    
    if command -v gradle &> /dev/null; then
        echo "システムのGradleが見つかりました。Wrapperを生成しますか？ (y/n)"
        read -p "> " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            gradle wrapper --gradle-version=8.2 --distribution-type=bin
            echo "Gradle Wrapper が生成されました。"
        fi
    else
        echo "システムにGradleがインストールされていません。"
        echo "Android Studioを使用してプロジェクトを開いてください。"
    fi
else
    echo "Gradle Wrapper JARが存在します。"
fi

# Android SDKの確認
echo ""
echo "Android SDK環境の確認："
if [ -z "$ANDROID_HOME" ]; then
    echo "⚠️  ANDROID_HOME環境変数が設定されていません。"
    echo "   Android Studioでプロジェクトを開いてください。"
else
    echo "✅ ANDROID_HOME: $ANDROID_HOME"
    if [ -d "$ANDROID_HOME/platforms" ]; then
        echo "✅ Android SDK platforms が見つかりました"
        ls "$ANDROID_HOME/platforms" | head -3
    fi
fi

# プロジェクト構成の確認
echo ""
echo "プロジェクト構成の確認："
if [ -f "app/src/main/java/com/example/easyapp/MainActivity.kt" ]; then
    echo "✅ MainActivity.kt"
fi
if [ -f "app/src/main/java/com/example/easyapp/LegacyActivity.kt" ]; then
    echo "✅ LegacyActivity.kt"
fi
if [ -f "app/src/main/java/com/example/easyapp/scanner/BarcodeScanner.kt" ]; then
    echo "✅ BarcodeScanner.kt (バーコードスキャン機能)"
fi
if [ -f "app/src/main/AndroidManifest.xml" ]; then
    echo "✅ AndroidManifest.xml"
fi

echo ""
echo "セットアップ完了！"
echo ""
echo "次のステップ："
echo "1. Android Studioでプロジェクトを開く"
echo "2. Gradle Syncを実行"
echo "3. AVD (エミュレータ) または実機を接続"
echo "4. Run ボタンでアプリを実行"
echo ""
echo "バーコードスキャン機能："
echo "- エミュレータ: テストスキャンボタンで動作確認"
echo "- 実機: 実際のバーコードスキャナで読み取り可能"