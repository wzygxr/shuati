package class045;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 212. 单词搜索 II
 * 
 * 题目描述：
 * 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words，
 * 返回所有二维网格上的单词。单词必须按照字母顺序，通过相邻的单元格内的字母构成。
 * 其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母在一个单词中不允许被重复使用。
 * 
 * 约束条件：
 * - 1 <= m, n <= 12
 * - 1 <= words.length <= 3 * 10^4
 * - 1 <= words[i].length <= 10
 * 
 * 测试链接：https://leetcode.cn/problems/word-search-ii/
 * 
 * 算法思路：
 * 1. 构建前缀树，将所有单词插入前缀树
 * 2. 从每个网格位置开始深度优先搜索，查找能构成的单词
 * 3. 在搜索过程中，利用前缀树剪枝，提高效率
 * 
 * 核心优化：
 * 使用前缀树存储单词列表，可以在搜索过程中快速判断当前路径是否可能是某个单词的前缀，
 * 从而避免无效的深度搜索，大大提高搜索效率。
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
 * - 搜索过程：O(m * n * 4^l)，其中m和n是网格的行数和列数，l是最长单词的长度
 * - 总体时间复杂度：O(∑len(words[i]) + m * n * 4^l)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
 * - 递归栈空间：O(l)，其中l是最长单词的长度
 * - 总体空间复杂度：O(∑len(words[i]) + l)
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效地存储和查询单词，避免了重复搜索
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或网格为空的情况
 * 2. 边界情况：网格中没有单词或单词为空的情况
 * 3. 极端输入：大量单词或网格很大或单词很长的情况
 * 4. 鲁棒性：处理重复单词和特殊字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. LeetCode 212. 单词搜索 II (本题)
 * 2. LeetCode 79. 单词搜索
 * 3. LeetCode 208. 实现 Trie (前缀树)
 * 4. LeetCode 211. 添加与搜索单词 - 数据结构设计
 * 5. LintCode 132. 单词搜索 II
 * 6. 牛客网 NC137. 单词搜索
 * 7. HackerRank - Word Search
 * 8. CodeChef - WORDSEARCH
 * 9. SPOJ - WORDS
 * 10. AtCoder - Grid 1
 */
public class Code03_WordSearchII {

