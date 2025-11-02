package class094;

import java.util.Arrays;

// 买卖股票的最佳时机 II (Best Time to Buy and Sell Stock II)
// 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
// 设计一个算法来计算你所能获取的最大利润。
// 你可以尽可能地完成更多的交易（多次买卖一支股票）。
// 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、数组遍历(Array Traversal)
// 时间复杂度: O(n)，其中n是价格数组长度
// 空间复杂度: O(1)，仅使用常数额外空间
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
// 相关题目: LeetCode 121. 买卖股票的最佳时机、LeetCode 55. 跳跃游戏
// 贪心算法专题 - 补充题目收集与详解
public class Code07_BestTimeToBuyAndSellStockII {

	/*
	 * 算法思路详解：
	 * 1. 贪心策略核心：只要明天价格比今天高，就在今天买入明天卖出，
	 *    这相当于收集所有正的价格差，实现利润最大化
	 * 2. 遍历优化：通过一次遍历价格数组，计算相邻两天的价格差
	 * 3. 利润累加机制：如果价格差为正（即价格上涨），则将差值累加到总利润中
	 *
	 * 时间复杂度分析：
	 * - O(n)，其中n是价格数组长度，只需要一次遍历即可完成计算
	 * 空间复杂度分析：
	 * - O(1)，仅使用了常数级别的额外空间存储maxProfit变量
	 * 是否最优解：是，这是处理此类问题的最优解法，无法进一步优化
	 *
	 * 工程化最佳实践：
	 * 1. 输入验证：严格检查输入参数的有效性，防止空指针异常
	 * 2. 边界处理：妥善处理各种边界情况，如空数组、单元素数组等
	 * 3. 性能优化：采用单次遍历策略，避免重复计算
	 * 4. 代码可读性：使用语义明确的变量名和详尽的注释
	 *
	 * 极端场景与边界情况处理：
	 * 1. 空输入场景：prices为空数组或null时直接返回0
	 * 2. 极值场景：只有一个价格或所有价格相同（利润为0）
	 * 3. 重复数据场景：多个连续相同价格的处理
	 * 4. 特殊序列场景：价格持续上涨（最大利润）或持续下跌（利润为0）
	 * 5. 波动序列场景：价格频繁波动时的利润计算
	 *
	 * 跨语言实现差异与优化：
	 * 1. Java实现：使用增强for循环或传统for循环遍历数组
	 * 2. C++实现：使用传统for循环或范围for循环，注意数组边界
	 * 3. Python实现：使用for循环或enumerate函数，利用列表特性
	 * 4. 内存管理：不同语言的垃圾回收机制对性能的影响
	 *
	 * 调试与测试策略：
	 * 1. 过程可视化：在关键节点打印每日价格和累计利润
	 * 2. 断言验证：在循环体内添加断言确保利润计算正确
	 * 3. 性能监控：跟踪遍历过程的实际执行时间
	 * 4. 边界测试：设计覆盖所有边界条件的测试用例
	 * 5. 压力测试：使用大规模数据验证算法稳定性
	 *
	 * 实际应用场景与拓展：
	 * 1. 金融交易：股票、期货等金融产品的日内交易策略
	 * 2. 商品贸易：商品价格波动时的买卖决策
	 * 3. 能源管理：电力市场价格波动时的购电策略
	 * 4. 库存管理：商品进销差价最大化策略
	 * 5. 投资组合：多种资产价格波动时的最优交易时机
	 *
	 * 算法深入解析：
	 * 1. 贪心策略原理：收集所有正收益交易，等价于多次买卖
	 * 2. 数学本质：计算所有相邻元素正差值之和
	 * 3. 策略等价性：连续上涨时一次交易与多次交易利润相同
	 * 4. 风险控制：在实际应用中需考虑交易成本和风险
	 */
	public static int maxProfit(int[] prices) {
		// 异常处理：检查输入是否为空
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int maxProfit = 0;
		
		// 遍历价格数组，计算相邻两天的价格差
		for (int i = 1; i < prices.length; i++) {
			// 如果明天价格比今天高，则累加利润
			if (prices[i] > prices[i - 1]) {
				maxProfit += prices[i] - prices[i - 1];
			}
		}
		
		return maxProfit;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1
		int[] prices1 = {7, 1, 5, 3, 6, 4};
		System.out.println("测试用例1结果: " + maxProfit(prices1)); // 期望输出: 7
		
		// 测试用例2
		int[] prices2 = {1, 2, 3, 4, 5};
		System.out.println("测试用例2结果: " + maxProfit(prices2)); // 期望输出: 4
		
		// 测试用例3
		int[] prices3 = {7, 6, 4, 3, 1};
		System.out.println("测试用例3结果: " + maxProfit(prices3)); // 期望输出: 0
		
		// 测试用例4：边界情况
		int[] prices4 = {1};
		System.out.println("测试用例4结果: " + maxProfit(prices4)); // 期望输出: 0
		
		// 测试用例5：极端情况
		int[] prices5 = {1, 2, 1, 2, 1, 2};
		System.out.println("测试用例5结果: " + maxProfit(prices5)); // 期望输出: 3
	}

