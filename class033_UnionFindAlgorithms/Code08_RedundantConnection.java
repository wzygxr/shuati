package class056;

import java.util.*;

/**
 * 冗余连接
 * 树可以看成是一个连通且无环的无向图。
 * 给定往一棵n个节点(节点值1～n)的树中添加一条边后的图。
 * 添加的边的两个顶点包含在1到n中间，且这条附加的边不属于树中已存在的边。
 * 图的信息记录于长度为n的二维数组edges，edges[i] = [ai, bi]表示图中在ai和bi之间存在一条边。
 * 请找出一条可以删去的边，删除后可使得剩余部分是一棵有n个节点的树。
 * 如果有多个答案，则返回数组edges中最后出现的边。
 * 
 * 示例 1:
 * 输入: edges = [[1,2],[1,3],[2,3]]
 * 输出: [2,3]
 * 
 * 示例 2:
 * 输入: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
 * 输出: [1,4]
 * 
 * 约束条件:
 * n == edges.length
 * 3 <= n <= 1000
 * edges[i].length == 2
 * 1 <= ai < bi <= edges.length
 * ai != bi
 * edges中无重复元素
 * 给定的图是连通的
 * 
 * 测试链接: https://leetcode.cn/problems/redundant-connection/
 * 相关平台: LeetCode 684, LintCode 1048, 牛客网
 */
public class Code08_RedundantConnection {
    
    /**
     * 使用并查集解决冗余连接问题
     * 
     * 解题思路：
     * 1. 遍历所有的边，对于每条边的两个顶点：
     *    - 如果它们已经在同一个连通分量中，说明添加这条边会形成环，这就是我们要找的冗余边
     *    - 否则，将这两个顶点所在的连通分量合并
     * 2. 由于题目要求返回最后出现的冗余边，我们按顺序遍历边，找到第一条形成环的边即可
     * 
     * 时间复杂度：O(N * α(N))，其中N是边的数量，α是阿克曼函数的反函数
     * 空间复杂度：O(N)
     * 
     * @param edges 边的数组
     * @return 冗余的边
     */
    public static int[] findRedundantConnection(int[][] edges) {
        if (edges == null || edges.length == 0) {
            return new int[0];
        }
        
        // 节点数量等于边的数量（因为是树加一条边）
        int n = edges.length;
        UnionFind unionFind = new UnionFind(n + 1); // 节点编号从1开始，所以需要n+1个位置
        
        // 遍历所有的边
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            
            // 如果两个节点已经在同一个连通分量中，说明添加这条边会形成环
            if (unionFind.isConnected(node1, node2)) {
                return edge; // 返回这条冗余的边
            } else {
                // 否则将两个节点所在的连通分量合并
                unionFind.union(node1, node2);
            }
        }
        
        // 根据题目保证，一定会有一条冗余边，所以这里不会执行到
        return new int[0];
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
        
        /**
         * 判断两个节点是否在同一个集合中
         * @param x 第一个节点
         * @param y 第二个节点
         * @return 如果在同一个集合中返回true，否则返回false
         */
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] edges1 = {{1,2},{1,3},{2,3}};
        int[] result1 = findRedundantConnection(edges1);
        System.out.println("测试用例1结果: [" + result1[0] + ", " + result1[1] + "]"); // 预期输出: [2, 3]
        
        // 测试用例2
        int[][] edges2 = {{1,2},{2,3},{3,4},{1,4},{1,5}};
        int[] result2 = findRedundantConnection(edges2);
        System.out.println("测试用例2结果: [" + result2[0] + ", " + result2[1] + "]"); // 预期输出: [1, 4]
        
        // 测试用例3
        int[][] edges3 = {{1,2},{1,3},{1,4},{3,4},{4,5}};
        int[] result3 = findRedundantConnection(edges3);
        System.out.println("测试用例3结果: [" + result3[0] + ", " + result3[1] + "]"); // 预期输出: [3, 4]
    }
}