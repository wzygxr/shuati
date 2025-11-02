// LeetCode 1123. 最深叶节点的最近公共祖先
// 题目描述：给定一个二叉树，返回其最深叶节点的最近公共祖先。
// 算法思想：1. 首先计算树的最大深度；2. 然后找到深度等于最大深度的所有叶节点；3. 最后找到这些叶节点的最近公共祖先
// 测试链接：https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/
// 时间复杂度：O(n)
// 空间复杂度：O(h)，h为树高

import java.util.*;

public class Code24_LeetCode1123 {
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
    
    private int maxDepth;
    private TreeNode lca;
    
    /**
     * 找到最深叶节点的最近公共祖先
     * @param root 二叉树的根节点
     * @return 最深叶节点的最近公共祖先
     */
    public TreeNode lcaDeepestLeaves(TreeNode root) {
        maxDepth = 0;
        lca = null;
        
        // 首先计算树的最大深度
        computeDepth(root, 0);
        
        // 然后找到最深叶节点的最近公共祖先
        findLCA(root, 0);
        
        return lca;
    }
    
    /**
     * 计算树的最大深度
     * @param node 当前节点
     * @param depth 当前深度
     */
    private void computeDepth(TreeNode node, int depth) {
        if (node == null) {
            return;
        }
        
        maxDepth = Math.max(maxDepth, depth);
        computeDepth(node.left, depth + 1);
        computeDepth(node.right, depth + 1);
    }
    
    /**
     * 找到最深叶节点的最近公共祖先
     * @param node 当前节点
     * @param depth 当前深度
     * @return 以node为根的子树中最深节点的深度
     */
    private int findLCA(TreeNode node, int depth) {
        if (node == null) {
            return depth - 1; // 返回上一层的深度
        }
        
        // 递归计算左右子树中最深节点的深度
        int leftDepth = findLCA(node.left, depth + 1);
        int rightDepth = findLCA(node.right, depth + 1);
        
        // 如果左右子树都包含最深节点，那么当前节点就是这些最深节点的最近公共祖先
        if (leftDepth == maxDepth && rightDepth == maxDepth) {
            lca = node;
        }
        // 如果只有左子树包含最深节点，那么最近公共祖先在左子树中
        else if (leftDepth == maxDepth) {
            // 左子树中的最深节点的最近公共祖先会在递归过程中被设置
        }
        // 如果只有右子树包含最深节点，那么最近公共祖先在右子树中
        else if (rightDepth == maxDepth) {
            // 右子树中的最深节点的最近公共祖先会在递归过程中被设置
        }
        
        // 返回以当前节点为根的子树中最深节点的深度
        return Math.max(leftDepth, rightDepth);
    }
    
    /**
     * 优化版本：一次性递归完成最大深度计算和最近公共祖先查找
     * @param root 二叉树的根节点
     * @return 最深叶节点的最近公共祖先
     */
    public TreeNode lcaDeepestLeavesOptimized(TreeNode root) {
        return dfs(root).node;
    }
    
    private Result dfs(TreeNode node) {
        if (node == null) {
            return new Result(null, 0);
        }
        
        Result left = dfs(node.left);
        Result right = dfs(node.right);
        
        // 如果左右子树深度相同，当前节点就是最近公共祖先
        if (left.depth == right.depth) {
            return new Result(node, left.depth + 1);
        }
        // 否则，选择深度较大的子树中的结果
        else if (left.depth > right.depth) {
            return new Result(left.node, left.depth + 1);
        } else {
            return new Result(right.node, right.depth + 1);
        }
    }
    
    // 辅助类：用于存储节点和深度信息
    private static class Result {
        TreeNode node;
        int depth;
        Result(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }
    
    /**
     * 辅助方法：根据数组构建二叉树
     * @param nums 数组，null表示空节点
     * @param index 当前索引
     * @return 构建的二叉树节点
     */
    private static TreeNode buildTree(Integer[] nums, int index) {
        if (index >= nums.length || nums[index] == null) {
            return null;
        }
        
        TreeNode node = new TreeNode(nums[index]);
        node.left = buildTree(nums, 2 * index + 1);
        node.right = buildTree(nums, 2 * index + 2);
        
        return node;
    }
    
    /**
     * 辅助方法：打印树的节点值（用于调试）
     * @param node 二叉树节点
     */
    private static void printTree(TreeNode node) {
        if (node == null) {
            System.out.print("null ");
            return;
        }
        System.out.print(node.val + " ");
        printTree(node.left);
        printTree(node.right);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code24_LeetCode1123 solution = new Code24_LeetCode1123();
        
        // 测试用例1
        Integer[] nums1 = {3, 5, 1, 6, 2, 0, 8, null, null, 7, 4};
        TreeNode root1 = buildTree(nums1, 0);
        TreeNode result1 = solution.lcaDeepestLeaves(root1);
        TreeNode result1Optimized = solution.lcaDeepestLeavesOptimized(root1);
        System.out.print("测试用例1结果: ");
        printTree(result1);
        System.out.println();
        System.out.print("优化版本结果: ");
        printTree(result1Optimized);
        System.out.println();
        // 期望输出: 2 7 4
        
        // 测试用例2
        Integer[] nums2 = {1};
        TreeNode root2 = buildTree(nums2, 0);
        TreeNode result2 = solution.lcaDeepestLeaves(root2);
        System.out.print("测试用例2结果: ");
        printTree(result2);
        System.out.println();
        // 期望输出: 1
        
        // 测试用例3
        Integer[] nums3 = {0, 1, 3, null, 2};
        TreeNode root3 = buildTree(nums3, 0);
        TreeNode result3 = solution.lcaDeepestLeaves(root3);
        System.out.print("测试用例3结果: ");
        printTree(result3);
        System.out.println();
        // 期望输出: 2
    }
}

// 注意：
// 1. 这道题虽然不是直接找树的重心，但可以应用类似的思想：寻找一个节点，使得它到最深叶节点的距离尽可能小
// 2. 树的重心是使得最大子树的大小最小的节点，而本题是寻找最深叶节点的最近公共祖先
// 3. 两种算法都利用了树形结构的特性，通过深度优先搜索来计算子树的属性
// 4. 优化版本的算法更加高效，只需要一次深度优先搜索就能同时获取深度和最近公共祖先信息