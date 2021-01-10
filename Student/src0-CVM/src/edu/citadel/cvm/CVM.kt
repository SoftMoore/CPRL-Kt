package edu.citadel.cvm


import edu.citadel.compiler.util.ByteUtil
import edu.citadel.compiler.util.format

import java.io.*
import java.nio.charset.StandardCharsets
import java.util.Scanner
import kotlin.system.exitProcess


/** exit return value for failure  */
private const val FAILURE = -1

/** 1K = 2**10  */
private const val K = 1024

/** default memory size for the virtual machine */
private const val DEFAULT_MEMORY_SIZE = 8*K


/**
 * This method constructs a CPRL virtual machine, loads the byte code
 * from the specified file into memory, and runs the byte code.
 *
 * Usage: java CVM filename
 *
 * where filename is the name of a file containing the byte code
 * for a CPRL program.
 */
fun main(args : Array<String>)
  {
    if (args.size != 1)
      {
        System.err.println("Usage: java CVM filename")
        exitProcess(FAILURE)   // stop the VM with a nonzero status code
      }

    val sourceFile = File(args[0])

    if (!sourceFile.isFile)
      {
        System.err.println("*** File " + args[0] + " not found ***")
        exitProcess(FAILURE)
      }

    val codeFile = FileInputStream(sourceFile)

    val vm = CVM(DEFAULT_MEMORY_SIZE)
    vm.loadProgram(codeFile)
    vm.run()
  }


/**
 * This class implements a virtual machine for the programming language CPRL.
 * It interprets instructions for a hypothetical CPRL computer.
 *
 * @param numOfBytes the number of bytes in memory of the virtual machine
 */
