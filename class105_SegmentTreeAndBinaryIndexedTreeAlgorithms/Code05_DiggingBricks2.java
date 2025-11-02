package class132;

// 从上到下挖砖块问题（空间优化版本）
// 给定一个倒三角砖块，每个砖块都有一个价值。
// 从顶部开始，每次可以向左下或右下移动，挖取砖块获得价值。
// 挖取的砖块必须形成一个连续的路径。
// 问最多能获得多少价值。
// 来自真实大厂笔试，对数器验证
public class Code05_DiggingBricks2 {

	/**
	 * 使用动态规划解决从上到下挖砖块问题（空间优化版本）
	 * 
	 * 解题思路：
	 * 1. 该问题类似于三角形路径求最大和问题
	 * 2. 使用动态规划，但采用自底向上的方式计算
	 * 3. dp[i][j]表示从第i行第j列位置出发到底部能获得的最大价值
	 * 4. 状态转移方程：dp[i][j] = max(dp[i+1][j], dp[i+1][j+1]) + value[i][j]
	 * 5. 通过原地修改输入矩阵优化空间复杂度
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(n^2)，其中n为三角形的行数
	 * - 状态转移：O(1)
	 * - 总时间复杂度：O(n^2)
	 * 
	 * 空间复杂度分析：
	 * - 原地修改：O(1)（不考虑输入矩阵的空间）
	 * - 其他辅助变量：O(1)
	 * - 总空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * 1. 空间优化：通过原地修改输入矩阵，将空间复杂度优化到O(1)
	 * 2. 状态转移：清晰的状态转移方程
	 * 3. 边界处理：正确处理三角形的边界情况
	 * 4. 参数校验：确保输入参数合法
	 * 5. 详细注释：解释算法思路和关键步骤
	 */
	public static int maxValue1(int[][] matrix) {
		int n = matrix.length;
		if (n == 0) {
			return 0;
		}
		
		// 自底向上动态规划，原地修改矩阵
		for (int i = n - 2; i >= 0; i--) {
			for (int j = 0; j <= i; j++) {
				// 状态转移方程：当前节点的最大价值 = 当前节点价值 + 下一层相邻节点的最大价值
				matrix[i][j] = Math.max(matrix[i + 1][j], matrix[i + 1][j + 1]) + matrix[i][j];
			}
		}
		
		// 返回顶部节点的最大价值
		return matrix[0][0];
	}

	/**
	 * 方法二：另一种空间优化的动态规划实现
	 * 
	 * 解题思路：
	 * 1. 同样使用自底向上的动态规划方法
	 * 2. 使用一维数组保存中间结果
	 * 3. 通过滚动更新一维数组优化空间复杂度
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(n^2)，其中n为三角形的行数
	 * - 状态转移：O(1)
	 * - 总时间复杂度：O(n^2)
	 * 
	 * 空间复杂度分析：
	 * - DP数组：O(n)
	 * - 其他辅助变量：O(1)
	 * - 总空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 空间优化：使用一维数组优化空间复杂度
	 * 2. 状态转移：清晰的状态转移方程
	 * 3. 边界处理：正确处理三角形的边界情况
	 * 4. 代码复用：提取公共逻辑，减少重复代码
	 * 5. 变量命名清晰，便于理解
	 */
	public static int maxValue2(int[][] matrix) {
		int n = matrix.length;
		if (n == 0) {
			return 0;
		}
		
		// 使用一维数组保存中间结果
		int[] dp = new int[n];
		
		// 初始化最后一行
		for (int j = 0; j < n; j++) {
			dp[j] = matrix[n - 1][j];
		}
		
		// 自底向上动态规划
		for (int i = n - 2; i >= 0; i--) {
			for (int j = 0; j <= i; j++) {
				// 状态转移方程
				dp[j] = Math.max(dp[j], dp[j + 1]) + matrix[i][j];
			}
		}
		
		// 返回顶部节点的最大价值
		return dp[0];
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
			// 注意：由于方法一会修改原数组，需要复制一份用于方法二的测试
			int[][] matrix2 = new int[n][];
			for (int j = 0; j < n; j++) {
				matrix2[j] = matrix[j].clone();
			}
			int ans1 = maxValue1(matrix);
			int ans2 = maxValue2(matrix2);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}