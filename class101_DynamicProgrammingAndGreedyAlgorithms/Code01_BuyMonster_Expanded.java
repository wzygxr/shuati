package class087;

// 贿赂怪兽问题扩展实现
// 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的n只怪兽
// 如果你当前的能力小于i号怪兽的能力，则必须付出b[i]的钱贿赂这个怪兽
// 然后怪兽就会加入你，他的能力a[i]直接累加到你的能力上
// 如果你当前的能力大于等于i号怪兽的能力，你可以选择直接通过，且能力不会下降
// 但你依然可以选择贿赂这个怪兽，然后怪兽的能力直接累加到你的能力上
// 返回通过所有的怪兽，需要花的最小钱数

import java.util.*;

public class Code01_BuyMonster_Expanded {
	
	/*
	 * 类似题目1：花最少的钱通过所有的怪兽（腾讯面试题）
	 * 题目描述：
	 * 给定两个数组：
	 * int[] d，d[i]表示i号怪兽的能力值
	 * int[] p，p[i]表示贿赂i号怪兽需要的钱数
	 * 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的n只怪兽
	 * 如果你当前的能力小于i号怪兽的能力，则必须付出p[i]的钱贿赂这个怪兽
	 * 然后怪兽就会加入你，他的能力d[i]直接累加到你的能力上
	 * 如果你当前的能力大于等于i号怪兽的能力，你可以选择直接通过，且能力不会下降
	 * 但你依然可以选择贿赂这个怪兽，然后怪兽的能力直接累加到你的能力上
	 * 返回通过所有的怪兽，需要花的最小钱数
	 * 
	 * 示例：
	 * d = {5, 3, 1, 1, 1, 8}
	 * p = {2, 1, 2, 2, 2, 30}
	 * 返回：3 (只需要贿赂前两个就够了)
	 * 
	 * 解题思路：
	 * 这个问题与贿赂怪兽问题完全相同，只是变量名不同。
	 * 我们可以使用动态规划来解决。
	 * 
	 * 方法一：基于能力值的动态规划
	 * dp[i][j] 表示处理前i个怪兽，当前能力值为j时，所需的最少钱数
	 * 
	 * 方法二：基于金钱数的动态规划
	 * dp[i][j] 表示花费最多j的钱，处理前i个怪兽时能获得的最大能力值
	 */
	
	// 花最少的钱通过所有的怪兽 - 解法一：基于金钱数的动态规划
	// 时间复杂度: O(n * sum(p))，其中n是怪兽数量，sum(p)是所有贿赂费用的总和
	// 空间复杂度: O(n * sum(p))
	public static long minMoneyToPassMonsters1(int[] d, int[] p) {
		int sum = 0;
		for (int money : p) {
			sum += money;
		}
		
		// dp[i][j] 表示花费最多j的钱，处理前i个怪兽时能获得的最大能力值
		long[][] dp = new long[d.length + 1][sum + 1];
		
		// 初始化：不花钱不获得能力
		for (int j = 0; j <= sum; j++) {
			dp[0][j] = 0;
		}
		
		// 填充dp表
		for (int i = 1; i <= d.length; i++) {
			for (int j = 0; j <= sum; j++) {
				// 初始化为负无穷，表示无法达到该状态
				dp[i][j] = Long.MIN_VALUE;
				
				// 不贿赂当前怪兽（如果能力足够）
				if (dp[i-1][j] >= d[i-1]) {
					dp[i][j] = Math.max(dp[i][j], dp[i-1][j]);
				}
				
				// 贿赂当前怪兽（如果有足够钱）
				if (j >= p[i-1] && dp[i-1][j - p[i-1]] != Long.MIN_VALUE) {
					dp[i][j] = Math.max(dp[i][j], dp[i-1][j - p[i-1]] + d[i-1]);
				}
			}
		}
		
		// 找到能通过所有怪兽的最少钱数
		for (int j = 0; j <= sum; j++) {
			if (dp[d.length][j] >= 0) {  // 能力值非负表示可以通过所有怪兽
				return j;
			}
		}
		
		return sum;
	}
	
