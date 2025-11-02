package class038;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

// 字符串的全部子序列
// 子序列本身是可以有重复的，只是这个题目要求去重
// 测试链接 : https://www.nowcoder.com/practice/92e6247998294f2c933906fdedbc6e6a
public class Code01_Subsequences {

	/**
	 * 生成字符串的所有子序列（方法1）
	 * 使用StringBuilder构建路径
	 * 
	 * 算法思路：
	 * 1. 对于每个字符，我们有两种选择：包含在子序列中或不包含
	 * 2. 通过递归实现这种选择过程
	 * 3. 当遍历完所有字符时，将当前路径加入结果集
	 * 
	 * 时间复杂度：O(2^n * n)，其中n为字符串长度
	 * 空间复杂度：O(2^n * n)，用于存储所有子序列
	 * 
	 * @param str 输入字符串
	 * @return 所有不重复的子序列
	 */
	public static String[] generatePermutation1(String str) {
		char[] s = str.toCharArray();
		HashSet<String> set = new HashSet<>();
		f1(s, 0, new StringBuilder(), set);
		int m = set.size();
		String[] ans = new String[m];
		int i = 0;
		for (String cur : set) {
			ans[i++] = cur;
		}
		return ans;
	}

	// s[i...]，之前决定的路径path，set收集结果时去重
	public static void f1(char[] s, int i, StringBuilder path, HashSet<String> set) {
		if (i == s.length) {
			set.add(path.toString());
		} else {
			path.append(s[i]); // 加到路径中去
			f1(s, i + 1, path, set);
			path.deleteCharAt(path.length() - 1); // 从路径中移除
			f1(s, i + 1, path, set);
		}
	}

	/**
	 * 生成字符串的所有子序列（方法2）
	 * 使用字符数组构建路径
	 * 
	 * 算法思路：
	 * 与方法1相同，但使用字符数组和size变量来维护路径
	 * 这种方法避免了StringBuilder的频繁创建和销毁
	 * 
	 * 时间复杂度：O(2^n * n)
	 * 空间复杂度：O(2^n * n)
	 * 
	 * @param str 输入字符串
	 * @return 所有不重复的子序列
	 */
	public static String[] generatePermutation2(String str) {
		char[] s = str.toCharArray();
		HashSet<String> set = new HashSet<>();
		f2(s, 0, new char[s.length], 0, set);
		int m = set.size();
		String[] ans = new String[m];
		int i = 0;
		for (String cur : set) {
			ans[i++] = cur;
		}
		return ans;
	}

	public static void f2(char[] s, int i, char[] path, int size, HashSet<String> set) {
		if (i == s.length) {
			set.add(String.valueOf(path, 0, size));
		} else {
			path[size] = s[i];
			f2(s, i + 1, path, size + 1, set);
			f2(s, i + 1, path, size, set);
		}
	}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		String test1 = "abc";
		String[] result1 = generatePermutation1(test1);
		System.out.println("输入: " + test1);
		System.out.print("输出: [");
		for (int i = 0; i < result1.length; i++) {
			System.out.print("\"" + result1[i] + "\"");
			if (i < result1.length - 1) System.out.print(", ");
		}
		System.out.println("]");
		
		// 测试用例2
		String test2 = "aab";
		String[] result2 = generatePermutation2(test2);
		System.out.println("\n输入: " + test2);
		System.out.print("输出: [");
		for (int i = 0; i < result2.length; i++) {
			System.out.print("\"" + result2[i] + "\"");
			if (i < result2.length - 1) System.out.print(", ");
		}
		System.out.println("]");
	}

}