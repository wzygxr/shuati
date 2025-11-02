package class064;

/**
 * 飞行路线（语言提供的堆）
 * 
 * 题目链接：https://www.luogu.com.cn/problem/P4568
 * 
 * 题目描述：
 * Alice和Bob现在要乘飞机旅行，他们选择了一家相对便宜的航空公司
 * 该航空公司一共在n个城市设有业务，设这些城市分别标记为0 ~ n−1
 * 一共有m种航线，每种航线连接两个城市，并且航线有一定的价格
 * Alice 和 Bob 现在要从一个城市沿着航线到达另一个城市，途中可以进行转机
 * 航空公司对他们这次旅行也推出优惠，他们可以免费在最多k种航线上搭乘飞机
 * 那么 Alice 和 Bob 这次出行最少花费多少
 * 
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的状态不仅包括城市位置，还包括已使用的免费机会次数。
 * 我们将状态定义为(城市, 已使用免费机会次数)，图中的节点是这些状态对。
 * 边有两种类型：
 * 1. 免费边：使用一次免费机会乘坐航班，花费为0
 * 2. 付费边：正常付费乘坐航班，花费为票价
 * 使用Dijkstra算法找到从起点状态(起点城市, 0次免费机会)到终点状态(终点城市, 任意免费机会次数)的最少花费。
 * 
 * 算法应用场景：
 * - 优惠券使用策略优化
 * - 资源受限的路径规划
 * - 多状态动态规划问题
 * 
 * 时间复杂度分析：
 * O((V+k*E)log(V+k*E)) 其中V是城市数，E是航线数
 * 
 * 空间复杂度分析：
 * O(V*k) 存储距离数组和访问标记数组
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.PriorityQueue;

public class Code06_FlightPath1 {

	public static int MAXN = 10001;
	public static int MAXM = 100001;
	public static int MAXK = 11;

	// 链式前向星建图需要
	// head[i] 存储城市i的第一条边的索引
	public static int[] head = new int[MAXN];
	// next[i] 存储第i条边的下一条边的索引
	public static int[] next = new int[MAXM];
	// to[i] 存储第i条边的终点城市
	public static int[] to = new int[MAXM];
	// weight[i] 存储第i条边的权重（票价）
	public static int[] weight = new int[MAXM];
	// 边的计数器
	public static int cnt;

	// Dijkstra需要
	// distance[i][j]表示到达城市i且已使用j次免费机会的最少花费
	// 初始化为最大值，表示尚未访问
	public static int[][] distance = new int[MAXN][MAXK];

	// visited[i][j]表示状态(城市i, 使用j次免费机会)是否已经确定了最短路径
	// 用于避免重复处理已经确定最短路径的状态
	public static boolean[][] visited = new boolean[MAXN][MAXK];

	// 用语言自己提供的堆
	// 动态结构，不推荐（相比自定义堆效率较低）
	// 数组含义：[0] 到达的城市编号, [1] 已经使用的免单次数, [2] 沿途的花费
	public static PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[2] - b[2]);

	public static int n, m, k, s, t;

	/**
	 * 初始化函数
	 * 
	 * 主要工作：
	 * 1. 初始化链式前向星数据结构
	 * 2. 初始化距离数组和访问标记数组
	 * 3. 清空优先队列
	 * 
	 * 时间复杂度: O(n*k)
	 * 空间复杂度: O(n*k)
	 */
	public static void build() {
		cnt = 1;  // 边的索引从1开始
		for (int i = 0; i < n; i++) {
			head[i] = 0;  // 初始化链式前向星头指针
			for (int j = 0; j <= k; j++) {
				distance[i][j] = Integer.MAX_VALUE;  // 初始化距离为无穷大
				visited[i][j] = false;               // 初始化访问标记为false
			}
		}
		heap.clear();  // 清空优先队列
	}

	/**
	 * 链式前向星加边
	 * 
	 * 算法步骤：
	 * 1. 将新边插入到链表头部
	 * 2. 更新相关指针
	 * 
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * 
	 * @param u 起点城市
	 * @param v 终点城市
	 * @param w 航线价格
	 */
	public static void addEdge(int u, int v, int w) {
		next[cnt] = head[u];    // 新边的下一条边指向原来的第一条边
		to[cnt] = v;            // 设置边的终点
		weight[cnt] = w;        // 设置边的权重
		head[u] = cnt++;        // 更新城市u的第一条边为新边
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;           // 城市数量
			in.nextToken(); m = (int) in.nval;  // 航线数量
			in.nextToken(); k = (int) in.nval;  // 免费机会次数
			in.nextToken(); s = (int) in.nval;  // 起点城市
			in.nextToken(); t = (int) in.nval;  // 终点城市
			build();  // 初始化数据结构
			for (int i = 0, a, b, c; i < m; i++) {
				in.nextToken(); a = (int) in.nval;  // 起点城市
				in.nextToken(); b = (int) in.nval;  // 终点城市
				in.nextToken(); c = (int) in.nval;  // 航线价格
				addEdge(a, b, c);  // 添加无向边
				addEdge(b, a, c);  // 添加无向边
			}
			out.println(dijkstra());  // 输出最短路径
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * Dijkstra算法主函数
	 * 
	 * 算法核心思想：
	 * 1. 将问题转化为图论中的最短路径问题
	 * 2. 状态定义为(城市, 已使用免费机会次数)，图中的节点是这些状态对
	 * 3. 边有两种类型：免费边和付费边
	 * 4. 使用Dijkstra算法找到从起点状态到终点状态的最少花费
	 * 
	 * 算法步骤：
	 * 1. 初始化距离数组，起点状态距离为0，其他状态为无穷大
	 * 2. 使用优先队列维护待处理状态，按花费从小到大排序
	 * 3. 不断取出花费最小的状态，通过使用或不使用免费机会扩展新状态
	 * 4. 当处理到终点城市时，直接返回结果（剪枝优化）
	 * 
	 * 时间复杂度: O((V+k*E)log(V+k*E)) 其中V是城市数，E是航线数
	 * 空间复杂度: O(V*k)
	 * 
	 * @return 从起点城市到终点城市的最少花费
	 */
	public static int dijkstra() {
		// 初始状态：在起点城市且未使用免费机会，花费为0
		distance[s][0] = 0;
		// 将起点状态加入优先队列
		heap.add(new int[] { s, 0, 0 });
		
		// Dijkstra算法主循环
		while (!heap.isEmpty()) {
			// 取出花费最小的状态
			int[] record = heap.poll();
			int u = record[0];     // 当前城市
			int use = record[1];   // 已使用免费机会次数
			int cost = record[2];  // 当前花费
			
			// 如果已经处理过，跳过
			// 这是为了避免同一状态多次处理导致的重复计算
			if (visited[u][use]) {
				continue;
			}
			
			// 标记为已处理，表示已确定从起点到该状态的最少花费
			visited[u][use] = true;
			
			// 如果到达终点，直接返回结果
			// 常见剪枝优化：发现终点直接返回，不用等都结束
			// 这是因为Dijkstra算法的特性保证了第一次到达终点时就是最优解
			if (u == t) {
				return cost;
			}
			
			// 遍历所有出边（从当前城市出发的所有航线）
			// ei是边的索引，通过head[u]获取第一条边，通过next[ei]获取下一条边
			for (int ei = head[u], v, w; ei > 0; ei = next[ei]) {
				v = to[ei];    // 下一个城市
				w = weight[ei]; // 航线价格
				
				// 使用免费机会
				// 如果还有免费机会且使用免费机会后花费更少
				if (use < k && distance[v][use + 1] > distance[u][use]) {
					// 使用免费
					distance[v][use + 1] = distance[u][use];  // 花费为0
					// 将使用免费机会后的新状态加入优先队列
					heap.add(new int[] { v, use + 1, distance[v][use + 1] });
				}
				
				// 不使用免费机会
				// 如果不使用免费机会且花费更少
				if (distance[v][use] > distance[u][use] + w) {
					// 不用免费
					distance[v][use] = distance[u][use] + w;  // 花费为原花费加票价
					// 将不使用免费机会的新状态加入优先队列
					heap.add(new int[] { v, use, distance[v][use] });
				}
			}
		}
		// 理论上不会执行到这里，因为从起点到终点总是存在路径
		return -1;
	}

}