	// 花最少的钱通过所有的怪兽 - 解法二：基于能力值的动态规划
	// 时间复杂度: O(n * sum(d))，其中n是怪兽数量，sum(d)是所有怪兽能力的总和
	// 空间复杂度: O(n * sum(d))
	public static long minMoneyToPassMonsters2(int[] d, int[] p) {
		int sum = 0;
		for (int ability : d) {
			sum += ability;
		}
		
		// dp[i][j] 表示处理前i个怪兽，当前能力值为j时，所需的最少钱数
		// 使用Long.MAX_VALUE / 2避免溢出
		long[][] dp = new long[d.length + 1][sum + 1];
		
		// 初始化：所有状态初始化为无穷大
		for (int i = 0; i <= d.length; i++) {
			Arrays.fill(dp[i], Long.MAX_VALUE / 2);
		}
		// 初始状态：处理0个怪兽，能力值为0，需要0钱
		dp[0][0] = 0;
		
		// 填充dp表
		for (int i = 1; i <= d.length; i++) {
			for (int j = 0; j <= sum; j++) {
				// 不贿赂当前怪兽（如果能力足够）
				if (j >= d[i-1] && dp[i-1][j] != Long.MAX_VALUE / 2) {
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j]);
				}
				
				// 贿赂当前怪兽（如果能力值可达）
				if (j >= d[i-1] && dp[i-1][j - d[i-1]] != Long.MAX_VALUE / 2) {
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j - d[i-1]] + p[i-1]);
				}
			}
		}
		
		// 找到通过所有怪兽的最少钱数
		long result = Long.MAX_VALUE / 2;
		for (int j = 0; j <= sum; j++) {
			result = Math.min(result, dp[d.length][j]);
		}
		
		return result == Long.MAX_VALUE / 2 ? -1 : result;
	}
	
	/*
	 * 类似题目2：Bribe the Prisoners（Google Code Jam 2009, Round 1C C）
	 * 题目描述：
	 * 有连续编号为1到n的牢房，每个牢房最初住着一个犯人。
	 * 你需要释放m个犯人，给出释放犯人的编号序列。
	 * 当释放犯人k时，需要贿赂犯人k两边的犯人，直到遇见空牢房或者边界。
	 * 求最小的贿赂金币数。
	 * 
	 * 示例：
	 * n = 8, m = 1, 释放犯人3
	 * 犯人1,2需要贿赂（2个金币），犯人4,5,6,7,8需要贿赂（5个金币）
	 * 总共需要7个金币
	 * 
	 * 解题思路：
	 * 这是一个区间动态规划问题。
	 * dp[i][j] 表示释放编号在i到j之间的所有需要释放的犯人所需的最少金币数
	 * 状态转移方程：
	 * dp[i][j] = min{dp[i][k-1] + dp[k+1][j] + (a[j+1] - a[i-1] - 2)} for k in i..j
	 * 其中a数组是需要释放的犯人编号，加上哨兵a[0]=0和a[m+1]=n+1
	 */
	
	// Bribe the Prisoners 解法
	// 时间复杂度: O(m^3)，其中m是要释放的犯人数量
	// 空间复杂度: O(m^2)
	public static int bribePrisoners(int n, int[] prisoners) {
		int m = prisoners.length;
		// 添加哨兵节点，a[0]=0, a[m+1]=n+1
		int[] a = new int[m + 2];
		a[0] = 0;
		for (int i = 0; i < m; i++) {
			a[i + 1] = prisoners[i];
		}
		a[m + 1] = n + 1;
		
		// dp[i][j] 表示释放编号在a[i]到a[j]之间的所有需要释放的犯人所需的最少金币数
		int[][] dp = new int[m + 2][m + 2];
		
		// 区间DP，按区间长度从小到大计算
		// len表示区间长度
		for (int len = 2; len <= m + 1; len++) {
			// i表示区间起始位置
			for (int i = 0; i + len <= m + 1; i++) {
				// j表示区间结束位置
				int j = i + len;
				// 初始化为最大值
				dp[i][j] = Integer.MAX_VALUE;
				// 枚举最后一个释放的犯人位置k
				for (int k = i + 1; k < j; k++) {
					// 状态转移方程：
					// dp[i][k]表示释放i到k-1位置的犯人所需金币数
					// dp[k][j]表示释放k+1到j位置的犯人所需金币数
					// (a[j] - a[i] - 2)表示释放第k个犯人时需要贿赂的金币数
					dp[i][j] = Math.min(dp[i][j], 
						dp[i][k] + dp[k][j] + (a[j] - a[i] - 2));
				}
			}
		}
		
		return dp[0][m + 1];
	}
	
	/*
	 * 类似题目3：分糖果问题（LeetCode 135）
	 * 题目描述：
	 * n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
	 * 你需要按照以下要求，给这些孩子分发糖果：
	 * 每个孩子至少分配到 1 个糖果。
	 * 相邻两个孩子评分更高的孩子会获得更多的糖果。
	 * 请你给每个孩子分发糖果，计算并返回需要准备的最少糖果数目。
	 * 
	 * 示例：
	 * 输入：ratings = [1,0,2]
	 * 输出：5
	 * 解释：你可以分别给第一个、第二个、第三个孩子分发 2、1、2 颗糖果。
	 * 
	 * 解题思路：
	 * 这是一个贪心算法问题。
	 * 我们可以将「相邻的孩子中，评分高的孩子必须获得更多的糖果」这句话拆分为两个规则：
	 * 1. 从左到右遍历，如果右边评分比左边高，则右边糖果数比左边多1
	 * 2. 从右到左遍历，如果左边评分比右边高，则左边糖果数更新为比右边多1和当前值的最大值
	 */
	
	// 分糖果问题 - 贪心算法解法
	// 时间复杂度: O(n)，其中n是孩子数量
	// 空间复杂度: O(n)
	public static int candy(int[] ratings) {
		int n = ratings.length;
		// 每个孩子至少分配到1个糖果
		int[] candies = new int[n];
		Arrays.fill(candies, 1);
		
		// 从左到右遍历，如果右边评分比左边高，则右边糖果数比左边多1
		for (int i = 1; i < n; i++) {
			if (ratings[i] > ratings[i-1]) {
				candies[i] = candies[i-1] + 1;
			}
		}
		
		// 从右到左遍历，如果左边评分比右边高，则左边糖果数更新为比右边多1和当前值的最大值
		for (int i = n - 2; i >= 0; i--) {
			if (ratings[i] > ratings[i+1]) {
				candies[i] = Math.max(candies[i], candies[i+1] + 1);
			}
		}
		
		// 计算总糖果数
		int total = 0;
		for (int candy : candies) {
			total += candy;
		}
		
		return total;
	}
	
	/*
	 * 类似题目4：最低成本爬楼梯（LeetCode 746）
	 * 题目描述：
	 * 给你一个整数数组 cost ，其中 cost[i] 是从楼梯第 i 个台阶向上爬需要支付的费用。
	 * 一旦你支付此费用，即可选择向上爬一个或者两个台阶。
	 * 你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。
	 * 请你计算并返回达到楼梯顶部的最低花费。
	 * 
	 * 示例：
	 * 输入：cost = [10,15,20]
	 * 输出：15
	 * 解释：你将从下标为 1 的台阶开始。
	 * 支付 15 ，向上爬两个台阶，到达楼梯顶部。
	 * 总花费为 15 。
	 * 
	 * 解题思路：
	 * 这是一个动态规划问题。
	 * dp[i] 表示到达第i个台阶的最低花费
	 * 状态转移方程：
	 * dp[i] = min(dp[i-1], dp[i-2]) + cost[i]
	 */
	
	// 最低成本爬楼梯 - 动态规划解法
	// 时间复杂度: O(n)，其中n是台阶数量
	// 空间复杂度: O(n)
	public static int minCostClimbingStairs(int[] cost) {
		int n = cost.length;
		// dp[i] 表示到达第i个台阶的最低花费
		int[] dp = new int[n];
		dp[0] = cost[0];
		dp[1] = cost[1];
		
		// 状态转移
		for (int i = 2; i < n; i++) {
			dp[i] = Math.min(dp[i-1], dp[i-2]) + cost[i];
		}
		
		// 到达顶部可以从最后一个台阶或倒数第二个台阶上去
		return Math.min(dp[n-1], dp[n-2]);
	}
	
	// 最低成本爬楼梯 - 空间优化解法
	// 时间复杂度: O(n)，其中n是台阶数量
	// 空间复杂度: O(1)
	public static int minCostClimbingStairs2(int[] cost) {
		int n = cost.length;
		int prev = cost[0];
		int curr = cost[1];
		
		// 状态转移，只需要保存前两个状态
		for (int i = 2; i < n; i++) {
			int next = Math.min(prev, curr) + cost[i];
			prev = curr;
			curr = next;
		}
		
		// 到达顶部可以从最后一个台阶或倒数第二个台阶上去
		return Math.min(prev, curr);
	}
	
	/*
	 * 类似题目5：打家劫舍（LeetCode 198）
	 * 题目描述：
	 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
	 * 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
	 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
	 * 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，
	 * 一夜之内能够偷窃到的最高金额。
	 * 
	 * 示例：
	 * 输入：[1,2,3,1]
	 * 输出：4
	 * 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
	 * 偷窃到的最高金额 = 1 + 3 = 4 。
	 * 
	 * 解题思路：
	 * 这是一个动态规划问题。
	 * dp[i] 表示考虑前i个房屋能偷到的最大金额
	 * 状态转移方程：
	 * dp[i] = max(dp[i-1], dp[i-2] + nums[i-1])
	 * 其中nums[i-1]表示第i个房屋的金额
	 */
	
	// 打家劫舍 - 动态规划解法
	// 时间复杂度: O(n)，其中n是房屋数量
	// 空间复杂度: O(n)
	public static int rob(int[] nums) {
		int n = nums.length;
		if (n == 0) return 0;
		if (n == 1) return nums[0];
		
		// dp[i] 表示考虑前i个房屋能偷到的最大金额
		int[] dp = new int[n + 1];
		dp[1] = nums[0];
		
		// 状态转移
		for (int i = 2; i <= n; i++) {
			dp[i] = Math.max(dp[i-1], dp[i-2] + nums[i-1]);
		}
		
		return dp[n];
	}
	
	// 打家劫舍 - 空间优化解法
	// 时间复杂度: O(n)，其中n是房屋数量
	// 空间复杂度: O(1)
	public static int rob2(int[] nums) {
		int n = nums.length;
		if (n == 0) return 0;
		if (n == 1) return nums[0];
		
		int prev = 0;
		int curr = nums[0];
		
		// 状态转移，只需要保存前两个状态
		for (int i = 1; i < n; i++) {
			int next = Math.max(curr, prev + nums[i]);
			prev = curr;
			curr = next;
		}
		
		return curr;
	}
	
	/*
 * 类似题目4：石子合并问题（洛谷P1880）
 * 题目描述：
 * 在一个圆形操场的四周摆放着n堆石子，现要将石子有次序地合并成一堆。
 * 规定每次只能选相邻的2堆石子合并成新的一堆，并将新的一堆石子的数目，记为该次合并的得分。
 * 试设计一个算法，计算出将n堆石子合并成1堆的最小得分和最大得分。
 * 
 * 示例：
 * 输入：4
 *      4 4 5 9
 * 输出：43 54
 * 
 * 解题思路：
 * 这是一个区间动态规划问题。
 * 对于环形问题，通常的处理方法是将数组长度翻倍，转化为线性问题。
 * dp[i][j] 表示合并第i到第j堆石子的最小得分或最大得分。
 * 状态转移方程：
 * dp[i][j] = min/max(dp[i][k] + dp[k+1][j] + sum[i][j])，其中i <= k < j
 * sum[i][j] 表示第i到第j堆石子的总数。
 */

