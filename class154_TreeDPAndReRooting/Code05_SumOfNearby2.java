package class123;

// 每个节点距离k以内的权值和(迭代版)
// 题目来源：USACO 2012 FEB Nearby Cows
// 题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=124
// 测试链接 : https://www.luogu.com.cn/problem/P3047
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
给定一棵树，每个节点有权值。对于每个节点，计算距离它不超过k的所有节点的权值和。

算法思路：
1. 第一次DFS：计算以节点1为根时，每个节点向子树的权值和
   - sum[u][i]表示节点u的子树中，距离u恰好为i的节点权值和

2. 第二次DFS：换根DP，计算每个节点作为根时的距离k以内权值和
   - dp[u][i]表示整棵树中，距离u恰好为i的节点权值和
   - 当从节点u换根到节点v时：
     * dp[v][0] = sum[v][0] （v节点本身）
     * dp[v][1] = sum[v][1] + dp[u][0] （v的子节点 + u节点）
     * 对于i >= 2: dp[v][i] = sum[v][i] + dp[u][i-1] - sum[v][i-2]
       - dp[u][i-1]是距离u为i-1的节点权值和
       - sum[v][i-2]是要减去的重复计算部分

时间复杂度：O(n*k) - 两次DFS遍历，每次处理k个距离
空间复杂度：O(n*k) - 存储图和DP数组
是否为最优解：是，对于k较小的情况，这是最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code05_SumOfNearby2.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code05_SumOfNearby1.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code05_SumOfNearby1.cpp
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_SumOfNearby2 {

	public static int MAXN = 100001;

	public static int MAXK = 21;

	public static int n;

	public static int k;

	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int cnt;

	public static int[][] sum = new int[MAXN][MAXK];

	public static int[][] dp = new int[MAXN][MAXK];

	public static void build() {
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
	}

	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	// dfs1方法改迭代版
	// 不会改，看讲解118，讲了怎么从递归版改成迭代版
	public static int[][] ufe = new int[MAXN][3];

	public static int stackSize;

	public static int u, f, e;

	public static void push(int u, int f, int e) {
		ufe[stackSize][0] = u;
		ufe[stackSize][1] = f;
		ufe[stackSize][2] = e;
		stackSize++;
	}

	public static void pop() {
		--stackSize;
		u = ufe[stackSize][0];
		f = ufe[stackSize][1];
		e = ufe[stackSize][2];
	}

	public static void dfs1(int root) {
		stackSize = 0;
		push(root, 0, -1);
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				e = head[u];
			} else {
				e = next[e];
			}
			if (e != 0) {
				push(u, f, e);
				if (to[e] != f) {
					push(to[e], u, -1);
				}
			} else {
				for (int e = head[u], v; e != 0; e = next[e]) {
					v = to[e];
					if (v != f) {
						for (int j = 1; j <= k; j++) {
							sum[u][j] += sum[v][j - 1];
						}
					}
				}
			}
		}
	}

	// dfs2方法改迭代版
	// 不会改，看讲解118，讲了怎么从递归版改成迭代版
	public static void dfs2(int root) {
		stackSize = 0;
		push(root, 0, -1);
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				e = head[u];
			} else {
				e = next[e];
			}
			if (e != 0) {
				push(u, f, e);
				int v = to[e];
				if (v != f) {
					dp[v][0] = sum[v][0];
					dp[v][1] = sum[v][1] + dp[u][0];
					for (int i = 2; i <= k; i++) {
						dp[v][i] = sum[v][i] + dp[u][i - 1] - sum[v][i - 2];
					}
					push(v, u, -1);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		build();
		in.nextToken();
		k = (int) in.nval;
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			sum[i][0] = (int) in.nval;
		}
		dfs1(1);
		for (int i = 0; i <= k; i++) {
			dp[1][i] = sum[1][i];
		}
		dfs2(1);
		for (int i = 1, ans; i <= n; i++) {
			ans = 0;
			for (int j = 0; j <= k; j++) {
				ans += dp[i][j];
			}
			out.println(ans);
		}
		out.flush();
		out.close();
		br.close();
	}

}
