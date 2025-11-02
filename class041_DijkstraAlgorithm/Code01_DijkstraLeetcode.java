package class064;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Dijkstra算法模版（LeetCode）
 * 
 * 题目：网络延迟时间
 * 链接：https://leetcode.cn/problems/network-delay-time
 * 
 * 题目描述：
 * 有 n 个网络节点，标记为 1 到 n。
 * 给你一个列表 times，表示信号经过 有向 边的传递时间。
 * times[i] = (ui, vi, wi)，表示从ui到vi传递信号的时间是wi。
 * 现在，从某个节点 s 发出一个信号。
 * 需要多久才能使所有节点都收到信号？
 * 如果不能使所有节点收到信号，返回 -1。
 * 
 * 解题思路：
 * 这是一个典型的单源最短路径问题，可以使用Dijkstra算法解决。
 * 1. 构建图的邻接表表示
 * 2. 使用优先队列实现Dijkstra算法
 * 3. 计算从源节点到所有其他节点的最短距离
 * 4. 返回所有最短距离中的最大值，即为网络延迟时间
 * 
 * 算法应用场景：
 * - 网络路由协议
 * - GPS导航系统
 * - 社交网络中计算影响力传播时间
 * 
 * 时间复杂度分析：
 * - 方法1 (动态建图+普通堆)：O((V+E)logV)，其中V是节点数，E是边数
 * - 方法2 (链式前向星+反向索引堆)：O((V+E)logV)
 * 
 * 空间复杂度分析：
 * - 方法1：O(V+E)，存储图和距离数组
 * - 方法2：O(V+E)，存储链式前向星和反向索引堆
 */
public class Code01_DijkstraLeetcode {

	/**
	 * 方法1：动态建图+普通堆的实现
	 * 
	 * 算法步骤：
	 * 1. 构建邻接表表示的图
	 * 2. 初始化距离数组，源节点距离为0，其他节点为无穷大
	 * 3. 使用优先队列维护待处理节点，按距离从小到大排序
	 * 4. 不断取出距离最小的节点，更新其邻居节点的最短距离
	 * 5. 最后检查是否所有节点都能到达，返回最大距离
	 * 
	 * 时间复杂度：O((V+E)logV)
	 * 空间复杂度：O(V+E)
	 * 
	 * @param times 有向边的权重信息，times[i] = (ui, vi, wi)
	 * @param n 节点总数
	 * @param s 源节点
	 * @return 网络延迟时间，如果不能使所有节点收到信号则返回-1
	 */
	public static int networkDelayTime1(int[][] times, int n, int s) {
		// 构建邻接表表示的图
		// graph[i] 存储节点i的所有邻居节点及其边权重
		ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
		for (int i = 0; i <= n; i++) {
			graph.add(new ArrayList<>());
		}
		// 添加边到图中
		// 对于每条边 (u, v, w)，将其添加到u的邻居列表中
		for (int[] edge : times) {
			graph.get(edge[0]).add(new int[] { edge[1], edge[2] });
		}
		
		// distance[i] 表示从源节点s到节点i的最短距离
		int[] distance = new int[n + 1];
		// 初始化距离为无穷大，表示尚未访问
		Arrays.fill(distance, Integer.MAX_VALUE);
		// 源节点到自己的距离为0
		distance[s] = 0;
		
		// visited[i] 表示节点i是否已经确定了最短距离
		// 用于避免重复处理已经确定最短距离的节点
		boolean[] visited = new boolean[n + 1];
		
		// 优先队列，按距离从小到大排序
		// 数组含义：[0] 当前节点编号，[1] 源点到当前点距离
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		// 将源节点加入优先队列，距离为0
		heap.add(new int[] { s, 0 });
		
		// Dijkstra算法主循环
		while (!heap.isEmpty()) {
			// 取出距离源点最近的节点
			// 由于使用优先队列，第一个元素总是距离最小的节点
			int u = heap.poll()[0];
			// 如果已经处理过，跳过
			// 这是为了避免同一节点多次处理导致的重复计算
			if (visited[u]) {
				continue;
			}
			// 标记为已处理，表示已确定从源节点到该节点的最短距离
			visited[u] = true;
			
			// 遍历u的所有邻居节点
			// 对于每个邻居节点v，检查是否可以通过u节点获得更短的路径
			for (int[] edge : graph.get(u)) {
				int v = edge[0];  // 邻居节点
				int w = edge[1];  // 边的权重 (u到v的距离)
				
				// 如果邻居节点未访问且通过u到达v的距离更短，则更新
				// 松弛操作：如果 distance[u] + w < distance[v]，则更新distance[v]
				if (!visited[v] && distance[u] + w < distance[v]) {
					distance[v] = distance[u] + w;
					// 将更新后的节点加入优先队列
					heap.add(new int[] { v, distance[u] + w });
				}
			}
		}
		
		// 找到最大的最短距离
		// 这个最大值就是网络延迟时间，即所有节点收到信号所需的最长时间
		int ans = Integer.MIN_VALUE;
		for (int i = 1; i <= n; i++) {
			// 如果有节点无法到达，返回-1
			// 如果distance[i]仍为初始值Integer.MAX_VALUE，说明节点i不可达
			if (distance[i] == Integer.MAX_VALUE) {
				return -1;
			}
			// 更新最大距离
			ans = Math.max(ans, distance[i]);
		}
		// 返回网络延迟时间
		return ans;
	}

