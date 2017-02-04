@echo off
cd /d "%~dp0"

title Charlatano 0.9.6

call set bat="./build/Charlatano 0.9.6/Start Charlatano 0.9.6.bat"

:loop
if exist %bat% (
    call %bat%
    pause
) else (
    call build.bat
    cls
    goto loop
)