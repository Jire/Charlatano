@echo off
cd /d "%~dp0"
title Xena

set bat="./build/install/Charlatano/bin/Charlatano.bat"

:loop
if exist %bat% (
    call %bat%
    pause
) else (
    call build.bat
    cls
    title Charlatano
    goto loop
)