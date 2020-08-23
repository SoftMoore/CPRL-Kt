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
   BGE L4
   LDGADDR 0
   LOADW
   PUTINT
   LDCSTR " is negative"
   PUTSTR
   PUTEOL
   BR L5
L4:
   LDGADDR 0
   LOADW
   LDCINT 0
   CMP
   BNZ L8
   LDGADDR 0
   LOADW
   PUTINT
   LDCSTR " is zero"
   PUTSTR
   PUTEOL
   BR L5
L8:
   LDGADDR 0
   LOADW
   PUTINT
   LDCSTR " is positive"
   PUTSTR
   PUTEOL
L5:
   LDGADDR 0
   LOADW
   LDCINT 0
   CMP
   BZ L1
   BR L0
L1:
   HALT
