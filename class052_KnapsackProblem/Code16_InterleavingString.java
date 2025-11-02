package class074;

// 交错字符串
// 给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错组成的
// 测试链接 : https://leetcode.cn/problems/interleaving-string/

/*
 * 算法详解：
 * 交错字符串问题是一个经典的动态规划问题，用于验证字符串s3是否由s1和s2交错组成。
 * 交错意味着s3中的字符顺序必须保持s1和s2中字符的相对顺序。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示s1的前i个字符和s2的前j个字符能否交错组成s3的前i+j个字符
 * 2. 状态转移方程：
 *    - 如果s1[i-1] == s3[i+j-1]，则dp[i][j] = dp[i][j] || dp[i-1][j]
 *    - 如果s2[j-1] == s3[i+j-1]，则dp[i][j] = dp[i][j] || dp[i][j-1]
 * 3. 初始化：
 *    - dp[0][0] = true（两个空字符串可以组成空字符串）
 *    - dp[i][0] = (s1的前i个字符等于s3的前i个字符)
 *    - dp[0][j] = (s2的前j个字符等于s3的前j个字符)
 * 
 * 时间复杂度分析：
 * 设s1长度为m，s2长度为n
 * 1. 动态规划计算：O(m * n)
 * 总时间复杂度：O(m * n)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(m * n)
 * 2. 空间优化后：O(min(m, n))
 * 
 * 相关题目扩展：
 * 1. LeetCode 97. 交错字符串（本题）
 * 2. LeetCode 115. 不同的子序列
 * 3. LeetCode 1143. 最长公共子序列
 * 4. LeetCode 72. 编辑距离
 * 5. LeetCode 10. 正则表达式匹配
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空字符串、长度不匹配等边界情况
 * 3. 可配置性：可以将字符串比较逻辑作为参数传入
 * 4. 单元测试：为isInterleave方法编写测试用例
 * 5. 性能优化：对于长字符串，使用空间优化版本
 * 
 * 语言特性差异：
 * 1. Java：使用charAt方法访问字符，性能较好
 * 2. 字符串操作：Java字符串不可变，需要注意性能影响
 * 3. 内存管理：自动垃圾回收，无需手动管理内存
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 空间压缩：从二维dp优化到一维dp
 * 2. 提前终止：当发现长度不匹配时直接返回false
 * 3. 并行计算：对于大规模数据，可以考虑分块并行计算
 * 
 * 应用场景：
 * 1. 文本处理：验证字符串的组成结构
 * 2. 编译器：语法分析中的字符串匹配
 * 3. 生物信息学：DNA序列的比对分析
 */

public class Code16_InterleavingString {
    
