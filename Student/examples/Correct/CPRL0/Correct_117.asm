   PROGRAM 4
   LDCSTR "Enter value for x: "
   PUTSTR
   LDGADDR 0
   GETINT
   STOREW
   LDGADDR 0
   LOADW
   LDCINT 5
   CMP
   BG L0
   LDCB 1
   BR L1
L0:
   LDCB 0
L1:
   PUTBYTE
   HALT
