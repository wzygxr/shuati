package class145;

// [SDOI2016]排列计数
// 题目描述：求有多少种1到n的排列a，满足序列恰好有m个位置i，使得a_i = i。
// 1 <= T <= 5*10^5, 1 <= n <= 10^6, 0 <= m <= 10^6
// 测试链接: https://www.luogu.com.cn/problem/P4071

/*
二项式反演在排列计数问题中的应用：

问题描述：
求有多少种1到n的排列a，满足序列恰好有m个位置i，使得a_i = i。

解题思路：
设f(i)表示恰好有i个位置满足a[j] = j的排列数（即答案）
设g(i)表示至少有i个位置满足a[j] = j的排列数

显然，g(i)更容易计算：
1. 先从n个位置中选出i个位置固定，方案数为C(n, i)
2. 剩下的(n-i)个位置必须错排，方案数为D(n-i)
   
因此：g(i) = C(n, i) * D(n-i)

根据二项式反演公式2：
f(m) = Σ(i=m to n) (-1)^(i-m) * C(i, m) * g(i)
     = Σ(i=m to n) (-1)^(i-m) * C(i, m) * C(n, i) * D(n-i)

其中D(k)是k个元素的错排数，可以用递推公式计算：
D(0) = 1, D(1) = 0
D(k) = (k-1) * (D(k-1) + D(k-2))

相关题目：
1. 洛谷 P4071 [SDOI2016]排列计数（标准题目）
2. 洛谷 P1595 信封问题（错排问题）
*/

import java.io.*;
import java.util.*;

public class Code07_SDOI2016Permutation {
    static final int MOD = 1000000007;
    static final int MAXN = 1000001;
    
    // 预处理阶乘和逆元
    static long[] fact = new long[MAXN];
    static long[] ifact = new long[MAXN];
    // 错排数
    static long[] derange = new long[MAXN];
    
    static {
        // 预处理阶乘
        fact[0] = 1;
        for (int i = 1; i < MAXN; i++) {
            fact[i] = fact[i-1] * i % MOD;
        }
        
        // 预处理逆元
        ifact[MAXN-1] = pow(fact[MAXN-1], MOD-2);
        for (int i = MAXN-2; i >= 0; i--) {
            ifact[i] = ifact[i+1] * (i+1) % MOD;
        }
        
        // 预处理错排数
        derange[0] = 1;
        derange[1] = 0;
        for (int i = 2; i < MAXN; i++) {
            derange[i] = (i-1) * (derange[i-1] + derange[i-2]) % MOD;
        }
    }
    
    // 快速幂
    static long pow(long base, long exp) {
        long res = 1;
        while (exp > 0) {
            if (exp % 2 == 1) res = res * base % MOD;
            base = base * base % MOD;
            exp /= 2;
        }
        return res;
    }
    
    // 计算组合数C(n, k)
    static long comb(int n, int k) {
        if (k > n || k < 0) return 0;
        return fact[n] * ifact[k] % MOD * ifact[n-k] % MOD;
    }
    
    // 计算恰好有m个位置满足a[i] = i的排列数
    static long solve(int n, int m) {
        // 特殊情况处理
        if (m > n) return 0;
        
        // 使用二项式反演计算答案
        long ans = 0;
        for (int i = m; i <= n; i++) {
            // (-1)^(i-m) * C(i, m) * C(n, i) * D(n-i)
            long term = comb(i, m) * comb(n, i) % MOD * derange[n-i] % MOD;
            if ((i-m) % 2 == 0) {
                ans = (ans + term) % MOD;
            } else {
                ans = (ans - term + MOD) % MOD;
            }
        }
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int T = Integer.parseInt(br.readLine());
        for (int i = 0; i < T; i++) {
            String[] parts = br.readLine().split(" ");
            int n = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            out.println(solve(n, m));
        }
        
        out.flush();
        out.close();
        br.close();
    }
}