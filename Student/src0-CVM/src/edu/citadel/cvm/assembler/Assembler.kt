package edu.citadel.cvm.assembler


import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Source
import edu.citadel.cvm.assembler.ast.AST
import edu.citadel.cvm.assembler.ast.Instruction

import java.io.*
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess


private const val SUFFIX  = ".asm"
private const val FAILURE = -1


fun main(args : Array<String>)
  {
    // check args
    if (args.isEmpty() || args.size > 2)
        printUsageMessageAndExit()

    var fileName = args[0]

    if (args.size == 2)
      {
        fileName = args[1]

        if (args[0] == "-opt:off")
            Assembler.OPTIMIZE = false
        else if (args[0] != "-opt:on")
            printUsageMessageAndExit()
      }

    var sourceFile = File(fileName)

    if (!sourceFile.isFile)
      {
        // see if we can find the file by appending the suffix
        val index = fileName.lastIndexOf('.')

        if (index < 0 || fileName.substring(index) != SUFFIX)
          {
            fileName += SUFFIX
            sourceFile = File(fileName)

            if (!sourceFile.isFile)
              {
                System.err.println("*** File $fileName not found ***")
                exitProcess(FAILURE)
              }
          }
        else
          {
            // don't try to append the suffix
            System.err.println("*** File $fileName not found ***")
            exitProcess(FAILURE)
          }
      }

    val assembler = Assembler(sourceFile)
    assembler.assemble()

    println()
  }


/**
 * This method is useful for debugging.
 *
 * @param instructions the list of instructions to print
 */
private fun printInstructions(instructions : List<Instruction>)
  {
    println("There are " + instructions.size + " instructions")
    for (instruction in instructions)
        println(instruction)
    println()
  }


private fun printProgressMessage(message : String)
  {
    println(message)
  }


private fun printUsageMessageAndExit()
  {
    println("Usage: java Assembler <option> <source file>")
    println("where the option is omitted or is one of the following:")
    println("-opt:off   Turns off all assembler optimizations")
    println("-opt:on    Turns on all assembler optimizations (default)")
    println()
    exitProcess(0)
  }


/**
 * Assembler for the CPRL Virtual Machine.
 *
 * @constructor Construct an assembler with the specified source file.
 */
class Assembler(private val sourceFile : File)
  {
    /**
     * Assembles the source file.  If there are no errors in the source file,
     * the object code is placed in a file with the same base file name as
     * the source file but with a ".obj" suffix.
     *
     * @throws IOException if there are problems reading the source file
     *                     or writing to the target file.
     */
    fun assemble()
      {
        val reader  = FileReader(sourceFile, StandardCharsets.UTF_8)
        val source  = Source(reader)
        val scanner = Scanner(source)
        val parser  = Parser(scanner)

        printProgressMessage("Starting assembly for ${sourceFile.name} ...")

        // parse source file
        val prog = parser.parseProgram()

        // optimize
        if (!ErrorHandler.errorsExist() && OPTIMIZE)
          {
            printProgressMessage("Performing optimizations...")
            prog.optimize()
          }

        if (DEBUG)
          {
            println("Program after checking constraints")
            printInstructions(prog.getInstructions())
          }

        // set addresses
        if (!ErrorHandler.errorsExist())
          {
            printProgressMessage("Setting memory addresses...")
            prog.setAddresses()
          }

        // check constraints
        if (!ErrorHandler.errorsExist())
          {
            printProgressMessage("Checking constraints...")
            prog.checkConstraints()
          }

        // generate code
        if (!ErrorHandler.errorsExist())
          {
            printProgressMessage("Generating code...")
            AST.outputStream = getTargetOutputStream(sourceFile)

            // no error recovery from errors detected during code generation
            prog.emit()
          }

        if (ErrorHandler.errorsExist())
            printProgressMessage("*** Errors detected -- assembly terminated. ***")
        else
            printProgressMessage("Assembly complete.")
      }


    private fun getTargetOutputStream(sourceFile : File): OutputStream?
      {
        // get source file name minus the suffix
        var baseName = sourceFile.name
        val suffixIndex = baseName.lastIndexOf(SUFFIX)
        if (suffixIndex > 0)
            baseName = sourceFile.name.substring(0, suffixIndex)

        val targetFileName = "$baseName.obj"

        try
          {
            val targetFile = File(sourceFile.parent, targetFileName)
            return FileOutputStream(targetFile)
          }
        catch (e : IOException)
          {
            e.printStackTrace()
            exitProcess(FAILURE)
          }
      }


    companion object
      {
        private const val DEBUG = false
        var OPTIMIZE = true
      }
  }
