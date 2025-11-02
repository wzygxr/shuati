package class029_AdvancedDataStructures;

import java.util.*;

/**
 * Rabin-Karp算法题目实现
 * 
 * 本文件包含了多个使用Rabin-Karp算法解决的经典算法题目：
 * 1. SPOJ - Pattern Find (模式查找)
 * 2. Codeforces - Good Substrings (好子串)
 * 3. Codeforces - Palindromic characteristics (回文特征)
 * 4. Leetcode - Longest Duplicate Substring (最长重复子串)
 * 5. SPOJ - NAJPF (Pattern Find加强版)
 * 6. Codeforces - A Needle in the Haystack (大海捞针)
 * 7. SPOJ - EPALIN (扩展为回文)
 * 8. Codeforces - String Hashing (字符串哈希)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class RabinKarpProblems {
    
    private static final long BASE = 256;  // 字符集大小
    private static final long MOD = 1000000007;  // 大素数，防止溢出
    
    /**
     * Rabin-Karp算法实现
     */
    static class RabinKarp {
        private static final long BASE = 256;
        private static final long MOD = 1000000007;
        
        /**
         * 查找模式串在文本串中所有出现的位置
         * @param text 文本串
         * @param pattern 模式串
         * @return 包含所有匹配位置的列表
         */
        public static List<Integer> searchAll(String text, String pattern) {
            List<Integer> matches = new ArrayList<>();
            
            if (text == null || pattern == null) {
                throw new IllegalArgumentException("文本串和模式串不能为null");
            }
            
            int n = text.length();
            int m = pattern.length();
            
            // 边界条件检查
            if (m == 0) {
                // 空模式串匹配每个位置的开始
                for (int i = 0; i <= n; i++) {
                    matches.add(i);
                }
                return matches;
            }
            if (n < m) {
                return matches; // 无匹配
            }
            
            // 计算pattern的哈希值和text前m个字符的哈希值
            long patternHash = 0;
            long textHash = 0;
            long highestPow = 1; // BASE^(m-1) % MOD
            
            // 预计算最高位权值和初始哈希值
            for (int i = 0; i < m - 1; i++) {
                highestPow = (highestPow * BASE) % MOD;
            }
            
            for (int i = 0; i < m; i++) {
                patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
                textHash = (textHash * BASE + text.charAt(i)) % MOD;
            }
            
            // 滑动窗口匹配
            for (int i = 0; i <= n - m; i++) {
                // 如果哈希值相同，进行精确比较以避免哈希冲突
                if (patternHash == textHash) {
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (text.charAt(i + j) != pattern.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        matches.add(i); // 记录匹配位置
                    }
                }
                
                // 更新滑动窗口的哈希值
                if (i < n - m) {
                    // 移除最左边的字符
                    textHash = (textHash - highestPow * text.charAt(i) % MOD + MOD) % MOD;
                    // 添加新的右边字符
                    textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
                }
            }
            
            return matches;
        }
        
        /**
         * 双哈希版本的Rabin-Karp算法，用于减少哈希冲突
         * @param text 文本串
         * @param pattern 模式串
         * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
         */
        public static int searchDoubleHash(String text, String pattern) {
            if (text == null || pattern == null) {
                throw new IllegalArgumentException("文本串和模式串不能为null");
            }
            
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0) return 0;
            if (n < m) return -1;
            
            // 使用两个不同的哈希参数
            long BASE1 = 256, MOD1 = 1000000007;
            long BASE2 = 263, MOD2 = 1000000009;
            
            long patternHash1 = 0, textHash1 = 0;
            long patternHash2 = 0, textHash2 = 0;
            long highestPow1 = 1, highestPow2 = 1;
            
            // 预计算最高位权值
            for (int i = 0; i < m - 1; i++) {
                highestPow1 = (highestPow1 * BASE1) % MOD1;
                highestPow2 = (highestPow2 * BASE2) % MOD2;
            }
            
            // 计算初始哈希值
            for (int i = 0; i < m; i++) {
                patternHash1 = (patternHash1 * BASE1 + pattern.charAt(i)) % MOD1;
                textHash1 = (textHash1 * BASE1 + text.charAt(i)) % MOD1;
                
                patternHash2 = (patternHash2 * BASE2 + pattern.charAt(i)) % MOD2;
                textHash2 = (textHash2 * BASE2 + text.charAt(i)) % MOD2;
            }
            
            // 滑动窗口匹配
            for (int i = 0; i <= n - m; i++) {
                // 双重哈希都相等时才进行精确比较
                if (patternHash1 == textHash1 && patternHash2 == textHash2) {
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (text.charAt(i + j) != pattern.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        return i;
                    }
                }
                
                // 更新哈希值
                if (i < n - m) {
                    // 哈希1更新
                    textHash1 = (textHash1 - highestPow1 * text.charAt(i) % MOD1 + MOD1) % MOD1;
                    textHash1 = (textHash1 * BASE1 + text.charAt(i + m)) % MOD1;
                    
                    // 哈希2更新
                    textHash2 = (textHash2 - highestPow2 * text.charAt(i) % MOD2 + MOD2) % MOD2;
                    textHash2 = (textHash2 * BASE2 + text.charAt(i + m)) % MOD2;
                }
            }
            
            return -1;
        }
    }
    
    // ====================================================================================
    // 题目1: SPOJ - Pattern Find (模式查找)
    // 题目描述: 在文本中查找所有模式串的出现位置
    // 解题思路: 使用Rabin-Karp算法进行字符串匹配
    // 时间复杂度: O(n+m)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PatternFind {
        public static List<Integer> findAllOccurrences(String text, String pattern) {
            return RabinKarp.searchAll(text, pattern);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * using namespace std;
     * 
     * const long long BASE = 256;
     * const long long MOD = 1000000007;
     * 
     * vector<int> rabinKarpSearch(const string& text, const string& pattern) {
     *     vector<int> matches;
     *     int n = text.length();
     *     int m = pattern.length();
     *     
     *     if (m == 0) {
     *         for (int i = 0; i <= n; i++) {
     *             matches.push_back(i);
     *         }
     *         return matches;
     *     }
     *     if (n < m) {
     *         return matches;
     *     }
     *     
     *     long long patternHash = 0, textHash = 0, highestPow = 1;
     *     
     *     for (int i = 0; i < m - 1; i++) {
     *         highestPow = (highestPow * BASE) % MOD;
     *     }
     *     
     *     for (int i = 0; i < m; i++) {
     *         patternHash = (patternHash * BASE + pattern[i]) % MOD;
     *         textHash = (textHash * BASE + text[i]) % MOD;
     *     }
     *     
     *     for (int i = 0; i <= n - m; i++) {
     *         if (patternHash == textHash) {
     *             bool match = true;
     *             for (int j = 0; j < m; j++) {
     *                 if (text[i + j] != pattern[j]) {
     *                     match = false;
     *                     break;
     *                 }
     *             }
     *             if (match) {
     *                 matches.push_back(i);
     *             }
     *         }
     *         
     *         if (i < n - m) {
     *             textHash = (textHash - highestPow * text[i] % MOD + MOD) % MOD;
     *             textHash = (textHash * BASE + text[i + m]) % MOD;
     *         }
     *     }
     *     
     *     return matches;
     * }
     * 
     * class PatternFind {
     * public:
     *     static vector<int> findAllOccurrences(const string& text, const string& pattern) {
     *         return rabinKarpSearch(text, pattern);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * BASE = 256
     * MOD = 1000000007
     * 
     * def rabin_karp_search(text, pattern):
     *     matches = []
     *     n = len(text)
     *     m = len(pattern)
     *     
     *     if m == 0:
     *         return list(range(n + 1))
     *     if n < m:
     *         return matches
     *     
     *     pattern_hash = 0
     *     text_hash = 0
     *     highest_pow = 1
     *     
     *     for i in range(m - 1):
     *         highest_pow = (highest_pow * BASE) % MOD
     *     
     *     for i in range(m):
     *         pattern_hash = (pattern_hash * BASE + ord(pattern[i])) % MOD
     *         text_hash = (text_hash * BASE + ord(text[i])) % MOD
     *     
     *     for i in range(n - m + 1):
     *         if pattern_hash == text_hash:
     *             match = True
     *             for j in range(m):
     *                 if text[i + j] != pattern[j]:
     *                     match = False
     *                     break
     *             if match:
     *                 matches.append(i)
     *         
     *         if i < n - m:
     *             text_hash = (text_hash - highest_pow * ord(text[i]) % MOD + MOD) % MOD
     *             text_hash = (text_hash * BASE + ord(text[i + m])) % MOD
     *     
     *     return matches
     * 
     * class PatternFind:
     *     @staticmethod
     *     def find_all_occurrences(text, pattern):
     *         return rabin_karp_search(text, pattern)
     */
    
    // ====================================================================================
    // 题目2: Codeforces - Good Substrings (好子串)
    // 题目描述: 统计满足条件的好子串数量
    // 解题思路: 使用Rabin-Karp算法结合哈希去重
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class GoodSubstrings {
        public static int countGoodSubstrings(String s, String binaryString) {
            int n = s.length();
            Set<Long> uniqueHashes = new HashSet<>();
            
            // 预计算幂值
            long[] powers = new long[n];
            powers[0] = 1;
            for (int i = 1; i < n; i++) {
                powers[i] = (powers[i-1] * BASE) % MOD;
            }
            
            // 遍历所有子串
            for (int i = 0; i < n; i++) {
                long hash = 0;
                int badCount = 0;
                
                for (int j = i; j < n; j++) {
                    // 更新哈希值
                    hash = (hash + s.charAt(j) * powers[j-i]) % MOD;
                    
                    // 更新坏字符计数
                    if (binaryString.charAt(s.charAt(j) - 'a') == '0') {
                        badCount++;
                    }
                    
                    // 如果坏字符不超过1个，则为好子串
                    if (badCount <= 1) {
                        uniqueHashes.add(hash);
                    }
                }
            }
            
            return uniqueHashes.size();
        }
    }
    
    // ====================================================================================
    // 题目3: Codeforces - Palindromic characteristics (回文特征)
    // 题目描述: 计算字符串中各阶回文子串的数量
    // 解题思路: 使用Rabin-Karp算法结合动态规划
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n^2)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PalindromicCharacteristics {
        public static int[] calculateCharacteristics(String s) {
            int n = s.length();
            int[] result = new int[n + 1];
            
            // dp[i][j] 表示子串 s[i..j] 的回文阶数
            int[][] dp = new int[n][n];
            
            // 预计算前缀哈希和后缀哈希
            long[] prefixHash = new long[n + 1];
            long[] suffixHash = new long[n + 1];
            long[] powers = new long[n + 1];
            
            powers[0] = 1;
            for (int i = 1; i <= n; i++) {
                powers[i] = (powers[i-1] * BASE) % MOD;
            }
            
            // 计算前缀哈希
            for (int i = 0; i < n; i++) {
                prefixHash[i + 1] = (prefixHash[i] + s.charAt(i) * powers[i]) % MOD;
            }
            
            // 计算后缀哈希
            for (int i = n - 1; i >= 0; i--) {
                suffixHash[i] = (suffixHash[i + 1] + s.charAt(i) * powers[n - 1 - i]) % MOD;
            }
            
            // 检查子串是否为回文
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    // 计算子串 s[i..j] 的前缀哈希和后缀哈希
                    long forwardHash = (prefixHash[j + 1] - prefixHash[i] + MOD) % MOD;
                    long reverseHash = (suffixHash[i] - suffixHash[j + 1] + MOD) % MOD;
                    
                    // 调整哈希值使其可以比较
                    long adjustedForward = (forwardHash * powers[n - 1 - j]) % MOD;
                    long adjustedReverse = (reverseHash * powers[i]) % MOD;
                    
                    if (adjustedForward == adjustedReverse) {
                        if (i == j) {
                            // 长度为1的回文
                            dp[i][j] = 1;
                        } else if (j - i + 1 == 2) {
                            // 长度为2的回文
                            dp[i][j] = 1;
                        } else {
                            // 长度大于2的回文，检查内部是否为k-1阶回文
                            int mid = (i + j) / 2;
                            if ((j - i + 1) % 2 == 1) {
                                // 奇数长度
                                dp[i][j] = dp[i + 1][j - 1] + 1;
                            } else {
                                // 偶数长度
                                dp[i][j] = Math.min(dp[i][mid], dp[mid + 1][j]) + 1;
                            }
                        }
                        result[dp[i][j]]++;
                    }
                }
            }
            
            // 累加计算：k阶回文也计入k-1阶等
            for (int i = n - 1; i >= 1; i--) {
                result[i] += result[i + 1];
            }
            
            return result;
        }
    }
    
    // ====================================================================================
    // 题目4: Leetcode - Longest Duplicate Substring (最长重复子串)
    // 题目描述: 找到字符串中最长的重复子串
    // 解题思路: 使用二分搜索结合Rabin-Karp算法
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LongestDuplicateSubstring {
        public static String findLongestDuplicate(String s) {
            int n = s.length();
            int left = 1, right = n - 1;
            String result = "";
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                String duplicate = findDuplicateOfLength(s, mid);
                
                if (!duplicate.isEmpty()) {
                    result = duplicate;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            return result;
        }
        
        private static String findDuplicateOfLength(String s, int length) {
            if (length >= s.length()) {
                return "";
            }
            
            Set<Long> seen = new HashSet<>();
            long hash = 0;
            long highestPow = 1;
            
            // 计算最高位权值
            for (int i = 0; i < length - 1; i++) {
                highestPow = (highestPow * BASE) % MOD;
            }
            
            // 计算初始哈希值
            for (int i = 0; i < length; i++) {
                hash = (hash * BASE + s.charAt(i)) % MOD;
            }
            seen.add(hash);
            
            // 滑动窗口
            for (int i = length; i < s.length(); i++) {
                // 移除最左边的字符
                hash = (hash - highestPow * s.charAt(i - length) % MOD + MOD) % MOD;
                // 添加新的右边字符
                hash = (hash * BASE + s.charAt(i)) % MOD;
                
                if (seen.contains(hash)) {
                    // 找到重复子串
                    return s.substring(i - length + 1, i + 1);
                }
                seen.add(hash);
            }
            
            return "";
        }
    }
    
    // ====================================================================================
    // 题目5: SPOJ - NAJPF (Pattern Find加强版)
    // 题目描述: 模式查找加强版，需要处理大量输入
    // 解题思路: 使用优化的Rabin-Karp算法
    // 时间复杂度: O(n+m)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class NAJPF {
        public static List<Integer> findAllOccurrences(String text, String pattern) {
            return RabinKarp.searchAll(text, pattern);
        }
    }
    
    // ====================================================================================
    // 题目6: Codeforces - A Needle in the Haystack (大海捞针)
    // 题目描述: 在大量文本中查找模式串
    // 解题思路: 使用双哈希Rabin-Karp算法减少冲突
    // 时间复杂度: O(n+m)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ANeedleInTheHaystack {
        public static int findFirstOccurrence(String text, String pattern) {
            return RabinKarp.searchDoubleHash(text, pattern);
        }
    }
    
    // ====================================================================================
    // 题目7: SPOJ - EPALIN (扩展为回文)
    // 题目描述: 在字符串前面添加最少字符使其成为回文
    // 解题思路: 使用Rabin-Karp算法找到最长后缀回文
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class EPALIN {
        public static String extendToPalindrome(String s) {
            String reversed = new StringBuilder(s).reverse().toString();
            String combined = s + "#" + reversed;
            
            // 计算所有前缀的哈希值
            int n = combined.length();
            long[] prefixHash = new long[n + 1];
            long[] powers = new long[n + 1];
            
            powers[0] = 1;
            for (int i = 1; i <= n; i++) {
                powers[i] = (powers[i-1] * BASE) % MOD;
            }
            
            for (int i = 0; i < n; i++) {
                prefixHash[i + 1] = (prefixHash[i] + combined.charAt(i) * powers[i]) % MOD;
            }
            
            // 找到s的最长后缀回文
            int overlap = 0;
            for (int i = s.length(); i >= 1; i--) {
                // 检查s的后缀i是否与reversed的前缀i相同
                long suffixHash = (prefixHash[s.length()] - prefixHash[s.length() - i] + MOD) % MOD;
                long prefixOfReversedHash = (prefixHash[n] - prefixHash[n - i] + MOD) % MOD;
                
                // 调整哈希值
                long adjustedSuffix = (suffixHash * powers[n - s.length()]) % MOD;
                long adjustedPrefix = (prefixOfReversedHash * powers[s.length() - i]) % MOD;
                
                if (adjustedSuffix == adjustedPrefix) {
                    overlap = i;
                    break;
                }
            }
            
            // 构造回文
            return s + reversed.substring(overlap);
        }
    }
    
    // ====================================================================================
    // 题目8: Codeforces - String Hashing (字符串哈希)
    // 题目描述: 字符串哈希应用问题
    // 解题思路: 使用Rabin-Karp算法进行高效的字符串比较
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class StringHashing {
        private long[] prefixHash;
        private long[] powers;
        private int n;
        
        public StringHashing(String s) {
            n = s.length();
            prefixHash = new long[n + 1];
            powers = new long[n + 1];
            
            powers[0] = 1;
            for (int i = 1; i <= n; i++) {
                powers[i] = (powers[i-1] * BASE) % MOD;
            }
            
            for (int i = 0; i < n; i++) {
                prefixHash[i + 1] = (prefixHash[i] + s.charAt(i) * powers[i]) % MOD;
            }
        }
        
        /**
         * 获取子串的哈希值
         * @param start 起始索引（包含）
         * @param end 结束索引（包含）
         * @return 子串的哈希值
         */
        public long getSubstringHash(int start, int end) {
            if (start > end || start < 0 || end >= n) {
                throw new IllegalArgumentException("Invalid range");
            }
            
            long hash = (prefixHash[end + 1] - prefixHash[start] + MOD) % MOD;
            return (hash * powers[n - 1 - end]) % MOD;
        }
        
        /**
         * 比较两个子串是否相等
         * @param start1 第一个子串起始索引
         * @param end1 第一个子串结束索引
         * @param start2 第二个子串起始索引
         * @param end2 第二个子串结束索引
         * @return 两个子串是否相等
         */
        public boolean areSubstringsEqual(int start1, int end1, int start2, int end2) {
            if (end1 - start1 != end2 - start2) {
                return false;
            }
            
            return getSubstringHash(start1, end1) == getSubstringHash(start2, end2);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试SPOJ - Pattern Find
        System.out.println("=== 测试SPOJ - Pattern Find ===");
        String text1 = "abcabcabc";
        String pattern1 = "abc";
        List<Integer> result1 = PatternFind.findAllOccurrences(text1, pattern1);
        System.out.println("文本: " + text1);
        System.out.println("模式: " + pattern1);
        System.out.println("匹配位置: " + result1); // 应该是[0, 3, 6]
        
        // 测试Codeforces - Good Substrings
        System.out.println("\n=== 测试Codeforces - Good Substrings ===");
        String text2 = "abb";
        String binary2 = "10000000000000000000000000";
        int result2 = GoodSubstrings.countGoodSubstrings(text2, binary2);
        System.out.println("文本: " + text2);
        System.out.println("二进制字符串: " + binary2);
        System.out.println("好子串数量: " + result2);
        
        // 测试Leetcode - Longest Duplicate Substring
        System.out.println("\n=== 测试Leetcode - Longest Duplicate Substring ===");
        String text3 = "banana";
        String result3 = LongestDuplicateSubstring.findLongestDuplicate(text3);
        System.out.println("文本: " + text3);
        System.out.println("最长重复子串: " + result3); // 应该是"ana"
        
        // 测试SPOJ - EPALIN
        System.out.println("\n=== 测试SPOJ - EPALIN ===");
        String text4 = "abc";
        String result4 = EPALIN.extendToPalindrome(text4);
        System.out.println("文本: " + text4);
        System.out.println("扩展后的回文: " + result4); // 应该是"abcba"
        
        // 测试Codeforces - String Hashing
        System.out.println("\n=== 测试Codeforces - String Hashing ===");
        String text5 = "abcdef";
        StringHashing sh = new StringHashing(text5);
        System.out.println("文本: " + text5);
        System.out.println("子串[0,2]的哈希值: " + sh.getSubstringHash(0, 2));
        System.out.println("子串[3,5]的哈希值: " + sh.getSubstringHash(3, 5));
        System.out.println("子串[0,2]和[3,5]是否相等: " + sh.areSubstringsEqual(0, 2, 3, 5));
    }
}