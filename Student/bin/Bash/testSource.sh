#!/bin/bash

#
# Run CPRL TestScanner on the specified file
#

# set config environment variables
source cprl_config.sh

java -ea test.compiler.TestSourceKt $1
