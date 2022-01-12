#!/bin/sh

cd client-app
mvn clean compile assembly:single
if [ $? -ne 0 ]; then
    cd ..
    exit 22
fi
cd ..
