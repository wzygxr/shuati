package class140;

// LeetCode 878. 第 N 个神奇数字
// 如果正整数可以被 A 或 B 整除，那么它是神奇的。
// 返回第 N 个神奇数字。由于答案可能非常大，返回它模 10^9 + 7 的结果。
// 1 <= N <= 10^9
// 2 <= A, B <= 40000
// 测试链接：https://leetcode.cn/problems/nth-magical-number/

/**
 * LeetCode 878. 第 N 个神奇数字
 * 
 * 问题描述：
 * 如果正整数可以被 A 或 B 整除，那么它是神奇的。
 * 返回第 N 个神奇数字。由于答案可能非常大，返回它模 10^9 + 7 的结果。
 * 
 * 解题思路：
 * 1. 使用二分搜索法在可能的范围内查找第 N 个神奇数字
 * 2. 对于给定的数字 x，计算小于等于 x 的神奇数字个数
 * 3. 神奇数字个数 = x/A + x/B - x/lcm(A,B)
 * 4. 使用容斥原理避免重复计数
 * 
 * 数学原理：
 * 1. 容斥原理：|A ∪ B| = |A| + |B| - |A ∩ B|
 * 2. 最小公倍数：lcm(a,b) = a*b / gcd(a,b)
 * 3. 二分搜索：在有序序列中快速定位目标值
 * 
 * 时间复杂度：O(log(N * min(A,B)))，二分搜索的时间复杂度
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. LeetCode 878. 第 N 个神奇数字
 *    链接：https://leetcode.cn/problems/nth-magical-number/
 * 2. LeetCode 1201. 丑数 III
 *    链接：https://leetcode.cn/problems/ugly-number-iii/
 * 3. LeetCode 204. 计数质数
 *    链接：https://leetcode.cn/problems/count-primes/
 */

public class Code10_LeetCode878_NthMagicalNumber {
    
    private static final int MOD = 1000000007;
    
    /**
     * 计算第 N 个神奇数字
     * 
     * @param N 第 N 个
     * @param A 第一个除数
     * @param B 第二个除数
     * @return 第 N 个神奇数字模 10^9+7 的结果
     */
    public static int nthMagicalNumber(int N, int A, int B) {
        // 计算最小公倍数
        long lcm = lcm(A, B);
        
        // 二分搜索的左右边界
        long left = 1;
        long right = (long) N * Math.min(A, B);
        
        while (left < right) {
            long mid = left + (right - left) / 2;
            // 计算小于等于 mid 的神奇数字个数
            long count = mid / A + mid / B - mid / lcm;
            
            if (count < N) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return (int) (left % MOD);
    }
    
    /**
     * 计算两个数的最大公约数（欧几里得算法）
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return a 和 b 的最大公约数
     */
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    /**
     * 计算两个数的最小公倍数
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return a 和 b 的最小公倍数
     */
    private static long lcm(int a, int b) {
        return (long) a * b / gcd(a, b);
    }
    
    /**
     * 计算小于等于 x 的神奇数字个数
     * 
     * @param x 上限
     * @param A 第一个除数
     * @param B 第二个除数
     * @param lcm A 和 B 的最小公倍数
     * @return 小于等于 x 的神奇数字个数
     */
    private static long countMagicalNumbers(long x, int A, int B, long lcm) {
        return x / A + x / B - x / lcm;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1：基本测试
        System.out.println("测试用例1: N=1, A=2, B=3 -> " + nthMagicalNumber(1, 2, 3)); // 2
        
        // 测试用例2：N=4的情况
        System.out.println("测试用例2: N=4, A=2, B=3 -> " + nthMagicalNumber(4, 2, 3)); // 6
        
        // 测试用例3：A和B相等的情况
        System.out.println("测试用例3: N=3, A=2, B=2 -> " + nthMagicalNumber(3, 2, 2)); // 6
        
        // 测试用例4：较大的N
        System.out.println("测试用例4: N=5, A=2, B=4 -> " + nthMagicalNumber(5, 2, 4)); // 10
        
        // 测试用例5：边界情况
        System.out.println("测试用例5: N=1000000000, A=40000, B=40000 -> " + 
                         nthMagicalNumber(1000000000, 40000, 40000)); // 需要计算
        
        // 验证容斥原理
        System.out.println("验证容斥原理:");
        int A = 2, B = 3;
        long lcm = lcm(A, B);
        System.out.println("A=" + A + ", B=" + B + ", lcm=" + lcm);
        System.out.println("x=10时，神奇数字个数: " + countMagicalNumbers(10, A, B, lcm));
        System.out.println("实际神奇数字: 2,3,4,6,8,9,10 -> 共7个");
    }
}