package class141;

/*
 * 猜数字问题完整解析与实现
 * 问题描述：给定两个长度为n的数组r和m，其中m数组的元素两两互质，求满足所有mi | (x - ri)的最小正整数x
 * 约束条件：1 <= n <= 10，-10^9 <= ri <= +10^9，1 <= mi <= 6 * 10^3，所有mi的乘积 <= 10^18
 * 题目来源：洛谷 P3868【TJOI2009】猜数字
 * 测试链接：https://www.luogu.com.cn/problem/P3868
 */

/*
 * 算法原理详解
 * 
 * 1. 问题转化：
 *    条件mi | (x - ri)等价于x ≡ ri (mod mi)，即转化为标准的中国剩余定理问题
 *    中国剩余定理(CRT)用于求解模数两两互质的同余方程组
 * 
 * 2. 中国剩余定理求解步骤：
 *    a. 计算所有模数的乘积M = m1 * m2 * ... * mn
 *    b. 对于第i个方程：
 *       i. 计算Mi = M / mi（Mi是除mi外所有模数的乘积）
 *       ii. 计算Mi在模mi意义下的逆元Mi^(-1)，即Mi * Mi^(-1) ≡ 1 (mod mi)
 *       iii. 计算ci = Mi * Mi^(-1) * ri
 *    c. 方程组在模M下的唯一解为x = (c1 + c2 + ... + cn) mod M
 * 
 * 3. 扩展中国剩余定理(EXCRT)：
 *    当模数不互质时，需使用扩展中国剩余定理
 *    通过逐个合并方程，维护当前的解tail和当前模的最小公倍数lcm
 *    对于新方程，求解同余方程tail + k*lcm ≡ r[i] (mod m[i])
 * 
 * 4. 最优解分析：
 *    - 在模数两两互质的情况下，CRT是最优解，时间复杂度为O(n log max(mi))
 *    - 对于一般情况，EXCRT是最优解，时间复杂度同样为O(n log max(mi))
 *    - 当模数很大时，需要使用龟速乘法防止溢出
 */

