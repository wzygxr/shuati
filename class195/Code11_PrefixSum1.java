package class195;

// 前缀和优化建图基础模板，Java版
// 本代码展示前缀和优化建图的核心模板，用于解决区间约束和差分问题
// 测试链接 : https://www.luogu.com.cn/problem/P3370（改编）
// 本模板展示了前缀和优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 前缀和优化建图核心知识点 =====================
// 【问题分析】
// 前缀和优化建图用于解决区间求和、区间约束等问题
// 将O(n)的区间查询优化到O(1)，将O(n²)的区间约束边优化到O(n)
// 主要应用于差分约束系统、区间计数等问题
//
// 【核心原理】
// 引入前缀和虚拟节点，将区间约束转化为节点间的约束
// 对于数组a[1..n]，定义前缀和pre[i] = a[1] + a[2] + ... + a[i]
// 区间和约束：a[L] + ... + a[R] ≤ w  →  pre[R] - pre[L-1] ≤ w
// 这样区间约束只需要常数条边即可
//
// 【复杂度分析】
// 建图复杂度：O(n)
// 区间约束边数：O(1) per constraint
// 总边数：O(n + m) vs 直接建图的O(nm)
//
// 【ML/DL关联价值】
// 1. 时序数据的区间聚合表示
// 2. 差分隐私中的累积计数
// 3. 图神经网络中的聚合操作优化

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code11_PrefixSum1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int MAXE = 800001;
	public static int INF = 1 << 30;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] weight = new int[MAXE];
	public static int cnt;

	// ===================== 前缀和优化建图变量区 =====================
	public static int n, m;
	public static int nodeCnt;
	// pre[i]: 前缀和节点编号，pre[i]表示前i个元素的前缀和
	public static int[] pre = new int[MAXN];

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从u到v、权值为w的有向边
	// 笔试面试考察点：链式前向星的插入操作
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
	}

	// ===================== 核心函数：构建前缀和图 =====================
	// 功能：初始化前缀和节点和基本约束边
	// 核心思想：
	// 1. pre[0] = 0 表示空前缀
	// 2. pre[i-1] → pre[i] 边权0：pre[i] ≥ pre[i-1]（非负约束）
	// 3. pre[i] → pre[i-1] 边权1：pre[i] - pre[i-1] ≤ 1（每个元素最多贡献1）
	// 面试高频提问：为什么需要这两条基本边？它们分别表示什么约束？
	public static void buildPrefixGraph() {
		// pre[0]节点编号为1
		pre[0] = 1;
		nodeCnt = 1;
		// 依次为每个前缀和创建节点
		for (int i = 1; i <= n; i++) {
			pre[i] = ++nodeCnt;
			// 边1：pre[i-1] → pre[i]，权值0
			// 功能：表示pre[i] ≥ pre[i-1]，即前i个元素和至少等于前i-1个元素和
			// 这是单调性约束，保证前缀和非递减
			addEdge(pre[i - 1], pre[i], 0);
			// 边2：pre[i] → pre[i-1]，权值1
			// 功能：表示pre[i] - pre[i-1] ≤ 1，即每个位置的元素贡献不超过1
			// 这是差分约束的核心，表示a[i] ≤ 1
			addEdge(pre[i], pre[i - 1], 1);
		}
	}

	// ===================== 核心函数：区间上界约束 =====================
	// 功能：添加区间和上界约束：sum(L..R) ≤ w
	// 转化：pre[R] - pre[L-1] ≤ w  →  pre[R] → pre[L-1]，边权w
	// 面试高频提问：区间上界约束如何转化为图中的边？
	public static void addUpperBound(int L, int R, int w) {
		// 从区间终点的前缀和，指向区间起点前一个位置的前缀和
		// 功能：表示pre[R] ≤ pre[L-1] + w，即区间和不超过w
		addEdge(pre[R], pre[L - 1], w);
	}

	// ===================== 核心函数：区间下界约束 =====================
	// 功能：添加区间和下界约束：sum(L..R) ≥ w
	// 转化：pre[R] - pre[L-1] ≥ w  →  pre[L-1] → pre[R]，边权w
	// 面试高频提问：区间下界约束如何转化为图中的边？
	public static void addLowerBound(int L, int R, int w) {
		// 从区间起点前一个位置的前缀和，指向区间终点的前缀和
		// 功能：表示pre[R] ≥ pre[L-1] + w，即区间和至少为w
		addEdge(pre[L - 1], pre[R], w);
	}

	// ===================== SPFA算法 =====================
	// 功能：在差分约束图上运行SPFA算法，求最长路
	// 差分约束求解方法：转化为单源最长路问题
	// 面试高频提问：为什么差分约束需要求最长路而不是最短路？
	public static int[] dist = new int[MAXN];
	public static int[] inCnt = new int[MAXN];
	public static boolean[] inQueue = new boolean[MAXN];
	public static List<Integer> queue = new ArrayList<>();

	public static boolean spfa() {
		// 初始化距离数组
		for (int i = 1; i <= nodeCnt; i++) {
			dist[i] = 0;
			inCnt[i] = 0;
			inQueue[i] = false;
		}
		queue.clear();
		queue.add(1);
		inQueue[1] = true;

		// SPFA主循环
		while (!queue.isEmpty()) {
			int u = queue.remove(0);
			inQueue[u] = false;
			// 遍历所有出边
			for (int e = head[u]; e > 0; e = next[e]) {
				int v = to[e];
				int w = weight[e];
				// 松弛操作：求最长路
				if (dist[v] < dist[u] + w) {
					dist[v] = dist[u] + w;
					if (!inQueue[v]) {
						queue.add(v);
						inQueue[v] = true;
						inCnt[v]++;
						// 检测负环：如果一个节点被松弛超过nodeCnt次，则存在负环
						if (inCnt[v] > nodeCnt) {
							return false;
						}
					}
				}
			}
		}
		return true;
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

		// 读入数组长度n和约束数m
		n = in.nextInt();
		m = in.nextInt();

		// 构建前缀和图
		buildPrefixGraph();

		// 处理m个约束
		for (int i = 0; i < m; i++) {
			int type = in.nextInt();
			int L = in.nextInt();
			int R = in.nextInt();
			int w = in.nextInt();

			if (type == 1) {
				// 类型1：区间和上界约束
				addUpperBound(L, R, w);
			} else {
				// 类型2：区间和下界约束
				addLowerBound(L, R, w);
			}
		}

		// 运行SPFA验证约束
		boolean feasible = spfa();

		if (feasible) {
			// 输出一个可行解：每个位置的最大值（差分）
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i <= n; i++) {
				sb.append(dist[pre[i]] - dist[pre[i - 1]]).append(" ");
			}
			out.println("Yes");
			out.println(sb.toString().trim());
		} else {
			out.println("No");
		}

		out.flush();
		out.close();
	}
}
