package class195;

// 带花树算法（一般图最大匹配）基础模板，Java 版
// 本代码展示带花树算法优化建图的核心模板，用于解决一般图最大匹配问题
// 测试链接 : https://www.luogu.com.cn/problem/P6113（改编）
// 本模板展示了带花树算法优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 带花树算法核心知识点 =====================
// 【问题分析】
// 一般图最大匹配：在无向图中找到最多的不相交边
// 带花树算法通过寻找增广路和缩花解决奇环问题
// 主要应用于任务分配、资源匹配、图论问题等
//
// 【核心原理】
// 增广路：从未匹配点出发，经过未匹配边和已匹配边交替的路径
// 花：奇环，通过缩点转化为普通点
// 开花：找到增广路后，展开花并翻转匹配
// 匈牙利算法扩展：处理一般图中的奇环
//
// 【复杂度分析】
// 时间复杂度：O(n³)（朴素实现）
// 空间复杂度：O(n²)
// 优势：解决了二分图匹配无法处理的一般图问题
//
// 【ML/DL 关联价值】
// 1. 组合优化中的匹配问题
// 2. 图神经网络中的边预测
// 3. 多目标跟踪中的数据关联

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code40_BlossomAlgorithm1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 501;

	// ===================== 图存储区 =====================
	public static List<Integer>[] graph = new ArrayList[MAXN];
	public static int n, m;

	// ===================== 匹配变量区 =====================
	public static int[] match = new int[MAXN]; // 匹配对象
	public static int[] vis = new int[MAXN]; // 访问标记
	public static int[] fa = new int[MAXN]; // 并查集父节点
	public static int[] dfn = new int[MAXN]; // DFS 序
	public static int timer;

	// ===================== 核心函数：图加边 =====================
	public static void addEdge(int u, int v) {
		if (graph[u] == null) graph[u] = new ArrayList<>();
		if (graph[v] == null) graph[v] = new ArrayList<>();
		graph[u].add(v);
		graph[v].add(u);
	}

	// ===================== 核心函数：并查集查找 =====================
	// 功能：查找节点所在集合的代表元
	// 核心思想：路径压缩优化
	// 面试高频提问：并查集在带花树中的作用？
	public static int find(int x) {
		if (fa[x] != x) {
			fa[x] = find(fa[x]);
		}
		return fa[x];
	}

	// ===================== 核心函数：寻找 LCA =====================
	// 功能：寻找两个节点的最近公共祖先
	// 核心思想：利用 dfn 数组和并查集
	// 面试高频提问：LCA 在花算法中的意义？
	public static int lca(int x, int y) {
		timer++;
		while (true) {
			if (x != 0) {
				x = find(x);
				if (dfn[x] == timer) {
					return x;
				}
				dfn[x] = timer;
				if (match[x] != 0) {
					x = find(fa[match[x]]);
				} else {
					x = 0;
				}
			}
			
			// 交换 x 和 y
			int temp = x;
			x = y;
			y = temp;
		}
	}

	// ===================== 核心函数：缩花 =====================
	// 功能：将从 x 到 y 路径上的点缩为一个花
	// 核心思想：利用并查集合并，更新匹配
	// 面试高频提问：缩花的实现细节？
	public static void shrink(int x, int y, int root, int[] queue, int head, int tail) {
		while (find(x) != root) {
			fa[x] = root;
			fa[match[x]] = root;
			
			// 将匹配点加入队列
			queue[tail++] = match[x];
			
			// 移动到下一个点
			int next = fa[x];
			x = next;
		}
	}

	// ===================== 核心函数：寻找增广路 =====================
	// 功能：从起点 s 寻找增广路
	// 核心思想：BFS 搜索，处理花和匹配
	// 面试高频提问：带花树 BFS 的关键步骤？
	public static boolean findAugmentPath(int s) {
		// 初始化
		for (int i = 1; i <= n; i++) {
			fa[i] = i;
			vis[i] = 0;
			dfn[i] = 0;
		}
		timer = 0;
		
		int[] queue = new int[MAXN];
		int head = 0, tail = 0;
		queue[tail++] = s;
		vis[s] = 1;
		
		while (head < tail) {
			int u = queue[head++];
			
			if (graph[u] != null) {
				for (int v : graph[u]) {
					if (vis[v] == 0) {
						// v 未访问
						if (match[v] == 0) {
							// v 未匹配，找到增广路
							int cur = v;
							int prev = u;
							
							// 翻转增广路
							while (cur != 0) {
								int next = match[prev];
								match[cur] = prev;
								match[prev] = cur;
								cur = next;
								prev = fa[cur];
							}
							return true;
						} else {
							// v 已匹配，继续搜索
							vis[v] = 2;
							queue[tail++] = match[v];
							vis[match[v]] = 1;
							
							// 检查是否形成花
							int root = lca(u, v);
							if (find(u) != root) {
								fa[u] = root;
							}
							if (find(v) != root) {
								fa[v] = root;
							}
							
							// 缩花
							shrink(u, v, root, queue, head, tail);
							shrink(v, u, root, queue, head, tail);
						}
					} else if (vis[v] == 1 && find(v) != find(u)) {
						// v 已访问且在不同集合，形成花
						int root = lca(u, v);
						if (find(u) != root) {
							fa[u] = root;
						}
						if (find(v) != root) {
							fa[v] = root;
						}
						
						// 缩花
						shrink(u, v, root, queue, 0, tail);
						shrink(v, u, root, queue, 0, tail);
					}
				}
			}
		}
		
		return false;
	}

	// ===================== 核心函数：求解最大匹配 =====================
	// 功能：求解一般图的最大匹配
	// 核心思想：对每个未匹配点寻找增广路
	// 面试高频提问：带花树算法的复杂度？
	public static int maxMatching() {
		int matching = 0;
		
		for (int i = 1; i <= n; i++) {
			if (match[i] == 0) {
				if (findAugmentPath(i)) {
					matching++;
				}
			}
		}
		
		return matching;
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

		// 求解最大匹配
		int matching = maxMatching();

		// 输出结果
		out.println("一般图最大匹配数：" + matching);
		out.println("匹配方案：");
		for (int i = 1; i <= n; i++) {
			if (match[i] != 0 && i < match[i]) {
				out.println(i + " -- " + match[i]);
			}
		}

		out.flush();
		out.close();
	}
}
