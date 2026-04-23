package class195;

// 逛森林，java版
// 一共n个点，给定出发点s，初始时没有边，一共m条操作，操作类型有两种
// 操作 1 a b c d w : 如果点a和点b不连通，或者点c和点d不连通，忽略该操作
//       点a到点b路径上的任何一点，可以花费w的代价，传送到点c到点d路径上的任何一点
//       该操作是单向传送，不代表加边，判断连通性只考虑2类型的操作加入的边
// 操作 2 u v w : 点u和点v如果已经连通，忽略该操作，否则增加一条边权为w的无向边
// 完成m条操作后，打印从s点出发，到每个节点的最小花费，到达不了的节点打印-1
// 1 <= n <= 5 * 10^4    1 <= m <= 10^6    1 <= w <= 100
// 测试链接 : https://www.luogu.com.cn/problem/P5344
// 提交以下的code，提交时请把类名改成"Main"
// 本题卡常java的实现无法通过
// 想通过用C++实现，本节课Code08_Forest2文件就是C++的实现
// 两个版本的逻辑完全一样，C++版本可以通过所有测试

// ===================== 森林+倍增优化建图+Dijkstra核心知识点 =====================
// 【问题分析】
// 本题需要处理两种操作：加边操作和路径间传送操作
// 传送操作需要在路径上的任意点之间建立带权边
// 使用倍增+区间表优化建图，将O(n^2)的边数优化到O(nlogn)
//
// 【并查集】
// 用于维护连通性，判断操作是否应该执行
//
// 【倍增优化建图】
// 类似美丽的树问题，使用倍增表组织区间
// stout[u][p]: 出表节点，用于区间→单点连边
// stin[u][p]: 入表节点，用于单点→区间连边
//
// 【Dijkstra算法】
// 在构建的图上运行Dijkstra，求从s到所有点的最短路
//
// 【ML/DL关联价值】
// 1. 图结构上的最短路径学习
// 2. 层次化图表示

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class Code08_Forest1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 50001;
	public static int MAXM = 1000001;
	public static int MAXT = 4000001;
	public static int MAXE = 30000001;
	public static int MAXP = 17;
	public static int INF = 1 << 30;

	// ===================== 输入变量区 =====================
	public static int n, m, s;

	// ===================== 操作1记录区 =====================
	// 只记录应该发生的操作1
	public static int[] u1 = new int[MAXM];
	public static int[] v1 = new int[MAXM];
	public static int[] u2 = new int[MAXM];
	public static int[] v2 = new int[MAXM];
	public static int[] weight = new int[MAXM];
	public static int cntq;

	// ===================== 操作2建立的树存储区 =====================
	public static int[] head1 = new int[MAXN];
	public static int[] next1 = new int[MAXN << 1];
	public static int[] to1 = new int[MAXN << 1];
	public static int cnt1;

	// ===================== 操作1+操作2建立的图存储区 =====================
	public static int[] head2 = new int[MAXT];
	public static int[] next2 = new int[MAXE];
	public static int[] to2 = new int[MAXE];
	public static int[] weight2 = new int[MAXE];
	public static int cnt2;

	// ===================== 并查集变量区 =====================
	public static int[] father = new int[MAXN];

	// ===================== 树上倍增变量区 =====================
	public static int[] dep = new int[MAXN];
	public static int[][] stjump = new int[MAXN][MAXP];
	public static int[][] stout = new int[MAXN][MAXP];
	public static int[][] stin = new int[MAXN][MAXP];
	public static int cntt;

	// ===================== Dijkstra算法变量区 =====================
	public static int[] dist = new int[MAXT];
	public static boolean[] vis = new boolean[MAXT];
	public static PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);

	// ===================== 核心函数：操作2的树加边 =====================
	public static void addEdge1(int u, int v) {
		next1[++cnt1] = head1[u];
		to1[cnt1] = v;
		head1[u] = cnt1;
	}

	// ===================== 核心函数：操作1+操作2的图加边 =====================
	public static void addEdge2(int u, int v, int w) {
		next2[++cnt2] = head2[u];
		to2[cnt2] = v;
		weight2[cnt2] = w;
		head2[u] = cnt2;
	}

	// ===================== 核心函数：并查集查找 =====================
	public static int find(int i) {
		if (i != father[i]) {
			father[i] = find(father[i]);
		}
		return father[i];
	}

	// ===================== 核心函数：构建倍增表和区间表 =====================
	public static void build(int u, int fa) {
		dep[u] = dep[fa] + 1;
		// 初始化2^0级祖先
		stjump[u][0] = fa;
		// 构建出表节点
		stout[u][0] = ++cntt;
		addEdge2(u, cntt, 0);
		addEdge2(fa, cntt, 0);
		// 构建入表节点
		stin[u][0] = ++cntt;
		addEdge2(cntt, u, 0);
		addEdge2(cntt, fa, 0);
		// 构建更高层的倍增表
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
			// 构建出表节点
			stout[u][p] = ++cntt;
			addEdge2(stout[u][p - 1], cntt, 0);
			addEdge2(stout[stjump[u][p - 1]][p - 1], cntt, 0);
			// 构建入表节点
			stin[u][p] = ++cntt;
			addEdge2(cntt, stin[u][p - 1], 0);
			addEdge2(cntt, stin[stjump[u][p - 1]][p - 1], 0);
		}
		// 递归处理子节点
		for (int e = head1[u]; e > 0; e = next1[e]) {
			int v = to1[e];
			if (v != fa) {
				build(v, u);
			}
		}
	}

	// ===================== 核心函数：路径出表连边 =====================
	// 建立从路径x到y上所有节点到vnode的边
	public static void pathOut(int x, int y, int vnode) {
		if (dep[x] < dep[y]) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		// y直接连边
		addEdge2(y, vnode, 0);
		// 利用倍增表将x向上跳到与y同深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[x][p]] >= dep[y]) {
				addEdge2(stout[x][p], vnode, 0);
				x = stjump[x][p];
			}
		}
		if (x == y) {
			return;
		}
		// x和y同时向上跳
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[x][p] != stjump[y][p]) {
				addEdge2(stout[x][p], vnode, 0);
				addEdge2(stout[y][p], vnode, 0);
				x = stjump[x][p];
				y = stjump[y][p];
			}
		}
		// 最后一步
		addEdge2(stout[x][0], vnode, 0);
	}

	// ===================== 核心函数：路径入表连边 =====================
	// 建立从vnode到路径x到y上所有节点的边
	public static void pathIn(int x, int y, int vnode) {
		if (dep[x] < dep[y]) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		// y直接连边
		addEdge2(vnode, y, 0);
		// 利用倍增表将x向上跳到与y同深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[x][p]] >= dep[y]) {
				addEdge2(vnode, stin[x][p], 0);
				x = stjump[x][p];
			}
		}
		if (x == y) {
			return;
		}
		// x和y同时向上跳
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[x][p] != stjump[y][p]) {
				addEdge2(vnode, stin[x][p], 0);
				addEdge2(vnode, stin[y][p], 0);
				x = stjump[x][p];
				y = stjump[y][p];
			}
		}
		// 最后一步
		addEdge2(vnode, stin[x][0], 0);
	}

	// ===================== 核心函数：路径到路径的传送边 =====================
	// 建立从路径a-b到路径c-d的传送边，代价为w
	public static void pathToPath(int a, int b, int c, int d, int w) {
		// 创建两个虚拟节点
		int x = ++cntt;
		int y = ++cntt;
		// 路径a-b连到虚拟节点x
		pathOut(a, b, x);
		// 虚拟节点y连到路径c-d
		pathIn(c, d, y);
		// x到y的传送边
		addEdge2(x, y, w);
	}

	// ===================== 核心函数：Dijkstra算法 =====================
	public static void dijkstra() {
		// 初始化距离
		for (int i = 1; i <= cntt; i++) {
			dist[i] = INF;
		}
		dist[s] = 0;
		heap.add(new int[] { s, 0 });
		// Dijkstra主循环
		while (!heap.isEmpty()) {
			int[] cur = heap.poll();
			int u = cur[0];
			int d = cur[1];
			if (!vis[u]) {
				vis[u] = true;
				// 遍历所有邻接边
				for (int e = head2[u]; e > 0; e = next2[e]) {
					int v = to2[e];
					int w = weight2[e];
					if (!vis[v] && dist[v] > d + w) {
						dist[v] = d + w;
						heap.add(new int[] { v, dist[v] });
					}
				}
			}
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读入n, m, s
		n = in.nextInt();
		m = in.nextInt();
		s = in.nextInt();
		cntt = n;
		// 初始化并查集
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
		// 处理m条操作
		for (int i = 1, op, a, b, c, d, w, u, v; i <= m; i++) {
			op = in.nextInt();
			if (op == 1) {
				// 操作1：路径间传送
				a = in.nextInt();
				b = in.nextInt();
				c = in.nextInt();
				d = in.nextInt();
				w = in.nextInt();
				// 检查连通性
				if (find(a) == find(b) && find(c) == find(d)) {
					u1[++cntq] = a;
					v1[cntq] = b;
					u2[cntq] = c;
					v2[cntq] = d;
					weight[cntq] = w;
				}
			} else {
				// 操作2：加边
				u = in.nextInt();
				v = in.nextInt();
				w = in.nextInt();
				int ufa = find(u);
				int vfa = find(v);
				if (ufa != vfa) {
					// 加边到树
					addEdge1(u, v);
					addEdge1(v, u);
					// 加边到图
					addEdge2(u, v, w);
					addEdge2(v, u, w);
					// 合并并查集
					father[ufa] = vfa;
				}
			}
		}
		// 对每个连通块构建倍增表
		for (int i = 1; i <= n; i++) {
			if (dep[i] == 0) {
				build(i, i);
			}
		}
		// 处理所有操作1的传送边
		for (int i = 1; i <= cntq; i++) {
			pathToPath(u1[i], v1[i], u2[i], v2[i], weight[i]);
		}
		// 运行Dijkstra
		dijkstra();
		// 输出答案
		for (int i = 1; i <= n; i++) {
			out.print(dist[i] == INF ? -1 : dist[i]);
			out.print(" ");
		}
		out.flush();
		out.close();
	}

	// ===================== 快速读入工具类 =====================
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
				if (len <= 0)
					return -1;
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

}
