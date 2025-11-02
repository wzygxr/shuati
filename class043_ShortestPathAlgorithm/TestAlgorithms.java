package class143;

// 算法测试类
// 用于测试class143中实现的各种最短路算法

import java.util.*;

/**
 * 算法测试类
 * 用于测试class143中实现的各种最短路算法
 * 
 * 相关题目链接：
 * 1. LeetCode 743. Network Delay Time (Dijkstra算法)
 *    题目链接：https://leetcode.cn/problems/network-delay-time/
 *    题解链接：https://leetcode.cn/problems/network-delay-time/solution/
 * 
 * 2. LeetCode 542. 01 Matrix (01-BFS)
 *    题目链接：https://leetcode.cn/problems/01-matrix/
 *    题解链接：https://leetcode.cn/problems/01-matrix/solution/
 * 
 * 3. LeetCode 773. Sliding Puzzle (BFS)
 *    题目链接：https://leetcode.cn/problems/sliding-puzzle/
 *    题解链接：https://leetcode.cn/problems/sliding-puzzle/solution/
 * 
 * 4. 洛谷 P3371 【模板】单源最短路径（弱化版）(Dijkstra算法)
 *    题目链接：https://www.luogu.com.cn/problem/P3371
 * 
 * 5. 洛谷 P4779 【模板】单源最短路径（标准版）(Dijkstra算法)
 *    题目链接：https://www.luogu.com.cn/problem/P4779
 * 
 * 6. AtCoder Regular Contest 084 D - Small Multiple (同余最短路)
 *    题目链接：https://atcoder.jp/contests/arc084/tasks/arc084_b
 * 
 * 7. Codeforces 1063B Labyrinth (01-BFS)
 *    题目链接：https://codeforces.com/problemset/problem/1063/B
 * 
 * 8. LeetCode 2290. 到达角落需要移除障碍物的最小数目 (01-BFS)
 *    题目链接：https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/
 * 
 * 9. LeetCode 1368. 使网格图至少有一条有效路径的最小代价 (01-BFS)
 *    题目链接：https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
 * 
 * 10. CSP-J 2023 T4 旅游巴士 (同余最短路)
 *     题目链接：https://www.luogu.com.cn/problem/P9751
 * 
 * 11. LibreOJ #10072. 「一本通 3.2 练习 4」新年好 (最短路)
 *     题目链接：https://loj.ac/p/10072
 * 
 * 12. 洛谷 P1144 最短路计数 (最短路)
 *     题目链接：https://www.luogu.com.cn/problem/P1144
 * 
 * 13. POJ 1723 SOLDIERS (类似思想)
 *     题目链接：http://poj.org/problem?id=1723
 * 
 * 14. 洛谷 P2512 [HAOI2008] 糖果传递 (同余最短路)
 *     题目链接：https://www.luogu.com.cn/problem/P2512
 * 
 * 15. 洛谷 P3403 跳楼机 (同余最短路)
 *     题目链接：https://www.luogu.com.cn/problem/P3403
 */
public class TestAlgorithms {
    
    // 测试Dijkstra算法
    public static void testDijkstra() {
        System.out.println("=== 测试Dijkstra算法 ===");
        
        // 创建测试图
        // 图结构：
        // 0 -> 1 (权值10)
        // 0 -> 2 (权值5)
        // 1 -> 2 (权值2)
        // 1 -> 3 (权值1)
        // 2 -> 1 (权值3)
        // 2 -> 3 (权值9)
        // 2 -> 4 (权值2)
        // 3 -> 4 (权值4)
        // 4 -> 0 (权值7)
        // 4 -> 3 (权值6)
        
        int n = 5; // 节点数
        List<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 添加边
        graph[0].add(new Edge(1, 10));
        graph[0].add(new Edge(2, 5));
        graph[1].add(new Edge(2, 2));
        graph[1].add(new Edge(3, 1));
        graph[2].add(new Edge(1, 3));
        graph[2].add(new Edge(3, 9));
        graph[2].add(new Edge(4, 2));
        graph[3].add(new Edge(4, 4));
        graph[4].add(new Edge(0, 7));
        graph[4].add(new Edge(3, 6));
        
        // 测试从节点0开始的最短路径
        int[] dist = dijkstra(graph, 0);
        System.out.println("从节点0到各节点的最短距离：");
        for (int i = 0; i < n; i++) {
            System.out.println("到节点" + i + "的距离：" + dist[i]);
        }
        
        // 预期结果：
        // 到节点0的距离：0
        // 到节点1的距离：5
        // 到节点2的距离：5
        // 到节点3的距离：6
        // 到节点4的距离：7
    }
    
