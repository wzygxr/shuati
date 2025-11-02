package class050;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
 * 
 * 题目描述:
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 * 
 * 示例1:
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 
 * 示例2:
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 
 * 示例3:
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 *     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 * 
 * 提示:
 * 0 <= s.length <= 5 * 10^4
 * s 由英文字母、数字、符号和空格组成
 * 
 * 题目链接: https://leetcode.cn/problems/longest-substring-without-repeating-characters/
 * 
 * 解题思路:
 * 这道题可以使用滑动窗口（双指针）的方法来解决：
 * 
 * 方法一（滑动窗口 + HashSet）：
 * 1. 使用两个指针 left 和 right 表示当前窗口的左右边界
 * 2. 使用 HashSet 记录当前窗口中的字符
 * 3. 右指针向右移动，如果当前字符不在集合中，加入集合并更新最大长度
 * 4. 如果当前字符在集合中，移动左指针直到移除重复字符
 * 
 * 方法二（滑动窗口 + HashMap优化）：
 * 1. 使用 HashMap 记录每个字符最后出现的位置
 * 2. 当遇到重复字符时，可以直接将左指针移动到重复字符的下一个位置
 * 3. 避免左指针的逐步移动，提高效率
 * 
 * 时间复杂度: O(n)，每个字符最多被访问两次
 * 空间复杂度: O(min(m, n))，m为字符集大小
 * 是否最优解：是
 */

public class Code29_LongestSubstringWithoutRepeatingCharacters {
    
    /**
     * 解法一: 滑动窗口 + HashSet
     * 
     * @param s 输入字符串
     * @return 无重复字符的最长子串长度
     */
    public static int lengthOfLongestSubstringHashSet(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        Set<Character> window = new HashSet<>();
        int maxLength = 0;
        int left = 0, right = 0;
        int n = s.length();
        
        while (right < n) {
            char currentChar = s.charAt(right);
            
            // 如果窗口中没有当前字符，加入窗口
            if (!window.contains(currentChar)) {
                window.add(currentChar);
                maxLength = Math.max(maxLength, right - left + 1);
                right++;
            } else {
                // 有重复字符，移动左指针直到移除重复字符
                window.remove(s.charAt(left));
                left++;
            }
        }
        
        return maxLength;
    }
    
