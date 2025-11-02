package class180;

// 虚树(Virtual Tree)算法详解与应用
// 
// 虚树是一种优化技术，用于解决树上多次询问的问题，每次询问涉及部分关键点
// 虚树只保留关键点及其两两之间的LCA，节点数控制在O(k)级别，从而提高效率
//
// 算法核心思想：
// 1. 虚树包含所有关键点和它们两两之间的LCA
// 2. 虚树的节点数不超过2*k-1（k为关键点数）
// 3. 在虚树上进行DP等操作，避免遍历整棵树
//
// 构造方法：
// 方法一：二次排序法
// 1. 将关键点按DFS序排序
// 2. 相邻点求LCA并加入序列
// 3. 再次排序并去重得到虚树所有节点
// 4. 按照父子关系连接节点
//
// 方法二：单调栈法
// 1. 将关键点按DFS序排序
// 2. 用栈维护虚树的一条链
// 3. 逐个插入关键点并维护栈结构
//
// 应用场景：
// 1. 树上多次询问，每次询问涉及部分关键点
// 2. 需要在关键点及其LCA上进行DP等操作
// 3. 数据范围要求∑k较小（通常≤10^5）
//
// 相关题目：
// 1. 洛谷 P2495 - [SDOI2011]消耗战
//    链接：https://www.luogu.com.cn/problem/P2495
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
//
// 2. Codeforces 613D - Kingdom and Cities
//    链接：https://codeforces.com/problemset/problem/613/D
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
//
// 3. LOJ #6056 - 「雅礼集训 2017 Day11」回转寿司
//    链接：https://loj.ac/p/6056
//    题意：涉及树上关键点的查询问题
//
// 4. Codeforces 1000G - Two Melborians, One Siberian
//    链接：https://codeforces.com/problemset/problem/1000/G
//    题意：在树上处理多组询问，涉及关键点的最短距离等信息
//
// 5. AtCoder ABC154F - Many Many Paths
//    链接：https://atcoder.jp/contests/abc154/tasks/abc154_f
//    题意：计算树上路径数量，可以使用虚树优化
//
// 6. 洛谷 P4103 - [HEOI2014]大工程
//    链接：https://www.luogu.com.cn/problem/P4103
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
//
// 7. 洛谷 P3233 - [HNOI2014]世界树
//    链接：https://www.luogu.com.cn/problem/P3233
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算每个关键点能管理多少个点
//
// 8. Codeforces 1109D - Treeland and Viruses
//    链接：https://codeforces.com/problemset/problem/1109/D
//    题意：给一棵树和多个病毒源点，每个病毒源点以不同速度扩散，求每个点被哪个病毒源点感染
//
// 9. 洛谷 P3320 - [SDOI2015]寻宝游戏
//    链接：https://www.luogu.com.cn/problem/P3320
//    题意：给一棵树和多个操作，每次操作翻转一个点的状态，求收集所有宝藏的最短路径长度
//
// 10. 洛谷 P5327 - [ZJOI2019]语言
//    链接：https://www.luogu.com.cn/problem/P5327
//    题意：涉及树上路径覆盖的复杂问题
//
// 11. SPOJ QTREE5 - Query on a tree V
//    链接：https://www.spoj.com/problems/QTREE5/
//    题意：树上点颜色修改和查询距离最近的白色节点
//
// 12. 洛谷 P3232 - [HNOI2013]游走
//    链接：https://www.luogu.com.cn/problem/P3232
//    题意：给定无向连通图，通过高斯消元计算边的期望经过次数，再贪心编号使总得分期望最小
//
// 时间复杂度分析：
// 1. 预处理（DFS序、LCA）：O(n log n)
// 2. 构造虚树：O(k log k)
// 3. 在虚树上DP：O(k)
// 总体复杂度：O(n log n + ∑k log k)
//
// 空间复杂度：O(n + k)
//
// 工程化考量：
// 1. 注意虚树边通常没有边权，需要通过原树计算
// 2. 清空关键点标记时避免使用memset，用for循环逐个清除
// 3. 排序后的关键点顺序不是原节点序，如需按原序输出需额外保存
// 4. 虚树主要用于卡常题，需注意常数优化
//
// 算法设计本质与核心思想：
// 1. 设计动机：虚树算法的核心动机是优化树上多次询问问题。当需要对树上不同关键点集合进行多次查询时，
//    如果每次都遍历整棵树，时间复杂度会很高。虚树通过只保留关键点及其LCA，将问题规模从O(n)降低到O(k)。
// 2. 数学原理：
//    - LCA性质：任意两个节点的LCA在DFS序上具有特定性质，可以用于构建虚树
//    - 节点数量上界：虚树节点数不超过2*k-1，这是通过数学归纳法可以证明的
//    - 树的结构保持：虚树保持了原树中关键点之间的祖先关系
// 3. 与其它算法的关联：
//    - 树上倍增：虚树构建需要LCA，通常使用树上倍增算法
//    - 树形DP：虚树上的动态规划是解决问题的核心
//    - 单调栈：构建虚树时使用的单调栈技巧与其它算法中的单调栈类似
// 4. 工程化应用：
//    - 内存优化：避免使用全局数组清零，用循环逐个清除
//    - 常数优化：选择合适的虚树构建方法（单调栈法通常更快）
//    - 边界处理：正确处理根节点、叶子节点等特殊情况
//
// 语言特性差异与跨语言实现：
// 1. Java实现特点：
//    - 使用对象封装，代码结构清晰
//    - 自定义FastReader提高输入效率
//    - 递归深度可能受限，需要改用迭代实现
// 2. C++实现特点：
//    - 性能最优，适合大数据量
//    - 需要注意编译环境问题，避免使用复杂STL
//    - 指针操作灵活但需谨慎
// 3. Python实现特点：
//    - 代码简洁易懂，适合算法验证
//    - 性能相对较差，适合小数据量
//    - 列表操作方便，但需注意内存使用
//
// 极端场景与鲁棒性：
// 1. 空输入处理：关键点为空时的特殊处理
// 2. 极端数据规模：关键点数量接近节点总数、树退化为链的情况、深度很大的树结构
// 3. 边界条件：关键点包含根节点、关键点之间存在父子关系、关键点相邻的情况
//
// 性能优化策略：
// 1. 算法层面优化：选择合适的虚树构建方法、优化DP状态转移方程、预处理减少重复计算
// 2. 实现层面优化：减少函数调用开销、优化内存访问模式、使用位运算等底层优化技巧
// 3. 工程层面优化：输入输出优化、内存池技术、缓存友好设计
//
// 调试技巧与问题定位：
// 1. 中间过程打印：打印DFS序、打印LCA计算结果、打印虚树构建过程
// 2. 断言验证：验证虚树节点数量上界、验证关键点标记正确性、验证DP状态转移正确性
// 3. 性能分析：使用性能分析工具定位瓶颈、对比不同实现的性能差异、分析时间复杂度常数项影响

