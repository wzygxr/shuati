package class085;

/**
 * 萌数问题
 * 
 * 题目描述：
 * 如果一个数字，存在长度至少为2的回文子串，那么这种数字被称为萌数。
 * 比如101、110、111、1234321、45568。
 * 求[l,r]范围上，有多少个萌数。
 * 由于答案可能很大，所以输出答案对1000000007求余。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。
 * 状态定义：dp[i][pp][p][free] 表示处理到第i位，前前一位数字是pp，前一位数字是p，是否受到上界限制的状态下的方案数。
 * 
 * 算法分析：
 * 时间复杂度：O(L * 10 * 10 * 2) 其中L是数字的位数
 * 空间复杂度：O(L * 10 * 10 * 2) 用于存储DP状态
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
 * - 洛谷P3413: https://www.luogu.com.cn/problem/P3413
 * 
 * 多语言实现：
 * - Java: Code02_MengNumber.java
 * - Python: 暂无
 * - C++: 暂无
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code02_MengNumber {

	public static int MOD = 1000000007;

	public static int MAXN = 1001;

	public static int[][][][] dp = new int[MAXN][11][11][2];

	public static void build(int n) {
		for (int a = 0; a < n; a++) {
			for (int b = 0; b <= 10; b++) {
				for (int c = 0; c <= 10; c++) {
					for (int d = 0; d <= 1; d++) {
						dp[a][b][c][d] = -1;
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		String[] strs = br.readLine().split(" ");
		out.println(compute(strs[0].toCharArray(), strs[1].toCharArray()));
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算区间[l,r]内萌数的个数
	 * 
	 * @param l 区间下界字符数组
	 * @param r 区间上界字符数组
	 * @return 区间内萌数的个数
	 */
	public static int compute(char[] l, char[] r) {
		int ans = (cnt(r) - cnt(l) + MOD) % MOD;
		if (check(l)) {
			ans = (ans + 1) % MOD;
		}
		return ans;
	}

	/**
	 * 返回0~num范围上萌数有多少个
	 * 
	 * @param num 上界字符数组
	 * @return 0~num范围内萌数的个数
	 */
	public static int cnt(char[] num) {
		if (num[0] == '0') {
			return 0;
		}
		int n = num.length;
		long all = 0;
		long base = 1;
		for (int i = n - 1; i >= 0; i--) {
			// 不理解的话看一下，讲解041-同余原理
			all = (all + base * (num[i] - '0')) % MOD;
			base = (base * 10) % MOD;
		}
		build(n);
		return (int) ((all - f(num, 0, 10, 10, 0) + MOD) % MOD);
	}

	/**
	 * 数位DP核心递归函数
	 * 
	 * @param num 数字字符数组
	 * @param i 当前处理到第i位
	 * @param pp 前前一位数字，如果值是10，则表示那一位没有选择过数字
	 * @param p 前一位数字，如果值是10，则表示那一位没有选择过数字
	 * @param free 如果之前的位已经确定比num小，那么free == 1，表示接下的数字可以自由选择
	 *             如果之前的位和num一样，那么free == 0，表示接下的数字不能大于num当前位的数字
	 * @return <=num且不是萌数的数字有多少个
	 */
	public static int f(char[] num, int i, int pp, int p, int free) {
		if (i == num.length) {
			return 1;
		}
		if (dp[i][pp][p][free] != -1) {
			return dp[i][pp][p][free];
		}
		int ans = 0;
		if (free == 0) {
			if (p == 10) {
				// 当前来到的就是num的最高位
				ans = (ans + f(num, i + 1, 10, 10, 1)) % MOD; // 当前位不选数字
				for (int cur = 1; cur < num[i] - '0'; cur++) {
					ans = (ans + f(num, i + 1, p, cur, 1)) % MOD;
				}
				ans = (ans + f(num, i + 1, p, num[i] - '0', 0)) % MOD;
			} else {
				// free == 0，之前和num一样，此时不能自由选择数字
				// 前一位p，选择过数字，p -> 0 ~ 9
				for (int cur = 0; cur < num[i] - '0'; cur++) {
					if (pp != cur && p != cur) {
						ans = (ans + f(num, i + 1, p, cur, 1)) % MOD;
					}
				}
				if (pp != num[i] - '0' && p != num[i] - '0') {
					ans = (ans + f(num, i + 1, p, num[i] - '0', 0)) % MOD;
				}
			}
		} else {
			if (p == 10) {
				// free == 1，能自由选择数字
				// 从来没选过数字
				ans = (ans + f(num, i + 1, 10, 10, 1)) % MOD; // 依然不选数字
				for (int cur = 1; cur <= 9; cur++) {
					ans = (ans + f(num, i + 1, p, cur, 1)) % MOD;
				}
			} else {
				// free == 1，能自由选择数字
				// 之前选择过数字
				for (int cur = 0; cur <= 9; cur++) {
					if (pp != cur && p != cur) {
						ans = (ans + f(num, i + 1, p, cur, 1)) % MOD;
					}
				}
			}
		}
		dp[i][pp][p][free] = ans;
		return ans;
	}

	/**
	 * 检查一个数字是否为萌数
	 * 
	 * @param num 数字字符数组
	 * @return 如果是萌数返回true，否则返回false
	 */
	public static boolean check(char[] num) {
		for (int pp = -2, p = -1, i = 0; i < num.length; pp++, p++, i++) {
			if (pp >= 0 && num[pp] == num[i]) {
				return true;
			}
			if (p >= 0 && num[p] == num[i]) {
				return true;
			}
		}
		return false;
	}

}