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
   LDCSTR "x = "
   PUTSTR
   LDGADDR 0
   LOADW
   PUTINT
   PUTEOL
   LDCSTR "y = "
   PUTSTR
   LDGADDR 4
   LOADW
   PUTINT
   PUTEOL
   LDGADDR 0
   LOADW
   LDGADDR 4
   LOADW
   CMP
   BG L2
   LDCSTR "x <= y"
   PUTSTR
   PUTEOL
   BR L3
L2:
   LDCSTR "x > y"
   PUTSTR
   PUTEOL
L3:
   HALT
