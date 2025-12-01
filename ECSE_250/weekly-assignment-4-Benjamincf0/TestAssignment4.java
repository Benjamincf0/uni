import animals.mammals.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestAssignment4 {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @Test
    public void testAnimalCat_default(){
        Animal a = new Cat();
        Assert.assertNotNull(a.getBreed());
        Assert.assertNotNull(a.getColor());
        Assert.assertNotNull(a.getName());
        Assert.assertEquals(a.getSound(),"Meow");

        Animal b = new Dog();
        Assert.assertNotNull(b.getBreed());
        Assert.assertNotNull(b.getColor());
        Assert.assertNotNull(b.getName());
        Assert.assertEquals(b.getSound(),"Woof");
    }

    @Test
    public void testAnimalCat_nomood(){
        Cat a = new Cat(false,"996","white","american shot hair");
        Assert.assertEquals(a.getBreed(),"american shot hair");
        Assert.assertEquals(a.getColor(),"white");
        Assert.assertEquals(a.getName(),"996");
        Assert.assertEquals(a.getSound(),"Meow");
        System.setOut(new PrintStream(outputStreamCaptor));
        a.meow();
        String result = outputStreamCaptor.toString().trim();
        Assert.assertEquals(result,"MEOW!");

    }
    @Test
    public void testAnimalCat_mood(){
        Cat a = new Cat(true,"996","white","american shot hair");
        Assert.assertEquals(a.getBreed(),"american shot hair");
        Assert.assertEquals(a.getColor(),"white");
        Assert.assertEquals(a.getName(),"996");
        Assert.assertEquals(a.getSound(),"Meow");
        System.setOut(new PrintStream(outputStreamCaptor));
        a.meow();
        String result = outputStreamCaptor.toString().trim();
        Assert.assertEquals(result,"meow~");

    }

    @Test
    public void testAnimalDog_noenergy(){
        Dog a = new Dog("buddy","white","im not sure",0);
        Assert.assertEquals(a.getBreed(),"im not sure");
        Assert.assertEquals(a.getColor(),"white");
        Assert.assertEquals(a.getName(),"buddy");
        Assert.assertEquals(a.getSound(),"Woof");
        System.setOut(new PrintStream(outputStreamCaptor));
        a.bark();
        String result = outputStreamCaptor.toString().trim();
        Assert.assertEquals(result,"woof woof");

    }
    @Test
    public void testAnimalDog_energy(){
        Dog a = new Dog("buddy","white","im not sure",10);
        Assert.assertEquals(a.getBreed(),"im not sure");
        Assert.assertEquals(a.getColor(),"white");
        Assert.assertEquals(a.getName(),"buddy");
        Assert.assertEquals(a.getSound(),"Woof");
        System.setOut(new PrintStream(outputStreamCaptor));
        a.bark();
        String result = outputStreamCaptor.toString().trim();
        Assert.assertEquals(result,"WOOF WOOF!");

    }
}
