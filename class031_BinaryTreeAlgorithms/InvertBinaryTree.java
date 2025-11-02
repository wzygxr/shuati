package class037;

// LeetCode 226. Invert Binary Tree
// 题目链接: https://leetcode.cn/problems/invert-binary-tree/
// 题目描述: 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
// 翻转二叉树就是将每个节点的左右子树进行交换。
//
// 解题思路:
// 1. 使用递归深度优先搜索(DFS)
// 2. 对于每个节点，先递归翻转其左右子树
// 3. 然后交换当前节点的左右子树
// 4. 返回翻转后的根节点
//
// 时间复杂度: O(n) - n为树中节点的数量，需要访问每个节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是翻转二叉树的标准方法

public class InvertBinaryTree {

    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // 提交如下的方法
    public TreeNode invertTree(TreeNode root) {
        // 基本情况：空节点直接返回
        if (root == null) {
            return null;
        }
        
        // 递归翻转左右子树
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        
        // 交换左右子树
        root.left = right;
        root.right = left;
        
        return root;
    }

    // 测试用例
    public static void main(String[] args) {
        InvertBinaryTree solution = new InvertBinaryTree();

        // 测试用例1:
        // 原始树:
        //       4
        //      / \
        //     2   7
        //    / \ / \
        //   1  3 6  9
        //
        // 翻转后:
        //       4
        //      / \
        //     7   2
        //    / \ / \
        //   9  6 3  1
        TreeNode root1 = new TreeNode(4);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(7);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(3);
        root1.right.left = new TreeNode(6);
        root1.right.right = new TreeNode(9);
        
        System.out.println("翻转前:");
        printTree(root1);
        
        TreeNode invertedRoot1 = solution.invertTree(root1);
        System.out.println("翻转后:");
        printTree(invertedRoot1);
    }
    
    // 辅助方法：打印二叉树（前序遍历）
    private static void printTree(TreeNode root) {
        if (root == null) {
            System.out.print("null ");
            return;
        }
        System.out.print(root.val + " ");
        printTree(root.left);
        printTree(root.right);
    }
}