package class056;

import java.util.*;

/**
 * 连通网络的操作次数
 * 用以太网线缆将 n 台计算机连接成一个网络，计算机的编号从 0 到 n-1。
 * 线缆用 connections 表示，其中 connections[i] = [a, b] 连接了计算机 a 和 b。
 * 网络中的任何一台计算机都可以通过网络直接或间接访问同一个网络中其他任意一台计算机。
 * 给你这个计算机网络的初始布线 connections，你可以拔开任意两台直连计算机之间的线缆，
 * 并用它连接一对未直连的计算机。请你计算并返回使所有计算机都连通所需的最少操作次数。
 * 如果不可能，则返回 -1。
 * 
 * 示例 1:
 * 输入: n = 4, connections = [[0,1],[0,2],[1,2]]
 * 输出: 1
 * 解释: 拔下计算机 1 和 2 之间的线缆，并将它插到计算机 1 和 3 上。
 * 
 * 示例 2:
 * 输入: n = 6, connections = [[0,1],[0,2],[0,3],[1,2],[1,3]]
 * 输出: 2
 * 
 * 示例 3:
 * 输入: n = 6, connections = [[0,1],[0,2],[0,3],[1,2]]
 * 输出: -1
 * 解释: 线缆数量不足。
 * 
 * 示例 4:
 * 输入: n = 5, connections = [[0,1],[0,2],[3,4],[2,3]]
 * 输出: 0
 * 
 * 约束条件：
 * 1 <= n <= 10^5
 * 1 <= connections.length <= min(n*(n-1)/2, 10^5)
 * connections[i].length == 2
 * 0 <= connections[i][0], connections[i][1] < n
 * connections[i][0] != connections[i][1]
 * 没有重复的连接
 * 两台计算机不会通过多条线缆连接
 * 
 * 测试链接: https://leetcode.cn/problems/number-of-operations-to-make-network-connected/
 * 相关平台: LeetCode 1319
 */
public class Code12_NumberOfOperationsToMakeNetworkConnected {
    
    /**
     * 使用并查集解决连通网络的操作次数问题
     * 
     * 解题思路：
     * 1. 首先检查线缆数量是否足够，至少需要n-1条线缆才能连接n台计算机
     * 2. 使用并查集统计当前有多少个连通分量
     * 3. 要连接k个连通分量，需要k-1次操作
     * 
     * 时间复杂度：O(M * α(N))，其中M是connections的长度，N是计算机数量，α是阿克曼函数的反函数
     * 空间复杂度：O(N)
     * 
     * @param n 计算机数量
     * @param connections 线缆连接关系
     * @return 最少操作次数，如果不可能则返回-1
     */
    public static int makeConnected(int n, int[][] connections) {
        // 边界条件检查
        if (n <= 1) {
            return 0;
        }
        
        // 线缆数量不足，无法连接所有计算机
        if (connections.length < n - 1) {
            return -1;
        }
        
        // 创建并查集
        UnionFind uf = new UnionFind(n);
        
        // 处理所有连接
        int redundantCables = 0;  // 冗余线缆数量
        for (int[] connection : connections) {
            int a = connection[0];
            int b = connection[1];
            // 如果两个计算机已经在同一个连通分量中，则这条线缆是冗余的
            if (uf.find(a) == uf.find(b)) {
                redundantCables++;
            } else {
                uf.union(a, b);
            }
        }
        
        // 统计连通分量数量
        int components = uf.getComponents();
        
        // 需要components-1次操作来连接所有连通分量
        // 只要冗余线缆数量足够就行
        return components - 1;
    }
    
    /**
     * 并查集数据结构实现
     * 包含路径压缩和按秩合并优化
     */
    static class UnionFind {
        private int[] parent;  // parent[i]表示节点i的父节点
        private int[] rank;    // rank[i]表示以i为根的树的高度上界
        private int components; // 连通分量数量
        
        /**
         * 初始化并查集
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            // 初始时每个节点都是自己的父节点
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;  // 初始时每个树的秩为1
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
         * 使用按秩合并优化
         * @param x 第一个节点
         * @param y 第二个节点
         */
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一个集合中，则无需合并
            if (rootX != rootY) {
                // 按秩合并：将秩小的树合并到秩大的树下
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    // 秩相等时，任选一个作为根，并将其秩加1
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
                // 连通分量数量减1
                components--;
            }
        }
        
        /**
         * 获取连通分量数量
         * @return 连通分量数量
         */
        public int getComponents() {
            return components;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4;
        int[][] connections1 = {{0,1},{0,2},{1,2}};
        System.out.println("测试用例1结果: " + makeConnected(n1, connections1)); // 预期输出: 1
        
        // 测试用例2
        int n2 = 6;
        int[][] connections2 = {{0,1},{0,2},{0,3},{1,2},{1,3}};
        System.out.println("测试用例2结果: " + makeConnected(n2, connections2)); // 预期输出: 2
        
        // 测试用例3
        int n3 = 6;
        int[][] connections3 = {{0,1},{0,2},{0,3},{1,2}};
        System.out.println("测试用例3结果: " + makeConnected(n3, connections3)); // 预期输出: -1
        
        // 测试用例4
        int n4 = 5;
        int[][] connections4 = {{0,1},{0,2},{3,4},{2,3}};
        System.out.println("测试用例4结果: " + makeConnected(n4, connections4)); // 预期输出: 0
        
        // 测试用例5：单个计算机
        int n5 = 1;
        int[][] connections5 = {};
        System.out.println("测试用例5结果: " + makeConnected(n5, connections5)); // 预期输出: 0
    }
}