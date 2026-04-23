package class195;

// 动态开点线段树优化建图基础模板，Java 版
// 本代码展示动态开点线段树优化建图的核心模板，用于解决值域优化问题
// 测试链接 : https://www.luogu.com.cn/problem/P3293（改编）
// 本模板展示了动态开点线段树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 动态开点线段树优化建图核心知识点 =====================
// 【问题分析】
// 动态开点线段树用于解决值域很大但实际使用点很少的问题
// 通过动态创建节点，节省空间复杂度
// 主要应用于值域优化、离散化替代、动态区间等
//
// 【核心原理】
// 动态开点：只在需要时创建节点
// 值域映射：将数值映射到线段树区间
// 懒惰标记：延迟更新，优化复杂度
//
// 【复杂度分析】
// 空间复杂度：O(mlogM)（m 为操作数，M 为值域）
// 时间复杂度：O(mlogM)
// 优势：避免离散化，支持动态操作
//
// 【ML/DL 关联价值】
// 1. 大规模特征空间的稀疏表示
// 2. 动态图神经网络中的节点管理
// 3. 在线学习中的增量更新

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code22_DynamicSegmentTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 200001;
	public static int MAX_NODES = 4000001;
	public static int INF = 1 << 30;

	// ===================== 动态开点线段树变量区 =====================
	public static int[] leftChild = new int[MAX_NODES];
	public static int[] rightChild = new int[MAX_NODES];
	public static int[] minVal = new int[MAX_NODES];
	public static int[] maxVal = new int[MAX_NODES];
	public static int nodeCnt;
	public static int root;

	// ===================== 值域变量区 =====================
	public static int n, m;
	public static int minRange, maxRange;

	// ===================== 核心函数：创建新节点 =====================
	// 功能：动态创建一个新的线段树节点
	// 核心思想：只在需要时分配空间
	// 面试高频提问：动态开点相比静态开点有什么优势？
	public static int createNode() {
		int node = ++nodeCnt;
		leftChild[node] = 0;
		rightChild[node] = 0;
		minVal[node] = INF;
		maxVal[node] = -INF;
		return node;
	}

	// ===================== 核心函数：动态开点更新 =====================
	// 功能：在值域 [l,r] 中更新位置 pos 的值
	// 核心思想：动态创建路径上的节点
	// 面试高频提问：为什么只需要创建 O(logM) 个节点？
	public static void update(int node, int l, int r, int pos, int val) {
		if (l == r) {
			// 叶子节点
			minVal[node] = Math.min(minVal[node], val);
			maxVal[node] = Math.max(maxVal[node], val);
			return;
		}
		int mid = (l + r) >> 1;
		if (pos <= mid) {
			// 更新左子树
			if (leftChild[node] == 0) {
				leftChild[node] = createNode();
			}
			update(leftChild[node], l, mid, pos, val);
		} else {
			// 更新右子树
			if (rightChild[node] == 0) {
				rightChild[node] = createNode();
			}
			update(rightChild[node], mid + 1, r, pos, val);
		}
		// 更新当前节点
		if (leftChild[node] != 0) {
			minVal[node] = Math.min(minVal[node], minVal[leftChild[node]]);
			maxVal[node] = Math.max(maxVal[node], maxVal[leftChild[node]]);
		}
		if (rightChild[node] != 0) {
			minVal[node] = Math.min(minVal[node], minVal[rightChild[node]]);
			maxVal[node] = Math.max(maxVal[node], maxVal[rightChild[node]]);
		}
	}

	// ===================== 核心函数：区间查询最小值 =====================
	// 功能：查询值域 [ql,qr] 中的最小值
	// 核心思想：只遍历存在的节点
	// 面试高频提问：如何处理不存在的节点？
	public static int queryMin(int node, int l, int r, int ql, int qr) {
		if (node == 0 || ql > r || qr < l) {
			return INF;
		}
		if (ql <= l && r <= qr) {
			return minVal[node];
		}
		int mid = (l + r) >> 1;
		int leftMin = queryMin(leftChild[node], l, mid, ql, qr);
		int rightMin = queryMin(rightChild[node], mid + 1, r, ql, qr);
		return Math.min(leftMin, rightMin);
	}

	// ===================== 核心函数：区间查询最大值 =====================
	// 功能：查询值域 [ql,qr] 中的最大值
	// 核心思想：只遍历存在的节点
	// 面试高频提问：最大值和最小值查询有什么区别？
	public static int queryMax(int node, int l, int r, int ql, int qr) {
		if (node == 0 || ql > r || qr < l) {
			return -INF;
		}
		if (ql <= l && r <= qr) {
			return maxVal[node];
		}
		int mid = (l + r) >> 1;
		int leftMax = queryMax(leftChild[node], l, mid, ql, qr);
		int rightMax = queryMax(rightChild[node], mid + 1, r, ql, qr);
		return Math.max(leftMax, rightMax);
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
				// 操作 1：在位置 pos 插入值 val
				int pos = in.nextInt();
				int val = in.nextInt();
				update(root, minRange, maxRange, pos, val);
			} else if (op == 2) {
				// 操作 2：查询区间 [l,r] 的最小值
				int l = in.nextInt();
				int r = in.nextInt();
				int minV = queryMin(root, minRange, maxRange, l, r);
				out.println("区间 [" + l + ", " + r + "] 最小值：" + (minV == INF ? "不存在" : minV));
			} else if (op == 3) {
				// 操作 3：查询区间 [l,r] 的最大值
				int l = in.nextInt();
				int r = in.nextInt();
				int maxV = queryMax(root, minRange, maxRange, l, r);
				out.println("区间 [" + l + ", " + r + "] 最大值：" + (maxV == -INF ? "不存在" : maxV));
			}
		}

		// 输出使用的节点数
		out.println("使用的节点数：" + nodeCnt);

		out.flush();
		out.close();
	}
}
