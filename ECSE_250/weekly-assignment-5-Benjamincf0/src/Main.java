import animals.mammals.AnimalArrayList;
import animals.mammals.Dog;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    
        animalArrayListAddTest();
        equalityTest();
    }

    public static void animalArrayListAddTest() {
        AnimalArrayList list = new AnimalArrayList() ;
        
        for(int i=1; i<15; i++) {
            list.add(new Dog(String.valueOf(i)));
        }
		
		System.out.println(list);
    }
    
    public static void equalityTest() {
        Dog d1 = new Dog("Mellow");
        Dog d2 = new Dog("Mellow");
        Dog d1_copy = d1;
        System.out.println("d1==d1_copy returns "+String.valueOf(d1==d1_copy));
        System.out.println("d1.equals(d1_copy) returns "+String.valueOf(d1.equals(d1_copy)));

        System.out.println("d1==d2 returns "+String.valueOf(d1==d2));
        System.out.println("d1.equals(d2) returns "+String.valueOf(d1.equals(d2)));
    }
}
