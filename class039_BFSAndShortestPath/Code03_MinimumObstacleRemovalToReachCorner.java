package class062;

import java.util.ArrayDeque;

// 到达角落需要移除障碍物的最小数目
// 给你一个下标从 0 开始的二维整数数组 grid ，数组大小为 m x n
// 每个单元格都是两个值之一：
// 0 表示一个 空 单元格，
// 1 表示一个可以移除的 障碍物
// 你可以向上、下、左、右移动，从一个空单元格移动到另一个空单元格。
// 现在你需要从左上角 (0, 0) 移动到右下角 (m - 1, n - 1) 
// 返回需要移除的障碍物的最小数目
// 测试链接 : https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/
// 
// 算法思路：
// 这是一个典型的0-1 BFS问题
// 将网格看作图，每个单元格是一个节点
// 如果移动到空单元格(0)，边权为0
// 如果移动到障碍物单元格(1)，边权为1（需要移除障碍物）
// 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数
// 空间复杂度：O(m * n)，用于存储距离数组和队列
// 
// 工程化考量：
// 1. 使用ArrayDeque作为双端队列
// 2. 使用distance数组记录到每个点的最小移除障碍物数目
// 3. 通过比较新路径和已有路径的权重来决定是否更新
public class Code03_MinimumObstacleRemovalToReachCorner {

	// 0-1 BFS解法
	public static int minimumObstacles(int[][] grid) {
		// 四个方向的移动：上、右、下、左
		int[] move = { -1, 0, 1, 0, -1 };
		int m = grid.length;
		int n = grid[0].length;
		
		// distance[i][j]表示从起点(0,0)到(i,j)需要移除的障碍物最小数目
		int[][] distance = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				distance[i][j] = Integer.MAX_VALUE;
			}
		}
		
		// 双端队列，用于0-1 BFS
		ArrayDeque<int[]> deque = new ArrayDeque<>();
		deque.addFirst(new int[] { 0, 0 });
		distance[0][0] = 0;
		
		while (!deque.isEmpty()) {
			// 从队首取出节点
			int[] record = deque.pollFirst();
			int x = record[0];
			int y = record[1];
			
			// 如果到达终点
			if (x == m - 1 && y == n - 1) {
				return distance[x][y];
			}
			
			// 向四个方向扩展
			for (int i = 0; i < 4; i++) {
				int nx = x + move[i], ny = y + move[i + 1];
				// 检查边界
				if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
					// 计算移动到新位置需要增加的权重（0或1）
					int weight = grid[nx][ny];
					// 如果新路径更优
					if (distance[x][y] + weight < distance[nx][ny]) {
						distance[nx][ny] = distance[x][y] + weight;
						// 根据权重决定放在队首还是队尾
						if (weight == 0) {
							deque.addFirst(new int[] { nx, ny });
						} else {
							deque.addLast(new int[] { nx, ny });
						}
					}
				}
			}
		}
		return -1;
	}

}