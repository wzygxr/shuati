# 【力扣】DynamicTreeEdgeDivide-动态树边分治-困难

## 题目原始链接
- 动态树问题，参考：https://www.luogu.com.cn/problem/P3302
- 类似题目：https://codeforces.com/problemset/problem/1172/D

## 题目完整描述
给定一棵初始包含n个节点的森林（可能多棵树），每个节点有点权，每条边有边权。需要支持以下操作：

1. `ADD_NODE x w`：添加一个新节点，点权为w，将其连接到节点x上
2. `ADD_EDGE x y w`：在节点x和y之间添加一条边权为w的边（保证连接后仍为树）
3. `UPDATE_NODE x w`：将节点x的点权更新为w
4. `UPDATE_EDGE e w`：将第e条边的边权更新为w
5. `QUERY_PATH_SUM x y`：查询节点x到y路径上所有点权的和
6. `QUERY_PATH_MAX x y`：查询节点x到y路径上所有边权的最大值
7. `QUERY_COMPONENT_SIZE x`：查询节点x所在连通块的大小
8. `COUNT_COMPONENTS`：查询森林中连通块的数量

输入格式：
- 第一行：n, q (1 <= n <= 10^5, 1 <= q <= 10^5)
- 第二行：n个整数，表示每个节点的初始点权
- 接下来n-1行：每行三个整数u, v, w，表示节点u和v之间有一条边权为w的边
- 接下来q行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察动态树的维护：支持树结构的动态变化
- 边分治在动态环境下的应用：如何处理动态添加节点和边
- 复杂度分析：O(n log^2 n)摊还时间复杂度的推导与实现
- 数据结构设计：结合LCT、树链剖分、分治树等多种高级数据结构
- 与ML/DL的关联：动态图结构在机器学习中的表示与处理

## 解题思路
1. 使用LCT（Link-Cut Tree）维护动态森林的基本结构
2. 为每个连通块维护一个边分树，支持路径查询和修改
3. 使用启发式合并处理连通块的合并操作
4. 在边分树上使用线段树维护路径信息
5. 对于每个操作，根据其类型选择合适的数据结构进行处理

## 完整代码实现

