package test.cprl


import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Source
import edu.citadel.cprl.Scanner
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token

import java.io.*
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess
import java.io.PrintStream


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
        val fileReader = FileReader(fileName, StandardCharsets.UTF_8)
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
    val out = PrintStream(java.lang.System.out, true, java.nio.charset.StandardCharsets.UTF_8)
    out.printf("line: %2d   char: %2d   token: ",
        token.position.lineNumber,
        token.position.charNumber)

    val symbol = token.symbol
    if (symbol.isReservedWord())
        out.print("Reserved Word -> ")
    else if (symbol == Symbol.identifier    || symbol == Symbol.intLiteral
          || symbol == Symbol.stringLiteral || symbol == Symbol.charLiteral)
        out.print(token.symbol.toString() + " -> ")

    out.println(token.text)
  }


private fun printUsageAndExit()
  {
    println("Usage: java TestScanner <test file>")
    println()
    exitProcess(0)
  }
