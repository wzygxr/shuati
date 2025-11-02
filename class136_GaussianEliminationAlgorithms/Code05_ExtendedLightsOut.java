package class133;

/**
 * =================================================================================
 * 异或方程组求解：EXTENDED LIGHTS OUT 问题 - Code05_ExtendedLightsOut.java
 * =================================================================================
 * 
 * 问题来源：POJ 1222 EXTENDED LIGHTS OUT
 * 题目链接：http://poj.org/problem?id=1222
 * 
 * 问题描述：
 * 有一个5×6的灯阵，每个灯有一个开关。按下开关会改变以下灯的状态：
 * - 当前灯本身
 * - 上方相邻的灯（如果存在）
 * - 下方相邻的灯（如果存在）
 * - 左方相邻的灯（如果存在）
 * - 右方相邻的灯（如果存在）
 * 
 * 给出初始状态（每个灯开或关），求一种按开关的方案使得所有灯都关闭。
 * 
 * 数学建模：
 * 将问题转化为异或方程组求解：
 * - 变量xi：第i个灯是否需要按下（1表示按下，0表示不按下）
 * - 系数aij：按下第j个灯对第i个灯的影响（1表示有影响，0表示无影响）
 * - 常数项bi：第i个灯的初始状态（1表示开，0表示关）
 * 
 * 方程组形式：
 * a11*x1 XOR a12*x2 XOR ... XOR a1n*xn = b1
 * a21*x1 XOR a22*x2 XOR ... XOR a2n*xn = b2
 * ...
 * an1*x1 XOR an2*x2 XOR ... XOR ann*xn = bn
 * 
 * 其中n=30（5×6个灯），要求解向量x=(x1,x2,...,x30)。
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n³) = O(30³) = O(27000)
 *   - 矩阵构造：O(n²) = O(900)
 *   - 高斯消元：O(n³) = O(27000)
 *   - 回代求解：O(n²) = O(900)
 *   - 总复杂度：O(n³) 主导
 * 
 * 空间复杂度分析：
 * - 空间复杂度：O(n²) = O(900)
 *   - 增广矩阵：n×(n+1) = 30×31 = 930个整数
 *   - 结果数组：n = 30个整数
 *   - 临时变量：O(1)
 * 
 * 算法特点：
 * 1. 异或运算：使用异或代替加减运算，效率更高
 * 2. 模2运算：所有运算在模2下进行，相当于异或
 * 3. 稀疏矩阵：系数矩阵相对稀疏，但未做特殊优化
 * 4. 唯一解：对于5×6灯阵，通常有唯一解
 * 
 * 工程化考量：
 * 1. 位运算优化：使用异或运算加速计算
 * 2. 内存布局：二维数组便于矩阵操作
 * 3. 输入输出：支持多组测试数据
 * 4. 边界处理：正确处理灯阵边界情况
 * 
 * 应用扩展：
 * - 可扩展到任意大小的灯阵
 * - 可处理不同的影响模式（如对角线影响）
 * - 可用于类似的开关问题建模
 * 
 * 作者：算法之旅项目组
 * 版本：v1.0
 * 日期：2025-10-28
 * =================================================================================
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code05_ExtendedLightsOut {

	public static int MAXN = 35; // 5*6 + 1
	
	// 增广矩阵，用于高斯消元求解异或方程组
	public static int[][] mat = new int[MAXN][MAXN];
	
	// 结果数组
	public static int[] result = new int[MAXN];
	
	public static int n = 30; // 5*6个灯泡
	
	/**
	 * 高斯消元法求解异或方程组
	 * 时间复杂度: O(n^3)
	 * 空间复杂度: O(n^2)
	 * 
	 * 异或方程组形式：
	 * a11*x1 XOR a12*x2 XOR ... XOR a1n*xn = b1
	 * a21*x1 XOR a22*x2 XOR ... XOR a2n*xn = b2
	 * ...
	 * an1*x1 XOR an2*x2 XOR ... XOR ann*xn = bn
	 * 
	 * 其中xi表示第i个灯是否需要按下（1表示按下，0表示不按下）
	 * aij表示按下第j个灯对第i个灯的影响（1表示有影响，0表示无影响）
	 * bi表示第i个灯的初始状态（1表示开着，0表示关着）
	 */
	public static void gauss() {
		// 对每一列进行处理
		for (int i = 1; i <= n; i++) {
			// 寻找第i列中系数为1的行，将其交换到第i行
			int row = i;
			for (int j = i + 1; j <= n; j++) {
				if (mat[j][i] == 1) {
					row = j;
					break;
				}
			}
			
			// 如果找不到系数为1的行，则继续处理下一列
			if (mat[row][i] == 0) {
				continue;
			}
			
			// 将找到的行与第i行交换
			if (row != i) {
				for (int j = 1; j <= n + 1; j++) {
					int tmp = mat[i][j];
					mat[i][j] = mat[row][j];
					mat[row][j] = tmp;
				}
			}
			
			// 用第i行消除其他行的第i列系数
			for (int j = 1; j <= n; j++) {
				if (i != j && mat[j][i] == 1) {
					for (int k = 1; k <= n + 1; k++) {
						mat[j][k] ^= mat[i][k]; // 异或操作
					}
				}
			}
		}
		
		// 回代求解
		for (int i = n; i >= 1; i--) {
			result[i] = mat[i][n + 1];
			for (int j = i + 1; j <= n; j++) {
				result[i] ^= (mat[i][j] & result[j]); // 异或操作
			}
		}
	}
	
	/**
	 * 计算按下某个灯对其相邻灯的影响
	 * @param x 行号（1-5）
	 * @param y 列号（1-6）
	 * @return 灯的编号（1-30）
	 */
	public static int getId(int x, int y) {
		return (x - 1) * 6 + y;
	}
	
	/**
	 * 根据灯的编号获取行列坐标
	 * @param id 灯的编号（1-30）
	 * @return 包含行号和列号的数组
	 */
	public static int[] getPos(int id) {
		int[] pos = new int[2];
		pos[0] = (id - 1) / 6 + 1; // 行号
		pos[1] = (id - 1) % 6 + 1; // 列号
		return pos;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		in.nextToken();
		int cases = (int) in.nval;
		
		for (int t = 1; t <= cases; t++) {
			// 初始化矩阵
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n + 1; j++) {
					mat[i][j] = 0;
				}
			}
			
			// 读取初始状态
			for (int i = 1; i <= 5; i++) {
				for (int j = 1; j <= 6; j++) {
					in.nextToken();
					int id = getId(i, j);
					mat[id][n + 1] = (int) in.nval; // 设置初始状态
				}
			}
			
			// 构造系数矩阵
			// 对于每个灯，按下它会影响自己和相邻的灯
			for (int i = 1; i <= 5; i++) {
				for (int j = 1; j <= 6; j++) {
					int id = getId(i, j);
					// 按下当前灯会影响自己
					mat[id][id] = 1;
					
					// 按下当前灯会影响上方的灯
					if (i > 1) {
						int upId = getId(i - 1, j);
						mat[upId][id] = 1;
					}
					
					// 按下当前灯会影响下方的灯
					if (i < 5) {
						int downId = getId(i + 1, j);
						mat[downId][id] = 1;
					}
					
					// 按下当前灯会影响左方的灯
					if (j > 1) {
						int leftId = getId(i, j - 1);
						mat[leftId][id] = 1;
					}
					
					// 按下当前灯会影响右方的灯
					if (j < 6) {
						int rightId = getId(i, j + 1);
						mat[rightId][id] = 1;
					}
				}
			}
			
			// 使用高斯消元法求解
			gauss();
			
			// 输出结果
			out.println("PUZZLE #" + t);
			for (int i = 1; i <= 5; i++) {
				for (int j = 1; j <= 6; j++) {
					int id = getId(i, j);
					if (j > 1) {
						out.print(" ");
					}
					out.print(result[id]);
				}
				out.println();
			}
		}
		
		out.flush();
		out.close();
		br.close();
	}
}