@echo off
call ./build.bat
call cls
call java -jar ./server-impl/target/score-app-java-server.jar