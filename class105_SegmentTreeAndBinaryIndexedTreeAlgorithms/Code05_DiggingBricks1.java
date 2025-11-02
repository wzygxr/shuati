package class132;

// 从上到下挖砖块问题
// 给定一个倒三角砖块，每个砖块都有一个价值。
// 从顶部开始，每次可以向左下或右下移动，挖取砖块获得价值。
// 挖取的砖块必须形成一个连续的路径。
// 问最多能获得多少价值。
// 来自真实大厂笔试，对数器验证
public class Code05_DiggingBricks1 {

	/**
	 * 使用动态规划解决从上到下挖砖块问题
	 * 
	 * 解题思路：
	 * 1. 该问题类似于三角形路径求最大和问题
	 * 2. 使用动态规划，dp[i][j]表示到达第i行第j列位置时能获得的最大价值
	 * 3. 状态转移方程：dp[i][j] = max(dp[i-1][j-1], dp[i-1][j]) + value[i][j]
	 * 4. 边界处理：第一行和每行的边界元素需要特殊处理
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(n^2)，其中n为三角形的行数
	 * - 状态转移：O(1)
	 * - 总时间复杂度：O(n^2)
	 * 
	 * 空间复杂度分析：
	 * - DP数组：O(n^2)
	 * - 其他辅助变量：O(1)
	 * - 总空间复杂度：O(n^2)
	 * 
	 * 工程化考量：
	 * 1. 边界处理：正确处理三角形的边界情况
	 * 2. 状态转移：清晰的状态转移方程
	 * 3. 初始化：正确初始化DP数组的边界条件
	 * 4. 参数校验：确保输入参数合法
	 * 5. 详细注释：解释算法思路和关键步骤
	 */
	public static int maxValue1(int[][] matrix) {
		int n = matrix.length;
		if (n == 0) {
			return 0;
		}
		
		// dp[i][j]表示到达第i行第j列位置时能获得的最大价值
		int[][] dp = new int[n][n];
		
		// 初始化第一行
		dp[0][0] = matrix[0][0];
		
		// 动态规划填表
		for (int i = 1; i < n; i++) {
			// 处理每行的第一个元素（只能从上方到达）
			dp[i][0] = dp[i - 1][0] + matrix[i][0];
			
			// 处理每行的中间元素（可以从左上或右上到达）
			for (int j = 1; j < i; j++) {
				dp[i][j] = Math.max(dp[i - 1][j - 1], dp[i - 1][j]) + matrix[i][j];
			}
			
			// 处理每行的最后一个元素（只能从左上到达）
			dp[i][i] = dp[i - 1][i - 1] + matrix[i][i];
		}
		
		// 找到最后一行的最大值
		int ans = dp[n - 1][0];
		for (int j = 1; j < n; j++) {
			ans = Math.max(ans, dp[n - 1][j]);
		}
		
		return ans;
	}

	/**
	 * 方法二：空间优化的动态规划实现
	 * 
	 * 解题思路：
	 * 1. 同样使用动态规划解决三角形路径问题
	 * 2. 通过滚动数组优化空间复杂度
	 * 3. 只保存前一行的状态，节省空间
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(n^2)，其中n为三角形的行数
	 * - 状态转移：O(1)
	 * - 总时间复杂度：O(n^2)
	 * 
	 * 空间复杂度分析：
	 * - DP数组：O(n)（通过滚动数组优化）
	 * - 其他辅助变量：O(1)
	 * - 总空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 空间优化：使用滚动数组技术优化空间复杂度
	 * 2. 边界处理：正确处理三角形的边界情况
	 * 3. 状态转移：清晰的状态转移方程
	 * 4. 代码复用：提取公共逻辑，减少重复代码
	 * 5. 变量命名清晰，便于理解
	 */
	public static int maxValue2(int[][] matrix) {
		int n = matrix.length;
		if (n == 0) {
			return 0;
		}
		
		// 使用滚动数组优化空间复杂度
		int[] dp = new int[n];
		int[] next = new int[n];
		
		// 初始化第一行
		dp[0] = matrix[0][0];
		
		// 动态规划填表
		for (int i = 1; i < n; i++) {
			// 处理每行的第一个元素
			next[0] = dp[0] + matrix[i][0];
			
			// 处理每行的中间元素
			for (int j = 1; j < i; j++) {
				next[j] = Math.max(dp[j - 1], dp[j]) + matrix[i][j];
			}
			
			// 处理每行的最后一个元素
			next[i] = dp[i - 1] + matrix[i][i];
			
			// 更新dp数组（滚动数组）
			int[] tmp = dp;
			dp = next;
			next = tmp;
		}
		
		// 找到最后一行的最大值
		int ans = dp[0];
		for (int j = 1; j < n; j++) {
			ans = Math.max(ans, dp[j]);
		}
		
		return ans;
	}

	// 为了测试
	public static int[][] randomMatrix(int n) {
		int[][] matrix = new int[n][];
		for (int i = 0; i < n; i++) {
			matrix[i] = new int[i + 1];
			for (int j = 0; j <= i; j++) {
				matrix[i][j] = (int) (Math.random() * 100);
			}
		}
		return matrix;
	}

	// 为了测试
	public static void main(String[] args) {
		int n = 10;
		int testTime = 100;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[][] matrix = randomMatrix(n);
			int ans1 = maxValue1(matrix);
			int ans2 = maxValue2(matrix);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}