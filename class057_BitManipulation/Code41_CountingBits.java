package class031;

import java.util.*;

/**
 * 比特位计数（多种解法）
 * 测试链接：https://leetcode.cn/problems/counting-bits/
 * 
 * 题目描述：
 * 给定一个非负整数 n，计算从 0 到 n 的每个整数的二进制表示中 1 的个数，并返回一个数组。
 * 
 * 示例：
 * 输入: n = 2
 * 输出: [0,1,1]
 * 
 * 输入: n = 5
 * 输出: [0,1,1,2,1,2]
 * 
 * 解题思路：
 * 1. 直接计算法：对每个数使用位运算计算1的个数
 * 2. 动态规划法：利用已知结果推导新结果
 * 3. 位运算优化：利用位运算特性快速计算
 * 4. 查表法：预计算小范围结果，大范围复用
 * 
 * 时间复杂度分析：
 * - 直接计算法：O(nk)，k为平均位数
 * - 动态规划法：O(n)
 * - 位运算优化：O(n)
 * - 查表法：O(n)
 * 
 * 空间复杂度分析：
 * - 直接计算法：O(1) 或 O(n) 存储结果
 * - 动态规划法：O(n)
 * - 位运算优化：O(n)
 * - 查表法：O(n)
 */
public class Code41_CountingBits {
    
    /**
     * 方法1：直接计算法（朴素解法）
     * 对每个数使用位运算计算1的个数
     * 时间复杂度：O(nk)，k为平均位数
     * 空间复杂度：O(n)
     */
    public int[] countBits1(int n) {
        int[] result = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            result[i] = countOnes(i);
        }
        
