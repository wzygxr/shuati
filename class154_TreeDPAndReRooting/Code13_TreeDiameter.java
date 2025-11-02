package class123;

// 树的直径问题
// 题目来源：LeetCode 543. Diameter of Binary Tree
// 题目链接：https://leetcode.com/problems/diameter-of-binary-tree/
// 测试链接：https://leetcode.com/problems/diameter-of-binary-tree/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
树的直径是树中最长路径的长度。这是一个经典的树形DP问题，可以通过深度优先搜索（DFS）来解决。

算法思路：
方法一：两次DFS（或BFS）
1. 第一次DFS：从任意节点出发，找到离它最远的节点u
2. 第二次DFS：从节点u出发，找到离它最远的节点v
3. u到v的路径就是树的直径

方法二：单次DFS（推荐）
在一次DFS过程中，同时计算每个节点的最大深度，并更新全局的直径最大值
- 对于每个节点，维护两个值：
  - 当前节点的最大深度：max_depth = max(left_depth, right_depth) + 1
  - 当前节点的直径候选值：left_depth + right_depth
- 在遍历过程中，不断更新全局的直径最大值

本实现采用方法二，单次DFS解决问题。

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决树直径问题的最优方法

边界情况：
- 空树：返回0
- 单节点树：返回0（没有边）
- 链式树：正确计算最长路径

与机器学习/深度学习的联系：
- 树结构在图神经网络（GNN）中有广泛应用
- 树的直径等结构特征可以作为图的重要属性
- 在知识图谱中，路径长度是衡量实体间关系紧密程度的重要指标

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code13_TreeDiameter.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code13_TreeDiameter.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code13_TreeDiameter.cpp
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

public class Code13_TreeDiameter {

    // 全局变量，记录树的直径
    private int diameter;
    
    // 主函数，计算二叉树的直径
    public int diameterOfBinaryTree(TreeNode root) {
        // 重置直径值
        diameter = 0;
        
        // 边界条件处理：空树
        if (root == null) {
            return 0;
        }
        
        // 执行DFS，计算每个节点的深度并更新直径
        dfs(root);
        
        return diameter;
    }
    
    // 深度优先搜索函数，返回以当前节点为根的子树的最大深度
    // 同时在过程中计算并更新树的直径
    private int dfs(TreeNode node) {
        // 递归终止条件：节点为空
        if (node == null) {
            return 0;
        }
        
        // 递归计算左子树和右子树的最大深度
        int leftDepth = dfs(node.left);
        int rightDepth = dfs(node.right);
        
        // 更新全局直径：当前节点左子树最大深度 + 右子树最大深度
        // 这表示经过当前节点的最长路径
        diameter = Math.max(diameter, leftDepth + rightDepth);
        
        // 返回当前节点的最大深度（左右子树最大深度 + 1）
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试用例1: [1,2,3,4,5]
        //      1
        //     / \
        //    2   3
        //   / \
        //  4   5
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        
        Code13_TreeDiameter solution = new Code13_TreeDiameter();
        System.out.println("测试用例1结果: " + solution.diameterOfBinaryTree(root1)); // 预期输出: 3
        
        // 测试用例2: [1,2]
        //    1
        //   /
        //  2
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        System.out.println("测试用例2结果: " + solution.diameterOfBinaryTree(root2)); // 预期输出: 1
        
        // 测试用例3: 空树
        System.out.println("测试用例3结果: " + solution.diameterOfBinaryTree(null)); // 预期输出: 0
        
        // 测试用例4: 单节点树
        TreeNode root4 = new TreeNode(1);
        System.out.println("测试用例4结果: " + solution.diameterOfBinaryTree(root4)); // 预期输出: 0
    }
    
    /*
    工程化考量：
    1. 异常处理：
       - 处理了空树和单节点树的边界情况
       - 使用递归，需要注意递归深度可能导致的栈溢出问题
    
    2. 性能优化：
       - 使用单次DFS，避免了两次遍历
       - 通过返回值和全局变量的配合，一次性获取所需信息
    
    3. 代码质量：
       - 命名规范，函数职责单一
       - 添加了详细的注释说明算法思路和边界处理
       - 包含多个测试用例验证正确性
    
    4. 可扩展性：
       - 如果需要处理N叉树，可以扩展dfs函数以处理多个子节点
       - 如果需要知道直径的具体路径，可以在更新diameter时记录路径信息
    */
}