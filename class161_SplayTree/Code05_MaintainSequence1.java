package class153;

/**
 * Splay树实现 - 维护数列问题解决方案
 * 【题目来源】洛谷 P2042
 * 【题目链接】https://www.luogu.com.cn/problem/P2042
 * 【算法分析】
 * 使用Splay树维护序列信息，支持复杂的区间操作（插入、删除、翻转、区间更新、区间查询等）
 * 通过懒标记技术优化区间操作，避免对每个节点逐一修改
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 【空间复杂度】O(n)
 * 【实现特点】
 * 1. 使用懒标记技术优化区间操作
 * 2. 维护区间和、区间最大子段和等复杂信息
 * 3. 使用空间回收机制避免空间浪费
 */

/**
 * 维护数列问题
 * 【题目大意】
 * 初始时给定一个数列，实现如下六种操作：
 * 1. INSERT posi tot ...  : 在第posi个数字之后，插入长度为tot的数组，由...代表
 * 2. DELETE posi tot      : 从第posi个数字开始，删除长度为tot的部分
 * 3. MAKE-SAME posi tot c : 从第posi个数字开始，长度为tot的部分，值都设置成c
 * 4. REVERSE posi tot     : 从第posi个数字开始，翻转长度为tot的部分
 * 5. GET-SUM posi tot     : 从第posi个数字开始，查询长度为tot的部分的累加和
 * 6. MAX-SUM              : 查询整个数列中，非空子数组的最大累加和
 * 
 * 【解题思路】
 * 使用Splay树维护序列信息，通过懒标记技术优化区间操作
 * 1. 使用Splay树维护序列的中序遍历顺序
 * 2. 通过懒标记技术优化区间操作（翻转、更新等）
 * 3. 维护区间和、区间最大子段和等复杂信息
 * 4. 使用空间回收机制避免空间浪费
 * 
 * 【关键技巧】
 * 1. 使用哨兵节点简化边界处理
 * 2. 通过find+双重splay操作隔离目标区间
 * 3. 懒标记的下传和更新机制
 * 4. 空间回收机制
 */

// 测试链接 : https://www.luogu.com.cn/problem/P2042
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.InputMismatchException;

public class Code05_MaintainSequence1 {

	/**
	 * 【空间配置】预分配的最大节点数量
	 * 设置为500005是因为任何时刻数列中最多有5 * 10^5个数
	 */
	public static int MAXN = 500005;

	/**
	 * 【边界值设置】用于处理区间最大子段和的边界情况
	 * 设置为1000000001是为了处理负数情况
	 */
	public static int INF = 1000000001;

	/**
	 * 【树结构标识】
	 * head: 根节点索引
	 */
	public static int head = 0;

	/**
	 * 【辅助数组】
	 * arr: 用于构建Splay树的临时数组
	 * num: 节点存储的值
	 * father: 父节点索引
	 * left: 左子节点索引
	 * right: 右子节点索引
	 * size: 以该节点为根的子树大小
	 */
	public static int[] arr = new int[MAXN];
	public static int[] num = new int[MAXN];
	public static int[] father = new int[MAXN];
	public static int[] left = new int[MAXN];
	public static int[] right = new int[MAXN];
	public static int[] size = new int[MAXN];

	/**
	 * 【空间管理】用于空间回收和分配
	 * space: 可用空间编号数组
	 * si: 当前可用空间数量
	 */
	public static int[] space = new int[MAXN];
	public static int si;

	/**
	 * 【区间信息维护】为了支持区间操作而维护的额外信息
	 * sum: 区间和
	 * all: 区间最大子段和（不能为空）
	 * pre: 区间前缀最大和（可以为空）
	 * suf: 区间后缀最大和（可以为空）
	 */
	public static int[] sum = new int[MAXN];
	public static int[] all = new int[MAXN];
	public static int[] pre = new int[MAXN];
	public static int[] suf = new int[MAXN];

	/**
	 * 【懒标记技术】延迟传播标记，用于优化区间操作
	 * update: 区间是否重新设了值
	 * change: 如果区间重新设了值，设置成了什么
	 * reverse: 区间是否发生了翻转
	 */
	public static boolean[] update = new boolean[MAXN];
	public static int[] change = new int[MAXN];
	public static boolean[] reverse = new boolean[MAXN];

