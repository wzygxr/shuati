package class050;

/**
 * LeetCode 125. 验证回文串 (Valid Palindrome)
 * 
 * 题目描述:
 * 如果在将所有大写字符转换为小写字符、并移除所有非字母数字字符之后，短语正着读和反着读都一样，则可以认为该短语是一个 回文串 。
 * 字母和数字都属于字母数字字符。
 * 给你一个字符串 s，如果它是 回文串 ，返回 true ；否则，返回 false 。
 * 
 * 示例1:
 * 输入: s = "A man, a plan, a canal: Panama"
 * 输出: true
 * 解释: "amanaplanacanalpanama" 是回文串。
 * 
 * 示例2:
 * 输入: s = "race a car"
 * 输出: false
 * 解释: "raceacar" 不是回文串。
 * 
 * 示例3:
 * 输入: s = " "
 * 输出: true
 * 解释: 在移除非字母数字字符后，s 变为 "" 。由于空字符串正着反着读都一样，所以是回文串。
 * 
 * 提示:
 * 1 <= s.length <= 2 * 10^5
 * s 仅由可打印的 ASCII 字符组成
 * 
 * 题目链接: https://leetcode.cn/problems/valid-palindrome/
 * 
 * 解题思路:
 * 这道题可以使用双指针的方法来解决：
 * 
 * 方法一（双指针 + 字符处理）：
 * 1. 使用两个指针 left 和 right 分别指向字符串的首尾
 * 2. 跳过非字母数字字符，只比较字母数字字符
 * 3. 比较左右指针指向的字符（忽略大小写）
 * 4. 如果所有字符都匹配，则返回true，否则返回false
 * 
 * 时间复杂度: O(n)，n为字符串长度
 * 空间复杂度: O(1)
 * 是否最优解：是
 */

public class Code31_ValidPalindrome {
    
    /**
     * 解法一: 双指针（最优解）
     * 
     * @param s 输入字符串
     * @return 是否为回文串
     */
    public static boolean isPalindrome(String s) {
        if (s == null) {
            return false;
        }
        
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            // 跳过非字母数字字符（左指针）
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            
            // 跳过非字母数字字符（右指针）
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            // 比较字符（忽略大小写）
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * 解法二: 使用StringBuilder反转比较
     * 
     * @param s 输入字符串
     * @return 是否为回文串
     */
    public static boolean isPalindromeStringBuilder(String s) {
        if (s == null) {
            return false;
        }
        
        // 过滤非字母数字字符并转换为小写
        StringBuilder filtered = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                filtered.append(Character.toLowerCase(c));
            }
        }
        
        // 比较原字符串和反转后的字符串
        String original = filtered.toString();
        String reversed = filtered.reverse().toString();
        
