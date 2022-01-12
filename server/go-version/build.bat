@echo off
cd ./impl-score-app-server
call go build ./main/run-server.go
IF %ERRORLEVEL% NEQ 0 ( 
    cd ..
    exit 22
)
cd ..