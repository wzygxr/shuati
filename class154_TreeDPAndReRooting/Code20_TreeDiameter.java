// 树的直径 - LeetCode 543 / HDU 2196
// 给定一棵树，找到树中最长的路径（直径）
// 直径定义为树中任意两个节点之间的最长路径
// 测试链接 : https://leetcode.com/problems/diameter-of-binary-tree/
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=2196

/*
题目解析：
树的直径问题有两种经典解法：
1. 两次BFS/DFS：先找到最远点，再从最远点找到最远点
2. 树形DP：计算每个节点为根的子树中的最长路径

算法思路（树形DP版）：
1. 对于每个节点，计算以该节点为根的子树中的最长路径
2. 最长路径可能经过该节点（左子树最深 + 右子树最深）
3. 或者完全在某个子树中
4. 全局维护最大直径

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，树形DP是解决此类问题的最优方法

工程化考量：
1. 异常处理：空树、单节点树
2. 边界条件：链状树、星状树
3. 性能优化：避免重复计算

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code20_TreeDiameter.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code13_TreeDiameter.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code13_TreeDiameter.cpp
*/

import java.util.*;

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

public class Code20_TreeDiameter {
    
    private int diameter;
    
    public int diameterOfBinaryTree(TreeNode root) {
        diameter = 0;
        dfs(root);
        return diameter;
    }
    
    /**
     * 计算以当前节点为根的子树的最大深度
     * 同时更新全局直径
     */
    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的最大深度
        int leftDepth = dfs(node.left);
        int rightDepth = dfs(node.right);
        
        // 更新直径：经过当前节点的路径长度
        diameter = Math.max(diameter, leftDepth + rightDepth);
        
        // 返回以当前节点为根的子树的最大深度
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    // 通用树的直径计算（适用于多叉树）
    public int treeDiameter(List<List<Integer>> graph) {
        int n = graph.size();
        diameter = 0;
        dfs(0, -1, graph);
        return diameter;
    }
    
    /**
     * 计算通用树中每个节点的最大深度
     * 同时更新全局直径
     */
    private int dfs(int node, int parent, List<List<Integer>> graph) {
        int maxDepth1 = 0; // 最大深度
        int maxDepth2 = 0; // 次大深度
        
        for (int neighbor : graph.get(node)) {
            if (neighbor == parent) continue;
            
            int depth = dfs(neighbor, node, graph);
            
            if (depth > maxDepth1) {
                maxDepth2 = maxDepth1;
                maxDepth1 = depth;
            } else if (depth > maxDepth2) {
                maxDepth2 = depth;
            }
        }
        
        // 更新直径：经过当前节点的最长路径
        diameter = Math.max(diameter, maxDepth1 + maxDepth2);
        
        return maxDepth1 + 1;
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code20_TreeDiameter solution = new Code20_TreeDiameter();
        
        // 测试二叉树直径
        System.out.println("=== 二叉树直径测试 ===");
        
        // 测试用例1: [1,2,3,4,5]
        TreeNode root1 = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3));
        System.out.println("测试1: " + solution.diameterOfBinaryTree(root1)); // 期望: 3
        
        // 测试用例2: [1,2]
        TreeNode root2 = new TreeNode(1, new TreeNode(2), null);
        System.out.println("测试2: " + solution.diameterOfBinaryTree(root2)); // 期望: 1
        
        // 测试通用树直径
        System.out.println("=== 通用树直径测试 ===");
        
        // 构建树：0-1-2-3, 0-4
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 5; i++) graph.add(new ArrayList<>());
        graph.get(0).add(1); graph.get(0).add(4);
        graph.get(1).add(0); graph.get(1).add(2);
        graph.get(2).add(1); graph.get(2).add(3);
        graph.get(3).add(2);
        graph.get(4).add(0);
        
        System.out.println("测试3: " + solution.treeDiameter(graph)); // 期望: 3
    }
}