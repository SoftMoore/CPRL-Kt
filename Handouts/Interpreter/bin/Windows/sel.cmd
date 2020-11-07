@echo off

rem
rem Run SEL interpreter
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

rem Update CLASSPATH for SEL
set SEL_HOME=%CLASSES_HOME%\SEL
set CLASSPATH=%SEL_HOME%;%CLASSPATH%

java -ea edu.citadel.sel.InterpreterKt %1

rem restore settings
endlocal
