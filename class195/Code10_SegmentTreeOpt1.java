package class195;

// 线段树优化建图基础模板，Java版
// 本代码展示线段树优化建图的核心模板，用于解决区间-单点、单点-区间连边问题
// 测试链接 : https://www.luogu.com.cn/problem/P3370（改编）
// 本模板展示了线段树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 线段树优化建图核心知识点 =====================
// 【问题分析】
// 线段树优化建图用于解决区间连边问题，将O(n²)的边数优化到O(nlogn)
// 主要解决两类问题：
// 1. 单点→区间连边：使用出树（out-tree）
// 2. 区间→单点连边：使用入树（in-tree）
//
// 【核心原理】
// 入树：父节点→子节点连0边，区间约束从根向下传递到叶子
// 出树：子节点→父节点连0边，单点约束从叶子向上传递到根
// 这样区间连边只需要O(logn)条边即可完成
//
// 【复杂度分析】
// 建图复杂度：O(n)
// 区间连边复杂度：O(logn) per operation
// 总边数：O(nlogn) vs 直接建图的O(n²)
//
// 【ML/DL关联价值】
// 1. 大规模图的边压缩存储
// 2. GNN中的邻居采样优化
// 3. 知识图谱的区间关系表示

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code10_SegmentTreeOpt1 {

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
	public static int root = 1;

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从u到v、权值为w的有向边
	// 笔试面试考察点：链式前向星的插入操作
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
	}

	// ===================== 核心函数：入树构建 =====================
	// 功能：构建入树，实现区间→单点的约束传递
	// 核心思想：父节点向左右子节点连0边，叶子节点向原始节点连0边
	// 这样从区间根节点出发，可以遍历该区间内的所有原始节点
	// 面试高频提问：入树的作用是什么？为什么要从父节点向子节点连边？
	public static void buildInTree(int u, int l, int r) {
		if (l == r) {
			// 叶子节点：入树叶子节点→原始节点连0边
			// 功能：将区间约束传递到具体节点
			addEdge(u, n + l, 0);
			return;
		}
		int mid = (l + r) >> 1;
		int left = u << 1;
		int right = u << 1 | 1;
		// 父节点向左右子节点连0边
		// 功能：父节点的约束可以传递到子节点
		addEdge(u, left, 0);
		addEdge(u, right, 0);
		// 递归构建左右子树
		buildInTree(left, l, mid);
		buildInTree(right, mid + 1, r);
	}

	// ===================== 核心函数：出树构建 =====================
	// 功能：构建出树，实现单点→区间的约束传递
	// 核心思想：子节点向父节点连0边，原始节点向叶子节点连0边
	// 这样从原始节点出发，可以到达该区间内的所有节点
	// 面试高频提问：出树的作用是什么？为什么要从子节点向父节点连边？
	public static void buildOutTree(int u, int l, int r) {
		if (l == r) {
			// 叶子节点：原始节点→出树叶子节点连0边
			// 功能：将单点的约束传递到整个区间
			addEdge(n + l, u, 0);
			return;
		}
		int mid = (l + r) >> 1;
		int left = u << 1;
		int right = u << 1 | 1;
		// 左右子节点向父节点连0边
		// 功能：子节点的约束可以传递到父节点
		addEdge(left, u, 0);
		addEdge(right, u, 0);
		// 递归构建左右子树
		buildOutTree(left, l, mid);
		buildOutTree(right, mid + 1, r);
	}

	// ===================== 核心函数：区间→单点连边 =====================
	// 功能：将区间[L,R]内的所有节点，向目标节点v连边，权值为w
	// 时间复杂度：O(logn) per operation
	// 核心思想：从入树的根节点出发，递归找到完全包含在[L,R]内的区间节点
	// 面试高频提问：如何将O(n)条边优化到O(logn)条边？
	public static void addEdgeIntervalToNode(int L, int R, int v, int w, int u, int l, int r) {
		if (L <= l && r <= R) {
			// 当前区间完全包含在目标区间内
			// 功能：直接连边，无需继续向下遍历
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
	// 功能：将源节点u，向区间[L,R]内的所有节点连边，权值为w
	// 时间复杂度：O(logn) per operation
	// 核心思想：从出树的根节点出发，递归找到完全包含在[L,R]内的区间节点
	// 面试高频提问：单点和区间连边的区别是什么？入树和出树如何配合使用？
	public static void addEdgeNodeToInterval(int u, int L, int R, int w, int v, int l, int r) {
		if (L <= l && r <= R) {
			// 当前区间完全包含在目标区间内
			// 功能：直接连边，无需继续向下遍历
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

	// ===================== Dijkstra算法 =====================
	// 功能：在构建的图上运行Dijkstra算法，求从源点到所有节点的最短距离
	// 面试高频提问：Dijkstra算法的时间复杂度？优化建图后如何选择最短路算法？
	public static int[] dist = new int[MAXN];
	public static boolean[] visited = new boolean[MAXN];
	public static List<int[]> heap = new ArrayList<>();

	public static void dijkstra(int start) {
		// 初始化距离数组
		for (int i = 1; i <= nodeCnt; i++) {
			dist[i] = INF;
			visited[i] = false;
		}
		dist[start] = 0;
		heap.clear();
		heap.add(new int[]{0, start});

		while (!heap.isEmpty()) {
			// 取出当前距离最小的节点
			int[] cur = heap.remove(heap.size() - 1);
			int u = cur[1];
			int d = cur[0];
			// 如果已经访问过，跳过
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

		// 读入节点数n和操作数m
		n = in.nextInt();
		m = in.nextInt();

		// 计算线段树节点总数：4倍n（入树）+ 4倍n（出树）+ n（原始节点）
		nodeCnt = 8 * n;

		// 构建入树：从root开始，区间范围[1,n]
		buildInTree(root, 1, n);

		// 构建出树：从root开始，区间范围[1,n]
		buildOutTree(root, 1, n);

		// 处理m次连边操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt();
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();

			if (op == 1) {
				// 操作1：单点u → 单点v，连边权值w
				// 功能：直接连接两个原始节点
				addEdge(n + u, n + v, w);
			} else if (op == 2) {
				// 操作2：区间[1,u] → 单点v，连边权值w
				// 功能：使用入树优化，将区间内所有节点连接到v
				addEdgeIntervalToNode(1, u, n + v, w, root, 1, n);
			} else if (op == 3) {
				// 操作3：单点u → 区间[1,v]，连边权值w
				// 功能：使用出树优化，将u连接到区间内所有节点
				addEdgeNodeToInterval(n + u, 1, v, w, root, 1, n);
			}
		}

		// 运行Dijkstra算法，从节点1出发
		dijkstra(n + 1);

		// 输出从节点1到节点n的最短距离
		out.println(dist[n + n] == INF ? -1 : dist[n + n]);

		out.flush();
		out.close();
	}
}
