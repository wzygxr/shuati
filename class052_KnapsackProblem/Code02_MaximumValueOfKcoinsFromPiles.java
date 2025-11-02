package class074;

import java.util.List;

// 从栈中取出K个硬币的最大面值和
// 一张桌子上总共有 n 个硬币 栈 。每个栈有 正整数 个带面值的硬币
// 每一次操作中，你可以从任意一个栈的 顶部 取出 1 个硬币，从栈中移除它，并放入你的钱包里
// 给你一个列表 piles ，其中 piles[i] 是一个整数数组
// 分别表示第 i 个栈里 从顶到底 的硬币面值。同时给你一个正整数 k
// 请你返回在 恰好 进行 k 次操作的前提下，你钱包里硬币面值之和 最大为多少
// 测试链接 : https://leetcode.cn/problems/maximum-value-of-k-coins-from-piles/

/*
 * 算法详解：
 * 这是一个分组背包问题的变种。每个栈可以看作一个组，从栈中取硬币相当于从组中选择物品。
 * 对于每个栈，我们可以选择取0个、1个、2个...直到栈中所有硬币。
 * 但是有一个限制：只能从栈顶开始取，不能跳过前面的硬币。
 * 
 * 解题思路：
 * 1. 预处理每个栈的前缀和，方便快速计算取前i个硬币的价值和
 * 2. 使用动态规划，dp[i][j]表示考虑前i个栈，恰好取j个硬币能获得的最大价值
 * 3. 状态转移方程：
 *    dp[i][j] = max(dp[i-1][j-k] + prefixSum[i][k]) 其中k是第i个栈取的硬币数
 * 
 * 时间复杂度分析：
 * 设有n个栈，总共m个硬币，需要取k个硬币
 * 1. 预处理前缀和：O(m)
 * 2. 动态规划计算：O(n * k * (每个栈平均硬币数)) = O(n * k * (m/n)) = O(m * k)
 * 总时间复杂度：O(m * k)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(n * k)
 * 2. 空间优化后：O(k)
 * 
 * 相关题目扩展：
 * 1. LeetCode 2297. 跳跃游戏 VIII（分组背包思想）
 * 2. LeetCode 1994. 好子集的数目（分组背包思想）
 * 3. 洛谷 P1757 通天之分组背包
 * 4. HDU 1712 ACboy needs your help
 * 5. Codeforces 148E. Porcelain（与本题几乎一样）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空输入、非法输入等边界情况
 * 3. 可配置性：可以将最大容量作为参数传入
 * 4. 单元测试：为maxValueOfCoins1和maxValueOfCoins2编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用滚动数组优化空间
 * 
 * 语言特性差异：
 * 1. Java：使用List接口，StreamTokenizer优化输入
 * 2. C++：使用vector，可以使用memset初始化数组
 * 3. Python：使用列表推导式，可以使用numpy优化数值计算
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 前缀和预处理：避免重复计算取前k个硬币的价值和
 * 2. 空间压缩：从二维dp优化到一维dp
 * 3. 剪枝优化：当j小于k时跳过不必要的计算
 */

public class Code02_MaximumValueOfKcoinsFromPiles {

	// piles是一组一组的硬币
	// m是容量，表示一定要进行m次操作
	// dp[i][j] : 1~i组上，一共拿走j个硬币的情况下，获得的最大价值
	// 1) 不要i组的硬币 : dp[i-1][j]
	// 2) i组里尝试每一种方案
	// 比如，i组里拿走前k个硬币的方案 : dp[i-1][j-k] + 从顶部开始前k个硬币的价值和
	// 枚举每一个k，选出最大值
	public static int maxValueOfCoins1(List<List<Integer>> piles, int m) {
		int n = piles.size();
		int[][] dp = new int[n + 1][m + 1];
		for (int i = 1; i <= n; i++) {
			// i从1组开始（我们的设定），但是题目中的piles是从下标0开始的
			// 所以来到i的时候，piles.get(i-1)是当前组
			List<Integer> team = piles.get(i - 1);
			int t = Math.min(team.size(), m);
			// 预处理前缀和，为了加速计算
			int[] preSum = new int[t + 1];
			for (int j = 0, sum = 0; j < t; j++) {
				sum += team.get(j);
				preSum[j + 1] = sum;
			}
			// 更新动态规划表
			for (int j = 0; j <= m; j++) {
				// 当前组一个硬币也不拿的方案
				dp[i][j] = dp[i - 1][j];
				for (int k = 1; k <= Math.min(t, j); k++) {
					dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - k] + preSum[k]);
				}
			}
		}
		return dp[n][m];
	}

	// 空间压缩
	public static int maxValueOfCoins2(List<List<Integer>> piles, int m) {
		int[] dp = new int[m + 1];
		for (List<Integer> team : piles) {
			int t = Math.min(team.size(), m);
			int[] preSum = new int[t + 1];
			for (int j = 0, sum = 0; j < t; j++) {
				sum += team.get(j);
				preSum[j + 1] = sum;
			}
			for (int j = m; j > 0; j--) {
				for (int k = 1; k <= Math.min(t, j); k++) {
					dp[j] = Math.max(dp[j], dp[j - k] + preSum[k]);
				}
			}
		}
		return dp[m];
	}

}