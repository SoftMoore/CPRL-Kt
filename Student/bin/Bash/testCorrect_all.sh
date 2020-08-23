#!/bin/bash

#
# Run testCorrect on all ".obj" files in the current directory
#

echo Deleting all \".asm\" and \".obj\" files
echo
rm -f *.asm
rm -f *.obj

echo Recompiling all \".cprl\" files
echo
cprlc_all.sh > /dev/null

echo Reasembling all \".asm\" files
echo
assemble_all.sh > /dev/null

for file in *.obj
do
   filename=$(basename $file)
   filename="${filename%.*}"
   testCorrect.sh $filename
done
