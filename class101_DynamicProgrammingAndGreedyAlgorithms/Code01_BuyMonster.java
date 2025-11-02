package class087;

/**
 * 贿赂怪兽问题解决方案
 * 
 * 问题描述:
 * 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的n只怪兽
 * 如果你当前的能力小于i号怪兽的能力，则必须付出b[i]的钱贿赂这个怪兽
 * 然后怪兽就会加入你，他的能力a[i]直接累加到你的能力上
 * 如果你当前的能力大于等于i号怪兽的能力，你可以选择直接通过，且能力不会下降
 * 但你依然可以选择贿赂这个怪兽，然后怪兽的能力直接累加到你的能力上
 * 返回通过所有的怪兽，需要花的最小钱数
 * 
 * 解题思路:
 * 本题提供两种动态规划解法，根据数据特征选择最优算法：
 * 1. 基于金钱数的DP：适用于贿赂金额范围较小的情况
 * 2. 基于能力值的DP：适用于怪兽能力值范围较小的情况
 * 
 * 测试链接: https://www.nowcoder.com/practice/736e12861f9746ab8ae064d4aae2d5a9
 * 
 * 工程化考量:
 * - 使用高效的输入输出处理方式
 * - 提供多种解法以适应不同数据特征
 * - 包含空间优化版本
 * - 提供类似题目的扩展实现
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_BuyMonster {

	/**
	 * 主函数 - 程序入口点
	 * 
	 * 算法设计思考:
	 * 本题的核心在于根据不同数据特征选择最优算法：
	 * 1. 当怪兽能力值a[i]范围很大但贿赂金额b[i]范围不大时，使用基于金钱数的DP
	 * 2. 当贿赂金额b[i]范围很大但怪兽能力值a[i]范围不大时，使用基于能力值的DP
	 * 
	 * 输入输出处理:
	 * 使用BufferedReader和StreamTokenizer提高输入效率
	 * 使用PrintWriter提高输出效率
	 * 
	 * 工程化考量:
	 * - 处理多组测试用例
	 * - 资源释放和异常处理
	 */
	public static void main(String[] args) throws IOException {
		// 高效输入输出流初始化
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 处理多组测试用例
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			int n = (int) in.nval;
			int[] a = new int[n + 1];
			int[] b = new int[n + 1];
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				a[i] = (int) in.nval;
				in.nextToken();
				b[i] = (int) in.nval;
			}
			out.println(compute1(n, a, b));
		}
		
		// 资源释放
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 方法1: 基于金钱数的动态规划解法
	 * 
	 * 适用场景: 当贿赂金额b[i]数值范围相对较小时使用
	 * 算法思路: 以花费的金钱数作为状态，计算能获得的最大能力值
	 * 
	 * 状态定义: dp[i][j]表示花费最多j的钱，通过前i个怪兽能获得的最大能力值
	 * 状态转移:
	 * 1. 不贿赂当前怪兽: 如果dp[i-1][j] >= a[i]，则dp[i][j] = dp[i-1][j]
	 * 2. 贿赂当前怪兽: 如果j >= b[i]且dp[i-1][j-b[i]] != Integer.MIN_VALUE，则dp[i][j] = max(dp[i][j], dp[i-1][j-b[i]] + a[i])
	 * 
	 * 时间复杂度: O(n * 所有怪兽的钱数累加和)
	 * 空间复杂度: O(n * 所有怪兽的钱数累加和)
	 * 
	 * @param n 怪兽数量
	 * @param a 怪兽能力值数组，a[0]为哨兵
	 * @param b 贿赂金额数组，b[0]为哨兵
	 * @return 通过所有怪兽所需的最小金钱数，如果无法通过返回-1
	 */
	public static int compute1(int n, int[] a, int[] b) {
		// 计算所有贿赂金额的总和，作为DP状态的上界
		int m = 0;
		for (int money : b) {
			m += money;
		}
		
		// dp[i][j] : 花的钱不能超过j，通过前i个怪兽，最大能力是多少
		// 如果dp[i][j] == Integer.MIN_VALUE，表示无法通过
		int[][] dp = new int[n + 1][m + 1];
		
		// 状态转移计算
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				dp[i][j] = Integer.MIN_VALUE;
				
				// 情况1: 不贿赂当前怪兽（需要当前能力足够）
				if (dp[i - 1][j] >= a[i]) {
					dp[i][j] = dp[i - 1][j];
				}
				
				// 情况2: 贿赂当前怪兽（需要金钱足够且前i-1个怪兽能通过）
				if (j - b[i] >= 0 && dp[i - 1][j - b[i]] != Integer.MIN_VALUE) {
					dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - b[i]] + a[i]);
				}
			}
		}
		
		// 找到能通过所有怪兽的最小花费
		int ans = -1;
		for (int j = 0; j <= m; j++) {
			if (dp[n][j] != Integer.MIN_VALUE) {
				ans = j;
				break;
			}
		}
		return ans;
	}

	/**
	 * 方法2: 基于金钱数的动态规划解法（空间优化版本）
	 * 
	 * 适用场景: 当贿赂金额b[i]数值范围相对较小时使用，空间优化版本
	 * 算法思路: 使用滚动数组优化空间复杂度，其他逻辑与compute1相同
	 * 
	 * 状态定义: dp[j]表示花费最多j的钱，能获得的最大能力值
	 * 状态转移: 与compute1相同，但使用一维数组
	 * 
	 * 时间复杂度: O(n * 所有怪兽的钱数累加和)
	 * 空间复杂度: O(所有怪兽的钱数累加和)
	 * 
	 * @param n 怪兽数量
	 * @param a 怪兽能力值数组
	 * @param b 贿赂金额数组
	 * @return 通过所有怪兽所需的最小金钱数，如果无法通过返回-1
	 */
	public static int compute2(int n, int[] a, int[] b) {
		// 计算所有贿赂金额的总和，作为DP状态的上界
		int m = 0;
		for (int money : b) {
			m += money;
		}
		
		// 使用一维数组进行空间优化
		int[] dp = new int[m + 1];
		
		// 状态转移计算（从后往前遍历避免状态覆盖）
		for (int i = 1, cur; i <= n; i++) {
			for (int j = m; j >= 0; j--) {
				cur = Integer.MIN_VALUE;
				
				// 情况1: 不贿赂当前怪兽
				if (dp[j] >= a[i]) {
					cur = dp[j];
				}
				
				// 情况2: 贿赂当前怪兽
				if (j - b[i] >= 0 && dp[j - b[i]] != Integer.MIN_VALUE) {
					cur = Math.max(cur, dp[j - b[i]] + a[i]);
				}
				
				dp[j] = cur;
			}
		}
		
		// 找到最小花费
		int ans = -1;
		for (int j = 0; j <= m; j++) {
			if (dp[j] != Integer.MIN_VALUE) {
				ans = j;
				break;
			}
		}
		return ans;
	}

	/**
	 * 方法3: 基于能力值的动态规划解法
	 * 
	 * 适用场景: 当怪兽能力值a[i]数值范围相对较小时使用
	 * 算法思路: 以能力值作为状态，计算需要的最少金钱数
	 * 
	 * 状态定义: dp[i][j]表示能力正好是j，并且确保能通过前i个怪兽，需要至少花多少钱
	 * 状态转移:
	 * 1. 不贿赂当前怪兽: 如果j >= a[i]且dp[i-1][j] != Integer.MAX_VALUE，则dp[i][j] = dp[i-1][j]
	 * 2. 贿赂当前怪兽: 如果j >= a[i]且dp[i-1][j-a[i]] != Integer.MAX_VALUE，则dp[i][j] = min(dp[i][j], dp[i-1][j-a[i]] + b[i])
	 * 
	 * 时间复杂度: O(n * 所有怪兽的能力累加和)
	 * 空间复杂度: O(n * 所有怪兽的能力累加和)
	 * 
	 * @param n 怪兽数量
	 * @param a 怪兽能力值数组
	 * @param b 贿赂金额数组
	 * @return 通过所有怪兽所需的最小金钱数，如果无法通过返回-1
	 */
	public static int compute3(int n, int[] a, int[] b) {
		// 计算所有怪兽能力值的总和，作为DP状态的上界
		int m = 0;
		for (int ability : a) {
			m += ability;
		}
		
		// dp[i][j] : 能力正好是j，并且确保能通过前i个怪兽，需要至少花多少钱
		// 如果dp[i][j] == Integer.MAX_VALUE，表示无法达到该能力值
		int[][] dp = new int[n + 1][m + 1];
		
		// 初始化边界条件
		for (int j = 1; j <= m; j++) {
			dp[0][j] = Integer.MAX_VALUE;
		}
		
		// 状态转移计算
		for (int i = 1; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				dp[i][j] = Integer.MAX_VALUE;
				
				// 情况1: 不贿赂当前怪兽（需要能力足够且前i-1个怪兽能通过）
				if (j >= a[i] && dp[i - 1][j] != Integer.MAX_VALUE) {
					dp[i][j] = dp[i - 1][j];
				}
				
				// 情况2: 贿赂当前怪兽（需要能力足够且前i-1个怪兽能通过）
				if (j - a[i] >= 0 && dp[i - 1][j - a[i]] != Integer.MAX_VALUE) {
					dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - a[i]] + b[i]);
				}
			}
		}
		
		// 找到通过所有怪兽的最小花费
		int ans = Integer.MAX_VALUE;
		for (int j = 0; j <= m; j++) {
			ans = Math.min(ans, dp[n][j]);
		}
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	/**
	 * 方法4: 基于能力值的动态规划解法（空间优化版本）
	 * 
	 * 适用场景: 当怪兽能力值a[i]数值范围相对较小时使用，空间优化版本
	 * 算法思路: 使用滚动数组优化空间复杂度，其他逻辑与compute3相同
	 * 
	 * 状态定义: dp[j]表示能力正好是j时，需要的最少金钱数
	 * 状态转移: 与compute3相同，但使用一维数组
	 * 
	 * 时间复杂度: O(n * 所有怪兽的能力累加和)
	 * 空间复杂度: O(所有怪兽的能力累加和)
	 * 
	 * @param n 怪兽数量
	 * @param a 怪兽能力值数组
	 * @param b 贿赂金额数组
	 * @return 通过所有怪兽所需的最小金钱数，如果无法通过返回-1
	 */
	public static int compute4(int n, int[] a, int[] b) {
		// 计算所有怪兽能力值的总和，作为DP状态的上界
		int m = 0;
		for (int ability : a) {
			m += ability;
		}
		
		// 使用一维数组进行空间优化
		int[] dp = new int[m + 1];
		
		// 初始化边界条件
		for (int j = 1; j <= m; j++) {
			dp[j] = Integer.MAX_VALUE;
		}
		
		// 状态转移计算（从后往前遍历避免状态覆盖）
		for (int i = 1, cur; i <= n; i++) {
			for (int j = m; j >= 0; j--) {
				cur = Integer.MAX_VALUE;
				
				// 情况1: 不贿赂当前怪兽
				if (j >= a[i] && dp[j] != Integer.MAX_VALUE) {
					cur = dp[j];
				}
				
				// 情况2: 贿赂当前怪兽
				if (j - a[i] >= 0 && dp[j - a[i]] != Integer.MAX_VALUE) {
					cur = Math.min(cur, dp[j - a[i]] + b[i]);
				}
				
				dp[j] = cur;
			}
		}
		
		// 找到最小花费
		int ans = Integer.MAX_VALUE;
		for (int j = 0; j <= m; j++) {
			ans = Math.min(ans, dp[j]);
		}
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}
	
	/**
	 * 类似题目1：花最少的钱通过所有的怪兽（腾讯面试题）
	 * 
	 * 题目描述：
	 * 给定两个数组：
	 * int[] d，d[i]表示i号怪兽的能力值
	 * int[] p，p[i]表示贿赂i号怪兽需要的钱数
	 * 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的n只怪兽
	 * 如果你当前的能力小于i号怪兽的能力，则必须付出p[i]的钱贿赂这个怪兽
	 * 然后怪兽就会加入你，他的能力d[i]直接累加到你的能力上
	 * 如果你当前的能力大于等于i号怪兽的能力，你可以选择直接通过，且能力不会下降
	 * 但你依然可以选择贿赂这个怪兽，然后怪兽的能力直接累加到你的能力上
	 * 返回通过所有的怪兽，需要花的最小钱数
	 * 
	 * 示例：
	 * d = {5, 3, 1, 1, 1, 8}
	 * p = {2, 1, 2, 2, 2, 30}
	 * 返回：3 (只需要贿赂前两个就够了)
	 * 
	 * 解题思路：
	 * 这个问题与贿赂怪兽问题完全相同，只是变量名不同。
	 * 我们可以使用动态规划来解决。
	 * 
	 * 方法一：基于金钱数的动态规划
	 * dp[i][j] 表示花费最多j的钱，处理前i个怪兽时能获得的最大能力值
	 * 
	 * 方法二：基于能力值的动态规划
	 * dp[i][j] 表示处理前i个怪兽，当前能力值为j时，所需的最少钱数
	 */
	
	/**
	 * 花最少的钱通过所有的怪兽 - 解法一：基于金钱数的动态规划
	 * 
	 * 算法思路: 以花费的金钱数作为状态，计算能获得的最大能力值
	 * 状态定义: dp[i][j]表示花费最多j的钱，处理前i个怪兽时能获得的最大能力值
	 * 状态转移:
	 * 1. 不贿赂当前怪兽: 如果dp[i-1][j] >= d[i-1]，则dp[i][j] = max(dp[i][j], dp[i-1][j])
	 * 2. 贿赂当前怪兽: 如果j >= p[i-1]，则dp[i][j] = max(dp[i][j], dp[i-1][j-p[i-1]] + d[i-1])
	 * 
	 * 时间复杂度: O(n * sum(p))
	 * 空间复杂度: O(n * sum(p))
	 * 
	 * @param d 怪兽能力值数组
	 * @param p 贿赂金额数组
	 * @return 通过所有怪兽所需的最小金钱数
	 */
	public static long minMoneyToPassMonsters1(int[] d, int[] p) {
		// 计算所有贿赂金额的总和，作为DP状态的上界
		int sum = 0;
		for (int money : p) {
			sum += money;
		}
		
		// dp[i][j] 表示花费最多j的钱，处理前i个怪兽时能获得的最大能力值
		long[][] dp = new long[d.length + 1][sum + 1];
		
		// 初始化：不花钱不获得能力
		for (int j = 0; j <= sum; j++) {
			dp[0][j] = 0;
		}
		
		// 状态转移计算
		for (int i = 1; i <= d.length; i++) {
			for (int j = 0; j <= sum; j++) {
				// 不贿赂当前怪兽（如果能力足够）
				if (dp[i-1][j] >= d[i-1]) {
					dp[i][j] = Math.max(dp[i][j], dp[i-1][j]);
				}
				
				// 贿赂当前怪兽（如果有足够钱）
				if (j >= p[i-1]) {
					dp[i][j] = Math.max(dp[i][j], dp[i-1][j - p[i-1]] + d[i-1]);
				}
			}
		}
		
		// 找到能通过所有怪兽的最少钱数
		for (int j = 0; j <= sum; j++) {
			if (dp[d.length][j] != 0) {
				return j;
			}
		}
		
		return sum;
	}
	
	/**
	 * 花最少的钱通过所有的怪兽 - 解法二：基于能力值的动态规划
	 * 
	 * 算法思路: 以能力值作为状态，计算需要的最少金钱数
	 * 状态定义: dp[i][j]表示处理前i个怪兽，当前能力值为j时，所需的最少钱数
	 * 状态转移:
	 * 1. 不贿赂当前怪兽: 如果j >= d[i-1]且dp[i-1][j] != Long.MAX_VALUE/2，则dp[i][j] = min(dp[i][j], dp[i-1][j])
	 * 2. 贿赂当前怪兽: 如果j >= d[i-1]且dp[i-1][j-d[i-1]] != Long.MAX_VALUE/2，则dp[i][j] = min(dp[i][j], dp[i-1][j-d[i-1]] + p[i-1])
	 * 
	 * 时间复杂度: O(n * sum(d))
	 * 空间复杂度: O(n * sum(d))
	 * 
	 * @param d 怪兽能力值数组
	 * @param p 贿赂金额数组
	 * @return 通过所有怪兽所需的最小金钱数，如果无法通过返回-1
	 */
	public static long minMoneyToPassMonsters2(int[] d, int[] p) {
		// 计算所有怪兽能力值的总和，作为DP状态的上界
		int sum = 0;
		for (int ability : d) {
			sum += ability;
		}
		
		// dp[i][j] 表示处理前i个怪兽，当前能力值为j时，所需的最少钱数
		// 使用Long.MAX_VALUE / 2避免溢出
		long[][] dp = new long[d.length + 1][sum + 1];
		
		// 初始化：所有状态初始化为无穷大
		for (int i = 0; i <= d.length; i++) {
			Arrays.fill(dp[i], Long.MAX_VALUE / 2);
		}
		// 初始状态：处理0个怪兽，能力值为0，需要0钱
		dp[0][0] = 0;
		
		// 状态转移计算
		for (int i = 1; i <= d.length; i++) {
			for (int j = 0; j <= sum; j++) {
				// 不贿赂当前怪兽（如果能力足够）
				if (j >= d[i-1] && dp[i-1][j] != Long.MAX_VALUE / 2) {
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j]);
				}
				
				// 贿赂当前怪兽（如果能力值可达）
				if (j >= d[i-1] && dp[i-1][j - d[i-1]] != Long.MAX_VALUE / 2) {
					dp[i][j] = Math.min(dp[i][j], dp[i-1][j - d[i-1]] + p[i-1]);
				}
			}
		}
		
		// 找到通过所有怪兽的最少钱数
		long result = Long.MAX_VALUE / 2;
		for (int j = 0; j <= sum; j++) {
			result = Math.min(result, dp[d.length][j]);
		}
		
		return result == Long.MAX_VALUE / 2 ? -1 : result;
	}
	
	/**
	 * 类似题目2：Bribe the Prisoners（Google Code Jam 2009, Round 1C C）
	 * 
	 * 题目描述：
	 * 有连续编号为1到n的牢房，每个牢房最初住着一个犯人。
	 * 你需要释放m个犯人，给出释放犯人的编号序列。
	 * 当释放犯人k时，需要贿赂犯人k两边的犯人，直到遇见空牢房或者边界。
	 * 求最小的贿赂金币数。
	 * 
	 * 示例：
	 * n = 8, m = 1, 释放犯人3
	 * 犯人1,2需要贿赂（2个金币），犯人4,5,6,7,8需要贿赂（5个金币）
	 * 总共需要7个金币
	 * 
	 * 解题思路：
	 * 这是一个区间动态规划问题。
	 * dp[i][j] 表示释放编号在i到j之间的所有需要释放的犯人所需的最少金币数
	 * 状态转移方程：
	 * dp[i][j] = min{dp[i][k-1] + dp[k+1][j] + (a[j+1] - a[i-1] - 2)} for k in i..j
	 * 其中a数组是需要释放的犯人编号，加上哨兵a[0]=0和a[m+1]=n+1
	 */
	
	/**
	 * Bribe the Prisoners 解法
	 * 
	 * 算法思路: 区间动态规划，枚举区间内最后一个释放的犯人
	 * 状态定义: dp[i][j]表示释放编号在a[i]到a[j]之间的所有需要释放的犯人所需的最少金币数
	 * 状态转移: dp[i][j] = min{dp[i][k-1] + dp[k+1][j] + (a[j+1] - a[i-1] - 2)} for k in i+1..j-1
	 * 
	 * 时间复杂度: O(m³)
	 * 空间复杂度: O(m²)
	 * 
	 * @param n 牢房总数
	 * @param prisoners 需要释放的犯人编号数组
	 * @return 最小贿赂金币数
	 */
	public static int bribePrisoners(int n, int[] prisoners) {
		int m = prisoners.length;
		// 添加哨兵节点，a[0]=0, a[m+1]=n+1
		int[] a = new int[m + 2];
		a[0] = 0;
		for (int i = 0; i < m; i++) {
			a[i + 1] = prisoners[i];
		}
		a[m + 1] = n + 1;
		
		// dp[i][j] 表示释放编号在a[i]到a[j]之间的所有需要释放的犯人所需的最少金币数
		int[][] dp = new int[m + 2][m + 2];
		
		// 区间DP，按区间长度从小到大计算
		// len表示区间长度
		for (int len = 2; len <= m + 1; len++) {
			// i表示区间起始位置
			for (int i = 0; i + len <= m + 1; i++) {
				// j表示区间结束位置
				int j = i + len;
				dp[i][j] = Integer.MAX_VALUE;
				// 枚举最后一个释放的犯人位置k
				for (int k = i + 1; k < j; k++) {
					// 状态转移方程：
					// dp[i][k]表示释放i到k-1位置的犯人所需金币数
					// dp[k][j]表示释放k+1到j位置的犯人所需金币数
					// (a[j] - a[i] - 2)表示释放第k个犯人时需要贿赂的金币数
					dp[i][j] = Math.min(dp[i][j], 
						dp[i][k] + dp[k][j] + (a[j] - a[i] - 2));
				}
			}
		}
		
		return dp[0][m + 1];
	}
	
	/**
	 * 类似题目3：分糖果问题（LeetCode 135）
	 * 
	 * 题目描述：
	 * n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
	 * 你需要按照以下要求，给这些孩子分发糖果：
	 * 每个孩子至少分配到 1 个糖果。
	 * 相邻两个孩子评分更高的孩子会获得更多的糖果。
	 * 请你给每个孩子分发糖果，计算并返回需要准备的最少糖果数目。
	 * 
	 * 示例：
	 * 输入：ratings = [1,0,2]
	 * 输出：5
	 * 解释：你可以分别给第一个、第二个、第三个孩子分发 2、1、2 颗糖果。
	 * 
	 * 解题思路：
	 * 这是一个贪心算法问题。
	 * 我们可以将「相邻的孩子中，评分高的孩子必须获得更多的糖果」这句话拆分为两个规则：
	 * 1. 从左到右遍历，如果右边评分比左边高，则右边糖果数比左边多1
	 * 2. 从右到左遍历，如果左边评分比右边高，则左边糖果数更新为比右边多1和当前值的最大值
	 */
	
	/**
	 * 分糖果问题 - 贪心算法解法
	 * 
	 * 算法思路: 两次遍历贪心策略
	 * 1. 从左到右遍历：确保右边评分高的孩子比左边获得更多糖果
	 * 2. 从右到左遍历：确保左边评分高的孩子比右边获得更多糖果
	 * 
	 * 时间复杂度: O(n)，其中n是孩子数量
	 * 空间复杂度: O(n)
	 * 
	 * @param ratings 孩子评分数组
	 * @return 最少需要的糖果数目
	 */
	public static int candy(int[] ratings) {
		int n = ratings.length;
		// 每个孩子至少分配到1个糖果
		int[] candies = new int[n];
		Arrays.fill(candies, 1);
		
		// 从左到右遍历，如果右边评分比左边高，则右边糖果数比左边多1
		for (int i = 1; i < n; i++) {
			if (ratings[i] > ratings[i-1]) {
				candies[i] = candies[i-1] + 1;
			}
		}
		
		// 从右到左遍历，如果左边评分比右边高，则左边糖果数更新为比右边多1和当前值的最大值
		for (int i = n - 2; i >= 0; i--) {
			if (ratings[i] > ratings[i+1]) {
				candies[i] = Math.max(candies[i], candies[i+1] + 1);
			}
		}
		
		// 计算总糖果数
		int total = 0;
		for (int candy : candies) {
			total += candy;
		}
		
		return total;
	}
	
	/**
	 * 类似题目4：石子合并问题（洛谷P1880）
	 * 
	 * 题目描述：
	 * 在一个圆形操场的四周摆放N堆石子，现要将石子有次序地合并成一堆，
	 * 规定每次只能选相邻的2堆合并成新的一堆，并将新的一堆的石子数，记为该次合并的得分。
	 * 试设计出一个算法，计算出将N堆石子合并成1堆的最小得分和最大得分。
	 * 
	 * 示例：
	 * 输入：n = 4, stones = [4, 5, 9, 4]
	 * 输出：最小得分 = 43, 最大得分 = 54
	 * 
	 * 解题思路：
	 * 这是一个经典的区间动态规划问题。
	 * 由于是环形，我们可以将环拆成链，即复制一份数组接在后面。
	 * dp[i][j] 表示合并区间[i,j]的石子所需的最小/最大得分
	 * 状态转移方程：
	 * dp[i][j] = min/max{dp[i][k] + dp[k+1][j] + sum[i][j]} for k in i..j-1
	 * 其中sum[i][j]表示区间[i,j]的石子总数
	 */
	
	/**
	 * 石子合并问题 - 区间动态规划解法
	 * 
	 * 算法思路: 区间动态规划处理环形结构
	 * 1. 将环形数组转换为线性数组（复制一份数组接在后面）
	 * 2. 使用前缀和优化区间和计算
	 * 3. 区间DP按长度从小到大计算
	 * 
	 * 时间复杂度: O(n^3)，其中n是石子堆数
	 * 空间复杂度: O(n^2)
	 * 
	 * @param stones 石子堆数组
	 * @return 长度为2的数组，分别表示最小得分和最大得分
	 */
	public static int[] mergeStones(int[] stones) {
		int n = stones.length;
		// 为了处理环形结构，我们将数组复制一份接在后面
		int[] extended = new int[2 * n];
		for (int i = 0; i < n; i++) {
			extended[i] = extended[i + n] = stones[i];
		}
		
		// 计算前缀和，便于快速计算区间和
		int[] prefixSum = new int[2 * n + 1];
		for (int i = 0; i < 2 * n; i++) {
			prefixSum[i + 1] = prefixSum[i] + extended[i];
		}
		
		// dp[i][j] 表示合并区间[i,j]的石子所需的最小得分
		int[][] minDp = new int[2 * n][2 * n];
		// dp[i][j] 表示合并区间[i,j]的石子所需的最大得分
		int[][] maxDp = new int[2 * n][2 * n];
		
		// 初始化DP数组
		for (int i = 0; i < 2 * n; i++) {
			for (int j = 0; j < 2 * n; j++) {
				minDp[i][j] = Integer.MAX_VALUE;
				maxDp[i][j] = Integer.MIN_VALUE;
			}
		}
		
		// 区间DP，按区间长度从小到大计算
		for (int len = 2; len <= n; len++) {
			for (int i = 0; i <= 2 * n - len; i++) {
				int j = i + len - 1;
				// 区间[i,j]的石子总数
				int sum = prefixSum[j + 1] - prefixSum[i];
				
				// 枚举分割点
				for (int k = i; k < j; k++) {
					minDp[i][j] = Math.min(minDp[i][j], 
						minDp[i][k] + minDp[k + 1][j] + sum);
					maxDp[i][j] = Math.max(maxDp[i][j], 
						maxDp[i][k] + maxDp[k + 1][j] + sum);
				}
			}
		}
		
		// 找到最小得分和最大得分
		int minScore = Integer.MAX_VALUE;
		int maxScore = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			minScore = Math.min(minScore, minDp[i][i + n - 1]);
			maxScore = Math.max(maxScore, maxDp[i][i + n - 1]);
		}
		
		return new int[]{minScore, maxScore};
	}
}