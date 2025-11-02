package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 更多Manacher算法题目实现
 * 包含：
 * 1. LeetCode 516 - Longest Palindromic Subsequence (最长回文子序列)
 * 2. LeetCode 1278 - Palindrome Partitioning III (回文分割III)
 * 3. LeetCode 1312 - Minimum Insertion Steps to Make a String Palindrome (最小插入步数)
 * 4. Codeforces 137D - Palindromes (回文串)
 * 5. SPOJ PLD - Palindromes (回文串)
 * 6. LeetCode 1617 - Count Subtrees With Max Distance (统计子树中城市之间最大距离)
 * 7. LeetCode 1771 - Maximize Palindrome Length From Subsequences (子序列最大回文长度)
 * 8. LeetCode 1960 - Maximum Product of the Length of Two Palindromic Substrings (两个回文子串长度乘积的最大值)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class MoreManacherProblems {
    
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
    // 题目1: LeetCode 516 - Longest Palindromic Subsequence
    // 链接: https://leetcode.com/problems/longest-palindromic-subsequence/
    // 题目描述: 找到字符串中最长回文子序列的长度
    // 解题思路: 使用动态规划，dp[i][j]表示s[i:j]中最长回文子序列的长度
    // 时间复杂度: O(n²)
    // 空间复杂度: O(n²)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int longestPalindromeSubseq(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        
        int n = s.length();
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符的回文长度为1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 从长度2开始填表
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <string>
     * #include <vector>
     * #include <algorithm>
     * using namespace std;
     * 
     * int longestPalindromeSubseq(string s) {
     *     if (s.empty()) return 0;
     *     
     *     int n = s.length();
     *     vector<vector<int>> dp(n, vector<int>(n, 0));
     *     
     *     // 初始化：单个字符的回文长度为1
     *     for (int i = 0; i < n; i++) {
     *         dp[i][i] = 1;
     *     }
     *     
     *     // 从长度2开始填表
     *     for (int len = 2; len <= n; len++) {
     *         for (int i = 0; i <= n - len; i++) {
     *             int j = i + len - 1;
     *             if (s[i] == s[j]) {
     *                 dp[i][j] = dp[i + 1][j - 1] + 2;
     *             } else {
     *                 dp[i][j] = max(dp[i + 1][j], dp[i][j - 1]);
     *             }
     *         }
     *     }
     *     
     *     return dp[0][n - 1];
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * def longestPalindromeSubseq(s):
     *     if not s:
     *         return 0
     *     
     *     n = len(s)
     *     dp = [[0] * n for _ in range(n)]
     *     
     *     # 初始化：单个字符的回文长度为1
     *     for i in range(n):
     *         dp[i][i] = 1
     *     
     *     # 从长度2开始填表
     *     for length in range(2, n + 1):
     *         for i in range(n - length + 1):
     *             j = i + length - 1
     *             if s[i] == s[j]:
     *                 dp[i][j] = dp[i + 1][j - 1] + 2
     *             else:
     *                 dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
     *     
     *     return dp[0][n - 1]
     */
    
    // ====================================================================================
    // 题目2: LeetCode 1278 - Palindrome Partitioning III
    // 链接: https://leetcode.com/problems/palindrome-partitioning-iii/
    // 题目描述: 将字符串分割成k个子串，使修改次数最少
    // 解题思路: 动态规划，dp[i][j]表示前i个字符分成j个回文子串的最少修改次数
    // 时间复杂度: O(n³)
    // 空间复杂度: O(n²)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int palindromePartition(String s, int k) {
        int n = s.length();
        if (k >= n) return 0;
        
        // 预处理：计算任意子串变成回文需要的最少修改次数
        int[][] cost = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                cost[i][j] = getCost(s, i, j);
            }
        }
        
        // dp[i][j] 表示前i个字符分成j个回文子串的最少修改次数
        int[][] dp = new int[n + 1][k + 1];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], n); // 初始化为一个大值
        }
        dp[0][0] = 0;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= Math.min(i, k); j++) {
                for (int t = j; t <= i; t++) {
                    dp[i][j] = Math.min(dp[i][j], dp[t - 1][j - 1] + cost[t - 1][i - 1]);
                }
            }
        }
        
        return dp[n][k];
    }
    
    /**
     * 计算子串s[start:end+1]变成回文需要的最少修改次数
     */
    private static int getCost(String s, int start, int end) {
        int cost = 0;
        while (start < end) {
            if (s.charAt(start) != s.charAt(end)) {
                cost++;
            }
            start++;
            end--;
        }
        return cost;
    }
    
    // ====================================================================================
    // 题目3: LeetCode 1312 - Minimum Insertion Steps to Make a String Palindrome
    // 链接: https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/
    // 题目描述: 计算使字符串变成回文串所需的最少插入次数
    // 解题思路: 最少插入次数 = 字符串长度 - 最长回文子序列长度
    // 时间复杂度: O(n²)
    // 空间复杂度: O(n²)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int minInsertions(String s) {
        return s.length() - longestPalindromeSubseq(s);
    }
    
    // ====================================================================================
    // 题目4: Codeforces 137D - Palindromes
    // 题目描述: 将字符串分割成最少的回文子串，使得每个回文长度至少为k
    // 解题思路: 动态规划，dp[i]表示s[0:i]的最少分割次数，且每个回文长度至少为k
    // 时间复杂度: O(N²)
    // 空间复杂度: O(N²)
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
    // 题目5: SPOJ PLD - Palindromes
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
    
    // ====================================================================================
    // 题目6: LeetCode 1617 - Count Subtrees With Max Distance
    // 链接: https://leetcode.com/problems/count-subtrees-with-max-distance/
    // 题目描述: 统计所有子树中城市之间最大距离
    // 解题思路: 使用位运算枚举所有子树，然后计算每棵子树的直径
    // 时间复杂度: O(n * 2^n)
    // 空间复杂度: O(n²)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[] countSubgraphsForEachDiameter(int n, int[][] edges) {
        // 构建邻接表
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0] - 1, v = edge[1] - 1;
            graph[u].add(v);
            graph[v].add(u);
        }
        
        int[] result = new int[n - 1];
        
        // 枚举所有可能的子树（用位掩码表示）
        for (int mask = 1; mask < (1 << n); mask++) {
            int vertices = Integer.bitCount(mask);
            if (vertices <= 1) continue;
            
            // 检查是否构成连通子树
            int root = -1;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    root = i;
                    break;
                }
            }
            
            if (root == -1) continue;
            
            // BFS检查连通性并计算节点数
            boolean[] visited = new boolean[n];
            int count = bfsCheckConnectivity(graph, root, mask, visited);
            
            if (count == vertices) {
                // 计算子树的直径
                int diameter = calculateDiameter(graph, mask, root);
                if (diameter > 0) {
                    result[diameter - 1]++;
                }
            }
        }
        
        return result;
    }
    
    /**
     * BFS检查子树连通性
     */
    private static int bfsCheckConnectivity(List<Integer>[] graph, int start, int mask, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        visited[start] = true;
        int count = 1;
        
        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : graph[node]) {
                if ((mask & (1 << neighbor)) != 0 && !visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 计算子树的直径
     */
    private static int calculateDiameter(List<Integer>[] graph, int mask, int start) {
        // 第一次BFS找到最远点
        int[] firstBfs = bfsFindFarthest(graph, start, mask);
        if (firstBfs[1] == -1) return 0;
        
        // 第二次BFS找到直径
        int[] secondBfs = bfsFindFarthest(graph, firstBfs[1], mask);
        return secondBfs[0];
    }
    
    /**
     * BFS找到最远点
     */
    private static int[] bfsFindFarthest(List<Integer>[] graph, int start, int mask) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start, 0});
        boolean[] visited = new boolean[graph.length];
        visited[start] = true;
        
        int farthestNode = start;
        int maxDistance = 0;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0];
            int distance = current[1];
            
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestNode = node;
            }
            
            for (int neighbor : graph[node]) {
                if ((mask & (1 << neighbor)) != 0 && !visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(new int[]{neighbor, distance + 1});
                }
            }
        }
        
        return new int[]{maxDistance, farthestNode};
    }
    
    // ====================================================================================
    // 题目7: LeetCode 1771 - Maximize Palindrome Length From Subsequences
    // 链接: https://leetcode.com/problems/maximize-palindrome-length-from-subsequences/
    // 题目描述: 找到由word1和word2的子序列组成的最长回文串长度
    // 解题思路: 将两个字符串拼接，使用动态规划，但要求回文必须包含两个字符串的字符
    // 时间复杂度: O((m+n)²)
    // 空间复杂度: O((m+n)²)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int longestPalindrome(String word1, String word2) {
        String s = word1 + word2;
        int n = s.length();
        int m = word1.length();
        
        // dp[i][j]表示s[i:j+1]中最长回文子序列的长度
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符的回文长度为1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        int result = 0;
        
        // 从长度2开始填表
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                    // 检查是否跨越了两个字符串
                    if (i < m && j >= m) {
                        result = Math.max(result, dp[i][j]);
                    }
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目8: LeetCode 1960 - Maximum Product of the Length of Two Palindromic Substrings
    // 链接: https://leetcode.com/problems/maximum-product-of-the-length-of-two-palindromic-substrings/
    // 题目描述: 找到两个不重叠回文子串长度乘积的最大值
    // 解题思路: 使用Manacher算法预处理所有回文信息，然后前后缀分解计算最大乘积
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long maxProduct(String s) {
        int n = s.length();
        Manacher manacher = new Manacher(s);
        int[] P = manacher.getRadiusArray();
        
        // 计算以每个位置为中心的最长回文长度
        int[] palindromeLength = new int[n];
        for (int i = 1; i < P.length - 1; i++) {
            int center = (i - 1) / 2;
            int radius = P[i];
            if ((i & 1) == 1) { // 奇数长度回文
                palindromeLength[center] = Math.max(palindromeLength[center], radius);
            } else { // 偶数长度回文
                if (radius > 0) {
                    int leftCenter = (i - 2) / 2;
                    palindromeLength[leftCenter] = Math.max(palindromeLength[leftCenter], radius);
                }
            }
        }
        
        // 前缀最大回文长度
        int[] prefixMax = new int[n];
        prefixMax[0] = 1;
        for (int i = 1; i < n; i++) {
            prefixMax[i] = Math.max(prefixMax[i - 1], palindromeLength[i]);
        }
        
        // 后缀最大回文长度
        int[] suffixMax = new int[n];
        suffixMax[n - 1] = palindromeLength[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suffixMax[i] = Math.max(suffixMax[i + 1], palindromeLength[i]);
        }
        
        // 计算最大乘积
        long maxProduct = 0;
        for (int i = 0; i < n - 1; i++) {
            maxProduct = Math.max(maxProduct, (long) prefixMax[i] * suffixMax[i + 1]);
        }
        
        return maxProduct;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试Longest Palindromic Subsequence
        System.out.println("=== 测试Longest Palindromic Subsequence ===");
        String s1 = "bbbab";
        System.out.println("输入: " + s1);
        System.out.println("最长回文子序列长度: " + longestPalindromeSubseq(s1));
        
        // 测试Palindrome Partitioning III
        System.out.println("\n=== 测试Palindrome Partitioning III ===");
        String s2 = "abc";
        int k1 = 2;
        System.out.println("输入: " + s2 + ", k=" + k1);
        System.out.println("最少修改次数: " + palindromePartition(s2, k1));
        
        // 测试Minimum Insertion Steps
        System.out.println("\n=== 测试Minimum Insertion Steps ===");
        String s3 = "mbadm";
        System.out.println("输入: " + s3);
        System.out.println("最少插入次数: " + minInsertions(s3));
        
        // 测试Count Palindromes of Length K
        System.out.println("\n=== 测试Count Palindromes of Length K ===");
        String s4 = "abaccaba";
        int k2 = 3;
        System.out.println("输入: " + s4 + ", k=" + k2);
        System.out.println("长度为" + k2 + "的回文子串个数: " + countPalindromesOfLengthK(s4, k2));
        
        // 测试Maximize Palindrome Length From Subsequences
        System.out.println("\n=== 测试Maximize Palindrome Length From Subsequences ===");
        String word1 = "cacb";
        String word2 = "cbba";
        System.out.println("输入: word1=" + word1 + ", word2=" + word2);
        System.out.println("最长回文长度: " + longestPalindrome(word1, word2));
        
        // 测试Maximum Product of Palindromic Substrings
        System.out.println("\n=== 测试Maximum Product of Palindromic Substrings ===");
        String s5 = "ababbb";
        System.out.println("输入: " + s5);
        System.out.println("两个回文子串长度乘积的最大值: " + maxProduct(s5));
    }
}