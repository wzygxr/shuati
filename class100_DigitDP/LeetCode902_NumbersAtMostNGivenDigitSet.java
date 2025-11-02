package class085;

import java.util.Arrays;

/**
 * LeetCode 902. 最大为 N 的数字组合
 * 题目链接：https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
 * 
 * 题目描述：
 * 我们有一组排序的数字 D，它是 {'1','2','3','4','5','6','7','8','9'} 的非空子集。
 * 现在，我们用这些数字来构造数字，可以重复使用，例如 '11' 和 '12'。
 * 返回可以构造出的小于或等于 N 的正整数的数目。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。我们逐位构造数字，确保不超过N。
 * 状态定义：
 * dp[pos][limit][lead] 表示处理到第pos位，limit表示是否受到上界限制，lead表示是否有前导零
 * 
 * 算法分析：
 * 时间复杂度：O(log N * 2 * 2 * |D|) = O(log N * |D|)
 * 空间复杂度：O(log N)
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。通过逐位构造数字并使用记忆化搜索，
 * 可以高效地计算满足条件的数字个数。
 * 
 * 工程化考量：
 * 1. 数组排序：对可用数字进行排序以优化搜索过程
 * 2. 边界处理：正确处理前导零和上界限制
 * 3. 性能优化：使用记忆化搜索避免重复计算
 * 4. 代码可读性：清晰的变量命名和详细注释
 * 
 * 相关题目链接：
 * - LeetCode 902: https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
 * - AcWing 1084: https://www.acwing.com/problem/content/1086/
 * 
 * 多语言实现：
 * - Java: LeetCode902_NumbersAtMostNGivenDigitSet.java
 * - Python: LeetCode902_NumbersAtMostNGivenDigitSet.py
 * - C++: 暂无
 */

public class LeetCode902_NumbersAtMostNGivenDigitSet {
    
    // 数位DP记忆化数组
    private static int[][][] dp;
    // 存储数字N的每一位
    private static int[] digitsN;
    // 数字N的长度
    private static int lenN;
    // 可用的数字集合
    private static int[] digits;
    // 可用数字个数
    private static int lenD;
    
    /**
     * 主函数：计算可以构造出的小于或等于 N 的正整数的数目
     * 
     * @param D 可用的数字字符数组
     * @param N 上界
     * @return 满足条件的数字个数
     * 
     * 时间复杂度：O(log N * |D|)
     * 空间复杂度：O(log N)
     */
    public static int atMostNGivenDigitSet(String[] D, int N) {
        // 将字符串数组转换为整数数组并排序
        lenD = D.length;
        digits = new int[lenD];
        for (int i = 0; i < lenD; i++) {
            digits[i] = Integer.parseInt(D[i]);
        }
        Arrays.sort(digits);
        
        // 将N转换为数字数组
        String nStr = String.valueOf(N);
        lenN = nStr.length();
        digitsN = new int[lenN];
        for (int i = 0; i < lenN; i++) {
            digitsN[i] = nStr.charAt(i) - '0';
        }
        
        // 初始化DP数组
        dp = new int[lenN][2][2];
        for (int i = 0; i < lenN; i++) {
            for (int j = 0; j < 2; j++) {
                dp[i][j][0] = -1;
                dp[i][j][1] = -1;
            }
        }
        
        // 从最高位开始进行数位DP
        return dfs(0, true, true);
    }
    
