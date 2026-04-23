package class195;

// 2-SAT 优化建图基础模板，Java 版
// 本代码展示 2-SAT 问题的优化建图核心模板，用于解决布尔可满足性问题
// 测试链接 : https://www.luogu.com.cn/problem/P4782
// 本模板展示了 2-SAT 优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 2-SAT 优化建图核心知识点 =====================
// 【问题分析】
// 2-SAT 问题是判断一组布尔变量是否能满足所有约束的问题
// 每个约束涉及两个变量，形如 (x_i = a) OR (x_j = b)
// 通过建图转化为强连通分量问题
//
// 【核心原理】
// 变量拆分：每个变量 x_i 拆分为两个节点：x_i(true) 和 x_i(false)
// 约束转化：(x_i = a) OR (x_j = b) 转化为两条蕴含边
// 判断条件：如果 x_i(true) 和 x_i(false) 在同一 SCC 中，则无解
//
// 【复杂度分析】
// 节点数：2n（每个变量两个状态）
// 边数：2m（每个约束两条边）
// Tarjan 复杂度：O(n + m)
//
// 【ML/DL 关联价值】
// 1. 逻辑推理系统的自动验证
// 2. 约束满足问题的求解
// 3. 知识图谱中的逻辑一致性检查

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Code13_TwoSAT1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 2000001;
	public static int MAXE = 4000001;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int cnt;

	// ===================== Tarjan 算法变量区 =====================
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int[] scc = new int[MAXN];
	public static int timer, sccCnt;
	public static Stack<Integer> stack = new Stack<>();
	public static boolean[] inStack = new boolean[MAXN];

	// ===================== 2-SAT 变量区 =====================
	public static int n, m;

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	// ===================== 核心函数：变量编号转换 =====================
	// 功能：将变量 x_i 的第 k 个状态转换为节点编号
	// 核心思想：x_i(true) -> 2*i, x_i(false) -> 2*i+1
	// 面试高频提问：为什么要这样编号？如何表示否定？
	public static int getNodeId(int i, boolean val) {
		return val ? 2 * i : 2 * i + 1;
	}

	// ===================== 核心函数：添加 2-SAT 约束 =====================
	// 功能：添加约束 (x_i = a) OR (x_j = b)
	// 核心思想：转化为两条蕴含边
	// NOT(x_i = a) -> (x_j = b) 和 NOT(x_j = b) -> (x_i = a)
	// 面试高频提问：为什么是两条边？如何理解蕴含关系？
	public static void addClause(int i, boolean a, int j, boolean b) {
		// NOT(x_i = a) -> (x_j = b)
		addEdge(getNodeId(i, !a), getNodeId(j, b));
		// NOT(x_j = b) -> (x_i = a)
		addEdge(getNodeId(j, !b), getNodeId(i, a));
	}

	// ===================== 核心函数：Tarjan 算法求 SCC =====================
	// 功能：使用 Tarjan 算法求强连通分量
	// 核心思想：通过 dfn 和 low 数组识别 SCC
	// 面试高频提问：Tarjan 算法的原理？dfn 和 low 的区别？
	public static void tarjan(int u) {
		dfn[u] = low[u] = ++timer;
		stack.push(u);
		inStack[u] = true;

		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = next[e]) {
			int v = to[e];
			if (dfn[v] == 0) {
				// v 未访问，递归处理
				tarjan(v);
				low[u] = Math.min(low[u], low[v]);
			} else if (inStack[v]) {
				// v 在栈中，更新 low
				low[u] = Math.min(low[u], dfn[v]);
			}
		}

		// 找到 SCC 的根节点
		if (dfn[u] == low[u]) {
			sccCnt++;
			// 弹出 SCC 中的所有节点
			while (true) {
				int v = stack.pop();
				inStack[v] = false;
				scc[v] = sccCnt;
				if (u == v) {
					break;
				}
			}
		}
	}

	// ===================== 核心函数：2-SAT 求解 =====================
	// 功能：判断 2-SAT 问题是否有解
	// 核心思想：检查每个变量的两个状态是否在同一 SCC 中
	// 面试高频提问：为什么在同一 SCC 中就无解？如何输出方案？
	public static boolean solve() {
		// 对所有未访问的节点运行 Tarjan
		for (int i = 0; i < 2 * n; i++) {
			if (dfn[i] == 0) {
				tarjan(i);
			}
		}

		// 检查每个变量的两个状态
		for (int i = 0; i < n; i++) {
			if (scc[2 * i] == scc[2 * i + 1]) {
				// true 和 false 在同一 SCC 中，无解
				return false;
			}
		}
		return true;
	}

	// ===================== 核心函数：获取解 =====================
	// 功能：如果问题有解，返回一组可行解
	// 核心思想：选择 SCC 编号较小的状态
	// 面试高频提问：为什么选择 SCC 编号较小的？如何理解拓扑序？
	public static boolean[] getSolution() {
		boolean[] result = new boolean[n];
		for (int i = 0; i < n; i++) {
			// 选择 SCC 编号较小的状态
			result[i] = scc[2 * i] < scc[2 * i + 1];
		}
		return result;
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

		// 读入变量数和约束数
		n = in.nextInt();
		m = in.nextInt();

		// 处理 m 个约束
		for (int i = 0; i < m; i++) {
			int x = in.nextInt();
			int a = in.nextInt();
			int y = in.nextInt();
			int b = in.nextInt();
			// 添加约束 (x = a) OR (y = b)
			// 注意：输入从 1 开始，需要转换为从 0 开始
			addClause(x - 1, a == 1, y - 1, b == 1);
		}

		// 求解 2-SAT 问题
		if (solve()) {
			out.println("Yes");
			// 输出一组可行解
			boolean[] solution = getSolution();
			for (int i = 0; i < n; i++) {
				out.print(solution[i] ? 1 : 0);
				out.print(i == n - 1 ? "" : " ");
			}
			out.println();
		} else {
			out.println("No");
		}

		out.flush();
		out.close();
	}
}
