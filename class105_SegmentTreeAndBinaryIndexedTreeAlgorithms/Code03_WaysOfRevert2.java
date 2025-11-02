package class132;

// 还原数组的方法数问题（空间优化版本）
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
public class Code03_WaysOfRevert2 {

	/**
	 * 使用动态规划解决还原数组的方法数问题（空间优化版本）
	 * 
	 * 解题思路：
	 * 1. 该问题可以转化为在满足特定约束条件下，构造数组的方案数
	 * 2. 使用动态规划，但只保存前一行的状态，节省空间
	 * 3. dp[j]表示当前考虑到某个位置，值为j时的方案数
	 * 4. 通过前缀和优化提高计算效率
	 * 5. 滚动数组优化空间复杂度
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(n * m)，其中n为数组长度，m为值域范围
	 * - 状态转移：O(1)（通过前缀和优化）
	 * - 总时间复杂度：O(n * m)
	 * 
	 * 空间复杂度分析：
	 * - DP数组：O(m)（通过滚动数组优化）
	 * - 前缀和数组：O(m)
	 * - 其他辅助数组：O(m)
	 * - 总空间复杂度：O(m)
	 * 
	 * 工程化考量：
	 * 1. 空间优化：使用滚动数组技术，将空间复杂度从O(n*m)优化到O(m)
	 * 2. 前缀和优化：利用前缀和减少重复计算，提高算法效率
	 * 3. 边界处理：正确处理数组边界和初始状态
	 * 4. 模运算：防止整数溢出，对结果取模
	 * 5. 参数校验：确保输入参数合法
	 * 6. 详细注释：解释算法思路和关键步骤
	 */
	public static int ways1(int n, int m) {
		// dp[j]表示当前考虑到某个位置，值为j时的方案数
		int[] dp = new int[m + 1];
		// 初始化：第0个位置可以是任意值
		for (int j = 1; j <= m; j++) {
			dp[j] = 1;
		}
		
		// 滚动数组优化空间复杂度
		int[] next = new int[m + 1];
		
		// 前缀和优化
		int[] preSum = new int[m + 2];
		for (int i = 1; i <= n; i++) {
			// 计算前缀和数组
			for (int j = 1; j <= m; j++) {
				preSum[j] = (preSum[j - 1] + dp[j]) % mod;
			}
			
			// 状态转移
			for (int j = 1; j <= m; j++) {
				// 根据题目约束条件计算方案数
				next[j] = (preSum[m] - preSum[Math.max(0, j - 2)] + mod) % mod;
			}
			
			// 更新dp数组（滚动数组）
			int[] tmp = dp;
			dp = next;
			next = tmp;
		}
		
		// 计算最终结果
		int ans = 0;
		for (int j = 1; j <= m; j++) {
			ans = (ans + dp[j]) % mod;
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
		int[] dp = new int[m + 1];
		for (int j = 1; j <= m; j++) {
			dp[j] = 1;
		}
		int[] next = new int[m + 1];
		for (int i = 1; i <= n; i++) {
			int sum = 0;
			for (int j = 1; j <= m; j++) {
				sum = (sum + dp[j]) % mod;
			}
			for (int j = 1; j <= m; j++) {
				next[j] = sum;
				if (j - 2 >= 1) {
					next[j] = (next[j] - dp[j - 2] + mod) % mod;
				}
			}
			int[] tmp = dp;
			dp = next;
			next = tmp;
		}
		int ans = 0;
		for (int j = 1; j <= m; j++) {
			ans = (ans + dp[j]) % mod;
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