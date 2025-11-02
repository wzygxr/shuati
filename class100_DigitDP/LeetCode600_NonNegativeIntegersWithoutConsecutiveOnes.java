// package class085; // 注释掉包声明，便于直接运行

/**
 * LeetCode 600. 不含连续1的非负整数
 * 题目链接：https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
 * 
 * 题目描述：
 * 给定一个正整数 n，统计在 [0, n] 范围的非负整数中，有多少个整数的二进制表示中不存在连续的1。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。核心思想是逐位处理二进制数字，确保不出现连续的1。
 * 状态定义：dp[pos][pre][limit] 表示处理到第pos位，前一位是pre（0或1），limit表示是否受到上界限制。
 * 
 * 算法分析：
 * 时间复杂度：O(L) 其中L是数字n的二进制位数，因为状态数为O(L×2×2)=O(L)
 * 空间复杂度：O(L) 用于存储DP状态
 * 
 * 最优解分析：
 * 这是二进制数位DP的标准解法，对于此类约束问题是最优解。也可以使用斐波那契数列方法达到O(L)时间复杂度，
 * 但数位DP方法更加直观，易于理解和扩展。
 * 
 * 工程化考量：
 * 1. 二进制处理：将数字转换为二进制字符串进行处理
 * 2. 约束条件：前一位为1时，当前位不能为1
 * 3. 边界处理：正确处理n=0, n=1等边界情况
 * 4. 性能优化：使用记忆化搜索避免重复计算
 */

public class LeetCode600_NonNegativeIntegersWithoutConsecutiveOnes {
    
    // 数位DP记忆化数组：dp[pos][pre][limit]
    // pos: 当前处理的位置（0到len-1）
    // pre: 前一位数字（0或1）
    // limit: 是否受到上界限制（0或1）
    private static int[][][] dp;
    
    // 存储数字n的二进制表示的每一位
    private static int[] bits;
    
    // 数字n的二进制位数
    private static int len;
    
    /**
     * 主函数：统计在[0, n]范围内二进制表示中不含连续1的非负整数个数
     * 
     * @param n 目标数字
     * @return 满足条件的数字个数
     * 
     * 时间复杂度：O(L) 其中L是数字n的二进制位数
     * 空间复杂度：O(L) 用于存储DP数组
     * 
     * 算法步骤：
     * 1. 将数字n转换为二进制字符串，提取每一位二进制数字
     * 2. 初始化DP数组为-1（未计算状态）
     * 3. 从最高位开始进行深度优先搜索（DFS）
     * 4. 返回DFS结果
     */
    public static int findIntegers(int n) {
        // 边界条件处理：n为负数时返回0，n=0时返回1（只有0本身）
        if (n < 0) {
            return 0;
        }
        if (n == 0) {
            return 1;
        }
        
        // 将数字转换为二进制字符串，便于提取每一位二进制数字
        String binaryStr = Integer.toBinaryString(n);
        len = binaryStr.length();
        bits = new int[len];
        
        // 提取每一位二进制数字，存储在bits数组中
        for (int i = 0; i < len; i++) {
            bits[i] = binaryStr.charAt(i) - '0';
        }
        
        // 初始化DP数组，大小为[len][2][2]
        // 第二维大小为2，对应pre=0或1；第三维大小为2，对应limit=true或false
        dp = new int[len][2][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                // 初始化为-1，表示该状态尚未计算
                dp[i][j][0] = -1;
                dp[i][j][1] = -1;
            }
        }
        
