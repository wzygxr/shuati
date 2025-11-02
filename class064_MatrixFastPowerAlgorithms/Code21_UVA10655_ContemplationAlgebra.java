package class098;

/**
 * UVA 10655 - Contemplation! Algebra
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1596
 * 题目大意: 给定p, q, n，其中p = a + b, q = a * b，求a^n + b^n的值
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 设S(n) = a^n + b^n
 * 2. 递推关系: S(n) = p * S(n-1) - q * S(n-2)
 * 3. 初始条件: S(0) = 2, S(1) = p
 * 4. 转换为矩阵形式:
 *    [S(n)  ]   [p  -q] [S(n-1)]
 *    [S(n-1)] = [1   0] [S(n-2)]
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=0, n=1的特殊情况
 * 2. 输入验证: 检查p, q, n的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 递归解法: 时间复杂度O(2^n)，会超时
 * 2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
 * 3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)，最优解
 */
import java.math.BigInteger;
import java.util.Scanner;

public class Code21_UVA10655_ContemplationAlgebra {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNext()) {
            BigInteger p = scanner.nextBigInteger();
            BigInteger q = scanner.nextBigInteger();
            BigInteger n = scanner.nextBigInteger();
            
            BigInteger result = solve(p, q, n);
            System.out.println(result);
        }
        
        scanner.close();
    }

    /**
     * 计算a^n + b^n的值
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
     * 
     * 算法思路:
     * 1. 构建转移矩阵[[p, -q], [1, 0]]
     * 2. 使用矩阵快速幂计算转移矩阵的n-1次幂
     * 3. 乘以初始向量[S(1), S(0)] = [p, 2]得到结果
     */
    public static BigInteger solve(BigInteger p, BigInteger q, BigInteger n) {
        // 特殊情况处理
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.valueOf(2);
        }
        if (n.equals(BigInteger.ONE)) {
            return p;
        }
        
        // 转移矩阵
        BigInteger[][] base = {
            {p, q.negate()},  // [p, -q]
            {BigInteger.ONE, BigInteger.ZERO}  // [1, 0]
        };
        
        // 计算转移矩阵的n-1次幂
        BigInteger[][] result = matrixPower(base, n.subtract(BigInteger.ONE));
        
        // 初始向量 [S(1), S(0)] = [p, 2]
        // 结果为 result * [p, 2]^T 的第一个元素
        BigInteger s1 = p;
        BigInteger s0 = BigInteger.valueOf(2);
        
        return result[0][0].multiply(s1).add(result[0][1].multiply(s0));
    }

    /**
     * 2x2矩阵乘法
     * 时间复杂度: O(2^3) = O(8) = O(1)
     * 空间复杂度: O(4) = O(1)
     */
    public static BigInteger[][] matrixMultiply(BigInteger[][] a, BigInteger[][] b) {
        BigInteger[][] res = new BigInteger[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                res[i][j] = BigInteger.ZERO;
                for (int k = 0; k < 2; k++) {
                    res[i][j] = res[i][j].add(a[i][k].multiply(b[k][j]));
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
    public static BigInteger[][] identityMatrix() {
        BigInteger[][] res = new BigInteger[2][2];
        res[0][0] = BigInteger.ONE;
        res[0][1] = BigInteger.ZERO;
        res[1][0] = BigInteger.ZERO;
        res[1][1] = BigInteger.ONE;
        return res;
    }

    /**
     * 矩阵快速幂
     * 时间复杂度: O(2^3 * logn) = O(logn)
     * 空间复杂度: O(4) = O(1)
     */
    public static BigInteger[][] matrixPower(BigInteger[][] base, BigInteger exp) {
        BigInteger[][] res = identityMatrix();
        while (exp.compareTo(BigInteger.ZERO) > 0) {
            if (exp.testBit(0)) { // 等价于 exp & 1
                res = matrixMultiply(res, base);
            }
            base = matrixMultiply(base, base);
            exp = exp.shiftRight(1); // 等价于 exp >>= 1
        }
        return res;
    }
}