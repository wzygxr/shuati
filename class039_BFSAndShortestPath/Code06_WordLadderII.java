package class062;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

// 单词接龙 II
// 按字典 wordList 完成从单词 beginWord 到单词 endWord 转化
// 一个表示此过程的 转换序列 是形式上像 
// beginWord -> s1 -> s2 -> ... -> sk 这样的单词序列，并满足：
// 每对相邻的单词之间仅有单个字母不同
// 转换过程中的每个单词 si（1 <= i <= k）必须是字典 wordList 中的单词
// 注意，beginWord 不必是字典 wordList 中的单词
// sk == endWord
// 给你两个单词 beginWord 和 endWord ，以及一个字典 wordList
// 请你找出并返回所有从 beginWord 到 endWord 的 最短转换序列
// 如果不存在这样的转换序列，返回一个空列表
// 每个序列都应该以单词列表 [beginWord, s1, s2, ..., sk] 的形式返回
// 测试链接 : https://leetcode.cn/problems/word-ladder-ii/
// 
// 算法思路：
// 使用双向BFS构建图，然后使用DFS找到所有最短路径
// 1. 使用BFS从beginWord开始构建反向图（从endWord指向beginWord）
// 2. 在BFS过程中，只扩展能到达endWord的节点
// 3. 使用DFS在构建的图中找到所有从endWord到beginWord的路径
// 4. 将路径反转得到从beginWord到endWord的路径
// 
// 时间复杂度：O(N * M^2 + M * N^2)，其中N是单词数量，M是单词长度
// 空间复杂度：O(N * M^2)，用于存储图和访问状态
// 
// 工程化考量：
// 1. 使用HashMap存储图结构
// 2. 使用HashSet快速查找单词是否存在
// 3. 使用双向BFS优化搜索效率
public class Code06_WordLadderII {

	// 单词表 ： list -> hashSet
	public static HashSet<String> dict;

	public static HashSet<String> curLevel = new HashSet<>();

	public static HashSet<String> nextLevel = new HashSet<>();

	// 反向图
	public static HashMap<String, ArrayList<String>> graph = new HashMap<>();

	// 记录路径，当生成一条有效路的时候，拷贝进ans！
	public static LinkedList<String> path = new LinkedList<>();

	public static List<List<String>> ans = new ArrayList<>();

	public static void build(List<String> wordList) {
		dict = new HashSet<>(wordList);
		graph.clear();
		ans.clear();
		curLevel.clear();
		nextLevel.clear();
	}

	public static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
		build(wordList);
		// 如果目标单词不在词典中，直接返回空列表
		if (!dict.contains(endWord)) {
			return ans;
		}
		// 使用BFS构建图，如果能找到endWord则进行DFS搜索路径
		if (bfs(beginWord, endWord)) {
			dfs(endWord, beginWord);
		}
		return ans;
	}

	// begin -> end ，一层层bfs去，建图
	// 返回值：真的能找到end，返回true；false
	public static boolean bfs(String begin, String end) {
		boolean find = false;
		curLevel.add(begin);
		while (!curLevel.isEmpty()) {
			// 移除当前层的所有单词，避免在后续层中再次处理
			dict.removeAll(curLevel);
			// 处理当前层的所有单词
			for (String word : curLevel) {
				// word : 去扩
				// 每个位置，字符a~z，换一遍！检查在词表中是否存在
				// 避免，加工出自己
				char[] w = word.toCharArray();
				for (int i = 0; i < w.length; i++) {
					char old = w[i];
					// 尝试将第i个字符替换为a-z中的每个字符
					for (char ch = 'a'; ch <= 'z'; ch++) {
						w[i] = ch;
						String str = String.valueOf(w);
						// 如果新单词在词典中且不等于原单词
						if (dict.contains(str) && !str.equals(word)) {
							// 如果找到了目标单词
							if (str.equals(end)) {
								find = true;
							}
							// 在反向图中添加边
							graph.putIfAbsent(str, new ArrayList<>());
							graph.get(str).add(word);
							// 将新单词加入下一层
							nextLevel.add(str);
						}
					}
					w[i] = old;
				}
			}
			// 如果找到了目标单词，返回true
			if (find) {
				return true;
			} else {
				// 交换当前层和下一层
				HashSet<String> tmp = curLevel;
				curLevel = nextLevel;
				nextLevel = tmp;
				nextLevel.clear();
			}
		}
		return false;
	}

	// 使用DFS在构建的图中查找所有路径
	public static void dfs(String word, String aim) {
		// 将当前单词添加到路径开头
		path.addFirst(word);
		// 如果到达目标单词
		if (word.equals(aim)) {
			// 将路径添加到结果中
			ans.add(new ArrayList<>(path));
		// 如果图中包含当前单词
		} else if (graph.containsKey(word)) {
			// 递归处理所有前驱单词
			for (String next : graph.get(word)) {
				dfs(next, aim);
			}
		}
		// 回溯，移除当前单词
		path.removeFirst();
	}

}