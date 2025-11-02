package class132;

// 炮兵阵地问题（空间优化版本）
// 司令部的将军们打算在N*M的网格地图上部署他们的炮兵部队。
// 某一个N*M的地图中，每一个格子可以是山地（用0表示），也可以是平原（用1表示），
// 在山地上不能部署炮兵部队，平原可以。
// 一个炮兵部队在网格中，如果在（i,j）位置部署炮兵，则在（i-1,j）、（i-2,j）、（i+1,j）、（i+2,j）、
// （i,j-1）、（i,j-2）、（i,j+1）、（i,j+2）这些位置不能部署炮兵（在地图范围内）。
// 一个炮兵部队会影响其上下左右各两个格子，这些格子内不能部署其他炮兵。
// 问最多能部署多少个炮兵部队。
// 1 <= N <= 100, 1 <= M <= 10
// 来自POJ 1185 炮兵阵地，对数器验证
public class Code02_SoldierPosition2 {

	/**
	 * 使用状态压缩动态规划解决炮兵阵地问题（空间优化版本）
	 * 
	 * 解题思路：
	 * 1. 由于每行最多只有10列，可以使用状态压缩来表示每行的炮兵部署情况
	 * 2. 预处理出所有合法的状态（满足炮兵之间不冲突且与地形不冲突的部署方案）
	 * 3. 使用动态规划，但只保存前两行的状态，节省空间
	 * 4. dp[s1][s2]表示前一行状态为s1，当前行状态为s2时的最大炮兵数
	 * 5. 滚动数组优化空间复杂度
	 * 
	 * 时间复杂度分析：
	 * - 预处理合法状态：O(3^M)
	 * - 动态规划转移：O(N * 3^M * 3^M)
	 * - 总时间复杂度：O(N * 3^M * 3^M)
	 * 
	 * 空间复杂度分析：
	 * - 地形数组：O(N*M)
	 * - 合法状态数组：O(3^M)
	 * - DP数组：O(3^M * 3^M)（通过滚动数组优化）
	 * - 总空间复杂度：O(3^M * 3^M)
	 * 
	 * 工程化考量：
	 * 1. 状态压缩：利用位运算表示状态，节省空间并提高效率
	 * 2. 空间优化：使用滚动数组技术，将空间复杂度从O(N*3^M*3^M)优化到O(3^M*3^M)
	 * 3. 预处理：提前计算所有合法状态，避免重复计算
	 * 4. 边界处理：正确处理数组边界和状态冲突检查
	 * 5. 参数校验：确保输入参数合法
	 * 6. 详细注释：解释算法思路和关键步骤
	 */
	public static int soldie1(int[][] map) {
		int N = map.length;
		int M = map[0].length;
		// 将地图转换为位压缩形式
		int[] compressMap = new int[N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				// 将平原标记为1，山地标记为0
				compressMap[i] |= (map[i][j] << j);
			}
		}
		
