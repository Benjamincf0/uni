package wassignment12;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>>{
    public ArrayList<T> heap;

    public Heap() {
        heap = new ArrayList<>();
    }

    public void add(T e) {
        heap.add(e);
        int currentIndex = heap.size() - 1;
        
        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / 2;
            T parent = heap.get(parentIndex);

            if (e.compareTo(parent) < 0) {
                swap(currentIndex, parentIndex);
                currentIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    public T removeMin() {
        if (heap.isEmpty()) {
            return null;
        }

        T min = heap.get(0);
        T lastElement = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, lastElement);
            heapifyDown(0);
        }

        return min;
    }

    private void heapifyDown(int index) {
        int leftChildIndex = 2 * index + 1;
        int rightChildIndex = 2 * index + 2;
        int smallestIndex = index;

        if (leftChildIndex < heap.size() && heap.get(leftChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = leftChildIndex;
        }

        if (rightChildIndex < heap.size() && heap.get(rightChildIndex).compareTo(heap.get(smallestIndex)) < 0) {
            smallestIndex = rightChildIndex;
        }

        if (smallestIndex != index) {
            swap(index, smallestIndex);
            heapifyDown(smallestIndex);
        }
    }

    public void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}