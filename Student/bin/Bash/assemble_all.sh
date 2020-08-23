#!/bin/bash

#
# Run CPRL Assembler for all ".asm" files in the current directory
#

# set config environment variables
source cprl_config.sh

for file in *.asm
do
   java -ea edu.citadel.cvm.assembler.AssemblerKt $file
done
