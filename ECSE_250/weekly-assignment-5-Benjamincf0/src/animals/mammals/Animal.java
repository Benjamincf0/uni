package animals.mammals;

public abstract class Animal {
    private String name, color, breed;

    public Animal(String name, String color, String breed) {
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

    @Override
    public boolean equals(Object object) {
        if(null==object || !(object instanceof Animal)) return false;

        Animal animal = (Animal) object;
        return animal.breed.equals(this.breed) && animal.color.equals(this.color) && animal.name.equals(this.name);
    }
}
