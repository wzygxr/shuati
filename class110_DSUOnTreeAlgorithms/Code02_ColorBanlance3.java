package class163;

// 颜色平衡的子树，C++版（基于Java实现的C++版本）
// 题目来源: 洛谷 P9233
// 题目链接: https://www.luogu.com.cn/problem/P9233
// 
// 题目大意:
// 一共有n个节点，编号1~n，给定每个节点的颜色值和父亲节点编号
// 输入保证所有节点一定组成一棵树，并且1号节点是树头
// 如果一棵子树中，存在的每种颜色的节点个数都相同，这棵子树叫颜色平衡树
// 打印整棵树中有多少个子树是颜色平衡树
// 1 <= n、颜色值 <= 2 * 10^5
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每种颜色的出现次数，以及每种出现次数的颜色数量
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 离线处理所有查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 颜色平衡判断:
// 1. 维护每种颜色的出现次数(colorCnt)
// 2. 维护每种出现次数的颜色数量(colorNum)
// 3. 当colorCnt[color[u]] * colorNum[colorCnt[color[u]]] == siz[u]时，说明所有颜色出现次数相同
//
// 与Java版本的区别:
// 1. C++版本使用数组和指针，性能更优
// 2. C++版本使用iostream进行输入输出
// 3. C++版本使用全局变量，避免了类的开销
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
//
// 一共有n个节点，编号1~n，给定每个节点的颜色值和父亲节点编号
// 输入保证所有节点一定组成一棵树，并且1号节点是树头
// 如果一棵子树中，存在的每种颜色的节点个数都相同，这棵子树叫颜色平衡树
// 打印整棵树中有多少个子树是颜色平衡树
// 1 <= n、颜色值 <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P9233
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_ColorBanlance3 {

	public static int MAXN = 200001;
	public static int n;
	public static int[] color = new int[MAXN];
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN];
	public static int[] to = new int[MAXN];
	public static int cnt = 0;
	public static int[] siz = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] colorCnt = new int[MAXN];
	public static int[] colorNum = new int[MAXN];
	public static int ans = 0;

	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	public static void dfs1(int u) {
		siz[u] = 1;
		for (int e = head[u]; e > 0; e = next[e]) {
			dfs1(to[e]);
		}
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			siz[u] += siz[v];
			if (son[u] == 0 || siz[son[u]] < siz[v]) {
				son[u] = v;
			}
		}
	}

	public static void effect(int u) {
		colorCnt[color[u]]++;
		colorNum[colorCnt[color[u]] - 1]--;
		colorNum[colorCnt[color[u]]]++;
		for (int e = head[u]; e > 0; e = next[e]) {
			effect(to[e]);
		}
	}

	public static void cancel(int u) {
		colorCnt[color[u]]--;
		colorNum[colorCnt[color[u]] + 1]--;
		colorNum[colorCnt[color[u]]]++;
		for (int e = head[u]; e > 0; e = next[e]) {
			cancel(to[e]);
		}
	}

	public static void dfs2(int u, int keep) {
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != son[u]) {
				dfs2(v, 0);
			}
		}
		if (son[u] != 0) {
			dfs2(son[u], 1);
		}
		colorCnt[color[u]]++;
		colorNum[colorCnt[color[u]] - 1]--;
		colorNum[colorCnt[color[u]]]++;
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != son[u]) {
				effect(v);
			}
		}
		if (colorCnt[color[u]] * colorNum[colorCnt[color[u]]] == siz[u]) {
			ans++;
		}
		if (keep == 0) {
			cancel(u);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		for (int i = 1, father; i <= n; i++) {
			in.nextToken();
			color[i] = (int) in.nval;
			in.nextToken();
			father = (int) in.nval;
			if (i != 1) {
				addEdge(father, i);
			}
		}
		dfs1(1);
		dfs2(1, 0);
		out.println(ans);
		out.flush();
		out.close();
		br.close();
	}

}