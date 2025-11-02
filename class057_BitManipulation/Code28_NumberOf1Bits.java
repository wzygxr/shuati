/**
 * 位1的个数
 * 测试链接：https://leetcode.cn/problems/number-of-1-bits/
 * 
 * 题目描述：
 * 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
 * 
 * 解题思路：
 * 1. 逐位检查：检查每一位是否为1
 * 2. 快速方法：使用 n & (n-1) 技巧快速消除最低位的1
 * 3. 查表法：使用预计算的表来快速计算
 * 4. 分治法：使用分治思想并行计算
 * 
 * 时间复杂度：O(1) - 最多32次操作
 * 空间复杂度：O(1) - 只使用常数个变量
 */
public class Code28_NumberOf1Bits {
    
    /**
     * 方法1：逐位检查
     * 时间复杂度：O(k) - k是1的个数，最坏情况O(32)
     * 空间复杂度：O(1)
     */
    public int hammingWeight1(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            // 检查最低位是否为1
            if ((n & 1) == 1) {
                count++;
            }
            // 无符号右移
            n = n >>> 1;
        }
        return count;
    }
    
    /**
     * 方法2：快速方法（最优解）
     * 使用 n & (n-1) 技巧快速消除最低位的1
     * 时间复杂度：O(k) - k是1的个数
     * 空间复杂度：O(1)
     */
    public int hammingWeight2(int n) {
        int count = 0;
        while (n != 0) {
            // 每次操作消除最低位的1
            n = n & (n - 1);
            count++;
        }
        return count;
    }
    
    /**
     * 方法3：查表法（适合多次调用）
     * 时间复杂度：O(1) - 固定4次查表操作
     * 空间复杂度：O(256) - 预计算表
     */
    private static final int[] BIT_COUNT_TABLE = new int[256];
    
    static {
        // 预计算0-255的1的个数
        for (int i = 0; i < 256; i++) {
            BIT_COUNT_TABLE[i] = Integer.bitCount(i);
        }
    }
    
    public int hammingWeight3(int n) {
        // 将32位分成4个8位字节
        return BIT_COUNT_TABLE[n & 0xff] +
               BIT_COUNT_TABLE[(n >>> 8) & 0xff] +
               BIT_COUNT_TABLE[(n >>> 16) & 0xff] +
               BIT_COUNT_TABLE[(n >>> 24) & 0xff];
    }
    
    /**
     * 方法4：分治法（并行计算）
     * 时间复杂度：O(log32) = O(1)
     * 空间复杂度：O(1)
     */
    public int hammingWeight4(int n) {
        // 分治思想：先计算每2位的1的个数，再计算每4位，依此类推
        n = (n & 0x55555555) + ((n >>> 1) & 0x55555555);  // 每2位
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);  // 每4位
        n = (n & 0x0F0F0F0F) + ((n >>> 4) & 0x0F0F0F0F);  // 每8位
        n = (n & 0x00FF00FF) + ((n >>> 8) & 0x00FF00FF);  // 每16位
        n = (n & 0x0000FFFF) + ((n >>> 16) & 0x0000FFFF); // 每32位
        return n;
    }
    
    /**
     * 方法5：Java内置方法（实际工程中最优）
     * 时间复杂度：O(1) - 使用硬件指令
     * 空间复杂度：O(1)
     */
    public int hammingWeight5(int n) {
        return Integer.bitCount(n);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code28_NumberOf1Bits solution = new Code28_NumberOf1Bits();
        
        // 测试用例1：正常情况
        int n1 = 11;  // 二进制: 1011
        int result1 = solution.hammingWeight1(n1);
        int result2 = solution.hammingWeight2(n1);
        int result3 = solution.hammingWeight3(n1);
        int result4 = solution.hammingWeight4(n1);
        int result5 = solution.hammingWeight5(n1);
        System.out.println("测试用例1 - 输入: " + n1 + " (二进制: 1011)");
        System.out.println("方法1结果: " + result1 + " (预期: 3)");
        System.out.println("方法2结果: " + result2 + " (预期: 3)");
        System.out.println("方法3结果: " + result3 + " (预期: 3)");
        System.out.println("方法4结果: " + result4 + " (预期: 3)");
        System.out.println("方法5结果: " + result5 + " (预期: 3)");
        
        // 测试用例2：边界情况（全0）
        int n2 = 0;
        int result6 = solution.hammingWeight1(n2);
        System.out.println("测试用例2 - 输入: " + n2);
        System.out.println("方法1结果: " + result6 + " (预期: 0)");
        
        // 测试用例3：边界情况（全1）
        int n3 = -1;  // 二进制全1
        int result7 = solution.hammingWeight1(n3);
        System.out.println("测试用例3 - 输入: " + n3);
        System.out.println("方法1结果: " + result7 + " (预期: 32)");
        
        // 性能对比测试
        System.out.println("\n=== 性能分析 ===");
        long startTime, endTime;
        int testValue = 0x12345678;  // 测试值
        
        startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            solution.hammingWeight1(testValue);
        }
        endTime = System.nanoTime();
        System.out.println("方法1耗时: " + (endTime - startTime) / 1000000 + " ms");
        
        startTime = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            solution.hammingWeight2(testValue);
        }
        endTime = System.nanoTime();
        System.out.println("方法2耗时: " + (endTime - startTime) / 1000000 + " ms");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 逐位检查:");
        System.out.println("  时间复杂度: O(32) - 固定32次操作");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 快速方法:");
        System.out.println("  时间复杂度: O(k) - k是1的个数");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法3 - 查表法:");
        System.out.println("  时间复杂度: O(1) - 固定4次查表操作");
        System.out.println("  空间复杂度: O(256)");
        
        System.out.println("方法4 - 分治法:");
        System.out.println("  时间复杂度: O(log32) = O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法5 - Java内置:");
        System.out.println("  时间复杂度: O(1) - 硬件指令");
        System.out.println("  空间复杂度: O(1)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 方法选择：");
        System.out.println("   - 实际工程：方法5（内置方法）最优");
        System.out.println("   - 面试场景：方法2（快速方法）最优");
        System.out.println("   - 多次调用：方法3（查表法）最优");
        System.out.println("2. 边界处理：正确处理无符号整数");
        System.out.println("3. 性能优化：根据1的个数选择最优方法");
        System.out.println("4. 可读性：添加详细注释说明算法原理");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. n & (n-1) 技巧：快速消除最低位的1");
        System.out.println("2. 分治思想：并行计算提高效率");
        System.out.println("3. 查表优化：空间换时间策略");
        System.out.println("4. 硬件指令：利用CPU内置功能");
        System.out.println("5. 无符号右移：使用>>>避免符号问题");
    }
}