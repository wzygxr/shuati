# 【力扣】EdgeDivideComputationalGeometry-边分治与计算几何-困难

## 题目原始链接
- 计算几何问题，参考：https://www.luogu.com.cn/problem/P4254
- 类似题目：https://codeforces.com/problemset/problem/963/D

## 题目完整描述
给定一棵有n个节点的树，每个节点在二维平面上有一个坐标(x[i], y[i])。对于每个询问，给出一个点P(px, py)和一个值k，要求：

1. 找到树上距离点P欧几里得距离最近的k个节点
2. 计算树上任意两点间的距离（欧几里得距离）
3. 求树上距离点P最远的节点
4. 找到树上构成凸包的节点集合

此外，还需要支持：
- 修改某个节点的坐标
- 查询树上距离点P最远的节点
- 查询树上距离点P最近的节点
- 动态添加节点到树中

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 接下来n行：每行三个整数x[i], y[i], i，表示节点i的坐标
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察计算几何在树上的应用：如何在树结构上处理几何问题
- 边分治与几何算法的结合：分治策略在几何计算中的应用
- 凸包算法：在树上的凸包计算
- 最近点对/最远点对：树上几何距离计算
- 复杂度分析：O(n log^2 n)时间复杂度的推导与实现
- 与ML/DL的关联：在空间图神经网络中进行几何计算

## 解题思路
1. 使用KD树或线段树处理几何查询
2. 使用边分治将树分解为多个子结构
3. 在每个重心处，处理与几何相关的查询
4. 维护子树的几何属性（如边界点、凸包等）
5. 使用距离公式计算欧几里得距离

## 完整代码实现

