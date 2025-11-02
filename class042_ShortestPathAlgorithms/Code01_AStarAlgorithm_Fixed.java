import java.util.*;

/**
 * A*算法深度解析与多题目实现 - 修复版本
 * 
 * A*算法是一种启发式搜索算法，结合了Dijkstra算法的完备性和贪心最佳优先搜索的高效性
 * 核心公式: f(n) = g(n) + h(n)
 * 其中:
 * - f(n): 从初始状态经由状态n到目标状态的估计代价
 * - g(n): 在状态空间中从初始状态到状态n的实际代价
 * - h(n): 从状态n到目标状态的最佳路径的估计代价（启发函数）
 * 
 * 时间复杂度分析:
 * - 平均情况: O(b^d)，其中b是分支因子，d是解的深度
 * - 最坏情况: O(|V||E|)，退化为Dijkstra算法
 * - 空间复杂度: O(|V|)，需要存储开放列表和关闭列表
 * 
 * 关键特性:
 * 1. 可采纳性: 启发函数h(n)必须满足h(n) ≤ h*(n)，保证找到最优解
 * 2. 一致性: 对于任意节点n和其后继节点n'，满足h(n) ≤ c(n,n') + h(n')
 * 3. 最优性: 当启发函数可采纳时，A*算法保证找到最优解
 */
public class Code01_AStarAlgorithm_Fixed {

	// 方向数组：上、右、下、左（四方向移动）
	// 用于网格搜索中的相邻位置计算
	public static int[] move = new int[] { -1, 0, 1, 0, -1 };

	// 八方向移动数组（用于可以斜向移动的场景）
	public static int[] move8 = new int[] { -1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1 };

