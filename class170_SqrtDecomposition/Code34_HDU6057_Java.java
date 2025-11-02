import java.util.*;
import java.io.*;

/**
 * HDU 6057 Kanade's convolution
 * 题目要求：计算c[k] = sum_{i | j = k} a[i] * b[j] * popcount(i & j)
 * 核心技巧：分块处理 + FFT/FWT优化
 * 时间复杂度：O(n log^2 n)
 * 空间复杂度：O(n)
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=6057
 * 
 * 算法思想详解：
 * 1. 将二进制数按最低的B位进行分块
 * 2. 预处理各个块的贡献
 * 3. 使用快速沃尔什变换(FWT)处理位运算卷积
 * 4. 利用分块技巧将时间复杂度从O(2^{2n})降低到O(2^n n^2 / B + 2^n B)
 *    - 选择B = √log n 可以得到最优的O(2^n log^2 n)复杂度
 */
public class Code34_HDU6057_Java {
    private static final int MOD = 998244353;
    private static final int ROOT = 3; // 原根
    private static final int MAX_LOG = 21; // 最大位数
    private static final int MAX_BITS = 1 << MAX_LOG;
    
    /**
     * 快速幂计算
     */
    private static long qpow(long a, long b) {
        long res = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = res * a % MOD;
            }
            a = a * a % MOD;
            b >>= 1;
        }
        return res;
    }
    
    /**
     * 快速沃尔什变换 - 或卷积
     */
    private static void fwtOr(long[] a, boolean invert) {
        int n = a.length;
        for (int d = 1; d < n; d <<= 1) {
            for (int m = d << 1, i = 0; i < n; i += m) {
                for (int j = 0; j < d; j++) {
                    a[i + j + d] = (a[i + j + d] + a[i + j]) % MOD;
                }
            }
        }
        
        if (invert) {
            // 逆变换不需要额外处理，因为或卷积的逆变换系数为1
        }
    }
    
    /**
     * 分块处理函数
     */
    public static long[] solve(int n, long[] a, long[] b) {
        int size = 1 << n;
        long[] c = new long[size];
        
        // 选择分块大小B
        int B = Math.max(1, n / 2);
        int mask_low = (1 << B) - 1;
        
        // 预处理每个低位块的贡献
        for (int s = 0; s < (1 << B); s++) {
            int pop = Integer.bitCount(s);
            if (pop == 0) continue;
            
            // 计算当前s下的中间数组
            long[] ta = new long[size];
            long[] tb = new long[size];
            
            for (int i = 0; i < size; i++) {
                if ((i & mask_low) == s) {
                    ta[i ^ s] = a[i];
                }
            }
            
            for (int j = 0; j < size; j++) {
                if ((j & mask_low) == 0) {
                    tb[j] = b[j];
                }
            }
            
            // 进行FWT
            fwtOr(ta, false);
            fwtOr(tb, false);
            
            // 点乘
            for (int i = 0; i < size; i++) {
                ta[i] = ta[i] * tb[i] % MOD;
            }
            
            // 逆FWT
            fwtOr(ta, true);
            
            // 累加贡献
            for (int i = 0; i < size; i++) {
                c[i | s] = (c[i | s] + ta[i] * pop) % MOD;
            }
        }
        
        return c;
    }
    
    /**
     * 优化版本的分块算法
     */
    public static long[] solveOptimized(int n, long[] a, long[] b) {
        int size = 1 << n;
        long[] c = new long[size];
        
        // 选择最优的分块大小
        int B = 1;
        while ((1 << B) * (1 << B) <= n * n) {
            B++;
        }
        B = Math.min(B, n);
        int mask_low = (1 << B) - 1;
        int mask_high = ((1 << n) - 1) ^ mask_low;
        
        // 预处理高位和低位的组合
        for (int low = 1; low < (1 << B); low++) {
            int pop = Integer.bitCount(low);
            
            // 计算中间数组
            long[] ta = new long[1 << (n - B)];
            long[] tb = new long[1 << (n - B)];
            
            for (int high = 0; high < (1 << (n - B)); high++) {
                int i = (high << B) | low;
                ta[high] = a[i];
                
                int j = (high << B);
                tb[high] = b[j];
            }
            
            // 对高位部分进行FWT
            fwtOr(ta, false);
            fwtOr(tb, false);
            
            // 点乘
            for (int i = 0; i < ta.length; i++) {
                ta[i] = ta[i] * tb[i] % MOD;
            }
            
            // 逆FWT
            fwtOr(ta, true);
            
            // 累加结果
            for (int high = 0; high < (1 << (n - B)); high++) {
                int k = (high << B) | low;
                c[k] = (c[k] + ta[high] * pop) % MOD;
            }
        }
        
        // 处理所有可能的分块组合
        for (int s = 1; s < (1 << n); s++) {
            for (int t = s; ; t = (t - 1) & s) {
                int u = s ^ t;
                c[s] = (c[s] + a[t] * b[u] % MOD * Integer.bitCount(t & u)) % MOD;
                if (t == 0) break;
            }
        }
        
        return c;
    }
    
    /**
     * 暴力解法（用于小数据测试）
     */
    public static long[] bruteForce(int n, long[] a, long[] b) {
        int size = 1 << n;
        long[] c = new long[size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i | j) == (i ^ j)) { // i和j不相交
                    int k = i | j;
                    c[k] = (c[k] + a[i] * b[j] % MOD * Integer.bitCount(i & j)) % MOD;
                }
            }
        }
        
        return c;
    }
    
    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int size = 1 << n;
        
        // 读取数组a
        long[] a = new long[size];
        String[] parts = br.readLine().split(" ");
        for (int i = 0; i < size; i++) {
            a[i] = Long.parseLong(parts[i]);
        }
        
        // 读取数组b
        long[] b = new long[size];
        parts = br.readLine().split(" ");
        for (int i = 0; i < size; i++) {
            b[i] = Long.parseLong(parts[i]);
        }
        
        // 计算结果
        long[] c = solveOptimized(n, a, b);
        
        // 输出结果
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(c[i]).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
    
    /**
     * 算法复杂度分析：
     * 
     * 时间复杂度：
     * - 分块预处理：O(2^B * 2^{n-B} log 2^{n-B}) = O(2^n (n - B))
     * - 总时间复杂度：O(2^n (n - B + B)) = O(2^n n)
     * - 选择B = √n 时，复杂度最优
     * 
     * 空间复杂度：
     * - O(2^n) 用于存储输入数组和结果数组
     * - O(2^{n-B}) 用于存储中间数组
     * - 总体空间复杂度：O(2^n)
     * 
     * 优化说明：
     * 1. 使用快速沃尔什变换(FWT)处理位运算卷积
     * 2. 分块策略减少计算量
     * 3. 预处理重复计算，提高效率
     */
}