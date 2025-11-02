// package class103;  // 注释掉包声明，便于直接运行

import java.util.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * 高级Manacher算法题目实现
 * 包含更多复杂的回文串处理问题和Z函数应用
 * 
 * 本文件实现了以下高级题目：
 * 1. LeetCode 336. 回文对
 * 2. LeetCode 131. 分割回文串
 * 3. LeetCode 132. 分割回文串 II
 * 4. 洛谷 P1659 [国家集训队]拉拉队排练
 * 5. 洛谷 P4555 [国家集训队]最长双回文串
 * 6. SPOJ PALIN - The Next Palindrome
 * 7. HackerRank Build a Palindrome
 * 8. AtCoder ABC141E - Who Says a Pun?
 * 
 * 时间复杂度分析：O(n) 到 O(n²) 不等，取决于具体问题
 * 空间复杂度分析：O(n) 到 O(n²) 不等，取决于具体问题
 */

public class AdvancedManacherProblems {
    
    // 日志记录器 - 简化实现，避免复杂的日志依赖
    // private static final Logger logger = Logger.getLogger(AdvancedManacherProblems.class.getName());
    
    /**
     * LeetCode 336. 回文对
     * 给定一组互不相同的单词，找出所有不同的索引对(i, j)，使得两个单词拼接起来是回文串。
     * 
     * 解题思路：
     * 1. 使用字典树存储所有单词及其反转
     * 2. 对于每个单词，检查其前缀和后缀是否为回文
     * 3. 在字典树中查找剩余部分的匹配
     * 
     * 时间复杂度：O(n * k²)，其中n是单词数量，k是单词平均长度
     * 空间复杂度：O(n * k)
     * 
     * @param words 单词列表
     * @return 回文对列表
     */
    public static List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        if (words == null || words.length < 2) return result;
        
        // 构建字典树
        TrieNode root = new TrieNode();
        for (int i = 0; i < words.length; i++) {
            insertWord(root, words[i], i);
        }
        
        // 查找回文对
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            TrieNode node = root;
            
            // 检查整个单词是否在字典树中
            for (int j = 0; j < word.length(); j++) {
                char c = word.charAt(j);
                if (node.children[c - 'a'] == null) {
                    break;
                }
                node = node.children[c - 'a'];
                
                // 如果当前节点是某个单词的结尾，且剩余部分是回文
                if (node.wordIndex != -1 && node.wordIndex != i && 
                    isPalindrome(word, j + 1, word.length() - 1)) {
                    result.add(Arrays.asList(i, node.wordIndex));
                }
            }
            
