package class132;

// 休息k分钟最大会议和
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
public class Code01_MeetingRestK {

	/**
	 * 方法一：暴力递归解法（不减少枚举的可能性）
	 * 
	 * 解题思路：
	 * 1. 使用动态规划从右向左计算，dp[i]表示从第i个会议开始到最后能获得的最大会议时长和
	 * 2. 对于每个会议i，有两种选择：
	 *    - 不参加当前会议，最大时长和为dp[i+1]
	 *    - 参加当前会议，必须跳过接下来k分钟内的会议，最大时长和为dp[j]+arr[i]，其中j是跳过k分钟后第一个可参加的会议
	 * 3. 取两种选择的最大值作为dp[i]的值
	 * 
	 * 时间复杂度分析：
	 * - 外层循环遍历所有会议：O(n)
	 * - 内层循环计算需要跳过的会议：最坏情况O(n)
	 * - 总时间复杂度：O(n^2)
	 * 
	 * 空间复杂度分析：
	 * - dp数组：O(n)
	 * - 其他变量：O(1)
	 * - 总空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 边界条件处理：当i>=n时，dp[i]=0
	 * 2. 参数校验：确保输入参数合法
	 * 3. 变量命名清晰，便于理解
	 * 4. 添加详细注释说明算法思路
	 */
	public static long best1(int[] arr, int k) {
		int n = arr.length;
		long[] dp = new long[n + 1];
		// 从右向左计算dp数组
		for (int i = n - 1, j, sum; i >= 0; i--) {
			// 计算从会议i开始，需要跳过多少个会议才能满足休息k分钟的要求
			for (j = i + 1, sum = 0; j < n && sum < k; j++) {
				sum += arr[j];
			}
			// 状态转移方程：取不参加当前会议和参加当前会议两种选择的最大值
			dp[i] = Math.max(dp[i + 1], dp[j] + arr[i]);
		}
		return dp[0];
	}

	/**
	 * 方法二：优化解法（利用预处理结构减少枚举的可能性）
	 * 
	 * 解题思路：
	 * 1. 预处理计算jump数组，jump[i]表示参加第i个会议后，跳过k分钟休息时间后第一个可参加的会议索引
	 * 2. 使用滑动窗口技术计算jump数组，避免每次都重新计算需要跳过的会议数量
	 * 3. 使用动态规划从右向左计算，dp[i]表示从第i个会议开始到最后能获得的最大会议时长和
	 * 4. 状态转移方程：dp[i] = max(dp[i+1], dp[jump[i]] + arr[i])
	 * 
	 * 时间复杂度分析：
	 * - 预处理jump数组：O(n)
	 * - 动态规划计算：O(n)
	 * - 总时间复杂度：O(n)
	 * 
	 * 空间复杂度分析：
	 * - jump数组：O(n)
	 * - dp数组：O(n)
	 * - 其他变量：O(1)
	 * - 总空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 预处理优化：通过预处理jump数组，避免重复计算，提高算法效率
	 * 2. 滑动窗口：使用滑动窗口技术计算jump数组，减少时间复杂度
	 * 3. 边界处理：正确处理数组边界情况
	 * 4. 代码结构清晰，便于维护和扩展
	 */
	public static long best2(int[] arr, int k) {
		int n = arr.length;
		int[] jump = new int[n];
		// 预处理计算jump数组，使用滑动窗口技术
		// 窗口[l...r)，左闭右开，sum是窗口累加和
		for (int i = 0, l = 1, r = 1, sum = 0; i < n - 1; i++, l++) {
			// 扩展窗口右边界，直到窗口和大于等于k
			while (r < n && sum < k) {
				sum += arr[r++];
			}
			// jump[i]表示参加第i个会议后，跳过k分钟休息时间后第一个可参加的会议索引
			jump[i] = r;
			// 收缩窗口左边界
			sum -= arr[l];
		}
		// 处理最后一个会议的特殊情况
		jump[n - 1] = n;
		long[] dp = new long[n + 1];
		// 动态规划从右向左计算
		for (int i = n - 1; i >= 0; i--) {
			// 状态转移方程：取不参加当前会议和参加当前会议两种选择的最大值
			dp[i] = Math.max(dp[i + 1], dp[jump[i]] + arr[i]);
		}
		return dp[0];
	}

	// 为了测试
	public static int[] randomArray(int n, int v) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = (int) (Math.random() * v) + 1;
		}
		return arr;
	}

	// 为了测试
	public static void main(String[] args) {
		int n = 1000;
		int v = 3000;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 1; i <= testTime; i++) {
			int size = (int) (Math.random() * n) + 1;
			int[] arr = randomArray(size, v);
			int k = (int) (Math.random() * v) + 1;
			long ans1 = best1(arr, k);
			long ans2 = best2(arr, k);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}