    // 标准二维DP版本
    public static boolean isInterleave(String s1, String s2, String s3) {
        int m = s1.length();
        int n = s2.length();
        int len = s3.length();
        
        // 长度检查
        if (m + n != len) return false;
        
        // 创建DP数组
        boolean[][] dp = new boolean[m + 1][n + 1];
        
        // 初始化
        dp[0][0] = true;
        
        // 初始化第一列：只使用s1
        for (int i = 1; i <= m; i++) {
            dp[i][0] = dp[i - 1][0] && (s1.charAt(i - 1) == s3.charAt(i - 1));
        }
        
        // 初始化第一行：只使用s2
        for (int j = 1; j <= n; j++) {
            dp[0][j] = dp[0][j - 1] && (s2.charAt(j - 1) == s3.charAt(j - 1));
        }
        
        // 填充DP数组
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char c3 = s3.charAt(i + j - 1);
                
                // 检查是否可以从s1取字符
                if (s1.charAt(i - 1) == c3) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j];
                }
                
                // 检查是否可以从s2取字符
                if (s2.charAt(j - 1) == c3) {
                    dp[i][j] = dp[i][j] || dp[i][j - 1];
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 空间优化版本（使用一维数组）
    public static boolean isInterleaveOptimized(String s1, String s2, String s3) {
        int m = s1.length();
        int n = s2.length();
        int len = s3.length();
        
        // 长度检查
        if (m + n != len) return false;
        
        // 为了节省空间，让s2作为较短的字符串
        if (m < n) {
            return isInterleaveOptimized(s2, s1, s3);
        }
        
        // 使用一维DP数组
        boolean[] dp = new boolean[n + 1];
        
        // 初始化
        dp[0] = true;
        
        // 初始化第一行：只使用s2
        for (int j = 1; j <= n; j++) {
            dp[j] = dp[j - 1] && (s2.charAt(j - 1) == s3.charAt(j - 1));
        }
        
        // 填充DP数组
        for (int i = 1; i <= m; i++) {
            // 更新第一列：只使用s1
            dp[0] = dp[0] && (s1.charAt(i - 1) == s3.charAt(i - 1));
            
            for (int j = 1; j <= n; j++) {
                char c3 = s3.charAt(i + j - 1);
                boolean result = false;
                
                // 检查是否可以从s1取字符
                if (s1.charAt(i - 1) == c3) {
                    result = result || dp[j];  // 相当于dp[i-1][j]
                }
                
                // 检查是否可以从s2取字符
                if (s2.charAt(j - 1) == c3) {
                    result = result || dp[j - 1];  // 相当于dp[i][j-1]
                }
                
                dp[j] = result;
            }
        }
        
        return dp[n];
    }
    
    // DFS + 记忆化搜索版本
    public static boolean isInterleaveMemo(String s1, String s2, String s3) {
        int m = s1.length();
        int n = s2.length();
        int len = s3.length();
        
        if (m + n != len) return false;
        
        Boolean[][] memo = new Boolean[m + 1][n + 1];
        return dfs(s1, s2, s3, 0, 0, 0, memo);
    }
    
    private static boolean dfs(String s1, String s2, String s3, int i, int j, int k, Boolean[][] memo) {
        if (i == s1.length() && j == s2.length()) {
            return k == s3.length();
        }
        
        if (memo[i][j] != null) {
            return memo[i][j];
        }
        
        boolean result = false;
        
        // 尝试从s1取字符
        if (i < s1.length() && s1.charAt(i) == s3.charAt(k)) {
            result = result || dfs(s1, s2, s3, i + 1, j, k + 1, memo);
        }
        
        // 尝试从s2取字符
        if (j < s2.length() && s2.charAt(j) == s3.charAt(k)) {
            result = result || dfs(s1, s2, s3, i, j + 1, k + 1, memo);
        }
        
        memo[i][j] = result;
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1_1 = "aabcc";
        String s2_1 = "dbbca";
        String s3_1 = "aadbbcbcac";
        System.out.println("测试用例1:");
        System.out.println("标准版本: " + isInterleave(s1_1, s2_1, s3_1));
        System.out.println("优化版本: " + isInterleaveOptimized(s1_1, s2_1, s3_1));
        System.out.println("记忆化版本: " + isInterleaveMemo(s1_1, s2_1, s3_1));
        System.out.println("预期结果: true");
        System.out.println();
        
        // 测试用例2
        String s1_2 = "aabcc";
        String s2_2 = "dbbca";
        String s3_2 = "aadbbbaccc";
        System.out.println("测试用例2:");
        System.out.println("标准版本: " + isInterleave(s1_2, s2_2, s3_2));
        System.out.println("优化版本: " + isInterleaveOptimized(s1_2, s2_2, s3_2));
        System.out.println("记忆化版本: " + isInterleaveMemo(s1_2, s2_2, s3_2));
        System.out.println("预期结果: false");
        System.out.println();
        
        // 测试用例3：边界情况
        String s1_3 = "";
        String s2_3 = "";
        String s3_3 = "";
        System.out.println("测试用例3（空字符串）:");
        System.out.println("标准版本: " + isInterleave(s1_3, s2_3, s3_3));
        System.out.println("优化版本: " + isInterleaveOptimized(s1_3, s2_3, s3_3));
        System.out.println("记忆化版本: " + isInterleaveMemo(s1_3, s2_3, s3_3));
        System.out.println("预期结果: true");
        System.out.println();
        
        // 测试用例4：长度不匹配
        String s1_4 = "abc";
        String s2_4 = "def";
        String s3_4 = "abcd";
        System.out.println("测试用例4（长度不匹配）:");
        System.out.println("标准版本: " + isInterleave(s1_4, s2_4, s3_4));
        System.out.println("优化版本: " + isInterleaveOptimized(s1_4, s2_4, s3_4));
        System.out.println("记忆化版本: " + isInterleaveMemo(s1_4, s2_4, s3_4));
        System.out.println("预期结果: false");
    }
    
    /*
     * =============================================================================================
     * 补充题目：LeetCode 115. 不同的子序列
     * 题目链接：https://leetcode.cn/problems/distinct-subsequences/
     * 题目描述：给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。
     * 字符串的一个子序列是指，通过删除一些（也可以不删除）字符且不干扰剩余字符相对位置所组成的新字符串。
     * 
     * 解题思路：
     * 这是一个字符串动态规划问题，需要计算s中包含t作为子序列的不同方式数。
     * 
     * 状态定义：dp[i][j]表示s的前i个字符中，t的前j个字符作为子序列出现的次数
     * 状态转移方程：
     * - 如果s[i-1] == t[j-1]，则dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
     *   （选择匹配当前字符 或 不选择当前字符）
     * - 否则，dp[i][j] = dp[i-1][j]（只能不选择当前字符）
     * 初始条件：
     * - dp[i][0] = 1（空字符串是任何字符串的子序列）
     * - dp[0][j] = 0 (j > 0)（空字符串不包含非空子序列）
     * 
     * 时间复杂度：O(m * n)，其中m和n分别是s和t的长度
     * 空间复杂度：O(m * n)，可以优化到O(n)
     * 
     * Java实现：
     * public int numDistinct(String s, String t) {
     *     int m = s.length();
     *     int n = t.length();
     *     
     *     // 快速判断特殊情况
     *     if (m < n) return 0;
     *     if (m == n) return s.equals(t) ? 1 : 0;
     *     
     *     long[][] dp = new long[m + 1][n + 1];
     *     
     *     // 初始化
     *     for (int i = 0; i <= m; i++) {
     *         dp[i][0] = 1;
     *     }
     *     
     *     for (int i = 1; i <= m; i++) {
     *         for (int j = 1; j <= n; j++) {
     *             if (s.charAt(i - 1) == t.charAt(j - 1)) {
     *                 dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
     *             } else {
     *                 dp[i][j] = dp[i - 1][j];
     *             }
     *         }
     *     }
     *     
     *     return (int) dp[m][n];
     * }
     * 
     * 工程化考量：
     * 1. 数值溢出：使用long类型存储中间结果，避免溢出
     * 2. 模运算：及时取模，防止数值过大
     * 3. 边界条件：快速处理m < n等特殊情况
     * 
     * 优化思路：
     * 1. 空间压缩：使用一维数组进行优化
     * 2. 前缀和优化：对于某些模式可以使用前缀和进一步优化
     * 3. 哈希表：预处理t中每个字符的位置，加速匹配过程
     */
}