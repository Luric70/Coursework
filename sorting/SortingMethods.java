import java.util.List;
import java.util.ArrayList;

public class SortingMethods<E extends Comparable<E>> implements Demonstrable {
    private static final int UPPER_LIMIT_FOR_SHOW = 15;
    StringBuilder steps = new StringBuilder();

    /**
     * DO NOT CHANGE THIS METHOD
     */
    public String show() {
        steps.setLength(steps.length() - 1);
        return steps.toString();
    }

    /**
     * This is a bottom-up implementation of the mergesort algorithm where multiple small sublists of the same size
     * are merged in a single pass. The demonstration of this algorithm is done by showing each stage of the merge on
     * sublists of the same size (except possibly the very last sublist, if the input list has odd number of elements).
     * For example, if the input is [5, 2, 4, 6, 7, 1, 3], a call to show() returns the string:
     * <p>
     * [5, 2, 4, 6, 7, 1, 3]
     * <p>
     * [2, 5, 4, 6, 1, 7, 3]
     * <p>
     * [2, 4, 5, 6, 1, 3, 7]
     * <p>
     * [1, 2, 3, 4, 5, 6, 7]
     * </p>
     * The first line shows the input, the second line demonstrates the pairwise merges creating sorted sublists of
     * size 2 (except the very last sublist, which is [3]), the third line demonstrates merges of size-2 lists together
     * to create [2, 4, 5, 6] and [1, 3, 7] (again, the very last sublist was not a pair because the input has an odd
     * number of elements), and the fourth line merges the size-4 sublists together, which is the sorted list.
     *
     * @param elements the input list to be sorted
     */

    public void mergeSort(List<E> elements) {
        steps.setLength(0);
        int n = elements.size();

        for (int currSize = 1; currSize < n; currSize*= 2) {
            for (int leftStart = 0; leftStart < n-1; leftStart+= 2*currSize) {
                int mid = Math.min(leftStart + currSize-1, n-1);
                int rightEnd = Math.min(leftStart + 2*currSize-1, n-1);

                merge(elements, leftStart, mid, rightEnd);

                if (elements.size() < UPPER_LIMIT_FOR_SHOW) {
                    steps.append(elements.toString()).append('\n');
                }
            }
        }
    }

    private void merge(List<E> elements, int left, int mid, int right) {
        List<E> mergedList = new ArrayList<>();
        int lIndex = left;
        int rIndex = mid+1;


        while (lIndex <= mid && rIndex <= right) {
            if (elements.get(lIndex).compareTo(elements.get(rIndex)) <= 0) {
                mergedList.add(elements.get(lIndex));
                lIndex++;
            } else {
                mergedList.add(elements.get(rIndex));
                rIndex++;
            }
        }
        while (lIndex <= mid) {
            mergedList.add(elements.get(lIndex));
            lIndex++;
        }
        while (rIndex <= right) {
            mergedList.add(elements.get(rIndex));
            rIndex++;
        }
        int mergedIndex = left;
        for (E element : mergedList) {
            elements.set(mergedIndex, element);
            mergedIndex++;
        }
    }


    /**
         * This is an implementation of the insertion sort algorithm. The steps are demonstrated by adding a line to the
         * string representation whenever the unsorted portion of the input list decreases in size by 1. For example, if
         * the input is [8, 3, 15, 0, 9], then show() returns the string:
         * <p>
         * [8, 3, 15, 0, 9]
         * <p>
         * [3, 8, 15, 0, 9]
         * <p>
         * [3, 8, 15, 0, 9]
         * <p>
         * [0, 3, 8, 15, 9]
         * <p>
         * [0, 3, 8, 9, 15]
         * </p>
         *
         * @param elements the input list to be sorted
         */

    public void insertionSort(List<E> elements) {
        steps.setLength(0);
        if (elements.size() < UPPER_LIMIT_FOR_SHOW) {
            steps.append(elements.toString()).append('\n');
        }

        sort(elements);
    }

    private void sort(List<E> elements) {
        if (elements.size() < 2)
            return;
        int boundary = 1;
        while (boundary < elements.size()) {
            insert(elements, boundary);
            boundary++;
        }
    }

    protected void insert(List<E> elements, int boundary) {
        E toBeInserted = elements.remove(boundary);
        int check = boundary-1;
        while (check >= 0 && toBeInserted.compareTo(elements.get(check)) < 0)
            check--;
        elements.add(check+1, toBeInserted);

        if (elements.size() < UPPER_LIMIT_FOR_SHOW) {
            steps.append(elements.toString()).append('\n');
        }
    }

    public void selectionSort(List<E> elements) {
        steps.setLength(0);

        if (elements.size() < UPPER_LIMIT_FOR_SHOW) {
            steps.append(elements.toString()).append('\n');
        }

        int boundary = 0;
        while (boundary < elements.size() - 1) {
            int minIndex = findMinIndex(elements, boundary);
            swap(elements, boundary++, minIndex);
            if (elements.size() < UPPER_LIMIT_FOR_SHOW)
                steps.append(elements.toString()).append('\n');
        }
    }

    private int findMinIndex(List<E> elements, int boundary) {
        int minIndex = boundary;

        if (boundary == elements.size() - 1)
            return minIndex;

        E min = elements.get(minIndex);

        for (int i = boundary + 1; i < elements.size(); i++) {
            E e = elements.get(i);
            if (e.compareTo(min) < 0) {min = e; minIndex = i;}
        }
        return minIndex;
    }

    private void swap(List<E> elements, int i, int j) {
        if (i < 0 || j < 0 || i >= elements.size() || j >= elements.size()) {
            String err = String.format("Cannot swap elements between positions %d and %d in list of %d elements.",
                    i, j, elements.size());
            throw new IndexOutOfBoundsException(err);
        }
        E tmp = elements.get(i);

        elements.set(i, elements.get(j));
        elements.set(j, tmp);
    }
}