	/**
	 * 方法2：链式前向星+反向索引堆的实现
	 * 
	 * 算法优化点：
	 * 1. 使用链式前向星存储图，节省空间
	 * 2. 使用反向索引堆优化优先队列操作
	 * 
	 * 时间复杂度：O((V+E)logV)
	 * 空间复杂度：O(V+E)
	 * 
	 * @param times 有向边的权重信息，times[i] = (ui, vi, wi)
	 * @param n 节点总数
	 * @param s 源节点
	 * @return 网络延迟时间，如果不能使所有节点收到信号则返回-1
	 */
	public static int networkDelayTime2(int[][] times, int n, int s) {
		// 初始化数据结构
		build(n);
		// 构建链式前向星图
		for (int[] edge : times) {
			addEdge(edge[0], edge[1], edge[2]);
		}
		// 初始化源点，将源节点加入堆中，距离为0
		addOrUpdateOrIgnore(s, 0);
		
		// Dijkstra算法主循环
		while (!isEmpty()) {
			// 弹出距离最小的节点
			int u = pop();
			// 遍历u的所有出边
			// ei是边的索引，通过head[u]获取第一条边，通过next[ei]获取下一条边
			for (int ei = head[u]; ei > 0; ei = next[ei]) {
				// 更新邻居节点的最短距离
				// to[ei]是边的终点，distance[u] + weight[ei]是通过当前边到达终点的距离
				addOrUpdateOrIgnore(to[ei], distance[u] + weight[ei]);
			}
		}
		
		// 计算结果
		int ans = Integer.MIN_VALUE;
		for (int i = 1; i <= n; i++) {
			// 如果有节点无法到达，返回-1
			if (distance[i] == Integer.MAX_VALUE) {
				return -1;
			}
			// 更新最大距离
			ans = Math.max(ans, distance[i]);
		}
		return ans;
	}

	// 链式前向星和反向索引堆的相关数据结构和方法
	
	// 最大节点数和边数限制
	public static int MAXN = 101;
	public static int MAXM = 6001;

	// 链式前向星数据结构
	// head[i] 存储节点i的第一条边的索引
	public static int[] head = new int[MAXN];
	// next[i] 存储第i条边的下一条边的索引
	public static int[] next = new int[MAXM];
	// to[i] 存储第i条边的终点
	public static int[] to = new int[MAXM];
	// weight[i] 存储第i条边的权重
	public static int[] weight = new int[MAXM];
	// 边的计数器
	public static int cnt;

