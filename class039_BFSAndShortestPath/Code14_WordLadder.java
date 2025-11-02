package class062;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

// 单词接龙
// 字典 wordList 中从单词 beginWord 到 endWord 的转换序列是一个按下述规格形成的序列：
// 每一对相邻的单词只差一个字母
// 对于 1 <= i <= k 时，每个 si 都在 wordList 中（注意 beginWord 不需要在 wordList 中）
// sk == endWord
// 给你两个单词 beginWord 和 endWord 和一个字典 wordList
// 返回从 beginWord 到 endWord 的最短转换序列中的单词数目
// 如果不存在这样的转换序列，返回 0
// 测试链接 : https://leetcode.com/problems/word-ladder/
// 
// 算法思路：
// 使用双向BFS解决单词接龙问题
// 从beginWord和endWord同时开始搜索，每次扩展节点数较少的一端
// 当两端相遇时，找到最短路径
// 
// 时间复杂度：O(N * M^2)，其中N是单词数量，M是单词长度
// 空间复杂度：O(N * M^2)，用于存储图和访问状态
// 
// 工程化考量：
// 1. 双向搜索：从两端同时搜索提高效率
// 2. 图的构建：预处理单词列表构建模式图
// 3. 优化策略：每次扩展节点数较少的一端
public class Code14_WordLadder {

	// 存储单词列表
	public static HashSet<String> dict;

	// 存储当前层和下一层的单词
	public static HashSet<String> curLevel = new HashSet<>();
	public static HashSet<String> nextLevel = new HashSet<>();

	// 存储访问过的单词
	public static HashSet<String> visited = new HashSet<>();

	// 存储模式图，key为模式，value为匹配该模式的单词列表
	public static HashMap<String, ArrayList<String>> graph = new HashMap<>();

	public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
		// 初始化数据结构
		dict = new HashSet<>(wordList);
		visited.clear();
		graph.clear();

		// 如果目标单词不在词典中，直接返回0
		if (!dict.contains(endWord)) {
			return 0;
		}

		// 构建模式图
		buildGraph(wordList);

		// 双向BFS
		return bfs(beginWord, endWord);
	}

	// 构建模式图
	public static void buildGraph(List<String> wordList) {
		for (String word : wordList) {
			char[] chars = word.toCharArray();
			// 对每个位置尝试替换为*
			for (int i = 0; i < chars.length; i++) {
				char old = chars[i];
				chars[i] = '*';
				String pattern = new String(chars);
				graph.putIfAbsent(pattern, new ArrayList<>());
				graph.get(pattern).add(word);
				chars[i] = old;
			}
		}
	}

	// 双向BFS
	public static int bfs(String begin, String end) {
		curLevel.clear();
		nextLevel.clear();
		visited.clear();

		// 起点和终点分别加入两个集合
		curLevel.add(begin);
		nextLevel.add(end);
		visited.add(begin);
		visited.add(end);

		int level = 1;

		// 双向BFS搜索
		while (!curLevel.isEmpty() && !nextLevel.isEmpty()) {
			level++;
			// 选择节点数较少的一端进行扩展
			if (curLevel.size() > nextLevel.size()) {
				HashSet<String> temp = curLevel;
				curLevel = nextLevel;
				nextLevel = temp;
			}

			// 扩展当前层的所有单词
			HashSet<String> temp = new HashSet<>();
			for (String word : curLevel) {
				// 生成所有可能的模式
				char[] chars = word.toCharArray();
				for (int i = 0; i < chars.length; i++) {
					char old = chars[i];
					chars[i] = '*';
					String pattern = new String(chars);
					// 获取匹配该模式的所有单词
					if (graph.containsKey(pattern)) {
						for (String next : graph.get(pattern)) {
							// 如果在另一端集合中找到，说明相遇
							if (nextLevel.contains(next)) {
								return level;
							}
							// 如果未访问过，加入下一层
							if (!visited.contains(next)) {
								temp.add(next);
								visited.add(next);
							}
						}
					}
					chars[i] = old;
				}
			}
			curLevel = temp;
		}
		return 0;
	}

}