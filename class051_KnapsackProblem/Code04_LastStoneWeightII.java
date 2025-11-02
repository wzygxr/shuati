package class073;

/**
 * 最后一块石头的重量 II
 * 
 * 问题描述：
 * 有一堆石头，用整数数组 stones 表示，其中 stones[i] 表示第 i 块石头的重量。
 * 每一回合，从中选出任意两块石头，然后将它们一起粉碎。
 * 假设石头的重量分别为 x 和 y，且 x <= y，粉碎结果：
 * - 如果 x == y，那么两块石头都会被完全粉碎；
 * - 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
 * 最后，最多只会剩下一块石头，返回此石头最小的可能重量。如果没有石头剩下，就返回 0。
 * 
 * 解题思路：
 * 这是一个可以转化为01背包问题的变种。我们的目标是将石头分成两堆，使得它们的重量尽可能接近，
 * 这样最后剩下的石头重量就会最小（等于两堆重量之差）。
 * 
 * 具体分析：
 * 1. 如果我们能将石头分成两堆，重量分别为sum1和sum2，那么最终剩下的石头重量为|sum1 - sum2|
 * 2. 由于sum1 + sum2 = totalSum（石头总重量），所以剩下的重量为|totalSum - 2*sum1|
 * 3. 为了最小化这个值，我们需要让sum1尽可能接近totalSum/2
 * 4. 这转化为：在石头中选择一些，使得它们的总重量不超过totalSum/2，且尽可能接近totalSum/2
 * 5. 这正是一个01背包问题，背包容量为totalSum/2，物品重量为石头重量，目标是最大化能装入的重量
 * 
 * 时间复杂度：O(n * sum)，其中n是石头数量，sum是总重量
 * 空间复杂度：O(sum)，使用一维DP数组
 * 
 * 测试链接 : https://leetcode.cn/problems/last-stone-weight-ii/
 */
public class Code04_LastStoneWeightII {

	/**
	 * 计算最后一块石头的最小可能重量
	 * 
	 * 解题思路：
	 * 1. 这道题可以转化为将石头分为两堆，使得两堆重量差最小
	 * 2. 假设两堆分别为 A 和 B，A >= B
	 * 3. 最终剩下的石头重量就是 A - B
	 * 4. 要使 A - B 最小，就要使 B 尽可能接近 sum/2
	 * 5. 问题转化为：在不超过 sum/2 的前提下，背包最多能装多少重量的石头
	 * 6. 这就是一个标准的01背包问题
	 * 
	 * @param nums 石头重量数组
	 * @return 最后一块石头的最小可能重量
	 */
	public static int lastStoneWeightII(int[] nums) {
		// 参数验证
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		// 计算所有石头的总重量
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// nums中随意选择数字
		// 累加和一定要 <= sum / 2
		// 又尽量接近
		int near = near(nums, sum / 2);
		// 返回两堆石头重量差的最小值
		// 其中一堆重量为near，另一堆重量为sum-near
		// 重量差为 (sum-near) - near = sum - 2*near
		return sum - near - near;
	}

	/**
	 * 非负数组nums中，子序列累加和不超过t，但是最接近t的累加和是多少
	 * 01背包问题(子集累加和尽量接近t) + 空间压缩
	 * 
	 * 解题思路：
	 * 使用01背包问题的解法，dp[j]表示容量为j的背包最多能装的石头重量
	 * 状态转移方程：dp[j] = max(dp[j], dp[j - num] + num)
	 * 
	 * @param nums 数组
	 * @param t 目标值
	 * @return 不超过t但最接近t的子序列累加和
	 */
	public static int near(int[] nums, int t) {
		// dp[j] 表示在容量为j的背包中能装入的最大重量
		int[] dp = new int[t + 1];
		
		// 遍历每个石头（物品）
		for (int num : nums) {
			// 倒序遍历背包容量，确保每个物品只使用一次
			for (int j = t; j >= num; j--) {
				// 状态转移方程：
				// dp[j] = max(不选择当前石头, 选择当前石头)
				// 不选择当前石头：dp[j]（保持原值）
				// 选择当前石头：dp[j - num] + num（前一个状态+当前石头重量）
				// dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-nums[i]]+nums[i])
				dp[j] = Math.max(dp[j], dp[j - num] + num);
			}
		}
		
		// 返回容量为t的背包能装入的最大重量
		return dp[t];
	}
	
