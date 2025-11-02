package class079;

import java.util.*;

/**
 * 高级树形DP综合应用
 * 包含多个竞赛级别树形DP问题的解决方案
 * 
 * 时间复杂度分析：
 * - 基础树形DP: O(n)
 * - 换根DP: O(n) 
 * - 树形背包: O(n*m)
 * - 虚树DP: O(k log k)
 * 
 * 空间复杂度分析：
 * - 递归栈: O(h)
 * - DP数组: O(n)
 * - 图存储: O(n)
 */
public class Code12_AdvancedTreeDP {
    
    /**
     * 1. 树的最大独立集（Maximum Independent Set）
     * 问题描述：在树中选择最多的节点，使得任意两个被选节点都不相邻
     * 算法要点：树形DP，状态设计为选择/不选择当前节点
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMaxIndependentSet(List<List<Integer>> graph) {
        int n = graph.size();
        int[][] dp = new int[n][2]; // dp[u][0]: 不选u, dp[u][1]: 选u
        boolean[] visited = new boolean[n];
        
        dfsMIS(0, -1, graph, dp, visited);
        return Math.max(dp[0][0], dp[0][1]);
    }
    
    private static void dfsMIS(int u, int parent, List<List<Integer>> graph, int[][] dp, boolean[] visited) {
        visited[u] = true;
        dp[u][1] = 1; // 选择当前节点
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                dfsMIS(v, u, graph, dp, visited);
                // 不选u时，v可选可不选
                dp[u][0] += Math.max(dp[v][0], dp[v][1]);
                // 选u时，v不能选
                dp[u][1] += dp[v][0];
            }
        }
    }
    
    /**
     * 2. 树的最小顶点覆盖（Minimum Vertex Cover）
     * 问题描述：选择最少的节点，使得每条边至少有一个端点被选择
     * 算法要点：树形DP，状态转移与最大独立集相关
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMinVertexCover(List<List<Integer>> graph) {
        int n = graph.size();
        int[][] dp = new int[n][2]; // dp[u][0]: u不被覆盖, dp[u][1]: u被覆盖
        boolean[] visited = new boolean[n];
        
        dfsMVC(0, -1, graph, dp, visited);
        return Math.min(dp[0][0], dp[0][1]);
    }
    
    private static void dfsMVC(int u, int parent, List<List<Integer>> graph, int[][] dp, boolean[] visited) {
        visited[u] = true;
        dp[u][1] = 1; // 选择当前节点
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                dfsMVC(v, u, graph, dp, visited);
                // u不被覆盖时，v必须被覆盖
                dp[u][0] += dp[v][1];
                // u被覆盖时，v可选可不选
                dp[u][1] += Math.min(dp[v][0], dp[v][1]);
            }
        }
    }
    
    /**
     * 3. 树的最小支配集（Minimum Dominating Set）
     * 问题描述：选择最少的节点，使得每个节点要么被选择，要么与某个被选节点相邻
     * 算法要点：复杂状态设计的树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeMinDominatingSet(List<List<Integer>> graph) {
        int n = graph.size();
        // dp[u][0]: u被选择, dp[u][1]: u未被选择但被父节点覆盖, dp[u][2]: u未被覆盖
        int[][] dp = new int[n][3];
        boolean[] visited = new boolean[n];
        
        dfsMDS(0, -1, graph, dp, visited);
        return Math.min(dp[0][0], dp[0][2]); // 根节点不能要求父节点覆盖
    }
    
    private static void dfsMDS(int u, int parent, List<List<Integer>> graph, int[][] dp, boolean[] visited) {
        visited[u] = true;
        dp[u][0] = 1; // 选择当前节点
        dp[u][1] = 0; // 被父节点覆盖
        dp[u][2] = 0; // 未被覆盖
        
        int minDiff = Integer.MAX_VALUE;
        boolean hasChild = false;
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                hasChild = true;
                dfsMDS(v, u, graph, dp, visited);
                
                // 状态转移
                dp[u][0] += Math.min(Math.min(dp[v][0], dp[v][1]), dp[v][2]);
                dp[u][1] += Math.min(dp[v][0], dp[v][2]);
                dp[u][2] += Math.min(dp[v][0], dp[v][2]);
                
                // 记录最小差值，用于处理dp[u][2]的特殊情况
                minDiff = Math.min(minDiff, dp[v][0] - Math.min(dp[v][0], dp[v][2]));
            }
        }
        
        // 如果没有子节点，调整状态值
        if (!hasChild) {
            dp[u][1] = 0;
            dp[u][2] = Integer.MAX_VALUE / 2; // 避免溢出
        } else {
            // 处理dp[u][2]：至少有一个子节点被选择
            if (minDiff != Integer.MAX_VALUE) {
                dp[u][2] += minDiff;
            } else {
                dp[u][2] = Integer.MAX_VALUE / 2;
            }
        }
    }
    
    /**
     * 4. 树的带权最大独立集
     * 问题描述：每个节点有权重，选择权重和最大的独立集
     * 算法要点：带权树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int treeWeightedMaxIndependentSet(List<List<Integer>> graph, int[] weights) {
        int n = graph.size();
        int[][] dp = new int[n][2]; // dp[u][0]: 不选u, dp[u][1]: 选u
        boolean[] visited = new boolean[n];
        
        dfsWMIS(0, -1, graph, weights, dp, visited);
        return Math.max(dp[0][0], dp[0][1]);
    }
    
    private static void dfsWMIS(int u, int parent, List<List<Integer>> graph, int[] weights, int[][] dp, boolean[] visited) {
        visited[u] = true;
        dp[u][1] = weights[u]; // 选择当前节点获得权重
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                dfsWMIS(v, u, graph, weights, dp, visited);
                // 不选u时，v可选可不选
                dp[u][0] += Math.max(dp[v][0], dp[v][1]);
                // 选u时，v不能选
                dp[u][1] += dp[v][0];
            }
        }
    }
    
    /**
     * 5. 树的k着色问题
     * 问题描述：用k种颜色给树染色，相邻节点颜色不同，求方案数
     * 算法要点：组合数学 + 树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static long treeKColoring(int n, int k, int[][] edges) {
        // 构建图
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        // DP计算
        long[][] dp = new long[n][2]; // dp[u][0]: u染特定颜色时的方案数
        boolean[] visited = new boolean[n];
        
        dfsColoring(0, -1, graph, k, dp, visited);
        return dp[0][0] * k; // 根节点有k种颜色选择
    }
    
    private static void dfsColoring(int u, int parent, List<List<Integer>> graph, int k, long[][] dp, boolean[] visited) {
        visited[u] = true;
        dp[u][0] = 1; // 当前节点染特定颜色
        
        for (int v : graph.get(u)) {
            if (v != parent && !visited[v]) {
                dfsColoring(v, u, graph, k, dp, visited);
                // 子节点不能与父节点同色，所以有(k-1)种选择
                dp[u][0] *= (dp[v][0] * (k - 1));
            }
        }
    }
    
    /**
     * 6. 树的直径（带权版本）
     * 问题描述：求带权树的最长路径（直径）
     * 算法要点：两次DFS/BFS
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int weightedTreeDiameter(List<List<int[]>> graph) {
        int n = graph.size();
        // 第一次BFS找到最远点
        int[] result1 = bfsFarthest(0, graph);
        int farthest = result1[0];
        // 第二次BFS找到直径
        int[] result2 = bfsFarthest(farthest, graph);
        return result2[1];
    }
    
    private static int[] bfsFarthest(int start, List<List<int[]>> graph) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        dist[start] = 0;
        
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        
        int farthest = start, maxDist = 0;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int[] edge : graph.get(u)) {
                int v = edge[0], w = edge[1];
                if (dist[v] == -1) {
                    dist[v] = dist[u] + w;
                    if (dist[v] > maxDist) {
                        maxDist = dist[v];
                        farthest = v;
                    }
                    queue.offer(v);
                }
            }
        }
        
        return new int[]{farthest, maxDist};
    }
    
    /**
     * 7. 树的重心（Centroid）
     * 问题描述：找到删除后使得最大连通分量最小的节点
     * 算法要点：DFS计算子树大小
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static List<Integer> treeCentroids(List<List<Integer>> graph) {
        int n = graph.size();
        int[] size = new int[n];
        List<Integer> centroids = new ArrayList<>();
        
        dfsCentroid(0, -1, graph, size, centroids, n);
        return centroids;
    }
    
    private static void dfsCentroid(int u, int parent, List<List<Integer>> graph, int[] size, List<Integer> centroids, int n) {
        size[u] = 1;
        boolean isCentroid = true;
        
        for (int v : graph.get(u)) {
            if (v != parent) {
                dfsCentroid(v, u, graph, size, centroids, n);
                size[u] += size[v];
                if (size[v] > n / 2) {
                    isCentroid = false;
                }
            }
        }
        
        // 检查删除u后的最大连通分量
        if (n - size[u] > n / 2) {
            isCentroid = false;
        }
        
        if (isCentroid) {
            centroids.add(u);
        }
    }
    
    /**
     * 8. 树的路径统计问题
     * 问题描述：统计树上满足特定条件的路径数量
     * 算法要点：DFS + 哈希表
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public static int countPathsWithSum(TreeNode root, int targetSum) {
        Map<Long, Integer> prefixSum = new HashMap<>();
        prefixSum.put(0L, 1); // 空路径
        return dfsPathSum(root, 0L, targetSum, prefixSum);
    }
    
    private static int dfsPathSum(TreeNode node, long currentSum, int target, Map<Long, Integer> prefixSum) {
        if (node == null) return 0;
        
        currentSum += node.val;
        int count = prefixSum.getOrDefault(currentSum - target, 0);
        
        prefixSum.put(currentSum, prefixSum.getOrDefault(currentSum, 0) + 1);
        
        count += dfsPathSum(node.left, currentSum, target, prefixSum);
        count += dfsPathSum(node.right, currentSum, target, prefixSum);
        
        prefixSum.put(currentSum, prefixSum.get(currentSum) - 1);
        return count;
    }
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：简单树的最大独立集
        List<List<Integer>> graph1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) graph1.add(new ArrayList<>());
        graph1.get(0).add(1); graph1.get(0).add(2);
        graph1.get(1).add(0); graph1.get(1).add(3);
        graph1.get(2).add(0);
        graph1.get(3).add(1);
        
        System.out.println("最大独立集: " + treeMaxIndependentSet(graph1));
        
        // 测试用例2：最小顶点覆盖
        System.out.println("最小顶点覆盖: " + treeMinVertexCover(graph1));
        
        // 测试用例3：树的k着色
        int[][] edges = {{0,1}, {0,2}, {1,3}};
        System.out.println("3着色方案数: " + treeKColoring(4, 3, edges));
    }
}