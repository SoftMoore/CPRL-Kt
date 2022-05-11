@echo off

rem
rem Run CPRL compiler for all ".cprl" files in the current directory
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

for %%f in (*.cprl) do java -ea edu.citadel.cprl.CompilerKt %%f

rem restore settings
endlocal
