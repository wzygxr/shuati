package class077;

// 区间动态规划（Interval Dynamic Programming）综合题目实现
// 本文件包含多个经典区间DP问题的解决方案，涵盖不同平台的题目

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code07_BurstBalloons {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		String line = br.readLine();
		String[] strs = line.split(" ");
		int n = strs.length;
		int[] nums = new int[n];
		for (int i = 0; i < n; i++) {
			nums[i] = Integer.valueOf(strs[i]);
		}
		
		// 可以根据不同的题目修改这里的调用
		out.println(maxCoins(nums));
		out.flush();
		out.close();
		br.close();
	}

	// =============================================================================
	// 题目1: 戳气球（LeetCode 312）
	// 有 n 个气球，编号为0 到 n - 1，每个气球上都标有一个数字，这些数字存在数组 nums 中。
	// 现在要求你戳破所有的气球。戳破第 i 个气球，可以获得 nums[i - 1] * nums[i] * nums[i + 1] 枚硬币。
	// 这里的 i - 1 和 i + 1 代表和 i 相邻的两个气球的序号。如果 i - 1 或 i + 1 超出了数组的边界，
	// 那么就当它是一个数字为 1 的气球。
	// 求所能获得硬币的最大数量。
	// 测试链接 : https://leetcode.cn/problems/burst-balloons/
	// =============================================================================
	public static int maxCoins(int[] nums) {
		// 异常处理：空数组情况
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		int n = nums.length;
		
		// 创建新数组，在首尾添加值为1的虚拟气球，处理边界情况
		int[] val = new int[n + 2];
		val[0] = 1;
		val[n + 1] = 1;
		for (int i = 1; i <= n; i++) {
			val[i] = nums[i - 1];
		}
		
		// 状态定义：dp[i][j]表示戳破开区间(i,j)内所有气球能获得的最大硬币数
		int[][] dp = new int[n + 2][n + 2];
		
		// 枚举区间长度，从2开始（至少要有一个气球可以戳破）
		for (int len = 2; len <= n + 1; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n + 1 - len; i++) {
				// 计算区间终点j
				int j = i + len;
				// 枚举最后戳破的气球k
				for (int k = i + 1; k < j; k++) {
					// 状态转移方程：
					// 戳破k气球时，左右区间已经处理完毕，所以获得硬币数为val[i] * val[k] * val[j]
					dp[i][j] = Math.max(dp[i][j], 
						dp[i][k] + dp[k][j] + val[i] * val[k] * val[j]);
				}
			}
		}
		
		return dp[0][n + 1];
	}
	// 算法分析：
	// 时间复杂度：O(n^3)
	//   - 第一层循环枚举区间长度：O(n)
	//   - 第二层循环枚举区间起点：O(n)
	//   - 第三层循环枚举分割点：O(n)
	// 空间复杂度：O(n^2)
	//   - 二维dp数组占用空间：O(n^2)
	// 优化说明：
	//   - 该解法是最优解，因为问题规模为n时，区间DP的时间复杂度无法低于O(n^3)
	//   - 使用虚拟气球技巧简化边界处理

	// =============================================================================
	// 题目2: 分割回文串 II（LeetCode 132）
	// 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。
	// 返回符合要求的 最少分割次数 。
	// 测试链接: https://leetcode.cn/problems/palindrome-partitioning-ii/
	// =============================================================================
	public static int minCut(String s) {
		// 异常处理
		if (s == null || s.length() <= 1) {
			return 0; // 空字符串或单字符字符串不需要分割
		}
		
		int n = s.length();
		
		// 预处理：判断子串s[i...j]是否为回文串
		boolean[][] isPalindrome = new boolean[n][n];
		for (int i = n - 1; i >= 0; i--) {
			for (int j = i; j < n; j++) {
				if (s.charAt(i) == s.charAt(j) && (j - i <= 2 || isPalindrome[i + 1][j - 1])) {
					isPalindrome[i][j] = true;
				}
			}
		}
		
		// 状态定义：dp[i]表示字符串s[0...i]的最少分割次数
		int[] dp = new int[n];
		Arrays.fill(dp, Integer.MAX_VALUE);
		
		// 初始化：单个字符不需要分割
		for (int i = 0; i < n; i++) {
			if (isPalindrome[0][i]) {
				dp[i] = 0;
				continue;
			}
			// 状态转移：枚举最后一个分割点j
			for (int j = 0; j < i; j++) {
				if (isPalindrome[j + 1][i]) {
					dp[i] = Math.min(dp[i], dp[j] + 1);
				}
			}
		}
		
		return dp[n - 1];
	}
	// 算法分析：
	// 时间复杂度：O(n^2)
	//   - 预处理回文串：O(n^2)
	//   - 计算dp数组：O(n^2)
	// 空间复杂度：O(n^2)
	//   - isPalindrome数组：O(n^2)
	//   - dp数组：O(n)
	// 优化说明：
	//   - 该解法是最优解，时间复杂度无法进一步降低
	//   - 可以使用中心扩展法优化回文串预处理

	// =============================================================================
	// 题目3: 切棍子的最小成本（LeetCode 1547）
	// 有一根长度为n的木棍，我们需要把它切成k段。
	// 给定一个整数数组cuts，其中cuts[i]表示将木棍切开的位置。
	// 每次切割的成本是当前要切割的木棍的长度，求切完所有位置的最小总成本。
	// 测试链接: https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
	// =============================================================================
	public static int minCost(int n, int[] cuts) {
		// 异常处理
		if (cuts == null || cuts.length == 0) {
			return 0; // 不需要切割
		}
		
		// 对切割点进行排序
		Arrays.sort(cuts);
		
		// 构造新的切割点数组，包含0和n
		int m = cuts.length + 2;
		int[] points = new int[m];
		points[0] = 0;
		points[m - 1] = n;
		for (int i = 1; i < m - 1; i++) {
			points[i] = cuts[i - 1];
		}
		
		// 状态定义：dp[i][j]表示切割points[i]到points[j]之间的木棍的最小成本
		int[][] dp = new int[m][m];
		
		// 枚举区间长度，从2开始（至少有两个点）
		for (int len = 2; len < m; len++) {
			// 枚举区间起点i
			for (int i = 0; i + len < m; i++) {
				int j = i + len;
				// 初始化dp[i][j]为较大值
				dp[i][j] = Integer.MAX_VALUE;
				// 当前木棍的长度
				int cost = points[j] - points[i];
				// 枚举分割点k
				for (int k = i + 1; k < j; k++) {
					dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + cost);
				}
			}
		}
		
		return dp[0][m - 1];
	}
	// 算法分析：
	// 时间复杂度：O(m^3)，其中m是切割点数量+2
	// 空间复杂度：O(m^2)，用于存储dp数组
	// 优化说明：
	//   - 该解法是最优解，因为问题规模为m时，区间DP的时间复杂度无法低于O(m^3)
	//   - 可以使用四边形不等式优化到O(m^2)

	// =============================================================================
	// 题目4: 多边形三角剖分的最低得分（LeetCode 1039）
	// 给定一个凸多边形，顶点按顺时针顺序标记为A[0], A[1], ..., A[n-1]。
	// 对于每个三角形，计算三个顶点标记的乘积，然后将所有三角形的乘积相加。
	// 返回所有可能的三角剖分中，分数最低的那个。
	// 测试链接: https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
	// =============================================================================
	public static int minScoreTriangulation(int[] values) {
		// 异常处理
		if (values == null || values.length < 3) {
			return 0; // 无法形成三角形
		}
		
		int n = values.length;
		
		// 状态定义：dp[i][j]表示顶点i到j构成的多边形的最小三角剖分得分
		int[][] dp = new int[n][n];
		
		// 枚举区间长度，从3开始（至少需要3个点才能形成三角形）
		for (int len = 3; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;
				// 初始化dp[i][j]为较大值
				dp[i][j] = Integer.MAX_VALUE;
				// 枚举中间点k，将多边形分为三角形i-j-k和两个子多边形
				for (int k = i + 1; k < j; k++) {
					dp[i][j] = Math.min(dp[i][j], 
						dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]);
				}
			}
		}
		
		return dp[0][n - 1];
	}
	// 算法分析：
	// 时间复杂度：O(n^3)，其中n是顶点数量
	// 空间复杂度：O(n^2)，用于存储dp数组
	// 优化说明：
	//   - 该解法是最优解，因为问题规模为n时，区间DP的时间复杂度无法低于O(n^3)
	//   - 可以使用四边形不等式优化到O(n^2)

	// =============================================================================
	// 区间动态规划解题技巧总结
	// =============================================================================
	/*
	 * 1. 题型识别方法：
	 *    - 涉及区间最优解问题，如最大值、最小值
	 *    - 问题可以分解为子区间的最优解
	 *    - 需要枚举分割点将大区间分解为小区间
	 * 
	 * 2. 状态设计模式：
	 *    - 通常定义dp[i][j]表示区间[i,j]的最优解
	 *    - 根据具体问题调整状态含义
	 * 
	 * 3. 填表顺序：
	 *    - 按区间长度从小到大枚举
	 *    - 长度为1的区间通常可以直接初始化
	 *    - 长度大于1的区间通过分割点由小区间组合而来
	 * 
	 * 4. 优化技巧：
	 *    - 预处理：提前计算辅助信息（如回文判断）
	 *    - 空间压缩：某些问题可以优化空间复杂度
	 *    - 剪枝：利用问题特性减少不必要的计算
	 * 
	 * 5. 工程化考量：
	 *    - 异常处理：检查输入合法性，处理边界情况
	 *    - 边界条件：正确初始化长度为1的区间
	 *    - 性能优化：使用前缀和等技术减少重复计算
	 */
}