@echo off
cd /d "%~dp0"

title Charlatano 0.9.7

call set bat="./build/Charlatano 0.9.7/Start Charlatano 0.9.7.bat"

:loop
if exist %bat% (
    call %bat%
    pause
) else (
    call build.bat
    cls
    goto loop
)
