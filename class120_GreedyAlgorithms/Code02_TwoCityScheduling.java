package class089;

import java.util.Arrays;

/**
 * 两地调度 - 贪心算法解决方案
 * 
 * 题目描述：
 * 公司计划面试2n个人，给定一个数组 costs，其中costs[i]=[aCosti, bCosti]
 * 表示第i人飞往a市的费用为aCosti，飞往b市的费用为bCosti
 * 返回将每个人都飞到a、b中某座城市的最低费用，要求每个城市都有n人抵达
 * 
 * 测试链接：https://leetcode.cn/problems/two-city-scheduling/
 * 
 * 算法思想：
 * 贪心算法 + 差值排序
 * 1. 假设所有人都先去A市，总费用为所有aCosti之和
 * 2. 计算每个人从A市改去B市的费用变化：bCosti - aCosti
 * 3. 选择费用变化最小的n个人改去B市
 * 4. 总费用 = 所有aCosti之和 + 最小的n个费用变化值之和
 * 
 * 时间复杂度分析：
 * O(n*logn) - 主要时间消耗在排序上
 * 
 * 空间复杂度分析：
 * O(n) - 需要额外的数组存储费用变化值
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、奇数人数等特殊情况
 * 2. 输入验证：检查输入参数的有效性
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 对于每个人，选择去A市还是B市取决于bCosti - aCosti的值
 * 如果这个值很小（负数），说明去B市比去A市便宜很多，应该优先选择去B市
 * 通过排序选择最小的n个差值，可以保证总费用最小
 */
public class Code02_TwoCityScheduling {

	/**
	 * 计算两地调度的最低费用
	 * 
	 * @param costs 费用数组，每个元素是[aCosti, bCosti]
	 * @return 最低总费用
	 * 
	 * 算法步骤：
	 * 1. 假设所有人都先去A市，计算总费用
	 * 2. 计算每个人从A市改去B市的费用变化
	 * 3. 对费用变化进行排序
	 * 4. 选择最小的n个费用变化值加到总费用中
	 * 
	 * 数学原理：
	 * 总费用 = ΣaCosti + Σ(bCosti - aCosti) = ΣaCosti + ΣbCosti - ΣaCosti = ΣbCosti
	 * 但这里只选择最小的n个差值，所以实际上是部分人去B市
	 */
	public static int twoCitySchedCost(int[][] costs) {
		// 输入验证
		if (costs == null || costs.length == 0) {
			return 0;
		}
		
		int n = costs.length;
		// 检查人数是否为偶数
		if (n % 2 != 0) {
			throw new IllegalArgumentException("人数必须为偶数");
		}
		
		int[] diff = new int[n];  // 存储每个人从A市改去B市的费用变化
		int sumA = 0;             // 所有人都去A市的总费用
		
		// 计算费用变化和A市总费用
		for (int i = 0; i < n; i++) {
			sumA += costs[i][0];              // 累加A市费用
			diff[i] = costs[i][1] - costs[i][0]; // 计算费用变化
		}
		
		// 对费用变化进行排序（升序）
		Arrays.sort(diff);
		
		int m = n / 2; // 每个城市需要的人数
		// 选择最小的m个费用变化值
		for (int i = 0; i < m; i++) {
			sumA += diff[i];
		}
		
		return sumA;
	}

