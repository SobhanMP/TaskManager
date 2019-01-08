@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  TaskManager startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and TASK_MANAGER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\TaskManager.jar;%APP_HOME%\lib\jaxws-api-2.3.1.jar;%APP_HOME%\lib\jaxb-api-2.2.4.jar;%APP_HOME%\lib\javax.persistence-api-2.2.jar;%APP_HOME%\lib\spring-web-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-autoconfigure-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-boot-2.1.1.RELEASE.jar;%APP_HOME%\lib\spring-data-jpa-2.1.3.RELEASE.jar;%APP_HOME%\lib\mysql-connector-java-8.0.13.jar;%APP_HOME%\lib\spring-context-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-data-commons-2.1.3.RELEASE.jar;%APP_HOME%\lib\spring-orm-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-aop-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-jdbc-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-tx-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-beans-5.1.3.RELEASE.jar;%APP_HOME%\lib\stax-api-1.0-2.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\spring-expression-5.1.3.RELEASE.jar;%APP_HOME%\lib\spring-core-5.1.3.RELEASE.jar;%APP_HOME%\lib\javax.xml.soap-api-1.4.0.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\aspectjrt-1.9.2.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\spring-jcl-5.1.3.RELEASE.jar

@rem Execute TaskManager
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %TASK_MANAGER_OPTS%  -classpath "%CLASSPATH%" main.java.application.Application %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable TASK_MANAGER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%TASK_MANAGER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
