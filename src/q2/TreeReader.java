package q2;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

class TreeReader extends Thread {
    private final TreeNode root = TreeNode.getRoot();
    private final StringBuffer result = new StringBuffer();
    private final Random rng = new Random();

    public StringBuffer getResult() {
        return result;
    }

    private void traverse(TreeNode n) {
        if (n == null) return;
        result.append(n.getData());
        result.append(" ");
        traverse(n.getLeftChild());
        traverse(n.getRightChild());
    }
    public void run() {
        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        AtomicLong end = new AtomicLong(start.get() + 5000);
        while(System.currentTimeMillis() < end.get()) {
            try {
                traverse(root);
                result.append("\n");
                Thread.sleep(rng.nextInt(15) + 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
