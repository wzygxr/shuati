package class084;

/**
 * LeetCode 1012. 至少有 1 位重复的数字
 * 题目链接: https://leetcode.cn/problems/numbers-with-repeated-digits/
 * 
 * 题目描述:
 * 给定正整数 n，返回在 [1, n] 范围内具有至少 1 位重复数字的正整数的个数。
 * 
 * 解题思路:
 * 1. 补集思想：统计没有重复数字的数字个数，然后用总数减去它
 * 2. 数位DP方法：使用数位DP框架，逐位确定数字
 * 3. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 是否已开始填数字
 *    - 已使用数字的位掩码
 * 4. 关键点：用位掩码记录已使用的数字
 * 
 * 时间复杂度: O(10 * 2^10 * log n)
 * 空间复杂度: O(2^10 * log n)
 */
public class Code21_NumbersWithRepeatedDigitsLC1012 {

    /**
     * 数位DP解法
     * 时间复杂度: O(10 * 2^10 * log n)
     * 空间复杂度: O(2^10 * log n)
     */
    public static int numDupDigitsAtMostN(int n) {
        // 总数减去没有重复数字的数字个数
        return n - countUniqueDigits(n);
    }

    /**
     * 计算[0, n]中没有重复数字的数字个数
     */
    private static int countUniqueDigits(int n) {
        if (n <= 0) return 1; // 0
        
        // 将数字n转换为字符数组
        char[] s = String.valueOf(n).toCharArray();
        int len = s.length;
        
        // 记忆化数组：dp[pos][isLimit][isNum][mask]
        Integer[][][][] dp = new Integer[len][2][2][1 << 10];
        
        return dfs(s, 0, true, false, 0, dp);
    }

    /**
     * 数位DP递归函数
     * 
     * @param s 数字的字符数组表示
     * @param pos 当前处理位置
     * @param isLimit 是否受到上界限制
     * @param isNum 是否已开始填数字
     * @param mask 已使用数字的位掩码
     * @param dp 记忆化数组
     * @return 满足条件的数字个数
     */
    private static int dfs(char[] s, int pos, boolean isLimit, boolean isNum, 
                          int mask, Integer[][][][] dp) {
        // 递归终止条件：处理完所有数位
        if (pos == s.length) {
            return isNum ? 1 : 0; // 已填数字才计数
        }
        
        // 记忆化搜索
        if (!isLimit && isNum && dp[pos][0][0][mask] != null) {
            return dp[pos][0][0][mask];
        }
        
        int ans = 0;
        
        // 处理前导零：可以选择跳过当前位
        if (!isNum) {
            ans += dfs(s, pos + 1, false, false, mask, dp);
        }
        
        // 确定当前位可选数字范围
        int up = isLimit ? (s[pos] - '0') : 9;
        int start = isNum ? 0 : 1; // 处理前导零
        
        // 枚举当前位可选数字
        for (int d = start; d <= up; d++) {
            // 检查数字d是否已被使用
            if ((mask & (1 << d)) != 0) {
                continue; // 数字重复，跳过
            }
            
            // 递归处理下一位
            ans += dfs(s, pos + 1, isLimit && (d == up), true, 
                      mask | (1 << d), dp);
        }
        
        // 记忆化存储
        if (!isLimit && isNum) {
            dp[pos][0][0][mask] = ans;
        }
        
        return ans;
    }

    /**
     * 数学方法解法（排列组合）- 更高效的解法
     * 时间复杂度: O(10^2)
     * 空间复杂度: O(1)
     * 
     * 解题思路:
     * 1. 对于n位数，没有重复数字的数字个数可以用排列组合计算
     * 2. 总个数 = 1位数个数 + 2位数个数 + ... + n位数个数
     * 3. k位数个数 = 9 × 9 × 8 × ... × (11-k)
     */
    public static int numDupDigitsAtMostNMath(int n) {
        if (n <= 10) return 0;
        
        // 计算没有重复数字的数字个数
        int uniqueCount = countUniqueDigitsMath(n);
        return n - uniqueCount;
    }
    
    private static int countUniqueDigitsMath(int n) {
        if (n == 0) return 1;
        
        // 将n转换为字符串获取位数
        String s = String.valueOf(n);
        int len = s.length();
        
        // 计算位数小于len的数字个数
        int count = 0;
        for (int i = 1; i < len; i++) {
            count += countUniqueDigitsOfLength(i);
        }
        
        // 计算位数等于len且小于等于n的数字个数
        count += countUniqueDigitsEqualToLength(s);
        
        return count;
    }
    
    /**
     * 计算恰好k位数且没有重复数字的数字个数
     */
    private static int countUniqueDigitsOfLength(int k) {
        if (k == 0) return 1;
        if (k == 1) return 9;
        
        int count = 9; // 第一位有9种选择（1-9）
        int available = 9; // 剩余可用数字个数
        
        for (int i = 2; i <= k; i++) {
            count *= available;
            available--;
        }
        
        return count;
    }
    
    /**
     * 计算位数等于给定字符串长度且没有重复数字的数字个数
     */
    private static int countUniqueDigitsEqualToLength(String s) {
        int len = s.length();
        boolean[] used = new boolean[10];
        int count = 0;
        
        // 逐位处理
        for (int i = 0; i < len; i++) {
            int currentDigit = s.charAt(i) - '0';
            
            // 确定当前位可以选择的数字范围
            int start = (i == 0) ? 1 : 0; // 第一位不能为0
            for (int d = start; d < currentDigit; d++) {
                if (!used[d]) {
                    // 计算剩余位数的排列数
                    count += countPermutations(10 - countUsed(used) - 1, len - i - 1);
                }
            }
            
            // 如果当前数字已被使用，后面的数字都不满足条件
            if (used[currentDigit]) {
                break;
            }
            
            used[currentDigit] = true;
            
            // 如果是最后一位且所有数字都不重复，计数加1
            if (i == len - 1) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 计算排列数：从m个元素中取n个的排列数
     */
    private static int countPermutations(int m, int n) {
        if (n == 0) return 1;
        if (n > m) return 0;
        
        int result = 1;
        for (int i = 0; i < n; i++) {
            result *= (m - i);
        }
        return result;
    }
    
    /**
     * 统计已使用的数字个数
     */
    private static int countUsed(boolean[] used) {
        int count = 0;
        for (boolean u : used) {
            if (u) count++;
        }
        return count;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 20;
        System.out.println("n = " + n1);
        System.out.println("至少有1位重复数字的正整数个数: " + numDupDigitsAtMostN(n1));
        System.out.println("数学方法结果: " + numDupDigitsAtMostNMath(n1));
        // 预期输出: 1 (只有11满足条件)

        // 测试用例2
        int n2 = 100;
        System.out.println("n = " + n2);
        System.out.println("至少有1位重复数字的正整数个数: " + numDupDigitsAtMostN(n2));
        System.out.println("数学方法结果: " + numDupDigitsAtMostNMath(n2));
        // 预期输出: 10

        // 测试用例3
        int n3 = 10;
        System.out.println("n = " + n3);
        System.out.println("至少有1位重复数字的正整数个数: " + numDupDigitsAtMostN(n3));
        System.out.println("数学方法结果: " + numDupDigitsAtMostNMath(n3));
        // 预期输出: 0 (1-10中没有重复数字)

        // 测试用例4
        int n4 = 1000;
        System.out.println("n = " + n4);
        System.out.println("至少有1位重复数字的正整数个数: " + numDupDigitsAtMostN(n4));
        System.out.println("数学方法结果: " + numDupDigitsAtMostNMath(n4));
        // 预期输出: 262
    }
}