package class162;

// 攻略，java版
// 一共有n个节点，给定n-1条边，所有节点连成一棵树，每个点给定点权
// 规定1号点是头，任何路径都必须从头开始，然后走到某个叶节点停止
// 路径上的点，点权的累加和，叫做这个路径的收益
// 给定数字k，你可以随意选出k条路径，所有路径经过的点，需要取并集，也就是去重
// 并集中的点，点权的累加和，叫做k条路径的收益
// 打印k条路径的收益最大值
// 1 <= n、k <= 2 * 10^5
// 所有点权都是int类型的正数
// 测试链接 : https://www.luogu.com.cn/problem/P10641
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 问题分析:
 * 这是一道贪心和长链剖分结合的题目，主要涉及:
 * 1. 树形贪心策略
 * 2. 长链剖分优化
 * 3. 路径选择问题
 *
 * 解题思路:
 * 1. 贪心思想: 每条链的收益等于链顶节点的子树最大收益
 * 2. 长链剖分: 对树进行长链剖分，计算每条链的收益
 * 3. 贪心选择: 选择收益最大的k条链
 *
 * 时间复杂度: O(n * log n)
 * 空间复杂度: O(n)
 *
 * 算法详解:
 * 1. 树形DP:
 *    - money[u]: 以u为根的子树中的最大收益（从u出发到某个叶子节点的路径最大点权和）
 *    - 通过递归计算每个节点的money值
 * 2. 长链剖分:
 *    - 第一次DFS(dfs1/dfs3): 计算每个节点的子树信息并确定重儿子
 *    - 第二次DFS(dfs2/dfs4): 进行长链剖分，为每个节点分配链顶
 * 3. 贪心策略:
 *    - 每条长链的收益等于链顶节点的money值
 *    - 将所有长链的收益排序，选择最大的k个
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_Walkthrough1 {

	public static int MAXN = 200001;
	public static int n, k;
	public static int[] arr = new int[MAXN]; // 每个节点的点权

	// 链式前向星 - 用于存储树的邻接关系
	// head[u]: 节点u的第一条边的索引
	// next[i]: 第i条边的下一条边索引
	// to[i]: 第i条边指向的节点
	// cnt: 边的计数器
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cnt = 0;

	// 长链剖分的改写，根据money的值来标记最值钱儿子
	// fa[u]: 节点u的父节点
	// son[u]: 节点u的重儿子（子树money值最大的子节点）
	// top[u]: 节点u所在长链的顶部节点
	// money[u]: 以u为根的子树中的最大收益
	public static int[] fa = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] top = new int[MAXN];
	public static long[] money = new long[MAXN];

	// 每条链的头节点收益排序
	// sorted[i]: 第i条链的收益值
	public static long[] sorted = new long[MAXN];

	// 添加边到链式前向星结构中
	// u: 起点, v: 终点
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	// 递归版第一次DFS
	// 计算每个节点的父节点、子树信息，并确定重儿子
	// u: 当前节点, f: 父节点
	public static void dfs1(int u, int f) {
		// 遍历u的所有子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
			}
		}
		// 确定重儿子：选择子树money值最大的子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				if (son[u] == 0 || money[son[u]] < money[v]) {
					son[u] = v;
				}
			}
		}
		fa[u] = f; // 记录父节点
		// 计算以u为根的子树中的最大收益
		// money[u] = money[重儿子] + arr[u]
		money[u] = money[son[u]] + arr[u];
	}

	// 递归版第二次DFS
	// 进行长链剖分，为每个节点分配链顶
	// u: 当前节点, t: 当前链的顶部节点
	public static void dfs2(int u, int t) {
		top[u] = t; // 设置链顶
		if (son[u] == 0) { // 如果没有重儿子，说明是叶子节点
			return;
		}
		// 优先处理重儿子，保持长链的连续性
		dfs2(son[u], t);
		// 处理所有轻儿子，每个轻儿子作为新链的顶部
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa[u] && v != son[u]) { // 不是父节点且不是重儿子
				dfs2(v, v);
			}
		}
	}

	// 栈结构用于迭代版DFS
	// fse[stacksize][0]: 当前节点
	// fse[stacksize][1]: 父节点
	// fse[stacksize][2]: 边的索引
	public static int[][] fse = new int[MAXN][3];

	public static int stacksize, first, second, edge;

	// 将节点信息压入栈中
	public static void push(int fir, int sec, int edg) {
		fse[stacksize][0] = fir;
		fse[stacksize][1] = sec;
		fse[stacksize][2] = edg;
		stacksize++;
	}

	// 从栈中弹出节点信息
	public static void pop() {
		--stacksize;
		first = fse[stacksize][0];
		second = fse[stacksize][1];
		edge = fse[stacksize][2];
	}

	// dfs1的迭代版 - 避免递归深度过大导致栈溢出
	// 通过显式栈模拟递归过程
	public static void dfs3() {
		stacksize = 0;
		push(1, 0, -1); // 从根节点1开始，父节点为0，边索引为-1表示初次访问
		while (stacksize > 0) {
			pop();
			if (edge == -1) { // 初次访问节点
				edge = head[first]; // 获取第一条边
			} else {
				edge = next[edge]; // 获取下一条边
			}
			if (edge != 0) { // 如果还有边未处理
				push(first, second, edge);
				if (to[edge] != second) { // 如果不是回到父节点
					push(to[edge], first, -1); // 将子节点压入栈中
				}
			} else { // 所有子节点已处理完毕，计算子树信息
				// 确定重儿子：选择子树money值最大的子节点
				for (int e = head[first], v; e > 0; e = next[e]) {
					v = to[e];
					if (v != second) {
						if (son[first] == 0 || money[son[first]] < money[v]) {
							son[first] = v;
						}
					}
				}
				fa[first] = second; // 记录父节点
				// 计算以first为根的子树中的最大收益
				money[first] = money[son[first]] + arr[first];
			}
		}
	}

	// dfs2的迭代版 - 避免递归深度过大导致栈溢出
	public static void dfs4() {
		stacksize = 0;
		push(1, 1, -1); // 从根节点1开始，链顶为1
		while (stacksize > 0) {
			pop();
			if (edge == -1) { // 初次访问节点
				top[first] = second;
				if (son[first] == 0) { // 如果没有重儿子
					continue;
				}
				push(first, second, -2); // 标记需要处理轻儿子
				push(son[first], second, -1); // 优先处理重儿子
				continue;
			} else if (edge == -2) { // 需要处理轻儿子
				edge = head[first];
			} else {
				edge = next[edge]; // 获取下一条边
			}
			if (edge != 0) {
				push(first, second, edge);
				// 处理轻儿子，每个轻儿子作为新链的顶部
				if (to[edge] != fa[first] && to[edge] != son[first]) {
					push(to[edge], to[edge], -1);
				}
			}
		}
	}

	// 计算k条路径的最大收益
	// 贪心策略：选择收益最大的k条链
	public static long compute() {
		int len = 0;
		// 收集所有长链的收益值
		// 只有链顶节点的收益值才代表一条完整链的收益
		for (int i = 1; i <= n; i++) {
			if (top[i] == i) { // 如果i是链顶
				sorted[++len] = money[i]; // 记录收益值
			}
		}
		// 按收益值从大到小排序
		Arrays.sort(sorted, 1, len + 1);
		long ans = 0;
		// 选择收益最大的k条链
		for (int i = 1, j = len; i <= k; i++, j--) {
			ans += sorted[j];
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		// 读入所有节点的点权
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		// 读入所有边，构建链式前向星
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs3(); // 迭代版第一次DFS
		dfs4(); // 迭代版第二次DFS
		out.println(compute()); // 输出结果
		out.flush();
		out.close();
		br.close();
	}

}