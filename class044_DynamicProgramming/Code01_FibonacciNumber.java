package class066;

import java.util.Arrays;

/**
 * 斐波那契数 (Fibonacci Number)
 * 
 * 题目来源：LeetCode 509. 斐波那契数
 * 题目链接：https://leetcode.cn/problems/fibonacci-number/
 * 
 * 题目描述：
 * 斐波那契数，通常用 F(n) 表示，形成的序列称为斐波那契数列。
 * 该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。也就是：
 * F(0) = 0，F(1) = 1
 * F(n) = F(n - 1) + F(n - 2)，其中 n > 1
 * 给你 n ，请计算 F(n) 。
 * 
 * 示例 1：
 * 输入：n = 2
 * 输出：1
 * 解释：F(2) = F(1) + F(0) = 1 + 0 = 1
 * 
 * 示例 2：
 * 输入：n = 3
 * 输出：2
 * 解释：F(3) = F(2) + F(1) = 1 + 1 = 2
 * 
 * 示例 3：
 * 输入：n = 4
 * 输出：3
 * 解释：F(4) = F(3) + F(2) = 2 + 1 = 3
 * 
 * 提示：
 * 0 <= n <= 30
 * 
 * 解题思路：
 * 本题是动态规划的经典入门题目，展示了动态规划的基本思想：
 * 将大问题分解为小问题，通过保存小问题的解来避免重复计算，从而提高效率。
 * 
 * 我们提供了四种解法：
 * 1. 暴力递归：直接按照定义递归求解，但存在大量重复计算，时间复杂度为O(2^n)。
 * 2. 记忆化搜索：在暴力递归的基础上，通过缓存已计算的结果来避免重复计算，时间复杂度优化为O(n)。
 * 3. 动态规划：自底向上计算，先计算小问题的解，再逐步构建大问题的解，时间复杂度为O(n)。
 * 4. 空间优化的动态规划：在动态规划的基础上，只保存必要的状态，将空间复杂度优化到O(1)。
 * 
 * 算法复杂度分析：
 * - 暴力递归：时间复杂度 O(2^n)，空间复杂度 O(n)
 * - 记忆化搜索：时间复杂度 O(n)，空间复杂度 O(n)
 * - 动态规划：时间复杂度 O(n)，空间复杂度 O(n)
 * - 空间优化DP：时间复杂度 O(n)，空间复杂度 O(1)
 * 
 * 工程化考量：
 * 1. 边界处理：正确处理n=0和n=1的特殊情况
 * 2. 性能优化：提供多种解法，从低效到高效，展示优化过程
 * 3. 代码质量：清晰的变量命名和详细的注释说明
 * 4. 测试覆盖：包含基本测试用例和性能对比测试
 * 
 * 相关题目：
 * - LCR 126. 斐波那契数列 (剑指Offer)
 * - LintCode 366. Fibonacci
 * - AtCoder Educational DP Contest A - Frog 1
 * - 牛客网 剑指Offer 10- I. 斐波那契数列
 * - HackerRank Fibonacci Numbers
 * - CodeChef FIBQ
 * - SPOJ FIBOSUM
 * - Project Euler Problem 2
 */
public class Code01_FibonacciNumber {

	// 方法1：暴力递归解法
	// 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
	// 空间复杂度：O(n) - 递归调用栈的深度
	// 问题：存在大量重复计算，效率低下
	//
	// 算法思路：
	// 直接根据斐波那契数列的定义进行递归计算：
	// F(0) = 0
	// F(1) = 1
	// F(n) = F(n-1) + F(n-2) (n > 1)
	//
	// 举例说明：计算F(5)
	// F(5) = F(4) + F(3)
	// F(4) = F(3) + F(2)
	// F(3) = F(2) + F(1)
	// F(2) = F(1) + F(0)
	// 可以看到F(3)被计算了两次，F(2)被计算了三次，存在大量重复计算
	public static int fib1(int n) {
		return f1(n);
	}

	public static int f1(int i) {
		if (i == 0) {
			return 0;
		}
		if (i == 1) {
			return 1;
		}
		return f1(i - 1) + f1(i - 2);
	}

