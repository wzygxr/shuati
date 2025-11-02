package class074;

// 完全背包(模版)
// 给定一个正数t，表示背包的容量
// 有m种货物，每种货物可以选择任意个
// 每种货物都有体积costs[i]和价值values[i]
// 返回在不超过总容量的情况下，怎么挑选货物能达到价值最大
// 返回最大的价值
// 测试链接 : https://www.luogu.com.cn/problem/P1616
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的所有代码，并把主类名改成"Main"，可以直接通过

/*
 * 算法详解：
 * 完全背包问题是背包问题的一种变体，与01背包不同的是，每种物品可以选择无限个。
 * 解决完全背包问题的经典方法是动态规划。
 * 
 * 解题思路：
 * 1. 状态定义：dp[j]表示背包容量为j时能获得的最大价值
 * 2. 状态转移方程：dp[j] = max(dp[j], dp[j-cost[i]] + value[i])
 *    与01背包的区别在于，完全背包在更新状态时使用的是dp[j-cost[i]]而不是dp[i-1][j-cost[i]]
 *    这是因为每种物品可以选择多次，所以在同一轮中可以多次使用同一个物品
 * 3. 遍历顺序：外层遍历物品，内层正序遍历背包容量
 * 
 * 时间复杂度分析：
 * 设有m种物品，背包容量为t
 * 1. 动态规划计算：O(m * t)
 * 总时间复杂度：O(m * t)
 * 
 * 空间复杂度分析：
 * 1. 一维DP数组：O(t)
 * 
 * 相关题目扩展：
 * 1. 洛谷 P1616 疯狂的采药（本题）
 * 2. LeetCode 322. 零钱兑换
 * 3. LeetCode 518. 零钱兑换 II
 * 4. LeetCode 279. 完全平方数
 * 5. LeetCode 377. 组合总和 Ⅳ（注意：这不是完全背包，是排列问题）
 * 6. 洛谷 P1466 集合 subset sums
 * 7. 洛谷 P1198 洛谷校门外的树（完全背包思想）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空输入、非法输入等边界情况
 * 3. 可配置性：可以将MAXM和MAXT作为配置参数传入
 * 4. 单元测试：为compute1和compute2方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用位运算优化
 * 
 * 语言特性差异：
 * 1. Java：使用静态数组提高访问速度，StreamTokenizer优化输入
 * 2. C++：可以使用vector，但要注意内存分配开销
 * 3. Python：列表推导式简洁但性能较低，可使用numpy优化
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从二维dp优化到一维dp
 * 2. 遍历顺序优化：内层循环从cost[i]开始，避免不必要的判断
 * 3. 数据类型优化：使用long避免整数溢出
 * 
 * 与01背包的区别：
 * 1. 物品选择次数：01背包每种物品只能选择1次，完全背包可以选择无限次
 * 2. 状态转移方程：01背包使用dp[i-1][j-cost[i]]，完全背包使用dp[j-cost[i]]
 * 3. 遍历顺序：01背包内层逆序遍历，完全背包内层正序遍历
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code03_UnboundedKnapsack {

	public static int MAXM = 10001;

	public static int MAXT = 10000001;

	public static int[] cost = new int[MAXM];

	public static int[] val = new int[MAXM];

	public static long[] dp = new long[MAXT];

	public static int t, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			t = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			for (int i = 1; i <= m; i++) {
				in.nextToken();
				cost[i] = (int) in.nval;
				in.nextToken();
				val[i] = (int) in.nval;
			}
			out.println(compute2());
		}
		out.flush();
		out.close();
		br.close();
	}

	// 严格位置依赖的动态规划
	// 会空间不够，导致无法通过全部测试用例
	public static long compute1() {
		// dp[0][.....] = 0
		int[][] dp = new int[m + 1][t + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 0; j <= t; j++) {
				dp[i][j] = dp[i - 1][j];
				if (j - cost[i] >= 0) {
					dp[i][j] = Math.max(dp[i][j], dp[i][j - cost[i]] + val[i]);
				}
			}
		}
		return dp[m][t];
	}

	// 空间压缩
	// 可以通过全部测试用例
	public static long compute2() {
		Arrays.fill(dp, 1, t + 1, 0);
		for (int i = 1; i <= m; i++) {
			for (int j = cost[i]; j <= t; j++) {
				dp[j] = Math.max(dp[j], dp[j - cost[i]] + val[i]);
			}
		}
		return dp[t];
	}

}

/*
 * =============================================================================================
 * 补充题目1：LeetCode 322. 零钱兑换
 * 题目链接：https://leetcode.cn/problems/coin-change/
 * 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
 * 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
 * 你可以认为每种硬币的数量是无限的。
 * 
 * 解题思路：
 * 这是一个典型的完全背包问题的变种，我们需要找到最小数量的硬币，使得它们的总金额等于amount。
 * 
 * 状态定义：dp[j]表示凑成总金额j所需的最少硬币个数
 * 状态转移方程：dp[j] = min(dp[j], dp[j-coin] + 1)，其中coin是当前硬币的面额
 * 初始条件：dp[0] = 0（凑成总金额0不需要硬币），其他位置初始化为一个较大值（如amount+1）
 * 
 * 时间复杂度：O(coins.length * amount)
 * 空间复杂度：O(amount)
 * 
 * Java实现：
 * public int coinChange(int[] coins, int amount) {
 *     // 初始化dp数组，amount+1表示无法达到的状态
 *     int[] dp = new int[amount + 1];
 *     Arrays.fill(dp, amount + 1);
 *     dp[0] = 0; // 基础情况
 *     
 *     // 完全背包问题解法：每个硬币可以选多次
 *     for (int coin : coins) {
 *         // 正序遍历，允许重复选择
 *         for (int j = coin; j <= amount; j++) {
 *             dp[j] = Math.min(dp[j], dp[j - coin] + 1);
 *         }
 *     }
 *     
 *     // 如果dp[amount]仍然是初始值，说明无法凑出
 *     return dp[amount] > amount ? -1 : dp[amount];
 * }
 * 
 * C++实现：
 * int coinChange(vector<int>& coins, int amount) {
 *     // 初始化dp数组，INT_MAX - 1避免溢出
 *     vector<int> dp(amount + 1, INT_MAX - 1);
 *     dp[0] = 0;
 *     
 *     for (int coin : coins) {
 *         for (int j = coin; j <= amount; j++) {
 *             dp[j] = min(dp[j], dp[j - coin] + 1);
 *         }
 *     }
 *     
 *     return dp[amount] == INT_MAX - 1 ? -1 : dp[amount];
 * }
 * 
 * Python实现：
 * def coinChange(coins, amount):
 *     # 初始化dp数组，amount+1表示无法达到的状态
 *     dp = [amount + 1] * (amount + 1)
 *     dp[0] = 0
 *     
 *     for coin in coins:
 *         for j in range(coin, amount + 1):
 *             dp[j] = min(dp[j], dp[j - coin] + 1)
 *     
 *     return dp[amount] if dp[amount] != amount + 1 else -1
 * 
 * 工程化考量：
 * 1. 边界检查：检查coins数组是否为空，amount是否为负数
 * 2. 异常处理：处理无法凑出总金额的情况
 * 3. 性能优化：当硬币面额大于当前金额时可以跳过
 * 4. 数据结构选择：一维数组足够，空间复杂度为O(amount)
 * 
 * 优化思路：
 * 1. 硬币排序：可以先对硬币进行排序，当硬币面额超过剩余金额时提前终止内层循环
 * 2. BFS优化：对于求最小值问题，可以使用BFS更高效地找到解
 * 3. 剪枝：如果当前硬币的面额大于剩余金额，跳过当前硬币
 * 
 * =============================================================================================
 * 补充题目2：LeetCode 518. 零钱兑换 II
 * 题目链接：https://leetcode.cn/problems/coin-change-ii/
 * 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
 * 计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
 * 你可以认为每种硬币的数量是无限的。
 * 
 * 解题思路：
 * 这是一个典型的完全背包问题，我们需要计算凑成总金额的不同组合数。
 * 注意：这里的组合是不考虑顺序的，例如1+2和2+1视为同一种组合。
 * 
 * 状态定义：dp[j]表示凑成总金额j的不同组合数
 * 状态转移方程：dp[j] += dp[j-coin]，其中coin是当前硬币的面额
 * 初始条件：dp[0] = 1（凑成总金额0只有一种方式，即不选任何硬币）
 * 
 * 时间复杂度：O(coins.length * amount)
 * 空间复杂度：O(amount)
 * 
 * Java实现：
 * public int change(int amount, int[] coins) {
 *     int[] dp = new int[amount + 1];
 *     dp[0] = 1; // 基础情况
 *     
 *     // 外层遍历硬币，内层正序遍历金额（这样可以确保每个硬币按顺序使用，避免重复计算顺序不同的组合）
 *     for (int coin : coins) {
 *         for (int j = coin; j <= amount; j++) {
 *             dp[j] += dp[j - coin];
 *         }
 *     }
 *     
 *     return dp[amount];
 * }
 * 
 * C++实现：
 * int change(int amount, vector<int>& coins) {
 *     vector<int> dp(amount + 1, 0);
 *     dp[0] = 1;
 *     
 *     for (int coin : coins) {
 *         for (int j = coin; j <= amount; j++) {
 *             dp[j] += dp[j - coin];
 *         }
 *     }
 *     
 *     return dp[amount];
 * }
 * 
 * Python实现：
 * def change(amount, coins):
 *     dp = [0] * (amount + 1)
 *     dp[0] = 1
 *     
 *     for coin in coins:
 *         for j in range(coin, amount + 1):
 *             dp[j] += dp[j - coin]
 *     
 *     return dp[amount]
 * 
 * 工程化考量：
 * 1. 边界检查：处理amount为0的特殊情况
 * 2. 数值溢出：在Java中，对于大数据量可能需要使用long类型
 * 3. 内存优化：一维数组足够，空间复杂度为O(amount)
 * 4. 性能优化：当硬币面额大于当前金额时可以跳过
 * 
 * 优化思路：
 * 1. 前缀和优化：对于某些特殊情况，可以使用前缀和进一步优化计算
 * 2. 同余最短路：当amount很大但硬币面额较小时，可以使用同余最短路算法
 * 3. 记忆化搜索：也可以使用记忆化搜索的方法实现，但空间效率较低
 * 
 * 与排列问题的区别：
 * 如果我们想计算排列数（即1+2和2+1视为不同的情况），需要将遍历顺序调换：
 * 内层遍历硬币，外层遍历金额，如LeetCode 377. 组合总和 IV。
 */