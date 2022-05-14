# CPRL-Kt
This repository contains handouts, PowerPoint slides, and Kotlin source files to accompany the book *Introduction to Compiler Design: An Object-Oriented Approach Using Kotlin&trade;* (see <a href="https://www.amazon.com/Introduction-Compiler-Design-Object-Oriented-Approach/dp/1734139153/">https://www.amazon.com/Introduction-Compiler-Design-Object-Oriented-Approach/dp/1734139153/</a>).  The book discusses the implementation of a compiler for a relatively small programming language named CPRL (for Compiler PRoject Language), which was designed for teaching basics of compiler design.  Appendix A from the book is provided below.


## Appendix A <br>The Compiler Project

There are several general approaches for a compiler project in an academic course on compiler construction. One approach is to give detailed explanations and implementations about how to write a compiler for one source language but then concentrate the project around writing a compiler for a different source language. Another approach is to give explanations and partial implementations about how to write a compiler for a particular source language and then to concentrate the project around finishing the incomplete work and possibly extending the source language or targeting a different computer architecture. This book uses the latter approach.

The overall project of developing a compiler for CPRL is divided into 9 smaller projects as described below. For most of the projects, the book web site has lots of both complete and skeletal code to help you get started plus CPRL test programs that can be used to check your work. For each project you should test your compiler with both correct and incorrect CPRL programs as described in the projects below.

## Organizational Structure of the Compiler Project

As described below, the compiler project is organized into three separate organizational units corresponding to three Eclipse projects or three IntelliJ IDEA modules, with explicit build-path dependencies among the projects/modules &ndash; 1 is independent of the others, 2 depends only on 1, and 3 depends on both 1 and 2. These three organizational units correspond to three Java modules as defined in Java version 9 or later, and the Java module definitions, as defined in `module-info.java` files, are given below to make the dependencies explicit. Note that you are not required to use Java modules for the compiler project. Traditional classpath dependencies or IDE-specific dependencies among the separate organizational units will work just fine.

While it is possible to place all functions and classes in one Eclipse project or IntelliJ IDEA module, I recommend the following structure since it separates the architecture in a way that makes the dependencies transparent. If you are using an IDE other than Eclipse or IntelliJ IDEA, there will be a similar way to organize the compiler project into different organizational units within that IDE.

1. Module `edu.citadel.compiler` (`Compiler`) contains classes that are not directly tied to the CPRL programming language and therefore are useful on any compiler-related project. Examples include classes such as `Position` and `Source` defined in package `edu.citadel.compiler` plus utility classes (objects) such as `ByteUtil` and `CharUtil` defined in package `edu.citadel.compiler.util`. There is also a package named `test.compiler` that contains a couple of test programs for the principal classes in the module. This module has no dependencies on other modules. Its Java module definition is as follows:

````java
   module edu.citadel.compiler
     {
       exports edu.citadel.compiler;
       exports edu.citadel.compiler.util;
       requires kotlin.stdlib;
     }
````

2. Module `edu.citadel.cvm` (`CVM`) contains classes that implement the CVM, the virtual machine (emulator) for CPRL. It also contains classes that implement both a CVM assembler and a CVM disassembler. This module has a dependency on the `Compiler` module as described in item 1 above. Its Java module definition is as follows:

````java
   module edu.citadel.cvm
     {
       exports edu.citadel.cvm;
       exports edu.citadel.cvm.assembler;
       exports edu.citadel.cvm.assembler.ast;
       requires kotlin.stdlib;
       requires transitive edu.citadel.compiler;
     }
````

3. Module `edu.citadel.cprl` (`CPRL`) contains the classes that implement the CPRL compiler. Complete source code is provided for the other two modules described above, but only portions of the source code are provided for this module. **Although students will need to refer occasionally to the other two modules in order to understand the role their classes play in developing the CPRL compiler, all new development will take place only in this module.** This module has a dependency on modules `Compiler` and `CVM` as described in items 1 and 2 above. Its Java module definition is as follows:

````java
   module edu.citadel.cprl
     {
       exports edu.citadel.cprl;
       exports edu.citadel.cprl.ast;
       requires edu.citadel.cvm;
       requires kotlin.stdlib;
       requires transitive edu.citadel.compiler;
     }
````
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Since abstract syntax trees are not introduced until Project 4 below, the line

````java
   exports edu.citadel.cprl.ast;
````

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;can be commented out for Projects 0-3 as described below.

## Project 0: Getting Started

- This is not a real project but more of an initialization of your working environment for the compiler project. Download files `bin.zip`, `doc.zip`, `examples.zip`, `src-Compiler.zip`, and `src-CVM.zip` from the book&rsquo;s web site and unzip them into a directory that you will use for your compiler implementation. Zip files with names beginning &ldquo;`src…`&rdquo; contain Kotlin source files for the compiler project. Unzip the `src…` files and import them into your preferred IDE for Kotlin (e.g., Eclipse).

- File `bin.zip` contains sample Bash shell scripts and Windows command scripts for running and testing various stages of the compiler. When unzipped, bin.zip will create two subdirectories named &ldquo;`Bash`&rdquo; and &ldquo;`Windows`&rdquo; that contain the Bash and Windows script files, respectively. For each Windows &ldquo;`.cmd`&rdquo; file there is a corresponding Bash &ldquo;`.sh`&rdquo; file; e.g., `cprlc.cmd` and `cprlc.sh`. Pick the collection of script files for your programming environment.

 As an example of the contents of `bin.zip`, there is a script `cprlc.cmd` that will run the CPRL compiler on a single source file whose name is entered via standard input, and there is also a script `cprlc_all.cmd` that will run the compiler on all CPRL source files in the current working directory. Similarly, there are two scripts `assemble.cmd` and `assemble_all.cmd` for running the assembler and two scripts `disassemble.cmd` and `disassemble_all.cmd` for running the disassembler. Scripts with the &ldquo;`_all`&rdquo; suffix are useful for testing the compiler against collections of correct and/or incorrect programs. There is a script `cprl.cmd` for running a single compiled CPRL program on the CVM, and there are two scripts `testCorrect.cmd` and `testCorrect_all.cmd` that can be used for testing correct CPRL programs and comparing the output with expected output. Additionally, there are script files for testing the scanner and parser in the earlier projects.

 There are two important steps for using these script files. First, you will need to edit your `PATH` environment variable to place the directory containing the script files in your path so that the operating system can find them when you enter their names on the command line. And second, you will need to edit the file `cprl_config.cmd` (or `cprl_config.sh`) so that your `CLASSPATH` environment variable &ldquo;points to&rdquo; the directories containing the Kotlin class files for your project. Most of the other script files use `cprl_config.cmd` to set the class path appropriately.  For example, my personal setup for Kotlin uses an IntelliJ IDEA project workspace with three Kotlin modules in three separate directories named `Compiler`, `CPRL`, and `CVM`, and all Kotlin class files are in corresponding subdirectories of `out\production`, the default for IntelliJ IDEA.  When `cprl_config.cmd` is executed, it sets CLASSPATH to include the three directories containing the classes.

- File `doc.zip` contains dokka-generated HTML files (analogous to Javadoc files for Java) for the complete CPRL compiler as implemented by the author. These can be useful as a reference while working on the remaining projects.

- File `examples.zip` contains examples of correct and incorrect CPRL programs that can be used to test various parts of your compiler. There are three subdirectories in this zip file as follows:

  - Correct contains numerous correct CPRL programs. The programs are organized into four subdirectories for testing different projects as you progress though the compiler implementation. For example, there is a subdirectory containing only test programs for CPRL/0, the zero subset of CPRL (no subprograms or arrays) as outlined in Project 6 below. Testing should be performed cumulatively; i.e., you should always retest the CPRL/0 example programs when working later projects.

  - `Incorrect` contains numerous incorrect CPRL programs that will be used in testing error detection and recovery. Similar to the correct programs, these programs are organized into three subdirectories for testing different projects as you progress through the compiler implementation.

  - `ScannerTests` contains both correct and incorrect files that can be used for testing your scanner as describe in Project 1 below. These are not necessarily complete CPRL programs. For example, one of the files contains every valid symbol in CPRL including all reserved words, operators, and numerous user-defined identifiers and literals.

 You are strongly encouraged to develop additional test programs as you work though the remaining projects described below.

- File `src-Compiler.zip` contains the source code for module `Compiler` as described above. These classes are used by the other two modules, and they are potentially reusable on other compiler projects. All of the classes in this zip file are complete and require no additional work for use on the compiler project.

- File `src-CVM.zip` contains a complete implementation of `CVM`, the virtual machine (emulator) that will be used on subsequent projects to run CPRL programs. It also contains a complete implementation of a disassembler and a complete implementation of an assembler for `CVM`. You will run the assembler on assembly language files generated by your compiler to create machine code files that can be executed on the `CVM`. These classes are described in module `CVM` above. All of the classes in this zip file are complete and require no additional work for use on the compiler project.

## Project 1: Scanner

- Using the concepts from Chapter 5, implement a scanner for CPRL.

- Download file `src-Scanner.zip` from the book&rsquo;s web site, unzip the files, and import them into your preferred IDE. File `src-Scanner.zip` contains three classes in package `edu.citadel.cprl`. It has complete implementations for classes `Symbol` and `Token` plus a partial implementation of class `Scanner`. File `src-Scanner.zip` also contains a test driver that can be used together with the `testScanner.cmd` (or `testScanner.sh`) script file to &ldquo;wrap&rdquo; your scanner and run it against the example test files.

- **Complete the implementation for class `Scanner`.**

- Test your scanner with the scanner-specific test files and all correct CPRL examples.

## Project 2: Language Recognition

- Using the concepts from Chapter 6, implement a parser that performs language recognition for the full CPRL language (not just the zero subset) based on the language definition in Appendix C and the context-free grammar in Appendix D.

- Download file `src-ParserV1.zip` from the book&rsquo;s web site, unzip the files, and import them into your preferred IDE. File `src-ParserV1.zip` contains two classes in package `edu.citadel.cprl`, a complete implementation for class `IdTable` as described in Chapter 6 and a partial implementation of class `Parser` that performs only language recognition. File `src-ParserV1.zip` also contains a test driver that can be used together with script files `testParser.cmd` (or `testParser.sh`) and `testParser_all.cmd` (`testParser_all.sh`) to &ldquo;wrap&rdquo; both your scanner and parser together and run them against the example test files. The zip file also contains a text file showing the results that you should expect when running this version of the parser against the incorrect test examples.

- **Complete the implementation for class `Parser`.**

- Do not implement error recovery for this project; i.e., when an error is encountered, simply report the error and exit compilation. Follow the examples for the parser methods with complete implementations.

- Test with all correct and incorrect examples. At this point the parser should accept all correct programs and reject all incorrect programs **except** those with type errors and/or miscellaneous errors.  Detection of type errors and miscellaneous errors will be implemented in subsequent projects.  Use the text file showing expected results as a guide.

## Project 3: Error Recovery

- Using the concepts from Chapter 7, add error recovery to your parser.

- Download file `src-ParserV2.zip` from the book&rsquo;s web site and unzip the files. File `src-ParserV2.zip` contains only one class in package `edu.citadel.cprl`, a partial implementation of class `Parser` that demonstrates how to add error recovery to the parser methods. Do **not** import this class into your IDE since you already have an implementation for class `Parser`. Instead, use the class provided in this download as a guide to manually edit your existing parser in order to add error recovery. Use the test driver and script files from the previous project to run your parser against the example test programs. The zip file also contains a text file showing the results that you should expect when running this version of the parser against the incorrect test examples.

- **Edit your parser from the previous project to add error recovery. (Be sure to delete method `exit()` from your parser.)**

- Test with all correct and incorrect examples. At this point the parser should accept and reject exactly the same example programs as for the previous project, but this time your parser should report more than one error for some of the incorrect programs. Use the text file showing expected results as a guide.

## Project 4: Abstract Syntax Trees

- Using the concepts from Chapter 8, add generation of abstract syntax trees to your parser. All parsing methods should return AST objects or lists of AST objects. From now on we will start referring to our implementation a &ldquo;compiler&rdquo; even though it doesn&rsquo;t yet generate code.

- Download file `src-ParserV3.zip` from the book&rsquo;s web site and unzip the files. File `src-ParserV3.zip` contains full or partial implementations of 40 AST classes in package `edu.citadel.cprl.ast`. Approximately half of the AST classes are implemented completely, while the remaining AST classes have only partial implementations. You should import the AST classes into your IDE and implement any unimplemented constructors or &ldquo;`get`&rdquo; methods. For now use empty bodies for methods `checkConstraints()` and `emit()` in the AST classes that are not fully implemented.

  File `src-ParserV3.zip` contains full or partial implementations of several classes in package `edu.citadel.cprl` as follows:

  - A complete implementation for class `Compiler`. You will start using class `Compiler` in the next project. It is included now for convenience since `src-ParserV3.zip` is the last zip file you will need to download.

  - Complete implementations for classes `LoopContext` and `SubprogramContext`. As described in Chapter 8, these classes are used to track entry to and exit from loops and subprograms. Use these classes to check `exit` and `return` statements.

  - A complete implementation for class `IdTable`. As described in Chapter 8, `IdTable` now stores references to an identifier&rsquo;s declaration. This class replaces the previous implementation of `IdTable` that we have been using in the previous two projects. Use the complete version of `IdTable` to check for declaration and scope errors. With this new implementation for `IdTable` your parser will now be able to detect additional scope errors when we implement subprograms in Project 7.

  - A complete implementation of a simple enum class `ScopeLevel` with two constants `PROGRAM` and `SUBPROGRAM` as described in Chapter 8. The scope level is used to keep track of the level at which identifiers were declared.

  - A complete implementation for class `Type` and a partial implementation for its subclass `ArrayType`. You should study these two classes and then complete the implementation for `ArrayType`, which is straightforward.

  - A partial implementation for class `Parser`. As described in Chapter 8, the important parsing methods now return AST objects or a lists of AST objects. Do **not** import this class into your IDE since you already have an implementation for class Parser. Instead, use the class provided in this download as a guide to manually edit your existing parser in order to add generation of AST classes.

- **Implement the missing constructors in the AST classes, complete the implementation of class `ArrayType`, and edit your parser from the previous project to add generation of AST classes or lists of AST classes.**

- Test your parser with all correct and incorrect examples. File `src-ParserV3.zip` contains a text file showing the results that you should expect when running this version of the parser against the incorrect test examples. The major differences in test results between versions 2 and 3 of your parser are that version 3 should also detect `exit` statements that are not nested within loops and `return` statements that are not nested within subprograms.

## Project 5: Constraint Analysis for CPRL/0

- **Using the concepts from Chapter 9, implement `checkConstraints()' methods in the AST classes to perform full constraint analysis for the CPRL/0 subset (everything except subprograms and arrays).** In addition to the syntax and scope errors previously detected by your compiler, your compiler should also detect all type and miscellaneous errors for the CPRL/0 subset.

- Test with all correct and incorrect CPRL/0 examples. Henceforth you will use `Compiler` rather than `TestParser` to test your implementation with the CPRL examples. To invoke the compiler, use scripts `cprlc.cmd`/`cprlc.sh` or `cprlc_all.cmd`/`cprlc_all.sh`. At this point the compiler should accept all legal CPRL/0 programs and reject all illegal CPRL/0 programs. The examples directory contains a text file showing the expected results.

## Project 6: Code Generation for CPRL/0

- **Using the concepts from Chapter 11, implement `emit()` methods in the AST classes to perform code generation for CPRL/0.** At this point you are actually generating assembly language for the CVM.

- Use the assembler provided in file `src-CVM.zip` (see Project 0 above) and script files `assemble.cmd` (`assemble.sh`) and `assemble_all.cmd` (`assemble_all.sh`) to generate machine code for all correct CPRL/0 examples.

- Use script files `cprl.cmd`/`cprl.sh`, `testCorrect.cmd`/`testCorrect.sh`, and `testCorrect_all.cmd`/`testCorrect_all.sh` to run and test all correct CPRL/0 examples.

## Project 7: Subprograms

- **Using the concepts from Chapter 13, add constraint analysis and code generation for subprograms.**

- Test with all correct and incorrect subprogram examples. You should also retest all correct and incorrect CPPRL/0 examples.

## Project 8: Arrays

- **Using the concepts from Chapter 14, add constraint analysis and code generation for arrays.** Completion of this project results in the final version of your compiler.

- Correct any remaining errors.

- Test with all correct and incorrect examples. Run all generated object code files on CVM to ensure that code is being generated properly.

