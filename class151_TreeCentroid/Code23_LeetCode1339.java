// LeetCode 1339. 分裂二叉树的最大乘积
// 题目描述：给定一个二叉树，通过删除一条边将树分成两个子树，使得这两个子树的节点值之和的乘积最大。
// 算法思想：1. 先计算整棵树的节点值之和；2. 遍历树，对于每个子树计算其节点值之和，然后计算乘积；3. 找到最大乘积
// 测试链接：https://leetcode.com/problems/maximum-product-of-splitted-binary-tree/
// 时间复杂度：O(n)
// 空间复杂度：O(h)，h为树高

import java.util.*;

public class Code23_LeetCode1339 {
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
    
    private static final int MOD = 1000000007;
    private long totalSum; // 整棵树的节点值之和
    private long maxProduct; // 最大乘积
    
    /**
     * 计算分裂二叉树的最大乘积
     * @param root 二叉树的根节点
     * @return 最大乘积对10^9+7取模的结果
     */
    public int maxProduct(TreeNode root) {
        totalSum = 0;
        maxProduct = 0;
        
        // 计算整棵树的节点值之和
        totalSum = calculateSum(root);
        
        // 再次遍历树，计算每个子树的节点值之和，并更新最大乘积
        calculateSubtreeSum(root);
        
        return (int) (maxProduct % MOD);
    }
    
    /**
     * 计算树的节点值之和
     * @param node 当前节点
     * @return 以node为根的子树的节点值之和
     */
    private long calculateSum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.val + calculateSum(node.left) + calculateSum(node.right);
    }
    
    /**
     * 计算子树的节点值之和，并更新最大乘积
     * @param node 当前节点
     * @return 以node为根的子树的节点值之和
     */
    private long calculateSubtreeSum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        long subtreeSum = node.val + calculateSubtreeSum(node.left) + calculateSubtreeSum(node.right);
        
        // 计算当前子树与剩余部分的乘积
        long product = subtreeSum * (totalSum - subtreeSum);
        
        // 更新最大乘积
        if (product > maxProduct) {
            maxProduct = product;
        }
        
        return subtreeSum;
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
     * 测试方法
     */
    public static void main(String[] args) {
        Code23_LeetCode1339 solution = new Code23_LeetCode1339();
        
        // 测试用例1
        Integer[] nums1 = {1, 2, 3, 4, 5, 6};
        TreeNode root1 = buildTree(nums1, 0);
        int result1 = solution.maxProduct(root1);
        System.out.println("测试用例1结果: " + result1);
        // 期望输出: 110 (5*6 + 4*5+6? 不，实际是 (11) * (2+3+4+5+6) = 11*20=220? 等等，让我重新计算
        // 树的结构：
        //       1
        //      / \
        //     2   3
        //    / \ / 
        //   4  5 6
        // 总节点和: 1+2+3+4+5+6 = 21
        // 可能的分割：
        // - 分割1-2：左子树和为2+4+5=11，右子树和为21-11=10，乘积11*10=110
        // - 分割1-3：左子树和为3+6=9，右子树和为21-9=12，乘积9*12=108
        // - 分割2-4：左子树和为4，右子树和为21-4=17，乘积4*17=68
        // - 分割2-5：左子树和为5，右子树和为21-5=16，乘积5*16=80
        // - 分割3-6：左子树和为6，右子树和为21-6=15，乘积6*15=90
        // 最大乘积是110
        
        // 测试用例2
        Integer[] nums2 = {1, null, 2, 3, 4, null, null, 5, 6};
        TreeNode root2 = buildTree(nums2, 0);
        int result2 = solution.maxProduct(root2);
        System.out.println("测试用例2结果: " + result2);
        // 期望输出: 90 (5*6+3+4=22, 1+2+3+4+5+6=21? 等等，需要重新计算)
        // 树的结构：
        //       1
        //        \
        //         2
        //        / \
        //       3   4
        //          / \
        //         5   6
        // 总节点和: 1+2+3+4+5+6 = 21
        // 可能的分割：
        // - 分割1-2：左子树和为2+3+4+5+6=20，右子树和为1，乘积20*1=20
        // - 分割2-3：左子树和为3，右子树和为21-3=18，乘积3*18=54
        // - 分割2-4：左子树和为4+5+6=15，右子树和为21-15=6，乘积15*6=90
        // - 分割4-5：左子树和为5，右子树和为21-5=16，乘积5*16=80
        // - 分割4-6：左子树和为6，右子树和为21-6=15，乘积6*15=90
        // 最大乘积是90
    }
}

// 注意：
// 1. 题目中要求结果对10^9+7取模
// 2. 需要注意整数溢出问题，使用long类型来存储中间结果
// 3. 这道题虽然不是直接找树的重心，但可以应用类似的思想：寻找一个分割点，使得两部分的大小尽可能接近
// 4. 树的重心的定义是：删除该节点后，最大的子树的大小不超过整棵树大小的一半
// 5. 这道题的最优分割点也是使得两部分尽可能接近，所以与树的重心有密切关系