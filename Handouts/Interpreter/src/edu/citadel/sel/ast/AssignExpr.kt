package edu.citadel.sel.ast


import edu.citadel.sel.Symbol
import edu.citadel.sel.Token


/**
 * The abstract syntax tree node for an assignment expression.  An assignment
 * expression has the form "identifier <- expression".  A simple example would
 * be "x <- 5".
 *
 * @constructor Construct an assignment expression with an identifier (the left
 *              side of the expression) and an expression whose value is being
 *              assigned to the identifier.
 */
class AssignExpr(val idToken : Token, val expression : Expression) : Expression
  {
    override fun interpret(context : Context) : Double
      {
        val value = expression.interpret(context)
        val identifier : String = idToken.text
        context.put(identifier, value)
        return value
      }


    init
      {
        assert(idToken.symbol === Symbol.identifier)
      }
  }
