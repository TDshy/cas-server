@echo off
chcp 65001 >nul
setlocal EnableDelayedExpansion

echo ==========================================
echo CAS Server 7.x Build Script
echo For Tomcat 10 + JDK 17 + Spring 6
echo ==========================================
echo.

:: Check Java version
java -version 2>&1 | findstr "17\.\|21\." >nul
if errorlevel 1 (
    java -version 2>&1 | findstr "17" >nul
    if errorlevel 1 (
        echo [ERROR] JDK 17 or higher is required!
        java -version
        exit /b 1
    )
)

echo [INFO] Java version check passed.
echo.

:: Clean and package
echo [INFO] Building CAS WAR package...
echo [INFO] This may take several minutes on first run...
echo.

set MAVEN_HOME=E:\apache-maven-3.9.9
set PATH=%MAVEN_HOME%\bin;%PATH%

call mvn clean package -DskipTests %*

if errorlevel 1 (
    echo.
    echo [ERROR] Build failed!
    exit /b 1
)

echo.
echo ==========================================
echo [SUCCESS] Build completed!
echo ==========================================
echo.
echo Output: target\cas.war
echo.
echo Next steps:
echo   1. Deploy target\cas.war to Tomcat 10 webapps\
echo   2. Initialize database using db-init.sql
echo   3. Configure Tomcat 10 HTTPS connector
echo   4. Start Tomcat
echo   5. Access: https://localhost:8443/cas
echo.
echo Default test users:
echo   - casuser / casuser
echo   - admin / 123456
echo.

endlocal
