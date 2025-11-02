package class122;

import java.util.*;

/**
 * LeetCode 2646. 最小化旅行的价格
 * 
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/minimize-the-total-price-of-the-trips/
 * 
 * 题目描述：
 * 给定一棵树，每个节点有一个价格。可以选择将某些节点的价格减半（但相邻节点不能同时减半）。
 * 有多个旅行路径，每个路径从u到v。要求最小化所有旅行路径的总价格。
 * 
 * 算法原理：树上差分 + 树形DP
 * 1. 使用树上差分统计每条路径经过的节点次数
 * 2. 使用树形DP决策哪些节点减半以最小化总价格
 * 
 * 解题思路：
 * 1. 首先统计每条路径经过的节点次数（使用树上差分技术）
 * 2. 然后使用树形DP，在满足相邻节点不能同时减半的约束下，
 *    决策哪些节点减半以最小化总价格
 * 
 * 时间复杂度分析：
 * - 建图：O(N)
 * - 预处理LCA：O(N log N)
 * - 树上差分统计：O(M log N)，其中M是旅行路径数
 * - DFS统计节点次数：O(N)
 * - 树形DP：O(N)
 * 总时间复杂度：O(N log N + M log N)
 * 
 * 空间复杂度分析：
 * - 图的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 差分数组：O(N)
 * - DP数组：O(N)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用邻接表存储树结构，节省空间
 * 2. 使用BFS预处理深度和父节点，避免递归栈溢出
 * 3. 使用倍增法计算LCA，提高查询效率
 * 4. 树形DP状态设计清晰，便于理解和维护
 * 
 * 最优解分析：
 * 本解法结合了树上差分和树形DP，是解决此类问题的最优解。
 * 相比于暴力遍历每条路径的O(N*M)复杂度，树上差分可以将统计时间优化到O(M log N)。
 * 树形DP在O(N)时间内完成最优决策，整体效率很高。
 */
public class Code15_LeetCode2646 {
    
    static class Solution {
        private List<Integer>[] graph;  // 邻接表存储树结构
        private int[] price;            // 每个节点的价格
        private int[] count;            // 每个节点被经过的次数
        private int[][] dp;             // dp[u][0]:节点u不减半的最小价格, dp[u][1]:节点u减半的最小价格
        
        /**
         * 主函数，计算最小化旅行价格的总和
         * 
         * @param n 节点数
         * @param edges 树的边
         * @param price 每个节点的价格
         * @param trips 旅行路径
         * @return 最小化旅行价格的总和
         */
        public int minimumTotalPrice(int n, int[][] edges, int[] price, int[][] trips) {
            // 构建图
            graph = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                graph[i] = new ArrayList<>();
            }
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                graph[u].add(v);
                graph[v].add(u);
            }
            
            this.price = price;
            count = new int[n];  // 初始化节点被经过次数数组
            
            // 预处理LCA相关数据
            int LOG = 20;  // 倍增数组的大小，2^20足够处理1e5规模的树
            int[][] parent = new int[n][LOG];  // parent[i][j]表示节点i的2^j级祖先
            int[] depth = new int[n];          // depth[i]表示节点i的深度
            
