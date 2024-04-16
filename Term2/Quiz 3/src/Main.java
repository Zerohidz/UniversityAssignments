import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static PrintStream console;
    private static String filePath;

    public static void main(String[] args) {
        setSystemOut();
        readArgs(args);
        String[] lines = readAllLinesSafe(filePath);
        validateIfLinesAreValid(lines);
        finishOk();
    }

    private static void validateIfLinesAreValid(String[] lines) {
        for (String line : lines) {
            for (char c : line.toCharArray()) {
                if (c != ' ') {
                    if (ALPHABET.indexOf(c) < 0) {
                        finishWithError("The input file should not contain unexpected characters");
                    }
                }
            }
        }
    }

    private static String[] readAllLinesSafe(String filePath) {
        String[] lines = FileHelper.readAllLines(filePath);
        if (lines == null) {
            finishWithError("There should be an input file in the specified path");
            return null;
        } else if (lines.length == 0) {
            finishWithError("The input file should not be empty");
            return null;
        }
        return lines;
    }

    private static void setSystemOut() {
        console = System.out;
        try {
            System.setOut(new PrintStream(new FileOutputStream("output.txt", true)));
        } catch (FileNotFoundException e) {
            try {
                System.setOut(new PrintStream(new FileOutputStream("output.txt")));
            } catch (Exception e1) {
                finishWithError("Could not find and also Cannot create output.txt file");
            }
        }
    }

    private static void readArgs(String[] args) {
        if (args.length != 1) {
            finishWithError("There should be only 1 paremeter");
        }

        filePath = args[0];
    }

    private static void finishOk() {
        System.out.println("The program is running correctly");
        System.setOut(console);
        System.out.println("The input file was correct, the output is written to output.txt");
        System.exit(1);
    }

    private static void finishWithError(String message) {
        System.out.println(message);
        System.setOut(console);
        System.out.println("Written to output file as: " + message);
        System.exit(1);
    }
}
