/**
 * 最小窗口子序列
 * 给定字符串S和T，在S中寻找最短的子串，使得T是该子串的子序列
 * 
 * 题目来源：LeetCode 727. 最小窗口子序列
 * 测试链接：https://leetcode.cn/problems/minimum-window-subsequence/
 * 
 * 算法核心思想：
 * 使用动态规划解决最小窗口子序列问题，是字符串匹配和子序列问题的结合
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n*m)，其中n为S的长度，m为T的长度
 * - 优化版本：O(n*m)时间，O(n*m)空间
 * 
 * 空间复杂度分析：
 * - 基础版本：O(n*m)
 * - 优化版本：O(n*m)
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化
 * 
 * 工程化考量：
 * 1. 输入验证：检查空指针和边界条件
 * 2. 性能优化：使用动态规划预处理
 * 3. 异常处理：处理各种异常情况
 * 4. 测试覆盖：全面的单元测试
 * 
 * 应用场景：
 * - 文本搜索和匹配
 * - 基因序列分析
 * - 模式识别
 */
public class Code16_MinimumWindowSubsequence {

    /**
     * 基础动态规划解法
     * 使用二维DP数组存储匹配信息
     * 
     * 状态定义：
     * dp[i][j] 表示在S的前i个字符中匹配T的前j个字符时，窗口的起始位置
     * 
     * 状态转移方程：
     * 1. 如果S[i-1] == T[j-1]：当前字符匹配
     *    dp[i][j] = (j == 1) ? i-1 : dp[i-1][j-1]
     * 2. 如果S[i-1] != T[j-1]：继续使用前一个字符的匹配信息
     *    dp[i][j] = dp[i-1][j]
     * 
     * @param S 源字符串
     * @param T 目标子序列
     * @return 包含T作为子序列的最小窗口
     */
    public static String minWindowSubsequence1(String S, String T) {
        if (S == null || T == null || S.length() == 0 || T.length() == 0) {
            return "";
        }
        
        int n = S.length();
        int m = T.length();
        
        // dp[i][j] 表示在S[0..i-1]中匹配T[0..j-1]时的起始位置
        int[][] dp = new int[n + 1][m + 1];
        
        // 初始化：当T为空字符串时，起始位置为0
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        // 初始化：当j>0时，初始化为-1表示未匹配
        for (int j = 1; j <= m; j++) {
            dp[0][j] = -1;
        }
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (S.charAt(i - 1) == T.charAt(j - 1)) {
                    if (j == 1) {
                        // 匹配T的第一个字符，起始位置为i-1
                        dp[i][j] = i - 1;
                    } else {
                        // 继续使用前一个匹配信息
                        dp[i][j] = dp[i - 1][j - 1];
                    }
                } else {
                    // 字符不匹配，继承前一个位置的匹配信息
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        
        // 寻找最小窗口
        int minLength = Integer.MAX_VALUE;
        int start = -1;
        
        for (int i = 1; i <= n; i++) {
            if (dp[i][m] != -1) {
                int currentLength = i - dp[i][m];
                if (currentLength < minLength) {
                    minLength = currentLength;
                    start = dp[i][m];
                }
            }
        }
        
        return (start == -1) ? "" : S.substring(start, start + minLength);
    }

    /**
     * 优化版本 - 使用双指针和动态规划预处理
     * 更高效地找到最小窗口
     * 
     * 算法思路：
     * 1. 预处理next数组，记录每个位置下一个字符的出现位置
     * 2. 使用双指针遍历所有可能的窗口
     * 3. 利用预处理信息快速判断是否包含子序列
     * 
     * @param S 源字符串
     * @param T 目标子序列
     * @return 包含T作为子序列的最小窗口
     */
    public static String minWindowSubsequence2(String S, String T) {
        if (S == null || T == null || S.length() == 0 || T.length() == 0) {
            return "";
        }
        
        int n = S.length();
        int m = T.length();
        
        // 预处理：记录每个位置下一个字符的出现位置
        int[][] next = new int[n + 1][26];
        for (int i = 0; i < 26; i++) {
            next[n][i] = -1;
        }
        
        // 从后向前填充next数组
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 26; j++) {
                next[i][j] = next[i + 1][j];
            }
            next[i][S.charAt(i) - 'a'] = i;
        }
        
        // 寻找最小窗口
        int minLength = Integer.MAX_VALUE;
        int start = -1;
        
        // 遍历所有可能的起始位置
        for (int i = 0; i < n; i++) {
            if (S.charAt(i) == T.charAt(0)) {
                int pos = i;
                boolean found = true;
                
                // 尝试匹配整个T
                for (int j = 1; j < m; j++) {
                    pos = next[pos + 1][T.charAt(j) - 'a'];
                    if (pos == -1) {
                        found = false;
                        break;
                    }
                }
                
                if (found) {
                    int length = pos - i + 1;
                    if (length < minLength) {
                        minLength = length;
                        start = i;
                    }
                }
            }
        }
        
