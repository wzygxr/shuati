package class140;

// 格点连线上有几个格点
// 二维网格中只有x和y的值都为整数的坐标，才叫格点
// 给定两个格点，A在(x1, y1)，B在(x2, y2)
// 返回A和B的连线上，包括A和B在内，一共有几个格点
// -10^9 <= x1、y1、x2、y2 <= 10^9
// 测试链接 : https://lightoj.com/problem/how-many-points
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 格点连线上的格点数量问题
 * 
 * 问题描述：
 * 给定两个格点A(x1,y1)和B(x2,y2)，求线段AB上格点的数量（包括端点）
 * 
 * 解题思路：
 * 1. 线段上的格点数量等于dx和dy的最大公约数加1
 * 2. dx = |x2-x1|，dy = |y2-y1|
 * 3. 结果 = gcd(dx, dy) + 1
 * 
 * 数学原理：
 * 1. 如果线段的两个端点都是格点，那么线段上的格点数量可以通过最大公约数计算
 * 2. 这是因为线段可以被分成gcd(|x2-x1|, |y2-y1|)段，每段的长度相同且端点都是格点
 * 3. 加1是因为要包括两个端点
 * 
 * 时间复杂度：O(log(min(dx,dy)))，主要消耗在求最大公约数上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. LightOJ 1077 How Many Points?
 *    链接：https://lightoj.com/problem/how-many-points
 *    这是本题的来源，是一道经典题
 * 
 * 2. POJ 1265 Area
 *    链接：http://poj.org/problem?id=1265
 *    本题需要计算多边形边界上的格点数量，用到了相同的知识点
 * 
 * 3. HDU 5722 Jewelry
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5722
 *    本题涉及格点和几何计算
 * 
 * 4. Codeforces 514B - Han Solo and Lazer Gun
 *    链接：https://codeforces.com/problemset/problem/514/B
 *    本题需要判断点是否在一条直线上，涉及几何知识
 * 
 * 5. AtCoder Beginner Contest 161 - Problem D
 *    链接：https://atcoder.jp/contests/abc161/tasks/abc161_d
 *    本题涉及最大公约数的应用
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法等情况
 * 2. 边界条件：需要考虑dx或dy为0的情况
 * 3. 性能优化：对于大数据，要注意算法的时间复杂度
 * 4. 可读性：添加详细注释，变量命名清晰
 * 
 * 算法要点：
 * 1. 最大公约数的计算是解决此类问题的关键
 * 2. 理解格点连线上的格点数量公式
 * 3. 注意绝对值的处理
 */
public class Code03_HowManyPoints {

	/**
	 * 求最大公约数
	 * 使用欧几里得算法（辗转相除法）
	 * 
	 * 算法原理：
	 * gcd(a,b) = gcd(b, a%b)，当b=0时，gcd(a,b)=a
	 * 
	 * 时间复杂度：O(log(min(a,b)))
	 * 空间复杂度：O(log(min(a,b)))，递归调用栈
	 * 
	 * @param a 第一个数
	 * @param b 第二个数
	 * @return a和b的最大公约数
	 */
	public static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int cases = (int) in.nval;
		for (int t = 1; t <= cases; t++) {
			in.nextToken();
			long x1 = (long) in.nval;
			in.nextToken();
			long y1 = (long) in.nval;
			in.nextToken();
			long x2 = (long) in.nval;
			in.nextToken();
			long y2 = (long) in.nval;
			long ans = gcd(Math.abs(x1 - x2), Math.abs(y1 - y2)) + 1;
			out.println("Case " + t + ": " + ans);
		}
		out.flush();
		out.close();
		br.close();
	}

}