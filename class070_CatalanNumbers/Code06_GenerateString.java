package class147;

/**
 * 生成字符串问题 - 卡特兰数变形应用
 * 
 * 问题描述：
 * 有n个1和m个0，要组成n+m长度的数列，保证任意前缀上，1的数量 >= 0的数量
 * 返回有多少种排列方法，答案对 20100403 取模
 * 
 * 数学背景：
 * 这是卡特兰数问题的变形，结果为第min(n,m)项卡特兰数的变形。
 * 当n = m时就是标准卡特兰数。
 * 
 * 解法思路：
 * 1. 使用组合公式：C(n+m, m) - C(n+m, m-1)
 * 2. 利用预处理阶乘和逆元表优化计算
 * 
 * 相关题目链接：
 * - 洛谷 P1641 生成字符串: https://www.luogu.com.cn/problem/P1641
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
 * - HDU 1023 Train Problem II: http://acm.hdu.edu.cn/showproblem.php?pid=1023
 * - POJ 2084 Catalan Numbers: http://poj.org/problem?id=2084
 * 
 * 时间复杂度分析：
 * - 预处理阶乘和逆元：O(n+m)
 * - 计算组合数：O(1)
 * 
 * 空间复杂度分析：
 * - 存储阶乘和逆元表：O(n+m)
 * 
 * 工程化考量：
 * - 1 <= m <= n <= 10^6
 * - 答案对 20100403 取模
 * - 提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code06_GenerateString {

	public static int MOD = 20100403;

	public static int MAXN = 2000001;

	public static long[] fac = new long[MAXN];

	public static long[] inv = new long[MAXN];

	public static void build(int n) {
		fac[0] = inv[0] = 1;
		fac[1] = 1;
		for (int i = 2; i <= n; i++) {
			fac[i] = ((long) i * fac[i - 1]) % MOD;
		}
		inv[n] = power(fac[n], MOD - 2);
		for (int i = n - 1; i >= 1; i--) {
			inv[i] = ((long) (i + 1) * inv[i + 1]) % MOD;
		}
	}

	public static long power(long x, long p) {
		long ans = 1;
		while (p > 0) {
			if ((p & 1) == 1) {
				ans = (ans * x) % MOD;
			}
			x = (x * x) % MOD;
			p >>= 1;
		}
		return ans;
	}

	public static long c(int n, int k) {
		return (((fac[n] * inv[k]) % MOD) * inv[n - k]) % MOD;
	}

	public static long compute(int n, int m) {
		build(n + m);
		return (c(n + m, m) - c(n + m, m - 1) + MOD) % MOD;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		out.println(compute(n, m));
		out.flush();
		out.close();
		br.close();
	}

}
