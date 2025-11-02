package class073;

import java.util.HashMap;

/**
 * 目标和问题
 * 
 * 问题描述：
 * 给你一个非负整数数组 nums 和一个整数 target 。
 * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，
 * 可以构造一个表达式，返回可以通过上述方法构造的，运算结果等于 target 的不同表达式的数目。
 * 
 * 示例：
 * 输入：nums = [1,1,1,1,1], target = 3
 * 输出：5
 * 解释：一共有 5 种方法让最终目标和为 3 。
 * 
 * 解题思路：
 * 本题有多种解法：
 * 1. 暴力递归：对每个数字尝试加号或减号，递归计算所有可能的组合
 * 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
 * 3. 动态规划：使用二维DP数组，通过平移技巧处理负数下标问题
 * 4. 转化为01背包问题：通过数学推导将问题转化为求子集和的问题
 * 
 * 第四种方法是最优解：
 * 1. 将数组分为两个子集：正数集合A和负数集合B
 * 2. 有 sum(A) - sum(B) = target
 * 3. 两边同时加上 sum(A) + sum(B) 得到：2 * sum(A) = target + sum
 * 4. 即 sum(A) = (target + sum) / 2
 * 5. 问题转化为：求有多少个子集的和等于 (target + sum) / 2，这就是标准的01背包问题
 * 
 * 测试链接 : https://leetcode.cn/problems/target-sum/
 */
public class Code03_TargetSum {

	/**
	 * 普通尝试，暴力递归版
	 * 
	 * 解题思路：
	 * 对于数组中的每个元素，都有两种选择：加上该元素或减去该元素
	 * 递归地尝试所有可能的组合，当遍历完所有元素后，检查累加和是否等于target
	 * 
	 * 时间复杂度：O(2^n)，其中n是数组长度
	 * 空间复杂度：O(n)，递归栈深度
	 * 
	 * @param nums 非负整数数组
	 * @param target 目标和
	 * @return 不同表达式的数目
	 */
	public static int findTargetSumWays1(int[] nums, int target) {
		return f1(nums, target, 0, 0);
	}

	/**
	 * 暴力递归辅助函数
	 * 
	 * @param nums 非负整数数组
	 * @param target 目标和
	 * @param i 当前处理到数组的第i个元素
	 * @param sum 当前累加和
	 * @return 从第i个元素开始能构成target的不同表达式数目
	 */
	public static int f1(int[] nums, int target, int i, int sum) {
		// 基础情况：已经处理完所有元素
		if (i == nums.length) {
			// 如果当前累加和等于目标值，说明找到了一种有效方案
			return sum == target ? 1 : 0;
		}
		// 递归情况：对当前元素尝试加号和减号两种情况
		// 返回两种情况的方案数之和
		return f1(nums, target, i + 1, sum + nums[i]) + f1(nums, target, i + 1, sum - nums[i]);
	}

	/**
	 * 普通尝试，记忆化搜索版
	 * 
	 * 解题思路：
	 * 在暴力递归的基础上，使用哈希表缓存已经计算过的结果
	 * 避免重复计算相同状态（位置i和当前累加和sum）下的结果
	 * 
	 * 时间复杂度：O(n * sum)，其中n是数组长度，sum是数组元素和的范围
	 * 空间复杂度：O(n * sum)
	 * 
	 * @param nums 非负整数数组
	 * @param target 目标和
	 * @return 不同表达式的数目
	 */
	public static int findTargetSumWays2(int[] nums, int target) {
		// i, sum -> value（返回值）
		HashMap<Integer, HashMap<Integer, Integer>> dp = new HashMap<>();
		return f2(nums, target, 0, 0, dp);
	}

	/**
	 * 记忆化搜索辅助函数
	 * 
	 * @param nums 非负整数数组
	 * @param target 目标和
	 * @param i 当前处理到数组的第i个元素
	 * @param j 当前累加和
	 * @param dp 缓存已经计算过的结果
	 * @return 从第i个元素开始能构成target的不同表达式数目
	 */
	public static int f2(int[] nums, int target, int i, int j, HashMap<Integer, HashMap<Integer, Integer>> dp) {
		// 基础情况：已经处理完所有元素
		if (i == nums.length) {
			// 如果当前累加和等于目标值，说明找到了一种有效方案
			return j == target ? 1 : 0;
		}
		// 检查是否已经计算过当前状态
		if (dp.containsKey(i) && dp.get(i).containsKey(j)) {
			return dp.get(i).get(j);
		}
		// 递归计算两种情况的方案数之和
		int ans = f2(nums, target, i + 1, j + nums[i], dp) + f2(nums, target, i + 1, j - nums[i], dp);
		// 缓存计算结果
		dp.putIfAbsent(i, new HashMap<>());
		dp.get(i).put(j, ans);
		return ans;
	}

