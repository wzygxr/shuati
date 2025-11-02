package class065;

import java.util.*;

/**
 * Bellman-Ford算法深度解析与多题目实现
 * 
 * Bellman-Ford算法用于解决单源最短路径问题，特别适用于存在负权边的图
 * 核心思想: 通过n-1轮松弛操作，逐步逼近最短路径解
 * 
 * 算法特性:
 * - 负权边支持: 唯一能正确处理负权边的单源最短路径算法
 * - 负环检测: 可以通过第n轮松弛操作检测负权环的存在
 * - 边数限制: 天然支持带边数限制的最短路径问题
 * 
 * 时间复杂度: O(N×E)，其中N是节点数，E是边数
 * 空间复杂度: O(N)，距离数组存储
 * 
 * 适用场景:
 * - 存在负权边的图
 * - 需要检测负权环的场景
 * - 带边数限制的最短路径问题
 * - 分布式系统中的路由算法
 */
public class Code03_BellmanFord {

	/**
	 * LeetCode 787. K站中转内最便宜的航班 - Bellman-Ford算法应用
	 * 
	 * 题目链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
	 * 题目描述: 有 n 个城市通过航班连接。flights[i] = [fromi, toi, pricei] 表示航班信息。
	 * 给定出发城市 src 和目的地 dst，找到最多经过 k 站中转的最便宜价格。
	 * 
	 * 算法特点: 这是一个带边数限制的最短路径问题，Bellman-Ford算法天然支持
	 * 实现要点: 通过控制松弛轮数来限制路径的边数（中转站数）
	 * 
	 * 时间复杂度: O(K×E)，其中K是中转站限制，E是航班数量
	 * 空间复杂度: O(N)，使用滚动数组优化
	 * 
	 * @param n 城市数量
	 * @param flights 航班信息数组，每个元素为[起点,终点,价格]
	 * @param start 出发城市
	 * @param target 目标城市  
	 * @param k 最多中转站数
	 * @return 最便宜价格，如果不可达返回-1
	 */
	public static int findCheapestPrice(int n, int[][] flights, int start, int target, int k) {
		// 距离数组: cur[i]表示从起点到城市i的当前最小成本
		int[] cur = new int[n];
		Arrays.fill(cur, Integer.MAX_VALUE);
		cur[start] = 0; // 起点到自身的成本为0
		
		// 进行k+1轮松弛操作（k站中转意味着最多k+1条边）
		for (int i = 0; i <= k; i++) {
			// 使用临时数组保存本轮结果，避免同一轮中多次使用更新后的值
			int[] next = Arrays.copyOf(cur, n);
			boolean updated = false; // 标记本轮是否有更新
			
			// 遍历所有航班进行松弛操作
			for (int[] flight : flights) {
				int from = flight[0];
				int to = flight[1];
				int price = flight[2];
				
				// 松弛操作: 如果from城市可达，尝试更新to城市的成本
				if (cur[from] != Integer.MAX_VALUE && 
					cur[from] + price < next[to]) {
					next[to] = cur[from] + price;
					updated = true;
				}
			}
			
			// 更新当前距离数组
			cur = next;
			
			// 提前终止优化: 如果本轮没有更新，说明已收敛
			if (!updated) {
				break;
			}
		}
		
		// 返回结果，如果不可达返回-1
		return cur[target] == Integer.MAX_VALUE ? -1 : cur[target];
	}
	
	/**
	 * 标准Bellman-Ford算法实现 - 单源最短路径
	 * 
	 * 算法流程:
	 * 1. 初始化距离数组，源点距离为0，其他为无穷大
	 * 2. 进行n-1轮松弛操作（最短路径最多包含n-1条边）
	 * 3. 可选: 进行第n轮松弛检测负权环
	 * 
	 * @param n 节点数量
	 * @param edges 边信息数组，每个元素为[起点,终点,权重]
	 * @param src 源点
	 * @return 距离数组，如果存在负权环返回null
	 */
	public static int[] bellmanFord(int n, int[][] edges, int src) {
		int[] distance = new int[n];
		Arrays.fill(distance, Integer.MAX_VALUE);
		distance[src] = 0;
		
		// n-1轮松弛操作
		for (int i = 1; i < n; i++) {
			boolean updated = false;
			for (int[] edge : edges) {
				int u = edge[0], v = edge[1], w = edge[2];
				if (distance[u] != Integer.MAX_VALUE && 
					distance[u] + w < distance[v]) {
					distance[v] = distance[u] + w;
					updated = true;
				}
			}
			// 提前终止优化
			if (!updated) break;
		}
		
		return distance;
	}
	
