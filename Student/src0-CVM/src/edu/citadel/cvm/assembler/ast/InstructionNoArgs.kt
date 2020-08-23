package edu.citadel.cvm.assembler.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cvm.assembler.Token


/**
 * This class serves as a base class for the abstract syntax
 * tree for an assembly language instruction with no arguments.
 *
 * @constructor Construct a no-argument instruction with a
 *              list of labels and an opcode.
 */
abstract class InstructionNoArgs(labels : MutableList<Token>, opCode : Token)
    : Instruction(labels, opCode)
  {
    override val argSize : Int
        get() = 0


    override fun checkConstraints()
      {
        try
          {
            assertOpCode()
            checkLabels()
          }
        catch (e : ConstraintException)
          {
            ErrorHandler.reportError(e)
          }
      }
  }
