import data_structures.interfaces.*;
import data_structures.*;

public class Main {
    public static void main(String[] args) throws Exception {
		System.out.println("\n\n **************************TESTING SINGLYLINKEDLIST*****************************");
		SSinglyLinkedList<Integer> singlyLinkedList = new SSinglyLinkedList<>();
		testList(singlyLinkedList);
		System.out.println("\n\n **************************TESTING DOUBLYLINKEDLIST*****************************");
		SDoublyLinkedList<Integer> doublyLinkedList = new SDoublyLinkedList<>();
		testList(doublyLinkedList);
    }

    static void testList(SList<Integer> list) {
		System.out.println("1) Add 1,2,3,4") ;
		System.out.println("List should contain: 1,2,3,4");
		for(int i=1; i<=4; i++) list.add(i);
		System.out.println("******RESULTS********");
		System.out.println(list);
		System.out.println("*********************");

		System.out.println("\n2) Remove 1 and 4");
		System.out.println("List should contain: 2,3");
		int head = list.remove(0);
		int tail = list.remove(list.size()-1);
		System.out.println("******RESULTS********");
		System.out.println("Removed element = " + head + ", expected = 1") ;
		System.out.println("Removed element = " + tail + ", expected = 4") ;
		System.out.println(list);
		System.out.println("*********************");	

		System.out.println("\n3) Add 5 and 6 to the end of list");	
		System.out.println("List should contain: 2,3,5,6");
		list.add(5);
		list.add(6);
		System.out.println("******RESULTS********");
		System.out.println(list);
		System.out.println("*********************");

		System.out.println("\n5) Remove 3 and 6");
		System.out.println("List should contain: 2,5");
		list.remove(1);
		list.remove(2);
		System.out.println("******RESULTS********");
		System.out.println(list);
		System.out.println("*********************");

	}

}
