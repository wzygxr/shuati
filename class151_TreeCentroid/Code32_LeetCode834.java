package class120;

// 834. 树中距离之和
// 给定一个无向、连通的树。树中有 n 个节点，节点编号从 0 到 n-1。
// 给定整数 n 和数组 edges，其中 edges[i] = [ai, bi] 表示树中节点 ai 和 bi 之间有一条边。
// 返回一个长度为 n 的数组 answer，其中 answer[i] 是树中第 i 个节点与所有其他节点之间的距离之和。
// 测试链接 : https://leetcode.cn/problems/sum-of-distances-in-tree/
// 提交以下的code，提交时请把类名改成"Solution"，可以直接通过
// 时间复杂度：O(n)，空间复杂度：O(n)

import java.util.*;

public class Code32_LeetCode834 {

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
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
        
        // count[i] 表示以节点i为根的子树中的节点数量
        int[] count = new int[n];
        // res[i] 表示节点i到所有其他节点的距离之和
        int[] res = new int[n];
        
        // 第一次DFS：以0为根，计算count和res[0]
        dfs1(0, -1, graph, count, res);
        
        // 第二次DFS：换根DP，计算所有节点的res
        dfs2(0, -1, graph, count, res, n);
        
        return res;
    }
    
    // 第一次DFS：计算子树大小和根节点的距离和
    private void dfs1(int node, int parent, List<Integer>[] graph, int[] count, int[] res) {
        count[node] = 1; // 当前节点自身
        
        for (int neighbor : graph[node]) {
            if (neighbor == parent) continue;
            
            dfs1(neighbor, node, graph, count, res);
            
            count[node] += count[neighbor];
            res[node] += res[neighbor] + count[neighbor];
        }
    }
    
    // 第二次DFS：换根DP，计算所有节点的距离和
    private void dfs2(int node, int parent, List<Integer>[] graph, int[] count, int[] res, int n) {
        for (int neighbor : graph[node]) {
            if (neighbor == parent) continue;
            
            // 关键公式：当根从node换到neighbor时
            // res[neighbor] = res[node] - count[neighbor] + (n - count[neighbor])
            res[neighbor] = res[node] - count[neighbor] + (n - count[neighbor]);
            
            dfs2(neighbor, node, graph, count, res, n);
        }
    }
    
    // 方法二：更详细的实现，便于理解
    public int[] sumOfDistancesInTree2(int n, int[][] edges) {
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
        
        // subtreeSize[i]：以i为根的子树节点数
        int[] subtreeSize = new int[n];
        // distanceSum[i]：节点i到所有其他节点的距离和
        int[] distanceSum = new int[n];
        
        // 第一次DFS：计算子树大小和根节点的距离和
        postOrderDFS(0, -1, graph, subtreeSize, distanceSum);
        
        // 第二次DFS：换根计算所有节点的距离和
        preOrderDFS(0, -1, graph, subtreeSize, distanceSum, n);
        
        return distanceSum;
    }
    
    private void postOrderDFS(int node, int parent, List<Integer>[] graph, 
                             int[] subtreeSize, int[] distanceSum) {
        subtreeSize[node] = 1; // 当前节点自身
        
        for (int child : graph[node]) {
            if (child == parent) continue;
            
            postOrderDFS(child, node, graph, subtreeSize, distanceSum);
            
            // 更新子树大小
            subtreeSize[node] += subtreeSize[child];
            
            // 更新距离和：子节点的距离和 + 子节点子树中每个节点到当前节点的额外距离
            distanceSum[node] += distanceSum[child] + subtreeSize[child];
        }
    }
    
    private void preOrderDFS(int node, int parent, List<Integer>[] graph,
                           int[] subtreeSize, int[] distanceSum, int n) {
        for (int child : graph[node]) {
            if (child == parent) continue;
            
            // 换根公式推导：
            // 当根从node换到child时：
            // 1. 原来在child子树中的节点到新根child的距离减少了1
            // 2. 原来不在child子树中的节点到新根child的距离增加了1
            distanceSum[child] = distanceSum[node] - subtreeSize[child] + (n - subtreeSize[child]);
            
            preOrderDFS(child, node, graph, subtreeSize, distanceSum, n);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code32_LeetCode834 solution = new Code32_LeetCode834();
        
        // 测试用例1: n=6, edges=[[0,1],[0,2],[2,3],[2,4],[2,5]]
        int n1 = 6;
        int[][] edges1 = {{0,1}, {0,2}, {2,3}, {2,4}, {2,5}};
        int[] result1 = solution.sumOfDistancesInTree(n1, edges1);
        System.out.println("测试用例1结果: " + Arrays.toString(result1));
        // 期望输出: [8,12,6,10,10,10]
        
        // 测试用例2: n=1, edges=[]
        int n2 = 1;
        int[][] edges2 = {};
        int[] result2 = solution.sumOfDistancesInTree(n2, edges2);
        System.out.println("测试用例2结果: " + Arrays.toString(result2));
        // 期望输出: [0]
        
        // 测试用例3: n=2, edges=[[0,1]]
        int n3 = 2;
        int[][] edges3 = {{0,1}};
        int[] result3 = solution.sumOfDistancesInTree(n3, edges3);
        System.out.println("测试用例3结果: " + Arrays.toString(result3));
        // 期望输出: [1,1]
        
        // 测试用例4: 链状结构 n=4, edges=[[0,1],[1,2],[2,3]]
        int n4 = 4;
        int[][] edges4 = {{0,1}, {1,2}, {2,3}};
        int[] result4 = solution.sumOfDistancesInTree(n4, edges4);
        System.out.println("测试用例4结果: " + Arrays.toString(result4));
        // 期望输出: [6,4,4,6] 或类似（具体取决于结构）
    }
}

