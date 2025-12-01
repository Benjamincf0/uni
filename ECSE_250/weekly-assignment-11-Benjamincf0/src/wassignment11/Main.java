package wassignment11;

public class Main {
    public static void main(String[] args) {
        BinarySearchTree<Integer> tree2 = new BinarySearchTree<>();
        tree2.add(5);
        tree2.add(3);
        tree2.add(1);
        tree2.add(4);
        tree2.add(2);
        tree2.add(9);
        tree2.printLevelOrder();        
        tree2.printInOrder();
        tree2.printPostOrder();
        tree2.printPreOrder();

        BinarySearchTree<String> treeString = new BinarySearchTree<>();
        treeString.add("A");
        treeString.add("B");
        treeString.add("C");
        treeString.add("D");
        treeString.printLevelOrder();
        treeString.printInOrder();
        treeString.printPostOrder();
        treeString.printPreOrder();

        BinarySearchTree<String> treeString2 = new BinarySearchTree<>();
        treeString2.add("C");
        treeString2.add("D");
        treeString2.add("A");
        treeString2.add("B");
        treeString2.printLevelOrder();
        treeString2.printInOrder();
        treeString2.printPostOrder();
        treeString2.printPreOrder();
    }
}
