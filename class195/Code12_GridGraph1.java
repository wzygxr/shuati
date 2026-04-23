package class195;

// 网格图优化建图基础模板，Java 版
// 本代码展示网格图优化建图的核心模板，用于解决网格图上的最短路问题
// 测试链接 : https://www.luogu.com.cn/problem/P1141（改编）
// 本模板展示了网格图优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== 网格图优化建图核心知识点 =====================
// 【问题分析】
// 网格图优化建图用于解决网格图上的路径问题，将二维网格映射到一维节点
// 主要应用于迷宫问题、网格最短路、网格连通性等问题
//
// 【核心原理】
// 坐标映射：将 (x, y) 映射到一维编号 x * m + y
// 方向处理：使用方向数组处理上下左右四个方向
// 边界判断：检查新坐标是否在网格范围内
//
// 【复杂度分析】
// 节点数：n * m（原始网格节点）
// 边数：O(n * m)（每个节点最多 4 条出边）
// 最短路复杂度：O(nm log(nm))
//
// 【ML/DL 关联价值】
// 1. 图像处理中的网格卷积操作
// 2. 强化学习中的网格环境建模
// 3. 机器人路径规划的网格表示

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Code12_GridGraph1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 1000001;
	public static int MAXE = 4000001;
	public static int INF = 1 << 30;

	// ===================== 图存储区 =====================
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXE];
	public static int[] to = new int[MAXE];
	public static int[] weight = new int[MAXE];
	public static int cnt;

	// ===================== 网格图变量区 =====================
	public static int n, m;
	public static int[][] grid = new int[1001][1001];
	public static boolean[][] visited = new boolean[1001][1001];

	// ===================== 方向数组 =====================
	// 功能：表示上下左右四个方向的坐标偏移
	// 笔试面试考察点：方向数组的使用可以简化代码，避免重复写四个方向的逻辑
	public static int[] dx = {-1, 1, 0, 0};
	public static int[] dy = {0, 0, -1, 1};

	// ===================== 核心函数：图加边 =====================
	// 功能：向图中添加一条从 u 到 v、权值为 w 的有向边
	// 笔试面试考察点：链式前向星的插入操作
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt;
	}

	// ===================== 核心函数：坐标映射 =====================
	// 功能：将二维坐标 (x, y) 映射到一维节点编号
	// 核心思想：x * m + y，其中 m 是列数
	// 面试高频提问：为什么要将二维坐标映射到一维？如何反向映射？
	public static int getNodeId(int x, int y) {
		return x * m + y;
	}

	// ===================== 核心函数：边界检查 =====================
	// 功能：检查坐标 (x, y) 是否在网格范围内
	// 核心思想：检查 x 和 y 是否满足 0 <= x < n 且 0 <= y < m
	// 面试高频提问：如何处理网格边界？如何避免数组越界？
	public static boolean isValid(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < m;
	}

	// ===================== 核心函数：构建网格图 =====================
	// 功能：将网格图转化为邻接表表示的图
	// 核心思想：遍历每个网格点，向其四个方向的相邻点连边
	// 面试高频提问：网格图建图的时间复杂度？如何处理障碍物？
	public static void buildGridGraph() {
		// 遍历每个网格点
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				// 如果当前点是障碍物，跳过
				if (grid[i][j] == 1) {
					continue;
				}
				// 获取当前点的节点编号
				int u = getNodeId(i, j);
				// 遍历四个方向
				for (int k = 0; k < 4; k++) {
					int ni = i + dx[k];
					int nj = j + dy[k];
					// 检查新坐标是否有效且不是障碍物
					if (isValid(ni, nj) && grid[ni][nj] == 0) {
						int v = getNodeId(ni, nj);
						// 添加无向边（双向连边）
						addEdge(u, v, 1);
						addEdge(v, u, 1);
					}
				}
			}
		}
	}

	// ===================== 核心函数：BFS 求最短路 =====================
	// 功能：在网格图上运行 BFS，求从起点到终点的最短距离
	// 核心思想：BFS 天然适合求无权图的最短路
	// 面试高频提问：BFS 和 Dijkstra 的区别？为什么网格图常用 BFS？
	public static int bfs(int startX, int startY, int endX, int endY) {
		// 初始化访问数组
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				visited[i][j] = false;
			}
		}
		// 起点入队
		List<int[]> queue = new ArrayList<>();
		queue.add(new int[]{startX, startY, 0});
		visited[startX][startY] = true;

		while (!queue.isEmpty()) {
			// 取出队首元素
			int[] cur = queue.remove(0);
			int x = cur[0];
			int y = cur[1];
			int dist = cur[2];

			// 如果到达终点，返回距离
			if (x == endX && y == endY) {
				return dist;
			}

			// 遍历四个方向
			for (int k = 0; k < 4; k++) {
				int nx = x + dx[k];
				int ny = y + dy[k];
				// 检查新坐标是否有效且未访问
				if (isValid(nx, ny) && !visited[nx][ny] && grid[nx][ny] == 0) {
					visited[nx][ny] = true;
					queue.add(new int[]{nx, ny, dist + 1});
				}
			}
		}
		// 无法到达终点
		return -1;
	}

	// ===================== 核心函数：Dijkstra 求最短路 =====================
	// 功能：在带权网格图上运行 Dijkstra，求从起点到终点的最短距离
	// 核心思想：使用优先队列维护当前最短距离
	// 面试高频提问：Dijkstra 算法的时间复杂度？如何处理负权边？
	public static int dijkstra(int startX, int startY, int endX, int endY) {
		// 初始化距离数组
		int[][] dist = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				dist[i][j] = INF;
			}
		}
		dist[startX][startY] = 0;

		// 优先队列
		List<int[]> heap = new ArrayList<>();
		heap.add(new int[]{0, startX, startY});

		while (!heap.isEmpty()) {
			// 取出当前距离最小的元素
			int[] cur = heap.remove(heap.size() - 1);
			int d = cur[0];
			int x = cur[1];
			int y = cur[2];

			// 如果已经找到更优路径，跳过
			if (d > dist[x][y]) {
				continue;
			}

			// 如果到达终点，返回距离
			if (x == endX && y == endY) {
				return dist[x][y];
			}

			// 遍历四个方向
			for (int k = 0; k < 4; k++) {
				int nx = x + dx[k];
				int ny = y + dy[k];
				// 检查新坐标是否有效
				if (isValid(nx, ny) && grid[nx][ny] == 0) {
					int newDist = dist[x][y] + 1;
					if (newDist < dist[nx][ny]) {
						dist[nx][ny] = newDist;
						heap.add(new int[]{newDist, nx, ny});
					}
				}
			}
		}
		// 无法到达终点
		return -1;
	}

	// ===================== 快速读入类 =====================
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0) {
					return -1;
				}
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入网格大小
		n = in.nextInt();
		m = in.nextInt();

		// 读入网格
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				grid[i][j] = in.nextInt();
			}
		}

		// 读入起点和终点
		int startX = in.nextInt();
		int startY = in.nextInt();
		int endX = in.nextInt();
		int endY = in.nextInt();

		// 方法 1：使用 BFS 求最短路（无权图）
		int bfsDist = bfs(startX, startY, endX, endY);
		out.println("BFS 最短路距离：" + (bfsDist == -1 ? "不可达" : bfsDist));

		// 方法 2：使用 Dijkstra 求最短路（带权图）
		int dijkstraDist = dijkstra(startX, startY, endX, endY);
		out.println("Dijkstra 最短路距离：" + (dijkstraDist == -1 ? "不可达" : dijkstraDist));

		out.flush();
		out.close();
	}
}
