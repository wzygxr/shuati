package class135;

/**
 * Code06_SwitchProblem - 高斯消元法应用
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


// POJ 1830 开关问题
// 有N个相同的开关，每个开关都与某些开关有着联系
// 每当你打开或者关闭某个开关的时候，其他的与此开关相关联的开关也会相应地发生变化
// 给定开关的初始状态和目标状态，求有多少种方案可以完成任务
// 测试链接 : http://poj.org/problem?id=1830

/*
 * 题目解析:
 * 这是一个典型的开关问题，可以用高斯消元解决异或方程组来求解。
 * 
 * 解题思路:
 * 1. 将每个开关是否操作设为未知数xi(1表示操作，0表示不操作)
 * 2. 对于每个开关，建立一个方程表示该开关的最终状态
 * 3. 如果操作开关j会影响开关i，则系数aij为1，否则为0
 * 4. 常数项bi为开关i的初始状态与目标状态的异或值
 * 5. 方程形式: ai1*x1 ^ ai2*x2 ^ ... ^ ain*xn = bi
 *    其中^表示异或运算
 * 6. 使用高斯消元求解异或方程组
 * 7. 根据解的情况判断方案数：
 *    - 如果无解，输出"oh,it's impossible~!!"
 *    - 如果有唯一解，输出1
 *    - 如果有多个解，输出2^(自由元个数)
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 工程化考虑:
 * 1. 正确处理异或运算的性质
 * 2. 完整判断解的各种情况
 * 3. 正确计算方案数
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code06_SwitchProblem {

	public static int MAXN = 35;

	// 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示第i个方程的常数项
	public static int[][] mat = new int[MAXN][MAXN];

	public static int n;
	public static int[] startState = new int[MAXN];
	public static int[] endState = new int[MAXN];

	/*
	 * 高斯消元解决异或方程组
	 * 返回自由元个数，-1表示无解
	 * 时间复杂度: O(n^3)
	 * 空间复杂度: O(n^2)
	 */
	public static int gauss() {
		int row = 0; // 当前行

		// 高斯消元
		for (int col = 1; col <= n; col++) {
			int pivotRow = row;
			// 找到第col列中系数为1的行作为主元
			for (int i = row; i < n; i++) {
				if (mat[i][col] == 1) {
					pivotRow = i;
					break;
				}
			}

			// 如果第col列全为0，说明是自由元
			if (mat[pivotRow][col] == 0) {
				continue;
			}

			// 交换行
			if (pivotRow != row) {
				for (int j = 1; j <= n + 1; j++) {
					int temp = mat[row][j];
					mat[row][j] = mat[pivotRow][j];
					mat[pivotRow][j] = temp;
				}
			}

			// 用第row行消去其他行的第col列系数
			for (int i = 0; i < n; i++) {
				if (i != row && mat[i][col] == 1) {
					for (int j = col; j <= n + 1; j++) {
						mat[i][j] ^= mat[row][j]; // 异或运算
					}
				}
			}

			row++;
		}

		// 检查是否有解
		for (int i = row; i < n; i++) {
			if (mat[i][n + 1] != 0) {
				return -1; // 无解
			}
		}

		// 返回自由元个数
		return n - row;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		in.nextToken();
		int testCases = (int) in.nval;

		for (int t = 1; t <= testCases; t++) {
			in.nextToken();
			n = (int) in.nval;

			// 读取初始状态
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				startState[i] = (int) in.nval;
			}

			// 读取目标状态
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				endState[i] = (int) in.nval;
			}

			// 初始化矩阵
			for (int i = 0; i <= n; i++) {
				for (int j = 0; j <= n + 1; j++) {
					mat[i][j] = 0;
				}
			}

			// 读取开关关系
			int u, v;
			while (true) {
				in.nextToken();
				u = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				if (u == 0 && v == 0) break;
				mat[v][u] = 1; // 操作开关u会影响开关v
			}

			// 设置对角线元素（操作开关本身会影响自身）
			for (int i = 1; i <= n; i++) {
				mat[i][i] = 1;
			}

			// 设置常数项（初始状态与目标状态的异或值）
			for (int i = 1; i <= n; i++) {
				mat[i][n + 1] = startState[i] ^ endState[i];
			}

			// 高斯消元求解
			int freeVars = gauss();

			// 输出结果
			if (freeVars == -1) {
				out.println("oh,it's impossible~!!");
			} else {
				out.println(1 << freeVars); // 2^(自由元个数)
			}
		}

		out.flush();
		out.close();
		br.close();
	}
}