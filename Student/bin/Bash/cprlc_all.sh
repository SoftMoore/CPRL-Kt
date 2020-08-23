#!/bin/bash

#
# Run CPRL compiler for all ".cprl" files in the current directory
#

# set config environment variables
source cprl_config.sh

for file in *.cprl
do
   java -ea edu.citadel.cprl.CompilerKt $file
done
