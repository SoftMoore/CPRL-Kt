@echo off

rem
rem Run CVM disassembler on a single ".obj" file
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

java -ea edu.citadel.cvm.DisassemblerKt %1

rem restore settings
endlocal
