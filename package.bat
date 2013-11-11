@echo off
echo [INFO] Package the war in target dir.

call mvn clean package -Dmaven.test.skip=true

pause