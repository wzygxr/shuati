package class140;

// 机器人的移动区域
// 二维网格中只有x和y的值都为整数的坐标，才叫格点
// 某个机器人从格点(0,0)出发，每次机器人都走直线到达(x + dx, y + dy)的格点
// 一共移动n次，每次的(dx, dy)都给定，途中路线不会交叉，输入保证机器人最终回到(0,0)
// 机器人走的路线所围成的区域一定是多边形，输入保证机器人一定沿着逆时针方向行动
// 返回多边形的内部一共几个格点，多边形的边上一共几个格点，多边形的面积
// 3 <= n <= 100
// -100 <= dx、dy <= 100
// 测试链接 : http://poj.org/problem?id=1265
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 机器人移动区域问题（Pick定理应用）
 * 
 * 问题描述：
 * 机器人在二维网格上移动形成一个简单多边形，求多边形内部格点数、边界格点数和面积
 * 
 * 解题思路：
 * 1. 使用鞋带公式计算多边形面积
 * 2. 使用gcd计算每条边上的格点数，累加得到边界格点数
 * 3. 使用Pick定理计算内部格点数：内部格点数 = 面积 - 边界格点数/2 + 1
 * 
 * 数学原理：
 * 1. 鞋带公式（Shoelace formula）：用于计算简单多边形的面积
 *    对于顶点按逆时针顺序排列的多边形，面积 = 1/2 * Σ(xi*y(i+1) - x(i+1)*yi)
 * 2. Pick定理：给定顶点均为整点的简单多边形，面积A和内部格点数目i、边上格点数目b的关系：
 *    A = i + b/2 - 1
 * 3. 线段上的格点数：连接(x1,y1)和(x2,y2)的线段上的格点数为gcd(|x2-x1|, |y2-y1|) + 1
 * 
 * 时间复杂度：O(n*log(max(dx,dy)))，主要消耗在求最大公约数上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. POJ 1265 Area
 *    链接：http://poj.org/problem?id=1265
 *    这是本题的来源，是一道经典题
 * 
 * 2. LightOJ 1077 How Many Points?
 *    链接：https://lightoj.com/problem/how-many-points
 *    本题需要计算两点间线段上的格点数
 * 
 * 3. HDU 5722 Jewelry
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5722
 *    本题涉及格点和几何计算
 * 
 * 4. UVA 10088 - Trees on My Island
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1029
 *    本题同样是Pick定理的应用
 * 
 * 5. Codeforces 514B - Han Solo and Lazer Gun
 *    链接：https://codeforces.com/problemset/problem/514/B
 *    本题需要判断点是否在一条直线上，涉及几何知识
 * 
 * 工程化考虑：
 * 1. 异常处理：需要处理输入非法等情况
 * 2. 边界条件：需要考虑多边形退化的情况
 * 3. 性能优化：对于大数据，要注意算法的时间复杂度
 * 4. 可读性：添加详细注释，变量命名清晰
 * 
 * 算法要点：
 * 1. 鞋带公式是计算多边形面积的有效方法
 * 2. Pick定理建立了格点多边形面积与格点数量之间的关系
 * 3. 最大公约数在计算线段格点数中起到关键作用
 */
public class Code04_Area {

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
			int n = (int) in.nval;
			int edges = 0;
			double area = 0;
			for (int i = 1, x = 0, y = 0, dx, dy; i <= n; i++) {
				in.nextToken();
				dx = (int) in.nval;
				in.nextToken();
				dy = (int) in.nval;
				// 题目3的重要结论
				edges += gcd(Math.abs(dx), Math.abs(dy));
				// 逆时针方向转动的鞋带公式
				area += x * (y + dy) - (x + dx) * y;
				x += dx;
				y += dy;
			}
			// 鞋带公式最后要/2
			area /= 2;
			// pick定理
			// 多边形面积 = 边界上格点数/2 + 内部格点数 - 1
			// 内部格点数 = 多边形面积 - 边界上格点数/2 + 1
			int inners = (int) (area) - edges / 2 + 1;
			out.println("Scenario #" + t + ":");
			out.print(inners + " ");
			out.print(edges + " ");
			out.printf("%.1f", area);
			out.println();
			out.println();
		}
		out.flush();
		out.close();
		br.close();
	}

}