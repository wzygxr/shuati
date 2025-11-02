package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * 剑指Offer 48. 最长不含重复字符的子字符串
 * 题目链接：https://leetcode-cn.com/problems/zui-chang-bu-han-zhong-fu-zi-fu-de-zi-zi-fu-chuan-lcof/
 * 
 * 题目描述：
 * 请从字符串中找出一个最长的不包含重复字符的子字符串，计算该最长子字符串的长度。
 * 
 * 示例：
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 
 * 输入: "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 
 * 输入: "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 * 
 * 算法思路：
 * 使用滑动窗口技术：
 * 1. 维护一个滑动窗口，表示当前不包含重复字符的子串
 * 2. 使用哈希表记录每个字符最后出现的位置
 * 3. 右边界不断向右扩展窗口
 * 4. 当遇到重复字符时，移动左边界到重复字符的下一个位置
 * 5. 记录过程中的最大窗口长度
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(min(m,n))，其中m是字符集大小
 * 
 * 优化思路：
 * 1. 使用数组代替HashMap提高性能（ASCII字符集）
 * 2. 优化左边界移动策略
 * 3. 提前终止条件判断
 * 
 * 工程化考量：
 * 1. 边界情况处理：空字符串、单字符、全重复字符、全唯一字符
 * 2. 输入验证：支持各种字符集
 * 3. 性能优化：选择合适的数据结构
 * 4. 内存管理：避免不必要的空间占用
 */
public class Code19_JianZhiOffer48_LongestSubstringWithoutRepeating {
    
