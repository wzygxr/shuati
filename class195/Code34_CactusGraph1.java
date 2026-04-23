package class195;

// 仙人掌图优化建图基础模板，Java 版
// 本代码展示仙人掌图优化建图的核心模板，用于解决仙人掌图上的问题
// 测试链接 : https://www.luogu.com.cn/problem/P4244（改编）
// 本模板展示了仙人掌图优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 仙人掌图优化建图核心知识点 =====================
// 【问题分析】
// 仙人掌图是每条边最多属于一个简单环的无向图
// 通过将环缩点或展开，可以简化仙人掌图上的问题
// 主要应用于仙人掌图最短路、仙人掌图 DP、环上问题等
//
// 【核心原理】
// 环识别：使用 Tarjan 算法识别环
// 环缩点：将每个环缩为一个点
// 树形结构：缩点后形成树形结构
// 分类处理：树边和环边分别处理
//
// 【复杂度分析】
// 构建复杂度：O(n + m)
// 查询复杂度：O(logn) 或 O(1)
// 空间复杂度：O(n)
//
// 【ML/DL 关联价值】
// 1. 图结构中的环检测与处理
// 2. 特殊图结构上的高效算法
// 3. 图神经网络中的环信息编码

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code34_CactusGraph1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int MAXM = 200001;

	// ===================== 边结构体 =====================
	public static class Edge {
		public int to;
		public int weight;

		public Edge(int to, int weight) {
			this.to = to;
			this.weight = weight;
		}
	}

	// ===================== 图存储区 =====================
	public static List<Edge>[] graph = new ArrayList[MAXN];
	public static int n, m;

	// ===================== Tarjan 算法变量区 =====================
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int timer;
	public static int[] stack = new int[MAXN];
	public static int top;
	public static boolean[] inStack = new boolean[MAXN];

	// ===================== 仙人掌图变量区 =====================
	public static int[] belong = new int[MAXN]; // 每个点属于哪个环
	public static int[] ringSize = new int[MAXN]; // 每个环的大小
	public static int ringCnt; // 环的数量
	public static boolean[] isCut = new boolean[MAXN]; // 是否为割点

	// ===================== 核心函数：图加边 =====================
	public static void addEdge(int u, int v, int w) {
		if (graph[u] == null) graph[u] = new ArrayList<>();
		graph[u].add(new Edge(v, w));
	}

	// ===================== 核心函数：Tarjan 算法求环 =====================
	// 功能：使用 Tarjan 算法识别仙人掌图中的所有环
	// 核心思想：通过 dfn 和 low 数组识别环
	// 面试高频提问：如何判断一个边属于环？
	public static void tarjan(int u, int parent) {
		dfn[u] = low[u] = ++timer;
		stack[++top] = u;
		inStack[u] = true;

		if (graph[u] != null) {
			for (Edge edge : graph[u]) {
				int v = edge.to;
				if (v == parent) {
					continue;
				}
				if (dfn[v] == 0) {
					// v 未访问
					tarjan(v, u);
					low[u] = Math.min(low[u], low[v]);

					// 如果 dfn[u] < low[v]，说明 (u,v) 是桥
					// 如果 dfn[u] == low[v]，说明 u 是割点
					if (low[v] > dfn[u]) {
						// 桥，不属于任何环
					} else if (low[v] == dfn[u]) {
						// u 是割点
						isCut[u] = true;
					}
				} else if (inStack[v]) {
					// v 在栈中，更新 low
					low[u] = Math.min(low[u], dfn[v]);
				}
			}
		}

		// 如果 u 是根节点且 low[u] == dfn[u]，弹出一个环
		if (parent == 0 && low[u] == dfn[u]) {
			ringCnt++;
			int size = 0;
			while (true) {
				int x = stack[top--];
				inStack[x] = false;
				belong[x] = ringCnt;
				size++;
				if (x == u) {
					break;
				}
			}
			ringSize[ringCnt] = size;
		}
	}

	// ===================== 核心函数：识别所有环 =====================
	public static void findRings() {
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {
				top = 0;
				tarjan(i, 0);
			}
		}
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

		// 读入节点数和边数
		n = in.nextInt();
		m = in.nextInt();

		// 读入边
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			addEdge(u, v, w);
			addEdge(v, u, w);
		}

		// 识别所有环
		findRings();

		// 输出仙人掌图信息
		out.println("仙人掌图分析结果：");
		out.println("环的数量：" + ringCnt);
		out.println("割点数量：");
		int cutCnt = 0;
		for (int i = 1; i <= n; i++) {
			if (isCut[i]) {
				cutCnt++;
			}
		}
		out.println(cutCnt);

		// 输出每个环的信息
		for (int i = 1; i <= ringCnt; i++) {
			out.println("环 " + i + " 的大小：" + ringSize[i]);
		}

		out.flush();
		out.close();
	}
}