	/**
	 * 在二维字符网格中查找所有单词
	 * 
	 * 算法步骤详解：
	 * 1. 构建前缀树：
	 *    a. 初始化前缀树结构
	 *    b. 将所有单词插入前缀树
	 * 2. 网格搜索：
	 *    a. 遍历网格的每个位置作为搜索起点
	 *    b. 从每个起点开始进行深度优先搜索
	 *    c. 利用前缀树剪枝优化搜索过程
	 * 3. 结果收集：
	 *    a. 找到完整单词时将其加入结果列表
	 *    b. 避免重复添加相同单词
	 * 4. 清理资源：
	 *    a. 搜索完成后清空前缀树
	 * 
	 * 剪枝优化原理：
	 * 在深度优先搜索过程中，通过前缀树可以快速判断当前路径是否可能是某个单词的前缀。
	 * 如果当前路径在前缀树中不存在对应节点，说明该路径不可能构成任何单词，可以立即回溯。
	 * 
	 * 时间复杂度分析：
	 * - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
	 * - 搜索过程：O(m * n * 4^l)，其中m和n是网格的行数和列数，l是最长单词的长度
	 * - 总体时间复杂度：O(∑len(words[i]) + m * n * 4^l)
	 * 
	 * 空间复杂度分析：
	 * - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
	 * - 递归栈空间：O(l)，其中l是最长单词的长度
	 * - 总体空间复杂度：O(∑len(words[i]) + l)
	 * 
	 * 是否最优解：是
	 * 理由：使用前缀树可以高效地存储和查询单词，避免了重复搜索
	 * 
	 * 工程化考虑：
	 * 1. 异常处理：输入为空或网格为空的情况
	 * 2. 边界情况：网格中没有单词或单词为空的情况
	 * 3. 极端输入：大量单词或网格很大或单词很长的情况
	 * 4. 鲁棒性：处理重复单词和特殊字符
	 * 
	 * 语言特性差异：
	 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
	 * C++：可使用指针实现前缀树节点，更节省空间
	 * Python：可使用字典实现前缀树，代码更简洁
	 * 
	 * @param board 二维字符网格
	 * @param words 单词列表
	 * @return 在网格中找到的所有单词
	 */
	public static List<String> findWords(char[][] board, String[] words) {
		build(words);
		List<String> ans = new ArrayList<>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				dfs(board, i, j, 1, ans);
			}
		}
		clear();
		return ans;
	}

	/**
	 * 深度优先搜索查找单词
	 * 
	 * 算法步骤：
	 * 1. 边界检查：
	 *    a. 检查坐标是否越界
	 *    b. 检查是否已访问过该位置（通过字符为0判断）
	 * 2. 路径有效性检查：
	 *    a. 获取当前位置字符
	 *    b. 计算字符在前缀树中的路径索引
	 *    c. 检查前缀树中是否存在对应路径
	 * 3. 单词匹配检查：
	 *    a. 如果当前节点是单词结尾，将单词加入结果列表
	 *    b. 标记当前位置为已访问（设为0）
	 * 4. 四方向递归搜索：
	 *    a. 向上、下、左、右四个方向递归搜索
	 *    b. 累加找到的单词数量
	 * 5. 回溯处理：
	 *    a. 恢复当前位置字符
	 *    b. 更新前缀树节点的通过计数
	 * 
	 * 回溯优化：
	 * 通过将访问过的位置字符设为0来标记已访问，搜索完成后恢复原字符，
	 * 实现了高效的回溯机制，避免了额外的访问标记数组。
	 * 
	 * 时间复杂度：O(4^l)，其中l是最长单词的长度
	 * 空间复杂度：O(l)，递归栈空间
	 * 
	 * @param board 二维字符网格
	 * @param i 当前行索引
	 * @param j 当前列索引
	 * @param t 前缀树节点编号
	 * @param ans 结果列表
	 * @return 找到的单词数量
	 */
	// board : 二维网格
	// i,j : 此时来到的格子位置，i行、j列
	// t : 前缀树的编号
	// List<String> ans : 收集到了哪些字符串，都放入ans
	// 返回值 : 收集到了几个字符串
	public static int dfs(char[][] board, int i, int j, int t, List<String> ans) {
		// 越界 或者 走了回头路，直接返回0
		if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] == 0) {
			return 0;
		}
		// 不越界 且 不是回头路
		// 用tmp记录当前字符
		char tmp = board[i][j];
		// 路的编号
		// a -> 0
		// b -> 1
		// ...
		// z -> 25
		int road = tmp - 'a';
		t = tree[t][road];
		if (pass[t] == 0) {
			return 0;
		}
		// i，j位置有必要来
		// fix ：从当前i，j位置出发，一共收集到了几个字符串
		int fix = 0;
		if (end[t] != null) {
			fix++;
			ans.add(end[t]);
			end[t] = null;
		}
		// 把i，j位置的字符，改成0，后续的过程，是不可以再来到i，j位置的！
		board[i][j] = 0;
		fix += dfs(board, i - 1, j, t, ans);
		fix += dfs(board, i + 1, j, t, ans);
		fix += dfs(board, i, j - 1, t, ans);
		fix += dfs(board, i, j + 1, t, ans);
		pass[t] -= fix;
		board[i][j] = tmp;
		return fix;
	}

	public static int MAXN = 10001;

	public static int[][] tree = new int[MAXN][26];

	public static int[] pass = new int[MAXN];

	public static String[] end = new String[MAXN];

	public static int cnt;

	/**
	 * 构建前缀树
	 * 
	 * 算法步骤：
	 * 1. 初始化前缀树节点计数器
	 * 2. 遍历单词列表中的每个单词：
	 *    a. 从根节点开始遍历单词的每个字符
	 *    b. 计算字符的路径索引（字符-'a'）
	 *    c. 如果子节点不存在，则创建新节点
	 *    d. 移动到子节点，增加通过计数
	 *    e. 单词遍历完成后，标记单词结尾
	 * 
	 * 节点属性说明：
	 * - tree[i][j]：节点i的第j个子节点
	 * - pass[i]：经过节点i的单词数量
	 * - end[i]：以节点i结尾的单词
	 * 
	 * 时间复杂度：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
	 * 空间复杂度：O(∑len(words[i]))
	 * 
	 * @param words 单词列表
	 */
	public static void build(String[] words) {
		cnt = 1;
		for (String word : words) {
			int cur = 1;
			pass[cur]++;
			for (int i = 0, path; i < word.length(); i++) {
				path = word.charAt(i) - 'a';
				if (tree[cur][path] == 0) {
					tree[cur][path] = ++cnt;
				}
				cur = tree[cur][path];
				pass[cur]++;
			}
			end[cur] = word;
		}
	}

	/**
	 * 清空前缀树
	 * 
	 * 算法步骤：
	 * 1. 遍历所有已使用的节点
	 * 2. 将节点的子节点数组清零
	 * 3. 将节点的通过计数重置为0
	 * 4. 将节点的单词结尾标记设为null
	 * 
	 * 资源管理：
	 * 通过清空前缀树结构，释放内存资源，避免内存泄漏
	 * 
	 * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
	 * 空间复杂度：O(1)
	 */
	public static void clear() {
		for (int i = 1; i <= cnt; i++) {
			Arrays.fill(tree[i], 0);
			pass[i] = 0;
			end[i] = null;
		}
	}

}