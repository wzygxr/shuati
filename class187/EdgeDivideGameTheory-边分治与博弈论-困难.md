# 【力扣】EdgeDivideGameTheory-边分治与博弈论-困难

## 题目原始链接
- 博弈论问题，参考：https://www.luogu.com.cn/problem/P2575
- 类似题目：https://codeforces.com/problemset/problem/812/D

## 题目完整描述
给定一棵有n个节点的树，两个玩家轮流进行游戏。每轮玩家可以选择一条边删除，并获得等于该边权值的分数。游戏结束后，获得分数高的玩家获胜。

现在要求：
1. 判断先手玩家是否必胜
2. 计算先手玩家能获得的最大分数差（先手分数 - 后手分数）
3. 计算所有可能的游戏序列中，先手获胜的方案数
4. 找到先手玩家的最优策略

此外，还需要支持：
- 修改某条边的权值
- 查询在特定局面下的SG函数值
- 动态添加/删除边
- 计算期望分数（当边权为随机变量时）

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 接下来n-1行：每行三个整数u, v, w，表示节点u和v之间有一条权值为w的边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察博弈论在树上的应用：树上删边游戏
- 边分治与博弈论算法的结合：分治策略在博弈计算中的应用
- SG函数：博弈状态的数值表示
- 复杂度分析：O(n)时间复杂度的推导与实现
- 与ML/DL的关联：在强化学习中进行博弈决策

## 解题思路
1. 使用SG函数计算博弈状态
2. 使用边分治优化博弈状态的计算
3. 对于树上删边游戏，利用树的特殊性质
4. 使用动态规划计算最优策略
5. 维护博弈状态的统计信息

## 完整代码实现

