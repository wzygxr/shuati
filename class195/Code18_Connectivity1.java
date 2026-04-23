package class195;

// 连通性优化建图基础模板，Java 版
// 本代码展示连通性优化建图的核心模板，用于解决动态连通性问题
// 测试链接 : https://www.luogu.com.cn/problem/P3367（改编）
// 本模板展示了连通性优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 连通性优化建图核心知识点 =====================
// 【问题分析】
// 连通性优化建图用于解决动态连通性、缩点、桥和割点等问题
// 通过 Tarjan 算法、并查集等工具优化连通性判断
// 主要应用于强连通分量、双连通分量、缩点建图等
//
// 【核心原理】
// Tarjan 算法：通过 dfn 和 low 数组识别 SCC
// 并查集：维护无向图的连通性
// 缩点建图：将 SCC 缩成点，构建 DAG
//
// 【复杂度分析】
// Tarjan 复杂度：O(n + m)
// 并查集复杂度：O(α(n))（近似常数）
// 缩点建图：O(n + m)
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的连通性特征
// 2. 社区发现算法
// 3. 聚类分析中的连通性约束

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Code18_Connectivity1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int MAXE = 200001;

	// ===================== 原图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int cnt;

	// ===================== 缩点后新图存储区 =====================
	public static int[] newHead = new int[MAXN];
	public static int[] newNext = new int[MAXE];
	public static int[] newTo = new int[MAXE];
	public static int newCnt;

	// ===================== Tarjan 算法变量区 =====================
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int[] scc = new int[MAXN];
	public static int timer, sccCnt;
	public static Stack<Integer> stack = new Stack<>();
	public static boolean[] inStack = new boolean[MAXN];

	// ===================== 并查集变量区 =====================
	public static int[] parent = new int[MAXN];
	public static int[] rank = new int[MAXN];

	// ===================== 连通性变量区 =====================
	public static int n, m;
	public static int[] sccSize = new int[MAXN];
	public static int[] sccInDegree = new int[MAXN];

	// ===================== 核心函数：原图加边 =====================
	// 功能：向原图中添加一条从 u 到 v 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	// ===================== 核心函数：新图加边 =====================
	// 功能：向缩点后的新图中添加一条从 u 到 v 的有向边
	// 核心思想：用于构建 SCC 缩点后的 DAG
	// 面试高频提问：为什么要构建新图？如何避免重边？
	public static void addNewEdge(int u, int v) {
		newNext[++newCnt] = newHead[u];
		newTo[newCnt] = v;
		newHead[u] = newCnt;
	}

	// ===================== 核心函数：并查集初始化 =====================
	// 功能：初始化并查集，每个节点自成一个集合
	// 核心思想：parent[i] = i，rank[i] = 0
	// 面试高频提问：路径压缩和按秩合并的作用？时间复杂度？
	public static void initUnionFind() {
		for (int i = 1; i <= n; i++) {
			parent[i] = i;
			rank[i] = 0;
		}
	}

	// ===================== 核心函数：并查集查找 =====================
	// 功能：查找节点 x 所在集合的代表元（带路径压缩）
	// 核心思想：递归查找并压缩路径
	// 面试高频提问：路径压缩的原理？为什么能优化复杂度？
	public static int find(int x) {
		if (parent[x] != x) {
			parent[x] = find(parent[x]); // 路径压缩
		}
		return parent[x];
	}

	// ===================== 核心函数：并查集合并 =====================
	// 功能：合并节点 x 和 y 所在的集合（按秩合并）
	// 核心思想：将秩小的树合并到秩大的树上
	// 面试高频提问：按秩合并的优势？如何维护秩？
	public static void union(int x, int y) {
		int rootX = find(x);
		int rootY = find(y);
		if (rootX != rootY) {
			if (rank[rootX] < rank[rootY]) {
				parent[rootX] = rootY;
			} else {
				parent[rootY] = rootX;
				if (rank[rootX] == rank[rootY]) {
					rank[rootX]++;
				}
			}
		}
	}

	// ===================== 核心函数：Tarjan 算法求 SCC =====================
	// 功能：使用 Tarjan 算法求强连通分量
	// 核心思想：通过 dfn 和 low 数组识别 SCC
	// 面试高频提问：dfn 和 low 的区别？如何判断 SCC 的根？
	public static void tarjan(int u) {
		dfn[u] = low[u] = ++timer;
		stack.push(u);
		inStack[u] = true;

		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = next[e]) {
			int v = to[e];
			if (dfn[v] == 0) {
				// v 未访问，递归处理
				tarjan(v);
				low[u] = Math.min(low[u], low[v]);
			} else if (inStack[v]) {
				// v 在栈中，更新 low
				low[u] = Math.min(low[u], dfn[v]);
			}
		}

		// 找到 SCC 的根节点
		if (dfn[u] == low[u]) {
			sccCnt++;
			// 弹出 SCC 中的所有节点
			while (true) {
				int v = stack.pop();
				inStack[v] = false;
				scc[v] = sccCnt;
				sccSize[sccCnt]++;
				if (u == v) {
					break;
				}
			}
		}
	}

	// ===================== 核心函数：缩点建图 =====================
	// 功能：将原图缩点后构建新的 DAG
	// 核心思想：不同 SCC 之间的边保留，同一 SCC 内的边忽略
	// 面试高频提问：缩点后的图有什么性质？如何在新图上 DP？
	public static void buildCondensationGraph() {
		// 遍历原图的所有边
		for (int u = 1; u <= n; u++) {
			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				// 如果 u 和 v 不在同一 SCC 中
				if (scc[u] != scc[v]) {
					// 添加边 scc[u] -> scc[v]
					addNewEdge(scc[u], scc[v]);
					sccInDegree[scc[v]]++;
				}
			}
		}
	}

	// ===================== 核心函数：判断两点是否连通 =====================
	// 功能：判断节点 u 和 v 是否在同一 SCC 中
	// 核心思想：检查 scc[u] 和 scc[v] 是否相等
	// 面试高频提问：有向图和无向图的连通性判断有什么区别？
	public static boolean isConnected(int u, int v) {
		return scc[u] == scc[v];
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
		}

		// 对所有未访问的节点运行 Tarjan
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {
				tarjan(i);
			}
		}

		// 输出 SCC 信息
		out.println("强连通分量数量：" + sccCnt);
		for (int i = 1; i <= sccCnt; i++) {
			out.println("SCC " + i + " 的大小：" + sccSize[i]);
		}

		// 缩点建图
		buildCondensationGraph();

		// 输出新图信息
		out.println("\n缩点后的 DAG：");
		for (int i = 1; i <= sccCnt; i++) {
			out.print("SCC " + i + " 的出边指向：");
			List<Integer> targets = new ArrayList<>();
			for (int e = newHead[i]; e > 0; e = newNext[e]) {
				targets.add(newTo[e]);
			}
			out.println(String.join(", ", targets.stream().map(String::valueOf).toArray(String[]::new)));
		}

		// 查询示例
		int queryCount = in.nextInt();
		for (int i = 0; i < queryCount; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			if (isConnected(u, v)) {
				out.println("节点 " + u + " 和 " + v + " 在同一 SCC 中");
			} else {
				out.println("节点 " + u + " 和 " + v + " 不在同一 SCC 中");
			}
		}

		out.flush();
		out.close();
	}
}
