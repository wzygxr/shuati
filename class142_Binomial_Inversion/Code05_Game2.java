package class145;

// 游戏(迭代版)
// 一共有n个节点，n <= 5000，n为偶数，其中有m个点属于小A，有m个点属于小B，m为n的一半
// 给定n-1条边，节点之间组成一颗树，1号节点是根节点
// 给定长度为n的数组arr，arr[i]的值表示i号节点由谁拥有，0为小A拥有，1为小B拥有
// 游戏有m回合，每回合都有胜负，两人需要选择一个自己拥有、但之前没选过的点，作为本回合当前点
// 小A当前点的子树里有小B当前点，则小A胜；小B当前点的子树里有小A当前点，则小B胜；否则平局
// 返回m回合里能出现k次非平局的游戏方法数，打印k=0..m时的所有答案，对 998244353 取模
// 两场游戏视为不同的定义：当且仅当存在小A拥有的点x，小B在小A选择x的那个回合所选择的点不同
// 测试链接 : https://www.luogu.com.cn/problem/P6478
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
二项式反演在树上游戏问题中的应用：

问题描述：
给定一棵n个节点的树，其中n为偶数。有m=n/2个节点属于小A(标记为0)，m个节点属于小B(标记为1)。
游戏有m回合，每回合两个玩家各选一个自己拥有且未被选过的点。
如果小A选的点的子树包含小B选的点，则小A胜；
如果小B选的点的子树包含小A选的点，则小B胜；
否则为平局。
求出现恰好k次非平局情况的游戏方法数，对k=0到m输出所有答案。

解题思路：
设f(i)表示恰好出现i次非平局的方案数
设g(i)表示至少出现i次非平局的方案数

计算g(i)可以通过树形DP实现：
1. 对于每个节点u，计算其子树中属于对手的节点数
2. 使用树形DP，dp[u][i]表示以u为根的子树中，出现i次非平局的方案数
3. 在转移时考虑是否选择当前节点参与非平局情况

根据二项式反演：
f(k) = Σ(i=k to m) (-1)^(i-k) * C(i, k) * g(i) * (m-i)!

其中(m-i)!是因为剩下的(m-i)次游戏可以任意安排。

相关题目：
1. 洛谷 P6478 游戏（标准题目）
2. 树上计数问题，通常结合树形DP和组合数学

