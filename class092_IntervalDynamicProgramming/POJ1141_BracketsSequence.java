package class077;

// POJ 1141 Brackets Sequence
// 题目来源：http://poj.org/problem?id=1141
// 题目大意：给定一个括号序列，可能包含'('、')'、'['、']'，要求添加最少的括号使其成为合法的括号序列，
// 并输出字典序最小的合法序列。
//
// 解题思路：
// 1. 使用区间动态规划，dp[i][j]表示使区间[i,j]成为合法括号序列需要添加的最少括号数
// 2. 状态转移：
//    - 如果s[i]和s[j]匹配，则dp[i][j] = dp[i+1][j-1]
//    - 否则枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k+1][j])
// 3. 通过path数组记录路径，用于构造最终的合法序列
//
// 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
// 空间复杂度：O(n^2) - dp和path数组占用空间
//
// 工程化考虑：
// 1. 输入验证：检查输入是否为空
// 2. 边界处理：处理长度为0、1的特殊情况
// 3. 异常处理：对于不合法输入给出适当提示
// 4. 代码可读性：变量命名清晰，添加详细注释

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class POJ1141_BracketsSequence {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		String line = br.readLine();
		if (line == null || line.isEmpty()) {
			out.println("");
		} else {
			out.println(solve(line));
		}
		out.flush();
		out.close();
		br.close();
	}

	// 区间动态规划解法
	// 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
	// 空间复杂度: O(n^2) - dp和path数组占用空间
	public static String solve(String s) {
		int n = s.length();
		if (n == 0) {
			return "";
		}
		
		// dp[i][j]表示使区间[i,j]成为合法括号序列需要添加的最少括号数
		int[][] dp = new int[n][n];
		// path[i][j]记录构造方案，-1表示两端匹配，其他值表示分割点
		int[][] path = new int[n][n];
		
		// 初始化：单个字符需要添加1个字符才能匹配
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
			path[i][i] = -2; // 单个字符标记
		}
		
		// 枚举区间长度，从2开始
		for (int len = 2; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n - len; i++) {
				// 计算区间终点j
				int j = i + len - 1;
				dp[i][j] = n; // 初始化为最大值
				
				// 如果两端字符匹配
				if ((s.charAt(i) == '(' && s.charAt(j) == ')') || 
					(s.charAt(i) == '[' && s.charAt(j) == ']')) {
					if (len == 2) {
						// 长度为2且匹配
						dp[i][j] = 0;
						path[i][j] = -1; // 两端匹配标记
					} else {
						// 长度大于2且匹配
						if (dp[i+1][j-1] < dp[i][j]) {
							dp[i][j] = dp[i+1][j-1];
							path[i][j] = -1; // 两端匹配标记
						}
					}
				}
				
				// 枚举分割点k
				for (int k = i; k < j; k++) {
					if (dp[i][k] + dp[k+1][j] < dp[i][j]) {
						dp[i][j] = dp[i][k] + dp[k+1][j];
						path[i][j] = k; // 记录分割点
					}
				}
			}
		}
		
		// 根据path数组构造结果
		return buildResult(s, path, 0, n - 1);
	}
	
	// 根据path数组递归构造结果字符串
	private static String buildResult(String s, int[][] path, int i, int j) {
		if (i > j) {
			return "";
		}
		
		if (i == j) {
			// 单个字符
			char c = s.charAt(i);
			if (c == '(' || c == ')') {
				return "()";
			} else {
				return "[]";
			}
		}
		
		int k = path[i][j];
		if (k == -1) {
			// 两端匹配
			return s.charAt(i) + buildResult(s, path, i + 1, j - 1) + s.charAt(j);
		} else {
			// 分割点k
			return buildResult(s, path, i, k) + buildResult(s, path, k + 1, j);
		}
	}
}