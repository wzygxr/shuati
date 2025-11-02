package class061;

import java.util.Arrays;

// LeetCode 1135. Connecting Cities With Minimum Cost
// 题目链接: https://leetcode.cn/problems/connecting-cities-with-minimum-cost/
// 
// 题目描述:
// 有 n 个城市，从 1 到 n 进行编号。给定一个 roads 数组，其中 roads[i] = [ai, bi, costi] 表示城市 ai 和 bi 之间建有一条成本为 costi 的双向道路。
// 如果所有城市之间都能通过这些道路相互到达，则返回连接所有城市的最小成本；否则返回 -1。
//
// 解题思路:
// 这是一个典型的最小生成树问题。使用Kruskal算法：
// 1. 将所有边按权重升序排序
// 2. 使用并查集判断添加边是否会形成环
// 3. 依次选择不形成环的最小边，直到选择了n-1条边或遍历完所有边
// 4. 如果最终选择了n-1条边，则返回总成本；否则返回-1
//
// 时间复杂度: O(E * log E)，其中E是边数，主要是排序的时间复杂度
// 空间复杂度: O(V)，其中V是顶点数，用于并查集存储
// 是否为最优解: 是，这是解决最小生成树问题的经典方法
public class Code06_ConnectingCitiesWithMinimumCost {

    public static int minimumCost(int n, int[][] connections) {
        // 按权重升序排序所有边
        Arrays.sort(connections, (a, b) -> a[2] - b[2]);
        
        // 初始化并查集
        UnionFind uf = new UnionFind(n);
        
        int totalCost = 0;
        int edgesUsed = 0;
        
        // 遍历所有边
        for (int[] connection : connections) {
            int city1 = connection[0];
            int city2 = connection[1];
            int cost = connection[2];
            
            // 如果两个城市不在同一集合中，说明连接它们不会形成环
            if (uf.union(city1, city2)) {
                totalCost += cost;
                edgesUsed++;
                
                // 如果已经选择了n-1条边，则已形成最小生成树
                if (edgesUsed == n - 1) {
                    return totalCost;
                }
            }
        }
        
        // 如果无法连接所有城市，返回-1
        return -1;
    }
    
    // 并查集数据结构实现
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n + 1]; // 城市编号从1开始
            rank = new int[n + 1];
            
            // 初始化，每个节点的父节点是自己
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
            }
        }
        
        // 查找根节点（带路径压缩优化）
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 路径压缩
            }
            return parent[x];
        }
        
        // 合并两个集合（按秩合并优化）
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一集合中，返回false
            if (rootX == rootY) {
                return false;
            }
            
            // 按秩合并，将秩小的树合并到秩大的树下
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            return true;
        }
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3;
        int[][] connections1 = {{1, 2, 5}, {1, 3, 6}, {2, 3, 1}};
        System.out.println("测试用例1结果: " + minimumCost(n1, connections1)); // 预期输出: 6
        
        // 测试用例2
        int n2 = 4;
        int[][] connections2 = {{1, 2, 3}, {3, 4, 4}};
        System.out.println("测试用例2结果: " + minimumCost(n2, connections2)); // 预期输出: -1
    }
}