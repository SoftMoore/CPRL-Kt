#!/bin/bash

#
# Run CPRL TestParser on all ".cprl" files in the current directory
#

# set config environment variables
source cprl_config.sh

for file in *.cprl
do
   java -ea test.cprl.TestParserKt $file
done

