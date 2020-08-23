package edu.citadel.cprl.ast


import edu.citadel.compiler.CodeGenException
import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Symbol
import edu.citadel.cprl.Token
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a constant value expression, which is
 * either a literal or a declared constant identifier representing a literal.
 */
class ConstValue : Expression
  {
    /**
     * The literal token for the constant.  If the const value is created from a
     * constant declaration, then the literal is the one contained in the declaration.
     */
    private var literal : Token


    /**
     * Construct a constant value from a literal token.
     */
    constructor(literal : Token)
        : super(Type.getTypeOf(literal.symbol), literal.position)
      {
        this.literal = literal
      }


    /**
     * Construct a constant value from a constant identifier
     * token and its corresponding constant declaration.
     */
    constructor(identifier : Token, decl : ConstDecl)
        : super(decl.type, identifier.position)
      {
        literal = decl.literal as Token   // decl.literal should not be null at this point
      }


    /**
     * Returns an integer value for the declaration literal.  For an integer
     * literal, this method simply returns its integer value.  For a char
     * literal, this method returns the underlying integer value for the
     * character.  For a boolean literal, this method returns 0 for false
     * and 1 for true.  For any other literal, the method returns 0.
     */
    fun getLiteralIntValue() : Int
      {
        if (literal.symbol == Symbol.intLiteral)
            return Integer.parseInt(literal.text)
        else if (literal.symbol == Symbol.trueRW)
            return 1
        else if (literal.symbol == Symbol.charLiteral)
          {
            val ch = literal.text[1]
            return ch.toInt()
          }
        else
            return 0
      }


    override fun checkConstraints()
      {
        try
          {
            // check that an intLiteral can actually be converted to an integer
            if (literal.symbol == Symbol.intLiteral)
              {
                try
                  {
                    Integer.parseInt(literal.text)
                  }
                catch (e : NumberFormatException)
                  {
                    val errorMsg = "The number \"${literal.text}\" cannot " +
                                   " be converted to an integer in CPRL."
                    throw error(literal.position, errorMsg)
                  }
              }
          }
        catch (e : ConstraintException)
          {
            ErrorHandler.reportError(e)
          }
      }


    override fun emit()
      {
        when (type)
          {
            Type.Integer -> emit("LDCINT ${getLiteralIntValue()}")
            Type.Boolean -> emit("LDCB ${getLiteralIntValue()}")
            Type.Char    -> emit("LDCCH ${literal.text}")
            Type.String  -> emit("LDCSTR ${literal.text}")
            else         ->
              {
                val errorMsg = "Invalid type for constant value."
                throw CodeGenException(literal.position, errorMsg)
              }
          }
      }
  }
