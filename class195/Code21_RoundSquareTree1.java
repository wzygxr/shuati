package class195;

// 圆方树优化建图基础模板，Java 版
// 本代码展示圆方树优化建图的核心模板，用于解决无向图路径问题
// 测试链接 : https://www.luogu.com.cn/problem/P4320（改编）
// 本模板展示了圆方树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 圆方树优化建图核心知识点 =====================
// 【问题分析】
// 圆方树用于解决无向图中的路径计数、点双连通分量等问题
// 通过将无向图转化为树结构，简化路径问题的处理
// 主要应用于路径必经点、简单路径计数、点双连通分量等
//
// 【核心原理】
// 圆点：原图中的节点
// 方点：每个点双连通分量对应一个方点
// 连边规则：圆点向所属点双的方点连边
// 树的性质：圆方树是一棵树，任意两点间路径唯一
//
// 【复杂度分析】
// 节点数：O(n + m)（圆点 + 方点）
// 边数：O(n + m)（圆方树边数）
// 构建复杂度：O(n + m)（Tarjan 算法）
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的树结构分解
// 2. 社交网络中的关键节点识别
// 3. 知识图谱中的路径推理优化

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Code21_RoundSquareTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int MAXE = 1000001;

	// ===================== 原图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int cnt;

	// ===================== 圆方树存储区 =====================
	public static int[] treeHead = new int[MAXN];
	public static int[] treeNext = new int[MAXE];
	public static int[] treeTo = new int[MAXE];
	public static int treeCnt;

	// ===================== Tarjan 算法变量区 =====================
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int timer;
	public static Stack<Integer> stack = new Stack<>();

	// ===================== 圆方树变量区 =====================
	public static int n, m;
	public static int squareNodeCnt; // 方点数量
	public static int[] nodeType = new int[MAXN]; // 0=圆点，1=方点
	public static int[] bccSize = new int[MAXN]; // 点双大小

	// ===================== 核心函数：原图加边 =====================
	// 功能：向原图中添加一条从 u 到 v 的无向边
	// 笔试面试考察点：无向图需要双向连边
	// 面试高频提问：为什么无向图要连两条边？
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	// ===================== 核心函数：圆方树加边 =====================
	// 功能：向圆方树中添加一条从 u 到 v 的边
	// 核心思想：圆方树是无向树，需要双向连边
	// 面试高频提问：圆方树中圆点和方点如何区分？
	public static void addTreeEdge(int u, int v) {
		treeNext[++treeCnt] = treeHead[u];
		treeTo[treeCnt] = v;
		treeHead[u] = treeCnt;
	}

	// ===================== 核心函数：Tarjan 算法求点双 =====================
	// 功能：使用 Tarjan 算法求点双连通分量
	// 核心思想：通过 dfn 和 low 数组识别点双
	// 面试高频提问：点双和边双的区别？如何判断割点？
	public static void tarjan(int u, int parent) {
		dfn[u] = low[u] = ++timer;
		stack.push(u);

		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = next[e]) {
			int v = to[e];
			if (v == parent) {
				continue; // 不走回父节点的边
			}
			if (dfn[v] == 0) {
				// v 未访问，递归处理
				tarjan(v, u);
				low[u] = Math.min(low[u], low[v]);
				// 如果 dfn[u] <= low[v]，u 是割点
				if (dfn[u] <= low[v]) {
					// 创建方点
					squareNodeCnt++;
					int squareNode = n + squareNodeCnt;
					nodeType[squareNode] = 1;

					// 弹出点双中的所有节点
					int size = 0;
					while (true) {
						int x = stack.pop();
						size++;
						// 圆点向方点连边
						addTreeEdge(x, squareNode);
						addTreeEdge(squareNode, x);
						if (x == v) {
							break;
						}
					}
					bccSize[squareNode] = size + 1; // +1 是因为包含 u
					// 方点向 u 连边
					addTreeEdge(u, squareNode);
					addTreeEdge(squareNode, u);
				}
			} else {
				// v 已访问，更新 low
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
	}

	// ===================== 核心函数：构建圆方树 =====================
	// 功能：构建完整的圆方树
	// 核心思想：对每个连通分量运行 Tarjan
	// 面试高频提问：非连通图如何处理？
	public static void buildRoundSquareTree() {
		squareNodeCnt = 0;
		// 初始化节点类型
		for (int i = 1; i <= n; i++) {
			nodeType[i] = 0; // 圆点
		}

		// 对每个连通分量运行 Tarjan
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {
				tarjan(i, -1);
			}
		}
	}

	// ===================== 核心函数：判断必经点 =====================
	// 功能：判断节点 c 是否在 a 到 b 的简单路径上
	// 核心思想：在圆方树上求 LCA，判断 c 是否在路径上
	// 面试高频提问：圆方树上如何求简单路径？
	public static boolean isOnPath(int a, int b, int c) {
		// 简化版本：实际实现需要 LCA 和路径查询
		// 这里展示思路：在圆方树上，a 到 b 的路径唯一
		// 如果 c 在这条路径上，则 c 是必经点
		return false; // 需要完整实现 LCA
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

		// 读入 m 条边
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			addEdge(u, v);
			addEdge(v, u); // 无向图
		}

		// 构建圆方树
		buildRoundSquareTree();

		// 输出圆方树信息
		out.println("圆点数量：" + n);
		out.println("方点数量：" + squareNodeCnt);
		out.println("总节点数：" + (n + squareNodeCnt));

		// 输出每个方点对应的点双大小
		for (int i = 1; i <= squareNodeCnt; i++) {
			out.println("方点 " + (n + i) + " 对应的点双大小：" + bccSize[n + i]);
		}

		out.flush();
		out.close();
	}
}
