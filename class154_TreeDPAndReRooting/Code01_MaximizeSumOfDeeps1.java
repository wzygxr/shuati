package class123;

// 最大深度和(递归版)
// 题目来源：POJ 3478 [POI2008] STA-Station
// 题目链接：http://poj.org/problem?id=3478
// 测试链接 : https://www.luogu.com.cn/problem/P3478
// 提交以下的code，提交时请把类名改成"Main"
// C++这么写能通过，java会因为递归层数太多而爆栈
// java能通过的写法参考本节课Code01_MaximizeSumOfDeeps2文件

/*
题目解析：
这是一道经典的换根DP（Re-rooting DP）问题。我们需要找到一个节点作为根，使得所有节点到该根的深度之和最大。

算法思路：
1. 第一次DFS：以节点1为根，计算每个节点子树内的节点数量和子树内所有节点到该节点的深度之和
2. 第二次DFS：通过换根技术，计算每个节点作为根时的深度之和
   - 当我们从节点u换根到节点v时：
     * 节点v及其子树中的所有节点深度都减少1，总共减少size[v]
     * 其他节点深度都增加1，总共增加(n - size[v])
     * 因此：dp[v] = dp[u] - size[v] + (n - size[v]) = dp[u] + n - 2*size[v]

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code01_MaximizeSumOfDeeps1.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code01_MaximizeSumOfDeeps1.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code01_MaximizeSumOfDeeps1.cpp
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_MaximizeSumOfDeeps1 {

	public static int MAXN = 1000001;

	public static int n;

	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int cnt;

	public static int[] size = new int[MAXN];

	public static long[] sum = new long[MAXN];

	public static long[] dp = new long[MAXN];

	public static void build() {
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
	}

	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	// 第一次DFS：计算以节点1为根时，每个节点子树的信息
	// size[u]: 节点u的子树大小
	// sum[u]: 节点u的子树内所有节点到u的距离之和
	public static void dfs1(int u, int f) {
		// 先递归处理所有子节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
			}
		}
		// 计算当前节点的子树大小和距离和
		size[u] = 1;
		sum[u] = 0;
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				size[u] += size[v];
				sum[u] += sum[v] + size[v];
			}
		}
	}

	// 第二次DFS：换根DP，计算每个节点作为根时的距离和
	// dp[u]: 节点u作为根时，所有节点到u的距离之和
	public static void dfs2(int u, int f) {
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				// 换根公式：从u换根到v
				// v子树中的节点距离减少1，其他节点距离增加1
				dp[v] = dp[u] - size[v] + n - size[v];
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
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		// 第一次DFS计算以节点1为根的信息
		dfs1(1, 0);
		// 节点1作为根时的距离和就是sum[1]
		dp[1] = sum[1];
		// 第二次DFS换根计算所有节点作为根时的距离和
		dfs2(1, 0);
		// 找到距离和最大的节点
		long max = Long.MIN_VALUE;
		int ans = 0;
		for (int i = 1; i <= n; i++) {
			if (dp[i] > max) {
				max = dp[i];
				ans = i;
			}
		}
		out.println(ans);
		out.flush();
		out.close();
		br.close();
	}

}