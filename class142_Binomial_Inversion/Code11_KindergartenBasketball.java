package class145;

import java.io.*;
import java.util.*;

/**
 * 幼儿园篮球题（二项式反演解法）
 * 题目：洛谷 P2791 幼儿园篮球题
 * 链接：https://www.luogu.com.cn/problem/P2791
 * 描述：蔡徐坤专属篮球场上有N个篮球，其中M个是没气的。
 * ikun们会从N个篮球中准备n个球放在场地上，其中恰好有m个是没气的。
 * 蔡徐坤会在这n个篮球中随机选出k个投篮。如果投进了x个，则这次表演的失败度为x^L。
 * 求期望失败度。
 * 
 * 解题思路：
 * 使用二项式反演和第二类斯特林数解决期望计算问题。
 * E[x^L] = Σ(i=0 to L) S(L,i) * i! * C(m,i) * C(n-m,k-i) / C(n,k)
 * 其中S(L,i)是第二类斯特林数，表示将L个不同的球放入i个相同的盒子且每个盒子非空的方案数。
 * 
 * 时间复杂度：O(L^2 + k) - 预处理斯特林数O(L^2)，计算期望O(k)
 * 空间复杂度：O(L^2) - 存储斯特林数数组
 */
public class Code11_KindergartenBasketball {
    static final int MOD = 998244353;
    static final int MAXL = 200005;
    
    // 阶乘数组和逆元数组
    static long[] fact = new long[MAXL];
    static long[] invFact = new long[MAXL];
    
    // 第二类斯特林数
    static long[][] stirling = new long[MAXL][MAXL];
    
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
     * 预处理阶乘、阶乘逆元和第二类斯特林数
     */
    static void precompute(int maxL) {
        // 预处理阶乘和逆元
        fact[0] = 1;
        for (int i = 1; i <= maxL; i++) {
            fact[i] = fact[i-1] * i % MOD;
        }
        
        invFact[maxL] = pow(fact[maxL], MOD-2);
        for (int i = maxL-1; i >= 0; i--) {
            invFact[i] = invFact[i+1] * (i+1) % MOD;
        }
        
        // 预处理第二类斯特林数
        stirling[0][0] = 1;
        for (int i = 1; i <= maxL; i++) {
            for (int j = 1; j <= i; j++) {
                stirling[i][j] = (stirling[i-1][j-1] + j * stirling[i-1][j]) % MOD;
            }
        }
    }
    
    /**
     * 计算组合数C(n, k)
     * @param n 总数
     * @param k 选取数量
     * @return C(n, k) % MOD
     */
    static long comb(long n, long k) {
        if (k > n || k < 0) return 0;
        if (n < MAXL) {
            return fact[(int)n] * invFact[(int)k] % MOD * invFact[(int)(n-k)] % MOD;
        }
        
        // 大数情况，使用Lucas定理或直接计算
        long result = 1;
        for (long i = 0; i < k; i++) {
            result = result * (n - i) % MOD;
            result = result * pow(i + 1, MOD - 2) % MOD;
        }
        return result;
    }
    
    /**
     * 计算期望失败度E[x^L]
     * @param n 总球数
     * @param m 没气的球数
     * @param k 投篮数
     * @param L 失败度参数
     * @return E[x^L] % MOD
     */
    static long expectedValue(long n, long m, long k, int L) {
        long result = 0;
        for (int i = 0; i <= Math.min(L, k); i++) {
            // E[x^L] = Σ(i=0 to L) S(L,i) * i! * C(m,i) * C(n-m,k-i) / C(n,k)
            long term = stirling[L][i] * fact[i] % MOD;
            term = term * comb(m, i) % MOD;
            term = term * comb(n - m, k - i) % MOD;
            term = term * pow(comb(n, k), MOD - 2) % MOD;
            result = (result + term) % MOD;
        }
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        long N = Long.parseLong(parts[0]);
        long M = Long.parseLong(parts[1]);
        int S = Integer.parseInt(parts[2]);
        int L = Integer.parseInt(parts[3]);
        
        // 预处理
        precompute(L);
        
        for (int i = 0; i < S; i++) {
            parts = br.readLine().split(" ");
            long n = Long.parseLong(parts[0]);
            long m = Long.parseLong(parts[1]);
            long k = Long.parseLong(parts[2]);
            
            out.println(expectedValue(n, m, k, L));
        }
        
        out.flush();
        out.close();
        br.close();
    }
}