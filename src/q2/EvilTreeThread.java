package q2;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EvilTreeThread extends Thread {
    private final TreeNode root = TreeNode.getRoot();
    private final Random rng = new Random();

    private void traverse(TreeNode n) {
        if (n == null) return;

        //debug
//        System.out.println("Evil Thread:");
//        TreeNode.printValues(n);

        // chance to delete left subtree
        if (n.getLeftChild()!=null && rng.nextFloat() < 0.10) {

            // debug
//            System.out.println("deleting left subtree");

            n.setLeftChild(null);
            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // chance to delete right subtree
        else if (n.getRightChild()!=null &&  rng.nextFloat() < 0.10) {

            // debug
//            System.out.println("deleting right subtree");

            n.setRightChild(null);
            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // chance to create leftChild
        else if (n.getLeftChild() == null && rng.nextFloat() < 0.40) {

            // debug
//            System.out.println("creating left child");

            TreeNode.createChild(n, NodeType.LEFT_CHILD);
            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // chance to create rightChild
        else if (n.getRightChild() == null && rng.nextFloat() < 0.40) {

            // debug
//            System.out.println("creating right child");

            TreeNode.createChild(n, NodeType.RIGHT_CHILD);
            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            if (n.getRightChild()!=null && n.getLeftChild()!=null) {
                if (rng.nextFloat() < 0.5) {
                    // debug
//                    System.out.println("going to left child");
                    traverse(n.getLeftChild());
                }
                else {
                    // debug
//                    System.out.println("going to right child");
                    traverse(n.getRightChild());
                }
            }
            else if (n.getLeftChild()!=null) {
                // debug
//                System.out.println("going to left child");
                traverse(n.getLeftChild());
            }
            else if (n.getRightChild()!=null) {
                // debug
//                System.out.println("going to right child");
                traverse(n.getRightChild());
            }

        }
    }

    public void run() {
        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        AtomicLong end = new AtomicLong(start.get() + 5000);
        while(System.currentTimeMillis() < end.get()) {
            traverse(root);
        }
    }

}
