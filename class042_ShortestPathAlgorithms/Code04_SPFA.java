package class065;

import java.io.*;
import java.util.*;

/**
 * SPFA算法深度解析与多题目实现
 * 
 * SPFA算法是Bellman-Ford算法的队列优化版本，用于解决单源最短路径问题
 * 核心思想: 只对距离发生变化的节点进行松弛操作，使用队列维护待处理节点
 * 
 * 算法特性:
 * - 队列优化: 相比Bellman-Ford减少不必要的松弛操作
 * - 负权边支持: 可以处理负权边
 * - 负环检测: 通过节点入队次数检测负权环
 * - 平均效率高: 在稀疏图中表现优异
 * 
 * 时间复杂度: 平均O(E)，最坏O(VE)，其中V是节点数，E是边数
 * 空间复杂度: O(V+E)，需要邻接表存储图和队列维护节点
 * 
 * 适用场景:
 * - 稀疏图的单源最短路径
 * - 需要检测负权环的场景
 * - 动态图的最短路径计算
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code04_SPFA {

	// 常量定义 - 最大节点数和边数
	public static int MAXN = 2001;  // 最大节点数
	public static int MAXM = 6001;  // 最大边数
	public static int MAXQ = 4000001; // 队列最大容量

	// 链式前向星建图数据结构
	// 这种存储方式节省空间，适合稀疏图
	public static int[] head = new int[MAXN];   // 每个节点的第一条边索引
	public static int[] next = new int[MAXM];   // 下一条边索引
	public static int[] to = new int[MAXM];      // 边指向的节点
	public static int[] weight = new int[MAXM];  // 边的权重
	public static int cnt;                      // 当前边数量

	// SPFA算法核心数据结构
	public static int[] distance = new int[MAXN];    // 源点到各节点的最短距离
	public static int[] updateCnt = new int[MAXN];   // 节点松弛次数（用于负环检测）
	public static int[] queue = new int[MAXQ];       // 待处理节点队列
	public static int l, r;                         // 队列头尾指针
	public static boolean[] enter = new boolean[MAXN]; // 节点是否在队列中标记

	/**
	 * 初始化函数 - 重置所有数据结构为初始状态
	 * 
	 * 初始化策略:
	 * - 边计数器重置为1（链式前向星从1开始计数）
	 * - 队列指针重置为0（空队列）
	 * - 各数组从索引1到n进行初始化（节点编号从1开始）
	 * 
	 * @param n 节点数量
	 */
	public static void build(int n) {
		cnt = 1;        // 边计数器从1开始
		l = r = 0;       // 队列头尾指针重置
		
		// 初始化head数组（链式前向星）
		Arrays.fill(head, 1, n + 1, 0);
		// 初始化入队标记数组
		Arrays.fill(enter, 1, n + 1, false);
		// 初始化距离数组（无穷大表示不可达）
		Arrays.fill(distance, 1, n + 1, Integer.MAX_VALUE);
		// 初始化松弛次数数组
		Arrays.fill(updateCnt, 1, n + 1, 0);
	}

	/**
	 * 添加边函数 - 使用链式前向星存储图结构
	 * 
	 * 链式前向星存储优势:
	 * - 空间效率高: 只存储实际存在的边
	 * - 遍历效率高: 可以快速遍历某个节点的所有出边
	 * - 内存局部性好: 连续存储相关数据
	 * 
	 * 存储原理:
	 * head[u]指向节点u的第一条边
	 * next数组形成链表，连接同一节点的所有出边
	 * 
	 * @param u 边的起点
	 * @param v 边的终点  
	 * @param w 边的权重
	 */
	public static void addEdge(int u, int v, int w) {
		next[cnt] = head[u];   // 新边的next指向原第一条边
		to[cnt] = v;           // 设置边的终点
		weight[cnt] = w;       // 设置边的权重
		head[u] = cnt++;       // 更新节点u的第一条边索引
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int cases = (int) in.nval;
		for (int i = 0, n, m; i < cases; i++) {
			in.nextToken();
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			build(n);
			for (int j = 0, u, v, w; j < m; j++) {
				in.nextToken();
				u = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				in.nextToken();
				w = (int) in.nval;
				if (w >= 0) {
					// 如果权重非负，添加双向边
					addEdge(u, v, w);
					addEdge(v, u, w);
				} else {
					// 如果权重为负，只添加单向边
					addEdge(u, v, w);
				}
			}
			// 调用SPFA算法检测负环
			out.println(spfa(n) ? "YES" : "NO");
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * SPFA算法核心实现 - 负环检测版本
	 * 
	 * 算法流程:
	 * 1. 初始化源点距离为0，加入队列
	 * 2. 循环处理队列中的节点，直到队列为空
	 * 3. 对每个出队节点，遍历其所有出边进行松弛操作
	 * 4. 如果松弛成功且目标节点不在队列中，加入队列
	 * 5. 检测节点入队次数，超过n-1次说明存在负环
	 * 
	 * 负环检测原理:
	 * - 在不存在负环的图中，最短路径最多包含n-1条边
	 * - 如果某个节点被松弛超过n-1次，说明存在负环
	 * - 因为负环可以无限次减小路径长度
	 * 
	 * @param n 节点数量
	 * @return 如果存在负环返回true，否则返回false
	 */
	public static boolean spfa(int n) {
		// 初始化源点（节点1）
		distance[1] = 0;           // 源点到自身距离为0
		updateCnt[1]++;            // 源点松弛次数加1
		queue[r++] = 1;            // 源点加入队列
		enter[1] = true;           // 标记源点在队列中
		
		// 队列不为空时继续处理
		while (l < r) {
			int u = queue[l++];     // 取出队首节点
			enter[u] = false;        // 标记节点已出队
			
			// 遍历节点u的所有出边（链式前向星遍历）
			for (int ei = head[u]; ei > 0; ei = next[ei]) {
				int v = to[ei];      // 边的终点
				int w = weight[ei];  // 边的权重
				
				// 松弛操作: 如果通过u可以缩短到v的距离
				// 注意: 需要检查distance[u]不为无穷大，避免整数溢出
				if (distance[u] != Integer.MAX_VALUE && 
					distance[u] + w < distance[v]) {
					
					distance[v] = distance[u] + w; // 更新最短距离
					
					// 如果v不在队列中，加入队列
					if (!enter[v]) {
						// 负环检测: 如果节点v被松弛超过n-1次
						if (++updateCnt[v] > n - 1) {
							return true; // 存在负环
						}
						queue[r++] = v;  // 节点v加入队列
						enter[v] = true;  // 标记v在队列中
					}
				}
			}
		}
		
		return false; // 不存在负环
	}
	
	/**
	 * SPFA算法 - 单源最短路径版本（不带负环检测）
	 * 
	 * 适用于只需要最短路径，不需要检测负环的场景
	 * 相比带负环检测版本，效率更高
	 * 
	 * @param n 节点数量
	 * @param start 源点
	 * @return 距离数组，如果存在负环返回null
	 */
	public static int[] spfaShortestPath(int n, int start) {
		// 初始化
		Arrays.fill(distance, 1, n + 1, Integer.MAX_VALUE);
		Arrays.fill(enter, 1, n + 1, false);
		l = r = 0;
		
		distance[start] = 0;
		queue[r++] = start;
		enter[start] = true;
		
		while (l < r) {
			int u = queue[l++];
			enter[u] = false;
			
			for (int ei = head[u]; ei > 0; ei = next[ei]) {
				int v = to[ei];
				int w = weight[ei];
				
				if (distance[u] != Integer.MAX_VALUE && 
					distance[u] + w < distance[v]) {
					distance[v] = distance[u] + w;
					
					if (!enter[v]) {
						queue[r++] = v;
						enter[v] = true;
					}
				}
			}
		}
		
		// 返回距离数组的副本
		return Arrays.copyOfRange(distance, 1, n + 1);
	}

}

/* ============================ 补充题目1: LeetCode 743. 网络延迟时间 - SPFA实现 ============================ */

/**
 * LeetCode 743. 网络延迟时间 - SPFA算法实现
 * 
 * 使用SPFA算法解决网络延迟时间问题，相比Bellman-Ford效率更高
 * 特别适合稀疏图的单源最短路径计算
 */
class NetworkDelayTimeSPFA {
    public int networkDelayTime(int[][] times, int n, int k) {
        // 构建邻接表
        List<int[]>[] graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] time : times) {
            graph[time[0]].add(new int[]{time[1], time[2]});
        }
        
        // SPFA算法
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[k] = 0;
        
        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[n + 1];
        queue.offer(k);
        inQueue[k] = true;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;
            
            for (int[] edge : graph[u]) {
                int v = edge[0], w = edge[1];
                if (distance[u] != Integer.MAX_VALUE && 
                    distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    if (!inQueue[v]) {
                        queue.offer(v);
                        inQueue[v] = true;
                    }
                }
            }
        }
        
        // 计算最大延迟
        int maxDelay = 0;
        for (int i = 1; i <= n; i++) {
            if (distance[i] == Integer.MAX_VALUE) return -1;
            maxDelay = Math.max(maxDelay, distance[i]);
        }
        return maxDelay;
    }
}

