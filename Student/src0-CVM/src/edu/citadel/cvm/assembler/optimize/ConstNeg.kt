package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.ast.Instruction
import edu.citadel.cvm.assembler.ast.InstructionOneArg


/**
 * Performs a special type of constant folding by replacing an instruction
 * sequence of the form LDCINT x, NEG with LDCINT -x.
 */
class ConstNeg : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum : Int)
      {
        // quick check that there are at least 2 instructions remaining
        if (instNum > instructions.size - 2)
            return

        val instruction0 = instructions[instNum]
        val instruction1 = instructions[instNum + 1]

        val symbol0 = instruction0.opCode.symbol
        val symbol1 = instruction1.opCode.symbol

        // check that we have LDCINT followed by NEG
        if (symbol0 == Symbol.LDCINT && symbol1 == Symbol.NEG)
          {
            instruction0 as InstructionOneArg
            val constValue = instruction0.argToInt()
            val negValue = -constValue

            // make sure that the NEG instruction does not have any labels
            val inst1Labels = instruction1.labels
            if (inst1Labels.isEmpty())
              {
                instruction0.arg.text = negValue.toString()

                // remove the NEG instruction
                instructions.removeAt(instNum + 1)
              }
          }
      }
  }
