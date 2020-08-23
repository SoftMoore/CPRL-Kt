#!/bin/bash

#
# Run CVM disassembler on a single ".obj" file
#

# set config environment variables
source cprl_config.sh

java -ea edu.citadel.cvm.DisassemblerKt $1
