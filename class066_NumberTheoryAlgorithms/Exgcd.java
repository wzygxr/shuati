package class139;

/**
 * 扩展欧几里得算法示例
 * 
 * 算法介绍：
 * 扩展欧几里得算法是欧几里得算法的扩展，不仅能计算两个数的最大公约数，
 * 还能找到满足贝祖等式 ax + by = gcd(a,b) 的整数解 x 和 y。
 * 
 * 算法原理：
 * 1. 当 b=0 时，gcd(a,b)=a，此时 x=1, y=0
 * 2. 当 b≠0 时，递归计算 gcd(b, a%b) 的解 x1, y1
 * 3. 根据等式推导：x = y1, y = x1 - (a/b) * y1
 * 
 * 算法复杂度：
 * 时间复杂度：O(log(min(a, b)))
 * 空间复杂度：O(log(min(a, b)))
 * 
 * 应用场景：
 * 1. 求解线性同余方程 ax ≡ b (mod m)
 * 2. 求模逆元
 * 3. 求解线性不定方程 ax + by = c
 * 
 * 相关题目：
 * 1. 洛谷 P1082 [NOIP2012 提高组] 同余方程
 *    链接：https://www.luogu.com.cn/problem/P1082
 *    本题需要使用扩展欧几里得算法求模逆元
 * 
 * 2. 洛谷 P1516 青蛙的约会
 *    链接：https://www.luogu.com.cn/problem/P1516
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 3. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    与本题完全相同，是POJ上的经典题目
 * 
 * 4. POJ 2115 C Looooops
 *    链接：http://poj.org/problem?id=2115
 *    本题需要求解模线性方程，可以转化为线性丢番图方程
 * 
 * 5. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程wx + dy = p
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、负数等情况
 * 2. 边界条件：需要考虑a、b为0的特殊情况
 * 3. 性能优化：使用迭代版本避免递归调用栈开销
 * 
 * 调试能力：
 * 1. 添加断言验证中间结果
 * 2. 打印关键变量的实时值
 * 3. 性能退化排查
 */

public class Exgcd {

	// 扩展欧几里得算法所需的全局变量
	// d: 最大公约数
	// x, y: 方程ax + by = gcd(a,b)的解
	// px, py: 临时变量，用于保存递归前的解
	public static long d, x, y, px, py;

	/**
	 * 扩展欧几里得算法
	 * 
	 * 算法原理：
	 * 1. 当b=0时，gcd(a,0)=a，此时x=1, y=0
	 * 2. 当b≠0时，递归计算gcd(b, a%b)的解x1, y1
	 * 3. 根据等式推导：x = y1, y = x1 - (a/b) * y1
	 * 
	 * 时间复杂度：O(log(min(a, b)))
	 * 空间复杂度：O(log(min(a, b)))（递归调用栈）
	 * 
	 * @param a 系数a
	 * @param b 系数b
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
	 * 费马小定理计算逆元
	 * 
	 * 算法原理：
	 * 根据费马小定理，当p为质数时，a^(p-1) ≡ 1 (mod p)
	 * 因此 a^(p-2) ≡ a^(-1) (mod p)
	 * 
	 * 时间复杂度：O(log(p-2))
	 * 空间复杂度：O(1)
	 * 
	 * @param num 原数
	 * @param mod 模数（必须为质数）
	 * @return num在模mod下的逆元
	 */
	public static long fermat(long num, long mod) {
		return power(num, mod - 2, mod);
	}

	/**
	 * 快速幂算法
	 * 
	 * 算法原理：
	 * 通过位运算实现快速幂，每个中间过程都取模，防止溢出
	 * 
	 * 时间复杂度：O(log pow)
	 * 空间复杂度：O(1)
	 * 
	 * @param num 底数
	 * @param pow 指数
	 * @param mod 模数
	 * @return (num^pow) % mod
	 */
	public static long power(long num, long pow, long mod) {
		long ans = 1;
		while (pow > 0) {
			if ((pow & 1) == 1) {
				ans = (ans * num) % mod;
			}
			num = (num * num) % mod;
			pow >>= 1;
		}
		return ans;
	}

	/**
	 * 主方法 - 扩展欧几里得算法示例和测试
	 * 
	 * 算法思路：
	 * 1. 使用扩展欧几里得算法求解220x + 170y = gcd(220, 170)
	 * 2. 测试扩展欧几里得算法求逆元的正确性
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		// 扩展欧几里得算法例子
		int a = 220;
		int b = 170;
		exgcd(a, b);
		System.out.println("gcd(" + a + ", " + b + ")" + " = " + d);
		System.out.println("x = " + x + ", " + " y = " + y);

		// 扩展欧几里得算法可以去求逆元
		System.out.println("求逆元测试开始");
		long mod = 1000000007;
		long test = 10000000;
		for (long num = 1; num <= test; num++) {
			exgcd(num, mod);
			x = (x % mod + mod) % mod;
			if (x != fermat(num, mod)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("求逆元测试结束");
	}

}
