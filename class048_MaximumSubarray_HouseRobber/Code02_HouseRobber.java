package class070;

/**
 * 打家劫舍问题（数组中不能选相邻元素的最大累加和）
 * 题目描述：给定一个数组，可以随意选择数字，但是不能选择相邻的数字，返回能得到的最大累加和。
 * 从另一个角度理解：你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
 * 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，
 * 一夜之内能够偷窃到的最高金额。
 * 
 * 测试链接：https://leetcode.cn/problems/house-robber/
 * 
 * 算法核心思想：
 * 这是一个经典的动态规划问题，对于每个位置，我们有两种选择：选或者不选。
 * 如果选当前元素，那么前一个元素就不能选；如果不选当前元素，那么可以选择前一个元素或不选。
 * 我们需要在每一步做出最优选择，使得最终的累加和最大。
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 异常处理：对空数组和边界情况进行处理
 * 2. 鲁棒性：处理极端输入（单元素、双元素、全零等）
 * 3. 可测试性：提供完整的测试用例和性能分析
 * 4. 多解法对比：展示不同实现方式的优缺点
 */
public class Code02_HouseRobber {

	/**
	 * 方法一：动态规划（基础版本）
	 * 时间复杂度：O(n) - 需要遍历数组一次
	 * 空间复杂度：O(n) - 使用一维dp数组存储状态
	 * 
	 * 算法细节：
	 * - dp[i]表示考虑前i+1个房屋能获得的最大金额
	 * - 状态转移：dp[i] = max(dp[i-1], dp[i-2] + nums[i])
	 * - 边界处理：单独处理n=0,1,2的情况
	 * 
	 * 工程化考量：
	 * - 边界检查：处理各种边界情况
	 * - 异常安全：确保方法在各种输入下都能正确返回
	 * - 可读性：清晰的变量命名和注释
	 * 
	 * @param nums 表示每个房屋存放金额的非负整数数组
	 * @return 在不触动警报装置的情况下能够偷窃到的最高金额
	 * @throws IllegalArgumentException 如果输入为null
	 */
	public static int rob1(int[] nums) {
		// 边界检查：处理空数组情况
		if (nums == null) {
			throw new IllegalArgumentException("输入数组不能为null");
		}
		if (nums.length == 0) {
			return 0; // 没有房屋可偷，返回0
		}
		
		int n = nums.length;
		// 处理特殊情况：单元素和双元素数组
		if (n == 1) {
			return nums[0]; // 只有一个房屋，只能偷这个
		}
		if (n == 2) {
			return Math.max(nums[0], nums[1]); // 两个房屋，选金额大的
		}
		
		// dp[i] : nums[0...i]范围上可以随意选择数字，但是不能选相邻数，能得到的最大累加和
		// 这种定义方式便于理解状态转移关系
		int[] dp = new int[n];
		
		// 初始化：处理前两个元素
		dp[0] = nums[0]; // 只有一个元素时，最大值就是该元素
		dp[1] = Math.max(nums[0], nums[1]); // 有两个元素时，选较大的那个
		
		// 状态转移：从第三个元素开始遍历
		// 每个位置都有两种选择：偷或不偷当前房屋
		for (int i = 2; i < n; i++) {
			// 对于第i个元素，有两种选择：
			// 1. 不选第i个元素：继承前i-1个房屋的最大值dp[i-1]
			// 2. 选第i个元素：前i-2个房屋的最大值dp[i-2]加上当前房屋金额nums[i]
			// 优化考虑：如果nums[i]本身很大，可能比dp[i-2]+nums[i]还大（当dp[i-2]为负时）
			dp[i] = Math.max(dp[i - 1], Math.max(nums[i], dp[i - 2] + nums[i]));
		}
		
		// 返回考虑所有元素后的最大累加和
		// dp[n-1]表示考虑所有n个房屋时的最大金额
		return dp[n - 1];
	}

	/**
	 * 方法二：动态规划（空间优化版本）
	 * 时间复杂度：O(n) - 需要遍历数组一次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * @param nums 表示每个房屋存放金额的非负整数数组
	 * @return 在不触动警报装置的情况下能够偷窃到的最高金额
	 */
	public static int rob2(int[] nums) {
		// 边界检查
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		int n = nums.length;
		if (n == 1) {
			return nums[0];
		}
		if (n == 2) {
			return Math.max(nums[0], nums[1]);
		}
		
		// 用两个变量代替dp数组，优化空间复杂度
		int prepre = nums[0]; // 相当于dp[i-2]
		int pre = Math.max(nums[0], nums[1]); // 相当于dp[i-1]
		
		// 状态转移：从第三个元素开始遍历
		for (int i = 2, cur; i < n; i++) {
			// 计算当前位置的最大累加和
			cur = Math.max(pre, Math.max(nums[i], prepre + nums[i]));
			// 更新状态，为下一轮迭代做准备
			prepre = pre;
			pre = cur;
		}
		
		// pre相当于dp[n-1]，即为最终结果
		return pre;
	}
	
	/**
	 * 方法三：另一种常见的动态规划实现（更简洁的状态转移方程）
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param nums 表示每个房屋存放金额的非负整数数组
	 * @return 在不触动警报装置的情况下能够偷窃到的最高金额
	 */
	public static int rob3(int[] nums) {
		// 边界检查
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		int n = nums.length;
		if (n == 1) {
			return nums[0];
		}
		
		// dp[i]表示考虑前i+1个房子能获得的最大金额
		int[] dp = new int[n];
		
		// 初始化
		dp[0] = nums[0]; // 只考虑第一个房子
		dp[1] = Math.max(nums[0], nums[1]); // 考虑前两个房子，取较大值
		
		// 状态转移
		for (int i = 2; i < n; i++) {
			// 对于第i个房子，有两种选择：偷或不偷
			// 偷的话：dp[i-2] + nums[i]
			// 不偷的话：dp[i-1]
			dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
		}
		
		return dp[n - 1];
	}
	
