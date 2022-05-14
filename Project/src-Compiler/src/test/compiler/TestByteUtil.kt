package test.compiler


import edu.citadel.compiler.util.ByteUtil
import java.io.*


fun main()
  {
    var n  : Int
    var n2 : Int
    var c  : Char
    var c2 : Char
    var bytes: ByteArray

    val reader = BufferedReader(InputStreamReader(System.`in`))

    do
      {
        print("Enter a value for integer n (0 to stop):  ")
        val line = reader.readLine()
        n = Integer.parseInt(line)

        println("n = $n")

        bytes = ByteUtil.intToBytes(n)
        println("bytes[] = [${bytes[0]}, ${bytes[1]}, ${bytes[2]}, ${bytes[3]}]")

        n2 = ByteUtil.bytesToInt(bytes[0], bytes[1], bytes[2], bytes[3])
        println("n2 = $n2")
        println()
      }
    while (n != 0)

    c = 'z'

    do
      {
        print("Enter a letter or a numeric value for c (z to stop):  ")
        val line = reader.readLine()

        if (line != null && line.isNotEmpty())
          {
            if (Character.isDigit(line[0]))
              {
                // process a number
                n = Integer.parseInt(line)

                // convert number to a currentChar
                c = n.toChar()

                println("c = $c")

                bytes = ByteUtil.charToBytes(c)
                println("bytes[] = [${bytes[0]}, ${bytes[1]}]")

                c2 = ByteUtil.bytesToChar(bytes[0], bytes[1])
                println("c2 = $c2")

                println("charToHexString = ${ByteUtil.charToHexString(c)}")
                println()
              }
            else
              {
                // process a character
                c = line[0]

                println("c = $c")

                bytes = ByteUtil.charToBytes(c)
                println("bytes[] = [${bytes[0]}, ${bytes[1]}]")

                c2 = ByteUtil.bytesToChar(bytes[0], bytes[1])
                println("c2 = $c2")

                println("charToHexSting = ${ByteUtil.charToHexString(c)}")
                println()
              }
          }
      }
    while (c != 'z')
  }
