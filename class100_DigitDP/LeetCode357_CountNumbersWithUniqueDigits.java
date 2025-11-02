package class085;

/**
 * LeetCode 357. 统计各位数字都不同的数字个数
 * 题目链接：https://leetcode.cn/problems/count-numbers-with-unique-digits/
 * 
 * 题目描述：
 * 给你一个整数 n ，统计并返回各位数字都不同的数字 x 的个数，其中 0 <= x < 10^n。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。我们需要统计各位数字都不相同的数字个数。
 * 状态定义：
 * dp[pos][mask][limit][lead] 表示处理到第pos位，已使用的数字状态为mask，
 * limit表示是否受到上界限制，lead表示是否有前导零
 * 
 * 算法分析：
 * 时间复杂度：O(n * 2^10 * 2 * 2) = O(n)
 * 空间复杂度：O(n * 2^10)
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。也可以使用数学方法通过排列组合直接计算，
 * 但数位DP方法更加通用，易于扩展到其他数字约束问题。
 * 
 * 工程化考量：
 * 1. 位运算优化：使用位掩码表示已使用的数字状态
 * 2. 边界处理：正确处理n=0, n=1等边界情况
 * 3. 性能优化：使用记忆化搜索避免重复计算
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - LeetCode 357: https://leetcode.cn/problems/count-numbers-with-unique-digits/
 * - AcWing 1082: https://www.acwing.com/problem/content/1084/
 * 
 * 多语言实现：
 * - Java: LeetCode357_CountNumbersWithUniqueDigits.java
 * - Python: LeetCode357_CountNumbersWithUniqueDigits.py
 * - C++: LeetCode357_CountNumbersWithUniqueDigits.cpp
 */

public class LeetCode357_CountNumbersWithUniqueDigits {
    
    // 数位DP记忆化数组
    private static int[][][][] dp;
    // 存储上界的每一位
    private static int[] digits;
    // 上界的长度
    private static int len;
    
    /**
     * 主函数：统计各位数字都不同的数字个数
     * 
     * @param n 指数
     * @return 各位数字都不同的数字个数
     * 
     * 时间复杂度：O(n * 2^10)
     * 空间复杂度：O(n * 2^10)
     */
    public static int countNumbersWithUniqueDigits(int n) {
        // 特殊情况处理
        if (n == 0) {
            return 1;
        }
        
        // 计算上界 10^n - 1
        int upper = 1;
        for (int i = 0; i < n; i++) {
            upper *= 10;
        }
        upper--; // 10^n - 1
        
        // 将上界转换为数字数组
        String upperStr = String.valueOf(upper);
        len = upperStr.length();
        digits = new int[len];
        for (int i = 0; i < len; i++) {
            digits[i] = upperStr.charAt(i) - '0';
        }
        
        // 初始化DP数组
        dp = new int[len][1024][2][2]; // 2^10 = 1024
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 1024; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k][0] = -1;
                    dp[i][j][k][1] = -1;
                }
            }
        }
        
        // 从最高位开始进行数位DP
        return dfs(0, 0, true, true);
    }
    
    /**
     * 数位DP核心函数
     * 
     * @param pos 当前处理到第几位
     * @param mask 已使用的数字状态（用位运算表示）
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @return 满足条件的数字个数
     */
    private static int dfs(int pos, int mask, boolean limit, boolean lead) {
        // 递归终止条件：处理完所有数位
        if (pos == len) {
            // 只有在没有前导零的情况下才算一个有效数字
            return lead ? 0 : 1;
        }
        
        // 记忆化搜索优化：如果该状态已经计算过，直接返回结果
        if (!limit && !lead && dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0] != -1) {
            return dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0];
        }
        
        int result = 0;
        
        // 如果有前导零，可以继续选择前导零
        if (lead) {
            result += dfs(pos + 1, mask, false, true);
        }
        
        // 确定当前位可以填入的数字范围
        int maxDigit = limit ? digits[pos] : 9;
        
        // 枚举当前位可以填入的数字
        for (int digit = 0; digit <= maxDigit; digit++) {
            // 跳过前导零
            if (lead && digit == 0) {
                continue;
            }
            
            // 如果该数字已经使用过，跳过
            if (((mask >> digit) & 1) == 1) {
                continue;
            }
            
            // 递归处理下一位，更新mask
            result += dfs(pos + 1, mask | (1 << digit), limit && (digit == maxDigit), false);
        }
        
        // 记忆化存储结果
        if (!limit && !lead) {
            dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0] = result;
        }
        
        return result;
    }
    
    /**
     * 数学方法实现 - 替代解法，时间复杂度O(n)，空间复杂度O(1)
     * 使用排列组合直接计算结果
     */
    public static int countNumbersWithUniqueDigitsMath(int n) {
        if (n == 0) return 1;
        if (n == 1) return 10;
        
        // 第一位有9种选择(1-9)，第二位有9种选择(0-9除去第一位)，
        // 第三位有8种选择，以此类推
        int result = 10; // 一位数的情况
        int uniqueDigits = 9; // 两位数开始的首位选择
        
        for (int i = 2; i <= n && i <= 10; i++) {
            uniqueDigits *= (11 - i); // 第i位的选择数
            result += uniqueDigits;
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 2;
        int result1 = countNumbersWithUniqueDigits(n1);
        int result1Math = countNumbersWithUniqueDigitsMath(n1);
        System.out.println("测试用例1 - n = " + n1);
        System.out.println("数位DP结果: " + result1);
        System.out.println("数学方法结果: " + result1Math);
        System.out.println("期望输出: 91");
        System.out.println("测试结果: " + (result1 == 91 && result1Math == 91 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2
        int n2 = 0;
        int result2 = countNumbersWithUniqueDigits(n2);
        int result2Math = countNumbersWithUniqueDigitsMath(n2);
        System.out.println("测试用例2 - n = " + n2);
        System.out.println("数位DP结果: " + result2);
        System.out.println("数学方法结果: " + result2Math);
        System.out.println("期望输出: 1");
        System.out.println("测试结果: " + (result2 == 1 && result2Math == 1 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3
        int n3 = 1;
        int result3 = countNumbersWithUniqueDigits(n3);
        int result3Math = countNumbersWithUniqueDigitsMath(n3);
        System.out.println("测试用例3 - n = " + n3);
        System.out.println("数位DP结果: " + result3);
        System.out.println("数学方法结果: " + result3Math);
        System.out.println("期望输出: 10");
        System.out.println("测试结果: " + (result3 == 10 && result3Math == 10 ? "通过" : "失败"));
        System.out.println();
        
        // 性能测试
        int n4 = 8;
        long startTime = System.currentTimeMillis();
        int result4 = countNumbersWithUniqueDigits(n4);
        long dpTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result4Math = countNumbersWithUniqueDigitsMath(n4);
        long mathTime = System.currentTimeMillis() - startTime;
        
        System.out.println("性能测试 - n = " + n4);
        System.out.println("数位DP结果: " + result4 + ", 耗时: " + dpTime + "ms");
        System.out.println("数学方法结果: " + result4Math + ", 耗时: " + mathTime + "ms");
        System.out.println("数学方法比数位DP快 " + (double)dpTime / mathTime + " 倍");
    }
}