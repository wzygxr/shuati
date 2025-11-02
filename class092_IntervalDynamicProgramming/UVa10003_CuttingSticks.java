package class077;

// UVa 10003 Cutting Sticks
// 题目来源：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=944
// 题目大意：有一根长度为L的木棍，上面有n个切割点。每次切割的费用等于当前木棍的长度。
// 要求找出切割所有切割点的最小总费用。
//
// 解题思路：
// 1. 这是一个经典的区间动态规划问题，类似于石子合并问题
// 2. dp[i][j]表示切割区间[i,j]内所有切割点的最小费用
// 3. 状态转移：枚举最后一个切割点k，dp[i][j] = min(dp[i][k] + dp[k][j] + (cuts[j] - cuts[i]))
// 4. 需要将切割点排序，并在两端添加0和L作为边界
//
// 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
// 空间复杂度：O(n^2) - dp数组占用空间
//
// 工程化考虑：
// 1. 输入验证：检查输入是否合法
// 2. 边界处理：处理没有切割点的特殊情况
// 3. 排序处理：确保切割点按顺序排列
// 4. 异常处理：对于不合法输入给出适当提示

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class UVa10003_CuttingSticks {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		String line;
		while (!(line = br.readLine()).equals("0")) {
			int L = Integer.parseInt(line.trim());
			int n = Integer.parseInt(br.readLine().trim());
			
			String[] parts = br.readLine().split(" ");
			int[] cuts = new int[n + 2];
			cuts[0] = 0; // 起点
			for (int i = 1; i <= n; i++) {
				cuts[i] = Integer.parseInt(parts[i - 1]);
			}
			cuts[n + 1] = L; // 终点
			
			// 排序切割点
			Arrays.sort(cuts);
			
			int result = solve(cuts, n + 2);
			out.println("The minimum cutting is " + result + ".");
		}
		
		out.flush();
		out.close();
		br.close();
	}

	// 主函数：解决切木棍问题
	// 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
	// 空间复杂度: O(n^2) - dp数组占用空间
	public static int solve(int[] cuts, int n) {
		// dp[i][j]表示切割区间[i,j]内所有切割点的最小费用
		int[][] dp = new int[n][n];
		
		// 枚举区间长度，从2开始（至少需要两个端点）
		for (int len = 2; len < n; len++) {
			// 枚举区间起点i
			for (int i = 0; i < n - len; i++) {
				// 计算区间终点j
				int j = i + len;
				dp[i][j] = Integer.MAX_VALUE;
				
				// 枚举最后一个切割点k
				for (int k = i + 1; k < j; k++) {
					// 状态转移方程
					// dp[i][k]：切割左半部分的费用
					// dp[k][j]：切割右半部分的费用
					// cuts[j] - cuts[i]：当前切割的费用（当前木棍长度）
					dp[i][j] = Math.min(dp[i][j], 
						dp[i][k] + dp[k][j] + (cuts[j] - cuts[i]));
				}
			}
		}
		
		return dp[0][n - 1];
	}
}