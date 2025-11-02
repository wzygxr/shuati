package class050;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 76. 最小覆盖子串 (Minimum Window Substring)
 * 
 * 题目描述:
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
 * 
 * 注意：
 * - 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
 * - 如果 s 中存在这样的子串，我们保证它是唯一的答案。
 * 
 * 示例1:
 * 输入: s = "ADOBECODEBANC", t = "ABC"
 * 输出: "BANC"
 * 解释: 最小覆盖子串 "BANC" 包含 'A', 'B', 'C' 各一个。
 * 
 * 示例2:
 * 输入: s = "a", t = "a"
 * 输出: "a"
 * 
 * 示例3:
 * 输入: s = "a", t = "aa"
 * 输出: ""
 * 解释: t 中有两个 'a'，但 s 中只有一个 'a'，所以返回空字符串。
 * 
 * 提示:
 * - 1 <= s.length, t.length <= 10^5
 * - s 和 t 由英文字母组成
 * 
 * 题目链接: https://leetcode.cn/problems/minimum-window-substring/
 * 
 * 解题思路:
 * 这道题可以使用滑动窗口（双指针）的方法来解决：
 * 
 * 方法一（滑动窗口 + 哈希表）：
 * 1. 使用两个指针 left 和 right 表示当前窗口的左右边界
 * 2. 使用哈希表记录 t 中每个字符的出现次数
 * 3. 使用另一个哈希表记录当前窗口中包含 t 字符的情况
 * 4. 移动右指针扩展窗口，直到窗口包含 t 的所有字符
 * 5. 然后移动左指针收缩窗口，找到最小覆盖子串
 * 
 * 时间复杂度: O(n + m)，n为s长度，m为t长度
 * 空间复杂度: O(m)，存储t的字符频率
 * 是否最优解：是
 */

public class Code30_MinimumWindowSubstring {
    
