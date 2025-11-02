package class087;

/**
 * 两个排列的最长公共子序列长度
 * 
 * 问题描述:
 * 给出由1~n这些数字组成的两个排列
 * 求它们的最长公共子序列长度
 * 
 * 约束条件:
 * n <= 10^5
 * 
 * 解题思路:
 * 利用排列的特殊性质，将LCS问题转化为LIS问题
 * 1. 将第二个排列转换为第一个排列中对应数字的位置
 * 2. 问题转化为求位置序列的最长递增子序列
 * 3. 使用贪心+二分查找求解LIS
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P1439
 * 
 * 工程化考量:
 * - 使用高效的输入输出处理方式
 * - 时间复杂度优化至O(n log n)
 * - 空间复杂度优化至O(n)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code03_PermutationLCS {

	public static int MAXN = 100001;

	public static int[] a = new int[MAXN];

	public static int[] b = new int[MAXN];

	public static int[] where = new int[MAXN];

	public static int[] ends = new int[MAXN];

	public static int n;

	/**
	 * 主函数 - 程序入口点
	 * 
	 * 输入输出处理:
	 * 使用BufferedReader和StreamTokenizer提高输入效率
	 * 使用PrintWriter提高输出效率
	 * 
	 * 算法流程:
	 * 1. 读取两个排列
	 * 2. 调用compute方法计算LCS长度
	 * 3. 输出结果
	 * 
	 * 工程化考量:
	 * - 处理多组测试用例
	 * - 资源释放和异常处理
	 */
	public static void main(String[] args) throws IOException {
		// 高效输入输出流初始化
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 处理多组测试用例
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			
			// 读取第一个排列
			for (int i = 0; i < n; i++) {
				in.nextToken();
				a[i] = (int) in.nval;
			}
			
			// 读取第二个排列
			for (int i = 0; i < n; i++) {
				in.nextToken();
				b[i] = (int) in.nval;
			}
			
			// 计算并输出LCS长度
			out.println(compute());
		}
		
		// 资源释放
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算两个排列的最长公共子序列长度
	 * 
	 * 算法原理:
	 * 1. 创建位置映射: where[a[i]] = i
	 * 2. 将第二个排列转换为位置序列: b[i] = where[b[i]]
	 * 3. 问题转化为求位置序列的最长递增子序列
	 * 
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 
	 * @return 两个排列的最长公共子序列长度
	 */
	public static int compute() {
		// 创建位置映射: where[a[i]] = i
		for (int i = 0; i < n; i++) {
			where[a[i]] = i;
		}
		
		// 将第二个排列转换为位置序列
		for (int i = 0; i < n; i++) {
			b[i] = where[b[i]];
		}
		
		// 计算位置序列的最长递增子序列
		return lis();
	}

	/**
	 * 计算最长递增子序列长度
	 * 讲解072 - 最长递增子序列及其扩展
	 * 
	 * 算法思路: 贪心+二分查找
	 * 维护数组ends，ends[i]表示长度为i+1的递增子序列的最小结尾元素
	 * 遍历序列，对于每个元素:
	 * 1. 如果大于ends的最后一个元素，扩展ends
	 * 2. 否则替换ends中第一个大于等于该元素的位置
	 * 
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 
	 * @return 最长递增子序列长度
	 */
	public static int lis() {
		int len = 0;  // 当前最长递增子序列的长度
		
		for (int i = 0, find; i < n; i++) {
			// 使用二分查找找到第一个大于等于b[i]的位置
			find = bs(len, b[i]);
			
			if (find == -1) {
				// 没有找到，可以扩展ends
				ends[len++] = b[i];
			} else {
				// 替换该位置，使得结尾元素更小
				ends[find] = b[i];
			}
		}
		
		return len;
	}

	/**
	 * 二分查找辅助函数
	 * 在ends[0..len-1]中查找第一个大于等于num的位置
	 * 
	 * 算法思路: 标准二分查找变形
	 * 查找第一个大于等于目标值的位置
	 * 
	 * @param len 查找范围的长度
	 * @param num 目标值
	 * @return 第一个大于等于num的位置，如果不存在返回-1
	 */
	public static int bs(int len, int num) {
		int l = 0, r = len - 1, m, ans = -1;
		
		while (l <= r) {
			m = (l + r) / 2;
			
			if (ends[m] >= num) {
				// 找到一个可能的位置，继续向左查找更早的位置
				ans = m;
				r = m - 1;
			} else {
				// 当前位置的值小于目标值，向右查找
				l = m + 1;
			}
		}
		
		return ans;
	}
	
	/*
	 * 类似题目1：最长公共子序列（LeetCode 1143）
	 * 题目描述：
	 * 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
	 * 若这两个字符串没有公共子序列，则返回 0。
	 * 
	 * 示例：
	 * 输入：text1 = "abcde", text2 = "ace"
	 * 输出：3
	 * 解释：最长公共子序列是 "ace"，它的长度为 3。
	 * 
	 * 解题思路：
	 * 这是经典的LCS问题，使用动态规划解决。
	 * dp[i][j] 表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
	 * 状态转移方程：
	 * 如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
	 * 否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])
	 */
	
	// 最长公共子序列 - 二维动态规划解法
	public static int longestCommonSubsequence1(String text1, String text2) {
		int m = text1.length();
		int n = text2.length();
		
		// dp[i][j] 表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
		int[][] dp = new int[m + 1][n + 1];
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (text1.charAt(i-1) == text2.charAt(j-1)) {
					dp[i][j] = dp[i-1][j-1] + 1;
				} else {
					dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
				}
			}
		}
		
		return dp[m][n];
	}
	
	// 最长公共子序列 - 一维动态规划解法（空间优化）
	public static int longestCommonSubsequence2(String text1, String text2) {
		int m = text1.length();
		int n = text2.length();
		
		// 使用一维数组优化空间
		int[] dp = new int[n + 1];
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			int pre = 0; // 保存dp[i-1][j-1]的值
			for (int j = 1; j <= n; j++) {
				int temp = dp[j]; // 保存当前dp[j]的值，用于下一次循环作为dp[i-1][j-1]
				if (text1.charAt(i-1) == text2.charAt(j-1)) {
					dp[j] = pre + 1;
				} else {
					dp[j] = Math.max(dp[j], dp[j-1]);
				}
				pre = temp;
			}
		}
		
		return dp[n];
	}
	
	/*
	 * 类似题目2：最长重复子数组（LeetCode 718）
	 * 题目描述：
	 * 给两个整数数组 A 和 B ，返回两个数组中公共的、长度最长的子数组的长度。
	 * 
	 * 示例：
	 * 输入：A = [1,2,3,2,1], B = [3,2,1,4,7]
	 * 输出：3
	 * 解释：长度最长的公共子数组是 [3,2,1]。
	 * 
	 * 解题思路：
	 * 这个问题与LCS类似，但要求是连续的子数组。
	 * dp[i][j] 表示以A[i-1]和B[j-1]结尾的公共子数组的长度
	 * 状态转移方程：
	 * 如果A[i-1] == B[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
	 * 否则dp[i][j] = 0
	 */
	
	// 最长重复子数组 - 动态规划解法
	public static int findLength(int[] A, int[] B) {
		int m = A.length;
		int n = B.length;
		
		// dp[i][j] 表示以A[i-1]和B[j-1]结尾的公共子数组的长度
		int[][] dp = new int[m + 1][n + 1];
		int maxLen = 0;
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (A[i-1] == B[j-1]) {
					dp[i][j] = dp[i-1][j-1] + 1;
					maxLen = Math.max(maxLen, dp[i][j]);
				} else {
					dp[i][j] = 0;
				}
			}
		}
		
		return maxLen;
	}
	
	// 最长重复子数组 - 一维动态规划解法（空间优化）
	public static int findLength2(int[] A, int[] B) {
		int m = A.length;
		int n = B.length;
		
		// 使用一维数组优化空间
		int[] dp = new int[n + 1];
		int maxLen = 0;
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			int pre = 0; // 保存dp[i-1][j-1]的值
			for (int j = 1; j <= n; j++) {
				int temp = dp[j]; // 保存当前dp[j]的值，用于下一次循环作为dp[i-1][j-1]
				if (A[i-1] == B[j-1]) {
					dp[j] = pre + 1;
					maxLen = Math.max(maxLen, dp[j]);
				} else {
					dp[j] = 0;
				}
				pre = temp;
			}
		}
		
		return maxLen;
	}
	
	/*
	 * 类似题目3：不同的子序列（LeetCode 115）
	 * 题目描述：
	 * 给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。
	 * 
	 * 示例：
	 * 输入：s = "rabbbit", t = "rabbit"
	 * 输出：3
	 * 解释：有3种可以从 s 中得到 "rabbit" 的方案。
	 * 
	 * 解题思路：
	 * 这是一个动态规划问题，类似于LCS但求的是方案数。
	 * dp[i][j] 表示s的前i个字符的子序列中t的前j个字符出现的次数
	 * 状态转移方程：
	 * 如果s[i-1] == t[j-1]，则dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
	 * 否则dp[i][j] = dp[i-1][j]
	 */
	
	// 不同的子序列 - 动态规划解法
	public static int numDistinct(String s, String t) {
		int m = s.length();
		int n = t.length();
		
		// dp[i][j] 表示s的前i个字符的子序列中t的前j个字符出现的次数
		long[][] dp = new long[m + 1][n + 1];
		
		// 初始化：空字符串是任何字符串的一个子序列
		for (int i = 0; i <= m; i++) {
			dp[i][0] = 1;
		}
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (s.charAt(i-1) == t.charAt(j-1)) {
					dp[i][j] = dp[i-1][j-1] + dp[i-1][j];
				} else {
					dp[i][j] = dp[i-1][j];
				}
			}
		}
		
		return (int)dp[m][n];
	}
	
	/*
	 * 类似题目4：编辑距离（LeetCode 72）
	 * 题目描述：
	 * 给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数 。
	 * 你可以对一个单词进行如下三种操作：
	 * 插入一个字符
	 * 删除一个字符
	 * 替换一个字符
	 * 
	 * 示例：
	 * 输入：word1 = "horse", word2 = "ros"
	 * 输出：3
	 * 解释：
	 * horse -> rorse (将 'h' 替换为 'r')
	 * rorse -> rose (删除 'r')
	 * rose -> ros (删除 'e')
	 * 
	 * 解题思路：
	 * 这是一个经典的动态规划问题。
	 * dp[i][j] 表示word1的前i个字符转换成word2的前j个字符所需的最少操作数
	 * 状态转移方程：
	 * 如果word1[i-1] == word2[j-1]，则dp[i][j] = dp[i-1][j-1]
	 * 否则dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
	 */
	
	// 编辑距离 - 动态规划解法
	public static int minDistance(String word1, String word2) {
		int m = word1.length();
		int n = word2.length();
		
		// dp[i][j] 表示word1的前i个字符转换成word2的前j个字符所需的最少操作数
		int[][] dp = new int[m + 1][n + 1];
		
		// 初始化
		for (int i = 0; i <= m; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= n; j++) {
			dp[0][j] = j;
		}
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (word1.charAt(i-1) == word2.charAt(j-1)) {
					dp[i][j] = dp[i-1][j-1];
				} else {
					dp[i][j] = Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1;
				}
			}
		}
		
		return dp[m][n];
	}
}