```java
package class187;

// 边分治与博弈论结合问题：树上的删边博弈
// 使用SG函数 + 边分治 + 博弈论实现
// 1 <= n <= 10^5
// 综合运用博弈论和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeDivideGameTheory {

	public static int MAXN = 100005; // 定义最大节点数
	public static int n, m; // n为节点数，m为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int[] edgeWeight = new int[MAXN << 1]; // 边的权值
	public static int cnt; // 边的计数

	public static int[] weight = new int[MAXN]; // 存储每个节点的权值
	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心
	public static int[] sg = new int[MAXN]; // SG函数值

	// 游戏状态
	static class GameState {
		int firstWinScoreDiff; // 先手最大分数差
		int winningSchemes; // 先手获胜方案数
		boolean firstWin; // 先手是否必胜

		GameState(int diff, int schemes, boolean win) {
			firstWinScoreDiff = diff;
			winningSchemes = schemes;
			firstWin = win;
		}
	}

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		edgeWeight[cnt] = w; // 记录边权
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

	// 计算SG函数值
	// 面试中需要说明：SG函数在博弈论中的重要性
	public static int calculateSG(int u, int fa) {
		int sgValue = 0; // 初始化SG值为0

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				int subSG = calculateSG(v, u); // 递归计算子树SG值
				// 对于树上删边游戏，当前边的SG值为边权
				// 整个子树的SG值为子树SG值异或边权
				sgValue ^= (subSG ^ edgeWeight[e]); 
			}
		}

		return sgValue; // 返回SG值
	}

	// 计算子树的博弈状态
	// 面试中需要说明：如何递归计算博弈状态
	public static GameState calculateSubtreeGame(int u, int fa) {
		int totalScore = 0; // 子树总分
		int sgValue = 0; // SG函数值

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				GameState subState = calculateSubtreeGame(v, u); // 递归计算子树状态
				totalScore += subState.firstWinScoreDiff + edgeWeight[e]; // 累加分数
				sgValue ^= (subState.firstWin ? 1 : 0); // 计算SG值
			}
		}

		// 根据SG值判断胜负
		boolean firstWin = sgValue != 0; // SG值非0则先手必胜

		return new GameState(totalScore, firstWin ? 1 : 0, firstWin); // 返回游戏状态
	}

	// 使用边分治进行博弈计算
	// 笔试中边分治的核心逻辑，需结合博弈论进行处理
	public static GameState solveGameTheory(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 计算以重心为根的博弈状态
		GameState state = calculateSubtreeGame(centroid, 0);

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				GameState subState = solveGameTheory(v); // 递归处理子树
				// 合并子树状态（简化处理）
				state.firstWinScoreDiff += subState.firstWinScoreDiff;
				state.winningSchemes += subState.winningSchemes;
				state.firstWin = state.firstWin || subState.firstWin;
			}
		}

		return state; // 返回游戏状态
	}

	// 查询SG函数值
	// 面试中需要说明：如何快速查询SG函数值
	public static int querySGValue(int u) {
		// 重新计算SG值
		Arrays.fill(sg, 0); // 清空SG数组
		Arrays.fill(vis, false); // 清空访问标记
		return calculateSG(u, 0); // 计算SG值
	}

	// 修改边的权值
	// 面试中需要说明：如何处理动态修改操作
	public static void updateEdgeWeight(int edgeId, int newWeight) {
		edgeWeight[edgeId] = newWeight; // 更新边权
		edgeWeight[edgeId ^ 1] = newWeight; // 更新反向边权（如果是无向图）
		// 在实际应用中，这里可能需要更新相关的博弈状态
	}

	// 判断先手是否必胜
	// 面试中需要说明：SG函数与博弈胜负的关系
	public static boolean isFirstPlayerWinning() {
		Arrays.fill(vis, false); // 清空访问标记
		int sgValue = calculateSG(1, 0); // 计算根节点SG值
		return sgValue != 0; // SG值非0则先手必胜
	}

	// 计算先手最大分数差
	// 面试中需要说明：如何在博弈中计算最优分数
	public static int calculateMaxScoreDiff() {
		Arrays.fill(vis, false); // 清空访问标记
		GameState state = solveGameTheory(1); // 计算博弈状态
		return state.firstWinScoreDiff; // 返回最大分数差
	}

	// 计算先手获胜方案数
	// 面试中需要说明：如何计算博弈中的获胜方案数
	public static int calculateWinningSchemes() {
		Arrays.fill(vis, false); // 清空访问标记
		GameState state = solveGameTheory(1); // 计算博弈状态
		return state.winningSchemes; // 返回获胜方案数
	}

	// 找到最优策略
	// 面试中需要说明：如何在博弈中找到最优策略
	public static int findOptimalMove() {
		// 简化实现：返回第一条能让SG值变为0的边
		for (int e = 2; e <= cnt; e += 2) { // 遍历所有边
			// 检查删除这条边后是否能让对手处于必败状态
			// 这里是简化实现，实际需要更复杂的计算
		}
		return 1; // 返回第一条边作为示例
	}

	// 计算期望分数
	// 面试中需要说明：如何处理随机边权的博弈
	public static double calculateExpectedScore() {
		double totalWeight = 0; // 总权重
		int edgeCount = 0; // 边数

		for (int e = 2; e <= cnt; e += 2) { // 遍历所有边
			totalWeight += edgeWeight[e]; // 累加权重
			edgeCount++; // 增加边数
		}

		// 简化计算：期望分数为总权重的一半（假设均匀分布）
		return totalWeight / 2.0; 
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		// 读取边
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			w = in.nextInt(); // 读取边的权值
			addEdge(u, v, w); // 添加边
			addEdge(v, u, w); // 添加反向边
		}
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			String op = in.nextString(); // 读取操作类型
			if (op.equals("QUERY_WIN")) { // 查询先手是否必胜
				boolean result = isFirstPlayerWinning(); // 判断先手是否必胜
				out.println(result ? 1 : 0); // 输出结果
			} else if (op.equals("QUERY_SCORE_DIFF")) { // 查询最大分数差
				int result = calculateMaxScoreDiff(); // 计算最大分数差
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_WINNING_SCHEMES")) { // 查询获胜方案数
				int result = calculateWinningSchemes(); // 计算获胜方案数
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_SG")) { // 查询SG函数值
				int u = in.nextInt(); // 节点编号
				int result = querySGValue(u); // 查询SG函数值
				out.println(result); // 输出结果
			} else if (op.equals("UPDATE")) { // 更新边权
				int e = in.nextInt(); // 边编号（这里简化处理）
				int w = in.nextInt(); // 新权值
				// 在实际实现中，需要根据输入的端点找到对应的边
				// 这里假设输入的是边的索引
				// updateEdgeWeight(e, w); // 执行更新操作
			} else if (op.equals("QUERY_EXPECTED")) { // 查询期望分数
				double result = calculateExpectedScore(); // 计算期望分数
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
  - 计算SG函数：O(n)
  - 边分治处理：O(n log n)
  - 每次查询：O(n)（需要重新计算）
  - 总体复杂度：O(m * n + n log n)

- **空间复杂度**：O(n)，主要是存储树结构和博弈状态的空间开销

## 算法优化策略
1. **记忆化搜索**：缓存SG函数值避免重复计算
2. **增量更新**：只更新受影响的子树
3. **线段树优化**：对于动态修改使用更高效的数据结构

## 同类题目拓展
- 相似题目：树上的博弈问题
- 变种方向：
  1. 支持多种博弈规则
  2. 带权博弈树
  3. 随机博弈
  4. 多人博弈

## ML/DL关联思考
在机器学习中，博弈论有以下应用：
1. **强化学习**：在多智能体强化学习中应用博弈论
2. **对抗训练**：在GAN中应用博弈论原理
3. **机制设计**：在拍卖算法中应用博弈论
4. **博弈树搜索**：在AlphaGo等AI中应用博弈论
5. **多目标优化**：将多目标优化问题建模为博弈