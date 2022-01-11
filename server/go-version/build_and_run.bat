@echo off
call build.bat
if %ERRORLEVEL% EQU 0 call .\impl-score-app-server\run-server.exe