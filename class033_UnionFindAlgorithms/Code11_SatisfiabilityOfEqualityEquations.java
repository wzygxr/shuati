package class056;

import java.util.*;

/**
 * 等式方程的可满足性
 * 给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，
 * 并采用两种不同的形式之一："a==b" 或 "a!=b"。在这里，a 和 b 是小写字母（不一定不同），
 * 表示单字母变量名。只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。
 * 
 * 示例 1:
 * 输入: ["a==b","b!=a"]
 * 输出: false
 * 解释: 如果我们指定，a = 1 且 b = 1，那么可以满足第一个方程，但无法满足第二个方程。没有办法分配变量同时满足这两个方程。
 * 
 * 示例 2:
 * 输入: ["b==a","a==b"]
 * 输出: true
 * 解释: 我们可以指定 a = 1 且 b = 1 以满足满足这两个方程。
 * 
 * 示例 3:
 * 输入: ["a==b","b==c","a==c"]
 * 输出: true
 * 
 * 示例 4:
 * 输入: ["a==b","b!=c","c==a"]
 * 输出: false
 * 
 * 示例 5:
 * 输入: ["c==c","b==d","x!=z"]
 * 输出: true
 * 
 * 约束条件：
 * 1 <= equations.length <= 500
 * equations[i].length == 4
 * equations[i][0] 和 equations[i][3] 是小写字母
 * equations[i][1] 是 '=' 或 '!'
 * equations[i][2] 是 '='
 * 
 * 测试链接: https://leetcode.cn/problems/satisfiability-of-equality-equations/
 * 相关平台: LeetCode 990
 */
public class Code11_SatisfiabilityOfEqualityEquations {
    
    /**
     * 使用并查集解决等式方程的可满足性问题
     * 
     * 解题思路：
     * 1. 首先处理所有等式方程，将相等的变量连接到同一个集合中
     * 2. 然后检查所有不等式方程，如果两个变量在同一个集合中，则返回false
     * 3. 如果所有不等式方程都满足，则返回true
     * 
     * 时间复杂度：O(N * α(26))，其中N是方程数量，α是阿克曼函数的反函数
     * 空间复杂度：O(1)，因为只有26个小写字母
     * 
     * @param equations 方程数组
     * @return 如果可以满足所有方程返回true，否则返回false
     */
    public static boolean equationsPossible(String[] equations) {
        // 边界条件检查
        if (equations == null || equations.length == 0) {
            return true;
        }
        
        // 创建并查集，大小为26（对应26个小写字母）
        UnionFind uf = new UnionFind(26);
        
        // 首先处理所有等式方程
        for (String equation : equations) {
            if (equation.charAt(1) == '=') {
                int x = equation.charAt(0) - 'a';
                int y = equation.charAt(3) - 'a';
                uf.union(x, y);
            }
        }
        
        // 然后检查所有不等式方程
        for (String equation : equations) {
            if (equation.charAt(1) == '!') {
                int x = equation.charAt(0) - 'a';
                int y = equation.charAt(3) - 'a';
                // 如果两个变量在同一个集合中，则不等式不成立
                if (uf.find(x) == uf.find(y)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 并查集数据结构实现
     * 包含路径压缩优化
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n];
            // 初始时每个节点都是自己的父节点
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        /**
         * 查找节点的根节点（代表元素）
         * 使用路径压缩优化
         * @param x 要查找的节点
         * @return 节点x所在集合的根节点
         */
        public int find(int x) {
            if (parent[x] != x) {
                // 路径压缩：将路径上的所有节点直接连接到根节点
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        /**
         * 合并两个集合
         * @param x 第一个节点
         * @param y 第二个节点
         */
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            // 如果已经在同一个集合中，则无需合并
            if (rootX != rootY) {
                parent[rootX] = rootY;
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String[] equations1 = {"a==b", "b!=a"};
        System.out.println("测试用例1结果: " + equationsPossible(equations1)); // 预期输出: false
        
        // 测试用例2
        String[] equations2 = {"b==a", "a==b"};
        System.out.println("测试用例2结果: " + equationsPossible(equations2)); // 预期输出: true
        
        // 测试用例3
        String[] equations3 = {"a==b", "b==c", "a==c"};
        System.out.println("测试用例3结果: " + equationsPossible(equations3)); // 预期输出: true
        
        // 测试用例4
        String[] equations4 = {"a==b", "b!=c", "c==a"};
        System.out.println("测试用例4结果: " + equationsPossible(equations4)); // 预期输出: false
        
        // 测试用例5
        String[] equations5 = {"c==c", "b==d", "x!=z"};
        System.out.println("测试用例5结果: " + equationsPossible(equations5)); // 预期输出: true
        
        // 测试用例6：空数组
        String[] equations6 = {};
        System.out.println("测试用例6结果: " + equationsPossible(equations6)); // 预期输出: true
    }
}