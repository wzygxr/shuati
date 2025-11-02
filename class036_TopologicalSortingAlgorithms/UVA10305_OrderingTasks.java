package class059;

import java.util.*;

/**
 * UVA 10305 - Ordering Tasks
 * 
 * 题目描述：
 * 给定n个任务和m个任务之间的先后顺序关系，要求输出一个满足所有约束条件的任务执行顺序。
 * 
 * 解题思路：
 * 这是一道经典的拓扑排序模板题。我们可以使用Kahn算法来解决：
 * 1. 计算每个节点的入度
 * 2. 将所有入度为0的节点加入队列
 * 3. 不断从队列中取出节点，将其加入结果序列，并将其所有邻居节点的入度减1
 * 4. 如果邻居节点的入度变为0，则将其加入队列
 * 5. 重复步骤3-4直到队列为空
 * 
 * 时间复杂度：O(V + E)，其中V是节点数，E是边数
 * 空间复杂度：O(V + E)
 * 
 * 测试链接：https://vjudge.net/problem/UVA-10305
 */
public class UVA10305_OrderingTasks {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			int n = scanner.nextInt(); // 任务数量
			int m = scanner.nextInt(); // 约束关系数量
			
			// 输入结束条件
			if (n == 0 && m == 0) {
				break;
			}
			
			// 构建邻接表
			List<List<Integer>> graph = new ArrayList<>();
			for (int i = 0; i <= n; i++) {
				graph.add(new ArrayList<>());
			}
			
			// 入度数组
			int[] inDegree = new int[n + 1];
			
			// 读取约束关系
			for (int i = 0; i < m; i++) {
				int u = scanner.nextInt();
				int v = scanner.nextInt();
				graph.get(u).add(v);
				inDegree[v]++;
			}
			
			// 拓扑排序
			List<Integer> result = topologicalSort(graph, inDegree, n);
			
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
	 * 拓扑排序函数
	 * @param graph 邻接表表示的图
	 * @param inDegree 入度数组
	 * @param n 节点数量
	 * @return 拓扑排序结果
	 */
	public static List<Integer> topologicalSort(List<List<Integer>> graph, int[] inDegree, int n) {
		List<Integer> result = new ArrayList<>();
		Queue<Integer> queue = new LinkedList<>();
		
		// 将所有入度为0的节点加入队列
		for (int i = 1; i <= n; i++) {
			if (inDegree[i] == 0) {
				queue.offer(i);
			}
		}
		
		// Kahn算法进行拓扑排序
		while (!queue.isEmpty()) {
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