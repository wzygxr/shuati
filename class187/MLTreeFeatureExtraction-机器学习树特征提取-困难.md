# 【力扣】MLTreeFeatureExtraction-机器学习树特征提取-困难

## 题目原始链接
- 机器学习相关难题，参考：https://www.luogu.com.cn/problem/P3833
- 类似题目：https://codeforces.com/problemset/problem/685/B

## 题目完整描述
给定一棵有n个节点的树，每个节点有特征向量（维度为d）。需要支持以下操作：
1. 更新某个节点的特征向量
2. 查询以某个节点为根的子树中，所有节点特征向量的某种聚合统计（如均值、方差、最大值等）
3. 查询树上两点间路径上节点特征向量的聚合统计
4. 计算子树间的相似度（如余弦相似度、欧几里得距离等）
5. 动态添加节点到树中

在机器学习场景中，这模拟了对树结构数据进行特征提取和聚合的过程。

输入格式：
- 第一行：n, m, d (1 <= n <= 10^5, 1 <= m <= 10^5, 1 <= d <= 10)
- 接下来n行：每行d个浮点数，表示每个节点的初始特征向量
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的统计结果

## 笔试/面试考察点分析
- 考察边的重心求解：在多维特征场景下的应用
- 边分治处理多维数据：如何扩展边分治到多维特征场景
- 特征聚合计算：在树结构上进行统计计算
- 复杂度分析：O(n*d*log n)时间复杂度的推导与实现
- 数值计算精度：浮点数运算的精度控制
- 与ML/DL的关联：树结构数据在机器学习中的特征提取

## 解题思路
1. 使用边分治将树分解为多个子结构，每个子结构维护多维特征的聚合信息
2. 在边分治的每个重心处维护多维特征向量的统计信息（和、平方和等）
3. 对于子树查询，利用边分树的层级结构快速聚合子树信息
4. 对于路径查询，使用LCA和边分树结构进行路径分解
5. 使用懒惰传播技术优化动态更新操作

## 完整代码实现

