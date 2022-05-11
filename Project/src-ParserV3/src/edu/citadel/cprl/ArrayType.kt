package edu.citadel.cprl


/**
 * This class encapsulates the language concept of an array type
 * in the programming language CPRL.
 *
 * @constructor Construct an array type with the specified name, number
 * of elements, and the type of elements contained in the array.
 */
class ArrayType(typeName : String, val numElements : Int, val elementType : Type)
    : Type(..., ...)
// .... (Don't forget to call the superclass constructor with the name and size of the array type.)
// ...  (Question: What is the total size of the array?)
