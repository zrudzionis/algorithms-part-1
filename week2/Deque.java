import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;


public class Deque<Item> implements Iterable<Item> {
    private class Node {
        public Node left;
        public Node right;
        public Item value;

        public Node(Item value) {
            this.value = value;
        }
    }

    private Node first;
    private Node last;
    private int count = 0;

    public Deque() {
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public int size() {
        return this.count;
    }

    public void addFirst(Item item) {
        if (!add(item)) {
            Node newNode = new Node(item);
            first.left = newNode;
            newNode.right = first;
            first = newNode;
            count += 1;
        }
    }

    public void addLast(Item item) {
        if (!this.add(item)) {
            Node newNode = new Node(item);
            last.right = newNode;
            newNode.left = last;
            last = newNode;
            count += 1;
        }
    }

    private boolean add(Item value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot add null values");
        }
        if (this.isEmpty()) {
            Node newNode = new Node(value);
            first = newNode;
            last = newNode;
            count += 1;
            return true;
        } else {
            return false;
        }
    }

    public Item removeFirst() {
        Item value = this.remove();
        if (value == null) {
            value = this.first.value;
            this.first = this.first.right;
            this.first.left = null;
            this.count -= 1;
        }
        return value;
    }

    public Item removeLast() {
        Item value = this.remove();
        if (value == null) {
            value = this.last.value;
            this.last = this.last.left;
            this.last.right = null;
            this.count -= 1;
        }
        return value;
    }

    private Item remove() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        } else if (this.count == 1) {
            Item value = first.value;
            first = null;
            last = null;
            count -= 1;
            return value;
        } else {
            return null;
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node cur;

        public DequeIterator() {
            this.cur = first;
        }

        public boolean hasNext() {
            return this.cur != null;
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("Iterator does not have next");
            } else {
                Item value = cur.value;
                cur = cur.right;
                return value;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    public static void main(String[] args) {
        Deque<String> d = new Deque<String>();
        d.addFirst("1");
        d.addFirst("2");
        StdOut.println(d.removeFirst());
        StdOut.println(d.removeFirst());

        StdOut.println();
        d.addFirst("1");
        d.addFirst("2");
        StdOut.println(d.removeLast());
        StdOut.println(d.removeLast());

        StdOut.println();
        d.addFirst("1");
        d.addFirst("2");
        d.addFirst("3");
        StdOut.println(d.isEmpty());
        StdOut.println(d.removeFirst());
        StdOut.println(d.removeLast());
        StdOut.println(d.size());
        StdOut.println(d.removeLast());
        StdOut.println(d.size());
        StdOut.println(d.isEmpty());

        StdOut.println();
        d.addFirst("1");
        d.addFirst("2");
        d.addFirst("3");
        Iterator<String> it = d.iterator();
        while (it.hasNext()) {
            StdOut.println(it.next());
        }
    }
}
