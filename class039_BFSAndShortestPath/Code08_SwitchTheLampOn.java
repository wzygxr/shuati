package class062;

import java.util.ArrayDeque;
import java.util.Scanner;

// Switch the Lamp On
// 题目链接: https://www.luogu.com.cn/problem/P4667
// 
// 算法思路：
// 这是一个0-1 BFS问题
// 将网格看作图，每个交点是一个节点
// 如果两个相邻块的方向一致，移动到下一个交点不需要转换，边权为0
// 如果两个相邻块的方向不一致，移动到下一个交点需要转换，边权为1
// 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
// 
// 时间复杂度：O(n * m)，其中n和m分别是网格的行数和列数
// 空间复杂度：O(n * m)，用于存储距离数组和队列
public class Code08_SwitchTheLampOn {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		
		char[][] grid = new char[n][m];
		for (int i = 0; i < n; i++) {
			String line = scanner.next();
			grid[i] = line.toCharArray();
		}
		
		System.out.println(minSwitches(grid, n, m));
		
		scanner.close();
	}
	
	// 0-1 BFS解法
	public static int minSwitches(char[][] grid, int n, int m) {
		// 特殊情况：起点和终点重合
		if (n == 1 && m == 1) {
			return 0;
		}
		
		// 四个方向的移动：
		// 0: 上 (连接当前点和上方交点)
		// 1: 右 (连接当前点和右方交点)
		// 2: 下 (连接当前点和下方交点)
		// 3: 左 (连接当前点和左方交点)
		int[] dx = { -1, 0, 1, 0 };
		int[] dy = { 0, 1, 0, -1 };
		
		// distance[i][j]表示从起点(0,0)到交点(i,j)的最小转换次数
		int[][] distance = new int[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
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
			if (x == n && y == m) {
				return distance[x][y];
			}
			
			// 向四个方向扩展
			for (int i = 0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				
				// 检查边界
				if (nx >= 0 && nx <= n && ny >= 0 && ny <= m) {
					// 计算权重
					int weight = 1;
					// 根据当前位置和移动方向判断是否需要转换
					if (i == 0 && x > 0 && grid[x - 1][y] == '\\') {
						weight = 0;
					} else if (i == 1 && y < m && grid[x][y] == '/') {
						weight = 0;
					} else if (i == 2 && x < n && grid[x][y] == '\\') {
						weight = 0;
					} else if (i == 3 && y > 0 && grid[x][y - 1] == '/') {
						weight = 0;
					}
					
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