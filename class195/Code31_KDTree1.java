package class195;

// KD 树优化建图基础模板，Java 版
// 本代码展示 KD 树优化建图的核心模板，用于解决多维空间查询问题
// 测试链接 : https://www.luogu.com.cn/problem/P1429（改编）
// 本模板展示了 KD 树优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== KD 树优化建图核心知识点 =====================
// 【问题分析】
// KD 树用于解决多维空间中的最近邻搜索、范围查询等问题
// 通过交替按维度分割空间，构建二叉搜索树
// 主要应用于最近邻搜索、范围查询、空间索引等
//
// 【核心原理】
// 维度交替：每一层按不同维度分割
// 中位数选择：选择中位数作为分割点
// 空间划分：左子树包含小于分割点的点，右子树包含大于分割点的点
// 剪枝优化：查询时利用距离下界剪枝
//
// 【复杂度分析】
// 构建复杂度：O(nlogn)
// 查询复杂度：O(√n)（平均情况）
// 空间复杂度：O(n)
//
// 【ML/DL 关联价值】
// 1. KNN 算法中的最近邻搜索
// 2. 聚类分析中的空间划分
// 3. 计算机视觉中的特征匹配

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code31_KDTree1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static double INF = 1e18;

	// ===================== 点结构体 =====================
	// 存储二维空间中的一个点
	// 包含 x 和 y 坐标
	public static class Point {
		public double x; // x 坐标
		public double y; // y 坐标
		public int id; // 点的编号

		public Point(double x, double y, int id) {
			this.x = x;
			this.y = y;
			this.id = id;
		}
	}

	// ===================== KD 树节点 =====================
	// 存储 KD 树的一个节点
	// 包含点信息、左右儿子、包围盒
	public static class Node {
		public Point point; // 当前节点代表的点
		public int left; // 左儿子编号
		public int right; // 右儿子编号
		public double minX, maxX, minY, maxY; // 包围盒

		public Node(Point p) {
			this.point = p;
			this.left = 0;
			this.right = 0;
			this.minX = this.maxX = p.x;
			this.minY = this.maxY = p.y;
		}
	}

	// ===================== KD 树变量区 =====================
	public static Node[] tree = new Node[MAXN];
	public static int nodeCnt;
	public static int root;
	public static int n;

	// ===================== 数组变量区 =====================
	public static Point[] points = new Point[MAXN];

	// ===================== 查询变量区 =====================
	public static double queryX, queryY; // 查询点坐标
	public static double minDist; // 最小距离
	public static int nearestId; // 最近邻的点编号

	// ===================== 核心函数：计算两点距离平方 =====================
	// 功能：计算两点之间的欧几里得距离平方
	// 核心思想：避免开方运算，提高效率
	// 面试高频提问：为什么用距离平方而不是距离？
	public static double distSq(Point p1, Point p2) {
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		return dx * dx + dy * dy;
	}

	// ===================== 核心函数：点到矩形盒的最小距离 =====================
	// 功能：计算点到矩形包围盒的最小距离平方
	// 核心思想：如果点在盒内，距离为 0；否则计算到最近边的距离
	// 面试高频提问：如何利用包围盒剪枝？
	public static double distToBox(int node, double x, double y) {
		Node nd = tree[node];
		double dx = 0, dy = 0;

		// 计算 x 方向的距离
		if (x < nd.minX) {
			dx = nd.minX - x;
		} else if (x > nd.maxX) {
			dx = x - nd.maxX;
		}

		// 计算 y 方向的距离
		if (y < nd.minY) {
			dy = nd.minY - y;
		} else if (y > nd.maxY) {
			dy = y - nd.maxY;
		}

		return dx * dx + dy * dy;
	}

	// ===================== 核心函数：更新包围盒 =====================
	// 功能：根据左右子树更新当前节点的包围盒
	// 核心思想：包围盒包含所有子节点
	// 面试高频提问：包围盒的作用是什么？
	public static void updateBox(int node) {
		Node nd = tree[node];
		if (nd.left != 0) {
			Node left = tree[nd.left];
			nd.minX = Math.min(nd.minX, left.minX);
			nd.maxX = Math.max(nd.maxX, left.maxX);
			nd.minY = Math.min(nd.minY, left.minY);
			nd.maxY = Math.max(nd.maxY, left.maxY);
		}
		if (nd.right != 0) {
			Node right = tree[nd.right];
			nd.minX = Math.min(nd.minX, right.minX);
			nd.maxX = Math.max(nd.maxX, right.maxX);
			nd.minY = Math.min(nd.minY, right.minY);
			nd.maxY = Math.max(nd.maxY, right.maxY);
		}
	}

	// ===================== 核心函数：比较函数（按 x 坐标） =====================
	// 功能：比较两个点的 x 坐标
	// 核心思想：用于按 x 维度排序
	// 面试高频提问：为什么要交替按维度排序？
	public static class XComparator implements java.util.Comparator<Point> {
		@Override
		public int compare(Point a, Point b) {
			return Double.compare(a.x, b.x);
		}
	}

	// ===================== 核心函数：比较函数（按 y 坐标） =====================
	// 功能：比较两个点的 y 坐标
	// 核心思想：用于按 y 维度排序
	public static class YComparator implements java.util.Comparator<Point> {
		@Override
		public int compare(Point a, Point b) {
			return Double.compare(a.y, b.y);
		}
	}

	// ===================== 核心函数：构建 KD 树 =====================
	// 功能：递归构建 KD 树
	// 核心思想：交替按 x 和 y 维度分割，选择中位数作为根
	// 面试高频提问：KD 树的构建过程？
	public static int build(int l, int r, int depth) {
		if (l > r) {
			return 0;
		}

		int mid = (l + r) >> 1;

		// 按当前维度排序
		if (depth % 2 == 0) {
			java.util.Arrays.sort(points, l, r + 1, new XComparator());
		} else {
			java.util.Arrays.sort(points, l, r + 1, new YComparator());
		}

		// 创建当前节点
		int node = ++nodeCnt;
		tree[node] = new Node(points[mid]);

		// 递归构建左右子树
		tree[node].left = build(l, mid - 1, depth + 1);
		tree[node].right = build(mid + 1, r, depth + 1);

		// 更新包围盒
		updateBox(node);

		return node;
	}

	// ===================== 核心函数：查询最近邻 =====================
	// 功能：查询距离查询点最近的点
	// 核心思想：DFS 遍历，利用包围盒剪枝
	// 面试高频提问：KD 树最近邻查询的剪枝策略？
	public static void queryNearest(int node, int depth, double x, double y) {
		if (node == 0) {
			return;
		}

		Node nd = tree[node];
		Point p = nd.point;

		// 计算当前点的距离
		double d = distSq(p, new Point(x, y, 0));
		if (d < minDist) {
			minDist = d;
			nearestId = p.id;
		}

		// 计算到左右子树包围盒的距离
		double distLeft = nd.left != 0 ? distToBox(nd.left, x, y) : INF;
		double distRight = nd.right != 0 ? distToBox(nd.right, x, y) : INF;

		// 优先搜索距离小的子树
		if (distLeft < distRight) {
			if (distLeft < minDist) {
				queryNearest(nd.left, depth + 1, x, y);
			}
			if (distRight < minDist) {
				queryNearest(nd.right, depth + 1, x, y);
			}
		} else {
			if (distRight < minDist) {
				queryNearest(nd.right, depth + 1, x, y);
			}
			if (distLeft < minDist) {
				queryNearest(nd.left, depth + 1, x, y);
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

		double nextDouble() throws IOException {
			return Double.parseDouble(String.valueOf(nextInt()));
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入点数
		n = in.nextInt();

		// 读入所有点
		for (int i = 1; i <= n; i++) {
			double x = in.nextDouble();
			double y = in.nextDouble();
			points[i] = new Point(x, y, i);
		}

		// 构建 KD 树
		root = build(1, n, 0);

		// 查询示例
		int queryCount = in.nextInt();
		for (int i = 0; i < queryCount; i++) {
			queryX = in.nextDouble();
			queryY = in.nextDouble();

			minDist = INF;
			nearestId = -1;

			queryNearest(root, 0, queryX, queryY);

			out.println("查询点 (" + queryX + ", " + queryY + ") 的最近邻：");
			out.println("  最近点编号：" + nearestId);
			out.println("  最小距离：" + Math.sqrt(minDist));
		}

		out.flush();
		out.close();
	}
}
