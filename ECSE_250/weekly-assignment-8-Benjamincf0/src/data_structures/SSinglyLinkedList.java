package data_structures;

import data_structures.interfaces.SList;

public class SSinglyLinkedList<E> implements SList<E>{
    private class SSLLNode {
        private E data;
        private SSLLNode next;

        SSLLNode(E data) {
            this(data, null);
        }

        SSLLNode(E data, SSLLNode next) {
            this.data = data;
            this.next = next;
        }
    }

    private SSLLNode head;
    private SSLLNode tail;
    private int size;

    public SSinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(E e) {
        SSLLNode node = new SSLLNode(e);
        if (null==head) {
            head = node;
            tail = node;
        }
        else {
            tail.next = node;
            tail = node;
        }
        size++; 
    }

    @Override
    public void add(int index, E e) {
        if(index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        // TODO: implement
        SSLLNode node = new SSLLNode(e);
    
        if (index == 0) {
            node.next = head;
            head = node;
            if (size == 0) {
                tail = node;
            }
        } else if (index == size) {
            tail.next = node;
            tail = node;
        } else {
            SSLLNode prev = getNode(index - 1);
            node.next = prev.next;
            prev.next = node;
        }
        
        size++;
    }

    @Override
    public E get(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        SSLLNode pointer = getNode(index);
        return pointer.data;
    }

    @Override
    public E remove(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        SSLLNode ret;
        if (index==0) {
            ret = head;
            head = head.next;
            if (ret==tail) tail = null;
        }
        else if (index==size-1) {
            SSLLNode prev = getNode(index-1);
            ret = tail;
            prev.next = tail.next;
            tail = prev; 
        }
        else {
            SSLLNode prev = getNode(index-1);           
            ret = prev.next;
            prev.next = prev.next.next;
        }
        size--;
        return ret.data;
    }

    @Override
    public int size() {
        return size;
    }
    
    private SSLLNode getNode(int index) {
        if(index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        // return the i-th node from head
        SSLLNode node;
        node = head;
        for (int i=0; i<index; i++) {
            node = node.next;
        }
        return node;
    }

    public String toString() {
		String msg = "size = " + size + "\n" ;
        SSLLNode ptr = head;
		for ( int i = 0 ; i < size ; i++ ) {
			msg = msg + ptr.data + "," ; 
            ptr = ptr.next;
		}
		return msg ;
	}
}
