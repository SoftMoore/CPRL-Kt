#!/bin/bash

#
# Run SEL interpreter
#

# set config environment variables
source cprl_config.sh

# Update CLASSPATH for SEL
SEL_HOME=$CLASSES_HOME/SEL
export CLASSPATH=$SEL_HOME:$CLASSPATH

java -ea edu.citadel.sel.InterpreterKt $1
