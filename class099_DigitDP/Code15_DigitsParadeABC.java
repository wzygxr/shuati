package class084;

import java.util.*;

/**
 * AtCoder ABC135 D - Digits Parade
 * 题目链接：https://atcoder.jp/contests/abc135/tasks/abc135_d
 * 
 * 题目描述：
 * 给定一个由数字和'?'组成的字符串S，'?'可以替换成0-9的任意数字。
 * 求有多少种替换方案使得结果能被13整除，结果对10^9+7取模。
 * 
 * 解题思路：
 * 1. 数位DP方法：使用数位DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 当前数字对13的余数
 * 3. 关键点：'?'可以替换为0-9的任意数字
 * 
 * 时间复杂度分析：
 * - 状态数：字符串长度 × 13 ≈ 10^5 × 13 = 1.3×10^6
 * - 每个状态处理最多10种选择
 * - 总复杂度：O(13×10^5) 在可接受范围内
 * 
 * 空间复杂度分析：
 * - 记忆化数组：10^5 × 13 ≈ 1.3MB
 * 
 * 最优解分析：
 * 这是标准的最优解，利用数位DP处理模运算和通配符替换
 */

public class Code15_DigitsParadeABC {
    private static final int MOD = 1000000007;
    private static final int DIVISOR = 13;
    
    /**
     * 计算有多少种替换方案使得结果能被13整除
     * 时间复杂度: O(n * 13)
     * 空间复杂度: O(n * 13)
     */
    public static int countDivisibleBy13(String s) {
        int n = s.length();
        char[] chars = s.toCharArray();
        
        // dp[i][r] 表示处理到第i位，当前余数为r的方案数
        long[][] dp = new long[n + 1][DIVISOR];
        dp[0][0] = 1;  // 初始状态：余数为0有1种方案（空数字）
        
        // 从高位到低位动态规划
        for (int i = 0; i < n; i++) {
            for (int r = 0; r < DIVISOR; r++) {
                if (dp[i][r] == 0) continue;
                
                if (chars[i] == '?') {
                    // '?'可以替换为0-9的任意数字
                    for (int d = 0; d <= 9; d++) {
                        int newR = (r * 10 + d) % DIVISOR;
                        dp[i + 1][newR] = (dp[i + 1][newR] + dp[i][r]) % MOD;
                    }
                } else {
                    // 固定数字
                    int d = chars[i] - '0';
                    int newR = (r * 10 + d) % DIVISOR;
                    dp[i + 1][newR] = (dp[i + 1][newR] + dp[i][r]) % MOD;
                }
            }
        }
        
        return (int) dp[n][0];
    }
    
    /**
     * 使用记忆化DFS的替代解法（更符合数位DP传统风格）
     * 时间复杂度: O(n * 13)
     * 空间复杂度: O(n * 13)
     */
    public static int countDivisibleBy13DFS(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        
        // 记忆化数组
        Long[][] memo = new Long[n][DIVISOR];
        
        return (int) dfs(chars, 0, 0, memo);
    }
    
    private static long dfs(char[] chars, int pos, int remainder, Long[][] memo) {
        // 递归终止条件：处理完所有字符
        if (pos == chars.length) {
            return (remainder == 0) ? 1 : 0;
        }
        
        // 记忆化搜索
        if (memo[pos][remainder] != null) {
            return memo[pos][remainder];
        }
        
        long count = 0;
        
        if (chars[pos] == '?') {
            // '?'可以替换为0-9的任意数字
            for (int d = 0; d <= 9; d++) {
                int newRemainder = (remainder * 10 + d) % DIVISOR;
                count = (count + dfs(chars, pos + 1, newRemainder, memo)) % MOD;
            }
        } else {
            // 固定数字
            int d = chars[pos] - '0';
            int newRemainder = (remainder * 10 + d) % DIVISOR;
            count = (count + dfs(chars, pos + 1, newRemainder, memo)) % MOD;
        }
        
        // 记忆化存储
        memo[pos][remainder] = count;
        return count;
    }
    
