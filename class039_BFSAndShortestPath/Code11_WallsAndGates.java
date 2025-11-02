package class062;

// 墙与门
// 你被给定一个 m × n 的二维网格 rooms ，网格中有以下三种可能的初始化值：
// -1 表示墙或是障碍物
// 0 表示一扇门
// INF 无限表示一个空的房间。然后，我们用 2^31 - 1 = 2147483647 代表 INF
// 请你给每个空房间填上该房间到 最近门的距离 ，如果无法到达门，则填 INF
// 测试链接 : https://leetcode.com/problems/walls-and-gates/
// 
// 算法思路：
// 使用多源BFS解决距离填充问题
// 初始时将所有门（值为0的单元格）加入队列，作为BFS的起始点
// 从门开始向外扩展，每一轮BFS代表距离增加1
// 将空房间（值为INF）更新为其到最近门的距离
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数，每个单元格最多被访问一次
// 空间复杂度：O(m * n)，用于存储队列
// 
// 工程化考量：
// 1. 特殊值处理：正确处理墙(-1)、门(0)、空房间(INF)
// 2. 边界检查：确保移动后的位置在网格范围内
// 3. 距离更新：只更新空房间的距离值
public class Code11_WallsAndGates {

	public static int MAXN = 251;

	public static int MAXM = 251;

	public static int[][] queue = new int[MAXN * MAXM][2];

	public static int l, r;

	// 四个方向的移动：上、右、下、左
	public static int[] move = new int[] { -1, 0, 1, 0, -1 };
	
	public static final int INF = 2147483647;

	public static void wallsAndGates(int[][] rooms) {
		if (rooms == null || rooms.length == 0 || rooms[0].length == 0) {
			return;
		}
		
		int n = rooms.length;
		int m = rooms[0].length;
		
		l = r = 0;
		
		// 初始化队列，将所有门加入队列
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (rooms[i][j] == 0) {
					queue[r][0] = i;
					queue[r++][1] = j;
				}
			}
		}
		
		int distance = 0;
		// 多源BFS填充距离
		while (l < r) {
			distance++;
			int size = r - l;
			// 处理当前层的所有节点
			for (int k = 0, x, y, nx, ny; k < size; k++) {
				x = queue[l][0];
				y = queue[l++][1];
				// 向四个方向扩展
				for (int i = 0; i < 4; i++) {
					nx = x + move[i];
					ny = y + move[i + 1];
					// 检查边界和是否为空房间
					if (nx >= 0 && nx < n && ny >= 0 && ny < m && rooms[nx][ny] == INF) {
						rooms[nx][ny] = distance;
						queue[r][0] = nx;
						queue[r++][1] = ny;
					}
				}
			}
		}
	}

}