#!/bin/bash

#
# Test a CPRL program for correct execution.
# Assumes that commands cprlc and assemble have already run successfully.

# Command-line arguments are as follows:
# - %0 : the name of this batch file
# - %1 : the name of the CPRL object file to test

echo Testing Program $1

if [ ! -f $1.obj ]
then
    echo "Can't find $1.obj"
    echo
    exit 1
fi

if [ ! -f $1.out.txt ]
then
    echo "Can't find $1.out.txt"
    echo ""
    exit 1
fi

if [ -f $1.in.txt ]
then
    cprl.sh $1.obj < $1.in.txt > $1.out.tmp
else
    cprl.sh $1.obj > $1.out.tmp
fi

# comparing output files
echo Comparing files $1.out.tmp and $1.out.txt
diff --strip-trailing-cr $1.out.tmp $1.out.txt
if [ $? -ne 0 ]
then
    echo "*** Test Failed ***"
else
    echo "Test Passed"
fi

echo ""

