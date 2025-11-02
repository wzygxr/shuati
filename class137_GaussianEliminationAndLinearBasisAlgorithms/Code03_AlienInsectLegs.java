package class134;

/**
 * 高斯消元解决异或方程组 - 洛谷 P2447 外星千足虫
 * 
 * 题目描述：
 * 一共有n种虫子，编号1~n，虫子腿为奇数认为是外星虫，偶数认为是地球虫
 * 一共有m条虫子腿的测量记录，记录编号1~m
 * 比如其中一条测量记录为，011 1，表示1号虫没参与，2号、3号虫参与了，总腿数为奇数
 * 测量记录保证不会有自相矛盾的情况，但是可能有冗余的测量结果
 * 也许拥有从第1号到第k号测量记录就够了，k+1~m号测量记录有或者没有都不影响测量结果
 * 打印这个k，并且打印每种虫子到底是外星虫还是地球虫
 * 如果使用所有的测量结果，依然无法确定每种虫子的属性，打印"Cannot Determine"
 * 
 * 输入约束：
 * 1 <= n <= 1000
 * 1 <= m <= 2000
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P2447
 * 提交时请把类名改成"Main"
 * 
 * 算法原理详解：
 * 1. 问题建模：这是一个异或方程组的求解问题
 *    - 变量xi表示第i个虫子是否为外星虫（1表示外星虫，0表示地球虫）
 *    - 每条测量记录对应一个方程：参与测量的虫子腿数总和的奇偶性
 *    - 系数aij表示第j个虫子是否参与第i条记录的测量
 *    - 常数项bi表示第i条记录测量结果的奇偶性（1表示奇数，0表示偶数）
 * 2. 高斯消元：按顺序处理每条测量记录，逐步构建增广矩阵
 * 3. 解的情况判断：
 *    - 唯一解：系数矩阵的秩等于未知数个数n
 *    - 无解：出现矛盾方程（0=1的情况）
 *    - 无穷解：系数矩阵的秩小于n，存在自由元
 * 
 * 时间复杂度分析：
 * - 高斯消元：O(m * n^2) ≈ O(2000 * 1000^2) = O(2,000,000,000)
 * - 实际运行中由于使用位运算优化，复杂度会降低
 * - 最坏情况下需要处理所有m条记录
 * 
 * 空间复杂度分析：
 * - 增广矩阵：O(m * n) ≈ O(2000 * 1000) = O(2,000,000)
 * - 使用位运算压缩存储，实际空间占用会减少
 * 
 * 工程化考量：
 * 1. 性能优化：使用位运算压缩存储增广矩阵
 * 2. 内存管理：合理设置数组大小，避免内存溢出
 * 3. 边界处理：处理n=1或m=0的特殊情况
 * 4. 可读性：详细注释和变量命名规范
 * 
 * 关键优化点：
 * - 使用long数组进行位运算压缩存储，每个long可以存储64位
 * - 按顺序处理测量记录，动态构建增广矩阵
 * - 使用高斯消元求解异或方程组
 * - 记录需要使用的最少测量记录数k
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * 高斯消元解决异或方程组 - 洛谷 P2447 外星千足虫
 * 
 * 题目解析：
 * 本题要求根据测量记录确定每种虫子的类型（地球虫或外星虫）。
 * 地球虫的腿数为偶数，外星虫的腿数为奇数。
 * 每条测量记录表示参与测量的虫子腿数总和的奇偶性。
 * 
 * 解题思路：
 * 1. 将问题转化为异或方程组：
 *    - 每条测量记录对应一个方程
 *    - 变量xi表示第i个虫子是否为外星虫（1表示外星虫，0表示地球虫）
 *    - 系数aij表示第j个虫子是否参与第i条记录的测量
 *    - 常数项bi表示第i条记录测量结果的奇偶性
 * 2. 使用高斯消元求解
 * 3. 如果有唯一解，则可以确定所有虫子的类型
 * 4. 记录需要使用的最少测量记录数k
 * 
 * 时间复杂度：O(m * n^2)
 * 空间复杂度：O(m * n)
 */
public class Code03_AlienInsectLegs {

	public static int BIT = 64;

	public static int MAXN = 2002;

	public static int MAXM = MAXN / BIT + 1;

	public static long[][] mat = new long[MAXN][MAXM];

	public static int n, m, s;

	public static int need;

	/**
	 * 高斯消元解决异或方程组模版 + 位图，很小的改写
	 * 
	 * @param n 未知数个数
	 */
	public static void gauss(int n) {
		need = 0;
		for (int i = 1; i <= n; i++) {
			for (int j = i; j <= n; j++) {
				if (get(j, i) == 1) {
					swap(i, j);
					need = Math.max(need, j);
					break;
				}
			}
			// 一旦没有唯一解，可以结束了
			if (get(i, i) == 0) {
				return;
			}
			for (int j = 1; j <= n; j++) {
				if (i != j && get(j, i) == 1) {
					// 因为列从1开始，所以从第1位状态开始才有用
					// 于是1~n+1列的状态，对应1~n+1位
					// 但是位图中永远有0位，只不过从来不使用
					// 于是一共有n+2位状态，都需要异或
					eor(i, j, n + 2);
				}
			}
		}
	}

	/**
	 * 把row行，col列的状态设置成v
	 * 
	 * @param row 行号
	 * @param col 列号
	 * @param v   要设置的值（0或1）
	 */
	public static void set(int row, int col, int v) {
		if (v == 0) {
			mat[row][col / BIT] &= ~(1L << (col % BIT));
		} else {
			mat[row][col / BIT] |= 1L << (col % BIT);
		}
	}

	/**
	 * 得到row行，col列的状态
	 * 
	 * @param row 行号
	 * @param col 列号
	 * @return 该位置的值（0或1）
	 */
	public static int get(int row, int col) {
		return ((mat[row][col / BIT] >> (col % BIT)) & 1) == 1 ? 1 : 0;
	}

	/**
	 * row2行状态 = row2行状态 ^ row1行状态
	 * 
	 * @param row1 行号1
	 * @param row2 行号2
	 * @param bits 状态位数
	 */
	public static void eor(int row1, int row2, int bits) {
		for (int k = 0; k <= bits / BIT; k++) {
			mat[row2][k] ^= mat[row1][k];
		}
	}

	/**
	 * 交换矩阵中的两行
	 * 
	 * @param a 行号1
	 * @param b 行号2
	 */
	public static void swap(int a, int b) {
		long[] tmp = mat[a];
		mat[a] = mat[b];
		mat[b] = tmp;
	}

	public static void main(String[] args) throws IOException {
		Kattio io = new Kattio();
		n = io.nextInt();
		m = io.nextInt();
		s = Math.max(n, m);
		// 读取测量记录
		for (int i = 1; i <= m; i++) {
			char[] line = io.next().toCharArray();
			// 设置系数矩阵
			for (int j = 1; j <= n; j++) {
				set(i, j, line[j - 1] - '0');
			}
			// 设置常数项
			set(i, s + 1, io.nextInt());
		}
		// 高斯消元
		gauss(s);
		int sign = 1;
		// 判断是否有唯一解
		for (int i = 1; i <= n; i++) {
			if (get(i, i) == 0) {
				sign = 0;
				break;
			}
		}
		if (sign == 0) {
			io.println("Cannot Determine");
		} else {
			io.println(need);
			// 输出每种虫子的类型
			for (int i = 1; i <= n; i++) {
				if (get(i, s + 1) == 1) {
					io.println("?y7M#");
				} else {
					io.println("Earth");
				}
			}
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