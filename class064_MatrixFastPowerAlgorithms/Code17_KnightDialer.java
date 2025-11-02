package class098;

/**
 * LeetCode 935. 骑士拨号器
 * 
 * 题目链接: https://leetcode.cn/problems/knight-dialer/
 * 
 * 题目大意: 
 * 国际象棋中的骑士可以按照"日"字形移动，骑士在电话拨号盘上移动，计算骑士走n步的不同路径数
 * 
 * 问题分析:
 * 1. 电话拨号盘布局:
 *    1 2 3
 *    4 5 6
 *    7 8 9
 *    * 0 #
 * 2. 骑士移动规则: 从每个数字可以移动到特定的其他数字
 * 3. 求走n步的不同路径数
 * 
 * 解法分析:
 * 该问题可以转化为图论中的路径计数问题，使用矩阵快速幂求解
 * 
 * 数学建模:
 * 1. 构建10×10的转移矩阵表示骑士移动可能性
 * 2. 矩阵A[i][j] = 1表示可以从数字i移动到数字j，否则为0
 * 3. 转移矩阵的n-1次幂中所有元素之和即为答案
 * 
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=1的特殊情况
 * 2. 输入验证: 检查n的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
 * 2. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)
 * 3. 最优性: 当n较大时，矩阵快速幂明显优于动态规划
 */
public class Code17_KnightDialer {

    static final int MOD = 1000000007;

    /**
     * 计算骑士在拨号盘上走n步的不同路径数
     * 
     * 算法思路:
     * 1. 构建转移矩阵表示骑士移动规则
     * 2. 使用矩阵快速幂计算转移矩阵的n-1次幂
     * 3. 结果矩阵的所有元素之和即为答案
     * 
     * 数学原理:
     * 设f[i][j]表示走i步到达数字j的方案数
     * 则 f[i][j] = Σ(f[i-1][k] * A[k][j]) for all k
     * 其中A为转移矩阵
     * 
     * 通过矩阵表示:
     * [f[i][0], f[i][1], ..., f[i][9]] = [f[i-1][0], ..., f[i-1][9]] * A
     * 
     * 展开可得:
     * [f[n-1][0], ..., f[n-1][9]] = [f[0][0], ..., f[0][9]] * A^(n-1)
     * 
     * 初始状态: f[0][i] = 1 (可以从任意数字开始)
     * 答案: Σ(f[n-1][i]) for i in 0..9
     * 
     * @param n 步数
     * @return 不同路径数
     */
    public static int knightDialer(int n) {
        // 特殊情况处理
        if (n == 1) {
            return 10;  // 只走一步，可以从任意数字开始，共10种
        }
        
        // 骑士移动规则: 从每个数字可以移动到哪些数字
        // 0: 4,6
        // 1: 6,8
        // 2: 7,9
        // 3: 4,8
        // 4: 0,3,9
        // 5: 无
        // 6: 0,1,7
        // 7: 2,6
        // 8: 1,3
        // 9: 2,4
        
        // 构建10×10的转移矩阵
        long[][] base = new long[10][10];
        
        // 从0可以移动到4,6
        base[0][4] = 1;
        base[0][6] = 1;
        
        // 从1可以移动到6,8
        base[1][6] = 1;
        base[1][8] = 1;
        
        // 从2可以移动到7,9
        base[2][7] = 1;
        base[2][9] = 1;
        
        // 从3可以移动到4,8
        base[3][4] = 1;
        base[3][8] = 1;
        
        // 从4可以移动到0,3,9
        base[4][0] = 1;
        base[4][3] = 1;
        base[4][9] = 1;
        
        // 从5不能移动
        // base[5][*] = 0
        
        // 从6可以移动到0,1,7
        base[6][0] = 1;
        base[6][1] = 1;
        base[6][7] = 1;
        
        // 从7可以移动到2,6
        base[7][2] = 1;
        base[7][6] = 1;
        
        // 从8可以移动到1,3
        base[8][1] = 1;
        base[8][3] = 1;
        
        // 从9可以移动到2,4
        base[9][2] = 1;
        base[9][4] = 1;
        
        // 计算转移矩阵的n-1次幂
        long[][] result = matrixPower(base, n - 1);
        
        // 计算结果：所有元素之和
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                sum = (sum + result[i][j]) % MOD;
            }
        }
        
        return (int) sum;
    }

    /**
     * 矩阵乘法
     * 
     * 算法原理:
     * 对于矩阵A(size×size)和矩阵B(size×size)，结果矩阵C(size×size)中:
     * C[i][j] = Σ(A[i][k] * B[k][j]) for k in 0..size-1
     * 
     * 时间复杂度: O(size^3)
     * 空间复杂度: O(size^2)
     * 
     * 实现细节:
     * - 每步都进行模运算防止溢出
     * 
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @return 两个矩阵的乘积
     */
    public static long[][] matrixMultiply(long[][] a, long[][] b) {
        int size = a.length;
        long[][] res = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 
     * 数学性质:
     * 单位矩阵I满足: I * A = A * I = A
     * 主对角线上元素为1，其余为0
     * 
     * 时间复杂度: O(size^2)
     * 空间复杂度: O(size^2)
     * 
     * @param size 矩阵维度
     * @return size×size的单位矩阵
     */
    public static long[][] identityMatrix(int size) {
        long[][] res = new long[size][size];
        for (int i = 0; i < size; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    /**
     * 矩阵快速幂
     * 
     * 算法原理:
     * 利用二进制分解指数，通过不断平方和累积结果实现快速计算
     * 例如: A^13，13的二进制为1101
     * A^13 = A^8 * A^4 * A^1 (对应二进制位为1的位置)
     * 
     * 时间复杂度: O(size^3 * logexp)
     * 空间复杂度: O(size^2)
     * 
     * 实现技巧:
     * - 使用位运算优化指数分解
     * - 结果初始化为单位矩阵
     * 
     * @param base 底数矩阵
     * @param exp 指数
     * @return 矩阵base的exp次幂
     */
    public static long[][] matrixPower(long[][] base, int exp) {
        int size = base.length;
        long[][] res = identityMatrix(size);
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = matrixMultiply(res, base);
            }
            base = matrixMultiply(base, base);
            exp >>= 1;
        }
        return res;
    }

    public static void main(String[] args) {
        // 测试用例
        System.out.println("n=1: " + knightDialer(1));  // 10
        System.out.println("n=2: " + knightDialer(2));  // 20
        System.out.println("n=3: " + knightDialer(3));  // 46
        System.out.println("n=4: " + knightDialer(4));  // 104
        System.out.println("n=3131: " + knightDialer(3131));  // 136006598
    }
}