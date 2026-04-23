package class195;

// Lyndon 分解优化建图基础模板，Java 版
// 本代码展示 Lyndon 分解优化建图的核心模板，用于解决字符串周期问题
// 测试链接 : https://www.luogu.com.cn/problem/P6114（改编）
// 本模板展示了 Lyndon 分解优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== Lyndon 分解优化建图核心知识点 =====================
// 【问题分析】
// Lyndon 分解用于将字符串分解为 Lyndon 单词的序列
// Lyndon 单词：字典序小于其所有真后缀的字符串
// 主要应用于字符串周期、最小表示法、后缀数组等
//
// 【核心原理】
// Lyndon 单词：s < 所有 s 的真后缀
// 分解定理：任意字符串可唯一分解为 Lyndon 单词的降序序列
// Duval 算法：O(n) 时间求解 Lyndon 分解
// 贪心策略：每次选择最长的 Lyndon 前缀
//
// 【复杂度分析】
// 时间复杂度：O(n)（Duval 算法）
// 空间复杂度：O(n)
// 优势：线性时间求解，实现简单
//
// 【ML/DL 关联价值】
// 1. 字符串处理中的周期检测
// 2. 文本压缩中的重复模式识别
// 3. 生物信息学中的序列分析

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code37_LyndonDecomposition1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 1000001;

	// ===================== 字符串变量区 =====================
	public static char[] s = new char[MAXN];
	public static int n;

	// ===================== 分解结果变量区 =====================
	public static int[] start = new int[MAXN]; // 每个 Lyndon 单词的起始位置
	public static int[] end = new int[MAXN]; // 每个 Lyndon 单词的结束位置
	public static int cnt; // Lyndon 单词的数量

	// ===================== 核心函数：比较字符 =====================
	// 功能：比较两个位置的字符大小
	// 核心思想：直接比较字符的 ASCII 值
	// 面试高频提问：字符串比较的注意事项？
	public static int compare(int i, int j) {
		return Character.compare(s[i], s[j]);
	}

	// ===================== 核心函数：Duval 算法求解 Lyndon 分解 =====================
	// 功能：使用 Duval 算法求解字符串的 Lyndon 分解
	// 核心思想：贪心选择最长的 Lyndon 前缀
	// 面试高频提问：Duval 算法的正确性证明？
	public static void duval() {
		cnt = 0;
		int i = 0;

		while (i < n) {
			// 寻找以 i 开头的最长 Lyndon 单词
			int j = i + 1;
			int k = i;

			// 扩展 Lyndon 单词
			while (j < n && compare(k, j) <= 0) {
				if (compare(k, j) < 0) {
					// s[k] < s[j]，重置 k
					k = i;
				} else {
					// s[k] == s[j]，继续比较
					k++;
				}
				j++;
			}

			// 输出 Lyndon 单词
			while (i <= k) {
				start[++cnt] = i;
				end[cnt] = i + (j - k - 1);
				i += (j - k);
			}
		}
	}

	// ===================== 核心函数：验证 Lyndon 单词 =====================
	// 功能：验证 s[l..r] 是否为 Lyndon 单词
	// 核心思想：检查是否小于所有真后缀
	// 面试高频提问：Lyndon 单词的性质？
	public static boolean isLyndon(int l, int r) {
		// 暴力验证：检查是否小于所有真后缀
		for (int i = l + 1; i <= r; i++) {
			// 比较 s[l..r] 和 s[i..r]
			for (int j = l, k = i; j <= r && k <= r; j++, k++) {
				if (s[j] < s[k]) {
					break;
				} else if (s[j] > s[k]) {
					return false;
				}
				// 如果 s[j] == s[k]，继续比较
			}
		}
		return true;
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

		String next() throws IOException {
			StringBuilder sb = new StringBuilder();
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			while (c > ' ' && c != -1) {
				sb.append((char) c);
				c = readByte();
			}
			return sb.toString();
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入字符串
		String str = in.next();
		n = str.length();
		for (int i = 0; i < n; i++) {
			s[i] = str.charAt(i);
		}

		// 执行 Lyndon 分解
		duval();

		// 输出分解结果
		out.println("Lyndon 分解结果：");
		out.println("Lyndon 单词数量：" + cnt);
		for (int i = 1; i <= cnt; i++) {
			out.print("单词 " + i + ": ");
			for (int j = start[i]; j <= end[i]; j++) {
				out.print(s[j]);
			}
			out.println();
		}

		// 验证分解
		out.println("\n验证分解：");
		for (int i = 1; i <= cnt; i++) {
			boolean valid = isLyndon(start[i], end[i]);
			out.println("单词 " + i + " 是 Lyndon 单词：" + valid);
		}

		out.flush();
		out.close();
	}
}
