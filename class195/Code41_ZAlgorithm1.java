package class195;

// Z 算法（字符串匹配）基础模板，Java 版
// 本代码展示 Z 算法优化建图的核心模板，用于解决字符串匹配问题
// 测试链接 : https://www.luogu.com.cn/problem/P3375（改编）
// 本模板展示了 Z 算法优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== Z 算法核心知识点 =====================
// 【问题分析】
// Z 算法用于字符串匹配和周期检测
// 通过计算 Z 数组，快速找到模式串在文本串中的位置
// 主要应用于字符串匹配、周期检测、回文串等
//
// 【核心原理】
// Z 数组：z[i] 表示从 i 开始的后缀与整个串的最长公共前缀长度
// 匹配区间：维护最右的匹配区间 [l, r]
// 分类讨论：根据 i 与 r 的关系分情况计算 z[i]
// 线性扫描：从左到右依次计算每个 z[i]
//
// 【复杂度分析】
// 时间复杂度：O(n)（线性时间）
// 空间复杂度：O(n)
// 优势：相比 KMP 更直观，实现简单
//
// 【ML/DL 关联价值】
// 1. 字符串处理中的模式匹配
// 2. 文本挖掘中的重复模式检测
// 3. 生物信息学中的序列比对

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code41_ZAlgorithm1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 1000001;

	// ===================== 字符串变量区 =====================
	public static char[] s = new char[MAXN];
	public static int[] z = new int[MAXN];
	public static int n;

	// ===================== 核心函数：计算 Z 数组 =====================
	// 功能：计算字符串的 Z 数组
	// 核心思想：维护最右匹配区间 [l, r]
	// 面试高频提问：Z 算法的三种情况？
	public static void computeZArray() {
		n = s.length;
		int l = 0, r = 0;
		
		for (int i = 1; i < n; i++) {
			// 情况 1：i > r，暴力匹配
			if (i > r) {
				l = r = i;
				while (r < n && s[r - l] == s[r]) {
					r++;
				}
				z[i] = r - l;
				r--;
			} else {
				// 情况 2 和 3：i <= r，利用已计算的信息
				int k = i - l;
				
				// 情况 2：z[k] < r - i + 1，直接赋值
				if (z[k] < r - i + 1) {
					z[i] = z[k];
				} else {
					// 情况 3：z[k] >= r - i + 1，暴力扩展
					l = i;
					while (r < n && s[r - l] == s[r]) {
						r++;
					}
					z[i] = r - l;
					r--;
				}
			}
		}
	}

	// ===================== 核心函数：字符串匹配 =====================
	// 功能：使用 Z 算法进行字符串匹配
	// 核心思想：构造 pattern + '#' + text，计算 Z 数组
	// 面试高频提问：Z 算法如何用于字符串匹配？
	public static int[] stringMatch(String pattern, String text) {
		// 构造新串
		String combined = pattern + "#" + text;
		n = combined.length();
		s = combined.toCharArray();
		
		// 计算 Z 数组
		computeZArray();
		
		// 收集匹配位置
		int[] matches = new int[text.length()];
		int cnt = 0;
		int m = pattern.length();
		
		for (int i = m + 1; i < n; i++) {
			if (z[i] == m) {
				matches[cnt++] = i - m - 1; // 转换为 text 中的位置
			}
		}
		
		// 返回匹配位置数组（只返回有效部分）
		int[] result = new int[cnt];
		System.arraycopy(matches, 0, result, 0, cnt);
		return result;
	}

	// ===================== 核心函数：计算最小周期 =====================
	// 功能：计算字符串的最小周期
	// 核心思想：利用 Z 数组找到最短的重复单元
	// 面试高频提问：如何用 Z 算法求周期？
	public static int minPeriod() {
		for (int i = 1; i < n; i++) {
			if (i + z[i] == n && n % i == 0) {
				return i;
			}
		}
		return n;
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
		s = str.toCharArray();

		// 计算 Z 数组
		computeZArray();

		// 输出 Z 数组
		out.println("Z 数组：");
		for (int i = 0; i < n; i++) {
			out.print(z[i] + " ");
		}
		out.println();

		// 计算最小周期
		int period = minPeriod();
		out.println("最小周期：" + period);

		// 字符串匹配示例
		out.println("\n字符串匹配示例：");
		String pattern = in.next();
		String text = in.next();
		int[] matches = stringMatch(pattern, text);
		
		out.println("模式串 \"" + pattern + "\" 在文本串 \"" + text + "\" 中的位置：");
		for (int pos : matches) {
			out.print(pos + " ");
		}
		out.println();

		out.flush();
		out.close();
	}
}
