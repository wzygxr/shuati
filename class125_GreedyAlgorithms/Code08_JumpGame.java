package class094;

import java.util.Arrays;
import java.util.Stack;

// 跳跃游戏 (Jump Game)
// 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 判断你是否能够到达最后一个下标。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、数组遍历(Array Traversal)、可达性分析(Reachability Analysis)
// 时间复杂度: O(n)，其中n是数组长度
// 空间复杂度: O(1)，仅使用常数额外空间
// 测试链接 : https://leetcode.cn/problems/jump-game/
// 相关题目: LeetCode 45. 跳跃游戏 II、LeetCode 134. 加油站
// 贪心算法专题 - 补充题目收集与详解
public class Code08_JumpGame {

	/*
	 * 算法思路详解：
	 * 1. 贪心策略核心：维护能到达的最远位置maxReach，
	 *    这样可以确保在遍历过程中始终知道能够到达的边界
	 * 2. 遍历优化：通过一次遍历数组，动态更新最远可达位置
	 * 3. 不可达判断：如果当前位置i超过了maxReach，说明无法到达位置i
	 * 4. 可达性确认：当maxReach >= nums.length - 1时，说明可以到达终点
	 *
	 * 时间复杂度分析：
	 * - O(n)，其中n是数组长度，只需要一次遍历即可完成判断
	 * 空间复杂度分析：
	 * - O(1)，仅使用了常数级别的额外空间存储maxReach等变量
	 * 是否最优解：是，这是处理此类可达性问题的最优解法
	 *
	 * 工程化最佳实践：
	 * 1. 输入验证：严格检查输入参数的有效性，防止空指针异常
	 * 2. 边界处理：妥善处理各种边界情况，如空数组、单元素数组等
	 * 3. 性能优化：采用单次遍历策略，避免重复计算
	 * 4. 代码可读性：使用语义明确的变量名和详尽的注释
	 * 5. 提前终止：一旦确认可达就提前返回，避免不必要的计算
	 *
	 * 极端场景与边界情况处理：
	 * 1. 空输入场景：nums为空数组或null时直接返回false
	 * 2. 单元素场景：只有一个元素时肯定可达
	 * 3. 全零场景：所有元素都是0时只有位置0可达
	 * 4. 递增序列：元素值递增时可达性分析
	 * 5. 递减序列：元素值递减时的可达性判断
	 * 6. 特殊模式：如[1,0,1,0]等交替模式的处理
	 *
	 * 跨语言实现差异与优化：
	 * 1. Java实现：使用传统for循环，注意数组边界检查
	 * 2. C++实现：使用传统for循环，注意指针和数组访问
	 * 3. Python实现：使用for循环或enumerate函数，利用列表特性
	 * 4. 内存管理：不同语言的垃圾回收机制对性能的影响
	 *
	 * 调试与测试策略：
	 * 1. 过程可视化：在关键节点打印当前位置和最远可达位置
	 * 2. 断言验证：在循环体内添加断言确保maxReach单调不减
	 * 3. 性能监控：跟踪遍历过程的实际执行时间
	 * 4. 边界测试：设计覆盖所有边界条件的测试用例
	 * 5. 压力测试：使用大规模数据验证算法稳定性
	 *
	 * 实际应用场景与拓展：
	 * 1. 游戏开发：角色移动范围判断、关卡可达性分析
	 * 2. 网络路由：网络连通性快速判断、最短路径预判
	 * 3. 机器人路径规划：移动机器人可达区域分析
	 * 4. 任务调度：任务依赖关系可达性分析
	 * 5. 社交网络：人际关系传播范围分析
	 *
	 * 算法深入解析：
	 * 1. 贪心策略原理：维护最远可达位置确保全局最优
	 * 2. 算法不变式：遍历过程中maxReach始终表示当前能到达的最远位置
	 * 3. 正确性证明：如果位置i可达，则0到i-1所有位置都可达
	 * 4. 优化扩展：可记录具体路径或跳跃次数
	 */
	public static boolean canJump(int[] nums) {
		// 异常处理：检查输入是否为空
		if (nums == null || nums.length == 0) {
			return false;
		}
		
		// 边界条件：只有一个元素，肯定能到达
		if (nums.length == 1) {
			return true;
		}
		
		int maxReach = 0;  // 能到达的最远位置
		
		// 遍历数组
		for (int i = 0; i < nums.length; i++) {
			// 如果当前位置超过了能到达的最远位置，则无法到达终点
			if (i > maxReach) {
				return false;
			}
			
			// 更新能到达的最远位置
			maxReach = Math.max(maxReach, i + nums[i]);
			
			// 如果能到达的最远位置大于等于最后一个下标，则能到达终点
			if (maxReach >= nums.length - 1) {
				return true;
			}
		}
		
		return false;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = {2, 3, 1, 1, 4};
		System.out.println("测试用例1结果: " + canJump(nums1)); // 期望输出: true
		
		// 测试用例2
		int[] nums2 = {3, 2, 1, 0, 4};
		System.out.println("测试用例2结果: " + canJump(nums2)); // 期望输出: false
		
		// 测试用例3：边界情况
		int[] nums3 = {0};
		System.out.println("测试用例3结果: " + canJump(nums3)); // 期望输出: true
		
		// 测试用例4：极端情况
		int[] nums4 = {1, 0, 1, 0};
		System.out.println("测试用例4结果: " + canJump(nums4)); // 期望输出: false
		
		// 测试用例5：全为1
		int[] nums5 = {1, 1, 1, 1, 1};
		System.out.println("测试用例5结果: " + canJump(nums5)); // 期望输出: true
	}