	/**
	 * 【自底向上维护】更新节点信息
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 要更新的节点索引
	 */
	public static void up(int i) {
		int l = left[i], r = right[i];
		// 更新子树大小
		size[i] = size[l] + size[r] + 1;
		// 更新区间和
		sum[i] = sum[l] + sum[r] + num[i];
		// 更新区间最大子段和
		all[i] = Math.max(Math.max(all[l], all[r]), suf[l] + num[i] + pre[r]);
		// 更新区间前缀最大和
		pre[i] = Math.max(pre[l], sum[l] + num[i] + pre[r]);
		// 更新区间后缀最大和
		suf[i] = Math.max(suf[r], suf[l] + num[i] + sum[r]);
	}

	/**
	 * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
	 * 时间复杂度: O(1)
	 * @param i 需要判断的节点索引
	 * @return 1表示右子节点，0表示左子节点
	 */
	public static int lr(int i) {
		return right[father[i]] == i ? 1 : 0;
	}

	/**
	 * 【核心旋转操作】将节点i旋转至其父节点的位置
	 * 这是Splay树维护平衡的基本操作
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 需要旋转的节点索引
	 */
	public static void rotate(int i) {
		int f = father[i];     // 父节点索引
		int g = father[f];     // 祖父节点索引
		int soni = lr(i);      // 当前节点是父节点的左子还是右子
		int sonf = lr(f);      // 父节点是祖父节点的左子还是右子
		
		// 处理父节点与当前节点的子节点关系
		if (soni == 1) {       // 右子节点，右旋
			right[f] = left[i];
			if (right[f] != 0) {
				father[right[f]] = f;
			}
			left[i] = f;
		} else {               // 左子节点，左旋
			left[f] = right[i];
			if (left[f] != 0) {
				father[left[f]] = f;
			}
			right[i] = f;
		}
		
		// 处理祖父节点与当前节点的关系
		if (g != 0) {
			if (sonf == 1) {
				right[g] = i;
			} else {
				left[g] = i;
			}
		}
		
		// 更新父指针
		father[f] = i;
		father[i] = g;
		
		// 【重要】更新节点信息，注意顺序：先更新f，再更新i
		up(f);
		up(i);
	}

	/**
	 * 【核心伸展操作】将节点i旋转到goal的子节点位置
	 * 如果goal为0，则将i旋转到根节点
	 * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
	 * 时间复杂度: 均摊O(log n)，最坏情况O(n)
	 * 空间复杂度: O(1)
	 * @param i 需要旋转的节点索引
	 * @param goal 目标父节点索引
	 */
	public static void splay(int i, int goal) {
		int f = father[i], g = father[f];
		while (f != goal) {
			// 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
			// 这是保证Splay树均摊时间复杂度的关键
			if (g != goal) {
				// 如果父节点和当前节点在同侧，先旋转父节点（Zig-Zig）
				// 否则直接旋转当前节点（Zig-Zag）
				if (lr(i) == lr(f)) {
					rotate(f);
				} else {
					rotate(i);
				}
			}
			rotate(i);
			f = father[i];
			g = father[f];
		}
		
		// 如果旋转到根节点，更新根节点指针
		if (goal == 0) {
			head = i;
		}
	}

	/**
	 * 【懒标记设置】设置区间更新标记
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 要设置标记的节点索引
	 * @param val 更新的值
	 */
	public static void setValue(int i, int val) {
		if (i != 0) {
			// 设置更新标记
			update[i] = true;
			change[i] = val;
			// 更新当前节点的值和区间信息
			num[i] = val;
			sum[i] = size[i] * val;
			all[i] = Math.max(sum[i], val);
			pre[i] = Math.max(sum[i], 0);
			suf[i] = Math.max(sum[i], 0);
		}
	}

	/**
	 * 【懒标记设置】设置区间翻转标记
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 要设置标记的节点索引
	 */
	public static void setReverse(int i) {
		if (i != 0) {
			// 翻转标记
			int tmp = pre[i];
			pre[i] = suf[i];
			suf[i] = tmp;
			reverse[i] = !reverse[i];
		}
	}

