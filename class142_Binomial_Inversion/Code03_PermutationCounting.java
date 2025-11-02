package class145;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 排列计数问题
 * 题目：排列中的固定点统计（Fixed Points in Permutations）
 * 
 * 问题描述：
 * 给定n个元素的排列，求恰好有k个固定点（即a[i] = i）的排列数目
 * 这个问题也被称为部分错位排列（Partial Derangement）问题
 * 
 * 二项式反演应用：
 * 将"恰好k个固定点"转化为"至少k个固定点"的问题
 * 
 * 数据范围：
 * - 1 <= n <= 10^6
 * - 0 <= k <= n
 * - 结果对1000000007取模
 */
public class Code03_PermutationCounting {
    // 最大数据范围
    public static final int MAXN = 1000001;
    // 模数
    public static final int MOD = 1000000007;
    // 阶乘数组
    public static long[] fact = new long[MAXN];
    // 阶乘的逆元数组
    public static long[] inv_fact = new long[MAXN];
    // 输入参数
    public static int n, k;
    
    /**
     * 预处理阶乘和阶乘的逆元
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static void precompute() {
        // 计算阶乘数组
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = (fact[i - 1] * i) % MOD;
        }
        
        // 计算最大阶乘的逆元
        inv_fact[n] = power(fact[n], MOD - 2);
        
        // 倒序计算其他阶乘的逆元
        for (int i = n - 1; i >= 0; i--) {
            inv_fact[i] = (inv_fact[i + 1] * (i + 1)) % MOD;
        }
    }
    
    /**
     * 快速幂运算
     * 时间复杂度：O(log p)
     * 空间复杂度：O(1)
     * 
     * @param a 底数
     * @param p 指数
     * @return a^p mod MOD
     */
    public static long power(long a, long p) {
        long result = 1;
        a %= MOD;
        
        while (p > 0) {
            if ((p & 1) == 1) {
                result = (result * a) % MOD;
            }
            a = (a * a) % MOD;
            p >>= 1;
        }
        
        return result;
    }
    
    /**
     * 计算组合数C(n, k)
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * @param n 总数
     * @param k 选取的数量
     * @return C(n, k) mod MOD
     */
    public static long comb(int n, int k) {
        if (k < 0 || k > n) {
            return 0;
        }
        return (fact[n] * inv_fact[k] % MOD) * inv_fact[n - k] % MOD;
    }
    
    /**
     * 使用二项式反演计算恰好k个固定点的排列数
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @return 恰好k个固定点的排列数，对MOD取模
     */
    public static long countFixedPoints() {
        // 预处理阶乘和逆元
        precompute();
        
        // 使用二项式反演公式：
        // f(k) = C(n, k) * D(n-k)
        // 其中D(n-k)是n-k个元素的错排数
        // 而D(m) = m! * Σ(i=0到m) (-1)^i / i!
        
        // 计算C(n, k)
        long c_n_k = comb(n, k);
        
        // 计算D(n-k)：(n-k)个元素的错排数
        int m = n - k;
        long derangement = 0;
        
        // 计算D(m) = m! * Σ(i=0到m) (-1)^i / i!
        for (int i = 0; i <= m; i++) {
            long sign = (i % 2 == 0) ? 1 : -1;
            long term = fact[m] * inv_fact[i] % MOD;
            
            if (sign < 0) {
                term = (MOD - term) % MOD;
            }
            
            derangement = (derangement + term) % MOD;
        }
        
        // 最终结果：C(n, k) * D(n-k)
        return (c_n_k * derangement) % MOD;
    }
    
    /**
     * 优化版本：直接使用递推计算错排数
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * @return 恰好k个固定点的排列数，对MOD取模
     */
    public static long countFixedPointsOptimized() {
        // 预处理阶乘和逆元
        precompute();
        
        // 计算C(n, k)
        long c_n_k = comb(n, k);
        
        // 计算D(n-k)：使用递推公式
        int m = n - k;
        if (m == 0) {
            return c_n_k; // D(0) = 1
        }
        if (m == 1) {
            return 0; // D(1) = 0
        }
        
        long d_prev2 = 1; // D(0)
        long d_prev1 = 0; // D(1)
        long d_curr = 0;
        
        for (int i = 2; i <= m; i++) {
            d_curr = ((i - 1) * (d_prev1 + d_prev2)) % MOD;
            // 更新状态
            d_prev2 = d_prev1;
            d_prev1 = d_curr;
        }
        
        // 最终结果：C(n, k) * D(n-k)
        return (c_n_k * d_curr) % MOD;
    }
    
