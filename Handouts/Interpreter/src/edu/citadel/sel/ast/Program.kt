package edu.citadel.sel.ast


/**
 * The abstract syntax tree node for a program, which is simply a
 * list of expressions.
 *
 * @constructor Construct a program with a list of expressions.
 */
class Program(private val expressions : List<Expression>) : Expression
  {
    override fun interpret(context : Context) : Double
      {
        // interpret all but the last expression
        for (i in 0..expressions.size-2)
            expressions[i].interpret(context)

        // now interpret the last one
        val lastExpr = expressions[expressions.size - 1]
        return lastExpr.interpret(context)
      }
  }
