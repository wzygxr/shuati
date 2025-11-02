package class162;

// 火热旅馆，java版
// 一共有n个节点，给定n-1条边，所有节点连成一棵树
// 三个点构成一个点组(a, b, c)，打乱顺序认为是同一个点组
// 求树上有多少点组，内部任意两个节点之间的距离是一样的
// 1 <= n <= 10^5
// 答案一定在long类型范围内
// 测试链接 : https://www.luogu.com.cn/problem/P5904
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 问题分析:
 * 这是一道复杂的树形DP题目，结合长链剖分优化，主要涉及:
 * 1. 树形动态规划
 * 2. 长链剖分优化
 * 3. 计数问题
 *
 * 解题思路:
 * 1. 问题转化: 三个点距离相等，意味着它们构成一个中心点，到中心点距离相等
 * 2. DP状态设计: 
 *    - f[u][d]: u子树中到u距离为d的节点数
 *    - g[u][d]: u子树中已经匹配了两个点，还需要距离为d就能构成合法三元组的点对数
 * 3. 状态转移: 在处理节点u和其子节点v时
 *    - 用已有的g[u][d-1]和f[v][d]更新答案
 *    - 用已有的f[u][d-1]和f[v][d]更新g[u][d]
 *    - 用f[v][d]更新f[u][d]
 * 4. 长链剖分优化: 
 *    - 重儿子信息继承：通过指针偏移实现O(1)继承
 *    - 轻儿子信息合并：暴力合并，但每条链只合并一次
 *
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 *
 * 算法详解:
 * 1. 问题理解:
 *    - 三个点距离相等意味着它们到某个中心点的距离相等
 *    - 可以通过树形DP统计满足条件的三元组数量
 * 2. DP状态设计:
 *    - f[u][d]: 以u为根的子树中，到u距离为d的节点数
 *    - g[u][d]: 以u为根的子树中，已经选了两个点，还需要一个距离u为d的点就能构成合法三元组的方案数
 * 3. 状态转移:
 *    - 当处理节点u和其子节点v时，需要考虑三种情况：
 *      a) 在v子树中选三个点（u未被选中）
 *      b) 在u子树中选两个点，在v子树中选一个点
 *      c) 在u子树中选一个点，在v子树中选两个点
 * 4. 长链剖分优化:
 *    - 重儿子信息继承：通过指针偏移实现O(1)继承
 *    - 轻儿子信息合并：暴力合并，但每条链只合并一次
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code07_HotHotels1 {

	public static int MAXN = 100001;
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
	// fa[u]: 节点u的父节点
	// son[u]: 节点u的重儿子（子树深度最大的子节点）
	// len[u]: 以u为顶点的长链长度
	// cntd: dfs序计数器
	public static int[] fa = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] len = new int[MAXN];
	public static int cntd = 0;

	// 动态规划数组
	// fid[u]: 节点u的f数组在全局数组中的起始位置
	// gid[u]: 节点u的g数组在全局数组中的起始位置
	// f[i]: 全局f数组，f[父][i]依赖f[子][i-1]
	// g[i]: 全局g数组，g[父][i]依赖g[子][i+1]
	// ans: 答案计数器
	public static int[] fid = new int[MAXN]; // 每个点在动态规划表f中的开始位置，就是dfn序
	public static int[] gid = new int[MAXN]; // 每个点在动态规划表g中的开始位置，课上讲的设计
	public static long[] f = new long[MAXN]; // 动态规划表f，f[父][i]依赖f[子][i-1]
	public static long[] g = new long[MAXN << 1]; // 动态规划表g，g[父][i]依赖g[子][i+1]
	public static long ans = 0; // 答案

	// 设置节点u的第i位f值为v
	// 通过fid和索引实现空间复用
	public static void setf(int u, int i, long v) {
		f[fid[u] + i] = v;
	}

	// 获取节点u的第i位f值
	// 通过fid和索引实现空间复用
	public static long getf(int u, int i) {
		return f[fid[u] + i];
	}

	// 设置节点u的第i位g值为v
	// 通过gid和索引实现空间复用
	public static void setg(int u, int i, long v) {
		g[gid[u] + i] = v;
	}

	// 获取节点u的第i位g值
	// 通过gid和索引实现空间复用
	public static long getg(int u, int i) {
		return g[gid[u] + i];
	}

	// 添加边到链式前向星结构中
	// u: 起点, v: 终点
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 递归版第一次DFS
	// 计算每个节点的父节点、子树信息，并确定重儿子
	// u: 当前节点, f: 父节点
	public static void dfs1(int u, int f) {
		fa[u] = f; // 记录父节点
		// 遍历u的所有子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
			}
		}
		// 确定重儿子：选择子树深度最大的子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				if (son[u] == 0 || len[son[u]] < len[v]) {
					son[u] = v;
				}
			}
		}
		len[u] = len[son[u]] + 1; // 计算以u为顶点的长链长度
	}

	// 递归版第二次DFS
	// 给每个节点分配fid和gid
	// u: 当前节点, top: 当前链的顶部节点
	public static void dfs2(int u, int top) {
		fid[u] = cntd++; // 分配f数组起始位置
		if (son[u] == 0) { // 如果没有重儿子
			// 叶子节点的g数组起始位置设置为链顶节点fid的两倍
			gid[u] = fid[top] * 2;
			return;
		}
		// 优先处理重儿子
		dfs2(son[u], top);
		// 轻儿子作为新链的顶部
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != son[u] && v != fa[u]) { // 不是重儿子且不是父节点
				dfs2(v, v);
			}
		}
		// 非叶子节点的g数组起始位置设置为重儿子gid加1
		gid[u] = gid[son[u]] + 1;
	}

	// 递归版第三次DFS
	// 计算每个节点的f信息和g信息，同时统计答案
	// u: 当前节点
	public static void dfs3(int u) {
		setf(u, 0, 1); // u到自己的距离为0，节点数为1
		if (son[u] == 0) { // 如果没有重儿子，说明是叶子节点
			return;
		}
		// 优先处理重儿子，实现DP信息继承
		dfs3(son[u]);
		// 处理所有轻儿子
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != son[u] && v != fa[u]) { // 不是重儿子且不是父节点
				dfs3(v); // 处理轻儿子
				
				// 情况2，u树上，选择三个点，u没被选中，但跨u选点
				// 遍历轻儿子v的所有深度
				for (int i = 0; i <= len[v]; i++) {
					// 情况2的分支一，之前遍历的子树里选两个点，当前子树里选一个点
					// g[u][i]表示u子树中已选两个点，还需要距离为i的点
					// f[v][i-1]表示v子树中到v距离为i-1的点数（因为u到v距离为1）
					if (i < len[u] && i - 1 >= 0) {
						ans += getg(u, i) * getf(v, i - 1);
					}
					// 情况2的分支二，之前遍历的子树里选一个点，当前子树里选两个点
					// f[u][i]表示u子树中到u距离为i的点数
					// g[v][i+1]表示v子树中已选两个点，还需要距离为i+1的点
					if (i > 0 && i + 1 < len[v]) {
						ans += getf(u, i) * getg(v, i + 1);
					}
				}
				
				// 更新g数组信息
				for (int i = 0; i <= len[v]; i++) {
					// 更新g[u][i-1]: u子树中选两个点，还需要距离为i-1的点
					// 这来自于v子树中选两个点，还需要距离为i+1的点（因为u到v距离为1）
					if (i + 1 < len[v]) {
						setg(u, i, getg(u, i) + getg(v, i + 1));
					}
					// 更新g[u][i]: u子树中选两个点，还需要距离为i的点
					// 这来自于u子树中选一个点，v子树中选一个点（因为u到v距离为1）
					if (i - 1 >= 0) {
						setg(u, i, getg(u, i) + getf(u, i) * getf(v, i - 1));
					}
				}
				
				// 更新f数组信息
				for (int i = 0; i <= len[v]; i++) {
					// 更新f[u][i]: u子树中到u距离为i的点数
					// 这来自于v子树中到v距离为i-1的点数（因为u到v距离为1）
					if (i - 1 >= 0) {
						setf(u, i, getf(u, i) + getf(v, i - 1));
					}
				}
			}
		}
		// 情况1，u树上，选择三个点，u被选中
		// g[u][0]表示u子树中已选两个点，还需要距离为0的点（即u自己）
		ans += getg(u, 0);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		// 读入所有边，构建链式前向星
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs1(1, 0); // 第一次DFS，确定重儿子和父节点
		dfs2(1, 1); // 第二次DFS，分配fid和gid
		dfs3(1);    // 第三次DFS，计算DP值和答案
		out.println(ans); // 输出答案
		out.flush();
		out.close();
		br.close();
	}

}