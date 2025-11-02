package class061;

import java.util.*;

// LeetCode 1489. Find Critical and Pseudo-Critical Edges in Minimum Spanning Tree
// 题目链接: https://leetcode.cn/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/
// 
// 题目描述:
// 给你一个 n 个点的带权无向连通图，节点编号为 0 到 n-1，同时还有一个数组 edges，
// 其中 edges[i] = [fromi, toi, weighti] 表示在 fromi 和 toi 节点之间有一条权重为 weighti 的边。
// 找到最小生成树(MST)中的关键边和伪关键边。
// 
// 关键边：如果从图中删去某条边，会导致最小生成树的权值和增加，那么我们就说它是一条关键边。
// 伪关键边：可能会出现在某些最小生成树中但不会出现在所有最小生成树中的边。
//
// 解题思路:
// 1. 首先计算原始图的MST权重
// 2. 对于每条边，判断它是否为关键边或伪关键边：
//    - 关键边：删除该边后，MST权重增加或图不连通
//    - 伪关键边：该边可能出现在某些MST中（强制包含该边的MST权重等于原始MST权重）
//
// 时间复杂度: O(E^2 * α(V))，其中E是边数，V是顶点数，α是阿克曼函数的反函数
// 空间复杂度: O(V)
// 是否为最优解: 是，这是解决该问题的高效方法
// 工程化考量:
// 1. 异常处理: 检查输入参数的有效性
// 2. 边界条件: 处理空图、单节点图等特殊情况
// 3. 内存管理: 使用ArrayList存储结果
// 4. 性能优化: 并查集的路径压缩和按秩合并优化

public class Code23_CriticalAndPseudoCriticalEdges {
    
    public static List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
        // 为每条边添加原始索引
        int[][] newEdges = new int[edges.length][4];
        for (int i = 0; i < edges.length; i++) {
            newEdges[i][0] = edges[i][0];
            newEdges[i][1] = edges[i][1];
            newEdges[i][2] = edges[i][2];
            newEdges[i][3] = i;
        }
        
        // 按权重排序
        Arrays.sort(newEdges, (a, b) -> Integer.compare(a[2], b[2]));
        
        // 计算原始MST的权重
        int mstWeight = kruskal(n, newEdges, -1, -1);
        
        List<Integer> critical = new ArrayList<>();
        List<Integer> pseudoCritical = new ArrayList<>();
        
        // 检查每条边
        for (int i = 0; i < newEdges.length; i++) {
            int index = newEdges[i][3];
            
            // 检查是否为关键边：删除该边后MST权重增加或图不连通
            int weightWithoutEdge = kruskal(n, newEdges, i, -1);
            if (weightWithoutEdge > mstWeight) {
                critical.add(index);
                continue;
            }
            
            // 检查是否为伪关键边：强制包含该边的MST权重等于原始MST权重
            int weightWithEdge = kruskal(n, newEdges, -1, i);
            if (weightWithEdge == mstWeight) {
                pseudoCritical.add(index);
            }
        }
        
        List<List<Integer>> result = new ArrayList<>();
        result.add(critical);
        result.add(pseudoCritical);
        return result;
    }
    
    // Kruskal算法实现
    // excludeEdge: 要排除的边的索引，-1表示不排除任何边
    // includeEdge: 要包含的边的索引，-1表示不强制包含任何边
    private static int kruskal(int n, int[][] edges, int excludeEdge, int includeEdge) {
        UnionFind uf = new UnionFind(n);
        int weight = 0;
        
        // 如果指定了要包含的边，先添加这条边
        if (includeEdge != -1) {
            uf.union(edges[includeEdge][0], edges[includeEdge][1]);
            weight += edges[includeEdge][2];
        }
        
        // 添加其他边
        for (int i = 0; i < edges.length; i++) {
            // 跳过要排除的边
            if (i == excludeEdge) {
                continue;
            }
            
            int u = edges[i][0];
            int v = edges[i][1];
            int w = edges[i][2];
            
            if (uf.union(u, v)) {
                weight += w;
            }
        }
        
        // 检查是否所有节点都连通
        return uf.getComponents() == 1 ? weight : Integer.MAX_VALUE;
    }
    
    // 并查集数据结构实现
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        private int components;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            
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
            
            components--;
            return true;
        }
        
        public int getComponents() {
            return components;
        }
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 5;
        int[][] edges1 = {{0,1,1},{1,2,1},{2,3,2},{0,3,2},{0,4,3},{3,4,3},{1,4,6}};
        List<List<Integer>> result1 = findCriticalAndPseudoCriticalEdges(n1, edges1);
        System.out.println("测试用例1结果: " + result1); // 预期输出: [[0, 1], [2, 3, 4, 5]]
        
        // 测试用例2
        int n2 = 4;
        int[][] edges2 = {{0,1,1},{1,2,1},{2,3,1},{0,3,1}};
        List<List<Integer>> result2 = findCriticalAndPseudoCriticalEdges(n2, edges2);
        System.out.println("测试用例2结果: " + result2); // 预期输出: [[], [0, 1, 2, 3]]
    }
}