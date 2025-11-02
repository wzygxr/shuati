// LeetCode 357. 统计各位数字都不同的数字个数
// 题目描述：给你一个整数 n ，统计并返回各位数字都不同的数字 x 的个数，其中 0 <= x < 10^n。
// 测试链接：https://leetcode.cn/problems/count-numbers-with-unique-digits/
//
// 解题思路：
// 使用数位DP解决该问题。我们需要统计各位数字都不相同的数字个数。
// 状态定义：
// dp[pos][mask][limit][lead] 表示处理到第pos位，已使用的数字状态为mask，
// limit表示是否受到上界限制，lead表示是否有前导零
// 
// 时间复杂度：O(n * 2^10 * 2 * 2) = O(n)
// 空间复杂度：O(n * 2^10)

public class LeetCode357_CountNumbersWithUniqueDigits_NoPackage {
    
    // 数位DP记忆化数组
    private static int[][][][] dp;
    // 存储上界的每一位
    private static int[] digits;
    // 上界的长度
    private static int len;
    
    /**
     * 主函数：统计各位数字都不同的数字个数
     * @param n 指数
     * @return 各位数字都不同的数字个数
     */
    public static int countNumbersWithUniqueDigits(int n) {
        // 特殊情况处理
        if (n == 0) {
            return 1;
        }
        
        // 计算上界 10^n - 1
        int upper = 1;
        for (int i = 0; i < n; i++) {
            upper *= 10;
        }
        upper--; // 10^n - 1
        
        // 将上界转换为数字数组
        String upperStr = String.valueOf(upper);
        len = upperStr.length();
        digits = new int[len];
        for (int i = 0; i < len; i++) {
            digits[i] = upperStr.charAt(i) - '0';
        }
        
        // 初始化DP数组
        dp = new int[len][1024][2][2]; // 2^10 = 1024
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 1024; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k][0] = -1;
                    dp[i][j][k][1] = -1;
                }
            }
        }
        
        // 从最高位开始进行数位DP
        return dfs(0, 0, true, true);
    }
    
    /**
     * 数位DP核心函数
     * @param pos 当前处理到第几位
     * @param mask 已使用的数字状态（用位运算表示）
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @return 满足条件的数字个数
     */
    private static int dfs(int pos, int mask, boolean limit, boolean lead) {
        // 递归终止条件：处理完所有数位
        if (pos == len) {
            // 只有在没有前导零的情况下才算一个有效数字
            return lead ? 0 : 1;
        }
        
        // 记忆化搜索优化：如果该状态已经计算过，直接返回结果
        if (!limit && !lead && dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0] != -1) {
            return dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0];
        }
        
        int result = 0;
        
        // 如果有前导零，可以继续选择前导零
        if (lead) {
            result += dfs(pos + 1, mask, false, true);
        }
        
        // 确定当前位可以填入的数字范围
        int maxDigit = limit ? digits[pos] : 9;
        
        // 枚举当前位可以填入的数字
        for (int digit = 0; digit <= maxDigit; digit++) {
            // 跳过前导零
            if (lead && digit == 0) {
                continue;
            }
            
            // 如果该数字已经使用过，跳过
            if (((mask >> digit) & 1) == 1) {
                continue;
            }
            
            // 递归处理下一位，更新mask
            result += dfs(pos + 1, mask | (1 << digit), limit && (digit == maxDigit), false);
        }
        
        // 记忆化存储结果
        if (!limit && !lead) {
            dp[pos][mask][limit ? 1 : 0][lead ? 1 : 0] = result;
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 2;
        System.out.println("n = " + n1 + ", 结果 = " + countNumbersWithUniqueDigits(n1)); // 期望输出: 91
        
        // 测试用例2
        int n2 = 0;
        System.out.println("n = " + n2 + ", 结果 = " + countNumbersWithUniqueDigits(n2)); // 期望输出: 1
        
        // 测试用例3
        int n3 = 1;
        System.out.println("n = " + n3 + ", 结果 = " + countNumbersWithUniqueDigits(n3)); // 期望输出: 10
    }
}