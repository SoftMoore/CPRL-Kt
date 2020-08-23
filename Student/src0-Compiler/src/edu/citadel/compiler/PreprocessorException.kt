package edu.citadel.compiler


/**
 * Class for exceptions encountered during preprocessing phase of the assembler.
 *
 * @constructor Construct a preprocessor exception with the specified message and line number.
 */
class PreprocessorException(message : String, lineNum : Int)
    : Exception("*** Preprocessor error detected on line $lineNum:\n    $message")