        return (start == -1) ? "" : S.substring(start, start + minLength);
    }

    /**
     * 滑动窗口解法
     * 使用双指针技术优化性能
     * 
     * 算法思路：
     * 1. 使用左右指针维护一个窗口
     * 2. 向右扩展窗口直到包含整个T
     * 3. 向左收缩窗口寻找最小长度
     * 
     * @param S 源字符串
     * @param T 目标子序列
     * @return 包含T作为子序列的最小窗口
     */
    public static String minWindowSubsequence3(String S, String T) {
        if (S == null || T == null || S.length() == 0 || T.length() == 0) {
            return "";
        }
        
        int n = S.length();
        int m = T.length();
        int minLength = Integer.MAX_VALUE;
        int start = -1;
        
        // 遍历所有可能的起始位置
        for (int i = 0; i < n; i++) {
            if (S.charAt(i) == T.charAt(0)) {
                // 找到匹配T第一个字符的位置
                int tIndex = 0;
                int j = i;
                
                // 尝试匹配整个T
                while (j < n && tIndex < m) {
                    if (S.charAt(j) == T.charAt(tIndex)) {
                        tIndex++;
                    }
                    j++;
                }
                
                // 如果成功匹配整个T
                if (tIndex == m) {
                    // 从右向左收缩窗口
                    int end = j - 1;
                    tIndex = m - 1;
                    
                    for (int k = end; k >= i; k--) {
                        if (S.charAt(k) == T.charAt(tIndex)) {
                            tIndex--;
                        }
                        if (tIndex < 0) {
                            int length = end - k + 1;
                            if (length < minLength) {
                                minLength = length;
                                start = k;
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        return (start == -1) ? "" : S.substring(start, start + minLength);
    }

    /**
     * 单元测试
     */
    public static void main(String[] args) {
        System.out.println("=== 最小窗口子序列算法测试 ===");
        
        // 测试用例1：基本功能测试
        testCase("abcdebdde", "bde", "bcde", "基本功能测试");
        
        // 测试用例2：多个匹配窗口
        testCase("abbcabc", "abc", "abc", "多个匹配窗口测试");
        
        // 测试用例3：无匹配情况
        testCase("abcde", "xyz", "", "无匹配情况测试");
        
        // 测试用例4：完全相同字符串
        testCase("abc", "abc", "abc", "完全相同字符串测试");
        
        // 测试用例5：单字符匹配
        testCase("abc", "a", "a", "单字符匹配测试");
        
        // 测试用例6：LeetCode官方测试用例
        testCase("abcdebdde", "bde", "bcde", "LeetCode测试用例1");
        testCase("jmeqksfrsdcmsiwvaovztaqenprpvnbstl", "k", "k", "LeetCode测试用例2");
        
        // 性能测试
        performanceTest();
        
        System.out.println("=== 所有测试通过 ===");
    }
    
    private static void testCase(String S, String T, String expected, String description) {
        System.out.println("\n测试: " + description);
        System.out.println("输入: S = \"" + S + "\", T = \"" + T + "\"");
        System.out.println("预期结果: \"" + expected + "\"");
        
        String result1 = minWindowSubsequence1(S, T);
        String result2 = minWindowSubsequence2(S, T);
        String result3 = minWindowSubsequence3(S, T);
        
        System.out.println("方法1结果: \"" + result1 + "\" " + (result1.equals(expected) ? "✓" : "✗"));
        System.out.println("方法2结果: \"" + result2 + "\" " + (result2.equals(expected) ? "✓" : "✗"));
        System.out.println("方法3结果: \"" + result3 + "\" " + (result3.equals(expected) ? "✓" : "✗"));
        
        if (result1.equals(expected) && result2.equals(expected) && result3.equals(expected)) {
            System.out.println("✅ 测试通过");
        } else {
            System.out.println("❌ 测试失败");
            throw new AssertionError("测试用例失败: " + description);
        }
    }
    
    private static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成大规模测试数据
        String S = "abcdefghijklmnopqrstuvwxyz".repeat(100); // 2600字符
        String T = "abc";
        
        long startTime, endTime;
        
        // 测试方法1
        startTime = System.nanoTime();
        String result1 = minWindowSubsequence1(S, T);
        endTime = System.nanoTime();
        System.out.println("方法1耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试方法2
        startTime = System.nanoTime();
        String result2 = minWindowSubsequence2(S, T);
        endTime = System.nanoTime();
        System.out.println("方法2耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        // 测试方法3
        startTime = System.nanoTime();
        String result3 = minWindowSubsequence3(S, T);
        endTime = System.nanoTime();
        System.out.println("方法3耗时: " + (endTime - startTime) / 1e6 + "ms");
        
        System.out.println("结果一致性: " + 
            (result1.equals(result2) && result2.equals(result3) ? "✅" : "❌"));
    }
    
    /**
     * 调试工具：打印DP表
     */
    public static void printDPTable(String S, String T) {
        int n = S.length();
        int m = T.length();
        
        int[][] dp = new int[n + 1][m + 1];
        
        // 初始化
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (S.charAt(i - 1) == T.charAt(j - 1)) {
                    if (j == 1) {
                        dp[i][j] = i - 1;
                    } else {
                        dp[i][j] = dp[i - 1][j - 1];
                    }
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        
        // 打印DP表
        System.out.println("最小窗口子序列DP表:");
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