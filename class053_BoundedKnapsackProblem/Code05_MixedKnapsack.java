package class075;

/**
 * 混合背包问题 - 多重背包可行性问题（POJ 1742 Coins）
 * 
 * 问题描述：
 * 给定n种货币，每种货币有面值val[i]和数量cnt[i]
 * 想知道在钱数为1, 2, 3, ..., m时，能成功找零的钱数有多少种
 * 
 * 算法分类：动态规划 - 混合背包问题（01背包 + 完全背包 + 多重背包）
 * 
 * 算法原理：
 * 1. 根据物品数量cnt[i]的不同，采用不同的背包策略：
 *    - 当cnt[i] == 1时：视为01背包问题
 *    - 当val[i] * cnt[i] > m时：视为完全背包问题（因为数量足够多）
 *    - 其他情况：视为多重背包问题，使用滑动窗口优化
 * 
 * 2. 滑动窗口优化原理：
 *    - 按面值的余数分组处理
 *    - 维护一个大小为cnt[i]+1的滑动窗口
 *    - 使用trueCnt计数器记录窗口内可达状态的数量
 *    - 通过滑动窗口更新可达状态
 * 
 * 时间复杂度：O(n * m)
 * 空间复杂度：O(m)
 * 
 * 适用场景：
 * - 多重背包的可行性问题（判断某个金额是否可达）
 * - 需要统计可达状态数量的场景
 * - 数据规模中等（m ≤ 100000）的情况
 * 
 * 测试链接：http://poj.org/problem?id=1742（Coins）
 * 
 * 实现特点：
 * 1. 使用布尔数组dp记录可达状态
 * 2. 根据物品数量自动选择最优的背包策略
 * 3. 采用滑动窗口优化多重背包的可行性判断
 * 4. 高效的IO处理，适用于竞赛环境
 */

/**
 * 相关题目扩展（各大算法平台）：
 * 
 * 1. LeetCode（力扣）：
 *    - 474. Ones and Zeroes - https://leetcode.cn/problems/ones-and-zeroes/
 *      多维01背包问题，每个字符串需要同时消耗0和1的数量
 *    - 879. Profitable Schemes - https://leetcode.cn/problems/profitable-schemes/
 *      二维费用背包问题，需要同时考虑人数和利润
 *    - 322. Coin Change - https://leetcode.cn/problems/coin-change/
 *      完全背包问题，求组成金额所需的最少硬币数
 *    - 518. Coin Change II - https://leetcode.cn/problems/coin-change-ii/
 *      完全背包计数问题，求组成金额的方案数
 * 
 * 2. 洛谷（Luogu）：
 *    - P1833 樱花 - https://www.luogu.com.cn/problem/P1833
 *      混合背包问题，包含01背包、完全背包和多重背包
 *    - P1757 通天之分组背包 - https://www.luogu.com.cn/problem/P1757
 *      分组背包问题
 *    - P1064 金明的预算方案 - https://www.luogu.com.cn/problem/P1064
 *      依赖背包问题
 * 
 * 3. POJ：
 *    - POJ 1742. Coins - http://poj.org/problem?id=1742
 *      多重背包可行性问题，计算能组成多少种金额
 *    - POJ 1276. Cash Machine - http://poj.org/problem?id=1276
 *      多重背包优化问题，使用二进制优化或单调队列优化
 *    - POJ 3449. Consumer - http://poj.org/problem?id=3449
 *      有依赖的背包问题
 * 
 * 4. HDU：
 *    - HDU 2191. 悼念512汶川大地震遇难同胞 - http://acm.hdu.edu.cn/showproblem.php?pid=2191
 *      经典多重背包问题
 *    - HDU 3449. Consumer - http://acm.hdu.edu.cn/showproblem.php?pid=3449
 *      有依赖的背包问题，需要先购买主件
 * 
 * 5. Codeforces：
 *    - Codeforces 106C. Buns - https://codeforces.com/contest/106/problem/C
 *      分组背包与多重背包的混合应用
 *    - Codeforces 1003F. Abbreviation - https://codeforces.com/contest/1003/problem/F
 *      字符串处理与多重背包的结合
 * 
 * 6. AtCoder：
 *    - AtCoder DP Contest Problem F - https://atcoder.jp/contests/dp/tasks/dp_f
 *      最长公共子序列与背包思想的结合
 *    - AtCoder ABC153 F. Silver Fox vs Monster - https://atcoder.jp/contests/abc153/tasks/abc153_f
 *      贪心+前缀和优化的背包问题
 * 
 * 7. 牛客网：
 *    - NC19754. 多重背包 - https://ac.nowcoder.com/acm/problem/19754
 *      标准多重背包问题
 *    - NC17881. 最大价值 - https://ac.nowcoder.com/acm/problem/17881
 *      多重背包问题的变形应用
 * 
 * 8. AcWing：
 *    - AcWing 7. 混合背包问题 - https://www.acwing.com/problem/content/7/
 *      标准混合背包问题
 *    - AcWing 5. 多重背包问题II - https://www.acwing.com/problem/content/description/5/
 *      二进制优化的多重背包问题标准题目
 * 
 * 9. UVa OJ：
 *    - UVa 562. Dividing coins - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=503
 *      01背包变形，公平分配硬币
 *    - UVa 10130. SuperSale - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1071
 *      01背包问题的简单应用
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_MixedKnapsack {

	public static int MAXN = 101;

	public static int MAXM = 100001;

	public static int[] val = new int[MAXN];

	public static int[] cnt = new int[MAXN];

	public static boolean[] dp = new boolean[MAXM];

	public static int n, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			if (n != 0 || m != 0) {
				for (int i = 1; i <= n; i++) {
					in.nextToken();
					val[i] = (int) in.nval;
				}
				for (int i = 1; i <= n; i++) {
					in.nextToken();
					cnt[i] = (int) in.nval;
				}
				out.println(compute());
			}
		}
		out.flush();
		out.close();
		br.close();
	}

	// 直接提供空间压缩版
	public static int compute() {
		Arrays.fill(dp, 1, m + 1, false);
		dp[0] = true;
		for (int i = 1; i <= n; i++) {
			if (cnt[i] == 1) {
				// 01背包的空间压缩实现是从右往左更新的
				for (int j = m; j >= val[i]; j--) {
					if (dp[j - val[i]]) {
						dp[j] = true;
					}
				}
			} else if (val[i] * cnt[i] > m) {
				// 完全背包的空间压缩实现是从左往右更新的
				for (int j = val[i]; j <= m; j++) {
					if (dp[j - val[i]]) {
						dp[j] = true;
					}
				}
			} else {
				// 多重背包的空间压缩实现
				// 每一组都是从右往左更新的
				// 同余分组
				for (int mod = 0; mod < val[i]; mod++) {
					int trueCnt = 0;
					for (int j = m - mod, size = 0; j >= 0 && size <= cnt[i]; j -= val[i], size++) {
						trueCnt += dp[j] ? 1 : 0;
					}
					for (int j = m - mod, l = j - val[i] * (cnt[i] + 1); j >= 1; j -= val[i], l -= val[i]) {
						if (dp[j]) {
							trueCnt--;
						} else {
							if (trueCnt != 0) {
								dp[j] = true;
							}
						}
						if (l >= 0) {
							trueCnt += dp[l] ? 1 : 0;
						}
					}
				}
			}
		}
		int ans = 0;
		for (int i = 1; i <= m; i++) {
			if (dp[i]) {
				ans++;
			}
		}
		return ans;
	}

}