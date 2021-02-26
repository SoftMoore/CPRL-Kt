package edu.citadel.cvm.assembler.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.util.ByteUtil
import edu.citadel.cvm.Constants
import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token


/**
 * This abstract class implements common methods for the abstract
 * syntax tree of a single assembly language instruction.
 *
 * @constructor Construct an instruction with a list of labels and an opcode.
 */
abstract class Instruction(val labels : MutableList<Token>, val opCode : Token)
    : AST()
  {
    /**
     * The address of this instruction.
     */
    var address : Int = 0
        /**
         * Sets the memory address and defines label values for an instruction.
         */
        set(address)
          {
            field = address

            // define addresses for labels
            for (label in labels)
              {
                if (labelMap.containsKey(label.text))
                  {
                    val errorMessage = "This label has already been defined."
                    throw error(label.position, errorMessage)
                  }
                else
                    labelMap[label.text] = Integer.valueOf(address)
              }
          }


    /**
     * Returns the number of bytes in memory occupied by the argument.
     */
    protected abstract val argSize : Int


    /**
     * Returns the number of bytes in memory occupied by the instruction,
     * computed as 1 (for the opcode) plus the sizes of the operands.
     */
    open val size : Int
        get() = Constants.BYTES_PER_OPCODE + argSize


    /**
     * Map the text of the identifier token to an address on the stack.
     */
    fun defineIdAddress(identifier : Token, size : Int)
      {
        assert(identifier.symbol == Symbol.identifier)
            { "Expecting an identifier but found ${identifier.symbol}." }

        if (idMap.containsKey(identifier.text))
          {
            val errorMessage = "This identifier has already been defined."
            throw error(identifier.position, errorMessage)
          }
        else
          {
            idMap[identifier.text] = Integer.valueOf(idAddress)
            idAddress = idAddress + size
          }
      }


    /**
     * Returns the stack address associated with an identifier.
     */
    protected fun getIdAddress(identifier : Token) : Int
      {
        return idMap[identifier.text] as Int
      }


    /**
     * Checks that each label has a value defined in the label map.  This method
     * should not be called for an instruction before method setAddress().
     *
     * @throws ConstraintException if the instruction has a label that
     *                             is not defined in the label map.
     */
    protected fun checkLabels()
      {
        for (label in labels)
          {
            if (!labelMap.containsKey(label.text))
              {
                val errorMessage = ("label \"$label.text\" has not been defined.")
                throw ConstraintException(label.position, errorMessage)
              }
          }
      }


    /**
     * Calculates the displacement between an instruction's address and
     * a label (computed as label's address - instruction's address).
     * This method is used by branching and call instructions.
     */
    protected fun getDisplacement(labelArg : Token) : Int
      {
        val labelId = labelArg.text + ":"

        assert(labelMap.containsKey(labelId)) { "Label ${labelArg.text} not found." }

        val labelAddress = labelMap[labelId] as Int

        return labelAddress - this.address
      }


    /**
     * Asserts that the opCode token of the instruction has
     * the correct Symbol.  Implemented in each instruction
     * by calling the method assertOpCode(Symbol).
     */
    protected abstract fun assertOpCode()


    protected fun assertOpCode(opCode : Symbol)
      {
        assert(this.opCode.symbol == opCode) { "Wrong opCode." }
      }


    override fun toString() : String
      {
        val buffer = StringBuffer(100)

        // for now simply print the instruction
        for (label in labels)
            buffer.append(label.text + "\n")

        buffer.append("   ${opCode.text}")

        return buffer.toString()
      }


    companion object
      {
        /**
         * Maps label text (type String) to an address (type Integer).
         * Note that the label text always includes the colon (:) at the end.
         */
        val labelMap = HashMap<String, Int>()

        /**
         * Maps identifier text (type String) to a stack address (type Integer).
         */
        val idMap = HashMap<String, Int>()

        // Initialize address for identifiers (e.g., used in DEFINT).
        private var idAddress = Constants.BYTES_PER_FRAME
      }
  }
