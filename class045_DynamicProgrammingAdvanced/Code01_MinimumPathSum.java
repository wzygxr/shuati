package class067;

/**
 * 最小路径和 (Minimum Path Sum)
 * 
 * 题目描述：
 * 给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，
 * 使得路径上的数字总和为最小。每次只能向下或者向右移动一步。
 * 
 * 题目来源：LeetCode 64. 最小路径和
 * 题目链接：https://leetcode.cn/problems/minimum-path-sum/
 * 
 * 解题思路分析：
 * 1. 暴力递归：从终点向起点递归，存在大量重复计算，时间复杂度O(2^(m+n))
 * 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算，时间复杂度O(m*n)
 * 3. 严格位置依赖的动态规划：自底向上填表，避免递归开销，时间复杂度O(m*n)
 * 4. 空间优化版本：利用滚动数组思想，只保存必要的状态，空间复杂度O(min(m,n))
 * 
 * 时间复杂度分析：
 * - 暴力递归：O(2^(m+n)) - 每次递归有两种选择（向右或向下）
 * - 记忆化搜索：O(m*n) - 每个状态只计算一次
 * - 动态规划：O(m*n) - 需要填充整个DP表
 * - 空间优化DP：O(m*n) - 需要遍历整个网格
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(m+n) - 递归栈深度
 * - 记忆化搜索：O(m*n) - DP数组 + 递归栈
 * - 动态规划：O(m*n) - DP数组
 * - 空间优化DP：O(min(m,n)) - 只使用一维数组
 * 
 * 是否最优解：是 - 动态规划是解决此类最优路径问题的标准方法
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性，处理空网格、单行、单列等特殊情况
 * 2. 边界处理：处理边界条件，防止数组越界
 * 3. 性能优化：空间压缩降低内存使用，减少不必要的计算
 * 4. 可测试性：提供完整的测试用例，覆盖各种边界场景
 * 5. 可维护性：代码结构清晰，注释详细，便于理解和维护
 * 
 * 跨语言差异：
 * - Java：自动内存管理，强类型检查
 * - C++：需要手动管理内存，性能更高
 * - Python：语法简洁，动态类型，性能相对较慢
 * 
 * 极端场景处理：
 * - 空输入：返回0
 * - 单元素网格：直接返回该元素值
 * - 单行网格：只能向右移动，路径和为行元素累加
 * - 单列网格：只能向下移动，路径和为列元素累加
 * - 大网格：使用空间优化版本避免内存溢出
 * 
 * 调试技巧：
 * - 打印中间DP表状态，验证状态转移正确性
 * - 使用小规模测试用例验证算法正确性
 * - 对比不同方法的计算结果，确保一致性
 * 
 * 与机器学习联系：
 * - 路径规划问题在强化学习中有广泛应用
 * - 动态规划思想在马尔可夫决策过程中体现
 * - 最优路径搜索与图神经网络相关
 * 
 * 补充题目：0-1背包问题
 * 给定n个物品，每个物品有重量w[i]和价值v[i]，背包容量为C
 * 选择一些物品放入背包，使得总重量不超过C，且总价值最大
 * 测试链接：https://leetcode.cn/problems/partition-equal-subset-sum/
 * 题目来源：LeetCode 416. 分割等和子集（0-1背包的变形）
 * 0-1背包是动态规划中的经典问题，很多问题都可以转化为背包问题的变种
 */
public class Code01_MinimumPathSum {

	/**
	 * 方法1：暴力递归方法
	 * 
	 * 算法思想：从终点(i,j)向起点(0,0)递归，每次只能向左或向上移动
	 * 通过递归探索所有可能的路径，选择路径和最小的那条
	 * 
	 * 时间复杂度：O(2^(m+n)) - 每次递归有两种选择，最坏情况下需要遍历所有可能的路径
	 * 空间复杂度：O(m+n) - 递归栈深度，最坏情况下递归深度为m+n
	 * 
	 * 优点：思路直观，易于理解问题本质
	 * 缺点：存在大量重复计算，大数据量时会超时
	 * 
	 * 适用场景：仅用于理解问题本质，不适用于实际应用
	 * 
	 * @param grid 二维网格，包含非负整数
	 * @return 从左上角到右下角的最小路径和
	 * 
	 * 异常处理：检查输入参数合法性，处理空网格等特殊情况
	 * 边界处理：处理单行、单列网格等边界情况
	 */
	public static int minPathSum1(int[][] grid) {
		// 输入验证：检查网格是否为空或维度为0
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		// 从终点(grid.length-1, grid[0].length-1)开始递归
		return f1(grid, grid.length - 1, grid[0].length - 1);
	}