	// 反向索引堆数据结构
	// heap[i] 存储堆中第i个位置的节点编号
	public static int[] heap = new int[MAXN];
	// where[v] 存储节点v在堆中的位置
	// where[v] = -1，表示v这个节点，从来没有进入过堆
	// where[v] = -2，表示v这个节点，已经弹出过了
	// where[v] = i(>=0)，表示v这个节点，在堆上的i位置
	public static int[] where = new int[MAXN];
	// 堆的大小
	public static int heapSize;
	// distance[i] 存储从源节点到节点i的最短距离
	public static int[] distance = new int[MAXN];

	/**
	 * 初始化数据结构
	 * @param n 节点总数
	 */
	public static void build(int n) {
		cnt = 1;  // 边的索引从1开始
		heapSize = 0;  // 堆初始为空
		// 初始化链式前向星头指针
		Arrays.fill(head, 1, n + 1, 0);
		// 初始化反向索引堆
		Arrays.fill(where, 1, n + 1, -1);
		// 初始化距离数组
		Arrays.fill(distance, 1, n + 1, Integer.MAX_VALUE);
	}

	/**
	 * 链式前向星建图
	 * @param u 起点
	 * @param v 终点
	 * @param w 边权重
	 */
	public static void addEdge(int u, int v, int w) {
		next[cnt] = head[u];  // 新边的下一条边指向原来的第一条边
		to[cnt] = v;          // 设置边的终点
		weight[cnt] = w;      // 设置边的权重
		head[u] = cnt++;      // 更新节点u的第一条边为新边
	}

	/**
	 * 向堆中添加节点、更新节点或忽略节点
	 * @param v 节点编号
	 * @param c 距离值
	 */
	public static void addOrUpdateOrIgnore(int v, int c) {
		// 节点从未进入过堆
		if (where[v] == -1) {
			heap[heapSize] = v;        // 将节点加入堆末尾
			where[v] = heapSize++;     // 记录节点在堆中的位置
			distance[v] = c;           // 更新节点距离
			heapInsert(where[v]);      // 向上调整堆
		// 节点已在堆中
		} else if (where[v] >= 0) {
			distance[v] = Math.min(distance[v], c);  // 更新为更小的距离
			heapInsert(where[v]);                    // 向上调整堆
		}
	}

	/**
	 * 堆插入操作（向上调整）
	 * @param i 节点在堆中的位置
	 */
	public static void heapInsert(int i) {
		// 向上调整堆，直到满足堆性质
		while (distance[heap[i]] < distance[heap[(i - 1) / 2]]) {
			swap(i, (i - 1) / 2);  // 交换节点
			i = (i - 1) / 2;       // 更新位置
		}
	}

	/**
	 * 弹出堆顶元素
	 * @return 堆顶节点编号
	 */
	public static int pop() {
		int ans = heap[0];              // 获取堆顶元素
		swap(0, --heapSize);            // 将堆顶与最后一个元素交换
		heapify(0);                     // 向下调整堆
		where[ans] = -2;                // 标记节点已弹出
		return ans;                     // 返回堆顶元素
	}

	/**
	 * 堆化操作（向下调整）
	 * @param i 节点在堆中的位置
	 */
	public static void heapify(int i) {
		// 向下调整堆，直到满足堆性质
		int l = i * 2 + 1;  // 左子节点位置
		while (l < heapSize) {
			// 找到左右子节点中较小的一个
			int best = l + 1 < heapSize && distance[heap[l + 1]] < distance[heap[l]] ? l + 1 : l;
			// 比较父节点与子节点中的较小者
			best = distance[heap[best]] < distance[heap[i]] ? best : i;
			// 如果父节点已经是最小的，则停止调整
			if (best == i) {
				break;
			}
			// 交换节点并继续调整
			swap(best, i);
			i = best;
			l = i * 2 + 1;
		}
	}

