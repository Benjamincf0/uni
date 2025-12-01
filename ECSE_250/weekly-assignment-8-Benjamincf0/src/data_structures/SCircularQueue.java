package data_structures;

import data_structures.interfaces.SQueue;

public class SCircularQueue<E> implements SQueue<E>{
    private E[] queue;
    private int size;
    private int headIndex;
    private int tailIndex;
    private static final int DEFAULT_CAPACITY = 1;

    public SCircularQueue(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException();
        queue = (E[]) new Object[initialCapacity];
        size = 0;
        headIndex = 0;
        tailIndex = -1;
    }

    public SCircularQueue() {
        this(DEFAULT_CAPACITY);
    }
    
    @Override
    public void enqueue(E e) {
        if (size==queue.length) resize();
        tailIndex = (tailIndex+1)%queue.length;
        queue[tailIndex] = e;
        size++;
    }

    private void resize() {
        // TODO: implement
        E[] newQueue = (E[]) new Object[queue.length * 2];
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[(headIndex + i) % queue.length];
        }
        queue = newQueue;
        headIndex = 0;
        tailIndex = size - 1;
    }

    @Override
    public E dequeue() {
        if (size<1) throw new ArrayIndexOutOfBoundsException("Queue is empty!");
        // TODO: implement
        E element = queue[headIndex];
        queue[headIndex] = null;
        headIndex = (headIndex + 1) % queue.length;
        size--;
        return element;
    }

    @Override
    public E peek() {
        if (size<1) throw new ArrayIndexOutOfBoundsException("Queue is empty!");
        return queue[headIndex];
    }

    public String toString() {
		String msg = "size = " + size + "\n" ;
		msg = msg + "head = " + headIndex + "\n" ;
		msg = msg + "tail = " + tailIndex + "\n" ;		
		for ( int i = 0 ; i < size ; i++ ) {
			int index = (headIndex+i) % queue.length ;
			msg = msg + queue[index] + "," ; 
		}
		
		return msg ;
	}
    
}