// 石子合并问题 - 区间动态规划解法
// 时间复杂度: O(n^3)，其中n是石子堆数
// 空间复杂度: O(n^2)
public static int[] stoneMerge(int[] stones) {
	int n = stones.length;
	// 将数组长度翻倍，处理环形问题
	int[] newStones = new int[2 * n];
	for (int i = 0; i < 2 * n; i++) {
		newStones[i] = stones[i % n];
	}
	
	// 预处理前缀和
	int[] prefixSum = new int[2 * n + 1];
	for (int i = 1; i <= 2 * n; i++) {
		prefixSum[i] = prefixSum[i - 1] + newStones[i - 1];
	}
	
	// dpMin[i][j] 表示合并第i到第j堆石子的最小得分
	int[][] dpMin = new int[2 * n + 1][2 * n + 1];
	// dpMax[i][j] 表示合并第i到第j堆石子的最大得分
	int[][] dpMax = new int[2 * n + 1][2 * n + 1];
	
	// 初始化dp数组
	for (int i = 0; i <= 2 * n; i++) {
		Arrays.fill(dpMin[i], Integer.MAX_VALUE);
		Arrays.fill(dpMax[i], Integer.MIN_VALUE);
		// 单个石子的得分是0
		dpMin[i][i] = 0;
		dpMax[i][i] = 0;
	}
	
	// 枚举区间长度
	for (int len = 2; len <= n; len++) {
		// 枚举起点
		for (int i = 1; i + len - 1 <= 2 * n; i++) {
			int j = i + len - 1;
			// 枚举分割点
			for (int k = i; k < j; k++) {
				// 计算当前区间的石子总数
				int sum = prefixSum[j] - prefixSum[i - 1];
				// 更新最小得分
				dpMin[i][j] = Math.min(dpMin[i][j], dpMin[i][k] + dpMin[k+1][j] + sum);
				// 更新最大得分
				dpMax[i][j] = Math.max(dpMax[i][j], dpMax[i][k] + dpMax[k+1][j] + sum);
			}
		}
	}
	
	// 寻找最小和最大得分
	int minScore = Integer.MAX_VALUE;
	int maxScore = Integer.MIN_VALUE;
	for (int i = 1; i <= n; i++) {
		minScore = Math.min(minScore, dpMin[i][i + n - 1]);
		maxScore = Math.max(maxScore, dpMax[i][i + n - 1]);
	}
	
	return new int[]{minScore, maxScore};
}

