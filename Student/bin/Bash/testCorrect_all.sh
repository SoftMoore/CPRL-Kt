#!/bin/bash

#
# Run testCorrect on all ".obj" files in the current directory
#

echo Deleting all \".asm\" and \".obj\" files
rm -f *.asm
rm -f *.obj
echo

echo Recompiling all \".cprl\" files
cprlc_all.sh > /dev/null
echo

echo Reasembling all \".asm\" files
assemble_all.sh > /dev/null
echo

for file in *.obj
do
   filename=$(basename $file)
   filename="${filename%.*}"
   testCorrect.sh $filename
done
