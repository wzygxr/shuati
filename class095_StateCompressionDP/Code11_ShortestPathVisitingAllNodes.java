package class080;

import java.util.*;

// 访问所有节点的最短路径 (Shortest Path Visiting All Nodes)
// 存在一个由 n 个节点组成的无向连通图，图中的节点按从 0 到 n - 1 编号。
// 给你一个数组 graph 表示这个图。其中，graph[i] 是一个列表，由所有与节点 i 直接相连的节点组成。
// 返回能够访问所有节点的最短路径的长度。你可以在任一节点开始和停止，也可以多次重访节点。
// 测试链接 : https://leetcode.cn/problems/shortest-path-visiting-all-nodes/
public class Code11_ShortestPathVisitingAllNodes {

    // 使用状态压缩动态规划+BFS解决访问所有节点的最短路径问题
    // 核心思想：用二进制位表示已访问节点的集合，通过BFS找到最短路径
    // 时间复杂度: O(n^2 * 2^n)
    // 空间复杂度: O(n * 2^n)
    public static int shortestPathLength(int[][] graph) {
        int n = graph.length;
        
        // dp[mask][i] 表示访问了mask代表的节点集合，当前在节点i时的最短路径长度
        int[][] dp = new int[1 << n][n];
        // 初始化：将所有状态设为最大值
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
            }
        }
        
        // 队列用于BFS，存储[节点, 状态]
        Queue<int[]> queue = new LinkedList<>();
        
        // 初始状态：从每个节点开始，只访问了该节点
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 0;
            queue.offer(new int[]{i, 1 << i});
        }
        
        // BFS搜索
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int u = cur[0], mask = cur[1];
            
            // 如果已经访问了所有节点，返回路径长度
            if (mask == (1 << n) - 1) {
                return dp[mask][u];
            }
            
            // 遍历当前节点的所有邻居
            for (int v : graph[u]) {
                // 计算新的状态
                int newMask = mask | (1 << v);
                // 如果找到更短的路径
                if (dp[newMask][v] > dp[mask][u] + 1) {
                    dp[newMask][v] = dp[mask][u] + 1;
                    queue.offer(new int[]{v, newMask});
                }
            }
        }
        
        // 如果无法访问所有节点，返回-1
        return -1;
    }

}
