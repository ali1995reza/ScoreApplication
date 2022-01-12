#!/bin/sh

cd score-app
mvn clean install
if [ $? -ne 0 ]; then
    cd ..
    exit 22
fi
cd ../repo-impl
mvn clean install
if [ $? -ne 0 ]; then
    cd ..
    exit 22
fi
cd ../service-impl
mvn clean install
if [ $? -ne 0 ]; then
    cd ..
    exit 22
fi
cd ../server-impl
mvn clean compile assembly:single
if [ $? -ne 0 ]; then
    cd ..
    exit 22
fi
cd ..
