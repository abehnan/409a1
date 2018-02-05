package q2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EvilTreeThread extends Thread {
    private final TreeNode root = TreeNode.getRoot();
    private final Random rng = new Random();
    private static final ArrayList<TreeNode> orderedNodes = new ArrayList<>();

    private static void updateValues(TreeNode n) {
        if (n == null) return;
        orderedNodes.add(n);
        updateValues(n.getLeftChild());
        updateValues(n.getRightChild());
    }

    private void setCorrectFloatValue(TreeNode n) {
        orderedNodes.clear();
        updateValues(root);

//        if (orderedNodes.size() == 2) {
//            float min = orderedNodes.get(0).getFloat();
//            float max = 10000;
//            float nodeData = min + rng.nextFloat() * (max - min);
//            n.setData(nodeData);
//
//        }
//        else {
            int i;
            for (i = 0; i < orderedNodes.size(); i++) {
                if (orderedNodes.get(i).getFloat() == 0)
                    break;
            }
            if (i == orderedNodes.size()-1) {
                int minIndex = Math.max(0, i-1);
                float min = orderedNodes.get(minIndex).getFloat();
                float max = min + 1000;
                float nodeData = min + rng.nextFloat() * (max - min);
                n.setData(nodeData);
            }
            else {
                int minIndex = Math.max(0, i-1);
                int maxIndex = Math.min(orderedNodes.size()-1, i+1);
                float min = orderedNodes.get(minIndex).getFloat();
                float max = orderedNodes.get(maxIndex).getFloat();
                float nodeData = min + rng.nextFloat() * (max - min);
                n.setData(nodeData);
            }

//        }

    }

    private void traverse(TreeNode n) {
        if (n == null) return;

        // debug
        orderedNodes.clear();
        updateValues(root);
        orderedNodes.sort(Comparator.comparingInt(o -> (int) o.getFloat()));
        int i;
        for (i = 0; i < orderedNodes.size(); i++) {
            if (orderedNodes.get(i).equals(n))
                System.out.print("**" +new DecimalFormat("#.00").format(orderedNodes.get(i).getFloat()) + "** ");
            else
                System.out.print(new DecimalFormat("#.00").format(orderedNodes.get(i).getFloat()) + " ");
        }
        System.out.println();
        // end of debug

        // chance to delete left subtree
        if (n.getLeftChild()!=null && rng.nextFloat() < 0.10) {
            System.out.println("deleting left subtree");
            n.setLeftChild(null);
            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // chance to delete right subtree
        else if (n.getRightChild()!=null &&  rng.nextFloat() < 0.10) {
            System.out.println("deleting right subtree");
            n.setRightChild(null);
            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // chance to create leftChild
        else if (n.getLeftChild() == null && rng.nextFloat() < 0.40) {
            System.out.println("creating left child");
            TreeNode newNode = new TreeNode(n, NodeType.LEFT_CHILD, 0);
            n.setLeftChild(newNode);
            setCorrectFloatValue(newNode);

            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // chance to create rightChild
        else if (n.getRightChild() == null && rng.nextFloat() < 0.40) {
            System.out.println("creating right child");
            TreeNode newNode = new TreeNode(n, NodeType.RIGHT_CHILD, 0);
            n.setRightChild(newNode);
            setCorrectFloatValue(newNode);
            try {
                Thread.sleep(rng.nextInt(4)+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            if (n.getRightChild()!=null && n.getLeftChild()!=null) {
                if (rng.nextFloat() < 0.5) {
                    System.out.println("going to left child");
                    traverse(n.getLeftChild());
                }
                else {
                    System.out.println("going to right child");
                    traverse(n.getRightChild());
                }
            }
            else if (n.getLeftChild()!=null) {
                System.out.println("going to left child");
                traverse(n.getLeftChild());
            }
            else if (n.getRightChild()!=null) {
                System.out.println("going to right child");
                traverse(n.getRightChild());
            }

        }
    }

    public void run() {
//        AtomicLong start = new AtomicLong(System.currentTimeMillis());
//        AtomicLong end = new AtomicLong(start.get() + 5000);
//        while(System.currentTimeMillis() < end.get()) {
//            traverse(root);
//        }
        int count = 0;
        while (count < 30) {
            traverse(root);
            count++;
        }
    }

}
