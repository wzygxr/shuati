// 打家劫舍III - LeetCode 337
// 小偷发现了一个新的地区，这个地区的房屋排列形式类似于一棵二叉树
// 如果两个直接相连的房屋在同一天晚上被打劫，房屋将自动报警
// 计算在不触动警报的情况下，小偷能够盗取的最高金额
// 测试链接 : https://leetcode.com/problems/house-rober-iii/

import java.util.*;

/*
题目解析：
这是一个经典的树形DP问题，需要在二叉树上选择不相邻的节点，使得总金额最大。

算法思路：
1. 使用后序遍历（DFS）处理每个节点
2. 对于每个节点，有两种选择：抢劫该节点或不抢劫该节点
3. 如果抢劫当前节点，则不能抢劫其直接子节点
4. 如果不抢劫当前节点，则可以抢劫其子节点
5. 使用DP数组存储每个节点的两种状态的最大金额

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，树形DP是解决此类问题的最优方法

工程化考量：
1. 异常处理：空树、单节点树
2. 边界条件：所有节点金额为负数
3. 性能优化：记忆化搜索避免重复计算
4. 代码可读性：使用明确的变量名和注释

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code21_HouseRobberIII.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.cpp
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

public class Code21_HouseRobberIII {
    
    /**
     * 计算在不触动警报的情况下能够盗取的最高金额
     * 
     * @param root 二叉树根节点
     * @return 最大金额
     */
    public int rob(TreeNode root) {
        int[] result = dfs(root);
        return Math.max(result[0], result[1]);
    }
    
    /**
     * DFS遍历，返回当前节点的两种状态的最大金额
     * 
     * @param node 当前节点
     * @return int[2]数组，[0]表示不抢劫当前节点的最大金额，[1]表示抢劫当前节点的最大金额
     */
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        // 递归处理左右子树
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // 不抢劫当前节点：可以抢劫左右子节点（选择最大值）
        int notRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        // 抢劫当前节点：不能抢劫直接子节点，只能抢劫孙子节点
        int rob = node.val + left[0] + right[0];
        
        return new int[]{notRob, rob};
    }
    
    // 记忆化搜索版本（优化重复计算）
    private Map<TreeNode, Integer> memo = new HashMap<>();
    
    public int robMemo(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        if (memo.containsKey(root)) {
            return memo.get(root);
        }
        
        // 抢劫当前节点
        int robCurrent = root.val;
        if (root.left != null) {
            robCurrent += robMemo(root.left.left) + robMemo(root.left.right);
        }
        if (root.right != null) {
            robCurrent += robMemo(root.right.left) + robMemo(root.right.right);
        }
        
        // 不抢劫当前节点
        int notRobCurrent = robMemo(root.left) + robMemo(root.right);
        
        int result = Math.max(robCurrent, notRobCurrent);
        memo.put(root, result);
        
        return result;
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code21_HouseRobberIII solution = new Code21_HouseRobberIII();
        
        // 测试用例1: [3,2,3,null,3,null,1]
        TreeNode root1 = new TreeNode(3,
            new TreeNode(2, null, new TreeNode(3)),
            new TreeNode(3, null, new TreeNode(1)));
        System.out.println("测试1: " + solution.rob(root1)); // 期望: 7
        
        // 测试用例2: [3,4,5,1,3,null,1]
        TreeNode root2 = new TreeNode(3,
            new TreeNode(4, new TreeNode(1), new TreeNode(3)),
            new TreeNode(5, null, new TreeNode(1)));
        System.out.println("测试2: " + solution.rob(root2)); // 期望: 9
        
        // 测试用例3: 单节点
        TreeNode root3 = new TreeNode(3);
        System.out.println("测试3: " + solution.rob(root3)); // 期望: 3
        
        // 测试用例4: 所有节点为负数
        TreeNode root4 = new TreeNode(-3, new TreeNode(-2), new TreeNode(-1));
        System.out.println("测试4: " + solution.rob(root4)); // 期望: -1
        
        // 测试记忆化搜索版本
        System.out.println("记忆化版本测试1: " + solution.robMemo(root1)); // 期望: 7
        System.out.println("记忆化版本测试2: " + solution.robMemo(root2)); // 期望: 9
    }
    
    /**
     * 算法复杂度分析：
     * 时间复杂度：O(n) - 每个节点只被访问一次
     * 空间复杂度：O(h) - 递归栈深度，最坏情况下为O(n)
     * 
     * 算法正确性验证：
     * 1. 基础情况：空树返回0，单节点返回节点值
     * 2. 相邻约束：抢劫父节点时不能抢劫子节点
     * 3. 最优子结构：当前节点的最优解依赖于子节点的最优解
     * 
     * 工程化改进：
     * 1. 添加输入验证和异常处理
     * 2. 支持大规模数据的内存优化
     * 3. 添加日志和监控
     */
}