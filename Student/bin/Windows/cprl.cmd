@echo off

rem
rem Run CPRL Virtual Machine interpreter on a single ".obj" file
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

java -ea edu.citadel.cvm.CVMKt %1

rem restore settings
endlocal
