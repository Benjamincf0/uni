package assignment10;

public class NumOccurrencesChecker {

    // returns True if the list contains exactly k occurrences of n. Assume the list is sorted according to ascending order. Your program must run in O(logn).
    public boolean numOccursKTimes(int[] list, int n, int k) {
        // TODO: complete implementation
        int left1, right1, left2, right2;
        left1 = left2 = 0;
        right1 = right2 = list.length - 1;
        while (left1 < right1 || left2 < right2) {
            int mid1 = (int) ((left1 + right1 + 1) / 2);
            int mid2 = (int) ((left2 + right2) / 2);
            if (left1 < right1) {
                if (list[mid1] <= n) {
                    left1 = mid1;
                }
                else {
                    right1 = mid1 - 1;
                }
            }
            if (left2 < right2) {
                if (list[mid2] >= n ) {
                    right2 = mid2;
                }
                else {
                    left2 = mid2 + 1;
                }
            }
        }
        if (list[left1] != n) {
            return false;
        }
        System.out.println(left1 + " " + left1 + " " + left2 + " " + right2 );
        return ((left1 - left2 + 1) == k);
    }
}