            // 检查单词的反转
            String reversed = new StringBuilder(word).reverse().toString();
            TrieNode revNode = root;
            for (int j = 0; j < reversed.length(); j++) {
                char c = reversed.charAt(j);
                if (revNode.children[c - 'a'] == null) {
                    break;
                }
                revNode = revNode.children[c - 'a'];
                
                // 如果当前节点是某个单词的结尾，且剩余部分是回文
                if (revNode.wordIndex != -1 && revNode.wordIndex != i && 
                    isPalindrome(reversed, j + 1, reversed.length() - 1)) {
                    result.add(Arrays.asList(revNode.wordIndex, i));
                }
            }
        }
        
        return result;
    }
    
    /**
     * 字典树节点
     */
    static class TrieNode {
        TrieNode[] children;
        int wordIndex; // 存储单词索引，-1表示不是单词结尾
        
        public TrieNode() {
            children = new TrieNode[26];
            wordIndex = -1;
        }
    }
    
    /**
     * 向字典树中插入单词
     */
    private static void insertWord(TrieNode root, String word, int index) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.wordIndex = index;
    }
    
    /**
     * 判断子串是否为回文
     */
    private static boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    
    /**
     * LeetCode 131. 分割回文串
     * 给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。返回所有可能的分割方案。
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理回文信息
     * 2. 使用回溯法枚举所有分割方案
     * 3. 利用预处理信息快速判断子串是否为回文
     * 
     * 时间复杂度：O(n * 2ⁿ)
     * 空间复杂度：O(n²)
     * 
     * @param s 输入字符串
     * @return 所有分割方案
     */
    public static List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        if (s == null || s.length() == 0) return result;
        
        // 预处理回文信息
        boolean[][] isPalindrome = preprocessPalindrome(s);
        
        // 回溯法枚举所有分割方案
        backtrack(s, 0, new ArrayList<>(), result, isPalindrome);
        
        return result;
    }
    
    /**
     * 预处理回文信息
     */
    private static boolean[][] preprocessPalindrome(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        
        // 单个字符都是回文
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }
        
        // 两个字符的情况
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
        }
        
        // 长度大于2的情况
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]);
            }
        }
        
        return dp;
    }
    
    /**
     * 回溯法实现
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
    
    /**
     * LeetCode 132. 分割回文串 II
     * 给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数。
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理回文信息
     * 2. 使用动态规划计算最少分割次数
     * 3. 优化状态转移过程
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     * 
     * @param s 输入字符串
     * @return 最少分割次数
     */
    public static int minCut(String s) {
        int n = s.length();
        if (n <= 1) return 0;
        
        // 预处理回文信息
        boolean[][] isPalindrome = preprocessPalindrome(s);
        
        // 动态规划计算最少分割次数
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        
        for (int i = 0; i < n; i++) {
            if (isPalindrome[0][i]) {
                dp[i] = 0; // 整个子串是回文，不需要分割
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
    
    /**
     * 洛谷 P1659 [国家集训队]拉拉队排练
     * 求字符串中所有奇数长度回文串的长度乘积。
     * 
     * 解题思路：
     * 1. 使用Manacher算法找到所有奇数长度回文串
     * 2. 统计每个长度的回文串数量
     * 3. 计算前k大的长度乘积
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param s 输入字符串
     * @param k 前k大的长度
     * @return 长度乘积（取模）
     */
    public static long longestPalindromeProduct(String s, int k) {
        int n = s.length();
        if (n == 0) return 0;
        
        // 使用Manacher算法计算回文半径
        int[] radius = manacherOdd(s);
        
        // 统计每个长度的回文串数量
        int[] count = new int[n + 1];
        for (int i = 0; i < n; i++) {
            int len = 2 * radius[i] + 1;
            count[len]++;
        }
        
        // 计算前k大的长度乘积
        long product = 1;
        long mod = 1000000007;
        int remaining = k;
        
        for (int len = n; len >= 1 && remaining > 0; len -= 2) {
            if (count[len] > 0) {
                int take = Math.min(count[len], remaining);
                product = (product * pow(len, take, mod)) % mod;
                remaining -= take;
            }
        }
        
        return product;
    }
    
    /**
     * 快速幂计算
     */
    private static long pow(long base, int exponent, long mod) {
        long result = 1;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent >>= 1;
        }
        return result;
    }
    
    /**
     * Manacher算法计算奇回文串
     */
    private static int[] manacherOdd(String s) {
        int n = s.length();
        int[] radius = new int[n];
        
        for (int i = 0, l = 0, r = -1; i < n; i++) {
            int k = (i > r) ? 1 : Math.min(radius[l + r - i], r - i + 1);
            
            while (0 <= i - k && i + k < n && s.charAt(i - k) == s.charAt(i + k)) {
                k++;
            }
            
            radius[i] = k - 1;
            
            if (i + k - 1 > r) {
                l = i - k + 1;
                r = i + k - 1;
            }
        }
        
        return radius;
    }
    
    /**
     * 洛谷 P4555 [国家集训队]最长双回文串
     * 输入字符串s，求s的最长双回文子串t的长度（双回文子串就是可以分成两个回文串的字符串）
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理
     * 2. 计算每个位置作为分割点的左右最长回文
     * 3. 枚举所有分割点求最大值
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param s 输入字符串
     * @return 最长双回文串长度
     */
    public static int longestDoublePalindrome(String s) {
        int n = s.length();
        if (n < 2) return 0;
        
        // 预处理字符串
        String processed = preprocess(s);
        char[] chars = processed.toCharArray();
        int len = chars.length;
        int[] p = new int[len];
        
        int center = 0, right = 0;
        for (int i = 1; i < len - 1; i++) {
            int mirror = 2 * center - i;
            
            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }
            
            while (i + p[i] + 1 < len && i - p[i] - 1 >= 0 && 
                   chars[i + p[i] + 1] == chars[i - p[i] - 1]) {
                p[i]++;
            }
            
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }
        }
        
        // 计算每个位置的最长回文长度
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];
        
        // 从左到右计算每个位置的最长回文前缀
        int maxLen = 0;
        for (int i = 0; i < n; i++) {
            int pos = 2 * i + 1;
            maxLen = Math.max(maxLen, p[pos]);
            leftMax[i] = maxLen;
        }
        
        // 从右到左计算每个位置的最长回文后缀
        maxLen = 0;
        for (int i = n - 1; i >= 0; i--) {
            int pos = 2 * i + 1;
            maxLen = Math.max(maxLen, p[pos]);
            rightMax[i] = maxLen;
        }
        
        // 枚举分割点，计算最长双回文串
        int result = 0;
        for (int i = 0; i < n - 1; i++) {
            result = Math.max(result, leftMax[i] + rightMax[i + 1]);
        }
        
        return result;
    }
    
    /**
     * 预处理函数，用于在字符间插入'#'
     */
    private static String preprocess(String s) {
        int n = s.length();
        if (n == 0) return "^$";
        
        StringBuilder sb = new StringBuilder(2 * n + 3);
        sb.append("^");
        
        for (int i = 0; i < n; i++) {
            sb.append("#").append(s.charAt(i));
        }
        
        sb.append("#$");
        return sb.toString();
    }
    
    /**
     * SPOJ PALIN - The Next Palindrome
     * 给定一个整数，找到大于该数的最小回文数。
     * 
     * 解题思路：
     * 1. 将数字转换为字符串
     * 2. 构造下一个回文数
     * 3. 处理进位等特殊情况
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param numStr 数字字符串
     * @return 下一个回文数
     */
    public static String nextPalindrome(String numStr) {
        int n = numStr.length();
        char[] num = numStr.toCharArray();
        
        // 处理全9的情况
        if (isAllNines(num)) {
            StringBuilder result = new StringBuilder("1");
            for (int i = 0; i < n - 1; i++) {
                result.append("0");
            }
            result.append("1");
            return result.toString();
        }
        
        // 构造下一个回文数
        int mid = n / 2;
        boolean leftSmaller = false;
        int i = mid - 1;
        int j = (n % 2 == 0) ? mid : mid + 1;
        
        // 跳过已经相同的部分
        while (i >= 0 && num[i] == num[j]) {
            i--;
            j++;
        }
        
        // 检查是否需要增加中间部分
        if (i < 0 || num[i] < num[j]) {
            leftSmaller = true;
        }
        
        // 复制左半部分到右半部分
        while (i >= 0) {
            num[j] = num[i];
            i--;
            j++;
        }
        
        // 如果需要增加中间部分
        if (leftSmaller) {
            int carry = 1;
            i = mid - 1;
            
            if (n % 2 == 1) {
                int midNum = num[mid] - '0' + carry;
                carry = midNum / 10;
                num[mid] = (char)(midNum % 10 + '0');
                j = mid + 1;
            } else {
                j = mid;
            }
            
            // 处理进位
            while (i >= 0) {
                int digit = num[i] - '0' + carry;
                carry = digit / 10;
                num[i] = (char)(digit % 10 + '0');
                num[j] = num[i];
                i--;
                j++;
            }
        }
        
        return new String(num);
    }
    
    /**
     * 检查数字是否全为9
     */
    private static boolean isAllNines(char[] num) {
        for (char c : num) {
            if (c != '9') {
                return false;
            }
        }
        return true;
    }
    
    /**
     * HackerRank Build a Palindrome
     * 给定两个字符串a和b，从a中取一个非空前缀，从b中取一个非空后缀，拼接成一个回文串，求最长的回文串长度。
     * 
     * 解题思路：
     * 1. 使用Manacher算法预处理两个字符串
     * 2. 枚举所有可能的前缀后缀组合
     * 3. 检查拼接后的字符串是否为回文
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * @param a 字符串a
     * @param b 字符串b
     * @return 最长回文串长度
     */
    public static int buildPalindrome(String a, String b) {
        int maxLen = 0;
        int n = a.length(), m = b.length();
        
        // 预处理两个字符串的回文信息
        boolean[][] isPalindromeA = preprocessPalindrome(a);
        boolean[][] isPalindromeB = preprocessPalindrome(b);
        
        // 枚举所有可能的前缀后缀组合
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 从a取前缀，从b取后缀
                String prefix = a.substring(0, i + 1);
                String suffix = b.substring(m - j - 1);
                String combined = prefix + suffix;
                
                // 检查是否为回文
                if (isPalindrome(combined, 0, combined.length() - 1)) {
                    maxLen = Math.max(maxLen, combined.length());
                }
            }
        }
        
        return maxLen;
    }
    
    /**
     * AtCoder ABC141E - Who Says a Pun?
     * 给定一个长度为n的字符串s，找出两个不重叠的子串，使得它们相等且长度尽可能大。
     * 
     * 解题思路：
     * 1. 使用Z函数计算每个后缀的匹配情况
     * 2. 遍历所有可能的分割点
     * 3. 找到满足条件的最长子串
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * @param s 输入字符串
     * @return 最大长度
     */
    public static int whoSaysPun(String s) {
        int n = s.length();
        int maxLen = 0;
        
        // 对于每个可能的起始位置
        for (int i = 0; i < n; i++) {
            // 计算从位置i开始的Z函数
            int[] z = zFunction(s.substring(i));
            
            // 查找不重叠的相同子串
            for (int j = 1; j < z.length; j++) {
                if (z[j] > maxLen && j >= z[j]) {
                    maxLen = Math.max(maxLen, z[j]);
                }
            }
        }
        
        return maxLen;
    }
    
    /**
     * Z函数计算
     */
    private static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];
        z[0] = n;
        
        for (int i = 1, l = 0, r = 0; i < n; i++) {
            if (i <= r) {
                z[i] = Math.min(r - i + 1, z[i - l]);
            }
            
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            
            if (i + z[i] - 1 > r) {
                l = i;
                r = i + z[i] - 1;
            }
        }
        
        return z;
    }
    
    /**
     * 单元测试方法
     */
    public static void runUnitTests() {
        System.out.println("===== 高级Manacher算法题目测试 =====");
        
        // 测试回文对
        System.out.println("\n1. 回文对测试:");
        String[] words1 = {"abcd", "dcba", "lls", "s", "sssll"};
        List<List<Integer>> result1 = palindromePairs(words1);
        System.out.println("回文对结果: " + result1);
        
        // 测试分割回文串
        System.out.println("\n2. 分割回文串测试:");
        String s2 = "aab";
        List<List<String>> result2 = partition(s2);
        System.out.println("分割方案数量: " + result2.size());
        
        // 测试最少分割次数
        System.out.println("\n3. 最少分割次数测试:");
        String s3 = "aab";
        int result3 = minCut(s3);
        System.out.println("最少分割次数: " + result3);
        
        // 测试最长双回文串
        System.out.println("\n4. 最长双回文串测试:");
        String s4 = "baacaabbacabb";
        int result4 = longestDoublePalindrome(s4);
        System.out.println("最长双回文串长度: " + result4);
        
        // 测试下一个回文数
        System.out.println("\n5. 下一个回文数测试:");
        String num = "12345";
        String result5 = nextPalindrome(num);
        System.out.println("下一个回文数: " + result5);
        
        System.out.println("\n===== 测试完成 =====");
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        runUnitTests();
    }
}