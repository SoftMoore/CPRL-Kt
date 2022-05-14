package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.Token
import edu.citadel.cvm.assembler.ast.*


/**
 * Improves branching instructions where a nonconditional branch is used
 * to branch over a conditional branch (which can occur when an exit
 * statement appears at the end of a loop).  For example, the following code
 * `
 * BZ L1
 * BR L0
 * L1: ...`
 * can be improved as follows:
 * `
 * BNZ L0
 * L1: ...`
 */
class BranchingReduction : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum : Int)
      {
        // quick check that there are at least 3 instructions remaining
        if (instNum > instructions.size - 3)
            return

        val instruction0 = instructions[instNum]
        val instruction1 = instructions[instNum + 1]
        val instruction2 = instructions[instNum + 2]

        val symbol0 = instruction0.opCode.symbol
        val symbol1 = instruction1.opCode.symbol

        // make sure that we have a conditional branch followed by BR
        // instruction, and that the label argument for the conditional
        // branch immediately follows the BR instruction.
        if (isConditionalBranch(symbol0) && symbol1 == Symbol.BR)
          {
            instruction0 as InstructionOneArg
            instruction1 as InstructionOneArg

            if (containsLabel(instruction2.labels, instruction0.arg))
              {
                // combine labels for instructions 0 and 1
                val labels = combineLabels(instruction0.labels, instruction1.labels)

                // get argument label from instruction1
                val arg = instruction1.arg

                // make the new branch instruction
                val branchInst = makeDualBranchInst(labels, symbol0, arg)
                instructions[instNum] = branchInst

                // remove the unconditional branch instruction
                instructions.removeAt(instNum + 1)
              }
          }
      }


    /**
     * Combines the lists of labels into a single list.
     */
    private fun combineLabels(labels1 : MutableList<Token>, labels2: MutableList<Token>)
        : MutableList<Token>
      {
        if (labels1.isEmpty())
            return labels2
        else if (labels2.isEmpty())
            return labels1
        else
          {
            labels1.addAll(labels2)
            return labels1
          }
      }


    /**
     * Returns true if the symbol is a conditional branch; that is,
     * if the symbol is one of BNZ, BZ, BG, BGE, BL, or BLE.
     */
    private fun isConditionalBranch(s : Symbol) : Boolean
      {
        return s == Symbol.BNZ  || s == Symbol.BZ  || s == Symbol.BG
            || s == Symbol.BGE  || s == Symbol.BL  || s == Symbol.BLE
      }


    /**
     * Returns dual branch conditional branch instruction with the specified
     * instruction labels and label argument.  For example, if parameter s has
     * the value Symbol.BG, then an instruction of type InstructionBLE is returned.
     */
    private fun makeDualBranchInst(labels : MutableList<Token>, s : Symbol, labelArg : Token)
        : Instruction
      {
        return when (s)
          {
            Symbol.BNZ -> InstructionBZ(labels,  Token(Symbol.BZ),  labelArg)
            Symbol.BZ  -> InstructionBNZ(labels, Token(Symbol.BNZ), labelArg)
            Symbol.BG  -> InstructionBLE(labels, Token(Symbol.BLE), labelArg)
            Symbol.BGE -> InstructionBL(labels,  Token(Symbol.BL),  labelArg)
            Symbol.BL  -> InstructionBGE(labels, Token(Symbol.BGE), labelArg)
            Symbol.BLE -> InstructionBG(labels,  Token(Symbol.BG),  labelArg)
            else       -> throw IllegalArgumentException("Illegal branch instruction $s")
          }
      }


    /**
     * Returns true if the text of the second parameter label equals the
     * text of one of the labels in the list.  Returns false otherwise.
     */
    private fun containsLabel(labels : List<Token>, label : Token): Boolean
      {
        val labelStr = label.text + ":"
        for (l in labels)
          {
            if (l.text == labelStr)
                return true
          }

        return false
      }
  }