// 消耗战，java版
// 一共有n个节点，给定n-1条无向边，每条边有边权，所有节点组成一棵树
// 一共有q条查询，每条查询格式如下
// 查询 k a1 a2 ... ak : 给出了k个不同的关键节点，并且一定不包含1号节点
//                       你可以随意选择边进行切断，切断的代价就是边权
//                       目的是让所有关键点都无法到达1号节点，打印最小总代价
// 1 <= n、q <= 5 * 10^5
// 1 <= 所有查询给出的点的总数 <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P2495
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_War1 {

	public static int MAXN = 500001;
	public static int MAXP = 20;
	public static int n, q, k;

	public static int[] headg = new int[MAXN];
	public static int[] nextg = new int[MAXN << 1];
	public static int[] tog = new int[MAXN << 1];
	public static int[] weightg = new int[MAXN << 1];
	public static int cntg;

	public static int[] headv = new int[MAXN];
	public static int[] nextv = new int[MAXN];
	public static int[] tov = new int[MAXN];
	public static int[] weightv = new int[MAXN];
	public static int cntv;

	public static int[] dep = new int[MAXN];
	public static int[] dfn = new int[MAXN];
	public static int[][] stjump = new int[MAXN][MAXP];
	// 上方最小距离的倍增表
	public static int[][] mindist = new int[MAXN][MAXP];
	public static int cntd;

	public static int[] arr = new int[MAXN];
	public static boolean[] isKey = new boolean[MAXN];
	public static int[] tmp = new int[MAXN << 1];
	public static int[] stk = new int[MAXN];

	// cost[u]表示虚树中，u下方的所有关键节点，都连不上u的话，最小总代价
	public static long[] cost = new long[MAXN];

	public static void addEdgeG(int u, int v, int w) {
		nextg[++cntg] = headg[u];
		tog[cntg] = v;
		weightg[cntg] = w;
		headg[u] = cntg;
	}

	public static void addEdgeV(int u, int v, int w) {
		nextv[++cntv] = headv[u];
		tov[cntv] = v;
		weightv[cntv] = w;
		headv[u] = cntv;
	}

	public static void sortByDfn(int[] nums, int l, int r) {
		if (l >= r) return;
		int i = l, j = r;
		int pivot = nums[(l + r) >> 1];
		while (i <= j) {
			while (dfn[nums[i]] < dfn[pivot]) i++;
			while (dfn[nums[j]] > dfn[pivot]) j--;
			if (i <= j) {
				int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
				i++; j--;
			}
		}
		sortByDfn(nums, l, j);
		sortByDfn(nums, i, r);
	}

	public static void dfs(int u, int fa, int w) {
		dep[u] = dep[fa] + 1;
		dfn[u] = ++cntd;
		stjump[u][0] = fa;
		mindist[u][0] = w;
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
			mindist[u][p] = Math.min(mindist[u][p - 1], mindist[stjump[u][p - 1]][p - 1]);
		}
		for (int e = headg[u]; e > 0; e = nextg[e]) {
			if (tog[e] != fa) {
				dfs(tog[e], u, weightg[e]);
			}
		}
	}

	public static int getLca(int a, int b) {
		if (dep[a] < dep[b]) {
			int tmp = a; a = b; b = tmp;
		}
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[a][p]] >= dep[b]) {
				a = stjump[a][p];
			}
		}
		if (a == b) {
			return a;
		}
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		return stjump[a][0];
	}

	// 已知u一定是v的祖先节点，返回u到v路径上的最小边权
	public static int getDist(int u, int v) {
		int dist = 100000001;
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[v][p]] >= dep[u]) {
				dist = Math.min(dist, mindist[v][p]);
				v = stjump[v][p];
			}
		}
		return dist;
	}

	// 二次排序 + LCA连边的方式建立虚树
	public static int buildVirtualTree1() {
		sortByDfn(arr, 1, k);
		// 因为题目是让所有关键点不能和1号点连通
		// 所以一定要让1号点加入
		int len = 0;
		tmp[++len] = 1;
		for (int i = 1; i < k; i++) {
			tmp[++len] = arr[i];
			tmp[++len] = getLca(arr[i], arr[i + 1]);
		}
		tmp[++len] = arr[k];
		sortByDfn(tmp, 1, len);
		int unique = 1;
		for (int i = 2; i <= len; i++) {
			if (tmp[unique] != tmp[i]) {
				tmp[++unique] = tmp[i];
			}
		}
		cntv = 0;
		for (int i = 1; i <= unique; i++) {
			headv[tmp[i]] = 0;
		}
		for (int i = 1; i < unique; i++) {
			int lca = getLca(tmp[i], tmp[i + 1]);
			addEdgeV(lca, tmp[i + 1], getDist(lca, tmp[i + 1]));
		}
		return tmp[1];
	}

	// 单调栈的方式建立虚树
	public static int buildVirtualTree2() {
		sortByDfn(arr, 1, k);
		// 因为题目是让所有关键点不能和1号点连通
		// 所以一定要让1号点加入
		cntv = 0;
		headv[1] = 0;
		int top = 0;
		stk[++top] = 1;
		for (int i = 1; i <= k; i++) {
			int x = arr[i];
			int y = stk[top];
			int lca = getLca(x, y);
			while (top > 1 && dfn[stk[top - 1]] >= dfn[lca]) {
				addEdgeV(stk[top - 1], stk[top], getDist(stk[top - 1], stk[top]));
				top--;
			}
			if (lca != stk[top]) {
				headv[lca] = 0;
				addEdgeV(lca, stk[top], getDist(lca, stk[top]));
				stk[top] = lca;
			}
			headv[x] = 0;
			stk[++top] = x;
		}
		while (top > 1) {
			addEdgeV(stk[top - 1], stk[top], getDist(stk[top - 1], stk[top]));
			top--;
		}
		return stk[1];
	}

	public static void dp(int u) {
		for (int e = headv[u]; e > 0; e = nextv[e]) {
			dp(tov[e]);
		}
		cost[u] = 0;
		for (int e = headv[u], v, w; e > 0; e = nextv[e]) {
			v = tov[e];
			w = weightv[e];
			if (isKey[v]) {
				cost[u] += w;
			} else {
				cost[u] += Math.min(cost[v], w);
			}
		}
	}

	public static long compute() {
		for (int i = 1; i <= k; i++) {
			isKey[arr[i]] = true;
		}
		int tree = buildVirtualTree1();
		// int tree = buildVirtualTree2();
		dp(tree);
		for (int i = 1; i <= k; i++) {
			isKey[arr[i]] = false;
		}
		return cost[tree];
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt();
			v = in.nextInt();
			w = in.nextInt();
			addEdgeG(u, v, w);
			addEdgeG(v, u, w);
		}
		dfs(1, 0, 0);
		q = in.nextInt();
		for (int t = 1; t <= q; t++) {
			k = in.nextInt();
			for (int i = 1; i <= k; i++) {
				arr[i] = in.nextInt();
			}
			out.println(compute());
		}
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}