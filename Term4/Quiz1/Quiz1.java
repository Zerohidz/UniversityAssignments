import java.util.*;
import java.io.*;

public class Quiz1 {
    static class KeywordHeaderPair {
        String keyword;
        String header;
    
        public KeywordHeaderPair(String keyword, String header) {
            this.keyword = keyword;
            this.header = header;
        }
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> lines = ReadLinesFromFile("input.txt");

        ArrayList<String> ignoreLines = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();
        boolean part1 = true;
        for (String line : lines) {
            if (line.equals("..."))
                part1 = false;
            else if (part1)
                ignoreLines.add(line);
            else
                headers.add(line);
        }

        List<KeywordHeaderPair> keywordHeaderPairs = new ArrayList<>();

        for (String header : headers) {
            for (String word : header.split(" ")) {
                if (!ignoreLines.contains(word.toLowerCase())) {
                    keywordHeaderPairs.add(new KeywordHeaderPair(word, header));
                }
            }
        }

        Collections.sort(keywordHeaderPairs, (pair1, pair2) -> pair1.keyword.compareTo(pair2.keyword));

        for (KeywordHeaderPair pair : keywordHeaderPairs) {
            // capitalize the keyword in the header
            String[] words = pair.header.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (words[i].toLowerCase().equals(pair.keyword.toLowerCase())) {
                    words[i] = words[i].toUpperCase();
                }
            }
            pair.header = String.join(" ", words);

            System.out.println(pair.header);
        }
    }

    private static ArrayList<String> ReadLinesFromFile(String filePath) throws IOException {
        var lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();

        return lines;
    }
}
