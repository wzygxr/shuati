import java.util.Stack;

// UVA 551 Nesting a Bunch of Brackets
// 题目链接 : https://onlinejudge.org/external/5/551.pdf
//
// 题目描述:
// 在这个问题中，我们考虑包含括号的表达式，这些括号是正确嵌套的。
// 这些表达式是通过并置获得的，即通过将表达式的有限序列一个接一个地写下来。
// 每个表达式可以是单个字符，也可以是用一对匹配的括号括起来的表达式序列。
// 有几种括号对：()、[]、{}、<>。
// 在这个表达式中，除了括号之外，还有几种括号对，所以我们要对表达式施加第二个条件：
// 匹配的括号应该是同一种类的。
//
// 示例:
// 输入：([)]
// 输出：No
// 解释: 括号没有正确匹配
//
// 输入：([])
// 输出：Yes
// 解释: 括号正确匹配
//
// 解题思路:
// 使用栈来验证括号是否正确匹配
// 遍历字符串中的每个字符：
// 1. 如果是左括号，入栈
// 2. 如果是右括号，检查栈顶是否为对应的左括号
// 3. 如果匹配，弹出栈顶元素；如果不匹配，返回错误
// 4. 遍历结束后，如果栈为空，则括号正确匹配
//
// 时间复杂度: O(n)，其中n是字符串的长度，需要遍历字符串一次
// 空间复杂度: O(n)，栈的空间复杂度最坏情况下为O(n)

public class Code10_NestingBrackets {

	public static String checkBrackets(String s) {
		// 使用栈存储左括号
		Stack<Character> stack = new Stack<>();
		
		// 遍历字符串中的每个字符
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			// 如果是左括号，入栈
			if (c == '(' || c == '[' || c == '{' || c == '<') {
				stack.push(c);
			}
			// 如果是右括号，检查匹配
			else if (c == ')' || c == ']' || c == '}' || c == '>') {
				// 如果栈为空，说明没有对应的左括号
				if (stack.isEmpty()) {
					return "No";
				}
				
				// 弹出栈顶元素
				char top = stack.pop();
				
				// 检查是否匹配
				if ((c == ')' && top != '(') ||
					(c == ']' && top != '[') ||
					(c == '}' && top != '{') ||
					(c == '>' && top != '<')) {
					return "No";
				}
			}
		}
		
		// 如果栈为空，说明所有括号都正确匹配
		return stack.isEmpty() ? "Yes" : "No";
	}
	
	// 测试用例
	public static void main(String[] args) {
		// 测试用例1
		String s1 = "([])";
		System.out.println("输入: " + s1);
		System.out.println("输出: " + checkBrackets(s1));
		System.out.println("期望: Yes\n");
		
		// 测试用例2
		String s2 = "([)]";
		System.out.println("输入: " + s2);
		System.out.println("输出: " + checkBrackets(s2));
		System.out.println("期望: No\n");
		
		// 测试用例3
		String s3 = "([]{})";
		System.out.println("输入: " + s3);
		System.out.println("输出: " + checkBrackets(s3));
		System.out.println("期望: Yes\n");
		
		// 测试用例4
		String s4 = "([{}])";
		System.out.println("输入: " + s4);
		System.out.println("输出: " + checkBrackets(s4));
		System.out.println("期望: Yes\n");
	}
}