#!/bin/bash

#
# Configuration settings for the CPRL compiler project.
#
# These settings assume an IntelliJ IDEA project with three separate modules named
# Compiler, CPRL, and CVM.  Class files are placed in an "out\production" directory
# The project directory hierarchy is as follows:
#  PROJECTS_HOME
#     - Compiler
#     - CPRL
#     - CVM

# set PROJECT_HOME to the directory for your IntelliJ projects
PROJECTS_HOME=/mnt/c/JMooreMACS/Teaching/Compiler/Projects/

# set CLASSES_HOME to the directory name used for compiled Java classes
CLASSES_HOME=$PROJECTS_HOME/out/production

COMPILER_HOME=$CLASSES_HOME/Compiler
CPRL_HOME=$CLASSES_HOME/CPRL
CVM_HOME=$CLASSES_HOME/CVM

# set KT_LIB_HOME to the directory for the Kotlin jar files
KT_LIB_HOME=/mnt/c/Users/jmoor/AppData/Roaming/JetBrains/IdeaIC2020.2/plugins/Kotlin/kotlinc/lib

# Add all project-related class directories to the classpath.
export CLASSPATH="$COMPILER_HOME:$CPRL_HOME:$CVM_HOME:$KT_LIB_HOME/kotlin-stdlib.jar:$CLASSPATH"