    /**
     * 方法1：使用HashMap的滑动窗口解法
     * 
     * @param s 输入字符串
     * @return 最长不重复子串的长度
     */
    public static int lengthOfLongestSubstringWithHashMap(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // 使用HashMap记录字符最后出现的位置
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int maxLength = 0;
        int left = 0; // 滑动窗口左边界
        
        // 右边界不断向右扩展
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            
            // 如果字符已存在且在当前窗口内，则移动左边界
            if (charIndexMap.containsKey(currentChar) && charIndexMap.get(currentChar) >= left) {
                left = charIndexMap.get(currentChar) + 1;
            }
            
            // 更新字符最后出现的位置
            charIndexMap.put(currentChar, right);
            
            // 更新最大长度
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 方法2：使用数组优化的滑动窗口解法（仅适用于ASCII字符）
     * 
     * @param s 输入字符串
     * @return 最长不重复子串的长度
     */
    public static int lengthOfLongestSubstringWithArray(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // 使用数组记录字符最后出现的位置（ASCII字符集）
        int[] charIndex = new int[128];
        Arrays.fill(charIndex, -1);
        
        int maxLength = 0;
        int left = 0; // 滑动窗口左边界
        
        // 右边界不断向右扩展
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            
            // 如果字符已存在且在当前窗口内，则移动左边界
            if (charIndex[currentChar] >= left) {
                left = charIndex[currentChar] + 1;
            }
            
            // 更新字符最后出现的位置
            charIndex[currentChar] = right;
            
            // 更新最大长度
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 方法3：动态规划解法
     * dp[i]表示以第i个字符结尾的最长不重复子串长度
     * 
     * @param s 输入字符串
     * @return 最长不重复子串的长度
     */
    public static int lengthOfLongestSubstringDP(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // 使用HashMap记录字符最后出现的位置
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int maxLength = 0;
        int currentLength = 0; // 当前子串长度
        
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            
            // 获取字符上次出现的位置
            int lastOccurrence = charIndexMap.getOrDefault(currentChar, -1);
            
            // 更新字符最后出现的位置
            charIndexMap.put(currentChar, i);
            
            // 计算以当前字符结尾的最长不重复子串长度
            if (i - lastOccurrence > currentLength) {
                // 字符不在当前子串中
                currentLength++;
            } else {
                // 字符在当前子串中，更新长度
                currentLength = i - lastOccurrence;
            }
            
            // 更新最大长度
            maxLength = Math.max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 方法4：返回最长不重复子串本身（不仅仅是长度）
     * 
     * @param s 输入字符串
     * @return 最长不重复子串
     */
    public static String longestSubstringWithoutRepeating(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int maxLength = 0;
        int maxStart = 0; // 最长子串的起始位置
        int left = 0; // 滑动窗口左边界
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            
            // 如果字符已存在且在当前窗口内，则移动左边界
            if (charIndexMap.containsKey(currentChar) && charIndexMap.get(currentChar) >= left) {
                left = charIndexMap.get(currentChar) + 1;
            }
            
            // 更新字符最后出现的位置
            charIndexMap.put(currentChar, right);
            
            // 更新最大长度和起始位置
            if (right - left + 1 > maxLength) {
                maxLength = right - left + 1;
                maxStart = left;
            }
        }
        
        return s.substring(maxStart, maxStart + maxLength);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 剑指Offer 48. 最长不含重复字符的子字符串 ===");
        
        // 测试用例1
        String s1 = "abcabcbb";
        int result1a = lengthOfLongestSubstringWithHashMap(s1);
        int result1b = lengthOfLongestSubstringWithArray(s1);
        int result1c = lengthOfLongestSubstringDP(s1);
        String result1d = longestSubstringWithoutRepeating(s1);
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("HashMap方法结果: " + result1a + " (期望: 3)");
        System.out.println("数组方法结果: " + result1b + " (期望: 3)");
        System.out.println("动态规划方法结果: " + result1c + " (期望: 3)");
        System.out.println("最长子串: \"" + result1d + "\" (期望: \"abc\")");
        System.out.println("测试结果: " + (result1a == 3 && result1b == 3 && result1c == 3 && result1d.equals("abc") ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2
        String s2 = "bbbbb";
        int result2a = lengthOfLongestSubstringWithHashMap(s2);
        int result2b = lengthOfLongestSubstringWithArray(s2);
        int result2c = lengthOfLongestSubstringDP(s2);
        String result2d = longestSubstringWithoutRepeating(s2);
        System.out.println("输入: \"" + s2 + "\"");
        System.out.println("HashMap方法结果: " + result2a + " (期望: 1)");
        System.out.println("数组方法结果: " + result2b + " (期望: 1)");
        System.out.println("动态规划方法结果: " + result2c + " (期望: 1)");
        System.out.println("最长子串: \"" + result2d + "\" (期望: \"b\")");
        System.out.println("测试结果: " + (result2a == 1 && result2b == 1 && result2c == 1 && result2d.equals("b") ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3
        String s3 = "pwwkew";
        int result3a = lengthOfLongestSubstringWithHashMap(s3);
        int result3b = lengthOfLongestSubstringWithArray(s3);
        int result3c = lengthOfLongestSubstringDP(s3);
        String result3d = longestSubstringWithoutRepeating(s3);
        System.out.println("输入: \"" + s3 + "\"");
        System.out.println("HashMap方法结果: " + result3a + " (期望: 3)");
        System.out.println("数组方法结果: " + result3b + " (期望: 3)");
        System.out.println("动态规划方法结果: " + result3c + " (期望: 3)");
        System.out.println("最长子串: \"" + result3d + "\" (期望: \"wke\")");
        System.out.println("测试结果: " + (result3a == 3 && result3b == 3 && result3c == 3 && (result3d.equals("wke") || result3d.equals("kew")) ? "通过" : "失败"));
        System.out.println();
        
        // 边界情况测试
        String s4 = "";
        int result4a = lengthOfLongestSubstringWithHashMap(s4);
        int result4b = lengthOfLongestSubstringWithArray(s4);
        int result4c = lengthOfLongestSubstringDP(s4);
        String result4d = longestSubstringWithoutRepeating(s4);
        System.out.println("输入: \"" + s4 + "\" (空字符串)");
        System.out.println("HashMap方法结果: " + result4a + " (期望: 0)");
        System.out.println("数组方法结果: " + result4b + " (期望: 0)");
        System.out.println("动态规划方法结果: " + result4c + " (期望: 0)");
        System.out.println("最长子串: \"" + result4d + "\" (期望: \"\")");
        System.out.println("测试结果: " + (result4a == 0 && result4b == 0 && result4c == 0 && result4d.equals("") ? "通过" : "失败"));
        System.out.println();
        
        String s5 = "a";
        int result5a = lengthOfLongestSubstringWithHashMap(s5);
        int result5b = lengthOfLongestSubstringWithArray(s5);
        int result5c = lengthOfLongestSubstringDP(s5);
        String result5d = longestSubstringWithoutRepeating(s5);
        System.out.println("输入: \"" + s5 + "\" (单字符)");
        System.out.println("HashMap方法结果: " + result5a + " (期望: 1)");
        System.out.println("数组方法结果: " + result5b + " (期望: 1)");
        System.out.println("动态规划方法结果: " + result5c + " (期望: 1)");
        System.out.println("最长子串: \"" + result5d + "\" (期望: \"a\")");
        System.out.println("测试结果: " + (result5a == 1 && result5b == 1 && result5c == 1 && result5d.equals("a") ? "通过" : "失败"));
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        String largeString = sb.toString();
        
        long startTime = System.currentTimeMillis();
        int resultArray = lengthOfLongestSubstringWithArray(largeString);
        long arrayTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int resultHashMap = lengthOfLongestSubstringWithHashMap(largeString);
        long hashMapTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int resultDP = lengthOfLongestSubstringDP(largeString);
        long dpTime = System.currentTimeMillis() - startTime;
        
        System.out.println("大字符串长度: " + largeString.length());
        System.out.println("数组方法耗时: " + arrayTime + "ms, 结果: " + resultArray);
        System.out.println("HashMap方法耗时: " + hashMapTime + "ms, 结果: " + resultHashMap);
        System.out.println("动态规划方法耗时: " + dpTime + "ms, 结果: " + resultDP);
        System.out.println("结果一致性: " + (resultArray == resultHashMap && resultHashMap == resultDP ? "通过" : "失败"));
        
        System.out.println("所有测试完成");
    }
}