	/**
	 * 【懒标记下传】下传懒标记到子节点
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 要下传懒标记的节点索引
	 */
	public static void down(int i) {
		if (i == 0) return;
		
		// 处理区间更新标记
		if (update[i]) {
			setValue(left[i], change[i]);
			setValue(right[i], change[i]);
			update[i] = false; // 清除标记
		}
		
		// 处理区间翻转标记
		if (reverse[i]) {
			int tmp = left[i];
			left[i] = right[i];
			right[i] = tmp;
			setReverse(left[i]);
			setReverse(right[i]);
			reverse[i] = false; // 清除标记
		}
	}

	/**
	 * 【节点初始化】从空间池中取出一个节点并初始化
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param val 节点的初始值
	 * @return 初始化后的节点索引
	 */
	public static int init(int val) {
		// 从空间池中取出一个节点
		int i = space[si--];
		// 初始化节点信息
		size[i] = 1;
		num[i] = sum[i] = all[i] = val;
		pre[i] = suf[i] = Math.max(val, 0);
		father[i] = left[i] = right[i] = 0;
		update[i] = reverse[i] = false;
		return i;
	}

	/**
	 * 【构建树】根据数组构建Splay树
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(log n)
	 * @param l 数组左边界
	 * @param r 数组右边界
	 * @return 构建的树的根节点索引
	 */
	public static int build(int l, int r) {
		// 取中点作为根节点
		int mid = (l + r) / 2;
		int root = init(arr[mid]);
		
		// 递归构建左右子树
		if (l < mid) {
			left[root] = build(l, mid - 1);
			father[left[root]] = root;
		}
		if (mid < r) {
			right[root] = build(mid + 1, r);
			father[right[root]] = root;
		}
		
		// 更新节点信息
		up(root);
		return root;
	}

	/**
	 * 【查找操作】根据中序遍历的排名查找节点
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(1)
	 * @param rank 要查找的节点的中序遍历排名
	 * @return 找到的节点索引，未找到返回0
	 */
	public static int find(int rank) {
		int i = head;
		while (i != 0) {
			// 【懒标记处理】在访问子树前必须下传懒标记
			// 这是保证操作正确性的关键步骤
			down(i);
			
			if (size[left[i]] + 1 == rank) {
				return i;
			} else if (size[left[i]] >= rank) {
				i = left[i];
			} else {
				rank -= size[left[i]] + 1;
				i = right[i];
			}
		}
		return 0;
	}

	/**
	 * 【插入操作】在指定排名位置插入n个元素
	 * 时间复杂度: 均摊O(n log n)
	 * 空间复杂度: O(n)
	 * @param rank 插入位置的排名
	 * @param n 插入元素的数量
	 */
	public static void insert(int rank, int n) {
		if (rank == 0) {
			// 在最前面插入
			head = build(1, n);
		} else {
			// 找到插入位置的前驱和后继
			int l = find(rank);
			int r = find(rank + 1);
			// 将l旋转到根，r旋转到l的右子节点
			splay(l, 0);
			splay(r, l);
			// 构建新子树并连接
			left[r] = build(1, n);
			father[left[r]] = r;
			// 更新节点信息
			up(r);
			up(l);
		}
	}

	/**
	 * 【空间回收】回收以i为根的子树所占用的空间
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(log n)
	 * @param i 要回收的子树的根节点索引
	 */
	public static void recycle(int i) {
		if (i != 0) {
			// 将节点编号放回空间池
			space[++si] = i;
			// 递归回收左右子树
			recycle(left[i]);
			recycle(right[i]);
		}
	}

	/**
	 * 【删除操作】删除从指定排名开始的n个元素
	 * 时间复杂度: 均摊O(log n)
	 * 空间复杂度: O(1)
	 * @param rank 删除起始位置的排名
	 * @param n 删除元素的数量
	 */
	public static void delete(int rank, int n) {
		// 找到删除区间的前驱和后继
		int l = find(rank - 1);
		int r = find(rank + n);
		// 将l旋转到根，r旋转到l的右子节点
		splay(l, 0);
		splay(r, l);
		// 回收要删除的子树
		recycle(left[r]);
		// 断开连接
		left[r] = 0;
		// 更新节点信息
		up(r);
		up(l);
	}

