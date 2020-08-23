package edu.citadel.cvm.assembler.optimize


import edu.citadel.cvm.assembler.Symbol
import edu.citadel.cvm.assembler.ast.*


/**
 * Replaces runtime arithmetic on constants with compile-time arithmetic.
 */
class ConstFolding : Optimization
  {
    override fun optimize(instructions : MutableList<Instruction>, instNum : Int)
      {
        // quick check that there are at least three instructions remaining
        if (instNum > instructions.size - 3)
            return

        val instruction0 = instructions[instNum]
        val instruction1 = instructions[instNum + 1]
        val instruction2 = instructions[instNum + 2]

        // quick check that we are dealing with two constants
        val symbol0 = instruction0.opCode.symbol
        val symbol1 = instruction1.opCode.symbol
        val symbol2 = instruction2.opCode.symbol

        // quick check that we are dealing with two LDCINT instructions
        if (symbol0 != Symbol.LDCINT || symbol1 != Symbol.LDCINT)
            return

        // we are dealing with two constant integers
        instruction0 as InstructionOneArg
        instruction1 as InstructionOneArg

        val value0 = instruction0.argToInt()
        val value1 = instruction1.argToInt()

        // make sure that inst1 and inst2 do not have any labels
        if (instruction1.labels.isEmpty() || instruction2.labels.isEmpty())
          {
            when (symbol2)
              {
                Symbol.ADD ->
                  {
                    val sum = value0 + value1
                    instruction0.arg.text = sum.toString()
                  }
                Symbol.SUB ->
                  {
                    val difference = value0 - value1
                    instruction0.arg.text = difference.toString()
                  }
                Symbol.MUL ->
                  {
                    val product = value0*value1
                    instruction0.arg.text = product.toString()
                  }
                Symbol.DIV ->
                  {
                    val quotient = value0/value1
                    instruction0.arg.text = quotient.toString()
                  }
                Symbol.MOD ->
                  {
                    val remainder = value0%value1
                    instruction0.arg.text = remainder.toString()
                  }
                else       -> return
              }

            // modify the list of instructions to reflect the optimization
            instructions.removeAt(instNum + 1)
            instructions.removeAt(instNum + 1)
          }
      }
  }
