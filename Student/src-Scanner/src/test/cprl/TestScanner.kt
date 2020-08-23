package test.cprl


import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Source
import edu.citadel.cprl.Scanner
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token

import java.io.*
import kotlin.system.exitProcess


fun main(args : Array<String>)
  {
    try
      {
        // check args
        if (args.size != 1)
            printUsageAndExit()

        println("initializing...")
        println()

        val fileName   = args[0]
        val fileReader = FileReader(fileName)
        // write error messages to System.out
        ErrorHandler.setPrintWriter(PrintWriter(System.out, true))

        println("starting main loop...")
        println()

        val source  = Source(fileReader)
        val scanner = Scanner(source)
        var token : Token

        do
          {
            token = scanner.token
            printToken(token)
            scanner.advance()
          }
        while (token.symbol != Symbol.EOF)

        println()
        println("...done")
      }
    catch (e : Exception)
      {
        e.printStackTrace()
      }
  }

fun printToken(token : Token)
  {
    System.out.printf("line: %2d   char: %2d   token: ",
        token.position.lineNumber,
        token.position.charNumber)

    val symbol = token.symbol
    if (symbol.isReservedWord())
        print("Reserved Word -> ")
    else if (symbol == Symbol.identifier    || symbol == Symbol.intLiteral
          || symbol == Symbol.stringLiteral || symbol == Symbol.charLiteral)
        print(token.symbol.toString() + " -> ")

    println(token.text)
  }


private fun printUsageAndExit()
  {
    println("Usage: java TestScanner <test file>")
    println()
    exitProcess(0)
  }
