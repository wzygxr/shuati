package class061;

import java.util.*;

// POJ 1251 Jungle Roads
// 题目链接: http://poj.org/problem?id=1251
// 
// 题目描述:
// 在遥远的热带雨林中，有n个村庄，编号从A到Z（最多26个村庄）。
// 一些村庄之间有道路连接，但这些道路可能需要重建。
// 你的任务是重建一些道路，使得所有村庄都连通，并且重建成本最小。
//
// 输入格式:
// 每个测试用例以整数n（1<n<27）开始，表示村庄数量。
// 接下来n-1行描述每个村庄可以连接的道路：
// 第一行描述村庄A可以连接的道路，第二行描述村庄B可以连接的道路，以此类推。
// 每行的格式为：村庄名 道路数 目标村庄1 成本1 目标村庄2 成本2 ...
// 
// 解题思路:
// 这是一个标准的最小生成树问题。我们需要：
// 1. 将输入的村庄和道路信息转换为图的表示
// 2. 使用Kruskal或Prim算法计算最小生成树
// 3. 返回MST的总权重
//
// 时间复杂度: O(E * log E)，其中E是边数
// 空间复杂度: O(V + E)，其中V是顶点数，E是边数
// 是否为最优解: 是，这是解决该问题的标准方法
// 工程化考量:
// 1. 异常处理: 检查输入参数的有效性
// 2. 边界条件: 处理少于2个村庄的情况
// 3. 内存管理: 使用ArrayList存储边信息
// 4. 性能优化: 并查集的路径压缩和按秩合并优化

public class Code22_JungleRoads {
    
    public static int jungleRoads(int n, List<Edge> edges) {
        // 特殊情况：只有一个村庄
        if (n == 1) {
            return 0;
        }
        
        // 按权重排序
        Collections.sort(edges, (a, b) -> Integer.compare(a.weight, b.weight));
        
        // 使用并查集构建MST
        UnionFind uf = new UnionFind(n);
        int totalCost = 0;
        int edgesUsed = 0;
        
        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) {
                totalCost += edge.weight;
                edgesUsed++;
                // MST完成
                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }
        
        return totalCost;
    }
    
    // 边的结构体
    static class Edge {
        int u, v, weight;
        
        Edge(int u, int v, int weight) {
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
        // 输入：
        // 9
        // A 2 B 12 I 25
        // B 3 C 10 H 40 I 8
        // C 2 D 20 G 55
        // D 1 E 44
        // E 2 F 60 G 38
        // F 0
        // G 1 H 35
        // H 1 I 35
        //
        // 构建边列表
        List<Edge> edges1 = new ArrayList<>();
        // A-B:12, A-I:25
        edges1.add(new Edge(0, 1, 12));
        edges1.add(new Edge(0, 8, 25));
        // B-C:10, B-H:40, B-I:8
        edges1.add(new Edge(1, 2, 10));
        edges1.add(new Edge(1, 7, 40));
        edges1.add(new Edge(1, 8, 8));
        // C-D:20, C-G:55
        edges1.add(new Edge(2, 3, 20));
        edges1.add(new Edge(2, 6, 55));
        // D-E:44
        edges1.add(new Edge(3, 4, 44));
        // E-F:60, E-G:38
        edges1.add(new Edge(4, 5, 60));
        edges1.add(new Edge(4, 6, 38));
        // G-H:35
        edges1.add(new Edge(6, 7, 35));
        // H-I:35
        edges1.add(new Edge(7, 8, 35));
        
        int result1 = jungleRoads(9, edges1);
        System.out.println("测试用例1结果: " + result1); // 预期输出: 216
    }
}