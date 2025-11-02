package class063;

import java.util.*;

// 单词接龙
// 字典 wordList 中从单词 beginWord 和 endWord 的 转换序列
// 是一个按下述规格形成的序列 beginWord -> s1 -> s2 -> ... -> sk ：
// 每一对相邻的单词只差一个字母。
// 对于 1 <= i <= k 时，每个 si 都在 wordList 中
// 注意， beginWord 不需要在 wordList 中。sk == endWord
// 给你两个单词 beginWord 和 endWord 和一个字典 wordList
// 返回 从 beginWord 到 endWord 的 最短转换序列 中的 单词数目
// 如果不存在这样的转换序列，返回 0 。
// 测试链接 : https://leetcode.cn/problems/word-ladder/
// 
// 算法思路：
// 使用双向BFS算法，从起点和终点同时开始搜索，一旦两个搜索相遇，就找到了最短路径
// 时间复杂度：O(M^2 * N)，其中M是单词的长度，N是单词列表中的单词数量
// 空间复杂度：O(N * M)
// 
// 工程化考量：
// 1. 异常处理：检查endWord是否在wordList中
// 2. 性能优化：使用双向BFS减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// Java中使用HashSet进行快速查找，使用toCharArray进行字符操作
public class Code01_WordLadder {

	public static int ladderLength(String begin, String end, List<String> wordList) {
		// 总词表
		HashSet<String> dict = new HashSet<>(wordList);
		// 如果目标单词不在词典中，直接返回0
		if (!dict.contains(end)) {
			return 0;
		}
		// 数量小的一侧
		HashSet<String> smallLevel = new HashSet<>();
		// 数量大的一侧
		HashSet<String> bigLevel = new HashSet<>();
		// 由数量小的一侧，所扩展出的下一层列表
		HashSet<String> nextLevel = new HashSet<>();
		smallLevel.add(begin);
		bigLevel.add(end);
		// len记录路径长度，初始为2（包含begin和end）
		for (int len = 2; !smallLevel.isEmpty(); len++) {
			// 从小数量的一侧开始扩展
			for (String w : smallLevel) {
				// 从小侧扩展
				char[] word = w.toCharArray();
				for (int j = 0; j < word.length; j++) {
					// 每一位字符都试
					char old = word[j];
					for (char change = 'a'; change <= 'z'; change++) {
						// 每一位字符都从a到z换一遍
						if (change != old) {
							word[j] = change;
							String next = String.valueOf(word);
							// 如果在大侧找到了，说明两路相遇，返回路径长度
							if (bigLevel.contains(next)) {
								return len;
							}
							// 如果在词典中找到了，加入下一层并从词典中移除
							if (dict.contains(next)) {
								dict.remove(next);
								nextLevel.add(next);
							}
						}
					}
					word[j] = old;
				}
			}
			// 优化：始终从小的一侧开始扩展
			if (nextLevel.size() <= bigLevel.size()) {
				HashSet<String> tmp = smallLevel;
				smallLevel = nextLevel;
				nextLevel = tmp;
			} else {
				HashSet<String> tmp = smallLevel;
				smallLevel = bigLevel;
				bigLevel = nextLevel;
				nextLevel = tmp;
			}
			// 清空nextLevel，为下一轮扩展做准备
			nextLevel.clear();
		}
		return 0;
	}

	// 测试用例和主函数
	public static void main(String[] args) {
		// 测试用例1
		List<String> wordList1 = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
		System.out.println("测试用例1:");
		System.out.println("beginWord: hit, endWord: cog");
		System.out.println("wordList: [hot, dot, dog, lot, log, cog]");
		System.out.println("期望输出: 5");
		System.out.println("实际输出: " + ladderLength("hit", "cog", wordList1));
		System.out.println();
		
		// 测试用例2
		List<String> wordList2 = Arrays.asList("hot", "dot", "dog", "lot", "log");
		System.out.println("测试用例2:");
		System.out.println("beginWord: hit, endWord: cog");
		System.out.println("wordList: [hot, dot, dog, lot, log]");
		System.out.println("期望输出: 0");
		System.out.println("实际输出: " + ladderLength("hit", "cog", wordList2));
	}
}