	// 方法2：记忆化搜索（自顶向下动态规划）
	// 时间复杂度：O(n) - 每个状态只计算一次
	// 空间复杂度：O(n) - dp数组和递归调用栈
	// 优化：通过缓存已经计算的结果避免重复计算
	//
	// 算法思路：
	// 在暴力递归的基础上，使用一个数组dp来缓存已经计算过的值
	// 当需要计算f2(i)时，先检查dp[i]是否已经计算过：
	// - 如果已经计算过，直接返回dp[i]
	// - 如果没有计算过，递归计算，并将结果保存到dp[i]中
	//
	// 举例说明：计算F(5)
	// 第一次计算F(3)时，将结果保存到dp[3]
	// 当再次需要F(3)时，直接返回dp[3]，避免重复计算
	public static int fib2(int n) {
		int[] dp = new int[n + 1];
		Arrays.fill(dp, -1);
		return f2(n, dp);
	}

	public static int f2(int i, int[] dp) {
		if (i == 0) {
			return 0;
		}
		if (i == 1) {
			return 1;
		}
		if (dp[i] != -1) {
			return dp[i];
		}
		int ans = f2(i - 1, dp) + f2(i - 2, dp);
		dp[i] = ans;
		return ans;
	}

	// 方法3：动态规划（自底向上）
	// 时间复杂度：O(n) - 从底向上计算每个状态
	// 空间复杂度：O(n) - dp数组存储所有状态
	// 优化：避免了递归调用的开销
	//
	// 算法思路：
	// 从最小的子问题开始计算，逐步构建大问题的解：
	// 1. 初始化dp[0] = 0, dp[1] = 1
	// 2. 依次计算dp[2], dp[3], ..., dp[n]
	// 3. 每个dp[i] = dp[i-1] + dp[i-2]
	//
	// 举例说明：计算F(5)
	// dp[0] = 0
	// dp[1] = 1
	// dp[2] = dp[1] + dp[0] = 1 + 0 = 1
	// dp[3] = dp[2] + dp[1] = 1 + 1 = 2
	// dp[4] = dp[3] + dp[2] = 2 + 1 = 3
	// dp[5] = dp[4] + dp[3] = 3 + 2 = 5
	public static int fib3(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		int[] dp = new int[n + 1];
		dp[1] = 1;
		for (int i = 2; i <= n; i++) {
			dp[i] = dp[i - 1] + dp[i - 2];
		}
		return dp[n];
	}

	// 方法4：空间优化的动态规划
	// 时间复杂度：O(n) - 仍然需要计算所有状态
	// 空间复杂度：O(1) - 只保存必要的前两个状态值
	// 优化：只保存必要的状态，大幅减少空间使用
	//
	// 算法思路：
	// 观察方法3可以发现，计算dp[i]时只需要dp[i-1]和dp[i-2]的值
	// 因此，我们不需要保存所有的dp值，只需要保存前两个值即可
	//
	// 举例说明：计算F(5)
	// 初始化：lastLast = 0 (F(0)), last = 1 (F(1))
	// i=2: cur = lastLast + last = 0 + 1 = 1, 更新 lastLast = 1, last = 1
	// i=3: cur = lastLast + last = 1 + 1 = 2, 更新 lastLast = 1, last = 2
	// i=4: cur = lastLast + last = 1 + 2 = 3, 更新 lastLast = 2, last = 3
	// i=5: cur = lastLast + last = 2 + 3 = 5, 更新 lastLast = 3, last = 5
	// 返回 last = 5
	public static int fib4(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		int lastLast = 0, last = 1;
		for (int i = 2, cur; i <= n; i++) {
			cur = lastLast + last;
			lastLast = last;
			last = cur;
		}
		return last;
	}

	// 测试用例和性能对比
	public static void main(String[] args) {
		System.out.println("测试斐波那契数列实现：");
		
		// 测试小数值
		int n = 10;
		System.out.println("n = " + n);
		System.out.println("方法1 (暴力递归): " + fib1(n));
		System.out.println("方法2 (记忆化搜索): " + fib2(n));
		System.out.println("方法3 (动态规划): " + fib3(n));
		System.out.println("方法4 (空间优化): " + fib4(n));
		
		// 性能测试（只测试高效方法）
		n = 30;
		long start, end;
		
		start = System.currentTimeMillis();
		int result3 = fib3(n);
		end = System.currentTimeMillis();
		System.out.println("\n动态规划方法计算 fib(" + n + ") = " + result3 + "，耗时: " + (end - start) + "ms");
		
		start = System.currentTimeMillis();
		int result4 = fib4(n);
		end = System.currentTimeMillis();
		System.out.println("空间优化方法计算 fib(" + n + ") = " + result4 + "，耗时: " + (end - start) + "ms");
	}

}