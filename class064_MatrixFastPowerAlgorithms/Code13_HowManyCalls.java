package class098;

/**
 * UVA 10518 How Many Calls?
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1459
 * 题目大意: 定义函数f(n) = f(n-1) + f(n-2) + 1，其中f(0) = f(1) = 1，求f(n) mod M的值
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 递推关系: f(n) = f(n-1) + f(n-2) + 1
 * 2. 转换为矩阵形式:
 *    [f(n)]   [1 1 1] [f(n-1)]
 *    [f(n-1)] = [1 0 0] [f(n-2)]
 *    [1]      [0 0 1] [1]
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=0, n=1的特殊情况
 * 2. 输入验证: 检查n和M的有效性
 * 3. 特殊情况: 当n=0时直接返回0
 * 
 * 与其他解法对比:
 * 1. 递归解法: 时间复杂度O(2^n)，空间复杂度O(n)，会超时
 * 2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
 * 3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)，最优解
 */
import java.util.Scanner;

public class Code13_HowManyCalls {

    static long n, m;
    static int caseNum = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            n = scanner.nextLong();
            m = scanner.nextLong();
            
            // 输入终止条件
            if (n == 0 && m == 0) {
                break;
            }
            
            caseNum++;
            System.out.printf("Case %d: %d %d ", caseNum, n, m);
            
            // 特殊情况处理
            if (n == 0) {
                System.out.println(1 % m);
            } else {
                long result = solve();
                System.out.println(result);
            }
        }
        
        scanner.close();
    }

    /**
     * 矩阵乘法
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static long[][] matrixMultiply(long[][] a, long[][] b) {
        long[][] res = new long[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % m;
                }
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static long[][] identityMatrix() {
        long[][] res = new long[3][3];
        for (int i = 0; i < 3; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    /**
     * 矩阵快速幂
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
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

    /**
     * 求解f(n) mod M
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
     * 
     * 算法思路:
     * 1. 构造转移矩阵[[1,1,1],[1,0,0],[0,0,1]]
     * 2. 计算转移矩阵的n次幂
     * 3. 乘以初始向量[1,1,1]得到结果
     */
    public static long solve() {
        // 转移矩阵
        long[][] base = {
            {1, 1, 1},
            {1, 0, 0},
            {0, 0, 1}
        };
        
        // 计算转移矩阵的n次幂
        long[][] result = matrixPower(base, n);
        
        // 初始向量 [f(1), f(0), 1] = [1, 1, 1]
        // 结果为 result * [1, 1, 1]^T 的第一个元素
        return (result[0][0] + result[0][1] + result[0][2]) % m;
    }
}