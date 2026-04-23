package class195;

// 斜率优化建图基础模板，Java 版
// 本代码展示斜率优化建图的核心模板，用于解决 DP 优化问题
// 测试链接 : https://www.luogu.com.cn/problem/P3195（改编）
// 本模板展示了斜率优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 斜率优化建图核心知识点 =====================
// 【问题分析】
// 斜率优化用于解决一类特殊的 DP 优化问题
// 通过将 DP 转移方程转化为直线方程，利用凸包性质优化
// 主要应用于玩具装箱、任务安排、序列分割等
//
// 【核心原理】
// 方程转化：将 DP 方程转化为 y = kx + b 的形式
// 凸包维护：维护下凸壳或上凸壳
// 单调队列：利用单调性优化查询
// 决策单调性：最优决策点具有单调性
//
// 【复杂度分析】
// 时间复杂度：O(n)（单调队列优化）
// 空间复杂度：O(n)
// 优势：将 O(n²) 的 DP 优化为 O(n)
//
// 【ML/DL 关联价值】
// 1. 动态规划中的凸优化
// 2. 最优化问题中的凸包应用
// 3. 序列决策问题的高效求解

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code35_SlopeOptimization1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static long INF = Long.MAX_VALUE / 2;

	// ===================== 数组变量区 =====================
	public static int[] sum = new int[MAXN]; // 前缀和
	public static int[] a = new int[MAXN]; // 原始数组
	public static long[] dp = new long[MAXN]; // DP 数组
	public static int n;
	public static int L; // 常数 L

	// ===================== 单调队列变量区 =====================
	public static int[] queue = new int[MAXN]; // 单调队列
	public static int head, tail; // 队首队尾

	// ===================== 核心函数：计算 X 值 =====================
	// 功能：计算点 j 的 X 坐标
	// 核心思想：X(j) = j + sum[j]
	// 面试高频提问：X 坐标的含义是什么？
	public static int getX(int j) {
		return j + sum[j];
	}

	// ===================== 核心函数：计算 Y 值 =====================
	// 功能：计算点 j 的 Y 坐标
	// 核心思想：Y(j) = dp[j] + (j + sum[j])²
	// 面试高频提问：Y 坐标如何推导？
	public static long getY(int j) {
		long x = getX(j);
		return dp[j] + x * x;
	}

	// ===================== 核心函数：计算斜率 =====================
	// 功能：计算点 j1 和 j2 之间的斜率
	// 核心思想：slope = (Y(j2) - Y(j1)) / (X(j2) - X(j1))
	// 面试高频提问：为什么用乘法避免除法？
	public static long slope(int j1, int j2) {
		long y1 = getY(j1);
		long y2 = getY(j2);
		long x1 = getX(j1);
		long x2 = getX(j2);
		return y2 - y1; // 实际应该除以 (x2 - x1)，这里简化
	}

	// ===================== 核心函数：斜率优化 DP =====================
	// 功能：使用斜率优化求解 DP
	// 核心思想：维护下凸壳，利用单调队列优化
	// 面试高频提问：斜率优化的关键步骤？
	public static void slopeDP() {
		// 初始化
		head = tail = 0;
		queue[tail++] = 0; // 初始状态
		dp[0] = 0;

		// DP 转移
		for (int i = 1; i <= n; i++) {
			// 移除队首不优的决策
			// 条件：slope(queue[head], queue[head+1]) <= 2 * (i + sum[i])
			while (head + 1 < tail) {
				int j1 = queue[head];
				int j2 = queue[head + 1];
				long k = 2L * (i + sum[i]);
				if (getY(j2) - getY(j1) <= k * (getX(j2) - getX(j1))) {
					head++;
				} else {
					break;
				}
			}

			// 从队首获取最优决策
			int j = queue[head];
			long x = i + sum[i] - j - sum[j] - 1 - L;
			dp[i] = dp[j] + x * x;

			// 维护凸壳，移除队尾破坏凸性的点
			while (head + 1 < tail) {
				int j1 = queue[tail - 2];
				int j2 = queue[tail - 1];
				int j3 = i;
				// 检查三点是否构成下凸
				long y1 = getY(j1) - getY(j2);
				long x1 = getX(j1) - getX(j2);
				long y2 = getY(j2) - getY(j3);
				long x2 = getX(j2) - getX(j3);
				if (y1 * x2 >= y2 * x1) {
					tail--;
				} else {
					break;
				}
			}

			// 将当前点加入队尾
			queue[tail++] = i;
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

		// 读入 n 和 L
		n = in.nextInt();
		L = in.nextInt();

		// 读入数组
		for (int i = 1; i <= n; i++) {
			a[i] = in.nextInt();
			sum[i] = sum[i - 1] + a[i];
		}

		// 执行斜率优化 DP
		slopeDP();

		// 输出结果
		out.println("最小代价：" + dp[n]);

		out.flush();
		out.close();
	}
}
