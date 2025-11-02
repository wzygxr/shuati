package class119;

import java.util.*;

/**
 * LeetCode 2846. 边权重均等查询
 * 题目描述：给定一棵无权树，每条边有一个权值（1-26之间的整数），查询两个节点之间的路径上
 * 需要修改多少次边权才能使路径上的所有边权相等
 * 
 * 最优解算法：树上倍增 + 路径信息统计
 * 时间复杂度：预处理 O(n log n * 26)，单次查询 O(log n)
 * 空间复杂度：O(n log n * 26)
 * 
 * 解题思路：
 * 1. 使用树上倍增算法预处理每个节点到其祖先路径上各种权重的边数
 * 2. 对于每次查询，找到两点的LCA
 * 3. 通过路径分解计算查询路径上各种权重的边数
 * 4. 找出出现次数最多的权重，其他权重的边都需要修改
 */
public class Code07_MinOperationsQueries {
    /**
     * MinOperationsQueries 类实现边权重均等查询
     */
    public static class MinOperationsQueries {
        private int n;                // 节点数量
        private int LOG;              // 最大跳步级别
        private int[][] parent;       // parent[j][u] 表示u的2^j级祖先
        private int[] depth;          // 每个节点的深度
        private int[][][] cnt;        // cnt[j][u][k] 表示u到2^j级祖先路径上权值为k+1的边数
        private List<List<int[]>> adj; // 邻接表，存储树结构

        /**
         * 计算两个节点之间路径上最少需要修改多少次边权才能使所有边权相等
         * @param n 节点数量
         * @param edges 边数组，每个元素为 [u, v, w]
         * @param queries 查询数组，每个元素为 [u, v]
         * @return 每个查询的最小修改次数
         */
        public int[] minOperationsQueries(int n, int[][] edges, int[][] queries) {
            this.n = n;
            // 计算最大跳步级别
            this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
            
            // 初始化数据结构
            parent = new int[LOG][n];
            depth = new int[n];
            cnt = new int[LOG][n][26]; // 权值范围是1-26，所以数组大小为26
            
            // 构建邻接表
            adj = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                int w = edge[2] - 1; // 将权值调整为0-25范围，方便数组索引
                adj.get(u).add(new int[]{v, w});
                adj.get(v).add(new int[]{u, w});
            }
            
            // 初始化父数组和计数数组
            for (int i = 0; i < LOG; i++) {
                Arrays.fill(parent[i], -1);
            }
            
            // 深度优先搜索预处理
            dfs(0, -1, 0);
            
