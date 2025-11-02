// 二叉树中的最大路径和 - LeetCode 124
// 给定一个非空二叉树，找到路径和最大的路径
// 路径定义为从树中任意节点出发，达到任意节点的序列
// 该路径至少包含一个节点，且不一定经过根节点
// 测试链接 : https://leetcode.com/problems/binary-tree-maximum-path-sum/

/*
题目解析：
这是一道经典的树形DP问题，需要计算二叉树中的最大路径和。路径可以从任意节点开始，到任意节点结束。

算法思路：
1. 使用后序遍历（DFS）处理每个节点
2. 对于每个节点，计算以该节点为起点的最大路径和（只能向下延伸）
3. 同时计算经过该节点的最大路径和（可以包含左右子树）
4. 全局维护最大路径和

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，这是解决此类问题的最优方法

工程化考量：
1. 异常处理：处理空树、负数节点值
2. 边界条件：单节点树、所有节点为负数
3. 性能优化：避免重复计算，使用全局变量
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

public class Code19_MaximumPathSum {
    
    private int maxSum = Integer.MIN_VALUE;
    
    public int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        dfs(root);
        return maxSum;
    }
    
    /**
     * 计算以当前节点为起点的最大路径和（只能向下延伸）
     * 同时更新全局最大路径和（可以包含左右子树）
     */
    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的最大路径和
        int leftMax = Math.max(0, dfs(node.left));  // 如果为负，则选择0（不选择该子树）
        int rightMax = Math.max(0, dfs(node.right));
        
        // 计算经过当前节点的最大路径和（可以包含左右子树）
        int currentMax = node.val + leftMax + rightMax;
        maxSum = Math.max(maxSum, currentMax);
        
        // 返回以当前节点为起点的最大路径和（只能选择一条路径）
        return node.val + Math.max(leftMax, rightMax);
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code19_MaximumPathSum solution = new Code19_MaximumPathSum();
        
        // 测试用例1: [1,2,3]
        TreeNode root1 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        System.out.println("测试1: " + solution.maxPathSum(root1)); // 期望: 6
        
        // 测试用例2: [-10,9,20,null,null,15,7]
        TreeNode root2 = new TreeNode(-10,
            new TreeNode(9),
            new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        System.out.println("测试2: " + solution.maxPathSum(root2)); // 期望: 42
        
        // 测试用例3: 单节点
        TreeNode root3 = new TreeNode(-3);
        System.out.println("测试3: " + solution.maxPathSum(root3)); // 期望: -3
        
        // 测试用例4: 所有节点为负数
        TreeNode root4 = new TreeNode(-2, new TreeNode(-1), null);
        System.out.println("测试4: " + solution.maxPathSum(root4)); // 期望: -1
    }
}