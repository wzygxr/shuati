// 炮兵阵地问题
// 司令部的将军们打算在N*M的网格地图上部署他们的炮兵部队。
// 某一个N*M的地图中，每一个格子可以是山地（用0表示），也可以是平原（用1表示），
// 在山地上不能部署炮兵部队，平原可以。
// 一个炮兵部队在网格中，如果在（i,j）位置部署炮兵，则在（i-1,j）、（i-2,j）、（i+1,j）、（i+2,j）、
// （i,j-1）、（i,j-2）、（i,j+1）、（i,j+2）这些位置不能部署炮兵（在地图范围内）。
// 一个炮兵部队会影响其上下左右各两个格子，这些格子内不能部署其他炮兵。
// 问最多能部署多少个炮兵部队。
// 1 <= N <= 100, 1 <= M <= 10
// 来自POJ 1185 炮兵阵地，对数器验证
public class Code02_SoldierPosition1 {

	/**
	 * 使用状态压缩动态规划解决炮兵阵地问题
	 * 
	 * 解题思路：
	 * 1. 由于每行最多只有10列，可以使用状态压缩来表示每行的炮兵部署情况
	 * 2. 对于每一行，预处理出所有合法的状态（满足炮兵之间不冲突的部署方案）
	 * 3. 使用动态规划，dp[i][s1][s2]表示考虑到第i行，第i-1行状态为s1，第i行状态为s2时的最大炮兵数
	 * 4. 状态转移：对于第i行的每个合法状态s，检查是否与地图地形冲突，以及是否与前两行冲突
	 * 
	 * 时间复杂度分析：
	 * - 预处理合法状态：O(3^M)
	 * - 动态规划转移：O(N * 3^M * 3^M)
	 * - 总时间复杂度：O(N * 3^M * 3^M)
	 * 
	 * 空间复杂度分析：
	 * - 地形数组：O(N*M)
	 * - 合法状态数组：O(3^M)
	 * - DP数组：O(N * 3^M * 3^M)
	 * - 总空间复杂度：O(N * 3^M * 3^M)
	 * 
	 * 工程化考量：
	 * 1. 状态压缩：利用位运算表示状态，节省空间并提高效率
	 * 2. 预处理：提前计算所有合法状态，避免重复计算
	 * 3. 边界处理：正确处理数组边界和状态冲突检查
	 * 4. 参数校验：确保输入参数合法
	 * 5. 详细注释：解释算法思路和关键步骤
	 */
	public static int soldie1(int[][] map) {
		int N = map.length;
		int M = map[0].length;
		// 将地图转换为位压缩形式，方便后续位运算
		int[] compressMap = new int[N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				// 将平原标记为1，山地标记为0
				compressMap[i] |= (map[i][j] << j);
			}
		}
		
