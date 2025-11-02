package class041;

/**
 * 最大公约数(GCD)和最小公倍数(LCM)的计算实现
 * 
 * 本类提供了计算两个数的最大公约数和最小公倍数的方法
 * 使用欧几里得算法(辗转相除法)计算最大公约数
 * 利用数学关系 lcm(a,b) = |a*b| / gcd(a,b) 计算最小公倍数
 * 
 * 相关题目:
 * 1. LeetCode 2427. 公因子的数目
 *    题目链接: https://leetcode.cn/problems/number-of-common-factors/
 *    题目描述: 给你两个正整数 a 和 b，返回 a 和 b 的公因子的数目
 *    
 * 2. LeetCode 1979. 找出数组的最大公约数
 *    题目链接: https://leetcode.cn/problems/find-greatest-common-divisor-of-array/
 *    题目描述: 给你一个整数数组 nums，返回数组中最大数和最小数的最大公约数
 */
public class Code01_GcdAndLcm {

	/**
	 * 使用欧几里得算法(辗转相除法)计算两个数的最大公约数
	 * 
	 * 算法原理:
	 * gcd(a, b) = gcd(b, a % b)
	 * 当b为0时，gcd(a, 0) = a
	 * 
	 * 证明思路:
	 * 假设a % b = r，即需要证明的关系为：gcd(a, b) = gcd(b, r)
	 * 因为a % b = r，所以如下两个等式必然成立
	 * 1) a = b * q + r，q为0、1、2、3....中的一个整数
	 * 2) r = a − b * q，q为0、1、2、3....中的一个整数
	 * 假设u是a和b的公因子，则有: a = s * u, b = t * u
	 * 把a和b带入2)得到，r = s * u - t * u * q = (s - t * q) * u
	 * 这说明 : u如果是a和b的公因子，那么u也是r的因子
	 * 假设v是b和r的公因子，则有: b = x * v, r = y * v
	 * 把b和r带入1)得到，a = x * v * q + y * v = (x * q + y) * v
	 * 这说明 : v如果是b和r的公因子，那么v也是a的公因子
	 * 综上，a和b的每一个公因子 也是 b和r的一个公因子，反之亦然
	 * 所以，a和b的全体公因子集合 = b和r的全体公因子集合
	 * 即gcd(a, b) = gcd(b, r)
	 * 
	 * 时间复杂度: O(log(min(a,b)))
	 * 空间复杂度: O(log(min(a,b))) (递归调用栈)
	 * 
	 * @param a 第一个数
	 * @param b 第二个数
	 * @return a和b的最大公约数
	 */
	public static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	/**
	 * 计算两个数的最小公倍数
	 * 
	 * 算法原理:
	 * 利用数学关系 lcm(a,b) = |a*b| / gcd(a,b)
	 * 为了避免乘法溢出，先除后乘: a / gcd(a,b) * b
	 * 
	 * 时间复杂度: O(log(min(a,b)))
	 * 空间复杂度: O(log(min(a,b)))
	 * 
	 * @param a 第一个数
	 * @param b 第二个数
	 * @return a和b的最小公倍数
	 */
	public static long lcm(long a, long b) {
		return (long) a / gcd(a, b) * b;
	}

}
