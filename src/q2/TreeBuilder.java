package q2;

public class TreeBuilder extends Thread {
    private final TreeNode root = TreeNode.getRoot();

    private void generateTree(TreeNode n) {
        if (n == null) return;
        if (n.getDepth().get() >= 3) return;
        TreeNode.initializeLeftChild(n);
        generateTree(n.getLeftChild());
        TreeNode.initializeRightChild(n);
        generateTree(n.getRightChild());
    }

    public void run() {
        generateTree(root);
    }
}
