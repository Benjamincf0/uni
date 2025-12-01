// import any external file/module you use here
import tut02Exercises.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        testRollDice();
        testCharRightShift();
        testReverseArray();
    }

    public static void testRollDice() {
        RandomNumGenerator generator = new RandomNumGenerator(5);
        for (int i=0; i<3; i++) {
            System.out.println("Result of dice-roll: "+String.valueOf(generator.rollDice()));
        }
    }

    public static void  testCharRightShift() {
        char output = CharRightShift.charRightShift(' ', 2);
        System.out.println("Shift ' ' by 2: "+output);
        output = CharRightShift.charRightShift('a', 5);
        System.out.println("Shift 'a' by 5: "+output);
    }

    public static void testReverseArray() {
        // new int[] { 1, 2, 3, 4, 5 } is a shortcut to initialize an array with values
        // it is equivalent to new int[5], then assign each slot individually
        int[] a = new int[] { 1, 2, 3, 4, 5 };
        System.out.println("The array before reversal is: "+ Arrays.toString(a));
        ReverseArray.reverseArray(a);
        System.out.println("The array after reversal is: "+ Arrays.toString(a));
    }

}