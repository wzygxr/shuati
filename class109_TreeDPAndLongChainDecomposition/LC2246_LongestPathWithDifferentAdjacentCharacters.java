package class162;

// LeetCode 2246. 相邻字符不同的最长路径 - Java实现
// 树形DP经典题目

import java.util.*;

public class LC2246_LongestPathWithDifferentAdjacentCharacters {
    /**
     * 计算树中相邻字符不同的最长路径
     * 
     * 解题思路:
     * 1. 树形DP，通过DFS遍历来解决
     * 2. 对于每个节点，计算经过该节点的最长路径
     * 3. 维护每个节点向下延伸的最长路径和次长路径
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    
    private List<List<Integer>> graph;      // 邻接表表示的树
    private String s;                       // 节点字符
    private int maxLength = 0;              // 全局最长路径
    
    public int longestPath(int[] parent, String s) {
        int n = parent.length;
        this.s = s;
        
        // 初始化邻接表
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 构建树结构（从父节点指向子节点）
        for (int i = 1; i < n; i++) {
            graph.get(parent[i]).add(i);
        }
        
        // 从根节点开始DFS
        dfs(0);
        
        return maxLength;
    }
    
    /**
     * DFS计算以当前节点为根的子树中最长路径
     * @param node 当前节点
     * @return 从当前节点向下延伸的最长路径长度
     */
    private int dfs(int node) {
        int first = 0;   // 最长路径
        int second = 0;  // 次长路径
        
        // 遍历所有子节点
        for (int child : graph.get(node)) {
            int childPath = dfs(child);
            
            // 只有当子节点字符与当前节点字符不同时，才能连接
            if (s.charAt(child) != s.charAt(node)) {
                // 更新最长路径和次长路径
                if (childPath > first) {
                    second = first;
                    first = childPath;
                } else if (childPath > second) {
                    second = childPath;
                }
            }
        }
        
        // 经过当前节点的最长路径 = 最长路径 + 次长路径 + 1（当前节点）
        maxLength = Math.max(maxLength, first + second + 1);
        
        // 返回从当前节点向下延伸的最长路径长度
        return first + 1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        LC2246_LongestPathWithDifferentAdjacentCharacters solution = new LC2246_LongestPathWithDifferentAdjacentCharacters();
        
        // 测试用例1
        int[] parent1 = {-1, 0, 0, 1, 1, 2};
        String s1 = "abacbe";
        int result1 = solution.longestPath(parent1, s1);
        System.out.println("测试用例1结果: " + result1);
        // 预期输出: 3
        
        // 测试用例2
        int[] parent2 = {-1, 0, 0, 0};
        String s2 = "aabc";
        int result2 = solution.longestPath(parent2, s2);
        System.out.println("测试用例2结果: " + result2);
        // 预期输出: 3
    }
}