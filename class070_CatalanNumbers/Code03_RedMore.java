package class147;

/**
 * 任意前缀上红多于黑问题 - 卡特兰数应用
 * 
 * 问题描述：
 * 有n个红和n个黑，要组成2n长度的数列，保证任意前缀上，红的数量 >= 黑的数量
 * 返回有多少种排列方法，答案对 100 取模
 * 
 * 数学背景：
 * 这是卡特兰数的经典应用之一，也称为合法的01序列问题。
 * 给定n个0和n个1，能够满足任意前缀序列中0的个数都不少于1的个数的序列数目为第n项卡特兰数。
 * 
 * 解法思路：
 * 1. 动态规划方法：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
 * 2. 递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
 * 
 * 相关题目链接：
 * - 洛谷 P1722 红黑序列: https://www.luogu.com.cn/problem/P1722
 * - LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
 * - LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
 * - Codeforces 1204E Natasha, Sasha and the Prefix Sums: https://codeforces.com/problemset/problem/1204/E
 * - AtCoder ARC145 C: https://atcoder.jp/contests/arc145/tasks/arc145_c
 * 
 * 时间复杂度分析：
 * - 动态规划方法：O(n²)
 * - 递推公式：O(n)
 * 
 * 空间复杂度分析：
 * - 动态规划方法：O(n)
 * - 递推公式：O(1)
 * 
 * 工程化考量：
 * - 因为取模的数字含有很多因子，无法用费马小定理或者扩展欧几里得求逆元
 * - 同时注意到n的范围并不大，直接使用公式4（动态规划方法）
 * - 1 <= n <= 100
 * - 提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_RedMore {

	public static int MOD = 100;

	// 因为取模的数字含有很多因子
	// 无法用费马小定理或者扩展欧几里得求逆元
	// 同时注意到n的范围并不大，直接使用公式4
	public static long compute(int n) {
		long[] f = new long[n + 1];
		f[0] = f[1] = 1;
		for (int i = 2; i <= n; i++) {
			for (int l = 0, r = i - 1; l < i; l++, r--) {
				f[i] = (f[i] + f[l] * f[r] % MOD) % MOD;
			}
		}
		return f[n];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		out.println(compute(n));
		out.flush();
		out.close();
		br.close();
	}

}