	/**
	 * 普通尝试，严格位置依赖的动态规划
	 * 
	 * 解题思路：
	 * 使用二维DP数组，dp[i][j]表示处理前i个元素，累加和为j的方案数
	 * 由于累加和可能为负数，使用平移技巧将负数下标转换为非负数下标
	 * 
	 * 时间复杂度：O(n * sum)，其中n是数组长度，sum是数组元素和
	 * 空间复杂度：O(n * sum)
	 * 
	 * @param nums 非负整数数组
	 * @param target 目标和
	 * @return 不同表达式的数目
	 */
	public static int findTargetSumWays3(int[] nums, int target) {
		// 计算数组元素和
		int s = 0;
		for (int num : nums) {
			s += num;
		}
		// 如果目标值超出可能的范围，直接返回0
		if (target < -s || target > s) {
			return 0;
		}
		int n = nums.length;
		// -s ~ +s -> 2 * s + 1，计算DP数组的列数
		int m = 2 * s + 1;
		// 原本的dp[i][j]含义:
		// nums[0...i-1]范围上，已经形成的累加和是sum
		// nums[i...]范围上，每个数字可以标记+或者-
		// 最终形成累加和为target的不同表达式数目
		// 因为sum可能为负数，为了下标不出现负数，
		// "原本的dp[i][j]"由dp表中的dp[i][j + s]来表示
		// 也就是平移操作！
		// 一切"原本的dp[i][j]"一律平移到dp表中的dp[i][j + s]
		int[][] dp = new int[n + 1][m];
		// 原本的dp[n][target] = 1，平移！
		dp[n][target + s] = 1;
		// 从后往前填表
		for (int i = n - 1; i >= 0; i--) {
			for (int j = -s; j <= s; j++) {
				// 状态转移方程：
				// dp[i][j] = dp[i+1][j+nums[i]] + dp[i+1][j-nums[i]]
				// 即对当前元素选择加号或减号两种情况
				if (j + nums[i] + s < m) {
					// 原本是 : dp[i][j] = dp[i + 1][j + nums[i]]
					// 平移！
					dp[i][j + s] = dp[i + 1][j + nums[i] + s];
				}
				if (j - nums[i] + s >= 0) {
					// 原本是 : dp[i][j] += dp[i + 1][j - nums[i]]
					// 平移！
					dp[i][j + s] += dp[i + 1][j - nums[i] + s];
				}

			}
		}
		// 原本应该返回dp[0][0]
		// 平移！
		// 返回dp[0][0 + s]
		return dp[0][s];
	}

	/**
	 * 新思路，转化为01背包问题
	 * 
	 * 解题思路：
	 * 通过数学推导将问题转化为01背包问题：
	 * 1. 将数组分为两个子集：正数集合A和负数集合B
	 * 2. 有 sum(A) - sum(B) = target
	 * 3. 两边同时加上 sum(A) + sum(B) 得到：2 * sum(A) = target + sum
	 * 4. 即 sum(A) = (target + sum) / 2
	 * 5. 问题转化为：求有多少个子集的和等于 (target + sum) / 2
	 * 
	 * 时间复杂度：O(n * t)，其中n是数组长度，t是(target + sum) / 2
	 * 空间复杂度：O(t)
	 * 
	 * @param nums 非负整数数组
	 * @param target 目标和
	 * @return 不同表达式的数目
	 */
	public static int findTargetSumWays4(int[] nums, int target) {
		// 计算数组元素和
		int sum = 0;
		for (int n : nums) {
			sum += n;
		}
		// 如果sum小于target或者(target+sum)是奇数，直接返回0
		if (sum < target || ((target & 1) ^ (sum & 1)) == 1) {
			return 0;
		}
		// 转化为求子集和为(target + sum) / 2的方案数
		return subsets(nums, (target + sum) >> 1);
	}

	/**
	 * 求非负数组nums有多少个子序列累加和是t
	 * 
	 * 解题思路：
	 * 使用01背包问题的解法，dp[j]表示和为j的子集数目
	 * 状态转移方程：dp[j] = dp[j] + dp[j - nums[i]]
	 * 
	 * @param nums 非负整数数组
	 * @param t 目标子集和
	 * @return 和为t的子集数目
	 */
	public static int subsets(int[] nums, int t) {
		// 如果目标值为负数，直接返回0
		if (t < 0) {
			return 0;
		}
		// dp[j]表示和为j的子集数目
		int[] dp = new int[t + 1];
		// 初始状态：和为0的子集有1个（空集）
		dp[0] = 1;
		// 遍历每个数字
		for (int num : nums) { // i省略了
			// 从后往前遍历，确保每个数字只使用一次
			for (int j = t; j >= num; j--) {
				// 状态转移方程：选择当前数字或不选择当前数字
				dp[j] += dp[j - num];
			}
		}
		// 返回和为t的子集数目
		return dp[t];
	}
	
	/**
	 * 牛客网背包问题
	 * 
	 * 题目描述：玥玥带乔乔一起逃亡，现在有许多的东西要放到乔乔的包里面，
	 * 但是包的大小有限，所以我们只能够在里面放入非常重要的物品。
	 * 现在给出该种物品的数量、体积、价值的数值，希望你能够算出怎样能使背包的价值最大的组合方式，
	 * 并且输出这个数值，乔乔会非常感谢你。
	 * 
	 * 解题思路：
	 * 这是经典的01背包问题，使用动态规划求解
	 * dp[j]表示背包容量为j时能装入的最大价值
	 * 状态转移方程：dp[j] = max(dp[j], dp[j - volumes[i]] + values[i])
	 * 
	 * @param n 物品数量
	 * @param v 背包容量
	 * @param volumes 物品体积数组
	 * @param values 物品价值数组
	 * @return 背包能装入的最大价值
	 */
	public static int backpack(int n, int v, int[] volumes, int[] values) {
        // dp[j] 表示背包容量为j时能装入的最大价值
        int[] dp = new int[v + 1];
        
        // 遍历物品
        for (int i = 0; i < n; i++) {
            // 倒序遍历背包容量，确保每个物品只使用一次
            for (int j = v; j >= volumes[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - volumes[i]] + values[i]);
            }
        }
        
        return dp[v];
    }
    
    /*
     * 示例:
     * 输入: n = 4, v = 5
     * volumes = [1, 2, 3, 4]
     * values = [2, 4, 4, 5]
     * 输出: 8
     * 解释: 选择第1个和第3个物品，总重量为2+3=5，总价值为4+4=8
     *
     * 时间复杂度: O(n * v)
     * 空间复杂度: O(v)
     */

}