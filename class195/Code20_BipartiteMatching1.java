package class195;

// 二分图匹配优化建图基础模板，Java 版
// 本代码展示二分图匹配优化建图的核心模板，用于解决最大匹配问题
// 测试链接 : https://www.luogu.com.cn/problem/P3386（改编）
// 本模板展示了二分图匹配优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 二分图匹配优化建图核心知识点 =====================
// 【问题分析】
// 二分图匹配用于解决两类对象之间的最优配对问题
// 通过建图转化为最大流问题，使用匈牙利算法或最大流求解
// 主要应用于任务分配、资源匹配、课程安排等
//
// 【核心原理】
// 二分图构建：左部点表示一类对象，右部点表示另一类对象
// 虚拟源汇：添加源点连向左部，右部连向汇点
// 最大匹配：转化为最大流，所有边容量为 1
// 匈牙利算法：通过增广路寻找最大匹配
//
// 【复杂度分析】
// 节点数：O(n + m)（左右部点 + 源汇）
// 边数：O(E)（匹配关系数）
// 匈牙利算法：O(nE)
// 最大流（Dinic）：O(E√V)
//
// 【ML/DL 关联价值】
// 1. 推荐系统中的用户 - 物品匹配
// 2. 多目标跟踪中的数据关联
// 3. 图神经网络中的二分图结构处理

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code20_BipartiteMatching1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static int MAXE = 500001;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int cnt;

	// ===================== 匈牙利算法变量区 =====================
	public static int[] match = new int[MAXN];
	public static boolean[] visited = new boolean[MAXN];
	public static int n1, n2, m;

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	// 面试高频提问：二分图中为什么只需要单向连边？
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	// ===================== 核心函数：匈牙利算法 DFS =====================
	// 功能：从左部点 u 出发寻找增广路
	// 核心思想：贪心 + 回溯，为 u 寻找匹配或让已匹配的点重新匹配
	// 面试高频提问：增广路的性质？为什么找到增广路就能增加匹配数？
	public static boolean dfs(int u) {
		// 遍历 u 的所有邻接点
		for (int e = head[u]; e > 0; e = next[e]) {
			int v = to[e];
			// 如果 v 未被访问
			if (!visited[v]) {
				visited[v] = true;
				// 如果 v 未匹配，或可以为 v 的匹配点找到新的匹配
				if (match[v] == 0 || dfs(match[v])) {
					match[v] = u;
					return true;
				}
			}
		}
		return false;
	}

	// ===================== 核心函数：匈牙利算法求最大匹配 =====================
	// 功能：计算二分图的最大匹配数
	// 核心思想：依次为每个左部点寻找增广路
	// 面试高频提问：匈牙利算法的时间复杂度？如何优化？
	public static int hungarian() {
		int result = 0;
		Arrays.fill(match, 0);

		// 依次为每个左部点寻找匹配
		for (int i = 1; i <= n1; i++) {
			Arrays.fill(visited, false);
			if (dfs(i)) {
				result++;
			}
		}
		return result;
	}

	// ===================== 核心函数：获取匹配方案 =====================
	// 功能：返回具体的匹配方案
	// 核心思想：match 数组记录了右部点的匹配对象
	// 面试高频提问：如何输出匹配方案？匹配的唯一性如何？
	public static int[][] getMatching() {
		List<int[]> pairs = new ArrayList<>();
		for (int j = 1; j <= n2; j++) {
			if (match[j] != 0) {
				pairs.add(new int[]{match[j], j});
			}
		}
		return pairs.toArray(new int[pairs.size()][]);
	}

	// ===================== 核心函数：构建网络流模型 =====================
	// 功能：将二分图匹配转化为最大流问题
	// 核心思想：添加源点连左部，右部连汇点，所有边容量为 1
	// 面试高频提问：为什么可以转化为最大流？复杂度如何？
	public static void buildNetworkFlow(int source, int sink) {
		// 源点向左部点连边
		for (int i = 1; i <= n1; i++) {
			addEdge(source, i);
		}
		// 右部点向汇点连边
		for (int j = 1; j <= n2; j++) {
			addEdge(n1 + j, sink);
		}
	}

	// ===================== 快速读入类 =====================
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
				if (len <= 0) {
					return -1;
				}
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

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入左部点数、右部点数、边数
		n1 = in.nextInt();
		n2 = in.nextInt();
		m = in.nextInt();

		// 读入 m 条边
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			// 左部点 u 向右部点 v 连边
			addEdge(u, v);
		}

		// 使用匈牙利算法求最大匹配
		int maxMatching = hungarian();
		out.println("最大匹配数：" + maxMatching);

		// 输出匹配方案
		out.println("匹配方案：");
		int[][] matching = getMatching();
		for (int[] pair : matching) {
			out.println("左部 " + pair[0] + " <-> 右部 " + pair[1]);
		}

		out.flush();
		out.close();
	}
}
