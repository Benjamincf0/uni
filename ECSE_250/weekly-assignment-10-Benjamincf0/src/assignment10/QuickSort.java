package assignment10;
public class QuickSort {
    // sort list in-place
    public static void quicksort(int[] list, int low, int high) {
        if (list==null || list.length<1) throw new IllegalArgumentException("invalid list");
        if(high-low+1<=1 || low<0 || high>=list.length) return;
        int sortedPivot = placeAndDivide(list, low, high);
        quicksort(list, low, sortedPivot-1);
        quicksort(list, sortedPivot+1, high);
    }

    // return index of sorted pivot. Note: low and high are inclusive
    static int placeAndDivide(int[] list, int low, int high)  {
        // TODO: implement method
        int pivot = list[high];
        int i = low;
        for (int j = low; j <= high; j++) {
            if (list[j] <= pivot) {
                swap(list, i, j);
                i++;
            }
        }
        return i-1;
    }

    static void swap(int[] list, int i, int j) {
        int temp = list[i];
        list[i] = list[j];
        list[j] = temp;
    }
}
