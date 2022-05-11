   PROGRAM 8
   LDCB 0
   LDCB 0
   LDCB 0
   LDCB 0
   LDCB 0
   LDCB 0
   LDCB 0
   LDCB 1
   CMP
   BGE L2
   LDCSTR "false < true"
   PUTSTR
   PUTEOL
   BR L3
L2:
   LDCSTR "false >= true"
   PUTSTR
   PUTEOL
L3:
   LDCB 0
   LDCB 0
   LDCCH 'a'
   LDCB 0
   LDCB 0
   LDCCH 'b'
   CMP
   BGE L6
   LDCSTR "'a' < 'b'"
   PUTSTR
   PUTEOL
   BR L7
L6:
   LDCSTR "'a' >= 'b'"
   PUTSTR
   PUTEOL
L7:
   HALT
