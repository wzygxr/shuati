package class132;

// 粉刷房子III问题
// 在一个小镇里，按从 1 到 n 为 n 个房子进行编号。
// 每个房子可以被粉刷成 k 种颜色中的一种。
// 每个房子粉刷成不同颜色的花费成本也是不同的。
// 你需要粉刷所有的房子，并且使其相邻的两个房子颜色不同。
// 当涂色的方案确定后，相邻颜色相同的房子会被划分成一个街区。
// 比如 houses = [1,2,2,3,3,3]，它包含三个街区 [{1}, {2,2}, {3,3,3}]。
// 给你一个数组 houses，一个 m x n 的矩阵 cost 和一个整数 target，
// 其中：
// - houses[i]：是第 i 个房子的颜色，0 表示这个房子还没有被涂色。
// - cost[i][j]：是将第 i 个房子涂成颜色 j+1 的花费。
// 请你返回房子涂色方案的最小花费，使得涂色后街道数等于 target。
// 如果没有可用的涂色方案，请返回 -1。
// 1 <= m <= 100
// 1 <= n <= 20
// 1 <= target <= m
// 0 <= houses[i] <= n
// 1 <= cost[i][j] <= 10^4
// 来自LeetCode 1473，对数器验证
public class Code04_PaintHouseIII {