    /**
     * 单元测试函数
     */
    public static void testDigitsParade() {
        System.out.println("=== 测试Digits Parade ===");
        
        // 测试用例1: 简单情况
        String s1 = "??";
        int result1 = countDivisibleBy13(s1);
        int result1DFS = countDivisibleBy13DFS(s1);
        System.out.println("输入: " + s1);
        System.out.println("DP结果: " + result1);
        System.out.println("DFS结果: " + result1DFS);
        System.out.println("结果一致: " + (result1 == result1DFS));
        System.out.println("预期: 100种组合中有几个能被13整除");
        System.out.println();
        
        // 测试用例2: 固定数字
        String s2 = "13";
        int result2 = countDivisibleBy13(s2);
        int result2DFS = countDivisibleBy13DFS(s2);
        System.out.println("输入: " + s2);
        System.out.println("DP结果: " + result2);
        System.out.println("DFS结果: " + result2DFS);
        System.out.println("结果一致: " + (result2 == result2DFS));
        System.out.println("预期: 13能被13整除，所以为1");
        System.out.println();
        
        // 测试用例3: 混合情况
        String s3 = "1?2";
        int result3 = countDivisibleBy13(s3);
        int result3DFS = countDivisibleBy13DFS(s3);
        System.out.println("输入: " + s3);
        System.out.println("DP结果: " + result3);
        System.out.println("DFS结果: " + result3DFS);
        System.out.println("结果一致: " + (result3 == result3DFS));
        System.out.println();
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成测试用例
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append('?');
        }
        String longString = sb.toString();
        
        // 测试DP方法
        long startTimeDP = System.currentTimeMillis();
        int resultDP = countDivisibleBy13(longString.substring(0, 1000));
        long endTimeDP = System.currentTimeMillis();
        
        // 测试DFS方法
        long startTimeDFS = System.currentTimeMillis();
        int resultDFS = countDivisibleBy13DFS(longString.substring(0, 1000));
        long endTimeDFS = System.currentTimeMillis();
        
        System.out.println("字符串长度: 1000");
        System.out.println("DP方法耗时: " + (endTimeDP - startTimeDP) + "ms");
        System.out.println("DFS方法耗时: " + (endTimeDFS - startTimeDFS) + "ms");
        System.out.println("结果一致: " + (resultDP == resultDFS));
        System.out.println();
    }
    
    /**
     * 调试函数：验证特定字符串的替换方案
     */
    public static void debugDigitsParade() {
        System.out.println("=== 调试Digits Parade ===");
        
        String[] testCases = {
            "0", "1", "13", "26", "39", "52",
            "1?", "?3", "??", "1?3"
        };
        
        for (String s : testCases) {
            int result = countDivisibleBy13(s);
            System.out.println("输入: " + s + ", 方案数: " + result);
            
            // 对于短字符串，可以手动验证
            if (s.length() <= 2 && s.contains("?")) {
                System.out.print("  具体方案: ");
                int count = 0;
                for (int i = 0; i < Math.pow(10, s.length()); i++) {
                    String candidate = String.format("%0" + s.length() + "d", i);
                    boolean match = true;
                    for (int j = 0; j < s.length(); j++) {
                        if (s.charAt(j) != '?' && s.charAt(j) != candidate.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match && i % 13 == 0) {
                        System.out.print(candidate + " ");
                        count++;
                    }
                }
                System.out.println("\n  手动计数: " + count);
            }
            System.out.println();
        }
    }
    
    /**
     * 工程化考量总结：
     * 1. 模运算：结果对10^9+7取模，避免溢出
     * 2. 状态设计：合理设计状态参数，减少状态数
     * 3. 两种实现：提供DP和DFS两种解法，便于理解
     * 4. 性能优化：使用迭代DP避免递归栈开销
     * 5. 边界处理：正确处理空字符串和全'?'情况
     * 
     * 算法特色：
     * 1. 通配符处理：'?'可以替换为任意数字
     * 2. 模运算约束：结果必须能被13整除
     * 3. 动态规划：从高位到低位逐步计算
     * 4. 记忆化搜索：DFS解法更符合数位DP传统
     */
    
    public static void main(String[] args) {
        // 运行功能测试
        testDigitsParade();
        
        // 运行性能测试
        performanceTest();
        
        // 调试模式
        debugDigitsParade();
        
        // 边界测试
        System.out.println("=== 边界测试 ===");
        System.out.println("空字符串: " + countDivisibleBy13(""));
        System.out.println("单个'?': " + countDivisibleBy13("?"));
        System.out.println("全'?': " + countDivisibleBy13("???"));
    }
}