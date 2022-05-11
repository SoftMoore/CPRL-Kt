package edu.citadel.compiler.util


/**
 * Methods to convert integers and characters to byte representations, and vice versa.
 */
object ByteUtil
  {
    private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

    /**
     * Convert a single byte to a string of 2 hexadecimal digits.
     */
    fun byteToHexString(b : Byte) : String
      {
        val builder = StringBuilder(2)
        builder.append(HEX_ARRAY[b.toInt() ushr 4])
        builder.append(HEX_ARRAY[b.toInt() and 0x0F])
        return builder.toString()
      }


    /**
     * Convert a 2-byte char to a string of 4 hexadecimal digits.
     */
    fun charToHexString(c : Char) : String
      {
        val builder = StringBuilder(2)
        builder.append(HEX_ARRAY[c.code ushr 12])
        builder.append(HEX_ARRAY[c.code and 0x0F00 shr 8])
        builder.append(HEX_ARRAY[c.code and 0x00F0 shr 4])
        builder.append(HEX_ARRAY[c.code and 0x000F])
        return builder.toString()
      }


    /**
     * Convert an array of bytes to a string of hexadecimal digits separated by spaces.
     */
    fun bytesToHex(bytes : ByteArray) : String
      {
        val builder = StringBuilder(bytes.size*3)
        val hexChars = CharArray(bytes.size*2)
        for (i in bytes.indices)
        {
          val v : Int = bytes[i].toInt() and 0xFF
          hexChars[i*2] = HEX_ARRAY[v ushr 4]
          hexChars[i*2 + 1] = HEX_ARRAY[v and 0x0F]
          builder.append(HEX_ARRAY[v ushr 4])
          builder.append(HEX_ARRAY[v and 0x0F])
          builder.append(' ')
        }

        return builder.toString()
      }


    /**
     * Converts 2 bytes to a currentChar.  The bytes passed as arguments are
     * ordered with b0 as the high order byte and b1 as the low order byte.
     */
    fun bytesToChar(b0 : Byte, b1 : Byte) : Char
      {
        return (b0.toInt() shl 8 and 0x0000FF00 or (b1.toInt() and 0x000000FF)).toChar()
      }


    /**
     * Converts 4 bytes to an int.  The bytes passed as arguments are
     * ordered with b0 as the high order byte and b3 as the low order byte.
     */
    fun bytesToInt(b0 : Byte, b1 : Byte, b2 : Byte, b3 : Byte) : Int
      {
        return (b0.toInt() shl 24 and -0x1000000
                or (b1.toInt() shl 16 and 0x00FF0000)
                or (b2.toInt() shl 8 and 0x0000FF00)
                or (b3.toInt() and 0x000000FF))
      }


    /**
     * Converts a currentChar to an array of 2 bytes.  The bytes in the return
     * array are ordered with the one at index 0 as the high order byte
     * and the one at index 1 as the low order byte.
     */
    fun charToBytes(c : Char) : ByteArray
      {
        val result = ByteArray(2)

        result[0] = (c.code.ushr(8) and 0x00FF).toByte()
        result[1] = (c.code.ushr(0) and 0x00FF).toByte()

        return result
      }


    /**
     * Converts a short to an array of 2 bytes.  The bytes in the return
     * array are ordered with the one at index 0 as the high order byte
     * and the one at index 1 as the low order byte.
     */
    fun shortToBytes(n : Short) : ByteArray
      {
        val result = ByteArray(2)

        result[0] = (n.toInt().ushr(8) and 0x00FF).toByte()
        result[1] = (n.toInt()ushr(0) and 0x00FF).toByte()

        return result
      }


    /**
     * Converts an int to an array of 4 bytes.  The bytes in the return
     * array are ordered with the one at index 0 as the high order byte
     * and the one at index 3 as the low order byte.
     */
    fun intToBytes(n : Int) : ByteArray
      {
        val result = ByteArray(4)

        result[0] = (n.ushr(24) and 0x000000FF).toByte()
        result[1] = (n.ushr(16) and 0x000000FF).toByte()
        result[2] = (n.ushr(8)  and 0x000000FF).toByte()
        result[3] = (n.ushr(0)  and 0x000000FF).toByte()

        return result
      }
  }
