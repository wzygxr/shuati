// 苹果袋子问题
// 题目描述：
// 有装下8个苹果的袋子、装下6个苹果的袋子，一定要保证买苹果时所有使用的袋子都装满
// 对于无法装满所有袋子的方案不予考虑，给定n个苹果，返回至少要多少个袋子
// 如果不存在每个袋子都装满的方案返回-1
// 
// 解题思路：
// 这是一个典型的背包问题变种，可以使用动态规划或数学规律来解决
// 1. 递归暴力解法：尝试所有可能的组合
// 2. 动态规划解法：使用dp数组记录每个苹果数的最少袋子数
// 3. 数学规律解法：通过观察规律发现最优解
// 
// 相关题目：
// 1. 牛客网 - 买苹果：https://www.nowcoder.com/practice/61cfbb2e62104bc8aa3da5d44d38a6ef
// 2. 51Nod - 苹果和盘子问题
// 3. LeetCode 322. Coin Change (硬币找零)：https://leetcode.com/problems/coin-change/
// 4. POJ 1742. Coins (多重背包)：http://poj.org/problem?id=1742
// 5. 洛谷 P1616 疯狂的采药：https://www.luogu.com.cn/problem/P1616
// 6. Codeforces 996A. Hit the Lottery：https://codeforces.com/problemset/problem/996/A
// 7. Project Euler 31 - Coin sums：https://projecteuler.net/problem=31
// 8. HDU 2069. 硬币兑换机：http://acm.hdu.edu.cn/showproblem.php?pid=2069
// 9. 牛客网 - NC14532 硬币问题：https://ac.nowcoder.com/acm/problem/14532
// 10. UVA 674. Coin Change：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=615
// 
// 工程化考量：
// 1. 异常处理：处理负数输入
// 2. 边界条件：0个苹果需要0个袋子
// 3. 性能优化：使用数学规律O(1)解法
// 4. 可读性：清晰的变量命名和注释
public class Code01_AppleMinBags {

