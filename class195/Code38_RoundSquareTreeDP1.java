package class195;

// 圆方树 DP 优化建图基础模板，Java 版
// 本代码展示圆方树 DP 优化建图的核心模板，用于解决无向图上的 DP 问题
// 测试链接 : https://www.luogu.com.cn/problem/P4320（改编）
// 本模板展示了圆方树 DP 优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 圆方树 DP 优化建图核心知识点 =====================
// 【问题分析】
// 圆方树 DP 用于解决无向图上的动态规划问题
// 通过将无向图转化为树结构，在树上进行 DP
// 主要应用于无向图路径计数、必经点、点双连通分量等
//
// 【核心原理】
// 圆方树构建：将点双连通分量转化为方点
// 树形 DP：在圆方树上进行动态规划
// 状态设计：考虑圆点和方点的不同转移
// 答案合并：将圆点和方点的贡献合并
//
// 【复杂度分析】
// 构建复杂度：O(n + m)
// DP 复杂度：O(n)
// 空间复杂度：O(n)
//
// 【ML/DL 关联价值】
// 1. 图结构上的动态规划
// 2. 树形神经网络中的信息传递
// 3. 组合优化中的分解策略

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code38_RoundSquareTreeDP1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int MOD = 1000000007;

	// ===================== 原图存储区 =====================
	public static List<Integer>[] graph = new ArrayList[MAXN];
	public static int n, m;

	// ===================== 圆方树存储区 =====================
	public static List<Integer>[] tree = new ArrayList[MAXN * 2];
	public static int totalNodes; // 总节点数（圆点 + 方点）

	// ===================== Tarjan 算法变量区 =====================
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int timer;
	public static int[] stack = new int[MAXN];
	public static int top;
	public static boolean[] inStack = new boolean[MAXN];
	public static int squareCnt; // 方点数量

	// ===================== DP 变量区 =====================
	public static long[] dp = new long[MAXN * 2]; // DP 数组
	public static int[] size = new int[MAXN * 2]; // 子树大小

	// ===================== 核心函数：原图加边 =====================
	public static void addGraphEdge(int u, int v) {
		if (graph[u] == null) graph[u] = new ArrayList<>();
		if (graph[v] == null) graph[v] = new ArrayList<>();
		graph[u].add(v);
		graph[v].add(u);
	}

	// ===================== 核心函数：圆方树加边 =====================
	public static void addTreeEdge(int u, int v) {
		if (tree[u] == null) tree[u] = new ArrayList<>();
		if (tree[v] == null) tree[v] = new ArrayList<>();
		tree[u].add(v);
		tree[v].add(u);
	}

	// ===================== 核心函数：Tarjan 算法求点双 =====================
	// 功能：使用 Tarjan 算法求点双连通分量并构建圆方树
	// 核心思想：通过 dfn 和 low 数组识别点双
	// 面试高频提问：点双和边双的区别？
	public static void tarjan(int u, int parent) {
		dfn[u] = low[u] = ++timer;
		stack[++top] = u;
		inStack[u] = true;

		if (graph[u] != null) {
			for (int v : graph[u]) {
				if (v == parent) {
					continue;
				}
				if (dfn[v] == 0) {
					tarjan(v, u);
					low[u] = Math.min(low[u], low[v]);

					if (low[v] >= dfn[u]) {
						// u 是割点，创建一个方点
						squareCnt++;
						int squareNode = n + squareCnt;
						totalNodes = Math.max(totalNodes, squareNode);

						// 弹出点双中的所有节点
						int x;
						do {
							x = stack[top--];
							inStack[x] = false;
							// 圆点向方点连边
							addTreeEdge(x, squareNode);
							addTreeEdge(squareNode, x);
						} while (x != v);

						// 方点向 u 连边
						addTreeEdge(u, squareNode);
						addTreeEdge(squareNode, u);
					}
				} else if (inStack[v]) {
					low[u] = Math.min(low[u], dfn[v]);
				}
			}
		}
	}

	// ===================== 核心函数：构建圆方树 =====================
	public static void buildRoundSquareTree() {
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {
				top = 0;
				tarjan(i, 0);
			}
		}
	}

	// ===================== 核心函数：树形 DP =====================
	// 功能：在圆方树上进行动态规划
	// 核心思想：后序遍历，自底向上计算
	// 面试高频提问：圆方树 DP 的状态设计？
	public static void treeDP(int u, int parent) {
		// 初始化
		dp[u] = 1;
		size[u] = 1;

		if (tree[u] != null) {
			for (int v : tree[u]) {
				if (v != parent) {
					treeDP(v, u);
					// 合并子树信息
					dp[u] = (dp[u] + dp[v]) % MOD;
					size[u] += size[v];
				}
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
			addGraphEdge(u, v);
		}

		// 构建圆方树
		totalNodes = n;
		squareCnt = 0;
		buildRoundSquareTree();

		// 执行树形 DP
		treeDP(1, 0);

		// 输出结果
		out.println("圆方树 DP 结果：");
		out.println("总节点数：" + totalNodes);
		out.println("方点数量：" + squareCnt);
		out.println("根节点的 DP 值：" + dp[1]);
		out.println("根节点的子树大小：" + size[1]);

		out.flush();
		out.close();
	}
}
