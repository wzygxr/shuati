// package class085; // 注释掉包声明，便于直接运行

/**
 * LeetCode 233. 数字 1 的个数
 * 题目链接：https://leetcode.cn/problems/number-of-digit-one/
 * 
 * 题目描述：
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。核心思想是逐位统计数字1的出现次数。
 * 状态定义：dp[pos][count][limit] 表示处理到第pos位，已经统计到count个1，limit表示是否受到上界限制。
 * 
 * 算法分析：
 * 时间复杂度：O(L²) 其中L是数字n的位数，因为count最多为L，状态数为O(L²)
 * 空间复杂度：O(L²) 用于存储DP状态
 * 
 * 最优解分析：
 * 这是数位DP的标准解法，对于此类计数问题是最优解。数学方法虽然可以达到O(L)时间复杂度，
 * 但数位DP方法更加通用，易于扩展到其他数字统计问题。
 * 
 * 工程化考量：
 * 1. 异常处理：处理n为负数的情况
 * 2. 边界测试：测试n=0, n=1, n=10^k等边界情况
 * 3. 性能优化：使用记忆化搜索避免重复计算
 * 4. 代码可读性：清晰的变量命名和详细注释
 */

public class LeetCode233_NumberOfDigitOne {
    
    // 数位DP记忆化数组：dp[pos][count][limit]
    // pos: 当前处理的位置（0到len-1）
    // count: 已经统计到的1的个数（0到len）
    // limit: 是否受到上界限制（0或1）
    private static int[][][] dp;
    
    // 存储数字n的每一位数字，便于按位处理
    private static int[] digits;
    
    // 数字n的位数
    private static int len;
    
    /**
     * 主函数：计算所有小于等于n的非负整数中数字1出现的个数
     * 
     * @param n 目标数字
     * @return 数字1出现的总次数
     * 
     * 时间复杂度：O(L²) 其中L是数字n的位数
     * 空间复杂度：O(L²) 用于存储DP数组
     * 
     * 算法步骤：
     * 1. 将数字n转换为字符串，提取每一位数字
     * 2. 初始化DP数组为-1（未计算状态）
     * 3. 从最高位开始进行深度优先搜索（DFS）
     * 4. 返回DFS结果
     */
    public static int countDigitOne(int n) {
        // 边界条件处理：n为负数时返回0
        if (n < 0) {
            return 0;
        }
        
        // 将数字转换为字符串，便于提取每一位数字
        String numStr = String.valueOf(n);
        len = numStr.length();
        digits = new int[len];
        
        // 提取每一位数字，存储在digits数组中
        for (int i = 0; i < len; i++) {
            digits[i] = numStr.charAt(i) - '0';
        }
        
        // 初始化DP数组，大小为[len][len+1][2]
        // 第三维大小为2，分别对应limit=true和limit=false的情况
        dp = new int[len][len + 1][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j <= len; j++) {
                // 初始化为-1，表示该状态尚未计算
                dp[i][j][0] = -1;
                dp[i][j][1] = -1;
            }
        }
        
