package q2;

public class q2 {
    public static void main(String[] args) {
        TreeBuilder tb = new TreeBuilder();
        tb.start();
        try {
            tb.join();
//            TreeNode.printAll(TreeNode.getRoot());
            TreeReader tr = new TreeReader();
            tr.start();
            tr.join();
            StringBuffer result = tr.getResult();

            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
