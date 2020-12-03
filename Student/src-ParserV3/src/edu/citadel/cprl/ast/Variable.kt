package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.Position
import edu.citadel.cprl.ArrayType
import edu.citadel.cprl.ScopeLevel
import edu.citadel.cprl.Type


/**
 * The abstract syntax tree node for a variable, which is any named variable
 * that can appear on the left hand side of an assignment statement.
 *
 * @constructor Construct a variable with a reference to its declaration,
 *              its position, and a list of index expressions.
 */
open class Variable(val decl : NamedDecl,
                    position : Position,
                    val indexExprs : List<Expression>)
    : Expression(decl.type, position)
  {
    /**
     * Construct a variable that corresponds to a named value.
     */
    constructor(nv : NamedValue) : this(nv.decl, nv.position, nv.indexExprs)


    override fun checkConstraints()
      {
        try
          {
            assert(decl is SingleVarDecl || decl is ParameterDecl)
              { "Declaration is not a variable." }

            // each index expression must correspond to an array type
            for (expr in indexExprs)
              {
                expr.checkConstraints()

                // check that the variable's type is an array type
                if (type is ArrayType)
                  {
                    // Applying the index effectively changes the
                    // variable's type to the base type of the array
                    val arrayType = type as ArrayType
                    type = arrayType.elementType
                  }
                else
                  {
                    val errorMsg = "Index expression not allowed; not an array."
                    throw error(expr.position, errorMsg)
                  }

                // check that the type of the index expression is Integer
                if (expr.type != Type.Integer)
                  {
                    val errorMsg = "Index expression must have type Integer."
                    throw error(expr.position, errorMsg)
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
        if (decl is ParameterDecl && decl.isVarParam)
          {
            // address of actual parameter is value of var parameter
            emit("LDLADDR ${decl.relAddr}")
            emit("LOADW")
          }
        else if (decl.scopeLevel == ScopeLevel.PROGRAM)
            emit("LDGADDR ${decl.relAddr}")
        else
            emit("LDLADDR ${decl.relAddr}")

        // For an array, at this point the base address of the array
        // is on the top of the stack.  We need to replace it by the
        // sum:  base address + offset

// ...
// Big Hint: When you apply an index, you should be generating code based on the element type.

// Loop over the index expressions.

// Outside the loop declare
// Type declType = decl.getType();

// Inside the loop at the top, cast declType to a variable named arrayType (of type ArrayType).

// Instead of emitting based on getType().getSize(), emit based on arrayType.getElementType().getSize().

// At the end of the loop, set declType to arrayType.getElementType().
      }
  }
