package animals.mammals;

public abstract class Animal {
    private String name, color, breed;

    public Animal() {
        //TODO
        this.name = "";
        this.color = "";
        this.breed = "";
    }

    public Animal(String name, String color, String breed) {
        //TODO
        this.name = name;
        this.color = color;
        this.breed = breed; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public abstract String getSound();

}
