package edu.citadel.cvm.assembler


import edu.citadel.compiler.ErrorHandler
import edu.citadel.compiler.ParserException
import edu.citadel.cvm.assembler.ast.*

import java.util.*


/**
 * This class uses recursive descent to perform syntax analysis of the source language.
 *
 * @constructor Construct a parser with the specified scanner.
*/
class Parser(private val scanner : Scanner)
  {
    // program = ( instruction )* .
    fun parseProgram() : Program
      {
        val prog = Program()

        try
          {
            // NOTE:  identifier is not a valid starter for an instruction,
            // but we handle it as a special case in order to give better
            // error reporting/recovery when an opcode mnemonic is misspelled
            var symbol = scanner.symbol
            while (symbol.isOpCode
                    || symbol == Symbol.labelId
                    || symbol == Symbol.identifier)
              {
                val inst = parseInstruction()
                prog.addInstruction(inst!!)
                symbol = scanner.symbol
              }

            match(Symbol.EOF)
          }
        catch (e: ParserException)
          {
            ErrorHandler.reportError(e)
            val followers = arrayOf(Symbol.EOF)
            scanner.advanceTo(followers)
          }

        return prog
      }


    // instruction = ( labelId )* opCodeMnemonic (arg)? .
    fun parseInstruction() : Instruction?
      {
        var inst : Instruction? = null

        try
          {
            val labels = LinkedList<Token>()

            while (scanner.symbol == Symbol.labelId)
              {
                labels.add(scanner.token)
                match(Symbol.labelId)
              }

            checkOpCode()
            val opCode = scanner.token
            matchCurrentSymbol()

            var arg: Token? = null
            val numArgs = opCode.symbol.numArgs
            if (numArgs == 1)
              {
                arg = scanner.token
                matchCurrentSymbol()
              }

            inst = makeInstruction(labels, opCode, arg)
          }
        catch (e: ParserException)
          {
            ErrorHandler.reportError(e)
            scanner.advanceTo(instructionFollowers)
          }

        return inst
      }


    private fun makeInstruction(labels: MutableList<Token>, opCode: Token, arg: Token?)
        : Instruction
      {
        checkArgs(opCode, arg)

        when (opCode.symbol)
          {
            Symbol.HALT    -> return InstructionHALT(labels, opCode)
            Symbol.LOAD    -> return InstructionLOAD(labels, opCode, arg!!)
            Symbol.LOADB   -> return InstructionLOADB(labels, opCode)
            Symbol.LOAD2B  -> return InstructionLOAD2B(labels, opCode)
            Symbol.LOADW   -> return InstructionLOADW(labels, opCode)
            Symbol.LDCB    -> return InstructionLDCB(labels, opCode, arg!!)
            Symbol.LDCB0   -> return InstructionLDCB0(labels, opCode)
            Symbol.LDCB1   -> return InstructionLDCB1(labels, opCode)
            Symbol.LDCCH   -> return InstructionLDCCH(labels, opCode, arg!!)
            Symbol.LDCINT  -> return InstructionLDCINT(labels, opCode, arg!!)
            Symbol.LDCINT0 -> return InstructionLDCINT0(labels, opCode)
            Symbol.LDCINT1 -> return InstructionLDCINT1(labels, opCode)
            Symbol.LDCSTR  -> return InstructionLDCSTR(labels, opCode, arg!!)
            Symbol.LDLADDR -> return InstructionLDLADDR(labels, opCode, arg!!)
            Symbol.LDGADDR -> return InstructionLDGADDR(labels, opCode, arg!!)
            Symbol.STORE   -> return InstructionSTORE(labels, opCode, arg!!)
            Symbol.STOREB  -> return InstructionSTOREB(labels, opCode)
            Symbol.STORE2B -> return InstructionSTORE2B(labels, opCode)
            Symbol.STOREW  -> return InstructionSTOREW(labels, opCode)
            Symbol.CMP     -> return InstructionCMP(labels, opCode)
            Symbol.BR      -> return InstructionBR(labels, opCode, arg!!)
            Symbol.BNZ     -> return InstructionBNZ(labels, opCode, arg!!)
            Symbol.BZ      -> return InstructionBZ(labels, opCode, arg!!)
            Symbol.BG      -> return InstructionBG(labels, opCode, arg!!)
            Symbol.BGE     -> return InstructionBGE(labels, opCode, arg!!)
            Symbol.BL      -> return InstructionBL(labels, opCode, arg!!)
            Symbol.BLE     -> return InstructionBLE(labels, opCode, arg!!)
            Symbol.SHL     -> return InstructionSHL(labels, opCode, arg!!)
            Symbol.SHR     -> return InstructionSHR(labels, opCode, arg!!)
            Symbol.NOT     -> return InstructionNOT(labels, opCode)
            Symbol.ADD     -> return InstructionADD(labels, opCode)
            Symbol.SUB     -> return InstructionSUB(labels, opCode)
            Symbol.MUL     -> return InstructionMUL(labels, opCode)
            Symbol.DIV     -> return InstructionDIV(labels, opCode)
            Symbol.MOD     -> return InstructionMOD(labels, opCode)
            Symbol.NEG     -> return InstructionNEG(labels, opCode)
            Symbol.INC     -> return InstructionINC(labels, opCode)
            Symbol.DEC     -> return InstructionDEC(labels, opCode)
            Symbol.GETCH   -> return InstructionGETCH(labels, opCode)
            Symbol.GETINT  -> return InstructionGETINT(labels, opCode)
            Symbol.PUTBYTE -> return InstructionPUTBYTE(labels, opCode)
            Symbol.PUTCH   -> return InstructionPUTCH(labels, opCode)
            Symbol.PUTINT  -> return InstructionPUTINT(labels, opCode)
            Symbol.PUTEOL  -> return InstructionPUTEOL(labels, opCode)
            Symbol.PUTSTR  -> return InstructionPUTSTR(labels, opCode)
            Symbol.PROGRAM -> return InstructionPROGRAM(labels, opCode, arg!!)
            Symbol.PROC    -> return InstructionPROC(labels, opCode, arg!!)
            Symbol.CALL    -> return InstructionCALL(labels, opCode, arg!!)
            Symbol.RET     -> return InstructionRET(labels, opCode, arg!!)
            Symbol.ALLOC   -> return InstructionALLOC(labels, opCode, arg!!)
            Symbol.DEFINT  -> return InstructionDEFINT(labels, opCode, arg!!)
            else           ->
                // force an exception
                throw IllegalArgumentException("Parser.makeInstruction():"
                        + " opcode not handled at position ${opCode.position}")
          }
      }


    // utility parsing methods

    private fun checkOpCode()
      {
        if (!scanner.symbol.isOpCode)
          {
            val errorMessage = "Expecting an opcode but found \"${scanner.symbol}\" instead"
            throw error(errorMessage)
          }
      }


    private fun checkArgs(opCode : Token, arg : Token?)
      {
        val errorPosition = opCode.position
        val symbol  = opCode.symbol
        val numArgs = symbol.numArgs

        if (numArgs == 0)
          {
            if (arg != null)
              {
                val errorMessage = "No arguments allowed for this opcode."
                throw ParserException(errorPosition, errorMessage)
              }
          }
        else if (numArgs == 1)
          {
            if (arg == null)
              {
                val errorMessage = "One argument is required for this opcode."
                throw ParserException(errorPosition, errorMessage)
              }
          }
        else
          {
            val errorMessage = "Invalid number of arguments for opcode $opCode."
            throw ParserException(errorPosition, errorMessage)
          }
      }


    private fun match(expectedSymbol : Symbol)
      {
        if (scanner.symbol == expectedSymbol)
            scanner.advance()
        else
          {
            val errorMessage = "Expecting \"$expectedSymbol\" but found \"${scanner.symbol}\" instead"
            throw error(errorMessage)
          }
      }


    private fun matchCurrentSymbol()
      {
        scanner.advance()
      }


    private fun error(message : String) : ParserException
      {
        val errorPosition = scanner.token.position
        return ParserException(errorPosition, message)
      }


    companion object
      {
        /**
         * Symbols that can follow an assembly language instruction.
         */
        private val instructionFollowers = makeInstructionFollowers()


        /**
         * Returns an array of symbols that can follow an instruction.
         */
        private fun makeInstructionFollowers() : Array<Symbol>
          {
            val followers = LinkedList<Symbol>()

            // add all opcodes
            for (symbol in Symbol.values())
              {
                if (symbol.isOpCode)
                    followers.add(symbol)
              }

            // add labelId and EOF
            followers.add(Symbol.labelId)
            followers.add(Symbol.EOF)

            return followers.toTypedArray()
          }
      }
  }
