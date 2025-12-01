import data_structures.*;
import data_structures.interfaces.*;

public class Main {
    public static void main(String[] args) throws Exception {

		System.out.println("\n\n **************************TESTING SINGLYLINKEDLIST*****************************");
		testList(new SSinglyLinkedList<>());
		System.out.println("\n\n **************************TESTING DOUBLYLINKEDLIST*****************************");
		testList(new SDoublyLinkedList<>());
		System.out.println("\n\n **************************TESTING ARRAYLIST*****************************");
		testList(new SArrayList<>());

		System.out.println("\n\n **************************TESTING ARRAYLIST STACK*****************************");
		testStack(new SListStack<>(new SArrayList<>()));
		System.out.println("\n\n **************************TESTING SINGLYLINKEDLIST STACK*****************************");
		testStack(new SListStack<>(new SSinglyLinkedList<>()));
		System.out.println("\n\n **************************TESTING DOUBLYLINKEDLIST STACK*****************************");
		testStack(new SListStack<>(new SDoublyLinkedList<>()));

		System.out.println("\n\n **************************TESTING ARRAYLIST QUEUE*****************************");
		testQueue(new SListQueue<>(new SArrayList<>()));
		System.out.println("\n\n **************************TESTING SINGLYLINKEDLIST QUEUE*****************************");
		testQueue(new SListQueue<>(new SSinglyLinkedList<>()) );
		System.out.println("\n\n **************************TESTING DOUBLYLINKEDLIST QUEUE*****************************");
		testQueue(new SListQueue<>(new SDoublyLinkedList<>())  );
		
		System.out.println("\n\n **************************TESTING CIRCULAR QUEUE*****************************");
		testQueue(new SCircularQueue<>());
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

		System.out.println("\n4) Add 7 and 8 to the middle of list");
		System.out.println("List should contain: 2,3,7,8,5,6");
		list.add(2, 7);
		list.add(3, 8);
		System.out.println("******RESULTS********");
		System.out.println(list);
		System.out.println("*********************");

		System.out.println("\n5) Remove 3 and 8");
		System.out.println("List should contain: 2,7,5,6");
		list.remove(1);
		list.remove(2);
		System.out.println("******RESULTS********");
		System.out.println(list);
		System.out.println("*********************");

		System.out.println("\n5) Add 9 and 10 to start of list");
		System.out.println("List should contain: 10,9,2,7,5,6");
		list.add(0, 9);
		list.add(0, 10);
		System.out.println("******RESULTS********");
		System.out.println(list);
		System.out.println("*********************");
	}

	static void testQueue(SQueue<Integer> queue) {
		System.out.println("1) Enqueue 1,2,3,4") ;
		System.out.println("Queue should contain: 1,2,3,4");
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);		
		System.out.println("******RESULTS********");
		System.out.println(queue);
		System.out.println("*********************");
		
		System.out.println("\n2) Dequeue once") ;
		System.out.println("Queue should contain: 2,3,4");
		int top = queue.dequeue();
		System.out.println("******RESULTS********");
		System.out.println("Dequeued element = " + top + ", expected = 1") ;
		System.out.println(queue);
		System.out.println("*********************");

		System.out.println("\n3) Enqueue 5, 6") ;
		System.out.println("Queue should contain: 2,3,4,5,6");
		queue.enqueue(5);
		queue.enqueue(6);			
		System.out.println("******RESULTS********");
		System.out.println(queue);
		System.out.println("*********************");

		System.out.println("\n4) Enqueue 7, 8, dequeue 5 times, enqueue 9, 10") ;
		System.out.println("Queue should contain: 7,8,9,10");
		queue.enqueue(7);
		queue.enqueue(8);		
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.enqueue(9);
		queue.enqueue(10);
		System.out.println("******RESULTS********");
		System.out.println(queue);
		System.out.println("*********************");
	}

	static void testStack(SStack<Integer> stack) {
		System.out.println("1) Push 1,2,3,4") ;
		System.out.println("Stack should contain: 1,2,3,4");
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);		
		System.out.println("******RESULTS********");
		System.out.println(stack);
		System.out.println("*********************");
		
		System.out.println("\n2) Pop once") ;
		System.out.println("Stack should contain: 1,2,3");
		int top = stack.pop();
		System.out.println("******RESULTS********");
		System.out.println("Popped element = " + top + ", expected = 4") ;
		System.out.println(stack);
		System.out.println("*********************");

		System.out.println("\n3) Push 5, 6") ;
		System.out.println("Stack should contain: 1,2,3,5,6");
		stack.push(5);
		stack.push(6);			
		System.out.println("******RESULTS********");
		System.out.println(stack);
		System.out.println("*********************");

		System.out.println("\n4) Push 7, 8, dequeue 5 times, enqueue 9, 10") ;
		System.out.println("Stack should contain: 1,2,9,10");
		stack.push(7);
		stack.push(8);		
		stack.pop();
		stack.pop();
		stack.pop();
		stack.pop();
		stack.pop();
		stack.push(9);
		stack.push(10);
		System.out.println("******RESULTS********");
		System.out.println(stack);
		System.out.println("*********************");

	}
}
