@echo off
call ./build.bat
IF %ERRORLEVEL% NEQ 0 ( 
    cd ..
    exit 22
)
call cls
call ./run.bat