	/**
	 * Dijkstra算法实现 - 作为A*算法的对比基准
	 * 
	 * 算法特点:
	 * - 保证找到最短路径（在非负权图中）
	 * - 使用优先队列优化，每次扩展距离最小的节点
	 * - 适用于任意非负权图，不依赖启发函数
	 * 
	 * 时间复杂度: O(N*M*log(N*M))，其中N和M是网格的行数和列数
	 * 空间复杂度: O(N*M)，用于存储距离矩阵和访问标记
	 * 
	 * @param grid 网格地图，0表示障碍，1表示通路
	 * @param startX 起点行坐标
	 * @param startY 起点列坐标  
	 * @param targetX 目标行坐标
	 * @param targetY 目标列坐标
	 * @return 最短路径长度，如果不可达返回-1
	 */
	public static int minDistance1(int[][] grid, int startX, int startY, int targetX, int targetY) {
		// 边界检查：起点或终点为障碍物
		if (grid[startX][startY] == 0 || grid[targetX][targetY] == 0) {
			return -1;
		}
		
		int n = grid.length;
		int m = grid[0].length;
		
		// 距离矩阵初始化：记录从起点到每个位置的最短距离
		int[][] distance = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				distance[i][j] = Integer.MAX_VALUE;
			}
		}
		distance[startX][startY] = 1; // 起点距离为1（步数计数）
		
		// 访问标记矩阵：避免重复处理同一位置
		boolean[][] visited = new boolean[n][m];
		
		// 优先队列：按距离从小到大排序，用于选择下一个扩展节点
		// 存储格式: [行坐标, 列坐标, 当前距离]
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[2] - b[2]);
		heap.add(new int[] { startX, startY, 1 });
		
		while (!heap.isEmpty()) {
			int[] cur = heap.poll();
			int x = cur[0];
			int y = cur[1];
			
			// 跳过已访问的节点（由于优先队列中可能有重复节点）
			if (visited[x][y]) {
				continue;
			}
			visited[x][y] = true;
			
			// 到达目标位置，返回最短距离
			if (x == targetX && y == targetY) {
				return distance[x][y];
			}
			
			// 探索四个方向
			for (int i = 0; i < 4; i++) {
				int nx = x + move[i];
				int ny = y + move[i + 1];
				
				// 检查新位置是否有效且可达
				if (nx >= 0 && nx < n && ny >= 0 && ny < m && 
					grid[nx][ny] == 1 && !visited[nx][ny] &&
					distance[x][y] + 1 < distance[nx][ny]) {
					
					distance[nx][ny] = distance[x][y] + 1;
					heap.add(new int[] { nx, ny, distance[x][y] + 1 });
				}
			}
		}
		
		return -1; // 无法到达目标位置
	}

	/**
	 * A*算法实现 - 启发式搜索优化版本
	 * 
	 * 算法核心思想:
	 * - 使用估价函数f(n) = g(n) + h(n)指导搜索方向
	 * - g(n): 从起点到当前节点的实际代价
	 * - h(n): 从当前节点到目标节点的估计代价（启发函数）
	 * - 优先扩展f(n)值最小的节点，引导搜索向目标方向进行
	 * 
	 * 相比Dijkstra算法的优势:
	 * - 搜索方向更有针对性，减少不必要的节点扩展
	 * - 在大多数情况下能更快找到最优解
	 * - 特别适合有明确目标位置的路径规划问题
	 * 
	 * 时间复杂度: O(N*M*log(N*M))，其中N和M是网格的行数和列数
	 * 空间复杂度: O(N*M)，用于存储距离矩阵和访问标记
	 * 
	 * @param grid 网格地图，0表示障碍，1表示通路
	 * @param startX 起点行坐标
	 * @param startY 起点列坐标  
	 * @param targetX 目标行坐标
	 * @param targetY 目标列坐标
	 * @return 最短路径长度，如果不可达返回-1
	 */
	public static int minDistance2(int[][] grid, int startX, int startY, int targetX, int targetY) {
		// 边界检查：起点或终点为障碍物
		if (grid[startX][startY] == 0 || grid[targetX][targetY] == 0) {
			return -1;
		}
		
		int n = grid.length;
		int m = grid[0].length;
		
		// 距离矩阵初始化：记录从起点到每个位置的最短距离
		int[][] distance = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				distance[i][j] = Integer.MAX_VALUE;
			}
		}
		distance[startX][startY] = 1; // 起点距离为1（步数计数）
		
		// 访问标记矩阵：避免重复处理同一位置
		boolean[][] visited = new boolean[n][m];
		
		// A*算法的优先队列：按f(n) = g(n) + h(n)排序
		// 存储格式: [行坐标, 列坐标, f(n)值]
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[2] - b[2]);
		
		// 初始状态：起点加入队列，f(n) = g(n) + h(n) = 1 + 曼哈顿距离
		int initialF = 1 + manhattanDistance(startX, startY, targetX, targetY);
		heap.add(new int[] { startX, startY, initialF });
		
		while (!heap.isEmpty()) {
			int[] cur = heap.poll();
			int x = cur[0];
			int y = cur[1];
			
			// 跳过已访问的节点
			if (visited[x][y]) {
				continue;
			}
			visited[x][y] = true;
			
			// 到达目标位置，返回最短距离
			if (x == targetX && y == targetY) {
				return distance[x][y];
			}
			
			// 探索四个方向
			for (int i = 0; i < 4; i++) {
				int nx = x + move[i];
				int ny = y + move[i + 1];
				
				// 检查新位置是否有效且可达
				if (nx >= 0 && nx < n && ny >= 0 && ny < m && 
					grid[nx][ny] == 1 && !visited[nx][ny] &&
					distance[x][y] + 1 < distance[nx][ny]) {
					
					// 更新实际距离g(n)
					distance[nx][ny] = distance[x][y] + 1;
					
					// 计算新的f(n)值 = g(n) + h(n)
					int newF = distance[nx][ny] + manhattanDistance(nx, ny, targetX, targetY);
					heap.add(new int[] { nx, ny, newF });
				}
			}
		}
		
		return -1; // 无法到达目标位置
	}

	/**
	 * 曼哈顿距离启发函数 - 适用于四方向移动的网格
	 * 
	 * 数学公式: h(n) = |x₁ - x₂| + |y₁ - y₂|
	 * 
	 * 特性分析:
	 * - 可采纳性: 曼哈顿距离永远不会高估实际距离，保证A*算法找到最优解
	 * - 一致性: 满足三角不等式，保证算法的高效性
	 * - 计算效率: 只涉及绝对值运算，计算速度快
	 * 
	 * 适用场景: 只能上下左右移动的网格环境（如标准迷宫、城市网格道路）
	 * 
	 * @param x1 当前点x坐标
	 * @param y1 当前点y坐标
	 * @param x2 目标点x坐标
	 * @param y2 目标点y坐标
	 * @return 曼哈顿距离估计值
	 */
	public static int manhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	
	/**
	 * 切比雪夫距离启发函数 - 适用于八方向移动的网格
	 * 
	 * 数学公式: h(n) = max(|x₁ - x₂|, |y₁ - y₂|)
	 * 
	 * 特性分析:
	 * - 可采纳性: 在允许对角线移动时，切比雪夫距离是可采纳的
	 * - 一致性: 满足一致性条件，保证算法正确性
	 * - 计算效率: 涉及最大值运算，计算速度较快
	 * 
	 * 适用场景: 可以斜向移动的网格环境（如国际象棋中的王移动）
	 * 
	 * @param x1 当前点x坐标
	 * @param y1 当前点y坐标
	 * @param x2 目标点x坐标
	 * @param y2 目标点y坐标
	 * @return 切比雪夫距离估计值
	 */
	public static int chebyshevDistance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}
	
	/**
	 * 欧几里得距离启发函数 - 适用于连续空间任意方向移动
	 * 
	 * 数学公式: h(n) = √((x₁ - x₂)² + (y₁ - y₂)²)
	 * 
	 * 特性分析:
	 * - 可采纳性: 在连续空间中，欧式距离是可采纳的
	 * - 精确性: 提供最准确的距离估计
	 * - 计算成本: 涉及平方和开方运算，计算成本较高
	 * 
	 * 适用场景: 连续空间中的路径规划（如机器人导航、游戏中的自由移动）
	 * 
	 * @param x1 当前点x坐标
	 * @param y1 当前点y坐标
	 * @param x2 目标点x坐标
	 * @param y2 目标点y坐标
	 * @return 欧几里得距离估计值
	 */
	public static double euclideanDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	/**
	 * 对角线距离启发函数 - 八方向移动的优化版本
	 * 
	 * 数学公式: h(n) = D × max(|x₁ - x₂|, |y₁ - y₂|) + (D₂ - 2D) × min(|x₁ - x₂|, |y₁ - y₂|)
	 * 其中D是直线移动代价，D₂是对角线移动代价
	 * 
	 * 特性分析:
	 * - 精确性: 比切比雪夫距离更精确地估计实际代价
	 * - 适用性: 特别适合对角线移动代价与直线移动代价不同的场景
	 * 
	 * @param x1 当前点x坐标
	 * @param y1 当前点y坐标
	 * @param x2 目标点x坐标
	 * @param y2 目标点y坐标
	 * @param straightCost 直线移动代价
	 * @param diagonalCost 对角线移动代价
	 * @return 对角线距离估计值
	 */
	public static int diagonalDistance(int x1, int y1, int x2, int y2, int straightCost, int diagonalCost) {
		int dx = Math.abs(x1 - x2);
		int dy = Math.abs(y1 - y2);
		return straightCost * (dx + dy) + (diagonalCost - 2 * straightCost) * Math.min(dx, dy);
	}

	/**
	 * 生成随机网格用于测试
	 * 
	 * 网格生成策略:
	 * - 障碍物概率: 30%的概率生成障碍物(0)
	 * - 通路概率: 70%的概率生成通路(1)
	 * - 保证起点和终点为通路（在测试中会特殊处理）
	 * 
	 * @param n 网格大小(n×n)
	 * @return 随机生成的网格数组
	 */
	public static int[][] randomGrid(int n) {
		int[][] grid = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (Math.random() < 0.3) {
					grid[i][j] = 0; // 30%概率生成障碍物
				} else {
					grid[i][j] = 1; // 70%概率生成通路
				}
			}
		}
		return grid;
	}
	
	/**
	 * 确保起点和终点为通路的网格生成
	 * 
	 * 在随机网格的基础上，强制设置起点和终点为通路
	 * 避免测试用例因起点或终点为障碍物而失效
	 * 
	 * @param n 网格大小
	 * @param startX 起点x坐标
	 * @param startY 起点y坐标
	 * @param targetX 目标x坐标
	 * @param targetY 目标y坐标
	 * @return 确保起点终点通路的网格
	 */
	public static int[][] randomGridWithGuaranteedPath(int n, int startX, int startY, int targetX, int targetY) {
		int[][] grid = randomGrid(n);
		grid[startX][startY] = 1; // 强制起点为通路
		grid[targetX][targetY] = 1; // 强制终点为通路
		return grid;
	}

	/**
	 * 主测试函数 - 验证Dijkstra和A*算法的正确性和性能
	 * 
	 * 测试策略:
	 * 1. 功能测试: 随机生成测试用例，验证两种算法结果一致性
	 * 2. 性能测试: 大规模网格测试，比较两种算法的运行时间
	 * 3. 边界测试: 测试特殊场景下的算法表现
	 */
	public static void main(String[] args) {
		// 功能测试配置
		int len = 10; // 最大网格尺寸
		int testTime = 100; // 测试用例数量
		
		System.out.println("=== A*算法功能测试开始 ===");
		int errorCount = 0;
		
		for (int i = 0; i < testTime; i++) {
			// 随机生成网格尺寸和起点终点
			int n = (int) (Math.random() * len) + 2; // 网格尺寸2-11
			int startX = (int) (Math.random() * n);
			int startY = (int) (Math.random() * n);
			int targetX = (int) (Math.random() * n);
			int targetY = (int) (Math.random() * n);
			
			// 确保起点和终点不同
			if (startX == targetX && startY == targetY) {
				i--; // 重新生成测试用例
				continue;
			}
			
			// 生成确保起点终点通路的网格
			int[][] grid = randomGridWithGuaranteedPath(n, startX, startY, targetX, targetY);
			
			// 分别运行Dijkstra和A*算法
			int dijkstraResult = minDistance1(grid, startX, startY, targetX, targetY);
			int aStarResult = minDistance2(grid, startX, startY, targetX, targetY);
			
			// 验证结果一致性
			if (dijkstraResult != aStarResult) {
				errorCount++;
				System.out.printf("测试用例%d出错: Dijkstra=%d, A*=%d, 网格大小=%dx%d%n", 
					i+1, dijkstraResult, aStarResult, n, n);
			}
		}
		
		System.out.printf("功能测试完成: 总测试用例%d个，错误%d个%n", testTime, errorCount);
		System.out.println("=== 功能测试结束 ===");

		// 性能测试
		System.out.println("=== A*算法性能测试开始 ===");
		int performanceGridSize = 50; // 性能测试网格尺寸
		int[][] performanceGrid = randomGrid(performanceGridSize);
		
		// 设置起点和终点（对角线位置，确保最长路径）
		int startX = 0;
		int startY = 0;
		int targetX = performanceGridSize - 1;
		int targetY = performanceGridSize - 1;
		
		// 确保起点终点为通路
		performanceGrid[startX][startY] = 1;
		performanceGrid[targetX][targetY] = 1;
		
		long startTime, endTime;
		
		// Dijkstra算法性能测试
		startTime = System.currentTimeMillis();
		int dijkstraResult = minDistance1(performanceGrid, startX, startY, targetX, targetY);
		endTime = System.currentTimeMillis();
		long dijkstraTime = endTime - startTime;
		
		// A*算法性能测试
		startTime = System.currentTimeMillis();
		int aStarResult = minDistance2(performanceGrid, startX, startY, targetX, targetY);
		endTime = System.currentTimeMillis();
		long aStarTime = endTime - startTime;
		
		System.out.printf("网格大小: %dx%d%n", performanceGridSize, performanceGridSize);
		System.out.printf("Dijkstra算法结果: %d, 耗时: %dms%n", dijkstraResult, dijkstraTime);
		System.out.printf("A*算法结果: %d, 耗时: %dms%n", aStarResult, aStarTime);
		System.out.printf("性能提升: %.2f%%%n", (dijkstraTime - aStarTime) * 100.0 / dijkstraTime);
		System.out.println("=== 性能测试结束 ===");
		
		// 边界测试
		System.out.println("=== 边界测试开始 ===");
		boundaryTests();
		System.out.println("=== 边界测试结束 ===");
	}
	
	/**
	 * 边界测试函数 - 测试算法在各种边界条件下的表现
	 * 
	 * 测试场景包括:
	 * 1. 起点即终点
	 * 2. 不可达的网格
	 * 3. 无障碍物的网格
	 * 4. 全障碍物的网格
	 */
	public static void boundaryTests() {
		System.out.println("1. 测试起点即终点:");
		int[][] grid1 = {{1}};
		int result1 = minDistance2(grid1, 0, 0, 0, 0);
		System.out.println("   结果: " + result1 + " (期望: 0)");
		
		System.out.println("2. 测试不可达网格:");
		int[][] grid2 = {{1, 0}, {0, 1}};
		int result2 = minDistance2(grid2, 0, 0, 1, 1);
		System.out.println("   结果: " + result2 + " (期望: -1)");
		
		System.out.println("3. 测试无障碍网格:");
		int[][] grid3 = {{1, 1}, {1, 1}};
		int result3 = minDistance2(grid3, 0, 0, 1, 1);
		System.out.println("   结果: " + result3 + " (期望: 2)");
		
		System.out.println("4. 测试全障碍网格:");
		int[][] grid4 = {{0, 0}, {0, 0}};
		int result4 = minDistance2(grid4, 0, 0, 1, 1);
		System.out.println("   结果: " + result4 + " (期望: -1)");
	}
}