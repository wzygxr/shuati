package class099;

import java.util.Scanner;

/**
 * ZOJ 3609 Modular Inverse
 * 题目链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
 * 
 * 题目描述:
 * 给定两个整数a和m，求a在模m意义下的乘法逆元x，使得 a*x ≡ 1 (mod m)
 * 如果不存在这样的x，输出"Not Exist"
 * 
 * 解题思路:
 * 方法1: 扩展欧几里得算法
 * 方法2: 费马小定理（当m为质数时）
 * 
 * 时间复杂度:
 * - 扩展欧几里得算法: O(log(min(a, m)))
 * - 费马小定理: O(log m)
 * 
 * 空间复杂度: O(1)
 * 
 * 样例输入:
 * 3
 * 3 11
 * 4 12
 * 5 13
 * 
 * 样例输出:
 * 4
 * Not Exist
 * 8
 */
public class ZOJ3609_ModularInverse {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        
        for (int i = 0; i < t; i++) {
            long a = scanner.nextLong();
            long m = scanner.nextLong();
            
            long result = modInverse(a, m);
            if (result == -1) {
                System.out.println("Not Exist");
            } else {
                System.out.println(result);
            }
        }
        
        scanner.close();
    }
    
    /**
     * 使用扩展欧几里得算法求模逆元
     * 
     * @param a 要求逆元的数
     * @param m 模数
     * @return 如果存在逆元，返回最小正整数解；否则返回-1
     */
    public static long modInverse(long a, long m) {
        long x = 0, y = 0;
        long gcd = extendedGcd(a, m, x, y);
        
        // 如果gcd不为1，则逆元不存在
        if (gcd != 1) {
            return -1;
        }
        
        // 确保结果为正数
        return (x % m + m) % m;
    }
    
    /**
     * 扩展欧几里得算法
     * 求解 ax + by = gcd(a, b)
     * 
     * @param a 系数a
     * @param b 系数b
     * @param x 用于返回x的解
     * @param y 用于返回y的解
     * @return gcd(a, b)
     */
    public static long extendedGcd(long a, long b, long x, long y) {
        // 基本情况
        if (b == 0) {
            x = 1;
            y = 0;
            return a;
        }
        
        // 递归求解
        long x1 = 0, y1 = 0;
        long gcd = extendedGcd(b, a % b, x1, y1);
        
        // 更新x和y的值
        x = y1;
        y = x1 - (a / b) * y1;
        
        return gcd;
    }
    
    /**
     * 使用费马小定理求模逆元（当模数为质数时）
     * 根据费马小定理: a^(p-1) ≡ 1 (mod p)
     * 所以 a^(-1) ≡ a^(p-2) (mod p)
     * 
     * @param a 要求逆元的数
     * @param p 质数模数
     * @return a在模p意义下的逆元
     */
    public static long modInverseFermat(long a, long p) {
        return power(a, p - 2, p);
    }
    
    /**
     * 快速幂运算
     * 计算base^exp mod mod
     * 
     * @param base 底数
     * @param exp 指数
     * @param mod 模数
     * @return base^exp mod mod
     */
    public static long power(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp >>= 1;
        }
        
        return result;
    }
}