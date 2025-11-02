package class032;

import java.util.*;

/**
 * SPOJ BITS - 计算位数
 * 题目链接: https://www.spoj.com/problems/BITS/
 * 题目描述: 计算从1到N所有数字的二进制表示中1的总个数
 * 
 * 解题思路:
 * 方法1: 暴力统计 - 对每个数字计算1的个数
 * 方法2: 数学规律 - 利用二进制位模式统计
 * 方法3: 动态规划 - 使用递推关系优化计算
 * 
 * 时间复杂度分析:
 * 方法1: O(N log N) - 每个数字需要O(log N)时间统计1的个数
 * 方法2: O(log N) - 基于二进制位模式直接计算
 * 方法3: O(N) - 线性扫描，但需要O(N)空间
 * 
 * 空间复杂度分析:
 * 方法1: O(1) - 仅使用常数空间
 * 方法2: O(1) - 仅使用常数空间
 * 方法3: O(N) - 需要存储DP数组
 * 
 * 工程化考量:
 * 1. 输入验证: 检查N的范围是否合理
 * 2. 边界处理: 处理N=0和N=1的特殊情况
 * 3. 性能优化: 对于大N使用数学方法避免超时
 * 4. 内存管理: 避免不必要的内存分配
 */

public class Code12_CountBits {
    
    /**
     * 方法1: 暴力统计法
     * 对每个数字从1到N，统计其二进制表示中1的个数
     * 优点: 实现简单，易于理解
     * 缺点: 时间复杂度较高，不适合大N
     */
    public static long countBitsBruteForce(int n) {
        // 输入验证
        if (n <= 0) return 0;
        
        long totalBits = 0;
        for (int i = 1; i <= n; i++) {
            totalBits += Integer.bitCount(i);
        }
        return totalBits;
    }
    
    /**
     * 方法2: 数学规律法（最优解）
     * 利用二进制位模式统计1的个数
     * 思路: 对于每个二进制位，计算该位在1到N中出现的次数
     * 
     * 对于第k位（从0开始，最低位为第0位）:
     * 完整的周期数: n / (1 << (k+1))
     * 每个周期中该位出现1 << k次
     * 剩余部分: n % (1 << (k+1))
     * 如果剩余部分 >= (1 << k)，则额外出现 (剩余部分 - (1 << k) + 1)次
     * 
     * 时间复杂度: O(log N) - 只需要遍历二进制位数
     * 空间复杂度: O(1) - 常数空间
     */
    public static long countBitsMath(int n) {
        if (n <= 0) return 0;
        
        long totalBits = 0;
        int k = 0;
        int mask = 1;
        
        while (mask <= n) {
            // 计算完整的周期数
            long fullCycles = n / (mask << 1);
            // 每个完整周期中该位出现mask次
            totalBits += fullCycles * mask;
            
            // 计算剩余部分
            int remainder = n % (mask << 1);
            if (remainder >= mask) {
                totalBits += remainder - mask + 1;
            }
            
            // 移动到下一个位
            mask <<= 1;
            k++;
        }
        
        return totalBits;
    }
    
    /**
     * 方法3: 动态规划法
     * 使用递推关系: countBits(n) = countBits(n >> 1) + (n & 1)
     * 但为了效率，我们使用查表法优化
     * 
     * 时间复杂度: O(N) - 线性扫描
     * 空间复杂度: O(N) - 需要存储DP数组
     */
    public static long countBitsDP(int n) {
        if (n <= 0) return 0;
        
        int[] dp = new int[n + 1];
        long totalBits = 0;
        
        for (int i = 1; i <= n; i++) {
            // 递推关系: i中1的个数 = i/2中1的个数 + i的最低位
            dp[i] = dp[i >> 1] + (i & 1);
            totalBits += dp[i];
        }
        
        return totalBits;
    }
    
    /**
     * 方法4: 使用内置函数（实际应用推荐）
     * 利用Java内置的Integer.bitCount()，但使用数学优化避免O(N log N)
     * 对于大N，使用方法2的数学优化
     */
    public static long countBitsOptimized(int n) {
        if (n <= 1000000) {
            // 对于较小的n，使用DP方法更简单
            return countBitsDP(n);
        } else {
            // 对于大n，使用数学方法避免超时
            return countBitsMath(n);
        }
    }
    
