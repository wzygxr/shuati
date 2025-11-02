package class077;

// POJ 2955 Brackets
// 题目来源：http://poj.org/problem?id=2955
// 题目大意：给定一个只包含'('、')'、'['、']'的括号序列，求最长的合法括号子序列的长度。
// 合法括号序列定义：
// 1. 空序列是合法的
// 2. 如果A是合法的，则(A)和[A]都是合法的
// 3. 如果A和B都是合法的，则AB也是合法的
//
// 解题思路：
// 1. 使用区间动态规划，dp[i][j]表示区间[i,j]内最长合法括号子序列的长度
// 2. 状态转移：
//    - 如果s[i]和s[j]匹配，则dp[i][j] = dp[i+1][j-1] + 2
//    - 否则枚举分割点k，dp[i][j] = max(dp[i][k] + dp[k+1][j])
//
// 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
// 空间复杂度：O(n^2) - dp数组占用空间
//
// 工程化考虑：
// 1. 输入验证：检查输入是否为空
// 2. 边界处理：处理长度为0、1的特殊情况
// 3. 匹配判断：正确判断括号是否匹配
// 4. 异常处理：对于不合法输入给出适当提示

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class POJ2955_Brackets {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		String line;
		while (!(line = br.readLine()).equals("end")) {
			int result = solve(line);
			out.println(result);
		}
		
		out.flush();
		out.close();
		br.close();
	}

	// 主函数：解决最长合法括号子序列问题
	// 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
	// 空间复杂度: O(n^2) - dp数组占用空间
	public static int solve(String s) {
		int n = s.length();
		if (n == 0) {
			return 0;
		}
		
		// dp[i][j]表示区间[i,j]内最长合法括号子序列的长度
		int[][] dp = new int[n][n];
		
		// 初始化：单个字符无法构成合法序列
		for (int i = 0; i < n; i++) {
			dp[i][i] = 0;
		}
		
		// 枚举区间长度，从2开始
		for (int len = 2; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n - len; i++) {
				// 计算区间终点j
				int j = i + len - 1;
				
				// 如果两端字符匹配
				if ((s.charAt(i) == '(' && s.charAt(j) == ')') || 
					(s.charAt(i) == '[' && s.charAt(j) == ']')) {
					if (len == 2) {
						// 长度为2且匹配
						dp[i][j] = 2;
					} else {
						// 长度大于2且匹配
						dp[i][j] = dp[i+1][j-1] + 2;
					}
				}
				
				// 枚举分割点k，取最大值
				for (int k = i; k < j; k++) {
					dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k+1][j]);
				}
			}
		}
		
		return dp[0][n-1];
	}
}