        return result;
    }
    
    /**
     * 计算一个数的二进制表示中1的个数
     * 时间复杂度：O(k)，k为数的位数
     * 空间复杂度：O(1)
     */
    private int countOnes(int num) {
        int count = 0;
        while (num != 0) {
            count += num & 1;  // 检查最低位是否为1
            num >>>= 1;        // 无符号右移
        }
        return count;
    }
    
    /**
     * 方法2：Brian Kernighan算法
     * 利用 num & (num-1) 快速消除最低位的1
     * 时间复杂度：O(nk)，但k通常较小
     * 空间复杂度：O(n)
     */
    public int[] countBits2(int n) {
        int[] result = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            result[i] = countOnesBrianKernighan(i);
        }
        
        return result;
    }
    
    /**
     * Brian Kernighan算法计算1的个数
     * 时间复杂度：O(k)，k为1的个数（比位数更优）
     * 空间复杂度：O(1)
     */
    private int countOnesBrianKernighan(int num) {
        int count = 0;
        while (num != 0) {
            num &= num - 1;  // 消除最低位的1
            count++;
        }
        return count;
    }
    
    /**
     * 方法3：动态规划法（最高有效位）
     * 利用已知结果推导新结果：result[i] = result[i - highBit] + 1
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int[] countBits3(int n) {
        int[] result = new int[n + 1];
        int highBit = 0;  // 当前最高有效位
        
        for (int i = 1; i <= n; i++) {
            // 检查是否是2的幂（最高有效位发生变化）
            if ((i & (i - 1)) == 0) {
                highBit = i;
            }
            result[i] = result[i - highBit] + 1;
        }
        
        return result;
    }
    
    /**
     * 方法4：动态规划法（最低有效位）
     * 利用 result[i] = result[i >> 1] + (i & 1)
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int[] countBits4(int n) {
        int[] result = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            result[i] = result[i >> 1] + (i & 1);
        }
        
        return result;
    }
    
    /**
     * 方法5：动态规划法（最低设置位）
     * 利用 result[i] = result[i & (i-1)] + 1
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int[] countBits5(int n) {
        int[] result = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            result[i] = result[i & (i - 1)] + 1;
        }
        
        return result;
    }
    
    /**
     * 方法6：查表法（适用于小范围）
     * 预计算0-255的1的个数，大数复用结果
     * 时间复杂度：O(n)
     * 空间复杂度：O(256 + n)
     */
    public int[] countBits6(int n) {
        // 预计算0-255的1的个数
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = countOnesBrianKernighan(i);
        }
        
        int[] result = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            // 将32位整数分成4个字节分别查表
            result[i] = table[i & 0xFF] + 
                        table[(i >>> 8) & 0xFF] + 
                        table[(i >>> 16) & 0xFF] + 
                        table[(i >>> 24) & 0xFF];
        }
        
        return result;
    }
    
    /**
     * 方法7：位运算并行计算
     * 使用位运算技巧并行计算1的个数
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int[] countBits7(int n) {
        int[] result = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            result[i] = parallelCountOnes(i);
        }
        
        return result;
    }
    
    /**
     * 并行计算1的个数（位运算技巧）
     * 时间复杂度：O(1) 对于固定位数的整数
     * 空间复杂度：O(1)
     */
    private int parallelCountOnes(int num) {
        // 步骤1：每2位统计1的个数
        num = (num & 0x55555555) + ((num >>> 1) & 0x55555555);
        // 步骤2：每4位统计1的个数
        num = (num & 0x33333333) + ((num >>> 2) & 0x33333333);
        // 步骤3：每8位统计1的个数
        num = (num & 0x0F0F0F0F) + ((num >>> 4) & 0x0F0F0F0F);
        // 步骤4：每16位统计1的个数
        num = (num & 0x00FF00FF) + ((num >>> 8) & 0x00FF00FF);
        // 步骤5：每32位统计1的个数
        num = (num & 0x0000FFFF) + ((num >>> 16) & 0x0000FFFF);
        
        return num;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code41_CountingBits solution = new Code41_CountingBits();
        
        // 测试用例1：n = 2
        int n1 = 2;
        int[] result1 = solution.countBits3(n1);
        System.out.println("测试用例1 - n = " + n1);
        System.out.println("结果: " + Arrays.toString(result1));
        System.out.println("预期: [0, 1, 1]");
        
        // 测试用例2：n = 5
        int n2 = 5;
        int[] result2 = solution.countBits3(n2);
        System.out.println("测试用例2 - n = " + n2);
        System.out.println("结果: " + Arrays.toString(result2));
        System.out.println("预期: [0, 1, 1, 2, 1, 2]");
        
        // 测试用例3：n = 0
        int n3 = 0;
        int[] result3 = solution.countBits3(n3);
        System.out.println("测试用例3 - n = " + n3);
        System.out.println("结果: " + Arrays.toString(result3));
        System.out.println("预期: [0]");
        
        // 性能测试
        int n4 = 1000000;
        long startTime = System.currentTimeMillis();
        int[] result4 = solution.countBits3(n4);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - n = " + n4);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        
        // 所有方法结果对比
        System.out.println("\n=== 所有方法结果对比 ===");
        int testN = 10;
        System.out.println("测试 n = " + testN);
        System.out.println("方法1 (直接计算): " + Arrays.toString(solution.countBits1(testN)));
        System.out.println("方法2 (Brian Kernighan): " + Arrays.toString(solution.countBits2(testN)));
        System.out.println("方法3 (动态规划-最高位): " + Arrays.toString(solution.countBits3(testN)));
        System.out.println("方法4 (动态规划-最低位): " + Arrays.toString(solution.countBits4(testN)));
        System.out.println("方法5 (动态规划-最低设置位): " + Arrays.toString(solution.countBits5(testN)));
        System.out.println("方法6 (查表法): " + Arrays.toString(solution.countBits6(testN)));
        System.out.println("方法7 (并行计算): " + Arrays.toString(solution.countBits7(testN)));
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 直接计算法:");
        System.out.println("  时间复杂度: O(nk)，k为平均位数");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法2 - Brian Kernighan算法:");
        System.out.println("  时间复杂度: O(nk)，k为1的个数");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法3 - 动态规划(最高位):");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法4 - 动态规划(最低位):");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法5 - 动态规划(最低设置位):");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法6 - 查表法:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(256 + n)");
        
        System.out.println("方法7 - 并行计算:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 算法选择：方法3/4/5 (动态规划) 最优");
        System.out.println("2. 性能优化：避免O(nk)的朴素解法");
        System.out.println("3. 空间优化：动态规划法空间复杂度最优");
        System.out.println("4. 实际应用：适合处理大规模数据");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 动态规划思想：利用已知结果推导新结果");
        System.out.println("2. 位运算技巧：Brian Kernighan算法消除最低位1");
        System.out.println("3. 并行计算：使用位运算并行统计1的个数");
        System.out.println("4. 查表优化：预计算小范围结果，大范围复用");
        
        // 面试要点
        System.out.println("\n=== 面试要点 ===");
        System.out.println("1. 掌握多种解法，理解各自优缺点");
        System.out.println("2. 能够分析时间空间复杂度");
        System.out.println("3. 理解动态规划的状态转移方程");
        System.out.println("4. 了解位运算的优化技巧");
    }
}