package edu.citadel.sel


import edu.citadel.compiler.Source
import edu.citadel.sel.ast.Context
import edu.citadel.sel.ast.Program
import java.io.FileReader
import java.io.StringReader


fun main(args : Array<String>)
  {
    if (args.size == 1)
      {
        // run SEL program whose file name is given in the argument
        val filePath = args[0]
        val reader = FileReader(filePath)
        val source = Source(reader)
        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val context = Context()
        val program : Program = parser.parseProgram()
        println(program.interpret(context))
      }
    else
      {
        // run interactive interpreter
        var exprStr   : String? = ""
        var exprValue : Double

        println("Starting SEL interpreter.  Enter \":q\" to quit.")

        while (exprStr != null && exprStr != ":q")
          {
            prompt()
            exprStr = readLine()
            if (exprStr != null && exprStr != ":q")
              {
                exprValue = Interpreter.interpret(exprStr)
                println(exprValue)
              }
          }
      }
  }


/**
 * Print the screen prompt.
 */
fun prompt()
  {
    print("SEL> ")
  }


object Interpreter
  {
    private val context : Context = Context()


    /**
     * Interpret the specified expression.
     */
    fun interpret(expression : String) : Double
      {
        val reader = StringReader(expression)
        val source = Source(reader)
        val scanner = Scanner(source)
        val parser = Parser(scanner)
        val program : Program = parser.parseProgram()
        return program.interpret(context)
      }
  }
