package class064;

import java.util.*;

/**
 * 次小生成树
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P4180
 * 
 * 题目描述：
 * 给定一个包含 N 个点、M 条边的无向图，节点编号为 1~N。
 * 求该图的次小生成树的权值和。
 * 
 * 解题思路：
 * 次小生成树是指权值和严格大于最小生成树的最小生成树。
 * 我们采用以下策略：
 * 1. 首先使用Kruskal算法求出最小生成树(MST)
 * 2. 然后枚举每条不在MST中的边，将其加入MST中会形成一个环
 * 3. 在环中找到权值最大的边并删除，形成一个新的生成树
 * 4. 在所有可能的新生成树中找到权值最小的作为次小生成树
 * 
 * 算法应用场景：
 * - 网络设计中的备用方案
 * - 交通规划中的备选路线
 * - 图论中的优化问题
 * 
 * 时间复杂度分析：
 * O(E log E + V^2) 其中V是节点数，E是边数
 * 
 * 空间复杂度分析：
 * O(V^2) 存储图和路径信息
 */
public class Code12_SecondMinimumSpanningTree {

    /**
     * 并查集类
     * 用于Kruskal算法中检测环和维护连通性
     */
    static class UnionFind {
        int[] parent;  // parent[i]表示节点i的父节点
        int[] rank;    // rank[i]表示以i为根的树的高度（用于优化合并操作）
        
        /**
         * 构造函数
         * @param n 节点数量
         */
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            // 初始化：每个节点的父节点是自己，树高度为0
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }
        
