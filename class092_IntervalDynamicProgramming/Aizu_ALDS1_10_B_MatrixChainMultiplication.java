package class077;

// Aizu OJ ALDS1_10_B Matrix Chain Multiplication
// 题目来源：https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_10_B
// 题目大意：给定n个矩阵的维度，矩阵Ai的维度为di-1 × di。
// 矩阵乘法满足结合律，不同的加括号方式会导致不同的计算代价。
// 矩阵乘法的代价定义为标量乘法的次数。
// 求计算矩阵链乘积的最小标量乘法次数。
//
// 解题思路：
// 1. 这是经典的矩阵链乘法问题，使用区间动态规划解决
// 2. dp[i][j]表示计算矩阵Ai到Aj的最小标量乘法次数
// 3. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k+1][j] + d[i-1]*d[k]*d[j])
//
// 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
// 空间复杂度：O(n^2) - dp数组占用空间
//
// 工程化考虑：
// 1. 输入验证：检查输入是否合法
// 2. 边界处理：处理矩阵数量较少的特殊情况
// 3. 索引处理：正确处理矩阵维度数组的索引
// 4. 异常处理：对于不合法输入给出适当提示

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Aizu_ALDS1_10_B_MatrixChainMultiplication {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		int n = Integer.parseInt(br.readLine().trim());
		int[] dimensions = new int[n + 1];
		
		for (int i = 0; i < n; i++) {
			String[] parts = br.readLine().split(" ");
			dimensions[i] = Integer.parseInt(parts[0]);
			dimensions[i + 1] = Integer.parseInt(parts[1]);
		}
		
		int result = solve(dimensions, n);
		out.println(result);
		
		out.flush();
		out.close();
		br.close();
	}

	// 主函数：解决矩阵链乘法问题
	// 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
	// 空间复杂度: O(n^2) - dp数组占用空间
	public static int solve(int[] dimensions, int n) {
		if (n <= 1) {
			return 0;
		}
		
		// dp[i][j]表示计算矩阵Ai到Aj的最小标量乘法次数
		int[][] dp = new int[n][n];
		
		// 枚举区间长度，从2开始（至少需要2个矩阵才能相乘）
		for (int len = 2; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n - len; i++) {
				// 计算区间终点j
				int j = i + len - 1;
				dp[i][j] = Integer.MAX_VALUE;
				
				// 枚举分割点k
				for (int k = i; k < j; k++) {
					// 计算标量乘法次数
					// dimensions[i]：矩阵Ai的行数
					// dimensions[k+1]：矩阵Ak的列数，也是矩阵Ak+1的行数
					// dimensions[j+1]：矩阵Aj的列数
					int cost = dp[i][k] + dp[k + 1][j] + dimensions[i] * dimensions[k + 1] * dimensions[j + 1];
					
					dp[i][j] = Math.min(dp[i][j], cost);
				}
			}
		}
		
		return dp[0][n - 1];
	}
}