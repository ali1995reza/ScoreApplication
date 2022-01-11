@echo off
cd client-app
call mvn clean compile assembly:single
cd ..
