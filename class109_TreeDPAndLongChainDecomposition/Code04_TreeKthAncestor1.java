package class162;

// 树上k级祖先，java版
// 一共有n个节点，编号1~n，给定一个长度为n的数组arr，表示依赖关系
// 如果arr[i] = j，表示i号节点的父节点是j，如果arr[i] == 0，表示i号节点是树头
// 一共有m条查询，每条查询 x k : 打印x往上走k步的祖先节点编号
// 题目要求预处理的时间复杂度O(n * log n)，处理每条查询的时间复杂度O(1)
// 题目要求强制在线，必须按顺序处理每条查询，如何得到每条查询的入参，请打开测试链接查看
// 1 <= n <= 5 * 10^5
// 1 <= m <= 5 * 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P5903
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 问题分析:
 * 这是一道树上倍增和长链剖分结合的题目，主要涉及:
 * 1. 倍增算法求k级祖先
 * 2. 长链剖分优化
 * 3. 在线查询处理
 *
 * 解题思路:
 * 1. 倍增法: 预处理每个节点的2^i级祖先，查询时通过二进制分解快速找到答案
 * 2. 长链剖分优化: 利用长链剖分的性质，对每条长链预处理向上和向下的信息
 * 3. 查询优化: 结合倍增和长链剖分，实现O(1)查询
 *
 * 时间复杂度: 
 * - 预处理: O(n * log n)
 * - 查询: O(1)
 * 空间复杂度: O(n * log n)
 *
 * 算法详解:
 * 1. 倍增预处理:
 *    - stjump[u][i]: 节点u的第2^i级祖先
 *    - 通过递推关系: stjump[u][i] = stjump[stjump[u][i-1]][i-1] 计算
 * 2. 长链剖分:
 *    - 第一次DFS(dfs1/dfs3): 计算每个节点的子树深度并确定重儿子
 *    - 第二次DFS(dfs2/dfs4): 进行长链剖分，为每个节点分配dfs序
 * 3. 信息预处理:
 *    - 对每条长链预处理向上和向下的信息，存储在up和down数组中
 * 4. 查询处理:
 *    - query(x, k): 查询节点x的第k级祖先
 *    - 利用倍增表和长链信息快速定位答案
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_TreeKthAncestor1 {

	public static int MAXN = 500001;
	public static int MAXH = 20; // 最大深度，log2(5*10^5) ≈ 19
	public static int n, m;
	public static long s;
	public static int root;

	// 链式前向星，注意是有向图，所以边的数量不需要增倍
	// head[u]: 节点u的第一条边的索引
	// next[i]: 第i条边的下一条边索引
	// to[i]: 第i条边指向的节点
	// cntg: 边的计数器
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN];
	public static int[] to = new int[MAXN];
	public static int cntg = 0;

	// 倍增表 + 长链剖分
	// stjump[u][i]: 节点u的第2^i级祖先
	// dep[u]: 节点u的深度
	// len[u]: 以u为顶点的长链长度
	// son[u]: 节点u的重儿子（子树深度最大的子节点）
	// top[u]: 节点u所在长链的顶部节点
	// dfn[u]: 节点u的dfs序号
	// cntd: dfs序计数器
	public static int[][] stjump = new int[MAXN][MAXH];
	public static int[] dep = new int[MAXN];
	public static int[] len = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] top = new int[MAXN];
	public static int[] dfn = new int[MAXN];
	public static int cntd = 0;

	// 查询答案需要的辅助数组
	// high[i]: i的最高位位置，用于二进制分解
	// up[u]: 节点u向上第i步的祖先节点存储位置
	// down[u]: 节点u向下第i步的节点存储位置
	public static int[] high = new int[MAXN];
	public static int[] up = new int[MAXN];
	public static int[] down = new int[MAXN];

	// 题目规定如何得到输入数据的函数
	// C++中有无符号整数，java中没有，选择用long类型代替
	// 通过线性同余生成器生成查询数据
	public static long get(long x) {
		x ^= x << 13;
		x &= 0xffffffffL;
		x ^= x >>> 17;
		x ^= x << 5;
		x &= 0xffffffffL;
		return s = x;
	}

	// 设置up数组的值
	// u: 节点编号, i: 步数, v: 祖先节点编号
	public static void setUp(int u, int i, int v) {
		up[dfn[u] + i] = v;
	}

	// 获取up数组的值
	// u: 节点编号, i: 步数
	public static int getUp(int u, int i) {
		return up[dfn[u] + i];
	}

	// 设置down数组的值
	// u: 节点编号, i: 步数, v: 子节点编号
	public static void setDown(int u, int i, int v) {
		down[dfn[u] + i] = v;
	}

	// 获取down数组的值
	// u: 节点编号, i: 步数
	public static int getDown(int u, int i) {
		return down[dfn[u] + i];
	}

	// 添加边到链式前向星结构中
	// u: 起点, v: 终点
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 递归版第一次DFS
	// 计算每个节点的父节点、深度、子树深度，并确定重儿子
	// u: 当前节点, f: 父节点
	public static void dfs1(int u, int f) {
		stjump[u][0] = f; // 直接父节点
		// 预处理倍增表: stjump[u][i] = stjump[stjump[u][i-1]][i-1]
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		dep[u] = dep[f] + 1; // 计算深度
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
	// 进行长链剖分，为每个节点分配dfs序和链顶节点
	// u: 当前节点, t: 当前链的顶部节点
	public static void dfs2(int u, int t) {
		top[u] = t; // 设置链顶
		dfn[u] = ++cntd; // 分配dfs序
		if (son[u] == 0) { // 如果没有重儿子，说明是叶子节点
			return;
		}
		// 优先处理重儿子，保持长链的连续性
		dfs2(son[u], t);
		// 处理所有轻儿子，每个轻儿子作为新链的顶部
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != stjump[u][0] && v != son[u]) { // 不是父节点且不是重儿子
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
		push(root, 0, -1); // 从根节点开始，父节点为0，边索引为-1表示初次访问
		while (stacksize > 0) {
			pop();
			if (edge == -1) { // 初次访问节点
				stjump[first][0] = second; // 直接父节点
				// 预处理倍增表
				for (int p = 1; p < MAXH; p++) {
					stjump[first][p] = stjump[stjump[first][p - 1]][p - 1];
				}
				dep[first] = dep[second] + 1; // 计算深度
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
				// 确定重儿子：选择子树深度最大的子节点
				for (int e = head[first], v; e > 0; e = next[e]) {
					v = to[e];
					if (v != second) {
						if (son[first] == 0 || len[son[first]] < len[v]) {
							son[first] = v;
						}
					}
				}
				len[first] = len[son[first]] + 1; // 计算以first为顶点的长链长度
			}
		}
	}

	// dfs2的迭代版 - 避免递归深度过大导致栈溢出
	public static void dfs4() {
		stacksize = 0;
		push(root, root, -1); // 从根节点开始，链顶为root
		while (stacksize > 0) {
			pop();
			if (edge == -1) { // edge == -1，表示第一次来到当前节点，并且先处理重儿子
				top[first] = second;
				dfn[first] = ++cntd;
				if (son[first] == 0) { // 如果没有重儿子
					continue;
				}
				push(first, second, -2); // 标记需要处理轻儿子
				push(son[first], second, -1); // 优先处理重儿子
				continue;
			} else if (edge == -2) { // edge == -2，表示处理完当前节点的重儿子，回到了当前节点
				edge = head[first];
			} else { // edge >= 0, 继续处理其他的边
				edge = next[edge];
			}
			if (edge != 0) {
				push(first, second, edge);
				// 处理轻儿子，每个轻儿子作为新链的顶部
				if (to[edge] != stjump[first][0] && to[edge] != son[first]) {
					push(to[edge], to[edge], -1);
				}
			}
		}
	}

	// 预处理函数 - 构建倍增表和长链信息
	public static void prepare() {
		dfs3(); // 迭代版第一次DFS
		dfs4(); // 迭代版第二次DFS
		// 预处理high数组：high[i]表示i的最高位位置
		high[0] = -1;
		for (int i = 1; i <= n; i++) {
			high[i] = high[i / 2] + 1;
		}
		// 对每条长链预处理向上和向下的信息
		for (int u = 1; u <= n; u++) {
			if (top[u] == u) { // 如果u是长链的顶部
				// 预处理向上信息：从u开始向上走i步的祖先
				// 预处理向下信息：从u开始向下走i步的节点
				for (int i = 0, a = u, b = u; i < len[u]; i++, a = stjump[a][0], b = son[b]) {
					setUp(u, i, a);
					setDown(u, i, b);
				}
			}
		}
	}

	// 查询节点x的第k级祖先
	// 利用倍增表和长链信息快速定位答案
	public static int query(int x, int k) {
		if (k == 0) { // 特殊情况：0级祖先就是自己
			return x;
		}
		// 如果k是2的幂次，可以直接使用倍增表
		if (k == 1 << high[k]) {
			return stjump[x][high[k]];
		}
		// 一般情况：先跳到最近的链顶，再在链上查找
		x = stjump[x][high[k]]; // 先跳2^high[k]步
		k -= 1 << high[k]; // 剩余步数
		// 计算在链上的相对位置
		k -= dep[x] - dep[top[x]];
		x = top[x]; // 跳到链顶
		// 根据剩余步数确定祖先位置
		return (k >= 0) ? getUp(x, k) : getDown(x, -k);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		in.nextToken();
		s = (long) in.nval;
		// 构建树结构
		for (int i = 1, father; i <= n; i++) {
			in.nextToken();
			father = (int) in.nval;
			if (father == 0) {
				root = i; // 确定根节点
			} else {
				addEdge(father, i); // 添加有向边
			}
		}
		prepare(); // 预处理
		long ans = 0;
		// 处理所有查询
		for (int i = 1, x, k, lastAns = 0; i <= m; i++) {
			// 通过线性同余生成器生成查询参数
			x = (int) ((get(s) ^ lastAns) % n + 1);
			k = (int) ((get(s) ^ lastAns) % dep[x]);
			lastAns = query(x, k); // 查询答案
			ans ^= (long) i * lastAns; // 累加异或结果
		}
		out.println(ans);
		out.flush();
		out.close();
		br.close();
	}

}