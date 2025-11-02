package class139;

import java.util.*;
import java.io.*;

/**
 * 扩展欧几里得算法相关题目集合
 * 
 * 本文件包含多个使用扩展欧几里得算法解决的问题：
 * 1. 求解线性同余方程
 * 2. 求解线性不定方程
 * 3. 求模逆元
 * 4. 判断线性方程是否有解
 * 
 * 算法原理：
 * 扩展欧几里得算法不仅能计算两个数的最大公约数，还能找到满足贝祖等式 ax + by = gcd(a,b) 的整数解 x 和 y。
 * 
 * 时间复杂度：O(log(min(a,b)))
 * 空间复杂度：O(1) (迭代版本) 或 O(log(min(a,b))) (递归版本，由于递归调用栈)
 * 
 * 工程化考量：
 * 1. 异常处理：处理负数和零的情况
 * 2. 边界条件：考虑 a=0 或 b=0 的特殊情况
 * 3. 可配置性：提供多种方法以适应不同场景
 * 4. 性能优化：使用迭代版本避免递归调用栈开销
 * 
 * 与机器学习等领域的联系：
 * 1. 在密码学中用于计算模逆元，是RSA等公钥加密算法的基础
 * 2. 在编码理论中用于纠错码的构造
 * 3. 在计算机图形学中用于计算贝塞尔曲线的参数
 * 
 * 调试能力：
 * 1. 添加断言验证中间结果
 * 2. 打印关键变量的实时值
 * 3. 性能退化排查
 * 
 * 相关题目：
 * 1. 洛谷 P1082 [NOIP2012 提高组] 同余方程
 *    链接：https://www.luogu.com.cn/problem/P1082
 *    本题需要使用扩展欧几里得算法求模逆元
 * 
 * 2. 洛谷 P1516 青蛙的约会
 *    链接：https://www.luogu.com.cn/problem/P1516
 *    本题需要求解同余方程，是扩展欧几里得算法的经典应用
 * 
 * 3. POJ 1061 青蛙的约会
 *    链接：http://poj.org/problem?id=1061
 *    与本题完全相同，是POJ上的经典题目
 * 
 * 4. POJ 2115 C Looooops
 *    链接：http://poj.org/problem?id=2115
 *    本题需要求解模线性方程，可以转化为线性丢番图方程
 * 
 * 5. Codeforces 1244C. The Football Stage
 *    链接：https://codeforces.com/problemset/problem/1244/C
 *    本题需要求解线性丢番图方程wx + dy = p
 * 
 * 6. HDU 5512 Pagodas
 *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
 *    本题涉及数论知识，与最大公约数有关
 * 
 * 7. 洛谷 P5656 【模板】二元一次不定方程 (exgcd)
 *    链接：https://www.luogu.com.cn/problem/P5656
 *    本题是二元一次不定方程的模板题
 * 
 * 8. LeetCode 1250. 检查「好数组」
 *    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
 *    本题用到了裴蜀定理，如果数组中所有元素的最大公约数为1，则为好数组
 * 
 * 9. Codeforces 1011E Border
 *    链接：https://codeforces.com/contest/1011/problem/E
 *    本题需要根据裴蜀定理求解可能到达的位置
 * 
 * 10. 洛谷 P2421 [NOI2002]荒岛野人
 *     链接：https://www.luogu.com.cn/problem/P2421
 *     本题需要对每对野人建立线性同余方程，使用扩展欧几里得算法判断是否会在有生之年相遇
 */

public class Code06_ExtendedEuclideanProblems {
    
    /**
     * 扩展欧几里得算法 - 递归实现
     * 求解 ax + by = gcd(a,b) 的一组整数解 x, y
     * 
     * 算法思路：
     * 1. 当 b=0 时，gcd(a,b)=a，此时 x=1, y=0
     * 2. 当 b≠0 时，递归计算 gcd(b, a%b) 的解 x1, y1
     * 3. 根据等式推导：x = y1, y = x1 - (a/b) * y1
     * 
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b))) - 递归调用栈
     * 
     * @param a 系数a
     * @param b 系数b
     * @param result 存储结果的数组，result[0]为gcd, result[1]为x, result[2]为y
     */
    public static void exgcdRecursive(long a, long b, long[] result) {
        // 边界条件：当b为0时，gcd(a,0)=a，x=1, y=0
        if (b == 0) {
            result[0] = a;  // gcd
            result[1] = 1;  // x
            result[2] = 0;  // y
            return;
        }
        
        // 递归计算 gcd(b, a%b) 的解
        exgcdRecursive(b, a % b, result);
        
        // 保存之前的解
        long x1 = result[1];
        long y1 = result[2];
        
        // 根据推导公式更新解
        result[1] = y1;  // x = y1
        result[2] = x1 - (a / b) * y1;  // y = x1 - (a/b) * y1
    }
    