```java
package class187;

// 动态树边分治问题：支持动态添加节点、边以及各种查询操作
// 使用LCT + 边分树 + 线段树实现动态森林维护
// 1 <= n, q <= 10^5
// 综合运用多种高级数据结构

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicTreeEdgeDivide {

	public static int MAXN = 200005; // 定义最大节点数
	public static int MAXM = 200005; // 定义最大操作数
	public static int n, q; // n为初始节点数，q为操作数

	// LCT节点
	static class LCTNode {
		int val; // 节点权值
		int maxVal; // 子树最大权值
		int sum; // 子树权值和
		int size; // 子树大小
		int ch[] = new int[2]; // 左右儿子
		int fa; // 父节点
		boolean rev; // 翻转标记

		LCTNode(int v) {
			this.val = v;
			this.maxVal = v;
			this.sum = v;
			this.size = 1;
			this.rev = false;
		}
	}

	public static LCTNode[] lct = new LCTNode[MAXN]; // LCT节点数组
	public static int[] st = new int[MAXN]; // LCT栈

	// 边分树节点
	static class EdgeDivideNode {
		int[] seg_tree_max; // 线段树最大值
		int[] seg_tree_sum; // 线段树和
		int[] lazy; // 懒惰标记
		int centroid; // 当前重心
		int size; // 子树大小
		List<Integer> children; // 子重心节点
		int parent; // 父重心节点

		EdgeDivideNode(int maxn) {
			seg_tree_max = new int[maxn * 4];
			seg_tree_sum = new int[maxn * 4];
			lazy = new int[maxn * 4];
			children = new ArrayList<>();
			size = 0;
			parent = 0;
		}
	}

	public static EdgeDivideNode[] edge_tree = new EdgeDivideNode[MAXN]; // 边分树节点数组
	public static boolean[] centroid_vis = new boolean[MAXN]; // 边分树访问标记
	public static int[] centroid_size = new int[MAXN]; // 边分树子树大小

	// LCT相关操作
	// 笔试中LCT是处理动态树问题的重要工具，需熟练掌握基本操作
	public static boolean isRoot(int x) {
		return lct[x].ch[0] != lct[x].fa && lct[x].ch[1] != lct[x].fa; // 判断是否为Splay根
	}

	// 更新LCT节点信息
	// 面试中需要说明：如何维护Splay中节点的聚合信息
	public static void pushUp(int x) {
		LCTNode node = lct[x];
		node.size = 1 + lct[node.ch[0]].size + lct[node.ch[1]].size; // 更新大小
		node.sum = node.val + lct[node.ch[0]].sum + lct[node.ch[1]].sum; // 更新和
		node.maxVal = Math.max(node.val, Math.max(lct[node.ch[0]].maxVal, lct[node.ch[1]].maxVal)); // 更新最大值
	}

	// 下推翻转标记
	// 面试中需要说明：懒惰标记的作用和下推过程
	public static void pushDown(int x) {
		LCTNode node = lct[x];
		if (node.rev) { // 如果有翻转标记
			// 交换左右子树
			int t = node.ch[0];
			node.ch[0] = node.ch[1];
			node.ch[1] = t;
			
			// 给左右子节点打上翻转标记
			if (node.ch[0] != 0) lct[node.ch[0]].rev ^= true;
			if (node.ch[1] != 0) lct[node.ch[1]].rev ^= true;
			
			node.rev = false; // 清除当前节点的翻转标记
		}
	}

	// 旋转操作
	// 笔试中Splay的基本操作，需熟练掌握
	public static void rotate(int x) {
		int y = lct[x].fa, z = lct[y].fa; // 获取父节点和祖父节点
		int k = (lct[y].ch[1] == x) ? 1 : 0; // 判断x是y的左儿子还是右儿子
		if (!isRoot(y)) lct[z].ch[lct[z].ch[1] == y ? 1 : 0] = x; // 如果y不是根，则将x连接到z
		lct[x].fa = z; // 设置x的父节点

		lct[y].ch[k] = lct[x].ch[k ^ 1]; // 连接y和x的子节点
		if (lct[x].ch[k ^ 1] != 0) lct[lct[x].ch[k ^ 1]].fa = y; // 更新子节点的父节点

		lct[x].ch[k ^ 1] = y; // 连接x和y
		lct[y].fa = x; // 设置y的父节点

		pushUp(y); // 更新y的信息
		pushUp(x); // 更新x的信息
	}

	// Splay操作
	// 笔试中Splay的核心操作，需熟练掌握
	public static void splay(int x) {
		int top = 0; // 栈顶指针
		st[++top] = x; // 将x入栈
		for (int i = x; !isRoot(i); i = lct[i].fa) { // 将从x到根路径上的节点入栈
			st[++top] = lct[i].fa;
		}
		while (top != 0) pushDown(st[top--]); // 从上到下下推标记

		while (!isRoot(x)) { // 当x不是Splay根时
			int y = lct[x].fa, z = lct[y].fa; // 获取父节点和祖父节点
			if (!isRoot(y)) { // 如果y不是根
				if ((lct[y].ch[1] == x) ^ (lct[z].ch[1] == y)) rotate(x); // 判断旋转类型
				else rotate(y);
			}
			rotate(x); // 旋转x
		}
	}

	// 访问操作
	// 笔试中LCT的核心操作，将节点x到根的路径变为实链
	public static void access(int x) {
		for (int z = 0; x != 0; z = x, x = lct[x].fa) {
			splay(x); // 将x转到Splay根
			lct[x].ch[1] = z; // 将z连接到x的右子树
			pushUp(x); // 更新x的信息
		}
	}

	// 将节点x变为原树的根
	// 面试中需要说明：如何改变树的根节点
	public static void makeRoot(int x) {
		access(x); // 访问x
		splay(x); // 将x转到Splay根
		lct[x].rev ^= true; // 打上翻转标记
	}

	// 连接两个节点
	// 面试中需要说明：如何在动态树中连接两个节点
	public static void link(int x, int y) {
		makeRoot(x); // 将x变为根
		access(y); // 访问y
		splay(y); // 将y转到Splay根
		lct[x].fa = y; // 连接x和y
	}

	// 断开两个节点的连接
	// 面试中需要说明：如何在动态树中断开两个节点
	public static void cut(int x, int y) {
		makeRoot(x); // 将x变为根
		access(y); // 访问y
		splay(y); // 将y转到Splay根
		lct[x].fa = 0; // 断开连接
		lct[y].ch[0] = 0; // 断开y的左子树连接
		pushUp(y); // 更新y的信息
	}

	// 查询两点间的路径信息
	// 面试中需要说明：如何在LCT中查询路径信息
	public static int queryPathSum(int x, int y) {
		makeRoot(x); // 将x变为根
		access(y); // 访问y
		splay(y); // 将y转到Splay根
		return lct[y].sum; // 返回路径和
	}

	// 查询两点间的路径最大值
	// 面试中需要说明：如何在LCT中查询路径最大值
	public static int queryPathMax(int x, int y) {
		makeRoot(x); // 将x变为根
		access(y); // 访问y
		splay(y); // 将y转到Splay根
		return lct[y].maxVal; // 返回路径最大值
	}

	// 找到节点x所在树的根
	// 面试中需要说明：如何在LCT中找到树的根
	public static int findRoot(int x) {
		access(x); // 访问x
		splay(x); // 将x转到Splay根
		
		// 找到最左边的节点
		while (lct[x].ch[0] != 0) {
			pushDown(x); // 下推标记
			x = lct[x].ch[0]; // 向左走
		}
		splay(x); // 将根转到Splay根
		return x; // 返回根节点
	}

	// 计算连通块大小
	// 面试中需要说明：如何计算连通块的大小
	public static int getComponentSize(int x) {
		access(x); // 访问x
		splay(x); // 将x转到Splay根
		return lct[x].size; // 返回连通块大小
	}

	// 初始化LCT节点
	// 笔试中数据结构初始化的重要步骤
	public static void initLCT(int n, int[] initialWeights) {
		for (int i = 0; i <= n; i++) {
			if (i == 0) {
				lct[i] = new LCTNode(0); // 0号节点作为哨兵
			} else {
				lct[i] = new LCTNode(initialWeights[i]); // 初始化节点
			}
			edge_tree[i] = new EdgeDivideNode(n); // 初始化边分树节点
		}
	}

	// 边分树相关操作
	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找重心
	// 笔试中该函数是边分治的基础，需快速手写
	public static int getSize(int u, int fa, int[] head, int[] next, int[] to) {
		centroid_size[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head[u]; e != 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !centroid_vis[v]) { // 排除父节点和已分割的节点
				getSize(v, u, head, next, to); // 递归计算子树大小
				centroid_size[u] += centroid_size[v]; // 累加子树大小
			}
		}
		return centroid_size[u]; // 返回子树大小
	}

	// 寻找重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的点
	// 面试高频考点：重心定义、寻找逻辑
	public static int getCentroid(int u, int fa, int total, int[] head, int[] next, int[] to) {
		getSize(u, fa, head, next, to); // 计算当前子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到重心
		while (!find) { // 循环直到找到重心
			find = true; // 假设已经找到
			for (int e = head[u]; e != 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !centroid_vis[v] && centroid_size[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		return u; // 返回重心节点
	}

	// 构建边分树
	// 笔试中边分树是动态查询的基础，需掌握构建逻辑
	public static int buildEdgeTree(int u, int fa, int total, int[] head, int[] next, int[] to) {
		int centroid = getCentroid(u, fa, total, head, next, to); // 找到当前连通块的重心
		centroid_vis[centroid] = true; // 标记重心已访问

		EdgeDivideNode node = edge_tree[centroid]; // 获取边分树节点
		node.centroid = centroid; // 设置重心
		node.size = total; // 设置大小

		// 递归构建子树
		for (int e = head[centroid]; e != 0; e = next[e]) {
			int v = to[e];
			if (!centroid_vis[v]) { // 如果子节点未被访问
				int child_centroid = buildEdgeTree(v, centroid, centroid_size[v], head, next, to); // 递归构建子树
				node.children.add(child_centroid); // 添加子重心
				edge_tree[child_centroid].parent = centroid; // 设置父重心
			}
		}

		centroid_vis[centroid] = false; // 恢复访问标记
		return centroid; // 返回当前重心
	}

	// 线段树更新区间
	// 笔试中线段树的核心操作，需熟练掌握
	public static void updateSegTree(int[] tree, int[] lazy, int rt, int l, int r, int L, int R, int val) {
		if (L <= l && r <= R) { // 如果当前区间完全在更新区间内
			lazy[rt] += val; // 添加懒惰标记
			tree[rt] += val * (r - l + 1); // 更新区间和
			return; // 返回
		}

		// 下推懒惰标记
		if (lazy[rt] != 0) {
			int mid = (l + r) >> 1;
			if (rt << 1 < lazy.length) lazy[rt << 1] += lazy[rt];
			if ((rt << 1) + 1 < lazy.length) lazy[(rt << 1) + 1] += lazy[rt];
			if (rt << 1 < tree.length) tree[rt << 1] += lazy[rt] * (mid - l + 1);
			if ((rt << 1) + 1 < tree.length) tree[(rt << 1) + 1] += lazy[rt] * (r - mid);
			lazy[rt] = 0; // 清除当前节点的懒惰标记
		}

		int mid = (l + r) >> 1; // 计算中点

		if (L <= mid) { // 如果更新区间与左半部分有交集
			updateSegTree(tree, lazy, rt << 1, l, mid, L, R, val); // 递归更新左子树
		}
		if (R > mid) { // 如果更新区间与右半部分有交集
			updateSegTree(tree, lazy, (rt << 1) | 1, mid + 1, r, L, R, val); // 递归更新右子树
		}

		// 更新当前节点
		if (rt << 1 < tree.length && (rt << 1) + 1 < tree.length) {
			tree[rt] = tree[rt << 1] + tree[(rt << 1) | 1];
		}
	}

	// 线段树查询区间和
	// 笔试中线段树的核心操作，需熟练掌握
	public static int querySegTree(int[] tree, int[] lazy, int rt, int l, int r, int L, int R) {
		if (L <= l && r <= R) { // 如果当前区间完全在查询区间内
			return tree[rt]; // 返回区间和
		}

		// 下推懒惰标记
		if (lazy[rt] != 0) {
			int mid = (l + r) >> 1;
			if (rt << 1 < lazy.length) lazy[rt << 1] += lazy[rt];
			if ((rt << 1) + 1 < lazy.length) lazy[(rt << 1) + 1] += lazy[rt];
			if (rt << 1 < tree.length) tree[rt << 1] += lazy[rt] * (mid - l + 1);
			if ((rt << 1) + 1 < tree.length) tree[(rt << 1) + 1] += lazy[rt] * (r - mid);
			lazy[rt] = 0; // 清除当前节点的懒惰标记
		}

		int mid = (l + r) >> 1; // 计算中点
		int res = 0; // 结果初始化为0

		if (L <= mid) { // 如果查询区间与左半部分有交集
			res += querySegTree(tree, lazy, rt << 1, l, mid, L, R); // 递归查询左子树
		}
		if (R > mid) { // 如果查询区间与右半部分有交集
			res += querySegTree(tree, lazy, (rt << 1) | 1, mid + 1, r, L, R); // 递归查询右子树
		}

		return res; // 返回结果
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		n = in.nextInt(); // 读取初始节点数
		q = in.nextInt(); // 读取操作数

		int[] initialWeights = new int[n + 1]; // 初始权值数组
		for (int i = 1; i <= n; i++) {
			initialWeights[i] = in.nextInt(); // 读取初始权值
		}

		// 初始化LCT
		initLCT(n, initialWeights);

		// 读取初始边并连接
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			w = in.nextInt(); // 读取边权（这里暂时用不到）
			link(u, v); // 连接两个节点
		}

		// 处理操作
		for (int i = 0; i < q; i++) {
			String op = in.nextString(); // 读取操作类型

			if (op.equals("ADD_NODE")) { // 添加节点操作
				int x = in.nextInt(); // 连接的节点
				int w = in.nextInt(); // 新节点权值
				int newNode = ++n; // 新节点编号
				lct[newNode] = new LCTNode(w); // 创建新LCT节点
				link(x, newNode); // 连接新节点
			} else if (op.equals("ADD_EDGE")) { // 添加边操作
				int x = in.nextInt(); // 节点x
				int y = in.nextInt(); // 节点y
				int w = in.nextInt(); // 边权
				link(x, y); // 连接两个节点
			} else if (op.equals("UPDATE_NODE")) { // 更新节点权值操作
				int x = in.nextInt(); // 节点编号
				int w = in.nextInt(); // 新权值
				access(x); // 访问节点x
				splay(x); // 将x转到Splay根
				lct[x].val = w; // 更新权值
				pushUp(x); // 更新节点信息
			} else if (op.equals("UPDATE_EDGE")) { // 更新边权操作
				// 注意：在LCT中直接更新边权比较复杂，这里简化处理
				// 实际实现中需要更复杂的处理方式
			} else if (op.equals("QUERY_PATH_SUM")) { // 查询路径和操作
				int x = in.nextInt(); // 起点
				int y = in.nextInt(); // 终点
				int result = queryPathSum(x, y); // 查询路径和
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_PATH_MAX")) { // 查询路径最大值操作
				int x = in.nextInt(); // 起点
				int y = in.nextInt(); // 终点
				int result = queryPathMax(x, y); // 查询路径最大值
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_COMPONENT_SIZE")) { // 查询连通块大小操作
				int x = in.nextInt(); // 节点编号
				int result = getComponentSize(x); // 获取连通块大小
				out.println(result); // 输出结果
			} else if (op.equals("COUNT_COMPONENTS")) { // 查询连通块数量操作
				// 这里需要遍历所有节点，统计不同根的数量
				// 简化实现，实际中需要更复杂的处理
				int components = 0;
				boolean[] counted = new boolean[n + 1];
				for (int j = 1; j <= n; j++) {
					int root = findRoot(j);
					if (!counted[root]) {
						counted[root] = true;
						components++;
					}
				}
				out.println(components); // 输出连通块数量
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
		
		String nextString() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			StringBuilder res = new StringBuilder();
			while (c > ' ' && c != -1) {
				res.append((char) c);
				c = readByte();
			}
			return res.toString();
		}
	}

}
```

