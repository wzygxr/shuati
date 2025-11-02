package class140;

// 二元一次不定方程模版
// 给定a、b、c，求解方程ax + by = c
// 如果方程无解打印-1
// 如果方程无正整数解，但是有整数解
// 打印这些整数解中，x的最小正数值，y的最小正数值
// 如果方程有正整数解，打印正整数解的数量，同时打印所有正整数解中，
// x的最小正数值，y的最小正数值，x的最大正数值，y的最大正数值
// 1 <= a、b、c <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P5656
// 如下实现是正确的，但是洛谷平台对空间卡的很严，只有使用C++能全部通过
// java的版本就是无法完全通过的，空间会超过限制，主要是IO空间占用大
// 这是洛谷平台没有照顾各种语言的实现所导致的
// 在真正笔试、比赛时，一定是兼顾各种语言的，该实现是一定正确的
// C++版本就是Code01_DiophantineEquation2文件
// C++版本和java版本逻辑完全一样，但只有C++版本可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 二元一次不定方程求解器
 * 使用扩展欧几里得算法求解线性丢番图方程
 * 
 * 核心算法：扩展欧几里得算法
 * 相关数学定理：裴蜀定理
 * 
 * 时间复杂度：O(log(min(a,b)))
 * 空间复杂度：O(log(min(a,b)))，递归调用栈深度
 * 
 * 问题描述：
 * 给定a、b、c，求解方程ax + by = c
 * 
 * 解题思路：
 * 1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
 * 2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
 * 3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
 * 4. 根据通解公式求出满足条件的解
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
 * 2. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
 * 3. 通解公式：如果(x0,y0)是ax + by = c的一组特解，那么通解为：
 *    x = x0 + (b/gcd(a,b)) * t
 *    y = y0 - (a/gcd(a,b)) * t
 *    其中t为任意整数
 * 
 * 相关题目：
 * 1. 洛谷 P5656 【模板】二元一次不定方程 (exgcd)
 *    链接：https://www.luogu.com.cn/problem/P5656
 *    这是本题的来源，是一道模板题
 * 
 * 2. LeetCode 1250. 检查「好数组」
 *    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
 *    本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组
 * 
 * 3. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量
 * 
 * 4. HDU 5512 Pagodas
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
 *    本题涉及数论知识，与最大公约数有关
 * 
 * 5. POJ 2115 C Looooops
 *    链接：http://poj.org/problem?id=2115
 *    本题需要求解模线性方程，可以转化为线性丢番图方程
 * 
 * 6. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 7. LightOJ 1077 How Many Points?
 *    链接：https://lightoj.com/problem/how-many-points
 *    本题涉及最大公约数的应用，计算线段上的格点数量
 * 
 * 8. HDU 1792 A New Change Problem
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=1792
 *    本题是硬币问题的变形，求无法表示的最大数和无法表示的数的个数
 * 
 * 9. UVA 10088 - Trees on My Island
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1029
 *    本题需要使用Pick定理计算多边形内部的格点数量
 * 
 * 10. Codeforces 514B Han Solo and Lazer Gun
 *     链接：https://codeforces.com/problemset/problem/514B
 *     本题涉及最大公约数的应用，计算点在同一直线上的数量
 * 
 * 11. AtCoder ABC161 D Lunlun Number
 *     链接：https://atcoder.jp/contests/abc161/tasks/abc161_d
 *     本题使用BFS和数学方法，与数论相关
 * 
 * 12. AtCoder ARC084 B Small Multiple
 *     链接：https://atcoder.jp/contests/abc077/tasks/arc084_b
 *     本题使用01-BFS，与数论相关
 * 
 * 13. HDU 5722 Jewelry
 *     链接：https://acm.hdu.edu.cn/showproblem.php?pid=5722
 *     本题涉及最大公约数的应用
 * 
 * 14. LeetCode 365. 水壶问题
 *     链接：https://leetcode.cn/problems/water-and-jug-problem/
 *     本题可以用裴蜀定理解决，判断是否存在非负整数x,y使得ax + by = z
 * 
 * 15. 剑指Offer 44. 数字序列中某一位的数字
 *     链接：https://leetcode.cn/problems/shu-zi-xu-lie-zhong-mou-yi-wei-de-shu-zi-lcof/
 *     本题涉及数学规律的应用
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法、方程无解等情况
 * 2. 边界条件：需要考虑a、b、c为边界值的情况
 * 3. 性能优化：对于大数据，使用StreamTokenizer提高输入效率
 * 4. 可读性：添加详细注释，变量命名清晰
 * 5. 模块化：将复杂逻辑拆分为独立方法
 * 6. 可测试性：添加单元测试用例
 * 7. 防止溢出：使用long类型处理大数运算
 * 8. 输入输出优化：使用BufferedReader和PrintWriter提高IO效率
 * 9. 线程安全：考虑多线程环境下的安全性
 * 10. 错误信息：提供清晰的错误提示信息
 * 
 * 算法要点：
 * 1. 扩展欧几里得算法是解决此类问题的核心
 * 2. 裴蜀定理是判断方程是否有解的依据
 * 3. 通解公式是找出所有解的关键
 * 4. 对于正整数解，需要通过调整特解来找到满足条件的解
 * 
 * 调试技巧：
 * 1. 打印中间过程：在关键步骤添加打印语句，观察变量的变化过程
 * 2. 使用断言：验证关键中间结果的正确性
 * 3. 小例子测试：使用简单的测试用例验证算法的正确性
 * 
 * 跨语言差异：
 * 1. Java的递归深度限制为1000，可以通过调整JVM参数或迭代实现解决
 * 2. Java的long类型范围为-9,223,372,036,854,775,808到9,223,372,036,854,775,807，
 *    对于超过这个范围的整数需要使用BigInteger
 * 3. 输入输出效率：在Java中，BufferedReader和StreamTokenizer比Scanner更高效
 */
