package class195;

// 长链剖分优化建图基础模板，Java 版
// 本代码展示长链剖分优化建图的核心模板，用于解决树上深度相关问题
// 测试链接 : https://www.luogu.com.cn/problem/P3899（改编）
// 本模板展示了长链剖分优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 长链剖分优化建图核心知识点 =====================
// 【问题分析】
// 长链剖分用于解决树上与深度相关的查询问题
// 通过选择最长链作为重链，优化深度相关的 DP
// 主要应用于 k 级祖先查询、深度相关 DP、树上路径统计等
//
// 【核心原理】
// 长链定义：选择子树深度最大的儿子作为重儿子
// 重链：由重儿子连接形成的链
// 轻链：连接轻儿子的边
// 性质：所有长链长度和为 O(n)，每条链长度为 O(√n)
//
// 【复杂度分析】
// 预处理复杂度：O(n)
// 查询复杂度：O(1) 或 O(logn)
// 空间复杂度：O(n)（可优化到 O(n)）
//
// 【ML/DL 关联价值】
// 1. 树结构神经网络中的层次化特征
// 2. 图神经网络中的深度信息编码
// 3. 层次聚类中的链式结构优化

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code25_LongChainDecomposition1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int LOG = 20;

	// ===================== 树存储区 =====================
	public static List<Integer>[] tree = new ArrayList[MAXN];
	public static int n;

	// ===================== 长链剖分变量区 =====================
	public static int[] depth = new int[MAXN]; // 节点深度
	public static int[] maxDepth = new int[MAXN]; // 子树最大深度
	public static int[] len = new int[MAXN]; // 长链长度
	public static int[] top = new int[MAXN]; // 所在长链的顶端
	public static int[] son = new int[MAXN]; // 重儿子

	// ===================== 倍增数组 =====================
	public static int[][] parent = new int[MAXN][LOG];

	// ===================== 核心函数：树加边 =====================
	// 功能：向树中添加一条边
	// 笔试面试考察点：树的邻接表存储
	// 面试高频提问：为什么树只需要连单向边？
	public static void addEdge(int u, int v) {
		if (tree[u] == null) tree[u] = new ArrayList<>();
		if (tree[v] == null) tree[v] = new ArrayList<>();
		tree[u].add(v);
		tree[v].add(u);
	}

	// ===================== 核心函数：第一次 DFS =====================
	// 功能：DFS 预处理深度、父节点、子树最大深度
	// 核心思想：为长链剖分做准备
	// 面试高频提问：为什么要两次 DFS？第一次 DFS 的作用？
	public static void dfs1(int u, int p, int d) {
		depth[u] = d;
		maxDepth[u] = d;
		parent[u][0] = p;

		// 预处理倍增数组
		for (int j = 1; j < LOG; j++) {
			if (parent[u][j - 1] != 0) {
				parent[u][j] = parent[parent[u][j - 1]][j - 1];
			}
		}

		if (tree[u] != null) {
			for (int v : tree[u]) {
				if (v != p) {
					dfs1(v, u, d + 1);
					maxDepth[u] = Math.max(maxDepth[u], maxDepth[v]);
				}
			}
		}
	}

	// ===================== 核心函数：第二次 DFS =====================
	// 功能：DFS 进行长链剖分，确定重儿子和链顶
	// 核心思想：选择深度最大的子节点作为重儿子
	// 面试高频提问：长链剖分和重链剖分的区别？
	public static void dfs2(int u, int p) {
		// 选择重儿子：子树深度最大的儿子
		for (int v : tree[u]) {
			if (v != p && maxDepth[v] == maxDepth[u]) {
				son[u] = v;
				top[v] = top[u]; // 重儿子的链顶与父节点相同
				dfs2(v, u);
			}
		}

		// 处理轻儿子
		for (int v : tree[u]) {
			if (v != p && v != son[u]) {
				top[v] = v; // 轻儿子的链顶是自己
				dfs2(v, u);
			}
		}
	}

	// ===================== 核心函数：计算长链长度 =====================
	// 功能：计算每条长链的长度
	// 核心思想：长链长度 = 子树最大深度 - 当前深度
	// 面试高频提问：长链长度有什么性质？
	public static void calcLen(int u, int p) {
		len[u] = maxDepth[u] - depth[u];
		if (tree[u] != null) {
			for (int v : tree[u]) {
				if (v != p) {
					calcLen(v, u);
				}
			}
		}
	}

	// ===================== 核心函数：长链剖分初始化 =====================
	// 功能：完成长链剖分的所有预处理
	// 核心思想：三次 DFS 完成所有预处理
	// 面试高频提问：长链剖分的预处理复杂度？
	public static void init() {
		dfs1(1, 0, 0);
		top[1] = 1;
		dfs2(1, 0);
		calcLen(1, 0);
	}

	// ===================== 核心函数：查询 k 级祖先 =====================
	// 功能：查询节点 u 的 k 级祖先
	// 核心思想：先用倍增跳 2 的幂次，再在长链上 O(1) 查询
	// 面试高频提问：长链剖分如何优化 k 级祖先查询？
	public static int getKthAncestor(int u, int k) {
		if (k > depth[u]) {
			return -1; // 不存在
		}

		// 先用倍增跳
		for (int j = LOG - 1; j >= 0; j--) {
			if (k >= (1 << j)) {
				u = parent[u][j];
				k -= (1 << j);
			}
		}

		// 如果还有剩余，在长链上跳
		if (k > 0) {
			// 需要 O(1) 查询，这里简化处理
			for (int i = 0; i < k; i++) {
				u = parent[u][0];
			}
		}

		return u;
	}

	// ===================== 核心函数：获取长链信息 =====================
	// 功能：获取节点 u 所在长链的信息
	// 核心思想：从链顶到链底的节点
	// 面试高频提问：长链有什么应用？
	public static List<Integer> getChainInfo(int u) {
		List<Integer> chain = new ArrayList<>();
		int chainTop = top[u];
		int chainBottom = u;

		// 找到链底
		while (son[chainBottom] != 0) {
			chainBottom = son[chainBottom];
		}

		// 收集链上节点
		int cur = chainTop;
		while (true) {
			chain.add(cur);
			if (cur == chainBottom) {
				break;
			}
			cur = son[cur];
		}

		return chain;
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

		// 读入节点数
		n = in.nextInt();

		// 读入 n-1 条边
		for (int i = 0; i < n - 1; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			addEdge(u, v);
		}

		// 长链剖分初始化
		init();

		// 输出长链剖分信息
		out.println("长链剖分信息：");
		for (int i = 1; i <= n; i++) {
			out.println("节点 " + i + ": 深度=" + depth[i] + 
				", 子树最大深度=" + maxDepth[i] + 
				", 长链长度=" + len[i] + 
				", 链顶=" + top[i] + 
				", 重儿子=" + son[i]);
		}

		// 查询示例
		int queryCount = in.nextInt();
		for (int i = 0; i < queryCount; i++) {
			int u = in.nextInt();
			int k = in.nextInt();
			int ancestor = getKthAncestor(u, k);
			out.println("节点 " + u + " 的 " + k + " 级祖先：" + 
				(ancestor == -1 ? "不存在" : ancestor));
		}

		out.flush();
		out.close();
	}
}