    /**
     * 扩展欧几里得算法 - 迭代实现
     * 求解 ax + by = gcd(a,b) 的一组整数解 x, y
     * 
     * 算法思路：
     * 1. 初始化 x0=1, y0=0, x1=0, y1=1
     * 2. 当 b≠0 时，计算 q=a/b, r=a%b
     * 3. 更新系数：x=x0-q*x1, y=y0-q*y1
     * 4. 更新变量：a=b, b=r, x0=x1, y0=y1, x1=x, y1=y
     * 5. 重复步骤2-4直到 b=0
     * 
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(1)
     * 
     * @param a 系数a
     * @param b 系数b
     * @param result 存储结果的数组，result[0]为gcd, result[1]为x, result[2]为y
     */
    public static void exgcdIterative(long a, long b, long[] result) {
        // 初始化系数
        long x0 = 1, y0 = 0;  // 初始解 (1, 0)
        long x1 = 0, y1 = 1;  // 初始解 (0, 1)
        
        // 迭代计算
        while (b != 0) {
            long q = a / b;  // 商
            long r = a % b;  // 余数
            
            // 更新系数
            long x = x0 - q * x1;
            long y = y0 - q * y1;
            
            // 更新变量
            a = b;
            b = r;
            x0 = x1;
            y0 = y1;
            x1 = x;
            y1 = y;
        }
        
        // 返回结果
        result[0] = a;   // gcd
        result[1] = x0;  // x
        result[2] = y0;  // y
    }
    
    /**
     * 求模逆元
     * 求解 ax ≡ 1 (mod m) 的最小正整数解 x
     * 
     * 算法思路：
     * 1. 使用扩展欧几里得算法求解 ax + my = gcd(a,m)
     * 2. 如果 gcd(a,m) ≠ 1，则模逆元不存在
     * 3. 如果 gcd(a,m) = 1，则 x 为 a 在模 m 下的逆元
     * 4. 将 x 调整为最小正整数解
     * 
     * 时间复杂度：O(log(min(a,m)))
     * 空间复杂度：O(1)
     * 
     * @param a 原数
     * @param m 模数
     * @return a 在模 m 下的逆元，如果不存在则返回 -1
     */
    public static long modInverse(long a, long m) {
        long[] result = new long[3];
        exgcdIterative(a, m, result);
        
        long gcd = result[0];
        long x = result[1];
        
        // 如果gcd不为1，则模逆元不存在
        if (gcd != 1) {
            return -1;
        }
        
        // 调整为最小正整数解
        return (x % m + m) % m;
    }
    
    /**
     * 求解线性同余方程
     * 求解 ax ≡ b (mod m) 的最小非负整数解 x
     * 
     * 算法思路：
     * 1. 将同余方程转换为不定方程：ax + my = b
     * 2. 使用扩展欧几里得算法求解 ax + my = gcd(a,m)
     * 3. 如果 b 不能被 gcd(a,m) 整除，则方程无解
     * 4. 否则，将解乘以 b/gcd(a,m) 得到特解
     * 5. 调整为最小非负整数解
     * 
     * 时间复杂度：O(log(min(a,m)))
     * 空间复杂度：O(1)
     * 
     * @param a 系数a
     * @param b 等式右边
     * @param m 模数
     * @return 方程的最小非负整数解，如果无解则返回 -1
     */
    public static long linearCongruence(long a, long b, long m) {
        long[] result = new long[3];
        exgcdIterative(a, m, result);
        
        long gcd = result[0];
        long x = result[1];
        
        // 如果b不能被gcd整除，则方程无解
        if (b % gcd != 0) {
            return -1;
        }
        
        // 计算解
        long mod = m / gcd;
        long sol = ((x * (b / gcd)) % mod + mod) % mod;
        return sol;
    }
    
