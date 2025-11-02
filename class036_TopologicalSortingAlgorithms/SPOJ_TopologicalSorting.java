package class059;

import java.util.*;

/**
 * SPOJ TOPOSORT - Topological Sorting
 * 
 * 题目描述：
 * 给定一个有向无环图，输出其字典序最小的拓扑排序。
 * 如果不存在拓扑排序（图中有环），则输出"Sandro fails."
 * 
 * 解题思路：
 * 这道题要求输出字典序最小的拓扑排序，所以我们需要在Kahn算法的基础上做一些修改：
 * 1. 使用优先队列（最小堆）而不是普通队列来存储入度为0的节点
 * 2. 每次从优先队列中取出编号最小的节点
 * 3. 如果最终结果的节点数小于图中节点总数，说明图中有环
 * 
 * 算法步骤：
 * 1. 计算每个节点的入度
 * 2. 将所有入度为0的节点加入优先队列
 * 3. 不断从优先队列中取出编号最小的节点，加入结果序列
 * 4. 将该节点的所有邻居节点入度减1
 * 5. 如果邻居节点入度变为0，则加入优先队列
 * 6. 重复3-5直到队列为空
 * 7. 检查结果序列长度是否等于节点总数
 * 
 * 时间复杂度：O(V log V + E)
 * 空间复杂度：O(V + E)
 * 
 * 测试链接：https://www.spoj.com/problems/TOPOSORT/
 */
public class SPOJ_TopologicalSorting {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt(); // 节点数量
        int m = scanner.nextInt(); // 边的数量
        
        // 构建邻接表
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 入度数组
        int[] inDegree = new int[n + 1];
        
        // 读取边的信息
        for (int i = 0; i < m; i++) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            graph.get(from).add(to);
            inDegree[to]++;
        }
        
        // 拓扑排序（字典序最小）
        List<Integer> result = topologicalSortLexicographically(graph, inDegree, n);
        
        // 输出结果
        if (result.size() != n) {
            System.out.println("Sandro fails.");
        } else {
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