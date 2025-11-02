package class123;

// 打家劫舍III
// 题目来源：LeetCode 337. House Robber III
// 题目链接：https://leetcode.com/problems/house-robber-iii/
// 测试链接：https://leetcode.com/problems/house-robber-iii/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一个树形动态规划问题。在二叉树中，每个节点代表一个房子，节点的值代表该房子的价值。
我们需要选择一些节点，使得选中的节点互不相邻，并且这些节点的价值和最大。

算法思路：
对于每个节点，我们有两种选择：
1. 选择该节点：那么我们不能选择它的左右子节点
2. 不选择该节点：那么我们可以选择或者不选择它的左右子节点（取最大值）

使用树形DP，对于每个节点返回一个长度为2的数组：
- dp[0]: 不选择该节点时，以该节点为根的子树的最大收益
- dp[1]: 选择该节点时，以该节点为根的子树的最大收益

状态转移方程：
- 不选择当前节点：可以选择或不选择子节点，取最大值
  dp[0] = max(left[0], left[1]) + max(right[0], right[1])
- 选择当前节点：不能选择子节点
  dp[1] = root.val + left[0] + right[0]

最终结果是根节点的两种情况的最大值：max(root_dp[0], root_dp[1])

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决打家劫舍III问题的最优方法

边界情况：
- 空树：返回0
- 单节点树：返回该节点的值
- 所有节点价值为负数：应该选择不抢劫任何节点，返回0

与机器学习/深度学习的联系：
- 树形结构在决策树算法中广泛应用
- 动态规划思想与强化学习中的值函数估计有相似之处
- 在树结构上的优化问题与图神经网络（GNN）中的节点分类问题相关

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.cpp
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

public class Code14_HouseRobberIII {
    
    // 主函数，计算小偷一晚能够盗取的最高金额
    public int rob(TreeNode root) {
        // 边界条件处理：空树
        if (root == null) {
            return 0;
        }
        
        // 调用DP函数，获取根节点的两种状态
        int[] result = dfs(root);
        
        // 返回两种情况的最大值：选择根节点或不选择根节点
        return Math.max(result[0], result[1]);
    }
    
    // 深度优先搜索函数，返回一个长度为2的数组
    // result[0]: 不选择当前节点时的最大收益
    // result[1]: 选择当前节点时的最大收益
    private int[] dfs(TreeNode node) {
        // 递归终止条件：节点为空
        if (node == null) {
            return new int[]{0, 0};
        }
        
        // 递归计算左右子树的两种状态
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        
        // 计算当前节点的两种状态
        int[] result = new int[2];
        
        // 不选择当前节点：可以选择或不选择子节点，取最大值
        result[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        // 选择当前节点：不能选择子节点，只能加上不选择子节点时的最大值
        result[1] = node.val + left[0] + right[0];
        
        return result;
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试用例1: [3,2,3,null,3,null,1]
        //      3
        //     / \
        //    2   3
        //     \   \
        //      3   1
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.right = new TreeNode(3);
        root1.right.right = new TreeNode(1);
        
        Code14_HouseRobberIII solution = new Code14_HouseRobberIII();
        System.out.println("测试用例1结果: " + solution.rob(root1)); // 预期输出: 7 (选择3, 3, 1)
        
        // 测试用例2: [3,4,5,1,3,null,1]
        //      3
        //     / \
        //    4   5
        //   / \   \
        //  1   3   1
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(4);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(1);
        root2.left.right = new TreeNode(3);
        root2.right.right = new TreeNode(1);
        System.out.println("测试用例2结果: " + solution.rob(root2)); // 预期输出: 9 (选择4, 5, 1)
        
        // 测试用例3: 空树
        System.out.println("测试用例3结果: " + solution.rob(null)); // 预期输出: 0
        
        // 测试用例4: 单节点树
        TreeNode root4 = new TreeNode(1);
        System.out.println("测试用例4结果: " + solution.rob(root4)); // 预期输出: 1
        
        // 测试用例5: 所有节点价值为负数
        TreeNode root5 = new TreeNode(-1);
        root5.left = new TreeNode(-2);
        root5.right = new TreeNode(-3);
        System.out.println("测试用例5结果: " + solution.rob(root5)); // 预期输出: 0 (不选择任何节点)
    }
    
    /*
    工程化考量：
    1. 异常处理：
       - 处理了空树和单节点树的边界情况
       - 考虑了所有节点价值为负数的情况
    
    2. 性能优化：
       - 使用后序遍历，一次性计算所有需要的信息
       - 不需要使用额外的数据结构存储中间结果
    
    3. 代码质量：
       - 命名规范，函数职责单一
       - 添加了详细的注释说明算法思路和边界处理
       - 包含多个测试用例验证正确性
    
    4. 可扩展性：
       - 如果需要处理N叉树，可以扩展dfs函数以处理多个子节点
       - 如果需要记录具体选择了哪些节点，可以在返回结果中添加路径信息
    
    5. 调试技巧：
       - 可以在dfs函数中添加打印语句，输出当前节点的值和计算的两种状态
       - 使用调试器逐步执行，观察递归调用栈和变量变化
    */
}