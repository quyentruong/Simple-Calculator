//for each token in the postfix expression:
//      if token is an operator:
//        operand_2 ← pop from the stack
//        operand_1 ← pop from the stack
//        result ← evaluate token with operand_1 and operand_2
//        push result back onto the stack
//      else if token is an operand:
//        push token onto the stack
// result ← pop from the stack


import java.lang.reflect.Array;
import java.util.*;

class ShuntingYard {

//    public static void main(String[] args) {
//        String infix = "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3";
//        System.out.printf("infix:   %s%n", infix);
//        System.out.printf("postfix: %s%n", infixToPostfix(infix));
//    }

    static String infixToPostfix(String infix) {
        /* To find out the precedence, we take the index of the
           token in the ops string and divide by 2 (rounding down).
           This will give us: 0, 0, 1, 1, 2 */
        final String ops = "-+/*^";

        StringBuilder sb = new StringBuilder();
        Stack<Integer> s = new Stack<>();

        for (String token : infix.split("\\s")) {
            if (token.isEmpty())
                continue;
            char c = token.charAt(0);
            int idx = ops.indexOf(c);

            // check for operator
            if (idx != -1) {
                if (s.isEmpty())
                    s.push(idx);

                else {
                    while (!s.isEmpty()) {
                        int prec2 = s.peek() / 2;
                        int prec1 = idx / 2;
                        if (prec2 > prec1 || (prec2 == prec1 && c != '^'))
                            sb.append(ops.charAt(s.pop())).append(' ');
                        else break;
                    }
                    s.push(idx);
                }
            } else if (c == '(') {
                s.push(-2); // -2 stands for '('
            } else if (c == ')') {
                // until '(' on stack, pop operators.
                while (s.peek() != -2)
                    sb.append(ops.charAt(s.pop())).append(' ');
                s.pop();
            } else {
                sb.append(token).append(' ');
            }
        }
        while (!s.isEmpty())
            sb.append(ops.charAt(s.pop())).append(' ');
        return sb.toString();
    }
}

public class Calculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String s = input.nextLine();

        Stack<Integer> number = new Stack<>();
        List<String> arrayList = new ArrayList<>();
        for (char ch : s.toCharArray()) {
            if (ch >= 48 && ch <= 57 || ch == '.') {
                number.push(Character.getNumericValue(ch));
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '(' || ch == ')' || ch == '/' || ch ==' ') {
                StringBuffer number_s = new StringBuffer();
                while (!number.empty()) {
                    number_s.insert(0, number.pop());
                }
                if (number_s.length() != 0)
                    arrayList.add(number_s.toString());
                arrayList.add(Character.toChars(ch)[0] + "");
            }else{
                System.out.println("There is a error in your input");
                return;
            }
        }
        StringBuffer number_s = new StringBuffer();
        while (!number.empty()) {
            number_s.insert(0, number.pop());
        }
        arrayList.add(number_s.toString());

//        String f = "5 + 3 * 2 + ( 2 - 4 ) * 5";
//        System.out.println(ShuntingYard.infixToPostfix(s));
//        String[] exp = ShuntingYard.infixToPostfix(s).split(" ");
//        String[] exp = "3 4 2 x 1 5 - 2 3 ^ ^ / +".split(" ");
        ;
        String[] array = arrayList.toArray(new String[0]);
        System.out.println(Arrays.toString(array));
        String exp = String.join(" ", array);
        exp = ShuntingYard.infixToPostfix(exp);
        System.out.println(exp);
        System.out.println(postfixExp(exp.split(" ")));

    }

    private static boolean operator(char ch) {
        return ch == '+' || ch == '-' || ch == 'x' || ch == '*' || ch == '/' || ch == '%' || ch == '^';
    }

    private static double cal(double x, double y, char op) {
        double result = 0;
        switch (op) {
            case '^':
                result = Math.pow(x, y);
                break;
            case '+':
                result = x + y;
                break;
            case '-':
                result = x - y;
                break;
            case 'x':
                result = x * y;
                break;
            case '*':
                result = x * y;
                break;
            case '/':
                result = x / y;
                break;
            case '%':
                result = x % y;
                break;
        }
        return result;
    }

    public static double postfixExp(String[] exp) {
        Stack<String> stack = new Stack<>();
        for (String s : exp) {
            if (!operator(s.charAt(0))) {
                stack.push(s);
            } else {
                char op = s.charAt(0);
                double y = Double.valueOf(stack.pop());
                double x = Double.valueOf(stack.pop());
                double result = cal(x, y, op);
                stack.push(String.valueOf(result));
            }
        }
        return Double.valueOf(stack.pop());
    }
}
