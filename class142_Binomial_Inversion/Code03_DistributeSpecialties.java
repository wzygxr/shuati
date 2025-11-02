package class145;

// 分特产
// 一共有m种特产，arr[i]表示i种特产有几个
// 一共有n个同学，每个同学至少要得到一个特产
// 返回分配特产的方法数，答案对 1000000007 取模
// 0 <= n、m <= 1000
// 0 <= arr[i] <= 1000
// 测试链接 : https://www.luogu.com.cn/problem/P5505
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
二项式反演在分特产问题中的应用：

问题描述：
有m种特产，第i种特产有arr[i]个。
有n个同学，每个同学至少要得到一个特产。
求分配特产的方法数。

解题思路：
设f(i)表示恰好有i个同学没有分到特产的方案数
设g(i)表示至少有i个同学没有分到特产的方案数（钦定i个同学不分特产）

显然，g(i)更容易计算：
1. 从n个同学中选出i个同学不分特产，方案数为C(n, i)
2. 对于每种特产j，将arr[j]个特产分给剩下的(n-i)个同学，这是经典的插板法问题
   方案数为C(arr[j] + (n-i) - 1, (n-i) - 1) = C(arr[j] + n - i - 1, n - i - 1)
3. 所有特产的分配方案相乘得到总方案数

因此：g(i) = C(n, i) * Π(j=1 to m) C(arr[j] + n - i - 1, n - i - 1)

根据二项式反演公式3：
f(0) = Σ(i=0 to n) (-1)^i * C(i, 0) * g(i)
     = Σ(i=0 to n) (-1)^i * g(i)

相关题目：
1. 洛谷 P5505 [JSOI2011] 分特产（标准题目）
2. BZOJ 4710 分特产（相同题目）
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_DistributeSpecialties {

	public static int MAXN = 1001;

	public static int MAXK = MAXN * 2;

	public static int MOD = 1000000007;

	public static int[] arr = new int[MAXN];

	public static long[][] c = new long[MAXK][MAXK];

	public static long[] g = new long[MAXN];

	public static int n, k, m;

	public static long compute() {
		// 预处理组合数
		for (int i = 0; i <= k; i++) {
			c[i][0] = 1;
			for (int j = 1; j <= i; j++) {
				c[i][j] = (c[i - 1][j] + c[i - 1][j - 1]) % MOD;
			}
		}
		
		// 计算g[i]，表示至少i个同学没有分到特产的方案数
		for (int i = 0; i < n; i++) {
			// C(n, i) 从n个同学中选出i个同学不分特产
			g[i] = c[n][i];
			// 对于每种特产，计算分给剩下(n-i)个同学的方案数
			for (int j = 1; j <= m; j++) {
				// 第j种特产有arr[j]个，分给(n-i)个同学的方案数是C(arr[j] + (n-i) - 1, (n-i) - 1)
				// 即C(arr[j] + n - i - 1, n - i - 1)
				g[i] = (int) ((g[i] * c[arr[j] + n - i - 1][n - i - 1]) % MOD);
			}
		}
		g[n] = 0; // 所有同学都不分特产显然不可能
		
		// 使用二项式反演计算f(0)，即恰好0个同学没有分到特产的方案数
		long ans = 0;
		for (int i = 0; i <= n; i++) {
			if ((i & 1) == 0) {
				// i为偶数，(-1)^i = 1
				ans = (ans + g[i]) % MOD;
			} else {
				// i为奇数，(-1)^i = -1，用(MOD-1)代替-1
				ans = (ans + g[i] * (MOD - 1) % MOD) % MOD;
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
		k = n * 2;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

}