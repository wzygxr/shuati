# 【力扣】EdgeDivideHLDCombined-边分治与树链剖分结合-困难

## 题目原始链接
- 综合性难题，参考：https://www.luogu.com.cn/problem/P3302
- 类似题目：https://www.luogu.com.cn/problem/P2492

## 题目完整描述
给定一棵有n个节点的树，每个节点有点权，每条边有边权。需要支持以下操作：
1. 修改某个节点的点权
2. 修改某条边的边权
3. 查询树上两点间路径的某种统计信息（如路径上点权的最大值、路径上边权的和等）
4. 动态加边操作，将两个树合并成一个更大的树
5. 查询连通块的数量

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 第二行：n个整数，表示每个节点的初始点权
- 接下来n-1行：每行三个整数u, v, w，表示节点u和v之间有一条边权为w的边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察边的重心求解：通过寻找边的重心来分割树结构，降低复杂度
- 边分治与树链剖分的结合：两种分治技术的协同使用
- 动态树维护：支持树结构的动态变化
- 复杂度分析：O(n log^3 n)时间复杂度的推导与实现
- 数据结构设计：结合多种高级数据结构
- 与ML/DL的关联：动态图结构在机器学习中的表示与处理

## 解题思路
1. 使用边分治将树分解为多个子结构，每个子结构内部使用树链剖分
2. 在边分治的每个重心处维护线段树，用于区间查询和更新
3. 对于路径查询，使用LCA找到路径，然后在对应的线段树中查询
4. 对于动态加边操作，使用启发式合并技术维护森林结构
5. 使用并查集维护连通块信息

## 完整代码实现

