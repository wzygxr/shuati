package class079;

import java.util.*;

/**
 * 树形DP综合应用 - 高级题目与竞赛级别实现
 * 包含虚树构建、复杂状态设计、多约束条件等高级技术
 * 
 * 题目来源：Codeforces, AtCoder, 洛谷高级题目等
 * 算法类型：虚树DP、多状态DP、组合优化等
 * 
 * 相关题目:
 * 1. https://codeforces.com/contest/1187/problem/E (Tree Painting)
 * 2. https://codeforces.com/contest/1324/problem/F (Maximum White Subtree)
 * 3. https://atcoder.jp/contests/abc160/tasks/abc160_f (Distributing Integers)
 * 4. https://www.luogu.com.cn/problem/P2495 (最小消耗)
 * 5. https://www.luogu.com.cn/problem/P3246 (序列)
 */
public class Code14_TreeDPComprehensive {
    
    /**
     * 1. 虚树构建与应用
     * 问题描述：给定关键点集合，构建包含这些关键点的最小连通子图（虚树）
     * 应用场景：大规模树上多次查询的优化
     * 时间复杂度: O(k log k), 空间复杂度: O(k)
     */
    public static class VirtualTree {
        private List<List<Integer>> graph;
        private int[] depth;
        private int[][] parent;
        private int[] dfn;
        private int timer;
        private int n, log;
        
        public VirtualTree(List<List<Integer>> originalGraph) {
            this.n = originalGraph.size();
            this.graph = originalGraph;
            preprocess();
        }
        
        private void preprocess() {
            // 计算深度和DFS序
            depth = new int[n];
            dfn = new int[n];
            timer = 0;
            
            // 计算对数深度
            log = 1;
            while ((1 << log) < n) log++;
            parent = new int[n][log];
            
            dfsLCA(0, -1);
        }
        
        private void dfsLCA(int u, int p) {
            dfn[u] = timer++;
            parent[u][0] = p;
            for (int i = 1; i < log; i++) {
                if (parent[u][i-1] != -1) {
                    parent[u][i] = parent[parent[u][i-1]][i-1];
                } else {
                    parent[u][i] = -1;
                }
            }
            
            for (int v : graph.get(u)) {
                if (v != p) {
                    depth[v] = depth[u] + 1;
                    dfsLCA(v, u);
                }
            }
        }
        
        private int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            for (int i = log-1; i >= 0; i--) {
                if (depth[u] - (1 << i) >= depth[v]) {
                    u = parent[u][i];
                }
            }
            
            if (u == v) return u;
            
            for (int i = log-1; i >= 0; i--) {
                if (parent[u][i] != parent[v][i]) {
                    u = parent[u][i];
                    v = parent[v][i];
                }
            }
            
            return parent[u][0];
        }
        
