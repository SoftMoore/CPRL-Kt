package edu.citadel.cprl.ast


import edu.citadel.cprl.Token


/**
 * Base class for all binary expressions.  A binary expression is an expression
 * of the form "expression operator expression".  The first expression is called
 * the left operand, and the second expression is called the right operand.
 *
 * @constructor Construct a binary expression with its left operand, operator,
 *              and right operand.
 */
abstract class BinaryExpr(val leftOperand  : Expression,
                          val operator     : Token,
                          val rightOperand : Expression)
    : Expression(operator.position)
