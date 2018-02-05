package q2;

import java.util.Stack;

public class TreeBuilder extends Thread {
    private final TreeNode root = TreeNode.getRoot();

    public void run() {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while(!stack.empty()) {
            TreeNode currentNode = stack.pop();
            if (currentNode.getDepth().get() >= 3)
                continue;
            TreeNode.createChildren(currentNode);
            stack.push(currentNode.getLeftChild());
            stack.push(currentNode.getRightChild());
        }
    }
}
