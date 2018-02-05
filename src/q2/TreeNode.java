package q2;

// note: queue only being used for debug printing, not actual tree traversal
import java.util.ArrayList;
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
    private static final ArrayList<AtomicInteger> values = new ArrayList<>();
    private TreeNode leftChild;
    private TreeNode rightChild;
    private TreeNode parent;
    private NodeType type;
    private AtomicInteger data;
    private AtomicInteger depth;

    public static ArrayList<AtomicInteger> getAllValues() {
        return values;
    }

    private static void addValue(AtomicInteger i) {
        values.add(i);
    }

    public static void printValues() {
        System.out.print("[");
        for (AtomicInteger i : values) {
            System.out.print(Float.intBitsToFloat(i.get()) + ", ");
        }
        System.out.print("]\n");
    }

    private TreeNode() {
    }

    private TreeNode(TreeNode parent, NodeType type) {
        this.parent = parent;
        this.type = type;
        this.depth = new AtomicInteger(parent.getDepth().incrementAndGet());
        parent.getDepth().decrementAndGet();
        this.leftChild = null;
        this.rightChild = null;
        this.data = new AtomicInteger(Float.floatToIntBits(valueScaling.incrementAndGet()*1000 + rng.nextFloat()));
        addValue(this.data);
    }

    TreeNode(TreeNode parent, NodeType type, float value) {
        this.parent = parent;
        this.type = type;
        this.depth = new AtomicInteger(parent.getDepth().incrementAndGet());
        parent.getDepth().decrementAndGet();
        this.leftChild = null;
        this.rightChild = null;
        if (this.data == null)
            this.data = new AtomicInteger();
        this.setData(value);
    }

    public static TreeNode getRoot() {
        if (root == null) {
            root = new TreeNode();
            root.leftChild = null;
            root.rightChild = null;
            root.parent = null;
            root.depth = new AtomicInteger(0);
            root.type = NodeType.ROOT;
            root.data = new AtomicInteger(Float.floatToIntBits(rng.nextFloat()));
            values.add(root.data);
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

    public static void initializeLeftChild(TreeNode n) {
        if (n.leftChild == null)
            n.leftChild = new TreeNode(n, NodeType.LEFT_CHILD);
    }

    public static void initializeRightChild(TreeNode n) {
        if (n.rightChild == null)
            n.rightChild = new TreeNode(n, NodeType.RIGHT_CHILD);
    }

    public boolean equals(TreeNode n) {
        return getFloat() == n.getFloat();
    }

//    private void getPreviousNode(TreeNode n, TreeNode target, TreeNode previous) {
//        AtomicInteger min = getData();
//        if (n == null) return;
//        if (n.getFloat() > Float.intBitsToFloat(min.get())) {
//            min.set(Float.floatToIntBits(n.getFloat()));
//        }
//        if (n.equals(target)) {
//            return min
//        }
//
//    }


    // used by the evil thread to determine the boundaries
//    public static void generateNewData(TreeNode n) {
//        // find boundary by traversing it properly
//        TreeNode previousNode;
//        TreeNode nextNode;
//        TreeNode currentNode = getRoot();
//        while(true) {
//            if (currentNode)
//        }
//    }


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
                        "\tdata: " + currentNode.getFloat() +
                        "\ttype: " + currentNode.getType() +
                        ((currentNode.getParent() == null) ? "" : ("\tparent: " + currentNode.getParent().getFloat())));
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

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    private TreeNode getParent() {
        return parent;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

    private NodeType getType() {
        return type;
    }

    public float getFloat() {
        return Float.intBitsToFloat(data.get());
    }

    public int getInt() {
        return data.get();
    }

    public void setData(int data) {
        this.data.set(data);
    }

    public void setData(float data) {
        this.data.set(Float.floatToIntBits(data));
    }

    public AtomicInteger getData() {
        return data;
    }

    public AtomicInteger getDepth() {
        return depth;
    }

}