        // 从最高位（第0位）开始进行数位DP，初始状态：
        // pos=0, pre=0（假设前一位为0），limit=true, lead=true
        return dfs(0, 0, true, true);
    }
    
    /**
     * 数位DP核心递归函数
     * 
     * @param pos 当前处理的位置（从0到len-1）
     * @param pre 前一位的数字（0或1）
     * @param limit 是否受到上界限制（true表示受到限制）
     * @param lead 是否有前导零（true表示有前导零）
     * @return 从当前状态开始，满足条件的数字个数
     * 
     * 状态转移分析：
     * 1. 终止条件：处理完所有数位，返回1（找到一个有效数字）
     * 2. 记忆化检查：如果状态已经计算过，直接返回结果
     * 3. 确定可选数字范围：根据limit参数确定当前位最大可选数字（0或1）
     * 4. 枚举所有可能数字，检查约束条件（不能有连续1）
     * 5. 递归处理下一位，累加结果
     * 6. 记忆化存储结果，避免重复计算
     */
    private static int dfs(int pos, int pre, boolean limit, boolean lead) {
        // 递归终止条件：已经处理完所有二进制位
        if (pos == len) {
            return 1; // 找到一个有效的二进制数
        }
        
        // 记忆化搜索优化：只有不受限制且没有前导零的状态可以记忆化
        // 因为受限制的状态具有唯一性，不能共享计算结果
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            if (dp[pos][pre][limitIndex] != -1) {
                return dp[pos][pre][limitIndex];
            }
        }
        
        // 确定当前位可以填入的数字范围
        // 如果受到上界限制，最大数字为bits[pos]，否则为1（二进制只有0和1）
        int maxDigit = limit ? bits[pos] : 1;
        int result = 0;
        
        // 枚举当前位可以填入的所有可能数字（0到maxDigit）
        for (int digit = 0; digit <= maxDigit; digit++) {
            // 约束条件检查：不能有连续的1
            // 如果前一位是1且当前位也是1，则跳过该选择
            if (pre == 1 && digit == 1) {
                continue;
            }
            
            // 计算新的limit值：当前限制且digit等于最大数字时，下一位继续受限
            boolean newLimit = limit && (digit == maxDigit);
            
            // 计算新的lead值：当前有前导零且digit为0时，下一位继续有前导零
            boolean newLead = lead && (digit == 0);
            
            // 递归处理下一位，digit作为新的pre值
            result += dfs(pos + 1, digit, newLimit, newLead);
        }
        
        // 记忆化存储：只有不受限制且没有前导零的状态需要存储
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            dp[pos][pre][limitIndex] = result;
        }
        
        return result;
    }
    
    /**
     * 斐波那契数列方法 - 替代解法，时间复杂度O(L)，空间复杂度O(1)
     * 数学发现：不含连续1的二进制数个数满足斐波那契数列规律
     * f(n) = f(n-1) + f(n-2)，其中f(0)=1, f(1)=2
     * 这种方法更加高效，但只适用于此类特定问题
     */
    public static int findIntegersFibonacci(int n) {
        if (n < 0) return 0;
        if (n == 0) return 1;
        
        // 预处理斐波那契数列
        int[] fib = new int[32]; // 32位足够处理int范围
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 32; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        
        String binary = Integer.toBinaryString(n);
        int len = binary.length();
        int result = 0;
        boolean prevBit = false; // 记录前一位是否为1
        
        // 从高位到低位处理
        for (int i = 0; i < len; i++) {
            if (binary.charAt(i) == '1') {
                // 加上所有长度为len-i位且最高位为0的有效数
                result += fib[len - i - 1];
                if (prevBit) {
                    // 出现连续1，后面的数都不符合条件，直接返回当前结果
                    return result;
                }
                prevBit = true;
            } else {
                prevBit = false;
            }
        }
        
        // 加上n本身（如果n本身符合条件）
        return result + 1;
    }
    
    /**
     * 单元测试方法，验证算法正确性
     * 
     * 测试用例设计原则：
     * 1. 边界测试：n=0, n=1等边界情况
     * 2. 典型测试：n=5, n=10等典型情况
     * 3. 连续1测试：包含连续1的数字
     * 
     * 测试用例验证：
     * n=5时，二进制表示：0,1,10,11,100,101 → 但11有连续1，所以有效数为5个
     * n=1时，有效数：0,1 → 2个
     * n=2时，有效数：0,1,10 → 3个（11有连续1）
     */
    public static void main(String[] args) {
        // 测试用例1：n=5
        int n1 = 5;
        int result1 = findIntegers(n1);
        int result1Fib = findIntegersFibonacci(n1);
        System.out.println("测试用例1 - n = " + n1 + " (二进制: " + Integer.toBinaryString(n1) + ")");
        System.out.println("数位DP结果: " + result1);
        System.out.println("斐波那契方法结果: " + result1Fib);
        System.out.println("期望输出: 5");
        System.out.println("测试结果: " + (result1 == 5 && result1Fib == 5 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2：n=1
        int n2 = 1;
        int result2 = findIntegers(n2);
        int result2Fib = findIntegersFibonacci(n2);
        System.out.println("测试用例2 - n = " + n2 + " (二进制: " + Integer.toBinaryString(n2) + ")");
        System.out.println("数位DP结果: " + result2);
        System.out.println("斐波那契方法结果: " + result2Fib);
        System.out.println("期望输出: 2");
        System.out.println("测试结果: " + (result2 == 2 && result2Fib == 2 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3：n=2
        int n3 = 2;
        int result3 = findIntegers(n3);
        int result3Fib = findIntegersFibonacci(n3);
        System.out.println("测试用例3 - n = " + n3 + " (二进制: " + Integer.toBinaryString(n3) + ")");
        System.out.println("数位DP结果: " + result3);
        System.out.println("斐波那契方法结果: " + result3Fib);
        System.out.println("期望输出: 3");
        System.out.println("测试结果: " + (result3 == 3 && result3Fib == 3 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例4：n=10（包含连续1的情况）
        int n4 = 10;
        int result4 = findIntegers(n4);
        int result4Fib = findIntegersFibonacci(n4);
        System.out.println("测试用例4 - n = " + n4 + " (二进制: " + Integer.toBinaryString(n4) + ")");
        System.out.println("数位DP结果: " + result4);
        System.out.println("斐波那契方法结果: " + result4Fib);
        System.out.println("期望输出: 8"); // 0,1,10,100,101,1000,1001,1010
        System.out.println("测试结果: " + (result4 == 8 && result4Fib == 8 ? "通过" : "失败"));
        System.out.println();
        
        // 性能测试：n=10^9
        int n5 = 1000000000;
        long startTime = System.currentTimeMillis();
        int result5 = findIntegers(n5);
        long dpTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result5Fib = findIntegersFibonacci(n5);
        long fibTime = System.currentTimeMillis() - startTime;
        
        System.out.println("性能测试 - n = " + n5);
        System.out.println("数位DP结果: " + result5 + ", 耗时: " + dpTime + "ms");
        System.out.println("斐波那契方法结果: " + result5Fib + ", 耗时: " + fibTime + "ms");
        System.out.println("斐波那契方法比数位DP快 " + (double)dpTime / fibTime + " 倍");
        System.out.println("时间复杂度验证: 两种方法都是O(L)，但斐波那契方法常数更小");
    }
    
    /**
     * 调试方法：打印中间状态，帮助理解算法执行过程
     */
    public static void debugFindIntegers(int n) {
        System.out.println("调试模式 - n = " + n + " (二进制: " + Integer.toBinaryString(n) + ")");
        
        String binaryStr = Integer.toBinaryString(n);
        int len = binaryStr.length();
        int[] bits = new int[len];
        for (int i = 0; i < len; i++) {
            bits[i] = binaryStr.charAt(i) - '0';
        }
        
        System.out.print("二进制位: ");
        for (int bit : bits) {
            System.out.print(bit);
        }
        System.out.println();
        
        // 手动计算小范围结果验证
        int manualCount = 0;
        for (int i = 0; i <= n; i++) {
            String bin = Integer.toBinaryString(i);
            if (!bin.contains("11")) {
                manualCount++;
                System.out.println("有效数: " + i + " (二进制: " + bin + ")");
            }
        }
        
        System.out.println("手动计算结果: " + manualCount);
        System.out.println("数位DP结果: " + findIntegers(n));
        System.out.println("结果验证: " + (manualCount == findIntegers(n) ? "一致" : "不一致"));
    }
}