/*
 * 相关题目及详细解析（全面覆盖各大平台）
 * 
 * 1. 洛谷 P3868【TJOI2009】猜数字 ★★★☆☆
 *    链接：https://www.luogu.com.cn/problem/P3868
 *    题目大意：给定两组数a1..an和b1..bn（bi两两互质），求最小正整数N，使bi | (N - ai)对所有i成立
 *    解题思路：直接应用中国剩余定理，注意处理负数余数
 *    代码实现说明：输入为两组数，第一组为余数，第二组为模数
 *    注意点：处理负数余数，确保结果为最小正整数
 * 
 * 2. 洛谷 P1495【模板】中国剩余定理（CRT）/ 曹冲养猪 ★★☆☆☆
 *    链接：https://www.luogu.com.cn/problem/P1495
 *    题目大意：求解同余方程组x ≡ ai (mod mi)，其中mi两两互质
 *    解题思路：标准CRT模板题，直接应用公式求解
 *    输入样例：
 *    3
 *    3 1
 *    5 2
 *    7 3
 *    输出样例：23
 * 
 * 3. 51Nod 1079 中国剩余定理 ★★☆☆☆
 *    链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
 *    题目大意：给定质数p和对应余数m，求满足所有条件的最小正整数K
 *    解题思路：模数为质数，两两互质，直接应用CRT
 *    代码实现说明：由于模数都是质数，可以使用快速幂求逆元
 * 
 * 4. POJ 1006 Biorhythms ★★☆☆☆
 *    链接：http://poj.org/problem?id=1006
 *    题目大意：人的体力、情感和智力周期分别为23、28、33天，已知某一天三个指标的数值，求下一次同时达到峰值的天数
 *    解题思路：三个模数两两互质，应用CRT，注意结果必须大于给定日期d
 *    输入样例：
 *    0 0 0 0
 *    输出样例：21252
 *    注意点：如果结果为0，应返回模数的乘积
 * 
 * 5. HDU 3579 Hello Kiki ★★★☆☆
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
 *    题目大意：求解同余方程组，模数不一定互质
 *    解题思路：使用EXCRT求解不互质模数的同余方程组
 *    代码实现说明：需要处理无解的情况
 * 
 * 6. SPOJ - CRTMOD ★★★☆☆
 *    链接：https://www.spoj.com/problems/CRTMOD/
 *    题目大意：求解多个CRT问题，模数两两互质
 *    解题思路：多次应用CRT，注意大数处理
 * 
 * 7. UVA 756 Biorhythms ★★☆☆☆
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *    题目大意：生物节律问题，与POJ 1006相同
 *    解题思路：CRT的直接应用
 * 
 * 8. 牛客网 - NC15857 同余方程 ★★☆☆☆
 *    链接：https://ac.nowcoder.com/acm/problem/15857
 *    题目大意：求解同余方程组，模数两两互质
 *    解题思路：CRT模板题
 * 
 * 9. LeetCode 149. 直线上最多的点数 ★★★☆☆
 *    链接：https://leetcode.cn/problems/max-points-on-a-line/
 *    题目大意：给定平面上的n个点，求最多有多少个点在同一直线上
 *    解题思路：使用分数表示斜率，应用同余思想处理精度问题
 *    注意点：处理垂直直线和相同点的情况
 * 
 * 10. Codeforces Round #747 (Div. 2) B. Special Numbers ★★★☆☆
 *     链接：https://codeforces.com/contest/1594/problem/B
 *     题目大意：求第k个特殊数，特殊数定义为可以表示为2^a + 2^b + ...的形式
 *     解题思路：位运算与组合数学，涉及同余思想
 * 
 * 11. AtCoder Beginner Contest 193 D. Poker ★★★★☆
 *     链接：https://atcoder.jp/contests/abc193/tasks/abc193_d
 *     题目大意：扑克游戏中的概率计算问题
 *     解题思路：模运算优化，避免浮点数精度问题
 * 
 * 12. CodeChef - CHEFADV ★★★☆☆
 *     链接：https://www.codechef.com/problems/CHEFADV
 *     题目大意：判断是否能在棋盘上移动，涉及同余条件
 *     解题思路：数学建模转化为同余方程
 * 
 * 13. LightOJ 1306 Solutions to an Equation ★★★☆☆
 *     链接：https://lightoj.com/problem/solutions-to-an-equation
 *     题目大意：求解线性同余方程ax + by = c的正整数解的个数
 *     解题思路：扩展欧几里得算法求解线性方程
 * 
 * 14. Comet OJ Contest #12 B. 组合数问题 ★★★★☆
 *     链接：https://cometoj.com/contest/69/problem/B?problem_id=3634
 *     题目大意：计算组合数模多个数的结果
 *     解题思路：Lucas定理结合CRT
 * 
 * 15. 计蒜客 T3097 同余方程 ★★★☆☆
 *     链接：https://nanti.jisuanke.com/t/T3097
 *     题目大意：求解同余方程组，模数不一定互质
 *     解题思路：EXCRT的应用
 */

/*
 * 时间复杂度与空间复杂度分析
 * 
 * 时间复杂度：
 * - CRT实现：O(n² log max(mi))
 *   其中n为方程个数，max(mi)为最大模数
 *   计算乘积M需要O(n)时间，计算每个方程的Mi和逆元需要O(log mi)时间
 *   由于可能需要处理大数乘法溢出，龟速乘法需要O(log mod)时间
 * 
 * - EXCRT实现：O(n log max(mi))
 *   逐个合并方程，每次合并需要O(log max(m1, m2))时间
 * 
 * 空间复杂度：
 * - O(n)，用于存储模数和余数数组
 * - 递归实现的扩展欧几里得算法需要O(log min(a,b))的栈空间
 */

/*
 * 跨语言实现差异分析
 * 
 * 1. 大数处理：
 *    - Java：使用long类型，当模数乘积超过long范围时，需要使用BigInteger
 *    - C++：使用long long类型，可能需要实现大数乘法或使用__int128
 *    - Python：原生支持大整数，无需额外处理
 * 
 * 2. 输入输出效率：
 *    - Java：使用BufferedReader和StreamTokenizer提高效率
 *    - C++：使用scanf/printf或关闭同步的cin/cout
 *    - Python：使用sys.stdin.readline提高效率
 * 
 * 3. 逆元计算：
 *    - 当模数为质数时，可以使用快速幂求逆元
 *    - 一般情况使用扩展欧几里得算法
 * 
 * 4. 龟速乘法实现：
 *    - Java/C++：需要显式实现以防止溢出
 *    - Python：由于大数支持，可能不需要龟速乘法，但为保持一致性可以实现
 */