	// 补充题目1: LeetCode 55. 跳跃游戏 (Jump Game)
	// 题目描述: 给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
	// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
	// 判断你是否能够到达最后一个下标。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、数组遍历(Array Traversal)
	// 时间复杂度: O(n)，其中n是数组长度
	// 空间复杂度: O(1)，仅使用常数额外空间
	// 链接: https://leetcode.cn/problems/jump-game/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：维护能到达的最远位置，只要当前位置可达，
	 *    就可以更新最远可达位置
	 * 2. 可达性判断：如果当前位置超过了能到达的最远位置，则无法继续前进
	 * 3. 优化终止条件：一旦能到达最后位置就提前返回
	 *
	 * 算法步骤详解：
	 * 1. 初始化：设置能到达的最远位置为0
	 * 2. 遍历过程：
	 *    - 检查当前位置是否可达
	 *    - 更新能到达的最远位置为max(maxReach, i + nums[i])
	 *    - 若已能到达最后位置则提前返回true
	 * 3. 结果判断：遍历结束后检查是否能到达最后位置
	 *
	 * 算法正确性证明：
	 * 贪心选择性质：每次都尽可能扩展可达范围是最优选择
	 * 最优子结构：在确定当前可达范围后，剩余问题仍具有相同性质
	 */
	public static boolean canJump(int[] nums) {
		if (nums == null || nums.length == 0) {
			return true; // 空数组视为可以到达
		}
		
		int maxReach = 0; // 当前能到达的最远位置
		
		// 贪心策略：维护能到达的最远位置
		for (int i = 0; i < nums.length; i++) {
			// 如果当前位置超过了能到达的最远位置，无法继续前进
			if (i > maxReach) {
				return false;
			}
			// 更新能到达的最远位置
			maxReach = Math.max(maxReach, i + nums[i]);
			// 如果已经能到达或超过最后一个位置，直接返回true
			if (maxReach >= nums.length - 1) {
				return true;
			}
		}
		
		return maxReach >= nums.length - 1;
	}

