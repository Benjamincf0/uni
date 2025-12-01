// the line below defines the package (i.e. folder) where the file lives.
package tut02Exercises;

// import any external file/module you use here
import java.util.Random;

public class RandomNumGenerator {
    Random randomGenerator;

    public RandomNumGenerator(int seed) {
        randomGenerator = new Random(seed);
    }

    public int rollDice() {
        // TODO replace placeholder below 
        return randomGenerator.nextInt(1,7);
    }
}