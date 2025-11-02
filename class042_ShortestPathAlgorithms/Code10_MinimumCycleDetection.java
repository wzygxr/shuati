package class065;

import java.util.*;

/**
 * 最小环检测 - Floyd算法应用
 * 
 * 算法思路:
 * 在Floyd算法的执行过程中，当考虑中间节点k时，
 * 检查是否存在i->k和k->i的路径，从而形成环
 * 最小环长度 = dist[i][k] + dist[k][i]
 * 
 * 时间复杂度: O(N³)，与标准Floyd相同
 * 空间复杂度: O(N²)
 */
public class Code10_MinimumCycleDetection {
    
    /**
     * 查找图中的最小环
     * 
     * @param n 节点数量
     * @param edges 边数组，每个元素为[起点, 终点, 权重]
     * @return 最小环的长度，如果不存在环则返回-1
     */
    public static int findMinimumCycle(int n, int[][] edges) {
        // 初始化距离矩阵
        int[][] dist = new int[n][n];
        
        // 初始化距离矩阵
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }
        
        // 添加边信息
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            // 注意：这里假设是无向图，所以添加双向边
            // 如果是有向图，只需要添加 dist[u][v] = w;
            dist[u][v] = Math.min(dist[u][v], w);
            dist[v][u] = Math.min(dist[v][u], w);
        }
        
        int minCycle = Integer.MAX_VALUE;
        
        // Floyd算法检测最小环
        for (int k = 0; k < n; k++) {
            // 在更新之前，检查经过k的环
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && 
                        dist[k][j] != Integer.MAX_VALUE &&
                        dist[j][i] != Integer.MAX_VALUE) {
                        
                        minCycle = Math.min(minCycle, 
                            dist[i][k] + dist[k][j] + dist[j][i]);
                    }
                }
            }
            
            // 标准Floyd更新
            for (int i = 0; i < n; i++) {
                if (dist[i][k] == Integer.MAX_VALUE) continue;
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && 
                        dist[k][j] != Integer.MAX_VALUE &&
                        dist[i][j] > (long)dist[i][k] + dist[k][j]) {
                        
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        
        return minCycle == Integer.MAX_VALUE ? -1 : minCycle;
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1：存在环的图
        int n1 = 4;
        int[][] edges1 = {{0,1,1},{1,2,2},{2,3,3},{3,0,4},{0,2,5}};
        int result1 = findMinimumCycle(n1, edges1);
        System.out.println("测试用例1结果: " + result1); // 期望输出: 7 (环0->1->2->0)
        
        // 测试用例2：不存在环的图
        int n2 = 3;
        int[][] edges2 = {{0,1,1},{1,2,2}};
        int result2 = findMinimumCycle(n2, edges2);
        System.out.println("测试用例2结果: " + result2); // 期望输出: -1
    }
}