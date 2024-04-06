import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// This is a helper file I wrote to help me on different projects
// Dursun Zahid Korkmaz

public class FileHelper {
    /**
     * @param filePath The path of the file to read
     * @return The lines of the file as an array of strings
     */
    public static String[] readAllLines(String filePath) {
        try{
            int i = 0;
            int lenght = Files.readAllLines(Paths.get(filePath)).size();
            String[] results = new String[lenght];
            for (String line : Files.readAllLines(Paths.get(filePath))){
                results[i++] = line;
            }

            return results;

        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @param filePath The path of the file to write
     * @param lines The lines to write to the file
     * @return True if the file is written successfully, false otherwise
     */
    public static boolean overwriteFile(String filePath, String[] lines) {
        try {
            Files.write(Paths.get(filePath), String.join(System.lineSeparator(), lines).getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * @param filePath The path of the file to write
     * @param lines The lines to write to the file
     * @return True if the file is written successfully, false otherwise
     */
    public static boolean appendFile(String filePath, String[] lines) {
        try {
            Files.write(Paths.get(filePath), String.join(System.lineSeparator(), lines).getBytes(), StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}