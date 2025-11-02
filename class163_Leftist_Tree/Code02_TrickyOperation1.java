package class155;

/**
 * 洛谷 P3273 棘手的操作
 * 题目链接：https://www.luogu.com.cn/problem/P3273
 * 
 * 题目描述：
 * 编号1~n个节点，每个节点独立且有自己的权值，实现如下7种操作，操作一共调用m次：
 * U x y  : x所在的集合和y所在的集合合并
 * A1 x v : x节点的权值增加v
 * A2 x v : x所在的集合所有节点的权值增加v
 * A3 v   : 所有节点的权值增加v
 * F1 x   : 打印x节点的权值
 * F2 x   : 打印x所在的集合中，权值最大的节点的权值
 * F3     : 打印所有节点中，权值最大的节点的权值
 * 
 * 解题思路：
 * 使用左偏树（Leftist Tree）+ 并查集（Union-Find）+ 延迟标记（Lazy Propagation）的组合数据结构。
 * 
 * 核心思想：
 * 1. 使用左偏树维护每个集合中的元素，支持高效合并和删除操作
 * 2. 使用并查集维护集合的连通性
 * 3. 使用延迟标记技术处理区间更新操作
 * 4. 使用TreeMap维护所有集合头节点的权值，支持快速查询最大值
 * 
 * 关键优化：
 * 1. 延迟标记：避免对整棵树进行实际更新，只在需要时才下传标记
 * 2. 迭代遍历：避免递归遍历导致的栈溢出问题
 * 3. TreeMap：维护头节点权值的有序性，支持O(log n)查询最大值
 * 
 * 时间复杂度分析：
 * - U操作: O(log n)
 * - A1操作: O(log n)
 * - A2操作: O(log n)
 * - A3操作: O(1)
 * - F1操作: O(log n)
 * - F2操作: O(log n)
 * - F3操作: O(log n)
 * 
 * 空间复杂度分析:
 * - 存储节点信息: O(n)
 * - 存储左偏树结构: O(n)
 * - 存储并查集: O(n)
 * - TreeMap存储头节点: O(n)
 * - 总体: O(n)
 * 
 * 相关题目：
 * - Java实现：Code02_TrickyOperation1.java
 * - C++实现：Code02_TrickyOperation2.java
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

public class Code02_TrickyOperation1 {

	public static int MAXN = 300001;

	public static int n, m;

	// 左偏树需要的数组
	public static int[] num = new int[MAXN];    // 节点权值
	public static int[] up = new int[MAXN];     // 父节点
	public static int[] left = new int[MAXN];   // 左子节点
	public static int[] right = new int[MAXN];  // 右子节点
	public static int[] dist = new int[MAXN];   // 距离（空路径长度）

	// 并查集的路径信息
	public static int[] father = new int[MAXN];

	// 集合的大小信息
	public static int[] size = new int[MAXN];

	// 集合内所有数字应该加多少值（延迟标记）
	public static int[] add = new int[MAXN];

	// 所有集合头节点的值，进入这个有序表，头节点有序表
	public static TreeMap<Integer, Integer> heads = new TreeMap<>();

	// 所有数字应该加多少（全局延迟标记）
	public static int addAll = 0;

	// 准备好一个栈，用迭代方式实现先序遍历，不用递归方式
	public static int[] stack = new int[MAXN];

	/**
	 * 编号为h的节点不再是左偏树的头，在头节点有序表里删掉一份h节点的值
	 * @param h 节点编号
	 */
	public static void minusHead(int h) {
		if (h != 0) {
			int hnum = num[h] + add[h];
			if (heads.get(hnum) == 1) {
				heads.remove(hnum);
			} else {
				heads.put(hnum, heads.get(hnum) - 1);
			}
		}
	}

	/**
	 * 编号为h的节点当前是左偏树的头，在头节点有序表里增加一份h节点的值
	 * @param h 节点编号
	 */
	public static void addHead(int h) {
		if (h != 0) {
			int hnum = num[h] + add[h];
			heads.put(hnum, heads.getOrDefault(hnum, 0) + 1);
		}
	}

	/**
	 * 初始化数据结构
	 */
	public static void prepare() {
		dist[0] = -1;
		heads.clear();
		for (int i = 1; i <= n; i++) {
			up[i] = left[i] = right[i] = dist[i] = 0;
			father[i] = i;
			size[i] = 1;
			add[i] = 0;
			addHead(i);
		}
		addAll = 0;
	}

	/**
	 * 返回i节点所在左偏树的树头（并查集查找）
	 * @param i 节点编号
	 * @return 树头节点编号
	 */
	public static int find(int i) {
		father[i] = father[i] == i ? i : find(father[i]);
		return father[i];
	}

	/**
	 * 合并两棵左偏树
	 * @param i 第一棵左偏树的根节点
	 * @param j 第二棵左偏树的根节点
	 * @return 合并后的左偏树根节点
	 */
	public static int merge(int i, int j) {
		if (i == 0 || j == 0) {
			return i + j;
		}
		int tmp;
		// 维护大根堆性质
		if (num[i] < num[j]) {
			tmp = i;
			i = j;
			j = tmp;
		}
		right[i] = merge(right[i], j);
		up[right[i]] = i;
		// 维护左偏性质
		if (dist[left[i]] < dist[right[i]]) {
			tmp = left[i];
			left[i] = right[i];
			right[i] = tmp;
		}
		dist[i] = dist[right[i]] + 1;
		father[left[i]] = father[right[i]] = i;
		return i;
	}

	/**
	 * 节点i是所在左偏树的任意节点，删除节点i，返回整棵树的头节点编号
	 * @param i 要删除的节点编号
	 * @return 删除节点后整棵树的头节点编号
	 */
	public static int remove(int i) {
		int h = find(i);
		father[left[i]] = left[i];
		father[right[i]] = right[i];
		int s = merge(left[i], right[i]);
		int f = up[i];
		father[i] = s;
		up[s] = f;
		if (h != i) {
			father[s] = h;
			if (left[f] == i) {
				left[f] = s;
			} else {
				right[f] = s;
			}
			for (int d = dist[s], tmp; dist[f] > d + 1; f = up[f], d++) {
				dist[f] = d + 1;
				if (dist[left[f]] < dist[right[f]]) {
					tmp = left[f];
					left[f] = right[f];
					right[f] = tmp;
				}
			}
		}
		up[i] = left[i] = right[i] = dist[i] = 0;
		return father[s];
	}

	/**
	 * 以i为头的左偏树，遭遇了更大的左偏树
	 * i的标签信息取消，以i为头的整棵树所有节点的值增加v
	 * 不用递归实现先序遍历，容易爆栈，所以用迭代实现先序遍历
	 * @param i 左偏树根节点
	 * @param v 要增加的值
	 */
	public static void down(int i, int v) {
		if (i != 0) {
			add[i] = 0;
			int size = 0;
			stack[++size] = i;
			while (size > 0) {
				i = stack[size--];
				num[i] += v;
				if (right[i] != 0) {
					stack[++size] = right[i];
				}
				if (left[i] != 0) {
					stack[++size] = left[i];
				}
			}
		}
	}

	/**
	 * U x y  : x所在的集合和y所在的集合合并
	 * @param i 节点x
	 * @param j 节点y
	 */
	public static void u(int i, int j) {
		int l = find(i);
		int r = find(j);
		if (l == r) {
			return;
		}
		int lsize = size[l];
		minusHead(l);
		int rsize = size[r];
		minusHead(r);
		int addTag;
		if (lsize <= rsize) {
			down(l, add[l] - add[r]);
			addTag = add[r];
		} else {
			down(r, add[r] - add[l]);
			addTag = add[l];
		}
		int h = merge(l, r);
		size[h] = lsize + rsize;
		add[h] = addTag;
		addHead(h);
	}

	/**
	 * A1 x v : x节点的权值增加v
	 * @param i 节点x
	 * @param v 增加的值
	 */
	public static void a1(int i, int v) {
		int h = find(i);
		minusHead(h);
		int l = remove(i);
		if (l != 0) {
			size[l] = size[h] - 1;
			add[l] = add[h];
			addHead(l);
		}
		num[i] = num[i] + add[h] + v;
		father[i] = i;
		size[i] = 1;
		add[i] = 0;
		addHead(i);
		u(l, i);
	}

	/**
	 * A2 x v : x所在的集合所有节点的权值增加v
	 * @param i 节点x
	 * @param v 增加的值
	 */
	public static void a2(int i, int v) {
		int h = find(i);
		minusHead(h);
		add[h] += v;
		addHead(h);
	}

	/**
	 * A3 v   : 所有节点的权值增加v
	 * @param v 增加的值
	 */
	public static void a3(int v) {
		addAll += v;
	}

	/**
	 * F1 x   : 打印x节点的权值
	 * @param i 节点x
	 * @return 节点x的权值
	 */
	public static int f1(int i) {
		return num[i] + add[find(i)] + addAll;
	}

	/**
	 * F2 x   : 打印x所在的集合中，权值最大的节点的权值
	 * @param i 节点x
	 * @return x所在集合中权值最大的节点的权值
	 */
	public static int f2(int i) {
		int h = find(i);
		return num[h] + add[h] + addAll;
	}

	/**
	 * F3     : 打印所有节点中，权值最大的节点的权值
	 * @return 所有节点中权值最大的节点的权值
	 */
	public static int f3() {
		return heads.lastKey() + addAll;
	}

	/**
	 * 主函数
	 * 输入格式：
	 * 第一行包含一个整数n，表示节点数量
	 * 第二行包含n个整数，表示每个节点的初始权值
	 * 第三行包含一个整数m，表示操作数量
	 * 接下来m行，每行包含一个操作：
	 *   U x y  : x所在的集合和y所在的集合合并
	 *   A1 x v : x节点的权值增加v
	 *   A2 x v : x所在的集合所有节点的权值增加v
	 *   A3 v   : 所有节点的权值增加v
	 *   F1 x   : 打印x节点的权值
	 *   F2 x   : 打印x所在的集合中，权值最大的节点的权值
	 *   F3     : 打印所有节点中，权值最大的节点的权值
	 * 输出格式：
	 * 对于F1、F2、F3操作，输出相应的结果
	 */
	public static void main(String[] args) {
		ReaderWriter io = new ReaderWriter();
		n = io.nextInt();
		for (int i = 1; i <= n; i++) {
			num[i] = io.nextInt();
		}
		prepare();
		m = io.nextInt();
		String op;
		for (int i = 1, x, y; i <= m; i++) {
			op = io.next();
			if (op.equals("F3")) {
				io.println(f3());
			} else {
				x = io.nextInt();
				if (op.equals("U")) {
					y = io.nextInt();
					u(x, y);
				} else if (op.equals("A1")) {
					y = io.nextInt();
					a1(x, y);
				} else if (op.equals("A2")) {
					y = io.nextInt();
					a2(x, y);
				} else if (op.equals("A3")) {
					a3(x);
				} else if (op.equals("F1")) {
					io.println(f1(x));
				} else {
					io.println(f2(x));
				}
			}
		}
		io.flush();
		io.close();
	}

	// 读写工具类
	public static class ReaderWriter extends PrintWriter {
		byte[] buf = new byte[1 << 10];
		int bId = 0, bSize = 0;
		boolean eof = false;

		public ReaderWriter() {
			super(System.out);
		}

		private byte getByte() {
			if (bId >= bSize) {
				try {
					bSize = System.in.read(buf);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (bSize == -1)
					eof = true;
				bId = 0;
			}
			return buf[bId++];
		}

		byte c;

		public boolean hasNext() {
			if (eof)
				return false;
			while ((c = getByte()) <= 32 && !eof)
				;
			if (eof)
				return false;
			bId--;
			return true;
		}

		public String next() {
			if (!hasNext())
				return null;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			StringBuilder sb = new StringBuilder();
			while (c > 32) {
				sb.append((char) c);
				c = getByte();
			}
			return sb.toString();
		}

		public int nextInt() {
			if (!hasNext())
				return Integer.MIN_VALUE;
			int sign = 1;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			if (c == '-') {
				sign = -1;
				c = getByte();
			}
			int val = 0;
			while (c >= '0' && c <= '9') {
				val = val * 10 + (c - '0');
				c = getByte();
			}
			bId--;
			return val * sign;
		}

		public long nextLong() {
			if (!hasNext())
				return Long.MIN_VALUE;
			int sign = 1;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			if (c == '-') {
				sign = -1;
				c = getByte();
			}
			long val = 0;
			while (c >= '0' && c <= '9') {
				val = val * 10 + (c - '0');
				c = getByte();
			}
			bId--;
			return val * sign;
		}

		public double nextDouble() {
			if (!hasNext())
				return Double.NaN;
			int sign = 1;
			byte c = getByte();
			while (c <= 32)
				c = getByte();
			if (c == '-') {
				sign = -1;
				c = getByte();
			}
			double val = 0;
			while (c >= '0' && c <= '9') {
				val = val * 10 + (c - '0');
				c = getByte();
			}
			if (c == '.') {
				double mul = 1;
				c = getByte();
				while (c >= '0' && c <= '9') {
					mul *= 0.1;
					val += (c - '0') * mul;
					c = getByte();
				}
			}
			bId--;
			return val * sign;
		}
	}

}