import java.util.Arrays;

/**
 * 不相交的线（Uncrossed Lines）
 * 在两条独立的水平线上按给定的顺序写下 nums1 和 nums2 中的整数。
 * 现在，可以绘制一些连接两个数字 nums1[i] 和 nums2[j] 的直线，这些直线需要同时满足：
 * nums1[i] == nums2[j]
 * 且绘制的直线不与任何其他连线（非水平线）相交。
 * 请注意，连线即使在端点也不能相交：每个数字只能属于一条连线。
 * 以这种方法绘制线条，并返回可以绘制的最大连线数。
 * 
 * 题目来源：LeetCode 1035. 不相交的线
 * 测试链接：https://leetcode.cn/problems/uncrossed-lines/
 * 
 * 算法核心思想：
 * 使用动态规划解决不相交的线问题，本质上是求两个数组的最长公共子序列
 * 通过构建二维DP表来计算最大连线数
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n*m)，其中n为nums1的长度，m为nums2的长度
 * - 空间优化版本：O(n*m)时间，O(m)空间
 * 
 * 空间复杂度分析：
 * - 基础版本：O(n*m)
 * - 空间优化版本：O(m)
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(min(n,m))
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理空数组和极端情况
 * 3. 性能优化：使用滚动数组减少空间占用
 * 4. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 图论：平面图和交叉数问题
 * - 生物信息学：序列比对和基因分析
 * - 电路设计：布线和交叉避免
 */
public class Code10_UncrossedLines {

	/*
	 * 不相交的线 - 动态规划解法
	 * 使用动态规划解决不相交的线问题
	 * dp[i][j] 表示nums1的前i个数字和nums2的前j个数字能绘制的最大连线数
	 * 
	 * 状态转移方程：
	 * 如果 nums1[i-1] == nums2[j-1]：
	 *   dp[i][j] = dp[i-1][j-1] + 1
	 * 否则：
	 *   dp[i][j] = max(dp[i-1][j], dp[i][j-1])
	 * 
	 * 解释：
	 * 当当前数字相等时，可以连线，结果等于前面子数组的最大连线数加1
	 * 当当前数字不相等时，不能连线，取两种情况的最大值：
	 *   1. 不使用nums1[i-1]数字的情况：dp[i-1][j]
	 *   2. 不使用nums2[j-1]数字的情况：dp[i][j-1]
	 * 
	 * 边界条件：
	 * dp[0][j] = 0，表示nums1为空时无法连线
	 * dp[i][0] = 0，表示nums2为空时无法连线
	 * 
	 * 时间复杂度：O(n*m)，其中n为nums1的长度，m为nums2的长度
	 * 空间复杂度：O(n*m)
	 */
	public static int maxUncrossedLines(int[] nums1, int[] nums2) {
		// 输入验证
		if (nums1 == null || nums2 == null) {
			throw new IllegalArgumentException("输入数组不能为null");
		}
		
		int n = nums1.length;
		int m = nums2.length;
		
		// 边界情况处理
		if (n == 0 || m == 0) return 0;
		
		// dp[i][j] 表示nums1的前i个数字和nums2的前j个数字能绘制的最大连线数
		int[][] dp = new int[n + 1][m + 1];
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (nums1[i - 1] == nums2[j - 1]) {
					// 当前数字相等，可以连线
					// 连线数等于前面子数组的最大连线数加1
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					// 当前数字不相等，不能连线，取两种情况的最大值
					// 不使用nums1[i-1]数字的情况：dp[i-1][j]
					// 不使用nums2[j-1]数字的情况：dp[i][j-1]
					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}
		
		return dp[n][m];
	}

	/*
	 * 空间优化版本
	 * 使用滚动数组优化空间复杂度
	 * 
	 * 时间复杂度：O(n*m)
	 * 空间复杂度：O(m)
	 */
	public static int maxUncrossedLinesOptimized(int[] nums1, int[] nums2) {
		// 输入验证
		if (nums1 == null || nums2 == null) {
			throw new IllegalArgumentException("输入数组不能为null");
		}
		
		int n = nums1.length;
		int m = nums2.length;
		
		// 边界情况处理
		if (n == 0 || m == 0) return 0;
		
		// 只需要一行数组
		int[] dp = new int[m + 1];
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			int prev = dp[0];  // 保存dp[i-1][j-1]的值
			
			for (int j = 1; j <= m; j++) {
				int temp = dp[j];  // 保存当前dp[j]的值，用于下一次循环
				
				if (nums1[i - 1] == nums2[j - 1]) {
					// 当前数字相等，可以连线
					// 连线数等于前面子数组的最大连线数加1
					dp[j] = prev + 1;
				} else {
					// 当前数字不相等，不能连线，取两种情况的最大值
					// 不使用nums1[i-1]数字的情况：dp[i-1][j] 对应 temp
					// 不使用nums2[j-1]数字的情况：dp[i][j-1] 对应 dp[j-1]
					dp[j] = Math.max(temp, dp[j - 1]);
				}
				
				prev = temp;  // 更新prev为原来的dp[j]值
			}
		}
		
		return dp[m];
	}

	/**
	 * 测试函数
	 */
	// 测试函数
	public static void main(String[] args) {
		// 测试用例
		int[][][] testCases = {
			{{1,4,2}, {1,2,4}},     // 2
			{{2,5,1,2,5}, {10,5,2,1,5,2}}, // 3
			{{1,3,7,1,7,5}, {1,9,2,5,1}}, // 2
			{{1,2,3}, {4,5,6}},     // 0
			{{1,2,3}, {1,2,3}}      // 3
		};
		
		System.out.println("不相交的线测试:");
		for (int[][] testCase : testCases) {
			int[] nums1 = testCase[0];
			int[] nums2 = testCase[1];
			int result1 = maxUncrossedLines(nums1, nums2);
			int result2 = maxUncrossedLinesOptimized(nums1, nums2);
			System.out.printf("nums1=%s, nums2=%s => %d (optimized: %d)\n", 
				Arrays.toString(nums1), Arrays.toString(nums2), result1, result2);
		}
	}
}