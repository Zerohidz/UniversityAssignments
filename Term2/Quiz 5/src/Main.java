import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        args = new String[] { "io/input2.txt", "output.txt" };
        if (args.length != 2) {
            System.out.println("Usage: java Main <inputPath> <outputPath>");
            return;
        }

        String inputPath = args[0];
        String outputPath = args[1];

        try {
            System.setOut(new PrintStream(new File(outputPath)));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
        String[] lines = FileHelper.readAllLines(inputPath);
        for (String line : lines) {
            if (line.startsWith("Convert from Base 10 to Base 2:")) {
                int number = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                int base2Number = base10ToBase2(number);
                System.out.println("Equivalent of " + number + " (base 10) in base 2 is: " + base2Number);
            } else if (line.startsWith("Count from 1 up to n in binary:")) {
                int n = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                // print: Counting from 1 up to 15 in binary:
                System.out.println("Counting from 1 up to " + n + " in binary: " + countBinaryNumbers(n));
            } else if (line.startsWith("Check if following is palindrome or not:")) {
                String str = line.substring(line.indexOf(":") + 1).trim();
                if (checkIfPalindrome(str)) {
                    System.out.println("\"" + str + "\"" + " is a palindrome.");
                } else {
                    System.out.println("\"" + str + "\"" + " is not palindrome.");
                }
            } else if (line.startsWith("Check if following expression is valid or not:")) {
                String str = line.substring(line.indexOf(":") + 1).trim();
                if (checkIfBalanced(str)) {
                    System.out.println("\"" + str + "\"" + " is a valid expression.");
                } else {
                    System.out.println("\"" + str + "\"" + " is not a valid expression.");
                }
            }
        }
    }

    public static int base10ToBase2(int number) {
        Stack<Integer> stack = new Stack<Integer>();
        while (number > 0) {
            stack.push(number % 2);
            number /= 2;
        }
        int base2Number = 0;
        while (!stack.isEmpty()) {
            base2Number = base2Number * 10 + stack.pop();
        }
        return base2Number;
    }

    public static boolean checkIfBalanced(String str) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty()) {
                    return false;
                }
                char lastCharInStack = stack.pop();
                if ((c == ')' && lastCharInStack != '(') ||
                        (c == '}' && lastCharInStack != '{') ||
                        (c == ']' && lastCharInStack != '[')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static boolean checkIfPalindrome(String str) {
        str = str.replaceAll("[^a-zA-Z]", "").toLowerCase();
        Stack<Character> stack = new Stack<>();
        
        for (int i = 0; i < str.length(); i++) {
            stack.push(str.charAt(i));
        }
        String reversedStr = "";
        while (!stack.isEmpty()) {
            reversedStr += stack.pop();
        }
        return str.equals(reversedStr);
    }

    public static String countBinaryNumbers(int n) {
        Queue<String> queue = new Queue<>();
        queue.enqueue("1");
        String output = "";
        for (int i = 1; i <= n; i++) {
            String binaryNumber = queue.dequeue();
            output += binaryNumber + "\t";
            queue.enqueue(binaryNumber + "0");
            queue.enqueue(binaryNumber + "1");
        }
        return output;
    }

}