	/**
	 * 判断堆是否为空
	 * @return 堆是否为空
	 */
	public static boolean isEmpty() {
		return heapSize == 0;
	}

	/**
	 * 交换堆中两个位置的节点
	 * @param i 位置i
	 * @param j 位置j
	 */
	public static void swap(int i, int j) {
		int tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
		where[heap[i]] = i;  // 更新节点在堆中的位置记录
		where[heap[j]] = j;  // 更新节点在堆中的位置记录
	}

	// 算法总结注释...
	// 1. Dijkstra算法适用于解决单源最短路径问题，要求边权重非负
	// 2. 算法通过贪心策略，每次选择距离源点最近的未访问节点进行处理
	// 3. 使用优先队列可以高效地获取距离最小的节点
	// 4. 通过松弛操作更新邻居节点的最短距离
	// 5. 算法保证每次确定一个节点的最短距离后，该距离不会再被更新
	
	
	// =============================================================================
	// 第K短路问题
	// =============================================================================
	
	/**
	 * 第K短路问题
	 * 
	 * 题目描述：
	 * 给定一个有向图，求从起点s到终点t的第K短路径的长度。
	 * 
	 * 解题思路：
	 * 第K短路问题可以通过改进的Dijkstra算法来解决。
	 * 我们使用A*算法的思想，维护一个优先队列，队列中的元素按照预估总距离（已走距离+到终点的启发式距离）排序。
	 * 每次取出预估总距离最小的节点，如果是终点，则记录次数，当次数达到K时，返回当前距离。
	 * 为了提高效率，我们可以预先计算终点到所有其他节点的最短距离作为启发式函数。
	 * 
	 * 算法应用场景：
	 * - 交通导航中提供多条备选路线
	 * - 网络路由中的路径多样性
	 * - 机器人路径规划中的备选路径
	 * 
	 * 时间复杂度分析：
	 * O(E*K*log(E*K))，其中E是边数，K是要求的第K短路
	 * 
	 * 空间复杂度分析：
	 * O(V+E+K*V)，存储图、距离数组和优先队列
	 */
	
	/**
	 * 边的表示类
	 */
	static class Edge {
		int to;     // 目标节点
		int weight; // 边的权重
		
		public Edge(int to, int weight) {
			this.to = to;
			this.weight = weight;
		}
	}
	
	/**
	 * A*算法中的节点类
	 */
	static class AStarNode implements Comparable<AStarNode> {
		int currentDist; // 已走距离
		int estimatedTotal; // 预估总距离
		int node; // 当前节点
		
		public AStarNode(int currentDist, int estimatedTotal, int node) {
			this.currentDist = currentDist;
			this.estimatedTotal = estimatedTotal;
			this.node = node;
		}
		
		@Override
		public int compareTo(AStarNode other) {
			// 按照预估总距离排序
			return Integer.compare(this.estimatedTotal, other.estimatedTotal);
		}
	}
	
	/**
	 * 在反向图上运行Dijkstra算法，计算终点到所有节点的最短距离
	 * @param n 节点总数
	 * @param reverseGraph 反向图的邻接表表示
	 * @param end 终点
	 * @return 终点到所有节点的最短距离数组
	 */
	private static int[] dijkstraReverse(int n, java.util.List<java.util.List<Edge>> reverseGraph, int end) {
		int[] dist = new int[n + 1];
		java.util.Arrays.fill(dist, Integer.MAX_VALUE);
		dist[end] = 0;
		
		// 优先队列，按照距离排序
		java.util.PriorityQueue<int[]> pq = new java.util.PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
		pq.offer(new int[]{0, end});
		
		while (!pq.isEmpty()) {
			int[] current = pq.poll();
			int d = current[0];
			int u = current[1];
			
			if (d > dist[u]) continue;
			
			for (Edge edge : reverseGraph.get(u)) {
				int v = edge.to;
				int w = edge.weight;
				if (dist[v] > d + w) {
					dist[v] = d + w;
					pq.offer(new int[]{dist[v], v});
				}
			}
		}
		
		return dist;
	}
	
