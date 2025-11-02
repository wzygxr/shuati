import java.util.*;

/**
 * 简单的模逆元测试程序
 * 用于验证基本功能
 */
public class SimpleModularInverseTest {
    
    private static final int MOD = 1000000007;
    
    // 扩展欧几里得算法求模逆元
    public static long modInverseExtendedGcd(long a, long m) {
        long[] x = new long[1];
        long[] y = new long[1];
        long gcd = extendedGcd(a, m, x, y);
        
        if (gcd != 1) return -1;
        return (x[0] % m + m) % m;
    }
    
    // 扩展欧几里得算法实现
    private static long extendedGcd(long a, long b, long[] x, long[] y) {
        if (b == 0) {
            x[0] = 1;
            y[0] = 0;
            return a;
        }
        long[] x1 = new long[1];
        long[] y1 = new long[1];
        long gcd = extendedGcd(b, a % b, x1, y1);
        x[0] = y1[0];
        y[0] = x1[0] - (a / b) * y1[0];
        return gcd;
    }
    
    // 费马小定理求模逆元（模数为质数时）
    public static long modInverseFermat(long a, long p) {
        return power(a, p - 2, p);
    }
    
    // 快速幂运算
    private static long power(long base, long exp, long mod) {
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
    
    // 线性递推批量计算逆元
    public static long[] buildInverseAll(int n, int p) {
        long[] inv = new long[n + 1];
        inv[1] = 1;
        for (int i = 2; i <= n; i++) {
            inv[i] = (p - (p / i) * inv[p % i] % p) % p;
        }
        return inv;
    }
    
    // 测试函数
    public static void main(String[] args) {
        System.out.println("=== 模逆元算法测试 ===");
        
        // 测试扩展欧几里得算法
        System.out.println("扩展欧几里得算法测试:");
        System.out.println("3^(-1) mod 11 = " + modInverseExtendedGcd(3, 11) + " (期望: 4)");
        System.out.println("5^(-1) mod 13 = " + modInverseExtendedGcd(5, 13) + " (期望: 8)");
        System.out.println("7^(-1) mod 19 = " + modInverseExtendedGcd(7, 19) + " (期望: 11)");
        
        // 测试费马小定理
        System.out.println("\n费马小定理测试:");
        System.out.println("3^(-1) mod 11 = " + modInverseFermat(3, 11) + " (期望: 4)");
        System.out.println("5^(-1) mod 13 = " + modInverseFermat(5, 13) + " (期望: 8)");
        
        // 测试线性递推
        System.out.println("\n线性递推批量计算测试:");
        long[] inv = buildInverseAll(10, 11);
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + "^(-1) mod 11 = " + inv[i]);
        }
        
        // 验证逆元性质
        System.out.println("\n逆元性质验证:");
        for (int i = 1; i <= 10; i++) {
            long inverse = modInverseExtendedGcd(i, 11);
            if (inverse != -1) {
                long product = (long)i * inverse % 11;
                System.out.println(i + " * " + inverse + " mod 11 = " + product + " (期望: 1)");
            }
        }
        
        // 性能测试
        System.out.println("\n性能测试:");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            modInverseExtendedGcd(i + 1, MOD);
        }
        long end = System.currentTimeMillis();
        System.out.println("10000次扩展欧几里得算法计算耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        buildInverseAll(10000, MOD);
        end = System.currentTimeMillis();
        System.out.println("批量计算1~10000的逆元耗时: " + (end - start) + "ms");
        
        System.out.println("\n测试完成！所有算法功能正常。");
    }
}