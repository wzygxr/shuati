import java.util.Arrays;

/**
 * 不同的子序列
 * 给你两个字符串s和t，统计并返回在s的子序列中t出现的个数
 * 答案对 1000000007 取模
 * 
 * 题目来源：LeetCode 115. 不同的子序列
 * 测试链接：https://leetcode.cn/problems/distinct-subsequences/
 * 
 * 算法核心思想：
 * 使用动态规划解决子序列计数问题，关键在于理解状态转移方程和边界条件
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n*m)，其中n为s的长度，m为t的长度
 * - 空间优化版本：O(n*m)时间，O(m)空间
 * 
 * 空间复杂度分析：
 * - 基础版本：O(n*m)
 * - 空间优化版本：O(m)
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(min(n,m))
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理空字符串和极端情况
 * 3. 性能优化：使用滚动数组减少空间占用
 * 4. 数值安全：使用取模运算防止整数溢出
 * 5. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 自然语言处理：文本相似度计算、模式匹配
 * - 生物信息学：DNA序列比对、基因序列分析
 * - 信息检索：文档相似度计算、搜索引擎优化
 */
public class Code01_DistinctSubsequences {

    /**
     * 基础动态规划解法
     * 使用二维DP数组存储中间结果
     * 
     * 状态定义：
     * dp[i][j] 表示在字符串s的前i个字符中，可以组成字符串t的前j个字符的子序列数量
     * 
     * 状态转移方程：
     * 1. 如果s[i-1] == t[j-1]：可以选择使用或不使用当前字符
     *    dp[i][j] = dp[i-1][j] + dp[i-1][j-1]
     * 2. 如果s[i-1] != t[j-1]：只能不使用当前字符
     *    dp[i][j] = dp[i-1][j]
     * 
     * 边界条件：
     * - dp[i][0] = 1：t为空字符串时，只有空子序列一种方案
     * - dp[0][j] = 0 (j>0)：s为空字符串时，无法组成非空的t
     * 
     * @param str 源字符串s
     * @param target 目标字符串t
     * @return s的子序列中t出现的个数
     */
    public static int numDistinct1(String str, String target) {
        // 输入验证
        if (str == null || target == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        int n = s.length;
        int m = t.length;
        
        // 边界情况处理
        if (m == 0) return 1; // 目标字符串为空
        if (n == 0) return 0; // 源字符串为空
        
        // dp[i][j]: s[0..i-1]的子序列中等于t[0..j-1]的数量
        int[][] dp = new int[n + 1][m + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1; // t为空字符串时，只有空子序列
        }
        // dp[0][j]对于j>0默认为0，符合逻辑
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // 默认情况：不使用s[i-1]字符
                dp[i][j] = dp[i - 1][j];
                
                // 如果当前字符匹配，可以增加使用当前字符的方案数
                if (s[i - 1] == t[j - 1]) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        
        return dp[n][m];
    }

    /**
     * 空间优化版本 - 使用一维数组
     * 通过观察状态转移方程，发现dp[i][j]只依赖于dp[i-1][j]和dp[i-1][j-1]
     * 因此可以使用滚动数组技术优化空间复杂度
     * 
     * 关键技巧：
     * 1. 从右向左遍历，避免覆盖需要使用的历史值
     * 2. 使用一维数组dp[j]表示当前行的状态
     * 
     * @param str 源字符串s
     * @param target 目标字符串t
     * @return s的子序列中t出现的个数
     */
    public static int numDistinct2(String str, String target) {
        // 输入验证
        if (str == null || target == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        int n = s.length;
        int m = t.length;
        
        // 边界情况处理
        if (m == 0) return 1;
        if (n == 0) return 0;
        
        // 使用一维数组优化空间
        int[] dp = new int[m + 1];
        dp[0] = 1; // 基础情况：t为空字符串
        
        // 按行更新DP数组
        for (int i = 1; i <= n; i++) {
            // 从右向左遍历，避免覆盖需要的历史值
            for (int j = m; j >= 1; j--) {
                if (s[i - 1] == t[j - 1]) {
                    dp[j] += dp[j - 1];
                }
                // 如果不匹配，dp[j]保持不变（相当于dp[i][j] = dp[i-1][j]）
            }
        }
        
        return dp[m];
    }

    /**
     * 带取模运算的工业级版本
     * 处理大数溢出问题，符合题目要求对1000000007取模
     * 
     * 工程化改进：
     * 1. 添加取模运算防止整数溢出
     * 2. 更严格的输入验证
     * 3. 性能与安全的平衡
     * 
     * @param str 源字符串s
     * @param target 目标字符串t
     * @return s的子序列中t出现的个数，对1000000007取模
     */
    public static int numDistinct3(String str, String target) {
        // 严格的输入验证
        if (str == null || target == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        final int MOD = 1000000007;
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        int n = s.length;
        int m = t.length;
        
        // 边界情况处理
        if (m == 0) return 1;
        if (n == 0) return 0;
        if (m > n) return 0; // t比s长，不可能有子序列
        
        // 空间优化的一维DP数组
        int[] dp = new int[m + 1];
        dp[0] = 1;
        
        // 动态规划过程
        for (int i = 1; i <= n; i++) {
            // 从右向左遍历，避免覆盖
            for (int j = m; j >= 1; j--) {
                if (s[i - 1] == t[j - 1]) {
                    dp[j] = (dp[j] + dp[j - 1]) % MOD;
                }
                // 注意：这里不需要else分支，因为不匹配时dp[j]保持不变
            }
        }
        
        return dp[m];
    }

    /**
     * 递归解法（用于理解和对比）
     * 虽然效率较低，但有助于理解问题本质
     * 包含记忆化优化
     * 
     * @param s 源字符串s
     * @param t 目标字符串t
     * @return s的子序列中t出现的个数
     */
    public static int numDistinctRecursive(String s, String t) {
        int[][] memo = new int[s.length()][t.length()];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs(s, t, 0, 0, memo);
    }
    
    private static int dfs(String s, String t, int i, int j, int[][] memo) {
        // 目标字符串匹配完成
        if (j == t.length()) return 1;
        // 源字符串用完但目标字符串未完成
        if (i == s.length()) return 0;
        
        // 检查记忆化结果
        if (memo[i][j] != -1) return memo[i][j];
        
        int result = 0;
        // 情况1：使用当前字符（如果匹配）
        if (s.charAt(i) == t.charAt(j)) {
            result += dfs(s, t, i + 1, j + 1, memo);
        }
        // 情况2：不使用当前字符
        result += dfs(s, t, i + 1, j, memo);
        
        memo[i][j] = result;
        return result;
    }

    /**
     * 全面的单元测试
     * 覆盖各种边界情况和常见场景
     */
    public static void main(String[] args) {
        System.out.println("=== 不同的子序列算法测试 ===");
        
        // 测试用例1：基本功能测试
        testCase("rabbbit", "rabbit", 3, "基本功能测试");
        
        // 测试用例2：空目标字符串
        testCase("abc", "", 1, "空目标字符串测试");
        
        // 测试用例3：空源字符串
        testCase("", "abc", 0, "空源字符串测试");
        
        // 测试用例4：完全相同字符串
        testCase("abc", "abc", 1, "完全相同字符串测试");
        
        // 测试用例5：无匹配情况
        testCase("abc", "def", 0, "无匹配情况测试");
        
        // 测试用例6：单个字符
        testCase("aaa", "a", 3, "单个字符匹配测试");
        
        // 测试用例7：LeetCode官方测试用例
        testCase("babgbag", "bag", 5, "LeetCode测试用例");
        
        // 测试用例8：大数测试（取模功能）
        testCase("a".repeat(1000), "a".repeat(10), 1, "大数测试");
        
        // 性能对比测试
        performanceTest();
        
        System.out.println("=== 所有测试通过 ===");
    }
    
    private static void testCase(String s, String t, int expected, String description) {
        System.out.println("\n测试: " + description);
        System.out.println("输入: s = \"" + s + "\", t = \"" + t + "\"");
        System.out.println("预期结果: " + expected);
        
        int result1 = numDistinct1(s, t);
        int result2 = numDistinct2(s, t);
        int result3 = numDistinct3(s, t);
        
        System.out.println("方法1结果: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("方法2结果: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        System.out.println("方法3结果: " + result3 + " " + (result3 == expected ? "✓" : "✗"));
        
        if (result1 == expected && result2 == expected && result3 == expected) {
            System.out.println("✅ 测试通过");
        } else {
            System.out.println("❌ 测试失败");
            throw new AssertionError("测试用例失败: " + description);
        }
    }
    
    private static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成测试数据
        String s = "abcde".repeat(200); // 1000字符
        String t = "ace".repeat(66);    // 198字符
        
        long startTime, endTime;
        
        // 测试方法1（二维DP）
        startTime = System.nanoTime();
        int result1 = numDistinct1(s, t);
        endTime = System.nanoTime();
        System.out.println("二维DP耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试方法2（一维DP）
        startTime = System.nanoTime();
        int result2 = numDistinct2(s, t);
        endTime = System.nanoTime();
        System.out.println("一维DP耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试方法3（带取模）
        startTime = System.nanoTime();
        int result3 = numDistinct3(s, t);
        endTime = System.nanoTime();
        System.out.println("取模版本耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        System.out.println("结果一致性: " + (result1 == result2 && result2 == result3 ? "✅" : "❌"));
    }
    
    /**
     * 调试工具：打印DP表（用于理解算法过程）
     */
    public static void printDPTable(String s, String t) {
        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();
        int n = sArr.length;
        int m = tArr.length;
        
        int[][] dp = new int[n + 1][m + 1];
        
        // 初始化
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = dp[i - 1][j];
                if (sArr[i - 1] == tArr[j - 1]) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        
        // 打印DP表
        System.out.println("DP表:");
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
    }
}