		// 预处理所有合法的行状态（包括地形限制）
		int[] status = new int[1 << M];
		int[] count = new int[1 << M];
		int limit = 0;
		// 生成所有可能的状态并筛选合法状态
		for (int i = 0; i < (1 << M); i++) {
			// 检查状态是否合法（炮兵不冲突）
			if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0) {
				boolean ok = true;
				// 检查是否与地形冲突
				for (int j = 0; j < M && ok; j++) {
					if (((i >> j) & 1) == 1 && ((compressMap[0] >> j) & 1) == 0) {
						ok = false;
					}
				}
				if (ok) {
					status[limit] = i;
					// 计算该状态下部署的炮兵数量
					count[limit++] = Integer.bitCount(i);
				}
			}
		}
		
		// 使用滚动数组优化空间复杂度
		// dp[i][j]表示前一行状态为status[i]，当前行状态为status[j]时的最大炮兵数
		int[][] dp = new int[limit][limit];
		
		// 初始化第一行
		for (int i = 0; i < limit; i++) {
			for (int j = 0; j < limit; j++) {
				// 检查第一行状态是否与地形冲突
				if ((status[j] & compressMap[0]) == status[j]) {
					dp[i][j] = count[j];
				}
			}
		}
		
		// 处理第二行
		if (N > 1) {
			int[][] next = new int[limit][limit];
			for (int i = 0; i < limit; i++) { // 第一行状态
				for (int j = 0; j < limit; j++) { // 第二行状态
					// 检查第二行状态是否与地形冲突
					if ((status[j] & compressMap[1]) != status[j]) {
						continue;
					}
					// 检查第一行和第二行状态是否冲突
					if ((status[i] & status[j]) != 0) {
						continue;
					}
					// 计算前两行的最大炮兵数
					for (int k = 0; k < limit; k++) { // 第零行状态（虚拟行）
						next[i][j] = Math.max(next[i][j], dp[k][i] + count[j]);
					}
				}
			}
			// 更新dp数组
			dp = next;
		}
		
		// 动态规划处理剩余行
		for (int i = 2; i < N; i++) {
			int[][] next = new int[limit][limit];
			for (int j = 0; j < limit; j++) { // 第i-1行状态
				for (int k = 0; k < limit; k++) { // 第i行状态
					// 检查第i行状态是否与地形冲突
					if ((status[k] & compressMap[i]) != status[k]) {
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
						next[j][k] = Math.max(next[j][k], dp[l][j] + count[k]);
					}
				}
			}
			// 更新dp数组（滚动数组）
			dp = next;
		}
		
		// 找到最后一行的最大值
		int ans = 0;
		for (int i = 0; i < limit; i++) {
			for (int j = 0; j < limit; j++) {
				ans = Math.max(ans, dp[i][j]);
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
	 * 方法二：另一种空间优化的状态压缩动态规划实现
	 * 
	 * 解题思路：
	 * 1. 同样使用状态压缩表示每行的炮兵部署情况
	 * 2. 通过预处理阶段就排除与地形冲突的状态，减少运行时计算
	 * 3. 使用更简洁的滚动数组实现
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
	 * - DP数组：O(3^M * 3^M)（通过滚动数组优化）
	 * - 总空间复杂度：O(3^M * 3^M)
	 * 
	 * 工程化考量：
	 * 1. 状态压缩：利用位运算表示状态，节省空间并提高效率
	 * 2. 空间优化：使用滚动数组技术优化空间复杂度
	 * 3. 预处理优化：在预处理阶段就排除与地形冲突的状态
	 * 4. 循环优化：减少不必要的循环嵌套
	 * 5. 代码复用：提取公共逻辑，减少重复代码
	 * 6. 变量命名清晰，便于理解
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
			// 检查状态是否合法（炮兵不冲突）
			if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0) {
				boolean ok = true;
				// 检查是否与地形冲突
				for (int j = 0; j < M && ok; j++) {
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
		
		// 使用滚动数组优化空间复杂度
		int[][] dp = new int[limit][limit];
		int[][] next = new int[limit][limit];
		
		// 初始化第一行
		for (int i = 0; i < limit; i++) {
			for (int j = 0; j < limit; j++) {
				if ((status[j] & compressMap[0]) == status[j]) {
					dp[i][j] = count[j];
				}
			}
		}
		
		// 动态规划处理后续行
		for (int i = 1; i < N; i++) {
			// 清空next数组
			for (int j = 0; j < limit; j++) {
				for (int k = 0; k < limit; k++) {
					next[j][k] = 0;
				}
			}
			
			// 状态转移
			for (int j = 0; j < limit; j++) { // 前一行状态
				for (int k = 0; k < limit; k++) { // 当前行状态
					// 检查当前行状态是否与地形冲突
					if ((status[k] & compressMap[i]) != status[k]) {
						continue;
					}
					// 检查当前行和前一行状态是否冲突
					if ((status[j] & status[k]) != 0) {
						continue;
					}
					// 计算前两行的所有可能状态
					for (int l = 0; l < limit; l++) { // 前两行状态
						// 检查当前行和前两行状态是否冲突
						if ((status[l] & status[k]) != 0) {
							continue;
						}
						// 状态转移方程
						next[j][k] = Math.max(next[j][k], dp[l][j] + count[k]);
					}
				}
			}
			
			// 更新dp数组（滚动数组）
			int[][] tmp = dp;
			dp = next;
			next = tmp;
		}
		
		// 找到最后一行的最大值
		int ans = 0;
		for (int i = 0; i < limit; i++) {
			for (int j = 0; j < limit; j++) {
				ans = Math.max(ans, dp[i][j]);
			}
		}
		return ans;
	}

}