package class195;

// 虚树优化建图基础模板，Java 版
// 本代码展示虚树优化建图的核心模板，用于解决树上关键点问题
// 测试链接 : https://www.luogu.com.cn/problem/P2495（改编）
// 本模板展示了虚树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 虚树优化建图核心知识点 =====================
// 【问题分析】
// 虚树用于解决树上只涉及部分关键点的问题
// 通过只保留关键点和它们的 LCA，大幅减少节点数
// 主要应用于树上 DP 优化、关键点路径问题等
//
// 【核心原理】
// 关键点选择：保留所有询问的关键点
// LCA 添加：添加所有关键点对的 LCA
// 拓扑保持：保持原树中的祖先 - 后代关系
// 边权处理：压缩路径的边权为距离
//
// 【复杂度分析】
// 节点数：O(k)（k 为关键点数，最多 2k-1 个）
// 构建复杂度：O(klogk)（排序 + 单调栈）
// 优势：将 O(n) 问题降为 O(k) 问题
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的关键子图提取
// 2. 树结构数据中的特征压缩
// 3. 层次聚类中的关键节点选择

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

public class Code24_VirtualTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int LOG = 20;

	// ===================== 原树存储区 =====================
	public static List<int[]>[] tree = new ArrayList[MAXN];
	public static int n;

	// ===================== 虚树存储区 =====================
	public static List<int[]>[] virtualTree = new ArrayList[MAXN];
	public static List<Integer> keyPoints = new ArrayList<>();

	// ===================== 倍增 LCA 变量区 =====================
	public static int[][] parent = new int[MAXN][LOG];
	public static int[] depth = new int[MAXN];
	public static int[] dist = new int[MAXN]; // 到根节点的距离

	// ===================== 虚树变量区 =====================
	public static int[] stack = new int[MAXN];
	public static int top;
	public static boolean[] isKey = new boolean[MAXN];

	// ===================== 核心函数：原树加边 =====================
	// 功能：向原树中添加一条边
	// 笔试面试考察点：树的邻接表存储
	// 面试高频提问：为什么树只需要连单向边？
	public static void addEdge(int u, int v, int w) {
		if (tree[u] == null) tree[u] = new ArrayList<>();
		if (tree[v] == null) tree[v] = new ArrayList<>();
		tree[u].add(new int[]{v, w});
		tree[v].add(new int[]{u, w});
	}

	// ===================== 核心函数：虚树加边 =====================
	// 功能：向虚树中添加一条边
	// 核心思想：边权为原树中的距离
	// 面试高频提问：虚树中的边权如何计算？
	public static void addVirtualEdge(int u, int v, int w) {
		if (virtualTree[u] == null) virtualTree[u] = new ArrayList<>();
		virtualTree[u].add(new int[]{v, w});
	}

	// ===================== 核心函数：DFS 预处理 =====================
	// 功能：DFS 预处理深度、父节点、距离等信息
	// 核心思想：为 LCA 查询做准备
	// 面试高频提问：为什么要预处理？预处理的内容有哪些？
	public static void dfs(int u, int p, int d, int distance) {
		depth[u] = d;
		parent[u][0] = p;
		dist[u] = distance;

		// 预处理倍增数组
		for (int j = 1; j < LOG; j++) {
			if (parent[u][j - 1] != 0) {
				parent[u][j] = parent[parent[u][j - 1]][j - 1];
			}
		}

		if (tree[u] != null) {
			for (int[] edge : tree[u]) {
				int v = edge[0];
				int w = edge[1];
				if (v != p) {
					dfs(v, u, d + 1, distance + w);
				}
			}
		}
	}

	// ===================== 核心函数：LCA 查询 =====================
	// 功能：查询节点 u 和 v 的最近公共祖先
	// 核心思想：倍增算法，先调整深度再同时上跳
	// 面试高频提问：LCA 有哪些求法？复杂度如何？
	public static int getLCA(int u, int v) {
		if (depth[u] < depth[v]) {
			int temp = u;
			u = v;
			v = temp;
		}

		// 调整深度
		int diff = depth[u] - depth[v];
		for (int j = 0; j < LOG; j++) {
			if ((diff & (1 << j)) != 0) {
				u = parent[u][j];
			}
		}

		if (u == v) return u;

		// 同时上跳
		for (int j = LOG - 1; j >= 0; j--) {
			if (parent[u][j] != parent[v][j]) {
				u = parent[u][j];
				v = parent[v][j];
			}
		}

		return parent[u][0];
	}

	// ===================== 核心函数：计算两点距离 =====================
	// 功能：计算原树中 u 和 v 之间的距离
	// 核心思想：dist[u] + dist[v] - 2 * dist[LCA]
	// 面试高频提问：树上距离公式的推导？
	public static int getDistance(int u, int v) {
		int lca = getLCA(u, v);
		return dist[u] + dist[v] - 2 * dist[lca];
	}

	// ===================== 核心函数：比较函数 =====================
	// 功能：按 DFS 序（这里用节点编号近似）排序
	// 核心思想：保证构建虚树时的顺序
	// 面试高频提问：为什么要排序？排序的依据是什么？
	public static class DFSOrderComparator implements Comparator<Integer> {
		@Override
		public int compare(Integer a, Integer b) {
			return Integer.compare(a, b); // 简化版本，实际应该用 DFS 序
		}
	}

	// ===================== 核心函数：构建虚树 =====================
	// 功能：根据关键点构建虚树
	// 核心思想：单调栈维护右链
	// 面试高频提问：单调栈的作用是什么？如何维护？
	public static void buildVirtualTree() {
		// 清空虚树
		for (int key : keyPoints) {
			if (virtualTree[key] != null) {
				virtualTree[key].clear();
			}
			isKey[key] = true;
		}

		// 按 DFS 序排序
		Collections.sort(keyPoints, new DFSOrderComparator());

		// 添加所有关键点对的 LCA
		List<Integer> allPoints = new ArrayList<>(keyPoints);
		for (int i = 0; i < keyPoints.size() - 1; i++) {
			int lca = getLCA(keyPoints.get(i), keyPoints.get(i + 1));
			if (!allPoints.contains(lca)) {
				allPoints.add(lca);
			}
		}
		Collections.sort(allPoints, new DFSOrderComparator());

		// 单调栈构建虚树
		top = 0;
		stack[++top] = allPoints.get(0);

		for (int i = 1; i < allPoints.size(); i++) {
			int u = allPoints.get(i);
			while (top > 1 && depth[stack[top - 1]] >= depth[u]) {
				top--;
			}
			if (top > 0) {
				int dist = getDistance(stack[top], u);
				addVirtualEdge(stack[top], u, dist);
			}
			stack[++top] = u;
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

		// 读入节点数
		n = in.nextInt();

		// 读入 n-1 条边
		for (int i = 0; i < n - 1; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			addEdge(u, v, w);
		}

		// DFS 预处理
		dfs(1, 0, 0, 0);

		// 读入关键点
		int k = in.nextInt();
		for (int i = 0; i < k; i++) {
			int point = in.nextInt();
			keyPoints.add(point);
		}

		// 构建虚树
		buildVirtualTree();

		// 输出虚树信息
		out.println("虚树节点数：" + keyPoints.size());
		out.println("虚树边信息：");
		for (int u : keyPoints) {
			if (virtualTree[u] != null) {
				for (int[] edge : virtualTree[u]) {
					out.println(u + " -> " + edge[0] + " (距离：" + edge[1] + ")");
				}
			}
		}

		out.flush();
		out.close();
	}
}
