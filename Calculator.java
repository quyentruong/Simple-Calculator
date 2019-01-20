import java.util.ArrayList;
import java.util.Stack;

public class Calculator {
    public static void main(String[] args) {

        String exp = "((1.5+2)*3-4)*5 ^4";
        System.out.println(parseExp(exp));
        System.out.println(infixToPostFix(parseExp(exp)));
        System.out.println(postfixExp(exp));
    }

    private static ArrayList<String> parseExp(String exp) {
        exp = exp.replaceAll(" ", "");
        ArrayList<String> result = new ArrayList<>();
        StringBuffer number = new StringBuffer();
        for (char ch : exp.toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') {
                number.append(ch);
            } else if (priority(ch) >= -1) {
                if (number.length() > 0) {
                    result.add(number.toString());
                    number.delete(0, number.length());
                }
                result.add(ch + "");
            } else {
                throw new IllegalArgumentException("Invalid Input");
            }
        }
        if (number.length() > 0)
            result.add(number.toString());
        return result;
    }

    private static boolean isNumber(String s) {
        return s.matches("\\d+\\.?\\d*");
    }

    private static int priority(char ch) {
        switch (ch) {
            case '^':
                return 3;
            case '*':
            case '/':
            case '%':
                return 2;
            case '+':
            case '-':
                return 1;
            case '(':
            case ')':
                return -1;
            default:
                return -3;
        }
    }

    public static ArrayList<String> infixToPostFix(ArrayList<String> exp) {
        Stack<String> stack = new Stack<>();
        ArrayList<String> output = new ArrayList<>();
        for (String s : exp) {
            if (isNumber(s)) { // If s is number
                output.add(s);
            }
            // if s is operator
            // compare order of operators
            // while operator in stack is greater or equal current operator
            // output += stack.pop
            // then stack.push current operator
            else if (priority(s.charAt(0)) > 0) {
                while (!stack.empty() && (priority(stack.peek().charAt(0)) >= priority(s.charAt(0)))) {
                    output.add(stack.pop());
                }
                stack.push(s);
            } else if (s.charAt(0) == '(') { // if s is (
                stack.push(s);
            }
            // if s is )
            // output += stack.pop until find (
            else if (s.charAt(0) == ')') {
                while (!stack.empty() && stack.peek().charAt(0) != '(') {
                    output.add(stack.pop());
                }
                stack.pop();
            }
        }
        // stack.pop the remaining to output
        while (!stack.empty())
            output.add(stack.pop());
        return output;
    }

    private static double cal(double x, double y, char op) {
        switch (op) {
            case '^':
                return Math.pow(x, y);
            case '+':
                return x + y;
            case '-':
                return x - y;
            case '*':
                return x * y;
            case '/':
                return x / y;
            case '%':
                return x % y;
        }
        return -1;
    }

    public static double postfixExp(String exp) {
        Stack<String> stack = new Stack<>();
        for (String s : infixToPostFix(parseExp(exp))) {
            if (isNumber(s)) {
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
