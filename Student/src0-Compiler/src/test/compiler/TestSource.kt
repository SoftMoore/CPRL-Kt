package test.compiler


import edu.citadel.compiler.Source
import java.io.*


fun main(args: Array<String>)
  {
    try
      {
        val fileName   = args[0]
        val fileReader = FileReader(fileName)
        val source = Source(fileReader)

        while (source.currentChar != Source.EOF)
          {
            val c = source.currentChar

            if (c == '\n'.toInt())
                print("\\n")
            else if (c != '\r'.toInt())
                print(c.toChar())

            println("\t ${source.charPosition}")

            source.advance()
          }
      }
    catch (e: Exception)
      {
        e.printStackTrace()
      }
  }
