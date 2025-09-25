@ECHO OFF
SET DIR=%~dp0
SET JAR=%DIR%gradle\wrapper\gradle-wrapper.jar
IF NOT EXIST %JAR% (
  ECHO [WARN] gradle-wrapper.jar が存在しません。公式 wrapper を再生成してください: gradlew.bat wrapper
  gradle %*
  EXIT /B %ERRORLEVEL%
)
SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
"%JAVA_EXE%" -jar "%JAR%" %*
