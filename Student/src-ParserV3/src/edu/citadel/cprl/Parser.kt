package edu.citadel.cprl


import edu.citadel.compiler.Position
import edu.citadel.compiler.ParserException
import edu.citadel.compiler.InternalCompilerException
import edu.citadel.compiler.ErrorHandler
import edu.citadel.cprl.ast.*


/**
 * This class uses recursive descent to perform syntax analysis of
 * the CPRL source language and to generate an abstract syntax tree.
 *
 * @constructor Construct a parser with the specified scanner.
 */
class Parser (private val scanner : Scanner)
  {
    private val idTable = IdTable()
    private val loopContext = LoopContext()
    private val subprogramContext = SubprogramContext()


    /**
     * Symbols that can follow an initial declaration.
     */
    private val initialDeclFollowers = arrayOf(
// ...
            )


    /**
     * Symbols that can follow a subprogram declaration.
     */
    private val subprogDeclFollowers = arrayOf(
// ...
            )

    /**
     * Symbols that can follow a statement.
     */
    private val stmtFollowers = arrayOf(
// ...
            )

    /**
     * Symbols that can follow a factor.
     */
    private val factorFollowers = arrayOf(
            Symbol.semicolon, Symbol.loopRW,       Symbol.thenRW,      Symbol.rightParen,
            Symbol.andRW,     Symbol.orRW,         Symbol.equals,      Symbol.notEqual,
            Symbol.lessThan,  Symbol.lessOrEqual,  Symbol.greaterThan, Symbol.greaterOrEqual,
            Symbol.plus,      Symbol.minus,        Symbol.times,       Symbol.divide,
            Symbol.modRW,     Symbol.rightBracket, Symbol.comma)


    /**
     * Parse the following grammar rule:
     * `program = declarativePart statementPart "." .`
     *
     * @return the parsed program.  Returns a program with empty
     *         declarative and statement parts if parsing fails.
     */
    fun parseProgram() : Program
      {
        try
          {
            val declPart = parseDeclarativePart()
            val stmtPart = parseStatementPart()
            match(Symbol.dot)
            match(Symbol.EOF)
            return Program(declPart, stmtPart)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            val followers = arrayOf(Symbol.EOF)
            recover(followers)
            return Program()
          }
      }


    /**
     * Parse the following grammar rule:
     * `declarativePart = initialDecls subprogramDecls .`
     *
     * @return the parsed declarative part.
     */
    fun parseDeclarativePart() : DeclarativePart
      {
        val initialDecls = parseInitialDecls()
        val subprogDecls = parseSubprogramDecls()

        return DeclarativePart(initialDecls, subprogDecls)
      }


    /**
     * Parse the following grammar rule:
     * `initialDecls = ( initialDecl )* .`
     *
     * @return the list of initial declarations.
     */
    fun parseInitialDecls() : List<InitialDecl>
      {
        val initialDecls = ArrayList<InitialDecl>(10)

        while (scanner.symbol.isInitialDeclStarter())
          {
            val decl = parseInitialDecl()

            if (decl is VarDecl)
              {
                // add the single variable declarations
                for (singleVarDecl in decl.singleVarDecls)
                    initialDecls.add(singleVarDecl)
              }
            else if (decl != null)
                initialDecls.add(decl)
          }

        return initialDecls
      }


    /**
     * Parse the following grammar rule:
     * `initialDecl = constDecl | arrayTypeDecl | varDecl .`
     *
     * @return the parsed initial declaration.  Returns null if parsing fails.
     */
    fun parseInitialDecl() : InitialDecl?
      {
// ...   throw an internal error if the symbol is not one of constRW, varRW, or typeRW
      }


    /**
     * Parse the following grammar rule:
     * `constDecl = "const" constId ":=" literal ";" .`
     *
     * @return the parsed constant declaration. Returns null if parsing fails.
     */
    fun parseConstDecl() : ConstDecl?
      {
// ...
      }


    /**
     * Parse the following grammar rules:
     * `literal = intLiteral | charLiteral | stringLiteral | booleanLiteral .
     *  booleanLiteral = "true" | "false" .`
     *
     * @return the parsed literal token.  Returns null if parsing fails.
     */
    fun parseLiteral() : Token?
      {
        try
          {
            if (scanner.symbol.isLiteral())
              {
                val literal = scanner.token
                matchCurrentSymbol()
                return literal
              }
            else
                throw error("Invalid literal expression.")

          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            recover(factorFollowers)
            return null
          }
      }


    /**
     * Parse the following grammar rule:
     * `varDecl = "var" identifiers ":" typeName ";" .`
     *
     * @return the parsed variable declaration.  Returns null if parsing fails.
     */
    fun parseVarDecl() : VarDecl?
      {
        try
          {
            match(Symbol.varRW)
            val identifiers = parseIdentifiers()
            match(Symbol.colon)
            val varType = parseTypeName()
            match(Symbol.semicolon)

            val varDecl = VarDecl(identifiers, varType, idTable.scopeLevel)

            for (decl in varDecl.singleVarDecls)
                idTable.add(decl)

            return varDecl
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            recover(initialDeclFollowers)
            return null
          }
      }


    /**
     * Parse the following grammar rule:
     * `identifiers = identifier ( "," identifier )* .`
     *
     * @return the list of identifier tokens.  Returns an empty list if parsing fails.
     */
    fun parseIdentifiers() : List<Token>
      {
        try
          {
            val identifiers = ArrayList<Token>(10)
            var idToken = scanner.token
            match(Symbol.identifier)
            identifiers.add(idToken)

            while (scanner.symbol == Symbol.comma)
              {
                matchCurrentSymbol()
                idToken = scanner.token
                match(Symbol.identifier)
                identifiers.add(idToken)
              }

            return identifiers
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            val followers = arrayOf(Symbol.colon)
            recover(followers)
            return emptyList()
          }
      }


    /**
     * Parse the following grammar rule:
     * `arrayTypeDecl = "type" typeId "=" "array" "[" intConstValue "]" "of" typeName ";" .`
     *
     * @return the parsed array type declaration.  Returns null if parsing fails.
     */
    fun parseArrayTypeDecl() : ArrayTypeDecl?
      {
// ...
// Hint: If parseConstDecl() returns a null value, create a "dummy" token for the
//       ConstValue to prevent additional errors associated with a null pointer; e.g.,
//       Token token = new Token(Symbol.intLiteral, scanner.getPosition(), "0");
      }


    /**
     * Parse the following grammar rule:
     * `typeName = "Integer" | "Boolean" | "Char" | typeId .`
     *
     * @return the parsed named type object.  Returns Type.UNKNOWN if parsing fails.
     */
    fun parseTypeName() : Type
      {
        var type = Type.UNKNOWN

        try
          {
            when (scanner.symbol)
              {
                Symbol.IntegerRW ->
                  {
                    type = Type.Integer
                    matchCurrentSymbol()
                  }
                Symbol.BooleanRW ->
                  {
                    type = Type.Boolean
                    matchCurrentSymbol()
                  }
                Symbol.CharRW ->
                  {
                    type = Type.Char
                    matchCurrentSymbol()
                  }
                Symbol.identifier ->
                  {
                    val typeId = scanner.token
                    matchCurrentSymbol()
                    val decl = idTable[typeId]

                    if (decl != null)
                      {
                        if (decl is ArrayTypeDecl)
                            type = decl.type
                        else
                            throw error(typeId.position,
                                        "Identifier \"$typeId\" is not a valid type name.")
                      }
                    else
                        throw error(typeId.position,
                                    "Identifier \"$typeId\" has not been declared.")
                  }
                else -> throw error("Invalid type name.")
              }
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            val followers = arrayOf(Symbol.semicolon,  Symbol.comma,
                                    Symbol.rightParen, Symbol.isRW)
            recover(followers)
          }

        return type
      }


    /**
     * Parse the following grammar rule:
     * `subprogramDecls = ( subprogramDecl )* .`
     *
     * @return the list of subprogram declarations.
     */
    fun parseSubprogramDecls() : List<SubprogramDecl>
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `subprogramDecl = procedureDecl | functionDecl .`
     *
     * @return the parsed subprogram declaration.  Returns null if parsing fails.
     */
    fun parseSubprogramDecl() : SubprogramDecl?
      {
// ...   throw an internal error if the symbol is not one of procedureRW or functionRW
      }


    /**
     * Parse the following grammar rule:
     * `procedureDecl = "procedure" procId ( formalParameters )?
     * "is" initialDecls statementPart procId ";" .`
     *
     * @return the parsed procedure declaration.  Returns null if parsing fails.
     */
    fun parseProcedureDecl() : ProcedureDecl?
      {
        try
          {
            match(Symbol.procedureRW)
            val procId = scanner.token
            match(Symbol.identifier)
            val procDecl = ProcedureDecl(procId)
            idTable.add(procDecl)
            idTable.openScope()

            if (scanner.symbol == Symbol.leftParen)
                procDecl.formalParams = parseFormalParameters()

            match(Symbol.isRW)

            procDecl.initialDecls = parseInitialDecls()

            subprogramContext.beginSubprogramDecl(procDecl)
            procDecl.stmtPart = parseStatementPart()
            subprogramContext.endSubprogramDecl()
            idTable.closeScope()

            val procId2 = scanner.token
            match(Symbol.identifier)

            if (procId.text != procId2.text)
                throw error(procId2.position, "Procedure name mismatch.")

            match(Symbol.semicolon)
            return procDecl
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            recover(subprogDeclFollowers)
            return null
          }
    }


    /**
     * Parse the following grammar rule:
     * `functionDecl = "function" funcId ( formalParameters )? "return" typeName
     *                 "is" initialDecls statementPart funcId ";" .`
     *
     * @return the parsed function declaration.  Returns null if parsing fails.
     */
    fun parseFunctionDecl() : FunctionDecl?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `formalParameters = "(" parameterDecl ( "," parameterDecl )* ")" .`
     *
     * @return a list of formal parameter declarations.
     */
    fun parseFormalParameters() : List<ParameterDecl>
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `parameterDecl = ( "var" )? paramId ":" typeName .`
     *
     * @return the parsed parameter declaration.  Returns null if parsing fails.
     */
    fun parseParameterDecl() : ParameterDecl?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `statementPart = "begin" statements "end" .`
     *
     * @return the parsed statement part.  Returns a statement part
     *         with an empty list of statements if parsing fails.
     */
    fun parseStatementPart() : StatementPart
      {
        try
          {
            match(Symbol.beginRW)
            val statements = parseStatements()
            match(Symbol.endRW)
            return StatementPart(statements)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            val followers = arrayOf(Symbol.dot, Symbol.identifier)
            recover(followers)
            return StatementPart(emptyList())
          }
      }


    /**
     * Parse the following grammar rule:
     * `statements = ( statement )* .`
     *
     * @return a list of statements.  Returns an empty list if parsing fails.
     */
    fun parseStatements() : List<Statement>
      {
// ...   Hint: Add only non-null statements to the list.
      }


    /**
     * Parse the following grammar rule:
     * `statement = assignmentStmt | ifStmt | loopStmt | exitStmt | readStmt
     *            | writeStmt | writelnStmt | procedureCallStmt | returnStmt .`
     *
     * @return the parsed Statement.  Returns null if parsing fails.
     */
    fun parseStatement() : Statement?
      {
        // assumes that scanner.getSymbol() can start a statement
        assert(scanner.symbol.isStmtStarter()) { "Invalid statement." }

// ...

        // Error recovery here is complicated for identifiers since they can both
        // start a statement and appear elsewhere in the statement.  (Consider,
        // for example, an assignment statement or a procedure call statement.)
        // Since the most common error is to declare or reference an identifier
        // incorrectly, we will assume that this is the case and advance to the
        // next semicolon (which hopefully ends the erroneous statement) before
        // performing error recovery.
      }


    /**
     * Parse the following grammar rule:
     * `assignmentStmt = variable ":=" expression ";" .`
     *
     * @return the parsed assignment statement.  Returns null if parsing fails.
     */
    fun parseAssignmentStmt() : AssignmentStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `ifStmt = "if" booleanExpr "then" statements
     *           ( "elsif" booleanExpr "then" statements )*
     *           ( "else" statements )? "end" "if" ";" .`
     *
     * @return the parsed if statement.  Returns null if parsing fails.
     */
    fun parseIfStmt() : IfStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `loopStmt = ( "while" booleanExpr )? "loop" statements "end" "loop" ";" .`
     *
     * @return the parsed loop statement.  Returns null if parsing fails.
     */
    fun parseLoopStmt() : LoopStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `exitStmt = "exit" ( "when" booleanExpr )? ";" .`
     *
     * @return the parsed exit statement.  Returns null if parsing fails.
     */
    fun parseExitStmt() : ExitStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `readStmt = "read" variable ";" .`
     *
     * @return the parsed read statement.  Returns null if parsing fails.
     */
    fun parseReadStmt() : ReadStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `writeStmt = "write" expressions ";" .`
     *
     * @return the parsed write statement.  Returns null if parsing fails.
     */
    fun parseWriteStmt() : WriteStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `expressions = expression ( "," expression )* .`
     *
     * @return a list of Expressions.  Returns an empty list if parsing fails.
     */
    fun parseExpressions() : MutableList<Expression>
      {
// ...   Hint: Add only non-null expressions to the list.
      }


    /**
     * Parse the following grammar rule:
     * `writelnStmt = "writeln" ( expressions )? ";" .`
     *
     * @return the parsed writeln statement.  Returns null if parsing fails.
     */
    fun parseWritelnStmt() : WritelnStmt?
      {
        try
          {
            match(Symbol.writelnRW)

            val expressions : List<Expression>
            if (scanner.symbol.isExprStarter())
                expressions = parseExpressions()
            else
                expressions = emptyList()

            match(Symbol.semicolon)
            return WritelnStmt(expressions)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            recover(stmtFollowers)
            return null
          }
      }


    /**
     * Parse the following grammar rule:
     * `procedureCallStmt = procId ( actualParameters )? ";" .`
     *
     * @return the parsed procedure call statement.  Returns null if parsing fails.
     */
    fun parseProcedureCallStmt() : ProcedureCallStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `actualParameters = "(" expressions ")" .`
     *
     * @return a list of expressions.  Returns an empty list if parsing fails.
     */
    fun parseActualParameters() : List<Expression>
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `returnStmt = "return" ( expression )? ";" .`
     *
     * @return the parsed ReturnStmt.  Returns null if parsing fails.
     */
    fun parseReturnStmt() : ReturnStmt?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `variable = ( varId | paramId ) ( "[" expression "]" )* .`
     *
     * This method provides common logic for methods `parseVariable()` and
     * `parseNamedValue()`.  The method does not handle any parser exceptions but
     * throws them back to the calling method where they can be handled appropriately.
     *
     * @return the parsed variable.
     * @throws ParserException if parsing fails.
     * @see .parseVariable
     * @see .parseNamedValue
     */
    fun parseVariableExpr() : Variable
      {
        val idToken = scanner.token
        match(Symbol.identifier)
        val decl = idTable[idToken]

        if (decl == null)
            throw error("Identifier \"$idToken\" has not been declared.")
        else if (decl !is NamedDecl)
            throw error("Identifier \"$idToken\" is not a variable.")

        val namedDecl = decl as NamedDecl

        val indexExprs = ArrayList<Expression>(5)
        while (scanner.symbol == Symbol.leftBracket)
          {
            matchCurrentSymbol()
            val indexExpr = parseExpression()
            if (indexExpr != null)
                indexExprs.add(indexExpr)
            match(Symbol.rightBracket)
          }

        return Variable(namedDecl, idToken.position, indexExprs)
      }


    /**
     * Parse the following grammar rule:
     * `variable = ( varId | paramId ) ( "[" expression "]" )* .`
     *
     * @return the parsed Variable.  Returns null if parsing fails.
     */
    fun parseVariable() : Variable?
      {
        try
          {
            return parseVariableExpr()
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            val followers = arrayOf(Symbol.assign, Symbol.semicolon)
            recover(followers)
            return null
          }
      }


    /**
     * Parse the following grammar rules:
     * `expression = relation ( logicalOp relation )* .
     *  logicalOp = "and" | "or" . `
     *
     * @return the parsed expression.  Returns null if parsing fails
     */
    fun parseExpression() : Expression?
      {
        var expr = parseRelation()

        while (scanner.symbol.isLogicalOperator())
          {
            val operator = scanner.token
            matchCurrentSymbol()
            val expr2 = parseRelation()
            if (expr != null && expr2 != null)
                expr = LogicalExpr(expr, operator, expr2)
          }

        return expr
      }


    /**
     * Parse the following grammar rules:
     * `relation = simpleExpr ( relationalOp simpleExpr )? .
     *  relationalOp = "=" | "!=" | "<" | "<=" | ">" | ">=" .`
     *
     * @return the parsed relational expression.  Returns null if parsing fails
     */
    fun parseRelation() : Expression?
      {
// ...
      }


    /**
     * Parse the following grammar rules:
     * `simpleExpr = ( addingOp )? term ( addingOp term )* .
     *  addingOp = "+" | "-" .`
     *
     * @return the parsed simple expression.  Returns null if parsing fails.
     */
    fun parseSimpleExpr() : Expression?
      {
// ...
      }


    /**
     * Parse the following grammar rules:
     * `term = factor ( multiplyingOp factor )* .
     *  multiplyingOp = "*" | "/" | "mod" .`
     *
     * @return the parsed term expression.  Returns null if parsing fails.
     */
    fun parseTerm() : Expression?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `factor = "not" factor | constValue | namedValue | functionCall
     *         | "(" expression ")" .`
     *
     * @return the parsed factor expression.  Returns null if parsing fails.
     */
    fun parseFactor() : Expression?
      {
        try
          {
            var expr : Expression? = null

            when
              {
                scanner.symbol == Symbol.notRW ->
                  {
                    val operator = scanner.token
                    matchCurrentSymbol()
                    val factorExpr = parseFactor()
                    if (factorExpr != null)
                        expr = NotExpr(operator, factorExpr)
                  }
                scanner.symbol.isLiteral()     ->
                  {
                    // Handle constant literals separately from constant identifiers.
                    expr = parseConstValue()
                  }
                scanner.symbol == Symbol.identifier ->
                  {
                    // Handle identifiers based on whether they are declared
                    // as variables, constants, or functions.
                    val idToken = scanner.token
                    val decl = idTable[idToken]

                    if (decl != null)
                      {
                        when (decl)
                          {
                            is ConstDecl    -> expr = parseConstValue()
                            is NamedDecl    -> expr = parseNamedValue()
                            is FunctionDecl -> expr = parseFunctionCall()
                            else            -> throw error("Identifier \"${scanner.token}\" "
                                                        + "is not valid as an expression.")
                          }
                      }
                    else
                        throw error("Identifier \"${scanner.token}\" has not been declared.")
                  }
                scanner.symbol == Symbol.leftParen  ->
                  {
                    matchCurrentSymbol()
                    expr = parseExpression()
                    match(Symbol.rightParen)
                  }
                else                           -> throw error("Invalid expression.")
              }

            return expr
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            recover(factorFollowers)
            return null
          }
      }


    /**
     * Parse the following grammar rule:
     * `constValue = literal | constId .`
     *
     * @return the parsed constant value.  Returns null if parsing fails.
     */
    fun parseConstValue() : ConstValue?
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `namedValue = variable .`
     *
     * @return the parsed NamedValue.  Returns null if parsing fails.
     */
    fun parseNamedValue() : NamedValue?
      {
        try
          {
            val variableExpr = parseVariableExpr()
            return NamedValue(variableExpr)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            recover(factorFollowers)
            return null
          }
      }


    /**
     * Parse the following grammar rule:
     * `functionCall = funcId ( actualParameters )? .`
     *
     * @return the parsed function call.  Returns null if parsing fails.
     */
    fun parseFunctionCall() : FunctionCall?
      {
// ...
      }


    // Utility parsing methods


    /**
     * Check that the current scanner symbol is the expected symbol.  If it is,
     * then advance the scanner.  Otherwise, throw a ParserException object.
     */
    private fun match(expectedSymbol : Symbol)
      {
        if (scanner.symbol == expectedSymbol)
            scanner.advance()
        else
          {
            val errorMsg = "Expecting \"$expectedSymbol\" but " +
                           "found \"${scanner.token}\" instead."
            throw error(errorMsg)
          }
      }


    /**
     * Advance the scanner.  This method represents an unconditional match
     * with the current scanner symbol.
     */
    private fun matchCurrentSymbol()
      {
        scanner.advance()
      }


    /**
     * Advance the scanner until the current symbol is one of the
     * symbols in the specified array of follows.
     */
    private fun recover(followers : Array<Symbol>)
      {
        scanner.advanceTo(followers)
      }


    /**
     * Create a ParserException with the specified message and the
     * current scanner position.
     */
    private fun error(message : String)
        = ParserException(scanner.position, message)


    /**
     * Create a ParserException with the specified error position and message.
     */
    private fun error(errorPosition : Position, message : String)
        = ParserException(errorPosition, message)


    /**
     * Create an internal compiler exception with the specified message
     * and the current scanner position.
     */
    private fun internalError(message : String)
        = InternalCompilerException(scanner.position, message)
  }
