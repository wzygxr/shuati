package class123;

// 每个节点距离k以内的权值和(递归版)
// 题目来源：USACO 2012 FEB Nearby Cows
// 题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=124
// 测试链接 : https://www.luogu.com.cn/problem/P3047
// 提交以下的code，提交时请把类名改成"Main"
// C++这么写能通过，java会因为递归层数太多而爆栈
// java能通过的写法参考本节课Code05_SumOfNearby2文件

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
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code05_SumOfNearby1.java
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

public class Code05_SumOfNearby1 {

	public static int MAXN = 100001;

	public static int MAXK = 21;

	public static int n;

	public static int k;

	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int cnt;

	// sum[u][i] : 以u为头的子树内，距离为i的节点权值和
	public static int[][] sum = new int[MAXN][MAXK];

	// dp[u][i] : 以u做根，整棵树上，距离为i的节点权值和
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

	// 第一次DFS：计算以节点1为根时，每个节点向子树的距离权值和
	public static void dfs1(int u, int f) {
		// 先递归处理所有子节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
			}
		}
		// 计算从节点u向子树的距离权值和
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				// 对于每个距离j，将v子树中距离v为j-1的节点加到u的统计中
				for (int j = 1; j <= k; j++) {
					sum[u][j] += sum[v][j - 1];
				}
			}
		}
	}

	// 第二次DFS：换根DP，计算每个节点作为根时的距离权值和
	public static void dfs2(int u, int f) {
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				// 换根公式
				dp[v][0] = sum[v][0]; // 节点v本身
				dp[v][1] = sum[v][1] + dp[u][0]; // v的子节点 + u节点
				// 对于距离i >= 2的情况
				for (int i = 2; i <= k; i++) {
					// dp[v][i] = v子树中的节点 + u子树中除了v子树的节点
					// dp[u][i-1]是u子树中距离u为i-1的节点
					// sum[v][i-2]是v子树中距离v为i-2的节点，需要减去避免重复计算
					dp[v][i] = sum[v][i] + dp[u][i - 1] - sum[v][i - 2];
				}
				dfs2(v, u);
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
		// 读取每个节点的权值
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			sum[i][0] = (int) in.nval;
		}
		// 第一次DFS计算以节点1为根时的距离权值和
		dfs1(1, 0);
		// 初始化节点1作为根时的dp值
		for (int i = 0; i <= k; i++) {
			dp[1][i] = sum[1][i];
		}
		// 第二次DFS换根计算所有节点作为根时的距离权值和
		dfs2(1, 0);
		// 输出每个节点距离k以内的权值和
		for (int i = 1, ans; i <= n; i++) {
			ans = 0;
			// 将所有距离内的权值相加
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