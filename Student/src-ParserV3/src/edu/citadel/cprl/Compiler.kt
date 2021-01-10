package edu.citadel.cprl


import edu.citadel.compiler.CodeGenException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Source
import edu.citadel.cprl.ast.AST

import java.io.*
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess
import java.io.FileWriter

import java.io.PrintWriter





private const val SUFFIX = ".cprl"
private const val FAILURE = -1


/**
 * This function drives the compilation process.
 *
 * @param args must include the name of the CPRL source file, either the complete
 *        file name or the base file name with suffix ".cprl" omitted.
 */
fun main(args : Array<String>)
  {
    if (args.isEmpty() || args.size > 1)
        printUsageMessageAndExit()

    var fileName   = args[0]
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

    val compiler = Compiler(sourceFile)
    compiler.compile()

    println()
  }


private fun printProgressMessage(message : String)
  {
    println(message)
  }


private fun printUsageMessageAndExit()
  {
    println("Usage: java Compiler <source file>")
    println()
    exitProcess(0)
  }


/**
 * Compiler for the CPRL programming language.
 *
 * @constructor Construct a compiler with the specified source file.
 */
class Compiler(private val sourceFile : File)
  {
    /**
     * Compile the source file.  If there are no errors in the source file,
     * the object code is placed in a file with the same base file name as
     * the source file but with a ".obj" suffix.
     */
    fun compile()
      {
        val reader  = FileReader(sourceFile, StandardCharsets.UTF_8)
        val source  = Source(reader)
        val scanner = Scanner(source)
        val parser  = Parser(scanner)

        printProgressMessage("Starting compilation for ${sourceFile.name} ...")

        // parse source file
        val program = parser.parseProgram()

        // check constraints
        if (!ErrorHandler.errorsExist())
          {
            printProgressMessage("Checking constraints...")
            program.checkConstraints()
          }

        // generate code
        if (!ErrorHandler.errorsExist())
          {
            printProgressMessage("Generating code...")

            // no error recovery from errors detected during code generation
            try
              {
                AST.setPrintWriter(getTargetPrintWriter(sourceFile))
                program.emit()
              }
            catch (e : CodeGenException)
              {
                ErrorHandler.reportError(e)
              }
          }

        if (ErrorHandler.errorsExist())
            printProgressMessage("Errors detected -- compilation terminated.")
        else
            printProgressMessage("Compilation complete.")
      }


    /**
     * Returns a print writer used for writing the assembly code.  The target print
     * writer writes to a file with the same base file name as the source file but with a
     * ".asm" suffix.  Returns null if an error occurs.
     */
    private fun getTargetPrintWriter(sourceFile : File) : PrintWriter
      {
        // get source file name minus the suffix
        var baseName = sourceFile.name
        val suffixIndex = baseName.lastIndexOf(SUFFIX)
        if (suffixIndex > 0)
            baseName = sourceFile.name.substring(0, suffixIndex)

        val targetFileName = "$baseName.asm"

        try
          {
            val targetFile = File(sourceFile.parent, targetFileName)
            return PrintWriter(FileWriter(targetFile, StandardCharsets.UTF_8), true)
          }
        catch (e : IOException)
          {
            e.printStackTrace()
            exitProcess(FAILURE)
          }
      }
  }
