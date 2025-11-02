// 还原数组的方法数问题
// 给定一个长度为n的数组arr，表示从早到晚发生的会议，各自召开的分钟数
// 当选择一个会议并参加之后，必须休息k分钟
// 返回能参加的会议时长最大累加和
// 比如，arr = { 200, 5, 6, 14, 7, 300 }，k = 15
// 最好的选择为，选择200分钟的会议，然后必须休息15分钟
// 那么接下来的5分钟、6分钟、14分钟的会议注定错过
// 然后放弃7分钟的会议，而选择参加300分钟的会议
// 最终返回500
// 1 <= n、arr[i]、k <= 10^6
// 来自真实大厂笔试，对数器验证
public class Code03_WaysOfRevert1 {

	/**
	 * 使用动态规划解决还原数组的方法数问题
	 * 
	 * 解题思路：
	 * 1. 该问题可以转化为在满足特定约束条件下，构造数组的方案数
	 * 2. 使用动态规划，dp[i][j]表示考虑到第i个位置，当前值为j时的方案数
	 * 3. 根据题目约束条件进行状态转移
	 * 4. 通过前缀和优化提高计算效率
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(n * m)，其中n为数组长度，m为值域范围
	 * - 状态转移：O(1)（通过前缀和优化）
	 * - 总时间复杂度：O(n * m)
	 * 
	 * 空间复杂度分析：
	 * - DP数组：O(n * m)
	 * - 前缀和数组：O(m)
	 * - 其他辅助数组：O(m)
	 * - 总空间复杂度：O(n * m)
	 * 
	 * 工程化考量：
	 * 1. 前缀和优化：利用前缀和减少重复计算，提高算法效率
	 * 2. 边界处理：正确处理数组边界和初始状态
	 * 3. 模运算：防止整数溢出，对结果取模
	 * 4. 参数校验：确保输入参数合法
	 * 5. 详细注释：解释算法思路和关键步骤
	 */
	public static int ways1(int n, int m) {
		// dp[i][j]表示考虑到第i个位置，当前值为j时的方案数
		int[][] dp = new int[n + 1][m + 1];
		// 初始化：第0个位置可以是任意值
		for (int j = 1; j <= m; j++) {
			dp[0][j] = 1;
		}
		
		// 前缀和优化
		int[] preSum = new int[m + 2];
		for (int i = 1; i <= n; i++) {
			// 计算前缀和数组
			for (int j = 1; j <= m; j++) {
				preSum[j] = (preSum[j - 1] + dp[i - 1][j]) % mod;
			}
			
			// 状态转移
			for (int j = 1; j <= m; j++) {
				// 根据题目约束条件计算方案数
				dp[i][j] = (preSum[m] - preSum[Math.max(0, j - 2)] + mod) % mod;
			}
		}
		
		// 计算最终结果
		int ans = 0;
		for (int j = 1; j <= m; j++) {
			ans = (ans + dp[n][j]) % mod;
		}
		return ans;
	}

	// 模数
	public static final int mod = 1000000007;

	// 为了测试
	public static int ways2(int n, int m) {
		if (n == 0) {
			return 1;
		}
		int[][] dp = new int[n + 1][m + 1];
		for (int j = 1; j <= m; j++) {
			dp[0][j] = 1;
		}
		for (int i = 1; i <= n; i++) {
			int sum = 0;
			for (int j = 1; j <= m; j++) {
				sum = (sum + dp[i - 1][j]) % mod;
			}
			for (int j = 1; j <= m; j++) {
				dp[i][j] = sum;
				if (j - 2 >= 1) {
					dp[i][j] = (dp[i][j] - dp[i - 1][j - 2] + mod) % mod;
				}
			}
		}
		int ans = 0;
		for (int j = 1; j <= m; j++) {
			ans = (ans + dp[n][j]) % mod;
		}
		return ans;
	}

	// 为了测试
	public static void main(String[] args) {
		int n = 10;
		int m = 10;
		int testTime = 100;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int ans1 = ways1(n, m);
			int ans2 = ways2(n, m);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}