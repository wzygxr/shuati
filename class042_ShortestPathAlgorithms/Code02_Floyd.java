package class065;

import java.io.*;
import java.util.*;

/**
 * Floyd算法深度解析与多题目实现
 * 
 * Floyd算法用于解决多源最短路径问题，基于动态规划思想
 * 核心思想: 通过中间节点逐步优化任意两点间的最短距离
 * 状态转移方程: distance[i][j] = min(distance[i][j], distance[i][k] + distance[k][j])
 * 
 * 算法特性:
 * - 多源最短路径: 一次性求解所有点对间的最短路径
 * - 负权边支持: 可以处理负权边，但不能处理负权环
 * - 路径重建: 通过记录前驱节点可以重建具体路径
 * 
 * 时间复杂度: O(N³)，其中N是节点数量
 * 空间复杂度: O(N²)，需要存储距离矩阵
 * 
 * 适用场景: 节点数较少(N ≤ 500)的全源最短路径问题
 * 优势: 代码简洁，易于实现，支持负权边
 * 劣势: 时间复杂度较高，不适合大规模图
 */
public class Code02_Floyd {

	// 最大节点数常量定义
	public static int MAXN = 101;
	public static int MAXM = 10001;
	
	// 全局变量定义
	public static int[] path = new int[MAXM];          // 路径序列
	public static int[][] distance = new int[MAXN][MAXN]; // 距离矩阵
	public static int n, m, ans;                       // 节点数、边数、结果

