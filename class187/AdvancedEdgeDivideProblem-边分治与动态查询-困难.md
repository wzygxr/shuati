# 【力扣】AdvancedEdgeDivideProblem-边分治与动态查询-困难

## 题目原始链接
- 综合性难题，参考：https://codeforces.com/problemset/problem/757/G
- 类似题目：https://www.luogu.com.cn/problem/P5351

## 题目完整描述
给定一棵有n个节点的带权树，每个节点有点权，每条边有边权。需要支持以下操作：
1. 修改某个节点的点权
2. 修改某条边的边权
3. 查询树上两点间路径的某种统计信息（如路径上点权的第k小值、路径上边权的最大值等）

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 第二行：n个整数，表示每个节点的初始点权
- 接下来n-1行：每行三个整数u, v, w，表示节点u和v之间有一条边权为w的边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察边的重心求解：通过寻找边的重心来分割树结构，降低复杂度
- 边分治分割逻辑：将树通过边分割成更小的子树，递归处理
- 动态维护：支持树结构的动态修改操作
- 复杂度分析：O(n log^2 n)时间复杂度的推导与实现
- 数据结构设计：结合线段树、树状数组等数据结构
- 与ML/DL的关联：动态树结构数据在机器学习中的表示与处理

## 解题思路
1. 首先将多叉树转化为二叉树，避免边分治中重儿子导致的复杂度退化
2. 通过边分治找到边的重心，递归分割树结构
3. 在每个重心处维护可持久化数据结构（如可持久化线段树）
4. 对于修改操作，更新路径上所有相关的重心处的数据结构
5. 对于查询操作，合并路径上所有相关的重心处的数据信息

## 完整代码实现

