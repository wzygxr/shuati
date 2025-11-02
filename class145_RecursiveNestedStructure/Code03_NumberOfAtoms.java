package class039;

import java.util.TreeMap;

// 含有嵌套的分子式求原子数量
// 测试链接 : https://leetcode.cn/problems/number-of-atoms/
//
// 相关题目:
// 1. LeetCode 394. Decode String (字符串解码)
//    链接: https://leetcode.cn/problems/decode-string/
//    区别: 解码字符串而不是统计原子数量
//
// 2. LeetCode 772. Basic Calculator III (基本计算器 III)
//    链接: https://leetcode.cn/problems/basic-calculator-iii/
//    区别: 计算表达式而不是统计原子数量
//
// 3. LeetCode 856. Score of Parentheses (括号的分数)
//    链接: https://leetcode.cn/problems/score-of-parentheses/
//    区别: 计算括号的分数而不是统计原子数量
//
// 解题思路:
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的化学式
// 用全局变量where记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
// 用TreeMap存储原子名称和对应的数量，保证输出时按字典序排列
//
// 时间复杂度: O(n)，其中n是字符串的长度
// 空间复杂度: O(n)，递归调用栈的深度和存储原子数量的额外空间
public class Code03_NumberOfAtoms {

	public static String countOfAtoms(String str) {
		where = 0;
		TreeMap<String, Integer> map = f(str.toCharArray(), 0);
		StringBuilder ans = new StringBuilder();
		for (String key : map.keySet()) {
			ans.append(key);
			int cnt = map.get(key);
			if (cnt > 1) {
				ans.append(cnt);
			}
		}
		return ans.toString();
	}

	// 全局变量，记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
	public static int where;

	// s[i....]开始计算，遇到字符串终止 或者 遇到 ) 停止
	// 返回 : 自己负责的这一段字符串的结果，有序表！
	// 返回之间，更新全局变量where，为了上游函数知道从哪继续！
	public static TreeMap<String, Integer> f(char[] s, int i) {
		// ans是总表，存储原子名称和对应的数量
		TreeMap<String, Integer> ans = new TreeMap<>();
		// 之前收集到的名字，历史一部分
		StringBuilder name = new StringBuilder();
		// 之前收集到的有序表，历史一部分
		TreeMap<String, Integer> pre = null;
		// 历史翻几倍
		int cnt = 0;
		while (i < s.length && s[i] != ')') {
			if (s[i] >= 'A' && s[i] <= 'Z' || s[i] == '(') {
				fill(ans, name, pre, cnt);
				name.setLength(0);
				pre = null;
				cnt = 0;
				if (s[i] >= 'A' && s[i] <= 'Z') {
					name.append(s[i++]);
				} else {
					// 遇到 (
					pre = f(s, i + 1);
					i = where + 1;
				}
			} else if (s[i] >= 'a' && s[i] <= 'z') {
				name.append(s[i++]);
			} else {
				cnt = cnt * 10 + s[i++] - '0';
			}
		}
		fill(ans, name, pre, cnt);
		where = i;
		return ans;
	}

	// 将收集到的原子信息填充到结果中
	public static void fill(TreeMap<String, Integer> ans, StringBuilder name, TreeMap<String, Integer> pre, int cnt) {
		if (name.length() > 0 || pre != null) {
			cnt = cnt == 0 ? 1 : cnt;
			if (name.length() > 0) {
				String key = name.toString();
				ans.put(key, ans.getOrDefault(key, 0) + cnt);
			} else {
				for (String key : pre.keySet()) {
					ans.put(key, ans.getOrDefault(key, 0) + pre.get(key) * cnt);
				}
			}
		}
	}
	
	// 测试用例
	public static void main(String[] args) {
		String s = "Mg(OH)2";
		System.out.println("输入: " + s);
		System.out.println("输出: " + countOfAtoms(s));
		System.out.println("期望: H2MgO2");
	}
}