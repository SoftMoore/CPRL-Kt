@echo off

rem
rem Run testCorrect on all ".obj" files in the current directory
rem

echo Deleting all ".asm", ".obj", and ".tmp" files
del *.asm
del *.obj
del *.tmp
echo.

echo Recompiling all ".cprl files
call cprlc_all > nul
echo.

echo Reasembling all ".asm" files
call assemble_all > nul
echo.

for %%f in (*.obj) do (call testCorrect %%~nf)
