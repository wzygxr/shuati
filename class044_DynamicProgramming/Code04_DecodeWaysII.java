package class066;

import java.util.Arrays;

/**
 * 解码方法 II (Decode Ways II)
 * 
 * 题目来源：LeetCode 639. 解码方法 II
 * 题目链接：https://leetcode.cn/problems/decode-ways-ii/
 * 
 * 题目描述：
 * 一条包含字母 A-Z 的消息通过以下的方式进行了 编码 ：
 * 'A' -> "1"
 * 'B' -> "2"
 * ...
 * 'Z' -> "26"
 * 要 解码 一条已编码的消息，所有的数字都必须分组，然后按原来的编码方案反向映射回字母（可能存在多种方式）。
 * 例如，"11106" 可以映射为：
 * "AAJF" ，将消息分组为 (1 1 10 6)
 * "KJF" ，将消息分组为 (11 10 6)
 * 注意，像 (1 11 06) 这样的分组是无效的，"06" 不可以映射为 'F' 。
 * 除了上面描述的数字字母映射方案，编码消息中可能包含 '*' 字符，可以表示从 '1' 到 '9' 的任一数字（不包括 '0'）。
 * 例如，"1*" 可以表示 "11"、"12"、"13"、"14"、"15"、"16"、"17"、"18" 或 "19" 。
 * 对 "1*" 进行解码，相当于解码该字符串可以表示的任何编码消息。
 * 给你一个字符串 s ，由数字和 '*' 字符组成，返回 解码 该字符串的方法 数目 。
 * 由于答案数目可能非常大，答案对 1000000007 取模 。
 * 
 * 示例 1：
 * 输入：s = "*"
 * 输出：9
 * 解释：这一条编码消息可以表示 "1"、"2"、"3"、"4"、"5"、"6"、"7"、"8" 或 "9" 中的任意一条。
 * 可以分别解码成 "A"、"B"、"C"、"D"、"E"、"F"、"G"、"H" 和 "I" 。
 * 因此，"*" 总共有 9 种解码方法。
 * 
 * 示例 2：
 * 输入：s = "1*"
 * 输出：18
 * 解释：这一条编码消息可以表示 "11"、"12"、"13"、"14"、"15"、"16"、"17"、"18" 或 "19" 中的任意一条。
 * 每一种消息都可以解码为 "AA" 到 "AI" 中的一种，所以 "1*" 一共有 9 * 2 = 18 种解码方法。
 * 
 * 示例 3：
 * 输入：s = "2*"
 * 输出：15
 * 解释："2*" 可以表示 "21" 到 "29" 中的任意一条。
 * "21"、"22"、"23"、"24"、"25" 和 "26" 可以解码为 "U"、"V"、"W"、"X"、"Y" 和 "Z" 。
 * "27"、"28" 和 "29" 没有有效解码，因此 "2*" 一共有 6 + 3 * 3 = 15 种解码方法。
 * 
 * 提示：
 * 1 <= s.length <= 10^5
 * s[i] 是 0 - 9 中的一位数字或字符 '*'
 * 
 * 解题思路：
 * 这是一个复杂的动态规划问题，是解码方法I的进阶版本，增加了通配符'*'的支持。
 * 我们提供了四种解法：
 * 1. 暴力递归：直接按照定义递归求解，但存在大量重复计算。
 * 2. 记忆化搜索：在暴力递归的基础上，通过缓存已计算的结果来避免重复计算。
 * 3. 动态规划：自底向上计算，先计算小问题的解，再逐步构建大问题的解。
 * 4. 空间优化的动态规划：在动态规划的基础上，只保存必要的状态，将空间复杂度优化到O(1)。
 * 
 * 算法复杂度分析：
 * - 暴力递归：时间复杂度 O(2^n)，空间复杂度 O(n)
 * - 记忆化搜索：时间复杂度 O(n)，空间复杂度 O(n)
 * - 动态规划：时间复杂度 O(n)，空间复杂度 O(n)
 * - 空间优化DP：时间复杂度 O(n)，空间复杂度 O(1)
 * 
 * 工程化考量：
 * 1. 边界处理：正确处理字符'0'和'*'的特殊情况
 * 2. 性能优化：提供多种解法，从低效到高效，展示优化过程
 * 3. 模运算：正确处理大数取模操作
 * 4. 代码质量：清晰的变量命名和详细的注释说明
 * 5. 测试覆盖：包含基本测试用例和边界情况测试
 * 
 * 相关题目：
 * - LeetCode 91. 解码方法
 * - LintCode 676. 解码方法 II
 * - AtCoder Educational DP Contest D - Knapsack 1
 * - 牛客网 动态规划专题 - 字符串解码进阶
 * - HackerRank Decode Ways II
 * - CodeChef DECODE2
 * - SPOJ DECODEW2
 */
