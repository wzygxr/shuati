package number_theory;

import java.util.*;

/**
 * BSGS (Baby-Step Giant-Step) 算法实现
 * 
 * 算法简介:
 * BSGS算法用于求解离散对数问题: 给定a, b, p，求最小的非负整数x使得 a^x ≡ b (mod p)
 * 
 * 适用场景:
 * 1. 求解离散对数问题
 * 2. 当p较小时，可以使用BSGS算法
 * 3. 当p较大时，可以使用扩展BSGS算法
 * 
 * 核心思想:
 * 1. 将x表示为x = i*m - j，其中m = ceil(sqrt(p))
 * 2. 原式变为 a^(i*m - j) ≡ b (mod p)
 * 3. 移项得 a^(i*m) ≡ b * a^j (mod p)
 * 4. 预处理所有a^j (baby step)，然后枚举i计算a^(i*m)查找 (giant step)
 * 
 * 时间复杂度: O(sqrt(p))
 * 空间复杂度: O(sqrt(p))
 */
public class BSGS {
    private static final long MOD = 1000000007;
    
    /**
     * 快速幂运算
     */
    public static long powMod(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 扩展欧几里得算法
     */
    public static long[] extendedGcd(long a, long b) {
        if (b == 0) return new long[]{a, 1, 0};
        long[] result = extendedGcd(b, a % b);
        long gcd = result[0];
        long x = result[2];
        long y = result[1] - (a / b) * result[2];
        return new long[]{gcd, x, y};
    }
    
    /**
     * 模逆元
     */
    public static long modInverse(long a, long mod) {
        long[] result = extendedGcd(a, mod);
        if (result[0] != 1) return -1; // 不存在逆元
        return (result[1] % mod + mod) % mod;
    }
    
    /**
     * BSGS算法求解 a^x ≡ b (mod p)，其中gcd(a, p) = 1
     */
    public static long bsgs(long a, long b, long p) {
        a %= p;
        b %= p;
        if (b == 1) return 0;
        
        // 计算m = ceil(sqrt(p))
        long m = (long) Math.ceil(Math.sqrt(p));
        
        // Baby steps: 计算 a^j mod p 并存储到哈希表中
        Map<Long, Long> babySteps = new HashMap<>();
        long aj = 1;
        for (long j = 0; j < m; j++) {
            if (!babySteps.containsKey(aj)) {
                babySteps.put(aj, j);
            }
            aj = aj * a % p;
        }
        
        // Giant steps: 计算 γ = a^m mod p
        long gamma = powMod(a, m, p);
        
        // 查找满足条件的i
        long gammaI = 1;
        for (long i = 0; i < m; i++) {
            // 计算 b * (γ^i)^(-1) mod p
            long target = b * modInverse(gammaI, p) % p;
            if (babySteps.containsKey(target)) {
                long x = i * m + babySteps.get(target);
                if (x >= 0) return x;
            }
            gammaI = gammaI * gamma % p;
        }
        
        return -1; // 无解
    }
    
    /**
     * 扩展BSGS算法，处理gcd(a, p) ≠ 1的情况
     */
    public static long exBsgs(long a, long b, long p) {
        a %= p;
        b %= p;
        if (b == 1) return 0;
        
        long gcd = 1;
        long c = 0;
        long ap = a;
        long bp = b;
        long pp = p;
        
        // 处理gcd(a, p) ≠ 1的情况
        while ((gcd = gcd(ap, pp)) > 1) {
            if (bp % gcd != 0) return -1; // 无解
            pp /= gcd;
            bp /= gcd;
            c++;
            // 检查是否已经找到解
            long result = powMod(ap, c, p);
            if (result == bp) return c;
            ap = ap * a / gcd % pp;
        }
        
        // 使用BSGS算法求解约简后的方程
        long result = bsgs(ap, bp, pp);
        if (result == -1) return -1;
        return result + c;
    }
    
    /**
     * 求最大公约数
     */
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 洛谷P3846 [TJOI2007]可爱的质数/【模板】BSGS
     * 题目来源: https://www.luogu.com.cn/problem/P3846
     * 题目描述: 给定一个质数p，以及一个整数b，一个整数n，现在要求你计算一个最小的非负整数l，满足b^l ≡ n (mod p)
     * 解题思路: 直接使用BSGS算法求解离散对数问题
     * 时间复杂度: O(sqrt(p))
     * 空间复杂度: O(sqrt(p))
     * 
     * @param p 质数
     * @param b 底数
     * @param n 结果
     * @return 最小的非负整数l，如果无解返回-1
     */
    public static long solveP3846(long p, long b, long n) {
        // 特殊情况处理
        if (n == 1) return 0;  // b^0 = 1
        if (b == n) return 1;  // b^1 = b
        
        // 使用BSGS算法求解
        return bsgs(b, n, p);
    }
    
    /**
     * AtCoder ABC335 G - Discrete Logarithm Problems
     * 题目来源: https://atcoder.jp/contests/abc335/tasks/abc335_g
     * 题目描述: 给定N个整数A_1,...,A_N和素数P，求满足条件的整数对(i,j)的个数，
     * 条件是存在正整数k使得A_i^k ≡ A_j (mod P)
     * 解题思路: 对于每个A_i，我们预处理它能生成的所有值A_i^k mod P，然后统计每个值出现的次数。
     * 为了高效计算，我们使用BSGS算法来找出每个A_i生成的所有值。
     * 时间复杂度: O(N * sqrt(P))
     * 空间复杂度: O(N * sqrt(P))
     * 
     * @param n 整数个数
     * @param p 素数
     * @param a 整数数组
     * @return 满足条件的整数对(i,j)的个数
     */
    public static long solveABC335G(long n, long p, long[] a) {
        // 统计每个值出现的次数
        Map<Long, Long> valueCount = new HashMap<>();
        
        // 对于每个A_i，计算它能生成的所有值
        for (int i = 0; i < n; i++) {
            long ai = a[i];
            if (ai == 0) {
                // 特殊情况：0^k = 0 (k > 0)
                valueCount.put(0L, valueCount.getOrDefault(0L, 0L) + 1);
                continue;
            }
            
            // 使用BSGS算法找出ai生成的所有值
            Set<Long> generatedValues = new HashSet<>();
            long currentValue = ai % p;
            long cycleStart = -1;
            Map<Long, Integer> seen = new HashMap<>();
            
            // 找到循环节
            for (int k = 1; k <= p; k++) {
                if (seen.containsKey(currentValue)) {
                    cycleStart = seen.get(currentValue);
                    break;
                }
                seen.put(currentValue, k);
                generatedValues.add(currentValue);
                
                // 如果当前值是1，那么之后会循环
                if (currentValue == 1) {
                    break;
                }
                
                currentValue = currentValue * ai % p;
            }
            
            // 统计生成的值
            for (Long value : generatedValues) {
                valueCount.put(value, valueCount.getOrDefault(value, 0L) + 1);
            }
        }
        
        // 计算满足条件的对数
        long result = 0;
        for (int i = 0; i < n; i++) {
            long ai = a[i];
            if (valueCount.containsKey(ai)) {
                result += valueCount.get(ai);
            }
        }
        
        return result;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试BSGS算法
        long a1 = 2, b1 = 3, p1 = 11;
        long result1 = bsgs(a1, b1, p1);
        System.out.println("BSGS: " + a1 + "^x ≡ " + b1 + " (mod " + p1 + "), x = " + result1);
        
        // 测试扩展BSGS算法
        long a2 = 2, b2 = 3, p2 = 12;
        long result2 = exBsgs(a2, b2, p2);
        System.out.println("ExBSGS: " + a2 + "^x ≡ " + b2 + " (mod " + p2 + "), x = " + result2);
        
        // 测试洛谷P3846题目
        long p3 = 5, b3 = 2, n3 = 3;
        long result3 = solveP3846(p3, b3, n3);
        if (result3 == -1) {
            System.out.println("no solution");
        } else {
            System.out.println("P3846: " + b3 + "^x ≡ " + n3 + " (mod " + p3 + "), x = " + result3);
        }
        
        // 边界情况测试
        // 测试b^0 = 1的情况
        long p4 = 7, b4 = 3, n4 = 1;
        long result4 = solveP3846(p4, b4, n4);
        System.out.println("Boundary test 1: " + b4 + "^x ≡ " + n4 + " (mod " + p4 + "), x = " + result4);
        
        // 测试b^1 = b的情况
        long p5 = 11, b5 = 5, n5 = 5;
        long result5 = solveP3846(p5, b5, n5);
        System.out.println("Boundary test 2: " + b5 + "^x ≡ " + n5 + " (mod " + p5 + "), x = " + result5);
        
        // 测试无解情况
        long p6 = 7, b6 = 2, n6 = 3;  // 2^x ≡ 3 (mod 7) 无解
        long result6 = solveP3846(p6, b6, n6);
        if (result6 == -1) {
            System.out.println("Boundary test 3: " + b6 + "^x ≡ " + n6 + " (mod " + p6 + "), no solution");
        } else {
            System.out.println("Boundary test 3: " + b6 + "^x ≡ " + n6 + " (mod " + p6 + "), x = " + result6);
        }
        
        // 测试AtCoder ABC335 G题目
        long n7 = 3, p7 = 13;
        long[] a7 = {2, 3, 5};
        long result7 = solveABC335G(n7, p7, a7);
        System.out.println("ABC335G: n=" + n7 + ", p=" + p7 + ", result=" + result7);
    }
}