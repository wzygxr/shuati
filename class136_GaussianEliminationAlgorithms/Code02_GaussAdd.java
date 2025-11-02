package class133;

/**
 * 高斯消元法求解线性方程组模板（区分矛盾、多解、唯一解）
 * 问题来源：洛谷 P2455 线性方程组
 * 题目链接：https://www.luogu.com.cn/problem/P2455
 * 
 * 算法功能：
 * - 求解n元一次线性方程组
 * - 严格区分三种情况：矛盾（无解）、多解（无穷多解）、唯一解
 * - 输出相应的结果
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n³)，其中n为方程组中方程的个数
 *   - 选主元过程：O(n²)
 *   - 归一化过程：O(n²)
 *   - 消元过程：O(n³)
 * 
 * 空间复杂度分析：
 * - 空间复杂度：O(n²)，用于存储n×(n+1)的增广矩阵
 * 
 * 优化点：
 * 1. 选主元策略：选择当前列绝对值最大的元素作为主元，提高数值稳定性
 * 2. 精度控制：使用sml变量处理浮点数精度问题
 * 3. 三种情况判断：通过检查主元是否为0以及常数项是否非零来区分矛盾、多解和唯一解
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_GaussAdd {

	// 最大支持的变量数 + 2，防止越界
	public static int MAXN = 52;

	// 增广矩阵，mat[i][j]表示第i个方程中第j个变量的系数，mat[i][n+1]表示常数项
	public static double[][] mat = new double[MAXN][MAXN];

	// 变量个数
	public static int n;

	// 精度控制常量，用于判断浮点数是否为0
	// 当一个数字绝对值小于sml时，认为该数字是0
	public static double sml = 1e-7;

	/**
	 * 高斯消元算法主函数
	 * @param n 变量个数
	 * 
	 * 算法步骤：
	 * 1. 对于每一列（每一个主元位置）
	 * 2. 选主元：从所有行中找出该列绝对值最大的元素所在的行
	 * 3. 交换行：将主元行交换到当前处理的行
	 * 4. 如果主元不为0（考虑精度问题）：
	 *    a. 归一化：将主元行的主元系数化为1
	 *    b. 消元：用主元行消去其他所有行在该列的系数
	 * 5. 如果主元为0，跳过该列的归一化和消元，留给后续判断
	 */
	public static void gauss(int n) {
		// 逐列处理，i表示当前处理的列（主元列）
		for (int i = 1; i <= n; i++) {
			// 选主元：从所有行中找出第i列绝对值最大的元素
			int maxRow = i; // 记录主元所在的行
			for (int j = 1; j <= n; j++) {
				// 跳过已经处理过的主元行（避免重复选择）
				if (j < i && Math.abs(mat[j][j]) >= sml) {
					continue;
				}
				// 更新最大主元行
				if (Math.abs(mat[j][i]) > Math.abs(mat[maxRow][i])) {
					maxRow = j;
				}
			}
			
			// 交换当前行与主元所在行
			swap(i, maxRow);
			
			// 如果主元不为0，进行归一化和消元
			if (Math.abs(mat[i][i]) >= sml) {
				// 归一化：将主元行的主元系数化为1
				double pivot = mat[i][i]; // 主元的值
				for (int j = i; j <= n + 1; j++) {
					mat[i][j] /= pivot;
				}
				
				// 消元：用主元行消去其他所有行在第i列的系数
				for (int j = 1; j <= n; j++) {
					if (i != j) { // 跳过主元行自身
						// 计算消元系数
						double factor = mat[j][i] / mat[i][i];
						// 从第i列开始消元
						for (int k = i; k <= n + 1; k++) {
							mat[j][k] -= mat[i][k] * factor;
						}
					}
				}
			}
		}
	}

	/**
	 * 交换矩阵中的两行
	 * @param a 第一行的索引
	 * @param b 第二行的索引
	 */
	public static void swap(int a, int b) {
		double[] tmp = mat[a];
		mat[a] = mat[b];
		mat[b] = tmp;
	}

	/**
	 * 主函数：读取输入，执行高斯消元，判断解的类型并输出结果
	 */
	public static void main(String[] args) throws IOException {
		// 使用快速输入方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取变量个数n
		in.nextToken();
		n = (int) in.nval;
		
		// 读取增广矩阵
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n + 1; j++) {
				in.nextToken();
				mat[i][j] = (double) in.nval;
			}
		}
		
		// 执行高斯消元
		gauss(n);
		
		// 判断解的类型：1表示唯一解，0表示无穷多解，-1表示无解（矛盾）
		int solutionType = 1;
		for (int i = 1; i <= n; i++) {
			// 判断是否存在矛盾方程：系数全为0但常数项非0
			if (Math.abs(mat[i][i]) < sml && Math.abs(mat[i][n + 1]) >= sml) {
				solutionType = -1;
				break;
			}
			// 判断是否存在自由变量：系数全为0且常数项也为0
			if (Math.abs(mat[i][i]) < sml) {
				solutionType = 0;
			}
		}
		
		// 输出结果
		if (solutionType == 1) {
			// 有唯一解，输出每个变量的值（保留两位小数）
			for (int i = 1; i <= n; i++) {
				out.printf("x" + i + "=" + "%.2f\n", mat[i][n + 1]);
			}
		} else {
			// 输出解的类型：0表示无穷多解，-1表示无解
			out.println(solutionType);
		}
		
		// 刷新输出缓冲区
		out.flush();
		out.close();
		br.close();
	}
	
	/**
	 * 注意事项与工程化考量：
	 * 1. 浮点数精度问题：使用sml阈值判断是否为零，避免直接比较浮点数
	 * 2. 解的类型判断：
	 *    - 矛盾（无解）：存在一行，系数全为0但常数项非0
	 *    - 多解（无穷多解）：存在至少一个自由变量
	 *    - 唯一解：每个变量都有确定的值
	 * 3. 选主元策略：与Code01_GaussAdd相比，这里的选主元更加全面，考虑了所有行
	 * 4. 可扩展性改进：
	 *    - 对于无穷多解的情况，可以进一步计算自由变量的个数和通解
	 *    - 可以增加对非方阵的支持，处理方程个数与变量个数不同的情况
	 * 5. 数值稳定性：当矩阵接近奇异时，浮点误差可能会很大，可以考虑使用更高精度的数据类型
	 */
}