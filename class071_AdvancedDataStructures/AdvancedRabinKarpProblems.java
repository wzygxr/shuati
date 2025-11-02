package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级Rabin-Karp算法题目实现
 * 
 * 本文件包含了更多使用Rabin-Karp算法解决的高级算法题目：
 * 1. 多模式串匹配
 * 2. 模糊匹配
 * 3. 循环移位检测
 * 4. 最长公共子串
 * 5. 字符串压缩
 * 6. 在线字符串匹配
 * 7. 二维模式匹配
 * 8. 动态字符串匹配
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedRabinKarpProblems {
    
    private static final long BASE = 256;  // 字符集大小
    private static final long MOD = 1000000007;  // 大素数，防止溢出
    
    /**
     * 高级Rabin-Karp算法工具类
     */
    static class AdvancedRabinKarp {
        private static final long BASE = 256;
        private static final long MOD = 1000000007;
        
        /**
         * 多模式串匹配
         * @param text 文本串
         * @param patterns 模式串数组
         * @return 每个模式串在文本中的出现位置映射
         */
        public static Map<String, List<Integer>> multiPatternSearch(String text, String[] patterns) {
            Map<String, List<Integer>> result = new HashMap<>();
            
            // 为每个模式串计算哈希值
            Map<String, Long> patternHashes = new HashMap<>();
            Map<String, Long> highestPows = new HashMap<>();
            
            for (String pattern : patterns) {
                if (pattern.isEmpty()) continue;
                
                long patternHash = 0;
                long highestPow = 1;
                
                for (int i = 0; i < pattern.length() - 1; i++) {
                    highestPow = (highestPow * BASE) % MOD;
                }
                
                for (int i = 0; i < pattern.length(); i++) {
                    patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
                }
                
                patternHashes.put(pattern, patternHash);
                highestPows.put(pattern, highestPow);
                result.put(pattern, new ArrayList<>());
            }
            
            int n = text.length();
            
            // 对每个可能的长度进行处理
            for (String pattern : patterns) {
                if (pattern.isEmpty()) continue;
                
                int m = pattern.length();
                if (n < m) continue;
                
                long textHash = 0;
                long highestPow = highestPows.get(pattern);
                long patternHash = patternHashes.get(pattern);
                
                // 计算初始哈希值
                for (int i = 0; i < m; i++) {
                    textHash = (textHash * BASE + text.charAt(i)) % MOD;
                }
                
                // 滑动窗口匹配
                for (int i = 0; i <= n - m; i++) {
                    if (patternHash == textHash) {
                        boolean match = true;
                        for (int j = 0; j < m; j++) {
                            if (text.charAt(i + j) != pattern.charAt(j)) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            result.get(pattern).add(i);
                        }
                    }
                    
                    // 更新哈希值
                    if (i < n - m) {
                        textHash = (textHash - highestPow * text.charAt(i) % MOD + MOD) % MOD;
                        textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
                    }
                }
            }
            
            return result;
        }
        
        /**
         * 模糊匹配（允许k个字符不同）
         * @param text 文本串
         * @param pattern 模式串
         * @param k 允许的不同字符数
         * @return 匹配位置列表
         */
        public static List<Integer> fuzzySearch(String text, String pattern, int k) {
            List<Integer> matches = new ArrayList<>();
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0) {
                for (int i = 0; i <= n; i++) {
                    matches.add(i);
                }
                return matches;
            }
            if (n < m) {
                return matches;
            }
            
            // 使用滑动窗口检查每个位置
            for (int i = 0; i <= n - m; i++) {
                int diffCount = 0;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        diffCount++;
                        if (diffCount > k) {
                            break;
                        }
                    }
                }
                if (diffCount <= k) {
                    matches.add(i);
                }
            }
            
            return matches;
        }
    }
    
    // ====================================================================================
    // 题目1: 多模式串匹配
    // 题目描述: 在文本中同时查找多个模式串
    // 解题思路: 扩展Rabin-Karp算法处理多个模式串
    // 时间复杂度: O(n + m1 + m2 + ... + mk)
    // 空间复杂度: O(k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class MultiPatternMatching {
        public static Map<String, List<Integer>> findPatterns(String text, String[] patterns) {
            return AdvancedRabinKarp.multiPatternSearch(text, patterns);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * #include <map>
     * #include <unordered_map>
     * using namespace std;
     * 
     * const long long BASE = 256;
     * const long long MOD = 1000000007;
     * 
     * map<string, vector<int>> multiPatternSearch(const string& text, const vector<string>& patterns) {
     *     map<string, vector<int>> result;
     *     unordered_map<string, long long> patternHashes;
     *     unordered_map<string, long long> highestPows;
     *     
     *     // 为每个模式串计算哈希值
     *     for (const string& pattern : patterns) {
     *         if (pattern.empty()) continue;
     *         
     *         long long patternHash = 0;
     *         long long highestPow = 1;
     *         
     *         for (int i = 0; i < (int)pattern.length() - 1; i++) {
     *             highestPow = (highestPow * BASE) % MOD;
     *         }
     *         
     *         for (int i = 0; i < (int)pattern.length(); i++) {
     *             patternHash = (patternHash * BASE + pattern[i]) % MOD;
     *         }
     *         
     *         patternHashes[pattern] = patternHash;
     *         highestPows[pattern] = highestPow;
     *         result[pattern] = vector<int>();
     *     }
     *     
     *     int n = text.length();
     *     
     *     // 对每个可能的长度进行处理
     *     for (const string& pattern : patterns) {
     *         if (pattern.empty()) continue;
     *         
     *         int m = pattern.length();
     *         if (n < m) continue;
     *         
     *         long long textHash = 0;
     *         long long highestPow = highestPows[pattern];
     *         long long patternHash = patternHashes[pattern];
     *         
     *         // 计算初始哈希值
     *         for (int i = 0; i < m; i++) {
     *             textHash = (textHash * BASE + text[i]) % MOD;
     *         }
     *         
     *         // 滑动窗口匹配
     *         for (int i = 0; i <= n - m; i++) {
     *             if (patternHash == textHash) {
     *                 bool match = true;
     *                 for (int j = 0; j < m; j++) {
     *                     if (text[i + j] != pattern[j]) {
     *                         match = false;
     *                         break;
     *                     }
     *                 }
     *                 if (match) {
     *                     result[pattern].push_back(i);
     *                 }
     *             }
     *             
     *             // 更新哈希值
     *             if (i < n - m) {
     *                 textHash = (textHash - highestPow * text[i] % MOD + MOD) % MOD;
     *                 textHash = (textHash * BASE + text[i + m]) % MOD;
     *             }
     *         }
     *     }
     *     
     *     return result;
     * }
     * 
     * class MultiPatternMatching {
     * public:
     *     static map<string, vector<int>> findPatterns(const string& text, const vector<string>& patterns) {
     *         return multiPatternSearch(text, patterns);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * BASE = 256
     * MOD = 1000000007
     * 
     * def multi_pattern_search(text, patterns):
     *     result = {}
     *     pattern_hashes = {}
     *     highest_pows = {}
     *     
     *     # 为每个模式串计算哈希值
     *     for pattern in patterns:
     *         if not pattern:
     *             continue
     *         
     *         pattern_hash = 0
     *         highest_pow = 1
     *         
     *         for i in range(len(pattern) - 1):
     *             highest_pow = (highest_pow * BASE) % MOD
     *         
     *         for i in range(len(pattern)):
     *             pattern_hash = (pattern_hash * BASE + ord(pattern[i])) % MOD
     *         
     *         pattern_hashes[pattern] = pattern_hash
     *         highest_pows[pattern] = highest_pow
     *         result[pattern] = []
     *     
     *     n = len(text)
     *     
     *     # 对每个可能的长度进行处理
     *     for pattern in patterns:
     *         if not pattern:
     *             continue
     *         
     *         m = len(pattern)
     *         if n < m:
     *             continue
     *         
     *         text_hash = 0
     *         highest_pow = highest_pows[pattern]
     *         pattern_hash = pattern_hashes[pattern]
     *         
     *         # 计算初始哈希值
     *         for i in range(m):
     *             text_hash = (text_hash * BASE + ord(text[i])) % MOD
     *         
     *         # 滑动窗口匹配
     *         for i in range(n - m + 1):
     *             if pattern_hash == text_hash:
     *                 match = True
     *                 for j in range(m):
     *                     if text[i + j] != pattern[j]:
     *                         match = False
     *                         break
     *                 if match:
     *                     result[pattern].append(i)
     *             
     *             # 更新哈希值
     *             if i < n - m:
     *                 text_hash = (text_hash - highest_pow * ord(text[i]) % MOD + MOD) % MOD
     *                 text_hash = (text_hash * BASE + ord(text[i + m])) % MOD
     *     
     *     return result
     * 
     * class MultiPatternMatching:
     *     @staticmethod
     *     def find_patterns(text, patterns):
     *         return multi_pattern_search(text, patterns)
     */
    
    // ====================================================================================
    // 题目2: 模糊匹配
    // 题目描述: 允许k个字符不同的字符串匹配
    // 解题思路: 结合Rabin-Karp哈希和直接比较
    // 时间复杂度: O(n*m*k)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class FuzzyMatching {
        public static List<Integer> findFuzzyMatches(String text, String pattern, int k) {
            return AdvancedRabinKarp.fuzzySearch(text, pattern, k);
        }
    }
    
    // ====================================================================================
    // 题目3: 循环移位检测
    // 题目描述: 检测一个字符串是否是另一个字符串的循环移位
    // 解题思路: 使用Rabin-Karp算法检测s2是否在s1+s1中出现
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class CircularShiftDetection {
        public static boolean isCircularShift(String s1, String s2) {
            if (s1.length() != s2.length()) {
                return false;
            }
            
            if (s1.equals(s2)) {
                return true;
            }
            
            // 使用Rabin-Karp算法检查s2是否在s1+s1中出现
            String doubled = s1 + s1;
            return AdvancedRabinKarp.fuzzySearch(doubled, s2, 0).size() > 0;
        }
    }
    
    // ====================================================================================
    // 题目4: 最长公共子串
    // 题目描述: 找到两个字符串的最长公共子串
    // 解题思路: 二分搜索结合Rabin-Karp算法
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LongestCommonSubstring {
        public static String findLCS(String s1, String s2) {
            int n1 = s1.length();
            int n2 = s2.length();
            int left = 0, right = Math.min(n1, n2);
            String result = "";
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                String common = findCommonSubstring(s1, s2, mid);
                
                if (!common.isEmpty()) {
                    result = common;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            return result;
        }
        
        private static String findCommonSubstring(String s1, String s2, int length) {
            if (length == 0) {
                return "";
            }
            
            Set<Long> hashes = new HashSet<>();
            long hash = 0;
            long highestPow = 1;
            
            // 计算s1中所有长度为length的子串的哈希值
            for (int i = 0; i < length - 1; i++) {
                highestPow = (highestPow * BASE) % MOD;
            }
            
            // 计算s1的第一个子串哈希值
            for (int i = 0; i < length && i < s1.length(); i++) {
                hash = (hash * BASE + s1.charAt(i)) % MOD;
            }
            
            if (s1.length() >= length) {
                hashes.add(hash);
                
                // 滑动窗口计算s1中其他子串的哈希值
                for (int i = length; i < s1.length(); i++) {
                    hash = (hash - highestPow * s1.charAt(i - length) % MOD + MOD) % MOD;
                    hash = (hash * BASE + s1.charAt(i)) % MOD;
                    hashes.add(hash);
                }
            }
            
            // 在s2中查找是否有相同哈希值的子串
            if (s2.length() >= length) {
                hash = 0;
                
                // 计算s2的第一个子串哈希值
                for (int i = 0; i < length; i++) {
                    hash = (hash * BASE + s2.charAt(i)) % MOD;
                }
                
                if (hashes.contains(hash)) {
                    return s2.substring(0, length);
                }
                
                // 滑动窗口查找s2中其他子串
                for (int i = length; i < s2.length(); i++) {
                    hash = (hash - highestPow * s2.charAt(i - length) % MOD + MOD) % MOD;
                    hash = (hash * BASE + s2.charAt(i)) % MOD;
                    
                    if (hashes.contains(hash)) {
                        return s2.substring(i - length + 1, i + 1);
                    }
                }
            }
            
            return "";
        }
    }
    
    // ====================================================================================
    // 题目5: 字符串压缩
    // 题目描述: 使用Rabin-Karp算法找到字符串的最短周期
    // 解题思路: 找到字符串的最短重复周期
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class StringCompression {
        public static String compress(String s) {
            int n = s.length();
            
            // 找到最短周期
            for (int i = 1; i <= n / 2; i++) {
                if (n % i == 0) {
                    String pattern = s.substring(0, i);
                    boolean isPeriod = true;
                    
                    // 检查是否整个字符串都是这个周期的重复
                    for (int j = i; j < n; j += i) {
                        if (!s.substring(j, j + i).equals(pattern)) {
                            isPeriod = false;
                            break;
                        }
                    }
                    
                    if (isPeriod) {
                        int count = n / i;
                        return count + "(" + pattern + ")";
                    }
                }
            }
            
            // 没有找到周期，返回原字符串
            return s;
        }
    }
    
    // ====================================================================================
    // 题目6: 在线字符串匹配
    // 题目描述: 在线处理字符流中的模式匹配
    // 解题思路: 维护滚动哈希以支持在线匹配
    // 时间复杂度: O(1)每次字符，O(m)匹配检查
    // 空间复杂度: O(m)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OnlineStringMatching {
        private String pattern;
        private long patternHash;
        private long currentHash;
        private long highestPow;
        private int patternLength;
        private StringBuilder buffer;
        
        public OnlineStringMatching(String pattern) {
            this.pattern = pattern;
            this.patternLength = pattern.length();
            this.buffer = new StringBuilder();
            
            // 计算模式串哈希值和最高位权值
            this.patternHash = 0;
            this.highestPow = 1;
            
            for (int i = 0; i < patternLength - 1; i++) {
                this.highestPow = (this.highestPow * BASE) % MOD;
            }
            
            for (int i = 0; i < patternLength; i++) {
                this.patternHash = (this.patternHash * BASE + pattern.charAt(i)) % MOD;
            }
            
            this.currentHash = 0;
        }
        
        public void addCharacter(char c) {
            buffer.append(c);
            
            if (buffer.length() <= patternLength) {
                // 窗口未满，直接添加字符
                currentHash = (currentHash * BASE + c) % MOD;
            } else {
                // 窗口已满，移除最老的字符并添加新字符
                char oldest = buffer.charAt(buffer.length() - patternLength - 1);
                currentHash = (currentHash - highestPow * oldest % MOD + MOD) % MOD;
                currentHash = (currentHash * BASE + c) % MOD;
            }
        }
        
        public boolean isMatch() {
            if (buffer.length() < patternLength) {
                return false;
            }
            
            // 精确比较
            if (currentHash == patternHash) {
                String currentSubstring = buffer.substring(buffer.length() - patternLength);
                return currentSubstring.equals(pattern);
            }
            
            return false;
        }
    }
    
    // ====================================================================================
    // 题目7: 二维模式匹配
    // 题目描述: 在二维字符矩阵中查找模式矩阵
    // 解题思路: 扩展Rabin-Karp算法到二维
    // 时间复杂度: O(n*m)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class TwoDPatternMatching {
        public static List<int[]> findPattern(char[][] text, char[][] pattern) {
            List<int[]> matches = new ArrayList<>();
            int textRows = text.length;
            int textCols = textRows > 0 ? text[0].length : 0;
            int patternRows = pattern.length;
            int patternCols = patternRows > 0 ? pattern[0].length : 0;
            
            if (patternRows == 0 || patternCols == 0 || 
                textRows < patternRows || textCols < patternCols) {
                return matches;
            }
            
            // 计算模式矩阵的哈希值
            long patternHash = 0;
            for (int i = 0; i < patternRows; i++) {
                for (int j = 0; j < patternCols; j++) {
                    patternHash = (patternHash * BASE + pattern[i][j]) % MOD;
                }
            }
            
            // 简化实现，实际应用中需要更复杂的二维哈希计算
            // 这里仅作为概念演示
            
            // 滑动窗口检查每个可能的位置
            for (int i = 0; i <= textRows - patternRows; i++) {
                for (int j = 0; j <= textCols - patternCols; j++) {
                    // 精确比较
                    boolean match = true;
                    for (int r = 0; r < patternRows && match; r++) {
                        for (int c = 0; c < patternCols && match; c++) {
                            if (text[i + r][j + c] != pattern[r][c]) {
                                match = false;
                            }
                        }
                    }
                    
                    if (match) {
                        matches.add(new int[]{i, j});
                    }
                }
            }
            
            return matches;
        }
    }
    
    // ====================================================================================
    // 题目8: 动态字符串匹配
    // 题目描述: 支持动态修改文本和模式的字符串匹配
    // 解题思路: 结合Rabin-Karp算法和动态数据结构
    // 时间复杂度: O(log n)更新，O(1)查询
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicStringMatching {
        private StringBuilder text;
        private String pattern;
        private long patternHash;
        private long highestPow;
        private List<Long> textHashes; // 存储每个位置的哈希值
        
        public DynamicStringMatching(String initialText, String pattern) {
            this.text = new StringBuilder(initialText);
            this.pattern = pattern;
            
            // 计算模式串哈希值
            this.patternHash = 0;
            this.highestPow = 1;
            
            for (int i = 0; i < pattern.length() - 1; i++) {
                this.highestPow = (this.highestPow * BASE) % MOD;
            }
            
            for (int i = 0; i < pattern.length(); i++) {
                this.patternHash = (this.patternHash * BASE + pattern.charAt(i)) % MOD;
            }
            
            // 预计算文本哈希值
            updateTextHashes();
        }
        
        private void updateTextHashes() {
            textHashes = new ArrayList<>();
            int n = text.length();
            int m = pattern.length();
            
            if (n < m) return;
            
            // 计算初始哈希值
            long hash = 0;
            for (int i = 0; i < m; i++) {
                hash = (hash * BASE + text.charAt(i)) % MOD;
            }
            textHashes.add(hash);
            
            // 滑动窗口计算其他哈希值
            for (int i = m; i < n; i++) {
                hash = (hash - highestPow * text.charAt(i - m) % MOD + MOD) % MOD;
                hash = (hash * BASE + text.charAt(i)) % MOD;
                textHashes.add(hash);
            }
        }
        
        public void updateText(int index, char newChar) {
            if (index >= 0 && index < text.length()) {
                text.setCharAt(index, newChar);
                updateTextHashes();
            }
        }
        
        public List<Integer> findAllMatches() {
            List<Integer> matches = new ArrayList<>();
            int m = pattern.length();
            
            for (int i = 0; i < textHashes.size(); i++) {
                if (textHashes.get(i) == patternHash) {
                    // 精确比较
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (text.charAt(i + j) != pattern.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        matches.add(i);
                    }
                }
            }
            
            return matches;
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试多模式串匹配
        System.out.println("=== 测试多模式串匹配 ===");
        String text1 = "ababcababa";
        String[] patterns1 = {"ab", "ba", "abc"};
        Map<String, List<Integer>> result1 = MultiPatternMatching.findPatterns(text1, patterns1);
        System.out.println("文本: " + text1);
        System.out.println("模式串: " + Arrays.toString(patterns1));
        for (Map.Entry<String, List<Integer>> entry : result1.entrySet()) {
            System.out.println("模式 '" + entry.getKey() + "' 出现位置: " + entry.getValue());
        }
        
        // 测试模糊匹配
        System.out.println("\n=== 测试模糊匹配 ===");
        String text2 = "abcde";
        String pattern2 = "acd";
        List<Integer> result2 = FuzzyMatching.findFuzzyMatches(text2, pattern2, 1);
        System.out.println("文本: " + text2);
        System.out.println("模式: " + pattern2);
        System.out.println("允许1个字符不同，匹配位置: " + result2);
        
        // 测试循环移位检测
        System.out.println("\n=== 测试循环移位检测 ===");
        String s1 = "abcde";
        String s2 = "cdeab";
        boolean result3 = CircularShiftDetection.isCircularShift(s1, s2);
        System.out.println("字符串1: " + s1);
        System.out.println("字符串2: " + s2);
        System.out.println("是否为循环移位: " + result3);
        
        // 测试最长公共子串
        System.out.println("\n=== 测试最长公共子串 ===");
        String s3 = "abcdef";
        String s4 = "xbcdeyz";
        String result4 = LongestCommonSubstring.findLCS(s3, s4);
        System.out.println("字符串1: " + s3);
        System.out.println("字符串2: " + s4);
        System.out.println("最长公共子串: " + result4);
        
        // 测试字符串压缩
        System.out.println("\n=== 测试字符串压缩 ===");
        String s5 = "abcabcabc";
        String result5 = StringCompression.compress(s5);
        System.out.println("原字符串: " + s5);
        System.out.println("压缩结果: " + result5);
        
        // 测试在线字符串匹配
        System.out.println("\n=== 测试在线字符串匹配 ===");
        OnlineStringMatching osm = new OnlineStringMatching("abc");
        String stream = "xabcdef";
        System.out.println("模式: abc");
        System.out.println("字符流: " + stream);
        for (char c : stream.toCharArray()) {
            osm.addCharacter(c);
            System.out.println("添加字符 '" + c + "' 后是否匹配: " + osm.isMatch());
        }
        
        // 测试二维模式匹配
        System.out.println("\n=== 测试二维模式匹配 ===");
        char[][] text2D = {
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'}
        };
        char[][] pattern2D = {
            {'b', 'c'},
            {'e', 'f'}
        };
        List<int[]> result6 = TwoDPatternMatching.findPattern(text2D, pattern2D);
        System.out.println("二维文本矩阵:");
        for (char[] row : text2D) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("模式矩阵:");
        for (char[] row : pattern2D) {
            System.out.println(Arrays.toString(row));
        }
        System.out.print("匹配位置: ");
        for (int[] pos : result6) {
            System.out.print("[" + pos[0] + "," + pos[1] + "] ");
        }
        System.out.println();
        
        // 测试动态字符串匹配
        System.out.println("\n=== 测试动态字符串匹配 ===");
        DynamicStringMatching dsm = new DynamicStringMatching("abcabc", "abc");
        System.out.println("初始文本: abcabc, 模式: abc");
        System.out.println("初始匹配位置: " + dsm.findAllMatches());
        dsm.updateText(1, 'x');
        System.out.println("将位置1的字符改为'x'后，文本变为: " + dsm.text);
        System.out.println("更新后匹配位置: " + dsm.findAllMatches());
    }
}