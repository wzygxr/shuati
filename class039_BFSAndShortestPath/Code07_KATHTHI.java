package class062;

import java.util.ArrayDeque;
import java.util.Scanner;

// KATHTHI
// 题目链接: https://www.spoj.com/problems/KATHTHI/
// 
// 算法思路：
// 这是一个典型的0-1 BFS问题
// 将网格看作图，每个单元格是一个节点
// 如果移动到相同字符的单元格，边权为0
// 如果移动到不同字符的单元格，边权为1
// 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
// 
// 时间复杂度：O(n * m)，其中n和m分别是网格的行数和列数
// 空间复杂度：O(n * m)，用于存储距离数组和队列
public class Code07_KATHTHI {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int t = scanner.nextInt();
		
		while (t-- > 0) {
			int n = scanner.nextInt();
			int m = scanner.nextInt();
			char[][] grid = new char[n][m];
			
			for (int i = 0; i < n; i++) {
				String line = scanner.next();
				grid[i] = line.toCharArray();
			}
			
			System.out.println(minChanges(grid, n, m));
		}
		
		scanner.close();
	}
	
	// 0-1 BFS解法
	public static int minChanges(char[][] grid, int n, int m) {
		// 四个方向的移动：上、右、下、左
		int[] move = { -1, 0, 1, 0, -1 };
		
		// distance[i][j]表示从起点(0,0)到(i,j)的最小变化次数
		int[][] distance = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
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
			if (x == n - 1 && y == m - 1) {
				return distance[x][y];
			}
			
			// 向四个方向扩展
			for (int i = 0; i < 4; i++) {
				int nx = x + move[i];
				int ny = y + move[i + 1];
				
				// 检查边界
				if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
					// 如果字符相同，权重为0；否则权重为1
					int weight = (grid[x][y] != grid[nx][ny]) ? 1 : 0;
					
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