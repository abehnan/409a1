package q2;

public class q2 {
    public static void main(String[] args) {
        TreeBuilder tb = new TreeBuilder();
        tb.start();
        try {
            tb.join();
//            TreeNode.printAll(TreeNode.getRoot());
//            TreeReader treeReader1 = new TreeReader();
//            TreeReader treeReader2 = new TreeReader();
            EvilTreeThread evil = new EvilTreeThread();

//            treeReader1.start();
//            treeReader2.start();
            evil.start();
//            treeReader1.join();
//            treeReader2.join();
            evil.join();

//            System.out.println("treeReader1:\n"+treeReader1.getResult());
//            System.out.println("treeReader2:\n"+treeReader2.getResult());
//            TreeNode.printValues();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
