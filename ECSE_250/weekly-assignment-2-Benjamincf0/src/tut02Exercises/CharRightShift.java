// the line below defines the package (i.e. folder) where the file lives.
package tut02Exercises;

public class CharRightShift {

    public static char charRightShift(char ch, int shift) {

        // Verify if the input is a lowercase letter
        if (ch < 'a' || ch > 'z') {
            return ch;
        }

        // TODO Figure out the position of the character in the alphabet
        int pos = ch - 'a';

        // TODO If negative, convert the shift into a positive one
        if (shift < 0) {
            shift = 26 + shift%26;
        }
        

        // TODO Find the position of the shifted character in the alphabet
        int newPos = 'a' + (pos+shift)% 26;

        return (char) newPos;
    }
}
