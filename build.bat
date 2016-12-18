@echo off
cd /d "%~dp0"
title Charlatano Builder
call gradlew installDist
echo.
pause