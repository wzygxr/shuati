package class157;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * 范围修改的可持久化线段树，经典的方式，java版
 * 
 * 题目来源: SPOJ TTM - To the moon
 * 题目链接: https://www.spoj.com/problems/TTM/
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，时间戳t=0，arr认为是0版本的数组
 * 一共有m条操作，每条操作为如下四种类型中的一种
 * C x y z : 当前时间戳t版本的数组，[x..y]范围每个数字增加z，得到t+1版本数组，并且t++
 * Q x y   : 当前时间戳t版本的数组，打印[x..y]范围累加和
 * H x y z : z版本的数组，打印[x..y]范围的累加和
 * B x     : 当前时间戳t设置成x
 * 
 * 解题思路:
 * 使用可持久化线段树解决带历史版本的区间修改问题。
 * 1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
 * 2. 使用懒惰标记技术处理区间修改
 * 3. 通过clone函数实现节点的复制，确保历史版本的完整性
 * 4. 在需要下传懒惰标记时，先复制子节点再进行操作
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n、m <= 10^5
 * -10^9 <= arr[i] <= +10^9
 * 
 * 示例:
 * 输入:
 * 5 10
 * 5 6 7 8 9
 * Q 1 5
 * C 2 4 10
 * Q 1 5
 * H 1 5 0
 * B 3
 * Q 1 5
 * C 1 5 20
 * Q 1 5
 * H 1 5 3
 * Q 1 5
 * 
 * 输出:
 * 35
 * 55
 * 35
 * 55
 * 75
 * 55
 */
public class Code03_RangePersistentClassic1 {

	public static int MAXN = 100001;

	public static int MAXT = MAXN * 70;

	public static int n, m, t = 0;

	public static int[] arr = new int[MAXN];

	public static int[] root = new int[MAXN];

	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	// 累加和信息
	public static long[] sum = new long[MAXT];

	// 懒更新信息，范围增加的懒更新
	public static long[] add = new long[MAXT];

	public static int cnt = 0;

	/**
	 * 克隆节点
	 * @param i 要克隆的节点编号
	 * @return 新节点编号
	 */
	public static int clone(int i) {
		int rt = ++cnt;
		left[rt] = left[i];
		right[rt] = right[i];
		sum[rt] = sum[i];
		add[rt] = add[i];
		return rt;
	}

	/**
	 * 更新节点信息
	 * @param i 节点编号
	 */
	public static void up(int i) {
		sum[i] = sum[left[i]] + sum[right[i]];
	}

	/**
	 * 懒更新操作
	 * @param i 节点编号
	 * @param v 增加的值
	 * @param n 区间长度
	 */
	public static void lazy(int i, long v, int n) {
		sum[i] += v * n;
		add[i] += v;
	}

	/**
	 * 下传懒更新标记
	 * @param i 节点编号
	 * @param ln 左子区间长度
	 * @param rn 右子区间长度
	 */
	public static void down(int i, int ln, int rn) {
		if (add[i] != 0) {
			left[i] = clone(left[i]);
			right[i] = clone(right[i]);
			lazy(left[i], add[i], ln);
			lazy(right[i], add[i], rn);
			add[i] = 0;
		}
	}

	/**
	 * 建立线段树
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 根节点编号
	 */
	public static int build(int l, int r) {
		int rt = ++cnt;
		add[rt] = 0;
		if (l == r) {
			sum[rt] = arr[l];
		} else {
			int mid = (l + r) / 2;
			left[rt] = build(l, mid);
			right[rt] = build(mid + 1, r);
			up(rt);
		}
		return rt;
	}

	/**
	 * 区间增加操作
	 * @param jobl 操作区间左端点
	 * @param jobr 操作区间右端点
	 * @param jobv 增加的值
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param i 当前节点编号
	 * @return 新节点编号
	 */
	public static int add(int jobl, int jobr, long jobv, int l, int r, int i) {
		int rt = clone(i);
		if (jobl <= l && r <= jobr) {
			lazy(rt, jobv, r - l + 1);
		} else {
			int mid = (l + r) / 2;
			down(rt, mid - l + 1, r - mid);
			if (jobl <= mid) {
				left[rt] = add(jobl, jobr, jobv, l, mid, left[rt]);
			}
			if (jobr > mid) {
				right[rt] = add(jobl, jobr, jobv, mid + 1, r, right[rt]);
			}
			up(rt);
		}
		return rt;
	}

	/**
	 * 区间查询操作
	 * @param jobl 查询区间左端点
	 * @param jobr 查询区间右端点
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param i 当前节点编号
	 * @return 区间和
	 */
	public static long query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		int mid = (l + r) / 2;
		down(i, mid - l + 1, r - mid);
		long ans = 0;
		if (jobl <= mid) {
			ans += query(jobl, jobr, l, mid, left[i]);
		}
		if (jobr > mid) {
			ans += query(jobl, jobr, mid + 1, r, right[i]);
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		root[0] = build(1, n);
		String op;
		for (int i = 1, x, y, z; i <= m; i++) {
			op = in.next();
			if (op.equals("C")) {
				x = in.nextInt();
				y = in.nextInt();
				z = in.nextInt();
				root[t + 1] = add(x, y, z, 1, n, root[t]);
				t++;
			} else if (op.equals("Q")) {
				x = in.nextInt();
				y = in.nextInt();
				out.write(query(x, y, 1, n, root[t]) + "\n");
			} else if (op.equals("H")) {
				x = in.nextInt();
				y = in.nextInt();
				z = in.nextInt();
				out.write(query(x, y, 1, n, root[z]) + "\n");
			} else {
				x = in.nextInt();
				t = x;
			}
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

		public boolean hasNext() throws IOException {
			while (hasNextByte()) {
				byte b = buffer[ptr];
				if (!isWhitespace(b))
					return true;
				ptr++;
			}
			return false;
		}

		public String next() throws IOException {
			byte c;
			do {
				c = readByte();
				if (c == -1)
					return null;
			} while (c <= ' ');
			StringBuilder sb = new StringBuilder();
			while (c > ' ') {
				sb.append((char) c);
				c = readByte();
			}
			return sb.toString();
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

		public double nextDouble() throws IOException {
			double num = 0, div = 1;
			byte b = readByte();
			while (isWhitespace(b))
				b = readByte();
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			while (!isWhitespace(b) && b != '.' && b != -1) {
				num = num * 10 + (b - '0');
				b = readByte();
			}
			if (b == '.') {
				b = readByte();
				while (!isWhitespace(b) && b != -1) {
					num += (b - '0') / (div *= 10);
					b = readByte();
				}
			}
			return minus ? -num : num;
		}

		private boolean isWhitespace(byte b) {
			return b == ' ' || b == '\n' || b == '\r' || b == '\t';
		}
	}

}