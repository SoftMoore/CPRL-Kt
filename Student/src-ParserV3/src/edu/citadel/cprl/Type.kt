package edu.citadel.cprl


import edu.citadel.cvm.Constants


/**
 * This class encapsulates the language types for the programming language CPRL.
 * Type sizes are initialized to values appropriate for the CPRL virtual machine.
 *
 * @constructor Construct a new Type with the specified type name and size.
 */
open class Type protected constructor(private val name : String, var size : Int = 0)
  {
    /**
     * Returns true if and only if this type is a scalar type.
     * The scalar types in CPRL are Integer, Boolean, and Char.
     */
    val isScalar : Boolean
        get() = this == Integer || this == Boolean || this == Char


    /**
     * Returns the name for this type.
     */
    override fun toString() : String = name


    override fun hashCode() : Int = name.hashCode()


    override fun equals(other : Any?) : Boolean
      {
        if (this === other)
            return true

        if (other !is Type)
            return false

        if (name != other.name)
            return false

        return true
      }


    companion object
      {
        // predefined types
        val Boolean = Type("Boolean", Constants.BYTES_PER_BOOLEAN)
        val Integer = Type("Integer", Constants.BYTES_PER_INTEGER)
        val Char    = Type("Char",    Constants.BYTES_PER_CHAR)
        val String  = Type("String")   // actual size not needed in CPRL

        // an address of the target machine
        val Address = Type("Address", Constants.BYTES_PER_ADDRESS)

        // compiler-internal types
        val UNKNOWN = Type("UNKNOWN")
        val none    = Type("none")


        /**
         * Returns the type of a literal symbol.  For example, if the
         * symbol is an intLiteral, then Type.Integer is returned.
         *
         * @throws IllegalArgumentException if the symbol is not a literal
         */
        fun getTypeOf(literal : Symbol) : Type
          {
            if (!literal.isLiteral())
                throw IllegalArgumentException("Symbol is not a literal symbol")

            return if (literal == Symbol.intLiteral)
                Integer
            else if (literal == Symbol.stringLiteral)
                String
            else if (literal == Symbol.charLiteral)
                Char
            else if (literal == Symbol.trueRW || literal == Symbol.falseRW)
                Boolean
            else
                UNKNOWN
          }
      }
  }
