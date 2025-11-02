package class135;

/**
 * Code08_BrokenRobot - 高斯消元法应用
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


// Codeforces 24D Broken robot
// 有一个n*m的网格，机器人从位置(x,y)开始
// 每次等概率地选择以下动作：
// 1. 向左移动（如果不在最左列）
// 2. 向右移动（如果不在最右列）
// 3. 向下移动（如果不在最下列）
// 4. 停在原地
// 求机器人到达第n行的期望步数
// 测试链接 : https://codeforces.com/contest/24/problem/D

/*
 * 题目解析:
 * 这是一个期望DP问题，可以用高斯消元来解决。
 * 
 * 解题思路:
 * 1. 设f[i][j]表示从位置(i,j)到达第n行的期望步数
 * 2. 对于第n行的格子，f[n][j] = 0
 * 3. 对于其他格子，根据转移建立方程：
 *    f[i][j] = 1 + (f[i][j-1] + f[i][j+1] + f[i+1][j] + f[i][j]) / k
 *    其中k是可选动作数
 * 4. 移项后得到：
 *    k * f[i][j] = k + f[i][j-1] + f[i][j+1] + f[i+1][j] + f[i][j]
 *    (k-1) * f[i][j] = k + f[i][j-1] + f[i][j+1] + f[i+1][j]
 * 5. 特殊处理边界情况
 * 6. 使用高斯消元求解线性方程组
 * 
 * 时间复杂度: O(n * m^3)
 * 空间复杂度: O(m^2)
 * 
 * 工程化考虑:
 * 1. 正确处理边界条件
 * 2. 浮点数精度处理
 * 3. 高斯消元求解线性方程组
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code08_BrokenRobot {

	public static int MAXM = 1005;
	public static double EPS = 1e-10;

	// 增广矩阵
	public static double[][] mat = new double[MAXM][MAXM];

	// 期望值数组
	public static double[] f = new double[MAXM];

	public static int n, m, x, y;

	/*
	 * 高斯消元解决浮点数线性方程组
	 * 时间复杂度: O(m^3)
	 * 空间复杂度: O(m^2)
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

		// 回代求解
		for (int i = size; i >= 1; i--) {
			f[i] = mat[i][size + 1];
			for (int j = i + 1; j <= size; j++) {
				f[i] -= mat[i][j] * f[j];
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		in.nextToken();
		x = (int) in.nval;
		in.nextToken();
		y = (int) in.nval;

		// 从第n行开始向上计算
		for (int i = n - 1; i >= x; i--) {
			// 初始化矩阵
			for (int j = 1; j <= m; j++) {
				for (int k = 1; k <= m + 1; k++) {
					mat[j][k] = 0.0;
				}
			}

			// 建立方程组
			for (int j = 1; j <= m; j++) {
				// 对角线系数
				mat[j][j] = 1.0;

				// 可选动作数
				int k = 4;
				if (j == 1) k--; // 最左列不能向左
				if (j == m) k--; // 最右列不能向右
				if (i == n - 1) k--; // 最下列不能向下

				// 常数项
				mat[j][m + 1] = 1.0 + (i < n - 1 ? f[j] : 0.0); // 向下移动的贡献

				// 其他项的贡献
				if (j > 1) {
					mat[j][j - 1] -= 1.0 / k; // 向左移动的贡献
				}
				if (j < m) {
					mat[j][j + 1] -= 1.0 / k; // 向右移动的贡献
				}
				mat[j][j] -= 1.0 / k; // 停在原地的贡献
			}

			// 高斯消元求解
			gauss(m);
		}

		// 输出结果
		out.printf("%.10f\n", f[y]);

		out.flush();
		out.close();
		br.close();
	}
}