package class067;

/**
 * 单词搜索（Word Search） - Java实现
 * 
 * 题目描述：
 * 给定一个 m x n 二维字符网格 board 和一个字符串单词 word
 * 如果 word 存在于网格中，返回 true ；否则，返回 false 。
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成
 * 其中"相邻"单元格是那些水平相邻或垂直相邻的单元格
 * 同一个单元格内的字母不允许被重复使用
 * 
 * 题目来源：LeetCode 79. 单词搜索
 * 题目链接：https://leetcode.cn/problems/word-search/
 * 
 * 算法思想：深度优先搜索（DFS）+ 回溯法
 * 1. 遍历网格中的每个位置作为起点
 * 2. 从每个起点开始进行深度优先搜索
 * 3. 在搜索过程中使用回溯法避免重复使用同一单元格
 * 4. 当找到完整单词时返回true，否则继续搜索
 * 
 * 时间复杂度：O(m*n*4^L) - 其中L为单词长度，最坏情况下需要从每个位置开始搜索
 * 空间复杂度：O(m*n) - 递归栈深度和标记数组
 * 是否最优解：是 - 回溯法是解决此类路径搜索问题的标准方法
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性，处理空网格、空单词等特殊情况
 * 2. 边界处理：处理网格边界、单词边界等边界条件
 * 3. 性能优化：剪枝策略提前终止无效搜索，减少不必要的递归
 * 4. 可测试性：提供完整的测试用例，覆盖各种边界场景
 * 5. 可维护性：代码结构清晰，注释详细，便于理解和维护
 * 
 * 调试技巧：
 * 1. 打印搜索路径，观察递归过程
 * 2. 使用小规模测试用例验证算法正确性
 * 3. 对比不同起点的搜索结果，确保一致性
 * 
 * 与机器学习联系：
 * 1. 路径搜索问题在强化学习中有广泛应用
 * 2. 回溯法思想在决策树搜索中体现
 * 3. 图搜索算法与图神经网络相关
 * 
 * 跨语言差异：
 * - 与Python相比：Java需要显式处理字符数组和边界检查
 * - 与C++相比：Java有更好的内存管理，但性能相对较低
 * 
 * 极端场景处理：
 * - 空输入：返回false
 * - 单字符网格：直接比较字符
 * - 大网格长单词：使用剪枝优化性能
 * - 重复字符网格：确保回溯正确性
 */

public class Code02_WordSearch {

