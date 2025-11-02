package class121;

// 树的直径模版(两遍dfs)
// 给定一棵树，边权可能为负，求直径长度
// 测试链接 : https://www.luogu.com.cn/problem/U81904
// 提交以下的code，提交时请把类名改成"Main"
// 会有无法通过的用例，因为树上有负边

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 树的直径求解器 - 两次DFS方法
 * 
 * 算法说明：
 * 1. 从任意一点开始，找到距离它最远的点s
 * 2. 从s开始，找到距离它最远的点t
 * 3. s到t的距离即为树的直径
 * 
 * 适用场景：
 * - 适用于边权非负的情况
 * - 对于有负权边的树，此方法可能不适用
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
public class Code01_Diameter1 {

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

	// 直径的开始点
	public static int start;

	// 直径的结束点
	public static int end;

	// 直径长度
	public static int diameter;

	// dist[i] : 从规定的头节点出发，走到i的距离
	public static int[] dist = new int[MAXN];

	// last[i] : 从规定的头节点出发，i节点的上一个节点
	public static int[] last = new int[MAXN];

	/**
	 * 初始化数据结构
	 * 重置边的计数器和邻接表头数组
	 */
	public static void build() {
		cnt = 1;
		Arrays.fill(head, 1, n + 1, 0);
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
	 * 计算树的直径
	 * 使用两次DFS方法：
	 * 1. 从节点1开始DFS，找到距离最远的节点start
	 * 2. 从start开始DFS，找到距离最远的节点end
	 * 3. start到end的距离就是树的直径
	 */
	public static void road() {
		dfs(1, 0, 0);
		start = 1;
		for (int i = 2; i <= n; i++) {
			if (dist[i] > dist[start]) {
				start = i;
			}
		}
		dfs(start, 0, 0);
		end = 1;
		for (int i = 2; i <= n; i++) {
			if (dist[i] > dist[end]) {
				end = i;
			}
		}
		diameter = dist[end];
	}

	/**
	 * 深度优先搜索
	 * @param u 当前节点
	 * @param f 父节点
	 * @param w 到父节点的边的权重
	 */
	public static void dfs(int u, int f, int w) {
		last[u] = f;
		dist[u] = dist[f] + w;
		for (int e = head[u]; e != 0; e = next[e]) {
			if (to[e] != f) {
				dfs(to[e], u, weight[e]);
			}
		}
	}

	/**
	 * 主方法
	 * 读取输入数据，构建树，计算直径并输出结果
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
		road();
		out.println(diameter);
		out.flush();
		out.close();
		br.close();
	}

}