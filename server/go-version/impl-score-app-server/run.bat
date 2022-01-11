@echo off
go build .\main\run-server.go
if %ERRORLEVEL% EQU 0 run-server.exe