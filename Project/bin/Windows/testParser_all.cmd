@echo off

rem
rem Run CPRL TestParser on all ".cprl" files in the current directory
rem

rem set config environment variables locally
setlocal
call cprl_config.cmd

for %%f in (*.cprl) do java -ea test.cprl.TestParserKt %%f

rem restore settings
endlocal

