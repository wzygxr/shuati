package class041;

import java.math.BigInteger;

/**
 * 同余运算原理实现
 * 
 * 本类演示了加法、减法、乘法的同余原理
 * 不包括除法，因为除法必须求逆元
 * 
 * 同余定理:
 * 如果 a ≡ b (mod m) 且 c ≡ d (mod m)
 * 那么:
 * 1. a + c ≡ b + d (mod m)
 * 2. a - c ≡ b - d (mod m)
 * 3. a * c ≡ b * d (mod m)
 * 
 * 相关题目:
 * 1. LeetCode 1201. 丑数 III
 *    题目链接: https://leetcode.cn/problems/ugly-number-iii/
 *    题目描述: 编写一个程序，找出第n个丑数，丑数是可以被a或b或c整除的正整数。
 *    
 * 2. LeetCode 878. 第N个神奇数字
 *    题目链接: https://leetcode.cn/problems/nth-magical-number/
 *    题目描述: 一个正整数如果能被 a 或 b 整除，那么它是神奇的。给定三个整数 n , a , b ，返回第 n 个神奇的数字。
 *    
 * 3. Codeforces 630B. Moore's Law
 *    题目链接: https://codeforces.com/problemset/problem/630/B
 *    题目描述: 计算在摩尔定律下，计算机性能随时间的变化。
 */
public class Code03_SameMod {

	/**
	 * 生成随机长整型数用于测试
	 * 
	 * @return 随机长整型数
	 */
	public static long random() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	/**
	 * 使用BigInteger计算 ((a + b) * (c - d) + (a * c - b * d)) % mod 的非负结果
	 * 
	 * 算法思路:
	 * 使用BigInteger进行大数运算，避免溢出
	 * 
	 * 时间复杂度: O(1) (BigInteger运算)
	 * 空间复杂度: O(1)
	 * 
	 * @param a 第一个数
	 * @param b 第二个数
	 * @param c 第三个数
	 * @param d 第四个数
	 * @param mod 模数
	 * @return 计算结果的非负值
	 */
	public static int f1(long a, long b, long c, long d, int mod) {
		BigInteger o1 = new BigInteger(String.valueOf(a)); // a
		BigInteger o2 = new BigInteger(String.valueOf(b)); // b
		BigInteger o3 = new BigInteger(String.valueOf(c)); // c
		BigInteger o4 = new BigInteger(String.valueOf(d)); // d
		BigInteger o5 = o1.add(o2); // a + b
		BigInteger o6 = o3.subtract(o4); // c - d
		BigInteger o7 = o1.multiply(o3); // a * c
		BigInteger o8 = o2.multiply(o4); // b * d
		BigInteger o9 = o5.multiply(o6); // (a + b) * (c - d)
		BigInteger o10 = o7.subtract(o8); // (a * c - b * d)
		BigInteger o11 = o9.add(o10); // ((a + b) * (c - d) + (a * c - b * d))
		// ((a + b) * (c - d) + (a * c - b * d)) % mod
		BigInteger o12 = o11.mod(new BigInteger(String.valueOf(mod)));
		if (o12.signum() == -1) {
			// 如果是负数那么+mod返回
			return o12.add(new BigInteger(String.valueOf(mod))).intValue();
		} else {
			// 如果不是负数直接返回
			return o12.intValue();
		}
	}

	/**
	 * 使用同余定理计算 ((a + b) * (c - d) + (a * c - b * d)) % mod 的非负结果
	 * 
	 * 算法思路:
	 * 利用同余定理的性质进行计算:
	 * 1. (a + b) % mod = ((a % mod) + (b % mod)) % mod
	 * 2. (a - b) % mod = ((a % mod) - (b % mod) + mod) % mod (保证结果非负)
	 * 3. (a * b) % mod = ((a % mod) * (b % mod)) % mod
	 * 
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * 
	 * @param a 第一个数
	 * @param b 第二个数
	 * @param c 第三个数
	 * @param d 第四个数
	 * @param mod 模数
	 * @return 计算结果的非负值
	 */
	public static int f2(long a, long b, long c, long d, int mod) {
		int o1 = (int) (a % mod); // a
		int o2 = (int) (b % mod); // b
		int o3 = (int) (c % mod); // c
		int o4 = (int) (d % mod); // d
		int o5 = (o1 + o2) % mod; // a + b
		int o6 = (o3 - o4 + mod) % mod; // c - d
		int o7 = (int) (((long) o1 * o3) % mod); // a * c
		int o8 = (int) (((long) o2 * o4) % mod); // b * d
		int o9 = (int) (((long) o5 * o6) % mod); // (a + b) * (c - d)
		int o10 = (o7 - o8 + mod) % mod; // (a * c - b * d)
		int ans = (o9 + o10) % mod; // ((a + b) * (c - d) + (a * c - b * d)) % mod
		return ans;
	}

	/**
	 * 主函数，用于测试f1和f2方法的正确性
	 * 
	 * 测试思路:
	 * 1. 生成大量随机测试数据
	 * 2. 分别使用f1和f2计算结果
	 * 3. 比较两种方法的结果是否一致
	 */
	public static void main(String[] args) {
		System.out.println("测试开始");
		int testTime = 100000;
		int mod = 1000000007;
		for (int i = 0; i < testTime; i++) {
			long a = random();
			long b = random();
			long c = random();
			long d = random();
			if (f1(a, b, c, d, mod) != f2(a, b, c, d, mod)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");

		System.out.println("===");
		long a = random();
		long b = random();
		long c = random();
		long d = random();
		System.out.println("a : " + a);
		System.out.println("b : " + b);
		System.out.println("c : " + c);
		System.out.println("d : " + d);
		System.out.println("===");
		System.out.println("f1 : " + f1(a, b, c, d, mod));
		System.out.println("f2 : " + f2(a, b, c, d, mod));
	}

}