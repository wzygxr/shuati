package class162;

// 长链剖分优化动态规划模版题，java版
// 一共有n个节点，给定n-1条边，所有节点连成一棵树，规定1号节点是头
// 规定任何点到自己的距离为0
// 定义d(u, x)，以u为头的子树中，到u的距离为x的节点数
// 对于每个点u，想知道哪个尽量小的x，能取得最大的d(u, x)值
// 打印每个点的答案x
// 1 <= n <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/CF1009F
// 测试链接 : https://codeforces.com/problemset/problem/1009/F
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 问题分析:
 * 这是一道经典的长链剖分优化树形DP的题目，主要涉及:
 * 1. 树形动态规划
 * 2. 长链剖分优化
 * 3. 深度相关DP状态设计
 *
 * 解题思路:
 * 1. 暴力解法: 对每个节点u，计算其子树中到u距离为x的节点数d(u,x)，时间复杂度O(n²)
 * 2. 长链剖分优化: 利用长链剖分的性质，对DP数组进行空间和时间优化，时间复杂度O(n)
 *
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 *
 * 算法详解:
 * 1. DP状态设计:
 *    - dp[u][d]: 以u为根的子树中，到u距离为d的节点数
 *    - ansx[u]: 节点u的答案，即最小的x使得d(u,x)最大
 * 2. 状态转移:
 *    - dp[u][0] = 1 (u到自己的距离为0)
 *    - dp[u][d] = Σ dp[v][d-1] (v是u的子节点)
 * 3. 长链剖分优化:
 *    - 重儿子信息继承：通过指针偏移实现O(1)继承
 *    - 轻儿子信息合并：暴力合并，但每条链只合并一次
 * 4. 空间优化:
 *    - 同一条长链共享内存空间，通过dfn序和指针偏移实现
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code06_DominantIndices1 {
	// 常量定义
	public static int MAXN = 1000001; // 最大节点数
	public static int n;

	// 链式前向星 - 用于存储树的邻接关系
	// head[u]: 节点u的第一条边的索引
	// next[i]: 第i条边的下一条边索引
	// to[i]: 第i条边指向的节点
	// cntg: 边的计数器
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cntg = 0;

	// 长链剖分相关数组
	// len[u]: 以u为顶点的长链长度
	// son[u]: 节点u的重儿子（子树深度最大的子节点）
	// dfn[u]: 节点u的dfs序号
	// cntd: dfs序计数器
	public static int[] len = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] dfn = new int[MAXN];
	public static int cntd = 0;

	// 动态规划数组和答案数组
	// dp[u]: 节点u的DP数组在全局数组中的起始位置
	// ansx[u]: 节点u的答案
	public static int[] dp = new int[MAXN << 1]; // 全局DP数组，空间优化
	public static int[] ansx = new int[MAXN];

	// 设置节点u的第i位DP值为v
	// 通过dfn序和指针偏移实现空间复用
	public static void setdp(int u, int i, int v) {
		dp[dfn[u] + i] = v;
	}

	// 获取节点u的第i位DP值
	// 通过dfn序和指针偏移实现空间复用
	public static int getdp(int u, int i) {
		return dp[dfn[u] + i];
	}

	// 添加边到链式前向星结构中
	// u: 起点, v: 终点
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 递归版第一次DFS
	// 计算每个节点的子树信息并确定重儿子
	// u: 当前节点, fa: 父节点
	public static void dfs1(int u, int fa) {
		// 遍历u的所有子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa) {
				dfs1(v, u);
			}
		}
		// 确定重儿子：选择子树深度最大的子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa) {
				if (son[u] == 0 || len[son[u]] < len[v]) {
					son[u] = v;
				}
			}
		}
		len[u] = len[son[u]] + 1; // 计算以u为顶点的长链长度
	}

	// 递归版第二次DFS
	// 进行长链剖分和DP计算
	// u: 当前节点, fa: 父节点
	public static void dfs2(int u, int fa) {
		dfn[u] = ++cntd; // 分配dfs序
		setdp(u, 0, 1); // u到自己的距离为0，节点数为1
		ansx[u] = 0; // 初始化答案
		if (son[u] == 0) { // 如果没有重儿子，说明是叶子节点
			return;
		}
		// 优先处理重儿子，实现DP信息继承
		dfs2(son[u], u);
		// 更新节点u的答案
		// 由于重儿子的信息已经计算完成，可以直接继承
		ansx[u] = ansx[son[u]] + 1; // 重儿子的答案需要加1（距离增加1）
		// 处理所有轻儿子
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa && v != son[u]) { // 不是父节点且不是重儿子
				dfs2(v, u); // 处理轻儿子
				// 暴力合并轻儿子的信息到当前节点
				// 注意：这里只需要循环轻儿子的深度次
				// 每条链只会被合并一次，因此总时间复杂度仍为O(n)
				for (int i = 1; i <= len[v]; i++) {
					// 更新DP值：当前节点距离i的节点数 = 原有节点数 + 轻儿子距离i-1的节点数
					setdp(u, i, getdp(u, i) + getdp(v, i - 1));
					// 更新答案：找到使dp[u][i]最大的最小i
					// 如果当前值更大，或者值相等但索引更小，则更新答案
					if (getdp(u, i) > getdp(u, ansx[u]) || 
					    (getdp(u, i) == getdp(u, ansx[u]) && i < ansx[u])) {
						ansx[u] = i;
					}
				}
			}
		}
		// 特殊处理：如果最大值为1，说明只有节点u自己，答案应为0
		if (getdp(u, ansx[u]) == 1) {
			ansx[u] = 0;
		}
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		n = io.nextInt();
		// 读入所有边，构建链式前向星
		for (int i = 1, u, v; i < n; i++) {
			u = io.nextInt();
			v = io.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs1(1, 0); // 第一次DFS，确定重儿子
		dfs2(1, 0); // 第二次DFS，进行长链剖分和DP计算
		// 输出所有节点的答案
		for (int i = 1; i <= n; i++) {
			io.println(ansx[i]);
		}
		io.flush();
		io.close();
	}

	// 读写工具类 - 提供高效的输入输出
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