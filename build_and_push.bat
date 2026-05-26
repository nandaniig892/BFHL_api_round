@echo off
REM ============================================================
REM  BFHL API  –  Build, Test & Push to GitHub
REM  Run this script from inside the Bajaj__api_Final folder
REM ============================================================

echo.
echo ==============================================
echo  STEP 1: Running tests
echo ==============================================
call mvn clean test
IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Tests failed. Fix errors before pushing.
    pause
    exit /b 1
)
echo [OK] All tests passed.

echo.
echo ==============================================
echo  STEP 2: Building production JAR
echo ==============================================
call mvn clean package -DskipTests
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Build failed.
    pause
    exit /b 1
)
echo [OK] JAR built at target\bfhl-api-1.0.0.jar

echo.
echo ==============================================
echo  STEP 3: Initialising Git and pushing to GitHub
echo ==============================================

REM Make sure we are inside the project folder
git init
git remote remove origin 2>NUL
git remote add origin https://github.com/nandaniig892/BFHL_api_round.git

git add .
git commit -m "feat: BFHL qualifier API - POST /bfhl with all required fields"

REM Force-push so the remote repo is replaced with this clean state
git branch -M main
git push -u origin main --force

IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Push failed. Make sure you are logged into GitHub in Git credential manager.
    pause
    exit /b 1
)

echo.
echo ============================================================
echo  DONE!  Code is live at:
echo  https://github.com/nandaniig892/BFHL_api_round
echo ============================================================
echo.
echo  NEXT: Deploy to Railway or Render
echo  - Railway : https://railway.app  (connect GitHub repo)
echo  - Render  : https://render.com   (New Web Service → Spring Boot)
echo.
pause
