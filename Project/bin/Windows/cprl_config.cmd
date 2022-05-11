@echo off

rem
rem Configuration settings for the CPRL compiler project.
rem
rem These settings assume an IntelliJ IDEA project with three separate modules named
rem Compiler, CPRL, and CVM.  Class files are placed in an "out\production" directory
rem The project directory hierarchy is as follows:
rem  PROJECTS_HOME
rem     - Compiler
rem     - CPRL
rem     - CVM


rem set PROJECTS_HOME to the directory for your IntelliJ projects
set PROJECTS_HOME=C:\JMooreMACS\Teaching\Compiler\Projects\CPRL

rem set CLASSES_HOME to the directory name used for compiled Java classes
set CLASSES_HOME=%PROJECTS_HOME%\out\production

set COMPILER_HOME=%CLASSES_HOME%\Compiler
set CPRL_HOME=%CLASSES_HOME%\CPRL
set CVM_HOME=%CLASSES_HOME%\CVM

rem set KT_LIB_HOME to the directory for the Kotlin jar files
set KT_LIB_HOME=C:\Users\jmoor\AppData\Roaming\JetBrains\IdeaIC2020.2\plugins\Kotlin\kotlinc\lib

rem Add all project-related class directories to the classpath.
set CLASSPATH=%COMPILER_HOME%;%CPRL_HOME%;%CVM_HOME%;%KT_LIB_HOME%\kotlin-stdlib.jar;%CLASSPATH%
