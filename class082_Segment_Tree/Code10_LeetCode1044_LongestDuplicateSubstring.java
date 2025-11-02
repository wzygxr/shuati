/**
 * 文件名: Code10_LeetCode1044_LongestDuplicateSubstring.java
 * 算法名称: LeetCode 1044. 最长重复子串
 * 应用场景: 字符串处理、滚动哈希、二分查找
 * 实现语言: Java
 * 作者: 算法实现者
 * 创建时间: 2024-10-26
 * 最后修改: 2024-10-26
 * 版本: 1.0
 * 
 * 题目来源: https://leetcode.com/problems/longest-duplicate-substring/
 * 题目描述: 给定一个字符串 s，找出其中最长的重复子串。如果有多个最长重复子串，返回任意一个。
 * 
 * 解题思路:
 * 1. 使用二分查找确定可能的最长子串长度
 * 2. 对于每个长度mid，使用滚动哈希计算所有长度为mid的子串的哈希值
 * 3. 使用哈希集合检测是否存在重复的子串
 * 4. 使用双哈希技术减少哈希冲突的概率
 * 
 * 时间复杂度分析:
 * - 二分查找: O(log n)
 * - 每次检查: O(n) 计算哈希值
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 哈希集合存储哈希值: O(n)
 * - 辅助数组: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * - 使用大质数减少哈希冲突
 * - 双哈希技术提高准确性
 * - 处理边界情况(空字符串、单字符等)
 * - 优化内存使用，避免不必要的对象创建
 */

import java.util.HashSet;
import java.util.Set;

public class Code10_LeetCode1044_LongestDuplicateSubstring {
    
    // 双哈希技术使用的大质数
    private static final long MOD1 = 1000000007L;
    private static final long MOD2 = 1000000009L;
    
    // 基数，通常选择大于字符集大小的质数
    private static final long BASE = 131L;
    
    /**
     * 主方法：寻找最长重复子串
     * 
     * @param s 输入字符串
     * @return 最长重复子串，如果没有重复子串则返回空字符串
     * 
     * 算法步骤:
     * 1. 边界检查：空字符串或单字符处理
     * 2. 二分查找确定可能的最长子串长度
     * 3. 对于每个长度mid，检查是否存在重复子串
     * 4. 使用滚动哈希优化性能
     */
    public String longestDupSubstring(String s) {
        if (s == null || s.length() <= 1) {
            return "";
        }
        
        int n = s.length();
        int left = 1, right = n - 1;
        String result = "";
        
        // 预处理幂数组，用于快速计算哈希值
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        pow1[0] = 1;
        pow2[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow1[i] = (pow1[i - 1] * BASE) % MOD1;
            pow2[i] = (pow2[i - 1] * BASE) % MOD2;
        }
        
        // 预处理前缀哈希数组
        long[] hash1 = new long[n + 1];
        long[] hash2 = new long[n + 1];
        for (int i = 0; i < n; i++) {
            hash1[i + 1] = (hash1[i] * BASE + s.charAt(i)) % MOD1;
            hash2[i + 1] = (hash2[i] * BASE + s.charAt(i)) % MOD2;
        }
        
        // 二分查找最长重复子串长度
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String dup = findDuplicate(s, mid, hash1, hash2, pow1, pow2);
            
            if (dup != null) {
                // 找到重复子串，尝试更长的长度
                result = dup;
                left = mid + 1;
            } else {
                // 未找到重复子串，尝试更短的长度
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 查找指定长度的重复子串
     * 
     * @param s 输入字符串
     * @param len 要查找的子串长度
     * @param hash1 第一个哈希函数的前缀哈希数组
     * @param hash2 第二个哈希函数的前缀哈希数组
     * @param pow1 第一个哈希函数的幂数组
     * @param pow2 第二个哈希函数的幂数组
     * @return 重复子串，如果不存在则返回null
     * 
     * 算法步骤:
     * 1. 使用滚动哈希计算所有长度为len的子串的哈希值
     * 2. 使用双哈希技术减少冲突
     * 3. 使用哈希集合检测重复
     * 4. 如果找到重复，返回对应的子串
     */
    private String findDuplicate(String s, int len, long[] hash1, long[] hash2, 
                                long[] pow1, long[] pow2) {
        Set<Long> seen = new HashSet<>();
        int n = s.length();
        
        for (int i = 0; i <= n - len; i++) {
            // 计算子串的哈希值（双哈希）
            long h1 = (hash1[i + len] - hash1[i] * pow1[len] % MOD1 + MOD1) % MOD1;
            long h2 = (hash2[i + len] - hash2[i] * pow2[len] % MOD2 + MOD2) % MOD2;
            
            // 将双哈希组合成一个唯一的键
            long key = h1 * MOD2 + h2;
            
            if (seen.contains(key)) {
                // 找到重复子串，返回该子串
                return s.substring(i, i + len);
            }
            
            seen.add(key);
        }
        
        return null;
    }
    
    /**
     * 测试方法：验证算法正确性
     * 
     * 测试用例设计:
     * 1. 空字符串和单字符边界情况
     * 2. 普通重复子串情况
     * 3. 多个重复子串情况
     * 4. 无重复子串情况
     * 5. 极端长字符串情况
     */
    public static void main(String[] args) {
        Code10_LeetCode1044_LongestDuplicateSubstring solution = new Code10_LeetCode1044_LongestDuplicateSubstring();
        
        // 测试用例1: 普通情况
        String test1 = "banana";
        System.out.println("测试1 (banana): " + solution.longestDupSubstring(test1));
        // 预期输出: "ana" 或 "na"
        
        // 测试用例2: 多个重复子串
        String test2 = "abcd";
        System.out.println("测试2 (abcd): " + solution.longestDupSubstring(test2));
        // 预期输出: ""
        
        // 测试用例3: 边界情况
        String test3 = "a";
        System.out.println("测试3 (a): " + solution.longestDupSubstring(test3));
        // 预期输出: ""
        
        // 测试用例4: 长重复子串
        String test4 = "abcabcabc";
        System.out.println("测试4 (abcabcabc): " + solution.longestDupSubstring(test4));
        // 预期输出: "abcabc"
        
        // 测试用例5: 空字符串
        String test5 = "";
        System.out.println("测试5 (空字符串): " + solution.longestDupSubstring(test5));
        // 预期输出: ""
    }
    
    /**
     * 性能分析:
     * - 时间复杂度: O(n log n)
     *   - 二分查找: O(log n)
     *   - 每次检查: O(n) 计算哈希值
     * - 空间复杂度: O(n)
     *   - 哈希集合: O(n)
     *   - 辅助数组: O(n)
     * 
     * 优化策略:
     * 1. 使用滚动哈希避免重复计算
     * 2. 双哈希技术减少冲突概率
     * 3. 预处理幂数组提高计算效率
     * 4. 使用HashSet提供O(1)的查找性能
     * 
     * 异常处理:
     * - 空字符串和单字符边界情况
     * - 大质数取模防止整数溢出
     * - 负模数处理确保正确性
     */
}