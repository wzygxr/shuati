package class077;

// SPOJ MIXTURES
// 题目来源：https://www.spoj.com/problems/MIXTURES/
// 题目大意：有n个混合物排成一排，每个混合物有一个颜色值(0-99)。
// 每次可以合并相邻的两个混合物，合并后的新混合物颜色值为两个混合物颜色值之和对100取模。
// 合并的代价为两个混合物颜色值的乘积。
// 求合并所有混合物的最小代价。
//
// 解题思路：
// 1. 这是另一个经典的区间动态规划问题，类似于石子合并
// 2. dp[i][j]表示合并区间[i,j]内所有混合物的最小代价
// 3. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k+1][j] + cost(i,k,j))
// 4. 需要预处理前缀和数组来快速计算区间和，以及颜色值
//
// 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
// 空间复杂度：O(n^2) - dp数组占用空间
//
// 工程化考虑：
// 1. 输入验证：检查输入是否合法
// 2. 模运算处理：正确处理颜色值的模运算
// 3. 边界处理：处理混合物数量较少的特殊情况
// 4. 异常处理：对于不合法输入给出适当提示

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class SPOJ_MIXTURES {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			int n = Integer.parseInt(line.trim());
			String[] parts = br.readLine().split(" ");
			int[] colors = new int[n];
			for (int i = 0; i < n; i++) {
				colors[i] = Integer.parseInt(parts[i]);
			}
			
			int result = solve(colors, n);
			out.println(result);
		}
		
		out.flush();
		out.close();
		br.close();
	}

	// 主函数：解决混合物合并问题
	// 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
	// 空间复杂度: O(n^2) - dp数组占用空间
	public static int solve(int[] colors, int n) {
		if (n <= 1) {
			return 0;
		}
		
		// 计算前缀和数组，用于快速计算区间和
		int[] prefixSum = new int[n + 1];
		for (int i = 0; i < n; i++) {
			prefixSum[i + 1] = prefixSum[i] + colors[i];
		}
		
		// dp[i][j]表示合并区间[i,j]内所有混合物的最小代价
		int[][] dp = new int[n][n];
		// color[i][j]表示合并区间[i,j]内所有混合物后的颜色值
		int[][] color = new int[n][n];
		
		// 初始化：单个混合物的颜色值
		for (int i = 0; i < n; i++) {
			color[i][i] = colors[i];
		}
		
		// 枚举区间长度，从2开始（至少需要2个混合物才能合并）
		for (int len = 2; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n - len; i++) {
				// 计算区间终点j
				int j = i + len - 1;
				dp[i][j] = Integer.MAX_VALUE;
				
				// 枚举分割点k
				for (int k = i; k < j; k++) {
					// 计算合并代价
					int cost = dp[i][k] + dp[k + 1][j] + color[i][k] * color[k + 1][j];
					
					if (cost < dp[i][j]) {
						dp[i][j] = cost;
						// 计算合并后的颜色值
						color[i][j] = (color[i][k] + color[k + 1][j]) % 100;
					}
				}
			}
		}
		
		return dp[0][n - 1];
	}
}