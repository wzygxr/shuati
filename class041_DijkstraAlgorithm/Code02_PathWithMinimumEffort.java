package class064;

import java.util.PriorityQueue;

/**
 * 最小体力消耗路径
 * 
 * 题目链接：https://leetcode.cn/problems/path-with-minimum-effort/
 * 
 * 题目描述：
 * 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights，
 * 其中 heights[row][col] 表示格子 (row, col) 的高度。
 * 一开始你在最左上角的格子 (0, 0) ，且你希望去最右下角的格子 (rows-1, columns-1) 
 * （注意下标从 0 开始编号）。你每次可以往 上，下，左，右 四个方向之一移动。
 * 你想要找到耗费 体力 最小的一条路径。
 * 一条路径耗费的体力值是路径上，相邻格子之间高度差绝对值的最大值。
 * 请你返回从左上角走到右下角的最小 体力消耗值。
 * 
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的"距离"定义为路径上相邻格子高度差绝对值的最大值。
 * 我们将每个格子看作图中的一个节点，相邻格子之间有边连接，边的权重是高度差的绝对值。
 * 使用Dijkstra算法找到从起点到终点的最小体力消耗路径。
 * 
 * 算法应用场景：
 * - 地形路径规划
 * - 网络传输中的最大延迟路径
 * - 游戏中的角色移动路径优化
 * 
 * 时间复杂度分析：
 * O(mn*log(mn)) 其中m和n分别是地图的行数和列数
 * 
 * 空间复杂度分析：
 * O(mn) 存储距离数组和访问标记数组
 */
public class Code02_PathWithMinimumEffort {

	// 方向数组：0:上，1:右，2:下，3:左
	// 通过这种方式可以简化四个方向的遍历
	// move[i]和move[i+1]组成一个方向向量
	public static int[] move = new int[] { -1, 0, 1, 0, -1 };

	/**
	 * 使用Dijkstra算法求解最小体力消耗路径
	 * 
	 * 算法核心思想：
	 * 1. 将问题转化为图论中的最短路径问题
	 * 2. 每个格子是一个节点，相邻格子之间有边连接
	 * 3. 边的权重定义为相邻格子高度差的绝对值
	 * 4. 路径的体力消耗定义为路径上所有边权重的最大值
	 * 5. 使用Dijkstra算法找到从起点到终点的最小体力消耗路径
	 * 
	 * 算法步骤：
	 * 1. 初始化距离数组，起点距离为0，其他点为无穷大
	 * 2. 使用优先队列维护待处理节点，按体力消耗从小到大排序
	 * 3. 不断取出体力消耗最小的节点，更新其邻居节点的最小体力消耗
	 * 4. 当处理到终点时，直接返回结果（剪枝优化）
	 * 
	 * 时间复杂度：O(mn*log(mn)) 其中m和n分别是地图的行数和列数
	 * 空间复杂度：O(mn)
	 * 
	 * @param heights 二维地图，heights[i][j]表示格子(i,j)的高度
	 * @return 从左上角走到右下角的最小体力消耗值
	 */
	public int minimumEffortPath(int[][] heights) {
		// (0,0)源点
		// -> (x,y)
		int n = heights.length;      // 地图行数
		int m = heights[0].length;   // 地图列数
		
		// distance[i][j]表示从起点(0,0)到点(i,j)的最小体力消耗
		// 初始化为最大值，表示尚未访问
		int[][] distance = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				distance[i][j] = Integer.MAX_VALUE;
			}
		}
		// 起点体力消耗为0
		distance[0][0] = 0;
		
		// visited[i][j]表示点(i,j)是否已经确定了最短路径
		// 用于避免重复处理已经确定最短路径的节点
		boolean[][] visited = new boolean[n][m];
		
		// 优先队列，按体力消耗从小到大排序
		// 数组含义：[0] 格子的行, [1] 格子的列, [2] 源点到当前格子的代价
		PriorityQueue<int[]> heap = new PriorityQueue<int[]>((a, b) -> a[2] - b[2]);
		// 将起点加入优先队列，体力消耗为0
		heap.add(new int[] { 0, 0, 0 });
		
		// Dijkstra算法主循环
		while (!heap.isEmpty()) {
			// 取出体力消耗最小的节点
			int[] record = heap.poll();
			int x = record[0];   // 当前行
			int y = record[1];   // 当前列
			int c = record[2];   // 当前体力消耗
			
			// 如果已经处理过，跳过
			// 这是为了避免同一节点多次处理导致的重复计算
			if (visited[x][y]) {
				continue;
			}
			
			// 如果到达终点，直接返回结果
			// 常见剪枝优化：发现终点直接返回，不用等都结束
			// 这是因为Dijkstra算法的特性保证了第一次到达终点时就是最优解
			if (x == n - 1 && y == m - 1) {
				return c;
			}
			
			// 标记为已处理，表示已确定从起点到该点的最小体力消耗
			visited[x][y] = true;
			
			// 向四个方向扩展（上、右、下、左）
			for (int i = 0; i < 4; i++) {
				// 计算新位置的坐标
				int nx = x + move[i];     // 新行
				int ny = y + move[i + 1]; // 新列
				
				// 检查边界条件和是否已访问
				// 1. 新位置不能超出地图边界
				// 2. 新位置不能是已经处理过的节点
				if (nx >= 0 && nx < n && ny >= 0 && ny < m && !visited[nx][ny]) {
					// 计算通过当前路径到达新点的体力消耗
					// 注意：这里的体力消耗定义为路径上所有相邻格子高度差绝对值的最大值
					// 而不是简单的累加
					int nc = Math.max(c, Math.abs(heights[x][y] - heights[nx][ny]));
					
					// 如果新的体力消耗更小，则更新
					// 松弛操作：如果 nc < distance[nx][ny]，则更新distance[nx][ny]
					if (nc < distance[nx][ny]) {
						distance[nx][ny] = nc;
						// 将更新后的节点加入优先队列
						heap.add(new int[] { nx, ny, nc });
					}
				}
			}
		}
		// 理论上不会执行到这里，因为从左上角到右下角总是存在路径
		return -1;
	}

}