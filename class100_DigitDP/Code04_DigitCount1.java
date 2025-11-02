package class085;

/**
 * 范围内的数字计数问题
 * 
 * 题目描述：
 * 给定两个正整数a和b，求在[a,b]范围上的所有整数中，
 * 某个数码d出现了多少次。
 * 
 * 解题思路：
 * 使用数位统计方法解决该问题。
 * 通过逐位分析每一位上数码d出现的次数来计算总数。
 * 
 * 算法分析：
 * 时间复杂度：O(log n) 其中n是输入数字
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
 * - LeetCode 1067: https://leetcode.cn/problems/digit-count-in-range/
 * - AcWing 338: https://www.acwing.com/problem/content/340/
 * 
 * 多语言实现：
 * - Java: Code04_DigitCount1.java
 * - Python: 暂无
 * - C++: 暂无
 */

public class Code04_DigitCount1 {

	/**
	 * 计算区间[a,b]内数码d出现的次数
	 * 
	 * @param d 数码
	 * @param a 区间下界
	 * @param b 区间上界
	 * @return 区间内数码d出现的次数
	 */
	public static int digitsCount(int d, int a, int b) {
		return count(b, d) - count(a - 1, d);
	}

	/**
	 * 统计1~num范围上所有的数中，数码d出现了多少次
	 * 注意是1~num范围，不是0~num范围
	 * 
	 * @param num 上界
	 * @param d 数码
	 * @return 1~num范围内数码d出现的次数
	 */
	public static int count(int num, int d) {
		int ans = 0;
		// left : 当前位左边的情况数
		// right : 当前位右边的情况数
		// 当前位的数字是cur
		for (int right = 1, tmp = num, left, cur; tmp != 0; right *= 10, tmp /= 10) {
			// 情况1：
			// d != 0
			// 1 ~ 30583 , d = 5
			// cur < d的情况
			// 个位cur=3 : 0000~3057 5
			// 个位上没有额外加
			//
			// cur > d的情况
			// 十位cur=8 : 000~304 5 0~9
			// 十位上额外加 : 305 5 0~9
			//
			// cur == d的情况
			// 百位cur=5 : 00~29 5 00~99
			// 百位上额外加 : 30 5 00~83
			// ...
			// 情况2：
			// d == 0
			// 1 ~ 30583 d = 0
			// cur > d的情况
			// 个位cur=3 : 0001~3057 0
			// 个位上额外加 : 3058 0
			//
			// cur > d的情况
			// 十位cur=8 : 001~304 0 0~9
			// 十位上额外加 : 305 0 0~9
			//
			// cur > d的情况
			// 百位cur=5 : 01~29 0 00~99
			// 百位上额外加 : 30 0 00~99
			//
			// cur == d的情况
			// 千位cur=0 : 1~2 0 000~099
			// 千位上额外加 : 3 0 000~583
			left = tmp / 10;
			cur = tmp % 10;
			if (d == 0) {
				left--;
			}
			ans += left * right;
			if (cur > d) {
				ans += right;
			} else if (cur == d) {
				ans += num % right + 1;
			}
		}
		return ans;
	}

}