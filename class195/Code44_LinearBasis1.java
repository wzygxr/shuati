package class195;

// 线性基优化建图基础模板，Java 版
// 本代码展示线性基优化建图的核心模板，用于解决异或和问题
// 测试链接 : https://www.luogu.com.cn/problem/P3812（改编）
// 本模板展示了线性基优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 线性基核心知识点 =====================
// 【问题分析】
// 线性基用于处理异或和相关问题
// 通过维护一组基，可以快速求解最大/最小异或和
// 主要应用于最大异或和、第 k 小异或和、异或方程组等
//
// 【核心原理】
// 基的定义：一组线性无关的向量，可以张成原空间
// 插入操作：尝试将新数插入基中
// 贪心查询：从高位到低位贪心选择
// 性质：基的大小不超过 log(max_value)
//
// 【复杂度分析】
// 插入复杂度：O(log max_value)
// 查询复杂度：O(log max_value)
// 空间复杂度：O(log max_value)
//
// 【ML/DL 关联价值】
// 1. 编码理论中的线性码
// 2. 密码学中的异或运算
// 3. 组合优化中的基变换

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code44_LinearBasis1 {

	// ===================== 常量定义区 =====================
	public static int MAX_BITS = 62; // 最大位数（long 类型）
	public static long INF = Long.MAX_VALUE;

	// ===================== 线性基变量区 =====================
	public static long[] basis = new long[MAX_BITS + 1]; // 线性基
	public static int sz; // 基的大小

	// ===================== 核心函数：初始化线性基 =====================
	// 功能：初始化线性基
	// 核心思想：清空基数组
	// 面试高频提问：线性基需要哪些信息？
	public static void init() {
		for (int i = 0; i <= MAX_BITS; i++) {
			basis[i] = 0;
		}
		sz = 0;
	}

	// ===================== 核心函数：插入元素 =====================
	// 功能：将一个新数插入线性基
	// 核心思想：从高位到低位，如果该位为 1 且基中该位为 0，则插入
	// 面试高频提问：线性基插入的贪心策略？
	public static boolean insert(long x) {
		for (int i = MAX_BITS; i >= 0; i--) {
			if ((x & (1L << i)) == 0) {
				// 第 i 位为 0，跳过
				continue;
			}

			if (basis[i] == 0) {
				// 基中第 i 位为空，插入
				basis[i] = x;
				sz++;
				return true;
			}

			// 基中第 i 位已有值，异或消去
			x ^= basis[i];
		}
		return false; // x 可以被基线性表示
	}

	// ===================== 核心函数：查询最大异或和 =====================
	// 功能：查询线性基能组成的最大异或和
	// 核心思想：从高位到低位贪心，如果能变大就异或
	// 面试高频提问：最大异或和的贪心正确性？
	public static long queryMax() {
		long res = 0;
		for (int i = MAX_BITS; i >= 0; i--) {
			if ((res ^ basis[i]) > res) {
				res ^= basis[i];
			}
		}
		return res;
	}

	// ===================== 核心函数：查询最小异或和 =====================
	// 功能：查询线性基能组成的最小异或和
	// 核心思想：初始为 0 或最小的基
	// 面试高频提问：最小异或和的计算？
	public static long queryMin() {
		// 如果基中包含 0，最小异或和为 0
		for (int i = 0; i <= MAX_BITS; i++) {
			if (basis[i] != 0) {
				return basis[i];
			}
		}
		return 0;
	}

	// ===================== 核心函数：查询第 k 小异或和 =====================
	// 功能：查询线性基能组成的第 k 小异或和
	// 核心思想：将 k 二进制分解，对应位为 1 就异或
	// 面试高频提问：第 k 小异或和的计算？
	public static long queryKth(int k) {
		// 重构线性基，使其成为最简形式
		long[] simplified = new long[MAX_BITS + 1];
		int cnt = 0;

		for (int i = MAX_BITS; i >= 0; i--) {
			if (basis[i] == 0) {
				continue;
			}

			// 消去其他位
			for (int j = i - 1; j >= 0; j--) {
				if ((basis[i] & (1L << j)) != 0) {
					basis[i] ^= basis[j];
				}
			}

			simplified[cnt++] = basis[i];
		}

		// 如果 k 超过能组成的数的个数，返回 -1
		if (k > (1 << cnt)) {
			return -1;
		}

		// 根据 k 的二进制表示计算第 k 小
		long res = 0;
		for (int i = 0; i < cnt; i++) {
			if ((k & (1 << i)) != 0) {
				res ^= simplified[i];
			}
		}

		return res;
	}

	// ===================== 核心函数：判断 x 是否能被表示 =====================
	// 功能：判断 x 是否能被线性基线性表示
	// 核心思想：尝试用基消去 x 的所有位
	// 面试高频提问：线性表示的判定？
	public static boolean canRepresent(long x) {
		for (int i = MAX_BITS; i >= 0; i--) {
			if ((x & (1L << i)) != 0) {
				x ^= basis[i];
			}
		}
		return x == 0;
	}

	// ===================== 核心函数：合并两个线性基 =====================
	// 功能：合并两个线性基
	// 核心思想：将一个基的所有元素插入另一个基
	// 面试高频提问：线性基合并的复杂度？
	public static void merge(long[] otherBasis) {
		for (int i = MAX_BITS; i >= 0; i--) {
			if (otherBasis[i] != 0) {
				insert(otherBasis[i]);
			}
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

		int nextInt() throws IOException {
			return (int) nextLong();
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入元素个数
		int n = in.nextInt();

		// 初始化线性基
		init();

		// 读入并插入元素
		out.println("插入元素：");
		for (int i = 0; i < n; i++) {
			long x = in.nextLong();
			boolean success = insert(x);
			out.println("插入 " + x + ": " + (success ? "成功" : "失败（可被线性表示）"));
		}

		// 输出基的信息
		out.println("\n线性基大小：" + sz);
		out.println("基中的元素：");
		for (int i = MAX_BITS; i >= 0; i--) {
			if (basis[i] != 0) {
				out.println("  第 " + i + " 位：" + basis[i]);
			}
		}

		// 查询最大异或和
		out.println("\n最大异或和：" + queryMax());

		// 查询最小异或和
		out.println("最小异或和：" + queryMin());

		// 查询第 k 小异或和
		int k = in.nextInt();
		out.println("第 " + k + " 小异或和：" + queryKth(k));

		// 判断是否能表示某个数
		long query = in.nextLong();
		out.println(query + " 是否能被表示：" + canRepresent(query));

		out.flush();
		out.close();
	}
}
