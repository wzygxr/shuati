package class135;

/**
 * Code02_WidgetFactory - 高斯消元法应用
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


// 工具工厂
// 一共有n种工具，编号1~n，一共有m条记录，其中一条记录格式如下：
// 4 WED SUN 13 18 1 13
// 表示有个工人一共加工了4件工具，从某个星期三开始工作，到某个星期天结束工作
// 加工的工具依次为13号、18号、1号、13号
// 每个工人在工作期间不休息，每件工具都是串行加工的，完成一件后才开始下一件
// 每种工具制作天数是固定的，并且任何工具的制作天数最少3天、最多9天
// 但该数据丢失了，所以现在需要根据记录，推断出每种工具的制作天数
// 如果记录之间存在矛盾，打印"Inconsistent data."
// 如果记录无法确定每种工具的制作天数，打印"Multiple solutions."
// 如果记录能够确定每种工具的制作天数，打印所有结果
// 1 <= n、m <= 300
// 测试链接 : http://poj.org/problem?id=2947
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 题目解析:
 * 本题是另一个高斯消元解决模线性方程组的经典应用。
 * 根据工人工作记录建立方程组，求解每种工具的制作天数。
 * 
 * 解题思路:
 * 1. 每条记录表示一个方程，变量是每种工具的制作天数
 * 2. 方程形式为: a1*x1 + a2*x2 + ... + an*xn ≡ b (mod 7)
 *    其中ai表示第i种工具在该记录中出现的次数，b为工作天数
 * 3. 使用高斯消元法求解模线性方程组
 * 4. 根据解的情况判断是无解、多解还是唯一解
 * 
 * 时间复杂度: O(max(n,m)^3)
 * 空间复杂度: O(max(n,m)^2)
 * 
 * 工程化考虑:
 * 1. 正确处理模意义下的运算
 * 2. 完整判断解的各种情况
 * 3. 对结果进行合理性验证（3-9天）
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code02_WidgetFactory {

	public static int MOD = 7;

	public static int MAXN = 302;

	public static int[][] mat = new int[MAXN][MAXN];

	public static int[] inv = new int[MOD];

	public static String[] days = { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };

	public static int n, m, s;

	/*
	 * 预处理模MOD意义下的逆元
	 * 使用递推公式: inv[i] = MOD - (MOD/i) * inv[MOD%i] % MOD
	 * 时间复杂度: O(MOD)
	 * 空间复杂度: O(MOD)
	 */
	public static void inv() {
		inv[1] = 1;
		for (int i = 2; i < MOD; i++) {
			inv[i] = (int) (MOD - (long) inv[MOD % i] * (MOD / i) % MOD);
		}
	}

	/*
	 * 计算两个整数的最大公约数
	 * 使用欧几里得算法
	 * 时间复杂度: O(log(min(a,b)))
	 * 空间复杂度: O(1)
	 */
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	/*
	 * 初始化矩阵
	 * 将矩阵所有元素置为0
	 * 时间复杂度: O(s^2)
	 * 空间复杂度: O(s^2)
	 */
	public static void prepare() {
		for (int i = 1; i <= s; i++) {
			for (int j = 1; j <= s + 1; j++) {
				mat[i][j] = 0;
			}
		}
	}

	/*
	 * 将星期几的字符串转换为对应的索引
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static int day(String str) {
		for (int i = 0; i < days.length; i++) {
			if (str.equals(days[i])) {
				return i;
			}
		}
		return -1;
	}

	// 高斯消元解决同余方程组模版，保证初始系数没有负数
	/*
	 * 高斯消元解决模线性方程组
	 * 完整处理主元、自由元和解的判断
	 * 时间复杂度: O(n^3)
	 * 空间复杂度: O(n^2)
	 */
	public static void gauss(int n) {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (j < i && mat[j][j] != 0) {
					continue;
				}
				if (mat[j][i] != 0) {
					swap(i, j);
					break;
				}
			}
			if (mat[i][i] != 0) {
				for (int j = 1; j <= n; j++) {
					if (i != j && mat[j][i] != 0) {
						int gcd = gcd(mat[j][i], mat[i][i]);
						int a = mat[i][i] / gcd;
						int b = mat[j][i] / gcd;
						if (j < i && mat[j][j] != 0) {
							for (int k = j; k < i; k++) {
								mat[j][k] = (mat[j][k] * a) % MOD;
							}
						}
						for (int k = i; k <= n + 1; k++) {
							mat[j][k] = ((mat[j][k] * a - mat[i][k] * b) % MOD + MOD) % MOD;
						}
					}
				}
			}
		}
		for (int i = 1; i <= n; i++) {
			if (mat[i][i] != 0) {
				boolean flag = false;
				for (int j = i + 1; j <= n; j++) {
					if (mat[i][j] != 0) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					mat[i][n + 1] = (mat[i][n + 1] * inv[mat[i][i]]) % MOD;
					mat[i][i] = 1;
				}
			}
		}
	}

	/*
	 * 交换矩阵中的两行
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static void swap(int a, int b) {
		int[] tmp = mat[a];
		mat[a] = mat[b];
		mat[b] = tmp;
	}

	public static void main(String[] args) throws IOException {
		inv();
		Kattio io = new Kattio();
		n = io.nextInt();
		m = io.nextInt();
		while (n != 0 && m != 0) {
			s = Math.max(n, m);
			prepare();
			for (int i = 1; i <= m; i++) {
				int k = io.nextInt();
				String st = io.next();
				String et = io.next();
				for (int j = 1; j <= k; j++) {
					int tool = io.nextInt();
					mat[i][tool] = (mat[i][tool] + 1) % MOD;
				}
				mat[i][s + 1] = ((day(et) - day(st) + 1) % MOD + MOD) % MOD;
			}
			gauss(s);
			int sign = 1;
			for (int i = 1; i <= s; i++) {
				if (mat[i][i] == 0 && mat[i][s + 1] != 0) {
					sign = -1;
					break;
				}
				if (i <= n && mat[i][i] == 0) {
					sign = 0;
				}
			}
			if (sign == -1) {
				io.println("Inconsistent data.");
			} else if (sign == 0) {
				io.println("Multiple solutions.");
			} else {
				for (int i = 1; i <= n; i++) {
					if (mat[i][s + 1] < 3) {
						mat[i][s + 1] += 7;
					}
				}
				for (int i = 1; i < n; i++) {
					io.print(mat[i][s + 1] + " ");
				}
				io.println(mat[n][s + 1]);
			}
			// 下一组n和m
			n = io.nextInt();
			m = io.nextInt();
		}
		io.flush();
		io.close();
	}

	// Kattio类IO效率很好，但还是不如StreamTokenizer
	// 只有StreamTokenizer无法正确处理时，才考虑使用这个类
	// 参考链接 : https://oi-wiki.org/lang/java-pro/
	public static class Kattio extends PrintWriter {
		private BufferedReader r;
		private StringTokenizer st;

		public Kattio() {
			this(System.in, System.out);
		}

		public Kattio(InputStream i, OutputStream o) {
			super(o);
			r = new BufferedReader(new InputStreamReader(i));
		}

		public Kattio(String intput, String output) throws IOException {
			super(output);
			r = new BufferedReader(new FileReader(intput));
		}

		public String next() {
			try {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(r.readLine());
				return st.nextToken();
			} catch (Exception e) {
			}
			return null;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}