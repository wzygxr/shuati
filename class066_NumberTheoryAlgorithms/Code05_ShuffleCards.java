package class139;

/**
 * 洗牌
 * 
 * 题目描述：
 * 一共有n张牌，n一定是偶数，每张牌的牌面从1到n，洗牌规则如下
 * 比如n = 6，牌面最初排列为1 2 3 4 5 6
 * 先分成左堆1 2 3，右堆4 5 6，然后按照右堆第i张在前，左堆第i张在后的方式依次放置
 * 所以洗一次后，得到 4 1 5 2 6 3
 * 如果再洗一次，得到 2 4 6 1 3 5
 * 如果再洗一次，得到 1 2 3 4 5 6
 * 想知道n张牌洗m次的之后，第l张牌，是什么牌面
 * 
 * 解题思路：
 * 1. 通过数学推导找到洗牌的规律
 * 2. 使用快速幂和扩展欧几里得算法求解
 * 
 * 算法复杂度：
 * 时间复杂度：O(log m)
 * 空间复杂度：O(log m)
 * 
 * 题目链接：
 * 洛谷 P2054 [AHOI2005] 洗牌
 * https://www.luogu.com.cn/problem/P2054
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
 * 3. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、大数等情况
 * 2. 边界条件：需要考虑n、m、l的边界值
 * 3. 性能优化：使用快速幂和扩展欧几里得算法
 * 
 * 调试能力：
 * 1. 添加断言验证中间结果
 * 2. 打印关键变量的实时值
 * 3. 性能退化排查
 * 
 * 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code05_ShuffleCards {

	// 扩展欧几里得算法所需的全局变量
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
	 * 龟速乘法（防止溢出的乘法实现）
	 * 
	 * 算法原理：
	 * 通过位运算实现乘法，每个中间过程都取模，防止溢出
	 * 
	 * 时间复杂度：O(log b)
	 * 空间复杂度：O(1)
	 * 
	 * @param a 乘数a
	 * @param b 乘数b
	 * @param mod 模数
	 * @return (a * b) % mod
	 */
	public static long multiply(long a, long b, long mod) {
		// 既然是在%mod的意义下，那么a和b可以都转化成非负的
		// 本题不转化无所谓，但是其他题目可能需要转化
		// 尤其是b需要转化，否则while循环会跑不完
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
	 * 快速幂算法
	 * 
	 * 算法原理：
	 * 通过位运算实现快速幂，每个中间过程都取模，防止溢出
	 * 
	 * 时间复杂度：O(log b)
	 * 空间复杂度：O(1)
	 * 
	 * @param a 底数
	 * @param b 指数
	 * @param mod 模数
	 * @return (a^b) % mod
	 */
	public static long power(long a, long b, long mod) {
		long ans = 1;
		while (b > 0) {
			if ((b & 1) == 1) {
				ans = multiply(ans, a, mod);
			}
			a = multiply(a, a, mod);
			b >>= 1;
		}
		return ans;
	}

	/**
	 * 计算洗牌后第l张牌的牌面
	 * 
	 * 算法思路：
	 * 1. 根据数学推导，洗m次后第l张牌的牌面为 (2^m * l) % (n+1)
	 * 2. 由于n和m可能很大，需要使用快速幂和模逆元
	 * 
	 * @param n 牌的数量
	 * @param m 洗牌次数
	 * @param l 位置
	 * @return 洗牌后第l张牌的牌面
	 */
	public static long compute(long n, long m, long l) {
		long mod = n + 1;
		exgcd(power(2, m, mod), mod);
		long x0 = (x % mod + mod) % mod;
		return multiply(x0, l, mod);
	}

	/**
	 * 主方法 - 洗牌问题
	 * 
	 * 算法思路：
	 * 1. 读取n、m、l
	 * 2. 调用compute方法计算结果
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		long n = (long) in.nval;
		in.nextToken();
		long m = (long) in.nval;
		in.nextToken();
		long l = (long) in.nval;
		out.println(compute(n, m, l));
		out.flush();
		out.close();
		br.close();
	}

}
