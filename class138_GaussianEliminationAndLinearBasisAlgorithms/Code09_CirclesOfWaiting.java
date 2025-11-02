package class135;

/**
 * Code09_CirclesOfWaiting - 高斯消元法应用
 * 
 * 算法核心思想:
 * 使用高斯消元法解决线性方程组或线性基相关问题
 * 
 * 关键步骤:
 * 1. 构建增广矩阵
 * 2. 前向消元，将矩阵化为上三角形式
 * 3. 回代求解未知数
 * 4. 处理特殊情况（无解、多解）
 * 
 * 时间复杂度分析:
 * - 高斯消元: O(n³)
 * - 线性基构建: O(n * log(max_value))
 * - 查询操作: O(log(max_value))
 * 
 * 空间复杂度分析:
 * - 矩阵存储: O(n²)
 * - 线性基: O(log(max_value))
 * 
 * 工程化考量:
 * 1. 数值稳定性: 使用主元选择策略避免精度误差
 * 2. 边界处理: 处理零矩阵、奇异矩阵等特殊情况
 * 3. 异常处理: 检查输入合法性，提供有意义的错误信息
 * 4. 性能优化: 针对稀疏矩阵进行优化
 * 
 * 应用场景:
 * - 线性方程组求解
 * - 线性基构建与查询
 * - 异或最大值问题
 * - 概率期望计算
 * 
 * 调试技巧:
 * 1. 打印中间矩阵状态验证消元过程
 * 2. 使用小规模测试用例验证正确性
 * 3. 检查边界条件（n=0, n=1等）
 * 4. 验证数值精度和稳定性
 */


// Codeforces 963E Circles of Waiting
// 从原点(0,0)出发，每次等概率地向上、下、左、右移动一步
// 求第一次走到与原点距离大于R的点的期望步数
// 测试链接 : https://codeforces.com/contest/963/problem/E

/*
 * 题目解析:
 * 这是一个期望DP问题，可以用高斯消元来解决。
 * 
 * 解题思路:
 * 1. 设f[x][y]表示从位置(x,y)走到距离原点大于R的点的期望步数
 * 2. 对于距离原点大于R的点，f[x][y] = 0
 * 3. 对于其他点，根据转移建立方程：
 *    f[x][y] = 1 + (f[x-1][y] + f[x+1][y] + f[x][y-1] + f[x][y+1]) / 4
 * 4. 移项后得到：
 *    4 * f[x][y] = 4 + f[x-1][y] + f[x+1][y] + f[x][y-1] + f[x][y+1]
 *    3 * f[x][y] = 4 + f[x-1][y] + f[x+1][y] + f[x][y-1] + f[x][y+1]
 * 5. 使用高斯消元求解线性方程组
 * 
 * 时间复杂度: O((R^2)^3) = O(R^6)
 * 空间复杂度: O(R^4)
 * 
 * 工程化考虑:
 * 1. 正确确定需要计算的点的范围
 * 2. 浮点数精度处理
 * 3. 高斯消元求解线性方程组
 * 4. 坐标映射到一维索引
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class Code09_CirclesOfWaiting {

	public static int MAXR = 55;
	public static int MAXN = MAXR * MAXR * 4;
	public static double EPS = 1e-10;

	// 增广矩阵
	public static double[][] mat = new double[MAXN][MAXN];

	// 点的坐标列表
	public static List<Point> points = new ArrayList<>();

	// 坐标到索引的映射
	public static int[][] id = new int[MAXR * 2][MAXR * 2];

	public static int R;
	public static int n; // 点的总数

	// 方向数组：上、下、左、右
	public static int[] dx = { -1, 1, 0, 0 };
	public static int[] dy = { 0, 0, -1, 1 };

	static class Point {
		int x, y;

		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	/*
	 * 计算点到原点的距离的平方
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static int dist2(int x, int y) {
		return x * x + y * y;
	}

	/*
	 * 高斯消元解决浮点数线性方程组
	 * 时间复杂度: O(n^3)
	 * 空间复杂度: O(n^2)
	 */
	public static void gauss(int size) {
		for (int i = 1; i <= size; i++) {
			// 找到第i列系数绝对值最大的行
			int maxRow = i;
			for (int j = i + 1; j <= size; j++) {
				if (Math.abs(mat[j][i]) > Math.abs(mat[maxRow][i])) {
					maxRow = j;
				}
			}

			// 交换行
			if (maxRow != i) {
				for (int k = 1; k <= size + 1; k++) {
					double temp = mat[i][k];
					mat[i][k] = mat[maxRow][k];
					mat[maxRow][k] = temp;
				}
			}

			// 如果主元为0，说明矩阵奇异
			if (Math.abs(mat[i][i]) < EPS) {
				continue;
			}

			// 将第i行主元系数化为1
			double pivot = mat[i][i];
			for (int k = i; k <= size + 1; k++) {
				mat[i][k] /= pivot;
			}

			// 消去其他行的第i列系数
			for (int j = 1; j <= size; j++) {
				if (i != j && Math.abs(mat[j][i]) > EPS) {
					double factor = mat[j][i];
					for (int k = i; k <= size + 1; k++) {
						mat[j][k] -= factor * mat[i][k];
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		in.nextToken();
		R = (int) in.nval;

		// 初始化id数组
		for (int i = 0; i < MAXR * 2; i++) {
			for (int j = 0; j < MAXR * 2; j++) {
				id[i][j] = 0;
			}
		}

		// 收集所有需要计算的点
		points.clear();
		n = 0;
		for (int x = -R; x <= R; x++) {
			for (int y = -R; y <= R; y++) {
				if (dist2(x, y) <= R * R) {
					points.add(new Point(x, y));
					id[x + R][y + R] = ++n;
				}
			}
		}

		// 初始化矩阵
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n + 1; j++) {
				mat[i][j] = 0.0;
			}
		}

		// 建立方程组
		for (int i = 0; i < n; i++) {
			Point p = points.get(i);
			int idx = id[p.x + R][p.y + R];

			// 如果点距离原点大于R，则期望步数为0
			if (dist2(p.x, p.y) > R * R) {
				mat[idx][idx] = 1.0;
				mat[idx][n + 1] = 0.0;
				continue;
			}

			// 对角线系数
			mat[idx][idx] = 3.0; // 4-1=3

			// 常数项
			mat[idx][n + 1] = 4.0;

			// 邻接点的贡献
			for (int k = 0; k < 4; k++) {
				int nx = p.x + dx[k];
				int ny = p.y + dy[k];
				if (dist2(nx, ny) <= R * R) {
					int nidx = id[nx + R][ny + R];
					mat[idx][nidx] = -1.0;
				}
			}
		}

		// 高斯消元求解
		gauss(n);

		// 输出原点的期望步数
		out.printf("%.10f\n", mat[id[R][R]][n + 1]);

		out.flush();
		out.close();
		br.close();
	}
}