package class085;

/**
 * 范围内的数字计数问题（统计所有数码出现次数）
 * 
 * 题目描述：
 * 给定两个正整数a和b，求在[a,b]范围上的所有整数中，
 * 每个数码(digit)各出现了多少次。
 * 
 * 解题思路：
 * 使用数位统计方法解决该问题。
 * 通过逐位分析每一位上各个数码出现的次数来计算总数。
 * 
 * 算法分析：
 * 时间复杂度：O(10 * log n) 其中n是输入数字
 * 空间复杂度：O(1)
 * 
 * 最优解分析：
 * 这是数位统计的标准解法，对于此类计数问题是最优解。
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入边界情况
 * 2. 边界测试：测试各种边界情况
 * 3. 性能优化：使用数学方法直接计算避免逐个枚举
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - 洛谷P2602: https://www.luogu.com.cn/problem/P2602
 * - ZOJ 3962: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827365001
 * 
 * 多语言实现：
 * - Java: Code04_DigitCount2.java
 * - Python: 暂无
 * - C++: 暂无
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_DigitCount2 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			long a = (long) in.nval;
			in.nextToken();
			long b = (long) in.nval;
			for (int i = 0; i < 9; i++) {
				out.print(digitsCount(i, a, b) + " ");
			}
			out.println(digitsCount(9, a, b));
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算区间[a,b]内数码d出现的次数
	 * 
	 * @param d 数码
	 * @param a 区间下界
	 * @param b 区间上界
	 * @return 区间内数码d出现的次数
	 */
	public static long digitsCount(int d, long a, long b) {
		return count(b, d) - count(a - 1, d);
	}

	/**
	 * 统计1~num范围上所有的数中，数码d出现了多少次
	 * 
	 * @param num 上界
	 * @param d 数码
	 * @return 1~num范围内数码d出现的次数
	 */
	public static long count(long num, int d) {
		long ans = 0;
		for (long right = 1, tmp = num, left, cur; tmp != 0; right *= 10, tmp /= 10) {
			left = tmp / 10;
			if (d == 0) {
				left--;
			}
			ans += left * right;
			cur = tmp % 10;
			if (cur > d) {
				ans += right;
			} else if (cur == d) {
				ans += num % right + 1;
			}
		}
		return ans;
	}

}