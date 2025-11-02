package class061;

import java.util.*;

// UVa 10369 Arctic Network
// 题目链接: https://vjudge.net/problem/UVA-10369
// 
// 题目描述:
// 国防部(DOD)希望通过无线网络连接若干偏远地区的军事基地。该网络由两种不同类型的连接组成：
// 1. 卫星信道 - 可以连接任意两个站点，数量有限
// 2. 地面连接 - 通过无线电收发器连接，成本与距离成正比
// 
// 给定基地的坐标和可用的卫星信道数，确定使所有基地连通所需的最小无线电传输距离D。
//
// 解题思路:
// 这是一个最小生成树的变种问题。我们有S个卫星信道，可以连接任意两个站点，
// 这意味着我们可以将整个网络分成S个连通分量，每个连通分量内的站点通过地面连接。
// 因此，我们需要构建最小生成树，然后删除最大的S-1条边，剩下的最大边就是答案。
//
// 具体步骤：
// 1. 计算所有站点之间的欧几里得距离
// 2. 使用Kruskal算法构建最小生成树
// 3. 在MST中，最大的S-1条边可以被卫星信道替代
// 4. 返回第(S-1)大的边的权重作为答案
//
// 时间复杂度: O(N^2 * log(N^2)) = O(N^2 * log N)，其中N是站点数
// 空间复杂度: O(N^2)
// 是否为最优解: 是，这是解决该问题的高效方法
// 工程化考量:
// 1. 异常处理: 检查输入参数的有效性
// 2. 边界条件: 处理少于2个站点的情况
// 3. 内存管理: 使用ArrayList存储边信息
// 4. 性能优化: 并查集的路径压缩和按秩合并优化

public class Code21_ArcticNetwork {
    
    public static double arcticNetwork(int S, int[][] positions) {
        int N = positions.length;
        
        // 特殊情况：站点数小于等于卫星信道数
        if (N <= S) {
            return 0.0;
        }
        
        // 构建所有边（站点间的距离）
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double distance = Math.sqrt(
                    Math.pow(positions[i][0] - positions[j][0], 2) +
                    Math.pow(positions[i][1] - positions[j][1], 2)
                );
                edges.add(new Edge(i, j, distance));
            }
        }
        
        // 按权重排序
        Collections.sort(edges, (a, b) -> Double.compare(a.weight, b.weight));
        
        // 使用并查集构建MST
        UnionFind uf = new UnionFind(N);
        List<Double> mstEdges = new ArrayList<>();
        
        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) {
                mstEdges.add(edge.weight);
                // MST完成
                if (mstEdges.size() == N - 1) {
                    break;
                }
            }
        }
        
        // 我们可以使用S个卫星信道来替代最大的S-1条边
        // 因此，我们需要返回第(N-S)大的边的权重
        return mstEdges.get(N - S - 1);
    }
    
    // 边的结构体
    static class Edge {
        int u, v;
        double weight;
        
        Edge(int u, int v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }
    
    // 并查集数据结构实现
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            
            // 初始化，每个节点的父节点是自己
            for (int i = 0; i < n; i++) {
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
        int S1 = 2;
        int[][] positions1 = {{0, 100}, {0, 300}, {0, 600}, {150, 750}};
        System.out.printf("测试用例1结果: %.2f\n", arcticNetwork(S1, positions1)); // 预期输出: 212.13
        
        // 测试用例2
        int S2 = 1;
        int[][] positions2 = {{0, 1}, {0, 2}, {0, 4}, {0, 7}, {0, 11}};
        System.out.printf("测试用例2结果: %.2f\n", arcticNetwork(S2, positions2)); // 预期输出: 7.00
    }
}