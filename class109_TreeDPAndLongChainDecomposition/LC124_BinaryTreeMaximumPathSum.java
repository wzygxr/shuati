/**
 * LeetCode 124. 二叉树中的最大路径和 (Binary Tree Maximum Path Sum) - Java实现
 * 题目链接：https://leetcode.com/problems/binary-tree-maximum-path-sum/
 * 
 * 题目描述：
 * 给定一个非空二叉树，返回其最大路径和。路径被定义为一条从树中任意节点出发，
 * 达到任意节点的序列。该路径至少包含一个节点，且不一定经过根节点。
 * 
 * 算法思路（树形DP经典问题）：
 * 1. 树形DP思想：对于每个节点，计算以该节点为起点的最大路径和
 * 2. 路径类型：路径可能出现在左子树、右子树，或穿过当前节点
 * 3. 全局维护：在递归过程中维护全局最大路径和
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n) - 每个节点访问一次，无法优化
 * - 空间复杂度：O(h) - 递归栈深度，h为树的高度，最坏情况O(n)
 * 
 * 最优解验证：这是最优解，无法进一步优化时间复杂度
 * 
 * 工程化考量：
 * 1. 异常处理：处理负数、全负数等边界情况
 * 2. 数值边界：使用Integer.MIN_VALUE处理可能的整数溢出
 * 3. 可测试性：提供完整的测试用例覆盖各种场景
 * 4. 可读性：清晰的变量命名和注释
 * 
 * 算法技巧总结：
 * - 见到树形结构求最大路径和问题，优先考虑树形DP
 * - 路径可能不经过根节点，需要在每个节点处计算可能的最大路径
 * - 对于负数节点，需要判断是否舍弃（max(0, gain)）
 * 
 * 边界场景处理：
 * - 单节点树：返回节点值
 * - 全负数树：返回最大的单个节点值
 * - 包含负数的树：需要正确判断是否包含负数节点
 * - 大数值树：注意整数溢出问题
 * 
 * 反直觉关键点：
 * - 路径不一定经过根节点，可能完全在子树中
 * - 负数节点可能被舍弃，但单个负数节点可能是最大路径
 * - 递归返回的是单边最大路径，而全局维护的是可能穿过当前节点的路径
 */

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

public class LC124_BinaryTreeMaximumPathSum {
    private int maxSum = Integer.MIN_VALUE;
    
    /**
     * 主方法：计算二叉树的最大路径和
     * @param root 二叉树根节点
     * @return 最大路径和
     */
    public int maxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        maxSum = Integer.MIN_VALUE;
        maxGain(root);
        return maxSum;
    }
    
    /**
     * 递归计算以当前节点为起点的最大增益
     * @param node 当前节点
     * @return 以当前节点为起点的最大路径和（只能选择一条分支）
     */
    private int maxGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的最大增益（如果为负则舍弃）
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        
        // 计算穿过当前节点的路径和
        int pathThroughNode = node.val + leftGain + rightGain;
        
        // 更新全局最大路径和
        maxSum = Math.max(maxSum, pathThroughNode);
        
        // 返回以当前节点为起点的最大路径和（只能选择一条分支）
        return node.val + Math.max(leftGain, rightGain);
    }
    
    /**
     * 测试用例：验证算法正确性
     * 包含多种边界情况和典型情况
     */
    public static void main(String[] args) {
        LC124_BinaryTreeMaximumPathSum solution = new LC124_BinaryTreeMaximumPathSum();
        
        // 测试用例1：单节点树
        TreeNode root1 = new TreeNode(1);
        System.out.println("测试用例1 - 单节点树: " + solution.maxPathSum(root1) + " (期望: 1)");
        
        // 测试用例2：示例树 [1,2,3]
        //       1
        //      / \
        //     2   3
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(3);
        System.out.println("测试用例2 - 示例树: " + solution.maxPathSum(root2) + " (期望: 6)");
        
        // 测试用例3：包含负数的树 [-10,9,20,null,null,15,7]
        //       -10
        //       / \
        //      9  20
        //        /  \
        //       15   7
        TreeNode root3 = new TreeNode(-10);
        root3.left = new TreeNode(9);
        root3.right = new TreeNode(20);
        root3.right.left = new TreeNode(15);
        root3.right.right = new TreeNode(7);
        System.out.println("测试用例3 - 包含负数树: " + solution.maxPathSum(root3) + " (期望: 42)");
        
        // 测试用例4：全负数树
        TreeNode root4 = new TreeNode(-3);
        root4.left = new TreeNode(-2);
        root4.right = new TreeNode(-1);
        System.out.println("测试用例4 - 全负数树: " + solution.maxPathSum(root4) + " (期望: -1)");
        
        // 测试用例5：复杂树
        TreeNode root5 = new TreeNode(5);
        root5.left = new TreeNode(4);
        root5.right = new TreeNode(8);
        root5.left.left = new TreeNode(11);
        root5.left.left.left = new TreeNode(7);
        root5.left.left.right = new TreeNode(2);
        root5.right.left = new TreeNode(13);
        root5.right.right = new TreeNode(4);
        root5.right.right.right = new TreeNode(1);
        System.out.println("测试用例5 - 复杂树: " + solution.maxPathSum(root5) + " (期望: 48)");
        
        System.out.println("所有测试用例执行完成！");
    }
}