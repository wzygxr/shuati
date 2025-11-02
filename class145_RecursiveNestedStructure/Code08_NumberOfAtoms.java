import java.util.TreeMap;

// LeetCode 726. Number of Atoms (原子的数量)
// 测试链接 : https://leetcode.cn/problems/number-of-atoms/
//
// 题目描述:
// 给定一个化学式formula（作为字符串），返回每种原子的数量。
// 原子总是以一个大写字母开始，接着跟随0个或任意个小写字母，表示原子的名字。
// 如果数量大于 1，原子后会跟着数字表示原子的数量。如果数量等于 1 则不会跟数字。
// 例如，"H2O" 和 "H2O2" 是可行的，但 "H1O2" 这个表达是不可行的。
// 两个化学式连在一起是新的化学式。例如 "H2O2He3Mg4" 也是化学式。
// 一个括号中的化学式和数字（可选择）也是化学式。
// 例如 "(H2O2)" 和 "(H2O2)3" 是化学式。
// 给定一个化学式，输出所有原子的数量。
// 格式为：第一个（按字典序）原子的名子，跟着它的数量（如果数量大于 1），
// 然后是第二个原子的名字（按字典序），跟着它的数量（如果数量大于 1），
// 以此类推。
//
// 示例:
// 输入: formula = "H2O"
// 输出: "H2O"
// 解释: 原子的数量是 {'H': 2, 'O': 1}。
//
// 输入: formula = "Mg(OH)2"
// 输出: "H2MgO2"
// 解释: 原子的数量是 {'H': 2, 'Mg': 1, 'O': 2}。
//
// 输入: formula = "K4(ON(SO3)2)2"
// 输出: "K4N2O14S4"
// 解释: 原子的数量是 {'K': 4, 'N': 2, 'O': 14, 'S': 4}。
//
// 解题思路:
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的化学式
// 用全局变量where记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
// 用TreeMap存储原子名称和对应的数量，保证输出时按字典序排列
//
// 时间复杂度: O(n)，其中n是字符串的长度
// 空间复杂度: O(n)，递归调用栈的深度和存储原子数量的额外空间

public class Code08_NumberOfAtoms {

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
		// 测试用例1
		String s1 = "H2O";
		System.out.println("输入: " + s1);
		System.out.println("输出: " + countOfAtoms(s1));
		System.out.println("期望: H2O\n");
		
		// 测试用例2
		String s2 = "Mg(OH)2";
		System.out.println("输入: " + s2);
		System.out.println("输出: " + countOfAtoms(s2));
		System.out.println("期望: H2MgO2\n");
		
		// 测试用例3
		String s3 = "K4(ON(SO3)2)2";
		System.out.println("输入: " + s3);
		System.out.println("输出: " + countOfAtoms(s3));
		System.out.println("期望: K4N2O14S4\n");
	}
}