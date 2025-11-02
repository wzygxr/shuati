package class145;

import java.io.*;
import java.util.*;

/**
 * 情侣？给我烧了！（二项式反演解法）
 * 题目：洛谷 P4921 [MtOI2018]情侣？给我烧了！
 * 链接：https://www.luogu.com.cn/problem/P4921
 * 描述：有n对情侣来到电影院观看电影。在电影院，恰好留有n排座位，每排包含2个座位，共2×n个座位。
 * 现在，每个人将会随机坐在某一个位置上，且恰好将这2×n个座位坐满。
 * 如果一对情侣坐在了同一排的座位上，那么我们称这对情侣是和睦的。
 * 求出当k=0,1,...,n时，共有多少种不同的就坐方案满足恰好有k对情侣是和睦的。
 * 
 * 解题思路：
 * 使用二项式反演解决"恰好k对情侣和睦"的问题。
 * 设f(k)表示恰好k对情侣和睦的方案数
 * 设g(k)表示至少k对情侣和睦的方案数
 * 根据二项式反演公式：f(k) = Σ(i=k to n) (-1)^(i-k) * C(i,k) * g(i)
 * 
 * 计算g(i)：
 * 1. 从n对情侣中选择i对情侣，方案数为C(n,i)
 * 2. 这i对情侣必须坐在同一排，方案数为A(n,i)（排列）
 * 3. 每对和睦情侣内部可以交换位置，方案数为2^i
 * 4. 剩下的2*(n-i)个人任意排列，方案数为(2*(n-i))!
 * 因此：g(i) = C(n,i) * A(n,i) * 2^i * (2*(n-i))!
 * 
 * 时间复杂度：O(n^2) - 预处理阶乘和逆元O(n)，计算每个f(k)需要O(n)
 * 空间复杂度：O(n) - 存储阶乘和逆元数组
 */
public class Code10_CouplesBurned {
    static final int MOD = 998244353;
    static final int MAXN = 2005;
    
    // 阶乘数组和逆元数组
    static long[] fact = new long[MAXN];
    static long[] invFact = new long[MAXN];
    
    /**
     * 快速幂运算
     * @param base 底数
     * @param exp 指数
     * @return base^exp % MOD
     */
    static long pow(long base, long exp) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) result = result * base % MOD;
            base = base * base % MOD;
            exp /= 2;
        }
        return result;
    }
    
    /**
     * 预处理阶乘和阶乘逆元
     */
    static void precompute() {
        fact[0] = 1;
        for (int i = 1; i < MAXN; i++) {
            fact[i] = fact[i-1] * i % MOD;
        }
        
        invFact[MAXN-1] = pow(fact[MAXN-1], MOD-2);
        for (int i = MAXN-2; i >= 0; i--) {
            invFact[i] = invFact[i+1] * (i+1) % MOD;
        }
    }
    
    /**
     * 计算组合数C(n, k)
     * @param n 总数
     * @param k 选取数量
     * @return C(n, k) % MOD
     */
    static long comb(int n, int k) {
        if (k > n || k < 0) return 0;
        return fact[n] * invFact[k] % MOD * invFact[n-k] % MOD;
    }
    
    /**
     * 计算排列数A(n, k)
     * @param n 总数
     * @param k 选取数量
     * @return A(n, k) % MOD
     */
    static long perm(int n, int k) {
        if (k > n || k < 0) return 0;
        return fact[n] * invFact[n-k] % MOD;
    }
    
    /**
     * 计算至少k对情侣和睦的方案数g(k)
     * @param n 总情侣对数
     * @param k 至少和睦的情侣对数
     * @return g(k) % MOD
     */
    static long g(int n, int k) {
        if (k > n) return 0;
        // g(k) = C(n,k) * A(n,k) * 2^k * (2*(n-k))!
        return comb(n, k) * perm(n, k) % MOD * pow(2, k) % MOD * fact[2*(n-k)] % MOD;
    }
    
    /**
     * 使用二项式反演计算恰好k对情侣和睦的方案数f(k)
     * @param n 总情侣对数
     * @param k 恰好和睦的情侣对数
     * @return f(k) % MOD
     */
    static long f(int n, int k) {
        long result = 0;
        for (int i = k; i <= n; i++) {
            // f(k) = Σ(i=k to n) (-1)^(i-k) * C(i,k) * g(i)
            long term = comb(i, k) * g(n, i) % MOD;
            if ((i-k) % 2 == 0) {
                result = (result + term) % MOD;
            } else {
                result = (result - term + MOD) % MOD;
            }
        }
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 预处理
        precompute();
        
        int T = Integer.parseInt(br.readLine());
        while (T-- > 0) {
            int n = Integer.parseInt(br.readLine());
            for (int k = 0; k <= n; k++) {
                out.println(f(n, k));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}