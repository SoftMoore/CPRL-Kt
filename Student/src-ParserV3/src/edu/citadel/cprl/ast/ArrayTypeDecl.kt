package edu.citadel.cprl.ast


import edu.citadel.compiler.ConstraintException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.ArrayType;
import edu.citadel.cprl.Token;
import edu.citadel.cprl.Type;

/**
 * The abstract syntax tree node for an array type declaration.
 *
 * @param typeId the token containing the identifier for the array
 * @param elementType the type of elements in the array
 * @property numElements the number of elements in the array
 */
class ArrayTypeDecl(typeId : Token, elementType : Type, private val numElements : ConstValue)
    : InitialDecl(typeId, ArrayType(typeId.text, numElements.getLiteralIntValue(), elementType))
  {
    override fun checkConstraints()
      {
// ...
      }
  }
