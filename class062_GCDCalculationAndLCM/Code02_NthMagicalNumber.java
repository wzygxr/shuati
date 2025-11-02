package class041;

/**
 * 第N个神奇数字
 * 
 * 问题描述:
 * 一个正整数如果能被 a 或 b 整除，那么它是神奇的。
 * 给定三个整数 n , a , b ，返回第 n 个神奇的数字。
 * 因为答案可能很大，所以返回答案 对 1000000007 取模
 * 
 * 解题思路:
 * 使用二分查找 + 容斥原理
 * 在区间[1, m]中，神奇数字的个数为: m/a + m/b - m/lcm(a,b)
 * 其中m/a表示能被a整除的数的个数，m/b表示能被b整除的数的个数
 * m/lcm(a,b)表示同时能被a和b整除的数的个数(需要去重)
 * 
 * 相关题目:
 * 1. LeetCode 878. 第N个神奇数字
 *    题目链接: https://leetcode.cn/problems/nth-magical-number/
 *    题目描述: 一个正整数如果能被 a 或 b 整除，那么它是神奇的。给定三个整数 n , a , b ，返回第 n 个神奇的数字。
 *    
 * 2. LeetCode 1201. 丑数 III
 *    题目链接: https://leetcode.cn/problems/ugly-number-iii/
 *    题目描述: 编写一个程序，找出第n个丑数，丑数是可以被a或b或c整除的正整数。
 *    
 * 3. Codeforces 808D. Array Division
 *    题目链接: https://codeforces.com/problemset/problem/808/D
 *    题目描述: 给定一个数组，将其分为两部分，使得两部分的和相等。
 */
public class Code02_NthMagicalNumber {

	/**
	 * 计算第n个神奇数字
	 * 
	 * 算法思路:
	 * 使用二分查找确定第n个神奇数字
	 * 在区间[1, m]中，神奇数字的个数为: m/a + m/b - m/lcm(a,b)
	 * 利用容斥原理避免重复计算同时被a和b整除的数
	 * 
	 * 时间复杂度: O(log(n * min(a,b)))
	 * 空间复杂度: O(1)
	 * 
	 * @param n 第n个神奇数字
	 * @param a 能被a整除的数是神奇的
	 * @param b 能被b整除的数是神奇的
	 * @return 第n个神奇数字对1000000007取模的结果
	 */
	public static int nthMagicalNumber(int n, int a, int b) {
		long lcm = lcm(a, b);
		long ans = 0;
		// 二分查找的范围: [0, n * min(a, b)]
		// 最坏情况下，第n个神奇数字不会超过n * min(a, b)
		for (long l = 0, r = (long) n * Math.min(a, b), m = 0; l <= r;) {
			m = (l + r) / 2;
			// 计算在[1, m]区间内神奇数字的个数
			// m/a: 能被a整除的数的个数
			// m/b: 能被b整除的数的个数
			// m/lcm: 同时能被a和b整除的数的个数(需要去重)
			if (m / a + m / b - m / lcm >= n) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return (int) (ans % 1000000007);
	}

	/**
	 * 计算两个数的最大公约数
	 * 使用欧几里得算法(辗转相除法)
	 * 
	 * 时间复杂度: O(log(min(a,b)))
	 * 空间复杂度: O(log(min(a,b)))
	 */
	public static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	/**
	 * 计算两个数的最小公倍数
	 * 利用数学关系 lcm(a,b) = |a*b| / gcd(a,b)
	 * 
	 * 时间复杂度: O(log(min(a,b)))
	 * 空间复杂度: O(log(min(a,b)))
	 */
	public static long lcm(long a, long b) {
		return (long) a / gcd(a, b) * b;
	}

}
