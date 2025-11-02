package class036;

import java.util.*;

// LeetCode 226. 翻转二叉树
// 题目链接: https://leetcode.cn/problems/invert-binary-tree/
// 题目大意: 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。

public class LeetCode226_InvertBinaryTree {
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * 方法1: 递归实现翻转二叉树
     * 思路:
     * 1. 如果当前节点为空，直接返回
     * 2. 递归翻转左子树
     * 3. 递归翻转右子树
     * 4. 交换左右子树
     * 5. 返回根节点
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(h) - h是树的高度，递归调用栈的深度
     */
    public static TreeNode invertTree1(TreeNode root) {
        // 递归终止条件
        if (root == null) {
            return null;
        }
        
        // 递归翻转左子树和右子树
        TreeNode left = invertTree1(root.left);
        TreeNode right = invertTree1(root.right);
        
        // 交换左右子树
        root.left = right;
        root.right = left;
        
        return root;
    }
    
    /**
     * 方法2: 迭代实现翻转二叉树
     * 思路:
     * 1. 使用队列进行层序遍历
     * 2. 对于每个节点，交换其左右子树
     * 3. 将左右子节点加入队列继续处理
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static TreeNode invertTree2(TreeNode root) {
        if (root == null) {
            return null;
        }
        
        // 使用队列进行层序遍历
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            // 取出队首节点
            TreeNode current = queue.poll();
            
            // 交换当前节点的左右子树
            TreeNode temp = current.left;
            current.left = current.right;
            current.right = temp;
            
            // 将左右子节点加入队列（如果存在）
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        
        return root;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试二叉树:
        //     4
        //    / \
        //   2   7
        //  / \ / \
        // 1  3 6  9
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(7);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(9);
        
        System.out.println("翻转前:");
        printTree(root);
        
        // 翻转二叉树
        TreeNode invertedRoot = invertTree1(root);
        
        System.out.println("翻转后:");
        printTree(invertedRoot);
    }
    
    // 辅助方法：打印二叉树（前序遍历）
    public static void printTree(TreeNode root) {
        if (root != null) {
            System.out.print(root.val + " ");
            printTree(root.left);
            printTree(root.right);
        }
    }
}