            // 构建倍增表
            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i < n; i++) {
                    if (parent[j-1][i] != -1) {
                        parent[j][i] = parent[j-1][parent[j-1][i]];
                        // 合并两个跳跃段的计数信息
                        for (int k = 0; k < 26; k++) {
                            cnt[j][i][k] = cnt[j-1][i][k] + cnt[j-1][parent[j-1][i]][k];
                        }
                    }
                }
            }
            
            // 处理查询
            int[] result = new int[queries.length];
            for (int i = 0; i < queries.length; i++) {
                int u = queries[i][0];
                int v = queries[i][1];
                result[i] = query(u, v);
            }
            
            return result;
        }
        
        /**
         * 深度优先搜索预处理每个节点的父节点、深度和到父节点的边权计数
         * @param u 当前节点
         * @param p 父节点
         * @param d 当前深度
         */
        private void dfs(int u, int p, int d) {
            parent[0][u] = p;
            depth[u] = d;
            
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (v != p) {
                    // 直接连接的边的权值计数
                    cnt[0][v][w] = 1;
                    dfs(v, u, d + 1);
                }
            }
        }
        
        /**
         * 查找两个节点的最近公共祖先
         * @param u 节点u
         * @param v 节点v
         * @return 最近公共祖先
         */
        private int lca(int u, int v) {
            // 先将较深的节点提升到同一深度
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 将u提升到v的深度
            for (int j = LOG - 1; j >= 0; j--) {
                if (depth[u] - (1 << j) >= depth[v]) {
                    u = parent[j][u];
                }
            }
            
            if (u == v) {
                return u;
            }
            
            // 同时提升两个节点，直到找到共同祖先
            for (int j = LOG - 1; j >= 0; j--) {
                if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                    u = parent[j][u];
                    v = parent[j][v];
                }
            }
            
            return parent[0][u];
        }
        
        /**
         * 统计从节点u到其某个祖先路径上各权值的边数
         * @param u 起始节点
         * @param ancestor 祖先节点
         * @return 权值计数数组
         */
        private int[] getCount(int u, int ancestor) {
            int[] res = new int[26];
            
            for (int j = LOG - 1; j >= 0; j--) {
                if (depth[u] - (1 << j) >= depth[ancestor]) {
                    for (int k = 0; k < 26; k++) {
                        res[k] += cnt[j][u][k];
                    }
                    u = parent[j][u];
                }
            }
            
            return res;
        }
        
        /**
         * 处理单个查询，计算路径上的最小修改次数
         * @param u 起始节点
         * @param v 终止节点
         * @return 最小修改次数
         */
        private int query(int u, int v) {
            int ancestor = lca(u, v);
            
            // 获取u到LCA的权值计数
            int[] cntU = getCount(u, ancestor);
            // 获取v到LCA的权值计数
            int[] cntV = getCount(v, ancestor);
            
            // 合并计数
            int[] total = new int[26];
            for (int k = 0; k < 26; k++) {
                total[k] = cntU[k] + cntV[k];
            }
            
            // 计算路径总长度
            int pathLength = depth[u] + depth[v] - 2 * depth[ancestor];
            
            // 找出出现次数最多的权值
            int maxCount = 0;
            for (int count : total) {
                maxCount = Math.max(maxCount, count);
            }
            
            // 最小修改次数 = 总边数 - 最多出现次数
            return pathLength - maxCount;
        }
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        MinOperationsQueries solver = new MinOperationsQueries();
        
        // 示例测试
        int n1 = 7;
        int[][] edges1 = {
            {0, 1, 1},
            {1, 2, 1},
            {2, 3, 1},
            {3, 4, 2},
            {4, 5, 2},
            {5, 6, 2}
        };
        int[][] queries1 = {
            {0, 3},
            {3, 6},
            {2, 6},
            {0, 6}
        };
        
        int[] results1 = solver.minOperationsQueries(n1, edges1, queries1);
        System.out.println("示例1结果:");
        for (int res : results1) {
            System.out.print(res + " ");
        }
        System.out.println(); // 预期输出: 0 0 1 3
        
        // 另一个测试用例
        int n2 = 3;
        int[][] edges2 = {
            {0, 1, 4},
            {1, 2, 4}
        };
        int[][] queries2 = {
            {0, 2}
        };
        
        int[] results2 = solver.minOperationsQueries(n2, edges2, queries2);
        System.out.println("示例2结果:");
        for (int res : results2) {
            System.out.print(res + " ");
        }
        System.out.println(); // 预期输出: 0
    }
    
    /**
     * 算法优化与工程化考量：
     * 1. 权值映射：将1-26的权值映射到0-25，提高数组访问效率
     * 2. 多维数组设计：cnt[j][u][k]设计充分利用空间局部性
     * 3. 预处理优化：一次性预处理所有信息，支持快速查询
     * 4. 路径分解：将u-v路径分解为u-LCA和v-LCA两段处理
     * 5. 空间优化：使用26大小的数组存储权值计数，适合题目约束
     * 
     * 异常场景与边界场景：
     * - u和v是同一个节点的情况（修改次数为0）
     * - 路径上所有边权都相同的情况（修改次数为0）
     * - 路径上各边权都不同的情况（需要修改次数为边数-1）
     * - 树退化成链表的极端情况
     */
}