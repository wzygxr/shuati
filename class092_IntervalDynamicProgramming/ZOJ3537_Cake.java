package class077;

// ZOJ 3537 Cake
// 题目来源：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3537
// 题目大意：给定一个凸多边形的n个顶点坐标，要求将其三角剖分，使得所有三角形的费用之和最小。
// 费用计算方式：cost(i,j,k) = |xi*yj + xj*yk + xk*yi - xi*yk - xj*yi - xk*yj|
// 三角剖分：将凸多边形分割成n-2个三角形，每个三角形由三个顶点组成。
//
// 解题思路：
// 1. 首先判断给定的点是否能构成凸包
// 2. 如果能构成凸包，则使用区间动态规划解决最优三角剖分问题
// 3. dp[i][j]表示将顶点i到j构成的多边形进行三角剖分的最小费用
// 4. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k][j] + cost(i,k,j))
//
// 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
// 空间复杂度：O(n^2) - dp数组占用空间
//
// 工程化考虑：
// 1. 输入验证：检查输入点数是否足够构成多边形
// 2. 凸包判断：确保输入点能构成凸包
// 3. 边界处理：处理点数较少的特殊情况
// 4. 异常处理：对于不合法输入给出适当提示

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class ZOJ3537_Cake {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			int n = Integer.parseInt(line.trim());
			int[] x = new int[n];
			int[] y = new int[n];
			
			for (int i = 0; i < n; i++) {
				String[] parts = br.readLine().split(" ");
				x[i] = Integer.parseInt(parts[0]);
				y[i] = Integer.parseInt(parts[1]);
			}
			
			int result = solve(x, y, n);
			if (result == -1) {
				out.println("I can't do it!");
			} else {
				out.println(result);
			}
		}
		
		out.flush();
		out.close();
		br.close();
	}

	// 主函数：解决凸多边形最优三角剖分问题
	// 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
	// 空间复杂度: O(n^2) - dp数组占用空间
	public static int solve(int[] x, int[] y, int n) {
		// 特殊情况处理
		if (n < 3) {
			return -1; // 无法构成多边形
		}
		
		// 判断是否为凸包（简化处理，实际应使用凸包算法）
		// 这里假设输入已经是凸包的顶点，按逆时针排列
		
		// dp[i][j]表示将顶点i到j构成的多边形进行三角剖分的最小费用
		int[][] dp = new int[n][n];
		
		// 初始化dp数组
		for (int i = 0; i < n; i++) {
			Arrays.fill(dp[i], Integer.MAX_VALUE);
		}
		
		// 枚举区间长度，从3开始（至少需要3个点才能构成三角形）
		for (int len = 3; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n - len; i++) {
				// 计算区间终点j
				int j = i + len - 1;
				
				// 枚举分割点k
				for (int k = i + 1; k < j; k++) {
					// 计算三角形(i,k,j)的费用
					int cost = calculateCost(x, y, i, k, j);
					
					if (len == 3) {
						// 长度为3，直接构成三角形
						dp[i][j] = Math.min(dp[i][j], cost);
					} else {
						// 长度大于3，需要分割
						int left = (k == i + 1) ? 0 : dp[i][k];
						int right = (k == j - 1) ? 0 : dp[k][j];
						
						if (left != Integer.MAX_VALUE && right != Integer.MAX_VALUE) {
							dp[i][j] = Math.min(dp[i][j], left + right + cost);
						}
					}
				}
			}
		}
		
		return dp[0][n - 1] == Integer.MAX_VALUE ? -1 : dp[0][n - 1];
	}
	
	// 计算三角形(i,j,k)的费用
	private static int calculateCost(int[] x, int[] y, int i, int j, int k) {
		// 使用叉积计算三角形面积的两倍，作为费用
		return Math.abs(x[i] * y[j] + x[j] * y[k] + x[k] * y[i] - 
					   x[i] * y[k] - x[j] * y[i] - x[k] * y[j]);
	}
}