/*
算法思路与树的重心联系：
本题与树的重心密切相关，因为：
1. 树的重心是使得到所有节点距离和最小的节点
2. 本题需要计算每个节点到所有其他节点的距离和
3. 换根DP的思想在树的重心问题中也有应用

时间复杂度分析：
- 两次DFS遍历，每次O(n)，总时间复杂度O(n)

空间复杂度分析：
- 邻接表存储：O(n)
- 递归栈深度：O(n)
- 辅助数组：O(n)
- 总空间复杂度：O(n)

工程化考量：
1. 图构建：使用邻接表而不是邻接矩阵以节省空间
2. 避免循环：使用parent参数防止DFS中的循环
3. 性能优化：换根DP是解决此类问题的最优方法

数学推导：
关键公式：res[child] = res[parent] - count[child] + (n - count[child])
推导过程：
1. 当根从parent换到child时：
2. 在child子树中的节点到新根的距离减少1：-count[child]
3. 不在child子树中的节点到新根的距离增加1：+(n - count[child])

与网络优化联系：
本题可以应用于网络优化：
1. 服务器位置选择：选择距离和最小的节点作为服务器位置
2. 网络拓扑优化：优化节点间通信距离
3. 分布式系统：数据副本放置策略

调试技巧：
1. 打印每个节点的子树大小和距离和进行调试
2. 使用小规模树结构验证换根公式的正确性
3. 特别注意边界情况（单节点、双边等）

面试要点：
1. 能够解释换根DP的思想和数学推导
2. 能够处理边界情况和特殊输入
3. 能够分析算法的时间复杂度和空间复杂度
4. 能够将算法扩展到带权树的情况

关键设计细节：
1. 需要两次DFS：第一次计算基础值，第二次换根计算
2. 子树大小的计算是基础
3. 换根公式是核心，需要理解其数学含义

反直觉但关键的设计：
1. 距离和的计算不直接累加，而是通过子树大小间接计算
2. 换根公式看似简单但数学证明复杂
3. 算法的时间复杂度是线性的，而不是直观的O(n²)

与机器学习联系：
1. 图神经网络中的节点特征聚合
2. 层次聚类中的中心点选择
3. 推荐系统中的用户距离计算

性能优化技巧：
1. 使用邻接表而不是邻接矩阵
2. 避免重复计算，利用子树信息
3. 使用递归栈而不是显式栈以简化代码
*/