        /**
         * 查找操作（带路径压缩优化）
         * @param x 节点
         * @return x所在集合的根节点
         */
        public int find(int x) {
            // 路径压缩：将查找路径上的所有节点直接连接到根节点
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        /**
         * 合并操作（按秩合并优化）
         * @param x 节点x
         * @param y 节点y
         * @return 如果合并成功返回true，如果已在同一集合返回false
         */
        public boolean union(int x, int y) {
            int rootX = find(x);  // x所在集合的根节点
            int rootY = find(y);  // y所在集合的根节点
            
            // 如果已在同一集合，无法合并
            if (rootX == rootY) {
                return false;
            }
            
            // 按秩合并：将高度较小的树连接到高度较大的树下
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                // 高度相同时，任选一个作为根，并将其高度加1
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }
    
    /**
     * 边类
     * 表示图中的一条边
     */
    static class Edge {
        int u, v, w;  // u和v是边的两个端点，w是边的权重
        
        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    /**
     * 使用Kruskal算法求解次小生成树
     * 
     * 算法核心思想：
     * 1. 首先使用Kruskal算法求出最小生成树
     * 2. 预处理计算MST中任意两点间路径上的最大边权和严格次大边权
     * 3. 枚举每条不在MST中的边，将其加入MST中会形成一个环
     * 4. 在环中找到合适的边删除，形成新的生成树
     * 5. 在所有可能的新生成树中找到权值最小的作为次小生成树
     * 
     * 算法步骤：
     * 1. 对所有边按权重排序
     * 2. 使用并查集构建最小生成树
     * 3. 预处理计算路径上的最大边权和次大边权
     * 4. 枚举非MST边，计算可能的新生成树权值
     * 5. 返回最小的新生成树权值
     * 
     * 时间复杂度: O(E log E + V^2) 其中V是节点数，E是边数
     * 空间复杂度: O(V^2)
     * 
     * @param n 节点数量
     * @param edges 边的信息，edges[i] = [起点, 终点, 权重]
     * @return 次小生成树的权值和，如果不存在返回-1
     */
    public static long secondMinimumSpanningTree(int n, int[][] edges) {
        // 将边按权重排序，为Kruskal算法做准备
        List<Edge> edgeList = new ArrayList<>();
        for (int[] edge : edges) {
            edgeList.add(new Edge(edge[0], edge[1], edge[2]));
        }
        // 按边权重从小到大排序
        Collections.sort(edgeList, (a, b) -> a.w - b.w);
        
        // 构建最小生成树
        UnionFind uf = new UnionFind(n + 1);  // 节点编号从1开始
        List<Edge> mstEdges = new ArrayList<>(); // MST中的边
        long mstWeight = 0;  // MST的权值和
        
        // Kruskal算法主循环
        for (Edge edge : edgeList) {
            // 如果连接两个不同连通分量，将边加入MST
            if (uf.union(edge.u, edge.v)) {
                mstEdges.add(edge);
                mstWeight += edge.w;
            }
        }
        
        // 构建MST的邻接表表示，用于后续DFS
        // mstGraph[i]存储节点i在MST中的所有邻居节点及其边权重
        List<List<int[]>> mstGraph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            mstGraph.add(new ArrayList<>());
        }
        
        // 构建MST的邻接表
        for (Edge edge : mstEdges) {
            // 无向图，需要添加两个方向的边
            mstGraph.get(edge.u).add(new int[]{edge.v, edge.w});
            mstGraph.get(edge.v).add(new int[]{edge.u, edge.w});
        }
        
        // 预处理：计算MST中任意两点间路径上的最大边权和严格次大边权
        // maxEdge[i][j]表示MST中从节点i到节点j路径上的最大边权
        int[][] maxEdge = new int[n + 1][n + 1];
        // secondMaxEdge[i][j]表示MST中从节点i到节点j路径上的严格次大边权
        int[][] secondMaxEdge = new int[n + 1][n + 1];
        
        // 对每个节点作为起点进行DFS，计算到其他节点的路径信息
        for (int i = 1; i <= n; i++) {
            // DFS计算从节点i出发到其他节点的路径上的最大边权和严格次大边权
            dfs(mstGraph, i, -1, i, 0, 0, maxEdge, secondMaxEdge);
        }
        
        // 寻找次小生成树
        long secondMstWeight = Long.MAX_VALUE;
        
        // 枚举每条边
        for (Edge edge : edgeList) {
            int u = edge.u;
            int v = edge.v;
            int w = edge.w;
            
            // 如果这条边不在MST中
            if (!isInMST(mstEdges, u, v)) {
                // 计算在MST中加入这条边后形成环，环上最大边权
                int maxInCycle = maxEdge[u][v];
                
                // 如果这条边的权重大于环上最大边权，则形成新的生成树
                if (w > maxInCycle) {
                    // 新生成树权值 = MST权值 + 新边权重 - 删除边权重
                    long newWeight = mstWeight + w - maxInCycle;
                    secondMstWeight = Math.min(secondMstWeight, newWeight);
                } 
                // 如果这条边的权重等于环上最大边权，则需要考虑环上次大边权
                else if (w == maxInCycle) {
                    int secondMaxInCycle = secondMaxEdge[u][v];
                    // 如果存在次大边权（不为0），则可以形成新的生成树
                    if (secondMaxInCycle != 0) {
                        // 新生成树权值 = MST权值 + 新边权重 - 删除边权重
                        long newWeight = mstWeight + w - secondMaxInCycle;
                        secondMstWeight = Math.min(secondMstWeight, newWeight);
                    }
                }
            }
        }
        
        // 返回次小生成树权值，如果不存在返回-1
        return secondMstWeight == Long.MAX_VALUE ? -1 : secondMstWeight;
    }
    
    /**
     * DFS计算路径上的最大边权和严格次大边权
     * 
     * @param graph MST的邻接表表示
     * @param start 起始节点
     * @param parent 父节点（避免回溯）
     * @param current 当前节点
     * @param maxW 当前路径上的最大边权
     * @param secondMaxW 当前路径上的严格次大边权
     * @param maxEdge 存储最大边权的数组
     * @param secondMaxEdge 存储次大边权的数组
     */
    private static void dfs(List<List<int[]>> graph, int start, int parent, int current, 
                           int maxW, int secondMaxW, int[][] maxEdge, int[][] secondMaxEdge) {
        // 记录从start到current的路径上的最大边权和次大边权
        maxEdge[start][current] = maxW;
        secondMaxEdge[start][current] = secondMaxW;
        
        // 遍历当前节点的所有邻居
        for (int[] edge : graph.get(current)) {
            int next = edge[0];  // 邻居节点
            int w = edge[1];     // 边的权重
            
            // 避免回到父节点
            if (next != parent) {
                // 更新路径上的最大边权和次大边权
                int newMaxW = maxW;
                int newSecondMaxW = secondMaxW;
                
                // 如果当前边权重更大，更新最大和次大边权
                if (w > maxW) {
                    newSecondMaxW = maxW;  // 原最大变为次大
                    newMaxW = w;           // 当前边权重变为最大
                } else if (w > secondMaxW && w != maxW) {
                    // 如果当前边权重大于次大且不等于最大，更新次大边权
                    newSecondMaxW = w;
                }
                
                // 递归处理邻居节点
                dfs(graph, start, current, next, newMaxW, newSecondMaxW, maxEdge, secondMaxEdge);
            }
        }
    }
    
    /**
     * 判断边是否在MST中
     * 
     * @param mstEdges MST中的边列表
     * @param u 边的一个端点
     * @param v 边的另一个端点
     * @return 如果边在MST中返回true，否则返回false
     */
    private static boolean isInMST(List<Edge> mstEdges, int u, int v) {
        // 遍历MST中的所有边
        for (Edge edge : mstEdges) {
            // 由于是无向图，需要检查两个方向
            if ((edge.u == u && edge.v == v) || (edge.u == v && edge.v == u)) {
                return true;
            }
        }
        return false;
    }

    // 测试用例
    public static void main(String[] args) {
        // 示例
        // 输入: n = 4, edges = [[1, 2, 1], [1, 3, 1], [2, 4, 1], [3, 4, 1]]
        // 输出: 4
        // 解释: 最小生成树权值为3，次小生成树权值为4
        int n = 4;
        int[][] edges = {{1, 2, 1}, {1, 3, 1}, {2, 4, 1}, {3, 4, 1}};
        System.out.println(secondMinimumSpanningTree(n, edges)); // 输出: 4
    }
}