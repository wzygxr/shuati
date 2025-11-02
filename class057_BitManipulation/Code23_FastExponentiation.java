package class031;

/**
 * 位运算实现快速幂算法
 * 测试链接：https://leetcode.cn/problems/powx-n/
 * 
 * 题目描述：
 * 实现 pow(x, n)，即计算 x 的 n 次幂函数。
 * 
 * 示例：
 * 输入：x = 2.00000, n = 10
 * 输出：1024.00000
 * 
 * 输入：x = 2.10000, n = 3
 * 输出：9.26100
 * 
 * 输入：x = 2.00000, n = -2
 * 输出：0.25000
 * 解释：2^-2 = 1/2^2 = 1/4 = 0.25
 * 
 * 提示：
 * -100.0 < x < 100.0
 * -2^31 <= n <= 2^31-1
 * n 是一个整数
 * 要么 x 不为零，要么 n > 0
 * -10^4 <= x^n <= 10^4
 * 
 * 解题思路：
 * 快速幂算法（Exponentiation by Squaring）利用位运算和分治思想，将时间复杂度从O(n)降低到O(log n)。
 * 核心思想：x^n = (x^2)^(n/2) * x^(n % 2)
 * 
 * 时间复杂度：O(log n) - 每次将指数减半
 * 空间复杂度：O(1) - 只使用常数个变量
 */
public class Code23_FastExponentiation {
    
    /**
     * 使用快速幂算法计算x的n次方
     * @param x 底数
     * @param n 指数
     * @return x的n次方结果
     */
    public static double myPow(double x, int n) {
        // 处理特殊情况
        if (n == 0) return 1.0;
        if (x == 0.0) return 0.0;
        if (x == 1.0) return 1.0;
        if (x == -1.0) return (n % 2 == 0) ? 1.0 : -1.0;
        
        // 处理n为最小值的情况（避免整数溢出）
        long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        
        double result = 1.0;
        double current = x;
        
        // 使用位运算快速计算幂
        while (N > 0) {
            // 如果当前位为1，则乘以对应的幂
            if ((N & 1) == 1) {
                result *= current;
            }
            // 平方当前值
            current *= current;
            // 右移一位（相当于除以2）
            N >>= 1;
        }
        
        return result;
    }
    
    /**
     * 递归实现快速幂算法
     * @param x 底数
     * @param n 指数
     * @return x的n次方结果
     */
    public static double myPowRecursive(double x, int n) {
        // 处理特殊情况
        if (n == 0) return 1.0;
        if (x == 0.0) return 0.0;
        
        long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        
        return fastPow(x, N);
    }
    
    private static double fastPow(double x, long n) {
        if (n == 0) return 1.0;
        
        double half = fastPow(x, n / 2);
        
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
    
    /**
     * 处理大数取模的快速幂算法（常用于密码学）
     * @param x 底数
     * @param n 指数
     * @param mod 模数
     * @return (x^n) % mod
     */
    public static long modPow(long x, long n, long mod) {
        if (mod == 1) return 0;
        if (n == 0) return 1;
        
        x = x % mod;
        long result = 1;
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result = (result * x) % mod;
            }
            x = (x * x) % mod;
            n >>= 1;
        }
        
        return result;
    }
    
    /**
     * 矩阵快速幂算法（用于斐波那契数列等）
     * 计算矩阵的n次幂
     * @param matrix 2x2矩阵
     * @param n 指数
     * @return 矩阵的n次幂
     */
    public static long[][] matrixPow(long[][] matrix, int n) {
        // 单位矩阵
        long[][] result = {{1, 0}, {0, 1}};
        long[][] current = matrix.clone();
        
        while (n > 0) {
            if ((n & 1) == 1) {
                result = multiplyMatrix(result, current);
            }
            current = multiplyMatrix(current, current);
            n >>= 1;
        }
        
        return result;
    }
    
    /**
     * 2x2矩阵乘法
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @return 矩阵乘积
     */
    private static long[][] multiplyMatrix(long[][] a, long[][] b) {
        long[][] result = new long[2][2];
        result[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        result[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        result[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        result[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
        return result;
    }
    
    /**
     * 使用矩阵快速幂计算斐波那契数列第n项
     * @param n 项数
     * @return 斐波那契数列第n项
     */
    public static long fibonacci(int n) {
        if (n <= 1) return n;
        
        long[][] base = {{1, 1}, {1, 0}};
        long[][] result = matrixPow(base, n - 1);
        return result[0][0];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试基本快速幂
        System.out.println("=== 基本快速幂测试 ===");
        System.out.println("2^10 = " + myPow(2.0, 10)); // 1024.0
        System.out.println("2.1^3 = " + myPow(2.1, 3)); // 9.261
        System.out.println("2^-2 = " + myPow(2.0, -2)); // 0.25
        System.out.println("0^5 = " + myPow(0.0, 5)); // 0.0
        System.out.println("1^100 = " + myPow(1.0, 100)); // 1.0
        
        // 测试递归实现
        System.out.println("\n=== 递归快速幂测试 ===");
        System.out.println("2^10 = " + myPowRecursive(2.0, 10)); // 1024.0
        System.out.println("2^-2 = " + myPowRecursive(2.0, -2)); // 0.25
        
        // 测试模幂运算
        System.out.println("\n=== 模幂运算测试 ===");
        System.out.println("3^10 mod 7 = " + modPow(3, 10, 7)); // 3^10=59049, 59049%7=4
        System.out.println("2^100 mod 13 = " + modPow(2, 100, 13)); // 2^100 mod 13 = 3
        
        // 测试矩阵快速幂（斐波那契数列）
        System.out.println("\n=== 矩阵快速幂测试（斐波那契） ===");
        System.out.println("F(10) = " + fibonacci(10)); // 55
        System.out.println("F(20) = " + fibonacci(20)); // 6765
        
        // 性能对比测试
        System.out.println("\n=== 性能对比测试 ===");
        long startTime, endTime;
        
        // 快速幂
        startTime = System.nanoTime();
        double result1 = myPow(2.0, 1000000);
        endTime = System.nanoTime();
        System.out.println("快速幂耗时: " + (endTime - startTime) + " ns");
        
        // 普通幂运算（对比）
        startTime = System.nanoTime();
        double result2 = 1.0;
        for (int i = 0; i < 1000000; i++) {
            result2 *= 2.0;
        }
        endTime = System.nanoTime();
        System.out.println("普通幂运算耗时: " + (endTime - startTime) + " ns");
    }
    
    /**
     * 工程化考量：
     * 1. 边界条件处理：指数为0、底数为0、负指数等情况
     * 2. 整数溢出：处理n为Integer.MIN_VALUE的情况
     * 3. 精度问题：浮点数运算的精度控制
     * 4. 性能优化：使用位运算替代乘除法和取模运算
     * 5. 异常处理：输入验证和错误处理
     * 
     * 应用场景：
     * 1. 密码学：RSA加密、Diffie-Hellman密钥交换
     * 2. 数值计算：科学计算、图形学
     * 3. 算法优化：动态规划、组合数学
     * 4. 数学问题：斐波那契数列、线性递推
     * 
     * 算法原理：
     * 快速幂算法的核心思想是将指数n分解为二进制形式，然后通过平方和乘法组合结果。
     * 例如：计算3^13
     * 13的二进制：1101
     * 3^13 = 3^(8+4+1) = 3^8 * 3^4 * 3^1
     * 通过不断平方：3^1, 3^2, 3^4, 3^8...
     * 然后根据二进制位选择需要乘入结果的幂
     */
}