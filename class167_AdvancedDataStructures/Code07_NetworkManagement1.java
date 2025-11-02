package class160;

/**
 * 网络管理问题 - 树链剖分 + 树状数组套线段树解法 (Java版本)
 * 
 * 问题描述：
 * 给定一棵包含n个节点的树，每个节点有一个点权。
 * 支持以下两种操作：
 * 1. 更新操作 0 x y：将节点x的点权修改为y
 * 2. 查询操作 k x y：查询节点x到节点y路径上第k大的点权值，如果路径上节点数不足k个，则输出"invalid request!"
 * 
 * 算法思路：
 * 这是一个树上路径第k大查询问题，采用树链剖分 + 树状数组套线段树的解决方案。
 * 
 * 数据结构设计：
 * 1. 使用树链剖分将树上路径查询转化为区间查询问题
 * 2. 外层使用树状数组维护DFS序上的信息
 * 3. 内层使用权值线段树维护每个位置上数字的出现次数
 * 4. 通过离散化处理大数值范围
 * 
 * 核心思想：
 * 1. 通过DFS序将树上操作转化为序列操作
 * 2. 利用树状数组维护前缀信息，在线段树上进行第k大查询
 * 3. 树上路径[x,y]的查询转化为4个DFS序区间的组合操作
 * 
 * 时间复杂度分析：
 * 1. 预处理阶段：O(n log n) - DFS序和离散化排序
 * 2. 单次更新操作：O(log n * log s) - 树状数组更新路径上各节点的线段树操作
 * 3. 单次查询操作：O(log²n * log s) - 树链剖分跳转 + 树状数组查询 + 线段树第k大查询
 * 其中n为节点数，s为离散化后的值域大小
 * 
 * 空间复杂度分析：
 * 1. 存储树结构：O(n)
 * 2. 树状数组：O(n)
 * 3. 线段树节点：最坏情况下O(n * log s)，实际使用中远小于该值
 * 4. 树链剖分辅助数组：O(n)
 * 总体空间复杂度：O(n * log s)
 * 
 * 算法优势：
 * 1. 支持动态修改和查询操作
 * 2. 可以处理任意树上路径查询
 * 3. 相比于树链剖分套线段树，实现更简单
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数因子较大
 * 
 * 适用场景：
 * 1. 树上动态路径第k大查询
 * 2. 树上节点权值可以动态修改
 * 3. 查询和更新操作混合进行
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P4175
 * 
 * 输入格式：
 * 第一行包含两个整数n和m，分别表示节点数和操作数
 * 第二行包含n个整数，表示每个节点的初始点权
 * 接下来n-1行，每行包含两个整数u和v，表示节点u和v之间有一条边
 * 接下来m行，每行描述一个操作：
 *   - "0 x y" 表示更新操作
 *   - "k x y" 表示查询操作（k > 0）
 * 
 * 输出格式：
 * 对于每个查询操作，如果路径上节点数不足k个，输出"invalid request!"，否则输出第k大的点权值
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code07_NetworkManagement1 {

	public static int MAXN = 80001;

	public static int MAXT = MAXN * 110;

	public static int MAXH = 18;

	public static int n, m, s;

	// 节点权值数组
	public static int[] arr = new int[MAXN];

	// 操作记录数组，ques[i][0]表示操作类型(0表示更新，>0表示查询k值)
	// 更新操作：ques[i][1]=x, ques[i][2]=y
	// 查询操作：ques[i][1]=x, ques[i][2]=y, ques[i][0]=k
	public static int[][] ques = new int[MAXN][3];

	// 离散化数组，存储所有可能出现的数值并排序
	public static int[] sorted = new int[MAXN << 1];

	// 链式前向星存储树结构
	public static int[] head = new int[MAXN];  // 邻接表头
	public static int[] next = new int[MAXN << 1];  // 下一条边
	public static int[] to = new int[MAXN << 1];    // 边指向的节点
	public static int cntg = 0;  // 边计数器

	// 树状数组，root[i]表示以节点i为根的线段树根节点编号
	public static int[] root = new int[MAXN];

	// 线段树节点信息
	public static int[] left = new int[MAXT];   // 左子节点编号
	public static int[] right = new int[MAXT];  // 右子节点编号
	public static int[] sum = new int[MAXT];    // 节点维护的区间和（数字出现次数）

	public static int cntt = 0;  // 线段树节点计数器

	// 树链剖分和DFS序相关数组
	public static int[] deep = new int[MAXN];   // 节点深度
	public static int[] size = new int[MAXN];   // 节点子树大小
	public static int[] dfn = new int[MAXN];    // 节点DFS序
	public static int[][] stjump = new int[MAXN][MAXH];  // 倍增跳转表
	public static int cntd = 0;  // DFS序计数器

	// 查询时使用的辅助数组
	public static int[] addTree = new int[MAXN];    // 需要增加计数的线段树根节点
	public static int[] minusTree = new int[MAXN];  // 需要减少计数的线段树根节点

	// 辅助数组元素计数器
	public static int cntadd;
	public static int cntminus;

	/**
	 * 添加一条无向边到链式前向星结构中
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 在已排序的sorted数组中查找数字num的位置（离散化后的值）
	 * @param num 待查找的数字
	 * @return 离散化后的值，如果未找到返回-1
	 */
	public static int kth(int num) {
		int left = 1, right = s, mid;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] == num) {
				return mid;
			} else if (sorted[mid] < num) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return -1;
	}

	/**
	 * 计算树状数组的lowbit值
	 * @param i 输入数字
	 * @return i的lowbit值，即i的二进制表示中最右边的1所代表的数值
	 */
	public static int lowbit(int i) {
		return i & -i;
	}

	/**
	 * DFS递归版，用于计算树链剖分所需信息
	 * 注意：Java版本提交会爆栈，C++版本不会爆栈
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfs1(int u, int fa) {
		deep[u] = deep[fa] + 1;  // 计算节点深度
		size[u] = 1;             // 初始化子树大小
		dfn[u] = ++cntd;         // 分配DFS序
		stjump[u][0] = fa;       // 初始化倍增跳转表
		
		// 构建倍增跳转表
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		
		// 递归处理所有子节点
		for (int e = head[u]; e > 0; e = next[e]) {
			if (to[e] != fa) {
				dfs1(to[e], u);
			}
		}
		
		// 计算子树大小
		for (int e = head[u]; e > 0; e = next[e]) {
			if (to[e] != fa) {
				size[u] += size[to[e]];
			}
		}
	}

	// DFS迭代版相关变量和函数，用于避免递归爆栈
	public static int[][] ufe = new int[MAXN][3];
	public static int stackSize, u, f, e;

	/**
	 * 将状态压入栈中
	 * @param u 当前节点
	 * @param f 父节点
	 * @param e 当前处理的边
	 */
	public static void push(int u, int f, int e) {
		ufe[stackSize][0] = u;
		ufe[stackSize][1] = f;
		ufe[stackSize][2] = e;
		stackSize++;
	}

	/**
	 * 从栈中弹出状态
	 */
	public static void pop() {
		--stackSize;
		u = ufe[stackSize][0];
		f = ufe[stackSize][1];
		e = ufe[stackSize][2];
	}

	/**
	 * DFS迭代版，用于计算树链剖分所需信息，避免递归爆栈
	 */
	public static void dfs2() {
		stackSize = 0;
		push(1, 0, -1);  // 从根节点1开始DFS
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				// 第一次访问节点u
				deep[u] = deep[f] + 1;  // 计算节点深度
				size[u] = 1;            // 初始化子树大小
				dfn[u] = ++cntd;        // 分配DFS序
				stjump[u][0] = f;       // 初始化倍增跳转表
				
				// 构建倍增跳转表
				for (int p = 1; p < MAXH; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				
				e = head[u];  // 开始处理u的邻接边
			} else {
				// 继续处理u的邻接边
				e = next[e];
			}
			
			if (e != 0) {
				// 还有边需要处理
				push(u, f, e);  // 保存当前状态
				if (to[e] != f) {
					// 如果邻接点不是父节点，则继续DFS
					push(to[e], u, -1);
				}
			} else {
				// 所有邻接边处理完毕，计算子树大小
				for (int e = head[u]; e > 0; e = next[e]) {
					if (to[e] != f) {
						size[u] += size[to[e]];
					}
				}
			}
		}
	}

	/**
	 * 计算两个节点的最近公共祖先(LCA)
	 * @param a 节点a
	 * @param b 节点b
	 * @return 节点a和b的最近公共祖先
	 */
	public static int lca(int a, int b) {
		// 确保a的深度不小于b
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		// 将a向上跳到与b同一深度
		for (int p = MAXH - 1; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		
		// 如果a就是b的祖先，直接返回
		if (a == b) {
			return a;
		}
		
		// a和b一起向上跳，直到它们的父节点相同
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		
		// 返回最近公共祖先
		return stjump[a][0];
	}

	/**
	 * 在线段树中增加或减少某个值的计数
	 * @param jobi 需要操作的值（离散化后的索引）
	 * @param jobv 操作的数值（+1表示增加，-1表示减少）
	 * @param l 线段树当前节点维护的区间左端点
	 * @param r 线段树当前节点维护的区间右端点
	 * @param i 线段树当前节点编号（0表示需要新建节点）
	 * @return 更新后的节点编号
	 */
	public static int innerAdd(int jobi, int jobv, int l, int r, int i) {
		if (i == 0) {
			i = ++cntt;  // 新建节点
		}
		if (l == r) {
			sum[i] += jobv;  // 叶子节点，直接更新计数
		} else {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				// 目标值在左半区间
				left[i] = innerAdd(jobi, jobv, l, mid, left[i]);
			} else {
				// 目标值在右半区间
				right[i] = innerAdd(jobi, jobv, mid + 1, r, right[i]);
			}
			// 更新当前节点的计数（左右子树计数之和）
			sum[i] = sum[left[i]] + sum[right[i]];
		}
		return i;
	}

	/**
	 * 在线段树上二分查找第k大的值
	 * @param jobk 查找第k大的值
	 * @param l 当前查询区间左端点
	 * @param r 当前查询区间右端点
	 * @return 第k大值在sorted数组中的索引
	 */
	public static int innerQuery(int jobk, int l, int r) {
		if (l == r) {
			return l;  // 到达叶子节点，返回索引
		}
		int mid = (l + r) / 2;
		
		// 计算所有加法操作在线段树左子树上的计数总和
		int leftsum = 0;
		for (int i = 1; i <= cntadd; i++) {
			leftsum += sum[left[addTree[i]]];
		}
		
		// 减去所有减法操作在线段树左子树上的计数总和
		for (int i = 1; i <= cntminus; i++) {
			leftsum -= sum[left[minusTree[i]]];
		}
		
		if (jobk <= leftsum) {
			// 第k大值在左子树中
			// 更新所有操作涉及的线段树节点为它们的左子节点
			for (int i = 1; i <= cntadd; i++) {
				addTree[i] = left[addTree[i]];
			}
			for (int i = 1; i <= cntminus; i++) {
				minusTree[i] = left[minusTree[i]];
			}
			return innerQuery(jobk, l, mid);
		} else {
			// 第k大值在右子树中
			// 更新所有操作涉及的线段树节点为它们的右子节点
			for (int i = 1; i <= cntadd; i++) {
				addTree[i] = right[addTree[i]];
			}
			for (int i = 1; i <= cntminus; i++) {
				minusTree[i] = right[minusTree[i]];
			}
			return innerQuery(jobk - leftsum, mid + 1, r);
		}
	}

	/**
	 * 在树状数组中增加或减少某个位置上值的计数
	 * @param i DFS序位置
	 * @param val 值（离散化后的索引）
	 * @param cnt 操作数值（+1表示增加，-1表示减少）
	 */
	public static void add(int i, int val, int cnt) {
		for (; i <= n; i += lowbit(i)) {
			root[i] = innerAdd(val, cnt, 1, s, root[i]);
		}
	}

	/**
	 * 更新节点的点权
	 * @param i 需要更新的节点编号
	 * @param v 新的点权值
	 */
	public static void update(int i, int v) {
		// 删除旧值
		add(dfn[i], arr[i], -1);
		add(dfn[i] + size[i], arr[i], 1);
		
		// 更新节点权值
		arr[i] = kth(v);
		
		// 插入新值
		add(dfn[i], arr[i], 1);
		add(dfn[i] + size[i], arr[i], -1);
	}

	/**
	 * 查询树上路径[x, y]中第k大的点权值
	 * @param x 路径起点
	 * @param y 路径终点
	 * @param k 查询第k大
	 * @return 第k大的点权值，如果不存在则返回-1
	 */
	public static int query(int x, int y, int k) {
		// 计算最近公共祖先
		int lca = lca(x, y);
		int lcafa = stjump[lca][0];  // LCA的父节点
		
		// 计算路径上节点数量
		int num = deep[x] + deep[y] - deep[lca] - deep[lcafa];
		
		// 如果路径上节点数不足k个，返回-1
		if (num < k) {
			return -1;
		}
		
		// 初始化辅助数组
		cntadd = cntminus = 0;
		
		// 收集路径x到根节点涉及的树状数组节点
		for (int i = dfn[x]; i > 0; i -= lowbit(i)) {
			addTree[++cntadd] = root[i];
		}
		
		// 收集路径y到根节点涉及的树状数组节点
		for (int i = dfn[y]; i > 0; i -= lowbit(i)) {
			addTree[++cntadd] = root[i];
		}
		
		// 减去路径lca到根节点涉及的树状数组节点（去重）
		for (int i = dfn[lca]; i > 0; i -= lowbit(i)) {
			minusTree[++cntminus] = root[i];
		}
		
		// 减去路径lca父节点到根节点涉及的树状数组节点
		for (int i = dfn[lcafa]; i > 0; i -= lowbit(i)) {
			minusTree[++cntminus] = root[i];
		}
		
		// 在线段树上二分查找第k大值，并通过sorted数组还原原始值
		// 注意：这里查找的是第(num - k + 1)小的值，等价于第k大的值
		return sorted[innerQuery(num - k + 1, 1, s)];
	}

	/**
	 * 预处理函数，包括离散化、DFS序计算和初始化树状数组
	 */
	public static void prepare() {
		s = 0;
		
		// 收集初始节点权值
		for (int i = 1; i <= n; i++) {
			sorted[++s] = arr[i];
		}
		
		// 收集所有更新操作中涉及的值
		for (int i = 1; i <= m; i++) {
			if (ques[i][0] == 0) {  // 更新操作
				sorted[++s] = ques[i][2];
			}
		}
		
		// 对所有值进行排序
		Arrays.sort(sorted, 1, s + 1);
		
		// 去重，得到离散化后的值域
		int len = 1;
		for (int i = 2; i <= s; i++) {
			if (sorted[len] != sorted[i]) {
				sorted[++len] = sorted[i];
			}
		}
		s = len;
		
		// 将原数组中的值替换为离散化后的索引
		for (int i = 1; i <= n; i++) {
			arr[i] = kth(arr[i]);
		}
		
		// 计算DFS序和树链剖分信息
		dfs2();
		
		// 初始化树状数组
		for (int i = 1; i <= n; i++) {
			add(dfn[i], arr[i], 1);
			add(dfn[i] + size[i], arr[i], -1);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		
		// 读取节点初始权值
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		
		// 读取树的边
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		
		// 读取所有操作
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			ques[i][0] = (int) in.nval;
			in.nextToken();
			ques[i][1] = (int) in.nval;
			in.nextToken();
			ques[i][2] = (int) in.nval;
		}
		
		// 预处理
		prepare();
		
		// 处理所有操作
		for (int i = 1, k, x, y; i <= m; i++) {
			k = ques[i][0];
			x = ques[i][1];
			y = ques[i][2];
			if (k == 0) {
				// 更新操作
				update(x, y);
			} else {
				// 查询操作
				int ans = query(x, y, k);
				if (ans == -1) {
					out.println("invalid request!");
				} else {
					out.println(ans);
				}
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}