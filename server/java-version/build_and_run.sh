#!/bin/sh

./build.sh
if [ $? -ne 0 ]; then
    cd ..
    exit 22
fi
clear
./run.sh