	/**
	 * 【区间重置操作】将从指定排名开始的n个元素都设置为val
	 * 时间复杂度: 均摊O(log n)
	 * 空间复杂度: O(1)
	 * @param rank 重置起始位置的排名
	 * @param n 重置元素的数量
	 * @param val 重置的值
	 */
	public static void reset(int rank, int n, int val) {
		// 找到重置区间的前驱和后继
		int l = find(rank - 1);
		int r = find(rank + n);
		// 将l旋转到根，r旋转到l的右子节点
		splay(l, 0);
		splay(r, l);
		// 设置更新标记
		setValue(left[r], val);
		// 更新节点信息
		up(r);
		up(l);
	}

	/**
	 * 【区间翻转操作】将从指定排名开始的n个元素顺序翻转
	 * 时间复杂度: 均摊O(log n)
	 * 空间复杂度: O(1)
	 * @param rank 翻转起始位置的排名
	 * @param n 翻转元素的数量
	 */
	public static void reverse(int rank, int n) {
		// 找到翻转区间的前驱和后继
		int l = find(rank - 1);
		int r = find(rank + n);
		// 将l旋转到根，r旋转到l的右子节点
		splay(l, 0);
		splay(r, l);
		// 设置翻转标记
		setReverse(left[r]);
		// 更新节点信息
		up(r);
		up(l);
	}

	/**
	 * 【区间查询操作】查询从指定排名开始的n个元素的和
	 * 时间复杂度: 均摊O(log n)
	 * 空间复杂度: O(1)
	 * @param rank 查询起始位置的排名
	 * @param n 查询元素的数量
	 * @return 区间和
	 */
	public static int querySum(int rank, int n) {
		// 找到查询区间的前驱和后继
		int l = find(rank - 1);
		int r = find(rank + n);
		// 将l旋转到根，r旋转到l的右子节点
		splay(l, 0);
		splay(r, l);
		// 返回区间和
		return sum[left[r]];
	}

	/**
	 * 【全局查询操作】查询整个数列中非空子数组的最大累加和
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @return 全局最大子段和
	 */
	public static int queryMax() {
		return all[head];
	}

