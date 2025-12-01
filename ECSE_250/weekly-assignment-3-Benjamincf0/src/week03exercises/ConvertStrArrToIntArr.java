// the line below defines the package (i.e. folder) where the file lives.
package week03exercises;

public class ConvertStrArrToIntArr {

    public static int[] convertStrArrToIntArr(String[] strArr) {
        int[] intArr = new int[strArr.length];
        // TODO complete method. Hint: use a try-catch block and catch for NumberFormatException and NullPointerException

        for (int i = 0; i < strArr.length; i++) {
            try {
                intArr[i] = Integer.parseInt(strArr[i].trim());
            } catch (Exception e) {
                // TODO: handle exception
                intArr[i] = 0;
            }
        }
        return intArr;
    }
}