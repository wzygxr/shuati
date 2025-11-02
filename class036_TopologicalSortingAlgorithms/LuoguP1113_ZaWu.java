package class059;

import java.util.*;

/**
 * 洛谷 P1113 杂务
 * 
 * 题目描述：
 * John的农场在给牛奶加工时，要把一些杂务完成。这些杂务可以形成一个有向无环图，
 * 每个杂务都有一个完成时间，某些杂务必须在一些杂务完成之后才能进行。
 * 请你帮John计算一下完成所有杂务需要的最少时间。
 * 
 * 解题思路：
 * 这道题是最长路径的拓扑排序问题。每个杂务都有一个执行时间，我们需要计算从开始
 * 到完成所有杂务的最短时间，也就是所有杂务完成时间的最大值。
 * 
 * 算法步骤：
 * 1. 使用拓扑排序处理依赖关系
 * 2. 对于每个节点，计算其最早开始时间 = max(所有前驱节点的完成时间)
 * 3. 节点的完成时间 = 最早开始时间 + 执行时间
 * 4. 所有节点完成时间的最大值就是答案
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1113
 */
public class LuoguP1113_ZaWu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt(); // 杂务数量
        
        // 存储每个杂务的信息
        int[] times = new int[n + 1];           // 每个杂务的执行时间
        int[] earliestStart = new int[n + 1];   // 每个杂务的最早开始时间
        int[] finishTime = new int[n + 1];      // 每个杂务的完成时间
        
        // 构建邻接表
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 入度数组
        int[] inDegree = new int[n + 1];
        
        // 读取每个杂务的信息
        for (int i = 1; i <= n; i++) {
            int id = scanner.nextInt();         // 杂务编号
            times[id] = scanner.nextInt();      // 执行时间
            
            // 读取依赖的杂务编号，以0结尾
            int dependency;
            while ((dependency = scanner.nextInt()) != 0) {
                graph.get(dependency).add(id);
                inDegree[id]++;
            }
        }
        
        // 拓扑排序计算最早完成时间
        int result = topologicalSortForLatestTime(graph, inDegree, times, earliestStart, finishTime, n);
        
        // 输出结果
        System.out.println(result);
        
        scanner.close();
    }
    
    /**
     * 拓扑排序计算完成所有杂务的最长时间
     * @param graph 邻接表
     * @param inDegree 入度数组
     * @param times 每个杂务的执行时间
     * @param earliestStart 每个杂务的最早开始时间
     * @param finishTime 每个杂务的完成时间
     * @param n 杂务数量
     * @return 完成所有杂务的最短时间
     */
    public static int topologicalSortForLatestTime(
            List<List<Integer>> graph, int[] inDegree, int[] times, 
            int[] earliestStart, int[] finishTime, int n) {
        Queue<Integer> queue = new LinkedList<>();
        
        // 将所有入度为0的节点加入队列
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int maxFinishTime = 0;
        
        // Kahn算法进行拓扑排序
        while (!queue.isEmpty()) {
            // 取出当前节点
            int current = queue.poll();
            
            // 计算当前节点的完成时间
            finishTime[current] = earliestStart[current] + times[current];
            maxFinishTime = Math.max(maxFinishTime, finishTime[current]);
            
            // 遍历当前节点的所有邻居
            for (int neighbor : graph.get(current)) {
                // 更新邻居节点的最早开始时间
                earliestStart[neighbor] = Math.max(earliestStart[neighbor], finishTime[current]);
                
                // 将邻居节点的入度减1
                inDegree[neighbor]--;
                // 如果邻居节点的入度变为0，则加入队列
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return maxFinishTime;
    }
}