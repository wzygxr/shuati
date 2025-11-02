package class084;

import java.util.*;

/**
 * 至少有 1 位重复的数字
 * 题目来源：LeetCode 1012. 至少有 1 位重复的数字
 * 题目链接：https://leetcode.cn/problems/numbers-with-repeated-digits/
 * 
 * 题目描述：
 * 给定正整数 n，返回在 [1, n] 范围内具有至少 1 位重复数字的正整数的个数。
 * 
 * 解题思路：
 * 1. 数位DP方法：使用数位DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 是否受上界限制
 *    - 是否已开始填数字
 *    - 已使用数字的位掩码
 * 3. 关键点：统计至少有一位重复的数字，可以转化为统计所有数字减去没有重复数字的数字
 * 
 * 时间复杂度分析：
 * - 状态数：log n × 2 × 2 × 2^10 ≈ 10 × 4 × 1024 = 40,960
 * - 每个状态处理最多10种选择
 * - 总复杂度：O(400,000) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 记忆化数组：10 × 2 × 2 × 1024 ≈ 40,960个状态
 * 
 * 最优解分析：
 * 这是标准的最优解，利用补集思想将问题转化为求没有重复数字的数字个数
 */

public class Code17_NumberWithRepeatedDigits {
    
    /**
     * 计算[1, n]中至少有一位重复数字的数字个数
     * 时间复杂度: O(log n × 2^10)
     * 空间复杂度: O(log n × 2^10)
     */
    public static int numDupDigitsAtMostN(int n) {
        if (n <= 10) return 0; // 1-10中没有重复数字的数字
        
        // 总数字个数减去没有重复数字的数字个数
        int totalNumbers = n;
        int numbersWithUniqueDigits = countNumbersWithUniqueDigits(n);
        
        return totalNumbers - numbersWithUniqueDigits;
    }
    
    /**
     * 计算[0, n]中没有重复数字的数字个数
     */
    private static int countNumbersWithUniqueDigits(int n) {
        if (n == 0) return 1;
        
        // 将数字转换为字符数组
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;
        
        // 记忆化数组：dp[pos][isLimit][isNum][mask]
        Integer[][][][] dp = new Integer[len][2][2][1 << 10];
        
        return dfs(digits, 0, true, false, 0, dp);
    }
    
    /**
     * 数位DP递归函数
     * 
     * @param digits 数字的字符数组表示
     * @param pos 当前处理位置
     * @param isLimit 是否受到上界限制
     * @param isNum 是否已开始填数字
     * @param mask 已使用数字的位掩码
     * @param dp 记忆化数组
     * @return 满足条件的数字个数
     */
    private static int dfs(char[] digits, int pos, boolean isLimit, boolean isNum, 
                          int mask, Integer[][][][] dp) {
        // 递归终止条件：处理完所有数位
        if (pos == digits.length) {
            return isNum ? 1 : 0; // 已填数字才计数
        }
        
        // 记忆化搜索
        if (!isLimit && isNum && dp[pos][0][0][mask] != null) {
            return dp[pos][0][0][mask];
        }
        
        int ans = 0;
        
        // 处理前导零：可以选择跳过当前位
        if (!isNum) {
            ans += dfs(digits, pos + 1, false, false, mask, dp);
        }
        
        // 确定当前位可选数字范围
        int up = isLimit ? (digits[pos] - '0') : 9;
        int start = isNum ? 0 : 1; // 处理前导零
        
        // 枚举当前位可选数字
        for (int d = start; d <= up; d++) {
            // 检查数字d是否已被使用
            if ((mask & (1 << d)) != 0) {
                continue; // 数字重复，跳过
            }
            
            // 递归处理下一位
            ans += dfs(digits, pos + 1, isLimit && (d == up), true, 
                      mask | (1 << d), dp);
        }
        
        // 记忆化存储
        if (!isLimit && isNum) {
            dp[pos][0][0][mask] = ans;
        }
        
        return ans;
    }
    
