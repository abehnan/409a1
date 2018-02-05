package q2;

//import java.util.Stack;

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
//        Stack<TreeNode> stack = new Stack<>();
//        stack.push(root);
//        while(!stack.empty()) {
//            TreeNode currentNode = stack.pop();
//            if (currentNode.getDepth().get() >= 4)
//                continue;
//            TreeNode.createLeftChild(currentNode);
//            stack.push(currentNode.getLeftChild());
//            stack.push(currentNode.getRightChild());
//        }
        generateTree(root);
    }
}