	/**
	 * Bellman-Ford算法带负环检测版本
	 * 
	 * 检测原理: 进行第n轮松弛，如果还能更新距离，说明存在负权环
	 * 
	 * @param n 节点数量
	 * @param edges 边信息数组
	 * @param src 源点
	 * @return 如果存在负权环返回true，否则返回false
	 */
	public static boolean hasNegativeCycle(int n, int[][] edges, int src) {
		int[] distance = bellmanFord(n, edges, src);
		
		// 第n轮检测负环
		for (int[] edge : edges) {
			int u = edge[0], v = edge[1], w = edge[2];
			if (distance[u] != Integer.MAX_VALUE && 
				distance[u] + w < distance[v]) {
				return true;  // 存在负权环
			}
		}
		
		return false; // 不存在负权环
	}
	
	/**
	 * 测试函数 - 验证算法正确性
	 */
	public static void main(String[] args) {
		System.out.println("=== Bellman-Ford算法测试 ===");
		
		// 测试LeetCode 787题目
		int n1 = 3;
		int[][] flights1 = {{0,1,100},{1,2,100},{0,2,500}};
		int src1 = 0, dst1 = 2, k1 = 1;
		int result1 = findCheapestPrice(n1, flights1, src1, dst1, k1);
		System.out.println("LeetCode 787测试结果: " + result1 + " (期望: 200)");
		
		// 测试标准Bellman-Ford
		int n2 = 5;
		int[][] edges2 = {{0,1,-1},{0,2,4},{1,2,3},{1,3,2},{1,4,2},{3,2,5},{3,1,1},{4,3,-3}};
		int[] dist2 = bellmanFord(n2, edges2, 0);
		System.out.println("标准Bellman-Ford测试: " + Arrays.toString(dist2));
		
		// 测试负环检测
		int n3 = 3;
		int[][] edges3 = {{0,1,-1},{1,2,-2},{2,0,-1}}; // 负权环
		boolean hasCycle = hasNegativeCycle(n3, edges3, 0);
		System.out.println("负环检测结果: " + hasCycle + " (期望: true)");
	}

}

/* ============================ 补充题目1: LeetCode 743. 网络延迟时间 ============================ */

/**
 * LeetCode 743. 网络延迟时间 - Bellman-Ford算法实现
 * 
 * 题目链接: https://leetcode.cn/problems/network-delay-time/
 * 题目描述: 有 n 个网络节点，标记为 1 到 n。给你一个列表 times，表示信号经过有向边的传递时间。
 * times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，wi 是传递时间。
 * 从节点 K 发出信号，需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1。
 * 
 * 算法实现要点:
 * 1. 距离数组初始化: 源点距离为0，其他节点为无穷大
 * 2. n-1轮松弛操作: 每轮遍历所有边进行松弛
 * 3. 结果检查: 找到最大距离，检查是否所有节点可达
 * 
 * 时间复杂度: O(N×E)
 * 空间复杂度: O(N)
 */
class NetworkDelayTime {
    public int networkDelayTime(int[][] times, int n, int k) {
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[k] = 0;
        
        // n-1轮松弛
        for (int i = 1; i < n; i++) {
            boolean updated = false;
            for (int[] time : times) {
                int u = time[0], v = time[1], w = time[2];
                if (distance[u] != Integer.MAX_VALUE && 
                    distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    updated = true;
                }
            }
            if (!updated) break;
        }
        
        // 检查结果
        int maxDelay = 0;
        for (int i = 1; i <= n; i++) {
            if (distance[i] == Integer.MAX_VALUE) return -1;
            maxDelay = Math.max(maxDelay, distance[i]);
        }
        return maxDelay;
    }
}

/* ============================ 补充题目2: POJ 3259. Wormholes ============================ */

/**
 * POJ 3259. Wormholes（虫洞问题）- Bellman-Ford负环检测应用
 * 
 * 题目描述: 农场中有普通路径（正权边）和虫洞（负权边），判断是否存在负权环
 * 即能否通过虫洞回到过去（时间旅行）
 * 
 * 算法思路: 使用Bellman-Ford算法检测图中是否存在负权环
 * 如果存在负权环，说明可以无限次循环，实现时间旅行
 * 
 * 时间复杂度: O(N×E)
 * 空间复杂度: O(N)
 */
