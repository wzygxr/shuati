package class166;

/**
 * 洛谷 P5227 连通图 - Java实现
 * 
 * 题目来源: 洛谷
 * 题目链接: https://www.luogu.com.cn/problem/P5227
 * 题目描述: 
 *   给定一个无向连通图和若干个小集合，每个小集合包含一些边
 *   对于每个集合，需要确定将集合中的边删掉后图是否保持联通
 *   集合间的询问相互独立
 * 
 * 解题思路:
 *   使用线段树分治 + 可撤销并查集
 *   1. 转换思路：找出每条边不存在的时间区间，在这些区间内不使用该边
 *   2. 将每条边不存在的时间区间映射到线段树上
 *   3. DFS遍历时维护连通性，如果发现整个图连通则标记答案
 * 
 * 时间复杂度: O((n + m) log k)
 * 空间复杂度: O(n + m)
 * 
 * 是否为最优解: 是
 *   这是处理动态图连通性验证问题的高效解法
 * 
 * 工程化考量:
 *   1. 使用FastIO提高输入输出效率
 *   2. 按秩合并优化并查集性能
 *   3. 精确回滚保证状态一致性
 * 
 * 适用场景:
 *   1. 动态图连通性验证问题
 *   2. 离线处理图论问题
 *   3. 需要验证删除边后图连通性的场景
 * 
 * 注意事项:
 *   1. 可撤销并查集不能使用路径压缩，只能按秩合并
 *   2. 线段树分治是离线算法
 *   3. 需要正确处理边不存在的时间区间
 * 
 * 1 <= n、k <= 10^5
 * 1 <= m <= 2 * 10^5
 * 1 <= c <= 4
 * 提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code06_ConnectedGraph1 {

	public static int MAXN = 100001;
	public static int MAXM = 200001;
	public static int MAXE = 400001;
	public static int MAXT = 10000001;
	public static int n, m, k;

	public static int[] u = new int[MAXM];
	public static int[] v = new int[MAXM];

	public static int[][] event = new int[MAXE][2];
	public static int ecnt = 0;
	public static boolean[] visit = new boolean[MAXM];

	public static int[] father = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[][] rollback = new int[MAXN][2];
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
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				add(jobl, jobr, jobx, joby, l, mid, i << 1);
			}
			if (jobr > mid) {
				add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
			}
		}
	}

	public static void dfs(int l, int r, int i) {
		boolean check = false;
		int unionCnt = 0;
		for (int ei = head[i]; ei > 0; ei = next[ei]) {
			int x = tox[ei], y = toy[ei], fx = find(x), fy = find(y);
			if (fx != fy) {
				union(fx, fy);
				unionCnt++;
			}
			if (siz[find(fx)] == n) {
				check = true;
				break;
			}
		}
		if (check) {
			for (int j = l; j <= r; j++) {
				ans[j] = true;
			}
		} else {
			if (l == r) {
				ans[l] = false;
			} else {
				int mid = (l + r) >> 1;
				dfs(l, mid, i << 1);
				dfs(mid + 1, r, i << 1 | 1);
			}
		}
		for (int j = 1; j <= unionCnt; j++) {
			undo();
		}
	}

	public static void prepare() {
		for (int i = 1; i <= n; i++) {
			father[i] = i;
			siz[i] = 1;
		}
		Arrays.sort(event, 1, ecnt + 1, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
		int eid, t;
		for (int l = 1, r = 1; l <= ecnt; l = ++r) {
			eid = event[l][0];
			visit[eid] = true;
			while (r + 1 <= ecnt && event[r + 1][0] == eid) {
				r++;
			}
			t = 1;
			for (int i = l; i <= r; i++) {
				if (t <= event[i][1] - 1) {
					add(t, event[i][1] - 1, u[eid], v[eid], 1, k, 1);
				}
				t = event[i][1] + 1;
			}
			if (t <= k) {
				add(t, k, u[eid], v[eid], 1, k, 1);
			}
		}
		for (int i = 1; i <= m; i++) {
			if (!visit[i]) {
				add(1, k, u[i], v[i], 1, k, 1);
			}
		}
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		n = io.nextInt();
		m = io.nextInt();
		for (int i = 1; i <= m; i++) {
			u[i] = io.nextInt();
			v[i] = io.nextInt();
		}
		k = io.nextInt();
		for (int i = 1, c; i <= k; i++) {
			c = io.nextInt();
			for (int j = 1; j <= c; j++) {
				event[++ecnt][0] = io.nextInt();
				event[ecnt][1] = i;
			}
		}
		prepare();
		dfs(1, k, 1);
		for (int i = 1; i <= k; i++) {
			if (ans[i]) {
				io.write("Connected\n");
			} else {
				io.write("Disconnected\n");
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