/*
 * 类似题目5：最长递增子序列（LeetCode 300）
 * 题目描述：
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。
 * 
 * 示例：
 * 输入：nums = [10,9,2,5,3,7,101,18]
 * 输出：4
 * 解释：最长递增子序列是 [2,3,7,101]，长度为4。
 * 
 * 解题思路：
 * 解法一：动态规划
 * dp[i] 表示以第i个元素结尾的最长递增子序列的长度
 * 状态转移方程：dp[i] = max(dp[j] + 1)，其中j < i且nums[j] < nums[i]
 * 时间复杂度：O(n^2)
 * 
 * 解法二：贪心 + 二分查找
 * 维护一个数组tails，其中tails[i]表示长度为i+1的递增子序列的末尾元素的最小值
 * 对于每个nums[i]，使用二分查找在tails数组中找到第一个大于等于nums[i]的位置，并更新
 * 时间复杂度：O(n log n)
 */

// 最长递增子序列 - 动态规划解法
// 时间复杂度: O(n^2)，其中n是数组长度
// 空间复杂度: O(n)
public static int lengthOfLIS(int[] nums) {
	int n = nums.length;
	if (n == 0) return 0;
	
	// dp[i] 表示以第i个元素结尾的最长递增子序列的长度
	int[] dp = new int[n];
	Arrays.fill(dp, 1);
	
	int maxLen = 1;
	// 状态转移
	for (int i = 1; i < n; i++) {
		for (int j = 0; j < i; j++) {
			if (nums[i] > nums[j]) {
				dp[i] = Math.max(dp[i], dp[j] + 1);
			}
		}
		maxLen = Math.max(maxLen, dp[i]);
	}
	
	return maxLen;
}

