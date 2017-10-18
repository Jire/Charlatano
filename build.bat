@echo off
title Charlatano Builder
goto start
:top
cls
echo Press any key to start build.
pause > nul
:start
cls
call gradlew charlatano
pause
goto top
