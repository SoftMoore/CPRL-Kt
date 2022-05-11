#!/bin/bash

#
# Run CPRL Assembler on a single ".asm" file
#

# set config environment variables
source cprl_config.sh

# The assembler permits more that one command-line
# argument to handle the -opt:off/-opt:on switch.
java -ea edu.citadel.cvm.assembler.AssemblerKt $*
