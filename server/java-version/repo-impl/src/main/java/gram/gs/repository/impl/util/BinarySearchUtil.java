package gram.gs.repository.impl.util;

import java.util.Collections;
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
        return Collections.binarySearch(arr, x, comparator);
    }
}
