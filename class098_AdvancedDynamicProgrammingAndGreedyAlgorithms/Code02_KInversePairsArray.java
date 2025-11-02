package class083;

// K个逆序对数组 (K Inverse Pairs Array)
// 逆序对的定义如下：
// 对于数组nums的第i个和第j个元素
// 如果满足0<=i<j<nums.length 且 nums[i]>nums[j]，则为一个逆序对
// 给你两个整数n和k，找出所有包含从1到n的数字
// 且恰好拥有k个逆序对的不同的数组的个数
// 由于答案可能很大，答案对 1000000007 取模
// 
// 相关题目链接:
// LeetCode 493. 翻转对: https://leetcode.cn/problems/reverse-pairs/
// LeetCode 315. 计算右侧小于当前元素的个数: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
// 洛谷 P1908. 逆序对: https://www.luogu.com.cn/problem/P1908
// HDU 1394. Minimum Inversion Number: http://acm.hdu.edu.cn/showproblem.php?pid=1394
// POJ 2299. Ultra-QuickSort: http://poj.org/problem?id=2299
// SPOJ INVCNT: https://www.spoj.com/problems/INVCNT/
// CodeChef INVCNT: https://www.codechef.com/problems/INVCNT
// HackerEarth Subarray Sum: https://www.hackerearth.com/problem/algorithm/subarray-sums/
// 
// 核心算法: 动态规划
// 时间复杂度: O(nk*min(n,k)) - 方法1，O(nk) - 方法2
// 空间复杂度: O(nk)
// 工程化考量: 模运算处理、边界条件处理、空间优化
// 
// 解题思路:
// 方法1: 暴力枚举，对于每个新加入的数字i，枚举它放在哪个位置
// 方法2: 优化枚举过程，使用滑动窗口思想避免重复计算
public class Code02_KInversePairsArray {

	// 最普通的动态规划
	// 不优化枚举
	// 时间复杂度: O(nk*min(n,k))
	// 空间复杂度: O(nk)
	public static int kInversePairs1(int n, int k) {
		int mod = 1000000007;
		// dp[i][j] : 1、2、3...i这些数字，形成的排列一定要有j个逆序对，请问这样的排列有几种
		int[][] dp = new int[n + 1][k + 1];
		// 基础情况：0个数字形成0个逆序对的排列有1种
		dp[0][0] = 1;
		// 填充DP表
		for (int i = 1; i <= n; i++) {
			// 0个逆序对的情况：只有一种排列方式（升序排列）
			dp[i][0] = 1;
			// 计算j个逆序对的情况
			for (int j = 1; j <= k; j++) {
				// 枚举新加入的数字i放在哪个位置
				if (i > j) {
					// 如果i>j，新数字放在最后j+1个位置都可以
					for (int p = 0; p <= j; p++) {
						dp[i][j] = (dp[i][j] + dp[i - 1][p]) % mod;
					}
				} else {
					// i <= j
					// 如果i<=j，新数字放在最后i个位置
					for (int p = j - i + 1; p <= j; p++) {
						dp[i][j] = (dp[i][j] + dp[i - 1][p]) % mod;
					}
				}
			}
		}
		// 返回n个数字形成k个逆序对的排列数
		return dp[n][k];
	}

	// 根据观察方法1优化枚举
	// 最优解
	// 其实可以进一步空间压缩
	// 有兴趣的同学自己试试吧
	// 时间复杂度: O(nk)
	// 空间复杂度: O(nk)
	public static int kInversePairs2(int n, int k) {
		int mod = 1000000007;
		// dp[i][j] : 1、2、3...i这些数字，形成的排列一定要有j个逆序对，请问这样的排列有几种
		int[][] dp = new int[n + 1][k + 1];
		// 基础情况：0个数字形成0个逆序对的排列有1种
		dp[0][0] = 1;
		// window : 窗口的累加和，用于优化枚举过程
		for (int i = 1, window; i <= n; i++) {
			// 0个逆序对的情况：只有一种排列方式（升序排列）
			dp[i][0] = 1;
			// 初始化窗口累加和
			window = 1;
			// 计算j个逆序对的情况
			for (int j = 1; j <= k; j++) {
				if (i > j) {
					// 如果i>j，累加新状态
					window = (window + dp[i - 1][j]) % mod;
				} else {
					// i <= j
					// 滑动窗口：加入新元素，移除旧元素
					window = ((window + dp[i - 1][j]) % mod - dp[i - 1][j - i] + mod) % mod;
				}
				// 更新DP值
				dp[i][j] = window;
			}
		}
		// 返回n个数字形成k个逆序对的排列数
		return dp[n][k];
	}

}