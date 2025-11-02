package class085;

/**
 * LeetCode 1012. 至少有 1 位重复的数字
 * 题目链接：https://leetcode.cn/problems/numbers-with-repeated-digits/
 * 
 * 题目描述：
 * 给定正整数 N，返回在 [1, N] 范围内具有至少 1 位重复数字的正整数的个数。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。我们采用补集的思想，先计算没有重复数字的个数，
 * 然后用总数减去它得到至少有1位重复数字的个数。
 * 状态定义：
 * dp[pos][mask][limit][lead] 表示处理到第pos位，已使用的数字状态为mask，
 * limit表示是否受到上界限制，lead表示是否有前导零
 * 
 * 算法分析：
 * 时间复杂度：O(log N * 2^10 * 2 * 2) = O(log N)
 * 空间复杂度：O(log N * 2^10)
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。通过补集思想将问题转化为计算
 * 没有重复数字的个数，可以简化问题的复杂度。
 * 
 * 工程化考量：
 * 1. 位运算优化：使用位掩码表示已使用的数字状态
 * 2. 补集思想：通过计算补集简化问题
 * 3. 边界处理：正确处理前导零和上界限制
 * 4. 性能优化：使用记忆化搜索避免重复计算
 * 5. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - LeetCode 1012: https://leetcode.cn/problems/numbers-with-repeated-digits/
 * - LeetCode 2376: https://leetcode.cn/problems/count-special-integers/
 * 
 * 多语言实现：
 * - Java: LeetCode1012_NumbersWithRepeatedDigits.java
 * - Python: LeetCode1012_NumbersWithRepeatedDigits.py
 * - C++: 暂无
 */

public class LeetCode1012_NumbersWithRepeatedDigits {
    
    // 数位DP记忆化数组
    private static int[][][][] dp;
    // 存储数字N的每一位
    private static int[] digitsN;
    // 数字N的长度
    private static int lenN;
    
    /**
     * 主函数：计算在 [1, N] 范围内具有至少 1 位重复数字的正整数的个数
     * 
     * @param N 上界
     * @return 至少有1位重复数字的正整数的个数
     * 
     * 时间复杂度：O(log N)
     * 空间复杂度：O(log N * 2^10)
     */
    public static int numDupDigitsAtMostN(int N) {
        // 将N转换为数字数组
        String nStr = String.valueOf(N);
        lenN = nStr.length();
        digitsN = new int[lenN];
        for (int i = 0; i < lenN; i++) {
            digitsN[i] = nStr.charAt(i) - '0';
        }
        
        // 初始化DP数组
        dp = new int[lenN][1024][2][2]; // 2^10 = 1024
        for (int i = 0; i < lenN; i++) {
            for (int j = 0; j < 1024; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k][0] = -1;
                    dp[i][j][k][1] = -1;
                }
            }
        }
        
        // 计算没有重复数字的个数
        int uniqueCount = dfs(0, 0, true, true);
        
        // 用总数减去没有重复数字的个数，得到至少有1位重复数字的个数
        return N - uniqueCount;
    }
    
    /**
     * 数位DP核心函数 - 计算没有重复数字的个数
     * 
     * @param pos 当前处理到第几位
     * @param mask 已使用的数字状态（用位运算表示）
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @return 没有重复数字的个数
     */
    private static int dfs(int pos, int mask, boolean limit, boolean lead) {
        // 递归终止条件：处理完所有数位
        if (pos == lenN) {
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
        int maxDigit = limit ? digitsN[pos] : 9;
        
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
    
    /**
     * 数学方法实现 - 替代解法
     * 直接计算至少有1位重复数字的个数
     * 
     * @param N 上界
     * @return 至少有1位重复数字的正整数的个数
     */
    public static int numDupDigitsAtMostNMath(int N) {
        // 将N转换为数字数组
        String nStr = String.valueOf(N);
        int len = nStr.length();
        int[] digits = new int[len];
        for (int i = 0; i < len; i++) {
            digits[i] = nStr.charAt(i) - '0';
        }
        
        int result = 0;
        
        // 计算位数小于len的所有数字中重复数字的个数
        // 位数为i的数字总共有9*10^(i-1)个，其中没有重复数字的有9*A(9,i-1)个
        for (int i = 1; i < len; i++) {
            int total = 9;
            for (int j = 1; j < i; j++) {
                total *= (10 - j);
            }
            result += 9 * (int)Math.pow(10, i - 1) - total;
        }
        
        // 计算位数等于len且小于等于N的数字中重复数字的个数
        boolean[] used = new boolean[10];
        for (int i = 0; i < len; i++) {
            // 计算小于digits[i]且未使用的数字个数
            int count = 0;
            for (int j = (i == 0 ? 1 : 0); j < digits[i]; j++) {
                if (!used[j]) {
                    count++;
                }
            }
            
            // 计算剩余位置可以填入的数字组合数
            int remaining = 1;
            int available = 10 - i - 1;
            for (int j = i + 1; j < len; j++) {
                remaining *= available--;
            }
            
            // 计算没有重复数字的个数
            int unique = count * remaining;
            
            // 计算有重复数字的个数
            int total = count * (int)Math.pow(10, len - i - 1);
            result += total - unique;
            
            // 如果当前数字已经被使用，说明N本身有重复数字
            if (used[digits[i]]) {
                break;
            }
            
            used[digits[i]] = true;
        }
        
        // 检查N本身是否有重复数字
        boolean hasDup = false;
        boolean[] check = new boolean[10];
        for (int i = 0; i < len; i++) {
            int digit = digits[i];
            if (check[digit]) {
                hasDup = true;
                break;
            }
            check[digit] = true;
        }
        
        if (hasDup) {
            result++;
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int N1 = 20;
        int result1 = numDupDigitsAtMostN(N1);
        int result1Math = numDupDigitsAtMostNMath(N1);
        System.out.println("测试用例1:");
        System.out.println("N = " + N1);
        System.out.println("数位DP结果: " + result1);
        System.out.println("数学方法结果: " + result1Math);
        System.out.println("期望输出: 1");
        System.out.println("测试结果: " + (result1 == 1 && result1Math == 1 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2
        int N2 = 100;
        int result2 = numDupDigitsAtMostN(N2);
        int result2Math = numDupDigitsAtMostNMath(N2);
        System.out.println("测试用例2:");
        System.out.println("N = " + N2);
        System.out.println("数位DP结果: " + result2);
        System.out.println("数学方法结果: " + result2Math);
        System.out.println("期望输出: 10");
        System.out.println("测试结果: " + (result2 == 10 && result2Math == 10 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3
        int N3 = 1000;
        int result3 = numDupDigitsAtMostN(N3);
        int result3Math = numDupDigitsAtMostNMath(N3);
        System.out.println("测试用例3:");
        System.out.println("N = " + N3);
        System.out.println("数位DP结果: " + result3);
        System.out.println("数学方法结果: " + result3Math);
        System.out.println("期望输出: 262");
        System.out.println("测试结果: " + (result3 == 262 && result3Math == 262 ? "通过" : "失败"));
        System.out.println();
    }
}