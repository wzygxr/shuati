package class099;

// 单个除数求逆元
// 对数器验证
// ZOJ 3609 Modular Inverse 题目实现
// 题目链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
// 题目名称: Modular Inverse
// 题目来源: ZOJ (Zhejiang University Online Judge)
// 题目难度: 简单
//
// 题目描述:
// 给定两个整数a和m，求a在模m意义下的乘法逆元x，使得 a*x ≡ 1 (mod m)
// 如果不存在这样的x，输出"Not Exist"
//
// 解题思路:
// 方法1: 扩展欧几里得算法
// 方法2: 费马小定理（当m为质数时）
//
// 时间复杂度分析:
// - 扩展欧几里得算法: O(log(min(a, m)))
// - 费马小定理: O(log m)
//
// 空间复杂度分析:
// - 扩展欧几里得算法: O(log(min(a, m)))（递归栈）
// - 费马小定理: O(1)
//
// 应用场景:
// 1. 密码学中的RSA算法
// 2. 组合数学中的组合数计算
// 3. 数论问题中的模运算优化
public class Code01_InverseSingle {

	public static void main(String[] args) {
		// 1) 必须保证a/b可以整除
		// 2) 必须保证mod是质数
		// 3) 必须保证b和mod的最大公约数为1
		int mod = 41;
		long b = 3671613L;
		long a = 67312L * b;
		System.out.println(compute1(a, b, mod));
		System.out.println(compute2(a, b, mod));
		
		// ZOJ 3609 测试用例
		System.out.println("ZOJ 3609 测试:");
		System.out.println(modInverse(3, 11));  // 应该输出 4
		System.out.println(modInverse(4, 12));  // 应该输出 Not Exist
		System.out.println(modInverse(5, 13));  // 应该输出 8
	}

	/**
	 * 直接计算 a/b mod mod 的结果
	 * 适用于 b 和 mod 不互质的情况
	 * 
	 * @param a 被除数
	 * @param b 除数
	 * @param mod 模数
	 * @return (a/b) mod mod 的结果
	 */
	public static int compute1(long a, long b, int mod) {
		return (int) ((a / b) % mod);
	}

	/**
	 * 使用模逆元计算 a/b mod mod 的结果
	 * 适用于 b 和 mod 互质的情况
	 * 根据模运算性质: (a/b) mod mod = (a mod mod) * (b^(-1) mod mod) mod mod
	 * 
	 * @param a 被除数
	 * @param b 除数
	 * @param mod 模数
	 * @return (a/b) mod mod 的结果
	 */
	public static int compute2(long a, long b, int mod) {
		long inv = power(b, mod - 2, mod);
		return (int) (((a % mod) * inv) % mod);
	}

	/**
	 * 乘法快速幂
	 * 计算b的n次方的结果%mod
	 * 
	 * 算法原理:
	 * 利用二进制表示指数n，将幂运算分解为若干次平方运算
	 * 例如: 3^10 = 3^8 * 3^2
	 * 
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(1)
	 * 
	 * @param b 底数
	 * @param n 指数
	 * @param mod 模数
	 * @return b^n mod mod
	 */
	public static long power(long b, int n, int mod) {
		long ans = 1;
		while (n > 0) {
			if ((n & 1) == 1) {
				ans = (ans * b) % mod;
			}
			b = (b * b) % mod;
			n >>= 1;
		}
		return ans;
	}
	
	/**
	 * 使用扩展欧几里得算法求模逆元
	 * ZOJ 3609 Modular Inverse 解法
	 * 
	 * 算法原理:
	 * 求解方程 ax + my = gcd(a, m)
	 * 当gcd(a, m) = 1时，x就是a的模逆元
	 * 
	 * 时间复杂度: O(log(min(a, m)))
	 * 空间复杂度: O(1)
	 * 
	 * @param a 要求逆元的数
	 * @param m 模数
	 * @return 如果存在逆元，返回最小正整数解；否则返回-1
	 */
	public static long modInverse(long a, long m) {
		long x = 0, y = 0;
		long gcd = extendedGcd(a, m, x, y);
		
		// 如果gcd不为1，则逆元不存在
		if (gcd != 1) {
			return -1;
		}
		
		// 确保结果为正数
		return (x % m + m) % m;
	}
	
	/**
	 * 扩展欧几里得算法
	 * 求解 ax + by = gcd(a, b)
	 * 
	 * 算法原理:
	 * 基于欧几里得算法的递归实现
	 * gcd(a, b) = gcd(b, a % b)
	 * 当b = 0时，gcd(a, b) = a
	 * 
	 * 递推关系:
	 * 如果 gcd(a, b) = ax + by
	 * 那么 gcd(b, a % b) = bx' + (a % b)y'
	 * 其中 a % b = a - (a/b)*b
	 * 所以 gcd(a, b) = bx' + (a - (a/b)*b)y' = ay' + b(x' - (a/b)y')
	 * 因此 x = y', y = x' - (a/b)y'
	 * 
	 * 时间复杂度: O(log(min(a, b)))
	 * 空间复杂度: O(log(min(a, b)))（递归栈）
	 * 
	 * @param a 系数a
	 * @param b 系数b
	 * @param x 用于返回x的解
	 * @param y 用于返回y的解
	 * @return gcd(a, b)
	 */
	public static long extendedGcd(long a, long b, long x, long y) {
		// 基本情况
		if (b == 0) {
			x = 1;
			y = 0;
			return a;
		}
		
		// 递归求解
		long x1 = 0, y1 = 0;
		long gcd = extendedGcd(b, a % b, x1, y1);
		
		// 更新x和y的值
		x = y1;
		y = x1 - (a / b) * y1;
		
		return gcd;
	}
	
	/**
	 * 使用费马小定理求模逆元（当模数为质数时）
	 * 根据费马小定理: a^(p-1) ≡ 1 (mod p)
	 * 所以 a^(-1) ≡ a^(p-2) (mod p)
	 * 
	 * 算法原理:
	 * 当模数p为质数且gcd(a, p) = 1时，可以使用费马小定理快速计算模逆元
	 * 
	 * 时间复杂度: O(log(p))
	 * 空间复杂度: O(1)
	 * 
	 * @param a 要求逆元的数
	 * @param p 质数模数
	 * @return a在模p意义下的逆元
	 */
	public static long modInverseFermat(long a, long p) {
		return power(a, (int)(p - 2), (int)p);
	}

}