package class137;

import java.util.Arrays;

/**
 * Codeforces 895C. Square Subsets
 * 题目链接：https://codeforces.com/contest/895/problem/C
 * 
 * 解题思路：
 * 这道题可以利用线性基和质因数分解来解决。
 * 1. 对于每个数，我们可以将其质因数分解，保留次数为奇数的质因数
 * 2. 这样，每个数可以表示为一个二进制向量，向量的每一位代表一个质数是否出现奇数次
 * 3. 问题转化为：在数组中选择一个非空子集，使得子集中所有数的向量异或结果为零向量
 * 4. 使用动态规划结合线性基来计算方案数
 * 
 * 时间复杂度：O(n * m * log m)，其中n是数组长度，m是质数的个数
 * 空间复杂度：O(2^m)，其中m是质数的个数
 */
public class Codeforces895C_SquareSubsets {
    private static final int MOD = 1000000007;
    private static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
    private static final int MAX_NUM = 70;
    private static final int PRIME_COUNT = PRIMES.length;
    
    /**
     * 计算满足条件的子集数目
     * @param a 输入数组
     * @return 满足条件的子集数目
     */
    public static int squareSubsets(int[] a) {
        // 统计每个数的出现次数
        int[] count = new int[MAX_NUM + 1];
        for (int num : a) {
            count[num]++;
        }
        
        // 初始化dp数组，dp[mask]表示异或结果为mask的子集数目
        long[] dp = new long[1 << PRIME_COUNT];
        dp[0] = 1; // 空子集
        
        // 处理每个数
        for (int num = 2; num <= MAX_NUM; num++) {
            if (count[num] == 0) {
                continue;
            }
            
            // 将数转换为向量：每个质数是否出现奇数次
            int mask = getMask(num);
            if (mask == 0) {
                // 这个数本身是平方数，可以选择任意次数，但至少选一次
                // 对于平方数，每个数可以选或不选，但至少选一个，所以贡献为 2^count[num] - 1
                long pow = 1;
                for (int i = 0; i < count[num]; i++) {
                    pow = (pow * 2) % MOD;
                }
                // 对于所有现有子集，可以选择添加任意非空的平方数集合
                for (int i = 0; i < (1 << PRIME_COUNT); i++) {
                    dp[i] = (dp[i] * pow) % MOD;
                }
            } else {
                // 非平方数，需要用线性基来处理
                // 创建一个临时数组，避免在更新过程中覆盖值
                long[] temp = Arrays.copyOf(dp, dp.length);
                long pow = 1; // 2^k - 1，表示选奇数个该数的方式数
                long pow2 = 1; // 2^(k-1)，表示选偶数个该数的方式数
                
                // 计算2^(count[num]-1) mod MOD
                for (int i = 0; i < count[num] - 1; i++) {
                    pow2 = (pow2 * 2) % MOD;
                }
                pow = (pow2 * 2) % MOD;
                
                // 对于每个现有的mask状态
                for (int i = 0; i < (1 << PRIME_COUNT); i++) {
                    // 选择奇数个该数
                    temp[i ^ mask] = (temp[i ^ mask] + dp[i] * pow2) % MOD;
                }
                
                dp = temp;
            }
        }
        
        // 减去空子集的情况
        return (int) ((dp[0] - 1 + MOD) % MOD);
    }
    
    /**
     * 将数字转换为向量表示
     * @param num 输入数字
     * @return 向量表示（二进制掩码）
     */
    private static int getMask(int num) {
        int mask = 0;
        for (int i = 0; i < PRIME_COUNT; i++) {
            int prime = PRIMES[i];
            int cnt = 0;
            while (num % prime == 0) {
                cnt++;
                num /= prime;
            }
            if (cnt % 2 == 1) {
                mask |= (1 << i);
            }
        }
        return mask;
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试用例1
        int[] a1 = {1, 1, 1};
        System.out.println("测试用例1结果: " + squareSubsets(a1)); // 预期输出: 7
        
        // 测试用例2
        int[] a2 = {2, 2, 2};
        System.out.println("测试用例2结果: " + squareSubsets(a2)); // 预期输出: 3
        
        // 测试用例3
        int[] a3 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};
        System.out.println("测试用例3结果: " + squareSubsets(a3)); // 预期输出: 1023
    }
}