	/**
	 * 求解第K短路问题
	 * @param n 节点总数
	 * @param edges 边列表，格式为 [u, v, w]
	 * @param start 起点
	 * @param end 终点
	 * @param K 要求的第K短路
	 * @return 第K短路的长度，如果不存在返回-1
	 */
	public static int findKthShortestPath(int n, int[][] edges, int start, int end, int K) {
		// 构建原图和反向图
		java.util.List<java.util.List<Edge>> graph = new java.util.ArrayList<>();
		java.util.List<java.util.List<Edge>> reverseGraph = new java.util.ArrayList<>();
		
		for (int i = 0; i <= n; i++) {
			graph.add(new java.util.ArrayList<>());
			reverseGraph.add(new java.util.ArrayList<>());
		}
		
		for (int[] edge : edges) {
			int u = edge[0];
			int v = edge[1];
			int w = edge[2];
			graph.get(u).add(new Edge(v, w));
			reverseGraph.get(v).add(new Edge(u, w));
		}
		
		// 步骤1：在反向图上运行Dijkstra算法，计算终点到所有节点的最短距离
		int[] distToEnd = dijkstraReverse(n, reverseGraph, end);
		
		// 步骤2：使用A*算法寻找第K短路
		java.util.PriorityQueue<AStarNode> pq = new java.util.PriorityQueue<>();
		// 初始化起点，预估总距离 = 已走距离(0) + 到终点的最短距离
		pq.offer(new AStarNode(0, distToEnd[start], start));
		
		// 记录到达每个节点的路径数
		int[] count = new int[n + 1];
		
		while (!pq.isEmpty()) {
			AStarNode current = pq.poll();
			int currentDist = current.currentDist;
			int u = current.node;
			
			// 如果到达终点，计数加一
			if (u == end) {
				count[u]++;
				if (count[u] == K) {
					return currentDist;
				}
			}
			
			// 如果到达该节点的路径数已经超过K，跳过
			if (count[u] > K) {
				continue;
			}
			count[u]++;
			
			// 遍历所有邻居节点
			for (Edge edge : graph.get(u)) {
				int v = edge.to;
				int w = edge.weight;
				int newDist = currentDist + w;
				int estimatedTotal = newDist + distToEnd[v];
				pq.offer(new AStarNode(newDist, estimatedTotal, v));
			}
		}
		
		return -1; // 不存在第K短路
	}
	
	
	// =============================================================================
	// 带状态的最短路径问题：电动车游城市
	// =============================================================================
	
	/**
	 * 带状态的最短路径问题：电动车游城市
	 * 
	 * 题目描述：
	 * 城市之间有公路相连，每条公路有长度。电动车有一个电池容量限制，每行驶1公里消耗1单位电量。
	 * 城市中可以充电，充电可以将电量恢复到满。求从起点到终点的最短路径长度。
	 * 
	 * 解题思路：
	 * 这是一个典型的带状态的最短路径问题。
	 * 状态不仅包括当前所在城市，还包括当前剩余电量。
	 * 我们可以使用Dijkstra算法的变种，其中每个状态是(城市, 电量)，边表示行驶或充电操作。
	 * 
	 * 算法应用场景：
	 * - 电动车路径规划
	 * - 资源受限的路径优化
	 * - 带约束条件的最短路径问题
	 * 
	 * 时间复杂度分析：
	 * O(C*E*log(C*V))，其中C是电池容量，E是边数，V是节点数
	 * 
	 * 空间复杂度分析：
	 * O(C*V)，存储状态和距离数组
	 */
	
	/**
	 * 带电量状态的节点类
	 */
	static class StateWithCharge implements Comparable<StateWithCharge> {
		int dist;  // 距离
		int city;  // 城市
		int charge; // 剩余电量
		
