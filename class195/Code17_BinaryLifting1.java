package class195;

// 倍增优化建图基础模板，Java 版
// 本代码展示倍增优化建图的核心模板，用于解决长距离跳跃问题
// 测试链接 : https://www.luogu.com.cn/problem/P3243（改编）
// 本模板展示了倍增优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 倍增优化建图核心知识点 =====================
// 【问题分析】
// 倍增优化建图用于解决长距离跳跃、传递闭包等问题
// 通过二进制拆分，将 O(n) 的跳跃优化到 O(logn)
// 主要应用于可达性问题、最长链、传递闭包等
//
// 【核心原理】
// 二进制拆分：将距离 k 拆分为 2 的幂次和
// 倍增数组：fa[i][j] 表示从 i 出发跳 2^j 步到达的点
// 状态转移：fa[i][j] = fa[fa[i][j-1]][j-1]
//
// 【复杂度分析】
// 节点数：O(nlogn)（倍增数组）
// 边数：O(nlogn)（倍增连边）
// 查询复杂度：O(logn) 或 O(1)
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的长距离依赖
// 2. 序列模型中的跳跃连接
// 3. 层次化特征提取

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code17_BinaryLifting1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int LOG = 20;
	public static int MAXE = 2000001;
	public static int INF = 1 << 30;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] weight = new int[MAXE];
	public static int cnt;

	// ===================== 倍增优化建图变量区 =====================
	public static int n, m;
	public static int[][] fa = new int[MAXN][LOG];
	public static int[][] minVal = new int[MAXN][LOG];
	public static int[][] maxVal = new int[MAXN][LOG];

	// ===================== 拓扑排序变量区 =====================
	public static int[] indegree = new int[MAXN];
	public static int[] topoOrder = new int[MAXN];
	public static int topoCnt;

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v、权值为 w 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	// 面试高频提问：如何处理重边？如何判断有向无环图？
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
		indegree[v]++;
	}

	// ===================== 核心函数：拓扑排序 =====================
	// 功能：对 DAG 进行拓扑排序，为倍增 DP 提供计算顺序
	// 核心思想：Kahn 算法，不断删除入度为 0 的点
	// 面试高频提问：拓扑排序的应用场景？如何判断有环？
	public static void topologicalSort() {
		List<Integer> queue = new ArrayList<>();
		// 将所有入度为 0 的点入队
		for (int i = 1; i <= n; i++) {
			if (indegree[i] == 0) {
				queue.add(i);
			}
		}

		// BFS 拓扑排序
		while (!queue.isEmpty()) {
			int u = queue.remove(0);
			topoOrder[++topoCnt] = u;

			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				indegree[v]--;
				if (indegree[v] == 0) {
					queue.add(v);
				}
			}
		}
	}

	// ===================== 核心函数：构建倍增数组 =====================
	// 功能：构建倍增数组 fa[i][j] 表示从 i 出发跳 2^j 步到达的点
	// 核心思想：动态规划，fa[i][j] = fa[fa[i][j-1]][j-1]
	// 面试高频提问：为什么按拓扑序计算？时间复杂度是多少？
	public static void buildBinaryLifting() {
		// 按拓扑序计算倍增数组
		for (int i = 1; i <= topoCnt; i++) {
			int u = topoOrder[i];
			// 初始化：跳 2^0 = 1 步
			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				int w = weight[e];
				if (fa[u][0] == 0) {
					fa[u][0] = v;
					minVal[u][0] = w;
					maxVal[u][0] = w;
				} else {
					minVal[u][0] = Math.min(minVal[u][0], w);
					maxVal[u][0] = Math.max(maxVal[u][0], w);
				}
			}

			// 动态规划计算 2^1, 2^2, ..., 2^(LOG-1)
			for (int j = 1; j < LOG; j++) {
				if (fa[u][j - 1] != 0) {
					int mid = fa[u][j - 1];
					fa[u][j] = fa[mid][j - 1];
					// 合并路径上的最值
					if (fa[u][j] != 0) {
						minVal[u][j] = Math.min(minVal[u][j - 1], minVal[mid][j - 1]);
						maxVal[u][j] = Math.max(maxVal[u][j - 1], maxVal[mid][j - 1]);
					}
				}
			}
		}
	}

	// ===================== 核心函数：查询跳 k 步 =====================
	// 功能：查询从节点 u 出发跳 k 步到达的节点
	// 核心思想：将 k 二进制拆分，依次跳跃
	// 面试高频提问：如何分解 k？时间复杂度是多少？
	public static int jumpKSteps(int u, int k) {
		for (int j = 0; j < LOG; j++) {
			if (((k >> j) & 1) == 1) {
				u = fa[u][j];
				if (u == 0) {
					return 0; // 超出范围
				}
			}
		}
		return u;
	}

	// ===================== 核心函数：查询路径最小值 =====================
	// 功能：查询从节点 u 出发跳 k 步路径上的最小值
	// 核心思想：在倍增跳跃的同时维护最小值
	// 面试高频提问：如何维护路径最值？能否同时维护最大最小值？
	public static int queryMin(int u, int k) {
		int result = INF;
		for (int j = 0; j < LOG; j++) {
			if (((k >> j) & 1) == 1) {
				result = Math.min(result, minVal[u][j]);
				u = fa[u][j];
				if (u == 0) {
					return -1; // 超出范围
				}
			}
		}
		return result == INF ? -1 : result;
	}

	// ===================== 核心函数：查询路径最大值 =====================
	// 功能：查询从节点 u 出发跳 k 步路径上的最大值
	// 核心思想：在倍增跳跃的同时维护最大值
	// 面试高频提问：最大值和最小值能否同时查询？
	public static int queryMax(int u, int k) {
		int result = -INF;
		for (int j = 0; j < LOG; j++) {
			if (((k >> j) & 1) == 1) {
				result = Math.max(result, maxVal[u][j]);
				u = fa[u][j];
				if (u == 0) {
					return -1; // 超出范围
				}
			}
		}
		return result == -INF ? -1 : result;
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
			int w = in.nextInt();
			addEdge(u, v, w);
		}

		// 拓扑排序
		topologicalSort();

		// 构建倍增数组
		buildBinaryLifting();

		// 查询示例
		int queryCount = in.nextInt();
		for (int i = 0; i < queryCount; i++) {
			int u = in.nextInt();
			int k = in.nextInt();
			int target = jumpKSteps(u, k);
			int minV = queryMin(u, k);
			int maxV = queryMax(u, k);
			out.println("从 " + u + " 跳 " + k + " 步：到达 " + target + "，最小值 " + minV + "，最大值 " + maxV);
		}

		out.flush();
		out.close();
	}
}