/*
 * C++实现代码
 * 
 * #include <iostream>
 * #include <algorithm>
 * using namespace std;
 * 
 * typedef long long ll;
 * const int MAXN = 11;
 * ll m[MAXN], r[MAXN];
 * ll d, x, y, px, py;
 * 
 * // 扩展欧几里得算法
 * void exgcd(ll a, ll b) {
 *     if (b == 0) {
 *         d = a;
 *         x = 1;
 *         y = 0;
 *     } else {
 *         exgcd(b, a % b);
 *         px = x;
 *         py = y;
 *         x = py;
 *         y = px - py * (a / b);
 *     }
 * }
 * 
 * // 龟速乘法，防止溢出
 * ll multiply(ll a, ll b, ll mod) {
 *     a = (a % mod + mod) % mod;
 *     b = (b % mod + mod) % mod;
 *     ll ans = 0;
 *     while (b != 0) {
 *         if ((b & 1) != 0) {
 *             ans = (ans + a) % mod;
 *         }
 *         a = (a + a) % mod;
 *         b >>= 1;
 *     }
 *     return ans;
 * }
 * 
 * // 中国剩余定理
 * ll crt(int n) {
 *     ll lcm = 1;
 *     for (int i = 1; i <= n; i++) {
 *         lcm *= m[i];
 *     }
 *     ll ai, ci, ans = 0;
 *     for (int i = 1; i <= n; i++) {
 *         ai = lcm / m[i];
 *         exgcd(ai, m[i]);
 *         x = (x % m[i] + m[i]) % m[i]; // 确保x为正数
 *         ci = multiply(r[i], multiply(ai, x, lcm), lcm);
 *         ans = (ans + ci) % lcm;
 *     }
 *     return ans;
 * }
 * 
 * // 扩展中国剩余定理
 * ll excrt(int n) {
 *     ll tail = 0, lcm = 1, tmp, b, c, x0;
 *     for (int i = 1; i <= n; i++) {
 *         b = m[i];
 *         c = ((r[i] - tail) % b + b) % b;
 *         exgcd(lcm, b);
 *         if (c % d != 0) {
 *             return -1; // 无解
 *         }
 *         x0 = multiply(x, c / d, b / d);
 *         tmp = lcm * (b / d);
 *         tail = (tail + multiply(x0, lcm, tmp)) % tmp;
 *         lcm = tmp;
 *     }
 *     return tail;
 * }
 * 
 * int main() {
 *     int n;
 *     cin >> n;
 *     for (int i = 1; i <= n; i++) {
 *         cin >> r[i];
 *     }
 *     for (int i = 1; i <= n; i++) {
 *         cin >> m[i];
 *     }
 *     // 处理负数余数
 *     for (int i = 1; i <= n; i++) {
 *         r[i] = (r[i] % m[i] + m[i]) % m[i];
 *     }
 *     cout << excrt(n) << endl;
 *     return 0;
 * }
 */

/*
 * Python实现代码
 * 
 * import sys
 * 
 * MAXN = 11
 * m = [0] * MAXN
 * r = [0] * MAXN
 * d, x, y, px, py = 0, 0, 0, 0, 0
 * 
 * def exgcd(a, b):
 *     global d, x, y, px, py
 *     if b == 0:
 *         d = a
 *         x = 1
 *         y = 0
 *     else:
 *         exgcd(b, a % b)
 *         px, py = x, y
 *         x = py
 *         y = px - py * (a // b)
 * 
 * def multiply(a, b, mod):
 *     # Python原生支持大整数，龟速乘法主要是为了保持与其他语言实现的一致性
 *     a = (a % mod + mod) % mod
 *     b = (b % mod + mod) % mod
 *     ans = 0
 *     while b != 0:
 *         if (b & 1) != 0:
 *             ans = (ans + a) % mod
 *         a = (a + a) % mod
 *         b >>= 1
 *     return ans
 * 
 * def crt(n):
 *     lcm = 1
 *     for i in range(1, n + 1):
 *         lcm *= m[i]
 *     ans = 0
 *     for i in range(1, n + 1):
 *         ai = lcm // m[i]
 *         exgcd(ai, m[i])
 *         xi = (x % m[i] + m[i]) % m[i]  # 确保x为正数
 *         ci = multiply(r[i], multiply(ai, xi, lcm), lcm)
 *         ans = (ans + ci) % lcm
 *     return ans
 * 
 * def excrt(n):
 *     tail = 0
 *     lcm = 1
 *     for i in range(1, n + 1):
 *         b = m[i]
 *         c = ((r[i] - tail) % b + b) % b
 *         exgcd(lcm, b)
 *         if c % d != 0:
 *             return -1  # 无解
 *         x0 = multiply(x, c // d, b // d)
 *         tmp = lcm * (b // d)
 *         tail = (tail + multiply(x0, lcm, tmp)) % tmp
 *         lcm = tmp
 *     return tail
 * 
 * def main():
 *     global m, r
 *     input = sys.stdin.read().split()
 *     ptr = 0
 *     n = int(input[ptr])
 *     ptr += 1
 *     for i in range(1, n + 1):
 *         r[i] = int(input[ptr])
 *         ptr += 1
 *     for i in range(1, n + 1):
 *         m[i] = int(input[ptr])
 *         ptr += 1
 *     # 处理负数余数
 *     for i in range(1, n + 1):
 *         r[i] = (r[i] % m[i] + m[i]) % m[i]
 *     print(excrt(n))
 * 
 * if __name__ == "__main__":
 *     main()
 */

