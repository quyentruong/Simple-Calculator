# Simple-Calculator

## Algorithm

* Parse expression to seperate numbers and operators
* Convert Infix format to Postfix format by using [Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm#The_algorithm_in_detail)
* Then using [Postfix evaluation algorithm](https://en.wikipedia.org/wiki/Reverse_Polish_notation#Postfix_evaluation_algorithm)

## Special Case

### Negative Number

* **-**(x + y) -> check negative at index 0. Then add **0** before it => **0** - (x+y)
* 3*-2 or (-2+3) -> if **'*'**, **'/'**, **'^'**, **'('** before negative, then push **'-'** to stack. When the next char is number, the new number is stack.pop + number.
* Note: For the '+' sign, just ignore it.

### Multiply between parentheses

* (2)(3) or 3(2) -> if (index > 0) and cur = '(' and ( prev = ')' or prev is number ) => add *
* (2)3 -> if (index < size -1) and cur = ')' and next is number => add *

### Absolute

* Use square bracker '[]' instead of pipe '|' because it's easier to detect the ending of absolute function
* To use binary operation, add '1' and '|' to the postfix when coverting

## Output

Note: Windows does not support ANSI. It means no color in console.

![output](https://github.com/quyentruong/Simple-Calculator/blob/master/images/calculator_output.JPG?raw=true)
