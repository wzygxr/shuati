package class062;

import java.util.*;

// 颜色交替的最短路径
// 题目描述：给定一个有向图，节点分别是红色和蓝色两种颜色的边，求从节点0到所有其他节点的颜色交替的最短路径长度
// LeetCode题目链接：https://leetcode.cn/problems/shortest-path-with-alternating-colors/
// 
// 算法思路：
// 使用0-1 BFS的变体，这里边的颜色作为权重的一种表示
// 我们需要记录到达每个节点时使用的最后一条边的颜色，以确保颜色交替
// 
// 时间复杂度：O(V + E)，其中V是节点数，E是边数
// 空间复杂度：O(V + E)，用于存储图的邻接表和距离数组
// 
// 工程化考量：
// 1. 异常处理：处理空图的情况
// 2. 数据结构选择：使用邻接表存储图，使用双端队列实现0-1 BFS
// 3. 状态表示：使用距离数组记录到达每个节点时的最后一条边颜色
public class Code16_ShortestPathWithAlternatingColors {
    
    // 表示边的颜色
    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int NO_COLOR = -1;
    
    public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        // 构建邻接表，每个节点存储两种颜色的边
        List<List<Integer>>[] graph = new List[2];
        graph[RED] = new ArrayList<>();
        graph[BLUE] = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            graph[RED].add(new ArrayList<>());
            graph[BLUE].add(new ArrayList<>());
        }
        
        // 添加红色边
        for (int[] edge : redEdges) {
            int from = edge[0];
            int to = edge[1];
            graph[RED].get(from).add(to);
        }
        
        // 添加蓝色边
        for (int[] edge : blueEdges) {
            int from = edge[0];
            int to = edge[1];
            graph[BLUE].get(from).add(to);
        }
        
        // 初始化距离数组，dist[i][j]表示到达节点i时最后一条边颜色为j的最短距离
        // j可以是0(红色)或1(蓝色)，初始值为-1表示不可达
        int[][] dist = new int[n][2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1);
        }
        
        // 使用双端队列实现0-1 BFS
        Deque<int[]> deque = new LinkedList<>();
        
        // 起点是0，初始时没有前一条边，可以选择红色或蓝色作为第一条边
        dist[0][RED] = 0;
        dist[0][BLUE] = 0;
        deque.offerFirst(new int[]{0, RED});
        deque.offerFirst(new int[]{0, BLUE});
        
        while (!deque.isEmpty()) {
            int[] current = deque.pollFirst();
            int node = current[0];
            int color = current[1];
            int currentDist = dist[node][color];
            
            // 下一条边应该是另一种颜色
            int nextColor = color == RED ? BLUE : RED;
            
            // 遍历所有下一种颜色的边
            for (int nextNode : graph[nextColor].get(node)) {
                // 如果该路径未被访问过，或者找到更短的路径
                if (dist[nextNode][nextColor] == -1) {
                    dist[nextNode][nextColor] = currentDist + 1;
                    // 添加到队列前端，因为权重为1（每条边的权重相同）
                    deque.offerLast(new int[]{nextNode, nextColor});
                }
            }
        }
        
        // 构建最终结果，对于每个节点，取两种颜色路径中的最小值
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            if (dist[i][RED] == -1 && dist[i][BLUE] == -1) {
                result[i] = -1; // 两种颜色都不可达
            } else if (dist[i][RED] == -1) {
                result[i] = dist[i][BLUE];
            } else if (dist[i][BLUE] == -1) {
                result[i] = dist[i][RED];
            } else {
                result[i] = Math.min(dist[i][RED], dist[i][BLUE]);
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code16_ShortestPathWithAlternatingColors solution = new Code16_ShortestPathWithAlternatingColors();
        
        // 测试用例1
        int n1 = 3;
        int[][] redEdges1 = {{0,1}, {1,2}};
        int[][] blueEdges1 = {};
        int[] result1 = solution.shortestAlternatingPaths(n1, redEdges1, blueEdges1);
        System.out.print("测试用例1结果：");
        for (int num : result1) {
            System.out.print(num + " ");
        }
        System.out.println(); // 预期输出: 0 1 -1
        
        // 测试用例2
        int n2 = 3;
        int[][] redEdges2 = {{0,1}};
        int[][] blueEdges2 = {{2,1}};
        int[] result2 = solution.shortestAlternatingPaths(n2, redEdges2, blueEdges2);
        System.out.print("测试用例2结果：");
        for (int num : result2) {
            System.out.print(num + " ");
        }
        System.out.println(); // 预期输出: 0 1 -1
    }
}