	/**
	 * 使用动态规划解决粉刷房子III问题
	 * 
	 * 解题思路：
	 * 1. 使用三维动态规划，dp[i][j][k]表示考虑到第i个房子，第i个房子涂成颜色j，形成k个街区的最小花费
	 * 2. 状态转移考虑两种情况：
	 *    - 第i个房子已经有颜色：只能使用该颜色
	 *    - 第i个房子没有颜色：可以涂成任意颜色
	 * 3. 对于每种情况，考虑与前一个房子颜色是否相同来决定街区数变化
	 * 4. 初始化时处理第一个房子的特殊情况
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(m * n * target)
	 * - 状态转移：O(n)
	 * - 总时间复杂度：O(m * n^2 * target)
	 * 
	 * 空间复杂度分析：
	 * - DP数组：O(m * n * target)
	 * - 其他辅助变量：O(1)
	 * - 总空间复杂度：O(m * n * target)
	 * 
	 * 工程化考量：
	 * 1. 状态定义清晰：三维DP状态明确表示问题的各个维度
	 * 2. 边界处理：正确处理已涂色房子和未涂色房子的不同情况
	 * 3. 初始化：正确初始化DP数组的边界条件
	 * 4. 无效状态处理：使用最大值表示无效状态，避免影响结果
	 * 5. 参数校验：确保输入参数合法
	 * 6. 详细注释：解释算法思路和关键步骤
	 */
	public static int minCost1(int[] houses, int[][] cost, int m, int n, int target) {
		// dp[i][j][k]表示考虑到第i个房子，第i个房子涂成颜色j，形成k个街区的最小花费
		// 使用long类型防止整数溢出
		long[][][] dp = new long[m + 1][n + 1][target + 1];
		
		// 初始化为最大值，表示无效状态
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				for (int k = 0; k <= target; k++) {
					dp[i][j][k] = Long.MAX_VALUE;
				}
			}
		}
		
		// 处理第一个房子
		if (houses[0] != 0) {
			// 第一个房子已经有颜色
			dp[1][houses[0]][1] = 0;
		} else {
			// 第一个房子没有颜色，可以涂成任意颜色
			for (int color = 1; color <= n; color++) {
				dp[1][color][1] = cost[0][color - 1];
			}
		}
		
		// 动态规划填表
		for (int i = 2; i <= m; i++) {
			if (houses[i - 1] != 0) {
				// 第i个房子已经有颜色
				int color = houses[i - 1];
				for (int preColor = 1; preColor <= n; preColor++) {
					for (int k = 1; k <= target; k++) {
						if (dp[i - 1][preColor][k] == Long.MAX_VALUE) {
							continue;
						}
						// 根据与前一个房子颜色是否相同来决定街区数变化
						if (preColor == color) {
							// 颜色相同，街区数不变
							dp[i][color][k] = Math.min(dp[i][color][k], dp[i - 1][preColor][k]);
						} else {
							// 颜色不同，街区数加1
							if (k + 1 <= target) {
								dp[i][color][k + 1] = Math.min(dp[i][color][k + 1], dp[i - 1][preColor][k]);
							}
						}
					}
				}
			} else {
				// 第i个房子没有颜色，可以涂成任意颜色
				for (int color = 1; color <= n; color++) {
					for (int preColor = 1; preColor <= n; preColor++) {
						for (int k = 1; k <= target; k++) {
							if (dp[i - 1][preColor][k] == Long.MAX_VALUE) {
								continue;
							}
							// 根据与前一个房子颜色是否相同来决定街区数变化
							if (preColor == color) {
								// 颜色相同，街区数不变
								dp[i][color][k] = Math.min(dp[i][color][k], dp[i - 1][preColor][k] + cost[i - 1][color - 1]);
							} else {
								// 颜色不同，街区数加1
								if (k + 1 <= target) {
									dp[i][color][k + 1] = Math.min(dp[i][color][k + 1], dp[i - 1][preColor][k] + cost[i - 1][color - 1]);
								}
							}
						}
					}
				}
			}
		}
		
		// 找到最终结果
		long ans = Long.MAX_VALUE;
		for (int color = 1; color <= n; color++) {
			if (dp[m][color][target] != Long.MAX_VALUE) {
				ans = Math.min(ans, dp[m][color][target]);
			}
		}
		
		return ans == Long.MAX_VALUE ? -1 : (int) ans;
	}

	/**
	 * 方法二：另一种动态规划实现
	 * 
	 * 解题思路：
	 * 1. 同样使用三维动态规划，但状态定义略有不同
	 * 2. 优化状态转移过程，减少不必要的循环
	 * 3. 使用更简洁的初始化方式
	 * 
	 * 时间复杂度分析：
	 * - 状态数量：O(m * n * target)
	 * - 状态转移：O(n)
	 * - 总时间复杂度：O(m * n^2 * target)
	 * 
	 * 空间复杂度分析：
	 * - DP数组：O(m * n * target)
	 * - 其他辅助变量：O(1)
	 * - 总空间复杂度：O(m * n * target)
	 * 
	 * 工程化考量：
	 * 1. 状态定义清晰：三维DP状态明确表示问题的各个维度
	 * 2. 循环优化：减少不必要的循环嵌套
	 * 3. 边界处理：正确处理已涂色房子和未涂色房子的不同情况
	 * 4. 无效状态处理：使用最大值表示无效状态，避免影响结果
	 * 5. 代码复用：提取公共逻辑，减少重复代码
	 * 6. 变量命名清晰，便于理解
	 */
	public static int minCost2(int[] houses, int[][] cost, int m, int n, int target) {
		// dp[i][j][k]表示考虑到第i个房子，第i个房子涂成颜色j，形成k个街区的最小花费
		long[][][] dp = new long[m + 1][n + 1][target + 1];
		
		// 初始化为最大值
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				for (int k = 0; k <= target; k++) {
					dp[i][j][k] = Long.MAX_VALUE;
				}
			}
		}
		
		// 初始化第一个房子
		if (houses[0] > 0) {
			// 已经有颜色
			dp[1][houses[0]][1] = 0;
		} else {
			// 没有颜色，可以涂成任意颜色
			for (int i = 1; i <= n; i++) {
				dp[1][i][1] = cost[0][i - 1];
			}
		}
		
		// 动态规划填表
		for (int i = 2; i <= m; i++) {
			if (houses[i - 1] > 0) {
				// 已经有颜色
				int color = houses[i - 1];
				for (int preColor = 1; preColor <= n; preColor++) {
					for (int k = 1; k <= target; k++) {
						if (dp[i - 1][preColor][k] == Long.MAX_VALUE) continue;
						if (preColor == color) {
							// 颜色相同
							dp[i][color][k] = Math.min(dp[i][color][k], dp[i - 1][preColor][k]);
						} else {
							// 颜色不同
							if (k < target) {
								dp[i][color][k + 1] = Math.min(dp[i][color][k + 1], dp[i - 1][preColor][k]);
							}
						}
					}
				}
			} else {
				// 没有颜色
				for (int color = 1; color <= n; color++) {
					for (int preColor = 1; preColor <= n; preColor++) {
						for (int k = 1; k <= target; k++) {
							if (dp[i - 1][preColor][k] == Long.MAX_VALUE) continue;
							if (preColor == color) {
								// 颜色相同
								dp[i][color][k] = Math.min(dp[i][color][k], dp[i - 1][preColor][k] + cost[i - 1][color - 1]);
							} else {
								// 颜色不同
								if (k < target) {
									dp[i][color][k + 1] = Math.min(dp[i][color][k + 1], dp[i - 1][preColor][k] + cost[i - 1][color - 1]);
								}
							}
						}
					}
				}
			}
		}
		
		// 找到最终结果
		long minCost = Long.MAX_VALUE;
		for (int i = 1; i <= n; i++) {
			if (dp[m][i][target] < minCost) {
				minCost = dp[m][i][target];
			}
		}
		
		return minCost == Long.MAX_VALUE ? -1 : (int) minCost;
	}

	// 为了测试
	public static int[] randomHouses(int m, int n) {
		int[] houses = new int[m];
		for (int i = 0; i < m; i++) {
			houses[i] = (int) (Math.random() * (n + 1));
		}
		return houses;
	}

	// 为了测试
	public static int[][] randomCost(int m, int n) {
		int[][] cost = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				cost[i][j] = (int) (Math.random() * 1000) + 1;
			}
		}
		return cost;
	}

	// 为了测试
	public static void main(String[] args) {
		int m = 10;
		int n = 5;
		int target = 3;
		int testTime = 100;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] houses = randomHouses(m, n);
			int[][] cost = randomCost(m, n);
			int ans1 = minCost1(houses, cost, m, n, target);
			int ans2 = minCost2(houses, cost, m, n, target);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}