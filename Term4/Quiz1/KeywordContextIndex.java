import java.util.*;

public class KeywordContextIndex {
    public static List<String> tokenize(String title) {
        // Tokenize a title into words
        return Arrays.asList(title.toLowerCase().split("\\s+"));
    }

    public static Map<String, List<int[]>> getKeywordContextIndex(List<String> titles) {
        // Generate the Keyword in Context index
        Map<String, List<int[]>> keywordIndex = new HashMap<>();
        for (int idx = 0; idx < titles.size(); idx++) {
            String title = titles.get(idx);
            List<String> words = tokenize(title);
            for (int pos = 0; pos < words.size(); pos++) {
                String word = words.get(pos);
                if (!keywordIndex.containsKey(word)) {
                    keywordIndex.put(word, new ArrayList<>());
                }
                keywordIndex.get(word).add(new int[]{idx, pos});
            }
        }
        return keywordIndex;
    }

    public static String capitalizeKeyword(String title, Set<int[]> keywordIndices) {
        // Capitalize the keywords in a title according to the provided indices
        List<String> words = tokenize(title);
        StringBuilder capitalizedTitle = new StringBuilder();
        for (int pos = 0; pos < words.size(); pos++) {
            String word = words.get(pos);
            if (keywordIndices.contains(new int[]{pos, word.hashCode()})) {
                capitalizedTitle.append(word.toUpperCase());
            } else {
                capitalizedTitle.append(word);
            }
            if (pos < words.size() - 1) {
                capitalizedTitle.append(" ");
            }
        }
        return capitalizedTitle.toString();
    }

    public static List<String> outputIndex(List<String> titles) {
        // Output the Keyword in Context index of the given titles
        Map<String, List<int[]>> keywordIndex = getKeywordContextIndex(titles);
        List<String> sortedKeywords = new ArrayList<>(keywordIndex.keySet());
        Collections.sort(sortedKeywords);
        List<String> output = new ArrayList<>();
        for (String keyword : sortedKeywords) {
            for (int[] index : keywordIndex.get(keyword)) {
                String capitalizedTitle = capitalizeKeyword(titles.get(index[0]), Collections.singleton(index));
                output.add(capitalizedTitle);
            }
        }
        return output;
    }

    public static void main(String[] args) {
        // Sample Input
        List<String> titles = Arrays.asList(
            "It is Time to Measure Time in a Timely Manner",
            "The Time is Right"
        );

        // Output
        List<String> output = outputIndex(titles);
        for (String title : output) {
            System.out.println(title);
        }
    }
}
