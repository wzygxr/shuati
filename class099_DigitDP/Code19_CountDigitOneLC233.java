package class084;

/**
 * LeetCode 233. 数字 1 的个数
 * 题目链接: https://leetcode.cn/problems/number-of-digit-one/
 * 
 * 题目描述:
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
 * 
 * 解题思路:
 * 1. 数位DP方法：使用数位DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 当前已出现的1的个数
 * 3. 关键点：统计1出现的次数而不是数字个数
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(log n)
 */
public class Code19_CountDigitOneLC233 {

    /**
     * 数位DP解法
     * 时间复杂度: O(log n) 每个数位最多计算两次(受限/不受限)
     * 空间复杂度: O(log n) 递归栈深度
     */
    public static int countDigitOne(int n) {
        if (n <= 0) {
            return 0;
        }
        // 将数字n转换为字符数组，方便按位处理
        char[] s = String.valueOf(n).toCharArray();
        int len = s.length;
        // dp[i][count][isLimit] 表示处理到第i位，已经出现了count个1，当前是否受限制时的方案数
        // -1表示未计算过
        int[][][] dp = new int[len][len][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                dp[i][j][0] = -1;
                dp[i][j][1] = -1;
            }
        }
        return f(s, 0, 0, true, dp);
    }

    // s: 数字的字符数组表示
    // i: 当前处理到第几位
    // count: 当前已经统计到的1的个数
    // isLimit: 当前位是否受到上限限制
    // dp: 记忆化数组
    private static int f(char[] s, int i, int count, boolean isLimit, int[][][] dp) {
        // 递归终止条件：已经处理完所有数位
        if (i == s.length) {
            return count;
        }

        // 记忆化：如果已经计算过该状态，直接返回结果
        if (!isLimit && dp[i][count][0] != -1) {
            return dp[i][count][0];
        }

        // 确定当前位可以填入的数字范围
        // 如果受限制，最大只能填入s[i]对应的数字，否则可以填入0-9
        int up = isLimit ? s[i] - '0' : 9;
        int ans = 0;

        // 枚举当前位可以填入的数字
        for (int d = 0; d <= up; d++) {
            // 递归处理下一位
            // 如果当前位填入1，则count+1
            // 下一位是否受限制：当前位受限制且填入了上限值
            ans += f(s, i + 1, count + (d == 1 ? 1 : 0), isLimit && d == up, dp);
        }

        // 记忆化存储结果
        if (!isLimit) {
            dp[i][count][0] = ans;
        }
        return ans;
    }

    /**
     * 数学方法解法（更高效）
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * 
     * 解题思路:
     * 1. 逐位分析每一位上1出现的次数
     * 2. 对于每一位，考虑高位、当前位和低位的影响
     * 3. 根据当前位与1的大小关系，分情况计算
     */
    public static int countDigitOneMath(int n) {
        if (n <= 0) return 0;
        
        int count = 0;
        long divisor = 1; // 当前位的权重
        
        while (n / divisor > 0) {
            long high = n / (divisor * 10); // 高位部分
            long current = (n / divisor) % 10; // 当前位
            long low = n % divisor; // 低位部分
            
            // 分情况讨论当前位上1的个数
            if (current > 1) {
                count += (high + 1) * divisor;
            } else if (current == 1) {
                count += high * divisor + low + 1;
            } else {
                count += high * divisor;
            }
            
            divisor *= 10;
        }
        
        return count;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 13;
        System.out.println("n = " + n1 + ", 数字1出现的次数: " + countDigitOne(n1));
        System.out.println("数学方法结果: " + countDigitOneMath(n1));
        // 预期输出: 6 (数字1, 10, 11, 12, 13中1出现了6次)

        // 测试用例2
        int n2 = 0;
        System.out.println("n = " + n2 + ", 数字1出现的次数: " + countDigitOne(n2));
        System.out.println("数学方法结果: " + countDigitOneMath(n2));
        // 预期输出: 0

        // 测试用例3
        int n3 = 100;
        System.out.println("n = " + n3 + ", 数字1出现的次数: " + countDigitOne(n3));
        System.out.println("数学方法结果: " + countDigitOneMath(n3));
        // 预期输出: 21

        // 测试用例4
        int n4 = 1000;
        System.out.println("n = " + n4 + ", 数字1出现的次数: " + countDigitOne(n4));
        System.out.println("数学方法结果: " + countDigitOneMath(n4));
        // 预期输出: 301
    }
}