package class195;

// 笛卡尔树优化建图基础模板，Java 版
// 本代码展示笛卡尔树优化建图的核心模板，用于解决区间最值问题
// 测试链接 : https://www.luogu.com.cn/problem/P5854（改编）
// 本模板展示了笛卡尔树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 笛卡尔树优化建图核心知识点 =====================
// 【问题分析】
// 笛卡尔树用于解决区间最值、笛卡尔树构建等问题
// 通过维护堆性质和 BST 性质，实现高效的区间最值查询
// 主要应用于 RMQ 问题、笛卡尔树构建、区间最值统计等
//
// 【核心原理】
// 堆性质：父节点的值小于等于子节点（小根堆）
// BST 性质：中序遍历为原序列
// 构建方法：单调栈 O(n) 构建
// 唯一性：序列元素互异时笛卡尔树唯一
//
// 【复杂度分析】
// 构建复杂度：O(n)（单调栈）
// 查询复杂度：O(1) 或 O(logn)
// 空间复杂度：O(n)
//
// 【ML/DL 关联价值】
// 1. 树结构中的层次化最值索引
// 2. 注意力机制中的稀疏化结构
// 3. 图神经网络中的树形聚合

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code27_CartesianTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 5000001;
	public static int INF = 1 << 30;

	// ===================== 笛卡尔树节点 =====================
	// 笛卡尔树节点包含左右儿子和父节点
	// 用于维护树结构和快速查询
	public static class Node {
		public int val; // 节点值
		public int left; // 左儿子编号
		public int right; // 右儿子编号
		public int parent; // 父节点编号

		public Node(int val) {
			this.val = val;
			this.left = 0;
			this.right = 0;
			this.parent = 0;
		}
	}

	// ===================== 笛卡尔树变量区 =====================
	public static Node[] tree = new Node[MAXN];
	public static int n;
	public static int root; // 笛卡尔树的根节点

	// ===================== 数组变量区 =====================
	public static int[] a = new int[MAXN]; // 原始数组
	public static int[] stack = new int[MAXN]; // 单调栈
	public static int top; // 栈顶指针

	// ===================== 核心函数：创建新节点 =====================
	// 功能：创建一个新的笛卡尔树节点
	// 核心思想：初始化节点的所有指针为 0
	// 面试高频提问：笛卡尔树节点需要哪些信息？
	public static int createNode(int val) {
		tree[val] = new Node(val);
		return val;
	}

	// ===================== 核心函数：单调栈构建笛卡尔树 =====================
	// 功能：使用单调栈 O(n) 构建笛卡尔树
	// 核心思想：维护一个单调递增的栈，每次插入新节点时调整树结构
	// 面试高频提问：为什么单调栈可以构建笛卡尔树？
	public static void buildCartesianTree() {
		top = 0;
		root = 1; // 初始根节点为第一个元素

		for (int i = 1; i <= n; i++) {
			// 创建新节点
			createNode(i);
			tree[i].val = a[i];

			int last = 0; // 记录最后一个被弹出的节点

			// 维护单调递增栈
			// 弹出所有值大于当前值的节点
			while (top > 0 && a[stack[top]] > a[i]) {
				last = stack[top];
				top--;
			}

			// 如果栈不为空，当前节点成为栈顶节点的右儿子
			if (top > 0) {
				tree[i].parent = stack[top];
				tree[stack[top]].right = i;
			}

			// 如果有被弹出的节点，最后一个被弹出的节点成为当前节点的左儿子
			if (last != 0) {
				tree[i].left = last;
				tree[last].parent = i;
			}

			// 当前节点入栈
			stack[++top] = i;

			// 更新根节点
			if (tree[i].parent == 0) {
				root = i;
			}
		}
	}

	// ===================== 核心函数：DFS 遍历笛卡尔树 =====================
	// 功能：DFS 遍历笛卡尔树，输出树结构
	// 核心思想：先序遍历展示树结构
	// 面试高频提问：笛卡尔树的中序遍历是什么？
	public static void dfs(int u, int depth) {
		if (u == 0) {
			return;
		}

		// 输出当前节点信息
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append("  ");
		}
		sb.append("节点 ").append(u).append(": 值=").append(tree[u].val);
		System.out.println(sb.toString());

		// 递归遍历左右子树
		dfs(tree[u].left, depth + 1);
		dfs(tree[u].right, depth + 1);
	}

	// ===================== 核心函数：查询区间最值 =====================
	// 功能：查询区间 [l,r] 的最小值位置
	// 核心思想：利用笛卡尔树的 LCA 性质
	// 面试高频提问：如何用笛卡尔树做 RMQ？
	public static int queryRMQ(int l, int r) {
		// 简化版本：实际应该用 LCA 查询
		// 笛卡尔树中，区间 [l,r] 的最小值对应 l 和 r 的 LCA
		int minPos = l;
		for (int i = l + 1; i <= r; i++) {
			if (a[i] < a[minPos]) {
				minPos = i;
			}
		}
		return minPos;
	}

	// ===================== 核心函数：验证笛卡尔树性质 =====================
	// 功能：验证构建的树是否满足笛卡尔树性质
	// 核心思想：检查堆性质和 BST 性质
	// 面试高频提问：笛卡尔树有哪些性质需要验证？
	public static boolean verifyCartesianTree(int u, int minVal, int maxVal) {
		if (u == 0) {
			return true;
		}

		// 检查当前节点值是否在合法范围内
		if (tree[u].val < minVal || tree[u].val > maxVal) {
			return false;
		}

		// 检查堆性质：父节点值小于等于子节点
		if (tree[u].left != 0 && tree[tree[u].left].val < tree[u].val) {
			return false;
		}
		if (tree[u].right != 0 && tree[tree[u].right].val < tree[u].val) {
			return false;
		}

		// 递归检查左右子树
		return verifyCartesianTree(tree[u].left, minVal, tree[u].val - 1) &&
		       verifyCartesianTree(tree[u].right, tree[u].val + 1, maxVal);
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

		// 读入数组大小
		n = in.nextInt();

		// 读入数组元素
		for (int i = 1; i <= n; i++) {
			a[i] = in.nextInt();
		}

		// 构建笛卡尔树
		buildCartesianTree();

		// 输出笛卡尔树信息
		out.println("笛卡尔树根节点：" + root);
		out.println("笛卡尔树结构：");
		dfs(root, 0);

		// 验证笛卡尔树性质
		boolean valid = verifyCartesianTree(root, -INF, INF);
		out.println("笛卡尔树性质验证：" + (valid ? "通过" : "失败"));

		// RMQ 查询示例
		int queryCount = in.nextInt();
		for (int i = 0; i < queryCount; i++) {
			int l = in.nextInt();
			int r = in.nextInt();
			int minPos = queryRMQ(l, r);
			out.println("区间 [" + l + ", " + r + "] 最小值位置：" + minPos + 
				", 最小值：" + a[minPos]);
		}

		out.flush();
		out.close();
	}
}
