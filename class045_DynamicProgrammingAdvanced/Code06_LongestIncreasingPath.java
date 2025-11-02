package class067;

// 矩阵中的最长递增路径
// 给定一个 m x n 整数矩阵 matrix ，找出其中 最长递增路径 的长度
// 对于每个单元格，你可以往上，下，左，右四个方向移动
// 你 不能 在 对角线 方向上移动或移动到 边界外（即不允许环绕）
// 测试链接 : https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
//
// 题目来源：LeetCode 329. 矩阵中的最长递增路径
// 题目链接：https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
// 时间复杂度：O(m*n) - 每个单元格只计算一次
// 空间复杂度：O(m*n) - DP数组 + 递归栈
// 是否最优解：是 - 记忆化搜索是解决此类图中路径问题的标准方法
//
// 解题思路：
// 1. 暴力递归：从每个单元格开始进行深度优先搜索，但存在大量重复计算
// 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
//
// 工程化考量：
// 1. 异常处理：检查输入参数合法性
// 2. 边界处理：处理空矩阵、单元素矩阵等特殊情况
// 3. 性能优化：记忆化搜索避免重复计算
// 4. 可测试性：提供完整的测试用例
//
// 算法详解：
// 这是一个经典的图搜索问题，可以看作在有向无环图(DAG)中寻找最长路径。
// 
// 状态定义：
// dp[i][j] 表示从位置(i,j)出发能走的最长递增路径长度
//
// 状态转移：
// 对于位置(i,j)，我们可以向四个方向移动到相邻位置，如果相邻位置的值大于当前位置的值，
// 则可以移动。转移方程为：
// dp[i][j] = max(dp[相邻位置]) + 1 （对于所有可以移动到的相邻位置）
//
// 边界条件：
// 当无法向任何方向移动时，路径长度为1（只有当前位置）
//
// 为什么使用记忆化搜索而不是BFS？
// 1. 问题特性：每个位置的最长路径长度是固定的，可以缓存
// 2. 实现简单：DFS+记忆化比BFS更直观
// 3. 时间复杂度相同：都是O(m*n)

public class Code06_LongestIncreasingPath {

	/**
	 * 方法1：暴力递归
	 * 时间复杂度：O(m*n*4^(m*n)) - 存在大量重复计算
	 * 空间复杂度：O(m*n) - 递归栈深度
	 * 该方法在大数据量时会超时，仅用于理解问题本质
	 */
	public static int longestIncreasingPath1(int[][] grid) {
		// 输入验证
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int ans = 0;
		// 从每个单元格开始搜索
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				ans = Math.max(ans, f1(grid, i, j));
			}
		}
		return ans;
	}

	/**
	 * 从(i,j)出发，能走出来多长的递增路径，返回最长长度
	 * @param grid 矩阵
	 * @param i    当前行坐标
	 * @param j    当前列坐标
	 * @return 从(i,j)出发的最长递增路径长度
	 */
	public static int f1(int[][] grid, int i, int j) {
		int next = 0;
		
		// 向上移动
		if (i > 0 && grid[i][j] < grid[i - 1][j]) {
			next = Math.max(next, f1(grid, i - 1, j));
		}
		
		// 向下移动
		if (i + 1 < grid.length && grid[i][j] < grid[i + 1][j]) {
			next = Math.max(next, f1(grid, i + 1, j));
		}
		
		// 向左移动
		if (j > 0 && grid[i][j] < grid[i][j - 1]) {
			next = Math.max(next, f1(grid, i, j - 1));
		}
		
		// 向右移动
		if (j + 1 < grid[0].length && grid[i][j] < grid[i][j + 1]) {
			next = Math.max(next, f1(grid, i, j + 1));
		}
		
		// 当前位置算1步，加上后续最长路径
		return next + 1;
	}

	/**
	 * 方法2：记忆化搜索
	 * 时间复杂度：O(m*n) - 每个单元格只计算一次
	 * 空间复杂度：O(m*n) - DP数组 + 递归栈
	 * 通过缓存已计算的结果避免重复计算
	 */
	public static int longestIncreasingPath2(int[][] grid) {
		// 输入验证
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int n = grid.length;
		int m = grid[0].length;
		
		// 创建DP数组并初始化为0，表示未计算
		int[][] dp = new int[n][m];
		
		int ans = 0;
		// 从每个单元格开始搜索
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				ans = Math.max(ans, f2(grid, i, j, dp));
			}
		}
		return ans;
	}

	/**
	 * 带记忆化的递归函数
	 * dp[i][j] 表示从(i,j)出发的最长递增路径长度
	 */
	public static int f2(int[][] grid, int i, int j, int[][] dp) {
		// 如果已经计算过，直接返回结果
		if (dp[i][j] != 0) {
			return dp[i][j];
		}
		
		int next = 0;
		
		// 向上移动
		if (i > 0 && grid[i][j] < grid[i - 1][j]) {
			next = Math.max(next, f2(grid, i - 1, j, dp));
		}
		
		// 向下移动
		if (i + 1 < grid.length && grid[i][j] < grid[i + 1][j]) {
			next = Math.max(next, f2(grid, i + 1, j, dp));
		}
		
		// 向左移动
		if (j > 0 && grid[i][j] < grid[i][j - 1]) {
			next = Math.max(next, f2(grid, i, j - 1, dp));
		}
		
		// 向右移动
		if (j + 1 < grid[0].length && grid[i][j] < grid[i][j + 1]) {
			next = Math.max(next, f2(grid, i, j + 1, dp));
		}
		
		// 当前位置算1步，加上后续最长路径
		int ans = next + 1;
		
		// 缓存结果并返回
		dp[i][j] = ans;
		return ans;
	}
	
	// 测试代码
	public static void main(String[] args) {
		// 测试用例1
		int[][] grid1 = {
			{9, 9, 4},
			{6, 6, 8},
			{2, 1, 1}
		};
		System.out.println("测试用例1:");
		System.out.println("矩阵:");
		printMatrix(grid1);
		System.out.println("最长递增路径长度: " + longestIncreasingPath2(grid1));  // 应该输出4
		
		// 测试用例2
		int[][] grid2 = {
			{3, 4, 5},
			{3, 2, 6},
			{2, 2, 1}
		};
		System.out.println("\n测试用例2:");
		System.out.println("矩阵:");
		printMatrix(grid2);
		System.out.println("最长递增路径长度: " + longestIncreasingPath2(grid2));  // 应该输出4
		
		// 测试用例3
		int[][] grid3 = {{1}};
		System.out.println("\n测试用例3:");
		System.out.println("矩阵:");
		printMatrix(grid3);
		System.out.println("最长递增路径长度: " + longestIncreasingPath2(grid3));  // 应该输出1
	}
	
	// 辅助方法：打印矩阵
	public static void printMatrix(int[][] matrix) {
		for (int[] row : matrix) {
			for (int val : row) {
				System.out.print(val + " ");
			}
			System.out.println();
		}
	}

}