	/**
	 * 距离矩阵初始化函数
	 * 
	 * 初始化策略:
	 * - 对角线元素: 节点到自身的距离为0
	 * - 其他元素: 初始化为无穷大(Integer.MAX_VALUE)，表示初始不可达
	 * - 后续会根据输入的边信息更新直接相连的节点距离
	 * 
	 * 注意事项:
	 * - 必须进行初始化，否则可能得到错误结果
	 * - 无穷大的选择要避免后续运算中的溢出问题
	 */
	public static void build() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					distance[i][j] = 0; // 节点到自身的距离为0
				} else {
					distance[i][j] = Integer.MAX_VALUE; // 初始不可达
				}
			}
		}
	}

	/**
	 * 主函数 - 处理洛谷P2910题目
	 * 
	 * 题目描述: 给定一个带权有向图，计算指定路径序列的总距离
	 * 如果路径中任意两点间不可达，则应该能够正确检测并处理
	 * 
	 * 输入格式:
	 * - 第一行: 节点数n，路径长度m
	 * - 第二行: m个节点编号（路径序列）
	 * - 接下来n行: n×n的邻接矩阵
	 * 
	 * 输出格式: 路径序列的总距离
	 * 
	 * 算法流程:
	 * 1. 读取输入数据并初始化距离矩阵
	 * 2. 运行Floyd算法计算所有点对间最短距离
	 * 3. 计算指定路径序列的总距离
	 * 4. 输出结果
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 处理多组测试数据（直到文件结束）
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			// 读取节点数和路径长度
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			
			// 读取路径序列
			for (int i = 0; i < m; i++) {
				in.nextToken();
				path[i] = (int) in.nval - 1; // 转换为0-based索引
			}
			
			// 初始化距离矩阵（重要步骤）
			build();
			
			// 读取邻接矩阵并填充距离矩阵
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					in.nextToken();
					distance[i][j] = (int) in.nval;
					// 注意: 题目输入可能包含不可达的情况（无穷大值）
					// 需要根据题目具体的无穷大表示方式进行调整
				}
			}
			
			// 执行Floyd算法计算所有点对最短路径
			floyd();
			
			// 计算路径序列的总距离
			ans = 0;
			boolean reachable = true;
			for (int i = 1; i < m; i++) {
				int from = path[i - 1];
				int to = path[i];
				
				// 检查路径是否可达
				if (distance[from][to] == Integer.MAX_VALUE) {
					reachable = false;
					break;
				}
				ans += distance[from][to];
			}
			
			// 输出结果（如果不可达，根据题目要求输出特定值）
			if (reachable) {
				out.println(ans);
			} else {
				out.println("INF"); // 或者根据题目要求输出其他值
			}
		}
		
		// 清理资源
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * Floyd算法核心实现
	 * 
	 * 算法原理:
	 * 动态规划思想，通过三重循环逐步优化距离矩阵
	 * 外层循环: 中间节点k（跳板节点）
	 * 中层循环: 起点i
	 * 内层循环: 终点j
	 * 
	 * 状态转移:
	 * 对于每对节点(i,j)，考虑是否通过中间节点k能够获得更短路径
	 * 即: distance[i][j] = min(distance[i][j], distance[i][k] + distance[k][j])
	 * 
	 * 关键要点:
	 * 1. 循环顺序必须正确: k在最外层，i和j在内层
	 * 2. 必须检查中间值是否为无穷大，避免整数溢出
	 * 3. 算法结束后，distance[i][j]存储的就是i到j的最短距离
	 * 
	 * 负权边处理:
	 * - 可以处理负权边，因为算法会不断尝试优化路径
	 * - 但不能处理负权环，因为负权环会导致距离无限减小
	 */
	public static void floyd() {
		// 三重循环: 中间节点k -> 起点i -> 终点j
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				// 优化: 如果i到k不可达，跳过内层循环
				if (distance[i][k] == Integer.MAX_VALUE) {
					continue;
				}
				for (int j = 0; j < n; j++) {
					// 检查i->k和k->j是否都可达，避免整数溢出
					if (distance[i][k] != Integer.MAX_VALUE && 
						distance[k][j] != Integer.MAX_VALUE) {
						
						// 状态转移: 尝试通过k优化i到j的路径
						if (distance[i][j] > distance[i][k] + distance[k][j]) {
							distance[i][j] = distance[i][k] + distance[k][j];
						}
					}
				}
			}
		}
	}
	
	/**
	 * Floyd算法变种: 带路径重建功能
	 * 
	 * 扩展功能: 不仅计算最短距离，还能重建具体路径
	 * 通过维护前驱节点矩阵，可以在算法结束后重建最短路径
	 */
	public static class FloydWithPathReconstruction {
		private int[][] dist;    // 距离矩阵
		private int[][] next;    // 路径重建矩阵
		private int n;          // 节点数量
		
		public FloydWithPathReconstruction(int n) {
			this.n = n;
			this.dist = new int[n][n];
			this.next = new int[n][n];
			initialize();
		}
		
		/**
		 * 初始化距离矩阵和路径重建矩阵
		 */
		private void initialize() {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (i == j) {
						dist[i][j] = 0;
						next[i][j] = j;
					} else {
						dist[i][j] = Integer.MAX_VALUE;
						next[i][j] = -1; // 表示不可达
					}
				}
			}
		}
		
		/**
		 * 添加边信息
		 */
		public void addEdge(int u, int v, int weight) {
			dist[u][v] = weight;
			next[u][v] = v;
		}
		
		/**
		 * 执行Floyd算法并重建路径
		 */
		public void floydWithPath() {
			for (int k = 0; k < n; k++) {
				for (int i = 0; i < n; i++) {
					if (dist[i][k] == Integer.MAX_VALUE) continue;
					for (int j = 0; j < n; j++) {
						if (dist[i][k] != Integer.MAX_VALUE && 
							dist[k][j] != Integer.MAX_VALUE &&
							dist[i][j] > dist[i][k] + dist[k][j]) {
							
							dist[i][j] = dist[i][k] + dist[k][j];
							next[i][j] = next[i][k]; // 更新路径信息
						}
					}
				}
			}
		}
		
		/**
		 * 重建从u到v的最短路径
		 */
		public List<Integer> reconstructPath(int u, int v) {
			List<Integer> path = new ArrayList<>();
			if (next[u][v] == -1) return path; // 不可达
			
			path.add(u);
			while (u != v) {
				u = next[u][v];
				path.add(u);
			}
			return path;
		}
	}
}

/* ============================ 补充题目1: 最小环检测 ============================ */

/**
 * Floyd算法应用: 检测图中的最小环
 * 
 * 算法思路:
 * 在Floyd算法的执行过程中，当考虑中间节点k时，
 * 检查是否存在i->k和k->i的路径，从而形成环
 * 最小环长度 = dist[i][k] + dist[k][i]
 * 
 * 时间复杂度: O(N³)，与标准Floyd相同
 * 空间复杂度: O(N²)
 */
