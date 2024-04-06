public class Algorithms {
    public static void insertionSort(int arr[]) {
        for (int i = 1; i < arr.length; ++i) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    private static void merge(int arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int leftArr[] = new int[n1];
        int rightArr[] = new int[n2];

        for (int i = 0; i < n1; ++i)
            leftArr[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            rightArr[j] = arr[m + 1 + j];

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    public static void mergeSort(int arr[], int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    public static void countSort(int[] inputArray) {
        int maxElement = 0;

        for (int i = 0; i < inputArray.length; i++) {
            maxElement = Math.max(maxElement, inputArray[i]);
        }

        int[] countArray = new int[maxElement + 1];

        for (int i = 0; i < inputArray.length; i++) {
            countArray[inputArray[i]]++;
        }

        int index = 0;
        for (int i = 0; i <= maxElement; i++) {
            for (int j = countArray[i]; j > 0; j--) {
                inputArray[index++] = i;
            }
        }
    }

    public static int linearSearch(int arr[], int x) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x)
                return i;
        }
        return -1;
    }

    public static int binarySearch(int arr[], int x) {
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == x)
                return mid;

            if (arr[mid] < x)
                low = mid + 1;

            else
                high = mid - 1;
        }

        return -1;
    }
}
