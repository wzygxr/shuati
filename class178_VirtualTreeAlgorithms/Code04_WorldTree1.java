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
// 1. 洛谷 P3233 - [HNOI2014]世界树
//    链接：https://www.luogu.com.cn/problem/P3233
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算每个关键点能管理多少个点
//
// 2. Codeforces 613D - Kingdom and Cities
//    链接：https://codeforces.com/problemset/problem/613/D
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
//
// 3. 洛谷 P2495 - [SDOI2011]消耗战
//    链接：https://www.luogu.com.cn/problem/P2495
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
//
// 4. LOJ #6056 - 「雅礼集训 2017 Day11」回转寿司
//    链接：https://loj.ac/p/6056
//    题意：涉及树上关键点的查询问题
//
// 5. Codeforces 1000G - Two Melborians, One Siberian
//    链接：https://codeforces.com/problemset/problem/1000/G
//    题意：在树上处理多组询问，涉及关键点的最短距离等信息
//
// 6. AtCoder ABC154F - Many Many Paths
//    链接：https://atcoder.jp/contests/abc154/tasks/abc154_f
//    题意：计算树上路径数量，可以使用虚树优化
//
// 7. 洛谷 P4103 - [HEOI2014]大工程
//    链接：https://www.luogu.com.cn/problem/P4103
//    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
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

