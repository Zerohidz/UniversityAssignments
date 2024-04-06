import java.io.File;
import java.io.PrintStream;

public class Main {
    private static String inputFileName;
    private static String outputFileName;
    private static PrintStream consoleStream;

    public static void main(String[] args) {
        readArgs(args);
        setSystemOutput();
        String[] commandStrings = readInputFile();
        CommandManager.initialize(commandStrings);
        CommandManager.executeCommands();

        terminateProgram("Successfully saved output file: " + outputFileName);
    }

    public static void terminateProgram() {
        System.exit(0);
    }

    public static void terminateProgram(String message) {
        setConsoleStream();
        System.out.println(message);
        terminateProgram();
    }

    private static void setConsoleStream() {
        System.setOut(consoleStream);
    }

    /**
     * Sets the output stream to the output file
     */
    private static void setSystemOutput() {
        try {
            consoleStream = System.out;
            System.setOut(new PrintStream(new File(outputFileName)));

        } catch (Exception e) {
            terminateProgram("Could not create output file!");
        }
    }

    /**
     * @param args
     *             Checks if the arguments are valid
     *             And reads the arguments
     */
    private static void readArgs(String[] args) {
        if (args.length != 2) {
            System.out.println("You need to use like this: java Main <input file> <output file>");
            terminateProgram();
        }

        inputFileName = args[0];
        outputFileName = args[1];
    }

    /**
     * @param inputFileName
     * @return the lines of the input file as an array of strings
     */
    private static String[] readInputFile() {
        String[] lines = FileHelper.readAllLines(inputFileName);
        if (lines == null) {
            setConsoleStream();
            terminateProgram("Failed to read Input File: " + inputFileName);
        }

        return lines;
    }
}