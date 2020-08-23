package edu.citadel.cvm


/**
 * The set of opcodes for the CPRL virtual machine
 */
object OpCode
  {
    // halt opcode
    const val HALT    : Byte = 0

    // load opcodes (move data from memory to top of stack)
    const val LOAD    : Byte = 10
    const val LOADB   : Byte = 11
    const val LOAD2B  : Byte = 12
    const val LOADW   : Byte = 13
    const val LDCB    : Byte = 14
    const val LDCCH   : Byte = 15
    const val LDCINT  : Byte = 16
    const val LDCSTR  : Byte = 17
    const val LDLADDR : Byte = 18
    const val LDGADDR : Byte = 19

    // optimized loads for special constants
    const val LDCB0   : Byte = 20
    const val LDCB1   : Byte = 21
    const val LDCINT0 : Byte = 22
    const val LDCINT1 : Byte = 23

    // store opcodes (move data from top of stack to memory)
    const val STORE   : Byte = 30
    const val STOREB  : Byte = 31
    const val STORE2B : Byte = 32
    const val STOREW  : Byte = 33

    // compare/branch opcodes
    const val CMP     : Byte = 40
    const val BR      : Byte = 41
    const val BNZ     : Byte = 42
    const val BZ      : Byte = 43
    const val BG      : Byte = 44
    const val BGE     : Byte = 45
    const val BL      : Byte = 46
    const val BLE     : Byte = 47

    // shift opcodes
    const val SHL     : Byte = 50
    const val SHR     : Byte = 51

    // logical not opcode
    const val NOT     : Byte = 60

    // arithmetic opcodes
    const val ADD     : Byte = 70
    const val SUB     : Byte = 71
    const val MUL     : Byte = 72
    const val DIV     : Byte = 73
    const val MOD     : Byte = 74
    const val NEG     : Byte = 75
    const val INC     : Byte = 76
    const val DEC     : Byte = 77

    // I/O opcodes
    const val GETCH   : Byte = 80
    const val GETINT  : Byte = 81
    const val PUTBYTE : Byte = 82
    const val PUTCH   : Byte = 83
    const val PUTINT  : Byte = 84
    const val PUTEOL  : Byte = 85
    const val PUTSTR  : Byte = 86

    // program/procedure opcodes
    const val PROGRAM : Byte = 90
    const val PROC    : Byte = 91
    const val CALL    : Byte = 92
    const val RET     : Byte = 93
    const val ALLOC   : Byte = 94


    /**
     * Returns a string representation for a declared opcode.
     * Returns Byte.toString(n) if the argument does not have
     * a value equal to any of the declared opcodes.
     */
    fun toString(n : Byte) : String
      {
        when (n)
          {
            HALT    -> return "HALT"
            LOAD    -> return "LOAD"
            LOADB   -> return "LOADB"
            LOAD2B  -> return "LOAD2B"
            LOADW   -> return "LOADW"
            LDCB    -> return "LDCB"
            LDCCH   -> return "LDCCH"
            LDCINT  -> return "LDCINT"
            LDCSTR  -> return "LDCSTR"
            LDLADDR -> return "LDLADDR"
            LDGADDR -> return "LDGADDR"
            LDCB0   -> return "LDCB0"
            LDCB1   -> return "LDCB1"
            LDCINT0 -> return "LDCINT0"
            LDCINT1 -> return "LDCINT1"
            STORE   -> return "STORE"
            STOREB  -> return "STOREB"
            STORE2B -> return "STORE2B"
            STOREW  -> return "STOREW"
            CMP     -> return "CMP"
            BR      -> return "BR"
            BNZ     -> return "BNZ"
            BZ      -> return "BZ"
            BG      -> return "BG"
            BGE     -> return "BGE"
            BL      -> return "BL"
            BLE     -> return "BLE"
            SHL     -> return "SHL"
            SHR     -> return "SHR"
            NOT     -> return "NOT"
            ADD     -> return "ADD"
            SUB     -> return "SUB"
            MUL     -> return "MUL"
            DIV     -> return "DIV"
            MOD     -> return "MOD"
            NEG     -> return "NEG"
            INC     -> return "INC"
            DEC     -> return "DEC"
            GETCH   -> return "GETCH"
            GETINT  -> return "GETINT"
            PUTBYTE -> return "PUTBYTE"
            PUTCH   -> return "PUTCH"
            PUTINT  -> return "PUTINT"
            PUTEOL  -> return "PUTEOL"
            PUTSTR  -> return "PUTSTR"
            CALL    -> return "CALL"
            PROC    -> return "PROC"
            PROGRAM -> return "PROGRAM"
            RET     -> return "RET"
            ALLOC   -> return "ALLOC"
            else    -> return "$n"
          }
      }
  }
