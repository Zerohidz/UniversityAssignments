import java.util.Arrays;
import java.util.function.ToIntBiFunction;

public class SearchingBenchmark {
    public static int[] TEST_SIZES = { 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000 };
    public static int TEST_REPEAT_COUNT = 1000;

    public double[] linearSearchRandomResults;
    public double[] linearSearchSortedResults;
    public double[] binarySearchSortedResults;

    private int[] arr;
    private int[] sortedArr;

    public SearchingBenchmark(int[] arr, int[] sortedArr) {
        this.arr = arr;
        this.sortedArr = sortedArr;
    }

    public void run() {
        linearSearchRandomResults = test(this.arr, (a, b) -> Algorithms.linearSearch(a, b));
        linearSearchSortedResults = test(this.sortedArr, (a, b) -> Algorithms.linearSearch(a, b));
        binarySearchSortedResults = test(this.sortedArr, (a, b) -> Algorithms.binarySearch(a, b));
    }

    private double[] test(int[] arr, ToIntBiFunction<int[], Integer> searchingAlgorithm) {
        System.out.println("Started running tests...");

        double[] results = new double[TEST_SIZES.length];
        for (int i = 0; i < TEST_SIZES.length; i++) {
            System.out.println("Testing with size " + TEST_SIZES[i] + "...");

            int size = TEST_SIZES[i];
            long totalTime = 0;
            for (int j = 0; j < TEST_REPEAT_COUNT; j++) {
                int randomInt = arr[(int) (Math.random() * arr.length)];
                int[] copyArr = Arrays.copyOf(arr, size);

                long startTime = System.nanoTime();
                searchingAlgorithm.applyAsInt(copyArr, randomInt);
                long endTime = System.nanoTime();

                totalTime += endTime - startTime;
            }
            results[i] = (totalTime / TEST_REPEAT_COUNT);
        }
        System.out.println("Test for " + searchingAlgorithm.getClass().getSimpleName() + " completed.");

        return results;
    }

    public void printResults(String header) {
        System.out.println(header);
        System.out.println("Linear Search Random Results:");
        if (linearSearchRandomResults != null) {
            for (int i = 0; i < linearSearchRandomResults.length; i++) {
                System.out.println("Size: " + TEST_SIZES[i] + ", Average Time: " + linearSearchRandomResults[i] + " ns");
            }
        }

        if (linearSearchSortedResults != null) {
            System.out.println("Linear Search Sorted Results:");
            for (int i = 0; i < linearSearchSortedResults.length; i++) {
                System.out.println("Size: " + TEST_SIZES[i] + ", Average Time: " + linearSearchSortedResults[i] + " ns");
            }
        }

        if (binarySearchSortedResults != null) {
            System.out.println("Binary Search Sorted Results:");
            for (int i = 0; i < binarySearchSortedResults.length; i++) {
                System.out.println("Size: " + TEST_SIZES[i] + ", Average Time: " + binarySearchSortedResults[i] + " ns");
            }
        }    
    }
}
