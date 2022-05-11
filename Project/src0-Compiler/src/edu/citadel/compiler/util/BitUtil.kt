package edu.citadel.compiler.util


/**
 * This class encapsulates several bit manipulation utility methods.
 */
object BitUtil
  {
    /**
     * Converts a string of 16 0s and 1s to a short value.
     */
    fun binaryStringToShort(bits : String) : Short
      {
        var result = 0

        if (bits.length != 16)
            throw  IllegalArgumentException("*** bad string length ***")

        var mask = 1 shl 15

        for (i in 0..15)
          {
            val c = bits[i]
            if (c == '1')
                result = result or mask
            else if (c != '0')
                throw IllegalArgumentException("*** non-binary character ***")

            mask = mask.ushr(1)
          }

        return result.toShort()
      }


    /**
     * Returns a binary string representation of the least significant
     * (right most) bits for the specified int.
     */
    fun toBinaryString(n : Int, numBits : Int = Integer.SIZE) : String
      {
        val builder = StringBuilder()

        var mask = 1 shl numBits - 1

        for (count in 0 until numBits)
          {
            builder.append(if (n and mask == 0) '0' else '1')
            mask = mask.ushr(1)
          }

        return builder.toString()
      }


    /**
     * Returns a binary string representation of the least significant
     * (right mode) bits for the specified short.
     */
    fun toBinaryString(n : Short, numBits : Int = Short.SIZE_BITS) : String
      {
        val builder = StringBuilder()
        val nInt = n.toInt()
        var mask = 1 shl numBits - 1

        for (count in 0 until numBits)
          {
            builder.append(if (nInt and mask == 0) '0' else '1')
            mask = mask.ushr(1)
          }

        return builder.toString()
      }


    /**
     * Returns a binary string representation of the least significant
     * (right mode) bits for the specified byte.
     */
    fun toBinaryString(n : Byte, numBits : Int = Short.SIZE_BITS) : String
      {

        val builder = StringBuilder()
        val nInt = n.toInt()
        var mask = 1 shl (numBits - 1)

        for (count in 0 until numBits)
          {
            builder.append(if (nInt and mask == 0) '0' else '1')
            mask = mask.ushr(1)
          }

        return builder.toString()
      }


    /**
     * Returns a hexadecimal string representation of the specified integer.
     */
    fun toHexString(n : Int) : String
      {
        val bytes = ByteUtil.intToBytes(n)

        val sb = StringBuilder()
        for (b in bytes)
            sb.append(String.format("%02X", b))

        return sb.toString()
      }


    /**
     * Returns a hexadecimal string representation of the specified short.
     */
    fun toHexString(n : Short) : String
      {
        val bytes = ByteUtil.shortToBytes(n)

        val sb = StringBuilder()
        for (b in bytes)
            sb.append(String.format("%02X", b))

        return sb.toString()
      }


    /**
     * Returns a hex string representation of the specified byte.
     */
    fun toHexString(n : Byte) : String
      {
        return String.format("%02X", n)
      }


    /**
     * Returns the n least significant (rightmost) bits in the
     * specified value as a signed (2s complement) integer.
     */
    fun bitsToInt(value : Int, n : Int) : Int
      {
        if (n >= Integer.SIZE)
          {
            val errorMsg = "*** Parameter n too large: $n ***"
            throw IllegalArgumentException(errorMsg)
          }

        // mask out rightmost n bits
        val mask = (-1).ushr(Integer.SIZE - n)
        var result = value and mask

        // if  bit (n - 1) is 1, we have a negative number, so set bits 32..n to 1.
        val nthBitMask = 1 shl n - 1
        if (result and nthBitMask != 0)
          {
            val maskComplement = mask.inv()
            result = result or maskComplement
          }

        return result
      }


    /**
     * Returns the n least significant (rightmost) bits in the specified
     * value as an unsigned integer; i.e., the most significant bit is not
     * interpreted as a sign bit.
     */
    fun bitsToUnsigned(value : Int, n : Int) : Int
      {
        if (n >= Integer.SIZE)
          {
            val errorMsg = "*** Parameter n too large: $n ***"
            throw IllegalArgumentException(errorMsg)
          }

        // mask out rightmost n bits
        val mask = (-1).ushr(Integer.SIZE - n)

        return value and mask
      }
  }
