package q2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

enum NodeType {
    ROOT,
    LEFT_CHILD,
    RIGHT_CHILD
}

public class TreeNode {
    private static TreeNode root = null;
    private static final Random rng = new Random();
    private TreeNode leftChild;
    private TreeNode rightChild;
    private TreeNode parent;
    private NodeType type;
    private AtomicInteger data;
    private AtomicInteger depth;

    private TreeNode() {
    }

    private TreeNode(TreeNode parent, NodeType type) {
        this.parent = parent;
        this.type = type;
        this.setDepth(new AtomicInteger(parent.getDepth().get() + 1));
        this.leftChild = null;
        this.rightChild = null;
        this.setData(new AtomicInteger(Float.floatToIntBits(depth.get() +rng.nextFloat())));
    }

    public static TreeNode getRoot() {
        if (root == null) {
            root = new TreeNode();
            root.leftChild = null;
            root.rightChild = null;
            root.parent = null;
            root.setDepth(new AtomicInteger(0));
            root.type = NodeType.ROOT;
            root.setData(new AtomicInteger(Float.floatToIntBits(rng.nextFloat())));
        }

        return root;
    }

    public static void createChildren(TreeNode n) {
        if (n.leftChild != null && n.rightChild != null)
            return;
        if (n.leftChild == null)
            n.leftChild = new TreeNode(n, NodeType.LEFT_CHILD);
        if (n.rightChild == null)
            n.rightChild = new TreeNode(n, NodeType.RIGHT_CHILD);
    }


    public static void printAll(TreeNode n) {
        if (n.getType() != NodeType.ROOT) return;
        Queue<TreeNode> currentLevel = new LinkedList<>();
        AtomicInteger nodesInCurrentLevel = new AtomicInteger(1);
        AtomicInteger nodesInNextLevel = new AtomicInteger(0);

        currentLevel.add(n);
        while (!currentLevel.isEmpty()) {
            TreeNode currentNode = currentLevel.poll();
            nodesInCurrentLevel.decrementAndGet();
            if (currentNode != null) {
                System.out.println(currentNode.getData() + " ");
                currentLevel.add(currentNode.getLeftChild());
                currentLevel.add(currentNode.getRightChild());
                nodesInNextLevel.addAndGet(2);
            }
            if (nodesInCurrentLevel.get() == 0) {
                System.out.println();
                nodesInCurrentLevel.set(nodesInNextLevel.get());
                nodesInNextLevel.set(0);
            }
        }
    }


    private boolean createLeftChild(TreeNode tn) {
        if (leftChild != null)
            return false;
        leftChild = new TreeNode(tn, NodeType.LEFT_CHILD);
        return true;
    }

    private boolean createRightChild(TreeNode tn) {
        if (rightChild != null)
            return false;
        rightChild = new TreeNode(tn, NodeType.RIGHT_CHILD);
        return true;
    }

    private void setData(AtomicInteger data) {
        this.data = data;
    }

    private void setDepth(AtomicInteger depth) {
        this.depth = depth;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public TreeNode getParent() {
        return parent;
    }

    private NodeType getType() {
        return type;
    }

    private float getData() {
        return Float.intBitsToFloat(data.get());
    }

    public AtomicInteger getDepth() {
        return depth;
    }

}
