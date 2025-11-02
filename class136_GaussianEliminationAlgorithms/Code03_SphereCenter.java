package class133;

/**
 * 球形空间的中心点 - n维空间中球心计算
 * 问题来源：洛谷 P4035 [JSOI2008]球形空间产生器
 * 题目链接：https://www.luogu.com.cn/problem/P4035
 * 
 * 算法功能：
 * - 在n维空间中，给定n+1个点，这些点都在同一个n维球面上
 * - 求这个球的球心坐标
 * 
 * 数学原理：
 * - 设球心为O(x1, x2, ..., xn)，球上任意一点为P(y1, y2, ..., yn)
 * - 球心到球面上任意一点的距离相等，即|OP| = R（R为球的半径）
 * - 对于球上的两点Pi和Pj，有|OPi|² = |OPj|²
 * - 展开得到：(x1-yi1)² + (x2-yi2)² + ... + (xn-yin)² = (x1-yj1)² + (x2-yj2)² + ... + (xn-yjn)²
 * - 化简得到：2*(yi1-yj1)*x1 + 2*(yi2-yj2)*x2 + ... + 2*(yin-yjn)*xn = (yi1²+yi2²+...+yin²) - (yj1²+yj2²+...+yjn²)
 * - 这样就得到了一个关于x1,x2,...,xn的线性方程组，可以用高斯消元法求解
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n³)，其中n为维度数
 *   - 构造方程组：O(n²)
 *   - 高斯消元：O(n³)
 * 
 * 空间复杂度分析：
 * - 空间复杂度：O(n²)，用于存储n×(n+1)的增广矩阵
 * 
 * 解题要点：
 * 1. 利用球心到球面上所有点距离相等的性质构造线性方程组
 * 2. 通过相邻两点构造方程，共n个方程n个未知数
 * 3. 使用高斯消元法求解线性方程组
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_SphereCenter {

	// 最大支持的维度数 + 2，防止越界
	public static int MAXN = 12;

	// 存储输入的点坐标数据
	// data[i][j]表示第i个点的第j维坐标
	public static double[][] data = new double[MAXN][MAXN];

	// 增广矩阵，用于高斯消元求解线性方程组
	// mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示常数项
	public static double[][] mat = new double[MAXN][MAXN];

	// 维度数
	public static int n;

	// 精度控制常量，用于判断浮点数是否为0
	public static double sml = 1e-7;

	/**
	 * 高斯消元算法主函数（用于求解加法方程组）
	 * @param n 未知数个数（维度数）
	 * 
	 * 算法步骤：
	 * 1. 对于每一列（每一个主元位置）
	 * 2. 选主元：从所有行中找出该列绝对值最大的元素所在的行
	 * 3. 交换行：将主元行交换到当前处理的行
	 * 4. 如果主元不为0（考虑精度问题）：
	 *    a. 归一化：将主元行的主元系数化为1
	 *    b. 消元：用主元行消去其他所有行在该列的系数
	 */
	public static void gauss(int n) {
		// 逐列处理，i表示当前处理的列（主元列）
		for (int i = 1; i <= n; i++) {
			// 选主元：从所有行中找出第i列绝对值最大的元素
			int max = i; // 记录主元所在的行
			for (int j = 1; j <= n; j++) {
				// 跳过已经处理过的主元行（避免重复选择）
				if (j < i && Math.abs(mat[j][j]) >= sml) {
					continue;
				}
				// 更新最大主元行
				if (Math.abs(mat[j][i]) > Math.abs(mat[max][i])) {
					max = j;
				}
			}
			
			// 交换当前行与主元所在行
			swap(i, max);
			
			// 如果主元不为0，进行归一化和消元
			if (Math.abs(mat[i][i]) >= sml) {
				// 归一化：将主元行的主元系数化为1
				double tmp = mat[i][i]; // 主元的值
				for (int j = i; j <= n + 1; j++) {
					mat[i][j] /= tmp;
				}
				
				// 消元：用主元行消去其他所有行在第i列的系数
				for (int j = 1; j <= n; j++) {
					if (i != j) { // 跳过主元行自身
						// 计算消元系数
						double rate = mat[j][i] / mat[i][i];
						// 从第i列开始消元
						for (int k = i; k <= n + 1; k++) {
							mat[j][k] -= mat[i][k] * rate;
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
	 * 主函数：读取输入，构造方程组，执行高斯消元，输出球心坐标
	 */
	public static void main(String[] args) throws IOException {
		// 使用快速输入方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取维度数n
		in.nextToken();
		n = (int) in.nval;
		
		// 读取n+1个点的坐标数据
		for (int i = 1; i <= n + 1; i++) {
			for (int j = 1; j <= n; j++) {
				in.nextToken();
				data[i][j] = (double) in.nval;
			}
		}
		
		// 构造线性方程组
		// 利用相邻两点到球心距离相等的性质构造方程
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				// 系数部分：2*(data[i][j] - data[i+1][j])
				mat[i][j] = 2 * (data[i][j] - data[i + 1][j]);
				// 常数项部分：data[i][j]^2 - data[i+1][j]^2
				mat[i][n + 1] += data[i][j] * data[i][j] - data[i + 1][j] * data[i + 1][j];
			}
		}
		
		// 执行高斯消元求解线性方程组
		gauss(n);
		
		// 输出球心坐标（保留3位小数）
		for (int i = 1; i <= n; i++) {
			out.printf("%.3f ", mat[i][n + 1]);
		}
		out.println();
		
		// 刷新输出缓冲区
		out.flush();
		out.close();
		br.close();
	}
	
	/**
	 * 注意事项与工程化考量：
	 * 1. 数学建模：将几何问题转化为代数问题的关键是利用距离相等的性质
	 * 2. 浮点数精度：使用sml阈值判断是否为零，避免直接比较浮点数
	 * 3. 方程构造：通过相邻点构造方程可以简化计算，避免冗余
	 * 4. 可扩展性改进：
	 *    - 可以处理更一般的球面拟合问题（点不严格在球面上）
	 *    - 可以扩展到椭球或其他二次曲面的中心计算
	 * 5. 边界情况：
	 *    - 当输入点共面或共线时，方程组可能无解或有无穷多解
	 *    - 需要验证解的正确性（所有点到球心距离是否相等）
	 * 6. 性能优化：
	 *    - 对于小规模问题（n<=10），当前实现已经足够高效
	 *    - 对于大规模问题，可以考虑使用更高效的矩阵库
	 */
}