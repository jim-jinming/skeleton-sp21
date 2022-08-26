package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private int size;
    private Node sentinel;

    public class Node {
        public Node prev;
        public T item;
        public Node next;

        public Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    /** Creates an empty linked list deque */
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /** Creates a deep copy of existingDeque,
     *  Deep copy: has no reference to the original,
     *  Only copy the primitive-typed values and its relations */
    public LinkedListDeque<T> deepCopy(LinkedListDeque<T> existingDeque) {
        // instantiate deque
        LinkedListDeque<T> freshDeque = new LinkedListDeque<T>();
        Node pointer = existingDeque.sentinel.prev;
        // iterate through existing-deque
        for(int count = 0; count < existingDeque.size; count++) {
            // get last node of existing-deque -> (item) AddFirst to fresh-deque
            freshDeque.addFirst(pointer.item);
            pointer = pointer.prev;
        }
        return freshDeque;
    }

    /** Adds an item of type T to the front of the deque */
    @Override
    public void addFirst(T item) {
        size += 1;
        // modify previousNode.next, which is always sentinel
        Node firstNode = new Node(sentinel, item, sentinel.next);
        sentinel.next = firstNode;
        // modify the nextNode.previous, to be this node.
        firstNode.next.prev = firstNode;
    }


    /** Adds an item of type T to the back of the deque */
    @Override
    public void addLast(T item) {
        size += 1;
        // previous node of sentinel is always pointed to the Last Node,
        // and the next node of Last Node is always pointed to the sentinel
        Node lastNode = new Node(sentinel.prev, item, sentinel);
        sentinel.prev = lastNode;
        lastNode.prev.next = lastNode;
    }

    /* Returns true if deque is empty, false otherwise */

    /** Returns the number of items in the deque */
    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last,
     * separated by a space.
     * Once all the items have been printed, print out a new line */
    @Override
    public void printDeque() {
        Node p = sentinel;
        for (int c = 0; c < size; c++) {
            System.out.print(p.next.item);
            System.out.println(" ");
            p = p.next;
        }
        System.out.print("\n");

    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null */
    @Override
    public T removeFirst() {
        if (size != 0) {
            size--;
            // reference the first node.
            Node toRemove = sentinel.next;
            Node secondFront = toRemove.next;
            // sentinel's next -> second-front node
            sentinel.next = secondFront;
            // second-front node's prev -> sentinel
            secondFront.prev = sentinel;
            // erase node-pointers
            toRemove.next = null;
            toRemove.prev = null;
            return toRemove.item;
        } else {
            return null;
        }
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null */
    @Override
    public T removeLast() {
        if (size != 0) {
            size--;
            Node toRemove = sentinel.prev;
            Node secondBack = toRemove.prev;
            // sentinel's prev -> second-back node.
            sentinel.prev = secondBack;
            // second-back node's next -> sentinel.
            secondBack.next = sentinel;
            // erase node-pointers
            toRemove.next = null;
            toRemove.prev = null;
            return toRemove.item;
        } else  {
            return null;
        }
    }

    /** Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * Must not alter the deque! */
    @Override
    public T get(int index) {
        // consider negative?
        Node startAt = sentinel;
        // index(0-) < size(1-)
        if (index > size - 1) {
            return null;
        }
        for (int count = 0; count <= index; count++) {
            startAt = startAt.next;
        }
        return startAt.item;
    }

    /** Recursive get */
    // copies variable in a private scope,
    // then operate on this mutable variables.
    private T getRecursive(Node startAt, int index) {
        if (index == 0) {
            return startAt.next.item;
        }
        return getRecursive(startAt.next, index - 1);
    }
    public T getRecursive(int index) {
        if (index > size - 1) {
            return null;
        }

        return getRecursive(sentinel, index);
    }
}
