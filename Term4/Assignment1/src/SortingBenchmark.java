import java.util.Arrays;
import java.util.function.Consumer;

public class SortingBenchmark {
    public static int[] TEST_SIZES = { 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000 };
    public static int TEST_REPEAT_COUNT = 10;

    public double[] insertionSortResults;
    public double[] mergeSortResults;
    public double[] countingSortResults;

    private int[] arr;

    public SortingBenchmark(int[] arr) {
        this.arr = arr;
    }

    public void run() {
        insertionSortResults = test((a) -> Algorithms.insertionSort(a));
        mergeSortResults = test((a) -> Algorithms.mergeSort(a, 0, a.length - 1));
        countingSortResults = test((a) -> Algorithms.countSort(a));
    }

    public void printResults(String header) {
        System.out.println(header);
        System.out.println("Insertion Sort Results:");
        if (insertionSortResults != null) {
            for (int i = 0; i < insertionSortResults.length; i++) {
                System.out.println("Size: " + TEST_SIZES[i] + ", Average Time: " + insertionSortResults[i] + " ms");
            }
        }

        if (mergeSortResults != null) {
            System.out.println("Merge Sort Results:");
            for (int i = 0; i < mergeSortResults.length; i++) {
                System.out.println("Size: " + TEST_SIZES[i] + ", Average Time: " + mergeSortResults[i] + " ms");
            }
        }

        if (countingSortResults != null) {
            System.out.println("Counting Sort Results:");
            for (int i = 0; i < countingSortResults.length; i++) {
                System.out.println("Size: " + TEST_SIZES[i] + ", Average Time: " + countingSortResults[i] + " ms");
            }
        }
    }

    private double[] test(Consumer<int[]> sortingAlgorithm) {
        System.out.println("Started running tests...");

        double[] results = new double[TEST_SIZES.length];
        for (int i = 0; i < TEST_SIZES.length; i++) {
            System.out.println("Testing with size " + TEST_SIZES[i] + "...");

            int size = TEST_SIZES[i];
            long totalTime = 0;
            for (int j = 0; j < TEST_REPEAT_COUNT; j++) {
                int[] copyArr = Arrays.copyOf(this.arr, size);

                long startTime = System.nanoTime();
                sortingAlgorithm.accept(copyArr);
                long endTime = System.nanoTime();

                totalTime += endTime - startTime;
            }
            results[i] = (totalTime / TEST_REPEAT_COUNT / 1000000.0);
        }
        System.out.println("Test for " + sortingAlgorithm.getClass().getSimpleName() + " completed.");

        return results;
    }
}
