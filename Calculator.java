import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        System.out.println("Welcome to my program - Calculator");
        System.out.println("Operator: " + ANSI_YELLOW + "+ - * / % ^ ( )" + ANSI_RESET);
        System.out.println("Example: " + ANSI_CYAN + "5 + (4 - 7) * 9^2" + ANSI_RESET);
        while (true) {
            System.out.print("Enter your math expression: ");
            try {
                input = bufferedReader.readLine();
                if (input.equalsIgnoreCase("stop"))
                    break;
                System.out.println("PostFix: " + ANSI_GREEN + String.join(" ", infixToPostFix(parseExp(input))) + ANSI_RESET);

                System.out.println("Result: " + ANSI_BLUE + postfixExp(input) + ANSI_RESET);
                System.out.println("Type " + ANSI_PURPLE + "'stop' " + ANSI_RESET + "to stop");
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Your math expression is invalid. Please try again." + ANSI_RESET);
            }

        }
    }

    private static ArrayList<String> parseExp(String exp) {
        exp = exp.replaceAll(" ", "");
        ArrayList<String> result = new ArrayList<>();
        StringBuffer number = new StringBuffer();
        char[] chs = exp.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char ch = chs[i];
            if (Character.isDigit(ch) || ch == '.') {
                number.append(ch);
            } else if (priority(ch) >= -1) {
                if (number.length() > 0) {
                    result.add(number.toString());
                    number.delete(0, number.length());
                }
                // insert * when previous of '(' is ')' or is non operator
                if (i > 0 && ch == '(' && (chs[i - 1] == ')' || priority(chs[i - 1]) < -1)) {
                    result.add("*");
                }
                result.add(ch + "");
                // insert * when after of ')' is '('
                if (i < chs.length - 1 && ch == ')' && priority(chs[i + 1]) < -1) {
                    result.add("*");
                }
            } else {
                throw new IllegalArgumentException("Invalid Input");
            }
        }
        if (number.length() > 0)
            result.add(number.toString());
        return result;
    }

    private static boolean isNumber(String s) {
        // \d+ check more than one digit
        // \.? check zero or one dot
        // \d* check zero or more than one digit
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
            case ')':
            case '(':
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

    public static String postfixExp(String exp) {
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
        double result = Double.valueOf(stack.pop());
        DecimalFormat df;
        // \d+ check more than one digit
        // \.? check zero or one dot
        // \d{5,} check more than five digits
        if ((String.valueOf(result).matches("\\d+\\.?\\d{5,}"))) {
            df = new DecimalFormat("#.######");
            return df.format(result);
        }
        // \d+ check more than one digit
        // \.? check zero or one dot
        // 0$ the ending is 0
        if (String.valueOf(result).matches("\\d+\\.?0$")) {
            df = new DecimalFormat("#");
            return df.format(result);
        }
        return String.valueOf(result);
    }
}
