package q2;

// note: queue only being used for debug printing, not actual tree traversal
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

enum NodeType {
    ROOT,
    LEFT_CHILD,
    RIGHT_CHILD
}
@SuppressWarnings("unused")
public class TreeNode {
    private static TreeNode root = null;
    private static final Random rng = new Random();
    private static final AtomicInteger valueScaling = new AtomicInteger(0);
    private static final ArrayList<TreeNode> orderedNodes = new ArrayList<>();
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
        this.depth = new AtomicInteger(parent.getDepth().incrementAndGet());
        parent.getDepth().decrementAndGet();
        this.leftChild = null;
        this.rightChild = null;
        this.data = new AtomicInteger(Float.floatToIntBits(valueScaling.incrementAndGet()*1000 + rng.nextFloat()));
    }

    private TreeNode(TreeNode parent, NodeType type, float value) {
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
        }

        return root;
    }

    private boolean equals(TreeNode n) {
        return this.getFloat()==n.getFloat();
    }

    public static void initializeLeftChild(TreeNode n) {
        if (n.leftChild == null)
            n.leftChild = new TreeNode(n, NodeType.LEFT_CHILD);
    }

    public static void initializeRightChild(TreeNode n) {
        if (n.rightChild == null)
            n.rightChild = new TreeNode(n, NodeType.RIGHT_CHILD);
    }

    private static void addOrderedNodes(TreeNode n) {
        if (n == null) return;
        orderedNodes.add(n);
        addOrderedNodes(n.getLeftChild());
        addOrderedNodes(n.getRightChild());
    }

    private static void updateOrderedNodes() {
        orderedNodes.clear();
        addOrderedNodes(root);
    }

    public static void createChild(TreeNode parent, NodeType type) {
        TreeNode newNode = new TreeNode(parent, type, 0);
        parent.setLeftChild(newNode);

        // update ordered values in tree
        updateOrderedNodes();

        // find index for new node
        int i;
        for (i = 0; i < orderedNodes.size(); i++) {
            if (orderedNodes.get(i).getFloat() == 0)
                break;
        }
        // set acceptable data value for new node
        // corner case: new node must be largest value, increase max bound by 1000
        if (i == orderedNodes.size()-1) {
            int minIndex = Math.max(0, i-1);
            float min = orderedNodes.get(minIndex).getFloat();
            float max = min + 1000;
            float nodeData = min + rng.nextFloat() * (max - min);
            newNode.setData(nodeData);
        }
        else {
            int minIndex = Math.max(0, i-1);
            int maxIndex = Math.min(orderedNodes.size()-1, i+1);
            float min = orderedNodes.get(minIndex).getFloat();
            float max = orderedNodes.get(maxIndex).getFloat();
            float nodeData = min + rng.nextFloat() * (max - min);
            newNode.setData(nodeData);
        }
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

    public float getFloat() {
        return Float.intBitsToFloat(data.get());
    }

    private void setData(float data) {
        this.data.set(Float.floatToIntBits(data));
    }

    public AtomicInteger getDepth() {
        return depth;
    }

    // debug
    // prints the entire tree using BFS
    // might not be useful to check for in order traversal
//    public static void printAll(TreeNode n) {
//        if (n.getType() != NodeType.ROOT) return;
//        Queue<TreeNode> currentLevel = new LinkedList<>();
//        AtomicInteger nodesInCurrentLevel = new AtomicInteger(1);
//        AtomicInteger nodesInNextLevel = new AtomicInteger(0);
//
//        currentLevel.add(n);
//        while (!currentLevel.isEmpty()) {
//            TreeNode currentNode = currentLevel.poll();
//            nodesInCurrentLevel.decrementAndGet();
//            if (currentNode != null) {
//                System.out.println("depth: " + currentNode.getDepth().get() +
//                        "\tdata: " + currentNode.getFloat() +
//                        "\ttype: " + currentNode.getType() +
//                        ((currentNode.getParent() == null) ? "" : ("\tparent: " + currentNode.getParent().getFloat())));
//                currentLevel.add(currentNode.getLeftChild());
//                currentLevel.add(currentNode.getRightChild());
//                nodesInNextLevel.addAndGet(2);
//            }
//            if (nodesInCurrentLevel.get() == 0) {
//                System.out.println();
//                nodesInCurrentLevel.set(nodesInNextLevel.get());
//                nodesInNextLevel.set(0);
//            }
//        }
//    }

    //  debug
    public static void printValues(TreeNode currentNode) {
        updateOrderedNodes();
        int i;
        for (i = 0; i < orderedNodes.size(); i++) {
            if (currentNode!= null) {
                if (orderedNodes.get(i).equals(currentNode))
                    System.out.print("**" +new DecimalFormat("#.00").format(orderedNodes.get(i).getFloat()) + "** ");
            }

            else
                System.out.print(new DecimalFormat("#.00").format(orderedNodes.get(i).getFloat()) + " ");
        }
        System.out.println();
    }
}
