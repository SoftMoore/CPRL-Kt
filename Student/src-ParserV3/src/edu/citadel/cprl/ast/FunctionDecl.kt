package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.Token


/**
 * The abstract syntax tree node for a function declaration.
 *
 * @constructor Construct a function declaration with its name (an identifier).
 */
class FunctionDecl(funcId : Token) : SubprogramDecl(funcId)
  {
    /**
     * Computes the relative address of the function return value.</br>
     * Note:  This method assumes that the relative addresses of all
     * formal parameters have been set.
     */
    fun getRelAddr() : Int
      {
        var firstParamAddr = 0

        if (formalParams.isNotEmpty())
          {
            val firstParamDecl = formalParams[0]
            firstParamAddr = firstParamDecl.relAddr
          }

        // the location for the return value is above the first parameter
        return firstParamAddr - type.size
      }


    override fun checkConstraints()
      {
// ...  Hint: See SubprogramDecl
      }


    /**
     * Returns true if the specified list of statements contains at least one
     * return statement.
     *
     * @param statements  the list of statements to check for a return statement.  If
     *                    any of the statements in the list contains nested statements
     *                    (e.g., an if statement or a loop statement), then the nested
     *                    statements are also checked for a return statement.
     */
    private fun hasReturnStmt(statements : List<Statement>) : Boolean
      {
// ...
      }


    override fun emit()
      {
// ...
      }
  }