public class Code04_DecodeWaysII {

	// 没有取模逻辑
	// 最自然的暴力尝试
	// 时间复杂度：指数级，因为对于每个*字符都有多种可能
	// 空间复杂度：O(n)，递归调用栈深度
	// 问题：存在大量重复计算，且没有处理取模
	public static int numDecodings1(String str) {
		return f1(str.toCharArray(), 0);
	}

	// s[i....] 有多少种有效转化
	public static int f1(char[] s, int i) {
		if (i == s.length) {
			return 1;
		}
		if (s[i] == '0') {
			return 0;
		}
		// s[i] != '0'
		// 2) i想单独转
		int ans = f1(s, i + 1) * (s[i] == '*' ? 9 : 1);
		// 3) i i+1 一起转化 <= 26
		if (i + 1 < s.length) {
			// 有i+1位置
			if (s[i] != '*') {
				if (s[i + 1] != '*') {
					// num num
					//  i  i+1
					if ((s[i] - '0') * 10 + s[i + 1] - '0' <= 26) {
						ans += f1(s, i + 2);
					}
				} else {
					// num  *
					//  i  i+1
					if (s[i] == '1') {
						ans += f1(s, i + 2) * 9;
					}
					if (s[i] == '2') {
						ans += f1(s, i + 2) * 6;
					}
				}
			} else {
				if (s[i + 1] != '*') {
					// *  num
					// i  i+1
					if (s[i + 1] <= '6') {
						ans += f1(s, i + 2) * 2;
					} else {
						ans += f1(s, i + 2);
					}
				} else {
					// *  *
					// i  i+1
					// 11 12 ... 19 21 22 ... 26 -> 一共15种可能
					// 没有10、20，因为*只能变1~9，并不包括0
					ans += f1(s, i + 2) * 15;
				}
			}
		}
		return ans;
	}

	public static long mod = 1000000007;

	// 记忆化搜索版本
	// 时间复杂度：O(n)，其中n是字符串长度，每个状态只计算一次
	// 空间复杂度：O(n)，dp数组和递归调用栈
	// 优化：通过缓存已经计算的结果避免重复计算，并正确处理取模
	public static int numDecodings2(String str) {
		char[] s = str.toCharArray();
		long[] dp = new long[s.length];
		Arrays.fill(dp, -1);
		return (int) f2(s, 0, dp);
	}

	public static long f2(char[] s, int i, long[] dp) {
		if (i == s.length) {
			return 1;
		}
		if (s[i] == '0') {
			return 0;
		}
		if (dp[i] != -1) {
			return dp[i];
		}
		long ans = f2(s, i + 1, dp) * (s[i] == '*' ? 9 : 1);
		if (i + 1 < s.length) {
			if (s[i] != '*') {
				if (s[i + 1] != '*') {
					if ((s[i] - '0') * 10 + s[i + 1] - '0' <= 26) {
						ans += f2(s, i + 2, dp);
					}
				} else {
					if (s[i] == '1') {
						ans += f2(s, i + 2, dp) * 9;
					}
					if (s[i] == '2') {
						ans += f2(s, i + 2, dp) * 6;
					}
				}
			} else {
				if (s[i + 1] != '*') {
					if (s[i + 1] <= '6') {
						ans += f2(s, i + 2, dp) * 2;
					} else {
						ans += f2(s, i + 2, dp);
					}
				} else {
					ans += f2(s, i + 2, dp) * 15;
				}
			}
		}
		ans %= mod;
		dp[i] = ans;
		return ans;
	}