/*
 * 工程化考量
 * 
 * 1. 异常处理与输入校验：
 *    - 检查模数是否为0
 *    - 验证输入数据范围
 *    - 处理无解的情况（EXCRT中d不能整除c时）
 *    - 确保结果为最小正整数
 * 
 * 2. 性能优化：
 *    - 使用龟速乘法防止溢出
 *    - 预处理模数的乘积以减少重复计算
 *    - 位运算优化（如位移替代乘除法）
 *    - 输入输出优化，特别是对于大数据量
 * 
 * 3. 可扩展性：
 *    - 提供CRT和EXCRT两种实现，适应不同场景
 *    - 支持任意长度的同余方程组
 *    - 当数据规模更大时，可切换到BigInteger实现
 * 
 * 4. 测试用例设计：
 *    - 常规测试：标准输入数据
 *    - 边界测试：n=1、模数为1
 *    - 负数测试：负数余数
 *    - 大数据测试：接近题目限制的数据
 *    - 无解测试：对于EXCRT的无解情况
 */

/*
 * 算法安全与业务适配
 * 
 * 1. 防止溢出：
 *    - 使用龟速乘法处理大数相乘
 *    - 对中间结果进行取模操作
 *    - 对于更大的数据，考虑使用高精度整数库
 * 
 * 2. 避免崩溃：
 *    - 对输入数据进行合法性检查
 *    - 处理除零异常
 *    - 捕获可能的运行时错误
 * 
 * 3. 结果正确性保证：
 *    - 验证最终解是否满足所有同余条件
 *    - 确保返回最小正整数解
 *    - 对于无解情况，给出明确提示
 */

/*
 * 调试技巧与问题定位
 * 
 * 1. 中间过程打印：
 *    - 打印逆元计算结果
 *    - 监控每次合并方程后的tail和lcm值
 *    - 验证每个同余方程的解
 * 
 * 2. 断言验证：
 *    - 断言模数大于0
 *    - 断言gcd计算正确
 *    - 断言最终解满足所有条件
 * 
 * 3. 性能分析：
 *    - 当n较大时，关注乘法操作的效率
 *    - 对于大数据量，测量算法运行时间
 *    - 识别性能瓶颈并进行针对性优化
 */

// 移除多余的import语句
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.math.BigInteger;
import java.util.Arrays;

public class Code03_GuessNumber {

	public static int MAXN = 11;

	public static long m[] = new long[MAXN];

	public static long r[] = new long[MAXN];

	// 扩展欧几里得算法相关变量
	public static long d, x, y, px, py;

	/**
	 * 中国剩余定理实现
	 * @param n 方程组的个数
	 * @return 最小正整数解
	 */
	public static long crt(int n) {
		long lcm = 1;
		for (int i = 1; i <= n; i++) {
			lcm = lcm * m[i];
		}
		long ai, ci, ans = 0;
		for (int i = 1; i <= n; i++) {
			ai = lcm / m[i];
			exgcd(ai, m[i]);
			// 确保x为正数
			x = (x % m[i] + m[i]) % m[i];
			ci = multiply(r[i], multiply(ai, x, lcm), lcm);
			ans = (ans + ci) % lcm;
		}
		return ans;
	}