    // Dijkstra算法实现
    public static int[] dijkstra(List<Edge>[] graph, int start) {
        int n = graph.length;
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> a.dist - b.dist);
        pq.offer(new Node(start, 0));
        
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            int u = curr.id;
            
            if (visited[u]) {
                continue;
            }
            
            visited[u] = true;
            
            for (Edge edge : graph[u]) {
                int v = edge.to;
                int w = edge.weight;
                
                if (!visited[v] && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new Node(v, dist[v]));
                }
            }
        }
        
        return dist;
    }
    
    // 测试01-BFS算法
    public static void testZeroOneBFS() {
        System.out.println("\n=== 测试01-BFS算法 ===");
        
        // 创建测试网格
        // 0表示空地，1表示墙
        // 从(0,0)到(2,2)的最短路径
        int[][] grid = {
            {0, 0, 1},
            {1, 0, 0},
            {0, 0, 0}
        };
        
        int result = zeroOneBFS(grid);
        System.out.println("从(0,0)到(2,2)的最短路径长度：" + result);
        
        // 预期结果：4
    }
    
    // 01-BFS算法实现（简化版）
    public static int zeroOneBFS(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dist = new int[m][n];
        boolean[][] visited = new boolean[m][n];
        
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            Arrays.fill(visited[i], false);
        }
        
        ArrayDeque<int[]> deque = new ArrayDeque<>();
        dist[0][0] = 0;
        deque.addFirst(new int[]{0, 0, 0});
        
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        
        while (!deque.isEmpty()) {
            int[] curr = deque.pollFirst();
            int x = curr[0];
            int y = curr[1];
            int d = curr[2];
            
            if (visited[x][y]) {
                continue;
            }
            
            visited[x][y] = true;
            
            if (x == m - 1 && y == n - 1) {
                return d;
            }
            
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || grid[nx][ny] == 1) {
                    continue;
                }
                
                int cost = 1; // 假设每步代价为1
                
                if (!visited[nx][ny] && d + cost < dist[nx][ny]) {
                    dist[nx][ny] = d + cost;
                    if (cost == 0) {
                        deque.addFirst(new int[]{nx, ny, d + cost});
                    } else {
                        deque.addLast(new int[]{nx, ny, d + cost});
                    }
                }
            }
        }
        
        return -1;
    }
    
    // 测试同余最短路算法
    public static void testModularShortestPath() {
        System.out.println("\n=== 测试同余最短路算法 ===");
        
        // 测试k=3的情况
        int k1 = 3;
        int result1 = modularShortestPath(k1);
        System.out.println("k=" + k1 + "时，结果：" + result1);
        
        // 测试k=7的情况
        int k2 = 7;
        int result2 = modularShortestPath(k2);
        System.out.println("k=" + k2 + "时，结果：" + result2);
        
        // 测试k=5的情况（无解）
        int k3 = 5;
        int result3 = modularShortestPath(k3);
        System.out.println("k=" + k3 + "时，结果：" + result3);
        
        // 预期结果：
        // k=3时，结果：3
        // k=7时，结果：6
        // k=5时，结果：-1
    }
    
    // 同余最短路算法实现（简化版）
    public static int modularShortestPath(int k) {
        if (k == 1) {
            return 1;
        }
        
        if (k % 2 == 0 || k % 5 == 0) {
            return -1;
        }
        
        int[] dist = new int[k];
        boolean[] visited = new boolean[k];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(visited, false);
        
        ArrayDeque<int[]> deque = new ArrayDeque<>();
        dist[1] = 1;
        deque.addFirst(new int[]{1, 1});
        
        while (!deque.isEmpty()) {
            int[] curr = deque.pollFirst();
            int r = curr[0];
            int d = curr[1];
            
            if (visited[r]) {
                continue;
            }
            
            visited[r] = true;
            
            if (r == 0) {
                return d;
            }
            
            int newRemainder = (r * 10 + 1) % k;
            if (!visited[newRemainder] && d + 1 < dist[newRemainder]) {
                dist[newRemainder] = d + 1;
                deque.addLast(new int[]{newRemainder, d + 1});
            }
        }
        
        return -1;
    }
    
    public static void main(String[] args) {
        // 运行所有测试
        testDijkstra();
        testZeroOneBFS();
        testModularShortestPath();
    }
    
    // 边的定义
    static class Edge {
        int to, weight;
        
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    // 节点的定义
    static class Node {
        int id, dist;
        
        Node(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }
    }
}