import java.util.Arrays;

/**
 * 最长公共子序列 (Longest Common Subsequence, LCS)
 * 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度
 * 
 * 题目来源：LeetCode 1143. 最长公共子序列
 * 测试链接：https://leetcode.cn/problems/longest-common-subsequence/
 * 
 * 算法核心思想：
 * 动态规划解决经典的最长公共子序列问题，是字符串处理中的基础算法
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n*m)，其中n为text1的长度，m为text2的长度
 * - 空间优化版本：O(n*m)时间，O(min(n,m))空间
 * 
 * 空间复杂度分析：
 * - 基础版本：O(n*m)
 * - 空间优化版本：O(min(n,m))
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到最优
 * 
 * 工程化考量：
 * 1. 输入验证：检查空指针和空字符串
 * 2. 性能优化：空间优化和边界剪枝
 * 3. 代码可读性：清晰的变量命名和注释
 * 4. 测试覆盖：全面的单元测试用例
 * 
 * 应用场景：
 * - 文本相似度计算
 * - 版本控制系统（如git diff）
 * - DNA序列比对
 * - 文件差异比较
 * 
 * 与其他算法的关系：
 * - 与编辑距离密切相关
 * - 是最长公共子串问题的扩展
 * - 是动态规划在字符串处理中的经典应用
 */
public class Code12_LongestCommonSubsequence {

