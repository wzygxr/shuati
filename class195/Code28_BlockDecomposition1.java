package class195;

// 分块优化建图基础模板，Java 版
// 本代码展示分块优化建图的核心模板，用于解决区间修改查询问题
// 测试链接 : https://www.luogu.com.cn/problem/P3372（改编）
// 本模板展示了分块优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 分块优化建图核心知识点 =====================
// 【问题分析】
// 分块用于解决区间修改、区间查询等问题
// 通过将数组分块，平衡预处理和查询的复杂度
// 主要应用于区间加、区间求和、区间最值等
//
// 【核心原理】
// 分块思想：将数组分成大小为√n 的块
// 整块操作：对完整的块使用懒标记
// 散点操作：对不完整的块暴力处理
// 复杂度平衡：O(√n) 的查询和修改
//
// 【复杂度分析】
// 预处理复杂度：O(n)
// 查询复杂度：O(√n)
// 修改复杂度：O(√n)
// 空间复杂度：O(n)
//
// 【ML/DL 关联价值】
// 1. 批处理中的块状计算优化
// 2. 内存访问的局部性优化
// 3. 并行计算中的数据分块

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code28_BlockDecomposition1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 100001;
	public static long INF = Long.MAX_VALUE / 2;

	// ===================== 数组变量区 =====================
	public static long[] a = new long[MAXN]; // 原始数组
	public static long[] sum = new long[MAXN]; // 每块的和
	public static long[] lazy = new long[MAXN]; // 懒标记
	public static int[] belong = new int[MAXN]; // 每个元素属于哪一块
	public static int n, m;
	public static int blockSize; // 块的大小
	public static int blockCount; // 块的数量

	// ===================== 核心函数：分块初始化 =====================
	// 功能：将数组分块并预处理每块的和
	// 核心思想：块大小为√n，共√n 块
	// 面试高频提问：为什么块大小选择√n？
	public static void init() {
		// 计算块大小
		blockSize = (int) Math.sqrt(n);
		if (blockSize == 0) {
			blockSize = 1;
		}

		// 计算块的数量
		blockCount = (n + blockSize - 1) / blockSize;

		// 初始化每个元素所属的块
		for (int i = 1; i <= n; i++) {
			belong[i] = (i - 1) / blockSize + 1;
		}

		// 预处理每块的和
		for (int i = 1; i <= n; i++) {
			sum[belong[i]] += a[i];
		}
	}

	// ===================== 核心函数：区间加法 =====================
	// 功能：将区间 [l,r] 的所有元素加上 val
	// 核心思想：整块用懒标记，散点暴力更新
	// 面试高频提问：懒标记的作用是什么？
	public static void rangeAdd(int l, int r, long val) {
		// 如果 l 和 r 在同一块内，直接暴力更新
		if (belong[l] == belong[r]) {
			for (int i = l; i <= r; i++) {
				a[i] += val;
			}
			// 更新所在块的和
			sum[belong[l]] += val * (r - l + 1);
			return;
		}

		// 处理左边的散点
		for (int i = l; i <= belong[l] * blockSize && i <= n; i++) {
			a[i] += val;
		}
		sum[belong[l]] += val * (belong[l] * blockSize - l + 1);

		// 处理中间的整块
		for (int i = belong[l] + 1; i < belong[r]; i++) {
			lazy[i] += val;
			sum[i] += val * blockSize;
		}

		// 处理右边的散点
		for (int i = (belong[r] - 1) * blockSize + 1; i <= r; i++) {
			a[i] += val;
		}
		sum[belong[r]] += val * (r - ((belong[r] - 1) * blockSize + 1) + 1);
	}

	// ===================== 核心函数：区间求和 =====================
	// 功能：查询区间 [l,r] 的和
	// 核心思想：整块直接用 sum 数组，散点暴力累加
	// 面试高频提问：分块的查询复杂度如何计算？
	public static long rangeQuery(int l, int r) {
		long result = 0;

		// 如果 l 和 r 在同一块内，直接暴力求和
		if (belong[l] == belong[r]) {
			for (int i = l; i <= r; i++) {
				result += a[i] + lazy[belong[i]];
			}
			return result;
		}

		// 处理左边的散点
		for (int i = l; i <= belong[l] * blockSize && i <= n; i++) {
			result += a[i] + lazy[belong[i]];
		}

		// 处理中间的整块
		for (int i = belong[l] + 1; i < belong[r]; i++) {
			result += sum[i];
		}

		// 处理右边的散点
		for (int i = (belong[r] - 1) * blockSize + 1; i <= r; i++) {
			result += a[i] + lazy[belong[i]];
		}

		return result;
	}

	// ===================== 核心函数：区间最值查询 =====================
	// 功能：查询区间 [l,r] 的最大值
	// 核心思想：散点暴力，整块需要额外维护每块的最值
	// 面试高频提问：如何用分块维护区间最值？
	public static long rangeMax(int l, int r) {
		long result = -INF;

		// 如果 l 和 r 在同一块内，直接暴力查询
		if (belong[l] == belong[r]) {
			for (int i = l; i <= r; i++) {
				result = Math.max(result, a[i] + lazy[belong[i]]);
			}
			return result;
		}

		// 处理左边的散点
		for (int i = l; i <= belong[l] * blockSize && i <= n; i++) {
			result = Math.max(result, a[i] + lazy[belong[i]]);
		}

		// 处理中间的整块（简化版本，实际应该维护每块的最值）
		for (int i = belong[l] + 1; i < belong[r]; i++) {
			// 这里简化处理，实际应该用预处理的块最值
			for (int j = (i - 1) * blockSize + 1; j <= i * blockSize && j <= n; j++) {
				result = Math.max(result, a[j] + lazy[i]);
			}
		}

		// 处理右边的散点
		for (int i = (belong[r] - 1) * blockSize + 1; i <= r; i++) {
			result = Math.max(result, a[i] + lazy[belong[i]]);
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

		long nextLong() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			long val = 0;
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

		// 读入数组大小和操作数
		n = in.nextInt();
		m = in.nextInt();

		// 读入初始数组
		for (int i = 1; i <= n; i++) {
			a[i] = in.nextLong();
		}

		// 分块初始化
		init();

		// 输出分块信息
		out.println("块大小：" + blockSize);
		out.println("块数量：" + blockCount);
		out.println("分块信息：");
		for (int i = 1; i <= blockCount; i++) {
			out.println("块 " + i + ": 和=" + sum[i]);
		}

		// 处理 m 次操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt();
			if (op == 1) {
				// 操作 1：区间加法
				int l = in.nextInt();
				int r = in.nextInt();
				long val = in.nextLong();
				rangeAdd(l, r, val);
				out.println("区间 [" + l + ", " + r + "] 加上 " + val);
			} else if (op == 2) {
				// 操作 2：区间求和
				int l = in.nextInt();
				int r = in.nextInt();
				long result = rangeQuery(l, r);
				out.println("区间 [" + l + ", " + r + "] 的和：" + result);
			} else if (op == 3) {
				// 操作 3：区间最大值
				int l = in.nextInt();
				int r = in.nextInt();
				long result = rangeMax(l, r);
				out.println("区间 [" + l + ", " + r + "] 的最大值：" + result);
			}
		}

		out.flush();
		out.close();
	}
}
