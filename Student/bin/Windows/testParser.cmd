@echo off

rem
rem Run CPRL TestParser on a single ".cprl" file
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

java -ea test.cprl.TestParserKt %1

rem restore settings
endlocal
