@echo off

rem
rem Run CPRL Assembler for all ".asm" files in the current directory
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

for %%f in (*.asm) do java -ea edu.citadel.cvm.assembler.AssemblerKt %%f

rem restore settings
endlocal
