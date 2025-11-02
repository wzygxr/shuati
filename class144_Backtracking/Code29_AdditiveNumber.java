package class038;

/**
 * LeetCode 306. 累加数
 * 
 * 题目描述：
 * 累加数是一个字符串，组成它的数字可以形成累加序列。
 * 一个有效的累加序列必须至少包含 3 个数。除了最开始的两个数以外，字符串中的其他数都等于它之前两个数相加的和。
 * 给定一个只包含数字 '0'-'9' 的字符串，编写一个算法来判断给定输入是否是累加数。
 * 
 * 示例：
 * 输入: "112358"
 * 输出: true
 * 解释: 累加序列为: 1, 1, 2, 3, 5, 8。1 + 1 = 2, 1 + 2 = 3, 2 + 3 = 5, 3 + 5 = 8
 * 
 * 输入: "199100199"
 * 输出: true
 * 解释: 累加序列为: 1, 99, 100, 199。1 + 99 = 100, 99 + 100 = 199
 * 
 * 提示：
 * 1 <= num.length <= 35
 * num 仅由数字（0 - 9）组成
 * 
 * 链接：https://leetcode.cn/problems/additive-number/
 * 
 * 算法思路：
 * 1. 使用回溯算法尝试所有可能的前两个数字分割方案
 * 2. 对于每个分割方案，验证后续数字是否满足累加关系
 * 3. 注意处理大数问题和前导零问题
 * 4. 使用剪枝优化：数字不能以0开头（除非数字本身就是0）
 * 
 * 时间复杂度：O(n^3)，需要枚举前两个数字的分割点
 * 空间复杂度：O(n)，递归栈深度
 */
public class Code29_AdditiveNumber {

