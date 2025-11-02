package class121;

// LeetCode 1443. 收集树上所有苹果的最少时间
// 题目：给你一棵有 n 个节点的无向树，节点编号为 0 到 n-1，它们中有一些节点有苹果。
// 给你一个二维整数数组 edges，其中 edges[i] = [ai, bi] 表示节点 ai 和 bi 之间有一条边。
// 另给你一个布尔数组 hasApple，其中 hasApple[i] = true 表示节点 i 有一个苹果，否则为 false。
// 你需要从节点 0 出发，收集树上所有苹果，并返回到节点 0 所需的最少时间（秒）。
// 每经过一条边需要 1 秒。
// 来源：LeetCode
// 链接：https://leetcode.cn/problems/minimum-time-to-collect-all-apples-in-a-tree/

import java.util.*;

public class LeetCode1443_MinimumTimeToCollectApples {
    
    /**
     * 计算收集所有苹果的最少时间
     * @param n 节点数量
     * @param edges 边列表
     * @param hasApple 苹果分布数组
     * @return 最少时间（秒）
     * 
     * 时间复杂度：O(n)，其中n是节点数量
     * 空间复杂度：O(n)，用于存储邻接表和DFS递归栈
     */
    public int minTime(int n, int[][] edges, List<Boolean> hasApple) {
        // 构建邻接表
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 使用DFS计算需要访问的路径总长度
        boolean[] visited = new boolean[n];
        return dfs(0, -1, graph, hasApple, visited);
    }
    
    /**
     * DFS遍历计算子树中收集苹果所需的时间
     * @param node 当前节点
     * @param parent 父节点
     * @param graph 邻接表
     * @param hasApple 苹果分布
     * @param visited 访问标记
     * @return 收集当前子树中苹果所需的时间
     */
    private int dfs(int node, int parent, List<Integer>[] graph, List<Boolean> hasApple, boolean[] visited) {
        visited[node] = true;
        
        int totalTime = 0;
        
        // 遍历所有邻居（除了父节点）
        for (int neighbor : graph[node]) {
            if (neighbor != parent && !visited[neighbor]) {
                // 递归处理子树
                int childTime = dfs(neighbor, node, graph, hasApple, visited);
                
                // 如果子树中有苹果或者子树需要访问，则需要加上往返时间
                if (childTime > 0 || hasApple.get(neighbor)) {
                    totalTime += childTime + 2; // 往返需要2秒
                }
            }
        }
        
        return totalTime;
    }
    
