Hi

I know i didn't follow the assignment exactly, but i had fun with this one so whatever

there are 4 things you can call in the console:

run - runs a program that outputs to a file
ex: "run test.yzy text.txt"
if you make the second argument "here", it outputs to console rather than to a file

yaz - opens up the yaz environment
ex: "yaz"
i wanted to copy the way python did it and this was helpfull during testing so i kept it

read - reads a file
ex: "read test.txt"
idk the usefulness of this when you are able to output to the console or open a yaz environment, but i have too keep it to at least sort of adhere to the assignment specs

exit - exits program


YAZ itself:
All the commands work normally, but in the yaz environment you can also say EXIT to be kicked back out to the yaz console.

Usage of variables:
because i added variables, heres how you use them:
    to set a variable: see SET command
    to use a variable: surround name of variable with apostrophes (example: 'X', 'Tempurature')

Extra Commands:
ADD/SUB/MUL/DIV [number 1] [number 2] - maths two numbers and either prints or returns them

SET [variable to set] [expression, number, or string] - creates and/or sets the variable specified in the first argument to the expression in the second
Ex:
SET tempInF 10 -> tempInF = 10
SET tempInC "CONVERT 'tempInF' F" -> tempInC = -12

CONCAT [string 1] [string 2] - Returns or prints result
Ex:
SET temp "CONVERT 1000 F"
CONCAT "temp in celcius: " 'temp' c -> temp in celcius: 513c

LOOP [number] [expression] - Do thing amount of times (has extra value _IT that tells you the loop iteration)
Note: inside commands, use "/" instead of """

Ex:
LOOP 3 "PRINT _IT" ->
0
1
2

SET X 0
LOOP 5 "SET X /ADD 'X' _IT/" (X becomes 0, then 1, then 3, then 6, then 10)
PRINT 'X' -> 10

3/2/2021
CHANGELOG 0.0.1:
started to use arrays (sorry)

CHANGELOG 0.1.0
added support for loading the environment manually, added EXIT

CHANGELOG 1.0.0
added the exit command outside of the environment

CHANGELOG 1.0.1:
added ADD, SUB, MUL, DIV

CHANGELOG 1.0.2:
added SET, PRINT

CHANGELOG 1.0.3:
added CONCAT

CHANGELOG 1.0.4:
added LOOP, _IT

CHANGELOG 2.0.0:
fixed bugs, added comments in ()

3/3/2021
CHANGELOG 2.0.1
added more error checking, also fixed/added error checking in the non-environment

future possible improvements:
[ ] add arguments with _IN
[ ] compiler? idk how to write assembly yet so idk