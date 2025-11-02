package class069;

/**
 * 矩阵中和能被 K 整除的路径 (Paths in Matrix Whose Sum is Divisible by K) - 路径计数动态规划
 * 
 * 题目描述：
 * 给一个下标从0开始的 n * m 整数矩阵 grid 和一个整数 k。
 * 从起点(0,0)出发，每步只能往下或者往右，你想要到达终点(m-1, n-1)。
 * 请你返回路径和能被 k 整除的路径数目，答案对 1000000007 取模。
 * 
 * 题目来源：LeetCode 2435. 矩阵中和能被 K 整除的路径
 * 测试链接：https://leetcode.cn/problems/paths-in-matrix-whose-sum-is-divisible-by-k/
 * 
 * 解题思路：
 * 这是一个路径计数动态规划问题，需要在网格中统计满足特定条件（路径和能被K整除）的路径数量。
 * 由于路径数量可能很大，需要对结果取模。
 * 
 * 算法实现：
 * 1. 暴力递归：尝试所有可能的路径
 * 2. 记忆化搜索：使用三维数组存储中间结果（位置+余数）
 * 3. 动态规划：自底向上填表，处理边界条件
 * 
 * 时间复杂度分析：
 * - 暴力递归：O(2^(m+n))，指数级复杂度
 * - 记忆化搜索：O(n * m * k)，多项式复杂度
 * - 动态规划：O(n * m * k)，需要填充三维DP表
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(m+n)，递归栈深度
 * - 记忆化搜索：O(n * m * k)，存储所有状态
 * - 动态规划：O(n * m * k)，三维DP表
 * 
 * 关键技巧：
 * 1. 模运算性质：利用同余定理简化计算
 * 2. 状态定义：dp[i][j][r]表示到达(i,j)时路径和模k余r的路径数
 * 3. 边界处理：起点和终点的特殊处理
 * 
 * 工程化考量：
 * 1. 大数处理：使用取模运算防止溢出
 * 2. 边界条件：单行单列网格的特殊处理
 * 3. 性能优化：动态规划优于递归解法
 * 4. 代码可读性：清晰的变量命名和注释
 */
public class Code04_PathsDivisibleByK {

	public static int mod = 1000000007;

	public static int numberOfPaths1(int[][] grid, int k) {
		int n = grid.length;
		int m = grid[0].length;
		return f1(grid, n, m, k, 0, 0, 0);
	}

	// 当前来到(i,j)位置，最终一定要走到右下角(n-1,m-1)
	// 从(i,j)出发，最终一定要走到右下角(n-1,m-1)，有多少条路径，累加和%k的余数是r
	public static int f1(int[][] grid, int n, int m, int k, int i, int j, int r) {
		if (i == n - 1 && j == m - 1) {
			return grid[i][j] % k == r ? 1 : 0;
		}
		// 后续需要凑出来的余数need
 		int need = (k + r - (grid[i][j] % k)) % k;
		int ans = 0;
		if (i + 1 < n) {
			ans = f1(grid, n, m, k, i + 1, j, need);
		}
		if (j + 1 < m) {
			ans = (ans + f1(grid, n, m, k, i, j + 1, need)) % mod;
		}
		return ans;
	}

	public static int numberOfPaths2(int[][] grid, int k) {
		int n = grid.length;
		int m = grid[0].length;
		int[][][] dp = new int[n][m][k];
		for (int a = 0; a < n; a++) {
			for (int b = 0; b < m; b++) {
				for (int c = 0; c < k; c++) {
					dp[a][b][c] = -1;
				}
			}
		}
		return f2(grid, n, m, k, 0, 0, 0, dp);
	}

	public static int f2(int[][] grid, int n, int m, int k, int i, int j, int r, int[][][] dp) {
		if (i == n - 1 && j == m - 1) {
			return grid[i][j] % k == r ? 1 : 0;
		}
		if (dp[i][j][r] != -1) {
			return dp[i][j][r];
		}
		int need = (k + r - grid[i][j] % k) % k;
		int ans = 0;
		if (i + 1 < n) {
			ans = f2(grid, n, m, k, i + 1, j, need, dp);
		}
		if (j + 1 < m) {
			ans = (ans + f2(grid, n, m, k, i, j + 1, need, dp)) % mod;
		}
		dp[i][j][r] = ans;
		return ans;
	}

	public static int numberOfPaths3(int[][] grid, int k) {
		int n = grid.length;
		int m = grid[0].length;
		int[][][] dp = new int[n][m][k];
		dp[n - 1][m - 1][grid[n - 1][m - 1] % k] = 1;
		for (int i = n - 2; i >= 0; i--) {
			for (int r = 0; r < k; r++) {
				dp[i][m - 1][r] = dp[i + 1][m - 1][(k + r - grid[i][m - 1] % k) % k];
			}
		}
		for (int j = m - 2; j >= 0; j--) {
			for (int r = 0; r < k; r++) {
				dp[n - 1][j][r] = dp[n - 1][j + 1][(k + r - grid[n - 1][j] % k) % k];
			}
		}
		for (int i = n - 2, need; i >= 0; i--) {
			for (int j = m - 2; j >= 0; j--) {
				for (int r = 0; r < k; r++) {
					need = (k + r - grid[i][j] % k) % k;
					dp[i][j][r] = dp[i + 1][j][need];
					dp[i][j][r] = (dp[i][j][r] + dp[i][j + 1][need]) % mod;
				}
			}
		}
		return dp[0][0][0];
	}

}
