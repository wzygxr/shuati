package class120;

// 1245. 树的直径（非二叉树版本）
// 给你一棵树，树中包含 n 个节点，节点编号从 0 到 n-1。
// 树用一个边列表来表示，其中 edges[i] = [u, v] 表示节点 u 和 v 之间有一条无向边。
// 返回这棵树的直径长度。
// 树的直径是树中任意两个节点之间最长路径的长度。
// 这条路径可能不经过根节点。
// 测试链接 : https://leetcode.cn/problems/tree-diameter/
// 提交以下的code，提交时请把类名改成"Solution"，可以直接通过
// 时间复杂度：O(n)，空间复杂度：O(n)

import java.util.*;

public class Code31_LeetCode1245 {

    public int treeDiameter(int[][] edges) {
        int n = edges.length + 1; // 节点数 = 边数 + 1
        
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
        
        // 第一次BFS：从任意节点（如0）出发，找到最远的节点A
        int[] firstBFS = bfs(0, graph, n);
        int nodeA = firstBFS[0];
        
        // 第二次BFS：从节点A出发，找到最远的节点B，距离就是直径
        int[] secondBFS = bfs(nodeA, graph, n);
        
        return secondBFS[1]; // 返回直径长度
    }
    
    // BFS方法，返回最远节点和距离
    private int[] bfs(int start, List<Integer>[] graph, int n) {
        int[] distance = new int[n];
        Arrays.fill(distance, -1);
        distance[start] = 0;
        
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        
        int farthestNode = start;
        int maxDistance = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : graph[current]) {
                if (distance[neighbor] == -1) { // 未访问过
                    distance[neighbor] = distance[current] + 1;
                    queue.offer(neighbor);
                    
                    if (distance[neighbor] > maxDistance) {
                        maxDistance = distance[neighbor];
                        farthestNode = neighbor;
                    }
                }
            }
        }
        
        return new int[]{farthestNode, maxDistance};
    }
    
    // 方法二：DFS实现（推荐，更符合树的重心思想）
    private int diameter = 0;
    
    public int treeDiameter2(int[][] edges) {
        int n = edges.length + 1;
        
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
        
        // 从任意节点开始DFS
        dfs(0, -1, graph);
        return diameter;
    }
    
    // DFS返回从当前节点出发的最长路径长度
    private int dfs(int node, int parent, List<Integer>[] graph) {
        int maxDepth1 = 0; // 最长深度
        int maxDepth2 = 0; // 次长深度
        
        for (int neighbor : graph[node]) {
            if (neighbor == parent) continue; // 避免回到父节点
            
            int depth = dfs(neighbor, node, graph) + 1;
            
            if (depth > maxDepth1) {
                maxDepth2 = maxDepth1;
                maxDepth1 = depth;
            } else if (depth > maxDepth2) {
                maxDepth2 = depth;
            }
        }
        
        // 更新直径：经过当前节点的最长路径
        diameter = Math.max(diameter, maxDepth1 + maxDepth2);
        
        // 返回从当前节点出发的最长路径长度
        return maxDepth1;
    }
    
    // 方法三：基于树的重心思想（树形DP）
    public int treeDiameter3(int[][] edges) {
        int n = edges.length + 1;
        
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
        
        // 使用树形DP计算直径
        int[] result = new int[1]; // 存储直径
        treeDP(0, -1, graph, result);
        return result[0];
    }
    
    // 树形DP：返回从当前节点出发的最长路径长度
    private int treeDP(int node, int parent, List<Integer>[] graph, int[] result) {
        int max1 = 0, max2 = 0;
        
        for (int child : graph[node]) {
            if (child == parent) continue;
            
            int depth = treeDP(child, node, graph, result) + 1;
            
            if (depth > max1) {
                max2 = max1;
                max1 = depth;
            } else if (depth > max2) {
                max2 = depth;
            }
        }
        
        // 更新直径
        result[0] = Math.max(result[0], max1 + max2);
        
        return max1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code31_LeetCode1245 solution = new Code31_LeetCode1245();
        
        // 测试用例1: [[0,1],[0,2]]
        int[][] edges1 = {{0,1}, {0,2}};
        System.out.println("测试用例1结果: " + solution.treeDiameter2(edges1)); // 期望输出: 2
        
        // 测试用例2: [[0,1],[1,2],[2,3],[1,4],[4,5]]
        int[][] edges2 = {{0,1}, {1,2}, {2,3}, {1,4}, {4,5}};
        System.out.println("测试用例2结果: " + solution.treeDiameter2(edges2)); // 期望输出: 4
        
        // 测试用例3: 单边
        int[][] edges3 = {{0,1}};
        System.out.println("测试用例3结果: " + solution.treeDiameter2(edges3)); // 期望输出: 1
        
        // 测试用例4: 链状结构
        int[][] edges4 = {{0,1}, {1,2}, {2,3}, {3,4}};
        System.out.println("测试用例4结果: " + solution.treeDiameter2(edges4)); // 期望输出: 4
        
        // 测试用例5: 星状结构
        int[][] edges5 = {{0,1}, {0,2}, {0,3}, {0,4}};
        System.out.println("测试用例5结果: " + solution.treeDiameter2(edges5)); // 期望输出: 2
    }
}

/*
算法思路与树的重心联系：
本题与树的重心密切相关，因为：
1. 树的直径的两个端点通常与重心有特定关系
2. 计算直径的方法可以用于寻找重心
3. 树形DP的思想在两者中都得到应用

时间复杂度分析：
- BFS方法：两次BFS，每次O(n)，总时间复杂度O(n)
- DFS方法：一次DFS遍历，时间复杂度O(n)
- 树形DP方法：一次DFS遍历，时间复杂度O(n)

空间复杂度分析：
- 邻接表存储：O(n)
- 递归栈深度：O(n)
- 总空间复杂度：O(n)

工程化考量：
1. 图构建：使用邻接表而不是邻接矩阵以节省空间
2. 避免循环：使用parent参数防止DFS中的循环
3. 性能优化：三种方法都是最优解，选择最易理解的方法

与网络拓扑联系：
本题可以应用于网络拓扑分析：
1. 网络延迟分析：直径代表最大延迟
2. 数据中心布局：优化服务器间通信距离
3. 路由算法：寻找最优通信路径

调试技巧：
1. 可视化树结构帮助理解算法执行过程
2. 打印每个节点的最长和次长路径进行调试
3. 使用小规模测试用例验证算法正确性

面试要点：
1. 能够解释为什么两次BFS可以找到直径
2. 能够比较三种方法的优劣
3. 能够处理边界情况（单节点、单边等）
4. 能够将算法扩展到带权树的情况

关键设计细节：
1. 直径不一定经过根节点
2. 需要同时记录最长和次长路径
3. 直径 = 最长路径 + 次长路径
4. 使用parent参数避免循环访问

反直觉但关键的设计：
1. 直径的两个端点不一定是叶子节点（但在树中通常是）
2. 两次BFS的方法看似简单但数学证明复杂
3. DFS方法比BFS方法更通用，适用于带权树

与机器学习联系：
1. 图神经网络中的消息传递机制
2. 树结构数据的特征提取
3. 层次聚类中的距离计算
*/
