package edu.citadel.cvm.assembler.optimize


/*
 * Utility for shift-related optimizations.
 */


/**
 * If n is a power of 2, returns log2(n) (i.e., returns the exponent);
 * otherwise returns 0.
 */
fun getShiftAmount(n : Int) : Byte
  {
    var n1 = n
    var result: Byte = 0

    while (n1 > 1)
      {
        if (n1%2 == 1)
            return 0
        else
          {
            n1 = n1/2
            ++result
          }
      }

    return result
  }
