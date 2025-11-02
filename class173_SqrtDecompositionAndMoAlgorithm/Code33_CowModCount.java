// 牛牛算题 - 整数分块算法实现 (Java版本)
// 题目来源: 牛客编程巅峰赛
// 题目大意: 计算对于小于等于n的每个数p，求n = p×k + m中的k×m之和
// 约束条件: 1 ≤ n ≤ Integer.MAX_VALUE

import java.io.*;
import java.util.*;

public class Code33_CowModCount {
    private static final int MOD = 1000000007;
    
    // 计算k*m之和，其中n = p*k + m (0 ≤ m < p)
    // 使用整数分块优化，时间复杂度O(√n)
    public static long cowModCount(long n) {
        long ans = 0;
        long i = 1;
        
        while (i <= n) {
            // 计算当前块的右端点
            long k = n / i;
            long j = n / k;
            
            // 计算k*n*(j-i+1) mod MOD
            long term1 = (k % MOD) * (n % MOD) % MOD;
            term1 = term1 * ((j - i + 1) % MOD) % MOD;
            
            // 计算k² * sum(p from i to j) mod MOD
            // sum(p from i to j) = (i + j) * (j - i + 1) / 2
            long sum_p = ((i % MOD) + (j % MOD)) % MOD;
            sum_p = sum_p * ((j - i + 1) % MOD) % MOD;
            sum_p = sum_p * 500000004L % MOD; // 乘以2的逆元mod MOD
            
            long term2 = (k % MOD) * (k % MOD) % MOD;
            term2 = term2 * sum_p % MOD;
            
            // 当前块的贡献是 (term1 - term2) mod MOD
            ans = (ans + term1 - term2 + MOD) % MOD; // +MOD确保结果非负
            
            // 移动到下一个块
            i = j + 1;
        }
        
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        long n = Long.parseLong(br.readLine());
        pw.println(cowModCount(n));
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 整数分块的时间复杂度为O(√n)
     * - 每个块的计算时间为O(1)
     * - 总共有O(√n)个块
     * - 因此总体时间复杂度为O(√n)
     * 
     * 空间复杂度分析：
     * - 只使用了常数个变量
     * - 空间复杂度为O(1)
     * 
     * Java语言特性注意事项：
     * 1. 使用long类型处理大数，避免溢出
     * 2. 注意模运算中负数的处理，需要加上MOD后再取模
     * 3. 2的逆元在MOD=1e9+7的情况下是500000004
     * 4. 输入输出使用BufferedReader和PrintWriter以提高效率
     * 
     * 数学推导：
     * 对于每个p，有n = p*k + m，其中0 ≤ m < p
     * 所以k = n/p（整数除法），m = n % p = n - p*k
     * 因此k*m = k*(n - p*k) = k*n - k²*p
     * 
     * 通过整数分块，我们可以将具有相同k值的p分成一组，每组内的计算可以批量处理。
     */
}