	/**
	 * 递归辅助函数：计算从(0,0)到(i,j)的最小路径和
	 * 
	 * 状态定义：f1(grid, i, j) 表示从(0,0)到(i,j)的最小路径和
	 * 状态转移：
	 * - 如果i=0且j=0：到达起点，返回grid[0][0]
	 * - 否则：最小路径和 = min(从上方来的路径和, 从左方来的路径和) + grid[i][j]
	 * 
	 * 递归终止条件：到达起点(0,0)
	 * 
	 * @param grid 二维网格
	 * @param i 当前行索引
	 * @param j 当前列索引
	 * @return 从(0,0)到(i,j)的最小路径和
	 * 
	 * 调试技巧：可以打印递归调用栈，观察重复计算情况
	 * 优化方向：添加缓存避免重复计算（记忆化搜索）
	 */
	private static int f1(int[][] grid, int i, int j) {
		// 基础情况：到达起点(0,0)，路径和就是该位置的值
		if (i == 0 && j == 0) {
			return grid[0][0];
		}
		
		// 初始化两个方向的路径和为最大值
		int up = Integer.MAX_VALUE;   // 从上方来的最小路径和
		int left = Integer.MAX_VALUE; // 从左方来的最小路径和
		
		// 如果可以从上方来（不是第一行），递归计算上方路径
		if (i - 1 >= 0) {
			up = f1(grid, i - 1, j);
		}
		
		// 如果可以从左方来（不是第一列），递归计算左方路径
		if (j - 1 >= 0) {
			left = f1(grid, i, j - 1);
		}
		
		// 当前位置的最小路径和 = min(从上方来, 从左方来) + 当前位置的值
		// 使用Math.min避免手动比较，提高代码可读性
		return grid[i][j] + Math.min(up, left);
	}