		public StateWithCharge(int dist, int city, int charge) {
			this.dist = dist;
			this.city = city;
			this.charge = charge;
		}
		
		@Override
		public int compareTo(StateWithCharge other) {
			// 按照距离排序
			return Integer.compare(this.dist, other.dist);
		}
	}
	
	/**
	 * 求解电动车路径规划问题
	 * @param n 城市总数
	 * @param edges 边列表，格式为 [u, v, w]
	 * @param start 起点城市
	 * @param end 终点城市
	 * @param capacity 电池容量
	 * @return 最短路径长度，如果无法到达返回-1
	 */
	public static int findShortestPathWithCharge(int n, int[][] edges, int start, int end, int capacity) {
		// 构建图的邻接表表示
		java.util.List<java.util.List<Edge>> graph = new java.util.ArrayList<>();
		for (int i = 0; i <= n; i++) {
			graph.add(new java.util.ArrayList<>());
		}
		
		for (int[] edge : edges) {
			int u = edge[0];
			int v = edge[1];
			int w = edge[2];
			graph.get(u).add(new Edge(v, w));
			graph.get(v).add(new Edge(u, w)); // 假设公路是双向的
		}
		
		// dist[u][c] 表示到达城市u且剩余电量为c时的最短距离
		int[][] dist = new int[n + 1][capacity + 1];
		for (int i = 0; i <= n; i++) {
			java.util.Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		
		// 优先队列，按照距离排序
		java.util.PriorityQueue<StateWithCharge> pq = new java.util.PriorityQueue<>();
		
		// 初始状态：起点，满电，距离为0
		dist[start][capacity] = 0;
		pq.offer(new StateWithCharge(0, start, capacity));
		
		while (!pq.isEmpty()) {
			StateWithCharge current = pq.poll();
			int d = current.dist;
			int u = current.city;
			int c = current.charge;
			
			// 如果已经到达终点，返回最短距离
			if (u == end) {
				return d;
			}
			
			// 如果当前距离大于记录的最小距离，跳过
			if (d > dist[u][c]) {
				continue;
			}
			
			// 操作1：在当前城市充电，将电量充满
			if (c < capacity) {
				if (dist[u][capacity] > d) {
					dist[u][capacity] = d;
					pq.offer(new StateWithCharge(d, u, capacity));
				}
			}
			
			// 操作2：前往相邻城市
			for (Edge edge : graph.get(u)) {
				int v = edge.to;
				int w = edge.weight;
				// 检查电量是否足够行驶这段距离
				if (c >= w) {
					int newC = c - w;
					if (dist[v][newC] > d + w) {
						dist[v][newC] = d + w;
						pq.offer(new StateWithCharge(d + w, v, newC));
					}
				}
			}
		}
		
		return -1; // 无法到达终点
	}
	
	
	// 测试代码
	public static void main(String[] args) {
		// 测试网络延迟时间
		int[][] times = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
		int n = 4;
		int k = 2;
		System.out.println("网络延迟时间: " + networkDelayTime1(times, n, k));
		System.out.println("网络延迟时间(链式前向星优化): " + networkDelayTime2(times, n, k));
		
		// 测试第K短路
		int[][] edgesKth = {{1, 2, 1}, {1, 3, 3}, {2, 3, 1}, {3, 4, 2}, {2, 4, 5}};
		int startKth = 1, endKth = 4, nKth = 4, K = 3;
		System.out.println("第3短路长度: " + findKthShortestPath(nKth, edgesKth, startKth, endKth, K));
		
		// 测试电动车路径规划
		int[][] edgesCharge = {{1, 2, 3}, {2, 3, 4}, {1, 3, 10}, {3, 4, 5}};
		int startCharge = 1, endCharge = 4, nCharge = 4, capacity = 6;
		System.out.println("电动车最短路径: " + findShortestPathWithCharge(nCharge, edgesCharge, startCharge, endCharge, capacity));
	}
}