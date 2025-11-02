package class133;

/**
 * 有一次错误称重求最重物品
 * 问题来源：洛谷 P5027 [SHOI2018]有一次错误称重求最重物品
 * 题目链接：https://www.luogu.com.cn/problem/P5027
 * 
 * 算法功能：
 * - 有n个物品，每个物品重量为正整数
 * - 有n+1条称重记录，其中恰好有一条是错误的
 * - 排除错误记录后，应能唯一确定所有物品的重量，且最重物品唯一
 * - 找出哪条记录是错误的，并输出最重物品的编号
 * 
 * 解题思路：
 * 1. 枚举每一条称重记录作为错误记录（即排除该记录）
 * 2. 用剩余的n条记录建立n元线性方程组
 * 3. 用高斯消元法求解方程组
 * 4. 检查解是否合法（重量为正整数且最重物品唯一）
 * 5. 如果恰好只有一种情况合法，则输出最重物品编号，否则输出"illegal"
 * 
 * 数学建模：
 * - 每条称重记录可以表示为一个线性方程
 * - 例如：3 2 5 6 10 表示 x2 + x5 + x6 = 10
 * - 这样就得到了一个线性方程组，可以用高斯消元法求解
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n⁴)
 *   - 枚举错误记录：O(n)
 *   - 高斯消元：O(n³)
 *   - 解的验证：O(n)
 * 
 * 空间复杂度分析：
 * - 空间复杂度：O(n²)，用于存储n×(n+1)的增广矩阵
 * 
 * 解题要点：
 * 1. 枚举法：尝试排除每一条记录，看是否能得到合法解
 * 2. 线性方程组：将称重记录转化为线性方程
 * 3. 解的验证：检查解是否满足所有约束条件
 * 4. 唯一性判断：确保最重物品唯一
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_FindMaxWeighing {

	// 最大支持的物品数 + 2，防止越界
	public static int MAXN = 102;

	// 存储称重数据
	// data[i][j]表示第i条称重记录中物品j是否被称重（1表示被称重，0表示未被称重）
	// data[i][n+1]表示第i条称重记录的总重量
	public static int[][] data = new int[MAXN][MAXN];

	// 增广矩阵，用于高斯消元求解线性方程组
	// mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示常数项
	public static double[][] mat = new double[MAXN][MAXN];

	// 物品数
	public static int n;

	// 精度控制常量，用于判断浮点数是否为0
	public static double sml = 1e-7;

	/**
	 * 高斯消元算法主函数（用于求解加法方程组）
	 * @param n 未知数个数（物品数）
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
	 * 检查当前方程组的解是否合法
	 * @return 0表示不合法，非0表示最重物品的编号
	 * 
	 * 合法解的条件：
	 * 1. 方程组有唯一解（系数矩阵满秩）
	 * 2. 所有物品重量为正整数
	 * 3. 最重物品唯一
	 */
	public static int check() {
		// 执行高斯消元求解线性方程组
		gauss(n);
		
		// 查找最重物品
		double maxv = Double.MIN_VALUE; // 最大重量
		int maxt = 0; // 最大重量的物品个数
		int ans = 0; // 最重物品编号
		
		// 检查每个物品的重量
		for (int i = 1; i <= n; i++) {
			// 检查方程组是否有唯一解（主元不为0）
			if (mat[i][i] == 0) {
				return 0;
			}
			
			// 检查重量是否为正整数
			if (mat[i][n + 1] <= 0 || mat[i][n + 1] != (int) mat[i][n + 1]) {
				return 0;
			}
			
			// 更新最重物品信息
			if (maxv < mat[i][n + 1]) {
				maxv = mat[i][n + 1];
				maxt = 1;
				ans = i;
			} else if (maxv == mat[i][n + 1]) {
				maxt++;
			}
		}
		
		// 检查最重物品是否唯一
		if (maxt > 1) {
			return 0;
		}
		
		return ans;
	}

	/**
	 * 交换称重数据中的两行
	 * @param i 第一行的索引
	 * @param j 第二行的索引
	 */
	public static void swapData(int i, int j) {
		int[] tmp = data[i];
		data[i] = data[j];
		data[j] = tmp;
	}

	/**
	 * 主函数：读取输入，枚举错误记录，找出最重物品
	 */
	public static void main(String[] args) throws IOException {
		// 使用快速输入方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取物品数n
		in.nextToken();
		n = (int) in.nval;
		
		// 读取n+1条称重记录
		for (int i = 1, m; i <= n + 1; i++) {
			// 读取第i条记录中称重的物品数
			in.nextToken();
			m = (int) in.nval;
			
			// 读取被称重的物品编号
			for (int j = 1, cur; j <= m; j++) {
				in.nextToken();
				cur = (int) in.nval;
				data[i][cur] = 1; // 标记物品被称重
			}
			
			// 读取总重量
			in.nextToken();
			data[i][n + 1] = (int) in.nval;
		}
		
		// 枚举每条记录作为错误记录
		int ans = 0; // 最重物品编号
		int times = 0; // 合法解的个数
		
		for (int k = 1; k <= n + 1; k++) {
			// 将第k条记录交换到最后一行（相当于排除该记录）
			swapData(k, n + 1);
			
			// 将前n条记录复制到增广矩阵
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n + 1; j++) {
					mat[i][j] = data[i][j];
				}
			}
			
			// 恢复数据顺序
			swapData(k, n + 1);
			
			// 检查当前情况是否能得到合法解
			int cur = check();
			if (cur != 0) {
				times++;
				ans = cur;
			}
		}
		
		// 输出结果
		if (times != 1) {
			// 合法解不唯一或不存在
			out.println("illegal");
		} else {
			// 输出最重物品编号
			out.println(ans);
		}
		
		// 刷新输出缓冲区
		out.flush();
		out.close();
		br.close();
	}
	
	/**
	 * 注意事项与工程化考量：
	 * 1. 枚举策略：通过枚举排除每条记录来验证解的唯一性
	 * 2. 浮点数精度：使用sml阈值判断是否为零，避免直接比较浮点数
	 * 3. 解的验证：需要检查解的合法性（正整数、唯一性）
	 * 4. 数据结构优化：使用交换行的方式避免复制大量数据
	 * 5. 边界情况：
	 *    - 当所有记录都正确或都错误时的处理
	 *    - 当物品数很少时的特殊情况
	 * 6. 可扩展性改进：
	 *    - 可以处理多个错误记录的情况
	 *    - 可以扩展到其他类型的约束条件
	 * 7. 性能优化：
	 *    - 对于大规模问题，可以考虑剪枝策略减少枚举次数
	 *    - 可以使用更高效的矩阵运算库
	 */
}