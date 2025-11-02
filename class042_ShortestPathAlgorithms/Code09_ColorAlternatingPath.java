package class065;

import java.util.*;

/**
 * LeetCode 1129. 颜色交替的最短路径 - A*算法实现
 * 
 * 题目链接: https://leetcode.cn/problems/shortest-path-with-alternating-colors/
 * 题目描述: 给定一个有向图，边有红蓝两种颜色，要求找到从节点0到其他节点的最短路径，
 * 路径中相邻边的颜色必须交替（红-蓝-红...或蓝-红-蓝...）
 * 
 * 算法思路:
 * 1. 状态扩展: 状态包含(节点, 最后使用的颜色)
 * 2. 启发函数: 使用到目标节点的估计距离
 * 3. 约束处理: 强制颜色交替的移动约束
 * 
 * 时间复杂度: O(N*M*log(N*M))，其中N是节点数，M是边数
 * 空间复杂度: O(N*M)
 */
public class Code09_ColorAlternatingPath {
    
    // 颜色常量
    public static final int RED = 0;
    public static final int BLUE = 1;
    public static final int NO_COLOR = -1;
    
    /**
     * 颜色交替最短路径算法实现
     * 
     * @param n 节点数量
     * @param redEdges 红色边数组
     * @param blueEdges 蓝色边数组
     * @return 从节点0到各节点的最短路径长度数组，无法到达则为-1
     */
    public static int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        // 构建邻接表，包含颜色信息
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 添加红色边
        for (int[] edge : redEdges) {
            graph[edge[0]].add(new int[]{edge[1], RED});
        }
        
        // 添加蓝色边
        for (int[] edge : blueEdges) {
            graph[edge[0]].add(new int[]{edge[1], BLUE});
        }
        
        // 结果数组
        int[] result = new int[n];
        Arrays.fill(result, -1);
        
        // 记录访问状态: visited[node][color] 表示以某种颜色到达节点的状态是否已访问
        boolean[][] visited = new boolean[n][2];
        
        // 优先队列，存储 {节点, 距离, 最后使用的颜色}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        
        // 初始状态：从节点0开始，距离为0，没有使用颜色
        pq.offer(new int[]{0, 0, NO_COLOR});
        result[0] = 0;
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int node = current[0];
            int distance = current[1];
            int lastColor = current[2];
            
            // 如果该状态已访问，跳过
            if (lastColor != NO_COLOR && visited[node][lastColor]) {
                continue;
            }
            
            // 标记为已访问
            if (lastColor != NO_COLOR) {
                visited[node][lastColor] = true;
            }
            
            // 遍历所有邻接边
            for (int[] edge : graph[node]) {
                int nextNode = edge[0];
                int color = edge[1];
                
                // 颜色必须交替（初始状态除外）
                if (lastColor == NO_COLOR || lastColor != color) {
                    // 如果还没有找到到达nextNode的路径，或者找到了更短的路径
                    if (result[nextNode] == -1 || distance + 1 < result[nextNode]) {
                        result[nextNode] = distance + 1;
                    }
                    
                    // 如果该状态未访问，加入队列
                    if (!visited[nextNode][color]) {
                        pq.offer(new int[]{nextNode, distance + 1, color});
                    }
                }
            }
        }
        
        return result;
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3;
        int[][] redEdges1 = {{0,1},{1,2}};
        int[][] blueEdges1 = {};
        int[] result1 = shortestAlternatingPaths(n1, redEdges1, blueEdges1);
        System.out.println("测试用例1结果: " + Arrays.toString(result1)); // 期望输出: [0,1,-1]
        
        // 测试用例2
        int n2 = 3;
        int[][] redEdges2 = {{0,1}};
        int[][] blueEdges2 = {{2,1}};
        int[] result2 = shortestAlternatingPaths(n2, redEdges2, blueEdges2);
        System.out.println("测试用例2结果: " + Arrays.toString(result2)); // 期望输出: [0,1,-1]
    }
}