	// 补充题目1: LeetCode 134. 加油站
	// 题目描述: 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
	// 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
	// 你从其中的一个加油站出发，开始时油箱为空。
	// 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
	// 注意：如果题目有解，该答案即为唯一答案。
	// 链接: https://leetcode.cn/problems/gas-station/
	public static int canCompleteCircuit(int[] gas, int[] cost) {
		if (gas == null || cost == null || gas.length != cost.length) {
			return -1; // 输入不合法
		}
		
		int n = gas.length;
		int totalGas = 0;    // 总油量
		int totalCost = 0;   // 总消耗
		int currentGas = 0;  // 当前剩余油量
		int startStation = 0; // 起始加油站
		
		// 贪心策略：如果从A到B的路上没油了，那么A到B之间的任何一个站点都不能作为起点
		for (int i = 0; i < n; i++) {
			totalGas += gas[i];
			totalCost += cost[i];
			currentGas += gas[i] - cost[i];
			
			// 如果当前剩余油量为负，说明从startStation到i的路径不可行
			if (currentGas < 0) {
				startStation = i + 1; // 从下一个站点重新开始计算
				currentGas = 0;       // 重置当前剩余油量
			}
		}
		
		// 如果总油量小于总消耗，那么无论如何都不可能绕行一周
		if (totalGas < totalCost) {
			return -1;
		}
		
		// 否则，startStation就是答案
		return startStation;
	}

	// 补充题目2: LeetCode 561. 数组拆分 I
	// 题目描述: 给定长度为 2n 的整数数组 nums ，你的任务是将这些数分成 n 对，
	// 例如 (a1, b1), (a2, b2), ..., (an, bn) ，使得从 1 到 n 的 min(ai, bi) 总和最大。
	// 返回该 最大总和 。
	// 链接: https://leetcode.cn/problems/array-partition-i/
	public static int arrayPairSum(int[] nums) {
		if (nums == null || nums.length % 2 != 0) {
			return 0; // 输入不合法
		}
		
		// 贪心策略：将数组排序后，每两个相邻的数分为一组，取较小的那个（即每对中的第一个数）
		Arrays.sort(nums);
		int maxSum = 0;
		
		// 每隔一个元素取一个（即每对中的第一个元素）
		for (int i = 0; i < nums.length; i += 2) {
			maxSum += nums[i];
		}
		
		return maxSum;
	}