迭代版说明：
由于Java递归深度限制，当树的深度较大时可能会栈溢出。
使用显式栈模拟递归过程，将递归版改为迭代版可以避免这个问题。
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code05_Game2 {

	public static final int MAXN = 5001;

	public static final int MOD = 998244353;

	public static int n, m;

	public static int[] arr = new int[MAXN];

	public static long[] fac = new long[MAXN];

	public static long[][] c = new long[MAXN][MAXN];

	// 链式前向星需要
	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int cnt;

	// dfs需要
	public static int[] size = new int[MAXN];

	public static int[][] belong = new int[MAXN][2];

	public static long[][] dp = new long[MAXN][MAXN];

	public static long[] backup = new long[MAXN];

	// 反演需要
	public static long[] g = new long[MAXN];

	// 最后答案
	public static long[] f = new long[MAXN];

	public static void build() {
		cnt = 1;
		fac[0] = 1;
		for (int i = 1; i <= n; i++) {
			head[i] = 0;
			fac[i] = fac[i - 1] * i % MOD;
		}
		for (int i = 0; i <= n; i++) {
			c[i][0] = 1;
			for (int j = 1; j <= i; j++) {
				c[i][j] = (c[i - 1][j] + c[i - 1][j - 1]) % MOD;
			}
		}
	}

	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	// 迭代版
	// ufe是为了实现迭代版而准备的栈
	// 不会改，看讲解118，讲了怎么从递归版改成迭代版
	public static int[][] ufe = new int[MAXN][3];

	public static int stackSize, u, fa, e;

	public static void push(int u, int fa, int e) {
		ufe[stackSize][0] = u;
		ufe[stackSize][1] = fa;
		ufe[stackSize][2] = e;
		stackSize++;
	}

	public static void pop() {
		--stackSize;
		u = ufe[stackSize][0];
		fa = ufe[stackSize][1];
		e = ufe[stackSize][2];
	}

	// 迭代版
	public static void dfs(int root) {
		stackSize = 0;
		push(root, 0, -1);
		int v, num;
		while (stackSize > 0) {
			pop();
			if (e == -1) { // 第一次来到当前节点，设置初始值
				size[u] = 1;
				belong[u][arr[u]] = 1;
				dp[u][0] = 1;
				e = head[u];
			} else { // 不是第一次来到当前节点
				v = to[e];
				if (v != fa) { // 之前的孩子，dfs过程计算完了，所以用之前孩子的信息，更新当前节点的信息
					for (int i = 0; i <= Math.min(size[u] / 2, m); i++) {
						backup[i] = dp[u][i];
						dp[u][i] = 0;
					}
					for (int l = 0; l <= Math.min(size[u] / 2, m); l++) {
						for (int r = 0; r <= Math.min(size[v] / 2, m - l); r++) {
							dp[u][l + r] = (dp[u][l + r] + backup[l] * dp[v][r] % MOD) % MOD;
						}
					}
					size[u] += size[v];
					belong[u][0] += belong[v][0];
					belong[u][1] += belong[v][1];
				}
				// 来到去往下一个孩子的边
				e = next[e];
			}
			if (e != 0) { // 还有后续子树
				push(u, fa, e);
				if (to[e] != fa) {
					push(to[e], u, -1);
				}
			} else { // 没有后续子树，最后计算包含头节点的方法数
				num = belong[u][arr[u] ^ 1];
				for (int i = 1; i <= Math.min(num, m); i++) {
					backup[i] = dp[u][i];
				}
				for (int i = 1; i <= Math.min(num, m); i++) {
					dp[u][i] = (dp[u][i] + backup[i - 1] * (num - i + 1) % MOD) % MOD;
				}
			}
		}
	}

	public static void compute() {
		dfs(1); // dfs是迭代版
		for (int i = 0; i <= m; i++) {
			g[i] = dp[1][i] * fac[m - i] % MOD;
		}
		for (int k = 0; k <= m; k++) {
			for (int i = k; i <= m; i++) {
				if (((i - k) & 1) == 0) {
					f[k] = (f[k] + c[i][k] * g[i] % MOD) % MOD;
				} else {
					f[k] = (f[k] + c[i][k] * g[i] % MOD * (MOD - 1) % MOD) % MOD;
				}
			}
		}
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		n = io.nextInt();
		m = n >> 1;
		build();
		String str = io.next();
		for (int i = 1; i <= n; i++) {
			arr[i] = str.charAt(i - 1) - '0';
		}
		for (int i = 1, u, v; i < n; i++) {
			u = io.nextInt();
			v = io.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		compute();
		for (int k = 0; k <= m; k++) {
			io.println(f[k]);
		}
		io.flush();
		io.close();
	}

	// Kattio类IO效率很好，但还是不如StreamTokenizer
	// 只有StreamTokenizer无法正确处理时，才考虑使用这个类
	// 参考链接 : https://oi-wiki.org/lang/java-pro/
	public static class Kattio extends PrintWriter {
		private BufferedReader r;
		private StringTokenizer st;

		public Kattio() {
			this(System.in, System.out);
		}

		public Kattio(InputStream i, OutputStream o) {
			super(o);
			r = new BufferedReader(new InputStreamReader(i));
		}

		public Kattio(String intput, String output) throws IOException {
			super(output);
			r = new BufferedReader(new FileReader(intput));
		}

		public String next() {
			try {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(r.readLine());
				return st.nextToken();
			} catch (Exception e) {
			}
			return null;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}