    /**
     * 使用BFS的迭代实现（避免递归深度过大）
     * @param n 节点数量
     * @param edges 边列表
     * @param hasApple 苹果分布
     * @return 最少时间
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int minTimeIterative(int n, int[][] edges, List<Boolean> hasApple) {
        // 构建邻接表
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 计算每个节点的深度和父节点
        int[] depth = new int[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        
        // BFS计算深度和父节点
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        depth[0] = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : graph[current]) {
                if (neighbor != parent[current]) {
                    parent[neighbor] = current;
                    depth[neighbor] = depth[current] + 1;
                    queue.offer(neighbor);
                }
            }
        }
        
        // 标记需要访问的节点
        boolean[] needVisit = new boolean[n];
        
        // 从有苹果的叶子节点开始，标记所有需要访问的路径
        for (int i = 0; i < n; i++) {
            if (hasApple.get(i)) {
                // 标记当前节点和所有祖先节点
                int currentNode = i;
                while (currentNode != -1 && !needVisit[currentNode]) {
                    needVisit[currentNode] = true;
                    currentNode = parent[currentNode];
                }
            }
        }
        
        // 计算需要访问的边数（每条边需要走2次：去和回）
        int totalEdges = 0;
        for (int i = 1; i < n; i++) { // 从1开始，因为节点0没有父节点
            if (needVisit[i]) {
                totalEdges++;
            }
        }
        
        // 总时间 = 需要访问的边数 * 2
        return totalEdges * 2;
    }
    
    /**
     * 使用树形DP的优化实现
     * @param n 节点数量
     * @param edges 边列表
     * @param hasApple 苹果分布
     * @return 最少时间
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int minTimeDP(int n, int[][] edges, List<Boolean> hasApple) {
        // 构建邻接表
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // DP数组：dp[i]表示从节点i出发收集子树中所有苹果所需的时间
        int[] dp = new int[n];
        boolean[] visited = new boolean[n];
        
        // 后序遍历计算DP值
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        
        // 记录每个节点的处理状态：0-未处理，1-正在处理子节点，2-处理完成
        int[] state = new int[n];
        
        while (!stack.isEmpty()) {
            int node = stack.peek();
            
            if (state[node] == 0) {
                // 第一次访问，标记为正在处理
                state[node] = 1;
                
                // 将所有未访问的子节点入栈
                for (int neighbor : graph[node]) {
                    if (state[neighbor] == 0) {
                        stack.push(neighbor);
                    }
                }
            } else if (state[node] == 1) {
                // 所有子节点处理完成，计算当前节点的DP值
                stack.pop();
                state[node] = 2;
                
                int totalTime = 0;
                boolean hasAppleInSubtree = hasApple.get(node);
                
                for (int neighbor : graph[node]) {
                    if (state[neighbor] == 2) {
                        // 如果子树需要访问或者子树中有苹果
                        if (dp[neighbor] > 0 || hasApple.get(neighbor)) {
                            totalTime += dp[neighbor] + 2;
                            hasAppleInSubtree = true;
                        }
                    }
                }
                
                // 如果当前子树有苹果或者需要访问，则更新DP值
                if (hasAppleInSubtree) {
                    dp[node] = totalTime;
                } else {
                    dp[node] = 0;
                }
            }
        }
        
        return dp[0];
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode1443_MinimumTimeToCollectApples solution = new LeetCode1443_MinimumTimeToCollectApples();
        
        // 测试用例1: n=7, edges=[[0,1],[0,2],[1,4],[1,5],[2,3],[2,6]], hasApple=[false,false,true,false,true,true,false]
        // 树结构：
        //       0
        //      / \
        //     1   2
        //    / \  / \
        //   4  5 3  6
        // 苹果在节点2,4,5
        // 预期输出：8（路径：0->1->4->1->5->1->0->2->0）
        int n1 = 7;
        int[][] edges1 = {{0,1},{0,2},{1,4},{1,5},{2,3},{2,6}};
        List<Boolean> hasApple1 = Arrays.asList(false, false, true, false, true, true, false);
        
        System.out.println("测试用例1结果: " + solution.minTime(n1, edges1, hasApple1)); // 应该输出8
        System.out.println("测试用例1(迭代)结果: " + solution.minTimeIterative(n1, edges1, hasApple1)); // 应该输出8
        System.out.println("测试用例1(DP)结果: " + solution.minTimeDP(n1, edges1, hasApple1)); // 应该输出8
        
        // 测试用例2: n=4, edges=[[0,2],[0,3],[1,2]], hasApple=[false,true,false,false]
        // 树结构：
        //   0
        //  / \
        // 2   3
        // |
        // 1
        // 苹果在节点1
        // 预期输出：4（路径：0->2->1->2->0）
        int n2 = 4;
        int[][] edges2 = {{0,2},{0,3},{1,2}};
        List<Boolean> hasApple2 = Arrays.asList(false, true, false, false);
        
        System.out.println("测试用例2结果: " + solution.minTime(n2, edges2, hasApple2)); // 应该输出4
        System.out.println("测试用例2(迭代)结果: " + solution.minTimeIterative(n2, edges2, hasApple2)); // 应该输出4
        System.out.println("测试用例2(DP)结果: " + solution.minTimeDP(n2, edges2, hasApple2)); // 应该输出4
        
        // 测试用例3: 没有苹果
        int n3 = 4;
        int[][] edges3 = {{0,1},{1,2},{2,3}};
        List<Boolean> hasApple3 = Arrays.asList(false, false, false, false);
        
        System.out.println("测试用例3结果: " + solution.minTime(n3, edges3, hasApple3)); // 应该输出0
        System.out.println("测试用例3(迭代)结果: " + solution.minTimeIterative(n3, edges3, hasApple3)); // 应该输出0
        System.out.println("测试用例3(DP)结果: " + solution.minTimeDP(n3, edges3, hasApple3)); // 应该输出0
        
        // 测试用例4: 所有节点都有苹果
        int n4 = 3;
        int[][] edges4 = {{0,1},{1,2}};
        List<Boolean> hasApple4 = Arrays.asList(true, true, true);
        
        System.out.println("测试用例4结果: " + solution.minTime(n4, edges4, hasApple4)); // 应该输出4
        System.out.println("测试用例4(迭代)结果: " + solution.minTimeIterative(n4, edges4, hasApple4)); // 应该输出4
        System.out.println("测试用例4(DP)结果: " + solution.minTimeDP(n4, edges4, hasApple4)); // 应该输出4
    }
}