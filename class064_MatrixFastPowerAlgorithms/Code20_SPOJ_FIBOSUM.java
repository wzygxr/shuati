package class098;

/**
 * SPOJ FIBOSUM - Fibonacci Sum
 * 题目链接: https://www.spoj.com/problems/FIBOSUM/
 * 题目大意: 给定两个整数n和m，求斐波那契数列从第n项到第m项的和，结果对1000000007取模
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 斐波那契数列定义: F(0)=0, F(1)=1, F(n)=F(n-1)+F(n-2)
 * 2. 斐波那契数列前n项和: S(n) = F(0)+F(1)+...+F(n) = F(n+2)-1
 * 3. 从第n项到第m项的和: S(m) - S(n-1) = F(m+2) - F(n+1)
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(m-n)降低到O(log(max(n,m)))
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n>m, n=0, m=0等特殊情况
 * 2. 输入验证: 检查n和m的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(m-n)，会超时
 * 2. 动态规划: 时间复杂度O(m)，空间复杂度O(1)
 * 3. 矩阵快速幂: 时间复杂度O(log(max(n,m)))，空间复杂度O(1)，最优解
 */
import java.util.Scanner;

public class Code20_SPOJ_FIBOSUM {

    static final long MOD = 1000000007;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt(); // 测试用例数量
        
        for (int t = 0; t < T; t++) {
            long n = scanner.nextLong();
            long m = scanner.nextLong();
            
            // 特殊情况处理
            if (n > m) {
                System.out.println(0);
                continue;
            }
            
            long result = solve(n, m);
            System.out.println(result);
        }
        
        scanner.close();
    }

    /**
     * 计算斐波那契数列从第n项到第m项的和
     * 时间复杂度: O(log(max(n,m)))
     * 空间复杂度: O(1)
     * 
     * 算法思路:
     * 1. 计算F(m+2)和F(n+1)
     * 2. 结果 = F(m+2) - F(n+1)
     * 3. 对结果取模并处理负数情况
     */
    public static long solve(long n, long m) {
        // S(m) - S(n-1) = F(m+2) - F(n+1)
        long fib_m_plus_2 = fibonacci(m + 2);
        long fib_n_plus_1 = fibonacci(n + 1);
        
        long result = (fib_m_plus_2 - fib_n_plus_1) % MOD;
        if (result < 0) {
            result += MOD;
        }
        return result;
    }

    /**
     * 计算斐波那契数列第n项
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
     * 
     * 算法思路:
     * 1. 使用矩阵快速幂计算斐波那契数
     * 2. 转移矩阵: [[1,1],[1,0]]
     * 3. 初始向量: [F(1), F(0)] = [1, 0]
     */
    public static long fibonacci(long n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        // 转移矩阵
        long[][] base = {
            {1, 1},
            {1, 0}
        };
        
        // 计算转移矩阵的n-1次幂
        long[][] result = matrixPower(base, n - 1);
        
        // 初始向量 [F(1), F(0)] = [1, 0]
        // 结果为 result * [1, 0]^T 的第一个元素
        return result[0][0];
    }

    /**
     * 2x2矩阵乘法
     * 时间复杂度: O(2^3) = O(8) = O(1)
     * 空间复杂度: O(4) = O(1)
     */
    public static long[][] matrixMultiply(long[][] a, long[][] b) {
        long[][] res = new long[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                res[i][j] = 0;
                for (int k = 0; k < 2; k++) {
                    res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 时间复杂度: O(2^2) = O(4) = O(1)
     * 空间复杂度: O(4) = O(1)
     */
    public static long[][] identityMatrix() {
        long[][] res = new long[2][2];
        res[0][0] = 1;
        res[0][1] = 0;
        res[1][0] = 0;
        res[1][1] = 1;
        return res;
    }

    /**
     * 矩阵快速幂
     * 时间复杂度: O(2^3 * logn) = O(logn)
     * 空间复杂度: O(4) = O(1)
     */
    public static long[][] matrixPower(long[][] base, long exp) {
        long[][] res = identityMatrix();
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = matrixMultiply(res, base);
            }
            base = matrixMultiply(base, base);
            exp >>= 1;
        }
        return res;
    }
}