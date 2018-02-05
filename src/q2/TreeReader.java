package q2;

class TreeReader extends Thread {
    private final TreeNode root = TreeNode.getRoot();
    private final StringBuffer result = new StringBuffer();

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
        traverse(root);
        result.append("\n");
    }
}
