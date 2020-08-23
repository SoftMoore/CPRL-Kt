package edu.citadel.compiler


/**
 * Class for exceptions encountered during constraint analysis.
 *
 * @constructor Construct a ConstraintException with the specified error message and position.
 */
class ConstraintException(position : Position, message : String)
    : CompilerException("Constraint", position, message)
