@echo off

rem
rem Test a CPRL program for correct execution
rem Assumes that commands cprlc and assemble have already run successfully.

rem Command-line arguments are as follows:
rem - %0 : the name of this batch file
rem - %1 : the name of the CPRL object file to test

echo Testing Program %1

if not exist %1.obj (
    echo Can't find %1.obj
    echo.
    goto :eof
)

if not exist %1.out.txt (
    echo Can't find %1.out.txt
    echo.
    goto :eof
)

if exist %1.in.txt (
    call cprl %1.obj < %1.in.txt > %1.out.tmp
) else (
    call cprl %1.obj > %1.out.tmp
)

rem comparing output files
fc /n %1.out.tmp %1.out.txt
if errorlevel 1 (echo *** Test Failed ***) else (echo Test Passed)
echo.

