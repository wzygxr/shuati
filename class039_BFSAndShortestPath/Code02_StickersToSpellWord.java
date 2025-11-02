package class062;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

// 贴纸拼词
// 我们有 n 种不同的贴纸。每个贴纸上都有一个小写的英文单词。
// 您想要拼写出给定的字符串 target ，方法是从收集的贴纸中切割单个字母并重新排列它们
// 如果你愿意，你可以多次使用每个贴纸，每个贴纸的数量是无限的。
// 返回你需要拼出 target 的最小贴纸数量。如果任务不可能，则返回 -1
// 注意：在所有的测试用例中，所有的单词都是从 1000 个最常见的美国英语单词中随机选择的
// 并且 target 被选择为两个随机单词的连接。
// 测试链接 : https://leetcode.cn/problems/stickers-to-spell-word/
// 
// 算法思路：
// 使用BFS搜索，状态是当前还需要拼写的字符串
// 初始状态是target，目标状态是空字符串
// 对于每个状态，尝试使用每种贴纸，得到新的状态
// 使用记忆化搜索避免重复计算
// 
// 时间复杂度：O(2^n * m * k)，其中n是target长度，m是贴纸数量，k是贴纸平均长度
// 空间复杂度：O(2^n)，用于存储访问过的状态
// 
// 工程化考量：
// 1. 字符串预处理：对贴纸中的字符进行排序，便于处理
// 2. 优化：只考虑能减少目标字符串第一个字符的贴纸
// 3. 边界情况：如果目标字符串中有贴纸中没有的字符，直接返回-1
public class Code02_StickersToSpellWord {

	public static int MAXN = 401;

	public static String[] queue = new String[MAXN];

	public static int l, r;

	// 下标0 -> a
	// 下标1 -> b
	// 下标2 -> c
	// ...
	// 下标25 -> z
	public static ArrayList<ArrayList<String>> graph = new ArrayList<>();

	static {
		for (int i = 0; i < 26; i++) {
			graph.add(new ArrayList<>());
		}
	}

	public static HashSet<String> visited = new HashSet<>();

	// 宽度优先遍历的解法
	// 也可以使用动态规划
	// 后续课程会有动态规划专题讲解
	public static int minStickers(String[] stickers, String target) {
		// 初始化图结构
		for (int i = 0; i < 26; i++) {
			graph.get(i).clear();
		}
		visited.clear();
		
		// 对每个贴纸进行预处理，按字符排序
		for (String str : stickers) {
			str = sort(str);
			// 对于每个字符，记录包含该字符的贴纸
			for (int i = 0; i < str.length(); i++) {
				// 避免重复添加相同贴纸
				if (i == 0 || str.charAt(i) != str.charAt(i - 1)) {
					graph.get(str.charAt(i) - 'a').add(str);
				}
			}
		}
		
		// 对目标字符串排序
		target = sort(target);
		visited.add(target);
		l = r = 0;
		queue[r++] = target;
		int level = 1;
		
		// 使用队列的形式是整层弹出
		while (l < r) {
			int size = r - l;
			// 处理当前层的所有状态
			for (int i = 0; i < size; i++) {
				String cur = queue[l++];
				// 只考虑能消除第一个字符的贴纸
				for (String s : graph.get(cur.charAt(0) - 'a')) {
					String next = next(cur, s);
					// 如果已经拼完所有字符
					if (next.equals("")) {
						return level;
					} else if (!visited.contains(next)) {
						visited.add(next);
						queue[r++] = next;
					}
				}
			}
			level++;
		}
		return -1;
	}

	// 对字符串按字符排序
	public static String sort(String str) {
		char[] s = str.toCharArray();
		Arrays.sort(s);
		return String.valueOf(s);
	}

	// 用贴纸s消除目标字符串t中的字符
	public static String next(String t, String s) {
		StringBuilder builder = new StringBuilder();
		// 双指针处理
		for (int i = 0, j = 0; i < t.length();) {
			// 如果贴纸字符用完了，或者目标字符小于贴纸字符，保留目标字符
			if (j == s.length()) {
				builder.append(t.charAt(i++));
			} else {
				// 如果目标字符小于贴纸字符，保留目标字符
				if (t.charAt(i) < s.charAt(j)) {
					builder.append(t.charAt(i++));
				// 如果目标字符大于贴纸字符，移动贴纸指针
				} else if (t.charAt(i) > s.charAt(j)) {
					j++;
				// 如果字符相等，同时移动两个指针（相当于消除）
				} else {
					i++;
					j++;
				}
			}
		}
		return builder.toString();
	}

}