package edu.citadel.cprl.ast


import edu.citadel.compiler.CodeGenException
import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.Position
import edu.citadel.cprl.Type

import java.io.PrintWriter


/**
 * Base class for all abstract syntax tree classes.
 */
abstract class AST
  {
    /**
     * Returns a new value for a label number.  This method should
     * be called once for each label before code generation.
     */
    protected fun getNewLabel() : String
     {
       ++currentLabelNum
       return "L$currentLabelNum"
     }

    /**
     * Creates/returns a new constraint exception with the specified position and message.
     */
    protected fun error(errorPos : Position, errorMsg : String) : ConstraintException
     {
       return ConstraintException(errorPos, errorMsg)
     }


    /**
     * Check semantic/contextual constraints.
     */
    abstract fun checkConstraints()

    /**
     * Emit the object code for the AST.
     *
     * @throws CodeGenException if the method is unable to generate appropriate target code.
     */
    abstract fun emit()


     /**
     * Returns true if the two types are assignment compatible.
     */
    protected fun matchTypes(t1 : Type, t2 : Type) : Boolean = t1 == t2


    /**
     * Emits the appropriate LOAD instruction based on the type.
     */
    protected fun emitLoadInst(t : Type)
      {
        when (t.size)
          {
            4    -> emit("LOADW")
            2    -> emit("LOAD2B")
            1    -> emit("LOADB")
            else -> emit("LOAD ${t.size}")
          }
      }


    /**
     * Emits the appropriate STORE instruction based on the type.
     */
    protected fun emitStoreInst(t : Type)
      {
        when (t.size)
          {
            4    -> emit("STOREW")
            2    -> emit("STORE2B")
            1    -> emit("STOREB")
            else -> emit("STORE ${t.size}")
          }
      }


    /**
     * Emit label for assembly instruction.  This instruction appends a colon
     * to the end of the label and writes out the result on a single line.
     */
    protected fun emitLabel(label : String)
      {
        out.println("$label:")
      }


    /**
     * Emit string representation for an assembly instruction.
     */
    protected fun emit(instruction : String)
      {
        out.println(SPACES + instruction)
      }


    companion object
      {
        // number of spaces to print before opcode
        private const val SPACES = "   "

        private var out : PrintWriter = PrintWriter(System.out)

        // current label number for control flow
        private var currentLabelNum = -1


        /**
         * Set the print writer to be used for code generation.
         */
        fun setPrintWriter(out : PrintWriter)
          {
            AST.out = out
          }
      }
  }