	/*
	 * 方法1：递归暴力解法
	 * 
	 * 解题思路：
	 * 递归地尝试使用6个或8个的袋子，计算剩余苹果所需的最少袋子数
	 * 
	 * 时间复杂度：O(2^(n/6))，指数级
	 * 空间复杂度：O(n)，递归栈深度
	 * 
	 * 优缺点分析：
	 * 优点：思路直观，易于理解和实现
	 * 缺点：时间复杂度高，不适合大规模数据
	 * 
	 * 适用场景：小规模数据验证，教学演示
	 */
	public static int bags1(int apple) {
		int ans = f(apple);
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	// 当前还有rest个苹果，使用的每个袋子必须装满，返回至少几个袋子
	public static int f(int rest) {
		if (rest < 0) {
			return Integer.MAX_VALUE;
		}
		if (rest == 0) {
			return 0;
		}
		// 使用8规格的袋子，剩余的苹果还需要几个袋子，有可能返回无效解
		int p1 = f(rest - 8);
		// 使用6规格的袋子，剩余的苹果还需要几个袋子，有可能返回无效解
		int p2 = f(rest - 6);
		
		// 如果使用8规格袋子的方案有效，则袋子数加1
		if (p1 != Integer.MAX_VALUE) {
			p1 += 1;
		}
		
		// 如果使用6规格袋子的方案有效，则袋子数加1
		if (p2 != Integer.MAX_VALUE) {
			p2 += 1;
		}
		
		// 返回两种方案中袋子数较少的方案
		return Math.min(p1, p2);
	}

	/*
	 * 方法2：动态规划解法
	 * 
	 * 解题思路：
	 * 使用dp[i]表示装i个苹果所需的最少袋子数
	 * 状态转移方程：dp[i] = min(dp[i-6]+1, dp[i-8]+1)
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * 优缺点分析：
	 * 优点：时间复杂度较低，适合中等规模数据
	 * 缺点：需要额外的O(n)空间
	 * 
	 * 适用场景：中等规模数据，需要准确结果的场景
	 */
	public static int bags2(int apple) {
		if (apple < 0) {
			return -1;
		}
		if (apple == 0) {
			return 0;
		}
		int[] dp = new int[apple + 1];
		// 初始化，除了0个苹果需要0个袋子，其他都初始化为最大值
		// 表示初始状态下，除了0个苹果不需要袋子外，其他数量的苹果都无法装袋
		for (int i = 1; i <= apple; i++) {
			dp[i] = Integer.MAX_VALUE;
		}
		
		// 动态规划填表，从小到大计算每个苹果数的最少袋子数
		for (int i = 1; i <= apple; i++) {
			// 尝试使用8规格的袋子
			// 如果当前苹果数大于等于8，且使用8规格袋子后剩余苹果可以装袋
			if (i >= 8 && dp[i - 8] != Integer.MAX_VALUE) {
				dp[i] = Math.min(dp[i], dp[i - 8] + 1);
			}
			
			// 尝试使用6规格的袋子
			// 如果当前苹果数大于等于6，且使用6规格袋子后剩余苹果可以装袋
			if (i >= 6 && dp[i - 6] != Integer.MAX_VALUE) {
				dp[i] = Math.min(dp[i], dp[i - 6] + 1);
			}
		}
		
		return dp[apple] == Integer.MAX_VALUE ? -1 : dp[apple];
	}

	/*
	 * 方法3：数学规律解法（最优）
	 * 
	 * 解题思路：
	 * 通过观察小规模数据的规律，发现：
	 * 1. 当苹果数量为奇数时无解（因为袋子都是偶数规格）
	 * 2. 当苹果数量小于18时，只有特定偶数有解
	 * 3. 当苹果数量>=18时，所有偶数都有解
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 优缺点分析：
	 * 优点：时间空间复杂度都是O(1)，性能最优
	 * 缺点：需要预先发现规律
	 * 
	 * 适用场景：大规模数据，对性能要求高的场景
	 */
	public static int bags3(int apple) {
		// 如果苹果数量为奇数，则无解
		// 因为6和8都是偶数，偶数个苹果无法组合成奇数个苹果
		if ((apple & 1) != 0) {
			return -1;
		}
		
		// 当苹果数量小于18时，只有特定的偶数有解
		if (apple < 18) {
			// 0个苹果需要0个袋子
			if (apple == 0) return 0;
			
			// 6个或8个苹果需要1个袋子
			if (apple == 6 || apple == 8) return 1;
			
			// 12、14、16个苹果需要2个袋子
			if (apple == 12 || apple == 14 || apple == 16) return 2;
			
			// 其他情况无解
			return -1;
		}
		
		// 当苹果数量>=18时，所有偶数都有解
		// 规律：(apple - 18) / 2 + 3
		// 例如：18个苹果需要3个袋子(2个6规格+1个6规格或3个6规格)
		//      20个苹果需要4个袋子(2个8规格+1个4规格，但4规格不存在，所以是2个6规格+1个8规格)
		return (apple - 18) / 2 + 3;
	}

	// ==================== 扩展题目1: 硬币找零问题 ====================
	/*
	 * LeetCode 322. Coin Change (中等)
	 * 题目：给定不同面额的硬币coins和总金额amount，计算凑成总金额所需的最少硬币数
	 * 如果无法凑成总金额，返回-1
	 * 网址：https://leetcode.com/problems/coin-change/
	 * 
	 * 动态规划解法：
	 * dp[i]表示凑成金额i所需的最少硬币数
	 * 时间复杂度：O(n * m)，其中n为金额，m为硬币种类数
	 * 空间复杂度：O(n)
	 */
	public static int coinChange(int[] coins, int amount) {
		if (amount < 0) return -1;
		if (amount == 0) return 0;
		
		int[] dp = new int[amount + 1];
		// 初始化，除了0金额需要0个硬币，其他都初始化为最大值
		for (int i = 1; i <= amount; i++) {
			dp[i] = Integer.MAX_VALUE;
		}
		
		// 动态规划填表
		for (int i = 1; i <= amount; i++) {
			for (int coin : coins) {
				if (i >= coin && dp[i - coin] != Integer.MAX_VALUE) {
					dp[i] = Math.min(dp[i], dp[i - coin] + 1);
				}
			}
		}
		
		return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
	}

	// ==================== 扩展题目2: 硬币组合问题 ====================
	/*
	 * LeetCode 518. Coin Change 2 (中等)
	 * 题目：给定不同面额的硬币coins和总金额amount，计算凑成总金额的硬币组合数
	 * 网址：https://leetcode.com/problems/coin-change-2/
	 * 
	 * 动态规划解法：
	 * dp[i]表示凑成金额i的硬币组合数
	 * 时间复杂度：O(n * m)
	 * 空间复杂度：O(n)
	 */
	public static int coinChange2(int[] coins, int amount) {
		if (amount < 0) return 0;
		if (amount == 0) return 1;
		
		int[] dp = new int[amount + 1];
		dp[0] = 1;
		
		// 注意：这里需要先遍历硬币，再遍历金额，避免重复计数
		for (int coin : coins) {
			for (int i = coin; i <= amount; i++) {
				dp[i] += dp[i - coin];
			}
		}
		
		return dp[amount];
	}

	// ==================== 扩展题目3: 多重背包问题 ====================
	/*
	 * POJ 1742 Coins (中等)
	 * 题目：有n种硬币，每种硬币有特定的面额和数量
	 * 问能凑出1到m之间多少种金额
	 * 网址：http://poj.org/problem?id=1742
	 * 
	 * 多重背包二进制优化解法：
	 * 时间复杂度：O(n * m * log(max_count))
	 * 空间复杂度：O(m)
	 */
	public static int poj1742(int[] coins, int[] counts, int m) {
		boolean[] dp = new boolean[m + 1];
		dp[0] = true;
		int result = 0;
		
		for (int i = 0; i < coins.length; i++) {
			int coin = coins[i];
			int count = counts[i];
			
			// 二进制优化
			for (int k = 1; count > 0; k <<= 1) {
				int mul = Math.min(k, count);
				int value = coin * mul;
				
				// 01背包，从大到小遍历
				for (int j = m; j >= value; j--) {
					if (dp[j - value]) {
						dp[j] = true;
					}
				}
				
				count -= mul;
			}
		}
		
		// 统计能凑出的金额数
		for (int i = 1; i <= m; i++) {
			if (dp[i]) result++;
		}
		
		return result;
	}

	// ==================== 扩展题目4: 完全背包问题 ====================
	/*
	 * 洛谷 P1616 疯狂的采药 (简单)
	 * 题目：完全背包问题，每种物品有无限个
	 * 网址：https://www.luogu.com.cn/problem/P1616
	 * 
	 * 完全背包解法：
	 * 时间复杂度：O(n * m)
	 * 空间复杂度：O(m)
	 */
	public static int luoguP1616(int T, int M, int[] times, int[] values) {
		int[] dp = new int[T + 1];
		
		for (int i = 0; i < M; i++) {
			int time = times[i];
			int value = values[i];
			
			// 完全背包，从小到大遍历
			for (int j = time; j <= T; j++) {
				dp[j] = Math.max(dp[j], dp[j - time] + value);
			}
		}
		
		return dp[T];
	}

	// ==================== 扩展题目5: 彩票问题 ====================
	/*
	 * Codeforces 996A. Hit the Lottery (简单)
	 * 题目：有面额为100,20,10,5,1的钞票，求凑出n所需的最少钞票数
	 * 网址：https://codeforces.com/problemset/problem/996/A
	 * 
	 * 贪心解法：
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 */
	public static int hitTheLottery(int n) {
		int[] denominations = {100, 20, 10, 5, 1};
		int count = 0;
		
		for (int denom : denominations) {
			count += n / denom;
			n %= denom;
		}
		
		return count;
	}

	// ==================== 扩展题目6: 硬币求和问题 ====================
	/*
	 * Project Euler 31 - Coin sums (中等)
	 * 题目：使用1p,2p,5p,10p,20p,50p,£1,£2硬币凑出£2的方法数
	 * 网址：https://projecteuler.net/problem=31
	 * 
	 * 动态规划解法：
	 * 时间复杂度：O(n * m)
	 * 空间复杂度：O(n)
	 */
	public static int projectEuler31() {
		int[] coins = {1, 2, 5, 10, 20, 50, 100, 200};
		int amount = 200;
		int[] dp = new int[amount + 1];
		dp[0] = 1;
		
		for (int coin : coins) {
			for (int i = coin; i <= amount; i++) {
				dp[i] += dp[i - coin];
			}
		}
		
		return dp[amount];
	}

	// ==================== 扩展题目7: 硬币兑换机 ====================
	/*
	 * HDU 2069. 硬币兑换机 (中等)
	 * 题目：有5种面额的硬币，求凑出n分钱的方案数，要求硬币总数不超过100枚
	 * 网址：http://acm.hdu.edu.cn/showproblem.php?pid=2069
	 * 
	 * 二维动态规划解法：
	 * dp[i][j]表示使用j枚硬币凑出i分钱的方案数
	 * 时间复杂度：O(n * m * k)
	 * 空间复杂度：O(n * k)
	 */
	public static int hdu2069(int n) {
		int[] coins = {1, 5, 10, 25, 50};
		int[][] dp = new int[n + 1][101];
		dp[0][0] = 1;
		
		for (int coin : coins) {
			for (int i = coin; i <= n; i++) {
				for (int j = 1; j <= 100; j++) {
					dp[i][j] += dp[i - coin][j - 1];
				}
			}
		}
		
		int result = 0;
		for (int j = 0; j <= 100; j++) {
			result += dp[n][j];
		}
		
		return result;
	}

	// ==================== 扩展题目8: 硬币问题 ====================
	/*
	 * 牛客网 - NC14532 硬币问题 (中等)
	 * 题目：有n种硬币，每种硬币有无限个，求凑出m元的方法数
	 * 网址：https://ac.nowcoder.com/acm/problem/14532
	 * 
	 * 完全背包解法：
	 * 时间复杂度：O(n * m)
	 * 空间复杂度：O(m)
	 */
	public static int nc14532(int n, int m, int[] coins) {
		int[] dp = new int[m + 1];
		dp[0] = 1;
		
		for (int coin : coins) {
			for (int i = coin; i <= m; i++) {
				dp[i] += dp[i - coin];
			}
		}
		
		return dp[m];
	}

	// ==================== 扩展题目9: 硬币找零问题 ====================
	/*
	 * UVA 674. Coin Change (中等)
	 * 题目：有5种面额的硬币，求凑出n分钱的方法数
	 * 网址：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=615
	 * 
	 * 完全背包解法：
	 * 时间复杂度：O(n * m)
	 * 空间复杂度：O(n)
	 */
	public static int uva674(int n) {
		int[] coins = {1, 5, 10, 25, 50};
		int[] dp = new int[n + 1];
		dp[0] = 1;
		
		for (int coin : coins) {
			for (int i = coin; i <= n; i++) {
				dp[i] += dp[i - coin];
			}
		}
		
		return dp[n];
	}

	// ==================== 测试方法 ====================
	public static void main(String[] args) {
		// 测试苹果袋子问题
		System.out.println("=== 苹果袋子问题测试 ===");
		for (int i = 0; i <= 20; i++) {
			int result1 = bags1(i);
			int result2 = bags2(i);
			int result3 = bags3(i);
			System.out.println(i + "个苹果: " + result1 + " / " + result2 + " / " + result3);
		}
		
		// 测试扩展题目
		System.out.println("\n=== 扩展题目测试 ===");
		
		// 测试硬币找零
		int[] coins1 = {1, 2, 5};
		System.out.println("Coin Change (11): " + coinChange(coins1, 11)); // 3
		
		// 测试硬币组合
		int[] coins2 = {1, 2, 5};
		System.out.println("Coin Change 2 (5): " + coinChange2(coins2, 5)); // 4
		
		// 测试多重背包
		int[] coins3 = {1, 2, 5};
		int[] counts = {3, 2, 1};
		System.out.println("POJ 1742 (10): " + poj1742(coins3, counts, 10)); // 8
		
		// 测试完全背包
		int[] times = {2, 3, 4};
		int[] values = {3, 4, 5};
		System.out.println("Luogu P1616 (10): " + luoguP1616(10, 3, times, values)); // 11
		
		// 测试彩票问题
		System.out.println("Hit the Lottery (125): " + hitTheLottery(125)); // 3
		
		// 测试硬币求和
		System.out.println("Project Euler 31: " + projectEuler31()); // 73682
		
		// 测试硬币兑换机
		System.out.println("HDU 2069 (100): " + hdu2069(100)); // 292
		
		// 测试UVA硬币找零
		System.out.println("UVA 674 (11): " + uva674(11)); // 4
	}
}