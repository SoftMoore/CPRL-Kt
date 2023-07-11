package test.compiler


import edu.citadel.compiler.Source
import java.io.*
import java.nio.charset.StandardCharsets


/*
 * Test class Source.
 */
fun main(args: Array<String>)
  {
    try
      {
        val fileName   = args[0]
        val fileReader = FileReader(fileName, StandardCharsets.UTF_8)
        val source     = Source(fileReader)
        val out        = PrintStream(System.out, true, StandardCharsets.UTF_8)

        while (source.currentChar != Source.EOF)
          {
            val c = source.currentChar

            if (c == '\n'.code)
                out.print("\\n")
            else if (c != '\r'.code)
                out.print(c.toChar())

            out.println("\t ${source.charPosition}")

            source.advance()
          }
      }
    catch (e: Exception)
      {
        e.printStackTrace()
      }
  }
