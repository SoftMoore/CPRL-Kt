@echo off

rem
rem Run CPRL TestSource on the specified file
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

java -ea test.compiler.TestSourceKt %1

rem restore settings
endlocal
