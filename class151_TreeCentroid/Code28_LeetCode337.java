package class120;

// 337. 打家劫舍 III
// 小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为 root 。
// 除了 root 之外，每栋房子有且只有一个"父"房子与之相连。
// 一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。
// 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
// 给定二叉树的根节点 root ，返回在不触动警报的情况下，小偷能够盗取的最高金额。
// 测试链接 : https://leetcode.cn/problems/house-robber-iii/
// 提交以下的code，提交时请把类名改成"Solution"，可以直接通过
// 时间复杂度：O(n)，空间复杂度：O(n)

import java.util.HashMap;
import java.util.Map;

public class Code28_LeetCode337 {

    // 树节点定义
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

    // 方法一：记忆化递归（树形DP）
    // 使用HashMap存储已经计算过的节点结果，避免重复计算
    public static int rob(TreeNode root) {
        Map<TreeNode, Integer> memo = new HashMap<>();
        return robHelper(root, memo);
    }

    private static int robHelper(TreeNode node, Map<TreeNode, Integer> memo) {
        if (node == null) {
            return 0;
        }
        
        // 如果已经计算过该节点的结果，直接返回
        if (memo.containsKey(node)) {
            return memo.get(node);
        }
        
        // 情况1：抢劫当前节点
        int robCurrent = node.val;
        if (node.left != null) {
            robCurrent += robHelper(node.left.left, memo) + robHelper(node.left.right, memo);
        }
        if (node.right != null) {
            robCurrent += robHelper(node.right.left, memo) + robHelper(node.right.right, memo);
        }
        
        // 情况2：不抢劫当前节点
        int skipCurrent = robHelper(node.left, memo) + robHelper(node.right, memo);
        
        // 取两种情况的最大值
        int result = Math.max(robCurrent, skipCurrent);
        memo.put(node, result);
        
        return result;
    }

    // 方法二：优化的树形DP（推荐）
    // 使用数组存储每个节点的两种状态：抢劫该节点和不抢劫该节点的最大金额
    public static int rob2(TreeNode root) {
        int[] result = robHelper2(root);
        return Math.max(result[0], result[1]);
    }

    // 返回一个长度为2的数组
    // result[0]表示不抢劫当前节点的最大金额
    // result[1]表示抢劫当前节点的最大金额
    private static int[] robHelper2(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int[] left = robHelper2(node.left);
        int[] right = robHelper2(node.right);
        
        // 不抢劫当前节点：左右子节点可以抢劫或不抢劫，取最大值
        int skipCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        // 抢劫当前节点：不能抢劫直接相连的子节点
        int robCurrent = node.val + left[0] + right[0];
        
        return new int[]{skipCurrent, robCurrent};
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: [3,2,3,null,3,null,1]
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.right = new TreeNode(3);
        root1.right.right = new TreeNode(1);
        
        System.out.println("测试用例1结果: " + rob2(root1)); // 期望输出: 7
        
        // 测试用例2: [3,4,5,1,3,null,1]
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(4);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(1);
        root2.left.right = new TreeNode(3);
        root2.right.right = new TreeNode(1);
        
        System.out.println("测试用例2结果: " + rob2(root2)); // 期望输出: 9
        
        // 测试用例3: 空树
        System.out.println("测试用例3结果: " + rob2(null)); // 期望输出: 0
    }
}

/*
算法思路与树的重心联系：
虽然本题不是直接求树的重心，但体现了树形DP的思想，这与树的重心算法有相似之处：
1. 都需要遍历整棵树
2. 都需要处理节点的状态转移
3. 都利用了树的结构特性

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 方法一使用了HashMap存储中间结果，空间复杂度为O(n)
- 方法二只使用了常数级别的额外空间（递归栈除外）

工程化考量：
1. 异常处理：处理空树情况
2. 性能优化：方法二比方法一更优，避免了HashMap的开销
3. 可读性：使用清晰的变量命名和注释
4. 可测试性：提供了多个测试用例

与机器学习联系：
树形DP的思想可以应用于决策树优化、强化学习中的状态价值计算等场景。
*/
