package edu.citadel.sel


/**
 * This class encapsulates the symbols of the programming language SEL.
 *
 * @constructor Constructs a new Symbol with its label.
 */
enum class Symbol(private val label : String)
  {
    // arithmetic operator symbols
    plus("+"),
    minus("-"),
    times("*"),
    divide("/"),

    // assignment, punctuation, and grouping symbols
    assign("="),
    leftParen("("),
    rightParen(")"),
    dot("."),

    // numeric literal and identifier
    numericLiteral("Numeric Literal"),
    identifier("Identifier"),

    // special scanning symbols
    EOL("End-of-Line"),
    EOF("End-of-File"),
    unknown("Unknown");


    /**
     * Returns true if this symbol is an adding operator.
     */
    val isAddingOperator : Boolean
        get() = (this == plus || this == minus)


    /**
     * Returns true if this symbol is a multiplying operator.
     */
    val isMultiplyingOperator : Boolean
        get() = (this == times || this == divide)


    /**
     * Returns true if this symbol can start an expression.
     */
    val isExprStarter : Boolean
        get() = this == numericLiteral || this == leftParen
             || this == plus           || this == minus


    /**
     * Returns the label for this Symbol.
     */
    override fun toString() : String = label
  }
