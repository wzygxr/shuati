// LeetCode 664. 奇怪的打印机
// 打印机有以下两个特殊要求：每次打印一个字符序列；每次可以打印任意数量的相同字符。
// 测试链接: https://leetcode.cn/problems/strange-printer/
// 
// 解题思路:
// 1. 状态定义：dp[i][j]表示打印区间[i,j]所需的最小打印次数
// 2. 状态转移：考虑两种策略：单独打印首字符，或者与后面相同字符一起打印
// 3. 时间复杂度：O(n^3)
// 4. 空间复杂度：O(n^2)
//
// 工程化考量:
// 1. 异常处理：检查输入字符串合法性
// 2. 边界处理：处理空字符串和单字符情况
// 3. 性能优化：使用区间DP标准模板
// 4. 测试覆盖：设计全面的测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code11_StrangePrinter {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        String s = br.readLine();
        out.println(strangePrinter(s));
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 区间DP解法
     * 时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
     * 空间复杂度: O(n^2) - dp数组占用空间
     * 
     * 解题思路:
     * 1. 状态定义：dp[i][j]表示打印字符串s在区间[i,j]所需的最小打印次数
     * 2. 状态转移：
     *    - 基础情况：dp[i][i] = 1（单个字符需要1次打印）
     *    - 如果s[i] == s[j]，则dp[i][j] = dp[i][j-1]（可以一起打印）
     *    - 否则，枚举分割点k：dp[i][j] = min(dp[i][k] + dp[k+1][j])
     * 3. 填表顺序：按区间长度从小到大
     */
    public static int strangePrinter(String s) {
        // 异常处理
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int n = s.length();
        
        // 状态定义：dp[i][j]表示打印区间[i,j]所需的最小打印次数
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符需要1次打印
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 枚举区间长度，从2开始
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 初始化dp[i][j]为较大值
                dp[i][j] = Integer.MAX_VALUE;
                
                // 策略1：如果首尾字符相同，可以一起打印
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i][j - 1];
                } else {
                    // 策略2：枚举分割点k，将区间分为[i,k]和[k+1,j]
                    for (int k = i; k < j; k++) {
                        dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j]);
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 优化版本 - 减少不必要的分割点枚举
     * 时间复杂度: O(n^3) 但实际运行更快
     * 空间复杂度: O(n^2)
     * 
     * 优化思路:
     * 1. 当s[i] == s[k]时，可以优化状态转移
     * 2. 减少重复计算
     */
    public static int strangePrinterOptimized(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int n = s.length();
        int[][] dp = new int[n][n];
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                // 关键优化：如果s[i] == s[k]，可以优化转移
                for (int k = i; k < j; k++) {
                    int temp = dp[i][k] + dp[k + 1][j];
                    if (s.charAt(i) == s.charAt(k)) {
                        // 进一步优化：如果首字符与分割点字符相同
                        temp = Math.min(temp, dp[i][k] + (k + 1 <= j ? dp[k + 1][j] - 1 : 0));
                    }
                    dp[i][j] = Math.min(dp[i][j], temp);
                }
                
                // 特殊处理：首尾字符相同的情况
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 记忆化搜索版本 - 递归+记忆化
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     * 
     * 优点: 代码更直观，易于理解
     * 缺点: 递归深度可能较大
     */
    public static int strangePrinterMemo(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int n = s.length();
        int[][] memo = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(memo[i], -1);
        }
        
        return dfs(s, 0, n - 1, memo);
    }
    
    private static int dfs(String s, int i, int j, int[][] memo) {
        if (i > j) {
            return 0;
        }
        
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        // 基础情况：单个字符
        if (i == j) {
            return 1;
        }
        
        int result = Integer.MAX_VALUE;
        
        // 策略1：单独打印首字符，然后打印剩余部分
        result = Math.min(result, 1 + dfs(s, i + 1, j, memo));
        
        // 策略2：如果首字符与后面某个字符相同，可以一起打印
        for (int k = i + 1; k <= j; k++) {
            if (s.charAt(i) == s.charAt(k)) {
                result = Math.min(result, dfs(s, i, k - 1, memo) + dfs(s, k + 1, j, memo));
            }
        }
        
        memo[i][j] = result;
        return result;
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        // 测试用例1：示例输入
        String s1 = "aaabbb";
        int result1 = strangePrinter(s1);
        System.out.println("Test 1 - Input: " + s1 + ", Expected: 2, Actual: " + result1);
        
        // 测试用例2：单个字符
        String s2 = "a";
        int result2 = strangePrinter(s2);
        System.out.println("Test 2 - Input: " + s2 + ", Expected: 1, Actual: " + result2);
        
        // 测试用例3：所有字符相同
        String s3 = "aaaaaaaa";
        int result3 = strangePrinter(s3);
        System.out.println("Test 3 - Input: " + s3 + ", Expected: 1, Actual: " + result3);
        
        // 测试用例4：交替字符
        String s4 = "ababab";
        int result4 = strangePrinter(s4);
        System.out.println("Test 4 - Input: " + s4 + ", Expected: 4, Actual: " + result4);
        
        // 验证不同方法的正确性
        int result1_opt = strangePrinterOptimized(s1);
        int result1_memo = strangePrinterMemo(s1);
        System.out.println("Validation - Basic: " + result1 + ", Optimized: " + result1_opt + ", Memo: " + result1_memo);
        
        assert result1 == result1_opt : "Different methods should give same result";
        assert result1 == result1_memo : "Different methods should give same result";
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成测试数据
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append((char)('a' + i % 26));
        }
        String testStr = sb.toString();
        
        long startTime = System.currentTimeMillis();
        int result = strangePrinter(testStr);
        long endTime = System.currentTimeMillis();
        
        System.out.println("Performance Test - Length: " + testStr.length() + 
                          ", Result: " + result + ", Time: " + (endTime - startTime) + "ms");
    }
}