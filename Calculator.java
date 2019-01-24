import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Calculator {
    private static String ANSI_RESET = "\u001B[0m";
    private static String ANSI_BLACK = "\u001B[30m";
    private static String ANSI_RED = "\u001B[31m";
    private static String ANSI_GREEN = "\u001B[32m";
    private static String ANSI_YELLOW = "\u001B[33m";
    private static String ANSI_BLUE = "\u001B[34m";
    private static String ANSI_PURPLE = "\u001B[35m";
    private static String ANSI_CYAN = "\u001B[36m";
    private static String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        client();
    }

    private static void client() {
        // Windows does not support ANSI
        if (System.getProperty("os.name").startsWith("Windows")) {
            ANSI_RESET = "";
            ANSI_BLACK = "";
            ANSI_RED = "";
            ANSI_GREEN = "";
            ANSI_YELLOW = "";
            ANSI_BLUE = "";
            ANSI_PURPLE = "";
            ANSI_CYAN = "";
            ANSI_WHITE = "";
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        System.out.println("Welcome to my program - Calculator");
        System.out.println("Operator: " + ANSI_YELLOW + "+ - * / % ^ | ( )" + ANSI_RESET);
        System.out.println("Note: " + ANSI_GREEN + "Use [] for absolute. Ex: [5-9]" + ANSI_RESET);
        System.out.println("Ex of input: " + ANSI_CYAN + "5 + 2([4 - 77] - 8)+2^3" + ANSI_RESET);
        System.out.println("Type: " + ANSI_PURPLE + "ex1, ex2, ex3, ex4" + ANSI_RESET + " to see example");
        while (true) {
            System.out.print("Enter your math expression: ");
            try {
                input = bufferedReader.readLine();
                input = inputSwitch(input);
                if (input.equals("stop"))
                    break;

                List<String> exp = parseExp(input);
                String infix = String.join(" ", exp).replaceAll("\\[", "|").replaceAll("]", "|");
                String postfix = String.join(" ", infixToPostFix(exp));
                System.out.println("InFix: " + ANSI_PURPLE + infix + ANSI_RESET);
                System.out.println("PostFix: " + ANSI_GREEN + postfix + ANSI_RESET);
                System.out.println("Result: " + ANSI_BLUE + postfixExp(input) + ANSI_RESET);
                System.out.println("Type " + ANSI_PURPLE + "'stop' " + ANSI_RESET + "to stop");
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Your math expression is invalid. Please try again." + ANSI_RESET);
            }
            System.out.println();
        }
    }

    private static String inputSwitch(String input) {
        switch (input.toLowerCase()) {
            case "stop":
                return "stop";
            case "ex1":
                return "5 + 2([4 - 77] - 8)+2^3";
            case "ex2":
                return "3[[4-6]-22]2^3";
            case "ex3":
                return "4* (4+2)(3+3) - 5";
            case "ex4":
                return "9(3)(2)[3-6]5(2)";
        }
        return input;
    }

    private static List<String> parseExp(String exp) {
        // replace all ++, --, -+, +- and spaces
        exp = exp.replaceAll("-{2}", "+");
        exp = exp.replaceAll("\\+{2,}", "+");
        exp = exp.replaceAll("-\\+|\\+-", "-");
        exp = exp.replaceAll(" ", "");

        List<String> result = new ArrayList<>();
        Stack<String> op = new Stack<>();
        StringBuffer number = new StringBuffer();
        char[] chs = exp.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char ch = chs[i];
            if (Character.isDigit(ch) || ch == '.') {
                number.append(ch);
            } else if (priority(ch) >= -1) {

                if (i == 0 && priority(ch) == 1) {
                    result.add("0");
                }

                if (number.length() > 0) {
                    result.add((op.size() > 0 ? op.pop() : "") + number.toString());
                    number.delete(0, number.length());
                }
                // insert * when previous of '(' is ')' or is non operator
                if (i > 0) {
                    // convert to negative number when '-' is after (, [, *, /, ^
                    if (ch == '-' && (priority(chs[i - 1]) >= 2 || priority(chs[i - 1]) == -1)) {
                        op.push("-");
                        continue;
                    }

                    // remove '+' when '+' is after (, [, *, /, ^
                    if (ch == '+' && (priority(chs[i - 1]) >= 2 || priority(chs[i - 1]) == -1)) {
                        continue;
                    }


                    if (priority(ch) == -1
                            && (priority(chs[i - 1]) == 0 || priority(chs[i - 1]) < -1)
                    ) {
                        result.add("*");
                    }
                }

                result.add(ch + "");
                // insert * when after of ')' is '('
                if (i < chs.length - 1) {
                    if (priority(ch) == 0 && priority(chs[i + 1]) < -1) {
                        result.add("*");
                    }

                }
            } else {
                throw new IllegalArgumentException("Invalid Input");
            }
        }
        if (number.length() > 0)
            result.add((op.size() > 0 ? op.pop() : "") + number.toString());

        return result;
    }

    private static boolean isNumber(String s) {
        // \d+ check more than one digit
        // \.? check zero or one dot
        // \d* check zero or more than one digit
        return s.matches("-?\\d+\\.?\\d*");
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
            case ']':
            case ')':
                return 0;
            case '[':
            case '(':
                return -1;
            default:
                return -3;
        }
    }

    private static List<String> infixToPostFix(List<String> exp) {
        Stack<String> stack = new Stack<>();
        List<String> output = new ArrayList<>();
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
            } else if (s.charAt(0) == '(' || s.charAt(0) == '[') { // if s is (
                stack.push(s);
            }
            // if s is )
            // output += stack.pop until find (
            else if (s.charAt(0) == ')') {
                while (!stack.empty() && stack.peek().charAt(0) != '(') {
                    output.add(stack.pop());
                }
                stack.pop();
            } else if (s.charAt(0) == ']') {
                while (!stack.empty() && stack.peek().charAt(0) != '[') {
                    output.add(stack.pop());
                }
                stack.pop();
                output.add("1");
                output.add("|");
            }
        }
        // stack.pop the remaining to output
        while (!stack.empty())
            output.add(stack.pop());
        return output;
    }

    private static double cal(double x, double y, char op) {
        switch (op) {
            case '|':
                return Math.abs(x) * Math.abs(y);
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
                return Math.floorMod((long) x, (long) y);
        }
        return -1;
    }

    static String postfixExp(String exp) {
        Stack<String> stack = new Stack<>();
        List<String> Exp = parseExp(exp);
        Exp = infixToPostFix(Exp);
        for (String s : Exp) {
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
        // -? check zero or one negative
        // \d+ check more than one digit
        // \.? check zero or one dot
        // \d{5,} check more than five digits
        if ((String.valueOf(result).matches("-?\\d+\\.?\\d{5,}"))) {
            df = new DecimalFormat("#.######");
            return df.format(result);
        }
        // -? check zero or one negative
        // \d+ check more than one digit
        // \.? check zero or one dot
        // 0$ the ending is 0
        if (String.valueOf(result).matches("-?\\d+\\.?0$")) {
            df = new DecimalFormat("#");
            return df.format(result);
        }
        return String.valueOf(result);
    }
}