public class Code01_DiophantineEquation1 {

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
	 * 
	 * 异常情况：
	 * - 当a和b都为0时，gcd无定义，函数行为未定义
	 * 
	 * 工程化考虑：
	 * - 递归实现简洁但可能在极端情况下导致栈溢出
	 * - 可以优化为迭代版本以提高效率
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

	public static long a, b, c, xd, yd, times;

	/**
	 * 主函数
	 * 
	 * 问题描述：
	 * 给定a、b、c，求解方程ax + by = c
	 * 
	 * 解题思路：
	 * 1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
	 * 2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
	 * 3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
	 * 4. 根据通解公式求出满足条件的解
	 * 
	 * 数学原理：
	 * 1. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
	 * 2. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
	 * 3. 通解公式：如果(x0,y0)是ax + by = c的一组特解，那么通解为：
	 *    x = x0 + (b/gcd(a,b)) * t
	 *    y = y0 - (a/gcd(a,b)) * t
	 *    其中t为任意整数
	 * 
	 * 时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
	 * 空间复杂度：O(log(min(a,b)))，递归调用栈的深度
	 * 
	 * 相关题目：
	 * 1. 洛谷 P5656 【模板】二元一次不定方程 (exgcd)
	 *    链接：https://www.luogu.com.cn/problem/P5656
	 *    这是本题的来源，是一道模板题
	 * 
	 * 2. LeetCode 1250. 检查「好数组」
	 *    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
	 *    本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组
	 * 
	 * 3. Codeforces 1244C. The Football Stage
	 *    链接：https://codeforces.com/problemset/problem/1244/C
	 *    本题需要求解线性丢番图方程wx + dy = p，其中w和d是给定的，p是变量
	 * 
	 * 4. HDU 5512 Pagodas
	 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
	 *    本题涉及数论知识，与最大公约数有关
	 * 
	 * 5. POJ 2115 C Looooops
	 *    链接：http://poj.org/problem?id=2115
	 *    本题需要求解模线性方程，可以转化为线性丢番图方程
	 * 
	 * 6. POJ 1061 青蛙的约会
	 *    链接：http://poj.org/problem?id=1061
	 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
	 * 
	 * 7. LightOJ 1077 How Many Points?
	 *    链接：https://lightoj.com/problem/how-many-points
	 *    本题涉及最大公约数的应用，计算线段上的格点数量
	 * 
	 * 8. HDU 1792 A New Change Problem
	 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=1792
	 *    本题是硬币问题的变形，求无法表示的最大数和无法表示的数的个数
	 * 
	 * 9. UVA 10088 - Trees on My Island
	 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1029
	 *    本题需要使用Pick定理计算多边形内部的格点数量
	 * 
	 * 10. Codeforces 514B Han Solo and Lazer Gun
	 *     链接：https://codeforces.com/problemset/problem/514B
	 *     本题涉及最大公约数的应用，计算点在同一直线上的数量
	 * 
	 * 11. AtCoder ABC161 D Lunlun Number
	 *     链接：https://atcoder.jp/contests/abc161/tasks/abc161_d
	 *     本题使用BFS和数学方法，与数论相关
	 * 
	 * 12. AtCoder ARC084 B Small Multiple
	 *     链接：https://atcoder.jp/contests/abc077/tasks/arc084_b
	 *     本题使用01-BFS，与数论相关
	 * 
	 * 13. HDU 5722 Jewelry
	 *     链接：https://acm.hdu.edu.cn/showproblem.php?pid=5722
	 *     本题涉及最大公约数的应用
	 * 
	 * 14. LeetCode 365. 水壶问题
	 *     链接：https://leetcode.cn/problems/water-and-jug-problem/
	 *     本题可以用裴蜀定理解决，判断是否存在非负整数x,y使得ax + by = z
	 * 
	 * 15. 剑指Offer 44. 数字序列中某一位的数字
	 *     链接：https://leetcode.cn/problems/shu-zi-xu-lie-zhong-mou-yi-wei-de-shu-zi-lcof/
	 *     本题涉及数学规律的应用
	 * 
	 * 工程化考虑：
	 * 1. 异常处理：需要处理输入非法、方程无解等情况
	 * 2. 边界条件：需要考虑a、b、c为边界值的情况
	 * 3. 性能优化：对于大数据，使用StreamTokenizer提高输入效率
	 * 4. 可读性：添加详细注释，变量命名清晰
	 * 5. 模块化：将复杂逻辑拆分为独立方法
	 * 6. 可测试性：添加单元测试用例
	 * 7. 防止溢出：使用long类型处理大数运算
	 * 8. 输入输出优化：使用BufferedReader和PrintWriter提高IO效率
	 * 9. 线程安全：考虑多线程环境下的安全性
	 * 10. 错误信息：提供清晰的错误提示信息
	 * 
	 * 算法要点：
	 * 1. 扩展欧几里得算法是解决此类问题的核心
	 * 2. 裴蜀定理是判断方程是否有解的依据
	 * 3. 通解公式是找出所有解的关键
	 * 4. 对于正整数解，需要通过调整特解来找到满足条件的解
	 * 
	 * 调试技巧：
	 * 1. 打印中间过程：在关键步骤添加打印语句，观察变量的变化过程
	 * 2. 使用断言：验证关键中间结果的正确性
	 * 3. 小例子测试：使用简单的测试用例验证算法的正确性
	 * 
	 * 跨语言差异：
	 * 1. Java的递归深度限制为1000，可以通过调整JVM参数或迭代实现解决
	 * 2. Java的long类型范围为-9,223,372,036,854,775,808到9,223,372,036,854,775,807，
	 *    对于超过这个范围的整数需要使用BigInteger
	 * 3. 输入输出效率：在Java中，BufferedReader和StreamTokenizer比Scanner更高效
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 使用BufferedReader和StreamTokenizer提高输入效率
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取测试用例数量
		in.nextToken();
		int cases = (int) in.nval;
		
		for (int t = 1; t <= cases; t++) {
			// 读取a, b, c
			in.nextToken();
			a = (long) in.nval;
			in.nextToken();
			b = (long) in.nval;
			in.nextToken();
			c = (long) in.nval;
			
			// 异常输入检查
			if (a <= 0 || b <= 0 || c <= 0) {
				out.println(-1);
				continue;
			}
			
			// 使用扩展欧几里得算法求解
			exgcd(a, b);
			
			// 判断方程是否有解：当c能被gcd(a,b)整除时有解
			if (c % d != 0) { // 无整数解
				out.println(-1);
			} else { // 有整数解
				// 将特解乘以c/gcd(a,b)得到原方程的一组特解
				x *= c / d;
				y *= c / d;
				
				// 计算通解中的系数
				xd = b / d; // x方向的周期
				yd = a / d; // y方向的周期
				
				// 调整x为最小正整数
				if (x < 0) {
					// x要想增长到>=1且最小的值，差几个xd，算出来就是k的值
					// 那应该是(1-x)/xd，结果向上取整
					times = (1 - x + xd - 1) / xd;
					x += xd * times;
					y -= yd * times;
				} else {
					// x要想减少到>=1且最小的值，差几个xd，算出来就是k的值，向下取整
					times = (x - 1) / xd;
					x -= xd * times;
					y += yd * times;
				}
				
				// 此时得到的(x, y)，是x为最小正整数时的一组解
				// 然后继续讨论
				if (y <= 0) { // 无正整数解
					// x能取得的最小正数
					out.print(x + " ");
					// y能取得的最小正数
					long yMinPositive = y + yd * ((1 - y + yd - 1) / yd);
					out.println(yMinPositive);
				} else { // 有正整数解
					// y减少到1以下，能减几次，就是正整数解的个数
					long count = (y - 1) / yd + 1;
					out.print(count + " ");
					// x能取得的最小正数
					out.print(x + " ");
					// y能取得的最小正数
					long minY = y - (y - 1) / yd * yd;
					out.print(minY + " ");
					// x能取得的最大正数
					long maxX = x + (y - 1) / yd * xd;
					out.print(maxX + " ");
					// y能取得的最大正数
					out.println(y);
				}
			}
		}
		
		// 确保资源释放和输出刷新
		out.flush();
		out.close();
		br.close();
	}

}