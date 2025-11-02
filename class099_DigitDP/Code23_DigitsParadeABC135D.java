package class084;

/**
 * AtCoder ABC135 D - Digits Parade
 * 题目链接: https://atcoder.jp/contests/abc135/tasks/abc135_d
 * 
 * 题目描述:
 * 给定一个由数字和'?'组成的字符串S，'?'可以替换成0-9的任意数字。
 * 求有多少种替换方案使得结果能被13整除，结果对10^9+7取模。
 * 
 * 解题思路:
 * 1. 数位DP方法：使用数位DP框架，逐位确定数字
 * 2. 状态设计需要记录：
 *    - 当前处理位置
 *    - 当前数字对13的余数
 * 3. 关键点：'?'可以替换为0-9的任意数字
 * 
 * 时间复杂度: O(n * 13)
 * 空间复杂度: O(n * 13)
 */
public class Code23_DigitsParadeABC135D {
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
    
    // 测试方法
    public static void main(String[] args) {
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
        
        // 测试用例4: 边界情况
        System.out.println("=== 边界测试 ===");
        System.out.println("空字符串: " + countDivisibleBy13(""));
        System.out.println("单个'?': " + countDivisibleBy13("?"));
        System.out.println("全'?': " + countDivisibleBy13("???"));
    }
}