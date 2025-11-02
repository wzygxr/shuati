package class075;

/**
 * 混合背包问题 - 观赏樱花（洛谷P1833）
 * 
 * 问题描述：
 * 给定一个背包的容量t，一共有n种货物，每种货物有以下属性：
 * - 花费(cost)：物品的重量
 * - 价值(val)：物品的价值
 * - 数量(cnt)：物品的数量限制
 * 
 * 特殊规则：
 * - 如果cnt == 0，代表这种货物可以无限选择（完全背包）
 * - 如果cnt > 0，那么cnt代表这种货物的数量（多重背包）
 * 
 * 算法分类：动态规划 - 混合背包问题（完全背包 + 多重背包）
 * 
 * 算法原理：
 * 1. 将完全背包问题转化为多重背包问题：通过设置足够大的数量限制（ENOUGH=1001）
 * 2. 使用二进制分组优化将多重背包转化为01背包问题
 * 3. 对转化后的01背包问题应用空间压缩的动态规划算法
 * 
 * 时间复杂度：O(t * Σlog c[i])，其中c[i]是第i种物品的数量（经过二进制分组后）
 * 空间复杂度：O(t)
 * 
 * 适用场景：
 * - 同时包含完全背包和多重背包的混合背包问题
 * - 背包容量中等（t ≤ 1000）的情况
 * - 需要处理时间格式输入的特殊场景
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1833（樱花）
 * 
 * 实现特点：
 * 1. 处理时间格式输入（小时:分钟）并转换为分钟数作为背包容量
 * 2. 使用二进制分组优化多重背包
 * 3. 采用空间压缩的01背包算法
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
 *    - P1776 宝物筛选 - https://www.luogu.com.cn/problem/P1776
 *      经典多重背包问题
 *    - P1616 疯狂的采药 - https://www.luogu.com.cn/problem/P1616
 *      完全背包问题，数据规模较大
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
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

// 完全背包转化为多重背包
// 再把多重背包通过二进制分组转化为01背包
public class Code03_CherryBlossomViewing {

	public static int MAXN = 100001;

	public static int MAXW = 1001;

	public static int ENOUGH = 1001;

	public static int[] v = new int[MAXN];

	public static int[] w = new int[MAXN];

	public static int[] dp = new int[MAXW];

	public static int hour1, minute1, hour2, minute2;

	public static int t, n, m;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		in.parseNumbers();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			hour1 = (int) in.nval;
			// 跳过冒号
			in.nextToken();
			in.nextToken();
			minute1 = (int) in.nval;
			in.nextToken();
			hour2 = (int) in.nval;
			// 跳过冒号
			in.nextToken();
			in.nextToken();
			minute2 = (int) in.nval;
			if (minute1 > minute2) {
				hour2--;
				minute2 += 60;
			}
			// 计算背包容量
			t = (hour2 - hour1) * 60 + minute2 - minute1;
			in.nextToken();
			n = (int) in.nval;
			m = 0;
			for (int i = 0, cost, val, cnt; i < n; i++) {
				in.nextToken();
				cost = (int) in.nval;
				in.nextToken();
				val = (int) in.nval;
				in.nextToken();
				cnt = (int) in.nval;
				if (cnt == 0) {
					cnt = ENOUGH;
				}
				// 二进制分组
				for (int k = 1; k <= cnt; k <<= 1) {
					v[++m] = k * val;
					w[m] = k * cost;
					cnt -= k;
				}
				if (cnt > 0) {
					v[++m] = cnt * val;
					w[m] = cnt * cost;
				}
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	// 01背包的空间压缩代码(模版)
	public static int compute() {
		Arrays.fill(dp, 0, t + 1, 0);
		for (int i = 1; i <= m; i++) {
			for (int j = t; j >= w[i]; j--) {
				dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
			}
		}
		return dp[t];
	}

}