    /**
     * 解法二: 滑动窗口 + HashMap优化（最优解）
     * 
     * @param s 输入字符串
     * @return 无重复字符的最长子串长度
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            
            // 如果字符已经存在，更新左指针位置
            if (charIndexMap.containsKey(currentChar)) {
                // 取最大值是为了避免左指针回退
                left = Math.max(left, charIndexMap.get(currentChar) + 1);
            }
            
            // 更新字符的最新位置
            charIndexMap.put(currentChar, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 解法三: 数组优化版（适用于ASCII字符）
     * 
     * @param s 输入字符串
     * @return 无重复字符的最长子串长度
     */
    public static int lengthOfLongestSubstringArray(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // 假设字符集为ASCII，使用数组记录字符最后出现的位置
        int[] lastIndex = new int[128]; // ASCII字符集大小
        // 初始化数组为-1
        for (int i = 0; i < lastIndex.length; i++) {
            lastIndex[i] = -1;
        }
        
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            
            // 如果字符已经存在，更新左指针位置
            if (lastIndex[currentChar] >= left) {
                left = lastIndex[currentChar] + 1;
            }
            
            // 更新字符的最新位置
            lastIndex[currentChar] = right;
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        String s1 = "abcabcbb";
        int expected1 = 3;
        System.out.println("测试用例1:");
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("解法一结果: " + lengthOfLongestSubstringHashSet(s1));
        System.out.println("解法二结果: " + lengthOfLongestSubstring(s1));
        System.out.println("解法三结果: " + lengthOfLongestSubstringArray(s1));
        System.out.println("期望: " + expected1);
        System.out.println();
        
        // 测试用例2
        String s2 = "bbbbb";
        int expected2 = 1;
        System.out.println("测试用例2:");
        System.out.println("输入: \"" + s2 + "\"");
        System.out.println("解法一结果: " + lengthOfLongestSubstringHashSet(s2));
        System.out.println("解法二结果: " + lengthOfLongestSubstring(s2));
        System.out.println("解法三结果: " + lengthOfLongestSubstringArray(s2));
        System.out.println("期望: " + expected2);
        System.out.println();
        
        // 测试用例3
        String s3 = "pwwkew";
        int expected3 = 3;
        System.out.println("测试用例3:");
        System.out.println("输入: \"" + s3 + "\"");
        System.out.println("解法一结果: " + lengthOfLongestSubstringHashSet(s3));
        System.out.println("解法二结果: " + lengthOfLongestSubstring(s3));
        System.out.println("解法三结果: " + lengthOfLongestSubstringArray(s3));
        System.out.println("期望: " + expected3);
        System.out.println();
        
        // 测试用例4 - 边界情况：空字符串
        String s4 = "";
        int expected4 = 0;
        System.out.println("测试用例4（空字符串）:");
        System.out.println("输入: \"" + s4 + "\"");
        System.out.println("解法一结果: " + lengthOfLongestSubstringHashSet(s4));
        System.out.println("解法二结果: " + lengthOfLongestSubstring(s4));
        System.out.println("解法三结果: " + lengthOfLongestSubstringArray(s4));
        System.out.println("期望: " + expected4);
        System.out.println();
        
        // 测试用例5 - 边界情况：单个字符
        String s5 = "a";
        int expected5 = 1;
        System.out.println("测试用例5（单个字符）:");
        System.out.println("输入: \"" + s5 + "\"");
        System.out.println("解法一结果: " + lengthOfLongestSubstringHashSet(s5));
        System.out.println("解法二结果: " + lengthOfLongestSubstring(s5));
        System.out.println("解法三结果: " + lengthOfLongestSubstringArray(s5));
        System.out.println("期望: " + expected5);
        System.out.println();
        
        // 测试用例6 - 复杂情况
        String s6 = "dvdf";
        int expected6 = 3;
        System.out.println("测试用例6（复杂情况）:");
        System.out.println("输入: \"" + s6 + "\"");
        System.out.println("解法一结果: " + lengthOfLongestSubstringHashSet(s6));
        System.out.println("解法二结果: " + lengthOfLongestSubstring(s6));
        System.out.println("解法三结果: " + lengthOfLongestSubstringArray(s6));
        System.out.println("期望: " + expected6);
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建长字符串进行性能测试
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append((char)('a' + (i % 26))); // 循环添加a-z
        }
        String longString = sb.toString();
        
        // 测试解法一的性能
        long startTime = System.nanoTime();
        int result1 = lengthOfLongestSubstringHashSet(longString);
        long endTime = System.nanoTime();
        long duration1 = (endTime - startTime) / 1000000; // 转换为毫秒
        System.out.println("解法一（HashSet）耗时: " + duration1 + "ms, 结果: " + result1);
        
        // 测试解法二的性能
        startTime = System.nanoTime();
        int result2 = lengthOfLongestSubstring(longString);
        endTime = System.nanoTime();
        long duration2 = (endTime - startTime) / 1000000;
        System.out.println("解法二（HashMap优化）耗时: " + duration2 + "ms, 结果: " + result2);
        
        // 测试解法三的性能
        startTime = System.nanoTime();
        int result3 = lengthOfLongestSubstringArray(longString);
        endTime = System.nanoTime();
        long duration3 = (endTime - startTime) / 1000000;
        System.out.println("解法三（数组优化）耗时: " + duration3 + "ms, 结果: " + result3);
        
        // 验证结果一致性
        System.out.println("所有解法结果一致: " + (result1 == result2 && result2 == result3));
    }
    
    public static void main(String[] args) {
        System.out.println("=== 无重复字符的最长子串 算法实现 ===");
        System.out.println();
        
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
    }
}