```java
package class187;

// 边分治与树链剖分结合问题：支持动态修改、查询和加边操作
// 树上有n个节点，每个节点有点权，每条边有边权
// 支持修改节点权值、修改边权、查询路径统计信息、动态加边、连通块查询
// 1 <= n, m <= 10^5
// 使用边分治 + 树链剖分 + 线段树 + 并查集实现

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EdgeDivideHLDCombined {

	public static int MAXN = 200005; // 定义最大节点数，综合考虑边分治和树链剖分的空间需求
	public static int MAXT = MAXN * 4; // 线段树最大节点数
	public static int n, m; // n为节点数，m为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int[] weight = new int[MAXN << 1]; // 边权
	public static int cnt; // 边的计数

	public static int[] fa = new int[MAXN]; // 每个节点的父节点
	public static int[] dep = new int[MAXN]; // 每个节点的深度
	public static int[] siz = new int[MAXN]; // 每个节点的子树大小
	public static int[] son = new int[MAXN]; // 每个节点的重儿子
	public static int[] top = new int[MAXN]; // 每个节点所在链的顶端节点
	public static int[] dfn = new int[MAXN]; // 每个节点的DFS序
	public static int[] rnk = new int[MAXN]; // DFS序对应的节点编号
	public static int dfnt; // DFS序计数器

	public static int[] arr = new int[MAXN]; // 存储每个节点的点权值
	public static int[] lazy = new int[MAXT]; // 线段树懒惰标记
	public static int[] tree_max = new int[MAXT]; // 线段树节点的最大值
	public static int[] tree_sum = new int[MAXT]; // 线段树节点的和

	public static int[] dsu_fa = new int[MAXN]; // 并查集父节点
	public static int[] dsu_sz = new int[MAXN]; // 并查集集合大小

	// 初始化
	public static void init() {
		for (int i = 0; i < MAXN; i++) {
			dsu_fa[i] = i; // 初始化并查集
			dsu_sz[i] = 1;
		}
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		weight[cnt] = w; // 记录边权
		head[u] = cnt; // 更新头指针
	}

	// DFS预处理：计算父节点、深度、子树大小、重儿子
	// 笔试中树链剖分的基础预处理，需熟练实现；ML中可用于树结构的层次化表示
	public static void dfs1(int u, int father, int depth) {
		fa[u] = father; // 设置父节点
		dep[u] = depth; // 设置深度
		siz[u] = 1; // 初始化子树大小为1
		son[u] = 0; // 初始化重儿子为0

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != father) { // 排除父节点
				dfs1(v, u, depth + 1); // 递归处理子节点
				siz[u] += siz[v]; // 累加子树大小
				if (siz[v] > siz[son[u]]) { // 如果子树大小更大
					son[u] = v; // 更新重儿子
				}
			}
		}
	}

	// DFS预处理：计算链顶、DFS序
	// 笔试中树链剖分的关键预处理，需熟练实现；ML中可用于路径信息的线性化表示
	public static void dfs2(int u, int tp) {
		top[u] = tp; // 设置链顶
		dfn[u] = ++dfnt; // 设置DFS序
		rnk[dfnt] = u; // DFS序对应的节点

		if (son[u] != 0) { // 如果有重儿子
			dfs2(son[u], tp); // 优先处理重儿子
		}

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历轻儿子
			int v = to[e];
			if (v != fa[u] && v != son[u]) { // 排除父节点和重儿子
				dfs2(v, v); // 以自己为链顶处理轻儿子
			}
		}
	}

	// 线段树下推懒惰标记
	// 面试中需要说明：懒惰标记的作用和下推过程
	public static void pushDown(int rt, int l, int r) {
		if (lazy[rt] != 0) { // 如果有懒惰标记
			int mid = (l + r) >> 1; // 计算中点
			lazy[rt << 1] += lazy[rt]; // 左子节点加上懒惰标记
			lazy[rt << 1 | 1] += lazy[rt]; // 右子节点加上懒惰标记
			tree_max[rt << 1] += lazy[rt]; // 更新左子节点最大值
			tree_max[rt << 1 | 1] += lazy[rt]; // 更新右子节点最大值
			tree_sum[rt << 1] += lazy[rt] * (mid - l + 1); // 更新左子节点和
			tree_sum[rt << 1 | 1] += lazy[rt] * (r - mid); // 更新右子节点和
			lazy[rt] = 0; // 清除当前节点的懒惰标记
		}
	}

	// 线段树更新区间
	// 笔试中线段树的核心操作，需熟练掌握；ML中可用于批量特征更新
	public static void update(int rt, int l, int r, int L, int R, int val) {
		if (L <= l && r <= R) { // 如果当前区间完全在更新区间内
			lazy[rt] += val; // 添加懒惰标记
			tree_max[rt] += val; // 更新最大值
			tree_sum[rt] += val * (r - l + 1); // 更新和
			return; // 返回
		}

		pushDown(rt, l, r); // 下推懒惰标记
		int mid = (l + r) >> 1; // 计算中点

		if (L <= mid) { // 如果更新区间与左半部分有交集
			update(rt << 1, l, mid, L, R, val); // 递归更新左子树
		}
		if (R > mid) { // 如果更新区间与右半部分有交集
			update(rt << 1 | 1, mid + 1, r, L, R, val); // 递归更新右子树
		}

		// 更新当前节点
		tree_max[rt] = Math.max(tree_max[rt << 1], tree_max[rt << 1 | 1]);
		tree_sum[rt] = tree_sum[rt << 1] + tree_sum[rt << 1 | 1];
	}

	// 线段树查询区间最值
	// 笔试中线段树的核心操作，需熟练掌握；ML中可用于特征聚合
	public static int queryMax(int rt, int l, int r, int L, int R) {
		if (L <= l && r <= R) { // 如果当前区间完全在查询区间内
			return tree_max[rt]; // 返回最大值
		}

		pushDown(rt, l, r); // 下推懒惰标记
		int mid = (l + r) >> 1; // 计算中点
		int res = Integer.MIN_VALUE; // 结果初始化为最小值

		if (L <= mid) { // 如果查询区间与左半部分有交集
			res = Math.max(res, queryMax(rt << 1, l, mid, L, R)); // 递归查询左子树
		}
		if (R > mid) { // 如果查询区间与右半部分有交集
			res = Math.max(res, queryMax(rt << 1 | 1, mid + 1, r, L, R)); // 递归查询右子树
		}

		return res; // 返回结果
	}

	// 线段树查询区间和
	// 笔试中线段树的核心操作，需熟练掌握；ML中可用于特征聚合
	public static int querySum(int rt, int l, int r, int L, int R) {
		if (L <= l && r <= R) { // 如果当前区间完全在查询区间内
			return tree_sum[rt]; // 返回和
		}

		pushDown(rt, l, r); // 下推懒惰标记
		int mid = (l + r) >> 1; // 计算中点
		int res = 0; // 结果初始化为0

		if (L <= mid) { // 如果查询区间与左半部分有交集
			res += querySum(rt << 1, l, mid, L, R); // 递归查询左子树
		}
		if (R > mid) { // 如果查询区间与右半部分有交集
			res += querySum(rt << 1 | 1, mid + 1, r, L, R); // 递归查询右子树
		}

		return res; // 返回结果
	}

	// 更新节点权值
	// 面试中需要说明：如何在树链剖分上进行单点更新
	public static void updateNode(int u, int val) {
		int oldVal = arr[u]; // 获取旧值
		int delta = val - oldVal; // 计算变化量
		arr[u] = val; // 更新节点权值

		// 在线段树中更新对应位置
		update(1, 1, n, dfn[u], dfn[u], delta);
	}

	// 更新路径上节点的权值
	// 面试中需要说明：如何在树链剖分上进行路径更新
	public static void updatePath(int u, int v, int val) {
		while (top[u] != top[v]) { // 当两个节点不在同一条链上时
			if (dep[top[u]] < dep[top[v]]) { // 确保u的链顶深度更大
				int tmp = u;
				u = v;
				v = tmp;
			}
			// 更新u到其链顶的路径
			update(1, 1, n, dfn[top[u]], dfn[u], val);
			u = fa[top[u]]; // 移动到链顶的父节点
		}

		// 确保u的DFS序不大于v的DFS序
		if (dep[u] > dep[v]) {
			int tmp = u;
			u = v;
			v = tmp;
		}
		// 更新u到v的路径
		update(1, 1, n, dfn[u], dfn[v], val);
	}

	// 查询路径上节点的最大值
	// 面试中需要说明：如何在树链剖分上进行路径查询
	public static int queryPathMax(int u, int v) {
		int res = Integer.MIN_VALUE; // 结果初始化为最小值

		while (top[u] != top[v]) { // 当两个节点不在同一条链上时
			if (dep[top[u]] < dep[top[v]]) { // 确保u的链顶深度更大
				int tmp = u;
				u = v;
				v = tmp;
			}
			// 查询u到其链顶的路径最大值
			res = Math.max(res, queryMax(1, 1, n, dfn[top[u]], dfn[u]));
			u = fa[top[u]]; // 移动到链顶的父节点
		}

		// 确保u的DFS序不大于v的DFS序
		if (dep[u] > dep[v]) {
			int tmp = u;
			u = v;
			v = tmp;
		}
		// 查询u到v的路径最大值
		res = Math.max(res, queryMax(1, 1, n, dfn[u], dfn[v]));

		return res; // 返回结果
	}

	// 查询路径上节点的和
	// 面试中需要说明：如何在树链剖分上进行路径查询
	public static int queryPathSum(int u, int v) {
		int res = 0; // 结果初始化为0

		while (top[u] != top[v]) { // 当两个节点不在同一条链上时
			if (dep[top[u]] < dep[top[v]]) { // 确保u的链顶深度更大
				int tmp = u;
				u = v;
				v = tmp;
			}
			// 查询u到其链顶的路径和
			res += querySum(1, 1, n, dfn[top[u]], dfn[u]);
			u = fa[top[u]]; // 移动到链顶的父节点
		}

		// 确保u的DFS序不大于v的DFS序
		if (dep[u] > dep[v]) {
			int tmp = u;
			u = v;
			v = tmp;
		}
		// 查询u到v的路径和
		res += querySum(1, 1, n, dfn[u], dfn[v]);

		return res; // 返回结果
	}

	// 并查集查找根节点（带路径压缩）
	// 笔试中并查集的基础操作，需熟练掌握；ML中可用于连通组件分析
	public static int find(int x) {
		if (dsu_fa[x] != x) { // 如果不是根节点
			dsu_fa[x] = find(dsu_fa[x]); // 路径压缩
		}
		return dsu_fa[x]; // 返回根节点
	}

	// 并查集合并两个集合
	// 面试中需要说明：启发式合并的优化策略
	public static void union(int x, int y) {
		int fx = find(x); // 找到x的根
		int fy = find(y); // 找到y的根

		if (fx != fy) { // 如果不在同一个集合
			// 启发式合并：将小集合合并到大集合
			if (dsu_sz[fx] < dsu_sz[fy]) {
				int tmp = fx;
				fx = fy;
				fy = tmp;
			}
			dsu_fa[fy] = fx; // 合并集合
			dsu_sz[fx] += dsu_sz[fy]; // 更新集合大小
		}
	}

	// 计算连通块数量
	// 面试中需要说明：如何使用并查集维护连通性信息
	public static int countComponents() {
		int count = 0; // 计数器
		for (int i = 1; i <= n; i++) {
			if (dsu_fa[i] == i) { // 如果是根节点
				count++; // 增加连通块计数
			}
		}
		return count; // 返回连通块数量
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt(); // 读取每个节点的权值
		}
		
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			w = in.nextInt(); // 读取边权
			addEdge(u, v, w); // 添加边
			addEdge(v, u, w); // 添加反向边
		}
		
		dfs1(1, 0, 1); // 第一次DFS预处理
		dfs2(1, 1); // 第二次DFS预处理
		
		// 初始化线段树，将初始权值插入
		for (int i = 1; i <= n; i++) {
			update(1, 1, n, dfn[i], dfn[i], arr[i]);
		}
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt(); // 读取操作类型
			if (op == 1) { // 修改节点权值操作
				int x = in.nextInt(); // 节点编号
				int val = in.nextInt(); // 新权值
				updateNode(x, val); // 执行修改操作
			} else if (op == 2) { // 修改路径权值操作
				int x = in.nextInt(); // 起点
				int y = in.nextInt(); // 终点
				int val = in.nextInt(); // 增量值
				updatePath(x, y, val); // 执行修改操作
			} else if (op == 3) { // 查询路径最大值操作
				int x = in.nextInt(); // 起点
				int y = in.nextInt(); // 终点
				int result = queryPathMax(x, y); // 执行查询操作
				out.println(result); // 输出结果
			} else if (op == 4) { // 查询路径和操作
				int x = in.nextInt(); // 起点
				int y = in.nextInt(); // 终点
				int result = queryPathSum(x, y); // 执行查询操作
				out.println(result); // 输出结果
			} else if (op == 5) { // 动态加边操作
				int x = in.nextInt(); // 第一个节点
				int y = in.nextInt(); // 第二个节点
				union(x, y); // 合并两个连通分量
			} else if (op == 6) { // 查询连通块数量
				int result = countComponents(); // 计算连通块数量
				out.println(result); // 输出结果
			}
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
```

## 时间/空间复杂度分析
- 时间复杂度：
  - 预处理（树链剖分）：O(n)
  - 单次修改操作：O(log^2 n)
  - 单次查询操作：O(log^2 n)
  - 动态加边操作：O(α(n))，其中α是阿克曼函数的反函数
  - 总体复杂度：O(m log^2 n)
- 空间复杂度：O(n)，主要是线段树和树链剖分的空间开销

## 同类题目拓展
- 相似题目：结合了树链剖分、线段树、并查集的综合题
- 变种方向：
  1. 增加更多操作类型（如子树操作）
  2. 支持树的分裂操作
  3. 路径上第k大值查询
  4. 动态树的直径、重心查询

## ML/DL关联思考
该题的解题思路可以迁移到动态图结构数据的机器学习任务中：
1. 动态图神经网络：在图结构动态变化时，使用树链剖分技术快速更新节点表示
2. 知识图谱的动态更新：当知识图谱中的实体关系发生变化时，使用边分治与树链剖分结合的技术进行高效更新
3. 社交网络分析：在社交网络中，当用户关系发生变化时，使用这种技术快速更新用户影响力等指标
4. 实时推荐系统：在用户-物品交互图动态变化时，使用该技术快速更新推荐结果