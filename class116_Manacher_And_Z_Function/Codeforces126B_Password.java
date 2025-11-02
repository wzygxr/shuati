package class103;

import java.util.*;
import java.io.*;

/**
 * Codeforces 126B. Password
 * 
 * 题目描述：
 * 给定一个字符串s，找出最长的子串t，它既是s的前缀，也是s的后缀，还在s的中间出现过。
 * 如果存在这样的子串，输出最长的那个；否则输出"Just a legend"。
 * 
 * 解题思路：
 * 使用Z函数（扩展KMP）算法解决此问题：
 * 1. 计算字符串s的Z函数数组z，其中z[i]表示以位置i开始的后缀与原字符串的最长公共前缀长度
 * 2. 遍历z数组，找到既是前缀又是后缀的子串（即z[i] == n-i的情况）
 * 3. 同时记录在中间出现过的前缀长度
 * 4. 找到满足所有条件的最长子串
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 题目链接：https://codeforces.com/problemset/problem/126/B
 * 相关题目：
 * 1. LeetCode 28. 找出字符串中第一个匹配项的下标 - https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
 * 2. LeetCode 214. 最短回文串 - https://leetcode.com/problems/shortest-palindrome/
 * 3. LeetCode 459. 重复的子字符串 - https://leetcode.com/problems/repeated-substring-pattern/
 * 4. SPOJ - Pattern Find
 * 5. HackerEarth - String Similarity
 * 6. AtCoder ABC141E - Who Says a Pun?
 */
public class Codeforces126B_Password {
    
    /**
     * 使用Z函数解决Codeforces 126B Password问题
     * 
     * @param s 输入字符串
     * @return 满足条件的最长子串，如果不存在则返回"Just a legend"
     */
    public static String solve(String s) {
        int n = s.length();
        if (n <= 2) return "Just a legend";
        
        // 计算Z函数
        int[] z = zFunction(s);
        
        // 记录在中间出现过的前缀长度
        boolean[] hasPrefix = new boolean[n + 1];
        
        // 标记在中间出现过的前缀长度
        for (int i = 1; i < n; i++) {
            if (z[i] > 0) {
                hasPrefix[z[i]] = true;
            }
        }
        
        // 查找既是前缀又是后缀且在中间出现过的最长子串
        int maxLen = 0;
        for (int i = 1; i < n; i++) {
            // 如果从位置i开始的后缀与原字符串的最长公共前缀长度等于后缀长度
            // 说明这个后缀与原字符串的前缀完全匹配
            if (z[i] == n - i && hasPrefix[z[i]]) {
                maxLen = Math.max(maxLen, z[i]);
            }
        }
        
        // 如果找到了满足条件的子串，返回它；否则返回"Just a legend"
        return maxLen > 0 ? s.substring(0, maxLen) : "Just a legend";
    }
    
    /**
     * Z函数计算
     * Z函数z[i]表示字符串s从位置i开始与字符串s从位置0开始的最长公共前缀长度
     * 
     * 算法原理：
     * 1. 维护一个匹配区间[l, r]，表示当前已知的最右匹配区间
     * 2. 对于当前位置i，如果i <= r，可以利用已计算的信息优化
     * 3. 利用对称性，z[i]至少为min(r - i + 1, z[i - l])
     * 4. 在此基础之上继续向右扩展匹配
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param s 输入字符串
     * @return Z函数数组
     */
    public static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];
        z[0] = n;
        
        // l: 当前最右匹配区间的左边界
        // r: 当前最右匹配区间的右边界
        for (int i = 1, l = 0, r = 0; i < n; i++) {
            // 利用已计算的信息优化
            // 如果i在当前匹配区间内
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            
            // 继续向右扩展匹配
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            
            // 更新最右匹配区间
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        
        return z;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试
        System.out.println(solve("fixprefixsuffix")); // 输出: fix
        System.out.println(solve("abcdabc")); // 输出: Just a legend
        System.out.println(solve("abcab")); // 输出: ab
    }
}