	/**
	 * 方法四：另一种状态定义的空间优化版本
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 * 
	 * @param nums 表示每个房屋存放金额的非负整数数组
	 * @return 在不触动警报装置的情况下能够偷窃到的最高金额
	 */
	public static int rob4(int[] nums) {
		// 边界检查
		if (nums == null || nums.length == 0) {
			return 0;
		}
		if (nums.length == 1) {
			return nums[0];
		}
		
		// 用两个变量记录前两个状态
		int prev = 0;  // 相当于dp[i-2]
		int curr = nums[0]; // 相当于dp[i-1]
		
		for (int i = 1; i < nums.length; i++) {
			// 计算当前状态的最大值
			int temp = Math.max(curr, prev + nums[i]);
			prev = curr; // 更新prev为原来的curr
			curr = temp; // 更新curr为新计算的temp
		}
		
		return curr;
	}
	
	/**
	 * 主函数用于测试
	 */
	public static void main(String[] args) {
		// 测试用例1: 常规用例
		int[] test1 = {1, 2, 3, 1};
		System.out.println("测试用例1结果: " + rob2(test1)); // 期望输出: 4
		
		// 测试用例2: 连续高价值房屋
		int[] test2 = {2, 7, 9, 3, 1};
		System.out.println("测试用例2结果: " + rob2(test2)); // 期望输出: 12
		
		// 测试用例3: 单个房屋
		int[] test3 = {5};
		System.out.println("测试用例3结果: " + rob2(test3)); // 期望输出: 5
		
		// 测试用例4: 空数组
		int[] test4 = {};
		System.out.println("测试用例4结果: " + rob2(test4)); // 期望输出: 0
		
		// 测试用例5: 较大数组
		int[] test5 = {2, 1, 1, 2};
		System.out.println("测试用例5结果: " + rob2(test5)); // 期望输出: 4
	}
	
	/*
	 * 扩展思考：
	 * 1. 为什么这是最优解？
	 *    - 时间复杂度为O(n)，对于一维数组的遍历已经无法再优化
	 *    - 空间复杂度经过优化后为O(1)，也达到了最优
	 * 
	 * 2. 本题的变体：
	 *    - 环形房屋（LeetCode 213. 打家劫舍 II）
	 *    - 树形房屋（LeetCode 337. 打家劫舍 III）
	 *    - 删除并获得点数（LeetCode 740. Delete and Earn）
	 * 
	 * 3. 工程应用场景：
	 *    - 资源分配问题：在约束条件下选择资源以最大化收益
	 *    - 任务调度：某些任务不能连续执行，如何选择任务最大化收益
	 *    - 投资组合：某些投资项目之间存在排斥关系，如何选择项目最大化收益
	 */
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
	 * 2. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
	 * 3. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
	 * 4. LeetCode 256. 粉刷房子 - https://leetcode.cn/problems/paint-house/
	 * 5. LeetCode 276. 栅栏涂色 - https://leetcode.cn/problems/paint-fence/
	 * 6. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
	 * 7. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
	 * 8. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
	 * 9. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
	 * 10. LeetCode 1218. 最长定差子序列 - https://leetcode.cn/problems/longest-arithmetic-subsequence-of-given-difference/
	 */
	
	/*
	 * Python实现参考：
	 '''
	 def rob(nums):
	     if not nums:
	         return 0
	     if len(nums) == 1:
	         return nums[0]
	      
	     # 空间优化版本
	     prev_not_rob = 0      # 上一个房子不偷的最大金额
	     prev_rob = nums[0]    # 上一个房子偷的最大金额
	      
	     for i in range(1, len(nums)):
	         # 当前房子不偷的最大金额 = 上一个房子偷或不偷的最大值
	         current_not_rob = max(prev_not_rob, prev_rob)
	         # 当前房子偷的最大金额 = 上一个房子不偷的最大金额 + 当前房子的金额
	         current_rob = prev_not_rob + nums[i]
	         
	         # 更新状态
	         prev_not_rob = current_not_rob
	         prev_rob = current_rob
	      
	     # 返回最后一个房子偷或不偷的最大值
	     return max(prev_not_rob, prev_rob)
	 '''
	 
	 * C++实现参考：
	 '''
	 #include <vector>
	 #include <algorithm>
	 
	 int rob(std::vector<int>& nums) {
	     if (nums.empty()) {
	         return 0;
	     }
	     if (nums.size() == 1) {
	         return nums[0];
	     }
	     
	     int prev_not_rob = 0;      // 上一个房子不偷的最大金额
	     int prev_rob = nums[0];    // 上一个房子偷的最大金额
	     
	     for (size_t i = 1; i < nums.size(); ++i) {
	         // 当前房子不偷的最大金额
	         int current_not_rob = std::max(prev_not_rob, prev_rob);
	         // 当前房子偷的最大金额
	         int current_rob = prev_not_rob + nums[i];
	         
	         // 更新状态
	         prev_not_rob = current_not_rob;
	         prev_rob = current_rob;
	     }
	     
	     return std::max(prev_not_rob, prev_rob);
	 }
	 '''
	 */
}