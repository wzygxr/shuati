package class128;

/**
 * 最好的部署问题
 * 
 * 问题描述：
 * - 一共有n台机器，编号1 ~ n，所有机器排成一排
 * - 每台机器必须部署，但可以决定部署顺序
 * - 部署时的收益取决于该机器相邻已部署机器的数量：
 *   * no[i]：部署i号机器时，相邻没有已部署机器的收益
 *   * one[i]：部署i号机器时，相邻有一台已部署机器的收益
 *   * both[i]：部署i号机器时，相邻有两台已部署机器的收益
 * - 注意：第1号和第n号机器最多只有一个相邻机器
 * - 目标：找到部署顺序，使得总收益最大
 * 
 * 约束条件：
 * - 1 <= n <= 10^5
 * - 0 <= no[i], one[i], both[i] <= 10^9
 * 
 * 算法思路：
 * 1. 区间DP解法（时间复杂度O(n^3)，不推荐）
 *    - 定义dp[l][r]：部署区间[l,r]内的所有机器的最大收益
 *    - 递归地考虑选择部署区间内的哪一台机器作为当前部署的机器
 *    - 对于部署机器i，它在区间中的位置决定了它能获得的收益：
 *      * 如果i是区间的左端点：获得one[i]收益
 *      * 如果i是区间的右端点：获得one[i]收益
 *      * 如果i是区间的中间点：获得both[i]收益
 *      * 然后递归求解剩余区间的最大收益
 * 
 * 2. 线性DP解法（时间复杂度O(n)，推荐）
 *    - 定义状态dp[i][0/1]：
 *      * dp[i][0]：在i号机器的前一台机器没有部署的情况下，部署i...n号机器能获得的最大收益
 *      * dp[i][1]：在i号机器的前一台机器已经部署的情况下，部署i...n号机器能获得的最大收益
 *    - 状态转移方程：
 *      * dp[i][0] = max(no[i] + dp[i+1][1], one[i] + dp[i+1][0])
 *        解释：当前机器前没有机器部署，那么部署后有两种选择：
 *        * 立即部署下一台机器（获得no[i]收益，下一台机器前有机器已部署）
 *        * 先部署后面的机器（获得one[i]收益，下一台机器前没有机器已部署）
 *      * dp[i][1] = max(one[i] + dp[i+1][1], both[i] + dp[i+1][0])
 *        解释：当前机器前有一台机器部署，那么部署后有两种选择：
 *        * 立即部署下一台机器（获得one[i]收益，下一台机器前有机器已部署）
 *        * 先部署后面的机器（获得both[i]收益，下一台机器前没有机器已部署）
 *    - 边界条件：
 *      * dp[n][0] = no[n]  // 最后一台机器前没有机器部署时的收益
 *      * dp[n][1] = one[n]  // 最后一台机器前有一台机器部署时的收益
 *    - 最终结果：dp[1][0]  // 从第一台机器开始，且其前没有机器部署
 * 
 * 时间复杂度对比：
 * - 区间DP解法：O(n^3)
 * - 线性DP解法：O(n)
 * 
 * 输入输出示例：
 * 输入：
 * n = 3
 * no = [0, 5, 3, 4]  // 索引0不使用
 * one = [0, 4, 5, 3]
 * both = [0, 0, 2, 0]
 * 输出：14
 * 解释：最优部署顺序是3 → 1 → 2，总收益为4 + 5 + 5 = 14
 * 
 * 来自真实大厂笔试，已通过对数器验证
 */
public class Code02_BestDeploy {

	public static int MAXN = 1001;

	public static int[] no = new int[MAXN];

	public static int[] one = new int[MAXN];

	public static int[] both = new int[MAXN];

	public static int n;

	/**
	 * 区间DP解法
	 * 
	 * 时间复杂度：O(n^3) - 不推荐用于大规模数据
	 * 空间复杂度：O(n^2)
	 * 
	 * @return 部署所有机器的最大收益
	 */
	public static int best1() {
		int[][] dp = new int[n + 1][n + 1];
		// 初始化DP数组为-1，表示未计算
		for (int l = 1; l <= n; l++) {
			for (int r = l; r <= n; r++) {
				dp[l][r] = -1;
			}
		}
		// 递归计算区间[1,n]的最大收益
		return f(1, n, dp);
	}