    /**
     * 数学方法（排列组合）- 更高效的解法
     * 时间复杂度: O(10^2)
     * 空间复杂度: O(1)
     * 
     * 解题思路：
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
    
    /**
     * 单元测试函数
     */
    public static void testNumDupDigitsAtMostN() {
        System.out.println("=== 测试至少有 1 位重复的数字 ===");
        
        // 测试用例1: 小数字
        int n1 = 20;
        int result1 = numDupDigitsAtMostN(n1);
        int result1Math = numDupDigitsAtMostNMath(n1);
        System.out.println("n = " + n1);
        System.out.println("DP结果: " + result1);
        System.out.println("数学结果: " + result1Math);
        System.out.println("结果一致: " + (result1 == result1Math));
        System.out.println("预期: 11, 22等数字有重复");
        System.out.println();
        
        // 测试用例2: 中等数字
        int n2 = 100;
        int result2 = numDupDigitsAtMostN(n2);
        int result2Math = numDupDigitsAtMostNMath(n2);
        System.out.println("n = " + n2);
        System.out.println("DP结果: " + result2);
        System.out.println("数学结果: " + result2Math);
        System.out.println("结果一致: " + (result2 == result2Math));
        System.out.println();
        
        // 测试用例3: 边界情况
        int n3 = 10;
        int result3 = numDupDigitsAtMostN(n3);
        int result3Math = numDupDigitsAtMostNMath(n3);
        System.out.println("n = " + n3);
        System.out.println("DP结果: " + result3);
        System.out.println("数学结果: " + result3Math);
        System.out.println("结果一致: " + (result3 == result3Math));
        System.out.println("预期: 1-10中没有重复数字");
        System.out.println();
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        int[] testCases = {100, 1000, 10000, 100000, 1000000, 10000000};
        
        for (int n : testCases) {
            long startTimeDP = System.nanoTime();
            int resultDP = numDupDigitsAtMostN(n);
            long endTimeDP = System.nanoTime();
            
            long startTimeMath = System.nanoTime();
            int resultMath = numDupDigitsAtMostNMath(n);
            long endTimeMath = System.nanoTime();
            
            long timeDP = endTimeDP - startTimeDP;
            long timeMath = endTimeMath - startTimeMath;
            
            System.out.println("n = " + n);
            System.out.println("DP方法耗时: " + timeDP + "ns");
            System.out.println("数学方法耗时: " + timeMath + "ns");
            if (timeMath > 0) {
                System.out.println("加速比: " + (double)timeDP / timeMath + "倍");
            }
            System.out.println("结果一致: " + (resultDP == resultMath));
            System.out.println();
        }
    }
    
    /**
     * 调试函数：验证特定范围内的结果
     */
    public static void debugNumDupDigitsAtMostN() {
        System.out.println("=== 调试至少有 1 位重复的数字 ===");
        
        for (int n = 1; n <= 30; n++) {
            int manualCount = 0;
            StringBuilder duplicateNumbers = new StringBuilder();
            
            for (int i = 1; i <= n; i++) {
                if (hasDuplicateDigits(i)) {
                    manualCount++;
                    if (duplicateNumbers.length() < 50) {
                        duplicateNumbers.append(i).append(" ");
                    }
                }
            }
            
            int dpResult = numDupDigitsAtMostN(n);
            int mathResult = numDupDigitsAtMostNMath(n);
            
            System.out.println("n = " + n + ", 重复数字个数: " + manualCount);
            System.out.println("DP结果: " + dpResult + ", 数学结果: " + mathResult);
            System.out.println("结果一致: " + (manualCount == dpResult && dpResult == mathResult));
            
            if (n <= 20) {
                System.out.println("重复数字: " + duplicateNumbers);
            }
            System.out.println();
        }
    }
    
    /**
     * 检查数字是否有重复数字
     */
    private static boolean hasDuplicateDigits(int num) {
        if (num < 10) return false;
        
        boolean[] digits = new boolean[10];
        while (num > 0) {
            int digit = num % 10;
            if (digits[digit]) {
                return true;
            }
            digits[digit] = true;
            num /= 10;
        }
        return false;
    }
    
    /**
     * 工程化考量总结：
     * 1. 补集思想：将问题转化为求补集，简化问题
     * 2. 两种解法：提供DP和数学两种解法，便于理解和选择
     * 3. 性能优化：数学方法更高效，DP方法更通用
     * 4. 边界处理：正确处理n=0和n=1的情况
     * 5. 状态设计：合理设计状态参数，减少状态数
     * 
     * 算法特色：
     * 1. 位掩码技术：高效记录数字使用情况
     * 2. 排列组合：利用数学性质优化计算
     * 3. 记忆化搜索：避免重复计算
     * 4. 补集转换：将复杂问题转化为简单问题
     */
    
    public static void main(String[] args) {
        // 运行功能测试
        testNumDupDigitsAtMostN();
        
        // 运行性能测试
        performanceTest();
        
        // 调试模式
        debugNumDupDigitsAtMostN();
        
        // 边界测试
        System.out.println("=== 边界测试 ===");
        System.out.println("n=0: " + numDupDigitsAtMostN(0));
        System.out.println("n=1: " + numDupDigitsAtMostN(1));
        System.out.println("n=11: " + numDupDigitsAtMostN(11)); // 第一个有重复数字的数
    }
}