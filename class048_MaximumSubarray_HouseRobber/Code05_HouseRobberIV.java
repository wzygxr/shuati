package class070;

/**
 * 打家劫舍 IV
 * 沿街有一排连续的房屋。每间房屋内都藏有一定的现金
 * 现在有一位小偷计划从这些房屋中窃取现金
 * 由于相邻的房屋装有相互连通的防盗系统，所以小偷不会窃取相邻的房屋
 * 小偷的 窃取能力 定义为他在窃取过程中能从单间房屋中窃取的 最大金额
 * 给你一个整数数组 nums 表示每间房屋存放的现金金额
 * 第i间房屋中放有nums[i]的钱数
 * 另给你一个整数k，表示小偷需要窃取至少 k 间房屋
 * 返回小偷需要的最小窃取能力值
 * 测试链接 : https://leetcode.cn/problems/house-robber-iv/
 * 
 * 算法核心思想：
 * 1. 这是一个二分搜索 + 动态规划（或贪心）的问题
 * 2. 关键观察：窃取能力值越大，能偷的房屋越多（单调性）
 * 3. 使用二分搜索在[min(nums), max(nums)]范围内寻找最小满足条件的ability
 * 4. 对于每个候选ability，计算最多能偷多少间房屋
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n * log(max-min)) - 二分搜索 + 线性验证
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、k=0等特殊情况
 * 2. 性能优化：使用贪心算法代替动态规划进行验证
 * 3. 鲁棒性：处理数值范围和边界条件
 */
public class Code05_HouseRobberIV {

	/**
	 * 计算小偷需要的最小窃取能力值
	 * 
	 * 算法原理：
	 * - 二分搜索：在房屋金额的最小值和最大值之间搜索
	 * - 验证函数：对于每个候选ability，计算最多能偷多少间房屋
	 * - 单调性：ability越大，能偷的房屋越多
	 * 
	 * 时间复杂度：O(n * log(max-min))
	 * 空间复杂度：O(1)
	 * 
	 * @param nums 房屋金额数组
	 * @param k 需要窃取的最小房屋数量
	 * @return 最小窃取能力值
	 * @throws IllegalArgumentException 如果输入无效
	 */
	public static int minCapability(int[] nums, int k) {
		// 边界检查
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		if (k <= 0) {
			throw new IllegalArgumentException("k必须大于0");
		}
		if (k > nums.length) {
			throw new IllegalArgumentException("k不能大于数组长度");
		}
		
		int n = nums.length;
		// 确定二分搜索的范围 [min, max]
		int minVal = nums[0];
		int maxVal = nums[0];
		for (int i = 1; i < n; i++) {
			minVal = Math.min(minVal, nums[i]);
			maxVal = Math.max(maxVal, nums[i]);
		}
		
		// 特殊情况：k=1时，最小能力值就是数组中的最小值
		if (k == 1) {
			return minVal;
		}
		
		// 二分搜索：在[minVal, maxVal]范围内寻找最小满足条件的ability
		int left = minVal;
		int right = maxVal;
		int answer = maxVal; // 初始化为最大值
		
		while (left <= right) {
			int mid = left + (right - left) / 2; // 防止溢出
			// 验证当前ability是否能偷至少k间房屋
			if (mostRobGreedy(nums, n, mid) >= k) {
				answer = mid; // 当前ability满足条件，尝试更小的值
				right = mid - 1;
			} else {
				left = mid + 1; // 当前ability不满足条件，需要更大的值
			}
		}
		
		return answer;
	}

	/**
	 * 动态规划版本：计算给定ability时最多能偷多少间房屋
	 * 
	 * 算法细节：
	 * - dp[i]表示考虑前i+1个房屋时最多能偷的房屋数量
	 * - 状态转移：dp[i] = max(dp[i-1], dp[i-2] + (nums[i] <= ability ? 1 : 0))
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param nums 房屋金额数组
	 * @param n 数组长度
	 * @param ability 窃取能力值
	 * @return 最多能偷的房屋数量
	 */
	public static int mostRobDP(int[] nums, int n, int ability) {
		// 边界情况处理
		if (n == 1) {
			return nums[0] <= ability ? 1 : 0;
		}
		if (n == 2) {
			return (nums[0] <= ability || nums[1] <= ability) ? 1 : 0;
		}
		
		// 动态规划数组
		int[] dp = new int[n];
		dp[0] = nums[0] <= ability ? 1 : 0;
		dp[1] = (nums[0] <= ability || nums[1] <= ability) ? 1 : 0;
		
		// 状态转移
		for (int i = 2; i < n; i++) {
			// 选择偷或不偷当前房屋
			dp[i] = Math.max(dp[i - 1], (nums[i] <= ability ? 1 : 0) + dp[i - 2]);
		}
		
		return dp[n - 1];
	}

