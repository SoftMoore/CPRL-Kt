package edu.citadel.cprl.ast


import edu.citadel.cprl.Token


/**
 * Base class for all unary expressions.  A unary expression is an expression
 * of the form "operator expression".  The expression following the operator
 * is called the operand.  A simple example would be "-x".
 *
 * @constructor Construct a unary expression with the specified operator and operand.
 */
abstract class UnaryExpr(val operator : Token, val operand : Expression)
    : Expression(operator.position)
