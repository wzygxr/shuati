package class089;

import java.util.HashMap;

/**
 * 吃掉N个橘子的最少天数 - 贪心算法 + 记忆化搜索解决方案
 * 
 * 题目描述：
 * 厨房里总共有 n 个橘子，你决定每一天选择如下方式之一吃这些橘子
 * 1）吃掉一个橘子
 * 2) 如果剩余橘子数 n 能被 2 整除，那么你可以吃掉 n/2 个橘子
 * 3) 如果剩余橘子数 n 能被 3 整除，那么你可以吃掉 2*(n/3) 个橘子
 * 每天你只能从以上 3 种方案中选择一种方案
 * 请你返回吃掉所有 n 个橘子的最少天数
 * 
 * 测试链接：https://leetcode.cn/problems/minimum-number-of-days-to-eat-n-oranges/
 * 
 * 算法思想：
 * 贪心算法 + 记忆化搜索（动态规划）
 * 1. 优先使用按比例吃橘子的方法（方法2和3），因为这样能更快减少橘子数量
 * 2. 对于每个n，考虑两种可能性：
 *    - 先吃到2的倍数，然后吃掉一半
 *    - 先吃到3的倍数，然后吃掉三分之二
 * 3. 选择天数最少的方法
 * 
 * 时间复杂度分析：
 * O(logn) - 由于每次递归都会将问题规模减半或减为三分之一
 * 
 * 空间复杂度分析：
 * O(logn) - 递归深度为logn，记忆化存储也需要O(logn)空间
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理n=0和n=1的特殊情况
 * 2. 记忆化优化：避免重复计算
 * 3. 递归深度：使用记忆化搜索控制递归深度
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 由于按比例吃橘子（方法2和3）能更快减少橘子数量，所以应该优先考虑这两种方法
 * 对于每个n，选择能最快减少橘子数量的方法
 */
public class Code03_MinimumNumberEatOranges {

	// 记忆化存储表，避免重复计算
	public static HashMap<Integer, Integer> dp = new HashMap<>();

	/**
	 * 计算吃掉n个橘子的最少天数
	 * 
	 * @param n 橘子数量
	 * @return 最少天数
	 * 
	 * 算法步骤：
	 * 1. 基础情况：n=0或1时，直接返回n
	 * 2. 如果已经计算过n，直接返回结果
	 * 3. 考虑两种贪心策略：
	 *    - 先吃到2的倍数，然后吃掉一半
	 *    - 先吃到3的倍数，然后吃掉三分之二
	 * 4. 选择天数最少的方法
	 * 
	 * 贪心策略解释：
	 * 方法2和3能更快减少橘子数量，所以应该优先考虑
	 * 但需要先通过方法1吃到合适的倍数
	 */
	public static int minDays(int n) {
		// 基础情况处理
		if (n <= 1) {
			return n;
		}
		
		// 记忆化检查：如果已经计算过，直接返回结果
		if (dp.containsKey(n)) {
			return dp.get(n);
		}
		
		// 贪心策略1：先吃到2的倍数，然后吃掉一半
		// 需要先吃掉 n % 2 个橘子（方法1），然后使用方法2吃掉一半
		// 总天数 = n % 2 + 1 + minDays(n / 2)
		int option1 = n % 2 + 1 + minDays(n / 2);
		
		// 贪心策略2：先吃到3的倍数，然后吃掉三分之二
		// 需要先吃掉 n % 3 个橘子（方法1），然后使用方法3吃掉三分之二
		// 总天数 = n % 3 + 1 + minDays(n / 3)
		int option2 = n % 3 + 1 + minDays(n / 3);
		
		// 选择天数最少的方法
		int ans = Math.min(option1, option2);
		
		// 存储结果到记忆化表
		dp.put(n, ans);
		return ans;
	}