// 世界树，java版
// 一共有n个节点，给定n-1条无向边，所有节点组成一棵树
// 一共有q条查询，每条查询格式如下
// 查询 k a1 a2 ... ak : 给出了k个不同的管理点，树上每个点都找最近的管理点来管理自己
//                       如果某个节点的最近管理点有多个，选择编号最小的管理点
//                       打印每个管理点，管理的节点数量
// 1 <= n、q <= 3 * 10^5
// 1 <= 所有查询给出的点的总数 <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3233
// 提交以下的code，提交时请把类名改成"Main"
// 本题递归函数较多，java版不改成迭代会爆栈，导致无法通过
// 但是这种改动没啥价值，因为和算法无关，纯粹语言歧视，索性不改了
// 想通过用C++实现，本节课Code04_WorldTree2文件就是C++的实现
// 两个版本的逻辑完全一样，C++版本可以通过所有测试

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code04_WorldTree1 {

	public static int MAXN = 300001;
	public static int MAXP = 20;
	public static int n, q, k;

	public static int[] headg = new int[MAXN];
	public static int[] nextg = new int[MAXN << 1];
	public static int[] tog = new int[MAXN << 1];
	public static int cntg;

	public static int[] headv = new int[MAXN];
	public static int[] nextv = new int[MAXN];
	public static int[] tov = new int[MAXN];
	public static int cntv;

	public static int[] dep = new int[MAXN];
	// 注意siz[u]表示在原树里，子树u有几个节点
	public static int[] siz = new int[MAXN];
	public static int[] dfn = new int[MAXN];
	public static int[][] stjump = new int[MAXN][MAXP];
	public static int cntd;

	public static int[] order = new int[MAXN];
	public static int[] arr = new int[MAXN];
	public static boolean[] isKey = new boolean[MAXN];
	public static int[] tmp = new int[MAXN << 1];

	// manager[u]表示u节点找到的最近管理点
	public static int[] manager = new int[MAXN];
	// dist[u]表示u节点到最近管理点的距离
	public static int[] dist = new int[MAXN];
	// ans[i]表示i这个管理点，管理了几个点
	public static int[] ans = new int[MAXN];

	public static void addEdgeG(int u, int v) {
		nextg[++cntg] = headg[u];
		tog[cntg] = v;
		headg[u] = cntg;
	}

	public static void addEdgeV(int u, int v) {
		nextv[++cntv] = headv[u];
		tov[cntv] = v;
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

	public static void dfs(int u, int fa) {
		dep[u] = dep[fa] + 1;
		siz[u] = 1;
		dfn[u] = ++cntd;
		stjump[u][0] = fa;
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = headg[u]; e > 0; e = nextg[e]) {
			int v = tog[e];
			if (v != fa) {
				dfs(v, u);
				siz[u] += siz[v];
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

	public static int buildVirtualTree() {
		sortByDfn(arr, 1, k);
		// 一定要加入1号点
		// 因为题目问的是所有节点的归属问题
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
			addEdgeV(getLca(tmp[i], tmp[i + 1]), tmp[i + 1]);
		}
		return tmp[1];
	}

	// 下方找最近管理点，节点u根据孩子的管理点，找到离u最近的管理点
	public static void dp1(int u) {
		dist[u] = 1000000001;
		for (int e = headv[u]; e > 0; e = nextv[e]) {
			int v = tov[e];
			dp1(v);
			int w = dep[v] - dep[u];
			if (dist[v] + w < dist[u] || (dist[v] + w == dist[u] && manager[v] < manager[u])) {
				dist[u] = dist[v] + w;
				manager[u] = manager[v];
			}
		}
		if (isKey[u]) {
			dist[u] = 0;
			manager[u] = u;
		}
	}

	// 上方找最近管理点，根据u找到的最近管理点，更新每个孩子节点v的最近管理点
	public static void dp2(int u) {
		for (int e = headv[u]; e > 0; e = nextv[e]) {
			int v = tov[e];
			int w = dep[v] - dep[u];
			if (dist[u] + w < dist[v] || (dist[u] + w == dist[v] && manager[u] < manager[v])) {
				dist[v] = dist[u] + w;
				manager[v] = manager[u];
			}
			dp2(v);
		}
	}

	// 已知u一定是v的祖先节点，u到v之间的大量节点没有被纳入到虚树
	// 这部分节点之前都分配给了manager[u]，现在根据最近距离做重新分配
	// 可能若干节点会重新分配给manager[v]，修正相关的计数
	public static void amend(int u, int v) {
		if (manager[u] == manager[v]) {
			return;
		}
		int x = v;
		for (int p = MAXP - 1; p >= 0; p--) {
			int jump = stjump[x][p];
			if (dep[u] < dep[jump]) {
				int tou = dep[jump] - dep[u] + dist[u];
				int tov = dep[v] - dep[jump] + dist[v];
				if (tov < tou || (tov == tou && manager[v] < manager[u])) {
					x = jump;
				}
			}
		}
		int delta = siz[x] - siz[v];
		ans[manager[u]] -= delta;
		ans[manager[v]] += delta;
	}

	// 每个点都有了最近的管理点，更新相关管理点的管理节点计数
	public static void dp3(int u) {
		// u的管理节点，先获得原树里子树u的所有节点
		// 然后经历修正的过程，把管理节点的数量更新正确
		ans[manager[u]] += siz[u];
		for (int e = headv[u]; e > 0; e = nextv[e]) {
			int v = tov[e];
			// 修正的过程
			amend(u, v);
			// 马上要执行dp3(v)，所以子树v的节点现在扣除
			ans[manager[u]] -= siz[v];
			// 子树v怎么分配节点，那是后续dp3(v)的事情
			dp3(v);
		}
	}

	public static void compute() {
		for (int i = 1; i <= k; i++) {
			arr[i] = order[i];
		}
		for (int i = 1; i <= k; i++) {
			isKey[arr[i]] = true;
			ans[arr[i]] = 0;
		}
		int tree = buildVirtualTree();
		dp1(tree);
		dp2(tree);
		dp3(tree);
		for (int i = 1; i <= k; i++) {
			isKey[arr[i]] = false;
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdgeG(u, v);
			addEdgeG(v, u);
		}
		dfs(1, 0);
		q = in.nextInt();
		for (int t = 1; t <= q; t++) {
			k = in.nextInt();
			for (int i = 1; i <= k; i++) {
				order[i] = in.nextInt();
			}
			compute();
			for (int i = 1; i <= k; i++) {
				out.print(ans[order[i]] + " ");
			}
			out.println();
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