	// 补充题目2: LeetCode 45. 跳跃游戏 II (Jump Game II)
	// 题目描述: 给定一个非负整数数组，你最初位于数组的第一个位置。
	// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
	// 你的目标是使用最少的跳跃次数到达数组的最后一个位置。
	// 假设你总是可以到达数组的最后一个位置。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、区间跳跃(Interval Jumping)
	// 时间复杂度: O(n)，其中n是数组长度
	// 空间复杂度: O(1)，仅使用常数额外空间
	// 链接: https://leetcode.cn/problems/jump-game-ii/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：在每次跳跃时，选择能跳得最远的位置，
	 *    这样可以最小化跳跃次数
	 * 2. 区间管理：维护当前跳跃能到达的边界和下次跳跃前能到达的最远位置
	 * 3. 跳跃决策：当遍历到当前跳跃边界时，必须进行下一次跳跃
	 *
	 * 算法步骤详解：
	 * 1. 初始化：设置跳跃次数、当前边界和最远位置
	 * 2. 遍历过程：
	 *    - 更新能到达的最远位置
	 *    - 当到达当前跳跃边界时增加跳跃次数
	 *    - 更新当前跳跃边界为最远位置
	 *    - 若已能到达最后位置则提前结束
	 * 3. 结果返回：返回累计的跳跃次数
	 *
	 * 算法优化与正确性：
	 * 贪心选择性质：每次在可到达范围内选择跳得最远的位置是最优的
	 * 最优子结构：确定最少跳跃次数后，剩余问题仍保持最优性
	 */
	public static int jump(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return 0; // 空数组或只有一个元素不需要跳跃
		}
		
		int jumps = 0;        // 跳跃次数
		int currentEnd = 0;   // 当前跳跃能到达的边界
		int farthest = 0;     // 在进行下次跳跃前能到达的最远位置
		
		// 贪心策略：每次在可到达范围内选择能跳得最远的位置
		for (int i = 0; i < nums.length - 1; i++) {
			// 更新能到达的最远位置
			farthest = Math.max(farthest, i + nums[i]);
			
			// 到达当前跳跃的边界，需要进行一次跳跃
			if (i == currentEnd) {
				jumps++;
				currentEnd = farthest;
				
				// 如果已经能到达最后位置，可以提前结束
				if (currentEnd >= nums.length - 1) {
					break;
				}
			}
		}
		
