package class195;

// 网络流拆点优化建图基础模板，Java 版
// 本代码展示网络流中拆点优化建图的核心模板，用于解决节点容量限制问题
// 测试链接 : https://www.luogu.com.cn/problem/P3376（改编）
// 本模板展示了拆点优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 网络流拆点优化建图核心知识点 =====================
// 【问题分析】
// 网络流拆点用于解决节点有容量限制的问题
// 将每个节点拆分为入点和出点，中间连一条容量为节点容量的边
// 主要应用于节点容量限制、点权问题等
//
// 【核心原理】
// 节点拆分：将节点 u 拆分为 u_in 和 u_out
// 容量限制：u_in -> u_out 连边，容量为节点容量
// 边转化：原边 (u, v) 转化为 (u_out, v_in)
//
// 【复杂度分析】
// 节点数：2n（每个节点拆分为两个）
// 边数：m + n（原边 + 拆点边）
// 最大流复杂度：O(n²m) 或 O(nm²)
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的节点特征聚合
// 2. 流量分配问题的建模
// 3. 资源调度中的容量限制处理

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code14_NetworkFlow1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int MAXE = 400001;
	public static int INF = 1 << 30;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] capacity = new int[MAXE];
	public static int[] flow = new int[MAXE];
	public static int cnt = 1;

	// ===================== 网络流变量区 =====================
	public static int n, m, s, t;
	public static int[] level = new int[MAXN];
	public static int[] current = new int[MAXN];

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v、容量为 cap 的有向边
	// 核心思想：同时添加正向边和反向边
	// 面试高频提问：为什么要添加反向边？反向边的作用是什么？
	public static void addEdge(int u, int v, int cap) {
		// 正向边
		next[++cnt] = head[u];
		to[cnt] = v;
		capacity[cnt] = cap;
		flow[cnt] = 0;
		head[u] = cnt;

		// 反向边
		next[++cnt] = head[v];
		to[cnt] = u;
		capacity[cnt] = 0;
		flow[cnt] = 0;
		head[v] = cnt;
	}

	// ===================== 核心函数：拆点建图 =====================
	// 功能：将每个节点拆分为入点和出点
	// 核心思想：u_in -> u_out 连边，容量为节点容量
	// 面试高频提问：为什么要拆点？如何处理点权和边权？
	public static void buildNodeSplitGraph(int[] nodeCapacity) {
		for (int i = 1; i <= n; i++) {
			int uIn = i;
			int uOut = i + n;
			// 入点到出点连边，容量为节点容量
			addEdge(uIn, uOut, nodeCapacity[i]);
		}
	}

	// ===================== 核心函数：BFS 构建分层图 =====================
	// 功能：使用 BFS 构建分层图，计算每个节点的层次
	// 核心思想：只走残量大于 0 的边
	// 面试高频提问：分层图的作用是什么？为什么需要当前弧优化？
	public static boolean bfs() {
		// 初始化层次数组
		for (int i = 1; i <= 2 * n; i++) {
			level[i] = -1;
		}
		level[s] = 0;

		// BFS 队列
		List<Integer> queue = new ArrayList<>();
		queue.add(s);

		while (!queue.isEmpty()) {
			int u = queue.remove(0);
			// 遍历所有邻接边
			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				// 只走残量大于 0 且未访问的边
				if (capacity[e] - flow[e] > 0 && level[v] == -1) {
					level[v] = level[u] + 1;
					queue.add(v);
				}
			}
		}
		// 返回汇点是否可达
		return level[t] != -1;
	}

	// ===================== 核心函数：DFS 寻找增广路 =====================
	// 功能：在分层图上 DFS 寻找增广路
	// 核心思想：只向下走（层次 +1），使用当前弧优化
	// 面试高频提问：当前弧优化的原理？为什么能提高效率？
	public static int dfs(int u, int pushed) {
		if (pushed == 0 || u == t) {
			return pushed;
		}

		int totalFlow = 0;
		// 当前弧优化：从上次访问的边开始
		for (int e = current[u]; e > 0; e = next[e]) {
			current[u] = e;
			int v = to[e];
			// 只向下走一层
			if (level[v] != level[u] + 1) {
				continue;
			}
			// 计算可推送的流量
			int push = dfs(v, Math.min(pushed, capacity[e] - flow[e]));
			if (push > 0) {
				// 更新正向边和反向边的流量
				flow[e] += push;
				flow[e ^ 1] -= push;
				totalFlow += push;
				pushed -= push;
				if (pushed == 0) {
					break;
				}
			}
		}
		return totalFlow;
	}

	// ===================== 核心函数：Dinic 算法求最大流 =====================
	// 功能：使用 Dinic 算法计算从 s 到 t 的最大流
	// 核心思想：BFS 分层 + DFS 多路增广
	// 面试高频提问：Dinic 算法的时间复杂度？为什么比 EK 算法快？
	public static int dinic() {
		int maxFlow = 0;
		// 不断构建分层图并增广
		while (bfs()) {
			// 复制当前弧数组
			System.arraycopy(head, 0, current, 0, 2 * n + 1);
			// DFS 寻找增广路
			int pushed = dfs(s, INF);
			while (pushed > 0) {
				maxFlow += pushed;
				pushed = dfs(s, INF);
			}
		}
		return maxFlow;
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

		// 读入节点数、边数、源点、汇点
		n = in.nextInt();
		m = in.nextInt();
		s = in.nextInt();
		t = in.nextInt();

		// 读入每个节点的容量
		int[] nodeCapacity = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			nodeCapacity[i] = in.nextInt();
		}

		// 拆点建图
		buildNodeSplitGraph(nodeCapacity);

		// 读入 m 条边
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int cap = in.nextInt();
			// 注意：u 的出点连向 v 的入点
			addEdge(u + n, v, cap);
		}

		// 计算最大流
		int maxFlow = dinic();
		out.println("最大流：" + maxFlow);

		out.flush();
		out.close();
	}
}