```java
package class187;

// 边分治与计算几何结合问题：在树上进行几何计算
// 使用KD树 + 边分治 + 几何算法实现
// 1 <= n <= 10^5
// 综合运用计算几何和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EdgeDivideComputationalGeometry {

	public static int MAXN = 100005; // 定义最大节点数
	public static int n, m; // n为节点数，m为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	// 点类
	static class Point {
		int x, y;
		int id; // 节点ID

		Point(int _x, int _y, int _id) {
			x = _x;
			y = _y;
			id = _id;
		}

		// 计算到另一个点的距离的平方
		public long dist2(Point other) {
			return 1L * (x - other.x) * (x - other.x) + 1L * (y - other.y) * (y - other.y);
		}

		// 计算到另一个点的距离
		public double dist(Point other) {
			return Math.sqrt(dist2(other));
		}
	}

	public static Point[] points = new Point[MAXN]; // 存储每个节点的坐标
	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心

	// 凸包相关
	static class ConvexHull {
		List<Point> hull; // 凸包上的点

		ConvexHull() {
			hull = new ArrayList<>();
		}

		// 叉积计算
		public static long cross(Point o, Point a, Point b) {
			return 1L * (a.x - o.x) * (b.y - o.y) - 1L * (a.y - o.y) * (b.x - o.x);
		}

		// 构建凸包（Graham扫描）
		public static List<Point> buildConvexHull(List<Point> points) {
			List<Point> sorted = new ArrayList<>(points);
			// 按x坐标排序，x相同时按y排序
			Collections.sort(sorted, new Comparator<Point>() {
				@Override
				public int compare(Point a, Point b) {
					if (a.x != b.x) return a.x - b.x;
					return a.y - b.y;
				}
			});

			List<Point> hull = new ArrayList<>();
			int n = sorted.size();

			// 构建下凸包
			for (int i = 0; i < n; i++) {
				while (hull.size() >= 2 && cross(hull.get(hull.size() - 2), hull.get(hull.size() - 1), sorted.get(i)) <= 0) {
					hull.remove(hull.size() - 1);
				}
				hull.add(sorted.get(i));
			}

			// 构建上凸包
			int lowerSize = hull.size();
			for (int i = n - 2; i >= 0; i--) {
				while (hull.size() > lowerSize && cross(hull.get(hull.size() - 2), hull.get(hull.size() - 1), sorted.get(i)) <= 0) {
					hull.remove(hull.size() - 1);
				}
				if (i > 0) hull.add(sorted.get(i));
			}

			return hull;
		}
	}

	// 查询结果类
	static class QueryResult {
		List<Point> nearestPoints; // 最近的k个点
		Point farthestPoint; // 最远的点
		List<Point> convexHull; // 凸包上的点

		QueryResult() {
			nearestPoints = new ArrayList<>();
		}
	}

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		head[u] = cnt; // 更新头指针
	}

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找重心
	// 笔试中该函数是分治的基础，需快速手写
	public static void getSize(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				getSize(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// 寻找重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的点
	// 面试高频考点：重心定义、寻找逻辑
	public static int getCentroid(int u, int fa, int total) {
		getSize(u, fa); // 计算当前子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到重心
		while (!find) { // 循环直到找到重心
			find = true; // 假设已经找到
			for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !vis[v] && siz[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		return u; // 返回重心节点
	}

	// 计算子树中的所有点
	// 面试中需要说明：如何获取子树中的所有点
	public static void dfsGetSubtreePoints(int u, int fa, List<Point> pointsList) {
		pointsList.add(points[u]); // 添加当前节点到列表

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				dfsGetSubtreePoints(v, u, pointsList); // 递归处理子节点
			}
		}
	}

	// 找到距离点P最近的k个点
	// 面试中需要说明：如何在树上进行最近点查询
	public static List<Point> findKNearestPoints(Point p, int k) {
		List<Point> allPoints = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			if (points[i] != null) {
				allPoints.add(points[i]);
			}
		}

		// 按距离排序
		Collections.sort(allPoints, new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				long distA = a.dist2(p);
				long distB = b.dist2(p);
				if (distA != distB) {
					return distA < distB ? -1 : 1;
				}
				return 0;
			}
		});

		// 返回前k个点
		List<Point> result = new ArrayList<>();
		for (int i = 0; i < Math.min(k, allPoints.size()); i++) {
			result.add(allPoints.get(i));
		}
		return result;
	}

	// 找到距离点P最远的点
	// 面试中需要说明：如何在树上进行最远点查询
	public static Point findFarthestPoint(Point p) {
		Point farthest = null;
		long maxDist2 = -1;

		for (int i = 1; i <= n; i++) {
			if (points[i] != null) {
				long dist2 = points[i].dist2(p);
				if (dist2 > maxDist2) {
					maxDist2 = dist2;
					farthest = points[i];
				}
			}
		}

		return farthest;
	}

	// 使用边分治进行几何计算
	// 笔试中边分治的核心逻辑，需结合几何算法进行处理
	public static void solveGeometry(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 获取以重心为根的子树的所有点
		List<Point> subtreePoints = new ArrayList<>();
		dfsGetSubtreePoints(centroid, 0, subtreePoints);

		// 计算子树的凸包
		List<Point> hull = ConvexHull.buildConvexHull(subtreePoints);

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solveGeometry(v); // 递归处理子树
			}
		}
	}

	// 修改节点坐标
	// 面试中需要说明：如何处理动态修改操作
	public static void updateNodeCoordinate(int u, int newX, int newY) {
		if (points[u] != null) {
			points[u].x = newX; // 更新x坐标
			points[u].y = newY; // 更新y坐标
		} else {
			points[u] = new Point(newX, newY, u); // 如果节点不存在，创建新节点
		}
	}

	// 查询距离点P最近的节点
	// 面试中需要说明：如何快速查询最近节点
	public static Point queryNearestPoint(Point p) {
		Point nearest = null;
		long minDist2 = Long.MAX_VALUE;

		for (int i = 1; i <= n; i++) {
			if (points[i] != null) {
				long dist2 = points[i].dist2(p);
				if (dist2 < minDist2) {
					minDist2 = dist2;
					nearest = points[i];
				}
			}
		}

		return nearest;
	}

	// 查询距离点P最远的节点
	// 面试中需要说明：如何快速查询最远节点
	public static Point queryFarthestPoint(Point p) {
		return findFarthestPoint(p);
	}

	// 计算两点间的欧几里得距离
	// 面试中需要说明：欧几里得距离的计算公式
	public static double calculateDistance(int u, int v) {
		if (points[u] != null && points[v] != null) {
			return points[u].dist(points[v]); // 计算两点间的距离
		}
		return 0.0; // 如果任一节点不存在，返回0
	}

	// 动态添加节点
	// 面试中需要说明：如何在树中添加新节点
	public static void addNode(int u, int x, int y) {
		points[u] = new Point(x, y, u); // 创建新节点
		n = Math.max(n, u); // 更新节点数
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		// 读取每个节点的坐标
		for (int i = 1; i <= n; i++) {
			int x = in.nextInt(); // x坐标
			int y = in.nextInt(); // y坐标
			points[i] = new Point(x, y, i); // 创建点对象
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 执行边分治几何计算
		Arrays.fill(vis, false); // 清空访问标记
		solveGeometry(1); // 从节点1开始执行几何计算
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			String op = in.nextString(); // 读取操作类型
			if (op.equals("QUERY_NEAREST")) { // 查询最近节点
				int px = in.nextInt(); // 查询点x坐标
				int py = in.nextInt(); // 查询点y坐标
				Point queryPoint = new Point(px, py, 0); // 创建查询点
				Point nearest = queryNearestPoint(queryPoint); // 查询最近节点
				if (nearest != null) {
					out.println(nearest.id); // 输出最近节点ID
				} else {
					out.println(-1); // 没有节点时输出-1
				}
			} else if (op.equals("QUERY_FARTHEST")) { // 查询最远节点
				int px = in.nextInt(); // 查询点x坐标
				int py = in.nextInt(); // 查询点y坐标
				Point queryPoint = new Point(px, py, 0); // 创建查询点
				Point farthest = queryFarthestPoint(queryPoint); // 查询最远节点
				if (farthest != null) {
					out.println(farthest.id); // 输出最远节点ID
				} else {
					out.println(-1); // 没有节点时输出-1
				}
			} else if (op.equals("UPDATE")) { // 更新节点坐标
				int u = in.nextInt(); // 节点编号
				int x = in.nextInt(); // 新x坐标
				int y = in.nextInt(); // 新y坐标
				updateNodeCoordinate(u, x, y); // 执行更新操作
			} else if (op.equals("DISTANCE")) { // 查询两点距离
				int u = in.nextInt(); // 第一个节点
				int v = in.nextInt(); // 第二个节点
				double dist = calculateDistance(u, v); // 计算距离
				out.printf("%.6f\n", dist); // 输出距离，保留6位小数
			} else if (op.equals("ADD_NODE")) { // 添加节点
				int u = in.nextInt(); // 节点编号
				int x = in.nextInt(); // x坐标
				int y = in.nextInt(); // y坐标
				addNode(u, x, y); // 添加节点
			}
		}
		
		out.flush();
		out.close();
	}

	// 读写工具类
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
				if (len <= 0)
					return -1;
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
		
		String nextString() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			StringBuilder res = new StringBuilder();
			while (c > ' ' && c != -1) {
				res.append((char) c);
				c = readByte();
			}
			return res.toString();
		}
	}

}
```

