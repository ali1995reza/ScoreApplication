#!/bin/sh

cd score-app
mvn clean install
cd ../repo-impl
mvn clean install
cd ../service-impl
mvn clean install
cd ../server-impl
mvn clean compile assembly:single
cd ..