    /**
     * 工程化改进版本：增加完整的异常处理和验证
     */
    public static long countBitsWithValidation(int n) {
        try {
            // 输入验证
            if (n < 0) {
                throw new IllegalArgumentException("Input n must be non-negative");
            }
            
            if (n == 0) return 0;
            if (n == 1) return 1;
            
            // 根据n的大小选择最优算法
            if (n <= 1000000) {
                return countBitsDP(n);
            } else {
                return countBitsMath(n);
            }
        } catch (Exception e) {
            System.err.println("Error in countBits: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void runTests() {
        System.out.println("=== SPOJ BITS - 计算位数 单元测试 ===");
        
        // 测试用例1: n=1
        int n1 = 1;
        long expected1 = 1;
        long result1 = countBitsMath(n1);
        System.out.printf("测试1: n=%d, 期望=%d, 实际=%d, %s%n", 
                         n1, expected1, result1, 
                         result1 == expected1 ? "通过" : "失败");
        
        // 测试用例2: n=3
        int n2 = 3;
        long expected2 = 4; // 1(1) + 2(1) + 3(2) = 4
        long result2 = countBitsMath(n2);
        System.out.printf("测试2: n=%d, 期望=%d, 实际=%d, %s%n", 
                         n2, expected2, result2, 
                         result2 == expected2 ? "通过" : "失败");
        
        // 测试用例3: n=5
        int n3 = 5;
        long expected3 = 7; // 1+1+2+1+2 = 7
        long result3 = countBitsMath(n3);
        System.out.printf("测试3: n=%d, 期望=%d, 实际=%d, %s%n", 
                         n3, expected3, result3, 
                         result3 == expected3 ? "通过" : "失败");
        
        // 测试不同方法的结果一致性
        System.out.println("\n=== 方法一致性测试 ===");
        int testN = 100;
        long r1 = countBitsBruteForce(testN);
        long r2 = countBitsMath(testN);
        long r3 = countBitsDP(testN);
        long r4 = countBitsOptimized(testN);
        
        System.out.printf("暴力法: %d%n", r1);
        System.out.printf("数学法: %d%n", r2);
        System.out.printf("动态规划: %d%n", r3);
        System.out.printf("优化法: %d%n", r4);
        System.out.printf("所有方法结果一致: %s%n", 
                         (r1 == r2 && r2 == r3 && r3 == r4) ? "是" : "否");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        int[] testSizes = {1000, 10000, 100000, 1000000, 10000000};
        
        for (int n : testSizes) {
            System.out.printf("n = %d:%n", n);
            
            // 测试数学方法
            long startTime = System.nanoTime();
            long resultMath = countBitsMath(n);
            long mathTime = System.nanoTime() - startTime;
            
            // 测试DP方法（仅在小规模时测试）
            long dpTime = 0;
            if (n <= 1000000) {
                startTime = System.nanoTime();
                long resultDP = countBitsDP(n);
                dpTime = System.nanoTime() - startTime;
                System.out.printf("  DP方法: %d ns, 结果: %d%n", dpTime, resultDP);
            }
            
            System.out.printf("  数学方法: %d ns, 结果: %d%n", mathTime, resultMath);
            
            if (n <= 1000000) {
                double ratio = (double) dpTime / mathTime;
                System.out.printf("  数学方法比DP快: %.2f倍%n", ratio);
            }
            System.out.println();
        }
    }
    
    /**
     * 复杂度分析
     */
    public static void complexityAnalysis() {
        System.out.println("=== 复杂度分析 ===");
        System.out.println("方法1（暴力统计）:");
        System.out.println("  时间复杂度: O(N log N) - 每个数字需要O(log N)时间统计1的个数");
        System.out.println("  空间复杂度: O(1) - 仅使用常数空间");
        System.out.println("  适用场景: 小规模数据，代码简单");
        
        System.out.println("\n方法2（数学规律）:");
        System.out.println("  时间复杂度: O(log N) - 只需要遍历二进制位数");
        System.out.println("  空间复杂度: O(1) - 常数空间");
        System.out.println("  适用场景: 大规模数据，最优解");
        
        System.out.println("\n方法3（动态规划）:");
        System.out.println("  时间复杂度: O(N) - 线性扫描");
        System.out.println("  空间复杂度: O(N) - 需要存储DP数组");
        System.out.println("  适用场景: 中等规模数据，实现简单");
        
        System.out.println("\n工程化建议:");
        System.out.println("1. 对于N <= 1,000,000，使用DP方法更简单直接");
        System.out.println("2. 对于N > 1,000,000，必须使用数学方法避免超时");
        System.out.println("3. 在实际应用中，应根据数据规模动态选择算法");
    }
    
    public static void main(String[] args) {
        System.out.println("SPOJ BITS - 计算从1到N所有数字的二进制表示中1的总个数");
        System.out.println("使用位运算和数学优化实现");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        complexityAnalysis();
        
        // 示例使用
        System.out.println("\n=== 示例使用 ===");
        int[] examples = {10, 100, 1000};
        for (int n : examples) {
            long result = countBitsWithValidation(n);
            System.out.printf("从1到%d的所有数字中1的总个数: %d%n", n, result);
        }
    }
}