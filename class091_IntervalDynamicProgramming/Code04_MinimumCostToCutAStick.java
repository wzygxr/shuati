package class076;

import java.util.Arrays;

/**
 * LeetCode 1547. 切棍子的最小成本
 * 题目链接：https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 
 * 题目描述：
 * 有一根长度为 n 个单位的木棍，棍上从 0 到 n 标记了若干位置。
 * 给你一个整数数组 cuts，其中 cuts[i] 表示你需要将棍子切开的位置。
 * 你可以按顺序完成切割，也可以根据需要更改切割的顺序。
 * 每次切割的成本都是当前要切割的棍子的长度，切棍子的总成本是历次切割成本的总和。
 * 对棍子进行切割将会把一根木棍分成两根较小的木棍，这两根木棍的长度和就是切割前木棍的长度。
 * 返回切棍子的最小总成本。
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，类似于矩阵链乘法和石子合并问题。
 * 首先对 cuts 数组进行排序，然后在两端添加 0 和 n，形成新的数组。
 * 定义状态 dp[i][j] 表示切割区间 [i,j] 内所有切割点的最小成本。
 * 状态转移方程：
 * dp[i][j] = min(dp[i][k] + dp[k][j] + (arr[j] - arr[i])) for k in (i+1, j-1)
 * 
 * 时间复杂度：O(m³)，其中 m 是 cuts 数组的长度
 * 空间复杂度：O(m²)
 * 
 * 工程化考量：
 * 1. 边界条件处理：没有切割点时成本为0
 * 2. 优化：可以使用四边形不等式优化到 O(m²)
 * 3. 输入验证：检查 cuts 数组是否为空
 * 
 * 相关题目扩展：
 * 1. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 4. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 5. LeetCode 1032. 字符流 - https://leetcode.cn/problems/stream-of-characters/
 * 6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 7. LintCode 1639. K 倍重复项删除 - https://www.lintcode.com/problem/1639/
 * 8. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 9. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 */
public class Code04_MinimumCostToCutAStick {

	// 记忆化搜索
	public static int minCost1(int n, int[] cuts) {
		int m = cuts.length;
		Arrays.sort(cuts);
		int[] arr = new int[m + 2];
		arr[0] = 0;
		for (int i = 1; i <= m; i++) {
			arr[i] = cuts[i - 1];
		}
		arr[m + 1] = n;
		int[][] dp = new int[m + 2][m + 2];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= m; j++) {
				dp[i][j] = -1;
			}
		}
		return f(arr, 1, m, dp);
	}
	
	// 切点[l....r]，决定一个顺序
	// 让切点都切完，总代价最小
	public static int f(int[] arr, int l, int r, int[][] dp) {
		if (l > r) {
			return 0;
		}
		if (l == r) {
			return arr[r + 1] - arr[l - 1];
		}
		if (dp[l][r] != -1) {
			return dp[l][r];
		}
		int ans = Integer.MAX_VALUE;
		for (int k = l; k <= r; k++) {
			ans = Math.min(ans, f(arr, l, k - 1, dp) + f(arr, k + 1, r, dp));
		}
		ans += arr[r + 1] - arr[l - 1];
		dp[l][r] = ans;
		return ans;
	}

	// 严格位置依赖的动态规划
	public static int minCost2(int n, int[] cuts) {
		int m = cuts.length;
		Arrays.sort(cuts);
		int[] arr = new int[m + 2];
		arr[0] = 0;
		for (int i = 1; i <= m; i++) {
			arr[i] = cuts[i - 1];
		}
		arr[m + 1] = n;
		int[][] dp = new int[m + 2][m + 2];
		for (int i = 1; i <= m; i++) {
			dp[i][i] = arr[i + 1] - arr[i - 1];
		}
		for (int l = m - 1, next; l >= 1; l--) {
			for (int r = l + 1; r <= m; r++) {
				next = Integer.MAX_VALUE;
				for (int k = l; k <= r; k++) {
					next = Math.min(next, dp[l][k - 1] + dp[k + 1][r]);
				}
				dp[l][r] = arr[r + 1] - arr[l - 1] + next;
			}
		}
		return dp[1][m];
	}

}