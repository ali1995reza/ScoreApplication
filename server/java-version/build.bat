@echo off
cd score-app
call mvn clean install
IF %ERRORLEVEL% NEQ 0 ( 
    cd ..
    exit 22
)
cd ../repo-impl
call mvn clean install
IF %ERRORLEVEL% NEQ 0 ( 
    cd ..
    exit 22
)
cd ../service-impl
call mvn clean install
IF %ERRORLEVEL% NEQ 0 ( 
    cd ..
    exit 22
)
cd ../server-impl
call mvn clean compile assembly:single
IF %ERRORLEVEL% NEQ 0 ( 
    cd ..
    exit 22
)
cd ..
