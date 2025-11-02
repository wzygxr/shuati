package class086;

// LeetCode 516. 最长回文子序列
// 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
// 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
// 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/

/**
 * 算法详解：最长回文子序列（LeetCode 516）
 * 
 * 问题描述：
 * 给定一个字符串s，找出其中最长的回文子序列的长度。
 * 回文子序列是指正着读和反着读都一样的子序列。
 * 
 * 算法思路：
 * 1. 动态规划：dp[i][j]表示s[i..j]的最长回文子序列长度
 * 2. 状态转移方程：
 *    - 如果s[i] == s[j]：dp[i][j] = dp[i+1][j-1] + 2
 *    - 否则：dp[i][j] = max(dp[i+1][j], dp[i][j-1])
 * 3. 边界条件：dp[i][i] = 1
 * 
 * 时间复杂度分析：
 * 1. 填充dp表：需要遍历所有子串，时间复杂度为O(n²)
 * 2. 总体时间复杂度：O(n²)
 * 
 * 空间复杂度分析：
 * 1. dp数组：需要存储n²个状态值，空间复杂度为O(n²)
 * 2. 空间优化版本：使用滚动数组可将空间复杂度优化到O(n)
 * 3. 总体空间复杂度：O(n²) 或 O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入字符串是否为空
 * 2. 边界处理：处理单字符字符串的情况
 * 3. 性能优化：使用空间优化版本减少内存使用
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 极端场景验证：
 * 1. 输入字符串为空的情况
 * 2. 单字符字符串的情况
 * 3. 全相同字符的字符串
 * 4. 完全不同的字符组成的字符串
 * 5. 大规模字符串的性能测试
 */
public class LeetCode516_Longest_Palindromic_Subsequence {
    
