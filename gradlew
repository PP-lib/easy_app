#!/usr/bin/env sh

# Simplified Gradle Wrapper script (Unix)
##############################################################################
# NOTE: 本来は公式生成スクリプトを使用してください。ここでは学習目的の最小版です。
##############################################################################
DIR="$( cd "$( dirname "$0" )" && pwd )"
GRADLE_WRAPPER_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
  echo "[WARN] gradle-wrapper.jar が存在しません。公式 wrapper を再生成してください: ./gradlew wrapper" >&2
  exec gradle "$@"
fi
JAVA_EXEC="${JAVA_HOME:+$JAVA_HOME/bin/}java"
exec "$JAVA_EXEC" -jar "$GRADLE_WRAPPER_JAR" "$@"
