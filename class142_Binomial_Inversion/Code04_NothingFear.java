package class145;

// 已经没有什么好害怕的了
// 给定两个长度为n的数组，a[i]表示第i个糖果的能量，b[i]表示第i个药片的能量
// 所有能量数值都不相同，每一个糖果要选一个药片进行配对
// 如果配对之后，糖果能量 > 药片能量，称为糖果大的配对
// 如果配对之后，糖果能量 < 药片能量，称为药片大的配对
// 希望做到，糖果大的配对数量 = 药片大的配对数量 + k
// 返回配对方法数，答案对 1000000009 取模
// 举例，a = [5, 35, 15, 45]，b = [40, 20, 10, 30]，k = 2，返回4，因为有4种配对方法
// (5-40，35-20，15-10，45-30)、(5-40，35-30，15-10，45-20)
// (5-20，35-30，15-10，45-40)、(5-30，35-20，15-10，45-40)
// 1 <= n <= 2000
// 0 <= k <= n
// 测试链接 : https://www.luogu.com.cn/problem/P4859
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
二项式反演在配对问题中的应用：

问题描述：
给定两个长度为n的数组a和b，将它们两两配对。
设糖果大的配对数为x，药片大的配对数为y，要求x - y = k，即x = y + k。
因为x + y = n，所以有：
  x + y = n
  x - y = k
解得：x = (n + k) / 2, y = (n - k) / 2

如果(n + k)是奇数，则无解。

解题思路：
设f(i)表示恰好有i个糖果大的配对的方案数（即答案）
设g(i)表示至少有i个糖果大的配对的方案数（钦定i个糖果大的配对）

g(i)的计算可以通过DP实现：
1. 将a和b数组排序
2. 对于a[j]，计算有多少个b[k]满足b[k] < a[j]，记为small[j]
3. 使用DP，dp[i][j]表示前i个元素中，选出了j个糖果大的配对的方案数

根据二项式反演：
f(k) = Σ(i=k to n) (-1)^(i-k) * C(i, k) * g(i) * (n-i)!

其中(n-i)!是因为剩下的(n-i)个配对可以任意排列。

相关题目：
1. 洛谷 P4859 已经没有什么好害怕的了（标准题目）
2. 类似题目可以转化为"恰好k个满足某条件"的问题
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code04_NothingFear {

	public static int MAXN = 2001;

	public static int MOD = 1000000009;

	public static int n, k;

	public static int[] a = new int[MAXN];

	public static int[] b = new int[MAXN];

	public static long[] fac = new long[MAXN];

	public static long[][] c = new long[MAXN][MAXN];

	public static long[] small = new long[MAXN];

	public static long[][] dp = new long[MAXN][MAXN];

	public static long[] g = new long[MAXN];

	public static void build() {
		fac[0] = 1;
		for (int i = 1; i <= n; i++) {
			fac[i] = fac[i - 1] * i % MOD;
		}
		for (int i = 0; i <= n; i++) {
			c[i][0] = 1;
			for (int j = 1; j <= i; j++) {
				c[i][j] = (c[i - 1][j] + c[i - 1][j - 1]) % MOD;
			}
		}
	}

	public static long compute() {
		build();
		// 对数组进行排序，便于后续计算
		Arrays.sort(a, 1, n + 1);
		Arrays.sort(b, 1, n + 1);
		
		// 计算对于每个a[i]，有多少个b[j]小于它
		for (int i = 1, cnt = 0; i <= n; i++) {
			while (cnt + 1 <= n && b[cnt + 1] < a[i]) {
				cnt++;
			}
			small[i] = cnt;
		}
		
		// DP计算至少i个糖果大的配对数
		// dp[i][j]表示考虑前i个元素，选出了j个糖果大的配对的方案数
		dp[0][0] = 1;
		for (int i = 1; i <= n; i++) {
			dp[i][0] = dp[i - 1][0];
			for (int j = 1; j <= i; j++) {
				// 不选择第i个元素作为糖果大的配对
				dp[i][j] = (dp[i - 1][j] + 
				           // 选择第i个元素作为糖果大的配对，有(small[i] - j + 1)种选择
				           dp[i - 1][j - 1] * (small[i] - j + 1) % MOD) % MOD;
			}
		}
		
		// g[i]表示至少i个糖果大的配对数，还要乘以剩余元素的排列数
		for (int i = 0; i <= n; i++) {
			g[i] = fac[n - i] * dp[n][i] % MOD;
		}
		
		// 二项式反演计算恰好k个糖果大的配对数
		long ans = 0;
		for (int i = k; i <= n; i++) {
			if (((i - k) & 1) == 0) {
				// (i-k)为偶数，(-1)^(i-k) = 1
				ans = (ans + c[i][k] * g[i] % MOD) % MOD;
			} else {
				// (i-k)为奇数，(-1)^(i-k) = -1
				// 用(MOD-1)代替-1
				ans = (ans + c[i][k] * g[i] % MOD * (MOD - 1) % MOD) % MOD;
			}
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			a[i] = (int) in.nval;
		}
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			b[i] = (int) in.nval;
		}
		if (((n + k) & 1) == 0) {
			// 如果(n+k)是偶数，则k = (n+k)/2
			k = (n + k) / 2;
			out.println(compute());
		} else {
			// 如果(n+k)是奇数，则无解
			out.println(0);
		}
		out.flush();
		out.close();
		br.close();
	}

}