package class145;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 集合计数问题
 * 题目：洛谷 P10596 集合计数 / BZOJ2839 集合计数
 * 链接：https://www.luogu.com.cn/problem/P10596
 * 描述：从2^n个子集中选出若干个集合，使交集恰好包含k个元素的方案数
 * 
 * 数据范围：
 * - 1 <= n <= 10^6
 * - 0 <= k <= n
 * - 结果对1000000007取模
 * 
 * 二项式反演应用：将"恰好k个元素"转化为"至少k个元素"的问题
 */
public class Code02_SetCounting {

    // 最大数据范围，根据题目要求设置为10^6+1
    public static int MAXN = 1000001;

    // 模数，题目要求结果对1e9+7取模
    public static int MOD = 1000000007;

    // 阶乘数组，用于计算组合数
    public static long[] fac = new long[MAXN];

    // 阶乘的逆元数组，用于计算组合数
    public static long[] inv = new long[MAXN];

    // g[i]表示选出若干集合，使得交集至少包含i个元素的方案数
    public static long[] g = new long[MAXN];

    // 输入参数：n是元素个数，k是目标交集大小
    public static int n, k;

    /**
     * 预处理阶乘和阶乘的逆元
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * 工程化考虑：
     * - 使用递推方式计算阶乘，避免重复计算
     * - 使用费马小定理计算逆元，因为MOD是质数
     * - 逆元采用倒序计算，提高效率
     */
    public static void build() {
        // 初始化阶乘数组
        fac[0] = inv[0] = 1;
        fac[1] = 1;
        
        // 计算阶乘：fac[i] = i! mod MOD
        for (int i = 2; i <= n; i++) {
            fac[i] = ((long) i * fac[i - 1]) % MOD;
        }
        
        // 使用费马小定理计算最大n的阶乘逆元
        // 费马小定理：当MOD是质数时，a^(MOD-1) ≡ 1 (mod MOD)，因此a^(MOD-2) ≡ a^(-1) (mod MOD)
        inv[n] = power(fac[n], MOD - 2);
        
        // 倒序计算其他阶乘的逆元
        // 利用性质：inv[i] = inv[i+1] * (i+1) mod MOD
        for (int i = n - 1; i >= 1; i--) {
            inv[i] = ((long) (i + 1) * inv[i + 1]) % MOD;
        }
    }

    /**
     * 快速幂运算，计算x^p % MOD
     * 
     * 时间复杂度：O(log p) - 二进制快速幂算法
     * 空间复杂度：O(1) - 只需要常数级额外空间
     * 
     * @param x 底数
     * @param p 指数
     * @return x^p mod MOD
     */
    public static long power(long x, long p) {
        long ans = 1;
        x %= MOD; // 先取模避免溢出
        
        while (p > 0) {
            // 如果当前二进制位为1，则乘上当前的x^2^i
            if ((p & 1) == 1) {
                ans = (ans * x) % MOD;
            }
            // x自乘，相当于x^(2^(i+1))
            x = (x * x) % MOD;
            // p右移一位，处理下一个二进制位
            p >>= 1;
        }
        return ans;
    }

    /**
     * 计算组合数C(n, k) = n! / (k! * (n-k)!)
     * 
     * 时间复杂度：O(1) - 直接利用预处理的阶乘和逆元计算
     * 空间复杂度：O(1)
     * 
     * @param n 总数
     * @param k 选取的数量
     * @return C(n,k) mod MOD
     * @throws IllegalArgumentException 当k < 0或k > n时抛出异常
     */
    public static long c(int n, int k) {
        // 边界条件检查
        if (k < 0 || k > n) {
            return 0; // C(n,k)=0 当k<0或k>n
        }
        // 利用预处理的阶乘和逆元计算组合数
        return (((fac[n] * inv[k]) % MOD) * inv[n - k]) % MOD;
    }

    /**
     * 计算集合计数的主函数
     * 
     * 算法原理：
     * 1. 定义f(k)为交集恰好有k个元素的方案数
     * 2. 定义g(k)为交集至少有k个元素的方案数
     * 3. 通过二项式反演，f(k) = Σ(i=k到n) [(-1)^(i-k) * C(i,k) * g(i)]
     * 4. g(i) = C(n,i) * (2^(2^(n-i)) - 1) 表示选择i个固定元素，其余元素任意组合
     * 
     * 时间复杂度分析：
     * - 预处理阶乘和逆元：O(n)
     * - 计算g数组：O(n)
     * - 计算最终答案：O(n)
     * 总时间复杂度：O(n)
     * 
     * 空间复杂度分析：
     * - 阶乘和逆元数组：O(n)
     * - g数组：O(n)
     * 总空间复杂度：O(n)
     * 
     * @return 交集恰好有k个元素的方案数，对MOD取模
     */
    public static long compute() {
        // 预处理阶乘和逆元
        build();
        
        // 计算g数组
        // 注意：这里采用逆序计算，从i=n开始，这样可以高效计算2^(2^(n-i))
        long tmp = 2; // 初始值：2^(2^0) = 2^1 = 2
        for (int i = n; i >= 0; i--) {
            g[i] = tmp; // g[i]暂时保存2^(2^(n-i))
            // 计算下一个tmp = 2^(2^(n-i+1)) = (2^(2^(n-i)))^2
            tmp = tmp * tmp % MOD;
        }
        
        // 计算完整的g[i] = C(n,i) * (2^(2^(n-i)) - 1)
        // 注意：减去1相当于加上MOD-1（模运算中的负数处理）
        for (int i = 0; i <= n; i++) {
            g[i] = (g[i] + MOD - 1) * c(n, i) % MOD;
        }
        
        // 应用二项式反演公式计算f(k)
        long ans = 0;
        for (int i = k; i <= n; i++) {
            // 计算符号：(-1)^(i-k)
            if (((i - k) & 1) == 0) {
                // 偶数次幂，符号为正
                ans = (ans + c(i, k) * g[i] % MOD) % MOD;
            } else {
                // 奇数次幂，符号为负，相当于乘以MOD-1
                ans = (ans + c(i, k) * g[i] % MOD * (MOD - 1) % MOD) % MOD;
            }
        }
        
        return ans;
    }

    /**
     * 主函数，处理输入输出
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 使用快速输入方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入参数n和k
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        k = (int) in.nval;
        
        // 验证输入参数的合法性
        if (n < 0 || k < 0 || k > n) {
            throw new IllegalArgumentException("输入参数不合法：n应大于等于0，k应在0到n之间");
        }
        
        // 调用计算函数并输出结果
        out.println(compute());
        
        // 确保输出被刷新并关闭资源
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 单元测试函数（用于验证算法正确性）
     * 
     * 工程化考虑：
     * - 测试边界条件
     * - 测试小规模输入
     * - 与已知结果对比
     */
    private static void test() {
        // 测试用例1：n=1, k=0
        n = 1; k = 0;
        long result1 = compute();
        System.out.printf("n=%d, k=%d, 结果=%d (期望: 1)\n", n, k, result1);
        
        // 测试用例2：n=2, k=1
        n = 2; k = 1;
        long result2 = compute();
        System.out.printf("n=%d, k=%d, 结果=%d (期望: 2)\n", n, k, result2);
        
        // 测试用例3：n=3, k=1
        n = 3; k = 1;
        long result3 = compute();
        System.out.printf("n=%d, k=%d, 结果=%d (期望: 12)\n", n, k, result3);
    }

}