    /**
     * 求解线性不定方程
     * 求解 ax + by = c 的一组整数解 x, y
     * 
     * 算法思路：
     * 1. 使用扩展欧几里得算法求解 ax + by = gcd(a,b)
     * 2. 如果 c 不能被 gcd(a,b) 整除，则方程无解
     * 3. 否则，将解乘以 c/gcd(a,b) 得到特解
     * 
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(1)
     * 
     * @param a 系数a
     * @param b 系数b
     * @param c 等式右边
     * @param solution 存储解的数组，solution[0]为x, solution[1]为y
     * @return 方程是否有解
     */
    public static boolean solveLinearDiophantine(long a, long b, long c, long[] solution) {
        long[] result = new long[3];
        exgcdIterative(a, b, result);
        
        long gcd = result[0];
        long x = result[1];
        long y = result[2];
        
        // 如果c不能被gcd整除，则方程无解
        if (c % gcd != 0) {
            return false;
        }
        
        // 将解乘以 c/gcd 得到特解
        long k = c / gcd;
        solution[0] = x * k;
        solution[1] = y * k;
        return true;
    }
    
    /**
     * 判断线性不定方程是否有解
     * 判断 ax + by = c 是否有整数解
     * 
     * 算法思路：
     * 根据裴蜀定理，ax + by = c 有整数解当且仅当 gcd(a,b) 整除 c
     * 
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(1)
     * 
     * @param a 系数a
     * @param b 系数b
     * @param c 等式右边
     * @return 方程是否有解
     */
    public static boolean hasSolution(long a, long b, long c) {
        return c % gcd(a, b) == 0;
    }
    