	/**
	 * 递归函数：计算部署区间[l,r]内所有机器的最大收益
	 * 假设l-1和r+1的机器都没有部署
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param dp 记忆化数组
	 * @return 部署区间[l,r]内机器的最大收益
	 */
	public static int f(int l, int r, int[][] dp) {
		// 基本情况：区间只有一台机器
		if (l == r) {
			return no[l];
		}
		// 检查是否已经计算过
		if (dp[l][r] != -1) {
			return dp[l][r];
		}
		// 选择部署左端点机器
		int ans = f(l + 1, r, dp) + one[l];
		// 选择部署右端点机器
		ans = Math.max(ans, f(l, r - 1, dp) + one[r]);
		// 尝试选择部署中间的每一台机器
		for (int i = l + 1; i < r; i++) {
			// 部署i后，区间分成左右两部分，i获得both[i]收益
			ans = Math.max(ans, f(l, i - 1, dp) + f(i + 1, r, dp) + both[i]);
		}
		// 记忆结果并返回
		dp[l][r] = ans;
		return ans;
	}

	/**
	 * 线性DP解法（推荐）
	 * 
	 * 时间复杂度：O(n) - 只需要一次线性遍历
	 * 空间复杂度：O(n) - 需要一个二维DP数组
	 * 
	 * @return 部署所有机器的最大收益
	 */
	public static int best2() {
		// dp[i][0] : i号机器的前一台机器没有部署的情况下，部署i...n号机器获得的最大收益
		// dp[i][1] : i号机器的前一台机器已经部署的情况下，部署i...n号机器获得的最大收益
		int[][] dp = new int[n + 1][2];
		
		// 设置边界条件
		dp[n][0] = no[n];  // 最后一台机器前没有机器部署
		dp[n][1] = one[n];  // 最后一台机器前有一台机器部署
		
		// 从后往前动态规划
		for (int i = n - 1; i >= 1; i--) {
			// 当前机器前没有机器部署的情况
			// 选择1：当前选no[i]，然后下一台必须部署（因为已经部署了当前机器）
			// 选择2：当前选one[i]，然后下一台可以不部署
			dp[i][0] = Math.max(no[i] + dp[i + 1][1], one[i] + dp[i + 1][0]);
			
			// 当前机器前有一台机器部署的情况
			// 注意：第一台和最后一台机器不会出现前有两台机器部署的情况
			// 选择1：当前选one[i]，然后下一台必须部署
			// 选择2：当前选both[i]，然后下一台可以不部署
			dp[i][1] = Math.max(one[i] + dp[i + 1][1], both[i] + dp[i + 1][0]);
		}
		
		// 第一台机器前不可能有机器部署，所以返回dp[1][0]
		return dp[1][0];
	}
	
	/**
	 * 线性DP解法的空间优化版本
	 * 
	 * 优化思路：
	 * 观察到每个状态只依赖下一个状态，可以只保存两个变量而不是整个数组
	 * 
	 * @return 部署所有机器的最大收益
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 */
	public static int best2_optimized() {
		// 初始化最后一台机器的状态
		long nextNoPrev = no[n];  // dp[i+1][0]
		long nextHasPrev = one[n];  // dp[i+1][1]
		
		// 从后往前计算
		for (int i = n - 1; i >= 1; i--) {
			// 计算当前状态
			long currNoPrev = Math.max(no[i] + nextHasPrev, one[i] + nextNoPrev);
			long currHasPrev = Math.max(one[i] + nextHasPrev, both[i] + nextNoPrev);
			
			// 更新下一轮的状态
			nextNoPrev = currNoPrev;
			nextHasPrev = currHasPrev;
		}
		
		return (int) nextNoPrev;
	}

	// 为了测试
	public static void random(int size, int v) {
		n = size;
		for (int i = 1; i <= n; i++) {
			no[i] = (int) (Math.random() * v);
			one[i] = (int) (Math.random() * v);
			both[i] = (int) (Math.random() * v);
		}
	}

	// 为了测试
	public static void main(String[] args) {
		int maxn = 100;
		int maxv = 100;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int size = (int) (Math.random() * maxn) + 1;
			random(size, maxv);
			int ans1 = best1();
			int ans2 = best2();
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}
