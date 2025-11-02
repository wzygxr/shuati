import java.util.ArrayList;

// LeetCode 224. Basic Calculator (基本计算器)
// 测试链接 : https://leetcode.cn/problems/basic-calculator/
//
// 题目描述:
// 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
// 整数除法仅保留整数部分。
// 你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
// 注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
//
// 示例:
// 输入：s = "1 + 1"
// 输出：2
//
// 输入：s = " 2-1 + 2 "
// 输出：3
//
// 输入：s = "(1+(4+5+2)-3)+(6+8)"
// 输出：23
//
// 解题思路:
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的表达式
// 使用两个ArrayList分别存储数字和操作符
// 只包含加减法和括号，不需要考虑运算符优先级
//
// 时间复杂度: O(n)，其中n是字符串的长度
// 空间复杂度: O(n)，递归调用栈的深度和存储数字操作符的额外空间
public class Code04_BasicCalculator {

	public static int calculate(String str) {
		where = 0;
		return f(str.toCharArray(), 0);
	}

	// 全局变量，记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
	public static int where;

	// s[i....]开始计算，遇到字符串终止 或者 遇到)停止
	// 返回 : 自己负责的这一段，计算的结果
	// 返回之间，更新全局变量where，为了上游函数知道从哪继续！
	public static int f(char[] s, int i) {
		int cur = 0;
		ArrayList<Integer> numbers = new ArrayList<>();
		ArrayList<Character> ops = new ArrayList<>();
		
		while (i < s.length && s[i] != ')') {
			if (s[i] >= '0' && s[i] <= '9') {
				cur = cur * 10 + s[i++] - '0';
			} else if (s[i] == '+' || s[i] == '-') {
				// 遇到了运算符 + -
				numbers.add(cur);
				ops.add(s[i]);
				cur = 0;
				i++;
			} else if (s[i] == '(') {
				// i (.....)
				// 遇到了左括号！
				cur = f(s, i + 1);
				i = where + 1;
			} else if (s[i] == ' ') {
				// 跳过空格
				i++;
			} else {
				i++;
			}
		}
		
		// 处理最后一个数字
		numbers.add(cur);
		where = i;
		return compute(numbers, ops);
	}

	// 计算最终结果，只处理加减法
	public static int compute(ArrayList<Integer> numbers, ArrayList<Character> ops) {
		int n = numbers.size();
		int ans = numbers.get(0);
		for (int i = 1; i < n; i++) {
			ans += ops.get(i - 1) == '+' ? numbers.get(i) : -numbers.get(i);
		}
		return ans;
	}
	
	// 测试用例
	public static void main(String[] args) {
		// 测试用例1
		String s1 = "1 + 1";
		System.out.println("输入: " + s1);
		System.out.println("输出: " + calculate(s1));
		System.out.println("期望: 2\n");
		
		// 测试用例2
		String s2 = " 2-1 + 2 ";
		System.out.println("输入: " + s2);
		System.out.println("输出: " + calculate(s2));
		System.out.println("期望: 3\n");
		
		// 测试用例3
		String s3 = "(1+(4+5+2)-3)+(6+8)";
		System.out.println("输入: " + s3);
		System.out.println("输出: " + calculate(s3));
		System.out.println("期望: 23\n");
	}
}