	// LeetCode 1049. 最后一块石头的重量 II
    // 题目描述：有一堆石头，用整数数组 stones 表示，每一回合，从中选出任意两块石头，然后将它们一起粉碎。
    // 假设石头的重量分别为 x 和 y，且 x <= y，粉碎结果：
    // 如果 x == y，那么两块石头都会被完全粉碎；
    // 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
    // 最后，最多只会剩下一块石头，返回此石头最小的可能重量。如果没有石头剩下，就返回 0。
    // 链接：https://leetcode.cn/problems/last-stone-weight-ii/
    
    /**
     * 计算最后一块石头的最小可能重量（LeetCode版本）
     * 
     * 解题思路：
     * 1. 这道题可以转化为将石头分为两堆，使得两堆重量差最小
     * 2. 假设两堆分别为 A 和 B，A >= B
     * 3. 最终剩下的石头重量就是 A - B
     * 4. 要使 A - B 最小，就要使 B 尽可能接近 sum/2
     * 5. 问题转化为：在不超过 sum/2 的前提下，背包最多能装多少重量的石头
     * 6. 这就是一个标准的01背包问题
     * 
     * @param stones 石头重量数组
     * @return 最后一块石头的最小可能重量
     */
    public static int lastStoneWeightIILeetcode(int[] stones) {
        // 参数验证
        if (stones == null || stones.length == 0) {
            return 0;
        }
        
        // 计算所有石头的总重量
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }
        
        // 目标是使其中一堆石头的重量尽可能接近总重量的一半
        int target = sum / 2;
        
        // dp[j] 表示容量为j的背包最多能装的石头重量
        int[] dp = new int[target + 1];
        
        // 遍历每个石头（物品）
        for (int stone : stones) {
            // 倒序遍历背包容量，确保每个物品只使用一次
            for (int j = target; j >= stone; j--) {
                // 状态转移方程：
                // dp[j] = max(不选择当前石头, 选择当前石头)
                // 不选择当前石头：dp[j]（保持原值）
                // 选择当前石头：dp[j - stone] + stone（前一个状态+当前石头重量）
                dp[j] = Math.max(dp[j], dp[j - stone] + stone);
            }
        }
        
        // 返回两堆石头重量差的最小值
        // 其中一堆重量为dp[target]，另一堆重量为sum-dp[target]
        // 重量差为 (sum-dp[target]) - dp[target] = sum - 2*dp[target]
        return sum - 2 * dp[target];
    }
    
    /*
     * 解题思路：
     * 1. 这道题可以转化为将石头分为两堆，使得两堆重量差最小
     * 2. 假设两堆分别为 A 和 B，A >= B
     * 3. 最终剩下的石头重量就是 A - B
     * 4. 要使 A - B 最小，就要使 B 尽可能接近 sum/2
     * 5. 问题转化为：在不超过 sum/2 的前提下，背包最多能装多少重量的石头
     * 6. 这就是一个标准的01背包问题
     *
     * 示例:
     * 输入: stones = [2,7,4,1,8,1]
     * 输出: 1
     * 解释: 
     *   最优分法:
     *   选 2,8,1 放一堆，总重量是11
     *   选 7,4,1 放另一堆，总重量是12
     *   最后剩下石头重量 = 12 - 11 = 1
     *
     * 时间复杂度: O(n * sum)
     *   - 外层循环遍历所有石头：O(n)
     *   - 内层循环遍历背包容量：O(sum)
     * 空间复杂度: O(sum)
     *   - 一维DP数组的空间消耗
     */

}