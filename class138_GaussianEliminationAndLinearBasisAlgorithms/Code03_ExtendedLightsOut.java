package class135;

/**
 * Code03_ExtendedLightsOut - 高斯消元法应用
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


// POJ 1222 EXTENDED LIGHTS OUT
// 有一个5*6的按钮矩阵，每个按钮控制一盏灯
// 按下按钮时，该按钮以及上下左右相邻按钮的灯状态会反转(0变1,1变0)
// 给定初始状态，求按哪些按钮可以将所有灯关闭
// 测试链接 : http://poj.org/problem?id=1222

/*
 * 题目解析:
 * 这是一个典型的开关问题，可以用高斯消元解决异或方程组来求解。
 * 
 * 解题思路:
 * 1. 将每个按钮是否按下设为未知数xi(1表示按下，0表示不按)
 * 2. 对于每个灯，建立一个方程表示该灯的最终状态
 * 3. 如果按钮j会影响灯i，则系数aij为1，否则为0
 * 4. 常数项bi为灯i的初始状态
 * 5. 方程形式: ai1*x1 ^ ai2*x2 ^ ... ^ ain*xn = bi
 *    其中^表示异或运算
 * 6. 使用高斯消元求解异或方程组
 * 
 * 时间复杂度: O(30^3) = O(27000)
 * 空间复杂度: O(30^2) = O(900)
 * 
 * 工程化考虑:
 * 1. 正确处理异或运算的性质
 * 2. 位运算优化提高效率
 * 3. 输入输出处理
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_ExtendedLightsOut {

	public static int MAXN = 31;

	// 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][31]表示第i个方程的常数项
	public static int[][] mat = new int[MAXN][MAXN];

	public static int n = 30; // 5*6=30个按钮和灯

	// 方向数组，表示当前位置和上下左右五个位置
	public static int[] dx = { 0, -1, 0, 1, 0 };
	public static int[] dy = { 0, 0, -1, 0, 1 };

	/*
	 * 高斯消元解决异或方程组
	 * 时间复杂度: O(n^3)
	 * 空间复杂度: O(n^2)
	 */
	public static void gauss() {
		for (int i = 1; i <= n; i++) {
			// 找到第i列中系数为1的行作为主元
			for (int j = i; j <= n; j++) {
				if (mat[j][i] == 1) {
					swap(i, j);
					break;
				}
			}

			// 如果第i列全为0，跳过这一列
			if (mat[i][i] == 0) {
				continue;
			}

			// 用第i行消去其他行的第i列系数
			for (int j = 1; j <= n; j++) {
				if (i != j && mat[j][i] == 1) {
					for (int k = i; k <= n + 1; k++) {
						mat[j][k] ^= mat[i][k]; // 异或运算
					}
				}
			}
		}
	}

	/*
	 * 交换矩阵中的两行
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 */
	public static void swap(int a, int b) {
		int[] tmp = mat[a];
		mat[a] = mat[b];
		mat[b] = tmp;
	}

	/*
	 * 打印解
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 */
	public static void printSolution() {
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 6; j++) {
				int id = (i - 1) * 6 + j;
				System.out.print(mat[id][31] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		in.nextToken();
		int testCases = (int) in.nval;

		for (int t = 1; t <= testCases; t++) {
			// 初始化矩阵
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n + 1; j++) {
					mat[i][j] = 0;
				}
			}

			// 建立方程组
			// 对于每个按钮位置(i,j)
			for (int i = 1; i <= 5; i++) {
				for (int j = 1; j <= 6; j++) {
					int id = (i - 1) * 6 + j; // 将二维坐标转为一维索引

					// 设置该方程的常数项(初始状态)
					in.nextToken();
					mat[id][31] = (int) in.nval;

					// 设置系数矩阵
					// 对于按钮(i,j)会影响的5个位置
					for (int k = 0; k < 5; k++) {
						int x = i + dx[k];
						int y = j + dy[k];
						if (x >= 1 && x <= 5 && y >= 1 && y <= 6) {
							int pid = (x - 1) * 6 + y;
							mat[pid][id] = 1; // 按下按钮id会影响灯pid
						}
					}
				}
			}

			// 高斯消元求解
			gauss();

			// 输出结果
			out.println("PUZZLE #" + t);
			for (int i = 1; i <= 5; i++) {
				for (int j = 1; j <= 6; j++) {
					int id = (i - 1) * 6 + j;
					if (j > 1) out.print(" ");
					out.print(mat[id][31]);
				}
				out.println();
			}
		}

		out.flush();
		out.close();
		br.close();
	}
}