        return original.equals(reversed);
    }
    
    /**
     * 解法三: 优化的双指针实现（避免重复计算）
     * 
     * @param s 输入字符串
     * @return 是否为回文串
     */
    public static boolean isPalindromeOptimized(String s) {
        if (s == null) {
            return false;
        }
        
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            char leftChar = s.charAt(left);
            char rightChar = s.charAt(right);
            
            // 如果左字符不是字母数字，跳过
            if (!isAlphanumeric(leftChar)) {
                left++;
                continue;
            }
            
            // 如果右字符不是字母数字，跳过
            if (!isAlphanumeric(rightChar)) {
                right--;
                continue;
            }
            
            // 比较字符（忽略大小写）
            if (toLowerCase(leftChar) != toLowerCase(rightChar)) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * 判断字符是否为字母或数字
     * 
     * @param c 字符
     * @return 是否为字母数字
     */
    private static boolean isAlphanumeric(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               (c >= '0' && c <= '9');
    }
    
    /**
     * 将字符转换为小写（自定义实现，避免调用Character.toLowerCase）
     * 
     * @param c 字符
     * @return 小写字符
     */
    private static char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char)(c - 'A' + 'a');
        }
        return c;
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        String s1 = "A man, a plan, a canal: Panama";
        boolean expected1 = true;
        System.out.println("测试用例1:");
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("解法一结果: " + isPalindrome(s1));
        System.out.println("解法二结果: " + isPalindromeStringBuilder(s1));
        System.out.println("解法三结果: " + isPalindromeOptimized(s1));
        System.out.println("期望: " + expected1);
        System.out.println();
        
        // 测试用例2
        String s2 = "race a car";
        boolean expected2 = false;
        System.out.println("测试用例2:");
        System.out.println("输入: \"" + s2 + "\"");
        System.out.println("解法一结果: " + isPalindrome(s2));
        System.out.println("解法二结果: " + isPalindromeStringBuilder(s2));
        System.out.println("解法三结果: " + isPalindromeOptimized(s2));
        System.out.println("期望: " + expected2);
        System.out.println();
        
        // 测试用例3
        String s3 = " ";
        boolean expected3 = true;
        System.out.println("测试用例3:");
        System.out.println("输入: \"" + s3 + "\"");
        System.out.println("解法一结果: " + isPalindrome(s3));
        System.out.println("解法二结果: " + isPalindromeStringBuilder(s3));
        System.out.println("解法三结果: " + isPalindromeOptimized(s3));
        System.out.println("期望: " + expected3);
        System.out.println();
        
        // 测试用例4 - 边界情况：空字符串
        String s4 = "";
        boolean expected4 = true;
        System.out.println("测试用例4（空字符串）:");
        System.out.println("输入: \"" + s4 + "\"");
        System.out.println("解法一结果: " + isPalindrome(s4));
        System.out.println("解法二结果: " + isPalindromeStringBuilder(s4));
        System.out.println("解法三结果: " + isPalindromeOptimized(s4));
        System.out.println("期望: " + expected4);
        System.out.println();
        
        // 测试用例5 - 边界情况：纯数字
        String s5 = "12321";
        boolean expected5 = true;
        System.out.println("测试用例5（纯数字）:");
        System.out.println("输入: \"" + s5 + "\"");
        System.out.println("解法一结果: " + isPalindrome(s5));
        System.out.println("解法二结果: " + isPalindromeStringBuilder(s5));
        System.out.println("解法三结果: " + isPalindromeOptimized(s5));
        System.out.println("期望: " + expected5);
        System.out.println();
        
        // 测试用例6 - 边界情况：混合字符
        String s6 = "0P";
        boolean expected6 = false;
        System.out.println("测试用例6（混合字符）:");
        System.out.println("输入: \"" + s6 + "\"");
        System.out.println("解法一结果: " + isPalindrome(s6));
        System.out.println("解法二结果: " + isPalindromeStringBuilder(s6));
        System.out.println("解法三结果: " + isPalindromeOptimized(s6));
        System.out.println("期望: " + expected6);
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建长字符串进行性能测试
        StringBuilder sb = new StringBuilder();
        // 添加大量非字母数字字符和字母数字字符混合
        for (int i = 0; i < 100000; i++) {
            if (i % 3 == 0) {
                sb.append("!@#");
            } else if (i % 3 == 1) {
                sb.append("abc");
            } else {
                sb.append("123");
            }
        }
        String longString = sb.toString();
        
        // 测试解法一的性能
        long startTime = System.nanoTime();
        boolean result1 = isPalindrome(longString);
        long endTime = System.nanoTime();
        long duration1 = (endTime - startTime) / 1000000; // 转换为毫秒
        System.out.println("解法一（双指针）耗时: " + duration1 + "ms, 结果: " + result1);
        
        // 测试解法二的性能
        startTime = System.nanoTime();
        boolean result2 = isPalindromeStringBuilder(longString);
        endTime = System.nanoTime();
        long duration2 = (endTime - startTime) / 1000000;
        System.out.println("解法二（StringBuilder）耗时: " + duration2 + "ms, 结果: " + result2);
        
        // 测试解法三的性能
        startTime = System.nanoTime();
        boolean result3 = isPalindromeOptimized(longString);
        endTime = System.nanoTime();
        long duration3 = (endTime - startTime) / 1000000;
        System.out.println("解法三（优化双指针）耗时: " + duration3 + "ms, 结果: " + result3);
        
        // 验证结果一致性
        System.out.println("所有解法结果一致: " + (result1 == result2 && result2 == result3));
    }
    
    /**
     * 边界条件测试
     */
    public static void boundaryTest() {
        // 测试null输入
        try {
            boolean result = isPalindrome(null);
            System.out.println("边界测试失败：null输入没有抛出异常");
        } catch (NullPointerException e) {
            System.out.println("边界测试通过：null输入正确抛出异常");
        }
        
        // 测试极端长字符串
        StringBuilder extremelyLong = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            extremelyLong.append('a');
        }
        String extremeString = extremelyLong.toString();
        
        long startTime = System.nanoTime();
        boolean result = isPalindrome(extremeString);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("极端长字符串测试耗时: " + duration + "ms, 结果: " + result);
    }
    
    /**
     * 算法分析
     */
    public static void algorithmAnalysis() {
        System.out.println("=== 算法分析 ===");
        System.out.println("1. 解法一（双指针）");
        System.out.println("   - 时间复杂度: O(n) - 每个字符最多被访问一次");
        System.out.println("   - 空间复杂度: O(1) - 只使用常数级别的额外空间");
        System.out.println("   - 优点: 原地操作，空间效率高");
        System.out.println("   - 缺点: 需要处理字符过滤逻辑");
        System.out.println();
        
        System.out.println("2. 解法二（StringBuilder反转）");
        System.out.println("   - 时间复杂度: O(n) - 需要遍历字符串两次");
        System.out.println("   - 空间复杂度: O(n) - 需要额外的字符串存储空间");
        System.out.println("   - 优点: 实现简单，易于理解");
        System.out.println("   - 缺点: 空间效率较低");
        System.out.println();
        
        System.out.println("3. 解法三（优化双指针）");
        System.out.println("   - 时间复杂度: O(n)");
        System.out.println("   - 空间复杂度: O(1)");
        System.out.println("   - 优点: 避免重复字符检查，效率最高");
        System.out.println("   - 缺点: 实现相对复杂");
        System.out.println();
        
        System.out.println("推荐使用解法一作为通用解决方案");
    }
    
    public static void main(String[] args) {
        System.out.println("=== 验证回文串 算法实现 ===");
        System.out.println();
        
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
        
        System.out.println("=== 边界条件测试 ===");
        boundaryTest();
        
        System.out.println("=== 算法分析 ===");
        algorithmAnalysis();
    }
}