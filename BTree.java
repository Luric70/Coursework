package cse214hw3;

import java.util.*;
import javax.naming.OperationNotSupportedException;

public class BTree<E extends Comparable<E>> {
    private final int minimumDegree;
    private Node<E> root;

    public BTree(int minimumDegree) {
        if (minimumDegree <= 0) {
            throw new IllegalArgumentException("Minimum degree > 0");
        }
        this.minimumDegree = minimumDegree;
        this.root = new Node<>(true);
    }

    private static class Node<T extends Comparable<T>> {
        private int num;
        private boolean leaf;
        private ArrayList<T> values;
        private ArrayList<Node<T>> children;

        private Node(boolean leaf) {
            this.leaf = leaf;
            this.num = 0;
            this.values = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        @Override
        public String toString() {
            return values.toString();
        }
    }

    public void add(E element) throws OperationNotSupportedException {
        if (element == null)
            return;

        //full root handling
        if (root.num == 2 * minimumDegree - 1) {
            Node<E> newRoot = new Node<>(false);
            newRoot.children.add(root);
            splitChild(newRoot, root, 0);
            root = newRoot;
        }
        insertNonFull(element, root);
    }

    private void insertNonFull(E element, Node<E> node) {
        if (node.values.contains(element))
            return;

        int i = node.num;
        if (node.leaf) {
            //if leaf, just insert
            while (i > 0 && element.compareTo(node.values.get(i - 1)) < 0) {
                i--;
            }
            node.values.add(element);
            node.num++;
            Collections.sort(node.values);
        } else {
            //not leaf/full node, split
            while (i > 0 && element.compareTo(node.values.get(i - 1)) < 0)
                i--;
            i++;

            if (node.children.get(i - 1).values.size() == 2 * minimumDegree - 1) {
                splitChild(node, node.children.get(i - 1), i - 1);
                //picks subtree
                if (element.compareTo(node.values.get(i - 1)) > 0) {
                    i++;
                }
            }

            insertNonFull(element, node.children.get(i - 1));
        }
    }


    private void splitChild(Node<E> parent, Node<E> child, int index) {
        Node<E> leftNode = new Node<>(child.leaf);

        int median = (2 * minimumDegree - 1) / 2;

        //add to left subtree
        for (int j = 0; j < median; j++) {
            leftNode.values.add(child.values.remove(minimumDegree));
        }

        if (!child.leaf) {
            for (int j = 0; j <= median; j++)
                leftNode.children.add(child.children.remove(minimumDegree));
        }
        parent.values.add(index, child.values.remove(minimumDegree - 1));
        parent.children.add(index + 1, leftNode);

        parent.num++;
        child.num = minimumDegree - 1; //update child num
    }

    public void addAll(Collection<E> elements) throws OperationNotSupportedException {
        for (E e : elements) {
            this.add(e);
        }
    }
    public void show() {
        String nodesep = " ";
        Queue<Node> queue1 = new LinkedList<>();
        Queue<Node> queue2 = new LinkedList<>();
        queue1.add(root); /* root of the tree being added */
        while (true) {
            while (!queue1.isEmpty()) {
                Node node = queue1.poll();
                System.out.printf("%s%s", node.toString(), nodesep);
                if (!node.children.isEmpty())
                    queue2.addAll(node.children);
            }
            System.out.printf("%n");
            if (queue2.isEmpty())
                break;
            else {
                queue1 = queue2;
                queue2 = new LinkedList<>();
            }
        }
    }

    public static class NodeAndIndex<E extends Comparable<E>> {
        private final Node<E> node;
        private final int index;

        public NodeAndIndex(Node<E> node, int index) {
            this.node = node;
            this.index = index;
        }

        @Override
        public String toString() {
            return "<" + node.values + ", " + index+ ">";
        }
    }
    public NodeAndIndex<E> find(E element) {    //For main method call
        return find(element, root); }

    private NodeAndIndex<E> find(E element, Node<E> node) {
        int i = 0;
        while (i < node.values.size() && element.compareTo(node.values.get(i)) > 0)
            ++i;
        if (i < node.values.size() && element.compareTo(node.values.get(i)) == 0)
            return new NodeAndIndex<>(node, i);
        if (node.leaf)
            return null;

        return find(element, node.children.get(i));
    }
}
