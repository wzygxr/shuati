package class032;

import java.util.*;

/**
 * 旅行商问题（TSP）第二部分 - 记忆化搜索和近似算法
 */

public class Code16_TravelingSalesman_part2 {
    
    /**
     * 方法3: 记忆化搜索版本
     * 使用递归+记忆化，代码更清晰
     */
    public static int tspMemo(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        int[][] memo = new int[1 << n][n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        
        return dfs(1, 0, n, graph, memo);
    }
    
    private static int dfs(int mask, int last, int n, int[][] graph, int[][] memo) {
        // 如果已经访问所有城市，返回回到起点的距离
        if (mask == (1 << n) - 1) {
            return graph[last][0];
        }
        
        if (memo[mask][last] != -1) {
            return memo[mask][last];
        }
        
        int minDistance = Integer.MAX_VALUE;
        
        for (int next = 0; next < n; next++) {
            // 如果next已经在mask中，跳过
            if ((mask & (1 << next)) != 0) continue;
            
            int newMask = mask | (1 << next);
            int distance = graph[last][next] + dfs(newMask, next, n, graph, memo);
            
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        
        memo[mask][last] = minDistance;
        return minDistance;
    }
    
    /**
     * 方法4: 最近邻启发式算法（近似解）
     * 贪心算法，每次选择最近的未访问城市
     * 时间复杂度: O(n^2)，空间复杂度: O(n)
     */
    public static int tspNearestNeighbor(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        boolean[] visited = new boolean[n];
        visited[0] = true;  // 从城市0开始
        int current = 0;
        int totalDistance = 0;
        int count = 1;
        
        while (count < n) {
            int next = -1;
            int minDist = Integer.MAX_VALUE;
            
            // 找到最近的未访问城市
            for (int i = 0; i < n; i++) {
                if (!visited[i] && graph[current][i] < minDist) {
                    minDist = graph[current][i];
                    next = i;
                }
            }
            
            if (next == -1) break;
            
            totalDistance += minDist;
            visited[next] = true;
            current = next;
            count++;
        }
        
        // 回到起点
        totalDistance += graph[current][0];
        return totalDistance;
    }
    
    /**
     * 方法5: 2-opt局部搜索算法
     * 通过交换边来改进解质量
     */
    public static int tsp2Opt(int[][] graph) {
        int n = graph.length;
        if (n <= 1) return 0;
        
        // 初始解：使用最近邻算法
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[n];
        int current = 0;
        tour.add(current);
        visited[current] = true;
        
        while (tour.size() < n) {
            int next = -1;
            int minDist = Integer.MAX_VALUE;
            
            for (int i = 0; i < n; i++) {
                if (!visited[i] && graph[current][i] < minDist) {
                    minDist = graph[current][i];
                    next = i;
                }
            }
            
            if (next == -1) break;
            
            tour.add(next);
            visited[next] = true;
            current = next;
        }
        
        tour.add(0);  // 回到起点
        
        // 2-opt优化
        boolean improved = true;
        while (improved) {
            improved = false;
            
            for (int i = 1; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int oldDistance = calculateTourDistance(graph, tour);
                    
                    // 尝试交换边 (i-1, i) 和 (j, j+1) 为 (i-1, j) 和 (i, j+1)
                    Collections.reverse(tour.subList(i, j + 1));
                    
                    int newDistance = calculateTourDistance(graph, tour);
                    
                    if (newDistance < oldDistance) {
                        improved = true;
                    } else {
                        // 恢复原状
                        Collections.reverse(tour.subList(i, j + 1));
                    }
                }
            }
        }
        
        return calculateTourDistance(graph, tour);
    }
    
    /**
     * 计算完整路径的距离
     */
    private static int calculateTourDistance(int[][] graph, List<Integer> tour) {
        int distance = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int from = tour.get(i);
            int to = tour.get(i + 1);
            distance += graph[from][to];
        }
        return distance;
    }
    
    // 继续添加测试和工具方法...