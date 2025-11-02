package class036;

import java.util.*;

// LeetCode 105. 从前序与中序遍历序列构造二叉树
// 题目链接: https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
// 题目大意: 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的先序遍历，inorder 是同一棵树的中序遍历，
// 请构造二叉树并返回其根节点。

public class LeetCode105_ConstructBinaryTreeFromPreorderAndInorderTraversal {
    
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
     * 方法1: 递归构建二叉树
     * 思路:
     * 1. 前序遍历的第一个元素是根节点
     * 2. 在中序遍历中找到根节点的位置，将中序遍历分为左子树和右子树
     * 3. 递归构建左子树和右子树
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储哈希表和递归调用栈
     */
    public static TreeNode buildTree1(int[] preorder, int[] inorder) {
        // 创建中序遍历值到索引的映射，用于快速查找根节点位置
        Map<Integer, Integer> inorderMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderMap.put(inorder[i], i);
        }
        
        return buildTreeHelper(preorder, 0, preorder.length - 1, 
                              inorder, 0, inorder.length - 1, inorderMap);
    }
    
    /**
     * 递归构建二叉树的辅助函数
     * @param preorder 前序遍历数组
     * @param preStart 前序遍历的起始索引
     * @param preEnd 前序遍历的结束索引
     * @param inorder 中序遍历数组
     * @param inStart 中序遍历的起始索引
     * @param inEnd 中序遍历的结束索引
     * @param inorderMap 中序遍历值到索引的映射
     * @return 构建的子树根节点
     */
    private static TreeNode buildTreeHelper(int[] preorder, int preStart, int preEnd,
                                           int[] inorder, int inStart, int inEnd,
                                           Map<Integer, Integer> inorderMap) {
        // 递归终止条件
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        
        // 前序遍历的第一个元素是根节点
        int rootVal = preorder[preStart];
        TreeNode root = new TreeNode(rootVal);
        
        // 在中序遍历中找到根节点的位置
        int rootIndex = inorderMap.get(rootVal);
        
        // 计算左子树的节点数量
        int leftSubtreeSize = rootIndex - inStart;
        
        // 递归构建左子树
        root.left = buildTreeHelper(preorder, preStart + 1, preStart + leftSubtreeSize,
                                   inorder, inStart, rootIndex - 1, inorderMap);
        
        // 递归构建右子树
        root.right = buildTreeHelper(preorder, preStart + leftSubtreeSize + 1, preEnd,
                                    inorder, rootIndex + 1, inEnd, inorderMap);
        
        return root;
    }
    
    /**
     * 方法2: 使用迭代器优化的递归方法
     * 思路: 使用一个全局索引跟踪前序遍历的当前位置
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储哈希表和递归调用栈
     */
    private static int preorderIndex;
    
    public static TreeNode buildTree2(int[] preorder, int[] inorder) {
        // 重置索引
        preorderIndex = 0;
        
        // 创建中序遍历值到索引的映射，用于快速查找根节点位置
        Map<Integer, Integer> inorderMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderMap.put(inorder[i], i);
        }
        
        return buildTreeHelper2(preorder, inorder, 0, inorder.length - 1, inorderMap);
    }
    
    /**
     * 使用迭代器优化的递归构建二叉树的辅助函数
     * @param preorder 前序遍历数组
     * @param inorder 中序遍历数组
     * @param inStart 中序遍历的起始索引
     * @param inEnd 中序遍历的结束索引
     * @param inorderMap 中序遍历值到索引的映射
     * @return 构建的子树根节点
     */
    private static TreeNode buildTreeHelper2(int[] preorder, int[] inorder,
                                            int inStart, int inEnd,
                                            Map<Integer, Integer> inorderMap) {
        // 递归终止条件
        if (inStart > inEnd) {
            return null;
        }
        
        // 前序遍历的当前元素是根节点
        int rootVal = preorder[preorderIndex++];
        TreeNode root = new TreeNode(rootVal);
        
        // 在中序遍历中找到根节点的位置
        int rootIndex = inorderMap.get(rootVal);
        
        // 递归构建左子树
        root.left = buildTreeHelper2(preorder, inorder, inStart, rootIndex - 1, inorderMap);
        
        // 递归构建右子树
        root.right = buildTreeHelper2(preorder, inorder, rootIndex + 1, inEnd, inorderMap);
        
        return root;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
        int[] preorder1 = {3, 9, 20, 15, 7};
        int[] inorder1 = {9, 3, 15, 20, 7};
        TreeNode root1 = buildTree1(preorder1, inorder1);
        System.out.println("方法1构建的树根节点值: " + root1.val);
        
        TreeNode root2 = buildTree2(preorder1, inorder1);
        System.out.println("方法2构建的树根节点值: " + root2.val);
    }
}