		return jumps;
	}

	// 补充题目3: LeetCode 605. 种花问题 (Can Place Flowers)
	// 题目描述: 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
	// 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
	// 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。
	// 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
	// 能则返回 true ，不能则返回 false 。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、数组遍历(Array Traversal)、约束满足(Constraint Satisfaction)
	// 时间复杂度: O(n)，其中n是花坛长度
	// 空间复杂度: O(1)，原地修改数组
	// 链接: https://leetcode.cn/problems/can-place-flowers/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：遍历花坛，尽可能多地种花，
	 *    在满足约束条件下优先种花
	 * 2. 约束检查：当前位置可以种花当且仅当：
	 *    - 当前位置为0（未种花）
	 *    - 前一个位置为0或不存在
	 *    - 后一个位置为0或不存在
	 * 3. 优化终止：一旦满足n朵花就提前返回
	 *
	 * 算法步骤详解：
	 * 1. 初始化：设置可种花计数器
	 * 2. 遍历过程：
	 *    - 检查当前位置是否满足种花条件
	 *    - 若满足则在当前位置种花并增加计数器
	 *    - 检查是否已满足n朵花，满足则提前返回true
	 * 3. 结果判断：返回累计种花数是否大于等于n
	 *
	 * 算法优化与边界处理：
	 * 边界情况：处理花坛首尾位置的约束检查
	 * 空间优化：可原地修改数组标记已种花位置
	 */
	public static boolean canPlaceFlowers(int[] flowerbed, int n) {
		if (flowerbed == null || flowerbed.length == 0) {
			return n == 0;
		}
		
		int count = 0; // 可以种的花的数量
		int len = flowerbed.length;
		
		// 贪心策略：遍历花坛，尽可能多地种花
		for (int i = 0; i < len; i++) {
			// 检查当前位置是否可以种花：当前位置为0，且前后都不是1
			boolean canPlant = flowerbed[i] == 0;
			if (i > 0) {
				canPlant = canPlant && (flowerbed[i - 1] == 0);
			}
			if (i < len - 1) {
				canPlant = canPlant && (flowerbed[i + 1] == 0);
			}
			
			if (canPlant) {
				flowerbed[i] = 1; // 在当前位置种花
				count++;
				
				// 如果已经能满足n朵花，提前返回
				if (count >= n) {
					return true;
				}
			}
		}
		
		return count >= n;
	}

	// 补充题目4: LeetCode 435. 无重叠区间 (Non-overlapping Intervals)
	// 题目描述: 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
	// 返回需要移除区间的最小数量，使剩余区间互不重叠。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、区间调度(Interval Scheduling)、排序(Sorting)
	// 时间复杂度: O(n*log(n))，其中n是区间数量
	// 空间复杂度: O(1)，仅使用常数额外空间
	// 链接: https://leetcode.cn/problems/non-overlapping-intervals/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：按区间结束位置排序，优先保留结束早的区间，
	 *    这样可以为后续区间留出更多空间
	 * 2. 区间选择：保留不重叠的区间，移除重叠的区间
	 * 3. 最优性保证：移除最少区间等价于保留最多区间
	 *
	 * 算法步骤详解：
	 * 1. 预处理：按区间结束位置升序排序
	 * 2. 选择过程：
	 *    - 维护上一个保留区间的结束位置
	 *    - 若当前区间与上一个保留区间不重叠则保留
	 *    - 更新上一个保留区间的结束位置
	 * 3. 结果计算：需移除数量 = 总数 - 保留数量
	 *
	 * 算法正确性证明：
	 * 贪心选择性质：选择结束位置最早的区间是最优的
	 * 最优子结构：确定保留区间后，剩余问题仍保持最优性
	 */
	public static int eraseOverlapIntervals(int[][] intervals) {
		if (intervals == null || intervals.length <= 1) {
			return 0;
		}
		
		// 按区间结束位置排序
		Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
		
		int count = 0;       // 保留的区间数量
		int end = Integer.MIN_VALUE; // 上一个保留的区间的结束位置
		
		// 贪心策略：优先保留结束早的区间
		for (int[] interval : intervals) {
			// 如果当前区间的开始位置大于等于上一个保留区间的结束位置，则不重叠
			if (interval[0] >= end) {
				count++;
				end = interval[1];
			}
			// 否则，该区间与上一个保留区间重叠，需要移除
		}
		
		// 需要移除的区间数量 = 总区间数量 - 保留的区间数量
		return intervals.length - count;
	}

	// 补充题目5: LeetCode 121. 买卖股票的最佳时机 (Best Time to Buy and Sell Stock)
	// 题目描述: 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
	// 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。
	// 设计一个算法来计算你所能获取的最大利润。
	// 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、单次交易(Single Transaction)、动态规划思想(DP Thinking)
	// 时间复杂度: O(n)，其中n是价格数组长度
	// 空间复杂度: O(1)，仅使用常数额外空间
	// 链接: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：记录到目前为止的最低价格，
	 *    每次计算当前价格卖出能获得的最大利润
	 * 2. 状态维护：同时维护最低价格和最大利润两个状态
	 * 3. 优化决策：仅在找到更低价格或更高利润时更新状态
	 *
	 * 算法步骤详解：
	 * 1. 初始化：设置最低价格为最大值，最大利润为0
	 * 2. 遍历过程：
	 *    - 若当前价格低于最低价格则更新最低价格
	 *    - 否则计算当前价格卖出的利润并更新最大利润
	 * 3. 结果返回：返回记录的最大利润
	 *
	 * 算法对比与拓展：
	 * 与II的区别：只能进行一次交易vs可进行多次交易
	 * 动态规划解法：可使用dp[i][0/1]表示第i天持有/不持有股票的最大利润
	 */
	public static int maxProfitSingle(int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int minPrice = Integer.MAX_VALUE; // 记录到目前为止的最低价格
		int maxProfit = 0;                // 记录最大利润
		
		// 贪心策略：每次记录到目前为止的最低价格，计算当前价格卖出能获得的最大利润
		for (int price : prices) {
			// 更新最低价格
			if (price < minPrice) {
				minPrice = price;
			}
			// 计算当前价格卖出能获得的利润，并更新最大利润
			else if (price - minPrice > maxProfit) {
				maxProfit = price - minPrice;
			}
		}
		
		return maxProfit;
	}
}