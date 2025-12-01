package data_structures;

import data_structures.interfaces.SList;
import data_structures.interfaces.SQueue;

public class SListQueue<E> implements SQueue<E>{
    private SList<E> queue;

    public SListQueue(SList<E> list) {
        queue = list;
    }

    public void enqueue(E e) {
        // TODO: implement one-liner
        queue.add(e);
    }

    public E dequeue() {
        // TODO: implement one-liner
        return queue.remove(0);
    }

    public E peek() {
        // TODO: implement one-liner
        return queue.get(0);
    }

    public String toString() {
		return queue.toString();
	}
}
