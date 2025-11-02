package class031;

/**
 * 位1的个数（多种解法）
 * 测试链接：https://leetcode.cn/problems/number-of-1-bits/
 * 
 * 题目描述：
 * 编写一个函数，输入是一个无符号整数（以二进制串的形式），
 * 返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
 * 
 * 示例：
 * 输入：n = 11 (二进制 00000000000000000000000000001011)
 * 输出：3
 * 解释：输入的二进制串 00000000000000000000000000001011 中，共有三位为 '1'。
 * 
 * 输入：n = 128 (二进制 00000000000000000000000010000000)
 * 输出：1
 * 
 * 提示：
 * 输入必须是长度为 32 的 二进制串。
 * 
 * 解题思路：
 * 1. 逐位检查法：检查每一位是否为1
 * 2. Brian Kernighan算法：n & (n-1) 消除最低位的1
 * 3. 查表法：预计算所有可能值的1的个数
 * 4. 分治法：使用位运算技巧分组统计
 * 5. 内置函数法：使用语言内置函数
 * 
 * 时间复杂度分析：
 * - 逐位检查法：O(32) = O(1)
 * - Brian Kernighan算法：O(k)，k为1的个数
 * - 查表法：O(1)
 * - 分治法：O(1)
 * - 内置函数法：O(1)
 * 
 * 空间复杂度分析：
 * - 逐位检查法：O(1)
 * - Brian Kernighan算法：O(1)
 * - 查表法：O(256) = O(1)
 * - 分治法：O(1)
 * - 内置函数法：O(1)
 */
public class Code38_Numberof1Bits {
    