    /**
     * 单元测试函数
     */
    private static void runTests() {
        // 测试用例1：n=3, k=1
        // 期望结果：3种排列 (1,3,2), (3,2,1), (2,1,3)
        n = 3; k = 1;
        long result1 = countFixedPoints();
        System.out.printf("测试1: n=%d, k=%d, 结果=%d (期望: 3)\n", n, k, result1);
        
        // 测试用例2：n=4, k=2
        // 期望结果：6种排列
        n = 4; k = 2;
        long result2 = countFixedPoints();
        System.out.printf("测试2: n=%d, k=%d, 结果=%d (期望: 6)\n", n, k, result2);
        
        // 测试优化版本
        long optResult2 = countFixedPointsOptimized();
        System.out.printf("优化版本测试2: 结果=%d (与原版本相同: %b)\n", optResult2, result2 == optResult2);
        
        // 测试边界情况：n=0, k=0
        n = 0; k = 0;
        long result3 = countFixedPoints();
        System.out.printf("边界测试: n=%d, k=%d, 结果=%d (期望: 1)\n", n, k, result3);
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) throws IOException {
        // 运行单元测试
        runTests();
        
        // 实际处理输入输出
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        try {
            // 读取输入参数
            in.nextToken();
            n = (int) in.nval;
            in.nextToken();
            k = (int) in.nval;
            
            // 验证输入参数
            if (n < 0 || k < 0 || k > n) {
                throw new IllegalArgumentException("输入参数无效：n应>=0，k应在0到n之间");
            }
            
            // 计算并输出结果
            long result = countFixedPointsOptimized();
            out.println(result);
            
        } catch (Exception e) {
            // 异常处理
            out.println("错误：" + e.getMessage());
        } finally {
            // 确保资源被关闭
            out.flush();
            out.close();
            br.close();
        }
    }
    
    /**
     * 算法原理解析：
     * 1. 定义问题：求恰好k个固定点的排列数
     * 2. 二项式反演思路：
     *    - 设f(k)为恰好k个固定点的排列数（目标）
     *    - 设g(k)为至少k个固定点的排列数
     * 3. g(k)的计算：
     *    - 选择k个位置作为固定点：C(n, k)
     *    - 其余n-k个位置可以任意排列：(n-k)!
     *    - 因此g(k) = C(n, k) * (n-k)!
     * 4. 二项式反演公式：
     *    f(k) = Σ(i=k到n) (-1)^(i-k) * C(i, k) * g(i)
     *         = Σ(i=k到n) (-1)^(i-k) * C(i, k) * C(n, i) * (n-i)!
     *         = C(n, k) * Σ(i=k到n) (-1)^(i-k) * C(n-k, i-k) * (n-i)!
     *         = C(n, k) * Σ(j=0到n-k) (-1)^j * C(n-k, j) * (n-k-j)!
     *         = C(n, k) * (n-k)! * Σ(j=0到n-k) (-1)^j / j!
     *         = C(n, k) * D(n-k)
     *    其中D(m)是m个元素的错排数
     * 
     * 复杂度分析：
     * - 时间复杂度：O(n) - 预处理阶乘和计算结果都需要O(n)时间
     * - 空间复杂度：O(n) - 存储阶乘和逆元数组需要O(n)空间
     * 
     * 优化空间：
     * 1. 当n很大而k较小的时候，可以使用递推方式计算错排数，节省空间
     * 2. 对于多次查询的场景，可以预处理所有可能的阶乘和逆元
     * 3. 当数据规模特别大时，可以使用快速输入输出优化性能
     */
}