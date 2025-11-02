package class059;

import java.util.*;

/**
 * HDU 1285 - 确定比赛名次
 * 
 * 题目描述：
 * 有N个比赛队（1<=N<500），编号依次为1，2，3，...，N进行比赛，比赛结束后，
 * 裁判委员会要将所有参赛队伍从前往后依次排名，但现在裁判委员会不能直接获得
 * 每个队的比赛成绩，只知道每场比赛的结果，即P1赢P2，用P1,P2表示，排名时P1在P2之前。
 * 现在请你编程序确定排名。
 * 
 * 注意：符合条件的排名可能不是唯一的，此时要求输出时编号小的队伍在前；
 * 输入数据保证是正确的，即输入数据确保一定能有一个符合要求的排名。
 * 
 * 解题思路：
 * 这是一道典型的拓扑排序题，但要求输出字典序最小的拓扑序列。
 * 为了实现字典序最小，我们在选择入度为0的节点时，使用优先队列（最小堆），
 * 每次选择编号最小的节点。
 * 
 * 算法步骤：
 * 1. 构建图和入度数组
 * 2. 将所有入度为0的节点加入优先队列
 * 3. 不断从优先队列中取出编号最小的节点，加入结果序列
 * 4. 将该节点的所有邻居节点入度减1
 * 5. 如果邻居节点入度变为0，则加入优先队列
 * 6. 重复3-5直到队列为空
 * 
 * 时间复杂度：O(V log V + E)，优先队列操作的复杂度
 * 空间复杂度：O(V + E)
 * 
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=1285
 */
public class HDU1285_DetermineTheRanking {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNext()) {
            int n = scanner.nextInt(); // 队伍数量
            int m = scanner.nextInt(); // 比赛结果数量
            
            // 构建邻接表
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            
            // 入度数组
            int[] inDegree = new int[n + 1];
            
            // 读取比赛结果
            for (int i = 0; i < m; i++) {
                int winner = scanner.nextInt();
                int loser = scanner.nextInt();
                graph.get(winner).add(loser);
                inDegree[loser]++;
            }
            
            // 拓扑排序（字典序最小）
            List<Integer> result = topologicalSortLexicographically(graph, inDegree, n);
            
            // 输出结果
            for (int i = 0; i < result.size(); i++) {
                if (i > 0) {
                    System.out.print(" ");
                }
                System.out.print(result.get(i));
            }
            System.out.println();
        }
        
        scanner.close();
    }
    
    /**
     * 字典序最小的拓扑排序
     * @param graph 邻接表
     * @param inDegree 入度数组
     * @param n 节点数量
     * @return 拓扑排序结果（字典序最小）
     */
    public static List<Integer> topologicalSortLexicographically(
            List<List<Integer>> graph, int[] inDegree, int n) {
        List<Integer> result = new ArrayList<>();
        // 使用优先队列（最小堆）保证每次取编号最小的节点
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        
        // 将所有入度为0的节点加入优先队列
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // Kahn算法进行拓扑排序
        while (!queue.isEmpty()) {
            // 取出编号最小的节点
            int current = queue.poll();
            result.add(current);
            
            // 遍历当前节点的所有邻居
            for (int neighbor : graph.get(current)) {
                // 将邻居节点的入度减1
                inDegree[neighbor]--;
                // 如果邻居节点的入度变为0，则加入队列
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return result;
    }
}