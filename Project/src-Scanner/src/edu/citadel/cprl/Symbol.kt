package edu.citadel.cprl


/**
 * This class encapsulates the symbols (also known as token types)
 * of the programming language CPRL.
 *
 * @constructor Construct a new symbol with its label.
 */
enum class Symbol(val label : String)
  {
    // reserved words
    BooleanRW("Boolean"),
    CharRW("Char"),
    IntegerRW("Integer"),
    StringRW("String"),
    andRW("and"),
    arrayRW("array"),
    beginRW("begin"),
    classRW("class"),
    constRW("const"),
    declareRW("declare"),
    elseRW("else"),
    elsifRW("elsif"),
    endRW("end"),
    exitRW("exit"),
    falseRW("false"),
    forRW("for"),
    functionRW("function"),
    ifRW("if"),
    inRW("in"),
    isRW("is"),
    loopRW("loop"),
    modRW("mod"),
    notRW("not"),
    ofRW("of"),
    orRW("or"),
    privateRW("private"),
    procedureRW("procedure"),
    programRW("program"),
    protectedRW("protected"),
    publicRW("public"),
    readRW("read"),
    readlnRW("readln"),
    returnRW("return"),
    thenRW("then"),
    trueRW("true"),
    typeRW("type"),
    varRW("var"),
    whenRW("when"),
    whileRW("while"),
    writeRW("write"),
    writelnRW("writeln"),

    // arithmetic operator symbols
    plus("+"),
    minus("-"),
    times("*"),
    divide("/"),

    // relational operator symbols
    equals("="),
    notEqual("!="),
    lessThan("<"),
    lessOrEqual("<="),
    greaterThan(">"),
    greaterOrEqual(">="),

    // assignment, punctuation, and grouping symbols
    assign(":="),
    leftParen("("),
    rightParen(")"),
    leftBracket("["),
    rightBracket("]"),
    comma(","),
    colon(":"),
    semicolon(";"),
    dot("."),

    // literal values and identifier symbols
    intLiteral("Integer Literal"),
    charLiteral("Character Literal"),
    stringLiteral("String Literal"),
    identifier("Identifier"),

    // special scanning symbols
    EOF("End-of-File"),
    unknown("Unknown");


    /**
     * Returns true if this symbol is a CPRL reserved word.
     */
    fun isReservedWord() : Boolean
        = this in BooleanRW..writelnRW


    /**
     * Returns true if this symbol can start an initial declaration.
     */
    fun isInitialDeclStarter() : Boolean
        = this == constRW || this == varRW || this == typeRW


    /**
     * Returns true if this symbol can start a subprogram declaration.
     */
    fun isSubprogramDeclStarter() : Boolean
        = this == procedureRW || this == functionRW


    /**
     * Returns true if this symbol can start a statement.
     */
    fun isStmtStarter() : Boolean
        =    this == exitRW  || this == identifier || this == ifRW
          || this == loopRW  || this == whileRW    || this == readRW
          || this == writeRW || this == writelnRW  || this == returnRW


    /**
     * Returns true if this symbol is a logical operator.
     */
    fun isLogicalOperator() : Boolean
        = this == andRW || this == orRW


    /**
     * Returns true if this symbol is a relational operator.
     */
    fun isRelationalOperator() : Boolean
        =    this == equals      || this == notEqual
          || this == lessThan    || this == lessOrEqual
          || this == greaterThan || this == greaterOrEqual


    /**
     * Returns true if this symbol is an adding operator.
     */
    fun isAddingOperator() : Boolean
        = this == plus || this == minus


    /**
     * Returns true if this symbol is a multiplying operator.
     */
    fun isMultiplyingOperator() : Boolean
        = this == times || this == divide || this == modRW


    /**
     * Returns true if this symbol is a literal.
     */
    fun isLiteral() : Boolean
        =    this == intLiteral || this == charLiteral || this == stringLiteral
          || this == trueRW     || this == falseRW


    /**
     * Returns true if this symbol can start an expression.
     */
    fun isExprStarter() : Boolean
        =    isLiteral()  || this == identifier || this == leftParen
          || this == plus || this == minus      || this == notRW


    /**
     * Returns the label for this Symbol.
     */
    override fun toString() : String = label
  }
