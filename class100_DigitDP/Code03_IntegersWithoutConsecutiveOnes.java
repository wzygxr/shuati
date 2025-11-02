package class085;

/**
 * 不含连续1的非负整数问题
 * 
 * 题目描述：
 * 给定一个正整数n，请你统计在[0, n]范围的非负整数中，
 * 有多少个整数的二进制表示中不存在连续的1。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。
 * 状态定义：cnt[len] 表示二进制如果有len位，所有二进制状态中不存在连续的1的状态有多少个。
 * 
 * 算法分析：
 * 时间复杂度：O(log n) 其中n是输入数字
 * 空间复杂度：O(log n) 用于存储辅助数组
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入边界情况
 * 2. 边界测试：测试各种边界情况
 * 3. 性能优化：使用预处理和记忆化搜索避免重复计算
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - LeetCode 600: https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
 * - AcWing 1083: https://www.acwing.com/problem/content/1085/
 * 
 * 多语言实现：
 * - Java: Code03_IntegersWithoutConsecutiveOnes.java
 * - Python: 暂无
 * - C++: 暂无
 */

public class Code03_IntegersWithoutConsecutiveOnes {

	/**
	 * 方法1：递归实现
	 * 
	 * @param n 输入正整数
	 * @return [0, n]范围内不含连续1的非负整数个数
	 */
	public static int findIntegers1(int n) {
		int[] cnt = new int[31];
		cnt[0] = 1;
		cnt[1] = 2;
		for (int len = 2; len <= 30; len++) {
			cnt[len] = cnt[len - 1] + cnt[len - 2];
		}
		return f(cnt, n, 30);
	}

	/**
	 * 递归函数
	 * 
	 * @param cnt 辅助数组，cnt[len]表示二进制如果有len位，所有二进制状态中不存在连续的1的状态有多少个
	 * @param num 输入数字
	 * @param i 当前处理到第i位
	 * @return <=num且不存在连续的1的状态有多少个
	 */
	public static int f(int[] cnt, int num, int i) {
		if (i == -1) {
			return 1; // num自身合法
		}
		int ans = 0;
		if ((num & (1 << i)) != 0) {
			ans += cnt[i];
			if ((num & (1 << (i + 1))) != 0) {
				// 如果num二进制状态，前一位是1，当前位也是1
				// 如果前缀保持和num一样，后续一定不合法了
				// 所以提前返回
				return ans;
			}
		}
		// 之前的高位和num一样，且合法，继续去i-1位递归
		ans += f(cnt, num, i - 1);
		return ans;
	}

	/**
	 * 方法2：迭代实现
	 * 只是把方法1从递归改成迭代而已，完全是等义改写，没有新东西
	 * 
	 * @param n 输入正整数
	 * @return [0, n]范围内不含连续1的非负整数个数
	 */
	public static int findIntegers2(int n) {
		int[] cnt = new int[31];
		cnt[0] = 1;
		cnt[1] = 2;
		for (int len = 2; len <= 30; len++) {
			cnt[len] = cnt[len - 1] + cnt[len - 2];
		}
		int ans = 0;
		for (int i = 30; i >= -1; i--) {
			if (i == -1) {
				ans++;
				break;
			}
			if ((n & (1 << i)) != 0) {
				ans += cnt[i];
				if ((n & (1 << (i + 1))) != 0) {
					break;
				}
			}
		}
		return ans;
	}

}