class MinimumCycleDetection {
	public int findMinimumCycle(int n, int[][] edges) {
		int[][] dist = new int[n][n];
		
		// 初始化距离矩阵
		for (int i = 0; i < n; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
			dist[i][i] = 0;
		}
		
		// 添加边信息
		for (int[] edge : edges) {
			int u = edge[0], v = edge[1], w = edge[2];
			dist[u][v] = w;
			// 如果是无向图，还需要 dist[v][u] = w;
		}
		
		int minCycle = Integer.MAX_VALUE;
		
		// Floyd算法检测最小环
		for (int k = 0; k < n; k++) {
			// 在更新之前，检查经过k的环
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < k; j++) {
					if (dist[i][k] != Integer.MAX_VALUE && 
						dist[k][j] != Integer.MAX_VALUE &&
						dist[j][i] != Integer.MAX_VALUE) {
						
						minCycle = Math.min(minCycle, 
							dist[i][k] + dist[k][j] + dist[j][i]);
					}
				}
			}
			
			// 标准Floyd更新
			for (int i = 0; i < n; i++) {
				if (dist[i][k] == Integer.MAX_VALUE) continue;
				for (int j = 0; j < n; j++) {
					if (dist[i][k] != Integer.MAX_VALUE && 
						dist[k][j] != Integer.MAX_VALUE &&
						dist[i][j] > dist[i][k] + dist[k][j]) {
						
						dist[i][j] = dist[i][k] + dist[k][j];
					}
				}
			}
		}
		
		return minCycle == Integer.MAX_VALUE ? -1 : minCycle;
	}
}

/* ============================ 补充题目2: 传递闭包 ============================ */

/**
 * Floyd算法应用: 计算有向图的传递闭包
 * 
 * 传递闭包定义: 如果存在从i到j的路径，则闭包矩阵[i][j]为true
 * 算法思路: 将Floyd算法中的距离计算改为布尔运算
 * 状态转移: reachable[i][j] = reachable[i][j] || (reachable[i][k] && reachable[k][j])
 * 
 * 时间复杂度: O(N³)
 * 空间复杂度: O(N²)
 */
class TransitiveClosure {
	public boolean[][] computeTransitiveClosure(int n, int[][] edges) {
		boolean[][] reachable = new boolean[n][n];
		
		// 初始化: 节点到自身可达，直接边可达
		for (int i = 0; i < n; i++) {
			reachable[i][i] = true;
		}
		for (int[] edge : edges) {
			reachable[edge[0]][edge[1]] = true;
		}
		
		// Floyd-Warshall算法计算传递闭包
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					reachable[i][j] = reachable[i][j] || 
						(reachable[i][k] && reachable[k][j]);
				}
			}
		}
		
		return reachable;
	}
}

/* ============================ Floyd算法工程实践总结 ============================ */

/**
 * Floyd算法工程实践关键要点:
 * 
 * 1. 算法选择考量:
 *    - 节点数量: N ≤ 500时适用，N > 1000时考虑其他算法
 *    - 查询频率: 需要频繁查询任意两点距离时，预处理优势明显
 *    - 图动态性: 静态图适合，动态图需要重新计算成本较高
 * 
 * 2. 性能优化策略:
 *    - 循环顺序优化: k在最外层是必须的
 *    - 提前终止: 当dist[i][k]不可达时跳过内层循环
 *    - 空间优化: 使用滚动数组可将空间降至O(N)
 * 
 * 3. 数值稳定性处理:
 *    - 整数溢出: 使用long类型或检查中间值
 *    - 浮点数精度: 注意浮点数比较的误差
 *    - 无穷大表示: 选择合适的不可能值，避免运算溢出
 * 
 * 4. 应用场景扩展:
 *    - 最小环检测: 在Floyd过程中检测环
 *    - 传递闭包: 布尔运算版本的Floyd
 *    - 中心性计算: 网络分析中的各种中心性指标
 *    - 相似度计算: 通过距离矩阵计算节点相似度
 * 
 * 5. 与其他算法对比:
 *    - vs Dijkstra: Floyd适合多源查询，Dijkstra适合单源
 *    - vs Bellman-Ford: Floyd代码简洁，Bellman-Ford支持负环检测
 *    - vs Johnson: Johnson适合稀疏图，Floyd适合稠密图
 */