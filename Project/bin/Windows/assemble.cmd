@echo off

rem
rem Run CPRL Assembler on a single ".asm" file
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

rem The assembler permits more that one command-line
rem argument to handle the -opt:off/-opt:on switch.
java -ea edu.citadel.cvm.assembler.AssemblerKt %*

rem restore settings
endlocal
