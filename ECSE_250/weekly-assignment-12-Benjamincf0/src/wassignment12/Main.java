package wassignment12;

public class Main {
    public static void main(String[] args) {
        Heap<Integer> heap = new Heap<>();
        heap.add(5);
        heap.add(3);
        heap.add(1);
        heap.add(4);
        heap.add(2);
        heap.add(9);
        System.out.println(heap.heap);

        System.out.println(heap.removeMin());
        System.out.println(heap.heap);

        System.out.println(heap.removeMin());
        System.out.println(heap.heap);

        Heap<Integer> heap1 = new Heap<>();
        heap1.add(7);
        heap1.add(11);
        heap1.add(3);
        heap1.add(1);
        heap1.add(9);
        heap1.add(2);
        System.out.println(heap1.heap);

        Heap<Integer> heap2 = new Heap<>();
        heap2.add(7);
        heap2.add(9);
        heap2.add(11);
        heap2.add(1);
        heap2.add(2);
        heap2.add(3);
        System.out.println(heap2.heap);
    }
}
