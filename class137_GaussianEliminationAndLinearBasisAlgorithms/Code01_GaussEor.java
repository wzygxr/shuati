package class134;

/**
 * 高斯消元解决异或方程组 - HDU 5833 Zhu and 772002
 * 
 * 题目描述：
 * 有一个长度为n的数组arr，可能有重复值，数字都是long类型的正数
 * 每个数拥有的质数因子一定不超过2000，每个数最多挑选一次
 * 在至少要选一个数的情况下，你可以随意挑选数字乘起来
 * 乘得的结果需要是完全平方数，请问有几种挑选数字的方法
 * 方法数可能很大，答案对 1000000007 取模
 * 
 * 输入约束：
 * 1 <= n <= 300
 * 1 <= arr[i] <= 10^18
 * 
 * 测试链接：https://acm.hdu.edu.cn/showproblem.php?pid=5833
 * 提交时请把类名改成"Main"
 * 
 * 算法原理详解：
 * 1. 数学建模：一个数是完全平方数当且仅当它的所有质因子的幂次都是偶数
 * 2. 质因数分解：对每个数进行质因数分解，统计每个质因子出现次数的奇偶性
 * 3. 异或方程组：每个质因子对应一个方程，表示该质因子在乘积中出现的总次数为偶数
 * 4. 高斯消元：求解方程组的自由元个数，方案数为 2^(自由元个数) - 1
 * 
 * 时间复杂度分析：
 * - 质数筛法：O(MAXV * log(log(MAXV))) ≈ O(2000)
 * - 质因数分解：O(n * π(2000)) ≈ O(300 * 303) ≈ 90,900
 * - 高斯消元：O(π(2000)^3) ≈ O(303^3) ≈ 27,818,127
 * - 总复杂度：O(π(2000)^3) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 质数数组：O(MAXV) ≈ O(2000)
 * - 增广矩阵：O(n * π(2000)) ≈ O(300 * 303) ≈ 90,900
 * - 总空间：O(n * π(2000)) 在可接受范围内
 * 
 * 工程化考量：
 * 1. 异常处理：处理质因数分解时的边界情况
 * 2. 性能优化：使用位运算优化异或操作
 * 3. 内存管理：合理设置数组大小避免内存溢出
 * 4. 可读性：详细注释和变量命名规范
 * 
 * 关键优化点：
 * - 使用质数筛法预处理2000以内的质数
 * - 对每个数进行质因数分解时只考虑2000以内的质因子
 * - 使用高斯消元求解异或方程组的自由元个数
 * - 使用快速幂计算2的幂次模1000000007
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
 * 高斯消元解决异或方程组 - HDU 5833 树的因子
 * 
 * 题目解析：
 * 本题要求从给定数组中选择一些数字，使得它们的乘积是完全平方数。
 * 一个数是完全平方数当且仅当它的所有质因子的幂次都是偶数。
 * 因此，我们需要选择一些数字，使得每个质因子在整个乘积中出现的次数都是偶数。
 * 
 * 解题思路：
 * 1. 对每个数进行质因数分解，统计每个质因子出现次数的奇偶性
 * 2. 构造异或方程组，每个方程表示一个质因子的奇偶性约束
 * 3. 使用高斯消元求解自由元个数
 * 4. 方案数为 2^(自由元个数) - 1（减1是因为不能一个都不选）
 * 
 * 时间复杂度：O(n * π(2000) + π(2000)^3)
 * 空间复杂度：O(n * π(2000))
 * 其中π(2000)表示2000以内的质数个数，约为303
 */
public class Code01_GaussEor {

	public static int MOD = 1000000007;

	public static int MAXV = 2000;

	public static int MAXN = 305;

	public static long[] arr = new long[MAXN];

	public static int[][] mat = new int[MAXN][MAXN];

	// 收集2000以内的质数，一共就303个，这是大于arr的个数的
	public static int[] prime = new int[MAXV + 1];

	// 2000以内质数的个数，一共就303个，这是大于arr的个数的
	public static int cnt;

	// 埃氏筛需要
	public static boolean[] visit = new boolean[MAXV + 1];