	/**
	 * 调试版本：打印计算过程中的中间结果
	 * 
	 * @param n 橘子数量
	 * @return 最少天数
	 */
	public static int debugMinDays(int n) {
		System.out.println("计算吃掉 " + n + " 个橘子的最少天数:");
		
		if (n <= 1) {
			System.out.println("基础情况: n=" + n + ", 返回 " + n);
			return n;
		}
		
		if (dp.containsKey(n)) {
			int cached = dp.get(n);
			System.out.println("记忆化命中: n=" + n + ", 返回缓存结果 " + cached);
			return cached;
		}
		
		System.out.println("考虑两种策略:");
		
		// 策略1分析
		int remainder2 = n % 2;
		int half = n / 2;
		System.out.println("策略1 - 吃到2的倍数:");
		System.out.println("  需要先吃掉 " + remainder2 + " 个橘子");
		System.out.println("  然后吃掉一半 (" + half + " 个橘子)");
		int option1 = remainder2 + 1 + debugMinDays(half);
		System.out.println("  策略1总天数: " + remainder2 + " + 1 + minDays(" + half + ") = " + option1);
		
		// 策略2分析
		int remainder3 = n % 3;
		int third = n / 3;
		System.out.println("策略2 - 吃到3的倍数:");
		System.out.println("  需要先吃掉 " + remainder3 + " 个橘子");
		System.out.println("  然后吃掉三分之二 (" + third + " 个橘子)");
		int option2 = remainder3 + 1 + debugMinDays(third);
		System.out.println("  策略2总天数: " + remainder3 + " + 1 + minDays(" + third + ") = " + option2);
		
		int ans = Math.min(option1, option2);
		System.out.println("选择较小值: min(" + option1 + ", " + option2 + ") = " + ans);
		
		dp.put(n, ans);
		System.out.println("存储结果: dp[" + n + "] = " + ans);
		
		return ans;
	}

	/**
	 * 测试函数：验证吃橘子算法的正确性
	 */
	public static void testMinDays() {
		System.out.println("吃掉N个橘子的最少天数算法测试开始");
		System.out.println("=============================");
		
		// 清空记忆化表
		dp.clear();
		
		// 测试用例1: n = 10
		int result1 = minDays(10);
		System.out.println("输入: n = 10");
		System.out.println("输出: " + result1);
		System.out.println("预期: 4");
		System.out.println((result1 == 4 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 清空记忆化表
		dp.clear();
		
		// 测试用例2: n = 6
		int result2 = minDays(6);
		System.out.println("输入: n = 6");
		System.out.println("输出: " + result2);
		System.out.println("预期: 3");
		System.out.println((result2 == 3 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 清空记忆化表
		dp.clear();
		
		// 测试用例3: n = 1
		int result3 = minDays(1);
		System.out.println("输入: n = 1");
		System.out.println("输出: " + result3);
		System.out.println("预期: 1");
		System.out.println((result3 == 1 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		// 清空记忆化表
		dp.clear();
		
		// 测试用例4: n = 56
		int result4 = minDays(56);
		System.out.println("输入: n = 56");
		System.out.println("输出: " + result4);
		System.out.println("预期: 6");
		System.out.println((result4 == 6 ? "✓ 通过" : "✗ 失败"));
		System.out.println();
		
		System.out.println("测试结束");
	}

	/**
	 * 性能测试：测试算法在大规模数据下的表现
	 */
	public static void performanceTest() {
		System.out.println("性能测试开始");
		System.out.println("============");
		
		// 清空记忆化表
		dp.clear();
		
		long startTime = System.currentTimeMillis();
		int result = minDays(1000000);
		long endTime = System.currentTimeMillis();
		
		System.out.println("输入: n = 1000000");
		System.out.println("输出: " + result);
		System.out.println("执行时间: " + (endTime - startTime) + " 毫秒");
		System.out.println("性能测试结束");
	}

	/**
	 * 主函数：运行测试
	 */
	public static void main(String[] args) {
		System.out.println("吃掉N个橘子的最少天数 - 贪心算法 + 记忆化搜索解决方案");
		System.out.println("===========================================");
		
		// 运行基础测试
		testMinDays();
		
		System.out.println("调试模式示例:");
		// 清空记忆化表
		dp.clear();
		System.out.println("对 n = 10 进行调试跟踪:");
		int debugResult = debugMinDays(10);
		System.out.println("最终结果: " + debugResult);
		
		System.out.println("算法分析:");
		System.out.println("- 时间复杂度: O(logn) - 由于每次递归都会将问题规模减半或减为三分之一");
		System.out.println("- 空间复杂度: O(logn) - 递归深度为logn，记忆化存储也需要O(logn)空间");
		System.out.println("- 贪心策略: 优先使用按比例吃橘子的方法，能更快减少橘子数量");
		System.out.println("- 最优性: 这种贪心策略能够得到全局最优解");
		System.out.println("- 记忆化优化: 避免重复计算，提高算法效率");
		
		// 可选：运行性能测试
		// System.out.println("性能测试:");
		// performanceTest();
	}
}
