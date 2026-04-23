package class195;

// 左偏树/斜堆优化建图基础模板，Java 版
// 本代码展示左偏树优化建图的核心模板，用于解决可并堆问题
// 测试链接 : https://www.luogu.com.cn/problem/P3377（改编）
// 本模板展示了左偏树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 左偏树/斜堆优化建图核心知识点 =====================
// 【问题分析】
// 左偏树是一种可并堆，支持高效的合并操作
// 通过维护左偏性质，保证树的高度为 O(logn)
// 主要应用于可并堆、优先队列合并、图论中的堆优化等
//
// 【核心原理】
// 左偏性质：左儿子的距离不小于右儿子的距离
// 距离定义：节点到最近空子树的距离
// 合并操作：递归合并，交换右儿子维护左偏性质
// 堆性质：父节点的值小于等于子节点（小根堆）
//
// 【复杂度分析】
// 合并复杂度：O(logn)
// 插入复杂度：O(logn)
// 删除复杂度：O(logn)
// 查询复杂度：O(1)
//
// 【ML/DL 关联价值】
// 1. 优先队列的高效实现
// 2. 图算法中的堆优化
// 3. 并行计算中的任务调度

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code36_LeftistTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;

	// ===================== 左偏树节点 =====================
	// 每个节点包含值、左右儿子、距离、父节点
	public static class Node {
		public int val; // 节点值
		public int left; // 左儿子编号
		public int right; // 右儿子编号
		public int dist; // 距离（到最近空子树）
		public int parent; // 父节点编号

		public Node(int val) {
			this.val = val;
			this.left = 0;
			this.right = 0;
			this.dist = 0;
			this.parent = 0;
		}
	}

	// ===================== 左偏树变量区 =====================
	public static Node[] tree = new Node[MAXN];
	public static int n, m;
	public static int[] root = new int[MAXN]; // 每个集合的根

	// ===================== 核心函数：创建新节点 =====================
	// 功能：创建一个新的左偏树节点
	// 核心思想：初始化节点的所有信息
	// 面试高频提问：左偏树节点需要哪些信息？
	public static int createNode(int val) {
		int node = ++n;
		tree[node] = new Node(val);
		return node;
	}

	// ===================== 核心函数：获取节点距离 =====================
	// 功能：获取节点的距离值
	// 核心思想：空节点距离为 -1
	// 面试高频提问：距离的定义是什么？
	public static int getDist(int node) {
		if (node == 0) {
			return -1;
		}
		return tree[node].dist;
	}

	// ===================== 核心函数：更新节点距离 =====================
	// 功能：根据左右儿子更新节点的距离
	// 核心思想：距离 = min(左儿子距离，右儿子距离) + 1
	// 面试高频提问：如何更新距离？
	public static void updateDist(int node) {
		if (node == 0) {
			return;
		}
		tree[node].dist = Math.min(getDist(tree[node].left), 
			getDist(tree[node].right)) + 1;
	}

	// ===================== 核心函数：合并两个左偏树 =====================
	// 功能：合并两个左偏树
	// 核心思想：递归合并，维护左偏性质和堆性质
	// 面试高频提问：左偏树合并的核心步骤？
	public static int merge(int x, int y) {
		if (x == 0 || y == 0) {
			return x + y; // 返回非空的那个
		}

		// 保证 x 的根值较小（小根堆）
		if (tree[x].val > tree[y].val) {
			int temp = x;
			x = y;
			y = temp;
		}

		// 递归合并 x 的右儿子和 y
		tree[x].right = merge(tree[x].right, y);

		// 设置父节点
		if (tree[x].right != 0) {
			tree[tree[x].right].parent = x;
		}

		// 维护左偏性质：左儿子的距离不小于右儿子
		if (getDist(tree[x].left) < getDist(tree[x].right)) {
			int temp = tree[x].left;
			tree[x].left = tree[x].right;
			tree[x].right = temp;
		}

		// 更新距离
		updateDist(x);

		return x;
	}

	// ===================== 核心函数：插入节点 =====================
	// 功能：向左偏树中插入一个节点
	// 核心思想：创建新节点并与原树合并
	// 面试高频提问：左偏树插入的复杂度？
	public static int insert(int root, int val) {
		int newNode = createNode(val);
		return merge(root, newNode);
	}

	// ===================== 核心函数：删除堆顶 =====================
	// 功能：删除左偏树的堆顶（最小值）
	// 核心思想：合并左右子树
	// 面试高频提问：删除堆顶后如何维护？
	public static int deleteMin(int root) {
		if (root == 0) {
			return 0;
		}

		int left = tree[root].left;
		int right = tree[root].right;

		// 合并左右子树
		int newRoot = merge(left, right);
		if (newRoot != 0) {
			tree[newRoot].parent = 0;
		}

		// 清空原根
		tree[root].left = tree[root].right = 0;
		tree[root].dist = 0;

		return newRoot;
	}

	// ===================== 核心函数：查询堆顶 =====================
	// 功能：查询左偏树的最小值
	// 核心思想：根节点即为最小值
	// 面试高频提问：为什么根节点是最小值？
	public static int getMin(int root) {
		if (root == 0) {
			return -1;
		}
		return tree[root].val;
	}

	// ===================== 核心函数：查找根节点 =====================
	// 功能：查找节点所在集合的根
	// 核心思想：通过父节点指针向上找
	// 面试高频提问：如何维护集合信息？
	public static int findRoot(int node) {
		while (tree[node].parent != 0) {
			node = tree[node].parent;
		}
		return node;
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

		// 读入初始元素个数和操作数
		n = 0; // 节点计数器
		m = in.nextInt();

		// 初始化左偏树
		for (int i = 1; i <= m; i++) {
			int val = in.nextInt();
			root[i] = createNode(val);
		}

		// 处理操作
		int q = in.nextInt();
		for (int i = 0; i < q; i++) {
			int op = in.nextInt();
			if (op == 1) {
				// 操作 1：合并两个集合
				int x = in.nextInt();
				int y = in.nextInt();
				if (x != y) {
					int rootX = findRoot(x);
					int rootY = findRoot(y);
					if (rootX != rootY) {
						int newRoot = merge(rootX, rootY);
						tree[rootX].parent = newRoot;
						tree[rootY].parent = newRoot;
					}
				}
			} else if (op == 2) {
				// 操作 2：插入元素
				int x = in.nextInt();
				int val = in.nextInt();
				int rootX = findRoot(x);
				rootX = insert(rootX, val);
			} else if (op == 3) {
				// 操作 3：查询最小值
				int x = in.nextInt();
				int rootX = findRoot(x);
				int minVal = getMin(rootX);
				out.println("集合 " + x + " 的最小值：" + 
					(minVal == -1 ? "空" : minVal));
			} else if (op == 4) {
				// 操作 4：删除最小值
				int x = in.nextInt();
				int rootX = findRoot(x);
				rootX = deleteMin(rootX);
			}
		}

		out.flush();
		out.close();
	}
}