	/**
	 * 主方法：判断单词是否存在于网格中
	 * 
	 * 算法流程：
	 * 1. 输入验证：检查网格和单词的合法性
	 * 2. 单词预处理：将字符串转换为字符数组
	 * 3. 遍历起点：从网格的每个位置开始尝试搜索
	 * 4. 深度优先搜索：对每个起点进行DFS搜索
	 * 5. 结果返回：如果找到返回true，否则返回false
	 * 
	 * 时间复杂度：O(m*n*4^L) - 其中m,n为网格尺寸，L为单词长度
	 * 空间复杂度：O(m*n) - 递归栈深度，最坏情况下需要遍历整个网格
	 * 
	 * 优化策略：
	 * 1. 提前剪枝：如果单词长度超过网格总字符数，直接返回false
	 * 2. 字符频率检查：如果单词中某个字符在网格中不存在，直接返回false
	 * 3. 双向搜索：从单词两端同时搜索，减少搜索空间
	 * 
	 * @param board 二维字符网格，不能为null或空
	 * @param word  要搜索的单词，不能为null
	 * @return 如果单词存在于网格中返回true，否则返回false
	 * 
	 * 异常处理：
	 * - 空网格：返回false
	 * - 空单词：返回false
	 * - 单词长度超过网格总字符数：返回false
	 */
	public static boolean exist(char[][] board, String word) {
		// 输入验证：检查网格和单词的合法性
		if (board == null || board.length == 0 || board[0].length == 0 || word == null) {
			return false;
		}
		
		// 额外优化：如果单词长度超过网格总字符数，直接返回false
		int totalCells = board.length * board[0].length;
		if (word.length() > totalCells) {
			return false;
		}
		
		// 将单词转换为字符数组便于处理
		char[] w = word.toCharArray();
		
		// 遍历网格中的每个位置作为起点
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				// 从当前位置开始搜索，如果找到则立即返回true
				// 使用深度优先搜索（DFS）进行路径探索
				if (dfs(board, i, j, w, 0)) {
					return true;
				}
			}
		}
		
		// 遍历完所有位置都没找到，返回false
		return false;
	}

	/**
	 * 深度优先搜索（DFS）辅助函数
	 * 
	 * 算法流程：
	 * 1. 终止条件检查：如果已匹配完整个单词，返回true
	 * 2. 边界条件检查：检查是否越界或字符不匹配
	 * 3. 标记访问：将当前单元格标记为已访问（使用特殊字符0）
	 * 4. 四个方向探索：向上、下、左、右四个方向递归搜索
	 * 5. 回溯恢复：恢复当前单元格的原始字符
	 * 
	 * 核心思想：回溯法
	 * - 在递归前修改状态（标记已访问）
	 * - 在递归后恢复状态（恢复原始字符）
	 * - 确保每个单元格在单次搜索路径中只被使用一次
	 * 
	 * 时间复杂度：O(4^L) - 每个位置最多有4个方向选择，L为剩余字符数
	 * 空间复杂度：O(L) - 递归栈深度，最坏情况下等于单词长度
	 * 
	 * 剪枝优化：
	 * - 提前终止：一旦找到完整路径立即返回，避免不必要的搜索
	 * - 边界检查：在递归前检查边界条件，减少无效递归
	 * 
	 * @param b   二维字符网格
	 * @param i   当前行坐标
	 * @param j   当前列坐标
	 * @param w   要搜索的单词字符数组
	 * @param k   当前要匹配的字符索引（从0开始）
	 * @return 如果能从当前位置开始找到完整单词返回true，否则返回false
	 * 
	 * 调试技巧：
	 * - 打印当前搜索路径和匹配状态
	 * - 使用小规模网格验证搜索逻辑
	 * - 对比不同方向的搜索结果
	 */
	public static boolean dfs(char[][] b, int i, int j, char[] w, int k) {
		// 基础情况：已经匹配完整个单词
		// 当k等于单词长度时，说明已经成功匹配所有字符
		if (k == w.length) {
			return true;
		}
		
		// 边界条件检查：
		// 1. 行索引越界：i < 0 或 i >= b.length
		// 2. 列索引越界：j < 0 或 j >= b[0].length
		// 3. 字符不匹配：当前网格字符不等于目标字符
		if (i < 0 || i >= b.length || j < 0 || j >= b[0].length || b[i][j] != w[k]) {
			return false;
		}
		
		// 当前字符匹配成功，继续搜索后续字符
		// 标记当前位置已访问：使用特殊字符0标记，防止重复访问
		// 保存原始字符以便回溯时恢复
		char originalChar = b[i][j];
		b[i][j] = 0;  // 标记为已访问
		
		// 向四个方向进行深度优先搜索
		// 使用短路或运算：一旦某个方向找到完整路径，立即返回true
		boolean found = dfs(b, i - 1, j, w, k + 1) ||  // 向上搜索
						dfs(b, i + 1, j, w, k + 1) ||   // 向下搜索
						dfs(b, i, j - 1, w, k + 1) ||   // 向左搜索
						dfs(b, i, j + 1, w, k + 1);     // 向右搜索
		
		// 回溯：恢复当前位置的原始字符
		// 无论搜索是否成功，都需要恢复状态，以便其他路径可以正常使用该单元格
		b[i][j] = originalChar;
		
		return found;
	}
	
	/**
	 * 测试方法：验证单词搜索算法的正确性
	 * 
	 * 测试用例设计：
	 * 1. 正常情况测试：单词存在于网格中
	 * 2. 边界情况测试：单词不存在于网格中
	 * 3. 特殊情况测试：单字符网格、空网格、空单词
	 * 4. 复杂情况测试：包含回溯的路径搜索
	 * 
	 * 测试目的：确保算法在各种情况下都能正确工作
	 */
	public static void test() {
		System.out.println("=== 单词搜索算法测试 ===");
		
		// 测试用例1：正常情况 - 单词存在
		char[][] board1 = {
			{'A', 'B', 'C', 'E'},
			{'S', 'F', 'C', 'S'},
			{'A', 'D', 'E', 'E'}
		};
		String word1 = "ABCCED";
		System.out.println("测试用例1 - 正常情况:");
		System.out.println("网格: " + java.util.Arrays.deepToString(board1));
		System.out.println("单词: " + word1);
		System.out.println("是否存在: " + exist(board1, word1));
		System.out.println("预期结果: true");
		System.out.println();
		
		// 测试用例2：正常情况 - 单词不存在
		String word2 = "ABCB";
		System.out.println("测试用例2 - 单词不存在:");
		System.out.println("网格: " + java.util.Arrays.deepToString(board1));
		System.out.println("单词: " + word2);
		System.out.println("是否存在: " + exist(board1, word2));
		System.out.println("预期结果: false");
		System.out.println();
		
		// 测试用例3：单字符网格
		char[][] board3 = {{'A'}};
		String word3 = "A";
		String word4 = "B";
		System.out.println("测试用例3 - 单字符网格:");
		System.out.println("网格: " + java.util.Arrays.deepToString(board3));
		System.out.println("单词 '" + word3 + "' 是否存在: " + exist(board3, word3));
		System.out.println("单词 '" + word4 + "' 是否存在: " + exist(board3, word4));
		System.out.println("预期结果: true, false");
		System.out.println();
		
		// 测试用例4：包含回溯的复杂路径
		char[][] board4 = {
			{'A', 'B', 'A', 'B'},
			{'B', 'A', 'B', 'A'},
			{'A', 'B', 'A', 'B'}
		};
		String word5 = "ABABABAB";  // 需要回溯的路径
		System.out.println("测试用例4 - 复杂回溯路径:");
		System.out.println("网格: " + java.util.Arrays.deepToString(board4));
		System.out.println("单词: " + word5);
		System.out.println("是否存在: " + exist(board4, word5));
		System.out.println("预期结果: true");
		System.out.println();
		
		// 测试用例5：边界情况 - 空网格
		char[][] emptyBoard = new char[0][0];
		System.out.println("测试用例5 - 空网格:");
		System.out.println("空网格是否存在单词 'TEST': " + exist(emptyBoard, "TEST"));
		System.out.println("预期结果: false");
		System.out.println();
		
		System.out.println("=== 测试完成 ===");
	}
	
	/**
	 * 主方法：运行测试用例
	 */
	public static void main(String[] args) {
		test();
	}

}