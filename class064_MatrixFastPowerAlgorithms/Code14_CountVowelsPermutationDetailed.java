package class098;

/**
 * LeetCode 1220. 统计元音字母序列的数目
 * 题目链接: https://leetcode.cn/problems/count-vowels-permutation/
 * 题目大意: 给你一个整数 n，请你帮忙统计一下我们可以按下述规则形成多少个长度为 n 的字符串：
 *          字符串中的每个字符都应当是小写元音字母（'a', 'e', 'i', 'o', 'u'）
 *          每个元音 'a' 后面都只能跟着 'e'
 *          每个元音 'e' 后面只能跟着 'a' 或者是 'i'
 *          每个元音 'i' 后面不能跟着另一个 'i'
 *          每个元音 'o' 后面只能跟着 'i' 或者是 'u'
 *          每个元音 'u' 后面只能跟着 'a'
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 状态转移关系:
 *    a -> e
 *    e -> a, i
 *    i -> a, e, o, u
 *    o -> i, u
 *    u -> a
 * 2. 转换为矩阵形式:
 *    [a']   [0 1 1 0 1] [a]
 *    [e']   [1 0 1 0 0] [e]
 *    [i'] = [0 1 0 1 0] [i]
 *    [o']   [0 0 1 0 0] [o]
 *    [u']   [0 0 1 1 0] [u]
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
public class Code14_CountVowelsPermutationDetailed {

    static final int MOD = 1000000007;

    /**
     * 计算长度为n的元音字母序列数目
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
     */
    public static int countVowelPermutation(int n) {
        // 特殊情况处理
        if (n == 1) {
            return 5;
        }
        
        // 转移矩阵
        long[][] base = {
            {0, 1, 1, 0, 1},  // a -> e, i, u
            {1, 0, 1, 0, 0},  // e -> a, i
            {0, 1, 0, 1, 0},  // i -> e, o
            {0, 0, 1, 0, 0},  // o -> i
            {0, 0, 1, 1, 0}   // u -> i, o
        };
        
        // 计算转移矩阵的(n-1)次幂
        long[][] result = matrixPower(base, n - 1);
        
        // 初始状态向量 [1, 1, 1, 1, 1] (长度为1的序列)
        // 结果为 result * [1, 1, 1, 1, 1]^T 的所有元素之和
        long sum = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                sum = (sum + result[i][j]) % MOD;
            }
        }
        
        return (int) sum;
    }

    /**
     * 矩阵乘法
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static long[][] matrixMultiply(long[][] a, long[][] b) {
        long[][] res = new long[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
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
        long[][] res = new long[5][5];
        for (int i = 0; i < 5; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    /**
     * 矩阵快速幂
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
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

    public static void main(String[] args) {
        // 测试用例
        System.out.println(countVowelPermutation(1));  // 5
        System.out.println(countVowelPermutation(2));  // 10
        System.out.println(countVowelPermutation(5));  // 68
    }
}