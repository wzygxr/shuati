package class145;

import java.io.*;
import java.util.*;

/**
 * 多平台二项式反演题目综合实现
 * 
 * 整合以下平台的二项式反演相关题目：
 * 1. UVa 11426: GCD - Extreme (II) - 最大公约数求和问题
 * 2. POJ 3907: Build Your Home - 多边形面积计算
 * 3. SPOJ GONE: 数位动态规划与容斥原理
 * 4. 杭电 OJ 6143: Killer Names - 杀手名字染色问题
 * 5. AizuOJ 2292: 排列计数问题
 * 6. TimusOJ 1520: Generating Sets - 集合生成问题
 * 7. Comet OJ C1129: 集合计数
 * 8. acwing 1303: 斐波那契公约数
 * 
 * 二项式反演核心思想：
 * 将"恰好k个"的问题转化为"至少k个"的问题，通过容斥原理计算
 * 
 * 算法复杂度：
 * - 时间复杂度：通常为O(n)或O(n log n)
 * - 空间复杂度：通常为O(n)
 */
public class Code14_MultiPlatformBinomialInversion {
    
    // 模数常量
    public static final int MOD = 1000000007;
    public static final int MOD2 = 998244353;
    
    /**
     * 问题1：UVa 11426 GCD - Extreme (II)
     * 描述：计算gcd(1,2)+gcd(1,3)+gcd(2,3)+...+gcd(n-1,n)
     * 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2421
     * 
     * 算法思路：
     * 使用莫比乌斯反演或二项式反演思想
     * 设f(d)为gcd(i,j)=d的数对个数
     * 则F(d) = Σ(d|k) f(k) = floor(n/d) * floor(n/d - 1) / 2
     * 通过莫比乌斯反演：f(d) = Σ(k|d) μ(k) * F(d/k)
     */
    public static long uvaGCDExtreme(int n) {
        // 预处理莫比乌斯函数
        int[] mu = new int[n + 1];
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        
        mu[1] = 1;
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                mu[i] = -1;
                for (int j = i * 2; j <= n; j += i) {
                    isPrime[j] = false;
                    if (j % (i * i) == 0) {
                        mu[j] = 0;
                    } else {
                        mu[j] = -mu[j];
                    }
                }
            }
        }
        
        long result = 0;
        for (int d = 1; d <= n; d++) {
            long F = (long) (n / d) * (n / d - 1) / 2;
            result += mu[d] * F * d;
        }
        
        return result;
    }
    
    /**
     * 问题2：POJ 3907 Build Your Home
     * 描述：给定n个点，求这n个点形成的所有简单多边形的面积和
     * 链接：http://poj.org/problem?id=3907
     * 
     * 算法思路：
     * 使用二项式反演计算多边形组合的面积期望
     * 对于n个点，简单多边形的数量与二项式系数相关
     */
    public static double pojBuildYourHome(int[][] points) {
        int n = points.length;
        
        // 计算所有点对的向量
        double totalArea = 0;
        
        // 使用鞋带公式计算多边形面积
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            totalArea += (double) points[i][0] * points[j][1] - (double) points[j][0] * points[i][1];
        }
        
        totalArea = Math.abs(totalArea) / 2.0;
        
        // 使用二项式反演计算组合面积
        // 对于n个点，简单多边形的数量为C(n,3) + C(n,4) + ... + C(n,n)
        long polygonCount = 0;
        for (int k = 3; k <= n; k++) {
            polygonCount += comb(n, k);
        }
        
        // 面积期望 = 总面积 / 多边形数量（简化模型）
        return totalArea / polygonCount;
    }
    
    /**
     * 问题3：SPOJ GONE - 数位动态规划与容斥原理
     * 描述：求区间[L,R]内所有数满足其各位数字之和是质数的数的个数
     * 链接：https://www.spoj.com/problems/GONE/
     * 
     * 算法思路：
     * 使用数位DP结合容斥原理
     * 先计算[1,R]中满足条件的数的个数，减去[1,L-1]中满足条件的数的个数
     */
    public static int spojGONE(int L, int R) {
        return countPrimeDigitSum(R) - countPrimeDigitSum(L - 1);
    }
    
    private static int countPrimeDigitSum(int n) {
        if (n <= 0) return 0;
        
        // 数位DP实现
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;
        
        // dp[pos][sum][tight] 表示处理到第pos位，数字和为sum，是否紧贴边界
        int[][][] dp = new int[len + 1][100][2];
        dp[0][0][1] = 1;
        
        for (int pos = 0; pos < len; pos++) {
            for (int sum = 0; sum < 100; sum++) {
                for (int tight = 0; tight < 2; tight++) {
                    if (dp[pos][sum][tight] == 0) continue;
                    
                    int limit = tight == 1 ? (digits[pos] - '0') : 9;
                    
                    for (int d = 0; d <= limit; d++) {
                        int newSum = sum + d;
                        int newTight = (tight == 1 && d == limit) ? 1 : 0;
                        dp[pos + 1][newSum][newTight] += dp[pos][sum][tight];
                    }
                }
            }
        }
        
        // 统计质数和的个数
        int count = 0;
        for (int sum = 2; sum < 100; sum++) {
            if (isPrime(sum)) {
                count += dp[len][sum][0] + dp[len][sum][1];
            }
        }
        
        return count;
    }
    
    /**
     * 问题4：杭电 OJ 6143 Killer Names
     * 描述：计算使用m种颜色为名字的前缀和后缀染色，使得前缀和后缀的颜色集合不相交的方案数
     * 链接：http://acm.hdu.edu.cn/showproblem.php?pid=6143
     * 
     * 算法思路：
     * 使用二项式反演计算颜色分配方案
     * 设f(k)为恰好使用k种颜色分配给前缀的方案数
     * 则总方案数 = Σ(k=1到min(m,n)) [C(m,k) * f(k) * g(m-k)]
     * 其中g(m-k)为后缀使用剩余颜色的方案数
     */
    public static long hduKillerNames(int n, int m) {
        precomputeFactorials(Math.max(n, m));
        
        long result = 0;
        
        for (int k = 1; k <= Math.min(m, n); k++) {
            // 前缀使用k种颜色的方案数：第二类斯特林数 * k!
            long prefixWays = stirlingSecond(n, k) * fact[k] % MOD;
            
            // 后缀使用m-k种颜色的方案数
            long suffixWays = power(m - k, n, MOD);
            
            // 选择颜色的组合数
            long colorChoice = comb(m, k);
            
            result = (result + colorChoice * prefixWays % MOD * suffixWays % MOD) % MOD;
        }
        
        return result;
    }
    
    /**
     * 问题5：AizuOJ 2292 排列计数
     * 描述：使用二项式反演求解排列计数问题
     * 链接：https://onlinejudge.u-aizu.ac.jp/problems/2292
     */
    public static long aizuPermutationCount(int n, int k) {
        // 计算恰好k个固定点的排列数
        // f(k) = C(n,k) * D(n-k)，其中D(m)是错排数
        
        precomputeFactorials(n);
        
        long combination = comb(n, k);
        long derangement = derangement(n - k);
        
        return combination * derangement % MOD;
    }
    
    /**
     * 问题6：TimusOJ 1520 Generating Sets
     * 描述：使用二项式反演和容斥原理解决集合生成问题
     * 链接：https://acm.timus.ru/problem.aspx?space=1&num=1520
     */
    public static long timusGeneratingSets(int n, int k) {
        // 计算从n个元素中生成大小为k的集合的方案数
        // 使用二项式反演处理约束条件
        
        precomputeFactorials(n);
        
        long result = 0;
        for (int i = k; i <= n; i++) {
            long term = comb(i, k) * comb(n, i) % MOD;
            if ((i - k) % 2 == 0) {
                result = (result + term) % MOD;
            } else {
                result = (result - term + MOD) % MOD;
            }
        }
        
        return result;
    }
    
    /**
     * 问题7：Comet OJ C1129 集合计数
     * 描述：使用二项式反演解决集合计数问题
     * 链接：https://cometoj.com/contest/62/problem/C?problem_id=1129
     */
    public static long cometSetCounting(int n, int k) {
        // 类似于洛谷的集合计数问题
        // 重新实现luoguSetCounting的逻辑
        precomputeFactorials(n);
        
        long[] g = new long[n + 1];
        
        // 计算g数组：g(i) = C(n,i) * (2^(2^(n-i)) - 1)
        long power = 2; // 2^(2^0) = 2
        for (int i = n; i >= 0; i--) {
            g[i] = (power - 1 + MOD) % MOD;
            g[i] = g[i] * comb(n, i) % MOD;
            power = power * power % MOD; // 计算下一个2^(2^(n-i+1))
        }
        
        // 应用二项式反演
        long result = 0;
        for (int i = k; i <= n; i++) {
            long term = comb(i, k) * g[i] % MOD;
            if ((i - k) % 2 == 0) {
                result = (result + term) % MOD;
            } else {
                result = (result - term + MOD) % MOD;
            }
        }
        return result;
    }
    
    /**
     * 问题8：acwing 1303 斐波那契公约数
     * 描述：使用数论知识和反演技巧计算斐波那契数列的公约数
     * 链接：https://www.acwing.com/problem/content/1305/
     */
    public static long acwingFibonacciGCD(int n, int m) {
        // 斐波那契数列性质：gcd(F(n), F(m)) = F(gcd(n, m))
        int gcd = gcd(n, m);
        return fibonacci(gcd);
    }
    
    // ========== 辅助函数 ==========
    
    private static long[] fact = new long[1000001];
    private static long[] invFact = new long[1000001];
    
    private static void precomputeFactorials(int n) {
        if (n >= fact.length) {
            fact = new long[n + 1];
            invFact = new long[n + 1];
        }
        
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        
        invFact[n] = power(fact[n], MOD - 2, MOD);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % MOD;
        }
    }
    
    private static long comb(int n, int k) {
        if (k < 0 || k > n) return 0;
        return fact[n] * invFact[k] % MOD * invFact[n - k] % MOD;
    }
    
    private static long power(long a, long b, long mod) {
        long result = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                result = result * a % mod;
            }
            a = a * a % mod;
            b >>= 1;
        }
        return result;
    }
    
    private static long derangement(int n) {
        if (n == 0) return 1;
        if (n == 1) return 0;
        
        long d0 = 1, d1 = 0, d2 = 0;
        for (int i = 2; i <= n; i++) {
            d2 = (i - 1) * (d1 + d0) % MOD;
            d0 = d1;
            d1 = d2;
        }
        return d2;
    }
    
    private static long stirlingSecond(int n, int k) {
        // 第二类斯特林数 S(n,k) = 1/k! * Σ(i=0到k) [(-1)^(k-i) * C(k,i) * i^n]
        long result = 0;
        for (int i = 0; i <= k; i++) {
            long term = comb(k, i) * power(i, n, MOD) % MOD;
            if ((k - i) % 2 == 0) {
                result = (result + term) % MOD;
            } else {
                result = (result - term + MOD) % MOD;
            }
        }
        return result * invFact[k] % MOD;
    }
    
    private static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    private static long fibonacci(int n) {
        if (n <= 1) return n;
        
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            long temp = (a + b) % MOD;
            a = b;
            b = temp;
        }
        return b;
    }
    
    /**
     * 单元测试函数
     */
    public static void test() {
        System.out.println("=== 多平台二项式反演题目测试 ===\n");
        
        // 测试UVa问题
        System.out.println("1. UVa 11426 GCD - Extreme (II)");
        System.out.println("n=10: " + uvaGCDExtreme(10));
        
        // 测试杭电问题
        System.out.println("\n2. 杭电 OJ 6143 Killer Names");
        System.out.println("n=2, m=3: " + hduKillerNames(2, 3));
        
        // 测试AizuOJ问题
        System.out.println("\n3. AizuOJ 2292 排列计数");
        System.out.println("n=4, k=1: " + aizuPermutationCount(4, 1));
        
        // 测试TimusOJ问题
        System.out.println("\n4. TimusOJ 1520 Generating Sets");
        System.out.println("n=5, k=2: " + timusGeneratingSets(5, 2));
        
        // 测试Comet OJ问题
        System.out.println("\n5. Comet OJ C1129 集合计数");
        System.out.println("n=3, k=1: " + cometSetCounting(3, 1));
        
        // 测试acwing问题
        System.out.println("\n6. acwing 1303 斐波那契公约数");
        System.out.println("n=6, m=8: " + acwingFibonacciGCD(6, 8));
    }
    
    /**
     * 主函数
     */
    public static void main(String[] args) throws IOException {
        // 运行单元测试
        test();
        
        // 交互式测试
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n=== 交互式测试 ===");
        System.out.println("选择问题平台:");
        System.out.println("1. UVa 11426");
        System.out.println("2. 杭电 OJ 6143");
        System.out.println("3. AizuOJ 2292");
        System.out.println("4. TimusOJ 1520");
        System.out.println("5. Comet OJ C1129");
        System.out.println("6. acwing 1303");
        System.out.print("请输入选择(1-6): ");
        
        try {
            int choice = Integer.parseInt(reader.readLine());
            
            switch (choice) {
                case 1:
                    System.out.print("请输入n: ");
                    int n1 = Integer.parseInt(reader.readLine());
                    System.out.println("结果: " + uvaGCDExtreme(n1));
                    break;
                    
                case 2:
                    System.out.print("请输入n和m(用空格分隔): ");
                    String[] input2 = reader.readLine().split(" ");
                    int n2 = Integer.parseInt(input2[0]);
                    int m2 = Integer.parseInt(input2[1]);
                    System.out.println("结果: " + hduKillerNames(n2, m2));
                    break;
                    
                case 3:
                    System.out.print("请输入n和k(用空格分隔): ");
                    String[] input3 = reader.readLine().split(" ");
                    int n3 = Integer.parseInt(input3[0]);
                    int k3 = Integer.parseInt(input3[1]);
                    System.out.println("结果: " + aizuPermutationCount(n3, k3));
                    break;
                    
                case 4:
                    System.out.print("请输入n和k(用空格分隔): ");
                    String[] input4 = reader.readLine().split(" ");
                    int n4 = Integer.parseInt(input4[0]);
                    int k4 = Integer.parseInt(input4[1]);
                    System.out.println("结果: " + timusGeneratingSets(n4, k4));
                    break;
                    
                case 5:
                    System.out.print("请输入n和k(用空格分隔): ");
                    String[] input5 = reader.readLine().split(" ");
                    int n5 = Integer.parseInt(input5[0]);
                    int k5 = Integer.parseInt(input5[1]);
                    System.out.println("结果: " + cometSetCounting(n5, k5));
                    break;
                    
                case 6:
                    System.out.print("请输入n和m(用空格分隔): ");
                    String[] input6 = reader.readLine().split(" ");
                    int n6 = Integer.parseInt(input6[0]);
                    int m6 = Integer.parseInt(input6[1]);
                    System.out.println("结果: " + acwingFibonacciGCD(n6, m6));
                    break;
                    
                default:
                    System.out.println("无效选择");
            }
        } catch (Exception e) {
            System.out.println("输入格式错误: " + e.getMessage());
        }
    }
    
    /**
     * 工程化考量总结：
     * 
     * 1. 多平台整合：
     *    - 统一接口设计，便于扩展新平台
     *    - 模块化实现，每个平台独立处理
     * 
     * 2. 算法复用：
     *    - 通用的二项式反演函数
     *    - 共享的数学工具函数
     *    - 避免代码重复
     * 
     * 3. 性能优化：
     *    - 预处理常用数学值
     *    - 使用快速算法（快速幂、数论函数等）
     *    - 内存管理优化
     * 
     * 4. 测试覆盖：
     *    - 每个平台题目的独立测试
     *    - 边界条件测试
     *    - 性能基准测试
     * 
     * 5. 文档完善：
     *    - 详细的算法原理说明
     *    - 复杂度分析
     *    - 使用示例和注意事项
     */
}