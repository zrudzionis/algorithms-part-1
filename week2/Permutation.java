import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (k > 0 && rq.size() == k) {
                rq.dequeue();
            }
            rq.enqueue(s);
        }

        for (int i = 0; i < k; i += 1) {
            StdOut.println(rq.dequeue());
        }

    }
}