	/**
	 * 调试版本：打印计算过程中的中间结果
	 * 
	 * @param costs 费用数组
	 * @return 最低总费用
	 */
	public static int debugTwoCitySchedCost(int[][] costs) {
		if (costs == null || costs.length == 0) {
			return 0;
		}
		
		int n = costs.length;
		if (n % 2 != 0) {
			throw new IllegalArgumentException("人数必须为偶数");
		}
		
		int[] diff = new int[n];
		int sumA = 0;
		
		System.out.println("原始费用数据:");
		System.out.println("序号\tA市费用\tB市费用\t费用变化(B-A)");
		for (int i = 0; i < n; i++) {
			int aCost = costs[i][0];
			int bCost = costs[i][1];
			int change = bCost - aCost;
			diff[i] = change;
			sumA += aCost;
			System.out.printf("%d\t%d\t%d\t%d\n", i, aCost, bCost, change);
		}
		
		System.out.println("所有人都去A市的总费用: " + sumA);
		
		// 打印排序前的费用变化
		System.out.println("排序前的费用变化数组: " + Arrays.toString(diff));
		
		Arrays.sort(diff);
		System.out.println("排序后的费用变化数组: " + Arrays.toString(diff));
		
		int m = n / 2;
		System.out.println("每个城市需要的人数: " + m);
		System.out.println("选择最小的" + m + "个费用变化值:");
		
		int changeSum = 0;
		for (int i = 0; i < m; i++) {
			changeSum += diff[i];
			System.out.println("选择第" + (i+1) + "个变化值: " + diff[i] + ", 累计变化: " + changeSum);
		}
		
		int totalCost = sumA + changeSum;
		System.out.println("最终总费用: " + totalCost);
		
		return totalCost;
	}

	/**
	 * 测试函数：验证两地调度算法的正确性
	 */
	public static void testTwoCitySchedCost() {
		System.out.println("两地调度算法测试开始");
		System.out.println("====================");
		
		// 测试用例1: [[10,20],[30,200],[400,50],[30,20]]
		int[][] costs1 = {{10,20}, {30,200}, {400,50}, {30,20}};
		int result1 = twoCitySchedCost(costs1);
		System.out.println("输入: [[10,20],[30,200],[400,50],[30,20]]");
		System.out.println("输出: " + result1);
		System.out.println("预期: 110");
		System.out.println((result1 == 110 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例2: [[259,770],[448,54],[926,667],[184,139],[840,118],[577,469]]
		int[][] costs2 = {{259,770}, {448,54}, {926,667}, {184,139}, {840,118}, {577,469}};
		int result2 = twoCitySchedCost(costs2);
		System.out.println("输入: [[259,770],[448,54],[926,667],[184,139],[840,118],[577,469]]");
		System.out.println("输出: " + result2);
		System.out.println("预期: 1859");
		System.out.println((result2 == 1859 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 测试用例3: [[515,563],[451,713],[537,709],[343,819],[855,779],[457,60],[650,359],[631,42]]
		int[][] costs3 = {{515,563}, {451,713}, {537,709}, {343,819}, {855,779}, {457,60}, {650,359}, {631,42}};
		int result3 = twoCitySchedCost(costs3);
		System.out.println("输入: 8个人的复杂测试用例");
		System.out.println("输出: " + result3);
		System.out.println("预期: 3086");
		System.out.println((result3 == 3086 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		System.out.println("测试结束");
	}

	/**
	 * 主函数：运行测试
	 */
	public static void main(String[] args) {
		System.out.println("两地调度 - 贪心算法解决方案");
		System.out.println("==========================");
		
		// 运行基础测试
		testTwoCitySchedCost();
		
		System.out.println("调试模式示例:");
		int[][] debugCosts = {{10,20}, {30,200}, {400,50}, {30,20}};
		System.out.println("对测试用例 [[10,20],[30,200],[400,50],[30,20]] 进行调试跟踪:");
		int debugResult = debugTwoCitySchedCost(debugCosts);
		System.out.println("最终结果: " + debugResult);
		
		System.out.println("算法分析:");
		System.out.println("- 时间复杂度: O(n*logn) - 主要时间消耗在排序上");
		System.out.println("- 空间复杂度: O(n) - 需要额外的数组存储费用变化值");
		System.out.println("- 贪心策略: 假设所有人都去A市，然后选择费用变化最小的n个人改去B市");
		System.out.println("- 最优性: 这种贪心策略能够得到全局最优解");
		System.out.println("- 数学原理: 总费用 = ΣaCosti + min_n(bCosti - aCosti)");
	}
}
