package class121;

// 树的直径模版(树型dp)
// 给定一棵树，边权可能为负，求直径长度
// 测试链接 : https://www.luogu.com.cn/problem/U81904
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有的用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 树的直径求解器 - 树形动态规划方法
 * 
 * 算法说明：
 * 通过一次DFS，在每个节点计算经过该节点的最长路径。
 * 这种方法可以处理负权边的情况。
 * 
 * 核心思想：
 * 对于每个节点u，维护两个值：
 * 1. dist[u]：从u开始必须往下走，能走出的最大距离（可以不选任何边）
 * 2. ans[u]：路径必须包含点u的情况下，最大路径和
 * 
 * 适用场景：
 * - 可以处理负权边的情况
 * - 适用于任何树结构
 * 
 * 时间复杂度：O(n)，其中n是树中节点的数量
 * 空间复杂度：O(n)，用于存储邻接表和辅助数组
 * 
 * 相关题目：
 * - 洛谷 U81904 树的直径
 * - LeetCode 543. 二叉树的直径
 * - SPOJ PT07Z - Longest path in a tree
 * - CSES 1131 - Tree Diameter
 * - 51Nod 2602 - 树的直径
 * - AtCoder ABC221F - Diameter Set
 * - Codeforces 1499F - Diameter Cuts
 */
public class Code01_Diameter2 {

	public static int MAXN = 500001;

	public static int n;

	// 邻接表头数组，head[i]表示节点i的第一条边在next数组中的索引
	public static int[] head = new int[MAXN];

	// 边的邻接表，next[i]表示第i条边的下一条边的索引
	public static int[] next = new int[MAXN << 1];

	// 边的邻接表，to[i]表示第i条边指向的节点
	public static int[] to = new int[MAXN << 1];

	// 边的邻接表，weight[i]表示第i条边的权重
	public static int[] weight = new int[MAXN << 1];

	// 边的计数器
	public static int cnt;

	// dist[u] : 从u开始必须往下走，能走出的最大距离，可以不选任何边
	public static int[] dist = new int[MAXN];

	// ans[u] : 路径必须包含点u的情况下，最大路径和
	public static int[] ans = new int[MAXN];

	/**
	 * 初始化数据结构
	 * 重置边的计数器和相关数组
	 */
	public static void build() {
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
		Arrays.fill(dist, 1, n + 1, 0);
		Arrays.fill(ans, 1, n + 1, 0);
	}

	/**
	 * 添加一条无向边
	 * @param u 起点
	 * @param v 终点
	 * @param w 边的权重
	 */
	public static void addEdge(int u, int v, int w) {
		next[cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt++;
	}

	/**
	 * 树形动态规划计算树的直径
	 * @param u 当前节点
	 * @param f 父节点
	 */
	public static void dp(int u, int f) {
		// 递归处理所有子节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dp(v, u);
			}
		}
		
		// 计算经过当前节点的最大路径
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				// 更新经过当前节点的最大路径
				ans[u] = Math.max(ans[u], dist[u] + dist[v] + weight[e]);
				// 更新从当前节点向下走的最大距离
				dist[u] = Math.max(dist[u], dist[v] + weight[e]);
			}
		}
	}

	/**
	 * 主方法
	 * 读取输入数据，构建树，使用树形DP计算直径并输出结果
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
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
		}
		dp(1, 0);
		int diameter = Integer.MIN_VALUE;
		for (int i = 1; i <= n; i++) {
			diameter = Math.max(diameter, ans[i]);
		}
		out.println(diameter);
		out.flush();
		out.close();
		br.close();
	}

}