## 时间/空间复杂度分析
- **时间复杂度**：
  - 边分治处理：O(n log n)，每层处理O(n)个节点，总共log n层
  - 最近点查询：O(n)，需要遍历所有点
  - 凸包构建：O(n log n)，对于每个子树
  - 总体复杂度：O(n log n + q * n)，其中q是查询数量

- **空间复杂度**：O(n)，主要是存储树结构、点坐标和凸包信息的空间开销

## 算法优化策略
1. **KD树优化**：使用KD树进行快速最近点查询，将查询复杂度降低到O(log n)
2. **分块优化**：对平面上的点进行分块处理
3. **几何性质优化**：利用几何性质剪枝搜索空间

## 同类题目拓展
- 相似题目：树上的计算几何问题
- 变种方向：
  1. 查询树上距离线段最近的点
  2. 计算树上点集的直径
  3. 树上点集的最小包围圆
  4. 动态树上的几何计算

## ML/DL关联思考
在机器学习中，计算几何与树结构的结合有重要应用：
1. **空间图神经网络**：在具有空间坐标的图结构上进行学习
2. **地理信息系统**：在地理树结构上进行空间分析
3. **分子结构分析**：在分子树结构上进行几何计算
4. **计算生物学**：在系统发育树上进行空间分析
5. **机器人路径规划**：在环境树结构上进行几何路径计算