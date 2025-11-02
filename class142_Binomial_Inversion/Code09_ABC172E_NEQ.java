package class145;

// AtCoder ABC172E NEQ
// 题目描述：给定两个数N M，要求构造两个长度为N的序列A和B，
// 满足以下条件：
// 1. 1 <= A_i, B_i <= M
// 2. A_i != B_i (1 <= i <= N)
// 3. A_i != A_j, B_i != B_j (1 <= i < j <= N)
// 求满足条件的序列对(A,B)的个数，答案对(10^9+7)取模。
// 1 <= N <= M <= 5*10^5
// 测试链接: https://atcoder.jp/contests/abc172/tasks/abc172_e

/*
二项式反演在序列计数问题中的应用：

问题描述：
构造两个长度为N的序列A和B，满足：
1. 元素范围在[1,M]之间
2. 对应位置元素不相等(A_i != B_i)
3. 各自序列内元素互不相等(A_i != A_j, B_i != B_j)

解题思路：
设f(i)表示恰好有i个位置满足A_j = B_j的方案数（目标）
设g(i)表示至少有i个位置满足A_j = B_j的方案数

显然，g(i)更容易计算：
1. 先从N个位置中选出i个位置，使得A_j = B_j，方案数为C(N, i)
2. 从M个数中选出i个数分配给这i个位置，方案数为P(M, i) = M!/(M-i)!
3. 剩下的N-i个位置需要满足A_j != B_j且各自序列内元素互不相等
   这等价于求长度为(N-i)的错排方案数，方案数为D(N-i,M-i)
   
因此：g(i) = C(N, i) * P(M, i) * D(N-i, M-i)

其中D(n, m)表示从m个数中选出n个数排列成序列，使得对应位置不相等的方案数，
可以用容斥原理计算：
D(n, m) = Σ(k=0 to n) (-1)^k * C(n, k) * P(m-k, n-k)

根据二项式反演公式2：
f(0) = Σ(i=0 to N) (-1)^i * C(N, i) * g(i)
     = Σ(i=0 to N) (-1)^i * C(N, i) * C(N, i) * P(M, i) * D(N-i, M-i)

相关题目：
1. AtCoder ABC172E NEQ（标准题目）
2. 洛谷 P4071 [SDOI2016]排列计数（类似思想）
*/

import java.io.*;
import java.util.*;

public class Code09_ABC172E_NEQ {
    static final int MOD = 1000000007;
    static final int MAXN = 500001;
    
    // 预处理阶乘和逆元
    static long[] fact = new long[MAXN];
    static long[] ifact = new long[MAXN];
    
    // 预处理
    static {
        fact[0] = 1;
        for (int i = 1; i < MAXN; i++) {
            fact[i] = fact[i-1] * i % MOD;
        }
        
        ifact[MAXN-1] = pow(fact[MAXN-1], MOD-2);
        for (int i = MAXN-2; i >= 0; i--) {
            ifact[i] = ifact[i+1] * (i+1) % MOD;
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
    
    // 计算排列数P(n, k)
    static long perm(int n, int k) {
        if (k > n || k < 0) return 0;
        return fact[n] * ifact[n-k] % MOD;
    }
    
    // 计算组合数C(n, k)
    static long comb(int n, int k) {
        if (k > n || k < 0) return 0;
        return fact[n] * ifact[k] % MOD * ifact[n-k] % MOD;
    }
    
    // 计算D(n, m)：从m个数中选出n个数排列成序列，使得对应位置不相等的方案数
    static long D(int n, int m) {
        if (n > m) return 0;
        long res = 0;
        for (int k = 0; k <= n; k++) {
            // (-1)^k * C(n, k) * P(m-k, n-k)
            long term = comb(n, k) * perm(m-k, n-k) % MOD;
            if (k % 2 == 0) {
                res = (res + term) % MOD;
            } else {
                res = (res - term + MOD) % MOD;
            }
        }
        return res;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        int N = Integer.parseInt(parts[0]);
        int M = Integer.parseInt(parts[1]);
        
        // 使用二项式反演计算答案
        long ans = 0;
        for (int i = 0; i <= N; i++) {
            // (-1)^i * C(N, i) * C(N, i) * P(M, i) * D(N-i, M-i)
            long term = comb(N, i) * comb(N, i) % MOD * perm(M, i) % MOD * D(N-i, M-i) % MOD;
            if (i % 2 == 0) {
                ans = (ans + term) % MOD;
            } else {
                ans = (ans - term + MOD) % MOD;
            }
        }
        
        out.println(ans);
        out.flush();
        out.close();
        br.close();
    }
}