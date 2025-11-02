package class139;

/**
 * 均匀生成器
 * 
 * 题目描述：
 * 如果有两个数字step和mod，那么可以由以下方式生成很多数字
 * seed(1) = 0，seed(i+1) = (seed(i) + step) % mod
 * 比如，step = 3、mod = 5
 * seed(1) = 0，seed(2) = 3，seed(3) = 1，seed(4) = 4，seed(5) = 2
 * 如果能产生0 ~ mod-1所有数字，step和mod的组合叫  "Good Choice"
 * 如果无法产生0 ~ mod-1所有数字，step和mod的组合叫 "Bad Choice"
 * 根据step和mod，打印结果
 * 
 * 解题思路：
 * 1. 根据数论知识，当gcd(step, mod) = 1时，能产生0 ~ mod-1所有数字
 * 2. 否则无法产生所有数字
 * 
 * 算法复杂度：
 * 时间复杂度：O(log(min(step, mod)))
 * 空间复杂度：O(log(min(step, mod)))
 * 
 * 题目链接：
 * POJ 1597 Uniform Generator
 * http://poj.org/problem?id=1597
 * 
 * 相关题目：
 * 1. 洛谷 P1516 青蛙的约会
 *    链接：https://www.luogu.com.cn/problem/P1516
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 2. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    与本题完全相同，是POJ上的经典题目
 * 
 * 3. POJ 2115 C Looooops
 *    链接：http://poj.org/problem?id=2115
 *    本题需要求解模线性方程，可以转化为线性丢番图方程
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法等情况
 * 2. 边界条件：需要考虑step、mod的边界值
 * 3. 性能优化：使用欧几里得算法计算最大公约数
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

public class Code03_UniformGenerator {

	/**
	 * 欧几里得算法计算最大公约数
	 * 
	 * 算法原理：
	 * gcd(a, b) = gcd(b, a % b)，当b为0时，gcd(a, 0) = a
	 * 
	 * 时间复杂度：O(log(min(a, b)))
	 * 空间复杂度：O(log(min(a, b)))（递归调用栈）
	 * 
	 * @param a 第一个整数
	 * @param b 第二个整数
	 * @return a和b的最大公约数
	 */
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	/**
	 * 主方法 - 均匀生成器问题
	 * 
	 * 算法思路：
	 * 1. 读取step和mod
	 * 2. 计算gcd(step, mod)
	 * 3. 如果gcd(step, mod) = 1，则为"Good Choice"，否则为"Bad Choice"
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			int step = (int) in.nval;
			in.nextToken();
			int mod = (int) in.nval;
			out.print(String.format("%10d", step) + String.format("%10d", mod) + "    ");
			out.println(gcd(step, mod) == 1 ? "Good Choice" : "Bad Choice");
			out.println(" ");
		}
		out.flush();
		out.close();
		br.close();
	}

}
