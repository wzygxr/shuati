package class123;

// 选择节点做根使流量和最大(迭代版)
// 题目来源：POJ 3585 Accumulation Degree
// 题目链接：http://poj.org/problem?id=3585
// 测试链接 : http://poj.org/problem?id=3585
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
给定一棵树，每条边有流量限制。对于每个节点作为根，计算从该节点流向所有叶子节点的最大流量和。
叶子节点是度数为1的节点（根节点度数为1时不算叶子）。

算法思路：
1. 第一次DFS：计算以节点1为根时，每个节点向其子树所有叶子节点的流量和
   - flow[u]表示从节点u流向其子树中所有叶子节点的流量和
   - 如果v是叶子节点，则flow[u] += weight(e)（e是u到v的边）
   - 如果v不是叶子节点，则flow[u] += min(flow[v], weight(e))

2. 第二次DFS：换根DP，计算每个节点作为根时的流量和
   - 当从节点u换根到节点v时：
     * 如果u是叶子节点，则dp[v] = flow[v] + weight(e)
     * 如果u不是叶子节点：
       - 计算u向外的流量：uOut = dp[u] - min(flow[v], weight(e))
       - 计算dp[v] = flow[v] + min(uOut, weight(e))

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code04_MaximizeFlow2.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code04_MaximizeFlow1.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code04_MaximizeFlow1.cpp
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code04_MaximizeFlow2 {

	public static int MAXN = 200001;

	public static int n;

	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int[] weight = new int[MAXN << 1];

	public static int cnt;

	public static int[] degree = new int[MAXN];

	public static int[] flow = new int[MAXN];

	public static int[] dp = new int[MAXN];

	public static void build() {
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
		Arrays.fill(degree, 1, n + 1, 0);
		Arrays.fill(flow, 1, n + 1, 0);
		Arrays.fill(dp, 1, n + 1, 0);
	}

	public static void addEdge(int u, int v, int w) {
		next[cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
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
						if (degree[v] == 1) {
							flow[u] += weight[e];
						} else {
							flow[u] += Math.min(flow[v], weight[e]);
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
					if (degree[u] == 1) {
						dp[v] = flow[v] + weight[e];
					} else {
						int uOut = dp[u] - Math.min(flow[v], weight[e]);
						dp[v] = flow[v] + Math.min(uOut, weight[e]);
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
		int testCase = (int) in.nval;
		for (int t = 1; t <= testCase; t++) {
			in.nextToken();
			n = (int) in.nval;
			build();
			for (int i = 1, u, v, w; i < n; i++) {
				in.nextToken();
				u = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				in.nextToken();
				w = (int) in.nval;
				addEdge(u, v, w);
				addEdge(v, u, w);
				degree[u]++;
				degree[v]++;
			}
			dfs1(1);
			dp[1] = flow[1];
			dfs2(1);
			int ans = 0;
			for (int i = 1; i <= n; i++) {
				ans = Math.max(ans, dp[i]);
			}
			out.println(ans);
		}
		out.flush();
		out.close();
		br.close();
	}

}
