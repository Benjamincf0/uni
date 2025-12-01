package data_structures.interfaces;

public interface SQueue<E> {
    void enqueue(E e);
    E dequeue();
    E peek();
}