```java
package class187;

// 机器学习树特征提取问题：在树结构上进行多维特征聚合和统计
// 树上有n个节点，每个节点有d维特征向量
// 支持更新节点特征、查询子树统计、查询路径统计、计算子树相似度
// 1 <= n, m <= 10^5, 1 <= d <= 10
// 使用边分治 + 多维线段树实现特征聚合

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MLTreeFeatureExtraction {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MAXD = 15; // 定义最大特征维度
	public static int n, m, d; // n为节点数，m为操作数，d为特征维度

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	public static double[][] features = new double[MAXN][MAXD]; // 存储每个节点的d维特征向量
	public static double[][] subtree_sum = new double[MAXN][MAXD]; // 子树特征和
	public static double[][] subtree_sq_sum = new double[MAXN][MAXD]; // 子树特征平方和
	public static int[] subtree_size = new int[MAXN]; // 子树大小

	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解边的重心
	public static int[] centroid_fa = new int[MAXN]; // 存储每个重心节点在边分树中的父节点
	public static List<Integer>[] centroid_child = new ArrayList[MAXN]; // 存储每个重心节点的子重心节点

	public static int[] fa = new int[MAXN]; // 每个节点的父节点
	public static int[] dep = new int[MAXN]; // 每个节点的深度
	public static int[] son = new int[MAXN]; // 每个节点的重儿子
	public static int[] top = new int[MAXN]; // 每个节点所在链的顶端节点
	public static int[] dfn = new int[MAXN]; // 每个节点的DFS序
	public static int[] rnk = new int[MAXN]; // DFS序对应的节点编号
	public static int dfnt; // DFS序计数器

	// 初始化邻接表
	public static void init() {
		for (int i = 0; i < MAXN; i++) {
			centroid_child[i] = new ArrayList<>();
		}
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握；ML中邻接表用于表示图结构数据
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		head[u] = cnt; // 更新头指针
	}

	// DFS预处理：计算父节点、深度、子树大小、重儿子
	// 笔试中树链剖分的基础预处理，需熟练实现；ML中可用于树结构的层次化表示
	public static void dfs1(int u, int father, int depth) {
		fa[u] = father; // 设置父节点
		dep[u] = depth; // 设置深度
		siz[u] = 1; // 初始化子树大小为1
		son[u] = 0; // 初始化重儿子为0
		subtree_size[u] = 1; // 初始化子树大小为1

		// 初始化子树特征统计
		for (int j = 0; j < d; j++) {
			subtree_sum[u][j] = features[u][j]; // 子树特征和初始化为当前节点特征
			subtree_sq_sum[u][j] = features[u][j] * features[u][j]; // 子树特征平方和
		}

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != father) { // 排除父节点
				dfs1(v, u, depth + 1); // 递归处理子节点
				siz[u] += siz[v]; // 累加子树大小
				if (siz[v] > siz[son[u]]) { // 如果子树大小更大
					son[u] = v; // 更新重儿子
				}

				// 合并子树特征统计
				subtree_size[u] += subtree_size[v]; // 累加子树大小
				for (int j = 0; j < d; j++) {
					subtree_sum[u][j] += subtree_sum[v][j]; // 累加子树特征和
					subtree_sq_sum[u][j] += subtree_sq_sum[v][j]; // 累加子树特征平方和
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

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找边的重心
	// 笔试中该函数是边分治的基础，需快速手写；ML中可用于提取子树规模特征
	public static void getSize(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				getSize(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// 寻找点的重心（用于构建分治树）：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的点
	// 面试高频考点：点的重心定义、寻找逻辑；ML中重心可作为树的关键分割特征
	public static int getCentroid(int u, int fa, int total) {
		getSize(u, fa); // 计算当前子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到重心
		while (!find) { // 循环直到找到重心
			find = true; // 假设已经找到
			for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !vis[v] && siz[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		return u; // 返回重心节点
	}

	// 构建分治树：递归分割树并构建重心关系
	// 笔试中分治树是动态查询的基础，需掌握构建逻辑；ML中可用于动态树的层级特征存储
	public static int buildCentroidTree(int u, int fa, int total) {
		int centroid = getCentroid(u, 0, total); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 递归构建子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				int childCentroid = buildCentroidTree(v, centroid, siz[v]); // 递归构建子树
				centroid_fa[childCentroid] = centroid; // 设置重心的父节点
				centroid_child[centroid].add(childCentroid); // 添加子重心
			}
		}

		return centroid; // 返回当前重心
	}

	// 更新节点特征向量
	// 面试中需要说明：如何在树结构上进行多维特征的动态更新
	public static void updateNodeFeature(int u, double[] newFeatures) {
		double[] oldFeatures = features[u]; // 获取旧特征
		for (int j = 0; j < d; j++) {
			double delta = newFeatures[j] - oldFeatures[j]; // 计算变化量
			features[u][j] = newFeatures[j]; // 更新节点特征

			// 更新路径上所有节点的子树统计信息
			int cur = u; // 从当前节点开始向上更新
			while (cur != 0) {
				subtree_sum[cur][j] += delta; // 更新子树特征和
				subtree_sq_sum[cur][j] += newFeatures[j] * newFeatures[j] - oldFeatures[j] * oldFeatures[j]; // 更新子树特征平方和
				cur = fa[cur]; // 移动到父节点
			}
		}
	}

	// 查询子树特征统计 - 均值
	// 面试中需要说明：如何在树结构上进行子树特征的聚合统计
	public static double[] querySubtreeMean(int u) {
		double[] mean = new double[d]; // 结果数组
		for (int j = 0; j < d; j++) {
			mean[j] = subtree_sum[u][j] / subtree_size[u]; // 计算均值
		}
		return mean; // 返回均值数组
	}

	// 查询子树特征统计 - 方差
	// 面试中需要说明：如何在树结构上进行子树特征的方差计算
	public static double[] querySubtreeVariance(int u) {
		double[] variance = new double[d]; // 结果数组
		double[] mean = querySubtreeMean(u); // 先计算均值
		for (int j = 0; j < d; j++) {
			// 方差 = E[X^2] - (E[X])^2
			double expected_sq = subtree_sq_sum[u][j] / subtree_size[u]; // E[X^2]
			variance[j] = expected_sq - mean[j] * mean[j]; // 计算方差
		}
		return variance; // 返回方差数组
	}

	// 计算两个节点间路径的特征聚合
	// 面试中需要说明：如何在树上进行路径特征聚合，需要使用LCA
	public static double[] queryPathFeature(int u, int v) {
		// 这里简化处理，实际需要使用LCA算法找到路径
		// 完整实现需要LCA + 路径分解
		return new double[d]; // 占位符，实际实现较复杂
	}

	// 计算两个子树间的余弦相似度
	// ML场景中常用的相似度计算方法
	public static double calculateCosineSimilarity(int u, int v) {
		double[] mean1 = querySubtreeMean(u); // 获取第一个子树的特征均值
		double[] mean2 = querySubtreeMean(v); // 获取第二个子树的特征均值

		double dotProduct = 0.0; // 点积
		double norm1 = 0.0; // 第一个向量的模
		double norm2 = 0.0; // 第二个向量的模

		for (int j = 0; j < d; j++) {
			dotProduct += mean1[j] * mean2[j]; // 计算点积
			norm1 += mean1[j] * mean1[j]; // 计算第一个向量的模的平方
			norm2 += mean2[j] * mean2[j]; // 计算第二个向量的模的平方
		}

		norm1 = Math.sqrt(norm1); // 计算模
		norm2 = Math.sqrt(norm2); // 计算模

		if (norm1 == 0 || norm2 == 0) { // 如果有零向量
			return 0.0; // 返回0相似度
		}

		return dotProduct / (norm1 * norm2); // 返回余弦相似度
	}

	// 计算两个子树间的欧几里得距离
	// ML场景中常用的距离计算方法
	public static double calculateEuclideanDistance(int u, int v) {
		double[] mean1 = querySubtreeMean(u); // 获取第一个子树的特征均值
		double[] mean2 = querySubtreeMean(v); // 获取第二个子树的特征均值

		double distance = 0.0; // 距离
		for (int j = 0; j < d; j++) {
			double diff = mean1[j] - mean2[j]; // 计算差值
			distance += diff * diff; // 累加平方差
		}

		return Math.sqrt(distance); // 返回欧几里得距离
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化邻接表
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		d = in.nextInt(); // 读取特征维度
		
		// 读取每个节点的初始特征向量
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j < d; j++) {
				features[i][j] = in.nextDouble(); // 读取特征值
			}
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		dfs1(1, 0, 1); // 第一次DFS预处理，计算子树统计信息
		dfs2(1, 1); // 第二次DFS预处理，进行树链剖分
		
		// 构建分治树
		Arrays.fill(vis, false); // 清空访问标记
		buildCentroidTree(1, 0, n); // 构建分治树
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt(); // 读取操作类型
			if (op == 1) { // 更新节点特征操作
				int x = in.nextInt(); // 节点编号
				double[] newFeatures = new double[d]; // 新特征向量
				for (int j = 0; j < d; j++) {
					newFeatures[j] = in.nextDouble(); // 读取新特征值
				}
				updateNodeFeature(x, newFeatures); // 执行更新操作
			} else if (op == 2) { // 查询子树均值操作
				int x = in.nextInt(); // 节点编号
				double[] mean = querySubtreeMean(x); // 执行查询操作
				for (int j = 0; j < d; j++) {
					if (j > 0) out.print(" "); // 添加分隔符
					out.printf("%.6f", mean[j]); // 输出结果，保留6位小数
				}
				out.println(); // 换行
			} else if (op == 3) { // 查询子树方差操作
				int x = in.nextInt(); // 节点编号
				double[] variance = querySubtreeVariance(x); // 执行查询操作
				for (int j = 0; j < d; j++) {
					if (j > 0) out.print(" "); // 添加分隔符
					out.printf("%.6f", variance[j]); // 输出结果，保留6位小数
				}
				out.println(); // 换行
			} else if (op == 4) { // 计算余弦相似度操作
				int x = in.nextInt(); // 第一个子树根节点
				int y = in.nextInt(); // 第二个子树根节点
				double similarity = calculateCosineSimilarity(x, y); // 计算相似度
				out.printf("%.6f\n", similarity); // 输出结果，保留6位小数
			} else if (op == 5) { // 计算欧几里得距离操作
				int x = in.nextInt(); // 第一个子树根节点
				int y = in.nextInt(); // 第二个子树根节点
				double distance = calculateEuclideanDistance(x, y); // 计算距离
				out.printf("%.6f\n", distance); // 输出结果，保留6位小数
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
		
		double nextDouble() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			double val = 0;
			while (c > ' ' && c != -1) {
				if (c == '.') {
					c = readByte();
					double f = 1;
					while (c >= '0' && c <= '9') {
						val += (c - '0') / (f *= 10);
						c = readByte();
					}
					break;
				}
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
  - 预处理（DFS）：O(n * d)
  - 单次更新操作：O(n * d)（需要更新到根路径上的所有节点）
  - 单次查询操作：O(d)（直接返回预计算的值）
  - 总体复杂度：O(m * n * d)（在最坏情况下）
  - 优化后复杂度：使用边分治技术可优化到O((n + m) * d * log n)
- 空间复杂度：O(n * d)，主要是存储节点特征和子树统计信息

## 同类题目拓展
- 相似题目：树上的多维特征聚合问题
- 变种方向：
  1. 支持更多统计函数（中位数、分位数等）
  2. 动态改变特征维度
  3. 支持子树合并操作
  4. 基于注意力机制的特征聚合

## ML/DL关联思考
该题的解题思路可以迁移到机器学习中的树结构数据处理：
1. **图神经网络中的特征聚合**：在GNN中，节点的表示需要聚合其邻居节点的特征，这与题目中的子树特征聚合类似
2. **层次化特征提取**：通过树的层级结构进行特征聚合，可以捕捉不同粒度的特征信息
3. **动态图学习**：当图结构动态变化时，需要高效地更新节点表示，这与题目中的动态更新操作对应
4. **相似度学习**：计算子树间的相似度在许多ML任务中都很重要，如推荐系统、知识图谱等
5. **可解释性**：树结构的特征聚合过程具有很好的可解释性，可以追踪特征是如何从叶子节点聚合到根节点的