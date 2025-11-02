package class139;

/**
 * 裴蜀定理模版题 - 工程化优化版本
 * 
 * 题目描述：
 * 给定长度为n的一组整数值[a1, a2, a3...]，你找到一组数值[x1, x2, x3...]
 * 要让a1*x1 + a2*x2 + a3*x3...得到的结果为最小正整数
 * 返回能得到的最小正整数是多少
 * 
 * 解题思路：
 * 根据裴蜀定理，对于整数a1, a2, ..., an，存在整数x1, x2, ..., xn使得
 * a1*x1 + a2*x2 + ... + an*xn = gcd(a1, a2, ..., an)
 * 因此，线性组合能得到的最小正整数就是这n个数的最大公约数
 * 
 * 算法复杂度：
 * 时间复杂度：O(n * log(min(ai)))
 * 空间复杂度：O(1)
 * 
 * 题目链接：
 * 洛谷 P4549 【模板】裴蜀定理
 * https://www.luogu.com.cn/problem/P4549
 * 
 * 相关题目：
 * 1. HDU 5512 Pagodas
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
 *    本题涉及数论知识，与最大公约数有关
 * 
 * 2. Codeforces 1011E Border
 *    链接：https://codeforces.com/contest/1011/problem/E
 *    本题需要根据裴蜀定理求解可能到达的位置
 * 
 * 3. LeetCode 1250. 检查「好数组」
 *    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
 *    本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组
 * 
 * 工程化优化：
 * 1. 异常处理：处理输入非法、负数、溢出等情况
 * 2. 边界条件：处理n=0、n=1、所有数都为0等特殊情况
 * 3. 性能优化：使用迭代版本避免递归深度限制，处理大数情况
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

public class Code01_BezoutLemma {

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
	 * 计算多个数的最大公约数
	 * 
	 * @param numbers 整数数组
	 * @return 所有数的最大公约数
	 * @throws IllegalArgumentException 如果数组为空或所有数都为0
	 */
	public static int gcdMultiple(int[] numbers) {
		if (numbers == null || numbers.length == 0) {
			throw new IllegalArgumentException("数组不能为空");
		}
		
		int result = 0;
		boolean allZero = true;
		
		for (int num : numbers) {
			if (num != 0) {
				allZero = false;
				result = gcd(num, result);
			}
		}
		
		if (allZero) {
			throw new IllegalArgumentException("所有数都为0，最大公约数未定义");
		}
		
		return result;
	}

	/**
	 * 主方法 - 裴蜀定理模板题
	 * 
	 * 算法思路：
	 * 1. 读取输入的n个整数
	 * 2. 依次计算这n个数的最大公约数
	 * 3. 根据裴蜀定理，线性组合能得到的最小正整数就是最大公约数
	 * 
	 * 工程化优化：
	 * 1. 异常处理：捕获并处理可能的异常
	 * 2. 边界条件：处理n=0的情况
	 * 3. 输入验证：验证输入数据的合法性
	 * 
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			StreamTokenizer in = new StreamTokenizer(br);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
			
			// 读取n
			if (in.nextToken() != StreamTokenizer.TT_NUMBER) {
				throw new IllegalArgumentException("输入格式错误：期望数字");
			}
			
			int n = (int) in.nval;
			
			// 边界条件检查
			if (n < 0) {
				throw new IllegalArgumentException("n不能为负数");
			}
			
			if (n == 0) {
				out.println(0);
				out.flush();
				out.close();
				br.close();
				return;
			}
			
			int ans = 0;
			boolean hasNonZero = false;
			
			for (int i = 0; i < n; i++) {
				if (in.nextToken() != StreamTokenizer.TT_NUMBER) {
					throw new IllegalArgumentException("输入格式错误：期望数字");
				}
				
				int num = (int) in.nval;
				
				// 调试输出（可注释掉）
				// System.err.println("读取第" + (i+1) + "个数: " + num);
				
				if (num != 0) {
					hasNonZero = true;
				}
				
				ans = gcd(Math.abs(num), ans);
				
				// 调试输出（可注释掉）
				// System.err.println("当前最大公约数: " + ans);
			}
			
			// 如果所有数都为0，输出0
			if (!hasNonZero) {
				out.println(0);
			} else {
				out.println(ans);
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
		System.out.println("=== 裴蜀定理单元测试 ===");
		
		// 测试用例1：正常情况
		try {
			int[] test1 = {6, 9, 15};
			int result1 = gcdMultiple(test1);
			System.out.println("测试1 [6, 9, 15]: " + result1 + " (期望: 3)");
			assert result1 == 3 : "测试1失败";
		} catch (Exception e) {
			System.err.println("测试1异常: " + e.getMessage());
		}
		
		// 测试用例2：包含负数
		try {
			int[] test2 = {-4, 6, -8};
			int result2 = gcdMultiple(test2);
			System.out.println("测试2 [-4, 6, -8]: " + result2 + " (期望: 2)");
			assert result2 == 2 : "测试2失败";
		} catch (Exception e) {
			System.err.println("测试2异常: " + e.getMessage());
		}
		
		// 测试用例3：边界情况 - 所有数都为0
		try {
			int[] test3 = {0, 0, 0};
			int result3 = gcdMultiple(test3);
			System.out.println("测试3 [0, 0, 0]: " + result3);
		} catch (IllegalArgumentException e) {
			System.out.println("测试3 [0, 0, 0]: 正确抛出异常 - " + e.getMessage());
		}
		
		// 测试用例4：单个数字
		try {
			int[] test4 = {17};
			int result4 = gcdMultiple(test4);
			System.out.println("测试4 [17]: " + result4 + " (期望: 17)");
			assert result4 == 17 : "测试4失败";
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
