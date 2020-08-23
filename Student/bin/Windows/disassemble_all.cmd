@echo off

rem
rem Run CVM disassembler on all ".obj" files in the current directory
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

for %%f in (*.obj) do java -ea edu.citadel.cvm.DisassemblerKt %%f

rem restore settings
endlocal