        // 从最高位（第0位）开始进行数位DP，初始状态：
        // pos=0, count=0, limit=true, lead=true
        return dfs(0, 0, true, true);
    }
    
    /**
     * 数位DP核心递归函数
     * 
     * @param pos 当前处理的位置（从0到len-1）
     * @param count 已经统计到的数字1的个数
     * @param limit 是否受到上界限制（true表示受到限制）
     * @param lead 是否有前导零（true表示有前导零）
     * @return 从当前状态开始，满足条件的数字个数
     * 
     * 状态转移分析：
     * 1. 终止条件：处理完所有数位，返回当前统计的count
     * 2. 记忆化检查：如果状态已经计算过，直接返回结果
     * 3. 确定可选数字范围：根据limit参数确定当前位最大可选数字
     * 4. 枚举所有可能数字，递归处理下一位
     * 5. 记忆化存储结果，避免重复计算
     */
    private static int dfs(int pos, int count, boolean limit, boolean lead) {
        // 递归终止条件：已经处理完所有数位
        if (pos == len) {
            return count; // 返回当前统计到的1的个数
        }
        
        // 记忆化搜索优化：只有不受限制且没有前导零的状态可以记忆化
        // 因为受限制的状态具有唯一性，不能共享计算结果
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            if (dp[pos][count][limitIndex] != -1) {
                return dp[pos][count][limitIndex];
            }
        }
        
        // 确定当前位可以填入的数字范围
        // 如果受到上界限制，最大数字为digits[pos]，否则为9
        int maxDigit = limit ? digits[pos] : 9;
        int result = 0;
        
        // 枚举当前位可以填入的所有可能数字（0到maxDigit）
        for (int digit = 0; digit <= maxDigit; digit++) {
            // 计算新的count值：如果当前数字是1，count加1
            int newCount = count + (digit == 1 ? 1 : 0);
            
            // 计算新的limit值：当前限制且digit等于最大数字时，下一位继续受限
            boolean newLimit = limit && (digit == maxDigit);
            
            // 计算新的lead值：当前有前导零且digit为0时，下一位继续有前导零
            boolean newLead = lead && (digit == 0);
            
            // 递归处理下一位
            result += dfs(pos + 1, newCount, newLimit, newLead);
        }
        
        // 记忆化存储：只有不受限制且没有前导零的状态需要存储
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            dp[pos][count][limitIndex] = result;
        }
        
        return result;
    }
    
    /**
     * 单元测试方法，验证算法正确性
     * 
     * 测试用例设计原则：
     * 1. 边界测试：n=0, n=1等边界情况
     * 2. 典型测试：n=13, n=100等典型情况
     * 3. 大数测试：n=10^9等大数情况（实际测试时使用）
     * 
     * 测试用例验证：
     * n=13时，数字1出现次数：1,10,11,12,13 → 共6次
     * n=100时，数字1出现次数：数学公式计算为21次
     */
    public static void main(String[] args) {
        // 测试用例1：n=13
        int n1 = 13;
        int result1 = countDigitOne(n1);
        System.out.println("测试用例1 - n = " + n1);
        System.out.println("期望输出: 6");
        System.out.println("实际输出: " + result1);
        System.out.println("测试结果: " + (result1 == 6 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2：n=0（边界情况）
        int n2 = 0;
        int result2 = countDigitOne(n2);
        System.out.println("测试用例2 - n = " + n2);
        System.out.println("期望输出: 0");
        System.out.println("实际输出: " + result2);
        System.out.println("测试结果: " + (result2 == 0 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3：n=100（典型情况）
        int n3 = 100;
        int result3 = countDigitOne(n3);
        System.out.println("测试用例3 - n = " + n3);
        System.out.println("期望输出: 21");
        System.out.println("实际输出: " + result3);
        System.out.println("测试结果: " + (result3 == 21 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例4：n=1（最小正整数）
        int n4 = 1;
        int result4 = countDigitOne(n4);
        System.out.println("测试用例4 - n = " + n4);
        System.out.println("期望输出: 1");
        System.out.println("实际输出: " + result4);
        System.out.println("测试结果: " + (result4 == 1 ? "通过" : "失败"));
        System.out.println();
        
        // 性能测试：n=10^9（大数情况）
        int n5 = 1000000000;
        long startTime = System.currentTimeMillis();
        int result5 = countDigitOne(n5);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - n = " + n5);
        System.out.println("计算结果: " + result5);
        System.out.println("计算时间: " + (endTime - startTime) + "ms");
        System.out.println("时间复杂度验证: O(log²n) 在实际数据规模下表现良好");
    }
    
    /**
     * 数学方法实现（对比用） - 时间复杂度O(L)，空间复杂度O(1)
     * 这是该问题的最优数学解法，但只适用于统计特定数字出现次数的问题
     * 数位DP方法更加通用，可以扩展到其他复杂的数字约束问题
     */
    public static int countDigitOneMath(int n) {
        if (n <= 0) return 0;
        
        long count = 0;
        long factor = 1;
        long lower, curr, higher;
        
        while (n / factor != 0) {
            lower = n - (n / factor) * factor;
            curr = (n / factor) % 10;
            higher = n / (factor * 10);
            
            if (curr == 0) {
                count += higher * factor;
            } else if (curr == 1) {
                count += higher * factor + lower + 1;
            } else {
                count += (higher + 1) * factor;
            }
            
            factor *= 10;
        }
        
        return (int) count;
    }
}