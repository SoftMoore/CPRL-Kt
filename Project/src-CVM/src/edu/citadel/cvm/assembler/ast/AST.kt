package edu.citadel.cvm.assembler.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.Position
import edu.citadel.compiler.util.ByteUtil

import java.io.OutputStream


/**
 * Base class for all abstract syntax trees
 */
abstract class AST
  {
    /**
     * Creates/returns a new constraint exception with the specified position and message.
     */
    protected fun error(errorPosition : Position, errorMessage : String) : ConstraintException
      {
        return ConstraintException(errorPosition, errorMessage)
      }


        /**
     * emit the opCode for the instruction
     */
    protected fun emit(opCode : Byte)
      {
        outputStream.write(opCode.toInt())
      }


    /**
     * emit an integer argument for the instruction
     */
    protected fun emit(arg : Int)
      {
        outputStream.write(ByteUtil.intToBytes(arg))
      }


    /**
     * emit a character argument for the instruction
     */
    protected fun emit(arg : Char)
      {
        outputStream.write(ByteUtil.charToBytes(arg))
      }


    /**
     * check semantic/contextual constraints
     */
    abstract fun checkConstraints()


    /**
     * emit the object code for the AST
     */
    abstract fun emit()


    companion object
      {
        /**
         * The OutputStream to be used for code generation
         */
        lateinit var outputStream : OutputStream
      }
  }
