@echo off

rem
rem Run CPRL TestScanner on the specified file
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

java -ea test.cprl.TestScannerKt %1

rem restore settings
endlocal
