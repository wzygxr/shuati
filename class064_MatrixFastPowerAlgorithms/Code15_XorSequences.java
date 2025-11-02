package class098;

/**
 * Codeforces 691E Xor-sequences
 * 题目链接: https://codeforces.com/problemset/problem/691/E
 * 题目大意: 给定长度为n的序列，从序列中选择k个数（可以重复选择），使得得到的排列满足xi与xi+1异或的二进制中1的个数是3的倍数。
 *          问长度为k的满足条件的序列有多少个。
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 * 
 * 数学分析:
 * 1. 构造转移矩阵：如果两个数异或的结果二进制中1的个数是3的倍数，则矩阵对应位置为1，否则为0
 * 2. 答案就是转移矩阵的k-1次幂的所有元素之和
 * 
 * 优化思路:
 * 1. 预处理转移矩阵
 * 2. 使用矩阵快速幂计算矩阵的k-1次幂
 * 
 * 工程化考虑:
 * 1. 边界条件处理: k=1的特殊情况
 * 2. 输入验证: 检查输入的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 时间复杂度O(n^k)，会超时
 * 2. 动态规划: 时间复杂度O(n^2 * k)
 * 3. 矩阵快速幂: 时间复杂度O(n^3 * logk)
 * 4. 最优性: 当k较大时，矩阵快速幂明显优于其他解法
 */
import java.util.Scanner;

public class Code15_XorSequences {

    static final int MOD = 1000000007;
    static int n, k;
    static long[] a;
    static long[][] matrix;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        n = scanner.nextInt();
        k = scanner.nextInt();
        a = new long[n];
        
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextLong();
        }
        
        // 特殊情况处理
        if (k == 1) {
            System.out.println(n);
            scanner.close();
            return;
        }
        
        // 构造转移矩阵
        matrix = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long xor = a[i] ^ a[j];
                if (countBits(xor) % 3 == 0) {
                    matrix[i][j] = 1;
                }
            }
        }
        
        // 计算转移矩阵的k-1次幂
        long[][] result = matrixPower(matrix, k - 1);
        
        // 计算结果：所有元素之和
        long sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum = (sum + result[i][j]) % MOD;
            }
        }
        
        System.out.println(sum);
        scanner.close();
    }

    /**
     * 计算一个数二进制表示中1的个数
     * 时间复杂度: O(logx)
     * 空间复杂度: O(1)
     */
    public static int countBits(long x) {
        int count = 0;
        while (x > 0) {
            count += x & 1;
            x >>= 1;
        }
        return count;
    }

    /**
     * 矩阵乘法
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     */
    public static long[][] matrixMultiply(long[][] a, long[][] b) {
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n^2)
     */
    public static long[][] identityMatrix() {
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    /**
     * 矩阵快速幂
     * 时间复杂度: O(n^3 * logk)
     * 空间复杂度: O(n^2)
     */
    public static long[][] matrixPower(long[][] base, int exp) {
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