    /**
     * 判断字符串是否是累加数
     * 
     * @param num 输入字符串
     * @return 是否是累加数
     */
    public static boolean isAdditiveNumber(String num) {
        int n = num.length();
        
        // 尝试所有可能的前两个数字分割方案
        for (int i = 1; i <= n / 2; i++) {
            for (int j = 1; Math.max(i, j) <= n - i - j; j++) {
                String num1 = num.substring(0, i);
                String num2 = num.substring(i, i + j);
                
                // 检查前两个数字是否合法
                if (isValid(num1) && isValid(num2)) {
                    if (backtrack(num, i + j, num1, num2)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    /**
     * 回溯函数验证后续数字
     * 
     * @param num 原始字符串
     * @param start 当前起始位置
     * @param prev1 前一个数字（字符串形式）
     * @param prev2 当前数字（字符串形式）
     * @return 是否满足累加关系
     */
    private static boolean backtrack(String num, int start, String prev1, String prev2) {
        // 终止条件：已处理完所有字符
        if (start == num.length()) {
            return true;
        }
        
        // 计算期望的下一个数字
        String expected = addStrings(prev1, prev2);
        
        // 检查剩余字符串是否以期望数字开头
        if (start + expected.length() <= num.length() && 
            num.substring(start, start + expected.length()).equals(expected)) {
            // 递归验证后续数字
            return backtrack(num, start + expected.length(), prev2, expected);
        }
        
        return false;
    }

    /**
     * 检查数字字符串是否合法
     * 数字不能以0开头，除非数字本身就是0
     * 
     * @param numStr 数字字符串
     * @return 是否合法
     */
    private static boolean isValid(String numStr) {
        // 如果长度大于1且以0开头，不合法
        if (numStr.length() > 1 && numStr.charAt(0) == '0') {
            return false;
        }
        return true;
    }

    /**
     * 大数加法：字符串形式的大数相加
     * 
     * @param num1 第一个数字字符串
     * @param num2 第二个数字字符串
     * @return 相加结果的字符串形式
     */
    private static String addStrings(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int i = num1.length() - 1, j = num2.length() - 1;
        int carry = 0;
        
        while (i >= 0 || j >= 0 || carry > 0) {
            int digit1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int digit2 = j >= 0 ? num2.charAt(j) - '0' : 0;
            
            int sum = digit1 + digit2 + carry;
            result.append(sum % 10);
            carry = sum / 10;
            
            i--;
            j--;
        }
        
        return result.reverse().toString();
    }

    /**
     * 解法二：使用DFS + 剪枝优化
     * 更直观的实现方式
     * 
     * @param num 输入字符串
     * @return 是否是累加数
     */
    public static boolean isAdditiveNumberDFS(String num) {
        return dfs(num, 0, null, null, 0);
    }

    private static boolean dfs(String num, int start, Long prev1, Long prev2, int count) {
        // 终止条件：已处理完所有字符且至少有三个数字
        if (start == num.length()) {
            return count >= 3;
        }
        
        // 尝试所有可能的分割长度
        for (int len = 1; start + len <= num.length(); len++) {
            String currentStr = num.substring(start, start + len);
            
            // 检查当前数字是否合法（不能有前导0）
            if (currentStr.length() > 1 && currentStr.charAt(0) == '0') {
                continue;
            }
            
            long current;
            try {
                current = Long.parseLong(currentStr);
            } catch (NumberFormatException e) {
                // 处理大数情况，使用字符串比较
                return false;
            }
            
            // 如果是前两个数字，直接递归
            if (count < 2) {
                if (count == 0) {
                    if (dfs(num, start + len, current, null, count + 1)) {
                        return true;
                    }
                } else {
                    if (dfs(num, start + len, prev1, current, count + 1)) {
                        return true;
                    }
                }
            } else {
                // 检查是否满足累加关系
                long expected = prev1 + prev2;
                if (current == expected) {
                    if (dfs(num, start + len, prev2, current, count + 1)) {
                        return true;
                    }
                } else if (current > expected) {
                    // 剪枝：如果当前数字已经大于期望值，提前终止
                    break;
                }
            }
        }
        
        return false;
    }

    /**
     * 解法三：使用迭代法验证
     * 适用于需要高效验证的情况
     * 
     * @param num 输入字符串
     * @return 是否是累加数
     */
    public static boolean isAdditiveNumberIterative(String num) {
        int n = num.length();
        
        // 尝试所有可能的前两个数字分割方案
        for (int i = 1; i <= n / 2; i++) {
            String num1 = num.substring(0, i);
            if (!isValid(num1)) continue;
            
            for (int j = 1; Math.max(i, j) <= n - i - j; j++) {
                String num2 = num.substring(i, i + j);
                if (!isValid(num2)) continue;
                
                if (isValidSequence(num, i + j, num1, num2)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    private static boolean isValidSequence(String num, int start, String num1, String num2) {
        int n = num.length();
        String prev1 = num1, prev2 = num2;
        int currentStart = start;
        
        while (currentStart < n) {
            String expected = addStrings(prev1, prev2);
            int expectedLen = expected.length();
            
            if (currentStart + expectedLen > n) {
                return false;
            }
            
            String actual = num.substring(currentStart, currentStart + expectedLen);
            if (!actual.equals(expected)) {
                return false;
            }
            
            prev1 = prev2;
            prev2 = expected;
            currentStart += expectedLen;
        }
        
        return true;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String num1 = "112358";
        boolean result1 = isAdditiveNumber(num1);
        System.out.println("输入: num = \"" + num1 + "\"");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        String num2 = "199100199";
        boolean result2 = isAdditiveNumber(num2);
        System.out.println("\n输入: num = \"" + num2 + "\"");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        String num3 = "1023";
        boolean result3 = isAdditiveNumber(num3);
        System.out.println("\n输入: num = \"" + num3 + "\"");
        System.out.println("输出: " + result3);
        
        // 测试DFS解法
        System.out.println("\n=== DFS解法测试 ===");
        boolean result4 = isAdditiveNumberDFS(num1);
        System.out.println("输入: num = \"" + num1 + "\"");
        System.out.println("输出: " + result4);
    }
}