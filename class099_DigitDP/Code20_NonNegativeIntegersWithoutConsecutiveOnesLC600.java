package class084;

/**
 * LeetCode 600. 不含连续1的非负整数
 * 题目链接: https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
 * 
 * 题目描述:
 * 给定一个正整数 n，返回在 [0, n] 范围内不含连续1的非负整数的个数。
 * 
 * 解题思路:
 * 1. 数位DP方法：使用数位DP框架，逐位确定二进制数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 前一位是否为1
 * 3. 关键点：当前位不能与前一位同时为1
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(log n)
 */
public class Code20_NonNegativeIntegersWithoutConsecutiveOnesLC600 {

    /**
     * 数位DP解法
     * 时间复杂度: O(log n) 每个数位最多计算几次(受限/不受限, 前一位是0/1)
     * 空间复杂度: O(log n) 递归栈深度
     */
    public static int findIntegers(int n) {
        // 将数字n转换为二进制字符数组，方便按位处理
        char[] s = Integer.toBinaryString(n).toCharArray();
        int len = s.length;
        // dp[i][prev][isLimit] 
        // 表示处理到第i位，前一位是prev，当前是否受限制时的方案数
        // -1表示未计算过
        int[][][] dp = new int[len][2][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        return f(s, 0, 0, true, dp);
    }

    // s: 数字的二进制字符数组表示
    // i: 当前处理到第几位
    // prev: 前一位填的数字
    // isLimit: 当前位是否受到上限限制
    // dp: 记忆化数组
    private static int f(char[] s, int i, int prev, boolean isLimit, int[][][] dp) {
        // 递归终止条件：已经处理完所有数位
        if (i == s.length) {
            // 成功构造一个有效数字
            return 1;
        }

        // 记忆化：如果已经计算过该状态，直接返回结果
        if (!isLimit && dp[i][prev][0] != -1) {
            return dp[i][prev][0];
        }

        // 确定当前位可以填入的数字范围
        // 如果受限制，最大只能填入s[i]对应的数字，否则可以填入0-1
        int up = isLimit ? s[i] - '0' : 1;
        int ans = 0;

        // 枚举当前位可以填入的数字
        for (int d = 0; d <= up; d++) {
            // 不能有连续的1
            if (prev == 1 && d == 1) {
                continue;
            }
            // 递归处理下一位
            // 下一位是否受限制：当前位受限制且填入了上限值
            ans += f(s, i + 1, d, isLimit && d == up, dp);
        }

        // 记忆化存储结果
        if (!isLimit) {
            dp[i][prev][0] = ans;
        }
        return ans;
    }

    /**
     * 数学方法解法（斐波那契数列）- 更高效的解法
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * 解题思路:
     * 1. 观察发现，不含连续1的二进制数个数满足斐波那契数列
     * 2. 对于k位二进制数，有效数字个数为fib(k+2)
     * 3. 利用这个性质可以快速计算
     */
    public static int findIntegersMath(int n) {
        if (n == 0) return 1;
        if (n == 1) return 2;
        
        // 预处理斐波那契数列
        int[] fib = new int[32];
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 32; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        
        String binary = Integer.toBinaryString(n);
        int len = binary.length();
        int ans = 0;
        boolean prevBit = false; // 前一位是否为1
        
        for (int i = 0; i < len; i++) {
            if (binary.charAt(i) == '1') {
                // 如果当前位为1，可以选择填0，后面位可以任意填
                ans += fib[len - i - 1];
                
                // 如果前一位也是1，说明出现了连续1，后面的数字都不满足条件
                if (prevBit) {
                    return ans;
                }
                prevBit = true;
            } else {
                prevBit = false;
            }
        }
        
        // 加上n本身（如果n本身满足条件）
        return ans + 1;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 5;
        System.out.println("n = " + n1 + ", 不含连续1的非负整数个数: " + findIntegers(n1));
        System.out.println("数学方法结果: " + findIntegersMath(n1));
        // 预期输出: 5 (0, 1, 2, 4, 5 满足条件，3的二进制是11，不满足)

        // 测试用例2
        int n2 = 1;
        System.out.println("n = " + n2 + ", 不含连续1的非负整数个数: " + findIntegers(n2));
        System.out.println("数学方法结果: " + findIntegersMath(n2));
        // 预期输出: 2 (0, 1 满足条件)

        // 测试用例3
        int n3 = 2;
        System.out.println("n = " + n3 + ", 不含连续1的非负整数个数: " + findIntegers(n3));
        System.out.println("数学方法结果: " + findIntegersMath(n3));
        // 预期输出: 3 (0, 1, 2 满足条件)
        
        // 测试用例4
        int n4 = 10;
        System.out.println("n = " + n4 + ", 不含连续1的非负整数个数: " + findIntegers(n4));
        System.out.println("数学方法结果: " + findIntegersMath(n4));
        // 预期输出: 8
    }
}