import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.NoSuchElementException;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (true) {
            try {
                String s = StdIn.readString();
                rq.enqueue(s);
                if (rq.size() > k) {
                    rq.dequeue();
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }

        for (int i = 0; i < k; i += 1) {
            StdOut.println(rq.dequeue());
        }

    }
}
