package class062;

import java.util.ArrayDeque;

// 使网格图至少有一条有效路径的最小代价
// 给你一个 m * n 的网格图 grid 。 grid 中每个格子都有一个数字
// 对应着从该格子出发下一步走的方向。 grid[i][j] 中的数字可能为以下几种情况：
// 1 ，下一步往右走，也就是你会从 grid[i][j] 走到 grid[i][j + 1]
// 2 ，下一步往左走，也就是你会从 grid[i][j] 走到 grid[i][j - 1]
// 3 ，下一步往下走，也就是你会从 grid[i][j] 走到 grid[i + 1][j]
// 4 ，下一步往上走，也就是你会从 grid[i][j] 走到 grid[i - 1][j]
// 注意网格图中可能会有 无效数字 ，因为它们可能指向 grid 以外的区域
// 一开始，你会从最左上角的格子 (0,0) 出发
// 我们定义一条 有效路径 为从格子 (0,0) 出发，每一步都顺着数字对应方向走
// 最终在最右下角的格子 (m - 1, n - 1) 结束的路径
// 有效路径 不需要是最短路径
// 你可以花费1的代价修改一个格子中的数字，但每个格子中的数字 只能修改一次
// 请你返回让网格图至少有一条有效路径的最小代价
// 测试链接 : https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
// 
// 算法思路：
// 这也是一个0-1 BFS问题
// 将网格看作图，每个单元格是一个节点
// 如果按照原有方向移动，边权为0（不需要修改）
// 如果改变方向移动，边权为1（需要修改，花费1的代价）
// 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数
// 空间复杂度：O(m * n)，用于存储距离数组和队列
// 
// 工程化考量：
// 1. 使用ArrayDeque作为双端队列
// 2. 使用distance数组记录到每个点的最小修改次数
// 3. 通过比较新路径和已有路径的权重来决定是否更新
public class Code04_MinimumCostToMakeAtLeastOneValidPath {

	// 0-1 BFS解法
	public static int minCost(int[][] grid) {
		// 格子的数值对应的方向:
		// 1 右
		// 2 左
		// 3 下
		// 4 上
		//                0      1         2          3         4
		int[][] move = { {}, { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
		int m = grid.length;
		int n = grid[0].length;
		
		// distance[i][j]表示从起点(0,0)到(i,j)的最小修改次数
		int[][] distance = new int[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				distance[i][j] = Integer.MAX_VALUE;
			}
		}
		
		// 双端队列，用于0-1 BFS
		ArrayDeque<int[]> q = new ArrayDeque<>();
		q.addFirst(new int[] { 0, 0 });
		distance[0][0] = 0;
		
		while (!q.isEmpty()) {
			// 从队首取出节点
			int[] record = q.pollFirst();
			int x = record[0];
			int y = record[1];
			
			// 如果到达终点
			if (x == m - 1 && y == n - 1) {
				return distance[x][y];
			}
			
			// 尝试四个方向
			for (int i = 1; i <= 4; i++) {
				int nx = x + move[i][0];
				int ny = y + move[i][1];
				// 如果当前格子的方向与尝试的方向一致，则不需要修改，权重为0；否则需要修改，权重为1
				int weight = grid[x][y] != i ? 1 : 0;
				
				// 检查边界和是否能找到更优路径
				if (nx >= 0 && nx < m && ny >= 0 && ny < n 
						&& distance[x][y] + weight < distance[nx][ny]) {
					distance[nx][ny] = distance[x][y] + weight;
					// 根据权重决定放在队首还是队尾
					if (weight == 0) {
						q.offerFirst(new int[] { nx, ny });
					} else {
						q.offerLast(new int[] { nx, ny });
					}
				}
			}
		}
		return -1;
	}

}