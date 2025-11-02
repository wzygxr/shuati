package class039;

// 含有嵌套的字符串解码
// 测试链接 : https://leetcode.cn/problems/decode-string/
//
// 相关题目:
// 1. LeetCode 726. Number of Atoms (原子的数量)
//    链接: https://leetcode.cn/problems/number-of-atoms/
//    区别: 处理化学式中的原子计数，结构类似但需要统计不同原子的数量
//
// 2. LeetCode 856. Score of Parentheses (括号的分数)
//    链接: https://leetcode.cn/problems/score-of-parentheses/
//    区别: 计算括号的分数，((())())这种结构的计算
//
// 3. LeetCode 385. Mini Parser (迷你语法分析器)
//    链接: https://leetcode.cn/problems/mini-parser/
//    区别: 解析嵌套的整数列表结构
//
// 解题思路:
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的字符串
// 用全局变量where记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
// 数字表示后续字符串的重复次数
//
// 时间复杂度: O(n)，其中n是输出字符串的长度
// 空间复杂度: O(n)，递归调用栈的深度
public class Code02_DecodeString {

	public static String decodeString(String str) {
		where = 0;
		return f(str.toCharArray(), 0);
	}

	// 全局变量，记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
	public static int where;

	// s[i....]开始计算，遇到字符串终止 或者 遇到 ] 停止
	// 返回 : 自己负责的这一段字符串的结果
	// 返回之间，更新全局变量where，为了上游函数知道从哪继续！
	public static String f(char[] s, int i) {
		StringBuilder path = new StringBuilder();
		int cnt = 0;
		while (i < s.length && s[i] != ']') {
			if ((s[i] >= 'a' && s[i] <= 'z') || (s[i] >= 'A' && s[i] <= 'Z')) {
				path.append(s[i++]);
			} else if (s[i] >= '0' && s[i] <= '9') {
				cnt = cnt * 10 + s[i++] - '0';
			} else {
				// 遇到 [ 
				// cnt = 7 * ? 
				path.append(get(cnt, f(s, i + 1)));
				i = where + 1;
				cnt = 0;
			}
		}
		where = i;
		return path.toString();
	}

	// 将字符串重复指定次数
	public static String get(int cnt, String str) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < cnt; i++) {
			builder.append(str);
		}
		return builder.toString();
	}
	
	// 测试用例
	public static void main(String[] args) {
		String s = "3[a2[c]]";
		System.out.println("输入: " + s);
		System.out.println("输出: " + decodeString(s));
		System.out.println("期望: accaccacc");
	}
}