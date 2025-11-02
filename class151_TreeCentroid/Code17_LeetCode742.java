package class120;

// LeetCode 742. 二叉树中最近的叶节点
// 题目描述：给定一个二叉树，其中每个节点都含有一个整数键，给定一个键 k，找出距离给定节点最近的叶节点
// 算法思想：将二叉树转换为无向图，然后进行广度优先搜索。对于大型树，可以先找到重心以优化搜索
// 测试链接：https://leetcode.cn/problems/closest-leaf-in-a-binary-tree/
// 时间复杂度：O(n)
// 空间复杂度：O(n)

import java.util.*;

public class Code17_LeetCode742 {
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    // 将二叉树转换为无向图
    private void buildGraph(TreeNode root, TreeNode parent, Map<Integer, List<Integer>> graph,
                          Map<Integer, Boolean> isLeaf) {
        if (root == null) return;
        
        // 初始化邻接表
        graph.putIfAbsent(root.val, new ArrayList<>());
        
        // 检查是否为叶节点
        if (root.left == null && root.right == null) {
            isLeaf.put(root.val, true);
        } else {
            isLeaf.put(root.val, false);
        }
        
        // 添加与父节点的连接
        if (parent != null) {
            graph.get(root.val).add(parent.val);
            graph.get(parent.val).add(root.val);
        }
        
        // 递归处理左右子树
        buildGraph(root.left, root, graph, isLeaf);
        buildGraph(root.right, root, graph, isLeaf);
    }
    
    // 寻找最近的叶节点
    public int findClosestLeaf(TreeNode root, int k) {
        // 构建图和标记叶节点
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Map<Integer, Boolean> isLeaf = new HashMap<>();
        buildGraph(root, null, graph, isLeaf);
        
        // 广度优先搜索
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        
        queue.offer(k);
        visited.add(k);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            // 如果是叶节点，返回
            if (isLeaf.get(current)) {
                return current;
            }
            
            // 遍历所有邻居
            for (int neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        // 不应该到达这里
        return -1;
    }
    
    // 用于测试的主方法
    public static void main(String[] args) {
        // 构建测试用例
        // 示例1: [1, 3, 2]
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(3);
        root1.right = new TreeNode(2);
        
        Code17_LeetCode742 solution = new Code17_LeetCode742();
        System.out.println("Example 1: " + solution.findClosestLeaf(root1, 1)); // Expected: 3
        
        // 示例2: [1]
        TreeNode root2 = new TreeNode(1);
        System.out.println("Example 2: " + solution.findClosestLeaf(root2, 1)); // Expected: 1
        
        // 示例3: [1,2,3,4,null,null,null,5,null,6]
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(2);
        root3.right = new TreeNode(3);
        root3.left.left = new TreeNode(4);
        root3.left.left.left = new TreeNode(5);
        root3.left.left.left.left = new TreeNode(6);
        System.out.println("Example 3: " + solution.findClosestLeaf(root3, 2)); // Expected: 3
    }
}