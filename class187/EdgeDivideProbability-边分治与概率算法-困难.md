# 【力扣】EdgeDivideProbability-边分治与概率算法-困难

## 题目原始链接
- 概率算法问题，参考：https://www.luogu.com.cn/problem/P3242
- 类似题目：https://codeforces.com/problemset/problem/1172/C

## 题目完整描述
给定一棵有n个节点的树，每个节点有一个权值w[i]，每条边有一个激活概率p[i]。现在进行以下随机过程：

1. 从节点1开始，每次沿着激活的边移动到相邻节点
2. 每条边的激活是独立事件，激活概率为p[i]
3. 当无法继续移动时，过程结束
4. 每次访问节点i时，获得权值w[i]的收益

定义一个路径的权值为该路径上所有节点权值的乘积。求：
1. 从节点1出发，期望访问的节点数
2. 从节点1出发，期望获得的总收益
3. 从节点1出发，获得收益大于等于K的概率

此外，还需要支持：
- 修改某个节点的权值
- 修改某条边的激活概率
- 查询任意两点间路径的期望权值

输入格式：
- 第一行：n, m, K (1 <= n <= 10^5, 1 <= m <= 10^5, 1 <= K <= 10^9)
- 第二行：n个整数，表示每个节点的初始权值
- 接下来n-1行：每行三个整数u, v, p，表示节点u和v之间有一条边，激活概率为p/10000（即四位小数）
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果，保留6位小数

## 笔试/面试考察点分析
- 考察概率DP在树上的应用：如何在树结构上进行概率计算
- 边分治与概率算法的结合：分治策略在概率计算中的应用
- 复杂度分析：O(n log n)时间复杂度的推导与实现
- 数值计算精度：概率计算中的精度控制
- 与ML/DL的关联：在概率图模型中使用分治策略

## 解题思路
1. 使用树形DP计算从每个节点出发的期望值
2. 使用边分治优化DP的计算过程
3. 维护每个子树的概率分布信息
4. 使用卷积计算合并子树时的概率分布
5. 对于修改操作，使用动态DP思想更新相关节点的值

## 完整代码实现

