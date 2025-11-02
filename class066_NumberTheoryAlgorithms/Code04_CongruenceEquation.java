package class139;

/**
 * 同余方程
 * 
 * 题目描述：
 * 求关于x的同余方程 ax ≡ 1(mod b) 的最小正整数解
 * 题目保证一定有解，也就是a和b互质
 * 
 * 解题思路：
 * 1. 将同余方程转化为不定方程：ax + by = 1
 * 2. 使用扩展欧几里得算法求解不定方程
 * 3. 得到特解后调整为最小正整数解
 * 
 * 算法复杂度：
 * 时间复杂度：O(log(min(a, b)))
 * 空间复杂度：O(log(min(a, b)))
 * 
 * 题目链接：
 * 洛谷 P1082 [NOIP2012 提高组] 同余方程
 * https://www.luogu.com.cn/problem/P1082
 * 
 * 相关题目：
 * 1. 洛谷 P1516 青蛙的约会
 *    链接：https://www.luogu.com.cn/problem/P1516
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 2. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    与本题类似，需要求解同余方程
 * 
 * 3. POJ 2115 C Looooops
 *    链接：http://poj.org/problem?id=2115
 *    本题需要求解模线性方程，可以转化为线性丢番图方程
 * 
 * 4. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程wx + dy = p
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、大数等情况
 * 2. 边界条件：需要考虑a、b的边界值
 * 3. 性能优化：使用扩展欧几里得算法求解
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

public class Code04_CongruenceEquation {

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
	 * 主方法 - 同余方程问题
	 * 
	 * 算法思路：
	 * 1. 读取a和b
	 * 2. 使用扩展欧几里得算法求解ax + by = 1
	 * 3. 调整解为最小正整数解
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		long a = (long) in.nval;
		in.nextToken();
		long b = (long) in.nval;
		exgcd(a, b);
		out.println((x % b + b) % b);
		out.flush();
		out.close();
		br.close();
	}

}
