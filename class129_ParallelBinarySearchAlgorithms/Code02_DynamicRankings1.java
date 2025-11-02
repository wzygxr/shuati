package class169;

/**
 * 带修改的区间第k小问题 - 整体二分算法实现
 * 
 * 问题描述：
 * 给定一个长度为n的数组arr，接下来是m条操作，每种操作是如下两种类型的一种
 * 操作 C x y   : 把x位置的值修改成y
 * 操作 Q x y v : 查询arr[x..y]范围上第v小的值
 * 
 * 约束条件：
 * 1 <= n、m <= 10^5
 * 1 <= 数组中的值 <= 10^9
 * 
 * 解题思路：
 * 使用整体二分算法解决带修改的区间第k小问题。
 * 1. 将所有操作（查询和修改）一起处理
 * 2. 二分答案的值域，利用树状数组维护区间内小于等于mid的元素个数
 * 3. 根据查询结果将操作分为两部分递归处理
 * 
 * 时间复杂度：O((n+m) * log(n) * log(max_value))
 * 空间复杂度：O(n)
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P2617
 * 本题是讲解160，树套树模版题，现在作为带修改的整体二分模版题
 * 提交时请将类名改为"Main"以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code02_DynamicRankings1 {

	public static int MAXN = 100001;
	public static int MAXE = MAXN << 2;
	public static int INF = 1000000001;
	public static int n, m;

	public static int[] arr = new int[MAXN];

	// 事件编号组成的数组
	public static int[] eid = new int[MAXE];
	// op == 1，代表修改事件，x处，值y，效果v
	// op == 2，代表查询事件，[x..y]范围上查询第v小，q表示问题的编号
	public static int[] op = new int[MAXE];
	public static int[] x = new int[MAXE];
	public static int[] y = new int[MAXE];
	public static int[] v = new int[MAXE];
	public static int[] q = new int[MAXE];
	public static int cnte = 0;
	public static int cntq = 0;

	// 树状数组
	public static int[] tree = new int[MAXN];

	// 整体二分
	public static int[] lset = new int[MAXE];
	public static int[] rset = new int[MAXE];

	// 查询的答案
	public static int[] ans = new int[MAXN];

	/**
	 * 计算一个数的lowbit值
	 * @param i 输入的数
	 * @return lowbit值
	 */
	public static int lowbit(int i) {
		return i & -i;
	}

	/**
	 * 在树状数组中给位置i增加v
	 * @param i 位置
	 * @param v 增加的值
	 */
	public static void add(int i, int v) {
		while (i <= n) {
			tree[i] += v;
			i += lowbit(i);
		}
	}

	/**
	 * 计算前缀和[1..i]
	 * @param i 位置
	 * @return 前缀和
	 */
	public static int sum(int i) {
		int ret = 0;
		while (i > 0) {
			ret += tree[i];
			i -= lowbit(i);
		}
		return ret;
	}

	/**
	 * 计算区间和[l..r]
	 * @param l 左端点
	 * @param r 右端点
	 * @return 区间和
	 */
	public static int query(int l, int r) {
		return sum(r) - sum(l - 1);
	}

	/**
	 * 整体二分核心函数
	 * @param el 操作范围的左端点
	 * @param er 操作范围的右端点
	 * @param vl 值域范围的左端点
	 * @param vr 值域范围的右端点
	 */
	public static void compute(int el, int er, int vl, int vr) {
		// 递归边界条件
		if (el > er) {
			return;
		}
		
		// 如果值域范围只有一个值，说明找到了答案
		if (vl == vr) {
			for (int i = el; i <= er; i++) {
				int id = eid[i];
				if (op[id] == 2) {
					// 查询操作，记录答案
					ans[q[id]] = vl;
				}
			}
		} else {
			// 二分中点
			int mid = (vl + vr) >> 1;
			
			// 将操作分为左右两部分
			int lsiz = 0, rsiz = 0;
			for (int i = el; i <= er; i++) {
				int id = eid[i];
				if (op[id] == 1) {
					// 修改操作
					if (y[id] <= mid) {
						// 值小于等于mid，加入左集合
						add(x[id], v[id]);
						lset[++lsiz] = id;
					} else {
						// 值大于mid，加入右集合
						rset[++rsiz] = id;
					}
				} else {
					// 查询操作
					int satisfy = query(x[id], y[id]);
					if (v[id] <= satisfy) {
						// 第k小的数在左半部分
						lset[++lsiz] = id;
					} else {
						// 第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
						v[id] -= satisfy;
						rset[++rsiz] = id;
					}
				}
			}
			
			// 重新排列操作顺序
			for (int i = 1; i <= lsiz; i++) {
				eid[el + i - 1] = lset[i];
			}
			for (int i = 1; i <= rsiz; i++) {
				eid[el + lsiz + i - 1] = rset[i];
			}
			
			// 撤销修改操作的影响
			for (int i = 1; i <= lsiz; i++) {
				int id = lset[i];
				if (op[id] == 1 && y[id] <= mid) {
					add(x[id], -v[id]);
				}
			}
			
			// 递归处理左右两部分
			compute(el, el + lsiz - 1, vl, mid);
			compute(el + lsiz, er, mid + 1, vr);
		}
	}

	/**
	 * 主函数，程序入口
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度n和操作数量m
		n = in.nextInt();
		m = in.nextInt();
		
		// 读取初始数组，并将每个元素作为修改操作记录
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
			op[++cnte] = 1;  // 修改操作
			x[cnte] = i;     // 位置
			y[cnte] = arr[i]; // 值
			v[cnte] = 1;     // 效果
		}
		
		char type;
		// 读取m条操作
		for (int i = 1; i <= m; i++) {
			type = in.nextChar();
			if (type == 'C') {
				// 修改操作 C x y : 把x位置的值修改成y
				int a = in.nextInt(); // 位置x
				int b = in.nextInt(); // 值y
				
				// 将修改操作拆分为两个操作：删除旧值和插入新值
				op[++cnte] = 1;  // 删除旧值
				x[cnte] = a;
				y[cnte] = arr[a]; // 旧值
				v[cnte] = -1;    // 删除效果
				
				op[++cnte] = 1;  // 插入新值
				x[cnte] = a;
				y[cnte] = b;     // 新值
				v[cnte] = 1;     // 插入效果
				
				arr[a] = b; // 更新数组
			} else {
				// 查询操作 Q x y v : 查询arr[x..y]范围上第v小的值
				op[++cnte] = 2;     // 查询操作
				x[cnte] = in.nextInt(); // 左端点
				y[cnte] = in.nextInt(); // 右端点
				v[cnte] = in.nextInt(); // 第k小
				q[cnte] = ++cntq;        // 查询编号
			}
		}
		
		// 初始化事件编号数组
		for (int i = 1; i <= cnte; i++) {
			eid[i] = i;
		}
		
		// 整体二分求解
		compute(1, cnte, 0, INF);
		
		// 输出查询结果
		for (int i = 1; i <= cntq; i++) {
			out.println(ans[i]);
		}
		
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		final private int BUFFER_SIZE = 1 << 16;
		private final InputStream in;
		private final byte[] buffer;
		private int ptr, len;

		public FastReader() {
			in = System.in;
			buffer = new byte[BUFFER_SIZE];
			ptr = len = 0;
		}

		private boolean hasNextByte() throws IOException {
			if (ptr < len)
				return true;
			ptr = 0;
			len = in.read(buffer);
			return len > 0;
		}

		private byte readByte() throws IOException {
			if (!hasNextByte())
				return -1;
			return buffer[ptr++];
		}

		public char nextChar() throws IOException {
			byte c;
			do {
				c = readByte();
				if (c == -1)
					return 0;
			} while (c <= ' ');
			char ans = 0;
			while (c > ' ') {
				ans = (char) c;
				c = readByte();
			}
			return ans;
		}

		public int nextInt() throws IOException {
			int num = 0;
			byte b = readByte();
			while (isWhitespace(b))
				b = readByte();
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			while (!isWhitespace(b) && b != -1) {
				num = num * 10 + (b - '0');
				b = readByte();
			}
			return minus ? -num : num;
		}

		private boolean isWhitespace(byte b) {
			return b == ' ' || b == '\n' || b == '\r' || b == '\t';
		}
	}

}
