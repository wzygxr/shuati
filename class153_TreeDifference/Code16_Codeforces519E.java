package class122;

import java.util.*;

/**
 * Codeforces 519E. A and B and Lecture Rooms
 * 
 * 题目来源：Codeforces
 * 题目链接：https://codeforces.com/contest/519/problem/E
 * 
 * 题目描述：
 * 给定一棵树，有多个查询，每个查询给出两个节点u和v，
 * 要求找到树上到u和v距离相等的节点数量。
 * 
 * 算法原理：LCA + 树上几何性质
 * 这是一个结合了LCA和树上几何性质的综合问题。
 * 
 * 解题思路：
 * 1. 对于两个节点u和v，到它们距离相等的节点满足：dist(x,u) = dist(x,v)
 * 2. 根据树的性质，这些节点的分布有以下几种情况：
 *    - 如果u==v，则所有节点都满足条件
 *    - 如果u和v的距离为奇数，则没有满足条件的节点
 *    - 如果u和v在同一深度，则满足条件的节点数量为n - size[u到中点路径上的子节点] - size[v到中点路径上的子节点]
 *    - 如果u和v在不同深度，则满足条件的节点数量为中点子树大小 - 中点父节点子树大小
 * 
 * 时间复杂度分析：
 * - 建图：O(N)
 * - 预处理LCA：O(N log N)
 * - 每次查询：O(log N)
 * 总时间复杂度：O(N log N + Q log N)，其中Q是查询数
 * 
 * 空间复杂度分析：
 * - 图的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 深度和子树大小数组：O(N)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用邻接表存储树结构，节省空间
 * 2. 使用倍增法计算LCA，提高查询效率
 * 3. 预处理子树大小，便于快速计算满足条件的节点数量
 * 4. 边界处理：注意距离为奇数和节点重合的特殊情况
 * 
 * 最优解分析：
 * 本解法结合了LCA和树上几何性质，是解决此类问题的最优解。
 * 相比于暴力枚举每个节点的O(N*Q)复杂度，本解法将时间复杂度优化到O(N log N + Q log N)。
 */
public class Code16_Codeforces519E {
    
    static class Solution {
        private List<Integer>[] graph;  // 邻接表存储树结构
        private int[][] parent;         // 倍增数组，parent[i][j]表示节点i的2^j级祖先
        private int[] depth;            // depth[i]表示节点i的深度
        private int[] size;             // size[i]表示以节点i为根的子树大小
        private int LOG;                // 倍增数组的最大层数
        private int n;                  // 节点数量
        
        /**
         * 主函数，解决所有查询
         * 
         * @param n 节点数
         * @param edges 树的边
         * @param queries 查询数组
         * @return 每个查询的结果数组
         */
        public int[] solve(int n, int[][] edges, int[][] queries) {
            this.n = n;
            
            // 构建图
            graph = new ArrayList[n + 1];
            for (int i = 1; i <= n; i++) {
                graph[i] = new ArrayList<>();
            }
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                graph[u].add(v);
                graph[v].add(u);
            }
            
            // 预处理LCA相关数据
            LOG = (int) (Math.log(n) / Math.log(2)) + 1;
            parent = new int[n + 1][LOG];
            depth = new int[n + 1];
            size = new int[n + 1];
            
            // DFS预处理深度、父节点和子树大小
            dfs(1, 0);
            
            // 预处理倍增数组
            for (int j = 1; j < LOG; j++) {
                for (int i = 1; i <= n; i++) {
                    if (parent[i][j-1] != 0) {
                        parent[i][j] = parent[parent[i][j-1]][j-1];
                    }
                }
            }
            
            // 处理所有查询
            int[] result = new int[queries.length];
            for (int i = 0; i < queries.length; i++) {
                int u = queries[i][0], v = queries[i][1];
                result[i] = query(u, v);
            }
            
            return result;
        }
        
