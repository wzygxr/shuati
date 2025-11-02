package class085;

/**
 * Windy数加强版问题
 * 
 * 题目描述：
 * windy数，加强版。需要改成long类型，除此之外和课上讲的完全一样。
 * 不含前导零且相邻两个数字之差至少为2的正整数被称为windy数。
 * windy想知道[a,b]范围上总共有多少个windy数。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。
 * 状态定义：dp[len][pre][free] 表示处理到第len位，前一位数字是pre，是否受到上界限制的状态下的方案数。
 * 
 * 算法分析：
 * 时间复杂度：O(L * 10 * 2) 其中L是数字的位数
 * 空间复杂度：O(L * 10 * 2) 用于存储DP状态
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。
 * 
 * 工程化考量：
 * 1. 异常处理：处理输入边界情况
 * 2. 边界测试：测试各种边界情况
 * 3. 性能优化：使用记忆化搜索避免重复计算
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - 洛谷P13085: https://www.luogu.com.cn/problem/P13085
 * 
 * 多语言实现：
 * - Java: FollowUpWindy.java
 * - Python: 暂无
 * - C++: 暂无
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class FollowUpWindy {

	public static int MAXLEN = 21;

	public static long[][][] dp = new long[MAXLEN][11][2];

	public static void build(int len) {
		for (int i = 0; i <= len; i++) {
			for (int j = 0; j <= 10; j++) {
				dp[i][j][0] = -1;
				dp[i][j][1] = -1;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			long a = (long) in.nval;
			in.nextToken();
			long b = (long) in.nval;
			out.println(compute(a, b));
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算区间[a,b]内windy数的个数
	 * 
	 * @param a 区间下界
	 * @param b 区间上界
	 * @return 区间内windy数的个数
	 */
	public static long compute(long a, long b) {
		return cnt(b) - cnt(a - 1);
	}

	/**
	 * 求0~num范围上，windy数的个数
	 * 
	 * @param num 上界
	 * @return 0~num范围内windy数的个数
	 */
	public static long cnt(long num) {
		if (num == 0) {
			return 1;
		}
		int len = 1;
		long offset = 1;
		long tmp = num / 10;
		while (tmp > 0) {
			len++;
			offset *= 10;
			tmp /= 10;
		}
		build(len);
		return f(num, offset, len, 10, 0);
	}

	/**
	 * 数位DP核心递归函数
	 * 
	 * @param num 数字n
	 * @param offset 完全由len决定，为了方便提取num中某一位数字
	 * @param len 从num的高位开始，还剩下len位没有决定
	 * @param pre 前一位的数字，如果pre == 10，表示从来没有选择过数字
	 * @param free 如果之前的位已经确定比num小，那么free == 1，表示接下的数字可以自由选择
	 *             如果之前的位和num一样，那么free == 0，表示接下的数字不能大于num当前位的数字
	 * @return <=num的windy数有多少个
	 */
	public static long f(long num, long offset, int len, int pre, int free) {
		if (len == 0) {
			return 1;
		}
		if (dp[len][pre][free] != -1) {
			return dp[len][pre][free];
		}
		int cur = (int) (num / offset % 10);
		long ans = 0;
		if (free == 0) {
			if (pre == 10) {
				ans += f(num, offset / 10, len - 1, 10, 1);
				for (int i = 1; i < cur; i++) {
					ans += f(num, offset / 10, len - 1, i, 1);
				}
				ans += f(num, offset / 10, len - 1, cur, 0);
			} else {
				for (int i = 0; i <= 9; i++) {
					if (i <= pre - 2 || i >= pre + 2) {
						if (i < cur) {
							ans += f(num, offset / 10, len - 1, i, 1);
						} else if (i == cur) {
							ans += f(num, offset / 10, len - 1, cur, 0);
						}
					}
				}
			}
		} else {
			if (pre == 10) {
				ans += f(num, offset / 10, len - 1, 10, 1);
				for (int i = 1; i <= 9; i++) {
					ans += f(num, offset / 10, len - 1, i, 1);
				}
			} else {
				for (int i = 0; i <= 9; i++) {
					if (i <= pre - 2 || i >= pre + 2) {
						ans += f(num, offset / 10, len - 1, i, 1);
					}
				}
			}
		}
		dp[len][pre][free] = ans;
		return ans;
	}

}