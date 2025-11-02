package class090;

/**
 * 砍竹子II - 贪心算法
 * 
 * 题目描述：
 * 现需要将一根长为正整数bamboo_len的竹子砍为若干段，每段长度均为正整数。
 * 请返回每段竹子长度的最大乘积是多少，答案需要对 1000000007 取模。
 * 
 * 解题思路：
 * 1. 根据数学分析，当每段长度尽可能接近自然常数e(约2.7)时，乘积最大
 * 2. 在整数情况下，最优解是尽可能多地切出长度为3的段
 * 3. 对于余数的处理：
 *    - 余数为0：全部切为3
 *    - 余数为1：将一个3和1组合成两个2(2*2=4 > 3*1=3)
 *    - 余数为2：直接保留2
 * 
 * 时间复杂度：O(log n) - 快速幂的时间复杂度
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * - LeetCode 14: https://leetcode.cn/problems/jian-sheng-zi-ii-lcof/
 * - 剑指Offer 14: https://leetcode.cn/problems/jian-sheng-zi-lcof/
 */
public class Code01_CuttingBamboo {

	/**
	 * 快速幂运算，用于计算大数幂次方并取模
	 * 
	 * @param x   底数
	 * @param n   指数
	 * @param mod 模数
	 * @return (x^n) % mod 的结果
	 */
	public static long power(long x, int n, int mod) {
		long ans = 1;
		while (n > 0) {
			// 如果n的最低位为1，则将当前x乘入结果
			if ((n & 1) == 1) {
				ans = (ans * x) % mod;
			}
			// x自乘，相当于指数翻倍
			x = (x * x) % mod;
			// n右移一位，相当于指数除以2
			n >>= 1;
		}
		return ans;
	}

	/**
	 * 计算将长度为n的竹子切成若干段后，各段长度的最大乘积
	 * 
	 * @param n 竹子的总长度
	 * @return 最大乘积对1000000007取模的结果
	 */
	public static int cuttingBamboo(int n) {
		// 特殊情况处理
		if (n == 2) {
			return 1; // 2只能切成1+1，乘积为1
		}
		if (n == 3) {
			return 2; // 3只能切成1+2，乘积为2
		}
		
		int mod = 1000000007;
		
		// 根据数学推导，最优策略是尽可能多地切出长度为3的段
		// n = 4  -> 2 * 2
		// n = 5  -> 3 * 2
		// n = 6  -> 3 * 3
		// n = 7  -> 3 * 2 * 2
		// n = 8  -> 3 * 3 * 2
		// n = 9  -> 3 * 3 * 3
		// n = 10 -> 3 * 3 * 2 * 2
		// n = 11 -> 3 * 3 * 3 * 2
		// n = 12 -> 3 * 3 * 3 * 3
		
		// 计算余数对应的处理方式
		int tail = n % 3 == 0 ? 1 : (n % 3 == 1 ? 4 : 2);
		
		// 计算需要多少个3
		int power = (tail == 1 ? n : (n - tail)) / 3;
		
		// 返回结果：3的power次方乘以tail，再对mod取模
		return (int) (power(3, power, mod) * tail % mod);
	}

}