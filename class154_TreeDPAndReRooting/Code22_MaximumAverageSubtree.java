// 子树中的最大平均值 - LeetCode 1120
// 给定一棵二叉树的根节点，找到平均值最大的子树
// 子树平均值定义为该子树所有节点值的和除以节点个数
// 测试链接 : https://leetcode.com/problems/maximum-average-subtree/

/*
题目解析：
这是一个树形DP问题，需要计算每个子树的和与节点数，然后计算平均值。

算法思路：
1. 使用后序遍历（DFS）处理每个节点
2. 对于每个节点，计算其子树的和与节点数
3. 计算当前子树的平均值
4. 全局维护最大平均值

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，这是解决此类问题的最优方法

工程化考量：
1. 精度处理：使用double类型避免整数除法精度丢失
2. 异常处理：空树、单节点树
3. 边界条件：所有节点值相同、节点值为负数

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code22_MaximumAverageSubtree.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.cpp
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

public class Code22_MaximumAverageSubtree {
    
    private double maxAverage;
    
    public double maximumAverageSubtree(TreeNode root) {
        maxAverage = 0.0;
        dfs(root);
        return maxAverage;
    }
    
    /**
     * DFS遍历，返回当前子树的和与节点数
     * 
     * @param node 当前节点
     * @return int[2]数组，[0]表示子树和，[1]表示节点数
     */
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        // 递归处理左右子树
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // 计算当前子树的和与节点数
        int sum = left[0] + right[0] + node.val;
        int count = left[1] + right[1] + 1;
        
        // 计算当前子树的平均值
        double average = (double) sum / count;
        maxAverage = Math.max(maxAverage, average);
        
        return new int[]{sum, count};
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code22_MaximumAverageSubtree solution = new Code22_MaximumAverageSubtree();
        
        // 测试用例1: [5,6,1]
        TreeNode root1 = new TreeNode(5, new TreeNode(6), new TreeNode(1));
        System.out.println("测试1: " + solution.maximumAverageSubtree(root1)); // 期望: 6.0
        
        // 测试用例2: [0,null,1]
        TreeNode root2 = new TreeNode(0, null, new TreeNode(1));
        System.out.println("测试2: " + solution.maximumAverageSubtree(root2)); // 期望: 1.0
        
        // 测试用例3: 单节点
        TreeNode root3 = new TreeNode(10);
        System.out.println("测试3: " + solution.maximumAverageSubtree(root3)); // 期望: 10.0
        
        // 测试用例4: 所有节点值相同
        TreeNode root4 = new TreeNode(3, 
            new TreeNode(3), 
            new TreeNode(3, new TreeNode(3), new TreeNode(3)));
        System.out.println("测试4: " + solution.maximumAverageSubtree(root4)); // 期望: 3.0
        
        // 测试用例5: 包含负数
        TreeNode root5 = new TreeNode(-1, new TreeNode(-2), new TreeNode(3));
        System.out.println("测试5: " + solution.maximumAverageSubtree(root5)); // 期望: 3.0
    }
    
    /**
     * 算法复杂度分析：
     * 时间复杂度：O(n) - 每个节点只被访问一次
     * 空间复杂度：O(h) - 递归栈深度
     * 
     * 算法正确性验证：
     * 1. 基础情况：空树返回0，单节点返回节点值
     * 2. 平均值计算：使用double避免精度问题
     * 3. 全局维护：正确更新最大平均值
     * 
     * 工程化改进：
     * 1. 添加输入验证
     * 2. 支持大规模数据
     * 3. 添加性能监控
     */
}