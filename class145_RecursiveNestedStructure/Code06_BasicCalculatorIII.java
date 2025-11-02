import java.util.ArrayList;

// LeetCode 772. Basic Calculator III (基本计算器 III)
// 测试链接 : https://leetcode.cn/problems/basic-calculator-iii/
//
// 题目描述:
// 实现一个基本计算器来计算并返回给定字符串表达式的值。
// 整数除法仅保留整数部分。
// 你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
// 注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
// 表达式中的所有整数都是非负整数，且在范围 [0, 2^31 - 1] 内
// 答案保证是32位整数
//
// 示例:
// 输入：s = "1+1"
// 输出：2
//
// 输入：s = "6-4/2"
// 输出：4
//
// 输入：s = "2*(5+5*2)/3+(6/2+8)"
// 输出：21
//
// 解题思路:
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的表达式
// 使用两个ArrayList分别存储数字和操作符
// 乘除法优先级高于加减法，需要特殊处理
//
// 时间复杂度: O(n)，其中n是字符串的长度
// 空间复杂度: O(n)，递归调用栈的深度和存储数字操作符的额外空间
public class Code06_BasicCalculatorIII {

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
			} else if (s[i] != '(') {
				// 遇到了运算符 + - * /
				push(numbers, ops, cur, s[i++]);
				cur = 0;
			} else {
				// i (.....)
				// 遇到了左括号！
				cur = f(s, i + 1);
				i = where + 1;
			}
		}
		push(numbers, ops, cur, '+');
		where = i;
		return compute(numbers, ops);
	}

	// 根据操作符处理数字，乘除法优先级高需要特殊处理
	public static void push(ArrayList<Integer> numbers, ArrayList<Character> ops, int cur, char op) {
		int n = numbers.size();
		if (n == 0 || ops.get(n - 1) == '+' || ops.get(n - 1) == '-') {
			numbers.add(cur);
			ops.add(op);
		} else {
			int topNumber = numbers.get(n - 1);
			char topOp = ops.get(n - 1);
			if (topOp == '*') {
				numbers.set(n - 1, topNumber * cur);
			} else {
				numbers.set(n - 1, topNumber / cur);
			}
			ops.set(n - 1, op);
		}
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
		String s1 = "1+1";
		System.out.println("输入: " + s1);
		System.out.println("输出: " + calculate(s1));
		System.out.println("期望: 2\n");
		
		// 测试用例2
		String s2 = "6-4/2";
		System.out.println("输入: " + s2);
		System.out.println("输出: " + calculate(s2));
		System.out.println("期望: 4\n");
		
		// 测试用例3
		String s3 = "2*(5+5*2)/3+(6/2+8)";
		System.out.println("输入: " + s3);
		System.out.println("输出: " + calculate(s3));
		System.out.println("期望: 21\n");
	}
}