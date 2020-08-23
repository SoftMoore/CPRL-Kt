package test.cprl


import edu.citadel.compiler.Source
import edu.citadel.cprl.Scanner
import edu.citadel.cprl.Parser
import edu.citadel.compiler.ErrorHandler

import java.io.*
import kotlin.system.exitProcess


private const val SUFFIX = ".cprl"
private const val FAILURE = -1


fun main(args : Array<String>)
  {
    // check args
    if (args.size != 1)
        printUsageAndExit()

    var fileName = args[0]

    lateinit var fileReader : FileReader

    printProgressMessage("Initializing...")

    try
      {
        fileReader = FileReader(fileName)
      }
    catch (e : FileNotFoundException)
      {
        // see if we can find the file by appending the suffix
        val index = fileName.lastIndexOf('.')

        if (index < 0 || fileName.substring(index) != SUFFIX)
          {
            try
              {
                fileName += SUFFIX
                fileReader = FileReader(fileName)
              }
            catch (ex : FileNotFoundException)
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

    printProgressMessage("Parsing $fileName...")

    val source  = Source(fileReader)
    val scanner = Scanner(source)
    val parser  = Parser(scanner)

    // write error messages to System.out
    ErrorHandler.setPrintWriter(PrintWriter(System.out, true))

    printProgressMessage("Starting compilation...")

    parser.parseProgram()

    if (ErrorHandler.errorsExist())
        printProgressMessage("Errors detected -- compilation terminated.")
    else
        printProgressMessage("Compilation complete.")

    println()
  }


private fun printProgressMessage(message : String)
  {
    println(message)
  }


private fun printUsageAndExit()
  {
    println("Usage: java TestParser <CPRL source file>")
    println()
    exitProcess(0)
  }
