package class085;

/**
 * 数字1的个数问题
 * 
 * 题目描述：
 * 给定一个整数n，计算所有小于等于n的非负整数中数字1出现的个数。
 * 
 * 解题思路：
 * 使用数位统计方法解决该问题。
 * 通过逐位分析每一位上数字1出现的次数来计算总数。
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
 * - LeetCode 233: https://leetcode.cn/problems/number-of-digit-one/
 * - 剑指Offer 43: https://leetcode.cn/problems/1nzheng-shu-zhong-1chu-xian-de-ci-shu-lcof/
 * 
 * 多语言实现：
 * - Java: Code04_DigitCount3.java
 * - Python: 暂无
 * - C++: 暂无
 */

public class Code04_DigitCount3 {

	/**
	 * 计算所有小于等于n的非负整数中数字1出现的个数
	 * 
	 * @param n 输入整数
	 * @return 数字1出现的总次数
	 */
	public static int countDigitOne(int n) {
		return count(n, 1);
	}

	/**
	 * 统计所有小于等于num的非负整数中数字d出现的个数
	 * 
	 * @param num 上界
	 * @param d 目标数字
	 * @return 数字d出现的总次数
	 */
	public static int count(int num, int d) {
		int ans = 0;
		for (int right = 1, tmp = num, left, cur; tmp != 0; right *= 10, tmp /= 10) {
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