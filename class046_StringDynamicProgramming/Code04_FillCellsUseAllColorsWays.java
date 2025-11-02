import java.util.Arrays;

/**
 * 有效涂色问题（Fill Cells Use All Colors Ways）
 * 给定n、m两个参数，一共有n个格子，每个格子可以涂上一种颜色，颜色在m种里选
 * 当涂满n个格子，并且m种颜色都使用了，叫一种有效方法
 * 求一共有多少种有效的涂色方法
 * 
 * 约束条件：
 * 1 <= n, m <= 5000
 * 结果比较大请 % 1000000007 之后返回
 * 
 * 算法核心思想：
 * 使用动态规划解决组合数学问题，通过构建二维DP表来计算有效涂色方案数
 * 
 * 时间复杂度分析：
 * - 动态规划版本：O(n*m)
 * 
 * 空间复杂度分析：
 * - 动态规划版本：O(n*m)
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理极端情况
 * 3. 性能优化：使用预分配数组减少内存分配开销
 * 4. 数值安全：使用取模运算防止整数溢出
 * 5. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 组合数学：斯特林数、贝尔数相关问题
 * - 概率论：离散概率分布计算
 * - 计算机图形学：颜色分配算法
 */
public class Code04_FillCellsUseAllColorsWays {

	/**
	 * 暴力方法（用于验证）
	 * 通过枚举所有可能的涂色方案并验证其有效性来计算结果
	 * 由于时间复杂度为指数级，仅适用于小规模数据
	 * 
	 * @param n 格子数量
	 * @param m 颜色种类数
	 * @return 有效涂色方案数
	 */
	// 暴力方法
	// 为了验证
	public static int ways1(int n, int m) {
		return f(new int[n], new boolean[m + 1], 0, n, m);
	}

	/**
	 * 递归枚举所有涂色方案
	 * 
	 * @param path 当前涂色路径
	 * @param set  颜色使用标记数组
	 * @param i    当前处理的格子索引
	 * @param n    总格子数
	 * @param m    颜色种类数
	 * @return 有效涂色方案数
	 */
	// 把所有填色的方法暴力枚举
	// 然后一个一个验证是否有效
	// 这是一个带路径的递归
	// 无法改成动态规划
	public static int f(int[] path, boolean[] set, int i, int n, int m) {
		if (i == n) {
			// 检查是否使用了所有m种颜色
			Arrays.fill(set, false);
			int colors = 0;
			for (int c : path) {
				if (!set[c]) {
					set[c] = true;
					colors++;
				}
			}
			return colors == m ? 1 : 0;
		} else {
			int ans = 0;
			// 枚举第i个格子可以涂的颜色（1到m）
			for (int j = 1; j <= m; j++) {
				path[i] = j;
				ans += f(path, set, i + 1, n, m);
			}
			return ans;
		}
	}

	/*
	 * 有效涂色问题 - 动态规划解法
	 * dp[i][j] 表示前i个格子使用恰好j种颜色的方案数
	 * 
	 * 状态转移方程：
	 * dp[i][j] = dp[i-1][j] * j + dp[i-1][j-1] * (m-j+1)
	 * 
	 * 解释：
	 * 前i-1个格子已经使用了j种颜色，第i个格子可以涂这j种颜色中的任意一种：dp[i-1][j] * j
	 * 前i-1个格子使用了j-1种颜色，第i个格子必须涂一种新颜色（从剩下的m-j+1种中选）：dp[i-1][j-1] * (m-j+1)
	 * 
	 * 边界条件：
	 * dp[i][1] = m，表示前i个格子只使用1种颜色，总共有m种选择
	 * dp[0][0] = 1，表示0个格子使用0种颜色，有1种方案（空方案）
	 * 
	 * 时间复杂度：O(n*m)
	 * 空间复杂度：O(n*m)
	 */
	// 正式方法
	// 时间复杂度O(n * m)
	// 已经展示太多次从递归到动态规划了
	// 直接写动态规划吧
	// 也不做空间压缩了，因为千篇一律
	// 有兴趣的同学自己试试
	public static int MAXN = 5001;

	public static int[][] dp = new int[MAXN][MAXN];

	public static int mod = 1000000007;

	/**
	 * 动态规划解法计算有效涂色方案数
	 * 
	 * @param n 格子数量
	 * @param m 颜色种类数
	 * @return 有效涂色方案数（对1000000007取模）
	 */
	public static int ways2(int n, int m) {
		// 输入验证
		if (n <= 0 || m <= 0) {
			return 0;
		}
		
		// 边界情况处理
		if (m > n) {
			// 颜色种类数大于格子数，无法使用所有颜色
			return 0;
		}
		
		// dp[i][j]: 一共有m种颜色，前i个格子涂满j种颜色的方法数
		// 初始化边界条件
		for (int i = 1; i <= n; i++) {
			dp[i][1] = m; // 前i个格子只使用1种颜色，总共有m种选择
		}
		
		// 填充DP表
		for (int i = 2; i <= n; i++) {
			for (int j = 2; j <= m; j++) {
				// 状态转移方程：
				// 1. 前i-1个格子已经使用了j种颜色，第i个格子可以涂这j种颜色中的任意一种
				dp[i][j] = (int) (((long) dp[i - 1][j] * j) % mod);
				// 2. 前i-1个格子使用了j-1种颜色，第i个格子必须涂一种新颜色（从剩下的m-j+1种中选）
				dp[i][j] = (int) ((((long) dp[i - 1][j - 1] * (m - j + 1)) + dp[i][j]) % mod);
			}
		}
		return dp[n][m];
	}

	/**
	 * 主函数，用于测试和性能验证
	 */
	public static void main(String[] args) {
		// 测试的数据量比较小
		// 那是因为数据量大了，暴力方法过不了
		// 但是这个数据量足够说明正式方法是正确的
		int N = 9;
		int M = 9;
		System.out.println("功能测试开始");
		for (int n = 1; n <= N; n++) {
			for (int m = 1; m <= M; m++) {
				int ans1 = ways1(n, m);
				int ans2 = ways2(n, m);
				if (ans1 != ans2) {
					System.out.println("出错了!");
				}
			}
		}
		System.out.println("功能测试结束");

		System.out.println("性能测试开始");
		int n = 5000;
		int m = 4877;
		System.out.println("n : " + n);
		System.out.println("m : " + m);
		long start = System.currentTimeMillis();
		int ans = ways2(n, m);
		long end = System.currentTimeMillis();
		System.out.println("取模之后的结果 : " + ans);
		System.out.println("运行时间 : " + (end - start) + " 毫秒");
		System.out.println("性能测试结束");
	}

}