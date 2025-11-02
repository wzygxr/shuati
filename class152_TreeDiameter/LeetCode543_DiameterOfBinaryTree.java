package class121;

// LeetCode 543. 二叉树的直径
// 题目：给定一棵二叉树，你需要计算它的直径长度。
// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
// 这条路径可能穿过也可能不穿过根结点。
// 两结点之间的路径长度是以它们之间边的数目表示。

import java.util.*;

// 二叉树节点定义
class TreeNode {
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

public class LeetCode543_DiameterOfBinaryTree {
    
    // 全局变量，用于记录最大直径
    private int maxDiameter = 0;
    
    /**
     * 计算二叉树的直径
     * @param root 二叉树根节点
     * @return 树的直径（边数）
     * 
     * 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点只访问一次
     * 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
     */
    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameter = 0;  // 重置全局变量
        depth(root);      // 计算每个节点的深度并更新最大直径
        return maxDiameter;
    }
    
    /**
     * 计算以当前节点为根的子树深度，并更新最大直径
     * @param node 当前节点
     * @return 当前节点为根的子树的最大深度
     */
    private int depth(TreeNode node) {
        // 基本情况：空节点深度为0
        if (node == null) {
            return 0;
        }
        
        // 递归计算左子树和右子树的最大深度
        int leftDepth = depth(node.left);
        int rightDepth = depth(node.right);
        
        // 经过当前节点的最长路径 = 左子树最大深度 + 右子树最大深度
        // 更新全局最大直径
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);
        
        // 返回以当前节点为根的子树的最大深度
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode543_DiameterOfBinaryTree solution = new LeetCode543_DiameterOfBinaryTree();
        
        // 测试用例1: [1,2,3,4,5]
        //     1
        //    / \
        //   2   3
        //  / \
        // 4   5
        // 预期输出: 3 (路径 [4,2,1,3] 或 [5,2,1,3])
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        
        System.out.println("测试用例1结果: " + solution.diameterOfBinaryTree(root1)); // 应该输出3
        
        // 测试用例2: [1,2]
        //   1
        //  /
        // 2
        // 预期输出: 1 (路径 [1,2])
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        
        System.out.println("测试用例2结果: " + solution.diameterOfBinaryTree(root2)); // 应该输出1
    }
}