        public List<List<Integer>> buildVirtualTree(List<Integer> keyPoints) {
            // 按DFS序排序关键点
            keyPoints.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
            
            // 添加LCA节点
            Set<Integer> virtualNodes = new HashSet<>(keyPoints);
            for (int i = 1; i < keyPoints.size(); i++) {
                virtualNodes.add(lca(keyPoints.get(i-1), keyPoints.get(i)));
            }
            
            List<Integer> sortedNodes = new ArrayList<>(virtualNodes);
            sortedNodes.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
            
            // 构建虚树
            List<List<Integer>> virtualTree = new ArrayList<>();
            for (int i = 0; i < n; i++) virtualTree.add(new ArrayList<>());
            
            Stack<Integer> stack = new Stack<>();
            stack.push(sortedNodes.get(0));
            
            for (int i = 1; i < sortedNodes.size(); i++) {
                int u = sortedNodes.get(i);
                while (stack.size() > 1 && depth[stack.peek()] > depth[lca(stack.peek(), u)]) {
                    int v = stack.pop();
                    virtualTree.get(stack.peek()).add(v);
                }
                
                int lcaNode = lca(stack.peek(), u);
                if (stack.peek() != lcaNode) {
                    virtualTree.get(lcaNode).add(stack.peek());
                    stack.pop();
                    stack.push(lcaNode);
                }
                stack.push(u);
            }
            
            while (stack.size() > 1) {
                int v = stack.pop();
                virtualTree.get(stack.peek()).add(v);
            }
            
            return virtualTree;
        }
    }
    
    /**
     * 2. 树上最大权独立集（带多约束条件）
     * 问题描述：选择节点使得权重和最大，且满足多个约束条件
     * 算法要点：复杂状态设计的树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMaxWeightedIndependentSetWithConstraints(
            List<List<Integer>> graph, int[] weights, int[] constraints) {
        int n = graph.size();
        // dp[u][0]: 不选u, dp[u][1]: 选u
        // 增加约束条件处理
        int[][][] dp = new int[n][2][constraints.length + 1];
        boolean[] visited = new boolean[n];
        
        dfsConstrainedMIS(0, -1, graph, weights, constraints, dp, visited);
        
        int maxResult = 0;
        for (int i = 0; i <= constraints.length; i++) {
            maxResult = Math.max(maxResult, Math.max(dp[0][0][i], dp[0][1][i]));
        }
        return maxResult;
    }
    
    private static void dfsConstrainedMIS(int u, int parent, List<List<Integer>> graph,
                                         int[] weights, int[] constraints,
                                         int[][][] dp, boolean[] visited) {
        visited[u] = true;
        
        // 初始化选择当前节点的情况
        for (int i = 0; i <= constraints.length; i++) {
            if (i >= weights[u]) {
                dp[u][1][i] = weights[u];
            }
        }
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                dfsConstrainedMIS(v, u, graph, weights, constraints, dp, visited);
                
                // 状态转移：考虑约束条件
                for (int i = constraints.length; i >= 0; i--) {
                    for (int j = 0; j <= i; j++) {
                        // 不选u时，v可选可不选
                        dp[u][0][i] = Math.max(dp[u][0][i], 
                            dp[u][0][i-j] + Math.max(dp[v][0][j], dp[v][1][j]));
                        
                        // 选u时，v不能选
                        if (i >= weights[u]) {
                            dp[u][1][i] = Math.max(dp[u][1][i],
                                dp[u][1][i-j] + dp[v][0][j]);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 3. 树上最小连通支配集
     * 问题描述：选择最少的节点形成连通子图，使得每个节点要么被选择，要么与某个被选节点相邻
     * 算法要点：连通性约束的树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMinConnectedDominatingSet(List<List<Integer>> graph) {
        int n = graph.size();
        // dp[u][0]: u未被覆盖, dp[u][1]: u被覆盖但不选, dp[u][2]: u被选择且连通
        int[][] dp = new int[n][3];
        boolean[] visited = new boolean[n];
        
        dfsConnectedDS(0, -1, graph, dp, visited);
        return Math.min(dp[0][1], dp[0][2]);
    }
    
    private static void dfsConnectedDS(int u, int parent, List<List<Integer>> graph,
                                      int[][] dp, boolean[] visited) {
        visited[u] = true;
        dp[u][2] = 1; // 选择当前节点
        
        int sumNotSelected = 0;
        boolean hasChild = false;
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                hasChild = true;
                dfsConnectedDS(v, u, graph, dp, visited);
                
                sumNotSelected += Math.min(dp[v][1], dp[v][2]);
                dp[u][2] += Math.min(dp[v][0], Math.min(dp[v][1], dp[v][2]));
            }
        }
        
        if (!hasChild) {
            // 叶子节点处理
            dp[u][0] = Integer.MAX_VALUE / 2;
            dp[u][1] = 0;
        } else {
            // 非叶子节点处理
            dp[u][0] = sumNotSelected;
            
            // 计算dp[u][1]: 至少有一个子节点被选择
            int minDiff = Integer.MAX_VALUE;
            for (int v : graph.get(u)) {
                if (v != parent) {
                    minDiff = Math.min(minDiff, dp[v][2] - Math.min(dp[v][1], dp[v][2]));
                }
            }
            dp[u][1] = sumNotSelected + minDiff;
        }
    }
    
    /**
     * 4. 树上路径覆盖问题
     * 问题描述：用最少的路径覆盖树的所有边，路径可以重叠
     * 算法要点：贪心 + 树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treePathCover(List<List<Integer>> graph) {
        int n = graph.size();
        int[] dp = new int[n]; // 以u为根的子树需要的最少路径数
        boolean[] visited = new boolean[n];
        
        dfsPathCover(0, -1, graph, dp, visited);
        return dp[0];
    }
    
    private static void dfsPathCover(int u, int parent, List<List<Integer>> graph,
                                    int[] dp, boolean[] visited) {
        visited[u] = true;
        
        int leafCount = 0;
        int sumDp = 0;
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                dfsPathCover(v, u, graph, dp, visited);
                sumDp += dp[v];
                
                if (graph.get(v).size() == 1) { // 叶子节点
                    leafCount++;
                }
            }
        }
        
        if (graph.get(u).size() == 1 && parent != -1) {
            // 叶子节点（非根）
            dp[u] = 1;
        } else {
            // 内部节点
            dp[u] = sumDp - Math.max(0, leafCount - 1);
        }
    }
    
    /**
     * 5. 树上最大匹配（Maximum Matching）
     * 问题描述：选择最多的不相交边
     * 算法要点：树形DP + 匹配理论
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMaximumMatching(List<List<Integer>> graph) {
        int n = graph.size();
        // dp[u][0]: u不参与匹配时的最大匹配数
        // dp[u][1]: u参与匹配时的最大匹配数
        int[][] dp = new int[n][2];
        boolean[] visited = new boolean[n];
        
        dfsMatching(0, -1, graph, dp, visited);
        return Math.max(dp[0][0], dp[0][1]);
    }
    
    private static void dfsMatching(int u, int parent, List<List<Integer>> graph,
                                   int[][] dp, boolean[] visited) {
        visited[u] = true;
        
        int sumNotMatched = 0;
        List<Integer> children = new ArrayList<>();
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                children.add(v);
                dfsMatching(v, u, graph, dp, visited);
                sumNotMatched += Math.max(dp[v][0], dp[v][1]);
            }
        }
        
        dp[u][0] = sumNotMatched;
        
        // 计算u参与匹配的情况
        int maxWithMatching = 0;
        for (int v : children) {
            // u与v匹配，其他子节点可以自由选择
            int current = 1 + dp[v][0]; // u与v匹配
            for (int w : children) {
                if (w != v) {
                    current += Math.max(dp[w][0], dp[w][1]);
                }
            }
            maxWithMatching = Math.max(maxWithMatching, current);
        }
        
        dp[u][1] = maxWithMatching;
    }
    
    /**
     * 6. 树上最小边覆盖
     * 问题描述：选择最少的边覆盖所有节点
     * 算法要点：树形DP，与最大匹配相关
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMinimumEdgeCover(List<List<Integer>> graph) {
        int n = graph.size();
        // 最小边覆盖 = 节点数 - 最大匹配
        int maxMatching = treeMaximumMatching(graph);
        return n - 1 - maxMatching;
    }
    
    /**
     * 7. 树上带权最大匹配
     * 问题描述：每条边有权重，选择权重和最大的不相交边集合
     * 算法要点：带权树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMaximumWeightedMatching(
            List<List<int[]>> weightedGraph) { // int[]{v, weight}
        int n = weightedGraph.size();
        // dp[u][0]: u不参与匹配的最大权重和
        // dp[u][1]: u参与匹配的最大权重和
        int[][] dp = new int[n][2];
        boolean[] visited = new boolean[n];
        
        dfsWeightedMatching(0, -1, weightedGraph, dp, visited);
        return Math.max(dp[0][0], dp[0][1]);
    }
    
    private static void dfsWeightedMatching(int u, int parent, 
                                           List<List<int[]>> weightedGraph,
                                           int[][] dp, boolean[] visited) {
        visited[u] = true;
        
        int sumNotMatched = 0;
        List<int[]> children = new ArrayList<>(); // int[]{v, weight}
        
        for (int[] edge : weightedGraph.get(u)) {
            int v = edge[0], weight = edge[1];
            if (v != parent && !visited[v]) {
                children.add(new int[]{v, weight});
                dfsWeightedMatching(v, u, weightedGraph, dp, visited);
                sumNotMatched += Math.max(dp[v][0], dp[v][1]);
            }
        }
        
        dp[u][0] = sumNotMatched;
        
        // 计算u参与匹配的情况
        int maxWithMatching = 0;
        for (int[] child : children) {
            int v = child[0], weight = child[1];
            // u与v匹配
            int current = weight + dp[v][0]; // u与v匹配的权重
            for (int[] other : children) {
                if (other[0] != v) {
                    current += Math.max(dp[other[0]][0], dp[other[0]][1]);
                }
            }
            maxWithMatching = Math.max(maxWithMatching, current);
        }
        
        dp[u][1] = maxWithMatching;
    }
    
    /**
     * 8. 树上最小斯坦纳树（Steiner Tree）
     * 问题描述：连接关键点的最小权重子树
     * 算法要点：状态压缩DP + 树形DP
     * 时间复杂度: O(3^k * n + 2^k * n²), 空间复杂度: O(2^k * n)
     */
    public static int treeSteinerTree(List<List<int[]>> graph, List<Integer> terminals) {
        int n = graph.size();
        int k = terminals.size();
        
        // 状态压缩：每个终端节点对应一个bit
        int[][] dp = new int[1 << k][n];
        
        // 初始化：单个终端节点
        for (int i = 0; i < k; i++) {
            int terminal = terminals.get(i);
            Arrays.fill(dp[1 << i], Integer.MAX_VALUE / 2);
            dp[1 << i][terminal] = 0;
        }
        
        // 状态转移
        for (int mask = 1; mask < (1 << k); mask++) {
            // 子树合并
            for (int u = 0; u < n; u++) {
                for (int submask = (mask - 1) & mask; submask > 0; submask = (submask - 1) & mask) {
                    dp[mask][u] = Math.min(dp[mask][u], 
                        dp[submask][u] + dp[mask ^ submask][u]);
                }
            }
            
            // Dijkstra-like relaxation
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            for (int u = 0; u < n; u++) {
                if (dp[mask][u] < Integer.MAX_VALUE / 2) {
                    pq.offer(new int[]{u, dp[mask][u]});
                }
            }
            
            while (!pq.isEmpty()) {
                int[] curr = pq.poll();
                int u = curr[0], dist = curr[1];
                if (dist > dp[mask][u]) continue;
                
                for (int[] edge : graph.get(u)) {
                    int v = edge[0], weight = edge[1];
                    int newDist = dist + weight;
                    if (newDist < dp[mask][v]) {
                        dp[mask][v] = newDist;
                        pq.offer(new int[]{v, newDist});
                    }
                }
            }
        }
        
        // 找到最小权重和
        int minCost = Integer.MAX_VALUE;
        for (int u = 0; u < n; u++) {
            minCost = Math.min(minCost, dp[(1 << k) - 1][u]);
        }
        
        return minCost;
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        // 测试虚树构建
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < 6; i++) graph.add(new ArrayList<>());
        graph.get(0).add(1); graph.get(0).add(2);
        graph.get(1).add(0); graph.get(1).add(3); graph.get(1).add(4);
        graph.get(2).add(0); graph.get(2).add(5);
        graph.get(3).add(1); graph.get(4).add(1); graph.get(5).add(2);
        
        VirtualTree vt = new VirtualTree(graph);
        List<Integer> keyPoints = Arrays.asList(3, 4, 5);
        List<List<Integer>> virtualTree = vt.buildVirtualTree(keyPoints);
        
        System.out.println("虚树构建完成，节点数: " + virtualTree.size());
        
        // 测试最大匹配
        int maxMatching = treeMaximumMatching(graph);
        System.out.println("树上最大匹配数: " + maxMatching);
        
        // 测试最小边覆盖
        int minEdgeCover = treeMinimumEdgeCover(graph);
        System.out.println("树上最小边覆盖: " + minEdgeCover);
    }
}