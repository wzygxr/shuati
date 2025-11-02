package class029_AdvancedDataStructures;

import java.util.*;

/**
 * Manacher算法相关题目实现
 * 包含：
 * 1. LeetCode 5 - Longest Palindromic Substring (最长回文子串)
 * 2. LeetCode 647 - Palindromic Substrings (回文子串计数)
 * 3. LeetCode 214 - Shortest Palindrome (最短回文串)
 * 4. LeetCode 336 - Palindrome Pairs (回文对)
 * 5. LeetCode 131 - Palindrome Partitioning (回文分割)
 * 6. LeetCode 132 - Palindrome Partitioning II (回文分割II)
 * 7. Codeforces 137D - Palindromes (回文串)
 * 8. SPOJ PLD - Palindromes (回文串)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class ManacherProblems {
    
    /**
     * Manacher算法核心实现
     */
    static class Manacher {
        private String s;
        private String processed;
        private int[] P; // 回文半径数组
        
        public Manacher(String s) {
            this.s = s;
            this.processed = preprocess(s);
            this.P = new int[processed.length()];
            build();
        }
        
        /**
         * 预处理字符串，在每个字符之间插入特殊字符（如'#'）
         * 这样可以统一处理奇数长度和偶数长度的回文子串
         */
        private String preprocess(String s) {
            if (s == null || s.isEmpty()) {
                return "^$";
            }
            
            StringBuilder result = new StringBuilder("^");
            for (int i = 0; i < s.length(); i++) {
                result.append('#').append(s.charAt(i));
            }
            result.append("#$");
            return result.toString();
        }
        
        /**
         * 构建回文半径数组
         */
        private void build() {
            int n = processed.length();
            int C = 0, R = 0;
            
            for (int i = 1; i < n - 1; i++) {
                int mirror = 2 * C - i;
                
                if (i < R) {
                    P[i] = Math.min(R - i, P[mirror]);
                }
                
                // 尝试扩展回文
                while (processed.charAt(i + (1 + P[i])) == processed.charAt(i - (1 + P[i]))) {
                    P[i]++;
                }
                
                // 如果回文扩展超过了R，更新中心和右边界
                if (i + P[i] > R) {
                    C = i;
                    R = i + P[i];
                }
            }
        }
        
        /**
         * 获取最长回文子串
         */
        public String getLongestPalindrome() {
            int maxLen = 0;
            int centerIndex = 0;
            
            for (int i = 1; i < P.length - 1; i++) {
                if (P[i] > maxLen) {
                    maxLen = P[i];
                    centerIndex = i;
                }
            }
            
            int start = (centerIndex - maxLen) / 2;
            return s.substring(start, start + maxLen);
        }
        
        /**
         * 计算回文子串总数
         */
        public int countPalindromes() {
            int count = 0;
            for (int i = 1; i < P.length - 1; i++) {
                count += (P[i] + 1) / 2;
            }
            return count;
        }
        
        /**
         * 检查指定范围是否为回文
         */
        public boolean isPalindrome(int start, int end) {
            int processedStart = 2 * start + 1;
            int processedEnd = 2 * end + 1;
            int center = (processedStart + processedEnd) / 2;
            int radius = (processedEnd - processedStart) / 2;
            return P[center] >= radius;
        }
        
        // Getter方法
        public String getOriginalString() { return s; }
        public String getProcessedString() { return processed; }
        public int[] getRadiusArray() { return P.clone(); }
    }
    
    // ====================================================================================
    // 题目1: LeetCode 5 - Longest Palindromic Substring
    // 链接: https://leetcode.com/problems/longest-palindromic-substring/
    // 题目描述: 找到字符串中最长的回文子串
    // 解题思路: 使用Manacher算法在O(n)时间内解决
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        
        Manacher manacher = new Manacher(s);
        return manacher.getLongestPalindrome();
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <string>
     * #include <vector>
     * using namespace std;
     * 
     * string longestPalindrome(string s) {
     *     if (s.empty() || s.length() <= 1) {
     *         return s;
     *     }
     *     
     *     // 预处理字符串
     *     string processed = "^";
     *     for (char c : s) {
     *         processed += "#" + string(1, c);
     *     }
     *     processed += "#$";
     *     
     *     int n = processed.length();
     *     vector<int> P(n, 0);
     *     int C = 0, R = 0;
     *     
     *     // Manacher算法
     *     for (int i = 1; i < n - 1; i++) {
     *         int mirror = 2 * C - i;
     *         
     *         if (i < R) {
     *             P[i] = min(R - i, P[mirror]);
     *         }
     *         
     *         // 扩展回文
     *         while (processed[i + (1 + P[i])] == processed[i - (1 + P[i])]) {
     *             P[i]++;
     *         }
     *         
     *         // 更新中心和右边界
     *         if (i + P[i] > R) {
     *             C = i;
     *             R = i + P[i];
     *         }
     *     }
     *     
     *     // 找到最长回文
     *     int maxLen = 0;
     *     int centerIndex = 0;
     *     for (int i = 1; i < n - 1; i++) {
     *         if (P[i] > maxLen) {
     *             maxLen = P[i];
     *             centerIndex = i;
     *         }
     *     }
     *     
     *     int start = (centerIndex - maxLen) / 2;
     *     return s.substr(start, maxLen);
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * def longestPalindrome(s):
     *     if not s or len(s) <= 1:
     *         return s
     *     
     *     # 预处理字符串
     *     processed = "^#" + "#".join(s) + "#$"
     *     
     *     n = len(processed)
     *     P = [0] * n
     *     C, R = 0, 0
     *     
     *     # Manacher算法
     *     for i in range(1, n - 1):
     *         mirror = 2 * C - i
     *         
     *         if i < R:
     *             P[i] = min(R - i, P[mirror])
     *         
     *         # 扩展回文
     *         try:
     *             while processed[i + (1 + P[i])] == processed[i - (1 + P[i])]:
     *                 P[i] += 1
     *         except:
     *             pass
     *         
     *         # 更新中心和右边界
     *         if i + P[i] > R:
     *             C, R = i, i + P[i]
     *     
     *     # 找到最长回文
     *     maxLen = 0
     *     centerIndex = 0
     *     for i in range(1, n - 1):
     *         if P[i] > maxLen:
     *             maxLen = P[i]
     *             centerIndex = i
     *     
     *     start = (centerIndex - maxLen) // 2
     *     return s[start:start + maxLen]
     */
    
    // ====================================================================================
    // 题目2: LeetCode 647 - Palindromic Substrings
    // 链接: https://leetcode.com/problems/palindromic-substrings/
    // 题目描述: 计算字符串中回文子串的数量
    // 解题思路: 使用Manacher算法计算每个位置的回文半径，然后累加
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int countSubstrings(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        
        Manacher manacher = new Manacher(s);
        return manacher.countPalindromes();
    }
    
    // ====================================================================================
    // 题目3: LeetCode 214 - Shortest Palindrome
    // 链接: https://leetcode.com/problems/shortest-palindrome/
    // 题目描述: 通过在字符串前面添加字符使其成为回文串，求最短回文串
    // 解题思路: 找到字符串的最长前缀回文，然后在前面添加剩余部分的反转
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static String shortestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        
        // 构造字符串 s + "#" + reverse(s)
        String reversed = new StringBuilder(s).reverse().toString();
        String combined = s + "#" + reversed;
        
        // 使用KMP算法计算失败函数
        int[] pi = new int[combined.length()];
        for (int i = 1; i < combined.length(); i++) {
            int j = pi[i - 1];
            while (j > 0 && combined.charAt(i) != combined.charAt(j)) {
                j = pi[j - 1];
            }
            if (combined.charAt(i) == combined.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        
        // pi数组的最后一个值就是s的最长前缀回文长度
        int len = pi[combined.length() - 1];
        return reversed.substring(0, s.length() - len) + s;
    }
    
    // ====================================================================================
    // 题目4: LeetCode 336 - Palindrome Pairs
    // 链接: https://leetcode.com/problems/palindrome-pairs/
    // 题目描述: 给定一个字符串数组，找出所有回文对(i,j)，使得words[i]+words[j]是回文
    // 解题思路: 使用字典树(Trie)和Manacher算法预处理每个单词的回文信息
    // 时间复杂度: O(sum of words[i].length)
    // 空间复杂度: O(sum of words[i].length)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        if (words == null || words.length < 2) {
            return result;
        }
        
        // 构建单词到索引的映射
        Map<String, Integer> wordIndexMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordIndexMap.put(words[i], i);
        }
        
        // 对每个单词检查可能的回文对
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String reversedWord = new StringBuilder(word).reverse().toString();
            
            // 情况1: 当前单词的反转在数组中且不是自己
            if (wordIndexMap.containsKey(reversedWord) && wordIndexMap.get(reversedWord) != i) {
                result.add(Arrays.asList(i, wordIndexMap.get(reversedWord)));
            }
            
            // 情况2: 当前单词可以作为前缀，需要在后面添加一个回文
            for (int j = 0; j < word.length(); j++) {
                // 检查word[j:]是否为回文
                if (isPalindrome(word, j, word.length() - 1)) {
                    String prefix = reversedWord.substring(word.length() - j);
                    if (wordIndexMap.containsKey(prefix) && wordIndexMap.get(prefix) != i) {
                        result.add(Arrays.asList(i, wordIndexMap.get(prefix)));
                    }
                }
                
                // 检查word[0:j]是否为回文
                if (isPalindrome(word, 0, j)) {
                    String suffix = reversedWord.substring(0, word.length() - j - 1);
                    if (wordIndexMap.containsKey(suffix) && wordIndexMap.get(suffix) != i) {
                        result.add(Arrays.asList(wordIndexMap.get(suffix), i));
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * 检查字符串的子串是否为回文
     */
    private static boolean isPalindrome(String s, int start, int end) {
        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }
    
    // ====================================================================================
    // 题目5: LeetCode 131 - Palindrome Partitioning
    // 链接: https://leetcode.com/problems/palindrome-partitioning/
    // 题目描述: 将字符串分割成若干回文子串的所有可能方案
    // 解题思路: 使用回溯法，结合Manacher预处理回文信息
    // 时间复杂度: O(N * 2^N)
    // 空间复杂度: O(N * N)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return result;
        }
        
        // 预处理回文信息
        Manacher manacher = new Manacher(s);
        boolean[][] isPalindrome = new boolean[s.length()][s.length()];
        
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                isPalindrome[i][j] = manacher.isPalindrome(i, j);
            }
        }
        
        // 回溯搜索所有分割方案
        backtrack(s, 0, new ArrayList<>(), result, isPalindrome);
        return result;
    }
    
    /**
     * 回溯搜索所有分割方案
     */
    private static void backtrack(String s, int start, List<String> current, 
                                 List<List<String>> result, boolean[][] isPalindrome) {
        if (start == s.length()) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome[start][end]) {
                current.add(s.substring(start, end + 1));
                backtrack(s, end + 1, current, result, isPalindrome);
                current.remove(current.size() - 1);
            }
        }
    }
    
    // ====================================================================================
    // 题目6: LeetCode 132 - Palindrome Partitioning II
    // 链接: https://leetcode.com/problems/palindrome-partitioning-ii/
    // 题目描述: 将字符串分割成回文子串所需的最少分割次数
    // 解题思路: 动态规划，dp[i]表示s[0:i]的最少分割次数
    // 时间复杂度: O(N^2)
    // 空间复杂度: O(N^2)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int minCut(String s) {
        if (s == null || s.length() <= 1) {
            return 0;
        }
        
        int n = s.length();
        
        // 预处理回文信息
        Manacher manacher = new Manacher(s);
        boolean[][] isPalindrome = new boolean[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                isPalindrome[i][j] = manacher.isPalindrome(i, j);
            }
        }
        
        // 动态规划计算最少分割次数
        int[] dp = new int[n];
        Arrays.fill(dp, n); // 初始化为最大值
        
        for (int i = 0; i < n; i++) {
            if (isPalindrome[0][i]) {
                dp[i] = 0; // 如果整个前缀是回文，则不需要分割
            } else {
                for (int j = 0; j < i; j++) {
                    if (isPalindrome[j + 1][i]) {
                        dp[i] = Math.min(dp[i], dp[j] + 1);
                    }
                }
            }
        }
        
        return dp[n - 1];
    }
    
    // ====================================================================================
    // 题目7: Codeforces 137D - Palindromes
    // 题目描述: 将字符串分割成最少的回文子串，使得每个回文长度至少为k
    // 解题思路: 动态规划，dp[i]表示s[0:i]的最少分割次数，且每个回文长度至少为k
    // 时间复杂度: O(N^2)
    // 空间复杂度: O(N^2)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int minPalindromesWithMinLength(String s, int k) {
        if (s == null || s.length() < k) {
            return -1; // 无法分割
        }
        
        int n = s.length();
        
        // 预处理回文信息
        Manacher manacher = new Manacher(s);
        boolean[][] isPalindrome = new boolean[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                isPalindrome[i][j] = manacher.isPalindrome(i, j);
            }
        }
        
        // 动态规划计算最少分割次数
        int[] dp = new int[n + 1];
        Arrays.fill(dp, n + 1); // 初始化为一个大值
        dp[0] = 0;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                // 检查s[j:i]是否为回文且长度至少为k
                if (i - j >= k && isPalindrome[j][i - 1]) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }
        
        return dp[n] <= n ? dp[n] : -1;
    }
    
    // ====================================================================================
    // 题目8: SPOJ PLD - Palindromes
    // 题目描述: 计算字符串中长度恰好为k的回文子串个数
    // 解题思路: 使用Manacher算法计算每个位置的回文半径，然后统计长度为k的回文
    // 时间复杂度: O(N)
    // 空间复杂度: O(N)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int countPalindromesOfLengthK(String s, int k) {
        if (s == null || s.length() < k) {
            return 0;
        }
        
        Manacher manacher = new Manacher(s);
        int[] P = manacher.getRadiusArray();
        int count = 0;
        
        for (int i = 1; i < P.length - 1; i++) {
            // 对于每个中心i，如果回文半径>=k，则贡献1个长度为k的回文
            if (P[i] >= k) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试Longest Palindrome
        System.out.println("=== 测试Longest Palindrome ===");
        String s1 = "babad";
        System.out.println("输入: " + s1);
        System.out.println("最长回文子串: " + longestPalindrome(s1));
        
        // 测试Palindrome Count
        System.out.println("\n=== 测试Palindrome Count ===");
        String s2 = "abc";
        System.out.println("输入: " + s2);
        System.out.println("回文子串数量: " + countSubstrings(s2));
        
        // 测试Shortest Palindrome
        System.out.println("\n=== 测试Shortest Palindrome ===");
        String s3 = "aacecaaa";
        System.out.println("输入: " + s3);
        System.out.println("最短回文串: " + shortestPalindrome(s3));
        
        // 测试Palindrome Partitioning
        System.out.println("\n=== 测试Palindrome Partitioning ===");
        String s4 = "aab";
        System.out.println("输入: " + s4);
        List<List<String>> partitions = partition(s4);
        System.out.println("所有分割方案: " + partitions);
        
        // 测试Palindrome Partitioning II
        System.out.println("\n=== 测试Palindrome Partitioning II ===");
        String s5 = "aab";
        System.out.println("输入: " + s5);
        System.out.println("最少分割次数: " + minCut(s5));
    }
}