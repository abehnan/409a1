package q2;

import javax.xml.soap.Node;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

enum NodeType {
    ROOT,
    LEFT_CHILD,
    RIGHT_CHILD
}

public class TreeNode {
    private static TreeNode root = null;
    private final Random rng = new Random();
    private TreeNode leftChild;
    private TreeNode rightChild;
    private final TreeNode parent;
    private final NodeType type;
    private AtomicInteger data;
    private AtomicInteger depth;

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public TreeNode getParent() {
        return parent;
    }

    public NodeType getType() {
        return type;
    }

    public AtomicInteger getData() {
        return data;
    }

    private AtomicInteger getDepth() {
        return depth;
    }

    private TreeNode() {
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
        this.depth.set(0);
        this.type = NodeType.ROOT;
        this.data.set(Float.floatToIntBits(rng.nextFloat()));
    }

    private TreeNode(TreeNode parent, NodeType type) {
        this.parent = parent;
        this.type = type;
        this.depth.set(parent.depth.addAndGet(1));
        this.leftChild = null;
        this.rightChild = null;
        this.data.set(Float.floatToIntBits(Float.intBitsToFloat(parent.getDepth().get())+rng.nextFloat()));
    }

    private boolean createChildren(TreeNode tn) {
        if (leftChild != null && rightChild != null)
            return false;
        if (leftChild == null)
            leftChild = new TreeNode(tn, NodeType.LEFT_CHILD);
        if (rightChild == null)
            rightChild = new TreeNode(tn, NodeType.RIGHT_CHILD);
        return true;
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

    public static TreeNode getRoot() {
        if (root == null)
            root = new TreeNode();

        return root;
    }
}
