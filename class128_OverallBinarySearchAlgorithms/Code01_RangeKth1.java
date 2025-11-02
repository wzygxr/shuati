package class168;

// 区间内第k小，第一种写法，java版
// 给定一个长度为n的数组，接下来有m条查询，格式如下
// 查询 l r k : 打印[l..r]范围内第k小的值
// 1 <= n、m <= 2 * 10^5
// 1 <= 数组中的数字 <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P3834
// 本题是讲解157，可持久化线段树模版题，现在作为整体二分的模版题
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
//
// 整体二分算法详解：
// 1. 算法思想：将所有查询一起进行二分，而不是对每个查询单独二分
// 2. 适用场景：多个具有二分性质的查询
// 3. 时间复杂度：O((N+M) * logN * log(maxValue))
// 4. 空间复杂度：O(N+M)
// 5. 核心思想：
//    - 离线处理所有查询
//    - 对值域进行二分
//    - 使用数据结构维护状态
//    - 根据结果将查询分类递归处理

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code01_RangeKth1 {

	public static int MAXN = 200001;
	public static int n, m;

	// 位置i，数值v
	public static int[][] arr = new int[MAXN][2];

	// 查询
	public static int[] qid = new int[MAXN];
	public static int[] l = new int[MAXN];
	public static int[] r = new int[MAXN];
	public static int[] k = new int[MAXN];

	// 树状数组
	public static int[] tree = new int[MAXN];

	// 整体二分
	public static int[] lset = new int[MAXN];
	public static int[] rset = new int[MAXN];

	// 查询的答案
	public static int[] ans = new int[MAXN];

	// 树状数组中的lowbit
	public static int lowbit(int i) {
		return i & -i;
	}

	// 树状数组中增加i位置的词频
	public static void add(int i, int v) {
		while (i <= n) {
			tree[i] += v;
			i += lowbit(i);
		}
	}

	// 树状数组中查询[1~i]范围的词频累加和
	public static int sum(int i) {
		int ret = 0;
		while (i > 0) {
			ret += tree[i];
			i -= lowbit(i);
		}
		return ret;
	}

	// 树状数组中查询[l~r]范围的词频累加和
	public static int query(int l, int r) {
		return sum(r) - sum(l - 1);
	}

	// 整体二分的第一种写法
	// 问题范围[ql..qr]，答案范围[vl..vr]，答案范围的每个下标都是数字的排名
	public static void compute(int ql, int qr, int vl, int vr) {
		if (ql > qr) {
			return;
		}
		if (vl == vr) {
			// 找到答案，记录结果
			for (int i = ql; i <= qr; i++) {
				ans[qid[i]] = arr[vl][1];
			}
		} else {
			// 修改数据状况
			int mid = (vl + vr) / 2;
			// 将值域小于等于mid的数加入树状数组
			for (int i = vl; i <= mid; i++) {
				add(arr[i][0], 1);
			}
			// 检查每个问题并划分左右
			int lsiz = 0, rsiz = 0;
			for (int i = ql; i <= qr; i++) {
				int id = qid[i];
				// 查询区间内小于等于arr[mid][1]的数的个数
				int satisfy = query(l[id], r[id]);
				if (satisfy >= k[id]) {
					// 说明第k小的数在左半部分
					lset[++lsiz] = id;
				} else {
					// 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
					k[id] -= satisfy;
					rset[++rsiz] = id;
				}
			}
			// 重新排列查询顺序
			for (int i = 1; i <= lsiz; i++) {
				qid[ql + i - 1] = lset[i];
			}
			for (int i = 1; i <= rsiz; i++) {
				qid[ql + lsiz + i - 1] = rset[i];
			}
			// 撤回数据状况
			for (int i = vl; i <= mid; i++) {
				add(arr[i][0], -1);
			}
			// 左右两侧各自递归
			compute(ql, ql + lsiz - 1, vl, mid);
			compute(ql + lsiz, qr, mid + 1, vr);
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i][0] = i;
			arr[i][1] = in.nextInt();
		}
		for (int i = 1; i <= m; i++) {
			qid[i] = i;
			l[i] = in.nextInt();
			r[i] = in.nextInt();
			k[i] = in.nextInt();
		}
		// 离散化，按数值排序
		Arrays.sort(arr, 1, n + 1, (a, b) -> a[1] - b[1]);
		compute(1, m, 1, n);
		for (int i = 1; i <= m; i++) {
			out.println(ans[i]);
		}
		out.flush();
		out.close();
	}

	// 读写工具类
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
				if (len <= 0)
					return -1;
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

}