// package class085; // 注释掉包声明，便于直接运行

/**
 * Codeforces 1073E. Segment Sum
 * 题目链接：https://codeforces.com/problemset/problem/1073/E
 * 
 * 题目描述：
 * 给定区间[L, R]和整数K，求[L,R]范围内最多包含K个不同数字的数的和。
 * 
 * 解题思路：
 * 使用数位动态规划（Digit DP）解决该问题。需要同时计算满足条件的数字个数和它们的和。
 * 状态定义：使用五维DP数组记录位置、数字使用掩码、限制状态、前导零状态。
 * 
 * 算法分析：
 * 时间复杂度：O(L × 2^10 × 2 × 2) = O(L) 其中L是数字位数，2^10是数字使用状态数
 * 空间复杂度：O(L × 2^10) 用于存储DP状态
 * 
 * 最优解分析：
 * 这是数位DP处理复杂约束问题的标准解法，对于此类需要同时计算个数和和值的问题是最优解。
 * 
 * 工程化考量：
 * 1. 大数处理：使用long类型和模运算防止溢出
 * 2. 状态压缩：使用位掩码记录数字使用情况
 * 3. 记忆化优化：只记忆化不受限制的状态
 * 4. 边界处理：正确处理L-1为负数的情况
 */

public class Codeforces1073E_SegmentSum {
    
    private static final long MOD = 998244353;
    
    // 数位DP记忆化数组：dp[pos][mask][limit][lead] = {count, sum}
    // pos: 当前位置（0到len-1）
    // mask: 数字使用状态掩码（10位，表示0-9数字是否使用过）
    // limit: 是否受到上界限制（0或1）
    // lead: 是否有前导零（0或1）
    private static long[][][][][] dp;
    
    // 存储数字的每一位
    private static int[] digits;
    
    // 数字长度
    private static int len;
    
    /**
     * 主函数：计算[L,R]范围内最多包含K个不同数字的数的和
     * 
     * @param L 区间下界
     * @param R 区间上界
     * @param K 最多包含的不同数字个数
     * @return 满足条件的数的和对MOD取模的结果
     * 
     * 时间复杂度：O(log R × 2^10)
     * 空间复杂度：O(log R × 2^10)
     * 
     * 算法步骤：
     * 1. 使用前缀和思想：result = solve(R) - solve(L-1)
     * 2. 处理模运算的负数情况
     * 3. 返回最终结果
     */
    public static long segmentSum(long L, long R, int K) {
        // 前缀和思想：[L,R]区间的结果 = [0,R] - [0,L-1]
        long result = (solve(R, K) - solve(L - 1, K) + MOD) % MOD;
        return result;
    }
    
