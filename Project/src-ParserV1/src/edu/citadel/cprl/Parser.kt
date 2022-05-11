package edu.citadel.cprl


import edu.citadel.compiler.Position;
import edu.citadel.compiler.ParserException;
import edu.citadel.compiler.InternalCompilerException;
import edu.citadel.compiler.ErrorHandler;

import kotlin.system.exitProcess


/**
 * This class uses recursive descent to perform syntax analysis of
 * the CPRL source language.
 */
class Parser(private val scanner : Scanner)
  {
    private val idTable = IdTable()


    /**
     * Parse the following grammar rule:
     * `program = declarativePart statementPart "." .`
     */
    fun parseProgram()
      {
        try
          {
            parseDeclarativePart()
            parseStatementPart()
            match(Symbol.dot)
            match(Symbol.EOF)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `declarativePart = initialDecls subprogramDecls .`
     */
    fun parseDeclarativePart()
      {
        parseInitialDecls()
        parseSubprogramDecls()
      }


    /**
     * Parse the following grammar rule:
     * `initialDecls = ( initialDecl )* .`
     */
    fun parseInitialDecls()
      {
        while (scanner.symbol.isInitialDeclStarter())
            parseInitialDecl()
      }


    /**
     * Parse the following grammar rule:
     * `initialDecl = constDecl | arrayTypeDecl | varDecl .`
     */
    fun parseInitialDecl()
      {
// ...   throw an internal error if the symbol is not one of constRW, varRW, or typeRW
      }


    /**
     * Parse the following grammar rule:
     * `constDecl = "const" constId ":=" literal ";" .`
     */
    fun parseConstDecl()
      {
// ...
      }


    /**
     * Parse the following grammar rules:
     * `literal = intLiteral | charLiteral | stringLiteral | booleanLiteral .
     *
     * booleanLiteral = "true" | "false" .`
     */
    fun parseLiteral()
      {
        try
          {
            if (scanner.symbol.isLiteral())
                matchCurrentSymbol()
            else
                throw error("Invalid literal expression.")
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `varDecl = "var" identifiers ":" typeName ";" .`
     */
    fun parseVarDecl()
      {
        try
          {
            match(Symbol.varRW)
            val identifiers = parseIdentifiers()
            match(Symbol.colon)
            parseTypeName()
            match(Symbol.semicolon)

            for (identifier in identifiers)
                idTable.add(identifier, IdType.variableId)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
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
            exit()
            return emptyList()   // should never execute
          }
      }


    /**
     * Parse the following grammar rule:
     * `arrayTypeDecl = "type" typeId "=" "array" "[" intConstValue "]" "of" typeName ";" .`
     */
    fun parseArrayTypeDecl()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `typeName = "Integer" | "Boolean" | "Char" | typeId .`
     */
    fun parseTypeName()
      {
        try
          {
            when (scanner.symbol)
              {
                Symbol.IntegerRW  -> matchCurrentSymbol()
                Symbol.BooleanRW  -> matchCurrentSymbol()
                Symbol.CharRW     -> matchCurrentSymbol()
                Symbol.identifier ->
                  {
                    val typeId = scanner.token
                    matchCurrentSymbol()
                    val idType = idTable[typeId]
                    if (idType != null)
                      {
                          if (idType != IdType.arrayTypeId)
                              throw error(typeId.position, "Identifier \"$typeId\""
                                        + " is not a valid type name.")
                      }
                    else throw error(typeId.position, "Identifier \"$typeId\""
                                   + " has not been declared.")
                  }
                else -> throw error("Invalid type name.")
              }
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `subprogramDecls = ( subprogramDecl )* .`
     */
    fun parseSubprogramDecls()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `subprogramDecl = procedureDecl | functionDecl .`
     */
    fun parseSubprogramDecl()
      {
// ...   throw an internal error if the symbol is not one of procedureRW or functionRW
      }


    /**
     * Parse the following grammar rule:
     * `procedureDecl = "procedure" procId ( formalParameters )?
     *                  "is" initialDecls statementPart procId ";" .`
     */
    fun parseProcedureDecl()
      {
        try
          {
            match(Symbol.procedureRW)
            val procId = scanner.token
            match(Symbol.identifier)
            idTable.add(procId, IdType.procedureId)
            idTable.openScope()

            if (scanner.symbol == Symbol.leftParen)
                parseFormalParameters()

            match(Symbol.isRW)
            parseInitialDecls()
            parseStatementPart()
            idTable.closeScope()
            val procId2 = scanner.token
            match(Symbol.identifier)

            if (procId.text != procId2.text)
                throw error(procId2.position, "Procedure name mismatch.")

            match(Symbol.semicolon)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `functionDecl = "function" funcId ( formalParameters )? "return" typeName
     *                 "is" initialDecls statementPart funcId ";" .`
     */
    fun parseFunctionDecl()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `formalParameters = "(" parameterDecl ( "," parameterDecl )* ")" .`
     */
    fun parseFormalParameters()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `parameterDecl = ( "var" )? paramId ":" typeName .`
     */
    fun parseParameterDecl()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `statementPart = "begin" statements "end" .`
     */
    fun parseStatementPart()
      {
        try
          {
            match(Symbol.beginRW)
            parseStatements()
            match(Symbol.endRW)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `statements = ( statement )* .`
     */
    fun parseStatements()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `statement = assignmentStmt | ifStmt | loopStmt | exitStmt | readStmt
     *            | writeStmt | writelnStmt | procedureCallStmt | returnStmt .`
     */
    fun parseStatement()
      {
// ...
// How can you parse assignmentStmt and procCallStmt since both start with an identifier?
// Hint: Use the identifier table.
      }


    /**
     * Parse the following grammar rule:
     * `assignmentStmt = variable ":=" expression ";" .`
     */
    fun parseAssignmentStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `ifStmt = "if" booleanExpr "then" statements
     *         ( "elsif" booleanExpr "then" statements )*
     *         ( "else" statements )? "end" "if" ";" .
   ` */
    fun parseIfStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `loopStmt = ( "while" booleanExpr )? "loop" statements "end" "loop" ";" .`
     */
    fun parseLoopStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `exitStmt = "exit" ( "when" booleanExpr )? ";" .`
     */
    fun parseExitStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `readStmt = "read" variable ";" .`
     */
    fun parseReadStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `writeStmt = "write" expressions ";" .`
     */
    fun parseWriteStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `expressions = expression ( "," expression )* .`
     */
    fun parseExpressions()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `writelnStmt = "writeln" ( expressions )? ";" .`
     */
    fun parseWritelnStmt()
      {
        try
          {
            match(Symbol.writelnRW)

            if (scanner.symbol.isExprStarter())
                parseExpressions()

            match(Symbol.semicolon)
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `procedureCallStmt = procId ( actualParameters )? ";" .`
     */
    fun parseProcedureCallStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `actualParameters = "(" expressions ")" .`
     */
    fun parseActualParameters()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `returnStmt = "return" ( expression )? ";" .`
     */
    fun parseReturnStmt()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `variable = ( varId | paramId ) ( "[" expression "]" )* .`
     *
     * This helper method provides common logic for methods parseVariable() and
     * parseNamedValue().  The method does not handle any ParserExceptions but
     * throws them back to the calling method where they can be handled appropriately.
     *
     * @throws ParserException if parsing fails.
     * @see .parseVariable
     * @see .parseNamedValue
     */
    fun parseVariableExpr()
      {
        val idToken = scanner.token
        match(Symbol.identifier)
        val idType = idTable[idToken]

        if (idType == null)
          {
            val errorMsg = "Identifier \"$idToken\" has not been declared."
            throw error(idToken.position, errorMsg)
          }
        else if (idType != IdType.variableId)
          {
            val errorMsg = "Identifier \"$idToken\" is not a variable."
            throw error(idToken.position, errorMsg)
          }

        while (scanner.symbol == Symbol.leftBracket)
          {
            matchCurrentSymbol()
            parseExpression()
            match(Symbol.rightBracket)
          }
      }


    /**
     * Parse the following grammar rule:
     * `variable = ( varId | paramId ) ( "[" expression "]" )* .`
     */
    fun parseVariable()
      {
        try
          {
            parseVariableExpr()
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rules:
     * `expression = relation ( logicalOp relation )* .
     *
     * logicalOp = "and" | "or" .`
     */
    fun parseExpression()
      {
        parseRelation()

        while (scanner.symbol.isLogicalOperator())
          {
            matchCurrentSymbol()
            parseRelation()
          }
      }


    /**
     * Parse the following grammar rules:
     * `relation = simpleExpr ( relationalOp simpleExpr )? .
     *
     * relationalOp = "=" | "!=" | "<" | "<=" | ">" | ">=" .`
     */
    fun parseRelation()
      {
// ...
      }


    /**
     * Parse the following grammar rules:
     * `simpleExpr = ( addingOp )? term ( addingOp term )* .
     *
     * addingOp = "+" | "-" .`
     */
    fun parseSimpleExpr()
      {
// ...
      }


    /**
     * Parse the following grammar rules:
     * `term = factor ( multiplyingOp factor )* .
     *
     * multiplyingOp = "*" | "/" | "mod" .`
     */
    fun parseTerm()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `factor = "not" factor | constValue | namedValue | functionCall
     *         | "(" expression ")" .`
     */
    fun parseFactor()
      {
        try
          {
            when
              {
                scanner.symbol == Symbol.notRW ->
                  {
                    matchCurrentSymbol()
                    parseFactor()
                  }
                scanner.symbol.isLiteral() ->
                  {
                    // Handle constant literals separately from constant identifiers.
                    parseConstValue()
                  }
                scanner.symbol == Symbol.identifier ->
                  {
                    // Handle identifiers based on whether they are
                    // declared as variables, constants, or functions.
                    val idToken = scanner.token
                    val idType = idTable[idToken]

                    if (idType != null)
                      {
                        when (idType)
                          {
                            IdType.constantId -> parseConstValue()
                            IdType.variableId -> parseNamedValue()
                            IdType.functionId -> parseFunctionCall()
                            else              -> throw error("Identifier \"${scanner.token}\""
                                                          +  " is not valid as an expression.")
                          }
                      }
                    else
                        throw error("Identifier \"${scanner.token}\" has not been declared.")
                  }
                scanner.symbol == Symbol.leftParen ->
                  {
                    matchCurrentSymbol()
                    parseExpression()
                    match(Symbol.rightParen)
                  }
                else -> throw error("Invalid expression.")
              }
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `constValue = literal | constId .`
     */
    fun parseConstValue()
      {
// ...
      }


    /**
     * Parse the following grammar rule:
     * `namedValue = variable .`
     */
    fun parseNamedValue()
      {
        try
          {
            parseVariableExpr()
          }
        catch (e : ParserException)
          {
            ErrorHandler.reportError(e)
            exit()
          }
      }


    /**
     * Parse the following grammar rule:
     * `functionCall = funcId ( actualParameters )? .`
     */
    fun parseFunctionCall()
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
            val errorMsg = ("Expecting \"$expectedSymbol\" but found"
                         + " \"${scanner.token}\" instead.")
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


    /**
     * Print an error message and exit the compilation process.
     */
    private fun exit()
      {
        println("Errors detected -- compilation terminated.")
        println()
        exitProcess(-1)
      }
  }
