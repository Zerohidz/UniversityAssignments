import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class Main {
    public static void main(String args[]) throws IOException {
        int[] randomData = Arrays.stream(readCSVColumn(args[0], 7)).mapToInt(Integer::parseInt).toArray();
        int[] sortedData = Arrays.stream(randomData).sorted().toArray();
        Integer[] reverseSortedDataBoxed = Arrays.stream(randomData).boxed().toArray(Integer[]::new);
        Arrays.sort(reverseSortedDataBoxed, Collections.reverseOrder());
        int[] reverseSortedData = Arrays.stream(reverseSortedDataBoxed).mapToInt(Integer::intValue).toArray();

        runBenchmarks(randomData, sortedData, reverseSortedData);
    }

    private static void runBenchmarks(int[] randomData, int[] sortedData, int[] reverseSortedData)
            throws IOException {
        SortingBenchmark randomBenchmark = new SortingBenchmark(randomData);
        SortingBenchmark sortedBenchmark = new SortingBenchmark(sortedData);
        SortingBenchmark reverseSortedBenchmark = new SortingBenchmark(reverseSortedData);
        SearchingBenchmark searchingBenchmark = new SearchingBenchmark(randomData, sortedData);

        // randomBenchmark.run();
        // randomBenchmark.printResults("Random Data Benchmark Results:");
        // Charter.showAndSaveSortingChart(
        //         "Random Data Sorting",
        //         SortingBenchmark.TEST_SIZES,
        //         new double[][] {
        //                 randomBenchmark.insertionSortResults,
        //                 randomBenchmark.mergeSortResults,
        //                 randomBenchmark.countingSortResults });

        // sortedBenchmark.run();
        // sortedBenchmark.printResults("Sorted Data Benchmark Results:");
        // Charter.showAndSaveSortingChart(
        //         "Sorted Data Sorting",
        //         SortingBenchmark.TEST_SIZES,
        //         new double[][] {
        //                 sortedBenchmark.insertionSortResults,
        //                 sortedBenchmark.mergeSortResults,
        //                 sortedBenchmark.countingSortResults });

        // reverseSortedBenchmark.run();
        // reverseSortedBenchmark.printResults("Reverse Sorted Data Benchmark Results:");
        // Charter.showAndSaveSortingChart(
        //         "Reverse Sorted Data Sorting",
        //         SortingBenchmark.TEST_SIZES,
        //         new double[][] {
        //                 reverseSortedBenchmark.insertionSortResults,
        //                 reverseSortedBenchmark.mergeSortResults,
        //                 reverseSortedBenchmark.countingSortResults });

        searchingBenchmark.run();
        searchingBenchmark.printResults("Searching Benchmark Results:");
        Charter.showAndSaveSearchingChart(
                "Searching Benchmark",
                SearchingBenchmark.TEST_SIZES,
                new double[][] {
                        searchingBenchmark.linearSearchRandomResults,
                        searchingBenchmark.linearSearchSortedResults,
                        searchingBenchmark.binarySearchSortedResults });
    }

    private static void sortAndSave(int[] randomData) throws IOException {
        int[] insertionSortData = Arrays.copyOf(randomData, randomData.length);
        int[] mergeSortData = Arrays.copyOf(randomData, randomData.length);
        int[] countingSortData = Arrays.copyOf(randomData, randomData.length);

        Algorithms.insertionSort(insertionSortData);
        Algorithms.mergeSort(mergeSortData, 0, mergeSortData.length - 1);
        Algorithms.countSort(countingSortData);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("insertion_sort_results.csv"))) {
            writer.write("insertion_sort_results\n");
            for (int i = 0; i < insertionSortData.length; i++) {
                writer.write(insertionSortData[i] + "\n");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("merge_sort_results.csv"))) {
            writer.write("merge_sort_results\n");
            for (int i = 0; i < mergeSortData.length; i++) {
                writer.write(mergeSortData[i] + "\n");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("counting_sort_results.csv"))) {
            writer.write("counting_sort_results\n");
            for (int i = 0; i < countingSortData.length; i++) {
                writer.write(countingSortData[i] + "\n");
            }
        }
    }

    private static String[] readCSVColumn(String filePath, int columnNumber) throws IOException {
        ArrayList<String> column = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                column.add(line.split(",")[columnNumber - 1]);
            }
        }

        return column.toArray(new String[0]);
    }
}
