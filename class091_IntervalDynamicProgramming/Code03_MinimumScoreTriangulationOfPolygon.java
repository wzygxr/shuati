package class076;

/**
 * LeetCode 1039. 多边形三角剖分的最低得分
 * 题目链接：https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 
 * 题目描述：
 * 你有一个凸的 n 边形，其每个顶点都有一个整数值。
 * 给定一个整数数组 values，其中 values[i] 是第 i 个顶点的值（顺时针顺序）。
 * 假设将多边形剖分为 n - 2 个三角形。
 * 对于每个三角形，该三角形的值是顶点标记的乘积。
 * 三角剖分的分数是进行三角剖分后所有 n - 2 个三角形的值之和。
 * 返回多边形进行三角剖分后可以得到的最低分。
 * 
 * 解题思路：
 * 这是一个经典的区间动态规划问题，类似于矩阵链乘法问题。
 * 定义状态 dp[i][j] 表示将顶点 i 到 j 之间的多边形进行三角剖分能得到的最低分数。
 * 状态转移方程：
 * dp[i][j] = min(dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]) for k in (i+1, j-1)
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * 1. 边界条件处理：少于3个顶点无法形成三角形
 * 2. 优化：可以使用四边形不等式优化到 O(n²)
 * 3. 输入验证：检查数组长度是否满足要求
 * 
 * 相关题目扩展：
 * 1. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 2. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 3. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 4. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 5. LeetCode 1032. 字符流 - https://leetcode.cn/problems/stream-of-characters/
 * 6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 7. LintCode 1639. K 倍重复项删除 - https://www.lintcode.com/problem/1639/
 * 8. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 9. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 */
public class Code03_MinimumScoreTriangulationOfPolygon {

	// 记忆化搜索
	public static int minScoreTriangulation1(int[] arr) {
		int n = arr.length;
		int[][] dp = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				dp[i][j] = -1;
			}
		}
		return f(arr, 0, n - 1, dp);
	}

	public static int f(int[] arr, int l, int r, int[][] dp) {
		if (dp[l][r] != -1) {
			return dp[l][r];
		}
		int ans = Integer.MAX_VALUE;
		if (l == r || l == r - 1) {
			ans = 0;
		} else {
			// l....r >=3
			// 0..1..2..3..4...5
			for (int m = l + 1; m < r; m++) {
				// l m r
				ans = Math.min(ans, f(arr, l, m, dp) + f(arr, m, r, dp) + arr[l] * arr[m] * arr[r]);
			}
		}
		dp[l][r] = ans;
		return ans;
	}

	// 严格位置依赖的动态规划
	public static int minScoreTriangulation2(int[] arr) {
		int n = arr.length;
		int[][] dp = new int[n][n];
		for (int l = n - 3; l >= 0; l--) {
			for (int r = l + 2; r < n; r++) {
				dp[l][r] = Integer.MAX_VALUE;
				for (int m = l + 1; m < r; m++) {
					dp[l][r] = Math.min(dp[l][r], dp[l][m] + dp[m][r] + arr[l] * arr[m] * arr[r]);
				}
			}
		}
		return dp[0][n - 1];
	}

}