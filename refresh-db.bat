@echo off
echo [INFO] Re-create the schema and provision the sample data.
 

call mvn antrun:run -Prefresh-db
 
pause