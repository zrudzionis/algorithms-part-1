import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private int cnt;
    private Item[] ar;

    public RandomizedQueue() {
        cnt = 0;
        ar = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return cnt == 0;
    }

    public int size() {
        return cnt;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Enqueue does not accept null");
        }
        ar[cnt] = item;
        cnt += 1;
        if (cnt == ar.length) {
            resize(ar.length * 2);
        }
    }

    private void resize(int k) {
        Item[] tmp = (Item[]) new Object[k];
        for (int i = 0; i < cnt; i++) {
            tmp[i] = ar[i];
        }
        ar = tmp;
    }

    public Item dequeue() {
        if (cnt == 0) {
            throw new NoSuchElementException("RandomizedQueue is empty");
        }
        int idx = StdRandom.uniform(cnt);
        Item item = ar[idx];
        ar[idx] = ar[cnt - 1];
        ar[cnt - 1] = null;
        cnt -= 1;
        if (ar.length > 4 && cnt == ar.length / 4) {
            resize(ar.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (cnt == 0) {
            throw new NoSuchElementException("RandomizedQueue is empty");
        }
        int idx = StdRandom.uniform(cnt);
        return ar[idx];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final int[] idxs;
        private int idx;

        public RandomizedQueueIterator() {
            idx = 0;
            idxs = new int[cnt];
            for (int i = 0; i < cnt; i++) {
                idxs[i] = i;
            }
            StdRandom.shuffle(idxs);
        }

        public boolean hasNext() {
            return idx < idxs.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator does not have next");
            } else {
                Item value = ar[idxs[idx]];
                idx += 1;
                return value;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    private static void testEnqueue1() {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("1");
        rq.enqueue("2");
        rq.dequeue();
        rq.dequeue();
        rq.enqueue("3");
        rq.dequeue();
        rq.enqueue("4");
        rq.enqueue("5");
        rq.dequeue();
        rq.dequeue();
    }

    private static void testEnqueue2() {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("6");
        rq.dequeue();
        rq.enqueue("7");
        rq.dequeue();
    }

    private static void testEnqueue3() {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("6");
        rq.dequeue();
        rq.enqueue("7");
        rq.dequeue();
    }

    private static void testEnqueue4() {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("1");
        rq.enqueue("2");
        rq.enqueue("3");
        rq.enqueue("4");
        rq.enqueue("5");
        rq.dequeue();
        rq.enqueue("6");
        rq.dequeue();
        rq.dequeue();
        rq.enqueue("7");
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.enqueue("8");
        rq.dequeue();
        rq.dequeue();
    }

    private static void testSample1() {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        StdOut.println("testSample1");
        rq.enqueue("1");
        rq.enqueue("2");
        rq.enqueue("3");
        rq.enqueue("4");
        rq.enqueue("5");

        for (int i = 0; i < rq.size(); i += 1) {
            StdOut.println(rq.sample());
        }
    }

    private static void testDequeue1() {
        StdOut.println("testDequeue1");
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        for (int i = 0; i < 1000; i++) {
            rq.enqueue(Integer.toString(i));
        }
        while (!rq.isEmpty()) {
            rq.dequeue();
        }
    }

    private static void testIterator1() {
        StdOut.println("testIterator1");
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("1");
        rq.enqueue("2");
        rq.enqueue("3");
        rq.enqueue("4");
        rq.enqueue("5");
        Iterator<String> it = rq.iterator();
        while (it.hasNext()) {
            StdOut.println(it.next());
        }
    }
    private static void testEnqueue5() {
        StdOut.println("testEnqueue5");
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        for (int i = 0; i < 1000; i++) {
            rq.enqueue(Integer.toString(i));
            rq.enqueue(Integer.toString(i));
            rq.dequeue();
        }
        while (!rq.isEmpty()) {
            rq.dequeue();
        }
    }

    public static void main(String[] args) {
        testEnqueue1();
        testEnqueue2();
        testEnqueue3();
        testEnqueue4();
        testSample1();
        testDequeue1();
        testIterator1();
        testEnqueue5();
    }
}