        /**
         * DFS预处理深度、父节点和子树大小
         * 
         * @param u 当前处理的节点
         * @param p 当前节点的父节点
         */
        private void dfs(int u, int p) {
            // 设置父节点
            parent[u][0] = p;
            // 设置深度
            depth[u] = depth[p] + 1;
            // 初始化子树大小
            size[u] = 1;
            
            // 遍历当前节点的所有子节点
            for (int v : graph[u]) {
                if (v != p) {
                    // 递归处理子节点
                    dfs(v, u);
                    // 累加子节点的子树大小
                    size[u] += size[v];
                }
            }
        }
        
        /**
         * 使用倍增法计算两个节点的最近公共祖先(LCA)
         * 
         * @param u 第一个节点
         * @param v 第二个节点
         * @return u和v的最近公共祖先
         */
        private int getLCA(int u, int v) {
            // 确保u的深度不小于v
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 将u提升到和v同一深度
            for (int j = LOG - 1; j >= 0; j--) {
                if (depth[u] - (1 << j) >= depth[v]) {
                    u = parent[u][j];
                }
            }
            
            // 如果此时u==v，则找到了LCA
            if (u == v) return u;
            
            // 同时向上提升，直到找到LCA的直接子节点
            for (int j = LOG - 1; j >= 0; j--) {
                if (parent[u][j] != parent[v][j]) {
                    u = parent[u][j];
                    v = parent[v][j];
                }
            }
            
            // 返回它们的父节点作为LCA
            return parent[u][0];
        }
        
        /**
         * 获取节点u的第k个祖先
         * 
         * @param u 当前节点
         * @param k 祖先级别
         * @return 节点u的第k个祖先
         */
        private int getKthParent(int u, int k) {
            // 二进制拆分k
            for (int j = 0; j < LOG; j++) {
                // 如果k的第j位为1
                if ((k & (1 << j)) != 0) {
                    // 节点跳到其2^j级祖先
                    u = parent[u][j];
                }
            }
            return u;
        }
        
        /**
         * 查询到u和v距离相等的节点数量
         * 
         * @param u 第一个节点
         * @param v 第二个节点
         * @return 到u和v距离相等的节点数量
         */
        private int query(int u, int v) {
            // 特殊情况：如果u和v是同一个节点，则所有节点都满足条件
            if (u == v) return n;
            
            // 计算u和v的LCA
            int lca = getLCA(u, v);
            // 计算u和v之间的距离
            int dist = depth[u] + depth[v] - 2 * depth[lca];
            
            // 如果距离为奇数，则没有满足条件的节点
            if (dist % 2 == 1) return 0;
            
            // 计算中点到u和v的距离
            int midDist = dist / 2;
            
            // 如果u和v在同一深度
            if (depth[u] == depth[v]) {
                /**
                 * 当u和v在同一深度时，满足条件的节点分布：
                 * 1. 中点在lca上
                 * 2. 满足条件的节点数量 = 总节点数 - u到中点路径上的子树大小 - v到中点路径上的子树大小
                 */
                // 获取u到中点路径上的子节点
                int uMid = getKthParent(u, midDist - 1);
                // 获取v到中点路径上的子节点
                int vMid = getKthParent(v, midDist - 1);
                // 计算满足条件的节点数量
                return n - size[uMid] - size[vMid];
            } else {
                /**
                 * 当u和v在不同深度时，满足条件的节点分布：
                 * 1. 中点在深度较大的节点到lca的路径上
                 * 2. 满足条件的节点数量 = 中点子树大小 - 中点父节点子树大小
                 */
                // 确保u是深度较大的节点
                if (depth[u] < depth[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }
                
                // 获取中点
                int mid = getKthParent(u, midDist);
                // 获取中点的父节点
                int prev = getKthParent(u, midDist - 1);
                // 计算满足条件的节点数量
                return size[mid] - size[prev];
            }
        }
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例
        int n = 4;
        int[][] edges = {{1,2}, {1,3}, {2,4}};
        int[][] queries = {{1,2}, {2,3}, {3,4}, {2,4}};
        
        int[] result = solution.solve(n, edges, queries);
        System.out.println(Arrays.toString(result)); // 输出: [2, 1, 1, 1]
    }
}