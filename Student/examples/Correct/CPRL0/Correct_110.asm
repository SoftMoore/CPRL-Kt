   PROGRAM 4
L0:
   LDCSTR "Enter an integer (0 to exit): "
   PUTSTR
   LDGADDR 0
   GETINT
   STOREW
   LDGADDR 0
   LOADW
   LDCINT 0
   CMP
   BGE L7
   LDGADDR 0
   LOADW
   PUTINT
   LDCSTR " is negative"
   PUTSTR
   PUTEOL
   BR L8
L7:
   LDGADDR 0
   LOADW
   LDCINT 0
   CMP
   BNZ L6
   LDGADDR 0
   LOADW
   PUTINT
   LDCSTR " is zero"
   PUTSTR
   PUTEOL
   BR L8
L6:
   LDGADDR 0
   LOADW
   PUTINT
   LDCSTR " is positive"
   PUTSTR
   PUTEOL
L8:
   LDGADDR 0
   LOADW
   LDCINT 0
   CMP
   BZ L1
   BR L0
L1:
   HALT
