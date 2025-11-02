package class029_AdvancedDataStructures.boyer_moore_problems;

/**
 * LeetCode 214 - 最短回文串
 * 题目链接：https://leetcode.com/problems/shortest-palindrome/
 * 
 * 题目描述：
 * 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。
 * 找到并返回可以用这种方式转换的最短回文串。
 * 
 * 示例 1:
 * 输入: s = "aacecaaa"
 * 输出: "aaacecaaa"
 * 
 * 示例 2:
 * 输入: s = "abcd"
 * 输出: "dcbabcd"
 * 
 * 时间复杂度：O(n) 使用KMP算法的预处理
 * 空间复杂度：O(n) 用于存储next数组
 * 
 * 工程化考量：
 * 1. 使用KMP算法的next数组优化回文判断
 * 2. 边界条件处理：空字符串、单字符字符串
 * 3. 性能优化：避免暴力匹配
 */
public class LeetCode_214_ShortestPalindrome {
    
    public String shortestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        
        // 构建反转字符串
        String reversed = new StringBuilder(s).reverse().toString();
        
        // 构建新字符串：s + "#" + reversed
        String combined = s + "#" + reversed;
        
        // 计算KMP算法的next数组
        int[] next = computeNextArray(combined);
        
        // 找到最长公共前后缀长度
        int maxLen = next[combined.length() - 1];
        
        // 需要添加的前缀是反转字符串中除去最长公共前后缀的部分
        String prefix = reversed.substring(0, s.length() - maxLen);
        
        return prefix + s;
    }
    
    /**
     * 计算KMP算法的next数组
     */
    private int[] computeNextArray(String pattern) {
        int n = pattern.length();
        int[] next = new int[n];
        next[0] = 0;
        
        int len = 0; // 当前最长公共前后缀长度
        int i = 1;
        
        while (i < n) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                next[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = next[len - 1];
                } else {
                    next[i] = 0;
                    i++;
                }
            }
        }
        
        return next;
    }
    
    /**
     * 单元测试
     */
    public static void main(String[] args) {
        LeetCode_214_ShortestPalindrome solution = new LeetCode_214_ShortestPalindrome();
        
        System.out.println("测试1: " + solution.shortestPalindrome("aacecaaa")); // aaacecaaa
        System.out.println("测试2: " + solution.shortestPalindrome("abcd")); // dcbabcd
        System.out.println("测试3: " + solution.shortestPalindrome("a")); // a
        System.out.println("测试4: " + solution.shortestPalindrome("")); // 
    }
}