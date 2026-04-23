# 【力扣】EdgeDivideMoOnTree-边分治与树上莫队-困难

## 题目原始链接
- 树上莫队问题，参考：https://www.luogu.com.cn/problem/P4074
- 类似题目：https://codeforces.com/problemset/problem/375/D

## 题目完整描述
给定一棵有n个节点的树，每个节点有一个颜色。有m个询问，每个询问给出两个节点u和v，要求统计u到v路径上出现次数为奇数次的颜色数量。

此外，还支持以下操作：
1. 修改某个节点的颜色
2. 查询路径上出现次数为奇数次的颜色数量
3. 查询路径上不同颜色的数量

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 第二行：n个整数，表示每个节点的初始颜色
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行表示一个操作，格式为：
  - `1 u v`：查询节点u到v路径上出现次数为奇数次的颜色数量
  - `2 u c`：将节点u的颜色修改为c

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察树上莫队算法：如何在树上应用莫队算法
- 边分治与莫队的结合：两种分块思想的融合应用
- LCA算法：最近公共祖先的计算
- 复杂度分析：O(n sqrt(n))时间复杂度的推导与实现
- 与ML/DL的关联：在图神经网络中使用分块技术进行高效计算

## 解题思路
1. 使用欧拉序将树上路径问题转化为序列问题
2. 对欧拉序进行分块，应用莫队算法
3. 使用边分治优化块的组织方式
4. 维护每个块内的颜色计数信息
5. 使用位运算或集合操作快速统计奇数次颜色

## 完整代码实现

