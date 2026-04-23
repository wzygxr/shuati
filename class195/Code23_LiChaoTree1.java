package class195;

// 李超树优化建图基础模板，Java 版
// 本代码展示李超树优化建图的核心模板，用于解决直线/函数最值问题
// 测试链接 : https://www.luogu.com.cn/problem/P4097（改编）
// 本模板展示了李超树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 李超树优化建图核心知识点 =====================
// 【问题分析】
// 李超树用于解决动态插入直线/函数，查询某点的最值问题
// 通过线段树结构维护直线集合，支持高效插入和查询
// 主要应用于斜率优化 DP、函数最值、几何问题等
//
// 【核心原理】
// 优势直线：在每个区间保留最优的直线
// 标记永久化：不删除旧直线，直接覆盖或保留
// 递归比较：在交点处比较两条直线的优劣
//
// 【复杂度分析】
// 空间复杂度：O(nlogM)（n 为操作数，M 为值域）
// 时间复杂度：O(nlog²M)（每次插入 O(logM)，查询 O(logM)）
// 优势：支持动态插入，无需离线处理
//
// 【ML/DL 关联价值】
// 1. 动态规划中的斜率优化
// 2. 凸优化问题中的分段线性函数
// 3. 在线学习中的模型更新

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code23_LiChaoTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int MAX_NODES = 4000001;
	public static long INF = Long.MAX_VALUE / 2;

	// ===================== 直线表示 =====================
	// 直线方程：y = kx + b
	// k 为斜率，b 为截距
	public static class Line {
		public long k; // 斜率
		public long b; // 截距

		public Line(long k, long b) {
			this.k = k;
			this.b = b;
		}

		// 计算直线在 x 处的值
		public long getValue(long x) {
			return k * x + b;
		}
	}

	// ===================== 李超树变量区 =====================
	public static Line[] tree = new Line[MAX_NODES];
	public static int nodeCnt;
	public static int root;

	// ===================== 值域变量区 =====================
	public static int n, m;
	public static int minRange, maxRange;

	// ===================== 核心函数：创建新节点 =====================
	// 功能：动态创建一个新的李超树节点
	// 核心思想：只在需要时分配空间
	// 面试高频提问：李超树为什么需要动态开点？
	public static int createNode() {
		return ++nodeCnt;
	}

	// ===================== 核心函数：比较两条直线 =====================
	// 功能：比较两条直线在位置 x 处的值
	// 核心思想：返回更优的直线（最大值或最小值）
	// 面试高频提问：如何判断哪条直线更优？
	public static Line compare(Line l1, Line l2, long x, boolean isMax) {
		if (l1 == null) return l2;
		if (l2 == null) return l1;
		long v1 = l1.getValue(x);
		long v2 = l2.getValue(x);
		return isMax ? (v1 > v2 ? l1 : l2) : (v1 < v2 ? l1 : l2);
	}

	// ===================== 核心函数：插入直线 =====================
	// 功能：向李超树中插入一条直线
	// 核心思想：在区间 [l,r] 中维护优势直线
	// 面试高频提问：为什么要在中点比较？如何处理交点？
	public static void insert(int node, int l, int r, Line newLine, boolean isMax) {
		if (l > r) return;

		// 如果当前节点为空，直接插入
		if (tree[node] == null) {
			tree[node] = newLine;
			return;
		}

		int mid = (l + r) >> 1;
		Line cur = tree[node];

		// 在中点处比较
		boolean betterAtMid = isMax ? 
			(newLine.getValue(mid) > cur.getValue(mid)) : 
			(newLine.getValue(mid) < cur.getValue(mid));

		if (betterAtMid) {
			// 新直线在中点更优，交换
			Line temp = cur;
			tree[node] = newLine;
			newLine = temp;
			cur = tree[node];
		}

		// 如果区间长度为 1，直接返回
		if (l == r) {
			return;
		}

		// 判断新直线在左半区间或右半区间是否可能更优
		boolean betterAtLeft = isMax ? 
			(newLine.getValue(l) > cur.getValue(l)) : 
			(newLine.getValue(l) < cur.getValue(l));
		boolean betterAtRight = isMax ? 
			(newLine.getValue(r) > cur.getValue(r)) : 
			(newLine.getValue(r) < cur.getValue(r));

		if (betterAtLeft) {
			// 在左半区间可能更优，递归处理左子树
			insert(node << 1, l, mid, newLine, isMax);
		} else if (betterAtRight) {
			// 在右半区间可能更优，递归处理右子树
			insert(node << 1 | 1, mid + 1, r, newLine, isMax);
		}
		// 否则新直线完全被当前直线支配，不需要继续插入
	}

	// ===================== 核心函数：查询最值 =====================
	// 功能：查询位置 x 处的最优值
	// 核心思想：遍历从根到叶子的路径，比较所有直线
	// 面试高频提问：为什么只需要比较路径上的直线？
	public static long query(int node, int l, int r, int x, boolean isMax) {
		if (node == 0 || l > r) {
			return isMax ? -INF : INF;
		}

		long result;
		if (tree[node] != null) {
			result = tree[node].getValue(x);
		} else {
			result = isMax ? -INF : INF;
		}

		if (l == r) {
			return result;
		}

		int mid = (l + r) >> 1;
		if (x <= mid) {
			long childResult = query(node << 1, l, mid, x, isMax);
			result = isMax ? Math.max(result, childResult) : Math.min(result, childResult);
		} else {
			long childResult = query(node << 1 | 1, mid + 1, r, x, isMax);
			result = isMax ? Math.max(result, childResult) : Math.min(result, childResult);
		}

		return result;
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

		long nextLong() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			long val = 0;
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

		// 读入操作数和值域范围
		m = in.nextInt();
		minRange = in.nextInt();
		maxRange = in.nextInt();

		// 创建根节点
		root = createNode();

		// 处理 m 次操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt();
			if (op == 1) {
				// 操作 1：插入直线 y = kx + b
				long k = in.nextLong();
				long b = in.nextLong();
				Line line = new Line(k, b);
				insert(root, minRange, maxRange, line, true); // 求最大值
			} else if (op == 2) {
				// 操作 2：查询位置 x 的最大值
				int x = in.nextInt();
				long result = query(root, minRange, maxRange, x, true);
				out.println("位置 " + x + " 的最大值：" + (result == -INF ? "不存在" : result));
			}
		}

		// 输出使用的节点数
		out.println("使用的节点数：" + nodeCnt);

		out.flush();
		out.close();
	}
}
