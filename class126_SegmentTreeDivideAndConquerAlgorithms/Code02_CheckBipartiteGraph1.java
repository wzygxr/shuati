package class166;

/**
 * 洛谷 P5787 二分图/【模板】线段树分治 - Java实现
 * 
 * 题目来源: 洛谷
 * 题目链接: https://www.luogu.com.cn/problem/P5787
 * 题目描述: 
 *   给定一个n个节点的图，每条边有一个存在时间区间[L,R]
 *   对于每个时间点，判断当前图是否为二分图
 * 
 * 解题思路:
 *   使用扩展域并查集来判断二分图，结合线段树分治处理时间区间
 *   1. 对于每个节点x，创建两个节点：x和x+n
 *   2. 如果x和y之间有边，则连接x和y+n，y和x+n
 *   3. 如果x和x+n在同一个连通分量中，则不是二分图
 *   4. 使用线段树分治处理时间区间
 * 
 * 时间复杂度: O((n + m) log k)
 * 空间复杂度: O(n + m)
 * 
 * 是否为最优解: 是
 *   这是处理动态二分图判定问题的经典解法
 * 
 * 工程化考量:
 *   1. 使用扩展域并查集判断二分图
 *   2. 线段树分治处理时间区间
 *   3. 精确回滚保证状态一致性
 * 
 * 适用场景:
 *   1. 动态图二分性维护
 *   2. 离线处理图论问题
 *   3. 需要判断图是否为二分图的场景
 * 
 * 注意事项:
 *   1. 扩展域并查集的正确实现
 *   2. 线段树分治是离线算法
 *   3. 需要正确处理边的存在时间区间
 * 
 * 1 <= n、k <= 10^5
 * 1 <= m <= 2 * 10^5
 * 1 <= x、y <= n
 * 0 <= l、r <= k
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Code02_CheckBipartiteGraph1 {

	public static int MAXN = 100001;
	public static int MAXT = 3000001;
	public static int n, m, k;

	public static int[] father = new int[MAXN << 1];
	public static int[] siz = new int[MAXN << 1];
	public static int[][] rollback = new int[MAXN << 1][2];
	public static int opsize = 0;

	public static int[] head = new int[MAXN << 2];
	public static int[] next = new int[MAXT];
	public static int[] tox = new int[MAXT];
	public static int[] toy = new int[MAXT];
	public static int cnt = 0;

	public static boolean[] ans = new boolean[MAXN];

	public static void addEdge(int i, int x, int y) {
		next[++cnt] = head[i];
		tox[cnt] = x;
		toy[cnt] = y;
		head[i] = cnt;
	}

	public static int find(int i) {
		while (i != father[i]) {
			i = father[i];
		}
		return i;
	}

	public static void union(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		if (siz[fx] < siz[fy]) {
			int tmp = fx;
			fx = fy;
			fy = tmp;
		}
		father[fy] = fx;
		siz[fx] += siz[fy];
		rollback[++opsize][0] = fx;
		rollback[opsize][1] = fy;
	}

	public static void undo() {
		int fx = rollback[opsize][0];
		int fy = rollback[opsize--][1];
		father[fy] = fy;
		siz[fx] -= siz[fy];
	}

	public static void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(i, jobx, joby);
		} else {
			int mid = (l + r) / 2;
			if (jobl <= mid) {
				add(jobl, jobr, jobx, joby, l, mid, i << 1);
			}
			if (jobr > mid) {
				add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
			}
		}
	}

	public static void dfs(int l, int r, int i) {
		boolean check = true;
		int unionCnt = 0;
		for (int ei = head[i]; ei > 0; ei = next[ei]) {
			int x = tox[ei], y = toy[ei], fx = find(x), fy = find(y);
			if (fx == fy) {
				check = false;
				break;
			} else {
				union(x, y + n);
				union(y, x + n);
				unionCnt += 2;
			}
		}
		if (check) {
			if (l == r) {
				ans[l] = true;
			} else {
				int mid = (l + r) / 2;
				dfs(l, mid, i << 1);
				dfs(mid + 1, r, i << 1 | 1);
			}
		} else {
			for (int k = l; k <= r; k++) {
				ans[k] = false;
			}
		}
		for (int k = 1; k <= unionCnt; k++) {
			undo();
		}
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		n = io.nextInt();
		m = io.nextInt();
		k = io.nextInt();
		for (int i = 1; i <= n * 2; i++) {
			father[i] = i;
			siz[i] = 1;
		}
		for (int i = 1, x, y, l, r; i <= m; i++) {
			x = io.nextInt();
			y = io.nextInt();
			l = io.nextInt();
			r = io.nextInt();
			add(l + 1, r, x, y, 1, k, 1);
		}
		dfs(1, k, 1);
		for (int i = 1; i <= k; i++) {
			if (ans[i]) {
				io.write("Yes\n");
			} else {
				io.write("No\n");
			}
		}
		io.flush();
	}

	// 读写工具类
	static class FastIO {
		private final InputStream is;
		private final OutputStream os;
		private final byte[] inbuf = new byte[1 << 16];
		private int lenbuf = 0;
		private int ptrbuf = 0;
		private final StringBuilder outBuf = new StringBuilder();

		public FastIO(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		private int readByte() {
			if (ptrbuf >= lenbuf) {
				ptrbuf = 0;
				try {
					lenbuf = is.read(inbuf);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (lenbuf == -1) {
					return -1;
				}
			}
			return inbuf[ptrbuf++] & 0xff;
		}

		private int skip() {
			int b;
			while ((b = readByte()) != -1) {
				if (b > ' ') {
					return b;
				}
			}
			return -1;
		}

		public int nextInt() {
			int b = skip();
			if (b == -1) {
				throw new RuntimeException("No more integers (EOF)");
			}
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = readByte();
			}
			int val = 0;
			while (b >= '0' && b <= '9') {
				val = val * 10 + (b - '0');
				b = readByte();
			}
			return negative ? -val : val;
		}

		public void write(String s) {
			outBuf.append(s);
		}

		public void writeInt(int x) {
			outBuf.append(x);
		}

		public void writelnInt(int x) {
			outBuf.append(x).append('\n');
		}

		public void flush() {
			try {
				os.write(outBuf.toString().getBytes());
				os.flush();
				outBuf.setLength(0);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
