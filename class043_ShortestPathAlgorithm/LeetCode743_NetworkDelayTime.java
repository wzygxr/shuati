package class143;

import java.util.*;

/**
 * LeetCode 743 - 网络延迟时间
 * 题目描述：
 * 有 n 个网络节点，标记为 1 到 n。
 * 给你一个列表 times，表示信号经过 有向 边的传递时间。times[i] = (u_i, v_i, w_i)，
 * 其中 u_i 是源节点，v_i 是目标节点，w_i 是一个信号从源节点传递到目标节点的时间。
 * 现在，从某个节点 k 发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1。
 * 
 * 算法：Dijkstra算法
 * 时间复杂度：O((V + E) * log V)，其中V是节点数，E是边数
 * 空间复杂度：O(V + E)
 * 
 * 相关题目链接：
 * 1. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
 * 2. LeetCode 787. K 站中转内最便宜的航班 - https://leetcode.cn/problems/cheapest-flights-within-k-stops/
 * 3. 洛谷 P4779 单源最短路径 - https://www.luogu.com.cn/problem/P4779
 * 4. POJ 2387 Til the Cows Come Home - http://poj.org/problem?id=2387
 * 5. Codeforces 20C Dijkstra? - https://codeforces.com/problemset/problem/20/C
 * 6. 洛谷 P3371 单源最短路径 - https://www.luogu.com.cn/problem/P3371
 * 7. HDU 2544 最短路 - https://acm.hdu.edu.cn/showproblem.php?pid=2544
 * 8. AtCoder ABC070 D - Transit Tree Path - https://atcoder.jp/contests/abc070/tasks/abc070_d
 * 9. 牛客 NC50439 最短路 - https://ac.nowcoder.com/acm/problem/50439
 * 10. SPOJ SHPATH - https://www.spoj.com/problems/SHPATH/
 * 11. ZOJ 2818 The Traveling Judges Problem - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366818
 * 12. 51Nod 1018 最短路 - https://www.51nod.com/Challenge/Problem.html#problemId=1018
 * 13. 洛谷 P1144 最短路计数 - https://www.luogu.com.cn/problem/P1144
 * 14. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
 * 15. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
 */
public class LeetCode743_NetworkDelayTime {
    
    /**
     * 解决网络延迟时间问题的主函数
     * 算法思路：
     * 1. 这是一道典型的单源最短路径问题，使用Dijkstra算法解决
     * 2. 从源节点k开始，计算到所有其他节点的最短距离
     * 3. 如果存在无法到达的节点，返回-1
     * 4. 否则返回所有最短距离中的最大值
     * 
     * 具体实现：
     * 1. 构建图的邻接表表示
     * 2. 使用优先队列优化的Dijkstra算法计算从节点k到所有节点的最短距离
     * 3. 如果存在无法到达的节点，返回-1
     * 4. 否则返回所有最短距离中的最大值
     * 
     * @param times 边的传递时间列表
     * @param n 节点数量
     * @param k 源节点
     * @return 所有节点都收到信号所需的最短时间，如果无法覆盖所有节点则返回-1
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        // 创建邻接表表示图
        // graph[i] 存储从节点i出发的所有边 [目标节点, 权重]
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 填充邻接表，每个节点存储其连接的边和对应的权重
        for (int[] time : times) {
            int u = time[0];
            int v = time[1];
            int w = time[2];
            // 添加从节点u到节点v的边，权重为w
            graph.get(u).add(new int[]{v, w});
        }
        
        // 使用优先队列（最小堆）实现Dijkstra算法
        // 优先队列中的元素是一个数组 [距离, 节点]
        // 按距离从小到大排序，确保每次取出距离最小的节点
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // 初始化距离数组，初始值设为无穷大
        // dist[i] 表示从源节点k到节点i的最短距离
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        
        // 源节点到自身的距离为0
        dist[k] = 0;
        // 将源节点加入优先队列，距离为0
        pq.offer(new int[]{0, k});
        
        // Dijkstra算法核心逻辑
        while (!pq.isEmpty()) {
            // 取出距离最小的节点
            int[] current = pq.poll();
            int currentDist = current[0];
            int currentNode = current[1];
            
            // 如果当前距离大于已记录的距离，说明这是一个旧的、不是最优的路径，可以跳过
            // 这是为了避免重复处理已经更新过的节点
            if (currentDist > dist[currentNode]) {
                continue;
            }
            
            // 遍历当前节点的所有邻居
            for (int[] neighbor : graph.get(currentNode)) {
                int nextNode = neighbor[0];
                int weight = neighbor[1];
                
                // 计算通过当前节点到达邻居的新距离
                // 新距离 = 当前节点距离 + 当前节点到邻居的边权重
                int newDist = currentDist + weight;
                
                // 如果找到更短的路径，则更新距离并将邻居节点加入优先队列
                if (newDist < dist[nextNode]) {
                    dist[nextNode] = newDist;
                    pq.offer(new int[]{newDist, nextNode});
                }
            }
        }
        
        // 找出所有节点中的最大距离，即为网络延迟时间
        // 这是因为所有节点都收到信号的时间取决于最后一个收到信号的节点
        int maxDelay = 0;
        for (int i = 1; i <= n; i++) {
            // 如果有节点无法到达，返回-1
            if (dist[i] == Integer.MAX_VALUE) {
                return -1;
            }
            // 更新最大延迟时间
            maxDelay = Math.max(maxDelay, dist[i]);
        }
        
        // 返回所有节点都收到信号所需的最短时间
        return maxDelay;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        LeetCode743_NetworkDelayTime solution = new LeetCode743_NetworkDelayTime();
        
        // 测试用例1
        int[][] times1 = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        int n1 = 4;
        int k1 = 2;
        System.out.println("测试用例1结果: " + solution.networkDelayTime(times1, n1, k1)); // 预期输出: 2
        
        // 测试用例2
        int[][] times2 = {{1, 2, 1}};
        int n2 = 2;
        int k2 = 1;
        System.out.println("测试用例2结果: " + solution.networkDelayTime(times2, n2, k2)); // 预期输出: 1
        
        // 测试用例3
        int[][] times3 = {{1, 2, 1}};
        int n3 = 2;
        int k3 = 2;
        System.out.println("测试用例3结果: " + solution.networkDelayTime(times3, n3, k3)); // 预期输出: -1
    }
}