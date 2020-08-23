package edu.citadel.cvm.assembler.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.Position

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
        var outputStream : OutputStream? = null
      }
  }
