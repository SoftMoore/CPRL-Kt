#!/bin/bash

#
# Run CPRL Virtual Machine interpreter on a single ".obj" file
#

# set config environment variables
source cprl_config.sh

java -ea edu.citadel.cvm.CVMKt $1
