@echo off

if "%OS%" == "Windows_NT" setlocal
rem ---------------------------------------------------------------------------
rem Start/Stop Script
rem

if not "%JRE_HOME%" == "" goto gotJreHome
if not "%JAVA_HOME%" == "" goto gotJavaHome
echo Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
echo At least one of these environment variable is needed to run this program
goto exit

:needJavaHome
rem Check if we have a usable JDK
if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if not exist "%JAVA_HOME%\bin\javaw.exe" goto noJavaHome
set "JRE_HOME=%JAVA_HOME%"
goto okJava

:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly.
goto exit

:gotJavaHome
rem No JRE given, use JAVA_HOME as JRE_HOME
set "JRE_HOME=%JAVA_HOME%"

:gotJreHome
rem Check if we have a usable JRE
if not exist "%JRE_HOME%\bin\java.exe" goto noJreHome
if not exist "%JRE_HOME%\bin\javaw.exe" goto noJreHome
goto okJava

:noJreHome
rem Needed at least a JRE
echo The JRE_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto exit

:okJava
rem Set standard command for invoking Java.
rem Also note the quoting as JAVA_HOME may contain spaces.
set _RUNJAVA="%JRE_HOME%\bin\java"

echo %_RUNJAVA%

rem Deal with classpath

set "M2=%HOMEPATH%\.m2\repository"

set "CLASSPATH=%M2%\asia\redact\bracket\properties\bracket-properties\1.3.6\bracket-properties-1.3.6.jar"
set "CLASSPATH=%CLASSPATH%;%M2%\com\fasterxml\jackson\core\jackson-core\2.2.3\jackson-core-2.2.3.jar"
set "CLASSPATH=%CLASSPATH%;%M2%\com\fasterxml\jackson\core\jackson-databind\2.2.3\jackson-databind-2.2.3.jar"
set "CLASSPATH=%CLASSPATH%;%M2%\com\fasterxml\jackson\core\jackson-annotations\2.2.3\jackson-annotations-2.2.3.jar"
set "CLASSPATH=%CLASSPATH%;%M2%\com\cryptoregistry\buttermilk-core\1.0.0\buttermilk-core-1.0.0.jar"
set "CLASSPATH=%CLASSPATH%;%M2%\net\iharder\base64\2.3.8\base64-2.3.8.jar"
set "CLASSPATH=%CLASSPATH%;%M2%\com\cryptoregistry\utility-apps\1.0.0\utility-apps-1.0.0.jar"

rem echo %CLASSPATH%

set MAINCLASS=com.cryptoregistry.utility.app.ObfuscateApp
set "JAVA_OPTS="

%_RUNJAVA% %JAVA_OPTS% -classpath "%CLASSPATH%" %MAINCLASS% %*

:end
