package class062;

import java.util.PriorityQueue;

// 二维接雨水
// 给你一个 m * n 的矩阵，其中的值均为非负整数，代表二维高度图每个单元的高度
// 请计算图中形状最多能接多少体积的雨水。
// 测试链接 : https://leetcode.cn/problems/trapping-rain-water-ii/
// 
// 算法思路：
// 这是一个使用优先队列的BFS问题
// 从边界开始，因为边界无法存储雨水
// 使用优先队列（最小堆）维护当前所有边界点中高度最低的点
// 每次取出高度最低的点，检查其相邻点
// 如果相邻点未访问过，计算该点能存储的雨水量
// 雨水量 = max(当前点高度, 相邻点高度) - 相邻点实际高度
// 将相邻点加入优先队列，高度为max(当前点高度, 相邻点高度)
// 
// 时间复杂度：O(m * n * log(m * n))，其中m和n分别是矩阵的行数和列数
// 空间复杂度：O(m * n)，用于存储访问状态和优先队列
// 
// 工程化考量：
// 1. 使用PriorityQueue作为优先队列（最小堆）
// 2. 使用visited数组记录访问状态
// 3. 从边界开始处理，确保正确计算雨水量
public class Code05_TrappingRainWaterII {

	// 使用优先队列的BFS解法
	public static int trapRainWater(int[][] height) {
		// 四个方向的移动：上、右、下、左
		int[] move = new int[] { -1, 0, 1, 0, -1 };
		int n = height.length;
		int m = height[0].length;
		
		// 优先队列，按高度排序，存储[行, 列, 水位线]
		// 水位线是指该点能保持的最高水位
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[2] - b[2]);
		boolean[][] visited = new boolean[n][m];
		
		// 将边界点加入优先队列
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				// 边界点
				if (i == 0 || i == n - 1 || j == 0 || j == m - 1) {
					heap.add(new int[] { i, j, height[i][j] });
					visited[i][j] = true;
				} else {
					visited[i][j] = false;
				}
			}
		}
		
		int ans = 0;
		while (!heap.isEmpty()) {
			// 取出高度最低的点
			int[] record = heap.poll();
			int r = record[0];
			int c = record[1];
			int w = record[2]; // 水位线
			
			// 累加雨水量
			ans += w - height[r][c];
			
			// 检查四个方向的相邻点
			for (int i = 0, nr, nc; i < 4; i++) {
				nr = r + move[i];
				nc = c + move[i + 1];
				// 检查边界和是否已访问
				if (nr >= 0 && nr < n && nc >= 0 && nc < m && !visited[nr][nc]) {
					// 新点的水位线是max(当前点水位线, 新点高度)
					heap.add(new int[] { nr, nc, Math.max(height[nr][nc], w) });
					visited[nr][nc] = true;
				}
			}
		}
		return ans;
	}

}