	// 补充题目3: LeetCode 402. 移掉K位数字
	// 题目描述: 给你一个以字符串表示的非负整数 num 和一个整数 k ，
	// 移除这个数中的 k 位数字，使得剩下的数字最小。
	// 请你以字符串形式返回这个最小的数字。
	// 链接: https://leetcode.cn/problems/remove-k-digits/
	public static String removeKdigits(String num, int k) {
		if (num == null || num.isEmpty() || k >= num.length()) {
			return "0"; // 移除所有数字，返回0
		}
		
		// 使用栈来存储需要保留的数字
		Stack<Character> stack = new Stack<>();
		
		// 贪心策略：从左到右遍历，如果当前数字小于栈顶数字，且还有删除次数，则弹出栈顶数字
		for (int i = 0; i < num.length(); i++) {
			char digit = num.charAt(i);
			// 当栈不为空，当前数字小于栈顶数字，且还有删除次数时，弹出栈顶数字
			while (!stack.isEmpty() && digit < stack.peek() && k > 0) {
				stack.pop();
				k--;
			}
			// 将当前数字入栈
			stack.push(digit);
		}
		
		// 如果还有删除次数，从栈顶删除
		while (k > 0) {
			stack.pop();
			k--;
		}
		
		// 构建结果字符串
		StringBuilder result = new StringBuilder();
		while (!stack.isEmpty()) {
			result.append(stack.pop());
		}
		result.reverse(); // 反转字符串，因为栈是后进先出的
		
		// 去除前导零
		int start = 0;
		while (start < result.length() && result.charAt(start) == '0') {
			start++;
		}
		
		// 如果全是零，返回"0"
		return start == result.length() ? "0" : result.substring(start);
	}

	// 补充题目4: LeetCode 122. 买卖股票的最佳时机 II（另一种贪心实现）
	// 题目描述: 给定一个数组 prices ，其中 prices[i] 表示股票第 i 天的价格。
	// 在每一天，你可能会决定购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。
	// 你也可以购买它，然后在 同一天 出售。
	// 返回 你能获得的 最大 利润 。
	// 链接: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
	public static int maxProfit(int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0; // 无法交易
		}
		
		int maxProfit = 0;
		// 贪心策略：只要后一天的价格比前一天高，就进行一次买卖
		for (int i = 1; i < prices.length; i++) {
			// 如果当天价格比前一天高，就进行交易
			if (prices[i] > prices[i - 1]) {
				maxProfit += prices[i] - prices[i - 1];
			}
		}
		
		return maxProfit;
	}

	// 补充题目5: LeetCode 665. 非递减数列
	// 题目描述: 给你一个长度为 n 的整数数组 nums ，请你判断在 最多 改变 1 个元素的情况下，
	// 该数组能否变成一个非递减数列。
	// 非递减数列的定义是：对于数组中任意的 i (0 <= i <= n-2)，总满足 nums[i] <= nums[i+1]。
	// 链接: https://leetcode.cn/problems/non-decreasing-array/
	public static boolean checkPossibility(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return true; // 空数组或只有一个元素是非递减的
		}
		
		int count = 0; // 记录需要修改的次数
		
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i] > nums[i + 1]) {
				count++;
				if (count > 1) {
					return false; // 需要修改超过1次
				}
				
				// 贪心策略：尽可能修改nums[i]而不是nums[i+1]，这样对后续影响更小
				// 但是如果nums[i-1] > nums[i+1]，则必须修改nums[i+1]
				if (i > 0 && nums[i - 1] > nums[i + 1]) {
					nums[i + 1] = nums[i]; // 修改nums[i+1]
				} else {
					nums[i] = nums[i + 1]; // 修改nums[i]
				}
			}
		}
		
		return true;
	}
}