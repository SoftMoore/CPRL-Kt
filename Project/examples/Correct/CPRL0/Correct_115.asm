   PROGRAM 8
   LDCSTR "Enter value for x: "
   PUTSTR
   LDGADDR 0
   GETINT
   STOREW
   LDCSTR "Enter value for y: "
   PUTSTR
   LDGADDR 4
   GETINT
   STOREW
   LDCSTR "x + y = "
   PUTSTR
   LDGADDR 0
   LOADW
   LDGADDR 4
   LOADW
   ADD
   PUTINT
   PUTEOL
   HALT
