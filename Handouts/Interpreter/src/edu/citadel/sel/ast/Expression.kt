package edu.citadel.sel.ast


/**
 * Interface for all SEL expressions.
 */
interface Expression
  {
    /**
     * Interpret the expression with the specified context.
     * @return the value of the expression.
     */
    fun interpret(context : Context) : Double
  }