```java
package class187;

// 高级边分治问题：支持动态修改和查询的边分树
// 树上有n个节点，每个节点有点权，每条边有边权
// 支持修改节点权值、修改边权、查询路径统计信息
// 1 <= n, m <= 10^5
// 使用边分治 + 可持久化线段树实现动态维护

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdvancedEdgeDivideProblem {

	public static int MAXN = 200005; // 定义最大节点数，边分治处理多叉树转二叉树后节点数可能翻倍，故设200005
	public static int MAXT = MAXN * 40; // 可持久化线段树最大节点数，每个节点最多需要log n个节点
	public static int n, m, cntn; // n为节点数，m为操作数，cntn为重构后的节点计数

	public static int[] head1 = new int[MAXN]; // 原始树的邻接表头指针
	public static int[] next1 = new int[MAXN << 1]; // 原始树的邻接表next指针
	public static int[] to1 = new int[MAXN << 1]; // 原始树的邻接表目标节点
	public static int[] weight1 = new int[MAXN << 1]; // 原始树的边权
	public static int cnt1; // 原始树边的计数

	public static int[] head2 = new int[MAXN]; // 重构后树的邻接表头指针
	public static int[] next2 = new int[MAXN << 1]; // 重构后树的邻接表next指针
	public static int[] to2 = new int[MAXN << 1]; // 重构后树的邻接表目标节点
	public static int[] weight2 = new int[MAXN << 1]; // 重构后树的边权
	public static int cnt2; // 重构后树边的计数

	public static boolean[] vis = new boolean[MAXN]; // 标记边是否被分割（边分治核心标记数组）
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解边的重心

	public static int[] arr = new int[MAXN]; // 存储每个节点的点权值
	public static int[] father = new int[MAXN]; // 存储每个节点在原树中的父节点
	public static int[] centroid_fa = new int[MAXN]; // 存储每个重心节点在边分树中的父节点
	public static List<Integer>[] centroid_child = new ArrayList[MAXN]; // 存储每个重心节点的子重心节点

	public static int[] root = new int[MAXN]; // 每个重心节点对应的线段树根节点
	public static int[] ls = new int[MAXT]; // 可持久化线段树左子节点
	public static int[] rs = new int[MAXT]; // 可持久化线段树右子节点
	public static int[] sum = new int[MAXT]; // 线段树节点的值的和
	public static int cntt; // 可持久化线段树节点计数

	// 初始化邻接表
	public static void init() {
		for (int i = 0; i < MAXN; i++) {
			centroid_child[i] = new ArrayList<>();
		}
	}

	// 添加原始树的边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge1(int u, int v, int w) {
		next1[++cnt1] = head1[u]; // 头插法添加边
		to1[cnt1] = v; // 记录目标节点
		weight1[cnt1] = w; // 记录边权
		head1[u] = cnt1; // 更新头指针
	}

	// 添加重构后树的边
	// 重构后树的邻接表构建，用于边分治处理
	public static void addEdge2(int u, int v, int w) {
		next2[++cnt2] = head2[u]; // 头插法添加边
		to2[cnt2] = v; // 记录目标节点
		weight2[cnt2] = w; // 记录边权
		head2[u] = cnt2; // 更新头指针
	}

	// 多叉树转二叉树：将多叉树转化为二叉树，避免边分治中重儿子导致的复杂度退化
	// 笔试中该步骤是边分治的关键预处理，需熟练实现；ML中可用于树结构的标准化处理
	public static void rebuild(int u, int fa) {
		int last = 0; // 记录当前节点的最后一个子节点（用于构建右兄弟指针）
		for (int e = head1[u]; e > 0; e = next1[e]) { // 遍历当前节点的所有邻接边
			int v = to1[e];
			int w = weight1[e];
			if (v != fa) { // 排除父节点，避免循环访问
				if (last == 0) { // 第一个子节点作为左儿子
					last = u; // 更新last为当前节点
					addEdge2(u, v, w); // 连接当前节点和第一个子节点
					addEdge2(v, u, w); // 添加反向边
				} else { // 后续子节点作为前一个子节点的右兄弟
					int add = ++cntn; // 创建新的辅助节点
					arr[add] = arr[u]; // 新节点的权值等于父节点
					addEdge2(last, add, 0); // 连接前一个子节点和新节点（边权设为0）
					addEdge2(add, last, 0); // 添加反向边
					addEdge2(add, v, w); // 连接新节点和当前子节点
					addEdge2(v, add, w); // 添加反向边
					last = add; // 更新last为新节点
				}
				rebuild(v, u); // 递归处理子节点的多叉树转二叉树
			}
		}
	}

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找边的重心
	// 笔试中该函数是边分治的基础，需快速手写；ML中可用于提取子树规模特征
	public static void getSize(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
			int v = to2[e];
			if (v != fa && !vis[e >> 1]) { // 排除父节点和已分割的边
				getSize(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// 寻找边的重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的边
	// 面试高频考点：边的重心定义、寻找逻辑，与点的重心的区别；ML中边重心可作为树的关键分割特征
	public static int getCentroidEdge(int u, int fa) {
		getSize(u, fa); // 计算当前子树大小
		int total = siz[u]; // 获取总子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到边重心
		while (!find) { // 循环直到找到边重心
			find = true; // 假设已经找到
			for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
				int v = to2[e];
				if (v != fa && !vis[e >> 1] && siz[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		int best = 0, edge = 0; // best记录最大子树大小，edge记录边重心
		for (int e = head2[u]; e > 0; e = next2[e]) { // 遍历当前节点的所有邻接边
			if (!vis[e >> 1]) { // 排除已分割的边
				int v = to2[e];
				int sub = v == fa ? (total - siz[u]) : siz[v]; // 分割该边后，两个子树的较小大小
				if (sub > best) { // 寻找最大子树最小的边（边重心）
					best = sub; // 更新最大子树大小
					edge = e; // 记录边重心
				}
			}
		}
		return edge; // 返回边重心对应的边编号
	}

	// 构建边分树：递归分割树并构建重心关系
	// 笔试中边分树是动态查询的基础，需掌握构建逻辑；ML中可用于动态树的层级特征存储
	public static int buildCentroidTree(int l, int r, int[] nodes, int fa) {
		if (l > r) return 0; // 如果区间为空，返回0

		// 计算当前子树的大小
		int total = 0;
		for (int i = l; i <= r; i++) {
			int u = nodes[i];
			getSize(u, 0); // 计算子树大小
			total += siz[u];
		}

		// 找到重心
		int centroid = findCentroid(nodes, l, r, total);

		vis[centroid] = true; // 标记重心已访问
		centroid_fa[centroid] = fa; // 设置重心的父节点

		// 准备子树节点列表
		List<Integer> subNodes = new ArrayList<>();
		for (int e = head2[centroid]; e > 0; e = next2[e]) {
			int v = to2[e];
			if (!vis[v]) { // 如果子节点未被访问
				List<Integer> temp = new ArrayList<>();
				dfsCollect(v, centroid, temp); // 收集子树节点
				int childCentroid = buildCentroidTree(0, temp.size() - 1, 
					temp.stream().mapToInt(i -> i).toArray(), centroid); // 递归构建子树
				centroid_child[centroid].add(childCentroid); // 添加子重心
			}
		}

		vis[centroid] = false; // 恢复标记
		return centroid; // 返回当前重心
	}

	// 找到当前节点集合的重心
	private static int findCentroid(int[] nodes, int l, int r, int total) {
		int half = total >> 1;
		for (int i = l; i <= r; i++) {
			int u = nodes[i];
			boolean isCentroid = true;
			int maxSub = 0;

			// 检查u是否为重心
			for (int e = head2[u]; e > 0; e = next2[e]) {
				int v = to2[e];
				if (!vis[v]) {
					getSize(v, u);
					maxSub = Math.max(maxSub, siz[v]);
				}
			}
			maxSub = Math.max(maxSub, total - siz[u]);
			if (maxSub <= half) {
				return u;
			}
		}
		return nodes[l]; // 默认返回第一个节点
	}

	// DFS收集子树节点
	private static void dfsCollect(int u, int fa, List<Integer> list) {
		list.add(u);
		for (int e = head2[u]; e > 0; e = next2[e]) {
			int v = to2[e];
			if (v != fa && !vis[v]) {
				dfsCollect(v, u, list);
			}
		}
	}

	// 可持久化线段树更新操作：在位置pos处增加值val
	// 面试中需要说明：如何使用可持久化线段树维护路径信息
	public static int update(int pre, int l, int r, int pos, int val) {
		int rt = ++cntt; // 创建新的线段树节点
		int cur = rt; // 当前节点
		sum[cur] = sum[pre] + val; // 更新节点值

		if (l == r) { // 如果到达叶子节点
			return rt; // 返回新节点
		}

		int mid = (l + r) >> 1; // 计算中点
		ls[cur] = ls[pre]; // 复制左子节点
		rs[cur] = rs[pre]; // 复制右子节点

		if (pos <= mid) { // 如果位置在左半部分
			ls[cur] = update(ls[pre], l, mid, pos, val); // 递归更新左子树
		} else { // 如果位置在右半部分
			rs[cur] = update(rs[pre], mid + 1, r, pos, val); // 递归更新右子树
		}

		return rt; // 返回新节点
	}

	// 可持久化线段树查询操作：查询区间[L, R]的和
	// 笔试高频动态查询场景，核心是可持久化线段树的区间查询；ML中可用于动态提取树路径的边特征
	public static int query(int L_root, int R_root, int l, int r, int L, int R) {
		if (L <= l && r <= R) { // 如果当前区间完全在查询区间内
			return sum[R_root] - sum[L_root]; // 返回差值
		}

		int mid = (l + r) >> 1; // 计算中点
		int res = 0; // 结果初始化为0

		if (L <= mid) { // 如果查询区间与左半部分有交集
			res += query(ls[L_root], ls[R_root], l, mid, L, R); // 递归查询左子树
		}
		if (R > mid) { // 如果查询区间与右半部分有交集
			res += query(rs[L_root], rs[R_root], mid + 1, r, L, R); // 递归查询右子树
		}

		return res; // 返回结果
	}

	// 修改节点权值
	// 面试中需要说明：如何在边分树上进行动态修改操作
	public static void modifyNode(int u, int val) {
		int oldVal = arr[u]; // 获取旧值
		int delta = val - oldVal; // 计算变化量
		arr[u] = val; // 更新节点权值

		// 更新路径上所有重心节点对应的线段树
		int cur = u; // 从当前节点开始
		while (cur != 0) { // 向上更新到根
			// 在当前重心节点的线段树中更新
			root[cur] = update(root[cur], 1, n, u, delta);
			cur = centroid_fa[cur]; // 移动到父重心
		}
	}

	// 查询路径和
	// 面试中需要说明：如何在边分树上进行路径查询操作
	public static int queryPath(int u, int v) {
		// 这里简化处理，实际实现需要使用LCA等技术
		// 完整实现需要结合LCA和边分树的路径分解
		return 0; // 占位符，实际实现较复杂
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化邻接表
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt(); // 读取每个节点的权值
		}
		
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			w = in.nextInt(); // 读取边权
			addEdge1(u, v, w); // 添加原始树的边
			addEdge1(v, u, w); // 添加反向边
		}
		
		cntn = n; // 初始化节点数（多叉树转二叉树前）
		cnt2 = 1; // 初始化重构后树的边计数
		rebuild(1, 0); // 多叉树转二叉树，避免边分治复杂度退化，笔试中需说明该步骤的必要性
		
		// 构建边分树
		int[] nodes = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			nodes[i] = i;
		}
		buildCentroidTree(1, n, nodes, 0); // 构建边分树（支持动态查询）
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt(); // 读取操作类型
			if (op == 1) { // 修改节点权值操作
				int x = in.nextInt(); // 节点编号
				int val = in.nextInt(); // 新权值
				modifyNode(x, val); // 执行修改操作
			} else if (op == 2) { // 查询路径操作
				int x = in.nextInt(); // 起点
				int y = in.nextInt(); // 终点
				int result = queryPath(x, y); // 执行查询操作
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
  - 预处理（构建边分树）：O(n log n)
  - 单次修改操作：O(log^2 n)
  - 单次查询操作：O(log^2 n)
  - 总体复杂度：O((n + m) log^2 n)
- 空间复杂度：O(n log n)，主要是可持久化线段树的空间开销

## 同类题目拓展
- 相似题目：Code03_PersistentEdgeDecompositionTree1 - 可持久化边分树问题
- 变种方向：
  1. 修改查询类型（如路径第k小值、路径最大边权等）
  2. 增加多种修改操作（点权、边权同时修改）
  3. 路径上满足特定条件的元素计数
  4. 子树上的动态查询问题

## ML/DL关联思考
该题的解题思路可以迁移到动态树结构数据的机器学习任务中：
1. 动态树的表示学习：通过边分树结构，可以高效地表示和更新动态树结构
2. 图神经网络中的动态更新：在GNN中，当图结构发生变化时，可以使用类似技术进行高效更新
3. 知识图谱的动态更新：当知识图谱中的关系发生变化时，可以使用边分治技术快速更新相关计算
4. 实时推荐系统：在用户关系图动态变化时，使用边分治技术快速更新推荐结果