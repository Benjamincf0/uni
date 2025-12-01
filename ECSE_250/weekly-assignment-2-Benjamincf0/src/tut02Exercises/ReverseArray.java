// the line below defines the package (i.e. folder) where the file lives.
package tut02Exercises;

public class ReverseArray {

    public static void swap(int[] a, int j, int k) {
        int tmp = a[j];
        a[j] = a[k];
        a[k] = tmp;
    }

    public static void reverseArray(int[] a) {
        // TODO reverse operation occurs in-place so nothing to return. Hint: use the provided swap method.
        int n = a.length;
        for (int i = 0; i < n / 2; i++) {
            swap(a, i, n - i - 1);
        }
    }
}