// 最长递增子序列 - 贪心 + 二分查找解法
// 时间复杂度: O(n log n)，其中n是数组长度
// 空间复杂度: O(n)
public static int lengthOfLIS2(int[] nums) {
	int n = nums.length;
	if (n == 0) return 0;
	
	// tails[i]表示长度为i+1的递增子序列的末尾元素的最小值
	int[] tails = new int[n];
	int len = 0;
	
	for (int num : nums) {
		// 二分查找第一个大于等于num的位置
		int left = 0, right = len;
		while (left < right) {
			int mid = left + (right - left) / 2;
			if (tails[mid] < num) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		// 更新tails数组
		tails[left] = num;
		// 如果更新的是最后一个位置，长度加1
		if (left == len) {
			len++;
		}
	}
	
	return len;
}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试贿赂怪兽问题
		int[] d1 = {5, 3, 1, 1, 1, 8};
		int[] p1 = {2, 1, 2, 2, 2, 30};
		System.out.println("贿赂怪兽问题解法一结果: " + minMoneyToPassMonsters1(d1, p1));
		System.out.println("贿赂怪兽问题解法二结果: " + minMoneyToPassMonsters2(d1, p1));
		
		// 测试Bribe the Prisoners问题
		int[] prisoners = {3, 6, 14};
		int result = bribePrisoners(20, prisoners);
		System.out.println("Bribe the Prisoners问题结果: " + result);
		
		// 测试分糖果问题
		int[] ratings = {1, 0, 2};
		System.out.println("分糖果问题结果: " + candy(ratings));
		
		// 测试石子合并问题
		int[] stones = {4, 5, 9, 4};
		int[] scores = mergeStones(stones);
		System.out.println("石子合并问题最小得分: " + scores[0] + ", 最大得分: " + scores[1]);
	}
}