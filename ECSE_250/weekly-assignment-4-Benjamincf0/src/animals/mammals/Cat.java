package animals.mammals;

public class Cat extends Animal{
    private boolean mood;

    public Cat() {
        //TODO
        super("", "", "");
        this.mood = false;
    }

    public Cat(boolean mood, String name, String color, String breed) {
        //TODO
        super(name, color, breed);
        this.mood = mood;
    }

    public void meow() {
        if(!mood) {
            System.out.println("MEOW!");
        }
        else {
            System.out.println("meow~");
        }
    }

   //TODO override getSound
   @Override
   public String getSound() {
       return "Meow";
   }
}
