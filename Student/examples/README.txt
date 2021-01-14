Test file NonAscii-UTF-8.cprl in directory examples/Correct/CPRL0 contains UTF-8 encoded
characters that will not display properly in text editors that do non support UTF-8.  On
a Windows system, the characters should display properly in Notepad and Notepad++, but
possibly not in other text editors.

The same issue also exists in the default Windows Command Prompt (or Windows Terminal).
For example, if you "type" the file to the console or if you run the compiled/assembled
object code, it will not display properly.  To display the characters correctly, first
make sure that the console font can display Unicode.  Truetype fonts such as Consolas and
Lucida Console will work.  Second, enter the command "chcp 65001" to change the code page
before displaying the test file or running the compiled/assembled object code.

This issue does not affect the correct functioning of a correct CPRL compiler or the CVM
assembler.  You should still be able to compile, assemble, and test this CPRL program
without errors.