            // BFS预处理深度和父节点
            Arrays.fill(depth, -1);
            depth[0] = 0;
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(0);
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : graph[u]) {
                    if (depth[v] == -1) {
                        depth[v] = depth[u] + 1;
                        parent[v][0] = u;  // 设置直接父节点
                        queue.offer(v);
                    }
                }
            }
            
            // 预处理倍增数组
            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i < n; i++) {
                    if (parent[i][j-1] != -1) {
                        parent[i][j] = parent[parent[i][j-1]][j-1];
                    }
                }
            }
            
            // 树上差分统计每条路径经过的节点次数
            for (int[] trip : trips) {
                int u = trip[0], v = trip[1];
                // 计算u和v的最近公共祖先
                int lca = getLCA(u, v, parent, depth, LOG);
                
                /**
                 * 树上点差分核心操作：
                 * 对于路径u->v，我们需要让路径上的所有节点计数加1
                 * 通过点差分技巧，我们只需要修改三个点：
                 * 1. count[u]++ - 在起点增加标记
                 * 2. count[v]++ - 在终点增加标记
                 * 3. count[lca] -= 2 - 在LCA处抵消多余的标记
                 * 
                 * 这样，当执行dfsCount回溯累分时，整个路径上的节点都会被正确计数
                 */
                count[u]++;
                count[v]++;
                count[lca] -= 2;
            }
            
            // DFS统计每个节点被经过的次数
            dfsCount(0, -1);
            
            // 树形DP决策哪些节点减半
            dp = new int[n][2];
            dfsDP(0, -1);
            
            // 返回根节点减半或不减半的最小值
            return Math.min(dp[0][0], dp[0][1]);
        }
        
        /**
         * DFS统计每个节点被经过的次数
         * 通过回溯累加子节点的差分标记，计算每个节点的最终经过次数
         * 
         * @param u 当前处理的节点
         * @param parent 当前节点的父节点
         */
        private void dfsCount(int u, int parent) {
            // 遍历当前节点的所有子节点
            for (int v : graph[u]) {
                if (v != parent) {
                    // 递归处理子节点
                    dfsCount(v, u);
                    // 将子节点的经过次数累加到当前节点
                    count[u] += count[v];
                }
            }
        }
        
        /**
         * 树形DP，决策哪些节点减半以最小化总价格
         * 状态转移：
         * dp[u][0] = price[u] * count[u] + Σmin(dp[v][0], dp[v][1])  // 当前节点不减半
         * dp[u][1] = (price[u]/2) * count[u] + Σdp[v][0]            // 当前节点减半
         * 
         * 约束条件：相邻节点不能同时减半
         * 
         * @param u 当前处理的节点
         * @param parent 当前节点的父节点
         */
        private void dfsDP(int u, int parent) {
            // 不减半的情况：当前节点价格不变
            dp[u][0] = price[u] * count[u];
            // 减半的情况：当前节点价格减半
            dp[u][1] = (price[u] / 2) * count[u];
            
            // 遍历当前节点的所有子节点
            for (int v : graph[u]) {
                if (v != parent) {
                    // 递归处理子节点
                    dfsDP(v, u);
                    
                    /**
                     * 状态转移方程：
                     * 1. 当前节点不减半，子节点可以减半或不减半，选择最小值
                     * 2. 当前节点减半，子节点不能减半（约束条件）
                     */
                    // 当前节点不减半，子节点可以减半或不减半
                    dp[u][0] += Math.min(dp[v][0], dp[v][1]);
                    // 当前节点减半，子节点不能减半
                    dp[u][1] += dp[v][0];
                }
            }
        }
        
        /**
         * 使用倍增法计算两个节点的最近公共祖先(LCA)
         * 
         * @param u 第一个节点
         * @param v 第二个节点
         * @param parent 倍增父节点数组
         * @param depth 深度数组
         * @param LOG 倍增数组大小
         * @return u和v的最近公共祖先
         */
        private int getLCA(int u, int v, int[][] parent, int[] depth, int LOG) {
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
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int n1 = 4;
        int[][] edges1 = {{0,1},{1,2},{1,3}};
        int[] price1 = {2,2,10,6};
        int[][] trips1 = {{0,3},{2,1},{2,3}};
        System.out.println(solution.minimumTotalPrice(n1, edges1, price1, trips1)); // 输出: 23
        
        // 测试用例2
        int n2 = 2;
        int[][] edges2 = {{0,1}};
        int[] price2 = {2,2};
        int[][] trips2 = {{0,0}};
        System.out.println(solution.minimumTotalPrice(n2, edges2, price2, trips2)); // 输出: 1
    }
}