/* ============================ SPFA算法工程实践总结 ============================ */

/**
 * SPFA算法工程实践关键要点:
 * 
 * 1. 性能优化策略:
 *    - 数据结构选择: 邻接表适合稀疏图，链式前向星节省空间
 *    - 队列优化: 使用双端队列(Deque)可以进一步提高效率
 *    - 内存优化: 对象池技术减少GC压力
 * 
 * 2. 算法变种与优化:
 *    - SLF优化: 新节点如果距离小于队首，插入队首
 *    - LLL优化: 维护队列平均距离，优化出队顺序
 *    - 容错SPFA: 增加随机化避免最坏情况
 * 
 * 3. 应用场景分析:
 *    - 优势场景: 稀疏图、动态图、需要负环检测
 *    - 劣势场景: 稠密图、恶意构造的最坏情况图
 *    - 特殊应用: 网络路由、实时系统、游戏AI
 * 
 * 4. 与其他算法对比:
 *    - vs Bellman-Ford: SPFA平均效率更高，但最坏情况相同
 *    - vs Dijkstra: SPFA支持负权边，但一般情况效率较低
 *    - vs Floyd: 单源vs多源，适用场景不同
 * 
 * 5. 实际工程考量:
 *    - 稳定性: 在最坏情况下可能退化为O(VE)，需要防护措施
 *    - 内存管理: 大规模图的存储和访问优化
 *    - 并发安全: 多线程环境下的线程安全问题
 */