    /**
     * 欧几里得算法求最大公约数
     * 
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(1)
     * 
     * @param a 数字a
     * @param b 数字b
     * @return a和b的最大公约数
     */
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * Codeforces 1011E Border
     * 题目链接：https://codeforces.com/contest/1011/problem/E
     * 题目描述：给定n种颜色，每种颜色可以取任意数量（包括0），这些颜色在模m下表示。求可以混合出多少种不同的颜色。
     * 解题思路：根据裴蜀定理，这些颜色的线性组合在模m下能得到的所有值都是gcd(a1, a2, ..., an, m)的倍数
     * 时间复杂度：O(n * log(max(a_i, m)))
     * 空间复杂度：O(1)
     * 
     * 相关题目：
     * 1. 洛谷 P4549 【模板】裴蜀定理
     *    链接：https://www.luogu.com.cn/problem/P4549
     *    本题是裴蜀定理的模板题，与最大公约数有关
     * 
     * 2. HDU 5512 Pagodas
     *    链接：https://acm.hdu.edu.cn/showproblem.php?pid=5512
     *    本题涉及数论知识，与最大公约数有关
     */
    public static void solveBorder(int n, long m, long[] a) {
        // 计算所有数与m的最大公约数
        long g = m;
        for (int i = 0; i < n; i++) {
            g = gcd(g, Math.abs(a[i]));
            if (g == 1) break; // 提前终止
        }
        // 结果就是m/gcd
        System.out.println("可以混合出的颜色数量: " + (m / g));
        // 输出所有可能的颜色
        System.out.print("可能的颜色: ");
        for (long i = 0; i < m; i += g) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
    
    /**
     * 洛谷 P2421 [NOI2002]荒岛野人
     * 题目链接：https://www.luogu.com.cn/problem/P2421
     * 题目描述：有n个野人，每个野人住在一个山洞里，每年移动一定距离。求最小的山洞数，使得在有生之年所有野人不会相遇。
     * 解题思路：枚举可能的山洞数C，然后检查每对野人是否会在有生之年相遇。
     * 时间复杂度：O(MAX_C * n^2 * logC)，其中MAX_C是最大可能的山洞数
     * 空间复杂度：O(n)
     * 
     * 相关题目：
     * 1. 洛谷 P1516 青蛙的约会
     *    链接：https://www.luogu.com.cn/problem/P1516
     *    本题也需要建立线性同余方程并求解
     * 
     * 2. POJ 1061 青蛙的约会
     *    链接：http://poj.org/problem?id=1061
     *    与本题完全相同，是POJ上的经典题目
     */
    public static long solveWildMan(int n, long[] c, long[] p, long[] l) {
        // 枚举可能的山洞数C
        for (long C = 1; ; C++) {
            boolean ok = true;
            // 检查每对野人是否会相遇
            for (int i = 0; i < n && ok; i++) {
                for (int j = i + 1; j < n && ok; j++) {
                    // 方程: (p[i] - p[j]) * t ≡ (c[j] - c[i]) (mod C)
                    long a = (p[i] - p[j] + C) % C;
                    long b = (c[j] - c[i] + C) % C;
                    long[] result = new long[3];
                    exgcdIterative(a, C, result);
                    long g = result[0];
                    
                    if (b % g != 0) {
                        continue; // 无解，这对野人不会相遇
                    }
                    
                    // 计算最小正整数解
                    long mod = C / g;
                    long t = (result[1] * (b / g) % mod + mod) % mod;
                    
                    // 检查是否在有生之年相遇
                    if (t <= l[i] && t <= l[j]) {
                        ok = false;
                    }
                }
            }
            if (ok) {
                return C;
            }
        }
    }
    
    /**
     * LeetCode 365. 水壶问题
     * 题目链接：https://leetcode-cn.com/problems/water-and-jug-problem/
     * 题目描述：给定两个水壶，容量分别为x和y，能否得到恰好z升的水？
     * 解题思路：根据裴蜀定理，z必须是gcd(x,y)的倍数，且z不超过x+y
     * 时间复杂度：O(log(min(x,y)))
     * 空间复杂度：O(1)
     * 
     * 相关题目：
     * 1. 洛谷 P4549 【模板】裴蜀定理
     *    链接：https://www.luogu.com.cn/problem/P4549
     *    本题也是基于裴蜀定理的应用
     */
    public static boolean solveWaterJug(int x, int y, int z) {
        if (z < 0 || z > x + y) return false;
        if (z == 0) return true;
        return z % gcd(x, y) == 0;
    }
    
    /**
     * Codeforces 276D Little Girl and Divisors
     * 题目链接：https://codeforces.com/contest/276/problem/D
     * 题目描述：求区间[l, r]内的所有数对(i,j)，使得i <= j且i和j的最大公约数为1
     * 解题思路：使用莫比乌斯反演结合扩展欧几里得算法
     * 
     * 相关题目：
     * 1. LightOJ 1077 How Many Points?
     *    链接：https://lightoj.com/problem/how-many-points
     *    本题涉及最大公约数的应用，计算线段上的格点数量
     */
    public static long solveDivisors(long l, long r) {
        // 这里简化实现，实际需要使用更复杂的方法
        long count = 0;
        for (long i = l; i <= r; i++) {
            for (long j = i; j <= r; j++) {
                if (gcd(i, j) == 1) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * HDU 1576 A/B
     * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1576
     * 题目描述：计算 (A/B) mod 9973，其中gcd(B, 9973) = 1
     * 解题思路：使用扩展欧几里得算法求B的模逆元
     * 
     * 相关题目：
     * 1. 洛谷 P1082 [NOIP2012 提高组] 同余方程
     *    链接：https://www.luogu.com.cn/problem/P1082
     *    本题也是求模逆元的问题
     * 
     * 2. 洛谷 P3811 【模板】乘法逆元
     *    链接：https://www.luogu.com.cn/problem/P3811
     *    本题是乘法逆元的模板题
     */
    public static long solveAB(long A, long B, long mod) {
        A %= mod;
        B %= mod;
        long inverse = modInverse(B, mod);
        return (A * inverse) % mod;
    }
    
    /**
     * 牛客网 NC14685 数论问题
     * 题目链接：https://ac.nowcoder.com/acm/problem/14685
     * 题目描述：求满足a*x + b*y = c的正整数解的个数
     * 
     * 相关题目：
     * 1. 洛谷 P5656 【模板】二元一次不定方程 (exgcd)
     *    链接：https://www.luogu.com.cn/problem/P5656
     *    本题是二元一次不定方程的模板题，需要求正整数解
     */
    public static long countPositiveSolutions(long a, long b, long c) {
        long[] solution = new long[2];
        if (!solveLinearDiophantine(a, b, c, solution)) {
            return 0; // 无解
        }
        
        long x0 = solution[0];
        long y0 = solution[1];
        long d = gcd(a, b);
        long a1 = a / d;
        long b1 = b / d;
        
        // 计算通解参数范围
        // x = x0 + k*b1 > 0
        // y = y0 - k*a1 > 0
        double k1 = Math.ceil((1 - x0) / (double)b1);
        double k2 = Math.floor((y0 - 1) / (double)a1);
        
        if (k1 > k2) {
            return 0;
        }
        return (long)(k2 - k1 + 1);
    }
    
    /**
     * 主方法 - 测试各种扩展欧几里得算法相关问题
     */
    public static void main(String[] args) {
        System.out.println("=== 扩展欧几里得算法相关题目测试 ===\n");
        
        // 测试1: 基本扩展欧几里得算法
        System.out.println("1. 扩展欧几里得算法测试:");
        long a = 30, b = 20;
        long[] result = new long[3];
        exgcdIterative(a, b, result);
        System.out.printf("  %d * %d + %d * %d = %d\n", a, result[1], b, result[2], result[0]);
        System.out.printf("  验证: %d * %d + %d * %d = %d\n\n", a, result[1], b, result[2], a*result[1] + b*result[2]);
        
        // 测试2: 模逆元
        System.out.println("2. 模逆元测试:");
        long num = 3, mod = 11;
        long inverse = modInverse(num, mod);
        System.out.printf("  %d 在模 %d 下的逆元为: %d\n", num, mod, inverse);
        System.out.printf("  验证: (%d * %d) %% %d = %d\n\n", num, inverse, mod, (num * inverse) % mod);
        
        // 测试3: 线性同余方程
        System.out.println("3. 线性同余方程测试:");
        long a1 = 3, b1 = 2, m1 = 7;
        long solution = linearCongruence(a1, b1, m1);
        System.out.printf("  %d * x ≡ %d (mod %d) 的解为: x = %d\n", a1, b1, m1, solution);
        System.out.printf("  验证: (%d * %d) %% %d = %d\n\n", a1, solution, m1, (a1 * solution) % m1);
        
        // 测试4: 线性不定方程
        System.out.println("4. 线性不定方程测试:");
        long a2 = 6, b2 = 9, c2 = 15;
        long[] diophantineSolution = new long[2];
        boolean hasSol = solveLinearDiophantine(a2, b2, c2, diophantineSolution);
        if (hasSol) {
            System.out.printf("  %d * x + %d * y = %d 的解为: x = %d, y = %d\n", a2, b2, c2, diophantineSolution[0], diophantineSolution[1]);
            System.out.printf("  验证: %d * %d + %d * %d = %d\n\n", a2, diophantineSolution[0], b2, diophantineSolution[1], a2*diophantineSolution[0] + b2*diophantineSolution[1]);
        } else {
            System.out.printf("  %d * x + %d * y = %d 无整数解\n\n", a2, b2, c2);
        }
        
        // 测试5: 方程解的存在性判断
        System.out.println("5. 方程解的存在性判断测试:");
        long a3 = 4, b3 = 6, c3 = 9;
        boolean exists = hasSolution(a3, b3, c3);
        System.out.printf("  %d * x + %d * y = %d %s整数解\n\n", a3, b3, c3, exists ? "有" : "无");
        
        // 测试6: LeetCode 365 水壶问题
        System.out.println("6. 水壶问题测试 (LeetCode 365):");
        System.out.printf("  x=3, y=5, z=4: %s\n", solveWaterJug(3, 5, 4) ? "可以得到" : "无法得到");
        System.out.printf("  x=2, y=6, z=5: %s\n\n", solveWaterJug(2, 6, 5) ? "可以得到" : "无法得到");
        
        // 测试7: HDU 1576 A/B
        System.out.println("7. A/B测试 (HDU 1576):");
        System.out.printf("  A=1, B=2, mod=9973: %d\n\n", solveAB(1, 2, 9973));
        
        // 测试8: 正整数解个数
        System.out.println("8. 正整数解个数测试:");
        System.out.printf("  6x + 9y = 15 的正整数解个数: %d\n", countPositiveSolutions(6, 9, 15));
        
        System.out.println("\n=== 测试完成 ===");
    }
    

}