	/**
	 * 【主函数】处理输入输出和操作调用
	 * 【输入输出优化】使用FastReader和FastWriter提高IO效率
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		FastReader in = new FastReader(System.in);
		FastWriter out = new FastWriter(System.out);
		int n = in.readInt();
		int m = in.readInt();
		
		// 所有可用空间编号，进入space数组
		si = MAXN - 1;
		for (int i = 1; i <= si; i++) {
			space[i] = i;
		}
		
		// 这里很重要，一方面准备好最左和最右的准备值
		// 另一方面设置all[0] = 极小值
		// 表示没有范围时，子数组最大累加和为极小值，因为不能为空
		arr[1] = arr[n + 2] = all[0] = -INF;
		for (int i = 1, j = 2; i <= n; i++, j++) {
			arr[j] = in.readInt();
		}
		
		// 建立初始树
		insert(0, n + 2);
		
		String op;
		for (int i = 1, posi, tot, c; i <= m; i++) {
			op = in.readString();
			if (op.equals("MAX-SUM")) {
				out.println(queryMax());
			} else {
				// 因为有最左的准备值，所以位置要后移一位
				posi = in.readInt() + 1;
				tot = in.readInt();
				if (op.equals("INSERT")) {
					for (int j = 1; j <= tot; j++) {
						arr[j] = in.readInt();
					}
					insert(posi, tot);
				} else if (op.equals("DELETE")) {
					delete(posi, tot);
				} else if (op.equals("MAKE-SAME")) {
					c = in.readInt();
					reset(posi, tot, c);
				} else if (op.equals("REVERSE")) {
					reverse(posi, tot);
				} else {
					out.println(querySum(posi, tot));
				}
			}
		}
		out.flush();
		out.close();
	}

	// 快读
	public static class FastReader {
		InputStream is;
		private byte[] inbuf = new byte[1024];
		public int lenbuf = 0;
		public int ptrbuf = 0;

		public FastReader(final InputStream is) {
			this.is = is;
		}

		public String readString() {
			char cur;
			do {
				cur = (char) readByte();
			} while (cur == ' ' || cur == '\n');
			StringBuilder builder = new StringBuilder();
			while (cur != ' ' && cur != '\n') {
				builder.append(cur);
				cur = (char) readByte();
			}
			return builder.toString();
		}

		public int readByte() {
			if (lenbuf == -1) {
				throw new InputMismatchException();
			}
			if (ptrbuf >= lenbuf) {
				ptrbuf = 0;
				try {
					lenbuf = is.read(inbuf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (lenbuf <= 0) {
					return -1;
				}
			}
			return inbuf[ptrbuf++];
		}

		public int readInt() {
			return (int) readLong();
		}

		public long readLong() {
			long num = 0;
			int b;
			boolean minus = false;
			while ((b = readByte()) != -1 && !((b >= '0' && b <= '9') || b == '-'))
				;
			if (b == '-') {
				minus = true;
				b = readByte();
			}

			while (true) {
				if (b >= '0' && b <= '9') {
					num = num * 10 + (b - '0');
				} else {
					return minus ? -num : num;
				}
				b = readByte();
			}
		}
	}

	// 快写
	public static class FastWriter {
		private static final int BUF_SIZE = 1 << 13;
		private final byte[] buf = new byte[BUF_SIZE];
		private OutputStream out;
		private Writer writer;
		private int ptr = 0;

		public FastWriter(Writer writer) {
			this.writer = new BufferedWriter(writer);
			out = new ByteArrayOutputStream();
		}

		public FastWriter(OutputStream os) {
			this.out = os;
		}

		public FastWriter(String path) {
			try {
				this.out = new FileOutputStream(path);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("FastWriter");
			}
		}

		public FastWriter write(byte b) {
			buf[ptr++] = b;
			if (ptr == BUF_SIZE) {
				innerflush();
			}
			return this;
		}

		public FastWriter write(String s) {
			s.chars().forEach(c -> {
				buf[ptr++] = (byte) c;
				if (ptr == BUF_SIZE) {
					innerflush();
				}
			});
			return this;
		}

		private static int countDigits(long l) {
			if (l >= 1000000000000000000L) {
				return 19;
			}
			if (l >= 100000000000000000L) {
				return 18;
			}
			if (l >= 10000000000000000L) {
				return 17;
			}
			if (l >= 1000000000000000L) {
				return 16;
			}
			if (l >= 100000000000000L) {
				return 15;
			}
			if (l >= 10000000000000L) {
				return 14;
			}
			if (l >= 1000000000000L) {
				return 13;
			}
			if (l >= 100000000000L) {
				return 12;
			}
			if (l >= 10000000000L) {
				return 11;
			}
			if (l >= 1000000000L) {
				return 10;
			}
			if (l >= 100000000L) {
				return 9;
			}
			if (l >= 10000000L) {
				return 8;
			}
			if (l >= 1000000L) {
				return 7;
			}
			if (l >= 100000L) {
				return 6;
			}
			if (l >= 10000L) {
				return 5;
			}
			if (l >= 1000L) {
				return 4;
			}
			if (l >= 100L) {
				return 3;
			}
			if (l >= 10L) {
				return 2;
			}
			return 1;
		}

		public FastWriter write(long x) {
			if (x == Long.MIN_VALUE) {
				return write("" + x);
			}
			if (ptr + 21 >= BUF_SIZE) {
				innerflush();
			}
			if (x < 0) {
				write((byte) '-');
				x = -x;
			}
			int d = countDigits(x);
			for (int i = ptr + d - 1; i >= ptr; i--) {
				buf[i] = (byte) ('0' + x % 10);
				x /= 10;
			}
			ptr += d;
			return this;
		}

		public FastWriter writeln(long x) {
			return write(x).writeln();
		}

		public FastWriter writeln() {
			return write((byte) '\n');
		}

		private void innerflush() {
			try {
				out.write(buf, 0, ptr);
				ptr = 0;
			} catch (IOException e) {
				throw new RuntimeException("innerflush");
			}
		}

		public void flush() {
			innerflush();
			try {
				if (writer != null) {
					writer.write(((ByteArrayOutputStream) out).toString());
					out = new ByteArrayOutputStream();
					writer.flush();
				} else {
					out.flush();
				}
			} catch (IOException e) {
				throw new RuntimeException("flush");
			}
		}

		public FastWriter println(long x) {
			return writeln(x);
		}

		public void close() {
			flush();
			try {
				out.close();
			} catch (Exception e) {
			}
		}

	}

}