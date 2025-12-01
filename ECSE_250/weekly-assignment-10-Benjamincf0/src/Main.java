import java.util.Arrays;

import assignment10.NumOccurrencesChecker;
import assignment10.QuickSort;

public class Main {

    public static void main(String[] args) throws Exception {
        int[] list = new int[]{3,5,7,2,8,10,1,4};
        System.out.println();
        System.out.println("\n\n**************** BEFORE SORTING ****************");
        System.out.println(Arrays.toString(list));
        System.out.println("**************** AFTER SORTING ****************");
        QuickSort.quicksort(list, 0, list.length-1);
        System.out.println(Arrays.toString(list));
        System.out.println("***************************************");

        testNumOccurKTimes( new int[]{2,4,5,5,5,5,5,6,6} , 5,5, true);
        testNumOccurKTimes( new int[]{2,4,5,5,5,5,5,6,6} , 5,4, false);
        testNumOccurKTimes( new int[]{2,4,5,5,5,5,5,6,6} , 5,6, false);
        testNumOccurKTimes( new int[]{438885258} , 438885258,1, true);
        testNumOccurKTimes( new int[]{438885258} , 438885258,2, false);
        testNumOccurKTimes( new int[]{438885258} , 432885258,1, false);
    }

    static void testNumOccurKTimes(int[] list, int n, int k, boolean expectedOutput) {
        NumOccurrencesChecker checker = new NumOccurrencesChecker();
        
        System.out.println("\n\n**************** testNumOccurKTimes ****************");
        System.out.println(Arrays.toString(list));
        System.out.println("***************************************");
        System.out.println("n: "+String.valueOf(n)+", k: "+String.valueOf(k));
        System.out.println("Expected output: "+expectedOutput);
        System.out.println("Actual output: "+checker.numOccursKTimes(list, n, k));
        System.out.println("***************************************");
    }
}
