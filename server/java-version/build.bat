@echo off
cd score-app
call mvn clean install
cd ../repo-impl
call mvn clean install
cd ../service-impl
call mvn clean install
cd ../server-impl
call mvn clean compile assembly:single
cd ..