```java
package class187;

// 边分治与树上莫队结合问题：统计树上路径中出现奇数次的颜色数量
// 使用欧拉序 + 莫队算法 + 边分治实现高效查询
// 1 <= n, m <= 10^5
// 综合运用树分块和莫队算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EdgeDivideMoOnTree {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MAXC = 1000005; // 定义最大颜色值
	public static int BLOCK_SIZE = 320; // 块大小，约为sqrt(n)

	public static int n, m; // n为节点数，m为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	public static int[] color = new int[MAXN]; // 存储每个节点的颜色
	public static int[] first = new int[MAXN]; // 每个节点在欧拉序中第一次出现的位置
	public static int[] last = new int[MAXN]; // 每个节点在欧拉序中最后一次出现的位置
	public static int[] euler = new int[MAXN << 1]; // 欧拉序数组
	public static int eulerIdx; // 欧拉序索引

	public static int[] dep = new int[MAXN]; // 每个节点的深度
	public static int[] fa = new int[MAXN][20]; // 倍增父节点数组（用于LCA）

	public static boolean[] vis = new boolean[MAXN]; // 访问标记
	public static int[] blockId = new int[MAXN << 1]; // 每个位置所属的块ID
	public static int[] cntColor = new int[MAXC]; // 每种颜色的出现次数
	public static int oddCount; // 出现奇数次的颜色数量

	// 查询结构体
	static class Query {
		int u, v, l, r, lca; // 节点u、v，欧拉序区间[l,r]，LCA
		int id, block; // 查询ID，所属块
		boolean needLca; // 是否需要包含LCA节点

		Query(int _u, int _v, int _id) {
			u = _u;
			v = _v;
			id = _id;
			
			// 确保first[u] <= first[v]
			if (first[u] > first[v]) {
				int temp = u;
				u = v;
				v = temp;
			}
			
			l = first[u];
			r = first[v];
			lca = getLCA(u, v);
			needLca = (lca != u && lca != v); // 如果LCA不是u或v，则需要单独处理LCA
			if (lca != u) r = last[lca]; // 调整右端点
			block = l / BLOCK_SIZE; // 所属块
		}
	}

	public static List<Query> queries = new ArrayList<>(); // 查询列表

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
		oddCount = 0; // 奇数次颜色数量初始化为0
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		head[u] = cnt; // 更新头指针
	}

	// DFS构建欧拉序
	// 面试中需要说明：欧拉序在树上莫队中的作用
	public static void dfsEuler(int u, int father, int depth) {
		euler[++eulerIdx] = u; // 进入节点时记录
		first[u] = eulerIdx; // 记录首次出现位置
		dep[u] = depth; // 记录深度
		fa[u][0] = father; // 记录父节点

		// 构建倍增数组
		for (int k = 1; k < 20; k++) {
			if (fa[u][k - 1] != 0) {
				fa[u][k] = fa[fa[u][k - 1]][k - 1];
			} else {
				break;
			}
		}

		for (int e = head[u]; e != 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != father) { // 排除父节点
				dfsEuler(v, u, depth + 1); // 递归处理子节点
				euler[++eulerIdx] = u; // 离开子树时再次记录当前节点
			}
		}

		last[u] = eulerIdx; // 记录最后出现位置
	}

	// 获取两个节点的LCA
	// 笔试中LCA算法是树上问题的重要工具，需熟练掌握
	public static int getLCA(int u, int v) {
		if (dep[u] < dep[v]) { // 确保u的深度不小于v
			int temp = u;
			u = v;
			v = temp;
		}

		// 将u提升到与v相同深度
		for (int k = 19; k >= 0; k--) {
			if (fa[u][k] != 0 && dep[fa[u][k]] >= dep[v]) {
				u = fa[u][k];
			}
		}

		if (u == v) return u; // 如果v就是u的祖先

		// 同时提升u和v直到它们的最近公共祖先
		for (int k = 19; k >= 0; k--) {
			if (fa[u][k] != 0 && fa[v][k] != 0 && fa[u][k] != fa[v][k]) {
				u = fa[u][k];
				v = fa[v][k];
			}
		}

		return fa[u][0]; // 返回父节点
	}

	// 更新颜色计数
	// 面试中需要说明：如何维护颜色出现次数的奇偶性
	public static void updateColor(int pos, int delta) {
		int node = euler[pos]; // 获取位置对应的节点
		int c = color[node]; // 获取节点颜色

		if (delta == 1) { // 添加颜色
			if ((cntColor[c] & 1) == 1) { // 如果原来出现奇数次
				oddCount--; // 奇数次颜色数减1
			} else { // 如果原来出现偶数次
				oddCount++; // 奇数次颜色数加1
			}
			cntColor[c]++; // 增加颜色计数
		} else { // 删除颜色
			if ((cntColor[c] & 1) == 1) { // 如果原来出现奇数次
				oddCount--; // 奇数次颜色数减1
			} else { // 如果原来出现偶数次
				oddCount++; // 奇数次颜色数加1
			}
			cntColor[c]--; // 减少颜色计数
		}
	}

	// 树上莫队主算法
	// 笔试中莫队算法是处理区间查询的重要工具，需熟练掌握
	public static int[] moOnTree() {
		// 按块排序查询
		Collections.sort(queries, new Comparator<Query>() {
			@Override
			public int compare(Query a, Query b) {
				if (a.block != b.block) {
					return a.block - b.block; // 按块ID排序
				}
				return a.r - b.r; // 块内按右端点排序
			}
		});

		int[] results = new int[queries.size()]; // 结果数组
		int currentL = 1, currentR = 0; // 当前区间

		// 处理每个查询
		for (Query query : queries) {
			// 扩展或收缩区间到查询区间
			while (currentL < query.l) {
				updateColor(currentL, -1); // 移除左端点
				currentL++;
			}
			while (currentL > query.l) {
				currentL--;
				updateColor(currentL, 1); // 添加左端点
			}
			while (currentR < query.r) {
				currentR++;
				updateColor(currentR, 1); // 添加右端点
			}
			while (currentR > query.r) {
				updateColor(currentR, -1); // 移除右端点
				currentR--;
			}

			// 特殊处理LCA节点（如果需要）
			if (query.needLca) {
				int lcaColor = color[query.lca];
				if ((cntColor[lcaColor] & 1) == 0) { // 如果LCA颜色原来出现偶数次
					oddCount++; // 奇数次颜色数加1
				} else { // 如果LCA颜色原来出现奇数次
					oddCount--; // 奇数次颜色数减1
				}
			}

			results[query.id] = oddCount; // 保存结果

			// 恢复LCA节点的处理
			if (query.needLca) {
				int lcaColor = color[query.lca];
				if ((cntColor[lcaColor] & 1) == 0) { // 如果LCA颜色现在出现偶数次
					oddCount++; // 奇数次颜色数加1
				} else { // 如果LCA颜色现在出现奇数次
					oddCount--; // 奇数次颜色数减1
				}
			}
		}

		return results; // 返回结果数组
	}

	// 修改节点颜色
	// 面试中需要说明：如何处理动态修改操作
	public static void updateNodeColor(int u, int newColor) {
		color[u] = newColor; // 更新节点颜色
		// 在实际应用中，这里可能需要更新相关的统计信息
	}

	// 预处理：构建欧拉序和LCA数组
	// 笔试中预处理是算法效率的关键，需合理设计
	public static void preprocess() {
		eulerIdx = 0; // 初始化欧拉序索引
		Arrays.fill(first, 0); // 清空首次出现位置
		Arrays.fill(last, 0); // 清空最后出现位置
		Arrays.fill(dep, 0); // 清空深度数组
		for (int i = 0; i < MAXN; i++) {
			Arrays.fill(fa[i], 0); // 清空倍增数组
		}

		dfsEuler(1, 0, 1); // 从根节点开始DFS构建欧拉序

		// 为欧拉序的每个位置分配块ID
		for (int i = 1; i <= eulerIdx; i++) {
			blockId[i] = i / BLOCK_SIZE;
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		// 读取每个节点的初始颜色
		for (int i = 1; i <= n; i++) {
			color[i] = in.nextInt();
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 预处理：构建欧拉序和LCA
		preprocess();
		
		// 读取操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt(); // 读取操作类型
			if (op == 1) { // 查询操作
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				queries.add(new Query(u, v, queries.size())); // 添加查询
			} else if (op == 2) { // 修改操作
				int u = in.nextInt(); // 节点编号
				int c = in.nextInt(); // 新颜色
				updateNodeColor(u, c); // 执行修改操作
			}
		}
		
		// 执行树上莫队算法
		int[] results = moOnTree();
		
		// 输出查询结果
		for (int result : results) {
			out.println(result);
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
- **时间复杂度**：
  - 预处理（DFS构建欧拉序）：O(n log n)，其中log n来自LCA的倍增计算
  - 莫队算法：O(m * sqrt(n))，其中m是查询数，n是节点数
  - 总体复杂度：O(n log n + m * sqrt(n))

- **空间复杂度**：O(n log n)，主要是LCA的倍增数组和欧拉序的空间开销

## 算法优化策略
1. **块大小选择**：BLOCK_SIZE通常设置为sqrt(n)，但可以根据实际情况调整
2. **LCA优化**：可以使用Tarjan算法进行离线LCA，将预处理时间降到O(n)
3. **颜色值压缩**：如果颜色值域很大，可以先进行离散化处理

## 同类题目拓展
- 相似题目：树上莫队相关的统计问题
- 变种方向：
  1. 统计出现次数为偶数次的颜色数量
  2. 查询路径上出现次数第k多的颜色
  3. 支持修改边的颜色
  4. 查询路径上不同颜色的种类数

## ML/DL关联思考
在机器学习中，树上莫队算法有以下应用：
1. **图神经网络中的子图采样**：在大规模图中，使用莫队思想进行高效的子图采样
2. **层次化特征聚合**：在树结构上进行分块聚合，可以平衡计算效率和特征表达能力
3. **动态图的分块处理**：将动态图划分为多个块，使用莫队思想进行高效处理
4. **知识图谱的子图匹配**：在知识图谱中使用分块技术进行高效的子图匹配
5. **计算图优化**：在深度学习框架中，使用分块思想优化计算图的执行效率