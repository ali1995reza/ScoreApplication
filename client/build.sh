#!/bin/sh

cd client-app
mvn clean compile assembly:single
cd ..
