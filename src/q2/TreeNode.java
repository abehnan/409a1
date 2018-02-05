package q2;

// note: queue only being used for debug printing, not actual tree traversal
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
    private static final AtomicInteger valueScaling = new AtomicInteger(0);
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
        this.setData(new AtomicInteger(Float.floatToIntBits(valueScaling.incrementAndGet() + rng.nextFloat())));
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

//    public static void createChildren(TreeNode n) {
//        if (n.leftChild != null && n.rightChild != null)
//            return;
//        if (n.leftChild == null)
//            n.leftChild = new TreeNode(n, NodeType.LEFT_CHILD);
//        if (n.rightChild == null)
//            n.rightChild = new TreeNode(n, NodeType.RIGHT_CHILD);
//    }

    public static void createLeftChild(TreeNode n) {
        if (n.leftChild == null)
            n.leftChild = new TreeNode(n, NodeType.LEFT_CHILD);
    }

    public static void createRightChild(TreeNode n) {
        if (n.rightChild == null)
            n.rightChild = new TreeNode(n, NodeType.RIGHT_CHILD);
    }

    // prints the entire tree using BFS
    // might not be useful to check for in order traversal
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
                System.out.println("depth: " + currentNode.getDepth().get() +
                        "\tdata: " + currentNode.getData() +
                        "\ttype: " + currentNode.getType() +
                        ((currentNode.getParent() == null) ? "" : ("\tparent: " + currentNode.getParent().getData())));
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

    private TreeNode getParent() {
        return parent;
    }

    private NodeType getType() {
        return type;
    }

    public float getData() {
        return Float.intBitsToFloat(data.get());
    }

    public AtomicInteger getDepth() {
        return depth;
    }

}
