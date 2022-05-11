package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.ast.Instruction
import edu.citadel.cvm.assembler.ast.InstructionOneArg


/**
 * Using function composition in CPRL can generate 2 or more consecutive
 * ALLOC instructions.  This optimization replaces an instruction sequence
 * of the form ALLOC n, ALLOC m with ALLOC (n + m).
 */
class Allocate : Optimization
  {
    override fun optimize(instructions: MutableList<Instruction>, instNum: Int)
      {
        // quick check that there are at least 2 instructions remaining
        if (instNum > instructions.size - 2)
            return

        val instruction0 = instructions[instNum]
        val instruction1 = instructions[instNum + 1]
        val symbol0 = instruction0.opCode.symbol
        val symbol1 = instruction1.opCode.symbol

        // Check that we have two ALLOC symbols.
        if (symbol0 === Symbol.ALLOC && symbol1 === Symbol.ALLOC)
          {
            val inst0 = instruction0 as InstructionOneArg
            val constValue0 = inst0.argToInt()
            val inst1 = instruction1 as InstructionOneArg
            val constValue1 = inst1.argToInt()

            // Make sure that the second ALLOC instruction does not have any labels.
            if (instruction1.labels.isEmpty())
              {
                // We are free to make this optimization.
                inst0.arg.text = Integer.toString(constValue0 + constValue1)

                // remove the second ALLOC instruction
                instructions.removeAt(instNum + 1)
              }
          }
      }
  }