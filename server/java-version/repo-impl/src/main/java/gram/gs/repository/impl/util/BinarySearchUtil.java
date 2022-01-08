package gram.gs.repository.impl.util;

import java.util.Comparator;
import java.util.List;

public class BinarySearchUtil {

    public static <T> int binaryAddOrSet(List<T> arr, T x, Comparator<T> comparator) {
        int index = binarySearch(arr, x, comparator);
        if (index >= 0) {
            arr.set(index, x);
        } else {
            index = -index - 1;
            arr.add(index, x);
        }

        return index;
    }

    public static <T> void binaryRemove(List<T> arr, T x, Comparator<T> comparator) {
        int index = binarySearch(arr, x, comparator);
        if (index >= 0) {
            arr.remove(index);
        }
    }

    public static <T> int binarySearch(List<T> arr, T x, Comparator<T> comparator) {
        int low = 0;
        int high = arr.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            T midVal = arr.get(mid);
            final int compareResult = comparator.compare(midVal, x);
            if (compareResult < 0) {
                low = mid + 1;
            } else if (compareResult > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
}