	/**
	 * 方法2：记忆化搜索方法
	 * 
	 * 算法思想：在暴力递归基础上增加缓存（DP数组），避免重复计算
	 * 当计算某个状态(i,j)时，先检查是否已经计算过，如果计算过直接返回结果
	 * 
	 * 时间复杂度：O(m*n) - 每个状态只计算一次，避免了重复计算
	 * 空间复杂度：O(m*n) - DP数组占用O(m*n)空间，递归栈深度O(m+n)
	 * 
	 * 优点：避免了重复计算，效率显著提升
	 * 缺点：仍然有递归开销，对于极大网格可能栈溢出
	 * 
	 * 适用场景：中等规模网格，状态转移关系复杂的情况
	 * 
	 * @param grid 二维网格
	 * @return 最小路径和
	 * 
	 * 缓存策略：使用二维数组dp[i][j]缓存计算结果
	 * 初始化值：-1表示未计算，其他值表示已计算的结果
	 * 缓存命中：计算前先检查缓存，避免重复计算
	 */
	public static int minPathSum2(int[][] grid) {
		// 输入验证
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int n = grid.length;
		int m = grid[0].length;
		
		// 创建DP缓存数组，初始化为-1表示未计算
		// 使用二维数组存储每个位置的最小路径和
		int[][] dp = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				dp[i][j] = -1;  // -1表示该位置的最小路径和尚未计算
			}
		}
		
		// 从终点开始记忆化搜索
		return f2(grid, grid.length - 1, grid[0].length - 1, dp);
	}

	/**
	 * 记忆化搜索的递归辅助函数
	 * 
	 * 算法流程：
	 * 1. 检查缓存：如果dp[i][j] != -1，说明已经计算过，直接返回结果
	 * 2. 递归终止：到达起点(0,0)，返回grid[0][0]
	 * 3. 递归计算：分别计算从上方和左方来的最小路径和
	 * 4. 缓存结果：将计算结果存入dp[i][j]，避免重复计算
	 * 
	 * @param grid 二维网格
	 * @param i 当前行索引
	 * @param j 当前列索引
	 * @param dp 缓存数组
	 * @return 从(0,0)到(i,j)的最小路径和
	 * 
	 * 缓存有效性：确保每个状态只计算一次
	 * 线程安全：单线程环境下安全，多线程需要同步机制
	 */
	private static int f2(int[][] grid, int i, int j, int[][] dp) {
		// 缓存检查：如果已经计算过，直接返回缓存结果
		// 这是记忆化搜索的核心，避免了重复计算
		if (dp[i][j] != -1) {
			return dp[i][j];
		}
		
		int ans;
		// 基础情况：到达起点(0,0)
		if (i == 0 && j == 0) {
			ans = grid[0][0];
		} else {
			// 初始化两个方向的路径和为最大值
			int up = Integer.MAX_VALUE;   // 从上方来的最小路径和
			int left = Integer.MAX_VALUE; // 从左方来的最小路径和
			
			// 如果可以从上方来（不是第一行），递归计算上方路径
			// 注意：这里使用记忆化搜索，避免重复计算
			if (i - 1 >= 0) {
				up = f2(grid, i - 1, j, dp);
			}
			
			// 如果可以从左方来（不是第一列），递归计算左方路径
			if (j - 1 >= 0) {
				left = f2(grid, i, j - 1, dp);
			}
			
			// 当前位置的最小路径和 = min(从上方来, 从左方来) + 当前位置的值
			ans = grid[i][j] + Math.min(up, left);
		}
		
		// 缓存结果：将计算结果存入dp数组，避免后续重复计算
		// 这是记忆化搜索的关键步骤
		dp[i][j] = ans;
		return ans;
	}

	/**
	 * 方法3：严格位置依赖的动态规划方法
	 * 
	 * 算法思想：自底向上填表，从起点开始逐步计算每个位置的最小路径和
	 * 通过明确的递推关系，避免递归开销，提高算法效率
	 * 
	 * 状态定义：dp[i][j] 表示从(0,0)到(i,j)的最小路径和
	 * 状态转移方程：
	 * - 当i=0且j=0：dp[0][0] = grid[0][0]
	 * - 当i=0且j>0：dp[0][j] = dp[0][j-1] + grid[0][j]（只能从左方来）
	 * - 当i>0且j=0：dp[i][0] = dp[i-1][0] + grid[i][0]（只能从上方来）
	 * - 当i>0且j>0：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
	 * 
	 * 时间复杂度：O(m*n) - 需要遍历整个网格，每个位置计算一次
	 * 空间复杂度：O(m*n) - 使用二维DP数组存储中间结果
	 * 
	 * 优点：无递归开销，效率稳定，易于理解和实现
	 * 缺点：空间复杂度较高，对于极大网格可能内存不足
	 * 
	 * 适用场景：各种规模的网格，特别是需要稳定性能的场景
	 * 
	 * @param grid 二维网格
	 * @return 最小路径和
	 * 
	 * 填表顺序：按行优先顺序填充，确保依赖的状态已经计算
	 * 边界处理：单独处理第一行和第一列的特殊情况
	 */
	public static int minPathSum3(int[][] grid) {
		// 输入验证：检查网格是否为空或维度为0
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int n = grid.length;
		int m = grid[0].length;
		
		// 创建DP数组：dp[i][j]表示从(0,0)到(i,j)的最小路径和
		int[][] dp = new int[n][m];
		
		// 初始化起点：dp[0][0] = grid[0][0]
		// 这是动态规划的基准情况
		dp[0][0] = grid[0][0];
		
		// 初始化第一列：只能从上方来，因为不能从左边来（左边是边界）
		// 对于第一列的每个位置(i,0)，路径和 = 上方位置(i-1,0)的路径和 + 当前值
		for (int i = 1; i < n; i++) {
			dp[i][0] = dp[i - 1][0] + grid[i][0];
		}
		
		// 初始化第一行：只能从左方来，因为不能从上方来（上方是边界）
		// 对于第一行的每个位置(0,j)，路径和 = 左方位置(0,j-1)的路径和 + 当前值
		for (int j = 1; j < m; j++) {
			dp[0][j] = dp[0][j - 1] + grid[0][j];
		}
		
		// 填充其余位置：对于非边界位置(i,j)，可以从上方或左方来
		// 选择路径和较小的方向 + 当前值
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				// 状态转移方程：dp[i][j] = min(从上方来, 从左方来) + 当前位置的值
				// 使用Math.min简化代码，提高可读性
				dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
			}
		}
		
		// 返回终点位置的最小路径和：dp[n-1][m-1]
		// 终点位置存储了从起点到终点的最小路径和
		return dp[n - 1][m - 1];
	}

	/**
	 * 方法4：严格位置依赖的动态规划 + 空间压缩技巧
	 * 
	 * 算法思想：利用滚动数组思想，只保存必要的状态，将空间复杂度从O(m*n)优化到O(min(m,n))
	 * 观察发现：在计算第i行时，只需要第i-1行的dp值和当前行已经计算的部分
	 * 因此可以使用一维数组来存储状态，通过滚动更新来节省空间
	 * 
	 * 状态定义：dp[j] 表示当前行第j列的最小路径和
	 * 状态转移：
	 * - 对于第一列：只能从上方来，dp[0] = dp[0] + grid[i][0]
	 * - 对于其他列：dp[j] = min(dp[j-1], dp[j]) + grid[i][j]
	 * 
	 * 时间复杂度：O(m*n) - 需要遍历整个网格，每个位置计算一次
	 * 空间复杂度：O(min(m,n)) - 只使用一维数组，长度为较小的维度
	 * 
	 * 优点：空间效率高，适合处理大规模网格
	 * 缺点：代码逻辑相对复杂，需要理解滚动数组的原理
	 * 
	 * 适用场景：大规模网格，内存受限的环境
	 * 
	 * @param grid 二维网格
	 * @return 最小路径和
	 * 
	 * 空间优化原理：利用状态依赖的局部性，只保存必要的中间结果
	 * 更新顺序：按行更新，每行从左到右更新
	 */
	public static int minPathSum4(int[][] grid) {
		// 输入验证
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int n = grid.length;
		int m = grid[0].length;
		
		// 空间优化：使用一维数组代替二维数组
		// 选择较小的维度作为数组长度，进一步优化空间
		// 这里假设列数m较小，如果行数n较小可以交换处理
		int[] dp = new int[m];
		
		// 初始化第一行：只能从左方来
		// dp[j] 表示第一行第j列的最小路径和
		dp[0] = grid[0][0];
		for (int j = 1; j < m; j++) {
			// 第一行的每个位置只能从左方来
			dp[j] = dp[j - 1] + grid[0][j];
		}
		
		// 逐行更新：从第二行开始处理
		for (int i = 1; i < n; i++) {
			// 更新第一列：只能从上方来
			// 注意：dp[0]存储的是上一行第一列的值，需要加上当前值
			dp[0] = dp[0] + grid[i][0];
			
			// 更新其余列：可以从上方或左方来
			for (int j = 1; j < m; j++) {
				// 关键理解：
				// - dp[j] 存储的是上一行第j列的值（从上方来的路径和）
				// - dp[j-1] 存储的是当前行第j-1列的值（从左方来的路径和）
				// 选择较小的路径和 + 当前值
				dp[j] = Math.min(dp[j - 1], dp[j]) + grid[i][j];
			}
		}
		
		// 返回终点位置的最小路径和：dp[m-1]
		// 经过逐行更新后，dp数组的最后一个元素就是最终结果
		return dp[m - 1];
	}

	/**
	 * 补充题目1：0-1背包问题标准实现（二维DP）
	 * 
	 * 问题描述：给定n个物品，每个物品有重量w[i]和价值v[i]，背包容量为C
	 * 选择一些物品放入背包，使得总重量不超过C，且总价值最大
	 * 
	 * 算法思想：动态规划，状态dp[i][j]表示前i个物品，背包容量为j时的最大价值
	 * 状态转移方程：
	 * - 不选择第i个物品：dp[i][j] = dp[i-1][j]
	 * - 选择第i个物品：dp[i][j] = dp[i-1][j-w[i-1]] + v[i-1]（如果j >= w[i-1]）
	 * - 取两种情况的最大值
	 * 
	 * 时间复杂度：O(n*C) - 需要填充n*C的DP表
	 * 空间复杂度：O(n*C) - 使用二维DP数组
	 * 
	 * @param w 物品重量数组
	 * @param v 物品价值数组
	 * @param C 背包容量
	 * @return 背包能装的最大价值
	 * 
	 * 应用场景：资源分配、投资决策等优化问题
	 */
	public static int knapsack1(int[] w, int[] v, int C) {
		// 输入验证
		if (w == null || v == null || w.length == 0 || v.length == 0 || w.length != v.length || C <= 0) {
			return 0;
		}
		int n = w.length;
		// dp[i][j] 表示前i个物品，背包容量为j时的最大价值
		int[][] dp = new int[n + 1][C + 1];
		
		// 逐行填充DP表：i从1到n，表示考虑前i个物品
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= C; j++) {
				// 不选择第i个物品：最大价值等于前i-1个物品在容量j时的最大价值
				dp[i][j] = dp[i - 1][j];
				// 选择第i个物品：如果当前容量j足够放下第i个物品
				if (j >= w[i - 1]) {
					// 最大价值 = max(不选择当前物品, 选择当前物品)
					// 选择当前物品：前i-1个物品在容量j-w[i-1]时的最大价值 + 当前物品价值
					dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - w[i - 1]] + v[i - 1]);
				}
			}
		}
		
		// 返回结果：考虑所有n个物品，背包容量为C时的最大价值
		return dp[n][C];
	}
	
	/**
	 * 补充题目2：0-1背包问题空间优化版本（一维DP）
	 * 
	 * 算法思想：利用滚动数组思想，将二维DP压缩为一维DP
	 * 关键观察：在计算第i行时，只需要第i-1行的数据
	 * 因此可以使用一维数组，通过逆序遍历容量来避免覆盖需要的数据
	 * 
	 * 状态定义：dp[j] 表示背包容量为j时的最大价值
	 * 状态转移：dp[j] = max(dp[j], dp[j-w[i]] + v[i])
	 * 
	 * 时间复杂度：O(n*C) - 需要遍历所有物品和容量
	 * 空间复杂度：O(C) - 只使用一维数组
	 * 
	 * 优点：空间效率高，适合处理大规模数据
	 * 关键点：必须逆序遍历容量，避免同一物品被多次选择
	 * 
	 * @param w 物品重量数组
	 * @param v 物品价值数组
	 * @param C 背包容量
	 * @return 背包能装的最大价值
	 */
	public static int knapsack2(int[] w, int[] v, int C) {
		// 输入验证
		if (w == null || v == null || w.length == 0 || v.length == 0 || w.length != v.length || C <= 0) {
			return 0;
		}
		int n = w.length;
		// 只使用一维数组：dp[j]表示背包容量为j时的最大价值
		int[] dp = new int[C + 1];
		
		// 遍历每个物品
		for (int i = 0; i < n; i++) {
			// 关键：逆序遍历容量，从大到小遍历
			// 这样可以确保每个物品只被选择一次
			for (int j = C; j >= w[i]; j--) {
				// 状态转移：选择当前物品或不选择当前物品的最大值
				dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
			}
		}
		
		return dp[C];
	}
	
	/**
	 * 补充题目3：分割等和子集问题（0-1背包的变形）
	 * 
	 * 问题描述：给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，
	 * 使得两个子集的元素和相等。
	 * 
	 * 算法思想：转化为0-1背包问题
	 * - 背包容量：数组总和的一半（target）
	 * - 物品重量：数组中的每个数字
	 * - 物品价值：不重要，这里关心的是能否恰好装满背包
	 * 
	 * 状态定义：dp[j] 表示能否用数组中的数字凑出和j
	 * 状态转移：dp[j] = dp[j] || dp[j-num]
	 * 
	 * 时间复杂度：O(n*target) - 其中target为数组总和的一半
	 * 空间复杂度：O(target) - 使用一维布尔数组
	 * 
	 * @param nums 正整数数组
	 * @return 是否可以将数组分割成两个和相等的子集
	 * 
	 * 应用场景：LeetCode 416. 分割等和子集
	 */
	public static boolean canPartition(int[] nums) {
		// 输入验证
		if (nums == null || nums.length < 2) {
			return false;
		}
		
		// 计算数组总和
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 如果总和为奇数，不可能分割成两个和相等的子集
		if (sum % 2 != 0) {
			return false;
		}
		
		int target = sum / 2;
		// 转化为0-1背包问题：能否从数组中选择一些数，使得它们的和等于target
		boolean[] dp = new boolean[target + 1];
		dp[0] = true; // 空集的和为0，总是可以凑出
		
		// 遍历数组中的每个数字
		for (int num : nums) {
			// 逆序遍历容量，避免同一数字被多次使用
			for (int j = target; j >= num; j--) {
				// 状态转移：当前容量j能否凑出 = 原来就能凑出 或 减去当前数字后能凑出
				dp[j] = dp[j] || dp[j - num];
			}
		}
		
		return dp[target];
	}
	
	/**
	 * 测试方法：验证所有算法的正确性
	 * 
	 * 测试用例设计：
	 * 1. 正常网格测试
	 * 2. 边界情况测试（单行、单列、单元素）
	 * 3. 背包问题测试
	 * 4. 分割等和子集测试
	 * 
	 * 测试目的：确保各种实现方法结果一致，验证算法正确性
	 */
	public static void main(String[] args) {
		System.out.println("=== 最小路径和算法测试 ===");
		
		// 测试用例1：标准3x3网格
		int[][] grid1 = {
			{1, 3, 1},
			{1, 5, 1},
			{4, 2, 1}
		};
		System.out.println("测试用例1 - 标准3x3网格:");
		System.out.println("暴力递归: " + minPathSum1(grid1) + " (仅小规模测试)");
		System.out.println("记忆化搜索: " + minPathSum2(grid1));
		System.out.println("动态规划: " + minPathSum3(grid1));
		System.out.println("空间优化DP: " + minPathSum4(grid1));
		System.out.println("预期结果: 7");
		System.out.println();
		
		// 测试用例2：2x3网格
		int[][] grid2 = {
			{1, 2, 3},
			{4, 5, 6}
		};
		System.out.println("测试用例2 - 2x3网格:");
		System.out.println("记忆化搜索: " + minPathSum2(grid2));
		System.out.println("动态规划: " + minPathSum3(grid2));
		System.out.println("空间优化DP: " + minPathSum4(grid2));
		System.out.println("预期结果: 12");
		System.out.println();
		
		// 测试用例3：单行网格
		int[][] grid3 = {{1, 2, 3, 4, 5}};
		System.out.println("测试用例3 - 单行网格:");
		System.out.println("记忆化搜索: " + minPathSum2(grid3));
		System.out.println("动态规划: " + minPathSum3(grid3));
		System.out.println("空间优化DP: " + minPathSum4(grid3));
		System.out.println("预期结果: 15");
		System.out.println();
		
		// 测试用例4：单列网格
		int[][] grid4 = {{1}, {2}, {3}, {4}, {5}};
		System.out.println("测试用例4 - 单列网格:");
		System.out.println("记忆化搜索: " + minPathSum2(grid4));
		System.out.println("动态规划: " + minPathSum3(grid4));
		System.out.println("空间优化DP: " + minPathSum4(grid4));
		System.out.println("预期结果: 15");
		System.out.println();
		
		// 测试用例5：单元素网格
		int[][] grid5 = {{5}};
		System.out.println("测试用例5 - 单元素网格:");
		System.out.println("记忆化搜索: " + minPathSum2(grid5));
		System.out.println("动态规划: " + minPathSum3(grid5));
		System.out.println("空间优化DP: " + minPathSum4(grid5));
		System.out.println("预期结果: 5");
		System.out.println();
		
		System.out.println("=== 0-1背包问题测试 ===");
		int[] w = {2, 3, 4, 5};
		int[] v = {3, 4, 5, 6};
		int C = 8;
		System.out.println("物品重量: " + java.util.Arrays.toString(w));
		System.out.println("物品价值: " + java.util.Arrays.toString(v));
		System.out.println("背包容量: " + C);
		System.out.println("标准DP最大价值: " + knapsack1(w, v, C));
		System.out.println("空间优化最大价值: " + knapsack2(w, v, C));
		System.out.println("预期结果: 9");
		System.out.println();
		
		System.out.println("=== 分割等和子集测试 ===");
		int[] nums1 = {1, 5, 11, 5};  // 可以分割
		int[] nums2 = {1, 2, 3, 5};   // 无法分割
		System.out.println("数组1 " + java.util.Arrays.toString(nums1) + " 能否分割: " + canPartition(nums1));
		System.out.println("数组2 " + java.util.Arrays.toString(nums2) + " 能否分割: " + canPartition(nums2));
		System.out.println("预期结果: true, false");
		
		System.out.println("
=== 测试完成 ===");
	}
}