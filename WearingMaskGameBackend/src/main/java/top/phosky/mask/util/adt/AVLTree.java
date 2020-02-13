package top.phosky.mask.util.adt;

//AvlTree class
//
//CONSTRUCTION: with no initializer
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//void remove( x )       --> Remove x (unimplemented)
//boolean contains( x )  --> Return true if x is present
//boolean remove( x )    --> Return true if x was present
//Comparable findMin( )  --> Return smallest item
//Comparable findMax( )  --> Return largest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//void printTree( )      --> Print tree in sorted order
//******************ERRORS********************************
//Throws UnderflowException as appropriate

import org.jetbrains.annotations.NotNull;
import top.phosky.mask.entity.User;

import java.util.Iterator;

/**
 * Implements an AVL tree. Note that all "matching" is based on the compareTo
 * method.
 *
 * @author Mark Allen Weiss & Phosky, NEU
 */
public class AVLTree<AnyType extends Comparable<? super AnyType>> implements Iterable<AnyType> {
    /**
     * Construct the tree.
     */
    public AVLTree() {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert(AnyType x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove(AnyType x) {
        root = remove(x, root);
    }

    /**
     * Internal method to remove from a subtree.
     *
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> remove(AnyType x, AvlNode<AnyType> t) {
        if (t == null)
            return t; // Item not found; do nothing

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = remove(x, t.left);
        else if (compareResult > 0)
            t.right = remove(x, t.right);
        else if (t.left != null && t.right != null) // Two children
        {
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
        } else
            t = (t.left != null) ? t.left : t.right;
        return balance(t);
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public AnyType findMin() {
        if (isEmpty())
            return null; // throw new UnderflowException( );
        return findMin(root).element;
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public AnyType findMax() {
        if (isEmpty())
            return null; // throw new UnderflowException( );
        return findMax(root).element;
    }

    /**
     * Find an item in the tree.
     *
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains(AnyType x) {
        return contains(x, root);
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree() {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume t is either balanced or within one of being balanced
    private AvlNode<AnyType> balance(AvlNode<AnyType> t) {
        if (t == null)
            return t;

        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE)
            if (height(t.left.left) >= height(t.left.right))
                t = rotateWithLeftChild(t);
            else
                t = doubleWithLeftChild(t);
        else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE)
            if (height(t.right.right) >= height(t.right.left))
                t = rotateWithRightChild(t);
            else
                t = doubleWithRightChild(t);

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(AvlNode<AnyType> t) {
        if (t == null)
            return -1;

        if (t != null) {
            int hl = checkBalance(t.left);
            int hr = checkBalance(t.right);
            if (Math.abs(height(t.left) - height(t.right)) > 1 || height(t.left) != hl || height(t.right) != hr)
                System.out.println("OOPS!!");
        }

        return height(t);
    }

    /**
     * Internal method to insert into a subtree.
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert(AnyType x, AvlNode<AnyType> t) {
        if (t == null)
            return new AvlNode<>(x, null, null);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = insert(x, t.left);
        else if (compareResult > 0)
            t.right = insert(x, t.right);
        else
            ; // Duplicate; do nothing
        return balance(t);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyType> findMin(AvlNode<AnyType> t) {
        if (t == null)
            return t;

        while (t.left != null)
            t = t.left;
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<AnyType> findMax(AvlNode<AnyType> t) {
        if (t == null)
            return t;

        while (t.right != null)
            t = t.right;
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains(AnyType x, AvlNode<AnyType> t) {
        while (t != null) {
            int compareResult = x.compareTo(t.element);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else
                return true; // Match
        }

        return false; // No match
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the tree.
     */
    private void printTree(AvlNode<AnyType> t) {
        if (t != null) {
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height(AvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    /**
     * Rotate binary tree node with left child. For AVL trees, this is a single
     * rotation for case 1. Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2) {
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child. For AVL trees, this is a single
     * rotation for case 4. Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> k1) {
        AvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child with its right child; then
     * node k3 with new left child. For AVL trees, this is a double rotation for
     * case 2. Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child with its left child; then
     * node k1 with new right child. For AVL trees, this is a double rotation for
     * case 3. Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithRightChild(AvlNode<AnyType> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    /**
     * 有两种实现方法：直接生成一个序列等待访问；一下一下地访问，用栈来实现
     * <p>
     * 我采用的是第一种，性能较低
     */
    @NotNull
    @Override
    public Iterator<AnyType> iterator() {
        Queue<AnyType> answerQueue = innerTravesal(root, new Queue<>());
        return answerQueue.iterator();
    }

    private Queue<AnyType> innerTravesal(AvlNode<AnyType> current, Queue<AnyType> ansQueue) {
        if (current != null) {
            ansQueue = innerTravesal(current.left, ansQueue);
            ansQueue.push(current.element);
            ansQueue = innerTravesal(current.right, ansQueue);
        }
        return ansQueue;
    }

    private static class AvlNode<AnyType> {
        // Constructors
        AvlNode(AnyType theElement) {
            this(theElement, null, null);
        }

        AvlNode(AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
            height = 0;
        }

        AnyType element; // The data in the node
        AvlNode<AnyType> left; // Left child
        AvlNode<AnyType> right; // Right child
        int height; // Height
    }

    /**
     * The tree root.
     */
    private AvlNode<AnyType> root;

    // Test program
    public static void main(String[] args) {
        AVLTree<User> t = new AVLTree<>();
        User u1 = new User("1", "", 123);
        User u2 = new User("2", "", 15151);
        User u3 = new User("3", "", 434);
        User u4 = new User("4", "", 999999);
        User u5 = new User("5", "", 54444);
        User u6 = new User("6", "", 345456);
        t.insert(u1);
        t.insert(u2);
        t.insert(u3);
        t.insert(u4);
        t.insert(u5);
        t.insert(u6);
        for (User u : t) {
            System.out.println(u.getWxID() + "  " + u.getMaxMarks());
        }


    }
}

/**
 * 基于链表的队列
 *
 * @author Phosky, NEU
 * @version 0.3
 */
class Queue<T> implements Iterable<T> {
    private static final long serialVersionUID = -7105179284382505263L;

    private Node<T> first;
    private Node<T> last;

    public Queue() {
        super();
    }

    public void push(T x) {
        if (last == null) {
            first = last = new Node<T>(x);
        } else {
            last.next = new Node<T>(x);
            last = last.next;
        }
    }

    public void pop() {
        if (first != null) {
            if (first.next == null) {
                first = last = null;
            } else {
                first = first.next;
            }
        }
    }

    /**
     * 在队列空的时候会返回null
     */
    public T top() {
        if (first == null) {
            return null;
        } else {
            return first.x;
        }
    }

    public boolean isEmpty() {
        return (first == null && last == null);
    }

    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {
            Node<T> n = first;

            @Override
            public boolean hasNext() {
                return n != null;
            }

            @Override
            public T next() {
                Node<T> nNow = n;
                n = n.next;
                return nNow.x;
            }
        };
        return it;
    }
}

class Node<T> {
    private static final long serialVersionUID = -7689515258891913687L;
    T x;
    Node<T> next;

    public Node(T x) {
        super();
        this.x = x;
    }
}

