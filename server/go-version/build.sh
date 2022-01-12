#!/bin/sh

cd ./impl-score-app-server
go build ./main/run-server.go
if [ $? -ne 0 ]; then
    cd ..
    exit 22
fi
cd ..
