package deque;

public class ArrayDeque<T> {
    // double-ended queue
    // keep track of the first/last item
    private int size;
    private int nextLast;
    private int nextFirst;
    private T[] items;
    // generic array
    public ArrayDeque() {
        size = 0;
        nextFirst = 4;
        nextLast = 5;
        items = (T[]) new Object[8];
    }
    /** bring out-of-bound index to within-bound */
    public int transistor(int index) {
        if (index < 0) {
            return items.length + index; // e.g. 8 + (-1) = 7
        } else if (index < items.length) {
            return index;
        } else {
            return index - items.length; // e.g. 8 - 8 = 0
        }
    }
    // add and remove must take constant time, except during resizing operations.
    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        if (items.length == size) {
            resize();
        }
        nextFirst = transistor(nextFirst);
        items[nextFirst] = item;
        nextFirst = nextFirst - 1;
        size = size + 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        if (items.length == size) {
            resize();
        }
        nextLast = transistor(nextLast);
        items[nextLast] = item;
        nextLast = nextLast + 1;
        size = size + 1;
    }
    /** Resize the Array */
    public void resize() {
        T[] biggerArray = (T[]) new Object[size * 2];
        // if nextLast > nextFirst
        int realFirst = transistor(nextFirst+1);
        int realLast = transistor(nextLast-1);
        int lengthBeforeResizing = items.length;
        if (realFirst < realLast) {
            System.arraycopy(items, realFirst, biggerArray, realFirst, size);
        } else {
            System.arraycopy(items, 0, biggerArray, 0, realLast+1);
            System.arraycopy(items, realFirst, biggerArray, lengthBeforeResizing + realFirst, lengthBeforeResizing-realFirst);
            nextFirst = nextFirst + lengthBeforeResizing;
        }
        items = biggerArray;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    // get and size must take constant time.
    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last,
     * separated by a space.
     * Once all the items have been printed, print out a new line. */
    public void printDeque() {

    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        nextFirst = transistor(nextFirst + 1);
        T temp = items[nextFirst];
        items[nextFirst] = null;
        size = size -1;
        return temp;
    }

   /** Removes and returns the item at the back of the deque.
    * If no such item exists, returns null. */
   public T removeLast() {
       if (isEmpty()) {
           return null;
       }
       nextLast = transistor(nextLast - 1);
       T temp = items[nextLast];
       items[nextLast] = null;
       size = size -1;
       return temp;
   }

    /** Gets the item at the given index,
     * where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque! */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[transistor(nextFirst + index -1)];
    }
}
