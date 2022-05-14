package edu.citadel.cvm.assembler


/**
 * This class encapsulates the symbols (also known as token types)
 * for the CVM assembly language.  The default constructor creates
 * a Symbol with its label and number of arguments.
 *
 * @constructor Construct a symbol with its label and number of arguments.
 */
enum class Symbol(private val label : String, val numArgs : Int = 0)
  {
    // halt opcode
    HALT("HALT", 0),

    // load/store opcodes
    LOAD("LOAD", 1),
    LOADB("LOADB", 0),
    LOAD2B("LOAD2B", 0),
    LOADW("LOADW", 0),
    LDCB("LDCB", 1),
    LDCCH("LDCCH", 1),
    LDCINT("LDCINT", 1),
    LDCSTR("LDCSTR", 1),
    LDLADDR("LDLADDR", 1),
    LDGADDR("LDGADDR", 1),

    LDCB0("LDCB0", 0),
    LDCB1("LDCB1", 0),
    LDCINT0("LDCINT0", 0),
    LDCINT1("LDCINT1", 0),

    STORE("STORE", 1),
    STOREB("STOREB", 0),
    STORE2B("STORE2B", 0),
    STOREW("STOREW", 0),

    // compare/branch opcodes
    CMP("CMP", 0),
    BR("BR", 1),
    BNZ("BNZ", 1),
    BZ("BZ", 1),
    BG("BG", 1),
    BGE("BGE", 1),
    BL("BL", 1),
    BLE("BLE", 1),

    // shift opcodes
    SHL("SHL", 1),
    SHR("SHR", 1),

    // logical not opcode
    NOT("NOT", 0),

    // arithmetic opcodes
    ADD("ADD", 0),
    SUB("SUB", 0),
    MUL("MUL", 0),
    DIV("DIV", 0),
    MOD("MOD", 0),
    NEG("NEG", 0),
    INC("INC", 0),
    DEC("DEC", 0),

    // I/O opcodes
    GETCH("GETCH", 0),
    GETINT("GETINT", 0),
    PUTBYTE("PUTBYTE", 0),
    PUTCH("PUTCH", 0),
    PUTINT("PUTINT", 0),
    PUTEOL("PUTEOL", 0),
    PUTSTR("PUTSTR", 0),

    // program/procedure opcodes
    PROGRAM("PROGRAM", 1),
    PROC("PROC", 1),
    CALL("CALL", 1),
    RET("RET", 1),
    ALLOC("ALLOC", 1),

    // pseudo opcodes
    DEFINT("DEFINT", 1),
    //    DEFCHAR("DEFCHAR", 1),
    //    DEFBOOL("DEFBOOL", 1),

    // unknown symbol (first symbol that is not an opcode)
    UNKNOWN("UNKNOWN"),

    // literal values and identifier symbols
    intLiteral("intLiteral"),
    stringLiteral("stringLiteral"),
    charLiteral("charLiteral"),
    identifier("identifier"),
    labelId("labelId"),

    // special scanning symbols
    EOF("EOF");


    val isOpCode : Boolean
        get() = this in HALT..DEFINT


    /**
     * Returns the label for this Symbol.
     */
    override fun toString() : String
      {
        return label
      }
  }