    /**
     * 基础动态规划解法
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     * 
     * 算法思想：
     * 使用二维dp数组，dp[i][j]表示子串s[i..j]的最长回文子序列长度。
     * 通过从短到长逐步计算所有子串的解。
     */
    public static int longestPalindromeSubseq(String s) {
        // 异常处理
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int n = s.length();
        
        // 特殊情况：单字符字符串
        if (n == 1) {
            return 1;
        }
        
        // dp[i][j] 表示s[i..j]的最长回文子序列长度
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符都是回文，长度为1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 从长度为2的子串开始计算
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                if (s.charAt(i) == s.charAt(j)) {
                    // 首尾字符相同
                    if (len == 2) {
                        // 长度为2的子串，首尾相同则长度为2
                        dp[i][j] = 2;
                    } else {
                        // 长度大于2，等于内部子串长度加2
                        dp[i][j] = dp[i + 1][j - 1] + 2;
                    }
                } else {
                    // 首尾字符不同，取两种情况的较大值
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 返回整个字符串的最长回文子序列长度
        return dp[0][n - 1];
    }
    
    /**
     * 空间优化版本（使用一维数组）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * 优化思路：
     * 观察状态转移方程，dp[i][j]只依赖于dp[i+1][j-1], dp[i+1][j], dp[i][j-1]
     * 可以使用一维数组，按长度从短到长计算。
     */
    public static int longestPalindromeSubseqOptimized(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int n = s.length();
        if (n == 1) {
            return 1;
        }
        
        // 使用一维数组存储状态
        int[] dp = new int[n];
        
        // 初始化：每个字符自身都是长度为1的回文
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }
        
        // 从右向左遍历
        for (int i = n - 2; i >= 0; i--) {
            int prev = 0; // 保存dp[i+1][j-1]的值
            for (int j = i + 1; j < n; j++) {
                int temp = dp[j]; // 保存当前值，用于下一轮计算
                
                if (s.charAt(i) == s.charAt(j)) {
                    // 首尾字符相同
                    dp[j] = prev + 2;
                } else {
                    // 首尾字符不同，取较大值
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                
                prev = temp; // 更新prev为dp[i+1][j-1]
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 递归+记忆化搜索解法
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     * 
     * 算法思想：
     * 使用递归函数计算每个子串的解，通过记忆化避免重复计算。
     * 这种方法更直观但可能栈溢出。
     */
    public static int longestPalindromeSubseqMemo(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int n = s.length();
        Integer[][] memo = new Integer[n][n];
        return dfs(s, 0, n - 1, memo);
    }
    
    private static int dfs(String s, int i, int j, Integer[][] memo) {
        // 边界条件
        if (i > j) {
            return 0;
        }
        if (i == j) {
            return 1;
        }
        
        // 检查是否已经计算过
        if (memo[i][j] != null) {
            return memo[i][j];
        }
        
        int result;
        if (s.charAt(i) == s.charAt(j)) {
            // 首尾字符相同
            result = dfs(s, i + 1, j - 1, memo) + 2;
        } else {
            // 首尾字符不同，取两种情况的最大值
            result = Math.max(dfs(s, i + 1, j, memo), dfs(s, i, j - 1, memo));
        }
        
        // 记忆化结果
        memo[i][j] = result;
        return result;
    }
    
    /**
     * 单元测试方法
     * 验证算法的正确性和各种边界情况
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 516 最长回文子序列测试 ===\n");
        
        // 测试用例1：基本功能测试
        testCase("测试用例1 - 基本功能", "bbbab", 4);
        
        // 测试用例2：LeetCode官方示例
        testCase("测试用例2 - 官方示例", "cbbd", 2);
        
        // 测试用例3：全相同字符
        testCase("测试用例3 - 全相同字符", "aaaa", 4);
        
        // 测试用例4：单字符
        testCase("测试用例4 - 单字符", "a", 1);
        
        // 测试用例5：空字符串
        testCase("测试用例5 - 空字符串", "", 0);
        
        // 测试用例6：交替字符
        testCase("测试用例6 - 交替字符", "abab", 3);
        
        // 测试用例7：复杂情况
        testCase("测试用例7 - 复杂情况", "abcabcabc", 5);
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 测试用例辅助方法
     */
    private static void testCase(String description, String s, int expected) {
        System.out.println(description);
        System.out.println("输入字符串: \"" + s + "\"");
        System.out.println("期望结果: " + expected);
        
        int result1 = longestPalindromeSubseq(s);
        int result2 = longestPalindromeSubseqOptimized(s);
        int result3 = longestPalindromeSubseqMemo(s);
        
        System.out.println("基础DP: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("优化DP: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        System.out.println("记忆化搜索: " + result3 + " " + (result3 == expected ? "✓" : "✗"));
        
        if (result1 == result2 && result2 == result3 && result1 == expected) {
            System.out.println("测试通过 ✓\n");
        } else {
            System.out.println("测试失败 ✗\n");
        }
    }
    
    /**
     * 性能测试方法
     * 测试算法在大规模数据下的表现
     */
    private static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成测试数据：大规模字符串
        int n = 1000;
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        
        // 生成随机字符串
        for (int i = 0; i < n; i++) {
            char c = (char) ('a' + random.nextInt(26));
            sb.append(c);
        }
        String s = sb.toString();
        
        System.out.println("测试数据规模: " + n + "个字符");
        
        // 测试基础DP算法
        long startTime = System.currentTimeMillis();
        int result1 = longestPalindromeSubseq(s);
        long endTime = System.currentTimeMillis();
        System.out.println("基础DP算法:");
        System.out.println("  结果: " + result1);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 测试优化DP算法
        startTime = System.currentTimeMillis();
        int result2 = longestPalindromeSubseqOptimized(s);
        endTime = System.currentTimeMillis();
        System.out.println("优化DP算法:");
        System.out.println("  结果: " + result2);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 验证结果一致性
        if (result1 == result2) {
            System.out.println("结果一致性验证: 通过 ✓");
        } else {
            System.out.println("结果一致性验证: 失败 ✗");
        }
        
        System.out.println("注意：记忆化搜索在大规模数据下可能栈溢出");
    }
    
    /**
     * 复杂度分析详细计算：
     * 
     * 基础动态规划：
     * - 时间：外层循环n次，内层循环n次 → O(n²)
     * - 空间：二维dp数组大小n×n → O(n²)
     * - 最优解：是理论最优，但空间使用较大
     * 
     * 空间优化版本：
     * - 时间：O(n²)，与基础版本相同
     * - 空间：一维数组大小n → O(n)
     * - 最优解：是，综合性能最好
     * 
     * 记忆化搜索：
     * - 时间：O(n²)，每个状态只计算一次
     * - 空间：O(n²) 记忆化数组 + O(n) 递归栈 → O(n²)
     * - 最优解：是，但可能栈溢出
     * 
     * 与LCS的关系：
     * 最长回文子序列问题可以转化为LCS问题：
     * LPS(s) = LCS(s, reverse(s))
     * 即字符串s的最长回文子序列长度等于s和s的逆序字符串的最长公共子序列长度。
     * 
     * 工程选择依据：
     * 1. 对于小规模数据：任意方法都可
     * 2. 对于中等规模数据：优先选择空间优化版本
     * 3. 对于大规模数据：空间优化版本避免内存不足
     * 
     * 算法调试技巧：
     * 1. 打印dp表观察填充过程
     * 2. 使用小规模测试用例验证正确性
     * 3. 添加断言验证关键假设
     */
}