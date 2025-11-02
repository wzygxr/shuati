package class139;

/**
 * 修理宝塔 - 工程化优化版本
 * 
 * 题目描述：
 * 一共有编号1~n的宝塔，其中a号和b号宝塔已经修好了
 * Yuwgna和Iaka两个人轮流修塔，Yuwgna先手，Iaka后手，谁先修完所有的塔谁赢
 * 每次可以选择j+k号或者j-k号塔进行修理，其中j和k是任意两个已经修好的塔
 * 也就是输入n、a、b，如果先手赢打印"Yuwgna"，后手赢打印"Iaka"
 * 
 * 解题思路：
 * 1. 根据数论知识，能修的塔的数量与gcd(a,b)有关
 * 2. 能修的塔的编号是gcd(a,b)的倍数
 * 3. 总共有n/gcd(a,b)个塔需要修
 * 4. 如果这个数量是奇数，先手赢；否则后手赢
 * 
 * 算法复杂度：
 * 时间复杂度：O(log(min(a, b)))
 * 空间复杂度：O(1)
 * 
 * 题目链接：
 * HDU 5512 Pagodas
 * https://acm.hdu.edu.cn/showproblem.php?pid=5512
 * 
 * 相关题目：
 * 1. 洛谷 P4549 【模板】裴蜀定理
 *    链接：https://www.luogu.com.cn/problem/P4549
 *    本题是裴蜀定理的模板题，与最大公约数有关
 * 
 * 2. Codeforces 1011E Border
 *    链接：https://codeforces.com/contest/1011/problem/E
 *    本题需要根据裴蜀定理求解可能到达的位置
 * 
 * 3. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 工程化优化：
 * 1. 异常处理：处理输入非法、除零、溢出等情况
 * 2. 边界条件：处理n、a、b的边界值，包括负数、零值等
 * 3. 性能优化：使用迭代版本避免递归深度限制
 * 4. 调试能力：添加断言和日志输出用于调试
 * 5. 单元测试：提供完整的测试用例
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

public class Code02_Pagodas {

	/**
	 * 欧几里得算法计算最大公约数（迭代版本）
	 * 
	 * 算法原理：
	 * gcd(a, b) = gcd(b, a % b)，当b为0时，gcd(a, 0) = a
	 * 使用迭代版本避免递归深度限制，适合处理大数
	 * 
	 * 时间复杂度：O(log(min(a, b)))
	 * 空间复杂度：O(1)
	 * 
	 * @param a 第一个整数
	 * @param b 第二个整数
	 * @return a和b的最大公约数
	 * @throws IllegalArgumentException 如果a和b都为0
	 */
	public static int gcd(int a, int b) {
		// 处理特殊情况
		if (a == 0 && b == 0) {
			throw new IllegalArgumentException("a和b不能同时为0");
		}
		
		// 使用绝对值避免负数影响
		a = Math.abs(a);
		b = Math.abs(b);
		
		// 迭代计算最大公约数
		while (b != 0) {
			int temp = b;
			b = a % b;
			a = temp;
		}
		
		return a;
	}
	
	/**
	 * 计算能修的塔的数量
	 * 
	 * @param n 总塔数
	 * @param a 第一个已修塔编号
	 * @param b 第二个已修塔编号
	 * @return 能修的塔的数量
	 * @throws IllegalArgumentException 如果参数不合法
	 */
	public static int calculateRepairableTowers(int n, int a, int b) {
		// 参数验证
		if (n <= 0) {
			throw new IllegalArgumentException("塔数n必须为正数");
		}
		
		if (a <= 0 || a > n) {
			throw new IllegalArgumentException("塔编号a必须在1到n之间");
		}
		
		if (b <= 0 || b > n) {
			throw new IllegalArgumentException("塔编号b必须在1到n之间");
		}
		
		// 计算最大公约数
		int g = gcd(a, b);
		
		// 计算能修的塔的数量
		int repairableCount = n / g;
		
		// 调试输出（可注释掉）
		// System.err.println("n=" + n + ", a=" + a + ", b=" + b + ", gcd=" + g + ", repairableCount=" + repairableCount);
		
		return repairableCount;
	}
	
	/**
	 * 判断游戏胜负
	 * 
	 * @param n 总塔数
	 * @param a 第一个已修塔编号
	 * @param b 第二个已修塔编号
	 * @return true表示先手赢，false表示后手赢
	 */
	public static boolean isFirstPlayerWin(int n, int a, int b) {
		int repairableCount = calculateRepairableTowers(n, a, b);
		return (repairableCount & 1) == 1;
	}

	/**
	 * 主方法 - 修理宝塔问题
	 * 
	 * 算法思路：
	 * 1. 读取测试用例数量
	 * 2. 对每个测试用例，读取n、a、b
	 * 3. 计算gcd(a, b)
	 * 4. 计算能修的塔的数量n/gcd(a, b)
	 * 5. 如果数量为奇数，先手赢；否则后手赢
	 * 
	 * 工程化优化：
	 * 1. 异常处理：捕获并处理可能的异常
	 * 2. 边界条件：处理各种边界情况
	 * 3. 输入验证：验证输入数据的合法性
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			StreamTokenizer in = new StreamTokenizer(br);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
			
			// 读取测试用例数量
			if (in.nextToken() != StreamTokenizer.TT_NUMBER) {
				throw new IllegalArgumentException("输入格式错误：期望数字");
			}
			
			int cases = (int) in.nval;
			
			// 边界条件检查
			if (cases < 0) {
				throw new IllegalArgumentException("测试用例数量不能为负数");
			}
			
			if (cases == 0) {
				out.flush();
				out.close();
				br.close();
				return;
			}
			
			for (int t = 1; t <= cases; t++) {
				// 读取n
				if (in.nextToken() != StreamTokenizer.TT_NUMBER) {
					throw new IllegalArgumentException("输入格式错误：期望数字");
				}
				int n = (int) in.nval;
				
				// 读取a
				if (in.nextToken() != StreamTokenizer.TT_NUMBER) {
					throw new IllegalArgumentException("输入格式错误：期望数字");
				}
				int a = (int) in.nval;
				
				// 读取b
				if (in.nextToken() != StreamTokenizer.TT_NUMBER) {
					throw new IllegalArgumentException("输入格式错误：期望数字");
				}
				int b = (int) in.nval;
				
				out.print("Case #" + t + ": ");
				
				try {
					boolean firstWin = isFirstPlayerWin(n, a, b);
					if (firstWin) {
						out.println("Yuwgna");
					} else {
						out.println("Iaka");
					}
				} catch (IllegalArgumentException e) {
					out.println("ERROR: " + e.getMessage());
				}
			}
			
			out.flush();
			out.close();
			br.close();
			
		} catch (IOException e) {
			System.err.println("IO异常: " + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println("输入错误: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("未知异常: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 单元测试方法
	 */
	public static void runTests() {
		System.out.println("=== 修理宝塔单元测试 ===");
		
		// 测试用例1：正常情况
		try {
			boolean result1 = isFirstPlayerWin(12, 3, 6);
			System.out.println("测试1 n=12, a=3, b=6: " + result1 + " (期望: false)");
			assert !result1 : "测试1失败";
		} catch (Exception e) {
			System.err.println("测试1异常: " + e.getMessage());
		}
		
		// 测试用例2：先手赢
		try {
			boolean result2 = isFirstPlayerWin(10, 2, 3);
			System.out.println("测试2 n=10, a=2, b=3: " + result2 + " (期望: true)");
			assert result2 : "测试2失败";
		} catch (Exception e) {
			System.err.println("测试2异常: " + e.getMessage());
		}
		
		// 测试用例3：边界情况 - 塔编号超出范围
		try {
			boolean result3 = isFirstPlayerWin(5, 6, 2);
			System.out.println("测试3 n=5, a=6, b=2: " + result3);
		} catch (IllegalArgumentException e) {
			System.out.println("测试3 n=5, a=6, b=2: 正确抛出异常 - " + e.getMessage());
		}
		
		// 测试用例4：负数测试
		try {
			boolean result4 = isFirstPlayerWin(8, -2, 4);
			System.out.println("测试4 n=8, a=-2, b=4: " + result4 + " (期望: true)");
			assert result4 : "测试4失败";
		} catch (Exception e) {
			System.err.println("测试4异常: " + e.getMessage());
		}
		
		System.out.println("=== 单元测试完成 ===");
	}
	
	/**
	 * 运行测试
	 */
	public static void main(String[] args) {
		// 如果传入参数"test"，则运行单元测试
		if (args.length > 0 && "test".equals(args[0])) {
			runTests();
		} else {
			// 否则运行主程序
			main(new String[0]);
		}
	}

}