    /**
     * 数位DP核心函数
     * 
     * @param pos 当前处理到第几位
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @return 满足条件的数字个数
     */
    private static int dfs(int pos, boolean limit, boolean lead) {
        // 递归终止条件：处理完所有数位
        if (pos == lenN) {
            // 只有在没有前导零的情况下才算一个有效数字
            return lead ? 0 : 1;
        }
        
        // 记忆化搜索优化：如果该状态已经计算过，直接返回结果
        if (!limit && !lead && dp[pos][limit ? 1 : 0][lead ? 1 : 0] != -1) {
            return dp[pos][limit ? 1 : 0][lead ? 1 : 0];
        }
        
        int result = 0;
        
        // 如果有前导零，可以继续选择前导零
        if (lead) {
            result += dfs(pos + 1, false, true);
        }
        
        // 确定当前位可以填入的数字范围
        int maxDigit = limit ? digitsN[pos] : 9;
        
        // 枚举当前位可以填入的数字
        for (int digit : digits) {
            // 如果当前数字超过限制，跳出循环
            if (digit > maxDigit) {
                break;
            }
            
            // 递归处理下一位
            result += dfs(pos + 1, limit && (digit == maxDigit), false);
        }
        
        // 记忆化存储结果
        if (!limit && !lead) {
            dp[pos][limit ? 1 : 0][lead ? 1 : 0] = result;
        }
        
        return result;
    }
    
    /**
     * 数学方法实现 - 替代解法
     * 通过分别计算位数小于N和位数等于N的情况来计算结果
     * 
     * @param D 可用的数字字符数组
     * @param N 上界
     * @return 满足条件的数字个数
     */
    public static int atMostNGivenDigitSetMath(String[] D, int N) {
        // 将字符串数组转换为整数数组
        int[] digits = new int[D.length];
        for (int i = 0; i < D.length; i++) {
            digits[i] = Integer.parseInt(D[i]);
        }
        
        String nStr = String.valueOf(N);
        int len = nStr.length();
        
        int result = 0;
        
        // 计算位数小于len的所有数字个数
        for (int i = 1; i < len; i++) {
            result += Math.pow(digits.length, i);
        }
        
        // 计算位数等于len且小于等于N的数字个数
        boolean same = true;
        for (int i = 0; i < len; i++) {
            int digit = nStr.charAt(i) - '0';
            int count = 0;
            
            for (int d : digits) {
                if (d < digit) {
                    count++;
                } else if (d == digit) {
                    // 找到相同数字，继续比较下一位
                    break;
                } else {
                    // 当前数字大于目标数字，后面不会有匹配
                    same = false;
                    break;
                }
            }
            
            result += count * Math.pow(digits.length, len - i - 1);
            
            if (!same) {
                break;
            }
            
            // 如果没有找到相同数字，说明N不能被构造出来
            boolean found = false;
            for (int d : digits) {
                if (d == digit) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                same = false;
            }
        }
        
        // 如果N本身可以被构造出来，需要加上1
        if (same) {
            result++;
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String[] D1 = {"1", "3", "5", "7"};
        int N1 = 100;
        int result1 = atMostNGivenDigitSet(D1, N1);
        int result1Math = atMostNGivenDigitSetMath(D1, N1);
        System.out.println("测试用例1:");
        System.out.println("D = " + Arrays.toString(D1) + ", N = " + N1);
        System.out.println("数位DP结果: " + result1);
        System.out.println("数学方法结果: " + result1Math);
        System.out.println("期望输出: 20");
        System.out.println("测试结果: " + (result1 == 20 && result1Math == 20 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2
        String[] D2 = {"1", "4", "9"};
        int N2 = 1000000000;
        int result2 = atMostNGivenDigitSet(D2, N2);
        int result2Math = atMostNGivenDigitSetMath(D2, N2);
        System.out.println("测试用例2:");
        System.out.println("D = " + Arrays.toString(D2) + ", N = " + N2);
        System.out.println("数位DP结果: " + result2);
        System.out.println("数学方法结果: " + result2Math);
        System.out.println("期望输出: 29523");
        System.out.println("测试结果: " + (result2 == 29523 && result2Math == 29523 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3
        String[] D3 = {"7"};
        int N3 = 8;
        int result3 = atMostNGivenDigitSet(D3, N3);
        int result3Math = atMostNGivenDigitSetMath(D3, N3);
        System.out.println("测试用例3:");
        System.out.println("D = " + Arrays.toString(D3) + ", N = " + N3);
        System.out.println("数位DP结果: " + result3);
        System.out.println("数学方法结果: " + result3Math);
        System.out.println("期望输出: 1");
        System.out.println("测试结果: " + (result3 == 1 && result3Math == 1 ? "通过" : "失败"));
        System.out.println();
    }
}