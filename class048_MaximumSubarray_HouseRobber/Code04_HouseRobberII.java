package class070;

/**
 * 环形数组中不能选相邻元素的最大累加和（打家劫舍 II）
 * 给定一个数组nums，长度为n
 * nums是一个环形数组，下标0和下标n-1是连在一起的
 * 可以随意选择数字，但是不能选择相邻的数字
 * 返回能得到的最大累加和
 * 测试链接 : https://leetcode.cn/problems/house-robber-ii/
 * 
 * 算法核心思想：
 * 1. 环形数组的打家劫舍问题可以分解为两个线性问题：
 *    a) 不偷第一个房屋（考虑nums[1...n-1]）
 *    b) 偷第一个房屋（考虑nums[0] + nums[2...n-2]）
 * 2. 取这两种情况的最大值作为最终结果
 * 3. 使用动态规划解决线性打家劫舍问题
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 需要遍历数组两次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、单元素数组等特殊情况
 * 2. 异常防御：处理索引越界等错误情况
 * 3. 性能优化：使用空间优化的动态规划
 */
public class Code04_HouseRobberII {

	/**
	 * 计算环形数组的打家劫舍最大金额
	 * 
	 * 算法原理：
	 * - 情况1：不偷第一个房屋，问题转化为nums[1...n-1]的线性打家劫舍
	 * - 情况2：偷第一个房屋，问题转化为nums[0] + nums[2...n-2]的线性打家劫舍
	 * - 取两种情况的最大值
	 * 
	 * 时间复杂度：O(n) - 两次线性打家劫舍计算
	 * 空间复杂度：O(1) - 常数空间
	 * 
	 * @param nums 环形数组，表示每个房屋的金额
	 * @return 最大可偷窃金额
	 * @throws IllegalArgumentException 如果输入数组为空
	 */
	public static int rob(int[] nums) {
		// 边界检查
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		int n = nums.length;
		// 特殊情况：单元素数组
		if (n == 1) {
			return nums[0];
		}
		
		// 情况1：不偷第一个房屋（考虑nums[1...n-1]）
		int case1 = best(nums, 1, n - 1);
		// 情况2：偷第一个房屋（考虑nums[0] + nums[2...n-2]）
		int case2 = nums[0] + best(nums, 2, n - 2);
		
		// 返回两种情况的最大值
		return Math.max(case1, case2);
	}

	/**
	 * 计算线性数组的打家劫舍最大金额（空间优化版本）
	 * 
	 * 算法细节：
	 * - 使用两个变量保存前两个状态，实现空间优化
	 * - 处理各种边界情况（空范围、单元素、双元素）
	 * - 时间复杂度：O(r-l+1)
	 * 
	 * @param nums 原始数组
	 * @param l 起始索引（包含）
	 * @param r 结束索引（包含）
	 * @return 指定范围内的最大打家劫舍金额
	 */
	public static int best(int[] nums, int l, int r) {
		// 边界检查：空范围
		if (l > r) {
			return 0;
		}
		// 单元素范围
		if (l == r) {
			return nums[l];
		}
		// 双元素范围
		if (l + 1 == r) {
			return Math.max(nums[l], nums[r]);
		}
		
		// 空间优化的动态规划
		int prepre = nums[l];  // dp[i-2]
		int pre = Math.max(nums[l], nums[l + 1]);  // dp[i-1]
		
		// 从第三个元素开始遍历
		for (int i = l + 2; i <= r; i++) {
			// 状态转移：选择偷或不偷当前房屋
			int current = Math.max(pre, nums[i] + Math.max(0, prepre));
			// 更新状态
			prepre = pre;
			pre = current;
		}
		
		return pre;
	}
	
	/**
	 * 主函数用于测试和演示
	 */
	public static void main(String[] args) {
		// 测试用例1: 标准环形数组
		int[] test1 = {2, 3, 2};
		System.out.println("测试用例1: " + java.util.Arrays.toString(test1));
		System.out.println("结果: " + rob(test1)); // 期望: 3
		
		// 测试用例2: 单元素数组
		int[] test2 = {1};
		System.out.println("测试用例2: " + java.util.Arrays.toString(test2));
		System.out.println("结果: " + rob(test2)); // 期望: 1
		
		// 测试用例3: 双元素数组
		int[] test3 = {1, 2};
		System.out.println("测试用例3: " + java.util.Arrays.toString(test3));
		System.out.println("结果: " + rob(test3)); // 期望: 2
		
		// 测试用例4: 复杂环形数组
		int[] test4 = {1, 2, 3, 1};
		System.out.println("测试用例4: " + java.util.Arrays.toString(test4));
		System.out.println("结果: " + rob(test4)); // 期望: 4
		
		// 测试用例5: 全正数数组
		int[] test5 = {5, 10, 5, 10, 5};
		System.out.println("测试用例5: " + java.util.Arrays.toString(test5));
		System.out.println("结果: " + rob(test5)); // 期望: 20
	}
	
	/*
	 * 扩展思考与深度分析：
	 * 
	 * 1. 算法正确性证明：
	 *    - 环形数组的打家劫舍问题可以分解为两个互斥的线性问题
	 *    - 情况1和情况2覆盖了所有可能的偷窃方案
	 *    - 取最大值保证了最优解
	 * 
	 * 2. 特殊情况处理：
	 *    - 单元素数组：直接返回该元素
	 *    - 双元素数组：返回较大的元素
	 *    - 空数组：抛出异常或返回0
	 * 
	 * 3. 工程应用场景：
	 *    - 环形资源分配：如环形网络中的资源优化
	 *    - 周期性任务调度：避免相邻周期冲突
	 *    - 环形缓冲区管理：最优资源利用
	 * 
	 * 4. 性能优化技巧：
	 *    - 空间优化：使用两个变量代替dp数组
	 *    - 范围优化：只计算必要的子范围
	 *    - 提前终止：对于特殊情况的快速处理
	 */
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
	 * 2. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
	 * 3. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
	 * 4. LeetCode 256. 粉刷房子 - https://leetcode.cn/problems/paint-house/
	 * 5. LeetCode 276. 栅栏涂色 - https://leetcode.cn/problems/paint-fence/
	 * 6. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
	 * 7. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
	 * 8. LeetCode 2560. 打家劫舍 IV - https://leetcode.cn/problems/house-robber-iv/
	 * 9. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
	 * 10. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
	 */
}