    /**
     * 方法1：逐位检查法
     * 时间复杂度：O(32) = O(1)
     * 空间复杂度：O(1)
     */
    public int hammingWeight1(int n) {
        int count = 0;
        
        // 检查32位中的每一位
        for (int i = 0; i < 32; i++) {
            // 检查第i位是否为1
            if ((n & (1 << i)) != 0) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 方法2：Brian Kernighan算法（推荐）
     * 核心思想：n & (n-1) 可以消除n的二进制表示中最右边的1
     * 时间复杂度：O(k)，k为1的个数
     * 空间复杂度：O(1)
     */
    public int hammingWeight2(int n) {
        int count = 0;
        
        while (n != 0) {
            n &= n - 1;  // 消除最低位的1
            count++;
        }
        
        return count;
    }
    
    /**
     * 方法3：查表法
     * 预计算0-255所有数字的1的个数
     * 时间复杂度：O(1)
     * 空间复杂度：O(256) = O(1)
     */
    public int hammingWeight3(int n) {
        // 预计算表：0-255每个数字的1的个数
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = table[i >> 1] + (i & 1);
        }
        
        // 将32位整数分成4个8位部分
        return table[n & 0xFF] + 
               table[(n >> 8) & 0xFF] + 
               table[(n >> 16) & 0xFF] + 
               table[(n >> 24) & 0xFF];
    }
    
    /**
     * 方法4：分治法（位运算技巧）
     * 使用分治思想，先计算每2位的1的个数，然后合并
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public int hammingWeight4(int n) {
        // 第一步：每2位统计1的个数
        // 二进制：01 -> 01, 10 -> 01, 11 -> 10
        n = (n & 0x55555555) + ((n >>> 1) & 0x55555555);
        
        // 第二步：每4位统计1的个数
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        
        // 第三步：每8位统计1的个数
        n = (n & 0x0F0F0F0F) + ((n >>> 4) & 0x0F0F0F0F);
        
        // 第四步：每16位统计1的个数
        n = (n & 0x00FF00FF) + ((n >>> 8) & 0x00FF00FF);
        
        // 第五步：每32位统计1的个数
        n = (n & 0x0000FFFF) + ((n >>> 16) & 0x0000FFFF);
        
        return n;
    }
    
    /**
     * 方法5：内置函数法
     * 使用Java内置的Integer.bitCount()
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public int hammingWeight5(int n) {
        return Integer.bitCount(n);
    }
    
    /**
     * 方法6：移位统计法（无符号右移）
     * 时间复杂度：O(32) = O(1)
     * 空间复杂度：O(1)
     */
    public int hammingWeight6(int n) {
        int count = 0;
        
        // 使用无符号右移，避免符号位的影响
        while (n != 0) {
            count += n & 1;
            n >>>= 1;  // 无符号右移
        }
        
        return count;
    }
    
    /**
     * 方法7：并行计算法（另一种分治）
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public int hammingWeight7(int n) {
        // 并行计算1的个数
        n = n - ((n >>> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        n = (n + (n >>> 4)) & 0x0F0F0F0F;
        n = n + (n >>> 8);
        n = n + (n >>> 16);
        return n & 0x3F;  // 最多32个1，所以取低6位
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code38_Numberof1Bits solution = new Code38_Numberof1Bits();
        
        // 测试用例1：正常情况
        int n1 = 11;  // 二进制: 1011
        int result1 = solution.hammingWeight2(n1);
        System.out.println("测试用例1 - 输入: " + n1 + " (二进制: " + Integer.toBinaryString(n1) + ")");
        System.out.println("结果: " + result1 + " (预期: 3)");
        
        // 测试用例2：2的幂
        int n2 = 16;  // 二进制: 10000
        int result2 = solution.hammingWeight2(n2);
        System.out.println("测试用例2 - 输入: " + n2 + " (二进制: " + Integer.toBinaryString(n2) + ")");
        System.out.println("结果: " + result2 + " (预期: 1)");
        
        // 测试用例3：全1
        int n3 = -1;  // 二进制: 11111111111111111111111111111111
        int result3 = solution.hammingWeight2(n3);
        System.out.println("测试用例3 - 输入: " + n3 + " (二进制: " + Integer.toBinaryString(n3) + ")");
        System.out.println("结果: " + result3 + " (预期: 32)");
        
        // 测试用例4：0
        int n4 = 0;  // 二进制: 0
        int result4 = solution.hammingWeight2(n4);
        System.out.println("测试用例4 - 输入: " + n4 + " (二进制: " + Integer.toBinaryString(n4) + ")");
        System.out.println("结果: " + result4 + " (预期: 0)");
        
        // 性能测试
        int largeNum = 0x7FFFFFFF;  // 最大正数
        long startTime = System.currentTimeMillis();
        int result5 = solution.hammingWeight2(largeNum);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - 输入: " + largeNum);
        System.out.println("结果: " + result5);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        
        // 所有方法结果对比
        System.out.println("\n=== 所有方法结果对比 ===");
        int testNum = 123456789;
        System.out.println("测试数字: " + testNum + " (二进制: " + Integer.toBinaryString(testNum) + ")");
        System.out.println("方法1 (逐位检查): " + solution.hammingWeight1(testNum));
        System.out.println("方法2 (Brian Kernighan): " + solution.hammingWeight2(testNum));
        System.out.println("方法3 (查表法): " + solution.hammingWeight3(testNum));
        System.out.println("方法4 (分治法): " + solution.hammingWeight4(testNum));
        System.out.println("方法5 (内置函数): " + solution.hammingWeight5(testNum));
        System.out.println("方法6 (移位统计): " + solution.hammingWeight6(testNum));
        System.out.println("方法7 (并行计算): " + solution.hammingWeight7(testNum));
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 逐位检查法:");
        System.out.println("  时间复杂度: O(32) = O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - Brian Kernighan算法:");
        System.out.println("  时间复杂度: O(k)，k为1的个数");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法3 - 查表法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(256) = O(1)");
        
        System.out.println("方法4 - 分治法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法5 - 内置函数法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 算法选择：方法2 (Brian Kernighan) 最优");
        System.out.println("2. 性能优化：避免不必要的循环");
        System.out.println("3. 可读性：方法2逻辑清晰");
        System.out.println("4. 实际应用：Java内置函数性能最好");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. Brian Kernighan算法：n & (n-1) 消除最低位1");
        System.out.println("2. 分治法：使用位运算并行计算");
        System.out.println("3. 查表法：空间换时间");
        System.out.println("4. 内置函数：利用语言特性");
    }
}