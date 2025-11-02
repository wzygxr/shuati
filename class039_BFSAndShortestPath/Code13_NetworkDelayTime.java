package class062;

import java.util.ArrayList;
import java.util.PriorityQueue;

// 网络延迟时间
// 有 n 个网络节点，标记为 1 到 n
// 给你一个列表 times ，表示信号经过有向边的传递时间
// times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，wi 是一个信号从源节点传递到目标节点的时间
// 现在，从某个节点 K 发出一个信号
// 需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1
// 测试链接 : https://leetcode.com/problems/network-delay-time/
// 
// 算法思路：
// 使用优先队列BFS（Dijkstra算法）解决单源最短路径问题
// 从节点K开始，计算到所有其他节点的最短传输时间
// 最终结果是所有节点中最长的传输时间
// 
// 时间复杂度：O(E log V)，其中E是边数，V是节点数
// 空间复杂度：O(V + E)，用于存储图和优先队列
// 
// 工程化考量：
// 1. 图的表示：使用邻接表存储有向图
// 2. 优先队列：使用最小堆维护当前距离最小的节点
// 3. 结果验证：检查是否所有节点都能到达
public class Code13_NetworkDelayTime {

	// 图的节点数上限
	public static int MAXN = 101;
	
	// 图的邻接表表示
	public static ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
	
	// 距离数组，distance[i]表示从节点K到节点i的最短时间
	public static int[] distance = new int[MAXN];
	
	// 访问状态数组
	public static boolean[] visited = new boolean[MAXN];
	
	// 优先队列，存储[节点, 距离]，按距离排序
	public static PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
	
	static {
		for (int i = 0; i < MAXN; i++) {
			graph.add(new ArrayList<>());
		}
	}

	public static int networkDelayTime(int[][] times, int n, int k) {
		// 初始化图
		for (int i = 1; i <= n; i++) {
			graph.get(i).clear();
		}
		
		// 构建邻接表
		for (int[] time : times) {
			int u = time[0];
			int v = time[1];
			int w = time[2];
			graph.get(u).add(new int[] { v, w });
		}
		
		// 初始化距离数组和访问状态数组
		for (int i = 1; i <= n; i++) {
			distance[i] = Integer.MAX_VALUE;
			visited[i] = false;
		}
		
		// 起点距离为0
		distance[k] = 0;
		heap.clear();
		heap.add(new int[] { k, 0 });
		
		// Dijkstra算法
		while (!heap.isEmpty()) {
			// 取出距离最小的节点
			int[] record = heap.poll();
			int u = record[0];
			int dist = record[1];
			
			// 如果已经访问过，跳过
			if (visited[u]) {
				continue;
			}
			
			visited[u] = true;
			
			// 更新相邻节点的距离
			for (int[] edge : graph.get(u)) {
				int v = edge[0];
				int w = edge[1];
				// 如果通过节点u到达节点v的距离更短，则更新
				if (!visited[v] && dist + w < distance[v]) {
					distance[v] = dist + w;
					heap.add(new int[] { v, distance[v] });
				}
			}
		}
		
		// 计算最大延迟时间
		int maxDelay = 0;
		for (int i = 1; i <= n; i++) {
			if (distance[i] == Integer.MAX_VALUE) {
				// 存在无法到达的节点
				return -1;
			}
			maxDelay = Math.max(maxDelay, distance[i]);
		}
		
		return maxDelay;
	}

}