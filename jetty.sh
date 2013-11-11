echo [INFO] Use maven jetty-plugin run the project.

set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m
echo MAVEN_OPTS

mvn jetty:run -Djetty.port=8080

