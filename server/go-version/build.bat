@echo off
cd ./impl-score-app-server
call go build ./main/run-server.go
cd ..