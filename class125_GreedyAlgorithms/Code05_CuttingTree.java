package class094;

// 砍树
// 一共有n棵树，每棵树都有两个信息：
// 第一天这棵树的初始重量、这棵树每天的增长重量
// 你每天最多能砍1棵树，砍下这棵树的收益为：
// 这棵树的初始重量 + 这棵树增长到这一天的总增重
// 从第1天开始，你一共有m天可以砍树，返回m天内你获得的最大收益
// 测试链接 : https://pintia.cn/problem-sets/91827364500/exam/problems/type/7?problemSetProblemId=91827367873
// 如果测试链接失效，搜索 "ZOJ Problem Set" 所在网站，找第3211题 "Dream City"
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

/*
 * 题目解析：
 * 这是一个资源分配和调度问题，要求在有限的天数内选择砍伐树木的顺序和数量以最大化收益。
 * 关键在于理解收益计算方式和决策约束：
 * 1. 收益 = 初始重量 + (砍伐天数-1) × 每天增长重量
 * 2. 每天只能砍伐一棵树
 * 3. 需要在m天内完成砍伐
 *
 * 解题思路：
 * 1. 贪心策略：按增长速度排序，增长慢的先砍
 * 2. 动态规划：使用01背包变种解决选择问题
 * 3. 状态设计：dp[i][j]表示前i棵树在j天内的最大收益
 * 4. 状态转移：考虑是否砍伐当前树
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_CuttingTree {

	// 树的个数、天数最大值，不超过的量
	public static int MAXN = 251;

	// 树的编号为1 ~ n
	// tree[i][0] : 第i棵树第一天的初始重量
	// tree[i][1] : 第i棵树每天的增长重量
	public static int[][] tree = new int[MAXN][2];

	// dp[i][j] : 在j天内，从前i棵树中选若干棵树进行砍伐，最大收益是多少
	public static int[][] dp = new int[MAXN][MAXN];

	public static int t, n, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		t = (int) in.nval;
		for (int i = 1; i <= t; i++) {
			in.nextToken();
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			for (int j = 1; j <= n; j++) {
				in.nextToken();
				tree[j][0] = (int) in.nval;
			}
			for (int j = 1; j <= n; j++) {
				in.nextToken();
				tree[j][1] = (int) in.nval;
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	// 讲解073 - 01背包
	/*
	 * 算法思路：
	 * 1. 贪心策略：根据增长速度排序，增长量小的在前，增长量大的在后
	 * 2. 动态规划：01背包问题的变种
	 *    - 状态定义：dp[i][j] 表示在j天内，从前i棵树中选若干棵树进行砍伐，最大收益是多少
	 *    - 状态转移：dp[i][j] = max(dp[i-1][j], dp[i-1][j-1] + tree[i][0] + tree[i][1] * (j-1))
	 *    - 初始条件：dp[0][...] = 0, dp[...][0] = 0
	 *
	 * 时间复杂度：O(n * logn + n * m) - 排序和DP的时间复杂度
	 * 空间复杂度：O(n * m) - DP数组的空间复杂度
	 * 是否最优解：是，这是处理此类问题的最优解法
	 *
	 * 工程化考量：
	 * 1. 异常处理：检查输入是否合法
	 * 2. 边界条件：处理n=0, m=0等特殊情况
	 * 3. 性能优化：使用静态数组避免频繁内存分配
	 * 4. 可读性：清晰的变量命名和注释
	 *
	 * 算法详解：
	 * 1. 贪心排序：按增长速度升序排列，增长慢的优先砍伐
	 * 2. 状态转移：对于每棵树，选择砍或不砍
	 * 3. 收益计算：初始重量 + (砍伐天数-1) × 每天增长重量
	 * 4. 边界处理：无树或无天数时收益为0
	 */
	public static int compute() {
		// 树的初始重量不决定树的顺序，因为任何树砍了，就获得固定的初始量，和砍伐的顺序无关
		// 根据增长速度排序，增长量小的在前，增长量大的在后
		// 认为越靠后的树，越要尽量晚的砍伐，课上的重点内容
		// 使用Arrays.sort对tree数组进行排序，范围是[1, n+1)
		// 比较器(o1, o2) -> o1[1] - o2[1]按增长速度升序排列
		Arrays.sort(tree, 1, n + 1, (o1, o2) -> o1[1] - o2[1]);
		
		// dp[0][...] = 0 : 表示如果没有树，不管过去多少天，收益都是0
		// dp[...][0] = 0 : 表示不管有几棵树，没有时间砍树，收益都是0
		// 动态规划填表过程
		for (int i = 1; i <= n; i++) {
			// 对于前i棵树
			for (int j = 1; j <= m; j++) {
				// 对于j天时间
				// 状态转移方程：
				// dp[i][j] = max(不砍第i棵树, 砍第i棵树)
				// 不砍第i棵树：dp[i-1][j]
				// 砍第i棵树：dp[i-1][j-1] + tree[i][0] + tree[i][1] * (j-1)
				// tree[i][0]：第i棵树的初始重量
				// tree[i][1] * (j-1)：第i棵树在第j天砍伐时的增长重量
				dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] + tree[i][0] + tree[i][1] * (j - 1));
			}
		}
		
		// 返回前n棵树在m天内的最大收益
		return dp[n][m];
	}
	
	/*
	 * 相关题目1: LeetCode 45. 跳跃游戏 II
	 * 题目链接: https://leetcode.cn/problems/jump-game-ii/
	 * 题目描述: 给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。
	 * 每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。
	 * 返回到达 nums[n - 1] 的最小跳跃次数。
	 * 解题思路: 贪心算法，每次选择能跳到最远位置的点
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 贪心策略：在当前跳跃范围内选择能跳到最远位置的点
	 * 2. 边界处理：不需要考虑最后一个位置
	 * 3. 优化提前：当能到达终点时提前结束
	 * 4. 状态维护：维护当前跳跃边界和最远可达位置
	 */
	public static int jump(int[] nums) {
		int n = nums.length;
		// 边界条件：数组长度小于等于1时不需要跳跃
		if (n <= 1) return 0;
		
		int jumps = 0;      // 跳跃次数
		int currentEnd = 0; // 当前跳跃能到达的最远位置
		int farthest = 0;   // 在当前跳跃范围内能到达的最远位置
		
		// 注意：不需要考虑最后一个位置，因为到达最后一个位置就结束了
		for (int i = 0; i < n - 1; i++) {
			// 更新在当前跳跃范围内能到达的最远位置
			// i + nums[i]表示从位置i能跳到的最远位置
			farthest = Math.max(farthest, i + nums[i]);
			
			// 如果到达当前跳跃的边界
			// 说明需要进行下一次跳跃
			if (i == currentEnd) {
				jumps++; // 跳跃次数加1
				currentEnd = farthest; // 更新当前跳跃的边界
				
				// 如果已经能到达最后一个位置，提前结束
				// 优化：避免不必要的计算
				if (currentEnd >= n - 1) {
					break;
				}
			}
		}
		
		return jumps; // 返回最小跳跃次数
	}
	
	/*
	 * 相关题目2: LeetCode 135. 分发糖果
	 * 题目链接: https://leetcode.cn/problems/candy/
	 * 题目描述: n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
	 * 你需要按照以下要求，给这些孩子分发糖果：
	 * 每个孩子至少分配到 1 个糖果。
	 * 相邻两个孩子评分更高的孩子会获得更多的糖果。
	 * 请你给每个孩子分发糖果，计算并返回需要准备的最少糖果数目。
	 * 解题思路: 贪心算法，两次遍历
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理此类问题的最优解法
	 *
	 * 算法详解：
	 * 1. 两次遍历：从左到右和从右到左
	 * 2. 左遍历：确保右评分高孩子获得更多糖果
	 * 3. 右遍历：确保左评分高孩子获得更多糖果
	 * 4. 结果合并：取两次遍历结果的最大值
	 */
	public static int candy(int[] ratings) {
		int n = ratings.length;
		// 边界条件：没有孩子时不需要糖果
		if (n == 0) return 0;
		
		// 从左到右遍历，确保右边评分高的孩子比左边的获得更多糖果
		int[] left = new int[n];
		// 初始化每个孩子至少1个糖果
		Arrays.fill(left, 1);
		for (int i = 1; i < n; i++) {
			// 如果当前孩子评分高于左边孩子
			if (ratings[i] > ratings[i - 1]) {
				// 当前孩子糖果数 = 左边孩子糖果数 + 1
				left[i] = left[i - 1] + 1;
			}
		}
		
		// 从右到左遍历，确保左边评分高的孩子比右边的获得更多糖果
		int[] right = new int[n];
		// 初始化每个孩子至少1个糖果
		Arrays.fill(right, 1);
		for (int i = n - 2; i >= 0; i--) {
			// 如果当前孩子评分高于右边孩子
			if (ratings[i] > ratings[i + 1]) {
				// 当前孩子糖果数 = 右边孩子糖果数 + 1
				right[i] = right[i + 1] + 1;
			}
		}
		
		// 取两次遍历结果的最大值，确保同时满足两个方向的约束
		int total = 0;
		for (int i = 0; i < n; i++) {
			// 每个孩子获得的糖果数取left和right中的最大值
			total += Math.max(left[i], right[i]);
		}
		
		return total; // 返回需要准备的最少糖果数目
	}
}