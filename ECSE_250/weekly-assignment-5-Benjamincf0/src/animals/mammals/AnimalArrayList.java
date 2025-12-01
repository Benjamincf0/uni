package animals.mammals;

public class AnimalArrayList {
	
	Animal[] array ;
	int size ;
	static int INITIAL_CAPACITY = 10;
			
	public AnimalArrayList() {
		array = new Animal[INITIAL_CAPACITY] ;
		size = 0 ;
	}
	
	public void add(Animal d) {
		//TODO 
		if (size >= array.length) {
			Animal[] newArray = new Animal[array.length * 2];
			for (int i = 0; i < array.length; i++) {
				newArray[i] = array[i];
			}
			array = newArray;
		}
		array[size] = d;
		size++;
	}
	
	public String toString() {
		
		// start with an empty string​
		String msg = "" ;
				
		// start with the head node				
		for (int i = 0 ; i < size ; i++ ) {
			// append animal name
			msg = msg + array[i].getName() + "\n" ;
		}		
		return msg ;
	}
}
