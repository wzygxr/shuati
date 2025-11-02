import java.util.ArrayList;

// 含有嵌套的表达式求值
// 力扣上本题为会员题，所以额外提供了牛客网的测试链接
// 如果在牛客网上提交，请将函数名从calculate改为solve
// 测试链接 : https://leetcode.cn/problems/basic-calculator-iii/
// 测试链接 : https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
//
// 相关题目:
// 1. LeetCode 224. Basic Calculator (基本计算器)
//    链接: https://leetcode.cn/problems/basic-calculator/
//    区别: 只包含加减法和括号
//
// 2. LeetCode 227. Basic Calculator II (基本计算器 II)
//    链接: https://leetcode.cn/problems/basic-calculator-ii/
//    区别: 包含加减乘除，但不包含括号
//
// 3. LeetCode 772. Basic Calculator III (基本计算器 III)
//    链接: https://leetcode.cn/problems/basic-calculator-iii/
//    区别: 包含加减乘除和括号，是这三题中最复杂的
//
// 解题思路:
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的表达式
// 使用两个ArrayList分别存储数字和操作符
// 乘除法优先级高于加减法，需要特殊处理
//
// 时间复杂度: O(n)，其中n是字符串的长度
// 空间复杂度: O(n)，递归调用栈的深度和存储数字操作符的额外空间
public class Code01_BasicCalculatorIII {

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
		String s = "2*(5+5*2)/3+(6/2+8)";
		System.out.println("输入: " + s);
		System.out.println("输出: " + calculate(s));
		System.out.println("期望: 21");
	}
}