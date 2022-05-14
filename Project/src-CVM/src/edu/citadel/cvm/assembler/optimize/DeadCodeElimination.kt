package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.ast.Instruction


/**
 * If an instruction without labels follows a return or an unconditional
 * branch, then that instruction is unreachable (dead) and can be removed.
 */
class DeadCodeElimination : Optimization
  {
    override fun optimize(instructions: MutableList<Instruction>, instNum: Int)
      {
        // quick check that there are at least 2 instructions remaining
        if (instNum > instructions.size - 2)
            return

        val instruction0 = instructions[instNum]
        val symbol0 = instruction0.opCode.symbol

        // Check that symbol0 is either BR or RET.
        if (symbol0 === Symbol.BR || symbol0 === Symbol.RET)
          {
            val instruction1 = instructions[instNum + 1]

            // check that the second instruction does not have any labels
            if (instruction1.labels.isEmpty())
              {
                // We are free to remove the second instruction.
                instructions.removeAt(instNum + 1)
              }
          }
      }
  }