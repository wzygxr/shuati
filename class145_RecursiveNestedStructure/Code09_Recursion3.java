// HackerRank Day 9: Recursion 3
// 测试链接 : https://www.hackerrank.com/challenges/30-recursion/problem
//
// 题目描述:
// 今天，我们学习一个算法概念叫做递归。查看教程选项卡以了解学习材料和指导视频。
// 编写一个阶乘函数，该函数以正整数n作为参数，并打印n的结果!(n的阶乘)。
// 注意: 如果使用递归方法，必须在__prepend代码模板中声明Main类。
//
// 示例:
// 输入：n = 3
// 输出：6
// 解释: 3! = 3 × 2 × 1 = 6
//
// 输入：n = 5
// 输出：120
// 解释: 5! = 5 × 4 × 3 × 2 × 1 = 120
//
// 解题思路:
// 使用递归实现阶乘函数
// 阶乘的递归定义：
// n! = n × (n-1)! (当n > 1时)
// n! = 1 (当n = 0或n = 1时)
//
// 时间复杂度: O(n)，需要递归调用n次
// 空间复杂度: O(n)，递归调用栈的深度为n

public class Code09_Recursion3 {

	// 计算n的阶乘
	public static int factorial(int n) {
		// 基础情况：0! = 1, 1! = 1
		if (n == 0 || n == 1) {
			return 1;
		}
		// 递归情况：n! = n × (n-1)!
		return n * factorial(n - 1);
	}
	
	// 测试用例
	public static void main(String[] args) {
		// 测试用例1
		int n1 = 3;
		System.out.println("输入: " + n1);
		System.out.println("输出: " + factorial(n1));
		System.out.println("期望: 6\n");
		
		// 测试用例2
		int n2 = 5;
		System.out.println("输入: " + n2);
		System.out.println("输出: " + factorial(n2));
		System.out.println("期望: 120\n");
		
		// 测试用例3
		int n3 = 0;
		System.out.println("输入: " + n3);
		System.out.println("输出: " + factorial(n3));
		System.out.println("期望: 1\n");
	}
}