class WormholesChecker {
    public boolean hasWormholeCycle(int n, int[][] paths, int[][] wormholes) {
        // 合并所有边
        List<int[]> edges = new ArrayList<>();
        
        // 添加普通路径（双向）
        for (int[] path : paths) {
            edges.add(new int[]{path[0], path[1], path[2]});
            edges.add(new int[]{path[1], path[0], path[2]});
        }
        
        // 添加虫洞（单向，负权）
        for (int[] wormhole : wormholes) {
            edges.add(new int[]{wormhole[0], wormhole[1], -wormhole[2]});
        }
        
        return Code03_BellmanFord.hasNegativeCycle(n, edges.toArray(new int[0][0]), 1);
    }
}

/* ============================ 补充题目3: 差分约束系统 ============================ */

/**
 * 差分约束系统求解 - Bellman-Ford算法应用
 * 
 * 问题描述: 求解一组形如 xj - xi ≤ ck 的不等式组
 * 算法思路: 将不等式转化为图论问题，使用Bellman-Ford求解
 * 
 * 转换方法:
 * 对于每个不等式 xj - xi ≤ ck，添加一条边 i->j，权重为ck
 * 添加超级源点0，到所有点的边权重为0
 * 运行Bellman-Ford算法，如果存在负环则无解，否则距离数组即为解
 * 
 * 时间复杂度: O(N×E)
 * 空间复杂度: O(N+E)
 */
class DifferenceConstraintsSolver {
    public int[] solveDifferenceConstraints(int n, int[][] constraints) {
        // 构建图（包含超级源点0）
        List<int[]> edges = new ArrayList<>();
        
        // 添加约束边
        for (int[] constraint : constraints) {
            edges.add(new int[]{constraint[0], constraint[1], constraint[2]});
        }
        
        // 添加超级源点边
        for (int i = 1; i <= n; i++) {
            edges.add(new int[]{0, i, 0});
        }
        
        // 运行Bellman-Ford
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[0] = 0;
        
        // n轮松弛（n+1个节点）
        for (int i = 0; i < n; i++) {
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], w = edge[2];
                if (distance[u] != Integer.MAX_VALUE && 
                    distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                }
            }
        }
        
        // 检测负环
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (distance[u] != Integer.MAX_VALUE && 
                distance[u] + w < distance[v]) {
                return null; // 无解
            }
        }
        
        // 返回解（去掉超级源点）
        int[] result = new int[n];
        System.arraycopy(distance, 1, result, 0, n);
        return result;
    }
}

/* ============================ Bellman-Ford算法工程实践总结 ============================ */

/**
 * Bellman-Ford算法工程实践关键要点:
 * 
 * 1. 算法优化策略:
 *    - 提前终止: 当某轮没有距离更新时提前结束
 *    - 队列优化: 使用SPFA算法提高平均性能
 *    - 滚动数组: 对于边数限制问题使用临时数组
 * 
 * 2. 数值稳定性处理:
 *    - 整数溢出: 使用long类型或检查中间值
 *    - 无穷大表示: 选择合适的不可达值
 *    - 浮点数精度: 注意浮点数比较的误差
 * 
 * 3. 应用场景分析:
 *    - 优势场景: 负权边、边数限制、负环检测
 *    - 劣势场景: 稠密图、非负权图（相比Dijkstra效率低）
 *    - 特殊应用: 网络路由协议、金融套利检测
 * 
 * 4. 与其他算法对比:
 *    - vs Dijkstra: 支持负权边，但时间复杂度更高
 *    - vs Floyd: 单源vs多源，Floyd适合小规模全源查询
 *    - vs SPFA: Bellman-Ford更稳定，SPFA平均性能更好
 * 
 * 5. 实际工程考量:
 *    - 内存访问模式: 连续内存访问优化
 *    - 缓存友好性: 数据局部性优化
 *    - 并行化潜力: 有限的可并行化机会
 * 
 * 6. 调试与测试策略:
 *    - 单元测试: 覆盖正常路径、负权边、负环等场景
 *    - 边界测试: 测试单节点、不连通图等边界情况
 *    - 性能测试: 针对不同规模图的性能分析
 */