package class097;

// Miller-Rabin测试，java版，不用BigInteger也能通过的实现
// 这个文件课上没有讲，课上讲的是，java中的long是64位
// 所以 long * long 需要128位才能不溢出，于是直接用BigInteger中自带的方法了
// 但是
// 如果a和b都是long类型，其实 a * b 的过程，用位运算去实现，中间结果都 % mod 即可
// 这样就不需要使用BigInteger
// 讲解033，位运算实现乘法，增加 每一步 % mod 的逻辑即可
// 重点看一下本文件中的 multiply 方法，就是位运算实现乘法的改写
// C++的同学也可以用这种方式来实现，也不需要定义128位的long类型
// 测试链接 : https://www.luogu.com.cn/problem/U148828
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 相关题目：
// 1. POJ 1811 Prime Test
//    链接：http://poj.org/problem?id=1811
//    题目描述：给定一个大整数(2 <= N < 2^54)，判断它是否为素数，如果不是输出最小质因子
// 2. Luogu U148828 大数质数判断
//    链接：https://www.luogu.com.cn/problem/U148828
//    题目描述：判断给定的大整数是否为质数
// 3. Codeforces 679A Bear and Prime 100 (交互题)
//    链接：https://codeforces.com/problemset/problem/679/A
//    题目描述：系统想了一个2到100之间的数，你需要通过最多20次询问判断这个数是否为质数

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code02_LargeNumberIsPrime4 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		int t = Integer.valueOf(br.readLine());
		for (int i = 0; i < t; i++) {
			long n = Long.valueOf(br.readLine());
			out.println(millerRabin(n) ? "Yes" : "No");
		}
		out.flush();
		out.close();
		br.close();
	}

	// 使用前12个质数作为测试底数，可以有效降低误判率
	public static long[] p = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37 };

	/**
	 * Miller-Rabin素性测试主函数
	 * 时间复杂度：O(s * (logn)^3)，其中s是测试轮数
	 * 空间复杂度：O(1)
	 * 
	 * @param n 待测试的数
	 * @return 如果是质数返回true，否则返回false
	 * 
	 * 算法特点：
	 * 1. 这是一个概率算法，有一定误判率
	 * 2. 对于合数，误判为质数的概率不超过(1/4)^s
	 * 3. 对于质数，永远不会误判
	 * 
	 * 工程化考虑：
	 * 1. 使用固定的质数作为底数，提高稳定性
	 * 2. 对于小数和偶数进行特殊处理，提高效率
	 */
	public static boolean millerRabin(long n) {
		if (n <= 2) {
			return n == 2;
		}
		// 偶数(除了2)都不是质数
		if ((n & 1) == 0) {
			return false;
		}
		for (int i = 0; i < p.length && p[i] < n; i++) {
			// witness函数用于单次测试
			if (witness(p[i], n)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Miller-Rabin单次测试函数
	 * 
	 * @param a 测试底数
	 * @param n 待测试数
	 * @return 如果n是合数返回true，否则返回false
	 * 
	 * 算法原理：
	 * 1. 将n-1表示为u*2^t的形式，其中u是奇数
	 * 2. 计算a^u mod n
	 * 3. 如果结果为1或n-1，则通过本次测试
	 * 4. 否则，重复计算平方模运算t-1次
	 * 5. 如果在过程中得到n-1，则通过本次测试
	 * 6. 否则，n是合数
	 */
	public static boolean witness(long a, long n) {
		long u = n - 1;
		int t = 0;
		// 将n-1分解为u*2^t的形式，其中u是奇数
		while ((u & 1) == 0) {
			t++;
			u >>= 1;
		}
		// 计算a^u mod n，使用multiply方法避免溢出
		long x1 = power(a, u, n), x2;
		for (int i = 1; i <= t; i++) {
			x2 = power(x1, 2, n);
			// 二次探测：如果x2=1但x1既不是1也不是n-1，则存在非平凡平方根，n是合数
			if (x2 == 1 && x1 != 1 && x1 != n - 1) {
				return true;
			}
			x1 = x2;
		}
		// 如果最后结果不是1，则违反费马小定理，n是合数
		if (x1 != 1) {
			return true;
		}
		return false;
	}

	/**
	 * 快速幂运算：计算 n^p mod mod
	 * 时间复杂度：O(log p)
	 * 空间复杂度：O(1)
	 * 
	 * @param n 底数
	 * @param p 指数
	 * @param mod 模数
	 * @return n^p mod mod
	 * 
	 * 算法原理：
	 * 1. 将指数p用二进制表示
	 * 2. 从低位到高位，如果该位为1，则将当前底数乘入结果
	 * 3. 每次将底数平方，指数右移一位
	 * 
	 * 与普通快速幂的区别：
	 * 使用multiply方法替代普通乘法，避免大数乘法溢出
	 */
	public static long power(long n, long p, long mod) {
		long ans = 1;
		while (p > 0) {
			if ((p & 1) == 1) {
				ans = multiply(ans, n, mod);
			}
			n = multiply(n, n, mod);
			p >>= 1;
		}
		return ans;
	}

	/**
	 * 龟速乘（防止溢出的乘法实现）
	 * 时间复杂度：O(log b)
	 * 空间复杂度：O(1)
	 * 
	 * @param a 被乘数
	 * @param b 乘数
	 * @param mod 模数
	 * @return (a * b) % mod
	 * 
	 * 算法原理：
	 * 1. 将乘数b用二进制表示
	 * 2. 从低位到高位，如果该位为1，则将当前被乘数加到结果中
	 * 3. 每次将被乘数加倍，乘数右移一位
	 * 4. 所有运算都对mod取模，避免溢出
	 * 
	 * 应用场景：
	 * 在需要计算(a * b) % mod且a、b都接近long最大值时，
	 * 直接计算a * b会溢出，使用龟速乘可以避免这个问题
	 */
	public static long multiply(long a, long b, long mod) {
		a = (a % mod + mod) % mod;
		b = (b % mod + mod) % mod;
		long ans = 0;
		while (b != 0) {
			if ((b & 1) != 0) {
				ans = (ans + a) % mod;
			}
			a = (a + a) % mod;
			b >>= 1;
		}
		return ans;
	}

}