	// pow2[i] : 2的i次方 % MOD
	public static int[] pow2 = new int[MAXN];

	public static int n;

	/**
	 * 预处理函数
	 * 1. 使用埃氏筛法找出2000以内的所有质数
	 * 2. 预计算2的幂次方
	 */
	public static void prepare() {
		// 得到2000以内的质数
		// 如果不会就去看，讲解097，埃氏筛
		// 当然也可以用欧拉筛，也在讲解097
		for (int i = 2; i * i <= MAXV; i++) {
			if (!visit[i]) {
				for (int j = i * i; j <= MAXV; j += i) {
					visit[j] = true;
				}
			}
		}
		cnt = 0;
		for (int i = 2; i <= MAXV; i++) {
			if (!visit[i]) {
				prime[++cnt] = i;
			}
		}
		// 2的i次方%MOD的结果
		pow2[0] = 1;
		for (int i = 1; i < MAXN; i++) {
			pow2[i] = (pow2[i - 1] * 2) % MOD;
		}
	}

	public static void main(String[] args) {
		prepare();
		// 题目会读取10^18范围内的long类型数字
		// 用StreamTokenizer可能无法正确读取，因为先变成double再转成long
		// 这里用Kattio类，具体看讲解019的代码中，Code05_Kattio文件
		// 有详细的说明
		Kattio io = new Kattio();
		int test = io.nextInt();
		for (int t = 1; t <= test; t++) {
			n = io.nextInt();
			for (int i = 1; i <= n; i++) {
				arr[i] = io.nextLong();
			}
			io.println("Case #" + t + ":");
			io.println(compute());
		}
		io.flush();
		io.close();
	}

	/**
	 * 计算满足条件的方案数
	 * 
	 * @return 满足条件的方案数
	 */
	public static int compute() {
		// 初始化矩阵
		for (int i = 1; i <= cnt; i++) {
			for (int j = 1; j <= cnt + 1; j++) {
				mat[i][j] = 0;
			}
		}
		long cur;
		// 构造增广矩阵
		for (int i = 1; i <= n; i++) {
			cur = arr[i];
			for (int j = 1; j <= cnt && cur != 0; j++) {
				// 统计质因子prime[j]在arr[i]中出现次数的奇偶性
				while (cur % prime[j] == 0) {
					mat[j][i] ^= 1; // 奇偶性用异或运算表示
					cur /= prime[j];
				}
			}
		}
		// 高斯消元
		gauss(cnt);
		int main = 0; // 主元的数量
		for (int i = 1; i <= cnt; i++) {
			if (mat[i][i] == 1) {
				main++;
			}
		}
		// 影响每个主元的自由元们一旦确定
		// 那么该主元选和不选也就唯一确定了
		// 所以重点是自由元如何决策，n - main就是自由元的数量
		// 自由元之间一定相互独立，每个自由元都可以做出选和不选的决定
		// 所以一共是2的(n - main)次方种决策
		// 但是想象一下，如果所有自由元都不选，
		// mat值的部分(cnt+1列)全是0啊！那么意味着，所有主元也都不选
		// 但是题目要求至少要选一个数，所以不能出现自由元都不选的情况
		// 所以返回方法数-1
		// 能够分析的前提是对主元和自由元之间的关系有清晰的认识
		return pow2[n - main] - 1;
	}

	/**
	 * 高斯消元解决异或方程组模版
	 * 
	 * @param n 未知数个数
	 */
	public static void gauss(int n) {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (j < i && mat[j][j] == 1) {
					continue;
				}
				if (mat[j][i] == 1) {
					swap(i, j);
					break;
				}
			}
			if (mat[i][i] == 1) {
				for (int j = 1; j <= n; j++) {
					if (i != j && mat[j][i] == 1) {
						for (int k = i; k <= n + 1; k++) {
							mat[j][k] ^= mat[i][k];
						}
					}
				}
			}
		}
	}

	/**
	 * 交换矩阵中的两行
	 * 
	 * @param a 行号1
	 * @param b 行号2
	 */
	public static void swap(int a, int b) {
		int[] tmp = mat[a];
		mat[a] = mat[b];
		mat[b] = tmp;
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