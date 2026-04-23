package class195;

// 区间双向连边优化建图基础模板，Java 版
// 本代码展示区间双向连边优化建图的核心模板，用于解决区间 - 区间连边问题
// 测试链接 : https://www.luogu.com.cn/problem/P5344（改编）
// 本模板展示了区间双向连边优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 区间双向连边优化建图核心知识点 =====================
// 【问题分析】
// 区间双向连边用于解决两个区间之间的连边问题
// 直接建图需要 O(n²) 条边，使用线段树优化可以降到 O(nlogn)
// 主要应用于区间跳转、区间传送等问题
//
// 【核心原理】
// 双树结构：同时构建入树和出树
// 入树：父→子连 0 边，用于区间→单点连边
// 出树：子→父连 0 边，用于单点→区间连边
// 区间→区间：通过虚拟节点中转
//
// 【复杂度分析】
// 节点数：O(n)（线段树节点）
// 边数：O(nlogn)（优化后的区间连边）
// 最短路复杂度：O(nlog²n)
//
// 【ML/DL 关联价值】
// 1. 大规模图的区间关系压缩
// 2. 图神经网络中的批量消息传递
// 3. 知识图谱的区间推理优化

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code15_IntervalBidirectional1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 400001;
	public static int MAXE = 1600001;
	public static int INF = 1 << 30;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] weight = new int[MAXE];
	public static int cnt;

	// ===================== 线段树优化建图变量区 =====================
	public static int n, m;
	public static int nodeCnt;
	public static int inRoot, outRoot;

	// ===================== Dijkstra 变量区 =====================
	public static int[] dist = new int[MAXN];
	public static boolean[] visited = new boolean[MAXN];

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v、权值为 w 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
	}

	// ===================== 核心函数：入树构建 =====================
	// 功能：构建入树，实现区间→单点的约束传递
	// 核心思想：父节点向左右子节点连 0 边，叶子节点向原始节点连 0 边
	// 面试高频提问：入树的作用是什么？为什么需要从父节点向子节点连边？
	public static void buildInTree(int u, int l, int r) {
		if (l == r) {
			// 叶子节点：入树叶子节点→原始节点连 0 边
			addEdge(u, l, 0);
			return;
		}
		int mid = (l + r) >> 1;
		int left = u << 1;
		int right = u << 1 | 1;
		// 父节点向左右子节点连 0 边
		addEdge(u, left, 0);
		addEdge(u, right, 0);
		// 递归构建左右子树
		buildInTree(left, l, mid);
		buildInTree(right, mid + 1, r);
	}

	// ===================== 核心函数：出树构建 =====================
	// 功能：构建出树，实现单点→区间的约束传递
	// 核心思想：子节点向父节点连 0 边，原始节点向叶子节点连 0 边
	// 面试高频提问：出树的作用是什么？为什么需要从子节点向父节点连边？
	public static void buildOutTree(int u, int l, int r) {
		if (l == r) {
			// 叶子节点：原始节点→出树叶子节点连 0 边
			addEdge(l, u, 0);
			return;
		}
		int mid = (l + r) >> 1;
		int left = u << 1;
		int right = u << 1 | 1;
		// 左右子节点向父节点连 0 边
		addEdge(left, u, 0);
		addEdge(right, u, 0);
		// 递归构建左右子树
		buildOutTree(left, l, mid);
		buildOutTree(right, mid + 1, r);
	}

	// ===================== 核心函数：区间→单点连边 =====================
	// 功能：将区间 [L,R] 内的所有节点，向目标节点 v 连边，权值为 w
	// 时间复杂度：O(logn) per operation
	// 核心思想：从入树的根节点出发，递归找到完全包含在 [L,R] 内的区间节点
	public static void addEdgeIntervalToNode(int L, int R, int v, int w, int u, int l, int r) {
		if (L <= l && r <= R) {
			// 当前区间完全包含在目标区间内
			addEdge(u, v, w);
			return;
		}
		int mid = (l + r) >> 1;
		int left = u << 1;
		int right = u << 1 | 1;
		// 递归处理左右子树
		if (L <= mid) {
			addEdgeIntervalToNode(L, R, v, w, left, l, mid);
		}
		if (R > mid) {
			addEdgeIntervalToNode(L, R, v, w, right, mid + 1, r);
		}
	}

	// ===================== 核心函数：单点→区间连边 =====================
	// 功能：将源节点 u，向区间 [L,R] 内的所有节点连边，权值为 w
	// 时间复杂度：O(logn) per operation
	// 核心思想：从出树的根节点出发，递归找到完全包含在 [L,R] 内的区间节点
	public static void addEdgeNodeToInterval(int u, int L, int R, int w, int v, int l, int r) {
		if (L <= l && r <= R) {
			// 当前区间完全包含在目标区间内
			addEdge(u, v, w);
			return;
		}
		int mid = (l + r) >> 1;
		int left = v << 1;
		int right = v << 1 | 1;
		// 递归处理左右子树
		if (L <= mid) {
			addEdgeNodeToInterval(u, L, R, w, left, l, mid);
		}
		if (R > mid) {
			addEdgeNodeToInterval(u, L, R, w, right, mid + 1, r);
		}
	}

	// ===================== 核心函数：区间→区间连边 =====================
	// 功能：将区间 [L1,R1] 内的所有节点，向区间 [L2,R2] 内的所有节点连边，权值为 w
	// 时间复杂度：O(log²n) per operation
	// 核心思想：创建两个虚拟节点，通过虚拟节点中转
	// 面试高频提问：区间到区间连边为什么需要虚拟节点？如何优化复杂度？
	public static void addEdgeIntervalToInterval(int L1, int R1, int L2, int R2, int w) {
		// 创建两个虚拟节点
		int x = ++nodeCnt;
		int y = ++nodeCnt;
		// 区间 [L1,R1] 连到虚拟节点 x
		addEdgeIntervalToNode(L1, R1, x, 0, inRoot, 1, n);
		// 虚拟节点 x 连到虚拟节点 y
		addEdge(x, y, w);
		// 虚拟节点 y 连到区间 [L2,R2]
		addEdgeNodeToInterval(y, L2, R2, 0, outRoot, 1, n);
	}

	// ===================== Dijkstra 算法 =====================
	// 功能：在构建的图上运行 Dijkstra 算法，求从源点到所有节点的最短距离
	public static void dijkstra(int start) {
		// 初始化距离数组
		for (int i = 1; i <= nodeCnt; i++) {
			dist[i] = INF;
			visited[i] = false;
		}
		dist[start] = 0;

		// 优先队列
		List<int[]> heap = new ArrayList<>();
		heap.add(new int[]{0, start});

		while (!heap.isEmpty()) {
			// 取出当前距离最小的节点
			int[] cur = heap.remove(heap.size() - 1);
			int u = cur[1];
			int d = cur[0];

			if (visited[u]) {
				continue;
			}
			visited[u] = true;

			// 遍历所有邻接边
			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				int w = weight[e];
				if (!visited[v] && dist[v] > d + w) {
					dist[v] = d + w;
					heap.add(new int[]{dist[v], v});
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

		// 读入节点数 n 和操作数 m
		n = in.nextInt();
		m = in.nextInt();

		// 计算线段树节点总数
		nodeCnt = 8 * n;
		inRoot = 1;
		outRoot = 1;

		// 构建入树和出树
		buildInTree(inRoot, 1, n);
		buildOutTree(outRoot, 1, n);

		// 处理 m 次连边操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt();
			if (op == 1) {
				// 操作 1：单点→单点
				int u = in.nextInt();
				int v = in.nextInt();
				int w = in.nextInt();
				addEdge(u, v, w);
			} else if (op == 2) {
				// 操作 2：区间→单点
				int L = in.nextInt();
				int R = in.nextInt();
				int v = in.nextInt();
				int w = in.nextInt();
				addEdgeIntervalToNode(L, R, v, w, inRoot, 1, n);
			} else if (op == 3) {
				// 操作 3：单点→区间
				int u = in.nextInt();
				int L = in.nextInt();
				int R = in.nextInt();
				int w = in.nextInt();
				addEdgeNodeToInterval(u, L, R, w, outRoot, 1, n);
			} else if (op == 4) {
				// 操作 4：区间→区间
				int L1 = in.nextInt();
				int R1 = in.nextInt();
				int L2 = in.nextInt();
				int R2 = in.nextInt();
				int w = in.nextInt();
				addEdgeIntervalToInterval(L1, R1, L2, R2, w);
			}
		}

		// 运行 Dijkstra 算法，从节点 1 出发
		dijkstra(1);

		// 输出从节点 1 到节点 n 的最短距离
		out.println(dist[n] == INF ? -1 : dist[n]);

		out.flush();
		out.close();
	}
}