	/**
	 * 空间优化版本：使用两个变量代替dp数组
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 * 
	 * @param nums 房屋金额数组
	 * @param n 数组长度
	 * @param ability 窃取能力值
	 * @return 最多能偷的房屋数量
	 */
	public static int mostRobOptimized(int[] nums, int n, int ability) {
		if (n == 1) {
			return nums[0] <= ability ? 1 : 0;
		}
		if (n == 2) {
			return (nums[0] <= ability || nums[1] <= ability) ? 1 : 0;
		}
		
		// 空间优化：只保存前两个状态
		int prepre = nums[0] <= ability ? 1 : 0;
		int pre = (nums[0] <= ability || nums[1] <= ability) ? 1 : 0;
		
		for (int i = 2; i < n; i++) {
			int current = Math.max(pre, (nums[i] <= ability ? 1 : 0) + prepre);
			prepre = pre;
			pre = current;
		}
		
		return pre;
	}

	/**
	 * 贪心算法版本：更高效的实现
	 * 
	 * 算法原理：
	 * - 贪心策略：只要能偷就偷，然后跳过下一个房屋
	 * - 这种策略在打家劫舍约束下是最优的
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 * 
	 * @param nums 房屋金额数组
	 * @param n 数组长度
	 * @param ability 窃取能力值
	 * @return 最多能偷的房屋数量
	 */
	public static int mostRobGreedy(int[] nums, int n, int ability) {
		int count = 0;
		int i = 0;
		
		while (i < n) {
			if (nums[i] <= ability) {
				// 偷当前房屋，然后跳过下一个
				count++;
				i += 2; // 跳过下一个房屋
			} else {
				// 不能偷当前房屋，检查下一个
				i++;
			}
		}
		
		return count;
	}
	
	/**
	 * 主函数用于测试和演示
	 */
	public static void main(String[] args) {
		// 测试用例1: 标准情况
		int[] test1 = {2, 3, 5, 9};
		int k1 = 2;
		System.out.println("测试用例1: nums = " + java.util.Arrays.toString(test1) + ", k = " + k1);
		System.out.println("结果: " + minCapability(test1, k1)); // 期望: 5
		
		// 测试用例2: 简单情况
		int[] test2 = {2, 7, 9, 3, 1};
		int k2 = 2;
		System.out.println("测试用例2: nums = " + java.util.Arrays.toString(test2) + ", k = " + k2);
		System.out.println("结果: " + minCapability(test2, k2)); // 期望: 2
		
		// 测试用例3: k=1的特殊情况
		int[] test3 = {1, 2, 3};
		int k3 = 1;
		System.out.println("测试用例3: nums = " + java.util.Arrays.toString(test3) + ", k = " + k3);
		System.out.println("结果: " + minCapability(test3, k3)); // 期望: 1
		
		// 测试用例4: 复杂情况
		int[] test4 = {4, 1, 2, 7, 5, 3, 1};
		int k4 = 3;
		System.out.println("测试用例4: nums = " + java.util.Arrays.toString(test4) + ", k = " + k4);
		System.out.println("结果: " + minCapability(test4, k4)); // 期望: 4
		
		// 验证不同实现方法的一致性
		System.out.println("方法验证: mostRobGreedy(test1, 4, 5) = " + mostRobGreedy(test1, 4, 5));
		System.out.println("方法验证: mostRobOptimized(test1, 4, 5) = " + mostRobOptimized(test1, 4, 5));
		System.out.println("方法验证: mostRobDP(test1, 4, 5) = " + mostRobDP(test1, 4, 5));
	}
	
	/*
	 * 扩展思考与深度分析：
	 * 
	 * 1. 算法正确性证明：
	 *    - 二分搜索的正确性基于单调性：ability越大，能偷的房屋越多
	 *    - 贪心算法的正确性：在不能偷相邻房屋的约束下，贪心策略是最优的
	 *    - 边界情况处理：k=1、k=n等特殊情况需要单独处理
	 * 
	 * 2. 性能优化分析：
	 *    - 二分搜索：将时间复杂度从O(n²)优化到O(n log n)
	 *    - 贪心算法：比动态规划更高效，且结果正确
	 *    - 空间优化：从O(n)优化到O(1)
	 * 
	 * 3. 工程应用场景：
	 *    - 资源分配：在约束条件下分配有限资源
	 *    - 阈值优化：寻找满足条件的最小阈值
	 *    - 调度问题：在约束条件下的最优调度
	 * 
	 * 4. 面试技巧：
	 *    - 先提出暴力解法，再优化到二分搜索
	 *    - 解释单调性的重要性
	 *    - 讨论不同验证方法的优缺点
	 */
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 2560. 打家劫舍 IV - https://leetcode.cn/problems/house-robber-iv/
	 * 2. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
	 * 3. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
	 * 4. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
	 * 5. LeetCode 1493. 删掉一个元素以后全为 1 的最长子数组 - https://leetcode.cn/problems/longest-subarray-of-1s-after-deleting-one-element/
	 * 6. LeetCode 1658. 将 x 减到 0 的最小操作数 - https://leetcode.cn/problems/minimum-operations-to-reduce-x-to-zero/
	 * 7. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
	 * 8. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
	 * 9. LeetCode 1011. 在 D 天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
	 * 10. LeetCode 1482. 制作 m 束花所需的最少天数 - https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/
	 */
}