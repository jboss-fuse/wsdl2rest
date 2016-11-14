@echo off

@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT" setlocal

if "%OS%" == "Windows_NT" (
  set "DIRNAME=%~dp0%"
) else (
  set DIRNAME=..\
)

set "HOME_DIR=%DIRNAME%..\"

pushd "%DIRNAME%.."
set "RESOLVED_JBOSS_HOME=%CD%"
popd

java ^
    -Dlog4j.configuration=file:%HOME_DIR%\config\logging.properties ^
    -jar %HOME_DIR%/lib/wsdl2rest-${project.version}.jar %*
