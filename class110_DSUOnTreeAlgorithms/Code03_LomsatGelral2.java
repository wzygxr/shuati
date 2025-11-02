package class163;

// 主导颜色累加和，C++版（基于Java实现的C++版本）
// 题目来源: Codeforces 600E / 洛谷 CF600E
// 题目链接: https://codeforces.com/problemset/problem/600/E
// 题目链接: https://www.luogu.com.cn/problem/CF600E
// 
// 题目大意:
// 一共有n个节点，编号1~n，给定n-1条边，所有节点连成一棵树，1号节点为树头
// 每个节点给定一种颜色值，主导颜色累加和定义如下
// 以x为头的子树上，哪种颜色出现最多，那种颜色就是主导颜色，主导颜色可能不止一种
// 所有主导颜色的值累加起来，每个主导颜色只累加一次，就是该子树的主导颜色累加和
// 打印1~n每个节点为头的子树的主导颜色累加和
// 1 <= n、颜色值 <= 10^5
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每种颜色的出现次数，以及最大出现次数和对应的累加和
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
// 主导颜色处理:
// 1. 维护每种颜色的出现次数(colorCnt)
// 2. 维护当前最大出现次数(maxCnt)
// 3. 维护主导颜色的累加和(ans)
// 4. 当颜色出现次数等于最大出现次数时，累加到答案中
// 5. 当颜色出现次数大于最大出现次数时，更新最大出现次数并重置答案
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
// 测试链接 : https://www.luogu.com.cn/problem/CF600E
// 测试链接 : https://codeforces.com/problemset/problem/600/E
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_LomsatGelral2 {

	public static int MAXN = 100001;
	public static int n;
	public static int[] color = new int[MAXN];

	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cnt = 0;

	public static int[] fa = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[] son = new int[MAXN];

	public static int[] colorCnt = new int[MAXN];
	public static int[] maxCnt = new int[MAXN];
	public static long[] ans = new long[MAXN];

	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	public static void dfs1(int u, int f) {
		fa[u] = f;
		siz[u] = 1;
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
			}
		}
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				siz[u] += siz[v];
				if (son[u] == 0 || siz[son[u]] < siz[v]) {
					son[u] = v;
				}
			}
		}
	}

	public static void effect(int u, int h) {
		colorCnt[color[u]]++;
		if (colorCnt[color[u]] == maxCnt[h]) {
			ans[h] += color[u];
		} else if (colorCnt[color[u]] > maxCnt[h]) {
			maxCnt[h] = colorCnt[color[u]];
			ans[h] = color[u];
		}
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa[u]) {
				effect(v, h);
			}
		}
	}

	public static void cancel(int u) {
		colorCnt[color[u]] = 0;
		maxCnt[u] = 0;
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa[u]) {
				cancel(v);
			}
		}
	}

	public static void dfs2(int u, int keep) {
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa[u] && v != son[u]) {
				dfs2(v, 0);
			}
		}
		if (son[u] != 0) {
			dfs2(son[u], 1);
		}
		maxCnt[u] = maxCnt[son[u]];
		ans[u] = ans[son[u]];
		colorCnt[color[u]]++;
		if (colorCnt[color[u]] == maxCnt[u]) {
			ans[u] += color[u];
		} else if (colorCnt[color[u]] > maxCnt[u]) {
			maxCnt[u] = colorCnt[color[u]];
			ans[u] = color[u];
		}
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa[u] && v != son[u]) {
				effect(v, u);
			}
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
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			color[i] = (int) in.nval;
		}
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs1(1, 0);
		dfs2(1, 0);
		for (int i = 1; i <= n; i++) {
			out.print(ans[i] + " ");
		}
		out.println();
		out.flush();
		out.close();
		br.close();
	}

}