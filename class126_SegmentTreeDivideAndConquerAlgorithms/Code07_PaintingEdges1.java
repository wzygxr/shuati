package class166;

/**
 * Codeforces 576E Painting Edges - Java实现
 * 
 * 题目来源: Codeforces
 * 题目链接: https://codeforces.com/problemset/problem/576/E
 * 洛谷链接: https://www.luogu.com.cn/problem/CF576E
 * 题目描述: 
 *   给定一张n个点m条边的无向图，每条边有一个颜色(初始为无色)
 *   一共有q条操作，每次将一条边染为k种颜色之一
 *   要求染完后对于任意i=1...k，当只有颜色为i的边存在时，图都是一张二分图
 *   如果执行操作后图仍合法则执行并打印"YES"，否则不执行并打印"NO"
 * 
 * 解题思路:
 *   使用线段树分治 + 多个扩展域并查集
 *   1. 对每种颜色维护一个扩展域并查集
 *   2. 检查染色后是否满足条件
 *   3. 不满足则撤销操作
 * 
 * 时间复杂度: O(k(n + q) log q)
 * 空间复杂度: O(k(n + q))
 * 
 * 是否为最优解: 是
 *   这是处理边染色二分图问题的高效解法
 * 
 * 工程化考量:
 *   1. 使用多个扩展域并查集分别维护每种颜色的连通性
 *   2. FastIO提高输入输出效率
 *   3. 精确回滚保证状态一致性
 * 
 * 适用场景:
 *   1. 边染色二分图问题
 *   2. 离线处理图论问题
 *   3. 需要维护多种颜色约束的场景
 * 
 * 注意事项:
 *   1. 扩展域并查集的正确实现
 *   2. 线段树分治是离线算法
 *   3. 需要正确处理操作的时间区间
 * 
 * 1 <= n、m、q <= 5 * 10^5
 * 1 <= k <= 50
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Code07_PaintingEdges1 {

	public static int MAXN = 500001;
	public static int MAXK = 51;
	public static int MAXT = 10000001;
	public static int n, m, k, q;

	public static int[] u = new int[MAXN];
	public static int[] v = new int[MAXN];

	public static int[] e = new int[MAXN];
	public static int[] c = new int[MAXN];
	public static int[] post = new int[MAXN];

	public static int[][] father = new int[MAXK][MAXN << 1];
	public static int[][] siz = new int[MAXK][MAXN << 1];
	public static int[][] rollback = new int[MAXN << 1][3];
	public static int opsize = 0;

	// 时间轴线段树的区间上的任务列表
	// 尤其注意qid的设置，课上进行了重点解释
	public static int[] head = new int[MAXN << 2];
	public static int[] next = new int[MAXT];
	public static int[] qid = new int[MAXT];
	public static int cnt = 0;

	// lastColor[i] : 第i号边上次成功涂上的颜色
	public static int[] lastColor = new int[MAXN];

	public static boolean[] ans = new boolean[MAXN];

	public static void addEdge(int i, int qi) {
		next[++cnt] = head[i];
		qid[cnt] = qi;
		head[i] = cnt;
	}

	public static int find(int color, int i) {
		while (i != father[color][i]) {
			i = father[color][i];
		}
		return i;
	}

	public static void union(int color, int x, int y) {
		int fx = find(color, x);
		int fy = find(color, y);
		if (siz[color][fx] < siz[color][fy]) {
			int tmp = fx;
			fx = fy;
			fy = tmp;
		}
		father[color][fy] = fx;
		siz[color][fx] += siz[color][fy];
		rollback[++opsize][0] = color;
		rollback[opsize][1] = fx;
		rollback[opsize][2] = fy;
	}

	public static void undo() {
		int color = rollback[opsize][0];
		int fx = rollback[opsize][1];
		int fy = rollback[opsize--][2];
		father[color][fy] = fy;
		siz[color][fx] -= siz[color][fy];
	}

	public static void add(int jobl, int jobr, int jobq, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(i, jobq);
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				add(jobl, jobr, jobq, l, mid, i << 1);
			}
			if (jobr > mid) {
				add(jobl, jobr, jobq, mid + 1, r, i << 1 | 1);
			}
		}
	}

	public static void dfs(int l, int r, int i) {
		int unionCnt = 0;
		int color, x, y, xn, yn, fx, fy, fxn, fyn;
		for (int ei = head[i]; ei > 0; ei = next[ei]) {
			color = c[qid[ei]];
			x = u[e[qid[ei]]];
			y = v[e[qid[ei]]];
			xn = x + n;
			yn = y + n;
			fx = find(color, x);
			fy = find(color, y);
			fxn = find(color, xn);
			fyn = find(color, yn);
			if (fx != fyn) {
				union(color, fx, fyn);
				unionCnt++;
			}
			if (fy != fxn) {
				union(color, fy, fxn);
				unionCnt++;
			}
		}
		if (l == r) {
			if (find(c[l], u[e[l]]) == find(c[l], v[e[l]])) {
				ans[l] = false;
				c[l] = lastColor[e[l]];
			} else {
				ans[l] = true;
				lastColor[e[l]] = c[l];
			}
		} else {
			int mid = (l + r) >> 1;
			dfs(l, mid, i << 1);
			dfs(mid + 1, r, i << 1 | 1);
		}
		for (int j = 1; j <= unionCnt; j++) {
			undo();
		}
	}

	public static void prepare() {
		for (int color = 1; color <= k; color++) {
			for (int i = 1; i <= n; i++) {
				father[color][i] = i;
				father[color][i + n] = i + n;
				siz[color][i] = 1;
				siz[color][i + n] = 1;
			}
		}
		for (int i = 1; i <= m; i++) {
			post[i] = q;
		}
		for (int i = q; i >= 1; i--) {
			if (i + 1 <= post[e[i]]) {
				add(i + 1, post[e[i]], i, 1, q, 1);
			}
			post[e[i]] = i;
		}
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		n = io.nextInt();
		m = io.nextInt();
		k = io.nextInt();
		q = io.nextInt();
		for (int i = 1; i <= m; i++) {
			u[i] = io.nextInt();
			v[i] = io.nextInt();
		}
		for (int i = 1; i <= q; i++) {
			e[i] = io.nextInt();
			c[i] = io.nextInt();
		}
		prepare();
		dfs(1, q, 1);
		for (int i = 1; i <= q; i++) {
			if (ans[i]) {
				io.write("YES\n");
			} else {
				io.write("NO\n");
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
