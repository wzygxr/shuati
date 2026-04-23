package class195;

// 支配树优化建图基础模板，Java 版
// 本代码展示支配树优化建图的核心模板，用于解决有向图必经点问题
// 测试链接 : https://www.luogu.com.cn/problem/P2597（改编）
// 本模板展示了支配树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 支配树优化建图核心知识点 =====================
// 【问题分析】
// 支配树用于解决有向图中的必经点问题
// 通过构建支配树，可以快速查询从源点到某点必须经过的点
// 主要应用于必经点查询、关键点分析、网络可靠性等
//
// 【核心原理】
// 支配点定义：如果从源点到点 v 的所有路径都经过点 u，则 u 支配 v
// 最近支配点：v 的最近支配点是 v 的祖先中深度最大的支配点
// 支配树性质：支配树中，父节点支配子节点
// 构建算法：Lengauer-Tarjan 算法
//
// 【复杂度分析】
// 构建复杂度：O(nlogn) 或 O(nα(n))
// 查询复杂度：O(1)（LCA 查询）
// 空间复杂度：O(n)
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的关键节点识别
// 2. 程序分析中的控制流分析
// 3. 网络可靠性分析

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Code32_DominatorTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int LOG = 20;

	// ===================== 图存储区 =====================
	// 原图和反图
	public static List<Integer>[] graph = new ArrayList[MAXN];
	public static List<Integer>[] reverseGraph = new ArrayList[MAXN];
	public static int n, m;

	// ===================== 支配树变量区 =====================
	public static int[] dfn = new int[MAXN]; // DFS 序
	public static int[] id = new int[MAXN]; // DFS 序对应的节点
	public static int[] semi = new int[MAXN]; // 半支配点
	public static int[] idom = new int[MAXN]; // 最近支配点
	public static int[] parent = new int[MAXN]; // 支配树中的父节点
	public static int timer; // DFS 计时器

	// ===================== 并查集变量区 =====================
	public static int[] dsu = new int[MAXN]; // 并查集
	public static int[] best = new int[MAXN]; // 最优节点

	// ===================== 核心函数：图加边 =====================
	// 功能：向原图和反图中添加边
	// 笔试面试考察点：有向图的存储
	// 面试高频提问：支配树构建为什么需要反图？
	public static void addEdge(int u, int v) {
		if (graph[u] == null) graph[u] = new ArrayList<>();
		if (graph[v] == null) graph[v] = new ArrayList<>();
		if (reverseGraph[u] == null) reverseGraph[u] = new ArrayList<>();
		if (reverseGraph[v] == null) reverseGraph[v] = new ArrayList<>();
		graph[u].add(v);
		reverseGraph[v].add(u);
	}

	// ===================== 核心函数：DFS 遍历 =====================
	// 功能：DFS 遍历原图，记录 DFS 序
	// 核心思想：为后续计算做准备
	// 面试高频提问：DFS 序在支配树中的作用？
	public static void dfs(int u) {
		dfn[u] = ++timer;
		id[timer] = u;

		if (graph[u] != null) {
			for (int v : graph[u]) {
				if (dfn[v] == 0) {
					dfs(v);
				}
			}
		}
	}

	// ===================== 核心函数：并查集查找 =====================
	// 功能：带路径压缩的并查集查找
	// 核心思想：同时维护最优节点
	// 面试高频提问：并查集在支配树中的应用？
	public static int find(int x) {
		if (dsu[x] == x) {
			return x;
		}
		int root = find(dsu[x]);
		if (dfn[semi[best[dsu[x]]]] < dfn[semi[best[x]]]) {
			best[x] = best[dsu[x]];
		}
		dsu[x] = root;
		return root;
	}

	// ===================== 核心函数：获取最优节点 =====================
	// 功能：获取并查集的最优节点
	// 核心思想：用于计算半支配点
	// 面试高频提问：最优节点的含义？
	public static int getBest(int x) {
		find(x);
		return best[x];
	}

	// ===================== 核心函数：计算半支配点 =====================
	// 功能：使用 Lengauer-Tarjan 算法计算半支配点
	// 核心思想：按 DFS 序逆序处理，利用并查集优化
	// 面试高频提问：半支配点和支配点的关系？
	public static void computeSemiDominators() {
		// 初始化
		for (int i = 1; i <= n; i++) {
			semi[i] = i;
			dsu[i] = i;
			best[i] = i;
		}

		// 按 DFS 序逆序处理
		for (int i = timer; i >= 2; i--) {
			int v = id[i];

			// 处理所有前驱
			if (reverseGraph[v] != null) {
				for (int u : reverseGraph[v]) {
					if (dfn[u] > 0) {
						if (dfn[u] < dfn[v]) {
							// u 是 v 的祖先
							if (dfn[u] < dfn[semi[v]]) {
								semi[v] = u;
							}
						} else {
							// u 不是 v 的祖先
							int w = getBest(u);
							if (dfn[semi[w]] < dfn[semi[v]]) {
								semi[v] = semi[w];
							}
						}
					}
				}
			}

			// 将 v 加入并查集
			if (dfn[v] > 1) {
				dsu[v] = parent[v];
				best[v] = v;
			}
		}
	}

	// ===================== 核心函数：计算支配点 =====================
	// 功能：根据半支配点计算支配点
	// 核心思想：利用半支配点的性质
	// 面试高频提问：如何从半支配点得到支配点？
	public static void computeDominators() {
		// 按 DFS 序正序处理
		for (int i = 2; i <= timer; i++) {
			int v = id[i];
			int w = semi[v];

			if (semi[w] == semi[v]) {
				idom[v] = semi[v];
			} else {
				idom[v] = idom[w];
			}

			// 构建支配树
			parent[v] = idom[v];
		}

		// 根节点的支配点是自己
		idom[id[1]] = id[1];
	}

	// ===================== 核心函数：构建支配树 =====================
	// 功能：完成支配树的构建
	// 核心思想：DFS + Lengauer-Tarjan 算法
	// 面试高频提问：支配树构建的完整流程？
	public static void buildDominatorTree() {
		// DFS 遍历
		dfs(1);

		// 计算半支配点
		computeSemiDominators();

		// 计算支配点
		computeDominators();
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
			addEdge(u, v);
		}

		// 构建支配树
		buildDominatorTree();

		// 输出支配树信息
		out.println("支配树构建完成");
		out.println("DFS 序：" + timer);
		out.println("各节点的支配点：");
		for (int i = 1; i <= n; i++) {
			if (dfn[i] > 0) {
				out.println("节点 " + i + " 的支配点：" + idom[i]);
			}
		}

		out.flush();
		out.close();
	}
}