    /**
     * 解法一: 滑动窗口 + 哈希表（最优解）
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    public static String minWindow(String s, String t) {
        if (s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }
        
        // 记录t中每个字符的出现次数
        Map<Character, Integer> targetMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetMap.put(c, targetMap.getOrDefault(c, 0) + 1);
        }
        
        // 记录当前窗口中字符的出现次数
        Map<Character, Integer> windowMap = new HashMap<>();
        
        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        int minStart = 0;
        int required = targetMap.size(); // 需要匹配的字符种类数
        int formed = 0; // 当前窗口中已匹配的字符种类数
        
        while (right < s.length()) {
            char rightChar = s.charAt(right);
            windowMap.put(rightChar, windowMap.getOrDefault(rightChar, 0) + 1);
            
            // 如果当前字符在t中，且窗口中出现次数等于t中出现次数，则formed+1
            if (targetMap.containsKey(rightChar) && 
                windowMap.get(rightChar).intValue() == targetMap.get(rightChar).intValue()) {
                formed++;
            }
            
            // 当窗口包含t的所有字符时，尝试收缩窗口
            while (left <= right && formed == required) {
                char leftChar = s.charAt(left);
                
                // 更新最小覆盖子串
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minStart = left;
                }
                
                // 移动左指针
                windowMap.put(leftChar, windowMap.get(leftChar) - 1);
                if (targetMap.containsKey(leftChar) && 
                    windowMap.get(leftChar).intValue() < targetMap.get(leftChar).intValue()) {
                    formed--;
                }
                left++;
            }
            
            right++;
        }
        
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
    }
    
    /**
     * 解法二: 优化版滑动窗口（使用数组替代哈希表）
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    public static String minWindowOptimized(String s, String t) {
        if (s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }
        
        // 使用数组记录字符频率（假设字符为ASCII）
        int[] targetFreq = new int[128];
        int[] windowFreq = new int[128];
        
        for (char c : t.toCharArray()) {
            targetFreq[c]++;
        }
        
        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        int minStart = 0;
        int required = 0;
        
        // 计算需要匹配的字符种类数
        for (int freq : targetFreq) {
            if (freq > 0) {
                required++;
            }
        }
        
        int formed = 0;
        
        while (right < s.length()) {
            char rightChar = s.charAt(right);
            windowFreq[rightChar]++;
            
            // 如果当前字符在t中，且窗口中出现次数等于t中出现次数
            if (targetFreq[rightChar] > 0 && windowFreq[rightChar] == targetFreq[rightChar]) {
                formed++;
            }
            
            // 当窗口包含t的所有字符时，尝试收缩窗口
            while (left <= right && formed == required) {
                char leftChar = s.charAt(left);
                
                // 更新最小覆盖子串
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minStart = left;
                }
                
                // 移动左指针
                windowFreq[leftChar]--;
                if (targetFreq[leftChar] > 0 && windowFreq[leftChar] < targetFreq[leftChar]) {
                    formed--;
                }
                left++;
            }
            
            right++;
        }
        
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
    }
    
    /**
     * 解法三: 进一步优化的滑动窗口（跳过无关字符）
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    public static String minWindowAdvanced(String s, String t) {
        if (s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }
        
        // 使用数组记录字符频率
        int[] targetFreq = new int[128];
        for (char c : t.toCharArray()) {
            targetFreq[c]++;
        }
        
        // 预处理：只保留s中在t中出现的字符及其位置
        int count = t.length();
        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        int minStart = 0;
        
        while (right < s.length()) {
            // 如果当前字符在t中，减少计数
            if (targetFreq[s.charAt(right)] > 0) {
                count--;
            }
            targetFreq[s.charAt(right)]--;
            right++;
            
            // 当计数为0时，表示窗口包含t的所有字符
            while (count == 0) {
                // 更新最小覆盖子串
                if (right - left < minLen) {
                    minLen = right - left;
                    minStart = left;
                }
                
                // 移动左指针
                targetFreq[s.charAt(left)]++;
                if (targetFreq[s.charAt(left)] > 0) {
                    count++;
                }
                left++;
            }
        }
        
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        String s1 = "ADOBECODEBANC";
        String t1 = "ABC";
        String expected1 = "BANC";
        System.out.println("测试用例1:");
        System.out.println("s = \"" + s1 + "\", t = \"" + t1 + "\"");
        System.out.println("解法一结果: \"" + minWindow(s1, t1) + "\"");
        System.out.println("解法二结果: \"" + minWindowOptimized(s1, t1) + "\"");
        System.out.println("解法三结果: \"" + minWindowAdvanced(s1, t1) + "\"");
        System.out.println("期望: \"" + expected1 + "\"");
        System.out.println();
        
        // 测试用例2
        String s2 = "a";
        String t2 = "a";
        String expected2 = "a";
        System.out.println("测试用例2:");
        System.out.println("s = \"" + s2 + "\", t = \"" + t2 + "\"");
        System.out.println("解法一结果: \"" + minWindow(s2, t2) + "\"");
        System.out.println("解法二结果: \"" + minWindowOptimized(s2, t2) + "\"");
        System.out.println("解法三结果: \"" + minWindowAdvanced(s2, t2) + "\"");
        System.out.println("期望: \"" + expected2 + "\"");
        System.out.println();
        
        // 测试用例3
        String s3 = "a";
        String t3 = "aa";
        String expected3 = "";
        System.out.println("测试用例3:");
        System.out.println("s = \"" + s3 + "\", t = \"" + t3 + "\"");
        System.out.println("解法一结果: \"" + minWindow(s3, t3) + "\"");
        System.out.println("解法二结果: \"" + minWindowOptimized(s3, t3) + "\"");
        System.out.println("解法三结果: \"" + minWindowAdvanced(s3, t3) + "\"");
        System.out.println("期望: \"" + expected3 + "\"");
        System.out.println();
        
        // 测试用例4 - 边界情况：s和t相同
        String s4 = "abc";
        String t4 = "abc";
        String expected4 = "abc";
        System.out.println("测试用例4（s和t相同）:");
        System.out.println("s = \"" + s4 + "\", t = \"" + t4 + "\"");
        System.out.println("解法一结果: \"" + minWindow(s4, t4) + "\"");
        System.out.println("解法二结果: \"" + minWindowOptimized(s4, t4) + "\"");
        System.out.println("解法三结果: \"" + minWindowAdvanced(s4, t4) + "\"");
        System.out.println("期望: \"" + expected4 + "\"");
        System.out.println();
        
        // 测试用例5 - 边界情况：t不在s中
        String s5 = "abcdef";
        String t5 = "xyz";
        String expected5 = "";
        System.out.println("测试用例5（t不在s中）:");
        System.out.println("s = \"" + s5 + "\", t = \"" + t5 + "\"");
        System.out.println("解法一结果: \"" + minWindow(s5, t5) + "\"");
        System.out.println("解法二结果: \"" + minWindowOptimized(s5, t5) + "\"");
        System.out.println("解法三结果: \"" + minWindowAdvanced(s5, t5) + "\"");
        System.out.println("期望: \"" + expected5 + "\"");
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建长字符串进行性能测试
        StringBuilder sbS = new StringBuilder();
        StringBuilder sbT = new StringBuilder();
        
        // s: 包含重复模式的字符串
        for (int i = 0; i < 10000; i++) {
            sbS.append("ABCDEFG");
        }
        String longS = sbS.toString();
        
        // t: 需要查找的子串
        sbT.append("ABC");
        String longT = sbT.toString();
        
        // 测试解法一的性能
        long startTime = System.nanoTime();
        String result1 = minWindow(longS, longT);
        long endTime = System.nanoTime();
        long duration1 = (endTime - startTime) / 1000000; // 转换为毫秒
        System.out.println("解法一（哈希表）耗时: " + duration1 + "ms, 结果长度: " + result1.length());
        
        // 测试解法二的性能
        startTime = System.nanoTime();
        String result2 = minWindowOptimized(longS, longT);
        endTime = System.nanoTime();
        long duration2 = (endTime - startTime) / 1000000;
        System.out.println("解法二（数组优化）耗时: " + duration2 + "ms, 结果长度: " + result2.length());
        
        // 测试解法三的性能
        startTime = System.nanoTime();
        String result3 = minWindowAdvanced(longS, longT);
        endTime = System.nanoTime();
        long duration3 = (endTime - startTime) / 1000000;
        System.out.println("解法三（高级优化）耗时: " + duration3 + "ms, 结果长度: " + result3.length());
        
        // 验证结果一致性
        System.out.println("所有解法结果一致: " + result1.equals(result2) && result2.equals(result3));
    }
    
    /**
     * 算法分析
     */
    public static void algorithmAnalysis() {
        System.out.println("=== 算法分析 ===");
        System.out.println("1. 解法一（滑动窗口 + 哈希表）");
        System.out.println("   - 时间复杂度: O(n + m) - n为s长度，m为t长度");
        System.out.println("   - 空间复杂度: O(m) - 存储t的字符频率");
        System.out.println("   - 优点: 通用性强，适用于任意字符集");
        System.out.println("   - 缺点: 哈希表操作有一定开销");
        System.out.println();
        
        System.out.println("2. 解法二（数组优化版）");
        System.out.println("   - 时间复杂度: O(n + m)");
        System.out.println("   - 空间复杂度: O(1) - 固定大小的数组");
        System.out.println("   - 优点: 效率高，适用于ASCII字符集");
        System.out.println("   - 缺点: 仅适用于有限字符集");
        System.out.println();
        
        System.out.println("3. 解法三（进一步优化）");
        System.out.println("   - 时间复杂度: O(n)");
        System.out.println("   - 空间复杂度: O(1)");
        System.out.println("   - 优点: 最优化实现，跳过无关字符");
        System.out.println("   - 缺点: 实现相对复杂");
        System.out.println();
        
        System.out.println("推荐使用解法二作为通用解决方案");
    }
    
    public static void main(String[] args) {
        System.out.println("=== 最小覆盖子串 算法实现 ===");
        System.out.println();
        
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
        
        System.out.println("=== 算法分析 ===");
        algorithmAnalysis();
    }
}