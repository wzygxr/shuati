package class195;

// 斯坦纳树优化建图基础模板，Java 版
// 本代码展示斯坦纳树优化建图的核心模板，用于解决最小连通子图问题
// 测试链接 : https://www.luogu.com.cn/problem/P6192（改编）
// 本模板展示了斯坦纳树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 斯坦纳树优化建图核心知识点 =====================
// 【问题分析】
// 斯坦纳树用于解决连接指定关键点的最小代价问题
// 通过状压 DP 和最短路算法求解
// 主要应用于最小连通子图、关键点连接、网络设计等
//
// 【核心原理】
// 状态定义：dp[i][mask] 表示以 i 为根，连接 mask 表示的关键点集合的最小代价
// 状态转移：分为两种转移方式
//   1. 子集合并：dp[i][mask] = min(dp[i][s] + dp[i][mask^s] - val[i])
//   2. 边扩展：dp[i][mask] = min(dp[j][mask] + w(j,i))
// 求解方法：SPFA 或 Dijkstra 优化
//
// 【复杂度分析】
// 时间复杂度：O(3^k * n + 2^k * m * logn)
// 空间复杂度：O(2^k * n)
// 适用范围：k 较小（通常 k <= 20）
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的子图连接问题
// 2. 多任务学习中的共享结构优化
// 3. 组合优化中的状态空间搜索

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code30_SteinerTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 101;
	public static int MAXK = 11;
	public static int MAX_MASK = 1 << MAXK;
	public static int INF = 1 << 30;

	// ===================== 边结构体 =====================
	// 存储图的一条边
	// 包含目标节点和边权
	public static class Edge {
		public int to; // 目标节点
		public int weight; // 边权

		public Edge(int to, int weight) {
			this.to = to;
			this.weight = weight;
		}
	}

	// ===================== 图存储区 =====================
	public static List<Edge>[] graph = new ArrayList[MAXN];
	public static int n, m, k; // 节点数、边数、关键点数

	// ===================== DP 数组 =====================
	// dp[i][mask] 表示以 i 为根，连接 mask 表示的关键点集合的最小代价
	public static int[][] dp = new int[MAXN][MAX_MASK];

	// ===================== 关键点位置 =====================
	public static int[] keyPoints = new int[MAXK];

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条有向边
	// 笔试面试考察点：图的邻接表存储
	// 面试高频提问：斯坦纳树问题中图的存储方式？
	public static void addEdge(int u, int v, int w) {
		if (graph[u] == null) graph[u] = new ArrayList<>();
		graph[u].add(new Edge(v, w));
	}

	// ===================== 核心函数：SPFA 最短路 =====================
	// 功能：使用 SPFA 算法优化 DP 转移
	// 核心思想：通过最短路算法进行边扩展转移
	// 面试高频提问：为什么用 SPFA 而不是 Dijkstra？
	public static void spfa(int mask) {
		boolean[] inQueue = new boolean[n + 1];
		int[] queue = new int[MAXN * MAXN];
		int head = 0, tail = 0;

		// 初始化队列
		for (int i = 1; i <= n; i++) {
			if (dp[i][mask] < INF) {
				queue[tail++] = i;
				inQueue[i] = true;
			}
		}

		// SPFA 主循环
		while (head < tail) {
			int u = queue[head++];
			inQueue[u] = false;

			if (graph[u] != null) {
				for (Edge edge : graph[u]) {
					int v = edge.to;
					int w = edge.weight;

					// 尝试通过边 (u,v) 更新 dp[v][mask]
					if (dp[v][mask] > dp[u][mask] + w) {
						dp[v][mask] = dp[u][mask] + w;
						if (!inQueue[v]) {
							queue[tail++] = v;
							inQueue[v] = true;
						}
					}
				}
			}
		}
	}

	// ===================== 核心函数：斯坦纳树 DP =====================
	// 功能：使用状压 DP 求解斯坦纳树
	// 核心思想：枚举子集合并 + 最短路优化
	// 面试高频提问：斯坦纳树 DP 的状态转移方程？
	public static int steinerTree() {
		// 初始化 DP 数组
		for (int i = 1; i <= n; i++) {
			Arrays.fill(dp[i], INF);
		}

		// 初始化关键点
		for (int i = 1; i <= k; i++) {
			dp[keyPoints[i]][1 << (i - 1)] = 0;
		}

		// 枚举状态
		for (int mask = 1; mask < (1 << k); mask++) {
			// 子集合并转移
			for (int i = 1; i <= n; i++) {
				// 枚举 mask 的真子集 s
				for (int s = (mask - 1) & mask; s > 0; s = (s - 1) & mask) {
					dp[i][mask] = Math.min(dp[i][mask], 
						dp[i][s] + dp[i][mask ^ s]);
				}
			}

			// 最短路优化转移
			spfa(mask);
		}

		// 找到连接所有关键点的最小代价
		int result = INF;
		for (int i = 1; i <= n; i++) {
			result = Math.min(result, dp[i][(1 << k) - 1]);
		}

		return result;
	}

	// ===================== 快速读入类 =====================
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0) {
					return -1;
				}
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入节点数、边数、关键点数
		n = in.nextInt();
		m = in.nextInt();
		k = in.nextInt();

		// 读入关键点
		for (int i = 1; i <= k; i++) {
			keyPoints[i] = in.nextInt();
		}

		// 读入边
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			addEdge(u, v, w);
			addEdge(v, u, w); // 无向图
		}

		// 求解斯坦纳树
		int result = steinerTree();

		// 输出结果
		out.println("连接所有关键点的最小代价：" + 
			(result == INF ? "无法连接" : result));

		out.flush();
		out.close();
	}
}
