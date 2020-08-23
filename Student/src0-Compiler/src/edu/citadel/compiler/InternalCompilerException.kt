package edu.citadel.compiler


/**
 * Class for exceptions encountered within the compiler.  These
 * exceptions represent problems with the implementation of the compiler
 * and should never occur if the compiler is implemented correctly.
 *
 * @constructor Construct an InternalCompilerException with the specified error message and position.
 */
class InternalCompilerException(position : Position, errorMessage : String)
    : RuntimeException("*** Internal Compiler Error near line $position:\n    $errorMessage")
