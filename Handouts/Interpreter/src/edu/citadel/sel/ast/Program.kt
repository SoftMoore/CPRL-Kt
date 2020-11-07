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
        for (expr in expressions)
            expr.interpret(context)

        val lastExpr = expressions[expressions.size - 1]
        return lastExpr.interpret(context)
      }
  }
