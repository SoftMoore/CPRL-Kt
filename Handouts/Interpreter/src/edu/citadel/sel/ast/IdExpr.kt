package edu.citadel.sel.ast


import edu.citadel.sel.Token


/**
 * The abstract syntax tree node for an identifier expression.  An identifier
 * expression is simply an identifier.  A simple example would be "x".
 *
 * @constructor Construct an identifier expression with the identifier token.
 */
class IdExpr(val idToken : Token) : Expression
  {
    override fun interpret(context : Context) : Double = context[idToken.text]
  }
