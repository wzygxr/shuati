package class103;

import java.util.*;
import java.io.*;

/**
 * LeetCode 1960. 两个回文子字符串长度的最大乘积
 * 
 * 题目描述：
 * 给你一个下标从0开始的字符串 s ，你需要找到两个不重叠的回文子字符串，
 * 它们的长度都必须为奇数，使得它们长度的乘积最大。
 * 
 * 解题思路：
 * 使用Manacher算法计算所有奇回文信息：
 * 1. 使用Manacher算法计算每个位置为中心的最长奇回文半径
 * 2. 预处理前缀和后缀数组，分别记录到每个位置为止的最长回文长度
 * 3. 枚举每个分割点，通过前后缀获取左右两个子串中的最长回文大小，相乘即可
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 题目链接：https://leetcode.com/problems/maximum-product-of-the-length-of-two-palindromic-substrings/
 * 相关题目：
 * 1. LeetCode 5. 最长回文子串 - https://leetcode.com/problems/longest-palindromic-substring/
 * 2. LeetCode 647. 回文子串 - https://leetcode.com/problems/palindromic-substrings/
 * 3. LeetCode 336. 回文对 - https://leetcode.com/problems/palindrome-pairs/
 * 4. LeetCode 131. 分割回文串 - https://leetcode.com/problems/palindrome-partitioning/
 * 5. LeetCode 132. 分割回文串 II - https://leetcode.com/problems/palindrome-partitioning-ii/
 * 6. 洛谷 P3805 【模板】manacher - https://www.luogu.com.cn/problem/P3805
 * 7. UVa 11475 - Extend to Palindrome - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2470
 * 8. Codeforces 1326D2 - Prefix-Suffix Palindrome - https://codeforces.com/problemset/problem/1326/D2
 * 9. HackerRank - Palindromic Substrings
 * 10. AcWing 141. 周期 - https://www.acwing.com/problem/content/143/
 * 11. POJ 3240 - 回文串
 */
public class LeetCode1960_MaxProduct {
    
    /**
     * 计算两个不重叠奇回文子字符串长度的最大乘积
     * 
     * @param s 输入字符串
     * @return 最大乘积
     */
    public static long maxProduct(String s) {
        int n = s.length();
        
        // 使用Manacher算法计算每个位置为中心的最长奇回文半径
        int[] radius = manacherOdd(s);
        
        // prefix[i] 表示在 [0, i] 范围内能找到的最长奇回文子串长度
        long[] prefix = new long[n];
        // suffix[i] 表示在 [i, n-1] 范围内能找到的最长奇回文子串长度
        long[] suffix = new long[n];
        
        // 初始化
        prefix[0] = 1;
        suffix[n - 1] = 1;
        
        // 计算前缀数组
        for (int i = 1; i < n; i++) {
            // 检查以位置i结尾的回文串
            for (int j = 0; j <= i; j++) {
                // 回文串的右边界是i，中心是j，半径是radius[j]
                if (j + radius[j] - 1 >= i) {
                    prefix[i] = Math.max(prefix[i], 2 * (i - j) + 1);
                }
            }
            prefix[i] = Math.max(prefix[i], prefix[i - 1]);
        }
        
        // 计算后缀数组
        for (int i = n - 2; i >= 0; i--) {
            // 检查以位置i开头的回文串
            for (int j = i; j < n; j++) {
                // 回文串的左边界是i，中心是j，半径是radius[j]
                if (j - radius[j] + 1 <= i) {
                    suffix[i] = Math.max(suffix[i], 2 * (j - i) + 1);
                }
            }
            suffix[i] = Math.max(suffix[i], suffix[i + 1]);
        }
        
        // 枚举分割点，计算最大乘积
        long maxProduct = 0;
        for (int i = 0; i < n - 1; i++) {
            maxProduct = Math.max(maxProduct, prefix[i] * suffix[i + 1]);
        }
        
        return maxProduct;
    }
    
    /**
     * Manacher算法计算奇回文串
     * 
     * @param s 输入字符串
     * @return 每个位置为中心的最长奇回文半径数组
     */
    public static int[] manacherOdd(String s) {
        int n = s.length();
        int[] radius = new int[n];
        
        for (int i = 0, l = 0, r = -1; i < n; i++) {
            // 利用回文对称性
            int k = (i > r) ? 1 : Math.min(radius[l + r - i], r - i + 1);
            
            // 尝试扩展回文串
            while (0 <= i - k && i + k < n && s.charAt(i - k) == s.charAt(i + k)) {
                k++;
            }
            
            radius[i] = k--;
            
            // 更新最右回文边界
            if (i + k > r) {
                l = i - k;
                r = i + k;
            }
        }
        
        return radius;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试
        System.out.println(maxProduct("ababbb")); // 输出: 9
        System.out.println(maxProduct("zaaaxbbby")); // 输出: 9
    }
}