class CVM (numOfBytes : Int)
  {
    /** scanner for handling basic integer I/O  */
    private val scanner = Scanner(System.`in`)

    /** Reader for handling basic char I/O  */
    private val reader = InputStreamReader(System.`in`, StandardCharsets.UTF_8)

    /** PrintStream for handling output */
    private val out = PrintStream(System.out, true, StandardCharsets.UTF_8);

    /** computer memory (for the virtual CPRL machine)  */
    private val memory = ByteArray(numOfBytes)

    /** program counter (index of the next instruction in memory)  */
    private var pc = 0

    /** base pointer  */
    private var bp = 0

    /** stack pointer (index of the top of the stack)  */
    private var sp = 0

    /** bottom of the stack  */
    private var sb = 0

    /** true if the virtual computer is currently running  */
    private var running = false


    /**
     * Loads the program into memory.
     *
     * @param codeFile the FileInputStream containing the object code
     */
    fun loadProgram(codeFile : FileInputStream)
      {
        var address = 0
        var inByte : Int

        try
          {
            inByte = codeFile.read()
            while (inByte != -1)
            {
              memory[address++] = inByte.toByte()
              inByte = codeFile.read()
            }

            bp = address
            sb = address
            sp = bp - 1
            codeFile.close()
          }
        catch (e : IOException)
          {
            error(e.toString())
          }
      }


    /**
     * Prints values of internal registers to standard output.
     */
    private fun printRegisters()
      {
        out.println("PC=$pc, BP=$bp, SB=$sb, SP=$sp")
      }


    /**
     * Prints a view of memory to standard output.
     */
    private fun printMemory()
      {
        var memAddr = 0
        var strLength : Int
        var byte0: Byte
        var byte1: Byte
        var byte2: Byte
        var byte3: Byte

        while (memAddr < sb)
          {
            // Prints "PC ->" in front of the correct memory address
            if (pc == memAddr)
                out.print("PC ->")
            else
                out.print("     ")

            val memAddrStr = format(memAddr, FIELD_WIDTH)

            when (val opCode = memory[memAddr])
              {
                // opcodes with zero operands
                OpCode.ADD,
                OpCode.CMP,
                OpCode.DEC,
                OpCode.DIV,
                OpCode.GETCH,
                OpCode.GETINT,
                OpCode.HALT,
                OpCode.LOADB,
                OpCode.LOAD2B,
                OpCode.LOADW,
                OpCode.INC,
                OpCode.MOD,
                OpCode.MUL,
                OpCode.NEG,
                OpCode.NOT,
                OpCode.PUTBYTE,
                OpCode.PUTCH,
                OpCode.PUTINT,
                OpCode.PUTEOL,
                OpCode.PUTSTR,
                OpCode.STOREB,
                OpCode.STORE2B,
                OpCode.STOREW,
                OpCode.SUB    ->
                  {
                    out.println("$memAddrStr:  ${OpCode.toString(opCode)}")
                    ++memAddr
                  }

                // opcodes with one byte operand
                OpCode.SHL,
                OpCode.SHR,
                OpCode.LDCB   ->
                  {
                    out.print("$memAddrStr:  ${OpCode.toString(opCode)}")
                    ++memAddr
                    out.println(" ${memory[memAddr++]}")
                  }

                // opcodes with one int operand
                OpCode.ALLOC,
                OpCode.BR,
                OpCode.BG,
                OpCode.BGE,
                OpCode.BL,
                OpCode.BLE,
                OpCode.BNZ,
                OpCode.BZ,
                OpCode.CALL,
                OpCode.LOAD,
                OpCode.LDCINT,
                OpCode.LDLADDR,
                OpCode.LDGADDR,
                OpCode.PROC,
                OpCode.PROGRAM,
                OpCode.RET,
                OpCode.STORE  ->
                  {
                    out.print("$memAddrStr:  ${OpCode.toString(opCode)}")
                    ++memAddr
                    byte0 = memory[memAddr++]
                    byte1 = memory[memAddr++]
                    byte2 = memory[memAddr++]
                    byte3 = memory[memAddr++]
                    out.println(" ${ByteUtil.bytesToInt(byte0, byte1, byte2, byte3)}")
                  }

                // special case:  LDCCH
                OpCode.LDCCH  ->
                  {
                    out.print("$memAddrStr:  ${OpCode.toString(opCode)}")
                    ++memAddr
                    byte0 = memory[memAddr++]
                    byte1 = memory[memAddr++]
                    out.println(" ${ByteUtil.bytesToChar(byte0, byte1)}")
                  }

                // special case:  LDCSTR
                OpCode.LDCSTR ->
                  {
                    out.print("$memAddrStr:  ${OpCode.toString(opCode)}")
                    ++memAddr
                    // now print the string
                    out.print("  \"")
                    byte0 = memory[memAddr++]
                    byte1 = memory[memAddr++]
                    byte2 = memory[memAddr++]
                    byte3 = memory[memAddr++]
                    strLength = ByteUtil.bytesToInt(byte0, byte1, byte2, byte3)

                    for (i in 0 until strLength)
                      {
                        byte0 = memory[memAddr++]
                        byte1 = memory[memAddr++]
                        out.print(ByteUtil.bytesToChar(byte0, byte1))
                      }

                    out.println("\"")
                  }

                else -> out.println("*** Unknown opCode ***")
              }
          }

        // now print remaining values that compose the stack
        memAddr = sb
        while (memAddr <= sp)
          {
            // Prints "SB ->", "BP ->", and "SP ->" in front of the correct memory address
            when
              {
                sb == memAddr -> out.print("SB ->")
                bp == memAddr -> out.print("BP ->")
                sp == memAddr -> out.print("SP ->")
                else          -> out.print("     ")
              }

            val memAddrStr = format(memAddr, FIELD_WIDTH)
            out.println("$memAddrStr:  ${memory[memAddr]}")
            ++memAddr
          }

        out.println()
    }


    /**
     * Prompt user and wait for user to press the enter key.
     */
    private fun pause()
      {
        out.println("Press enter to continue...")
        try
          {
            System.`in`.read()
          }
        catch (ex : IOException)
          {
            // ignore
          }
    }


    /**
     * Runs the program currently in memory.
     */
    fun run()
      {
        var opCode : Byte

        running = true
        pc = 0
        while (running)
          {
            if (DEBUG)
              {
                printRegisters()
                printMemory()
                pause()
              }

          opCode = fetchByte()

          when (opCode)
            {
              OpCode.ADD     -> add()
              OpCode.ALLOC   -> allocate()
              OpCode.BG      -> branchGreater()
              OpCode.BGE     -> branchGreaterOrEqual()
              OpCode.BL      -> branchLess()
              OpCode.BLE     -> branchLessOrEqual()
              OpCode.BNZ     -> branchNonZero()
              OpCode.BR      -> branch()
              OpCode.BZ      -> branchZero()
              OpCode.CALL    -> call()
              OpCode.CMP     -> compare()
              OpCode.DEC     -> decrement()
              OpCode.DIV     -> divide()
              OpCode.GETCH   -> getCh()
              OpCode.GETINT  -> getInt()
              OpCode.HALT    -> halt()
              OpCode.INC     -> increment()
              OpCode.LDCB    -> loadConstByte()
              OpCode.LDCB0   -> loadConstByteZero()
              OpCode.LDCB1   -> loadConstByteOne()
              OpCode.LDCCH   -> loadConstCh()
              OpCode.LDCINT  -> loadConstInt()
              OpCode.LDCINT0 -> loadConstIntZero()
              OpCode.LDCINT1 -> loadConstIntOne()
              OpCode.LDCSTR  -> loadConstStr()
              OpCode.LDLADDR -> loadLocalAddress()
              OpCode.LDGADDR -> loadGlobalAddress()
              OpCode.LOAD    -> load()
              OpCode.LOADB   -> loadByte()
              OpCode.LOAD2B  -> load2Bytes()
              OpCode.LOADW   -> loadWord()
              OpCode.MOD     -> modulo()
              OpCode.MUL     -> multiply()
              OpCode.NEG     -> negate()
              OpCode.NOT     -> not()
              OpCode.PROC    -> procedure()
              OpCode.PROGRAM -> program()
              OpCode.PUTBYTE -> putByte()
              OpCode.PUTCH   -> putChar()
              OpCode.PUTEOL  -> putEOL()
              OpCode.PUTINT  -> putInt()
              OpCode.PUTSTR  -> putString()
              OpCode.RET     -> returnInst()
              OpCode.SHL     -> shiftLeft()
              OpCode.SHR     -> shiftRight()
              OpCode.STORE   -> store()
              OpCode.STOREB  -> storeByte()
              OpCode.STORE2B -> store2Bytes()
              OpCode.STOREW  -> storeWord()
              OpCode.SUB     -> subtract()
              else           -> error("invalid machine instruction")
            }
        }
    }


    /**
     * Pop the top byte off the stack and return its value.
     */
    private fun popByte() : Byte
      {
        return memory[sp--]
      }


    /**
     * Pop the top character off the stack and return its value.
     */
    private fun popChar() : Char
      {
        val b1 = popByte()
        val b0 = popByte()

        return ByteUtil.bytesToChar(b0, b1)
      }


    /**
     * Pop the top integer off the stack and return its value.
     */
    private fun popInt() : Int
      {
        val b3 = popByte()
        val b2 = popByte()
        val b1 = popByte()
        val b0 = popByte()

        return ByteUtil.bytesToInt(b0, b1, b2, b3)
      }


    /**
     * Push a byte onto the stack.
     */
    private fun pushByte(b : Byte)
      {
        memory[++sp] = b
      }


    /**
     * Push a character onto the stack.
     */
    private fun pushChar(c : Char)
      {
        val bytes = ByteUtil.charToBytes(c)
        pushByte(bytes[0])
        pushByte(bytes[1])
      }


    /**
     * Push an integer onto the stack.
     */
    private fun pushInt(n : Int)
      {
        val bytes = ByteUtil.intToBytes(n)
        pushByte(bytes[0])
        pushByte(bytes[1])
        pushByte(bytes[2])
        pushByte(bytes[3])
      }


    /**
     * Fetch the next instruction/byte from memory.
     */
    private fun fetchByte() : Byte
      {
        return memory[pc++]
      }


    /**
     * Fetch the next instruction int operand from memory.
     */
    private fun fetchInt() : Int
      {
        val b0 = fetchByte()
        val b1 = fetchByte()
        val b2 = fetchByte()
        val b3 = fetchByte()

        return ByteUtil.bytesToInt(b0, b1, b2, b3)
      }


    /**
     * Fetch the next instruction currentChar operand from memory.
     */
    private fun fetchChar() : Char
      {
        val b0 = fetchByte()
        val b1 = fetchByte()

        return ByteUtil.bytesToChar(b0, b1)
      }


    /**
     * Returns the integer at the specified memory address.
     * Does not alter pc, sp, or bp.
     */
    private fun getInt(address: Int) : Int
      {
        val b0 = memory[address + 0]
        val b1 = memory[address + 1]
        val b2 = memory[address + 2]
        val b3 = memory[address + 3]

        return ByteUtil.bytesToInt(b0, b1, b2, b3)
      }


    //----------------------------------------------------------------------
    // End:  internal machine instructions that do NOT correspond to OpCodes


    // Start:  machine instructions corresponding to OpCodes
    //------------------------------------------------------

    private fun add()
      {
        val operand2 = popInt()
        val operand1 = popInt()
        pushInt(operand1 + operand2)
      }


    private fun allocate()
      {
        val numBytes = fetchInt()
        sp = sp + numBytes
      }


    /**
     * Unconditional branch.
     */
    private fun branch()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()
        pc = opCodeAddr + displacement
      }


    private fun branchGreater()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()
        val value = popByte()

        if (value > 0)
            pc = opCodeAddr + displacement
      }


    private fun branchGreaterOrEqual()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()
        val value = popByte()

        if (value >= 0)
            pc = opCodeAddr + displacement
      }


    private fun branchLess()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()
        val value = popByte()

        if (value < 0)
            pc = opCodeAddr + displacement
      }


    private fun branchLessOrEqual()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()
        val value = popByte()

        if (value <= 0)
            pc = opCodeAddr + displacement
      }


    /**
     * Branch if the byte on the top of the stack is nonzero (true).
     */
    private fun branchNonZero()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()
        val value = popByte()

        if (value.toInt() != 0)
            pc = opCodeAddr + displacement
      }


    /**
     * Branch if ZF (zero flag) is true.
     */
    private fun branchZero()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()
        val value = popByte()

        if (value.toInt() == 0)
            pc = opCodeAddr + displacement
      }


    private fun call()
      {
        val opCodeAddr   = pc - 1
        val displacement = fetchInt()

        pushInt(bp)          // dynamic link
        pushInt(pc)          // return address

        // set bp to starting address of new frame
        bp = sp - Constants.BYTES_PER_FRAME + 1

        // set pc to first statement of called procedure
        pc = opCodeAddr + displacement
      }


    private fun compare()
      {
        val operand2 = popInt()
        val operand1 = popInt()

        if (operand1 == operand2)
            pushByte(ZERO)
        else if (operand1 > operand2)
            pushByte(ONE)
        else
            pushByte(MINUS_ONE)
      }


    private fun decrement()
      {
        val operand = popInt()
        pushInt(operand - 1)
      }


    private fun divide()
      {
        val operand2 = popInt()
        val operand1 = popInt()

        if (operand2 != 0)
          pushInt(operand1/operand2)
        else
          error("*** FAULT:  Divide by zero ***")
      }


    private fun getInt()
      {
        try
          {
            val n = scanner.nextInt()
            pushInt(n)
          }
        catch (e: NumberFormatException)
          {
            error("Invalid input")
          }
      }


    private fun getCh()
      {
        try
          {
            val ch = reader.read()

            if (ch == EOF)
              error("Invalid input: EOF")

            pushChar(ch.toChar())
          }
        catch (ex : IOException)
          {
            ex.printStackTrace()
            error("Invalid input")
          }
      }


    private fun halt()
      {
        running = false
      }

    private fun increment()
      {
        val operand = popInt()
        pushInt(operand + 1)
      }


    /**
     * Loads a multibyte variable onto the stack.  The number of bytes
     * is an argument of the instruction, and the address of the
     * variable is obtained by popping it off the top of the stack.
     */
    private fun load()
      {
        val length  = fetchInt()
        val address = popInt()

        for (i in 0 until length)
          pushByte(memory[address + i])
      }


    private fun loadConstByte()
      {
        val b = fetchByte()
        pushByte(b)
      }


    private fun loadConstByteZero()
      {
        pushByte(0.toByte())
      }


    private fun loadConstByteOne()
      {
        pushByte(1.toByte())
      }


    private fun loadConstCh()
      {
        val ch = fetchChar()
        pushChar(ch)
      }


    private fun loadConstInt()
      {
        val value = fetchInt()
        pushInt(value)
      }

    private fun loadConstIntZero()
      {
        pushInt(0)
      }


    private fun loadConstIntOne()
      {
        pushInt(1)
      }


    private fun loadConstStr()
      {
        val strLength = fetchInt()
        val strAddr   = pc

        pushInt(strLength)
        pushInt(strAddr)
        pc = pc + 2*strLength
      }


    private fun loadLocalAddress()
      {
        val displacement = fetchInt()
        pushInt(bp + displacement)
      }


    private fun loadGlobalAddress()
      {
        val displacement = fetchInt()
        pushInt(sb + displacement)
      }


    /**
     * Loads a single byte onto the stack.  The address of the
     * byte is obtained by popping it off the top of the stack.
     */
    private fun loadByte()
      {
        val address = popInt()
        val b = memory[address]
        pushByte(b)
      }


    /**
     * Loads two bytes onto the stack.  The address of the first
     * byte is obtained by popping it off the top of the stack.
     */
    private fun load2Bytes()
      {
        val address = popInt()

        val b0 = memory[address + 0]
        val b1 = memory[address + 1]

        pushByte(b0)
        pushByte(b1)
      }


    /**
     * Loads a single word-size variable (four bytes) onto the stack.  The address
     * of the variable is obtained by popping it off the top of the stack.
     */
    private fun loadWord()
      {
        val address = popInt()

        val b0 = memory[address + 0]
        val b1 = memory[address + 1]
        val b2 = memory[address + 2]
        val b3 = memory[address + 3]

        val value = ByteUtil.bytesToInt(b0, b1, b2, b3)
        pushInt(value)
      }


    private fun modulo()
      {
        val operand2 = popInt()
        val operand1 = popInt()
        pushInt(operand1%operand2)
      }


    private fun multiply()
      {
        val operand2 = popInt()
        val operand1 = popInt()
        pushInt(operand1*operand2)
      }


    private fun negate()
      {
        val operand1 = popInt()
        pushInt(-operand1)
      }


    private operator fun not()
      {
        val operand = popByte()

        if (operand == FALSE)
            pushByte(TRUE)
        else
            pushByte(FALSE)
      }


    private fun procedure()
      {
        allocate()
      }


    private fun program()
      {
        val varLength = fetchInt()

        bp = sb
        sp = bp + varLength - 1

        if (sp >= memory.size)
            error("*** Out of memory ***")
      }


    private fun putChar()
      {
        out.print(popChar())
      }


    private fun putByte()
      {
        out.print(popByte())
      }


    private fun putInt()
      {
        out.print(popInt())
      }


    private fun putEOL()
      {
        out.println()
      }


    private fun putString()
      {
        var strAddr   = popInt()
        val strLength = popInt()

        val str = CharArray(strLength)

        for (i in 0 until strLength)
          {
            val b0 = memory[strAddr++]
            val b1 = memory[strAddr++]

            str[i] = ByteUtil.bytesToChar(b0, b1)
          }

        out.print(str)
      }


    private fun shiftLeft()
      {
        val operand = popInt()

        // zero out left three bits of shiftAmount
        val mask : Byte = 0x1F   // = 00011111 in binary
        val shiftAmount = (fetchByte().toInt() and mask.toInt())

        pushInt(operand shl shiftAmount)
      }


    private fun shiftRight()
      {
        val operand = popInt()

        // zero out left three bits of shiftAmount
        val mask : Byte = 0x1F   // = 00011111 in binary
        val shiftAmount = (fetchByte().toInt() and mask.toInt())

        pushInt(operand shr shiftAmount)
      }


    private fun returnInst()
      {
        val bpSave = bp
        val paramLength = fetchInt()

        sp = bpSave - paramLength - 1
        bp = getInt(bpSave)
        pc = getInt(bpSave + Constants.BYTES_PER_INTEGER)
      }


    private fun store()
      {
        val length = fetchInt()
        val data   = ByteArray(length)

        // pop bytes of data, storing in reverse order
        for (i in length - 1 downTo 0)
            data[i] = popByte()

        val destAddr = popInt()

        for (i in 0 until length)
            memory[destAddr + i] = data[i]
      }


    private fun storeByte()
      {
        val value    = popByte()
        val destAddr = popInt()

        memory[destAddr] = value
      }


    private fun store2Bytes()
      {
        val byte1    = popByte()
        val byte0    = popByte()
        val destAddr = popInt()

        memory[destAddr + 0] = byte0
        memory[destAddr + 1] = byte1
      }


    private fun storeWord()
      {
        val value    = popInt()
        val destAddr = popInt()

        val bytes = ByteUtil.intToBytes(value)

        memory[destAddr + 0] = bytes[0]
        memory[destAddr + 1] = bytes[1]
        memory[destAddr + 2] = bytes[2]
        memory[destAddr + 3] = bytes[3]
      }


    private fun subtract()
      {
        val operand2 = popInt()
        val operand1 = popInt()
        val result   = operand1 - operand2

        pushInt(result)
      }


    // End:  machine instructions corresponding to OpCodes
    //----------------------------------------------------

    companion object
      {
        private const val DEBUG = false

        /** virtual machine constant for false  */
        private const val FALSE = 0.toByte()

        /** virtual machine constant for true  */
        private const val TRUE = 1.toByte()

        /** virtual machine constant for byte value 0  */
        private const val ZERO = 0.toByte()

        /** virtual machine constant for byte value 1  */
        private const val ONE = 1.toByte()

        /** virtual machine constant for byte value -1  */
        private const val MINUS_ONE = (-1).toByte()

        /** end of file  */
        private const val EOF = -1

        /** field width for printing memory addresses  */
        private const val FIELD_WIDTH = 4
      }
  }
