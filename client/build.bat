@echo off
cd client-app
call mvn clean compile assembly:single
IF %ERRORLEVEL% NEQ 0 ( 
    cd ..
    exit 22
)
cd ..
