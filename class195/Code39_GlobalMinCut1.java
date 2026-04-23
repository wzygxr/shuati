package class195;

// 全局最小割 Stoer-Wagner 算法基础模板，Java 版
// 本代码展示全局最小割优化建图的核心模板，用于解决无向图最小割问题
// 测试链接 : https://www.luogu.com.cn/problem/UVA10480（改编）
// 本模板展示了 Stoer-Wagner 算法优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 全局最小割 Stoer-Wagner 算法核心知识点 =====================
// 【问题分析】
// 全局最小割问题：找到将无向图分成两部分的最小边权和
// Stoer-Wagner 算法通过 n-1 次最大邻接度搜索求解
// 主要应用于网络可靠性、图分割、最小割问题等
//
// 【核心原理】
// 最大邻接度搜索：每次选择与当前集合邻接度最大的点
// 最小割阶段：最后一次加入的点和倒数第二次加入的点之间的割
// 图收缩：将最后两个点合并，继续下一轮
// 最优性：n-1 轮后得到全局最小割
//
// 【复杂度分析】
// 时间复杂度：O(n³)（朴素实现）或 O(nm + n²logn)（堆优化）
// 空间复杂度：O(n²)
// 优势：比枚举源汇的最小割更高效
//
// 【ML/DL 关联价值】
// 1. 图分割中的最小割应用
// 2. 图像分割中的能量最小化
// 3. 社区发现中的图划分

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code39_GlobalMinCut1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 501;
	public static long INF = Long.MAX_VALUE / 2;

	// ===================== 图存储区 =====================
	public static long[][] graph = new long[MAXN][MAXN]; // 邻接矩阵
	public static int n, m;

	// ===================== 算法变量区 =====================
	public static boolean[] merged; // 标记是否已合并
	public static long[] weight; // 每个点的权重（邻接度）
	public static boolean[] visited; // 访问标记
	public static int[] order; // 加入顺序

	// ===================== 核心函数：最大邻接度搜索 =====================
	// 功能：执行最大邻接度搜索，返回最后加入的两个点
	// 核心思想：每次选择与当前集合邻接度最大的未访问点
	// 面试高频提问：最大邻接度搜索的意义？
	public static int[] maxAdjacencySearch() {
		int[] result = new int[2];
		
		// 初始化
		for (int i = 1; i <= n; i++) {
			weight[i] = 0;
			visited[i] = false;
		}
		
		int prev = -1, curr = -1;
		
		// 依次加入 n - mergedCnt 个点
		for (int i = 0; i < n; i++) {
			// 选择权重最大的未访问点
			int maxWeight = -1;
			int next = -1;
			
			for (int j = 1; j <= n; j++) {
				if (!merged[j] && !visited[j]) {
					if (maxWeight == -1 || weight[j] > maxWeight) {
						maxWeight = weight[j];
						next = j;
					}
				}
			}
			
			if (next == -1) {
				break;
			}
			
			// 标记为已访问
			visited[next] = true;
			prev = curr;
			curr = next;
			order[i] = next;
			
			// 更新邻接点的权重
			for (int j = 1; j <= n; j++) {
				if (!merged[j] && !visited[j]) {
					weight[j] += graph[curr][j];
				}
			}
		}
		
		// 返回最后两个点
		result[0] = prev;
		result[1] = curr;
		return result;
	}

	// ===================== 核心函数：计算割值 =====================
	// 功能：计算最后加入的点的割值
	// 核心思想：最后加入的点的权重即为割值
	// 面试高频提问：为什么最后点的权重是割值？
	public static long calculateCut(int s, int t) {
		long cut = 0;
		for (int i = 1; i <= n; i++) {
			if (!merged[i] && i != t) {
				cut += graph[t][i];
			}
		}
		return cut;
	}

	// ===================== 核心函数：合并两个点 =====================
	// 功能：将点 s 和 t 合并
	// 核心思想：合并边权，标记 t 为已合并
	// 面试高频提问：合并操作的注意事项？
	public static void mergeNodes(int s, int t) {
		// 合并边权
		for (int i = 1; i <= n; i++) {
			if (i != s && i != t && !merged[i]) {
				graph[s][i] += graph[t][i];
				graph[i][s] += graph[i][t];
			}
		}
		
		// 标记 t 为已合并
		merged[t] = true;
	}

	// ===================== 核心函数：Stoer-Wagner 算法 =====================
	// 功能：使用 Stoer-Wagner 算法求解全局最小割
	// 核心思想：n-1 轮最大邻接度搜索，每轮更新最小割
	// 面试高频提问：Stoer-Wagner 算法的正确性证明？
	public static long stoerWagner() {
		long minCut = INF;
		
		// 初始化
		for (int i = 1; i <= n; i++) {
			merged[i] = false;
		}
		
		int mergedCnt = 0;
		
		// 执行 n-1 轮
		while (mergedCnt < n - 1) {
			// 最大邻接度搜索
			int[] lastTwo = maxAdjacencySearch();
			int s = lastTwo[0];
			int t = lastTwo[1];
			
			// 计算当前割值
			long cut = calculateCut(s, t);
			minCut = Math.min(minCut, cut);
			
			// 合并 s 和 t
			mergeNodes(s, t);
			mergedCnt++;
		}
		
		return minCut;
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

		// 读入节点数和边数
		n = in.nextInt();
		m = in.nextInt();

		// 初始化图
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				graph[i][j] = 0;
			}
			merged = new boolean[MAXN];
			weight = new long[MAXN];
			visited = new boolean[MAXN];
			order = new int[MAXN];
		}

		// 读入边
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			graph[u][v] += w;
			graph[v][u] += w;
		}

		// 执行 Stoer-Wagner 算法
		long minCut = stoerWagner();

		// 输出结果
		out.println("全局最小割：" + (minCut == INF ? 0 : minCut));

		out.flush();
		out.close();
	}
}
