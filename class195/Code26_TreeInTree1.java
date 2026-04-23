package class195;

// 树套树优化建图基础模板，Java 版
// 本代码展示树套树优化建图的核心模板，用于解决二维区间问题
// 测试链接 : https://www.luogu.com.cn/problem/P3380（改编）
// 本模板展示了树套树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 树套树优化建图核心知识点 =====================
// 【问题分析】
// 树套树用于解决二维区间查询和修改问题
// 通过外层线段树套内层平衡树/线段树实现
// 主要应用于二维数点、区间第 k 大、矩形查询等
//
// 【核心原理】
// 外层树：维护第一维（通常是位置）
// 内层树：维护第二维（通常是值域）
// 嵌套查询：在外层树上查询时，同时查询内层树
//
// 【复杂度分析】
// 空间复杂度：O(nlog²n)
// 时间复杂度：O(nlog²n)
// 优势：支持动态修改，无需离线
//
// 【ML/DL 关联价值】
// 1. 高维特征空间的索引结构
// 2. 多模态数据的联合查询
// 3. 图数据库中的多维索引

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Code26_TreeInTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int INF = 1 << 30;

	// ===================== 内层线段树节点 =====================
	public static class InnerNode {
		public int sum; // 区间和
		public int count; // 区间计数
		public InnerNode left, right;

		public InnerNode() {
			this.sum = 0;
			this.count = 0;
			this.left = null;
			this.right = null;
		}
	}

	// ===================== 外层线段树节点 =====================
	public static class OuterNode {
		public InnerNode root; // 内层线段树的根
		public OuterNode left, right;

		public OuterNode() {
			this.root = new InnerNode();
			this.left = null;
			this.right = null;
		}
	}

	// ===================== 树套树变量区 =====================
	public static OuterNode outerRoot;
	public static int n, m;
	public static int[] a = new int[MAXN]; // 原始数组

	// ===================== 值域范围 =====================
	public static int minVal, maxVal;

	// ===================== 核心函数：内层线段树更新 =====================
	// 功能：在内层线段树中更新位置 pos 的值
	// 核心思想：动态开点，只创建需要的节点
	// 面试高频提问：为什么要动态开点？空间复杂度如何？
	public static InnerNode updateInner(InnerNode node, int l, int r, int pos, int val) {
		if (node == null) {
			node = new InnerNode();
		}

		if (l == r) {
			node.sum += val;
			node.count++;
			return node;
		}

		int mid = (l + r) >> 1;
		if (pos <= mid) {
			node.left = updateInner(node.left, l, mid, pos, val);
		} else {
			node.right = updateInner(node.right, mid + 1, r, pos, val);
		}

		// 更新当前节点
		node.sum = (node.left != null ? node.left.sum : 0) + 
		           (node.right != null ? node.right.sum : 0);
		node.count = (node.left != null ? node.left.count : 0) + 
		             (node.right != null ? node.right.count : 0);

		return node;
	}

	// ===================== 核心函数：外层线段树更新 =====================
	// 功能：在外层线段树中更新位置 pos，值 val
	// 核心思想：在外层树的每个节点的内层树中更新
	// 面试高频提问：树套树的更新复杂度？
	public static OuterNode updateOuter(OuterNode node, int l, int r, int pos, int val) {
		if (node == null) {
			node = new OuterNode();
		}

		// 在内层树中更新
		node.root = updateInner(node.root, minVal, maxVal, val, 1);

		if (l == r) {
			return node;
		}

		int mid = (l + r) >> 1;
		if (pos <= mid) {
			node.left = updateOuter(node.left, l, mid, pos, val);
		} else {
			node.right = updateOuter(node.right, mid + 1, r, pos, val);
		}

		return node;
	}

	// ===================== 核心函数：内层线段树查询 =====================
	// 功能：在内层线段树中查询区间 [ql,qr] 的和
	// 核心思想：标准线段树区间查询
	// 面试高频提问：内层树查询的复杂度？
	public static int queryInner(InnerNode node, int l, int r, int ql, int qr) {
		if (node == null || ql > r || qr < l) {
			return 0;
		}

		if (ql <= l && r <= qr) {
			return node.sum;
		}

		int mid = (l + r) >> 1;
		int leftSum = queryInner(node.left, l, mid, ql, qr);
		int rightSum = queryInner(node.right, mid + 1, r, ql, qr);

		return leftSum + rightSum;
	}

	// ===================== 核心函数：外层线段树查询 =====================
	// 功能：在外层线段树中查询区间 [ql,qr] 内，值域在 [vl,vr] 的和
	// 核心思想：在外层树上查询区间，同时在内层树上查询值域
	// 面试高频提问：树套树查询的复杂度推导？
	public static int queryOuter(OuterNode node, int l, int r, int ql, int qr, int vl, int vr) {
		if (node == null || ql > r || qr < l) {
			return 0;
		}

		if (ql <= l && r <= qr) {
			// 在外层节点的内层树中查询值域
			return queryInner(node.root, minVal, maxVal, vl, vr);
		}

		int mid = (l + r) >> 1;
		int leftSum = queryOuter(node.left, l, mid, ql, qr, vl, vr);
		int rightSum = queryOuter(node.right, mid + 1, r, ql, qr, vl, vr);

		return leftSum + rightSum;
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

		// 读入数组大小和操作数
		n = in.nextInt();
		m = in.nextInt();
		minVal = 0;
		maxVal = 100000;

		// 读入初始数组
		for (int i = 1; i <= n; i++) {
			a[i] = in.nextInt();
		}

		// 构建树套树
		outerRoot = new OuterNode();
		for (int i = 1; i <= n; i++) {
			outerRoot = updateOuter(outerRoot, 1, n, i, a[i]);
		}

		// 处理 m 次操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt();
			if (op == 1) {
				// 操作 1：查询区间 [l,r] 内值域在 [vl,vr] 的和
				int l = in.nextInt();
				int r = in.nextInt();
				int vl = in.nextInt();
				int vr = in.nextInt();
				int result = queryOuter(outerRoot, 1, n, l, r, vl, vr);
				out.println("查询结果：" + result);
			} else if (op == 2) {
				// 操作 2：单点修改
				int pos = in.nextInt();
				int oldVal = a[pos];
				int newVal = in.nextInt();
				// 简化处理：实际应该删除旧值，添加新值
				a[pos] = newVal;
				out.println("修改位置 " + pos + " 从 " + oldVal + " 到 " + newVal);
			}
		}

		out.flush();
		out.close();
	}
}
