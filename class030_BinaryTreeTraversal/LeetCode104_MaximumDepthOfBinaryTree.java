package class036;

import java.util.*;

// LeetCode 104. 二叉树的最大深度
// 题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
// 题目大意: 给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。

public class LeetCode104_MaximumDepthOfBinaryTree {
    
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
     * 方法1: 递归实现计算二叉树的最大深度
     * 思路:
     * 1. 如果当前节点为空，深度为0
     * 2. 递归计算左子树的最大深度
     * 3. 递归计算右子树的最大深度
     * 4. 返回左右子树最大深度的最大值加1
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(h) - h是树的高度，递归调用栈的深度
     */
    public static int maxDepth1(TreeNode root) {
        // 递归终止条件
        if (root == null) {
            return 0;
        }
        
        // 递归计算左子树和右子树的最大深度
        int leftDepth = maxDepth1(root.left);
        int rightDepth = maxDepth1(root.right);
        
        // 返回左右子树最大深度的最大值加1
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 方法2: 迭代实现计算二叉树的最大深度
     * 思路:
     * 1. 使用队列进行层序遍历
     * 2. 记录层数，每处理完一层层数加1
     * 3. 返回最终的层数
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static int maxDepth2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 使用队列进行层序遍历
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int depth = 0;
        
        while (!queue.isEmpty()) {
            // 记录当前层的节点数量
            int size = queue.size();
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; i++) {
                TreeNode current = queue.poll();
                
                // 将左右子节点加入队列（如果存在）
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 每处理完一层，深度加1
            depth++;
        }
        
        return depth;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试二叉树:
        //     3
        //    / \
        //   9  20
        //     /  \
        //    15   7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        
        System.out.println("递归方法计算的最大深度: " + maxDepth1(root));
        System.out.println("迭代方法计算的最大深度: " + maxDepth2(root));
        
        // 测试空树
        TreeNode emptyRoot = null;
        System.out.println("空树的最大深度: " + maxDepth1(emptyRoot));
        
        // 测试单节点树
        TreeNode singleRoot = new TreeNode(1);
        System.out.println("单节点树的最大深度: " + maxDepth1(singleRoot));
    }
}