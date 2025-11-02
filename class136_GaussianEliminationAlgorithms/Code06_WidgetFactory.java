package class133;

/**
 * POJ 2947 Widget Factory - 工厂产品生产天数计算
 * 题目链接：http://poj.org/problem?id=2947
 * 
 * 算法功能：
 * - 工厂生产n种产品，每种产品需要固定天数完成
 * - 工作日按周一到周日循环
 * - 给出m条生产记录，每条记录包含生产的各种产品数量和起止日期
 * - 求每种产品需要的天数（3-9天）
 * 
 * 数学建模：
 * - 每条记录可以表示为一个模线性方程
 * - 例如：生产2个产品1和3个产品2，从周一到周三，表示为 2*x1 + 3*x2 ≡ 3 (mod 7)
 * - 这样就得到了一个模7线性方程组，可以用高斯消元法求解
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n³)
 *   - 高斯消元：O(n³)
 *   - 回代求解：O(n²)
 * 
 * 空间复杂度分析：
 * - 空间复杂度：O(n²)，用于存储n×(n+1)的增广矩阵
 * 
 * 解题要点：
 * 1. 模线性方程组：处理周期性问题（7天一周）
 * 2. 扩展欧几里得算法：求解模线性方程
 * 3. 解的约束：每种产品需要3-9天
 * 4. 解的情况判断：无解、多解、唯一解
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code06_WidgetFactory {

	// 最大支持的产品数和记录数 + 5，防止越界
	public static int MAXN = 305;
	
	// 增广矩阵，用于高斯消元求解模线性方程组
	// mat[i][j]表示第i个方程中第j种产品的系数，mat[i][n+1]表示方程右边的常数项
	public static long[][] mat = new long[MAXN][MAXN];
	
	// 结果数组，存储每种产品需要的天数
	public static long[] result = new long[MAXN];
	
	// n种产品，m条记录
	public static int n, m;
	
	/**
	 * 将星期字符串转换为数字
	 * @param day 星期字符串（如"MON"）
	 * @return 对应的数字（1-7）
	 */
	public static int getDay(String day) {
		switch (day) {
			case "MON": return 1;
			case "TUE": return 2;
			case "WED": return 3;
			case "THU": return 4;
			case "FRI": return 5;
			case "SAT": return 6;
			case "SUN": return 7;
			default: return 0;
		}
	}
	
	/**
	 * 扩展欧几里得算法
	 * 求解 ax + by = gcd(a, b) 的整数解
	 * @param a 系数a
	 * @param b 系数b
	 * @return 包含gcd和解的数组 [gcd, x, y]
	 * 
	 * 算法原理：
	 * 1. 递归终止条件：当b=0时，gcd(a,0)=a，x=1，y=0
	 * 2. 递归求解：gcd(b, a%b) = bx' + (a%b)y'
	 * 3. 回代得到：ax + by = gcd(a,b)，其中x=y'，y=x'-(a/b)y'
	 */
	public static long[] exgcd(long a, long b) {
		if (b == 0) {
			return new long[]{a, 1, 0}; // gcd, x, y
		}
		long[] res = exgcd(b, a % b);
		long gcd = res[0];
		long x = res[2];
		long y = res[1] - (a / b) * res[2];
		return new long[]{gcd, x, y};
	}
	
	/**
	 * 求解模线性方程 ax ≡ b (mod n)
	 * @param a 系数a
	 * @param b 等式右边
	 * @param n 模数
	 * @return 解，无解返回-1
	 * 
	 * 算法步骤：
	 * 1. 使用扩展欧几里得算法求解 ax + ny = gcd(a,n)
	 * 2. 检查方程是否有解：b必须能被gcd(a,n)整除
	 * 3. 计算解：x = x' * (b/gcd) mod (n/gcd)
	 */
	public static long modLinearEquation(long a, long b, long n) {
		// 使用扩展欧几里得算法求解
		long[] res = exgcd(a, n);
		long gcd = res[0];
		long x = res[1];
		
		// 检查方程是否有解
		if (b % gcd != 0) {
			return -1; // 无解
		}
		
		// 计算解空间的模数
		long mod = n / gcd;
		// 计算最小正整数解
		long sol = ((x * (b / gcd)) % mod + mod) % mod;
		return sol;
	}
	
	/**
	 * 高斯消元法求解模线性方程组
	 * 时间复杂度: O(n^3)
	 * 空间复杂度: O(n^2)
	 * 
	 * 模线性方程组形式：
	 * a11*x1 + a12*x2 + ... + a1n*xn ≡ b1 (mod 7)
	 * a21*x1 + a22*x2 + ... + a2n*xn ≡ b2 (mod 7)
	 * ...
	 * an1*x1 + an2*x2 + ... + ann*xn ≡ bn (mod 7)
	 * 
	 * 其中：
	 * - xi表示第i种产品需要的天数
	 * - aij表示第j个记录中第i种产品的数量
	 * - bi表示第j个记录的起止日期差+1（因为包含起止两天）
	 * 
	 * @return -1表示无解，0表示多解，1表示唯一解
	 */
	public static int gauss() {
		int row = 1;
		
		// 前向消元过程
		// 对每一列进行处理
		for (int col = 1; col <= n && row <= m; col++) {
			// 寻找第col列中系数不为0的行，将其交换到第row行
			int pivotRow = row;
			for (int i = row; i <= m; i++) {
				if (mat[i][col] != 0) {
					pivotRow = i;
					break;
				}
			}
			
			// 如果找不到系数不为0的行，则继续处理下一列
			if (mat[pivotRow][col] == 0) {
				continue;
			}
			
			// 将找到的行与第row行交换
			if (pivotRow != row) {
				for (int j = 1; j <= n + 1; j++) {
					long tmp = mat[row][j];
					mat[row][j] = mat[pivotRow][j];
					mat[pivotRow][j] = tmp;
				}
			}
			
			// 用第row行消除其他行的第col列系数
			for (int i = 1; i <= m; i++) {
				if (i != row && mat[i][col] != 0) {
					// 计算最小公倍数，用于消元
					long lcm = mat[row][col] * mat[i][col] / gcd(Math.abs(mat[row][col]), Math.abs(mat[i][col]));
					long rate1 = lcm / mat[row][col];
					long rate2 = lcm / mat[i][col];
					
					// 对整行进行消元操作
					for (int j = 1; j <= n + 1; j++) {
						// 执行行减法，然后取模
						mat[i][j] = (mat[i][j] * rate2 - mat[row][j] * rate1) % 7;
						// 确保结果非负
						if (mat[i][j] < 0) {
							mat[i][j] += 7;
						}
					}
				}
			}
			
			row++;
		}
		
		// 检查是否有矛盾方程（无解情况）
		for (int i = row; i <= m; i++) {
			if (mat[i][n + 1] != 0) {
				return -1; // 无解
			}
		}
		
		// 检查是否有无穷多解
		if (row - 1 < n) {
			return 0; // 有无穷多解
		}
		
		// 回代求解过程
		Arrays.fill(result, 0);
		for (int i = n; i >= 1; i--) {
			// 计算当前方程左边已知部分的和
			long sum = mat[i][n + 1];
			for (int j = i + 1; j <= n; j++) {
				sum = (sum - mat[i][j] * result[j] % 7 + 7) % 7;
			}
			
			// 求解 mat[i][i] * result[i] ≡ sum (mod 7)
			long sol = modLinearEquation(mat[i][i], sum, 7);
			if (sol == -1) {
				return -1; // 无解
			}
			result[i] = sol;
			
			// 根据题目要求，解必须在[3, 9]范围内
			if (result[i] < 3 || result[i] > 9) {
				return -1; // 无解
			}
		}
		
		return 1; // 有唯一解
	}
	
	/**
	 * 求两个数的最大公约数（欧几里得算法）
	 * @param a 第一个数
	 * @param b 第二个数
	 * @return a和b的最大公约数
	 */
	public static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}
	
	/**
	 * 主函数：读取输入，构造方程组，求解并输出结果
	 */
	public static void main(String[] args) throws IOException {
		// 使用快速输入方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 处理多组测试数据
		while (true) {
			// 读取产品数n和记录数m
			in.nextToken();
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			
			// 当n和m都为0时结束程序
			if (n == 0 && m == 0) {
				break;
			}
			
			// 初始化矩阵为0
			for (int i = 1; i <= m; i++) {
				for (int j = 1; j <= n + 1; j++) {
					mat[i][j] = 0;
				}
			}
			
			// 读取m条生产记录
			for (int i = 1; i <= m; i++) {
				// 读取本条记录中涉及的产品种类数
				in.nextToken();
				int k = (int) in.nval;
				
				// 读取起止日期
				String startDay = br.readLine().trim();
				String endDay = br.readLine().trim();
				
				// 将星期转换为数字
				int start = getDay(startDay);
				int end = getDay(endDay);
				
				// 计算生产天数（包含起止两天）
				long days = (end - start + 1 + 7) % 7;
				if (days == 0) {
					days = 7;
				}
				// 设置方程右边的常数项
				mat[i][n + 1] = days;
				
				// 读取涉及的产品种类
				for (int j = 0; j < k; j++) {
					in.nextToken();
					int product = (int) in.nval;
					// 累加该产品在本条记录中的数量（作为系数）
					mat[i][product]++;
				}
			}
			
			// 使用高斯消元法求解模线性方程组
			int res = gauss();
			
			// 输出结果
			if (res == -1) {
				// 无解情况
				out.println("Inconsistent data.");
			} else if (res == 0) {
				// 多解情况
				out.println("Multiple solutions.");
			} else {
				// 唯一解情况，输出每种产品需要的天数
				for (int i = 1; i <= n; i++) {
					if (i > 1) {
						out.print(" ");
					}
					out.print(result[i]);
				}
				out.println();
			}
		}
		
		// 刷新输出缓冲区
		out.flush();
		out.close();
		br.close();
	}
	
	/**
	 * 注意事项与工程化考量：
	 * 1. 模运算处理：
	 *    - 模7运算处理周期性问题
	 *    - 负数取模时需要调整为正数
	 * 2. 解的约束检查：
	 *    - 根据题目要求，每种产品需要3-9天
	 * 3. 多种解的情况处理：
	 *    - 无解：存在矛盾方程
	 *    - 多解：方程数少于未知数个数
	 *    - 唯一解：方程组有唯一解且满足约束
	 * 4. 数值稳定性：
	 *    - 使用long类型避免中间计算溢出
	 *    - 扩展欧几里得算法保证计算精度
	 * 5. 可扩展性改进：
	 *    - 可以处理不同的周期（不是7天一周）
	 *    - 可以增加更多的约束条件
	 * 6. 性能优化：
	 *    - 对于稀疏矩阵可以使用特殊存储结构
	 *    - 可以使用更高效的模运算算法
	 */
}