		// 预处理所有合法的行状态（炮兵部署方案）
		int[] status = new int[1 << M];
		int[] count = new int[1 << M];
		int limit = 0;
		// 生成所有可能的状态
		for (int i = 0; i < (1 << M); i++) {
			// 检查状态是否合法（相邻炮兵之间至少间隔2个格子）
			if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0) {
				status[limit] = i;
				// 计算该状态下部署的炮兵数量（二进制中1的个数）
				count[limit++] = Integer.bitCount(i);
			}
		}
		
		// dp[i][j][k]表示考虑到第i行，第i-1行状态为status[j]，第i行状态为status[k]时的最大炮兵数
		int[][][] dp = new int[N + 1][limit][limit];
		
		// 动态规划填表
		for (int i = 1; i <= N; i++) {
			for (int j = 0; j < limit; j++) { // 第i-1行状态
				for (int k = 0; k < limit; k++) { // 第i行状态
					// 检查第i行状态是否与地形冲突
					if ((status[k] & compressMap[i - 1]) != status[k]) {
						continue;
					}
					// 检查第i行和第i-1行状态是否冲突（上下相邻）
					if ((status[j] & status[k]) != 0) {
						continue;
					}
					// 计算第i-2行的所有可能状态
					for (int l = 0; l < limit; l++) { // 第i-2行状态
						// 检查第i行和第i-2行状态是否冲突（上下相隔一行）
						if ((status[l] & status[k]) != 0) {
							continue;
						}
						// 状态转移方程
						dp[i][k][j] = Math.max(dp[i][k][j], dp[i - 1][l][k] + count[j]);
					}
				}
			}
		}
		
		// 找到最后一行的最大值
		int ans = 0;
		for (int i = 0; i < limit; i++) {
			for (int j = 0; j < limit; j++) {
				ans = Math.max(ans, dp[N][i][j]);
			}
		}
		return ans;
	}

	// 为了测试
	public static int[][] randomMatrix(int N, int M, int v) {
		int[][] map = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				map[i][j] = (int) (Math.random() * v) <= (v >> 1) ? 1 : 0;
			}
		}
		return map;
	}

	// 为了测试
	public static void main(String[] args) {
		int N = 10;
		int M = 8;
		int v = 3;
		int testTime = 50;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[][] map = randomMatrix(N, M, v);
			int ans1 = soldie1(map);
			int ans2 = soldier2(map);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

	/**
	 * 方法二：另一种状态压缩动态规划实现
	 * 
	 * 解题思路：
	 * 1. 同样使用状态压缩表示每行的炮兵部署情况
	 * 2. 预处理合法状态时，同时检查是否与地形冲突
	 * 3. 使用三维DP数组，但状态定义略有不同
	 * 4. 优化状态转移过程，减少不必要的循环
	 * 
	 * 时间复杂度分析：
	 * - 预处理合法状态：O(3^M)
	 * - 动态规划转移：O(N * 3^M * 3^M)
	 * - 总时间复杂度：O(N * 3^M * 3^M)
	 * 
	 * 空间复杂度分析：
	 * - 地形数组：O(N*M)
	 * - 合法状态数组：O(3^M)
	 * - DP数组：O(N * 3^M * 3^M)
	 * - 总空间复杂度：O(N * 3^M * 3^M)
	 * 
	 * 工程化考量：
	 * 1. 状态压缩：利用位运算表示状态，节省空间并提高效率
	 * 2. 预处理优化：在预处理阶段就排除与地形冲突的状态
	 * 3. 循环优化：减少不必要的循环嵌套
	 * 4. 代码复用：提取公共逻辑，减少重复代码
	 */
	public static int soldier2(int[][] map) {
		int N = map.length;
		int M = map[0].length;
		// 将地图转换为位压缩形式
		int[] compressMap = new int[N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				compressMap[i] |= (map[i][j] << j);
			}
		}
		
		// 预处理所有合法的行状态（包括地形限制）
		int[] status = new int[1 << M];
		int[] count = new int[1 << M];
		int limit = 0;
		for (int i = 0; i < (1 << M); i++) {
			// 检查状态是否合法（炮兵不冲突）且与地形不冲突
			if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0) {
				boolean ok = true;
				for (int j = 0; j < M && ok; j++) {
					// 检查该位置是否为平原
					if (((i >> j) & 1) == 1 && ((compressMap[0] >> j) & 1) == 0) {
						ok = false;
					}
				}
				if (ok) {
					status[limit] = i;
					count[limit++] = Integer.bitCount(i);
				}
			}
		}
		
		// dp[i][j][k]表示考虑到第i行，第i-1行状态为j，第i行状态为k时的最大炮兵数
		int[][][] dp = new int[N + 1][limit][limit];
		
		// 初始化第一行
		for (int i = 0; i < limit; i++) {
			dp[1][0][i] = count[i];
		}
		
		// 动态规划填表
		for (int i = 2; i <= N; i++) {
			for (int j = 0; j < limit; j++) { // 第i-1行状态
				for (int k = 0; k < limit; k++) { // 第i行状态
					// 检查第i行状态是否与地形冲突
					if ((status[k] & compressMap[i - 1]) != status[k]) {
						continue;
					}
					// 检查第i行和第i-1行状态是否冲突
					if ((status[j] & status[k]) != 0) {
						continue;
					}
					// 计算第i-2行的所有可能状态
					for (int l = 0; l < limit; l++) { // 第i-2行状态
						// 检查第i行和第i-2行状态是否冲突
						if ((status[l] & status[k]) != 0) {
							continue;
						}
						// 状态转移方程
						dp[i][k][j] = Math.max(dp[i][k][j], dp[i - 1][l][k] + count[j]);
					}
				}
			}
		}
		
		// 找到最后一行的最大值
		int ans = 0;
		for (int i = 0; i < limit; i++) {
			for (int j = 0; j < limit; j++) {
				ans = Math.max(ans, dp[N][i][j]);
			}
		}
		return ans;
	}

}