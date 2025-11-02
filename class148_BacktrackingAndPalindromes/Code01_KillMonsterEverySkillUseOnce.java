package class043;

// 现在有一个打怪类型的游戏，这个游戏是这样的，你有n个技能
// 每一个技能会有一个伤害，
// 同时若怪物小于等于一定的血量，则该技能可能造成双倍伤害
// 每一个技能最多只能释放一次，已知怪物有m点血量
// 现在想问你最少用几个技能能消灭掉他(血量小于等于0)
// 技能的数量是n，怪物的血量是m
// i号技能的伤害是x[i]，i号技能触发双倍伤害的血量最小值是y[i]
// 1 <= n <= 10
// 1 <= m、x[i]、y[i] <= 10^6
// 测试链接 : https://www.nowcoder.com/practice/d88ef50f8dab4850be8cd4b95514bbbd
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的所有代码，并把主类名改成"Main"
// 可以直接通过

/**
 * 回溯算法 - 技能打怪问题
 * 
 * 算法思路：
 * 1. 这是一个典型的回溯算法问题，需要找出最少的技能使用次数来击败怪物
 * 2. 使用回溯算法遍历所有可能的技能使用顺序，找到最少技能数的方案
 * 3. 在每一步尝试使用不同的技能，通过递归和回溯来探索所有可能性
 * 
 * 时间复杂度分析：
 * - 最坏情况下需要尝试所有技能的排列组合
 * - 技能数为n，时间复杂度为O(n!)
 * - 对于n<=10的情况，10! = 3,628,800，可以在合理时间内完成
 * 
 * 空间复杂度分析：
 * - 主要空间消耗是递归栈深度和存储技能信息的数组
 * - 递归深度最大为n，空间复杂度为O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：对输入数据进行校验
 * 2. 可配置性：MAXN常量可以调整以适应不同规模的问题
 * 3. 性能优化：使用回溯算法，通过交换元素避免创建新数组
 * 4. 鲁棒性：处理边界情况，如怪物血量为0或技能数组为空
 * 
 * 相关题目：
 * 1. LeetCode 46. 全排列 - https://leetcode.cn/problems/permutations/
 * 2. LeetCode 47. 全排列 II - https://leetcode.cn/problems/permutations-ii/
 * 3. LeetCode 39. 组合总和 - https://leetcode.cn/problems/combination-sum/
 * 4. LeetCode 40. 组合总和 II - https://leetcode.cn/problems/combination-sum-ii/
 * 5. LeetCode 78. 子集 - https://leetcode.cn/problems/subsets/
 * 6. LeetCode 90. 子集 II - https://leetcode.cn/problems/subsets-ii/
 * 7. 牛客网 - 打怪兽 - https://www.nowcoder.com/practice/d88ef50f8dab4850be8cd4b95514bbbd
 * 8. Codeforces 1312C - Make It Good - https://codeforces.com/problemset/problem/1312/C
 * 9. Codeforces 1332B - Composite Coloring - https://codeforces.com/problemset/problem/1332/B
 * 10. AtCoder ABC145D - Knight - https://atcoder.jp/contests/abc145/tasks/abc145_d
 * 11. AtCoder ABC159E - Dividing Chocolate - https://atcoder.jp/contests/abc159/tasks/abc159_e
 * 12. 洛谷 P1135 - 奇怪的电梯 - https://www.luogu.com.cn/problem/P1135
 * 13. 洛谷 P1157 - 组合的输出 - https://www.luogu.com.cn/problem/P1157
 * 14. HackerRank - The Coin Change Problem - https://www.hackerrank.com/challenges/coin-change/problem
 * 15. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 16. LintCode 135 - Combination Sum - https://www.lintcode.com/problem/135/
 * 17. SPOJ - PERMUT1 - Permutations - https://www.spoj.com/problems/PERMUT1/
 * 18. UVa 10004 - Bicoloring - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=945
 * 19. HDU 1010 - Tempter of the Bone - https://acm.hdu.edu.cn/showproblem.php?pid=1010
 * 20. POJ 1011 - Sticks - http://poj.org/problem?id=1011
 * 21. LeetCode 31. 下一个排列 - https://leetcode.cn/problems/next-permutation/
 * 22. LeetCode 77. 组合 - https://leetcode.cn/problems/combinations/
 * 23. LeetCode 79. 单词搜索 - https://leetcode.cn/problems/word-search/
 * 24. LeetCode 17. 电话号码的字母组合 - https://leetcode.cn/problems/letter-combinations-of-a-phone-number/
 * 25. LeetCode 22. 括号生成 - https://leetcode.cn/problems/generate-parentheses/
 * 26. LeetCode 10. 正则表达式匹配 - https://leetcode.cn/problems/regular-expression-matching/
 * 27. LeetCode 37. 解数独 - https://leetcode.cn/problems/sudoku-solver/
 * 28. LeetCode 51. N 皇后 - https://leetcode.cn/problems/n-queens/
 * 29. LeetCode 52. N 皇后 II - https://leetcode.cn/problems/n-queens-ii/
 * 30. LeetCode 140. 单词拆分 II - https://leetcode.cn/problems/word-break-ii/
 * 31. Codeforces 1327D. Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 32. Codeforces 1436E. Complicated Computations - https://codeforces.com/problemset/problem/1436/E
 * 33. HackerRank - Split the String - https://www.hackerrank.com/challenges/split-the-string/problem
 * 34. 洛谷 P1048. 采药 - https://www.luogu.com.cn/problem/P1048
 * 35. 洛谷 P1125. 笨小猴 - https://www.luogu.com.cn/problem/P1125
 * 36. LintCode 125. 背包问题 II - https://www.lintcode.com/problem/125/
 * 37. LintCode 200. 最长回文子串 - https://www.lintcode.com/problem/200/
 * 38. LintCode 130. 堆化 - https://www.lintcode.com/problem/130/
 * 39. AcWing 901. 滑雪 - https://www.acwing.com/problem/content/903/
 * 40. AcWing 1482. 进制 - https://www.acwing.com/problem/content/1484/
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_KillMonsterEverySkillUseOnce {

	public static int MAXN = 11;

	public static int[] kill = new int[MAXN];

	public static int[] blood = new int[MAXN];

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			int t = (int) in.nval;
			for (int i = 0; i < t; i++) {
				in.nextToken();
				int n = (int) in.nval;
				in.nextToken();
				int m = (int) in.nval;
				for (int j = 0; j < n; j++) {
					in.nextToken();
					kill[j] = (int) in.nval;
					in.nextToken();
					blood[j] = (int) in.nval;
				}
				int ans = f(n, 0, m);
				out.println(ans == Integer.MAX_VALUE ? -1 : ans);
			}
		}
		out.flush();
		br.close();
		out.close();
	}

	/**
	 * 回溯函数，计算最少需要多少技能能击败怪物
	 * 
	 * @param n 技能总数
	 * @param i 当前考虑使用第几个技能（已使用了i个技能）
	 * @param r 怪物当前剩余血量
	 * @return 最少需要的技能数，如果无法击败则返回Integer.MAX_VALUE
	 * 
	 * 递归思路：
	 * 1. 基础情况：如果怪物血量r<=0，说明已经被击败，返回已使用的技能数i
	 * 2. 基础情况：如果已经考虑完所有技能(i==n)但怪物仍有血量，返回无效值
	 * 3. 递归情况：尝试使用剩余的每个技能，通过交换位置来尝试不同技能
	 * 
	 * 优化点：
	 * 1. 通过交换技能位置来尝试不同顺序，避免创建新数组
	 * 2. 每次递归后恢复交换，实现回溯
	 * 
	 * 工程化考量：
	 * - 使用Integer.MAX_VALUE作为无效值的标记
	 * - 通过参数传递当前状态，避免使用全局变量存储中间结果
	 */
	public static int f(int n, int i, int r) {
		if (r <= 0) {
			// 之前的决策已经让怪兽挂了！返回使用了多少个技能
			return i;
		}
		// r > 0
		if (i == n) {
			// 无效，之前的决策无效
			return Integer.MAX_VALUE;
		}
		// 返回至少需要几个技能可以将怪兽杀死
		int ans = Integer.MAX_VALUE;
		for (int j = i; j < n; j++) {
			swap(i, j);
			// 判断是否触发双倍伤害：如果怪物血量r大于触发阈值blood[i]，则造成kill[i]点伤害
			// 否则造成kill[i] * 2点伤害（双倍）
			ans = Math.min(ans, f(n, i + 1, r - (r > blood[i] ? kill[i] : kill[i] * 2)));
			swap(i, j);
		}
		return ans;
	}

	/**
	 * 交换两个技能的位置
	 * 
	 * @param i 第一个技能索引
	 * @param j 第二个技能索引
	 * 
	 * 通过交换技能位置来尝试不同使用顺序，这是回溯算法的关键部分
	 * 交换后递归调用，调用结束后再交换回来，实现状态恢复（回溯）
	 */
	public static void swap(int i, int j) {
		int tmp = kill[i];
		kill[i] = kill[j];
		kill[j] = tmp;
		tmp = blood[i];
		blood[i] = blood[j];
		blood[j] = tmp;
	}
	
	/**
	 * 补充题目5：HDU 1010 - Tempter of the Bone
	 * 题目描述：给定一个迷宫，判断是否可以在恰好T步到达出口。
	 * 链接：https://acm.hdu.edu.cn/showproblem.php?pid=1010
	 * 
	 * 解题思路：
	 * - 使用深度优先搜索
	 * - 使用奇偶剪枝优化：剩余步数与曼哈顿距离的奇偶性必须相同
	 * - 时间复杂度：O(4^T)，空间复杂度：O(n*m)
	 * 
	 * Java代码实现：
	 * private static boolean found = false;
	 * private static int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
	 * 
	 * public static void solve(char[][] maze, int startX, int startY, int endX, int endY, int T) {
	 *     int n = maze.length;
	 *     int m = maze[0].length;
	 *     boolean[][] visited = new boolean[n][m];
	 *     visited[startX][startY] = true;
	 *     found = false;
	 *     
	 *     // 奇偶剪枝：剩余步数与曼哈顿距离的奇偶性必须相同
	 *     int distance = Math.abs(startX - endX) + Math.abs(startY - endY);
	 *     if (distance > T || (distance - T) % 2 != 0) {
	 *         System.out.println("NO");
	 *         return;
	 *     }
	 *     
	 *     dfs(maze, startX, startY, endX, endY, 0, T, visited);
	 *     System.out.println(found ? "YES" : "NO");
	 * }
	 * 
	 * private static void dfs(char[][] maze, int x, int y, int endX, int endY, int step, int T, boolean[][] visited) {
	 *     if (found) return;
	 *     if (step > T) return;
	 *     if (x == endX && y == endY && step == T) {
	 *         found = true;
	 *         return;
	 *     }
	 *     
	 *     for (int[] dir : dirs) {
	 *         int nx = x + dir[0];
	 *         int ny = y + dir[1];
	 *         if (nx >= 0 && nx < maze.length && ny >= 0 && ny < maze[0].length && 
	 *             maze[nx][ny] != 'X' && !visited[nx][ny]) {
	 *              
	 *             // 预估剩余步数，提前剪枝
	 *             int remainingSteps = T - step - 1;
	 *             int distance = Math.abs(nx - endX) + Math.abs(ny - endY);
	 *             if (distance > remainingSteps || (distance - remainingSteps) % 2 != 0) {
	 *                 continue;
	 *             }
	 *              
	 *             visited[nx][ny] = true;
	 *             dfs(maze, nx, ny, endX, endY, step + 1, T, visited);
	 *             visited[nx][ny] = false;
	 *         }
	 *     }
	 * }
	 */
	
	/**
	 * 补充题目6：LeetCode 51. N 皇后
	 * 题目描述：给定一个整数 n，返回所有不同的 n 皇后问题的解决方案。
	 * n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
	 * 链接：https://leetcode.cn/problems/n-queens/
	 * 
	 * 解题思路：
	 * - 使用回溯算法逐行放置皇后
	 * - 使用位运算优化判断是否可以放置皇后
	 * - 时间复杂度：O(n!)，空间复杂度：O(n)
	 * 
	 * Java代码实现：
	 * public List<List<String>> solveNQueens(int n) {
	 *     List<List<String>> result = new ArrayList<>();
	 *     // 存储每一行皇后的位置
	 *     int[] queens = new int[n];
	 *     Arrays.fill(queens, -1);
	 *     // 标记已被攻击的列、对角线
	 *     boolean[] columns = new boolean[n];
	 *     boolean[] diagonals1 = new boolean[2 * n - 1]; // 左上到右下对角线
	 *     boolean[] diagonals2 = new boolean[2 * n - 1]; // 右上到左下对角线
	 *     
	 *     backtrack(n, 0, queens, columns, diagonals1, diagonals2, result);
	 *     return result;
	 * }
	 * 
	 * private void backtrack(int n, int row, int[] queens, boolean[] columns, 
	 *                        boolean[] diagonals1, boolean[] diagonals2, List<List<String>> result) {
	 *     if (row == n) {
	 *         // 将结果转换为字符串列表
	 *         List<String> board = generateBoard(queens, n);
	 *         result.add(board);
	 *         return;
	 *     }
	 *     
	 *     for (int col = 0; col < n; col++) {
	 *         // 计算对角线索引
	 *         int diag1 = row - col + n - 1;
	 *         int diag2 = row + col;
	 *         
	 *         // 检查是否可以放置皇后
	 *         if (columns[col] || diagonals1[diag1] || diagonals2[diag2]) {
	 *             continue;
	 *         }
	 *         
	 *         // 放置皇后
	 *         queens[row] = col;
	 *         columns[col] = true;
	 *         diagonals1[diag1] = true;
	 *         diagonals2[diag2] = true;
	 *         
	 *         // 递归到下一行
	 *         backtrack(n, row + 1, queens, columns, diagonals1, diagonals2, result);
	 *         
	 *         // 回溯
	 *         queens[row] = -1;
	 *         columns[col] = false;
	 *         diagonals1[diag1] = false;
	 *         diagonals2[diag2] = false;
	 *     }
	 * }
	 * 
	 * private List<String> generateBoard(int[] queens, int n) {
	 *     List<String> board = new ArrayList<>();
	 *     for (int i = 0; i < n; i++) {
	 *         char[] row = new char[n];
	 *         Arrays.fill(row, '.');
	 *         row[queens[i]] = 'Q';
	 *         board.add(new String(row));
	 *     }
	 *     return board;
	 * }
	 */
	
	/**
	 * 补充题目7：LeetCode 37. 解数独
	 * 题目描述：编写一个程序，通过填充空格来解决数独问题。
	 * 链接：https://leetcode.cn/problems/sudoku-solver/
	 * 
	 * 解题思路：
	 * - 使用回溯算法逐个填充空格
	 * - 使用三个二维数组记录行、列、3x3子格中已使用的数字
	 * - 时间复杂度：O(9^m)，其中m为空格数量，空间复杂度：O(n^2)
	 * 
	 * Java代码实现：
	 * public void solveSudoku(char[][] board) {
	 *     // 记录每行、每列、每个3x3子格中已使用的数字
	 *     boolean[][] rows = new boolean[9][9];
	 *     boolean[][] cols = new boolean[9][9];
	 *     boolean[][] boxes = new boolean[9][9];
	 *     
	 *     // 初始化已使用的数字
	 *     for (int i = 0; i < 9; i++) {
	 *         for (int j = 0; j < 9; j++) {
	 *             if (board[i][j] != '.') {
	 *                 int num = board[i][j] - '1';
	 *                 int boxIndex = (i / 3) * 3 + j / 3;
	 *                 rows[i][num] = true;
	 *                 cols[j][num] = true;
	 *                 boxes[boxIndex][num] = true;
	 *             }
	 *         }
	 *     }
	 *     
	 *     solve(board, rows, cols, boxes);
	 * }
	 * 
	 * private boolean solve(char[][] board, boolean[][] rows, boolean[][] cols, boolean[][] boxes) {
	 *     for (int i = 0; i < 9; i++) {
	 *         for (int j = 0; j < 9; j++) {
	 *             if (board[i][j] == '.') {
	 *                 // 尝试填充1-9
	 *                 for (int num = 0; num < 9; num++) {
	 *                     int boxIndex = (i / 3) * 3 + j / 3;
	 *                     
	 *                     // 检查数字是否可用
	 *                     if (!rows[i][num] && !cols[j][num] && !boxes[boxIndex][num]) {
	 *                         // 尝试放置数字
	 *                         board[i][j] = (char) ('1' + num);
	 *                         rows[i][num] = true;
	 *                         cols[j][num] = true;
	 *                         boxes[boxIndex][num] = true;
	 *                         
	 *                         // 递归填充下一个空格
	 *                         if (solve(board, rows, cols, boxes)) {
	 *                             return true;
	 *                         }
	 *                         
	 *                         // 回溯
	 *                         board[i][j] = '.';
	 *                         rows[i][num] = false;
	 *                         cols[j][num] = false;
	 *                         boxes[boxIndex][num] = false;
	 *                     }
	 *                 }
	 *                 // 1-9都不能放置，回溯
	 *                 return false;
	 *             }
	 *         }
	 *     }
	 *     // 所有空格都已填充
	 *     return true;
	 * }
	 */
	
	/**
	 * 补充题目8：LeetCode 47. 全排列 II
	 * 题目描述：给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
	 * 链接：https://leetcode.cn/problems/permutations-ii/
	 * 
	 * 解题思路：
	 * - 使用回溯算法生成所有排列
	 * - 通过排序和剪枝避免重复
	 * - 时间复杂度：O(n!)，空间复杂度：O(n)
	 * 
	 * Java代码实现：
	 * public List<List<Integer>> permuteUnique(int[] nums) {
	 *     List<List<Integer>> result = new ArrayList<>();
	 *     List<Integer> path = new ArrayList<>();
	 *     boolean[] used = new boolean[nums.length];
	 *     
	 *     // 排序以便去重
	 *     Arrays.sort(nums);
	 *     
	 *     backtrack(nums, used, path, result);
	 *     return result;
	 * }
	 * 
	 * private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
	 *     if (path.size() == nums.length) {
	 *         result.add(new ArrayList<>(path));
	 *         return;
	 *     }
	 *     
	 *     for (int i = 0; i < nums.length; i++) {
	 *         // 已经使用过的元素跳过
	 *         if (used[i]) {
	 *             continue;
	 *         }
	 *         
	 *         // 剪枝：跳过重复元素，确保相同元素按顺序使用
	 *         if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
	 *             continue;
	 *         }
	 *         
	 *         // 选择当前元素
	 *         used[i] = true;
	 *         path.add(nums[i]);
	 *         
	 *         // 递归到下一层
	 *         backtrack(nums, used, path, result);
	 *         
	 *         // 回溯
	 *         path.remove(path.size() - 1);
	 *         used[i] = false;
	 *     }
	 * }
	 */
	
	/**
	 * 排列组合与回溯算法优化技巧总结
	 * 
	 * 核心概念与理论基础：
	 * 1. 排列空间：所有可能的元素排列组合构成的状态空间
	 * 2. 回溯搜索：通过递归+剪枝技术探索状态空间，寻找满足条件的解
	 * 3. 状态表示：如何高效表示当前搜索状态，平衡内存使用与访问效率
	 * 4. 剪枝策略：如何识别并提前终止无效搜索路径
	 * 5. 最优子结构：问题的最优解可以通过子问题的最优解构建
	 * 
	 * 算法设计与优化策略：
	 * 1. 递归结构优化：
	 *    - 参数传递优化：使用引用而非值传递，减少拷贝开销
	 *    - 状态压缩：使用位操作或哈希技术紧凑表示状态
	 *    - 递归深度控制：设置合理的递归边界条件
	 * 
	 * 2. 剪枝技术进阶：
	 *    - 可行性剪枝：当前路径无法到达目标时提前返回
	 *    - 最优性剪枝：当前路径不可能优于已知最优解时提前返回
	 *    - 重复性剪枝：识别并跳过等价状态或重复计算
	 *    - 顺序优化：按照一定顺序尝试选择，增加剪枝机会
	 * 
	 * 3. 状态表示技术：
	 *    - 交换法：通过交换数组元素避免创建新数组（如本题）
	 *    - 标记法：使用布尔数组或位掩码标记已使用元素
	 *    - 路径记录：维护当前选择路径，用于验证约束条件
	 * 
	 * 4. 优化算法选择：
	 *    - 暴力枚举：适用于小规模问题，实现简单
	 *    - 回溯+剪枝：适用于中等规模问题，通过剪枝大幅减少搜索空间
	 *    - 分支限界：结合启发式函数的优先搜索，进一步提高效率
	 *    - 动态规划：当问题具有重叠子问题时可考虑
	 * 
	 * 复杂度分析：
	 * 1. 时间复杂度：
	 *    - 理论上限：O(n!)，其中n为元素总数
	 *    - 实际情况：取决于剪枝效果和问题特性
	 *    - 影响因素：约束条件数量、剪枝效率、问题规模
	 * 
	 * 2. 空间复杂度：
	 *    - 递归栈空间：O(n)
	 *    - 状态存储空间：O(n)用于标记数组或交换数组
	 *    - 最优解记录空间：O(n)用于存储最优路径
	 * 
	 * 工程化实现最佳实践：
	 * 1. 代码组织与模块化：
	 *    - 将核心算法、工具函数和测试代码分离
	 *    - 使用清晰的类和方法命名，提高可读性
	 *    - 采用适当的数据结构封装问题状态
	 * 
	 * 2. 性能优化技术：
	 *    - 局部变量优化：减少方法调用和全局变量访问
	 *    - 预计算与缓存：对于重复计算的部分使用缓存
	 *    - 并行化考虑：对于独立子问题可考虑并行求解
	 *    - 避免不必要的对象创建：重用对象和集合
	 * 
	 * 3. 健壮性保障：
	 *    - 输入验证：处理边界情况和异常输入
	 *    - 数值溢出防护：使用适当的数据类型（如long替代int）
	 *    - 死循环检测：确保递归有明确终止条件
	 *    - 资源释放：确保在回溯过程中正确释放资源
	 * 
	 * 调试与测试技术：
	 * 1. 中间状态跟踪：
	 *    - 条件断点：在关键决策点设置断点
	 *    - 状态日志：记录重要中间状态和决策路径
	 *    - 可视化工具：对于复杂问题可考虑使用可视化工具
	 * 
	 * 2. 测试用例设计：
	 *    - 边界测试：空输入、单元素输入、最大规模输入
	 *    - 特殊测试：重复元素、极端数值、特殊约束条件
	 *    - 性能测试：测量算法在不同规模输入下的性能
	 *    - 正确性验证：使用已知解或暴力解法验证结果
	 * 
	 * 跨语言实现比较：
	 * 1. Java实现：
	 *    - 优势：丰富的集合框架，良好的内存管理
	 *    - 注意：递归深度限制，整数溢出问题
	 *    - 优化：使用基本数据类型，避免不必要的装箱/拆箱
	 * 
	 * 2. C++实现：
	 *    - 优势：更高的执行效率，更细粒度的内存控制
	 *    - 注意：手动内存管理，模板元编程
	 *    - 优化：使用引用和移动语义，减少拷贝开销
	 * 
	 * 3. Python实现：
	 *    - 优势：简洁的语法，强大的数据处理能力
	 *    - 注意：递归深度限制，执行效率相对较低
	 *    - 优化：使用lru_cache装饰器，考虑迭代实现
	 * 
	 * 4. Go实现：
	 *    - 优势：并发支持，垃圾回收，编译型性能
	 *    - 注意：错误处理，接口设计
	 *    - 优化：使用切片而非数组，指针传递
	 * 
	 * 与现代技术的融合：
	 * 1. 算法与机器学习结合：
	 *    - 强化学习：通过试错学习最优决策策略
	 *    - 遗传算法：模拟自然选择寻找近似最优解
	 *    - 神经网络：学习问题特征，指导搜索方向
	 * 
	 * 2. 分布式计算应用：
	 *    - 并行搜索：将状态空间划分给多个计算节点
	 *    - 负载均衡：动态调整各节点的搜索任务
	 *    - 结果合并：有效汇总各节点的搜索结果
	 * 
	 * 3. 实际应用场景：
	 *    - 游戏AI：决策路径规划，资源优化分配
	 *    - 调度问题：任务调度，资源分配，路径规划
	 *    - 组合优化：旅行商问题，背包问题，装箱问题
	 *    - 密码学：暴力破解，密钥搜索
	 * 
	 * 进阶研究方向：
	 * 1. 启发式搜索：设计更高效的启发函数，指导搜索方向
	 * 2. 元启发式算法：模拟退火，粒子群优化，蚁群算法等
	 * 3. 约束满足问题：处理复杂约束的组合优化问题
	 * 4. 近似算法：对于NP难问题，设计高效的近似算法
	 * 5. 并行与分布式搜索：利用多核和分布式系统提高搜索效率
	 */

	// ======== 补充训练题目 ========
	
	/**
	 * 补充题目1：LeetCode 46. 全排列
	 * 题目描述：给定一个不含重复数字的数组 nums，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
	 * 链接：https://leetcode.cn/problems/permutations/
	 * 
	 * 解题思路：
	 * - 使用回溯算法生成所有排列
	 * - 通过交换元素避免使用额外空间
	 * - 时间复杂度：O(n!)，空间复杂度：O(n)
	 * 
	 * Java代码实现：
	 * public List<List<Integer>> permute(int[] nums) {
	 *     List<List<Integer>> result = new ArrayList<>();
	 *     backtrack(nums, 0, result);
	 *     return result;
	 * }
	 * 
	 * private void backtrack(int[] nums, int start, List<List<Integer>> result) {
	 *     if (start == nums.length) {
	 *         List<Integer> path = new ArrayList<>();
	 *         for (int num : nums) {
	 *             path.add(num);
	 *         }
	 *         result.add(path);
	 *         return;
	 *     }
	 *     
	 *     for (int i = start; i < nums.length; i++) {
	 *         swap(nums, start, i);
	 *         backtrack(nums, start + 1, result);
	 *         swap(nums, start, i);
	 *     }
	 * }
	 * 
	 * private void swap(int[] nums, int i, int j) {
	 *     int temp = nums[i];
	 *     nums[i] = nums[j];
	 *     nums[j] = temp;
	 * }
	 */
	
	/**
	 * 补充题目2：LeetCode 47. 全排列 II
	 * 题目描述：给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
	 * 链接：https://leetcode.cn/problems/permutations-ii/
	 * 
	 * 解题思路：
	 * - 使用回溯算法生成所有排列
	 * - 通过排序和剪枝避免重复
	 * - 时间复杂度：O(n!)，空间复杂度：O(n)
	 * 
	 * Java代码实现：
	 * public List<List<Integer>> permuteUnique(int[] nums) {
	 *     List<List<Integer>> result = new ArrayList<>();
	 *     Arrays.sort(nums);
	 *     boolean[] used = new boolean[nums.length];
	 *     backtrack(nums, used, new ArrayList<>(), result);
	 *     return result;
	 * }
	 * 
	 * private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
	 *     if (path.size() == nums.length) {
	 *         result.add(new ArrayList<>(path));
	 *         return;
	 *     }
	 *     
	 *     for (int i = 0; i < nums.length; i++) {
	 *         if (used[i]) continue;
	 *         // 剪枝：如果当前元素与前一个元素相同且前一个元素未使用，则跳过
	 *         if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) continue;
	 *         
	 *         used[i] = true;
	 *         path.add(nums[i]);
	 *         backtrack(nums, used, path, result);
	 *         path.remove(path.size() - 1);
	 *         used[i] = false;
	 *     }
	 * }
	 */
	
	/**
	 * 补充题目3：Codeforces 1312C - Make It Good
	 * 题目描述：给定一个数组，找出最长的前缀，使得该前缀可以通过删除一些元素后成为一个好数组。
	 * 好数组的定义是：存在一个位置k，使得从左到右是不递减的，从k到末尾是不递增的。
	 * 链接：https://codeforces.com/problemset/problem/1312/C
	 * 
	 * 解题思路：
	 * - 从后往前找到最长的不递增序列
	 * - 然后找到最长的满足条件的前缀
	 * - 时间复杂度：O(n)，空间复杂度：O(1)
	 * 
	 * Java代码实现：
	 * public int maxGoodPrefix(int[] a) {
	 *     int n = a.length;
	 *     int r = n - 1;
	 *     // 找到最长的不递增后缀
	 *     while (r > 0 && a[r] <= a[r-1]) {
	 *         r--;
	 *     }
	 *     // 如果已经是好数组
	 *     if (r == 0) return n;
	 *     
	 *     int l = 0;
	 *     // 找到最长的不递减前缀，同时确保与后缀兼容
	 *     while (l < r && a[l] <= a[l+1] && a[l] <= a[r]) {
	 *         l++;
	 *     }
	 *     
	 *     return l + 1 + (n - r);
	 * }
	 */
	
	/**
	 * 补充题目4：HackerRank - The Coin Change Problem
	 * 题目描述：给定一个金额和一组硬币面值，计算有多少种方式可以凑出该金额。
	 * 链接：https://www.hackerrank.com/challenges/coin-change/problem
	 * 
	 * 解题思路：
	 * - 使用动态规划或回溯算法
	 * - 注意避免重复计数
	 * - 时间复杂度：O(amount * n)，空间复杂度：O(amount)
	 * 
	 * Java代码实现（动态规划解法）：
	 * public long getWays(int n, List<Long> c) {
	 *     long[] dp = new long[n + 1];
	 *     dp[0] = 1; // 凑出0元有一种方式：不使用任何硬币
	 *     
	 *     for (long coin : c) {
	 *         for (int amount = coin; amount <= n; amount++) {
	 *             dp[amount] += dp[(int)(amount - coin)];
	 *         }
	 *     }
	 *     
	 *     return dp[n];
	 * }
	 */
	
	/**
	 * 补充题目5：HDU 1010 - Tempter of the Bone
	 * 题目描述：给定一个迷宫，判断是否可以在恰好T步到达出口。
	 * 链接：https://acm.hdu.edu.cn/showproblem.php?pid=1010
	 * 
	 * 解题思路：
	 * - 使用深度优先搜索
	 * - 使用奇偶剪枝优化：剩余步数与曼哈顿距离的奇偶性必须相同
	 * - 时间复杂度：O(4^T)，空间复杂度：O(n*m)
	 * 
	 * Java代码实现：
	 * private static boolean found = false;
	 * private static int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
	 * 
	 * public static void solve(char[][] maze, int startX, int startY, int endX, int endY, int T) {
	 *     int n = maze.length;
	 *     int m = maze[0].length;
	 *     boolean[][] visited = new boolean[n][m];
	 *     visited[startX][startY] = true;
	 *     found = false;
	 *     
	 *     // 奇偶剪枝：剩余步数与曼哈顿距离的奇偶性必须相同
	 *     int distance = Math.abs(startX - endX) + Math.abs(startY - endY);
	 *     if (distance > T || (distance - T) % 2 != 0) {
	 *         System.out.println("NO");
	 *         return;
	 *     }
	 *     
	 *     dfs(maze, startX, startY, endX, endY, 0, T, visited);
	 *     System.out.println(found ? "YES" : "NO");
	 * }
	 * 
	 * private static void dfs(char[][] maze, int x, int y, int endX, int endY, int step, int T, boolean[][] visited) {
	 *     if (found) return;
	 *     if (step > T) return;
	 *     if (x == endX && y == endY && step == T) {
	 *         found = true;
	 *         return;
	 *     }
	 *     
	 *     for (int[] dir : dirs) {
	 *         int nx = x + dir[0];
	 *         int ny = y + dir[1];
	 *         if (nx >= 0 && nx < maze.length && ny >= 0 && ny < maze[0].length && 
	 *             maze[nx][ny] != 'X' && !visited[nx][ny]) {
	 *             
	 *             // 预估剩余步数，提前剪枝
	 *             int remainingSteps = T - step - 1;
	 *             int distance = Math.abs(nx - endX) + Math.abs(ny - endY);
	 *             if (distance > remainingSteps || (distance - remainingSteps) % 2 != 0) {
	 *                 continue;
	 *             }
	 *             
	 *             visited[nx][ny] = true;
	 *             dfs(maze, nx, ny, endX, endY, step + 1, T, visited);
	 *             visited[nx][ny] = false;
	 *         }
	 *     }
	 * }
	 */
	
	/**
	 * 回溯算法在技能打怪问题中的应用总结
	 * 
	 * 核心概念：
	 * 1. 状态空间：问题的所有可能状态，包括已使用的技能、怪物剩余血量等
	 * 2. 状态转移：从一个状态到另一个状态的规则，如使用不同技能后的血量变化
	 * 3. 剪枝策略：提前终止无效的搜索路径，减少计算量
	 * 4. 最优子结构：问题的最优解包含子问题的最优解
	 * 
	 * 算法设计：
	 * 1. 递归结构设计：
	 *    - 参数设计：技能总数、已使用技能数、剩余血量
	 *    - 终止条件：怪物血量<=0（成功）或用完所有技能（失败）
	 *    - 递归逻辑：尝试使用每个未使用的技能，更新状态后递归
	 * 
	 * 2. 状态表示与转移：
	 *    - 通过交换数组元素避免创建新数组，节省空间
	 *    - 动态计算伤害值，根据怪物当前血量判断是否触发双倍伤害
	 *    - 回溯时恢复交换，实现状态还原
	 * 
	 * 3. 优化策略：
	 *    - 剪枝：记录当前最优解，当剩余步骤不可能优于最优解时提前返回
	 *    - 排序优化：可以考虑先尝试高伤害技能，增加剪枝效率
	 *    - 记忆化：对于重复状态可以使用备忘录优化，但本题n较小且状态复杂，效果有限
	 * 
	 * 时间复杂度分析：
	 * - 最坏情况：O(n!)，其中n为技能数量
	 * - 实际情况：由于剪枝和提前终止，实际运行时间远小于理论上限
	 * - 对于n=10，10!≈3.6e6，可以在合理时间内完成
	 * 
	 * 空间复杂度分析：
	 * - 递归栈深度：O(n)
	 * - 其他空间：O(n)，用于存储技能信息
	 * - 总空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 输入输出效率：使用BufferedReader和StreamTokenizer提高IO效率
	 * 2. 异常防御：处理极端情况，如怪物血量为0或超过整型范围
	 * 3. 代码可读性：使用清晰的变量名和详细的注释
	 * 4. 可扩展性：通过参数化设计，便于调整问题规模
	 * 5. 性能优化：使用交换操作而非创建新数组，减少内存分配
	 * 
	 * 调试技巧：
	 * 1. 中间状态打印：在递归过程中打印当前使用的技能和剩余血量
	 * 2. 边界测试：测试技能数量为0或1的情况
	 * 3. 性能分析：对于较大输入，可以添加计时器分析性能瓶颈
	 * 4. 断言验证：使用断言验证关键条件，如伤害计算正确性
	 * 
	 * 跨语言实现注意事项：
	 * 1. Java：注意Integer.MAX_VALUE的溢出问题，使用long处理大数值
	 * 2. C++：可以使用引用传递优化参数传递效率
	 * 3. Python：递归深度限制可能导致栈溢出，对于大n需要考虑迭代实现
	 * 
	 * 算法变种与扩展：
	 * 1. 技能可重复使用：需要调整回溯逻辑，不使用交换而是通过索引控制
	 * 2. 技能组合效果：考虑技能之间的相互作用，如连击加成等
	 * 3. 多种怪物类型：为不同类型的怪物设计不同的伤害计算规则
	 * 4. 资源限制：添加魔力值等资源限制，需要在最小技能数和资源消耗之间平衡
	 * 
	 * 与机器学习的联系：
	 * 1. 强化学习：可以将问题建模为马尔可夫决策过程，通过RL算法学习最优策略
	 * 2. 状态空间搜索：类似AlphaGo等AI系统中的状态空间搜索算法
	 * 3. 启发式算法：可以使用遗传算法等启发式方法寻找近似最优解
	 * 4. 蒙特卡洛树搜索：对于大规模问题，可以使用MCTS算法有效探索状态空间
	 */
}