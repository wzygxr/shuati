package class140;

// POJ 2115 C Looooops
// A Compiler Mystery: We are given a C-language style for loop that tries to find the loop variable's final value integer k after n iterations.
// The for loop starts by setting variable to value A. Variable will never go beyond value B. Statement d is to be executed for each loop.
// The for loop looks like: for (variable = A; variable != B; variable += C) statement;
// You are to input the value of A, B, and C, and n is the number of iterations.
// Output the value of k if the loop terminates, otherwise print "FOREVER".
// 测试链接 : http://poj.org/problem?id=2115

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * POJ 2115 C Looooops问题
 * 
 * 问题描述：
 * 给定一个C语言风格的for循环，求循环变量的最终值。循环形式为：
 * for (variable = A; variable != B; variable += C)
 * 其中变量是k位无符号整数，范围是0到2^k-1。
 * 
 * 解题思路：
 * 1. 建立方程：设循环执行了t次，则有 (A + C*t) ≡ B (mod 2^k)
 * 2. 化简方程：C*t ≡ (B-A) (mod 2^k)
 * 3. 转换为线性丢番图方程：C*t + 2^k*y = (B-A)
 * 4. 使用扩展欧几里得算法求解
 * 
 * 数学原理：
 * 1. 模线性方程：ax ≡ b (mod m) 等价于 ax + my = b
 * 2. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
 * 3. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
 * 
 * 时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. POJ 2115 C Looooops
 *    链接：http://poj.org/problem?id=2115
 *    本题需要求解模线性方程，可以转化为线性丢番图方程
 * 
 * 2. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 3. 洛谷 P1516 青蛙的约会
 *    链接：https://www.luogu.com.cn/problem/P1516
 *    这是本题的来源，是一道经典题
 * 
 * 4. HDU 5512 Pagodas
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
 *    本题涉及数论知识，与最大公约数有关
 * 
 * 5. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、方程无解等情况
 * 2. 边界条件：需要考虑各参数为边界值的情况
 * 3. 性能优化：对于大数据，要注意算法的时间复杂度
 * 4. 可读性：添加详细注释，变量命名清晰
 * 
 * 算法要点：
 * 1. 模线性方程的转化是解决此类问题的关键
 * 2. 扩展欧几里得算法是解决线性丢番图方程的核心
 * 3. 裴蜀定理是判断方程是否有解的依据
 * 4. 对于最小正整数解，需要通过调整特解来找到满足条件的解
 */
public class Code07_Poj2115_Loops {

	// 扩展欧几里得算法相关变量
	public static long d, x, y, px, py;

	/**
	 * 扩展欧几里得算法
	 * 求解方程ax + by = gcd(a,b)的一组特解
	 * 
	 * 算法原理：
	 * 当b=0时，gcd(a,b)=a，此时x=1,y=0
	 * 当b≠0时，递归计算gcd(b,a%b)的解，然后根据推导公式得到原方程的解
	 * 
	 * 时间复杂度：O(log(min(a,b)))
	 * 空间复杂度：O(log(min(a,b)))，递归调用栈
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

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		while (true) {
			// 读取输入
			in.nextToken();
			long A = (long) in.nval;
			in.nextToken();
			long B = (long) in.nval;
			in.nextToken();
			long C = (long) in.nval;
			in.nextToken();
			long k = (long) in.nval;
			
			// 判断是否结束
			if (A == 0 && B == 0 && C == 0 && k == 0) {
				break;
			}
			
			// 计算2^k
			long mod = 1L << k;
			
			// 计算参数
			long a = C;
			long c = (B - A) % mod;
			if (c < 0) {
				c += mod;
			}
			
			// 使用扩展欧几里得算法求解
			exgcd(a, mod);
			
			// 判断方程是否有解
			if (c % d != 0) {
				out.println("FOREVER");
			} else {
				// 解出的特解
				long x0 = x * c / d;
				// 单次幅度
				long xd = mod / d;
				// x0调整成>=0的最小非负整数
				if (x0 < 0) {
					x0 += ((0 - x0 + xd - 1) / xd) * xd;
				} else {
					x0 -= (x0 / xd) * xd;
				}
				out.println(x0);
			}
		}
		
		out.flush();
		out.close();
		br.close();
	}

}