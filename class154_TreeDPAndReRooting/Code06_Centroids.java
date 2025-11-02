package class123;

// 哪些点可以改造成重心
// 题目来源：Codeforces 708C Centroids
// 题目链接：https://codeforces.com/problemset/problem/708/C
// 测试链接 : https://www.luogu.com.cn/problem/CF708C
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
给定一棵树，判断每个节点是否可以通过调整一条边（删除一条边并添加一条边）使其成为树的重心。
树的重心定义：删除该节点后，剩余的最大连通分量大小不超过n/2。

算法思路：
1. 第一次DFS：计算以节点1为根时，每个节点子树的大小和最大子树
   - size[u]表示节点u的子树大小
   - maxsub[u]表示节点u的最大子树对应的子节点
   - inner1[u]表示节点u内部（子树中）<=n/2的第一大子树大小
   - inner2[u]表示节点u内部（子树中）<=n/2的第二大子树大小
   - choose[u]表示inner1[u]对应的子节点

2. 第二次DFS：换根DP，计算每个节点作为根时的最大子树大小
   - outer[u]表示节点u外部（子树外）<=n/2的最大子树大小
   - 当从节点u换根到节点v时：
     * 如果n-size[v] <= n/2，则outer[v] = n-size[v]
     * 否则，如果choose[u] != v，则outer[v] = max(outer[u], inner1[u])
     * 否则，outer[v] = max(outer[u], inner2[u])

3. 检查函数：判断节点u是否能通过调整一条边成为重心
   - 如果size[maxsub[u]] > n/2，说明u的最大子树超过一半
     * 检查是否可以通过调整该子树使其不超过一半
   - 如果n-size[u] > n/2，说明u的外部部分超过一半
     * 检查是否可以通过调整外部部分使其不超过一半
   - 否则，u已经是重心

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code06_Centroids.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code06_Centroids.py
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code06_Centroids {

	public static int MAXN = 400001;

	public static int n;

	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int cnt;

	// size[i]: i内部，整棵子树大小
	public static int[] size = new int[MAXN];

	// maxsub[i]: i内部，最大子树，是i节点的哪个儿子拥有，记录节点编号
	public static int[] maxsub = new int[MAXN];

	// inner1[i]: i内部，<=n/2且第一大的子树是多大，记录大小
	public static int[] inner1 = new int[MAXN];

	// inner2[i]: i内部，<=n/2且第二大的子树是多大，记录大小
	public static int[] inner2 = new int[MAXN];

	// 注意: inner1[i]和inner2[i]，所代表的子树一定要来自i的不同儿子

	// choose[i]: inner1[i]所代表的子树，是i节点的哪个儿子拥有，记录节点编号
	public static int[] choose = new int[MAXN];

	// outer[i]: i外部，<=n/2且第一大的子树是多大，记录大小
	public static int[] outer = new int[MAXN];

	public static void build() {
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
		Arrays.fill(maxsub, 1, n + 1, 0);
		Arrays.fill(choose, 1, n + 1, 0);
		Arrays.fill(inner1, 1, n + 1, 0);
		Arrays.fill(inner2, 1, n + 1, 0);
		Arrays.fill(outer, 1, n + 1, 0);
	}

	public static void addEdge(int u, int v) {
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	// 第一次DFS：计算以节点1为根时，每个节点子树的信息
	public static void dfs1(int u, int f) {
		size[u] = 1;
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
				// 更新u的子树大小
				size[u] += size[v];
				// 更新u的最大子树
				if (size[maxsub[u]] < size[v]) {
					maxsub[u] = v;
				}
				// 计算u内部满足条件的最大子树和次大子树
				// 如果v子树大小不超过n/2，则考虑v子树；否则考虑v子树内的最大子树
				int innerSize = size[v] <= n / 2 ? size[v] : inner1[v];
				if (inner1[u] < innerSize) {
					choose[u] = v;
					inner2[u] = inner1[u];
					inner1[u] = innerSize;
				} else if (inner2[u] < innerSize) {
					inner2[u] = innerSize;
				}
			}
		}
	}

	// 第二次DFS：换根DP，计算每个节点作为根时的外部信息
	public static void dfs2(int u, int f) {
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				// 计算v节点外部满足条件的最大子树大小
				if (n - size[v] <= n / 2) {
					// u子树外的部分满足条件
					outer[v] = n - size[v];
				} else if (choose[u] != v) {
					// u的最大子树不是v，可以使用u的最大子树或外部部分
					outer[v] = Math.max(outer[u], inner1[u]);
				} else {
					// u的最大子树是v，只能使用u的次大子树或外部部分
					outer[v] = Math.max(outer[u], inner2[u]);
				}
				dfs2(v, u);
			}
		}
	}

	// 检查节点u是否能通过调整一条边成为重心
	public static boolean check(int u) {
		// 如果u的最大子树超过一半
		if (size[maxsub[u]] > n / 2) {
			// 检查是否可以通过调整该子树使其不超过一半
			// 调整方法是将该子树中最大的不超过n/2的部分分离出去
			return size[maxsub[u]] - inner1[maxsub[u]] <= n / 2;
		}
		// 如果u外部的部分超过一半
		if (n - size[u] > n / 2) {
			// 检查是否可以通过调整外部部分使其不超过一半
			// 调整方法是将外部最大的不超过n/2的部分分离出去
			return n - size[u] - outer[u] <= n / 2;
		}
		// 否则u已经是重心
		return true;
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
		// 第一次DFS计算以节点1为根时的信息
		dfs1(1, 0);
		// 第二次DFS换根计算所有节点作为根时的外部信息
		dfs2(1, 0);
		// 检查每个节点是否能成为重心
		for (int i = 1; i <= n; i++) {
			out.print(check(i) ? "1 " : "0 ");
		}
		out.println();
		out.flush();
		out.close();
		br.close();
	}

}