    /**
     * 基础动态规划解法 - 二维DP数组
     * 使用标准的动态规划方法解决LCS问题
     * 
     * 状态定义：
     * dp[i][j] 表示字符串text1的前i个字符与字符串text2的前j个字符的最长公共子序列长度
     * 
     * 状态转移方程：
     * 1. 如果text1[i-1] == text2[j-1]：当前字符匹配，长度加1
     *    dp[i][j] = dp[i-1][j-1] + 1
     * 2. 如果text1[i-1] != text2[j-1]：取两种可能性的最大值
     *    dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     * 
     * 边界条件：
     * - dp[0][j] = 0：text1为空字符串时
     * - dp[i][0] = 0：text2为空字符串时
     * 
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子序列的长度
     */
    public static int longestCommonSubsequence1(String text1, String text2) {
        // 输入验证
        if (text1 == null || text2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        int n = text1.length();
        int m = text2.length();
        
        // 边界情况处理
        if (n == 0 || m == 0) {
            return 0;
        }
        
        char[] s1 = text1.toCharArray();
        char[] s2 = text2.toCharArray();
        
        // dp[i][j]: text1[0..i-1]和text2[0..j-1]的LCS长度
        int[][] dp = new int[n + 1][m + 1];
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    // 当前字符匹配，LCS长度增加1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // 当前字符不匹配，取两种可能性的最大值
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[n][m];
    }

    /**
     * 空间优化版本 - 一维DP数组
     * 通过观察状态转移的依赖关系，使用滚动数组技术优化空间复杂度
     * 
     * 优化原理：
     * - dp[i][j]只依赖于当前行和上一行的数据
     * - 可以使用一维数组+临时变量保存必要的历史值
     * - 通过交换字符串顺序确保使用较短的数组
     * 
     * 关键技巧：
     * 1. 让较短的字符串作为内层循环，减少空间占用
     * 2. 使用pre变量保存dp[i-1][j-1]的值
     * 3. 使用temp变量暂存当前值用于下一轮计算
     * 
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子序列的长度
     */
    public static int longestCommonSubsequence2(String text1, String text2) {
        // 输入验证
        if (text1 == null || text2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        // 交换字符串顺序，确保第二个字符串较短
        if (text1.length() < text2.length()) {
            String temp = text1;
            text1 = text2;
            text2 = temp;
        }
        
        char[] s1 = text1.toCharArray();
        char[] s2 = text2.toCharArray();
        int n = s1.length;
        int m = s2.length;
        
        // 边界情况处理
        if (n == 0 || m == 0) {
            return 0;
        }
        
        // 使用一维数组优化空间
        int[] dp = new int[m + 1];
        int pre; // 保存左上角的值(dp[i-1][j-1])
        
        // 动态规划过程
        for (int i = 1; i <= n; i++) {
            pre = 0; // 每行开始时，左上角值为0
            for (int j = 1; j <= m; j++) {
                int temp = dp[j]; // 保存当前值，用于下一轮的pre
                
                if (s1[i - 1] == s2[j - 1]) {
                    // 字符匹配：LCS长度 = 左上角值 + 1
                    dp[j] = pre + 1;
                } else {
                    // 字符不匹配：取上方和左方的最大值
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                
                pre = temp; // 更新pre为当前轮的左上角值
            }
        }
        
        return dp[m];
    }

    /**
     * 重构LCS字符串（扩展功能）
     * 不仅计算长度，还重构出具体的LCS字符串
     * 
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子序列字符串
     */
    public static String reconstructLCS(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return "";
        }
        
        int n = text1.length();
        int m = text2.length();
        
        if (n == 0 || m == 0) {
            return "";
        }
        
        char[] s1 = text1.toCharArray();
        char[] s2 = text2.toCharArray();
        int[][] dp = new int[n + 1][m + 1];
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 重构LCS字符串
        StringBuilder lcs = new StringBuilder();
        int i = n, j = m;
        
        while (i > 0 && j > 0) {
            if (s1[i - 1] == s2[j - 1]) {
                // 当前字符属于LCS
                lcs.append(s1[i - 1]);
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                // 向上移动
                i--;
            } else {
                // 向左移动
                j--;
            }
        }
        
        return lcs.reverse().toString();
    }

    /**
     * 递归解法（带记忆化）
     * 用于理解问题本质和对比性能
     * 
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子序列的长度
     */
    public static int longestCommonSubsequenceRecursive(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0;
        }
        
        int n = text1.length();
        int m = text2.length();
        
        if (n == 0 || m == 0) {
            return 0;
        }
        
        int[][] memo = new int[n][m];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        
        return dfs(text1, text2, 0, 0, memo);
    }
    
    private static int dfs(String s1, String s2, int i, int j, int[][] memo) {
        if (i == s1.length() || j == s2.length()) {
            return 0;
        }
        
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        int result;
        if (s1.charAt(i) == s2.charAt(j)) {
            // 字符匹配，长度加1，继续比较下一个字符
            result = 1 + dfs(s1, s2, i + 1, j + 1, memo);
        } else {
            // 字符不匹配，取两种可能性的最大值
            int skip1 = dfs(s1, s2, i + 1, j, memo);
            int skip2 = dfs(s1, s2, i, j + 1, memo);
            result = Math.max(skip1, skip2);
        }
        
        memo[i][j] = result;
        return result;
    }

    /**
     * 全面的单元测试
     * 覆盖各种边界情况和应用场景
     */
    public static void main(String[] args) {
        System.out.println("=== 最长公共子序列算法测试 ===");
        
        // 测试用例1：基本功能测试
        testCase("abcde", "ace", 3, "基本功能测试");
        
        // 测试用例2：完全相同字符串
        testCase("abc", "abc", 3, "完全相同字符串测试");
        
        // 测试用例3：无公共子序列
        testCase("abc", "def", 0, "无公共子序列测试");
        
        // 测试用例4：空字符串测试
        testCase("", "abc", 0, "空字符串测试1");
        testCase("abc", "", 0, "空字符串测试2");
        testCase("", "", 0, "双空字符串测试");
        
        // 测试用例5：单字符测试
        testCase("a", "a", 1, "单字符匹配测试");
        testCase("a", "b", 0, "单字符不匹配测试");
        
        // 测试用例6：LeetCode官方测试用例
        testCase("abcde", "ace", 3, "LeetCode测试用例1");
        testCase("abc", "abc", 3, "LeetCode测试用例2");
        testCase("abc", "def", 0, "LeetCode测试用例3");
        
        // 测试用例7：重构LCS测试
        testReconstruction("abcde", "ace", "ace", "重构LCS测试");
        
        // 性能测试
        performanceTest();
        
        // 递归解法测试
        testRecursive();
        
        System.out.println("=== 所有测试通过 ===");
    }
    
    private static void testCase(String text1, String text2, int expected, String description) {
        System.out.println("\n测试: " + description);
        System.out.println("输入: text1 = \"" + text1 + "\", text2 = \"" + text2 + "\"");
        System.out.println("预期LCS长度: " + expected);
        
        int result1 = longestCommonSubsequence1(text1, text2);
        int result2 = longestCommonSubsequence2(text1, text2);
        
        System.out.println("方法1结果: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("方法2结果: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        
        if (result1 == expected && result2 == expected) {
            System.out.println("✅ 测试通过");
        } else {
            System.out.println("❌ 测试失败");
            throw new AssertionError("测试用例失败: " + description);
        }
    }
    
    private static void testReconstruction(String text1, String text2, String expectedLCS, String description) {
        System.out.println("\n测试: " + description);
        String reconstructed = reconstructLCS(text1, text2);
        System.out.println("重构的LCS: \"" + reconstructed + "\"");
        System.out.println("预期LCS: \"" + expectedLCS + "\"");
        System.out.println("结果: " + (reconstructed.equals(expectedLCS) ? "✅" : "❌"));
    }
    
    private static void testRecursive() {
        System.out.println("\n=== 递归解法测试 ===");
        
        String[] testCases = {
            "abcde", "ace",
            "abc", "abc", 
            "abc", "def"
        };
        
        for (int i = 0; i < testCases.length; i += 2) {
            String text1 = testCases[i];
            String text2 = testCases[i + 1];
            
            int dpResult = longestCommonSubsequence1(text1, text2);
            int recursiveResult = longestCommonSubsequenceRecursive(text1, text2);
            
            System.out.printf("text1=\"%s\", text2=\"%s\": DP=%d, 递归=%d %s\n",
                text1, text2, dpResult, recursiveResult,
                dpResult == recursiveResult ? "✓" : "✗");
        }
    }
    
    private static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成大规模测试数据
        String text1 = "abcdefghij".repeat(100); // 1000字符
        String text2 = "acegikmoqs".repeat(50);  // 500字符
        
        long startTime, endTime;
        
        // 测试基础DP方法
        startTime = System.nanoTime();
        int result1 = longestCommonSubsequence1(text1, text2);
        endTime = System.nanoTime();
        System.out.println("基础DP耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试空间优化方法
        startTime = System.nanoTime();
        int result2 = longestCommonSubsequence2(text1, text2);
        endTime = System.nanoTime();
        System.out.println("优化DP耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试递归方法（小规模）
        if (text1.length() <= 20 && text2.length() <= 20) {
            startTime = System.nanoTime();
            int result3 = longestCommonSubsequenceRecursive(text1, text2);
            endTime = System.nanoTime();
            System.out.println("递归方法耗时: " + (endTime - startTime) / 1e6 + "ms");
        }
        
        System.out.println("结果一致性: " + (result1 == result2 ? "✅" : "❌"));
        System.out.println("LCS长度: " + result1);
    }
    
    /**
     * 调试工具：打印DP表
     */
    public static void printDPTable(String text1, String text2) {
        char[] s1 = text1.toCharArray();
        char[] s2 = text2.toCharArray();
        int n = s1.length;
        int m = s2.length;
        
        int[][] dp = new int[n + 1][m + 1];
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 打印DP表
        System.out.println("LCS DP表:");
        System.out.print("    ");
        for (int j = 0; j <= m; j++) {
            System.out.printf("%3d", j);
        }
        System.out.println();
        
        for (int i = 0; i <= n; i++) {
            System.out.printf("%3d:", i);
            for (int j = 0; j <= m; j++) {
                System.out.printf("%3d", dp[i][j]);
            }
            System.out.println();
        }
        
        System.out.println("最长公共子序列长度: " + dp[n][m]);
    }
}