package animals.mammals;

public class Dog extends Animal{
	private int energy;

	public Dog() {
		//TODO
		super("", "", "");
        this.energy = 0;
	}

	public Dog(String name, String color, String breed, int energy) {
		//TODO
		super(name, color, breed);
        this.energy = energy;
	}

	public void bark() {
		if (energy>5) {
			System.out.println("WOOF WOOF!");
		}
		else {
			System.out.println("woof woof");
		}
	}

	//TODO override getSound
	@Override
    public String getSound() {
        return "Woof";
    }
}
