package class062;

// 腐烂的橘子
// 在给定的 m x n 网格 grid 中，每个单元格可以有以下三个值之一：
// 值 0 代表空单元格；
// 值 1 代表新鲜橘子；
// 值 2 代表腐烂的橘子。
// 每分钟，腐烂的橘子 四个方向上相邻 的新鲜橘子都会腐烂。
// 返回直到单元格中没有新鲜橘子为止所必须经过的最小分钟数
// 如果不可能，返回 -1
// 测试链接 : https://leetcode.com/problems/rotting-oranges/
// 
// 算法思路：
// 使用多源BFS解决腐烂过程模拟问题
// 初始时将所有腐烂的橘子加入队列，作为BFS的起始点
// 每一轮BFS代表一分钟，将相邻的新鲜橘子腐烂并加入队列
// 最后检查是否还有新鲜橘子未被腐烂
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数，每个单元格最多被访问一次
// 空间复杂度：O(m * n)，用于存储队列
// 
// 工程化考量：
// 1. 特殊情况处理：初始时就没有新鲜橘子直接返回0
// 2. 边界检查：确保移动后的位置在网格范围内
// 3. 结果验证：最后检查是否所有新鲜橘子都被腐烂
public class Code10_RottingOranges {

	public static int MAXN = 11;

	public static int MAXM = 11;

	public static int[][] queue = new int[MAXN * MAXM][2];

	public static int l, r;

	// 四个方向的移动：上、右、下、左
	public static int[] move = new int[] { -1, 0, 1, 0, -1 };

	public static int orangesRotting(int[][] grid) {
		int n = grid.length;
		int m = grid[0].length;
		
		l = r = 0;
		int fresh = 0; // 新鲜橘子数量
		
		// 初始化队列，将所有腐烂的橘子加入队列
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (grid[i][j] == 2) {
					queue[r][0] = i;
					queue[r++][1] = j;
				} else if (grid[i][j] == 1) {
					fresh++;
				}
			}
		}
		
		// 特殊情况：没有新鲜橘子
		if (fresh == 0) {
			return 0;
		}
		
		int minutes = 0;
		// 多源BFS模拟腐烂过程
		while (l < r) {
			minutes++;
			int size = r - l;
			// 处理当前层的所有腐烂橘子
			for (int k = 0, x, y, nx, ny; k < size; k++) {
				x = queue[l][0];
				y = queue[l++][1];
				// 向四个方向扩展
				for (int i = 0; i < 4; i++) {
					nx = x + move[i];
					ny = y + move[i + 1];
					// 检查边界和是否为新鲜橘子
					if (nx >= 0 && nx < n && ny >= 0 && ny < m && grid[nx][ny] == 1) {
						grid[nx][ny] = 2; // 腐烂
						fresh--; // 新鲜橘子减少
						queue[r][0] = nx;
						queue[r++][1] = ny;
					}
				}
			}
		}
		
		// 如果还有新鲜橘子未被腐烂，返回-1
		return fresh == 0 ? minutes - 1 : -1;
	}

}