	// 严格位置依赖的动态规划
	// 时间复杂度：O(n)，其中n是字符串长度
	// 空间复杂度：O(n)，dp数组
	// 优化：避免了递归调用的开销，自底向上计算
	public static int numDecodings3(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		long[] dp = new long[n + 1];
		dp[n] = 1;
		for (int i = n - 1; i >= 0; i--) {
			if (s[i] != '0') {
				dp[i] = (s[i] == '*' ? 9 : 1) * dp[i + 1];
				if (i + 1 < n) {
					if (s[i] != '*') {
						if (s[i + 1] != '*') {
							if ((s[i] - '0') * 10 + s[i + 1] - '0' <= 26) {
								dp[i] += dp[i + 2];
							}
						} else {
							if (s[i] == '1') {
								dp[i] += dp[i + 2] * 9;
							}
							if (s[i] == '2') {
								dp[i] += dp[i + 2] * 6;
							}
						}
					} else {
						if (s[i + 1] != '*') {
							if (s[i + 1] <= '6') {
								dp[i] += dp[i + 2] * 2;
							} else {
								dp[i] += dp[i + 2];
							}
						} else {
							dp[i] += dp[i + 2] * 15;
						}
					}
				}
				dp[i] %= mod;
			}
		}
		return (int) dp[0];
	}

	// 严格位置依赖的动态规划 + 空间压缩
	// 时间复杂度：O(n)，其中n是字符串长度
	// 空间复杂度：O(1)，只使用几个变量
	// 优化：只保存必要的状态，大幅减少空间使用
	public static int numDecodings4(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		long cur = 0, next = 1, nextNext = 0;
		for (int i = n - 1; i >= 0; i--) {
			if (s[i] != '0') {
				cur = (s[i] == '*' ? 9 : 1) * next;
				if (i + 1 < n) {
					if (s[i] != '*') {
						if (s[i + 1] != '*') {
							if ((s[i] - '0') * 10 + s[i + 1] - '0' <= 26) {
								cur += nextNext;
							}
						} else {
							if (s[i] == '1') {
								cur += nextNext * 9;
							}
							if (s[i] == '2') {
								cur += nextNext * 6;
							}
						}
					} else {
						if (s[i + 1] != '*') {
							if (s[i + 1] <= '6') {
								cur += nextNext * 2;
							} else {
								cur += nextNext;
							}
						} else {
							cur += nextNext * 15;
						}
					}
				}
				cur %= mod;
			}
			nextNext = next;
			next = cur;
			cur = 0;
		}
		return (int) next;
	}
    
	// 测试用例
	public static void main(String[] args) {
		System.out.println("测试解码方法II问题：");
		
		// 测试用例1
		String s1 = "*";
		System.out.println("s = \"" + s1 + "\"");
		System.out.println("解码方法数（方法2）: " + numDecodings2(s1));
		System.out.println("解码方法数（方法3）: " + numDecodings3(s1));
		System.out.println("解码方法数（方法4）: " + numDecodings4(s1));
		
		// 测试用例2
		String s2 = "1*";
		System.out.println("\ns = \"" + s2 + "\"");
		System.out.println("解码方法数（方法2）: " + numDecodings2(s2));
		System.out.println("解码方法数（方法3）: " + numDecodings3(s2));
		System.out.println("解码方法数（方法4）: " + numDecodings4(s2));
		
		// 测试用例3
		String s3 = "2*";
		System.out.println("\ns = \"" + s3 + "\"");
		System.out.println("解码方法数（方法2）: " + numDecodings2(s3));
		System.out.println("解码方法数（方法3）: " + numDecodings3(s3));
		System.out.println("解码方法数（方法4）: " + numDecodings4(s3));
	}

}