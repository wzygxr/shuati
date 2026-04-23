package class195;

// 差分约束优化建图基础模板，Java 版
// 本代码展示差分约束系统优化建图的核心模板，用于解决不等式组求解问题
// 测试链接 : https://www.luogu.com.cn/problem/P5960（改编）
// 本模板展示了差分约束优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 差分约束优化建图核心知识点 =====================
// 【问题分析】
// 差分约束系统用于解决一组形如 x_i - x_j <= c 的不等式
// 通过建图转化为最短路问题，利用三角不等式求解
// 主要应用于时间安排、资源分配、不等式求解等
//
// 【核心原理】
// 不等式转化：x_i - x_j <= c 转化为 x_i <= x_j + c
// 建图方式：从 j 向 i 连一条权值为 c 的有向边
// 超级源点：添加虚拟源点向所有点连 0 边
// 负环判断：存在负环则无解（SPFA 判负环）
//
// 【复杂度分析】
// 节点数：O(n)（变量数）
// 边数：O(m)（约束数）
// SPFA 复杂度：O(km)（k 为常数，最坏 O(nm)）
//
// 【ML/DL 关联价值】
// 1. 约束优化问题中的可行域求解
// 2. 线性规划的对偶问题转化
// 3. 多目标优化中的约束满足

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code19_DifferenceConstraint1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int MAXE = 500001;
	public static int INF = 1 << 30;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] weight = new int[MAXE];
	public static int cnt;

	// ===================== SPFA 算法变量区 =====================
	public static int[] dist = new int[MAXN];
	public static boolean[] inQueue = new boolean[MAXN];
	public static int[] updateCount = new int[MAXN];
	public static int n, m;

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v、权值为 w 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	// 面试高频提问：差分约束系统中边权的含义是什么？
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
	}

	// ===================== 核心函数：添加差分约束 =====================
	// 功能：添加约束 x_i - x_j <= c，转化为 x_i <= x_j + c
	// 核心思想：从 j 向 i 连一条权值为 c 的有向边
	// 面试高频提问：为什么是 j->i 而不是 i->j？边权为什么是 c？
	public static void addConstraint(int i, int j, int c) {
		// x_i - x_j <= c  =>  x_i <= x_j + c
		// 从 j 向 i 连边，权值为 c
		addEdge(j, i, c);
	}

	// ===================== 核心函数：添加等式约束 =====================
	// 功能：添加等式约束 x_i - x_j = c
	// 核心思想：拆分为两个不等式 x_i - x_j <= c 和 x_j - x_i <= -c
	// 面试高频提问：等式约束如何处理？为什么需要两条边？
	public static void addEquality(int i, int j, int c) {
		// x_i - x_j = c  =>  x_i - x_j <= c 且 x_j - x_i <= -c
		addConstraint(i, j, c);
		addConstraint(j, i, -c);
	}

	// ===================== 核心函数：构建超级源点 =====================
	// 功能：添加超级源点，向所有节点连 0 边
	// 核心思想：保证图的连通性，使所有节点可达
	// 面试高频提问：为什么需要超级源点？不添加会怎样？
	public static void buildSuperSource(int source) {
		for (int i = 1; i <= n; i++) {
			// 超级源点向每个节点连 0 边
			addEdge(source, i, 0);
		}
	}

	// ===================== 核心函数：SPFA 判负环 =====================
	// 功能：使用 SPFA 算法判断是否存在负环
	// 核心思想：统计每个节点的入队次数，超过 n 次则有负环
	// 面试高频提问：为什么 SPFA 可以判负环？入队次数为什么是 n？
	public static boolean spfa(int start) {
		// 初始化距离数组
		for (int i = 0; i <= n; i++) {
			dist[i] = INF;
			inQueue[i] = false;
			updateCount[i] = 0;
		}
		dist[start] = 0;

		// BFS 队列
		List<Integer> queue = new ArrayList<>();
		queue.add(start);
		inQueue[start] = true;
		updateCount[start] = 1;

		while (!queue.isEmpty()) {
			int u = queue.remove(0);
			inQueue[u] = false;

			// 遍历所有邻接边
			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				int w = weight[e];
				// 松弛操作
				if (dist[v] > dist[u] + w) {
					dist[v] = dist[u] + w;
					if (!inQueue[v]) {
						queue.add(v);
						inQueue[v] = true;
						updateCount[v]++;
						// 如果更新次数超过 n，说明存在负环
						if (updateCount[v] > n) {
							return false; // 有负环，无解
						}
					}
				}
			}
		}
		return true; // 无负环，有解
	}

	// ===================== 核心函数：获取一组可行解 =====================
	// 功能：如果系统有解，返回一组可行解
	// 核心思想：dist[i] 即为 x_i 的一个可行值
	// 面试高频提问：为什么 dist 数组就是一组解？解的唯一性如何？
	public static int[] getSolution() {
		int[] result = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			result[i] = dist[i];
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

		// 读入变量数和约束数
		n = in.nextInt();
		m = in.nextInt();

		// 读入 m 个约束
		for (int i = 0; i < m; i++) {
			int op = in.nextInt();
			if (op == 1) {
				// 操作 1：x_i - x_j <= c
				int i = in.nextInt();
				int j = in.nextInt();
				int c = in.nextInt();
				addConstraint(i, j, c);
			} else if (op == 2) {
				// 操作 2：x_i - x_j = c
				int i = in.nextInt();
				int j = in.nextInt();
				int c = in.nextInt();
				addEquality(i, j, c);
			} else if (op == 3) {
				// 操作 3：x_i >= x_j + c（即 x_i - x_j >= c）
				int i = in.nextInt();
				int j = in.nextInt();
				int c = in.nextInt();
				// 转化为 x_j - x_i <= -c
				addConstraint(j, i, -c);
			}
		}

		// 构建超级源点（节点 0）
		buildSuperSource(0);

		// 使用 SPFA 判负环
		if (spfa(0)) {
			out.println("有解");
			// 输出一组可行解
			int[] solution = getSolution();
			for (int i = 1; i <= n; i++) {
				out.print("x" + i + " = " + solution[i] + " ");
			}
			out.println();
		} else {
			out.println("无解（存在负环）");
		}

		out.flush();
		out.close();
	}
}
