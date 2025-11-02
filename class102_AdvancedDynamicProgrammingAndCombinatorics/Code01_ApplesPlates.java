package class128;

/**
 * 苹果和盘子问题（球盒模型）
 * 问题描述：
 * - 有m个苹果，苹果之间无差别
 * - 有n个盘子，盘子之间无差别
 * - 允许有些盘子为空
 * - 求有多少种不同的放置方法
 * 
 * 例如：5个苹果放进3个盘子，(1, 3, 1) (1, 1, 3) (3, 1, 1)认为是同一种方法
 * 
 * 算法思路：
 * - 这是一个经典的组合数学问题，属于球盒模型（n个相同的球放入m个相同的盒子）
 * - 使用动态规划解决，状态定义为f(m, n)表示m个苹果放入n个盘子的方法数
 * - 状态转移方程：
 *   1. 当n > m时，f(m, n) = f(m, m)（盘子比苹果多时，多余的盘子无意义）
 *   2. 当n <= m时，f(m, n) = f(m, n-1) + f(m-n, n)
 *      - f(m, n-1)：至少有一个盘子为空的情况
 *      - f(m-n, n)：所有盘子都不为空的情况（每个盘子先放一个苹果）
 * 
 * 边界条件：
 * - f(0, n) = 1（0个苹果放入任意多个盘子只有1种方法：都不放）
 * - f(m, 0) = 0（有苹果但没有盘子，无法放置）
 * 
 * 时间复杂度：O(m*n)
 * 空间复杂度：O(m*n)
 * 
 * 测试链接 : https://www.nowcoder.com/practice/bfd8234bb5e84be0b493656e390bdebf
 * 注意：提交时请把类名改成"Main"，可以通过所有用例
 * 
 * 整数分拆的特殊情况：
 * - 该问题等价于将m个苹果分拆成最多n个非递增的正整数之和（加上空盘子）
 * - 与整数分拆的区别是这里不考虑顺序且允许空盘子
 * - 整数分拆在组合数学中有重要应用，涉及到生成函数、递推关系和Partition函数
 * 
 * 输入输出示例：
 * 输入：7 3
 * 输出：8
 * 解释：有7个苹果，3个盘子，放置方法有：
 * (0, 0, 7), (0, 1, 6), (0, 2, 5), (0, 3, 4), (1, 1, 5), (1, 2, 4), (1, 3, 3), (2, 2, 3)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_ApplesPlates {

	public static int MAXM = 11;

	public static int MAXN = 11;

	public static int[][] dp = new int[MAXM][MAXN];

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int m = (int) in.nval;
		in.nextToken();
		int n = (int) in.nval;
		out.println(compute(m, n));
		out.flush();
		out.close();
		br.close();
	}

		/**
	 * 计算m个苹果放入n个盘子的方法数
	 * 
	 * @param m 苹果数量
	 * @param n 盘子数量
	 * @return 放置方法数
	 * 
	 * 时间复杂度：O(m*n)
	 * 空间复杂度：O(m*n)
	 */
	public static int compute(int m, int n) {
		// 初始化动态规划数组，-1表示未计算
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				dp[i][j] = -1;
			}
		}
		return f(m, n);
	}

	/**
	 * 动态规划核心函数，使用记忆化搜索实现
	 * 
	 * @param m 苹果数量
	 * @param n 盘子数量
	 * @return 放置方法数
	 */
	public static int f(int m, int n) {
		// 边界条件1：没有苹果，只有一种方法（都不放）
		if (m == 0) {
			return 1;
		}
		// 边界条件2：没有盘子，无法放置
		if (n == 0) {
			return 0;
		}
		// 记忆化搜索，避免重复计算
		if (dp[m][n] != -1) {
			return dp[m][n];
		}
		
		int ans;
		// 如果盘子数大于苹果数，则多余的盘子无意义
		if (n > m) {
			ans = f(m, m);
		} else {
			// 状态转移方程：
			// f(m, n-1)：至少有一个盘子为空的情况
			// f(m-n, n)：所有盘子都不为空的情况（每个盘子先放一个苹果）
			ans = f(m, n - 1) + f(m - n, n);
		}
		
		// 记录结果
		dp[m][n] = ans;
		return ans;
	}
	
	/**
	 * 类似题目与训练拓展：
	 * 1. LeetCode 343 - Integer Break
	 *    链接：https://leetcode.cn/problems/integer-break/
	 *    区别：将整数拆分为至少两个正整数的和，求乘积的最大值
	 *    算法：动态规划或数学推导
	 * 
	 * 2. LeetCode 279 - Perfect Squares
	 *    链接：https://leetcode.cn/problems/perfect-squares/
	 *    区别：求将整数n表示为完全平方数之和的最少项数
	 *    算法：BFS或动态规划
	 * 
	 * 3. LeetCode 322 - Coin Change
	 *    链接：https://leetcode.cn/problems/coin-change/
	 *    区别：求用最少的硬币数量组成指定金额
	 *    算法：动态规划或BFS
	 * 
	 * 4. LeetCode 518 - Coin Change II
	 *    链接：https://leetcode.cn/problems/coin-change-ii/
	 *    区别：求用不同面额硬币组成指定金额的组合数
	 *    算法：动态规划
	 * 
	 * 5. 牛客网 NC104 - 求正数数组的最小不可组成和
	 *    链接：https://www.nowcoder.com/practice/3350d379a5d44054b219de7af6708894
	 *    区别：求数组中无法组成的最小正整数和
	 *    算法：贪心或动态规划
	 * 
	 * 6. 洛谷 P1025 - 数的划分
	 *    链接：https://www.luogu.com.cn/problem/P1025
	 *    区别：将整数划分为k个正整数的和，顺序不同视为同一种方法
	 *    算法：动态规划
	 * 
	 * 7. HDU 1028 - Ignatius and the Princess III
	 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1028
	 *    区别：整数分拆问题，求分拆方式数
	 *    算法：动态规划
	 * 
	 * 8. 牛客网 NC14138 - 整数分拆
	 *    链接：https://www.nowcoder.com/practice/38b6d26b18bf49bc9fae3a3e2322a471
	 *    区别：将整数分拆成若干个不同的正整数之和
	 *    算法：动态规划
	 */
	
	/**
	 * 算法本质与技巧总结：
	 * 
	 * 1. 整数分拆思想：
	 *    - 本问题是整数分拆的一个特例，不考虑顺序且允许空盘子
	 *    - 整数分拆在组合数学中有重要应用，涉及到生成函数等高级概念
	 * 
	 * 2. 动态规划的状态定义：
	 *    - 状态定义要清晰，能够准确描述子问题
	 *    - 本题的状态f(m, n)定义为m个苹果放入n个盘子的方法数
	 *    - 状态定义的好坏直接影响动态规划的复杂度和实现难度
	 * 
	 * 3. 状态转移方程的推导：
	 *    - 通过将问题分解为互斥且完备的子问题来推导转移方程
	 *    - 本题通过是否允许空盘子将问题分为两种情况
	 *    - 转移方程的推导需要深入理解问题的性质
	 * 
	 * 4. 记忆化搜索的实现：
	 *    - 记忆化搜索是动态规划的递归实现方式
	 *    - 可以避免重复计算子问题，提高效率
	 *    - 在Java中需要注意递归深度的限制
	 * 
	 * 5. 空间优化技巧：
	 *    - 对于某些动态规划问题，可以使用滚动数组优化空间复杂度
	 *    - 本题可以将空间复杂度从O(m*n)优化到O(min(m, n))
	 */
	
	/**
	 * Java工程化实战建议：
	 * 
	 * 1. 输入输出优化：
	 *    - 使用BufferedReader和PrintWriter提高输入输出效率
	 *    - 使用StreamTokenizer处理数值输入，比Scanner更快
	 *    - 对于大规模数据，这种优化尤为重要
	 * 
	 * 2. 内存管理：
	 *    - 预先分配数组大小，避免动态扩容
	 *    - 考虑将动态规划数组作为局部变量，避免使用静态变量
	 *    - 对于大规模输入，可以考虑使用二维列表而不是二维数组
	 * 
	 * 3. 性能优化策略：
	 *    - 对于大规模输入，考虑使用迭代版的动态规划而不是递归
	 *    - 使用预计算的方式处理多个查询
	 *    - 避免在递归中创建不必要的对象
	 * 
	 * 4. 代码健壮性提升：
	 *    - 添加输入参数检查，确保m和n为非负整数
	 *    - 处理可能的边界情况，如m=0或n=0
	 *    - 使用try-catch-finally块确保资源正确关闭
	 *    - 考虑使用try-with-resources自动关闭资源
	 * 
	 * 5. Java特有优化技巧：
	 *    - 使用System.arraycopy进行数组复制，比循环复制更快
	 *    - 合理使用静态变量和实例变量
	 *    - 对于大规模数据，可以考虑使用BigInteger处理大数
	 * 
	 * 6. 调试与问题定位：
	 *    - 添加日志输出来跟踪算法的执行过程
	 *    - 使用断点调试工具分析递归调用栈
	 *    - 考虑添加单元测试验证算法的正确性
	 */

}