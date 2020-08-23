#!/bin/bash

#
# Run CVM disassembler on all ".obj" files in the current directory
#

# set config environment variables
source cprl_config.sh

for file in *.obj
do
   java -ea edu.citadel.cvm.DisassemblerKt $file
done

