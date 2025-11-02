package class079;

// 834. 树中距离之和
// 测试链接 : https://leetcode.cn/problems/sum-of-distances-in-tree/
import java.util.*;

public class Code09_SumOfDistancesInTree {
    
    // 提交如下的方法
    // 时间复杂度: O(n) n为节点数量，需要遍历所有节点两次
    // 空间复杂度: O(n) 用于存储图、子树大小和距离数组
    // 是否为最优解: 是，这是计算树中距离之和的标准方法，使用换根DP技术
    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        // 构建邻接表表示的树
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        // dp[i] 表示以节点i为根的子树中，所有节点到节点i的距离之和
        int[] dp = new int[n];
        // sz[i] 表示以节点i为根的子树的节点数量
        int[] sz = new int[n];
        // result[i] 表示所有节点到节点i的距离之和
        int[] result = new int[n];
        
        // 第一次DFS：计算以节点0为根时的dp和sz数组
        dfs1(0, -1, graph, dp, sz);
        
        // 第二次DFS：通过换根DP计算所有节点的结果
        dfs2(0, -1, graph, dp, sz, result);
        
        return result;
    }
    
    // 第一次DFS：计算以某个节点为根时，子树内的距离和以及子树大小
    private void dfs1(int u, int parent, List<List<Integer>> graph, int[] dp, int[] sz) {
        // 初始化当前节点的子树大小为1（节点本身）
        sz[u] = 1;
        // 初始化当前节点的子树内距离和为0
        dp[u] = 0;
        
        // 遍历当前节点的所有子节点
        for (int v : graph.get(u)) {
            // 避免回到父节点
            if (v == parent) continue;
            
            // 递归计算子节点的dp和sz
            dfs1(v, u, graph, dp, sz);
            
            // 更新当前节点的子树大小
            sz[u] += sz[v];
            // 更新当前节点的子树内距离和
            // 子节点v的子树中所有节点到u的距离比到v的距离多1
            dp[u] += dp[v] + sz[v];
        }
    }
    
    // 第二次DFS：通过换根DP计算所有节点到其他节点的距离之和
    private void dfs2(int u, int parent, List<List<Integer>> graph, int[] dp, int[] sz, int[] result) {
        // 当前节点的结果就是dp[u]
        result[u] = dp[u];
        
        // 遍历当前节点的所有子节点
        for (int v : graph.get(u)) {
            // 避免回到父节点
            if (v == parent) continue;
            
            // 换根：将根从u换到v
            // 保存原始值
            int dpU = dp[u], dpV = dp[v];
            int szU = sz[u], szV = sz[v];
            
            // 更新dp和sz值以反映根节点的变更
            // 当根从u变为v时：
            // 1. v的子树中的节点到v的距离比到u的距离少1，总共少sz[v]个距离单位
            // 2. 除了v的子树外，其他节点到v的距离比到u的距离多1，总共多(n - sz[v])个距离单位
            dp[u] = dp[u] - dp[v] - sz[v];
            sz[u] = sz[u] - sz[v];
            dp[v] = dp[v] + dp[u] + sz[u];
            sz[v] = sz[v] + sz[u];
            
            // 递归计算以v为根的结果
            dfs2(v, u, graph, dp, sz, result);
            
            // 恢复原始值，为处理下一个子节点做准备
            dp[u] = dpU;
            dp[v] = dpV;
            sz[u] = szU;
            sz[v] = szV;
        }
    }
    
    // 补充题目1: 310. 最小高度树
    // 题目链接: https://leetcode.cn/problems/minimum-height-trees/
    // 题目描述: 对于一个具有n个节点的无向树，找到所有可能的最小高度树的根节点。
    // 时间复杂度: O(n) 进行一次广度优先搜索
    // 空间复杂度: O(n) 用于存储图和队列
    // 是否为最优解: 是，这是解决最小高度树问题的高效方法
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        List<Integer> result = new ArrayList<>();
        
        // 边界情况：只有一个节点
        if (n == 1) {
            result.add(0);
            return result;
        }
        
        // 构建邻接表
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 存储每个节点的度数
        int[] degree = new int[n];
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
            degree[edge[0]]++;
            degree[edge[1]]++;
        }
        
        // 将所有叶子节点（度数为1）加入队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                queue.offer(i);
            }
        }
        
        // 逐步移除叶子节点，直到剩下1或2个节点
        while (n > 2) {
            int size = queue.size();
            n -= size;
            
            for (int i = 0; i < size; i++) {
                int leaf = queue.poll();
                for (int neighbor : graph.get(leaf)) {
                    if (--degree[neighbor] == 1) {
                        queue.offer(neighbor);
                    }
                }
            }
        }
        
        // 剩余的节点就是最小高度树的根
        result.addAll(queue);
        return result;
    }
    
    // 补充题目2: 1617. 统计子树中城市之间最大距离
    // 题目链接: https://leetcode.cn/problems/count-subtrees-with-max-distance-between-cities/
    // 题目描述: 给定一个由n个城市组成的树，计算所有可能的子树中，城市之间的最大距离的出现次数。
    // 时间复杂度: O(2^n * n) 枚举所有子集，并计算每个子集的直径
    // 空间复杂度: O(n) 用于存储图和辅助数组
    // 注意：这个实现使用暴力枚举，对于较大的n可能会超时
    public int[] countSubgraphsForEachDiameter(int n, int[][] edges) {
        // 构建邻接表
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0] - 1; // 转换为0-based索引
            int v = edge[1] - 1;
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        int[] result = new int[n - 1];
        
        // 枚举所有非空子集（除了单节点）
        for (int mask = 1; mask < (1 << n); mask++) {
            // 检查子集是否连通
            if (!isConnected(mask, graph)) {
                continue;
            }
            
            // 计算子树的直径
            int diameter = getDiameter(mask, graph);
            if (diameter > 0) {
                result[diameter - 1]++;
            }
        }
        
        return result;
    }
    
    // 检查给定mask表示的子集是否连通
    private boolean isConnected(int mask, List<List<Integer>> graph) {
        int n = graph.size();
        int[] visited = new int[n];
        int start = -1;
        
        // 找到第一个属于子集的节点
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                start = i;
                break;
            }
        }
        
        if (start == -1) return false;
        
        // DFS检查连通性
        dfsConnected(start, mask, graph, visited);
        
        // 验证所有属于子集的节点是否都被访问
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0 && visited[i] == 0) {
                return false;
            }
        }
        
        return true;
    }
    
    private void dfsConnected(int u, int mask, List<List<Integer>> graph, int[] visited) {
        visited[u] = 1;
        for (int v : graph.get(u)) {
            if ((mask & (1 << v)) != 0 && visited[v] == 0) {
                dfsConnected(v, mask, graph, visited);
            }
        }
    }
    
    // 计算给定mask表示的子树的直径
    private int getDiameter(int mask, List<List<Integer>> graph) {
        int n = graph.size();
        int maxDiameter = 0;
        
        // 找到子集中的所有节点
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                nodes.add(i);
            }
        }
        
        // 枚举所有节点对，计算距离，找出最大值
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                int distance = bfsDistance(nodes.get(i), nodes.get(j), mask, graph);
                maxDiameter = Math.max(maxDiameter, distance);
            }
        }
        
        return maxDiameter;
    }
    
    private int bfsDistance(int start, int end, int mask, List<List<Integer>> graph) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start, 0});
        Set<Integer> visited = new HashSet<>();
        visited.add(start);
        
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int node = curr[0];
            int dist = curr[1];
            
            if (node == end) {
                return dist;
            }
            
            for (int neighbor : graph.get(node)) {
                if ((mask & (1 << neighbor)) != 0 && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(new int[]{neighbor, dist + 1});
                }
            }
        }
        
        return -1; // 应该不会到达这里，因为已经确认是连通的
    }
    
    // 补充题目3: 2581. 统计可能的树根数目
    // 题目链接: https://leetcode.cn/problems/count-number-of-possible-root-nodes/
    // 题目描述: 给定一棵n个节点的无向树和k个查询，每个查询给出一个边，其中指定父节点和子节点的关系。
    // 计算有多少个节点可以作为树的根，使得所有查询条件都满足。
    // 时间复杂度: O(n+k) 进行两次DFS
    // 空间复杂度: O(n+k) 用于存储图和边信息
    public int rootCount(int[][] edges, int[][] guesses, int k) {
        int n = edges.length + 1;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        // 将猜测的边存入哈希集合，方便查询
        Set<Long> guessSet = new HashSet<>();
        for (int[] guess : guesses) {
            long u = guess[0];
            long v = guess[1];
            guessSet.add(u * n + v); // 编码边为一个长整数
        }
        
        // 第一次DFS：以0为根，计算正确的猜测数
        int[] correct = new int[1]; // 使用数组作为可变整数
        dfsRootCount(0, -1, graph, guessSet, correct, n);
        
        // 第二次DFS：通过换根计算每个节点作为根时的正确猜测数
        int result = 0;
        boolean[] visited = new boolean[n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, -1, correct[0]});
        visited[0] = true;
        
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int u = curr[0];
            int parent = curr[1];
            int correctCount = curr[2];
            
            // 检查当前节点作为根时是否满足条件
            if (correctCount >= k) {
                result++;
            }
            
            // 遍历子节点
            for (int v : graph.get(u)) {
                if (v != parent && !visited[v]) {
                    visited[v] = true;
                    int newCorrect = correctCount;
                    
                    // 当根从u换到v时，需要调整正确猜测数：
                    // 1. 边u->v在猜测中，现在变为v->u，可能不再正确
                    if (guessSet.contains((long)u * n + v)) {
                        newCorrect--;
                    }
                    // 2. 边v->u在猜测中，现在变为u->v，可能变为正确
                    if (guessSet.contains((long)v * n + u)) {
                        newCorrect++;
                    }
                    
                    queue.offer(new int[]{v, u, newCorrect});
                }
            }
        }
        
        return result;
    }
    
    private void dfsRootCount(int u, int parent, List<List<Integer>> graph, 
                             Set<Long> guessSet, int[] correct, int n) {
        for (int v : graph.get(u)) {
            if (v != parent) {
                // 检查u->v是否在猜测中
                if (guessSet.contains((long)u * n + v)) {
                    correct[0]++;
                }
                dfsRootCount(v, u, graph, guessSet, correct, n);
            }
        }
    }
    
    // 补充题目4: 1245. 树的直径（换根DP版本）
    // 题目链接: https://leetcode.cn/problems/tree-diameter/
    // 题目描述: 给一棵无向树，找到树中最长路径的长度。
    // 时间复杂度: O(n) n为节点数量，需要遍历所有节点两次
    // 空间复杂度: O(n) 用于存储图和辅助数组
    public int treeDiameterDP(int[][] edges) {
        if (edges == null || edges.length == 0) {
            return 0;
        }
        
        int n = edges.length + 1;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        int maxDiameter = 0;
        // 第一次DFS找到离任意节点最远的节点
        int[] result1 = dfsTreeDiameter(0, -1, graph);
        // 第二次DFS从最远节点出发找到树的直径
        int[] result2 = dfsTreeDiameter(result1[0], -1, graph);
        
        return result2[1];
    }
    
    // 返回最远节点和距离的数组 [最远节点, 距离]
    private int[] dfsTreeDiameter(int u, int parent, List<List<Integer>> graph) {
        int[] result = {u, 0}; // 默认最远节点是自己，距离为0
        
        for (int v : graph.get(u)) {
            if (v != parent) { // 避免回到父节点
                int[] current = dfsTreeDiameter(v, u, graph);
                int distance = current[1] + 1;
                
                if (distance > result[1]) { // 更新最长距离和最远节点
                    result[0] = current[0];
                    result[1] = distance;
                }
            }
        }
        
        return result;
    }
}