package data_structures;

import data_structures.interfaces.SList;

public class SArrayList<E> implements SList<E> {
    private E[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 1;

    public SArrayList(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException();
        data = (E[]) new Object[initialCapacity];
        size = 0;
    }

    public SArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void add(E e) {
        if (null==e) throw new IllegalArgumentException();
        if(size==data.length) resize();
        data[size++] = e;
    }

    private void resize() {
        E[] newList = (E[]) new Object[data.length*2];
        System.arraycopy(data, 0, newList, 0, size);
        data = newList;
    }

    @Override
    public void add(int index, E e) {
        if(index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        if(size==data.length) resize();
        if(index!=size)
            System.arraycopy(data, index, data, index+1, size-index);
        data[index] = e;
        size++;
    }

    @Override
    public E get(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return data[index];
    }

    @Override
    public E remove(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        // TODO: implement 
        E removedElement = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null; // Clear the last element
        size--;
        return removedElement;
    }

    @Override
    public int size() {
        return size;
    }
    

    public String toString() {
		String msg = "size = " + size + "\n" ;
		for ( int i = 0 ; i < size ; i++ ) {
			msg = msg + data[i] + "," ; 
		}
		return msg ;
	}
}
