package class123;

// 子树中的最大平均值
// 题目来源：LeetCode 1120. Maximum Average Subtree
// 题目链接：https://leetcode.com/problems/maximum-average-subtree/
// 测试链接：https://leetcode.com/problems/maximum-average-subtree/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一个树形动态规划问题。我们需要计算二叉树中所有可能子树的平均值，并找出其中的最大值。

算法思路：
对于每个节点，我们需要维护两个值：
1. 以该节点为根的子树的节点值总和
2. 以该节点为根的子树的节点数量

然后，对于每个节点，我们可以计算其对应的平均值（总和/数量），并更新全局的最大平均值。

状态转移方程：
- 子树节点值总和 = 当前节点值 + 左子树节点值总和 + 右子树节点值总和
- 子树节点数量 = 1 + 左子树节点数量 + 右子树节点数量
- 当前子树平均值 = 子树节点值总和 / 子树节点数量

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决子树最大平均值问题的最优方法

边界情况：
- 空树：不存在子树，理论上返回0或抛出异常，但根据题目约束通常输入不会是空树
- 单节点树：平均值就是该节点的值
- 所有节点值相同：最大平均值就是该节点值

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 子树特征提取与图神经网络（GNN）中的节点聚合操作类似
- 平均值计算是最基本的统计操作，在数据分析和模型训练中常用

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.cpp
*/

// Definition for a binary tree node.
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

public class Code15_MaximumAverageSubtree {
    
    // 全局变量，记录最大平均值
    private double maxAverage;
    
    // 主函数，计算树中任意子树的节点值的最大平均值
    public double maximumAverageSubtree(TreeNode root) {
        // 边界条件处理：空树
        if (root == null) {
            return 0.0;
        }
        
        // 重置最大平均值
        maxAverage = 0.0;
        
        // 执行DFS，计算每个子树的总和、节点数，并更新最大平均值
        dfs(root);
        
        return maxAverage;
    }
    
    // 深度优先搜索函数，返回一个长度为2的数组
    // result[0]: 以当前节点为根的子树的节点值总和
    // result[1]: 以当前节点为根的子树的节点数量
    private int[] dfs(TreeNode node) {
        // 递归终止条件：节点为空
        if (node == null) {
            return new int[]{0, 0};
        }
        
        // 递归计算左右子树的总和和节点数
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // 计算当前子树的总和和节点数
        int sum = node.val + left[0] + right[0];
        int count = 1 + left[1] + right[1];
        
        // 计算当前子树的平均值
        double average = (double) sum / count;
        
        // 更新全局最大平均值
        if (average > maxAverage) {
            maxAverage = average;
        }
        
        return new int[]{sum, count};
    }
    
    // 另一种实现方式：使用自定义的结果类，更加清晰
    public double maximumAverageSubtree2(TreeNode root) {
        // 边界条件处理：空树
        if (root == null) {
            return 0.0;
        }
        
        // 重置最大平均值
        maxAverage = 0.0;
        
        // 执行DFS，计算每个子树的总和、节点数，并更新最大平均值
        dfs2(root);
        
        return maxAverage;
    }
    
    // 深度优先搜索函数，返回一个Result对象
    private Result dfs2(TreeNode node) {
        // 递归终止条件：节点为空
        if (node == null) {
            return new Result(0, 0);
        }
        
        // 递归计算左右子树的总和和节点数
        Result left = dfs2(node.left);
        Result right = dfs2(node.right);
        
        // 计算当前子树的总和和节点数
        int sum = node.val + left.sum + right.sum;
        int count = 1 + left.count + right.count;
        
        // 计算当前子树的平均值
        double average = (double) sum / count;
        
        // 更新全局最大平均值
        if (average > maxAverage) {
            maxAverage = average;
        }
        
        return new Result(sum, count);
    }
    
    // 自定义的结果类，用于存储子树的总和和节点数
    private static class Result {
        int sum;
        int count;
        
        public Result(int sum, int count) {
            this.sum = sum;
            this.count = count;
        }
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试用例1: [5,6,1]
        //      5
        //     / \
        //    6   1
        TreeNode root1 = new TreeNode(5);
        root1.left = new TreeNode(6);
        root1.right = new TreeNode(1);
        
        Code15_MaximumAverageSubtree solution = new Code15_MaximumAverageSubtree();
        System.out.println("测试用例1结果: " + solution.maximumAverageSubtree(root1)); // 预期输出: 6.0 (子树[6])
        
        // 测试用例2: [0,null,1]
        //    0
        //     \
        //      1
        TreeNode root2 = new TreeNode(0);
        root2.right = new TreeNode(1);
        System.out.println("测试用例2结果: " + solution.maximumAverageSubtree(root2)); // 预期输出: 1.0 (子树[1])
        
        // 测试用例3: [3,1,3,1,1,1,1]
        //      3
        //     / \
        //    1   3
        //   / \ / \
        //  1  1 1  1
        TreeNode root3 = new TreeNode(3);
        root3.left = new TreeNode(1);
        root3.right = new TreeNode(3);
        root3.left.left = new TreeNode(1);
        root3.left.right = new TreeNode(1);
        root3.right.left = new TreeNode(1);
        root3.right.right = new TreeNode(1);
        System.out.println("测试用例3结果: " + solution.maximumAverageSubtree(root3)); // 预期输出: 3.0 (子树[3]或子树[3])
        
        // 测试用例4: 单节点树
        TreeNode root4 = new TreeNode(10);
        System.out.println("测试用例4结果: " + solution.maximumAverageSubtree(root4)); // 预期输出: 10.0
        
        // 测试用例5: 使用自定义Result类的实现
        System.out.println("测试用例1 (Result类实现): " + solution.maximumAverageSubtree2(root1)); // 预期输出: 6.0
    }
    
    /*
    工程化考量：
    1. 异常处理：
       - 处理了空树的边界情况
       - 注意浮点数精度问题，使用double类型存储平均值
    
    2. 性能优化：
       - 使用后序遍历，一次性计算所有需要的信息
       - 避免了重复计算子树的总和和节点数
    
    3. 代码质量：
       - 提供了两种实现方式：使用数组和使用自定义类
       - 添加了详细的注释说明算法思路和边界处理
       - 包含多个测试用例验证正确性
    
    4. 可扩展性：
       - 如果需要处理N叉树，可以扩展dfs函数以处理多个子节点
       - 如果需要记录具体哪个子树具有最大平均值，可以在更新maxAverage时记录节点信息
    
    5. 调试技巧：
       - 可以在dfs函数中添加打印语句，输出当前节点的值、子树总和、节点数和平均值
       - 使用调试器逐步执行，观察递归调用栈和变量变化
    
    6. 浮点精度考虑：
       - 当比较两个平均值的大小时，需要注意浮点数精度问题
       - 在实际应用中，可能需要使用epsilon进行比较
    */
}