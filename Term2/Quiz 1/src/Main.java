import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String armstrongText = "Armstrong numbers up to";
    private static final String emirpText = "Emirp numbers up to";
    private static final String abundantText = "Abundant numbers up to";
    private static final String ascendingText = "Ascending order sorting:";
    private static final String descendingText = "Descending order sorting:";

    private static String[] lines;
    private static ArrayList<String> outBuffer = new ArrayList<String>();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Invalid number of arguments");
            return;
        }
        lines = getLinesFromInputFile(args[0]);

        boolean exited = false;
        for (int i = 0; i < lines.length; i++) {
            switch (lines[i]) {
                case armstrongText + ":":
                    i = handleArmstrong(i);
                    break;
                case emirpText + ":":
                    i = handleEmirp(i);
                    break;
                case abundantText + ":":
                    i = handleAbundant(i);
                    break;
                case ascendingText:
                    i = handleAscending(i);
                    break;
                case descendingText:
                    i = handleDescending(i);
                    break;
                case "Exit":
                    exited = true;
                    break;
                default:
                    System.out.println("Invalid input at line: " + i);
                    break;
            }
            if (exited)
                break;
            outBuffer.add("\n");
        }
        outBuffer.add("Finished...");

        saveOutputFile();
    }

    private static int handleArmstrong(int i) {
        i++;
        outBuffer.add(armstrongText + " " + lines[i] + ":\n");
        int n = Integer.parseInt(lines[i]);
        boolean firstItem = true;
        for (int j = 1; j <= n; j++) {
            if (isArmstrong(j)) {
                if (!firstItem) {
                    outBuffer.add(" ");
                }
                outBuffer.add(Integer.toString(j));
                firstItem = false;
            }
        }
        outBuffer.add("\n");
        return i;
    }

    private static int handleEmirp(int i) {
        i++;
        outBuffer.add(emirpText + " " + lines[i] + ":\n");
        int n = Integer.parseInt(lines[i]);
        boolean firstItem = true;
        for (int j = 0; j <= n; j++) {
            if (isEmirp(j)) {
                if (!firstItem) {
                    outBuffer.add(" ");
                }
                outBuffer.add(Integer.toString(j));
                firstItem = false;
            }
        }
        outBuffer.add("\n");
        return i;
    }

    private static int handleAbundant(int i) {
        i++;
        outBuffer.add(abundantText + " " + lines[i] + ":\n");
        int n = Integer.parseInt(lines[i]);
        boolean firstItem = true;
        for (int j = 0; j <= n; j++) {
            if (isAbundant(j)) {
                if (!firstItem) {
                    outBuffer.add(" ");
                }
                outBuffer.add(Integer.toString(j));
                firstItem = false;
            }
        }
        outBuffer.add("\n");
        return i;
    }

    private static int handleAscending(int i) {
        outBuffer.add(ascendingText + "\n");
        i++;
        ArrayList<Integer> nums = new ArrayList<Integer>();
        while (true) {
            int n = Integer.parseInt(lines[i]);
            if (n == -1)
                break;
            i++;

            boolean inserted = false;
            for (int j = 0; j < nums.size(); j++) {
                if (nums.get(j) > n) {
                    nums.add(j, n);
                    inserted = true;
                    break;
                }
            }
            if (!inserted)
                nums.add(n);

            for (Integer num : nums) {
                if (num != nums.get(0))
                    outBuffer.add(" ");
                outBuffer.add(Integer.toString(num));
            }
            outBuffer.add("\n");
        }

        return i;
    }

    private static int handleDescending(int i) {
        outBuffer.add(descendingText + "\n");
        i++;
        ArrayList<Integer> nums = new ArrayList<Integer>();
        while (true) {
            int n = Integer.parseInt(lines[i]);
            if (n == -1)
                break;
            i++;

            boolean inserted = false;
            for (int j = 0; j < nums.size(); j++) {
                if (nums.get(j) < n) {
                    nums.add(j, n);
                    inserted = true;
                    break;
                }
            }
            if (!inserted)
                nums.add(n);

            for (Integer num : nums) {
                if (num != nums.get(0))
                    outBuffer.add(" ");
                outBuffer.add(Integer.toString(num));
            }
            outBuffer.add("\n");
        }

        return i;
    }

    private static boolean isArmstrong(int n) {
        int sum = 0;
        int temp = n;
        int pow = Integer.toString(n).length();

        while (temp != 0) {
            int digit = temp % 10;
            sum += Math.pow(digit, pow);
            temp /= 10;
        }

        return sum == n;
    }

    private static boolean isEmirp(int n) {
        if (n < 13)
            return false;

        int reversed = 0;
        int scalar = (int) Math.pow(10, Integer.toString(n).length() - 1);
        int temp = n;
        while (temp != 0) {
            int digit = temp % 10;
            reversed += digit * scalar;
            scalar /= 10;
            temp /= 10;
        }

        return n != reversed && (isPrime(n) && isPrime(reversed));
    }

    private static boolean isAbundant(int n) {
        int divisorSum = 0;
        for (int i = 1; i < n; i++) {
            if (n % i == 0) {
                divisorSum += i;
            }
        }

        return n < divisorSum;
    }

    private static boolean isPrime(int n) {
        if (n < 2)
            return false;

        for (int i = 2; i < n; i++) {
            if (n % i == 0)
                return false;
        }

        return true;
    }

    private static String[] getLinesFromInputFile(String filePath) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to read input file.");
        }

        return lines.toArray(new String[lines.size()]);
    }

    private static void saveOutputFile() {
        // write outBuffer to output.txt
        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            for (String line : outBuffer) {
                writer.print(line);
            }
            writer.close();

            System.out.println("Successfully saved output.txt.");
        } catch (Exception e) {
            System.out.println("Failed to write output file.");
        }
    }
}