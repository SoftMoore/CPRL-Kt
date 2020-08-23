package edu.citadel.cvm


/**
 * Object containing CVM-related constants.
 */
object Constants
  {
    const val BYTES_PER_OPCODE  = 1

    const val BYTES_PER_INTEGER = 4

    const val BYTES_PER_ADDRESS = 4

    const val BYTES_PER_CHAR    = 2

    const val BYTES_PER_BOOLEAN = 1

    /** frame contains 2 addresses -- return address and dynamic link  */
    const val BYTES_PER_FRAME   = 2*BYTES_PER_ADDRESS
  }