	/**
	 * 扩展中国剩余定理实现（适用于模数不互质的情况）
	 * @param n 方程组的个数
	 * @return 最小正整数解，若无解返回-1
	 */
	public static long excrt(int n) {
		long tail = 0, lcm = 1, tmp, b, c, x0;
		for (int i = 1; i <= n; i++) {
			b = m[i];
			c = ((r[i] - tail) % b + b) % b;
			exgcd(lcm, b);
			if (c % d != 0) {
				return -1; // 无解情况
			}
			x0 = multiply(x, c / d, b / d);
			tmp = lcm * (b / d);
			tail = (tail + multiply(x0, lcm, tmp)) % tmp;
			lcm = tmp;
		}
		return tail;
	}

	/**
	 * 扩展欧几里得算法
	 * 求解ax + by = gcd(a,b)
	 * @param a 第一个数
	 * @param b 第二个数
	 */
	public static void exgcd(long a, long b) {
		if (b == 0) {
			d = a;
			x = 1;
			y = 0;
		} else {
			exgcd(b, a % b);
			px = x;
			py = y;
			x = py;
			y = px - py * (a / b);
		}
	}

	/**
	 * 龟速乘法实现
	 * 用于防止大数乘法溢出
	 * @param a 第一个乘数
	 * @param b 第二个乘数
	 * @param mod 模数
	 * @return (a * b) % mod
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

	/**
	 * 计算最大公约数
	 * @param a 第一个数
	 * @param b 第二个数
	 * @return a和b的最大公约数
	 */
	public static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	/**
	 * 验证模数是否两两互质
	 * @param n 模数个数
	 * @return 是否两两互质
	 */
	public static boolean areCoprime(int n) {
		for (int i = 1; i <= n; i++) {
			for (int j = i + 1; j <= n; j++) {
				if (gcd(m[i], m[j]) != 1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 验证解是否满足所有同余方程
	 * @param solution 待验证的解
	 * @param n 方程个数
	 * @return 是否满足所有方程
	 */
	public static boolean validateSolution(long solution, int n) {
		for (int i = 1; i <= n; i++) {
			if ((solution - r[i]) % m[i] != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * BigInteger版本的CRT实现（处理更大的数据范围）
	 * @param n 方程组个数
	 * @return 最小正整数解
	 */
	public static BigInteger crtBigInteger(int n) {
		BigInteger M = BigInteger.ONE;
		BigInteger[] bigM = new BigInteger[MAXN];
		BigInteger[] bigR = new BigInteger[MAXN];
		
		for (int i = 1; i <= n; i++) {
			bigM[i] = BigInteger.valueOf(m[i]);
			bigR[i] = BigInteger.valueOf(r[i]);
			M = M.multiply(bigM[i]);
		}
		
		BigInteger ans = BigInteger.ZERO;
		for (int i = 1; i <= n; i++) {
			BigInteger Mi = M.divide(bigM[i]);
			BigInteger gcd = Mi.gcd(bigM[i]);
			if (!gcd.equals(BigInteger.ONE)) {
				// 模数不互质，但题目保证模数互质，这里仅作为健壮性检查
				return BigInteger.valueOf(-1);
			}
			BigInteger invMi = Mi.modInverse(bigM[i]);
			BigInteger ci = Mi.multiply(invMi).multiply(bigR[i]).mod(M);
			ans = ans.add(ci).mod(M);
		}
		
		return ans;
	}

	/**
	 * 测试函数
	 */
	public static void testGuessNumber() {
		// 测试用例1：标准情况
		int n1 = 3;
		long[] r1 = {0, 1, 2, 3}; // 索引从1开始
		long[] m1 = {0, 3, 5, 7};
		System.arraycopy(r1, 0, r, 0, r1.length);
		System.arraycopy(m1, 0, m, 0, m1.length);
		long solution1 = excrt(n1);
		System.out.println("测试用例1 结果: " + solution1 + ", 验证: " + validateSolution(solution1, n1));
		
		// 测试用例2：负数余数
		int n2 = 2;
		long[] r2 = {0, -1, -2};
		long[] m2 = {0, 4, 5};
		System.arraycopy(r2, 0, r, 0, r2.length);
		System.arraycopy(m2, 0, m, 0, m2.length);
		for (int i = 1; i <= n2; i++) {
			r[i] = (r[i] % m[i] + m[i]) % m[i];
		}
		long solution2 = excrt(n2);
		System.out.println("测试用例2 结果: " + solution2 + ", 验证: " + validateSolution(solution2, n2));
		
		// 测试用例3：n=1
		int n3 = 1;
		long[] r3 = {0, 5};
		long[] m3 = {0, 10};
		System.arraycopy(r3, 0, r, 0, r3.length);
		System.arraycopy(m3, 0, m, 0, m3.length);
		long solution3 = excrt(n3);
		System.out.println("测试用例3 结果: " + solution3 + ", 验证: " + validateSolution(solution3, n3));
	}

	public static void main(String[] args) throws IOException {
		// 启用测试函数（可选）
		// testGuessNumber();
		
		// 读取输入
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		
		// 输入校验
		if (n < 1 || n > 10) {
			throw new IllegalArgumentException("n must be between 1 and 10");
		}
		
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			r[i] = (long) in.nval;
		}
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			m[i] = (long) in.nval;
			// 校验模数是否为0
			if (m[i] <= 0) {
				throw new IllegalArgumentException("Modulus must be positive");
			}
		}
		
		// 处理负数余数，确保为非负数
		for (int i = 1; i <= n; i++) {
			r[i] = (r[i] % m[i] + m[i]) % m[i];
		}
		
		// 可以选择使用CRT或EXCRT
		// out.println(crt(n)); // 中国剩余定理解决（模数必须互质）
		long result = excrt(n); // 扩展中国剩余定理解决（更通用）
		
		// 结果处理
		if (result == -1) {
			out.println("No solution");
		} else {
			// 确保结果为最小正整数
			if (result == 0) {
				// 如果结果为0，计算所有模数的乘积作为最小正整数解
				long product = 1;
				for (int i = 1; i <= n; i++) {
					product *= m[i];
				}
				out.println(product);
			} else {
				out.println(result);
			}
		}
		
		// 资源关闭
		out.flush();
		out.close();
		br.close();
	}

}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_GuessNumber {

	public static int MAXN = 11;

	public static long m[] = new long[MAXN];

	public static long r[] = new long[MAXN];

	// 中国剩余定理模版
	public static long crt(int n) {
		long lcm = 1;
		for (int i = 1; i <= n; i++) {
			lcm = lcm * m[i];
		}
		long ai, ci, ans = 0;
		for (int i = 1; i <= n; i++) {
			ai = lcm / m[i];
			exgcd(ai, m[i]);
			ci = multiply(r[i], multiply(ai, x, lcm), lcm);
			ans = (ans + ci) % lcm;
		}
		return ans;
	}

	// 扩展中国剩余定理模版
	public static long excrt(int n) {
		long tail = 0, lcm = 1, tmp, b, c, x0;
		for (int i = 1; i <= n; i++) {
			b = m[i];
			c = ((r[i] - tail) % b + b) % b;
			exgcd(lcm, b);
			if (c % d != 0) {
				return -1;
			}
			x0 = multiply(x, c / d, b / d);
			tmp = lcm * (b / d);
			tail = (tail + multiply(x0, lcm, tmp)) % tmp;
			lcm = tmp;
		}
		return tail;
	}

	// 讲解139 - 扩展欧几里得算法
	public static long d, x, y, px, py;

	public static void exgcd(long a, long b) {
		if (b == 0) {
			d = a;
			x = 1;
			y = 0;
		} else {
			exgcd(b, a % b);
			px = x;
			py = y;
			x = py;
			y = px - py * (a / b);
		}
	}

	// 讲解033 - 位运算实现乘法
	// a*b过程每一步都%mod，这么写是防止溢出，也叫龟速乘
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

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			r[i] = (long) in.nval;
		}
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			m[i] = (long) in.nval;
		}
		// 题目输入的余数可能为负所以转化成非负数
		for (int i = 1; i <= n; i++) {
			r[i] = (r[i] % m[i] + m[i]) % m[i];
		}
		// out.println(crt(n)); // 中国剩余定理解决
		out.println(excrt(n)); // 扩展中国剩余定理解决
		out.flush();
		out.close();
		br.close();
	}

}