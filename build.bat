@echo off
title Charlatano Builder
:top
cls
echo Press any key to start build.
pause > nul
cls
call gradlew charlatano 2>build.log
pause
goto top