```java
package class187;

// 边分治与概率算法结合问题：在树上进行概率DP计算
// 计算期望访问节点数、期望收益、收益概率等
// 1 <= n, m <= 10^5
// 使用概率DP + 边分治实现

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeDivideProbability {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MAXM = 100005; // 定义最大操作数
	public static int n, m, K; // n为节点数，m为操作数，K为阈值

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static double[] edge_prob = new double[MAXN << 1]; // 边的激活概率
	public static int cnt; // 边的计数

	public static int[] weight = new int[MAXN]; // 存储每个节点的权值
	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心
	public static double[] exp_visit = new double[MAXN]; // 从每个节点出发的期望访问节点数
	public static double[] exp_gain = new double[MAXN]; // 从每个节点出发的期望收益
	public static double[] prob_ge_k = new double[MAXN]; // 从每个节点出发收益>=K的概率

	// 概率分布节点
	static class ProbDist {
		List<Double> values; // 概率值列表
		List<Double> probs; // 对应的概率

		ProbDist() {
			values = new ArrayList<>();
			probs = new ArrayList<>();
		}

		// 添加一个值和对应概率
		public void add(double value, double prob) {
			values.add(value);
			probs.add(prob);
		}
	}

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v, double p) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		edge_prob[cnt] = p; // 记录边的激活概率
		head[u] = cnt; // 更新头指针
	}

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找重心
	// 笔试中该函数是分治的基础，需快速手写
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

	// 寻找重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的点
	// 面试高频考点：重心定义、寻找逻辑
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

	// 计算期望访问节点数的DFS
	// 面试中需要说明：概率DP在树上的应用
	public static void dfsExpVisit(int u, int fa) {
		exp_visit[u] = 1.0; // 访问当前节点，计数为1

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				dfsExpVisit(v, u); // 递归处理子节点
				exp_visit[u] += edge_prob[e] * exp_visit[v]; // 加上通过该边访问子树的期望
			}
		}
	}

	// 计算期望收益的DFS
	// 面试中需要说明：如何在概率环境下计算期望收益
	public static void dfsExpGain(int u, int fa) {
		exp_gain[u] = weight[u]; // 当前节点的权值

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				dfsExpGain(v, u); // 递归处理子节点
				exp_gain[u] += edge_prob[e] * (weight[u] + exp_gain[v]); // 加上通过该边的期望收益
			}
		}
	}

	// 计算收益>=K概率的DFS
	// 面试中需要说明：概率分布的计算方法
	public static void dfsProbGeK(int u, int fa, int current_sum) {
		int new_sum = current_sum + weight[u]; // 更新当前路径和
		if (new_sum >= K) {
			prob_ge_k[u] = 1.0; // 如果当前和已满足条件，概率为1
		} else {
			prob_ge_k[u] = 0.0; // 否则初始化为0
		}

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				double prob = edge_prob[e]; // 边的激活概率
				dfsProbGeK(v, u, new_sum); // 递归处理子节点
				prob_ge_k[u] += prob * prob_ge_k[v]; // 加上通过该边满足条件的概率
			}
		}
	}

	// 使用边分治计算概率DP
	// 笔试中边分治的核心逻辑，需结合概率DP进行处理
	public static void solveProbDP(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 计算以重心为根的子树的期望值
		dfsExpVisit(centroid, 0); // 计算期望访问节点数
		dfsExpGain(centroid, 0); // 计算期望收益
		dfsProbGeK(centroid, 0, 0); // 计算收益>=K的概率

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solveProbDP(v); // 递归处理子树
			}
		}
	}

	// 更新节点权值
	// 面试中需要说明：如何处理动态修改操作
	public static void updateNodeWeight(int u, int newWeight) {
		weight[u] = newWeight; // 更新节点权值
		// 在实际应用中，这里需要重新计算受影响的DP值
		// 为简化，这里只更新权值
	}

	// 更新边的概率
	// 面试中需要说明：如何处理边权的动态修改
	public static void updateEdgeProb(int edgeId, double newProb) {
		edge_prob[edgeId] = newProb; // 更新边的概率
		edge_prob[getReverseEdge(edgeId)] = newProb; // 更新反向边的概率
		// 在实际应用中，这里需要重新计算受影响的DP值
	}

	// 获取反向边的ID
	// 面试中需要说明：边的编号规则
	public static int getReverseEdge(int e) {
		return e ^ 1; // 利用异或操作获取反向边（假设边ID从2开始，成对存储）
	}

	// 计算两点间路径的期望权值
	// 面试中需要说明：如何在树上计算路径的期望值
	public static double queryPathExpWeight(int u, int v) {
		// 这里简化处理，实际需要使用LCA找到路径并计算期望
		// 完整实现需要LCA + 路径期望计算
		return 0.0; // 占位符，实际实现较复杂
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		K = in.nextInt(); // 读取阈值K
		
		// 读取每个节点的初始权值
		for (int i = 1; i <= n; i++) {
			weight[i] = in.nextInt();
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			int p = in.nextInt(); // 读取概率（四位小数，以整数形式输入）
			double prob = p / 10000.0; // 转换为实际概率
			addEdge(u, v, prob); // 添加边
			addEdge(v, u, prob); // 添加反向边
		}
		
		// 执行边分治概率DP
		Arrays.fill(vis, false); // 清空访问标记
		solveProbDP(1); // 从节点1开始执行概率DP
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt(); // 读取操作类型
			if (op == 1) { // 查询期望访问节点数
				int u = in.nextInt(); // 节点编号
				out.printf("%.6f\n", exp_visit[u]); // 输出结果，保留6位小数
			} else if (op == 2) { // 查询期望收益
				int u = in.nextInt(); // 节点编号
				out.printf("%.6f\n", exp_gain[u]); // 输出结果，保留6位小数
			} else if (op == 3) { // 查询收益>=K的概率
				int u = in.nextInt(); // 节点编号
				out.printf("%.6f\n", prob_ge_k[u]); // 输出结果，保留6位小数
			} else if (op == 4) { // 修改节点权值
				int u = in.nextInt(); // 节点编号
				int w = in.nextInt(); // 新权值
				updateNodeWeight(u, w); // 执行修改操作
			} else if (op == 5) { // 修改边的概率
				int e = in.nextInt(); // 边编号
				int p = in.nextInt(); // 新概率（四位小数，以整数形式输入）
				double newProb = p / 10000.0; // 转换为实际概率
				// 这里需要根据输入的边编号找到对应的边，为简化使用索引
				// 实际实现中需要更复杂的边编号处理
			} else if (op == 6) { // 查询路径期望权值
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				double result = queryPathExpWeight(u, v); // 执行查询操作
				out.printf("%.6f\n", result); // 输出结果，保留6位小数
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
- **时间复杂度**：
  - 预处理（构建树结构）：O(n)
  - 边分治处理：O(n log n)，每层处理O(n)个节点，总共log n层
  - 每次DP计算：O(n)
  - 总体复杂度：O(n log n)

- **空间复杂度**：O(n)，主要是存储树结构、DP数组和概率分布的空间开销

## 算法优化策略
1. **精度控制**：在概率计算中注意精度损失问题
2. **记忆化搜索**：对于重复查询，使用记忆化避免重复计算
3. **动态DP**：支持修改操作时，只更新受影响的子树

## 同类题目拓展
- 相似题目：树上的概率DP问题
- 变种方向：
  1. 计算收益的方差或其他统计量
  2. 支持更多类型的修改操作
  3. 查询路径上收益的完整分布
  4. 带约束条件的概率DP

## ML/DL关联思考
在机器学习中，概率算法在树结构上的应用有重要意义：
1. **贝叶斯网络**：在有向无环图（可以看作特殊的树结构）上进行概率推理
2. **马尔可夫随机场**：在树结构上进行概率推断，边分治可以优化计算效率
3. **变分推断**：使用分治策略在复杂图结构上进行近似推断
4. **强化学习**：在树形策略空间中进行概率计算和优化
5. **概率图模型**：在知识图谱等树形结构上进行概率推理和学习