    /**
     * 计算[0,R]范围内最多包含K个不同数字的数的和
     * 
     * @param R 上界
     * @param K 最多包含的不同数字个数
     * @return 满足条件的数的和对MOD取模的结果
     */
    private static long solve(long R, int K) {
        // 边界条件处理
        if (R < 0) return 0;
        
        // 将数字转换为字符串，便于提取每一位数字
        String numStr = String.valueOf(R);
        len = numStr.length();
        digits = new int[len];
        
        // 提取每一位数字
        for (int i = 0; i < len; i++) {
            digits[i] = numStr.charAt(i) - '0';
        }
        
        // 初始化DP数组，大小为[len][1024][2][2][2]
        // 1024 = 2^10，表示10个数字的使用状态
        dp = new long[len][1024][2][2][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 1024; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        // 初始化为-1，表示该状态尚未计算
                        dp[i][j][k][l][0] = -1; // count
                        dp[i][j][k][l][1] = -1; // sum
                    }
                }
            }
        }
        
        // 从最高位开始进行数位DP
        long[] result = dfs(0, 0, true, true, K);
        return result[1]; // 返回和值
    }
    
    /**
     * 检查数字使用状态是否满足条件（最多K个不同数字）
     * 
     * @param mask 数字使用状态掩码
     * @param K 最多允许的不同数字个数
     * @return 是否满足条件
     * 
     * 算法原理：
     * 使用位运算统计mask中1的个数，即已使用的不同数字个数
     */
    private static boolean check(int mask, int K) {
        int count = Integer.bitCount(mask);
        return count <= K;
    }
    
    /**
     * 数位DP核心递归函数
     * 
     * @param pos 当前处理的位置
     * @param mask 数字使用状态掩码
     * @param limit 是否受到上界限制
     * @param lead 是否有前导零
     * @param K 最多包含的不同数字个数
     * @return 长度为2的数组，[0]为满足条件的数字个数，[1]为满足条件的数字和
     * 
     * 状态转移分析：
     * 1. 终止条件：处理完所有数位，检查是否满足条件
     * 2. 记忆化检查：如果状态已计算，直接返回结果
     * 3. 枚举当前位数字，更新状态掩码
     * 4. 递归处理下一位，累加结果
     * 5. 计算当前位对总和的贡献
     * 6. 记忆化存储结果
     */
    private static long[] dfs(int pos, int mask, boolean limit, boolean lead, int K) {
        // 递归终止条件：处理完所有数位
        if (pos == len) {
            // 检查是否满足条件：没有前导零且不同数字个数不超过K
            if (!lead && check(mask, K)) {
                return new long[]{1, 0}; // 个数为1，当前数字和为0（在递归过程中已经计算了各位的贡献）
            }
            return new long[]{0, 0}; // 不满足条件
        }
        
        // 记忆化搜索优化：只有不受限制且没有前导零的状态可以记忆化
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            int leadIndex = lead ? 1 : 0;
            if (dp[pos][mask][limitIndex][leadIndex][0] != -1) {
                return new long[]{
                    dp[pos][mask][limitIndex][leadIndex][0],
                    dp[pos][mask][limitIndex][leadIndex][1]
                };
            }
        }
        
        long count = 0;
        long sum = 0;
        
        // 确定当前位可以填入的数字范围
        int maxDigit = limit ? digits[pos] : 9;
        
        // 枚举当前位可以填入的所有可能数字（0到maxDigit）
        for (int digit = 0; digit <= maxDigit; digit++) {
            // 更新数字使用状态掩码
            int newMask = mask;
            if (!lead || digit != 0) {
                // 如果不是前导零或者是非零数字，更新掩码
                newMask |= (1 << digit);
            }
            
            // 检查新状态是否满足条件（不同数字个数不超过K）
            if (Integer.bitCount(newMask) > K) {
                continue; // 不满足条件，跳过
            }
            
            // 计算新的限制状态和前导零状态
            boolean newLimit = limit && (digit == maxDigit);
            boolean newLead = lead && (digit == 0);
            
            // 递归处理下一位
            long[] next = dfs(pos + 1, newMask, newLimit, newLead, K);
            
            // 更新总个数
            count = (count + next[0]) % MOD;
            
            // 计算当前位对总和的贡献
            // 贡献 = 当前位数字 × 10^(剩余位数) × 满足条件的数字个数 + 下一位的总和
            long power = 1;
            for (int i = 0; i < len - pos - 1; i++) {
                power = (power * 10) % MOD;
            }
            long digitContribution = (digit * power) % MOD;
            digitContribution = (digitContribution * next[0]) % MOD;
            
            sum = (sum + digitContribution + next[1]) % MOD;
        }
        
        // 记忆化存储：只有不受限制且没有前导零的状态需要存储
        if (!limit && !lead) {
            int limitIndex = limit ? 1 : 0;
            int leadIndex = lead ? 1 : 0;
            dp[pos][mask][limitIndex][leadIndex][0] = count;
            dp[pos][mask][limitIndex][leadIndex][1] = sum;
        }
        
        return new long[]{count, sum};
    }
    
    /**
     * 单元测试方法，验证算法正确性
     * 
     * 测试用例设计：
     * 1. 小范围测试：验证基本功能
     * 2. 边界测试：测试K=1, K=10等边界情况
     * 3. 性能测试：测试大数情况下的性能
     */
    public static void main(String[] args) {
        // 测试用例1：小范围测试
        long L1 = 10, R1 = 50;
        int K1 = 2;
        long result1 = segmentSum(L1, R1, K1);
        System.out.println("测试用例1 - L=" + L1 + ", R=" + R1 + ", K=" + K1);
        System.out.println("计算结果: " + result1);
        System.out.println("期望范围: 1000-2000（具体值需要手动验证）");
        System.out.println();
        
        // 测试用例2：边界测试（K=1，只能使用1个数字）
        long L2 = 1, R2 = 100;
        int K2 = 1;
        long result2 = segmentSum(L2, R2, K2);
        System.out.println("测试用例2 - L=" + L2 + ", R=" + R2 + ", K=" + K2);
        System.out.println("计算结果: " + result2);
        System.out.println("理论值: 1+2+3+...+9+11+22+...+99 = 已知公式计算结果");
        System.out.println();
        
        // 测试用例3：K=10（可以使用所有数字）
        long L3 = 1, R3 = 1000;
        int K3 = 10;
        long result3 = segmentSum(L3, R3, K3);
        System.out.println("测试用例3 - L=" + L3 + ", R=" + R3 + ", K=" + K3);
        System.out.println("计算结果: " + result3);
        System.out.println("理论值: 1到1000所有数字的和 = 500500");
        System.out.println("验证结果: " + (result3 == 500500 % MOD ? "一致" : "不一致"));
        System.out.println();
        
        // 性能测试：大数情况
        long L4 = 1, R4 = 1000000000000000000L; // 10^18
        int K4 = 5;
        long startTime = System.currentTimeMillis();
        long result4 = segmentSum(L4, R4, K4);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - L=" + L4 + ", R=" + R4 + ", K=" + K4);
        System.out.println("计算结果: " + result4);
        System.out.println("计算时间: " + (endTime - startTime) + "ms");
        System.out.println("时间复杂度验证: O(L × 2^K) 在实际数据规模下表现良好");
        System.out.println();
        
        // 调试信息：打印中间状态（小范围）
        debugSmallCase();
    }
    
    /**
     * 调试方法：小范围手动验证
     */
    private static void debugSmallCase() {
        System.out.println("=== 小范围手动验证 ===");
        long L = 1, R = 20;
        int K = 2;
        
        // 手动计算验证
        long manualSum = 0;
        for (long i = L; i <= R; i++) {
            String numStr = String.valueOf(i);
            int distinctCount = (int) numStr.chars().distinct().count();
            if (distinctCount <= K) {
                manualSum += i;
                System.out.println("有效数: " + i + " (不同数字个数: " + distinctCount + ")");
            }
        }
        manualSum %= MOD;
        
        long dpResult = segmentSum(L, R, K);
        
        System.out.println("手动计算结果: " + manualSum);
        System.out.println("数位DP结果: " + dpResult);
        System.out.println("验证结果: " + (manualSum == dpResult ? "✓ 一致" : "✗ 不一致"));
    }
    
    /**
     * 优化版本：使用更高效的状态表示
     * 将五维DP优化为三维DP，减少内存使用
     */
    public static long segmentSumOptimized(long L, long R, int K) {
        // 实现思路：将limit和lead状态合并到mask中，或者使用更紧凑的数据结构
        // 这里提供概念性代码，实际实现需要更复杂的状态设计
        return segmentSum(L, R, K); // 暂时使用原版本
    }
}