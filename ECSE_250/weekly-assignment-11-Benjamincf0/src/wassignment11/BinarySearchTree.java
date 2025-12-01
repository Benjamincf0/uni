package wassignment11;

import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<T extends Comparable<T>> {
    public class Node {
        T key;
        Node left;
        Node right;

        public Node(T key) {
            this.key = key;
        }
    }

    Node root = null;

    public void add(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        if (root == null) {
            root = new Node(key);
            return;
        }

        add(root, key);
    }

    private void add(Node node, T key) {
        // TODO: Add your code here
        Node next = null;
        if (key.compareTo(node.key) > 0) {
            if(node.right == null) {
                node.right = new Node(key);
                return;
            }
            add(node.right, key);
        } else {
            if (node.left == null) {
                node.left = new Node(key);
                return;
            }
            add(node.left, key);
        }
    }

    public void printPreOrder() {
        printPreOrder(root);
        System.out.println("");
    }

    public void printInOrder() {
        printInOrder(root);
        System.out.println("");
    }

    public void printPostOrder() {
        printPostOrder(root);
        System.out.println("");
    }

    private void printPreOrder(Node node) {
        // TODO: Add your code here
        if (node == null) {
            return;
        }
        System.out.print(node.key + ", ");
        printPreOrder(node.left);
        printPreOrder(node.right);
    }

    private void printInOrder(Node node) {
        //TODO: Add your code here
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.print(node.key + ", ");
        printInOrder(node.right);
    }

    private void printPostOrder(Node node) {
        //TODO: Add your code here
        if (node == null) {
            return;
        }
        printPostOrder(node.left);
        printPostOrder(node.right);
        System.out.print(node.key + ", ");
    }

    public void printLevelOrder() {
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node node = q.remove();
            System.out.print(node.key + ", ");
            if (node.left != null) {
                q.add(node.left);
            }
            if (node.right != null) {
                q.add(node.right);
            }
        }
        System.out.println("");
    }
}