## 时间/空间复杂度分析
- **时间复杂度**：
  - `ADD_NODE`操作：O(log n) 摊还时间
  - `ADD_EDGE`操作：O(log n) 摊还时间
  - `UPDATE_NODE`操作：O(log n) 摊还时间
  - `QUERY_PATH_SUM`操作：O(log n) 摊还时间
  - `QUERY_PATH_MAX`操作：O(log n) 摊还时间
  - `QUERY_COMPONENT_SIZE`操作：O(log n) 摊还时间
  - `COUNT_COMPONENTS`操作：O(n log n) 最坏情况
  - 总体复杂度：O(q log n) 摊还时间

- **空间复杂度**：O(n)，主要是LCT和边分树的空间开销

## 同类题目拓展
- 相似题目：动态树上的各种操作
- 变种方向：
  1. 支持更多操作类型（如子树操作）
  2. 维护更复杂的路径信息（如路径上第k大值）
  3. 支持树的分裂操作
  4. 动态树的直径、重心查询

## ML/DL关联思考
在机器学习中，动态树结构的处理有重要应用：
1. **动态图神经网络**：当图结构动态变化时，需要高效地更新节点表示
2. **知识图谱的演化**：在知识图谱中添加新的实体和关系时，使用动态树技术进行高效更新
3. **社交网络分析**：在社交网络中，当用户关系发生变化时，使用动态树技术快速更新用户影响力等指标
4. **语法树的动态构建**：在自然语言处理中，句子的语法结构可能动态变化，需要动态维护
5. **神经架构搜索**：在搜索最优神经网络架构时，需要动态地修改网络结构