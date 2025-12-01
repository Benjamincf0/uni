package data_structures;

import data_structures.interfaces.SList;

public class SDoublyLinkedList<E> implements SList<E>{

    private class SDLLNode {
        private E data;
        private SDLLNode next;
        private SDLLNode prev;

        SDLLNode(E data, SDLLNode next, SDLLNode prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private SDLLNode head;
    private SDLLNode tail;
    private int size;

    public SDoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(E e) {
        // TODO: add element to end of list
        SDLLNode newNode = new SDLLNode(e, null, this.tail);
        if (this.tail == null) {
            this.head = newNode;
        } else {
            this.tail.next = newNode;
        }
        this.tail = newNode;
        this.size++;
    }

    @Override
    public E get(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        SDLLNode node = getNode(index);
        return node.data;
    }

    @Override
    public E remove(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        SDLLNode ret;
        if (index==0) {
            ret = head;
            head = head.next;
            if (null!=head) head.prev = null;
            if (ret==tail) tail = null;
        }
        else if (index==size-1) {
            ret = tail;
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
            }
        }
        else {
            ret = getNode(index);
            ret.prev.next = ret.next;
            ret.next.prev = ret.prev;
        }
        size--;
        return ret.data;
    }

    @Override
    public int size() {
        return size;
    }
    
    private SDLLNode getNode(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        // return the i-th node from head
        // TODO: implement (refer to lecture slides)
        SDLLNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    public String toString() {
		String msg = "size = " + size + "\n" ;
        SDLLNode ptr = head;
		for ( int i = 0 ; i < size ; i++ ) {
			msg = msg + ptr.data + "," ; 
            ptr = ptr.next;
		}
		return msg ;
	}
}
